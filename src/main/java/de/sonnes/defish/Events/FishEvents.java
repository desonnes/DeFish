package de.sonnes.defish.Events;

import de.sonnes.defish.DeFish;
import de.sonnes.defish.Managers.LootManager;
import de.sonnes.defish.Utils.Hex;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FishEvents implements Listener {

    private final DeFish plugin;

    public FishEvents(DeFish plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onFish(PlayerFishEvent e) {

        if (e.getCaught() == null) return;

        LootManager lm = new LootManager(plugin.getDataFolder());
        Player p = e.getPlayer();
        double random = getRandomDouble();
        double chance = plugin.getConfig().getDouble("settings.chance");
        double chanceFinal = chance % 100.0;

        if(random < chanceFinal) {
            String itemname = plugin.getConfig().getString("message.onCatch");
            itemname = itemname.replace("{item_name}", lm.getRandomItem().getType().name());
            p.getInventory().addItem(lm.getRandomItem());
            p.sendMessage(Hex.color(itemname));
        }
    }

        public static double getRandomDouble() {
            double random = Math.random() * 1.01;
            return Math.floor(random * 100) / 100.0;
        }

}
