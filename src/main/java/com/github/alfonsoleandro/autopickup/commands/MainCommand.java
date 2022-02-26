package com.github.alfonsoleandro.autopickup.commands;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.utils.Message;
import com.github.alfonsoleandro.autopickup.utils.Settings;
import com.github.alfonsoleandro.autopickup.utils.ChangeSettings;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import org.bukkit.Bukkit;
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
    private final ChangeSettings changeSettings;

    public MainCommand(AutoPickup plugin) {
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        this.settings = plugin.getSettings();
        this.changeSettings = new ChangeSettings(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            this.messageSender.send(sender, "&c&lREQUIRED&c: []&8, &eOPTIONAL: {}");
            this.messageSender.send(sender, "");
            this.messageSender.send(sender, "&6List of commands:");
            this.messageSender.send(sender, "&f/" + label + " help &8- See this page");
            this.messageSender.send(sender, "&f/" + label + " version &8- See plugin's version");
            this.messageSender.send(sender, "&f/" + label + " reload &8- Reload plugin's configuration");
            this.messageSender.send(sender, "&f/" + label + " toggle {setting} {player} &8- Open menu or change settings manually");
            this.messageSender.send(sender, "&f/" + label + " enable [setting] {player} &8- Enable some setting");
            this.messageSender.send(sender, "&f/" + label + " disable [setting] {player} &8- Disable some setting");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("autoPickup.reload")) {
                this.messageSender.send(sender, Message.NO_PERMISSION);
                return true;
            }
            this.plugin.reload(false);
            this.messageSender.send(sender, "&aFiles reloaded");
            return true;
        }
        if (args[0].equalsIgnoreCase("version")) {
            if (!sender.hasPermission("autoPickup.version")) {
                this.messageSender.send(sender, Message.NO_PERMISSION);
                return true;
            }
            if (!this.plugin.getVersion().equals(this.plugin.getLatestVersion())) {
                this.messageSender.send(sender, "&fVersion: &e" + this.plugin.getVersion() + "&f. &cUpdate available!");
                this.messageSender.send(sender, "&fDownload here: http://bit.ly/2Pl4Rg7");
                return true;
            }
            this.messageSender.send(sender, "&fVersion: &e" + this.plugin.getVersion() + "&f. &aUp to date!");
            return true;
        }
        if (args[0].equalsIgnoreCase("toggle")) {
            if (sender instanceof ConsoleCommandSender) {
                this.messageSender.send(sender, Message.CANNOT_CONSOLE);
                return true;
            }
            if (args.length == 1) {
                this.settings.openToggleGUI((Player) sender);
                return true;
            }

            //if setting doesn't exist
            String settingToChange = args[1];
            if (this.changeSettings.settingNotExists(settingToChange)) {
                this.messageSender.send(sender, "&cUnknown setting, check the Spigot's page for help.");
                return true;
            }

            //if user wants to change it to another player
            if (args.length == 3) {
                Player playerToUse = Bukkit.getPlayerExact(args[2]);
                if (playerToUse == null) {
                    this.messageSender.send(sender, "&cSorry, but the player you tried to change their settings doesn't seem to be online.");
                    return true;
                }
                this.changeSettings.toggleSetting(settingToChange, playerToUse, true);
                return true;
            }

            Player player = (Player) sender;
            this.changeSettings.toggleSetting(settingToChange, player, false);
            return true;
        }
        if(args[0].equalsIgnoreCase("enable")){
            if (sender instanceof ConsoleCommandSender) {
                this.messageSender.send(sender, Message.CANNOT_CONSOLE);
                return true;
            }
            if(args.length == 1){
                this.messageSender.send(sender, "&cYou need to specify a setting to turn on");
                return true;
            }

            //if setting doesn't exist
            String settingToChange = args[1];
            if (this.changeSettings.settingNotExists(settingToChange)) {
                this.messageSender.send(sender, "&cUnknown setting, check the Spigot's page for help.");
                return true;
            }

            if(args.length == 3){
                Player playerToUse = Bukkit.getPlayerExact(args[2]);
                if (playerToUse == null) {
                    this.messageSender.send(sender, "&cSorry, but the player you tried to change their settings doesn't seem to be online.");
                    return true;
                }
                this.changeSettings.enableSetting(settingToChange, playerToUse, true);
                return true;
            }

            Player player = (Player) sender;
            this.changeSettings.enableSetting(settingToChange, player, false);
            return true;
        }

        if(args[0].equalsIgnoreCase("disable")){
            if (sender instanceof ConsoleCommandSender) {
                this.messageSender.send(sender, Message.CANNOT_CONSOLE);
                return true;
            }
            if(args.length == 1){
                this.messageSender.send(sender, "&cYou need to specify a setting to turn off");
                return true;
            }

            //if setting doesn't exist
            String settingToChange = args[1];
            if (this.changeSettings.settingNotExists(settingToChange)) {
                this.messageSender.send(sender, "&cUnknown setting, check the Spigot's page for help.");
                return true;
            }

            if(args.length == 3){
                Player playerToUse = Bukkit.getPlayerExact(args[2]);
                if (playerToUse == null) {
                    this.messageSender.send(sender, "&cSorry, but the player you tried to change their settings doesn't seem to be online.");
                    return true;
                }
                this.changeSettings.disableSetting(settingToChange, playerToUse, true);
                return true;
            }
            Player player = (Player) sender;
            this.changeSettings.disableSetting(settingToChange, player, false);
            return true;
        }

        //unknown command
        this.messageSender.send(sender, "&cUnknown command, try &e/" + label + " help");
        return true;
    }
}
