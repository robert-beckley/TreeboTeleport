package me.shakeforprotein.treeboteleport.Commands;

import me.shakeforprotein.treeboteleport.TreeboTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Iterator;

public class Tp2MePls implements CommandExecutor {

    private TreeboTeleport pl;

    public Tp2MePls(TreeboTeleport main) {
        this.pl = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!pl.getConfig().getBoolean("disabledCommands.tpahere")) {
            if (args.length == 1) {
                boolean foundPlayer = false;

                Iterator iter = Bukkit.getOnlinePlayers().iterator();
                while (iter.hasNext()) {
                    Player p = (Player) iter.next();
                    if (p.getName().equalsIgnoreCase(args[0])) {
                        foundPlayer = true;
                        String command = "";
                        if (pl.getConfig().get("tpRequest." + sender.getName()) == null || (System.currentTimeMillis() - 30000) > pl.getConfig().getLong("tpRequest." + sender.getName() + ".requestTime")) {

                            if (pl.getConfig().get("tptoggle." + p.getName()) == null || pl.getConfig().getInt("tptoggle." + p.getName()) == 0) {
                                sender.sendMessage(pl.badge + "Request to teleport " + p.getName() + " to your location sent.");
                                if (!sender.getName().equalsIgnoreCase("Joeynator")) {
                                    p.sendMessage("Player " + ChatColor.GOLD + sender.getName() + ChatColor.RESET + " would like you to teleport " + ChatColor.GOLD + "TO THEM");
                                    //p.sendMessage("Please type " + ChatColor.GREEN + "/tpok " + ChatColor.RESET + "in the next " + ChatColor.GREEN + " 30 Seconds" + ChatColor.RESET + " to Accept" );
                                    command = "tellraw " + p.getName() + " [\"\",{\"text\":\"Please type \"},{\"text\":\"/tpok\",\"color\":\"green\"},{\"text\":\" or click \"},{\"text\":\"[HERE]\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpok\"}},{\"text\":\" in the next \"},{\"text\":\"30 Seconds\",\"color\":\"green\"},{\"text\":\" to Accept\"}]";
                                } else {
                                    p.sendMessage("Player " + ChatColor.GOLD + sender.getName() + ChatColor.RED + " would like you to teleport " + ChatColor.GOLD + "TO THEM so they can drop you into the void or fuck you over some other way");
                                    //p.sendMessage("Please type " + ChatColor.GREEN + "/tpok " + ChatColor.RESET + "in the next " + ChatColor.GREEN + " 30 Seconds" + ChatColor.RESET + " to Accept" );
                                    command = "tellraw " + p.getName() + " [\"\",{\"text\":\"Please DON'T type \"},{\"text\":\"/tpok\",\"color\":\"green\"},{\"text\":\" or click \"},{\"text\":\"[HERE]\",\"color\":\"gold\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tpok\"}},{\"text\":\" in the next \"},{\"text\":\"30 Seconds\",\"color\":\"green\"},{\"text\":\" unless you Accept that you do this at your own risk\"}]";
                                }
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                pl.getConfig().set("tpRequest." + p.getName() + ".type", "toSender");
                                pl.getConfig().set("tpRequest." + p.getName() + ".requestTime", System.currentTimeMillis());
                                pl.getConfig().set("tpRequest." + p.getName() + ".requester", sender.getName());
                            } else {
                                sender.sendMessage(pl.err + p.getName() + " has disabled incomming teleport requests");
                            }
                        } else {
                            sender.sendMessage(pl.err + "You already have a pending teleport request.");
                        }
                    }
                }
                if (!foundPlayer) {
                    sender.sendMessage(pl.err + "Player " + ChatColor.GOLD + args[0] + ChatColor.RESET + " is not online on this server. Please check spelling");
                }
            } else {
                sender.sendMessage(pl.err + "This command requires a single player argument");
            }
        }else {
            sender.sendMessage(pl.err + "The command /" + cmd + " has been disabled on this server");
        }
        return true;
    }
}
