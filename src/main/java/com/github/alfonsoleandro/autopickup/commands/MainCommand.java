package com.github.alfonsoleandro.autopickup.commands;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.utils.Message;
import com.github.alfonsoleandro.autopickup.utils.Settings;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private final AutoPickup plugin;
    private final MessageSender<Message> messageSender;
    private final Settings settings;

    public MainCommand(AutoPickup plugin){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        this.settings = plugin.getSettings();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
            this.messageSender.send(sender, "&6List of commands");
            this.messageSender.send(sender, "&f/"+label+" help");
            this.messageSender.send(sender, "&f/"+label+" version");
            this.messageSender.send(sender, "&f/"+label+" reload");
            this.messageSender.send(sender, "&f/"+label+" toggle");


        }else if(args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("autoPickup.reload")) {
                this.messageSender.send(sender, Message.NO_PERMISSION);
                return true;
            }
            this.plugin.reload(false);
            this.messageSender.send(sender, "&aFiles reloaded");


        }else if(args[0].equalsIgnoreCase("version")) {
            if(!sender.hasPermission("autoPickup.version")) {
                this.messageSender.send(sender, Message.NO_PERMISSION);
                return true;
            }
            if(!this.plugin.getVersion().equals(this.plugin.getLatestVersion())) {
                this.messageSender.send(sender, "&fVersion: &e" + this.plugin.getVersion() + "&f. &cUpdate available!");
                this.messageSender.send(sender, "&fDownload here: http://bit.ly/2Pl4Rg7");
                return true;
            }
            this.messageSender.send(sender, "&fVersion: &e" + this.plugin.getVersion() + "&f. &aUp to date!");


        }else if(args[0].equalsIgnoreCase("toggle")){
            if(sender instanceof ConsoleCommandSender){
                this.messageSender.send(sender, Message.CANNOT_CONSOLE);
                return true;
            }
            this.settings.openToggleGUI((Player) sender);




            //unknown command
        }else {
            this.messageSender.send(sender, "&cUnknown command, try &e/"+label+" help");
        }
        return true;
    }





}
