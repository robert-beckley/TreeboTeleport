package me.shakeforprotein.treeboteleport.Methods.Guis;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class OpenHomesMenu {

    private TreeboTeleport pl;

    public OpenHomesMenu(TreeboTeleport main){
        this.pl = main;
    }

    public boolean openHomesMenu(Player p){
        String uuid = p.getUniqueId().toString();
        File menuYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + uuid + ".yml");
        FileConfiguration homesMenu = YamlConfiguration.loadConfiguration(menuYml);
        if(!menuYml.exists()) {
            try {
                menuYml.createNewFile();
                try{
                    homesMenu.options().copyDefaults();
                    homesMenu.save(menuYml);
                }
                catch (FileNotFoundException e){pl.makeLog(e);}
            } catch (IOException e) {
                pl.makeLog(e);
            }
        }

        try{

            String menuName = "Homes - " + p.getName();
            int invSize = 2 * 9;
            Inventory HomesMenu = Bukkit.createInventory(null, invSize, menuName);

            for(String item : homesMenu.getConfigurationSection("homes").getKeys(false)){
                if (homesMenu.get("homes." + item + ".icon") == null) {
                    homesMenu.set("homes." + item + ".icon", "RED_BED");
                }
                if (homesMenu.get("homes." + item + ".colour") == null){
                    homesMenu.set("homes." + item + ".colour", "WHITE" );
                }
                if (homesMenu.get("homes." + item + ".name") == null){
                    homesMenu.set("homes." + item + ".name", item);
                }

                Material icon = Material.getMaterial(homesMenu.getString("homes." + item + ".icon"));
                String colour = homesMenu.getString("homes." + item + ".colour");
                String displayName = ChatColor.valueOf(colour)+ homesMenu.getString("homes." + item + ".name");
                int x = homesMenu.getInt("homes." + item + ".x");
                int y = homesMenu.getInt("homes." + item + ".y");
                int z = homesMenu.getInt("homes." + item + ".z");
                String world = homesMenu.getString("homes." + item + ".world");
                ItemStack newItem = new ItemStack(icon, 1);
                ItemMeta itemMeta = newItem.getItemMeta();
                itemMeta.setDisplayName(displayName);
                List<String> itemLore = new ArrayList<String>();

                itemLore.add("X:" + Math.floor(x) + "  Y: " + y + "  Z: " +z);
                itemLore.add("World: " + world);
                itemMeta.setLore(itemLore);
                newItem.setItemMeta(itemMeta);

                HomesMenu.addItem(newItem);
            }

            if (homesMenu.getConfigurationSection("bed") != null) {

                int bedX = homesMenu.getInt("bed.x");
                int bedY = homesMenu.getInt("bed.y");
                int bedZ = homesMenu.getInt("bed.z");
                String bedWorld = homesMenu.getString("bed.world");

                ItemStack bedItem = new ItemStack(Material.RED_BED, 1);
                ItemMeta bedMeta = bedItem.getItemMeta();
                bedMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Your Bed");
                List<String> bedLore = new ArrayList<String>();
                bedLore.add("X: " + bedX + "  Y: " + bedY + "  Z: " + bedZ);
                bedLore.add("World: " + bedWorld);
                bedMeta.setLore(bedLore);
                bedItem.setItemMeta(bedMeta);
                HomesMenu.addItem(bedItem);
            }
            p.openInventory(HomesMenu);
        }
        catch(NullPointerException e){
            pl.makeLog(e);
        }
        return true;
    }
}
