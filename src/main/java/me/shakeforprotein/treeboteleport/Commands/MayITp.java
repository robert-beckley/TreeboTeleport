package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.Bungee.BungeeSend;
import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.time.Instant;

public class MayITp {

    private TreeboTeleport pl;
    private BungeeSend bungeeSend;

    public MayITp(TreeboTeleport main) {
        this.pl = main;
        this.bungeeSend = new BungeeSend(pl);
    }


    public boolean register(String command) {
        if (!pl.getConfig().getBoolean("disabledCommands." + command)) {
            BukkitCommand item2 = new BukkitCommand(command.toLowerCase()) {
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    this.setDescription("Requests permission to teleport to another player.");
                    this.setUsage("/tpa <playername> - requires tbteleport.player.tp");
                    this.setPermission("tbteleport.player.tp");
                    if (sender.hasPermission(this.getPermission())) {

                        if (args.length == 1) {
                            boolean foundPlayer = false;
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.getName().equalsIgnoreCase(args[0])) {
                                    foundPlayer = true;
                                    if (pl.getConfig().get("tpRequest." + sender.getName()) == null || (System.currentTimeMillis() - 30000) > pl.getConfig().getLong("tpRequest." + sender.getName() + ".requestTime")) {

                                        if (pl.getConfig().get("tptoggle." + p.getName()) == null || pl.getConfig().getInt("tptoggle." + p.getName()) == 0) {
                                            sender.sendMessage(pl.badge + "Request to teleport to " + p.getName() + "'s location has been sent.");
                                            p.sendMessage(pl.badge + "Player " + ChatColor.GOLD + sender.getName() + ChatColor.RESET + " would like to teleport " + ChatColor.GOLD + "TO YOU");
                                            String command = "tellraw " + p.getName() + " [\"\",{\"text\":\"Please type \"},{\"text\":\"/tpok\",\"color\":\"green\"},{\"text\":\" or click \"},{\"text\":\"[HERE]\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpok\"}},{\"text\":\" in the next \"},{\"text\":\"30 Seconds\",\"color\":\"green\"},{\"text\":\" to Accept\"}]";
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                            pl.getConfig().set("tpRequest." + p.getName() + ".type", "toPlayer");
                                            pl.getConfig().set("tpRequest." + p.getName() + ".requestTime", System.currentTimeMillis());
                                            pl.getConfig().set("tpRequest." + p.getName() + ".requester", sender.getName());
                                        } else {
                                            sender.sendMessage(pl.err + p.getName() + " has disabled incoming teleport requests");
                                        }
                                    } else {
                                        sender.sendMessage(pl.err + "You already have a pending teleport request.");
                                    }
                                }
                            }
                            if (!foundPlayer) {
                                sender.sendMessage(pl.err + "Player " + ChatColor.GOLD + args[0] + ChatColor.RESET + " is not online on this server. Attempting to locate on other servers");
                                bungeeSend.sendPluginMessage("CrossServerTPA", "ALL", "toPlayer," + sender.getName() + "," + args[0]);

                            }
                        } else {
                            sender.sendMessage(pl.err + "Incorrect usage. This command requires a single player argument");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have access to this command. You require permission node " + ChatColor.GOLD + this.getPermission());
                    }
                    return true;
                }
            };
            pl.registerNewCommand(pl.getDescription().getName(), item2);
        }

        return true;
    }

}
