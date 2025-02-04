package de.sonnes.defish;

import de.sonnes.defish.Commands.MainCommand;
import de.sonnes.defish.Commands.TabComplete;
import de.sonnes.defish.Events.FishEvents;
import de.sonnes.defish.Managers.ConfigManager;
import de.sonnes.defish.Managers.LootManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeFish extends JavaPlugin {
    private LootManager lootManager;

    @Override
    public void onEnable() {

        this.lootManager = new LootManager(getDataFolder());
        ConfigManager.initialize(this);
        getCommand("defish").setExecutor(new MainCommand(lootManager, this));
        getCommand("defish").setTabCompleter(new TabComplete());
        getServer().getPluginManager().registerEvents(new FishEvents(this), this);
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
