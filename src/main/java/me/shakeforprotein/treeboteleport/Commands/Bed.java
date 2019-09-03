package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bed implements CommandExecutor {

    private TreeboTeleport pl;

    public Bed(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!pl.getConfig().getBoolean("disabledCommands.bed")) {
        Player p = (Player) sender;
            if (p.getBedSpawnLocation() != null) {
                p.sendMessage(pl.badge + "Sending you to your bed");
                p.teleport(p.getBedSpawnLocation());
            } else {
                p.sendMessage(pl.err + "Bed Missing");
            }}
        else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }
        return true;
    }
}
