package de.sonnes.defish.Managers;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ConfigManager {
    private static JavaPlugin plugin;
    private static FileConfiguration config;

    public static void initialize(JavaPlugin mainPlugin) {
        plugin = mainPlugin;
        loadConfigs();
    }

    public static void loadConfigs() {
        try {
            File configFile = new File(plugin.getDataFolder(), "loot.yml");
            if (!configFile.exists()) {
                plugin.saveResource("loot.yml", false);
            }
            config = YamlConfiguration.loadConfiguration(configFile);

            if (!config.contains("items")) {
                plugin.getLogger().severe("Config doesn't have 'items' section!");
            }

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error has been occured when loading config: ", e);
        }
    }

    public static ItemStack getItemStack(String path) {
        ConfigurationSection section = config.getConfigurationSection(path);
        return getItemStackFromConfig(section);
    }

    private static ItemStack getItemStackFromConfig(ConfigurationSection section) {
        if (section == null) {
            plugin.getLogger().warning("Empty section of items in config");
            return null;
        }
        if (!section.contains("type")) {
            plugin.getLogger().severe("Missing item type in section: " + section.getName());
            return null;
        }

        Material material = Material.getMaterial(section.getString("type"));
        if (material == null) {
            plugin.getLogger().severe("Incorrect material type: " + section.getString("type"));
            return null;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (section.contains("name")) {
            meta.setDisplayName(section.getString("name"));
        }

        if (section.contains("lore")) {
            List<String> lore = new ArrayList<>();
            for (String line : section.getStringList("lore")) {
                lore.add(line);
            }
            meta.setLore(lore);
        }

        if (section.contains("enchantments")) {
            ConfigurationSection enchants = section.getConfigurationSection("enchantments");
            for (String enchantName : enchants.getKeys(false)) {
                Enchantment enchant = Enchantment.getByName(enchantName);
                if (enchant != null) {
                    meta.addEnchant(enchant, enchants.getInt(enchantName), true);
                }
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    public static void saveConfig() {
        try {
            config.save(new File(plugin.getDataFolder(), "loot.yml"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error has been occured when saving config: ", e);
        }
    }

    public static void reloadConfig() {
        loadConfigs();
    }
}
