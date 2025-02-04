package de.sonnes.defish.Managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootManager {
    private final File lootFile;
    private YamlConfiguration config;
    private final Random random = new Random();

    public LootManager(File dataFolder) {
        this.lootFile = new File(dataFolder, "loot.yml");
        reloadConfig();
    }

    public void reloadConfig() {
        if (!lootFile.exists()) {
            try {
                lootFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(lootFile);
    }

    public void addLootItem(ItemStack item) {
        ConfigurationSection itemsSection = config.createSection("items." + System.currentTimeMillis());
        itemsSection.set("item", item.serialize());
        itemsSection.set("chance", 100.0);
        saveConfig();
    }

    public ItemStack getRandomItem() {
        List<ItemStack> items = new ArrayList<>();
        ConfigurationSection itemsSection = config.getConfigurationSection("items");

        if (itemsSection == null) return null;

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
            if (itemSection != null) {
                items.add(ItemStack.deserialize(itemSection.getConfigurationSection("item").getValues(true)));
            }
        }

        if (items.isEmpty()) return null;
        return items.get(random.nextInt(items.size()));
    }

    private void saveConfig() {
        try {
            config.save(lootFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}