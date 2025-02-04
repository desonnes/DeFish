package de.sonnes.defish.Commands;

import de.sonnes.defish.DeFish;
import de.sonnes.defish.Utils.Hex;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import de.sonnes.defish.Managers.LootManager;

public class MainCommand implements CommandExecutor {
    private final LootManager lootManager;
    private final DeFish plugin;

    public MainCommand(LootManager lootManager, DeFish plugin) {
        this.lootManager = lootManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command only for players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("addloot")) {
            if (!player.hasPermission("defish.command.addloot")) {
                player.sendMessage(Hex.color(plugin.getConfig().getString("messages.noPermission")));
                return true;
            }

            ItemStack item = player.getInventory().getItemInMainHand();
            if (item == null || item.getType().isAir()) {
                player.sendMessage(Hex.color(plugin.getConfig().getString("messages.noItemInHand")));
                return true;
            }

            lootManager.addLootItem(item);
            player.sendMessage(Hex.color(plugin.getConfig().getString("messages.succesfulAddingItem")));
            return true;
        }
        return false;
    }
}
