package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Home implements CommandExecutor {

    private TreeboTeleport pl;

    public Home(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        File homesYml = new File(pl.getDataFolder() + File.separator + "homes", File.separator + p.getUniqueId() + ".yml");
        FileConfiguration homes = YamlConfiguration.loadConfiguration(homesYml);

        if(!homesYml.exists()){
            sender.sendMessage(pl.err + "You do not appear to have any homes. Use '/sethome <home name>' to create a home at your current location.");
        }

        if (args.length == 0) {
            if (homes.get("defaultHome") != null) {
                Bukkit.dispatchCommand(sender, "home default");
            }
            else{
                Bukkit.dispatchCommand(sender, "homes");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("default")) {
                if (homes.get("defaultHome") != null) {
                    boolean found = false;
                    for (String home : homes.getConfigurationSection("homes").getKeys(false)) {
                        if (homes.getString("homes." + home + ".name").equalsIgnoreCase("defaultHome")) {
                            Bukkit.dispatchCommand(sender, "home " + homes.getString("homes." + home + ".name"));
                            found = true;
                        }
                    }
                    if(!found){
                        sender.sendMessage(pl.badge + "Couldn't find your default home. Have you deleted it?");
                    }
                }
                else{
                    sender.sendMessage(pl.badge + "Default home not set");
                }
            }

            else if (args[0].equalsIgnoreCase("bed")){
                p.teleport(p.getBedSpawnLocation());
            }
            else{
                boolean found = false;
                for (String home : homes.getConfigurationSection("homes").getKeys(false)) {
                    if (homes.getString("homes." + home + ".name").equalsIgnoreCase(args[0])) {
                        int x = homes.getInt("homes." + home + ".x");
                        int y = homes.getInt("homes." + home + ".y");
                        int z = homes.getInt("homes." + home + ".z");
                        int pitch = homes.getInt("homes." + home + ".pitch");
                        int yaw = homes.getInt("homes." + home + ".yaw");
                        String world = homes.getString("homes." + home + ".world");
                        Location tpLoc = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                        p.sendMessage(pl.badge + "Returning you to " + home);
                        p.teleport(tpLoc);

                        found = true;
                    }
                }
                if(!found){
                    sender.sendMessage(pl.err + "No home found with name " + args[0]);
                }
            }
        }
        else{
            sender.sendMessage("Too many Arguments");
            Bukkit.dispatchCommand(sender, "homes");
        }
        return true;
    }
}
