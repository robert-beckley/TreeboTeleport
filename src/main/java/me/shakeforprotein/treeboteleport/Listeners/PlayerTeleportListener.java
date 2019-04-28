package me.shakeforprotein.treeboteleport.Listeners;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import sun.reflect.generics.tree.Tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PlayerTeleportListener implements Listener {

    private TreeboTeleport pl;

    public PlayerTeleportListener(TreeboTeleport main){
        this.pl = main;
    }

    @EventHandler
    public void playerTeleport(PlayerTeleportEvent e){
        Location from = e.getFrom();
        Player p = e.getPlayer();
        String pUUID = "player_" + p.getUniqueId();
        File lastLocFile = new File(pl.getDataFolder(), "lastLocation.yml");
        FileConfiguration lastLocConf = YamlConfiguration.loadConfiguration(lastLocFile);
        lastLocConf.set(pUUID + ".name", p.getName());
        lastLocConf.set(pUUID + ".uuid", p.getUniqueId().toString());
        lastLocConf.set(pUUID + ".location", from);
        try{lastLocConf.save(lastLocFile);}
        catch (IOException err){
            System.out.println("Failed to save previous teleport location for user " + p.getUniqueId() + " (" + p.getName() + ")");
            pl.makeLog(err);
        }
    }
}