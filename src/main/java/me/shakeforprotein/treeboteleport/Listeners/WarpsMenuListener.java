package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.io.File;


public class WarpsMenuListener implements Listener {

    private TreeboTeleport pl;

    public WarpsMenuListener(TreeboTeleport main) {
        this.pl = main;
    }

    @EventHandler
    public void invClickEvent(InventoryClickEvent e) {
        Inventory inv = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();
        String name = inv.getName();
        int slot = e.getSlot();
        File warpsYml = new File(pl.getDataFolder(), "warps.yml");
        FileConfiguration warpsMenu = YamlConfiguration.loadConfiguration(warpsYml);

        if (name.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', warpsMenu.getString("menuName")))) {
            e.setCancelled(true);
            if (inv.getItem(slot) != null) {
                Bukkit.dispatchCommand(p, "warpto " + inv.getItem(slot).getItemMeta().getLore().get(0).split(": ")[1]);

            }
        }
    }
}
