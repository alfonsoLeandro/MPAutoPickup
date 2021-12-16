package com.github.alfonsoleandro.autopickup.commands;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupSettings;
import com.github.alfonsoleandro.autopickup.utils.Message;
import com.github.alfonsoleandro.mputils.guis.SimpleGUI;
import com.github.alfonsoleandro.mputils.itemstacks.MPItemStacks;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import com.github.alfonsoleandro.mputils.string.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {

    private final AutoPickup plugin;
    private final MessageSender<Message> messageSender;

    public MainCommand(AutoPickup plugin){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
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
            openToggleGUI((Player) sender);




            //unknown command
        }else {
            this.messageSender.send(sender, "&cUnknown command, try &e/"+label+" help");
        }
        return true;
    }


    private void openToggleGUI(Player player){
        FileConfiguration config = this.plugin.getConfig();
        SimpleGUI gui = new SimpleGUI(
                StringUtils.colorizeString('&', config.getString("config.GUI.title")),
                9,
                "MPAutoPickup"
        );

        AutoPickupSettings settings = this.plugin.getAutoPickupManager().getPlayer(player);

        ItemStack block;
        ItemStack mob;
        ItemStack exp;
        ItemStack smeltBlock;
        ItemStack smeltMob;

        //TODO: load gui items in settings
        if(player.hasPermission("autoPickup.autoPickup.block")){
            if(settings.autoPickupBlocksEnabled()){
                block = getConfigGUIItem("auto pickup block drops.enabled");
            }else{
                block = getConfigGUIItem("auto pickup block drops.disabled");
            }
        }else{
            block = getConfigGUIItem("auto pickup block drops.no permission");
        }

        if(player.hasPermission("autoPickup.autoPickup.mob")){
            if(settings.autoPickupMobDropsEnabled()){
                mob = getConfigGUIItem("auto pickup mob drops.enabled");
            }else{
                mob = getConfigGUIItem("auto pickup mob drops.disabled");
            }
        }else{
            mob = getConfigGUIItem("auto pickup mob drops.no permission");
        }

        if(player.hasPermission("autoPickup.autoPickup.exp")){
            if(settings.autoPickupExpEnabled()){
                exp = getConfigGUIItem("auto pickup exp.enabled");
            }else{
                exp = getConfigGUIItem("auto pickup exp.disabled");
            }
        }else{
            exp = getConfigGUIItem("auto pickup exp.no permission");
        }

        if(player.hasPermission("autoPickup.autoSmelt.blocks")){
            if(settings.autoSmeltBlocksEnabled()){
                smeltBlock = getConfigGUIItem("auto smelt blocks.enabled");
            }else{
                smeltBlock = getConfigGUIItem("auto smelt blocks.disabled");
            }
        }else{
            smeltBlock = getConfigGUIItem("auto smelt blocks.no permission");
        }

        if(player.hasPermission("autoPickup.autoSmelt.mobs")){
            if(settings.autoSmeltMobEnabled()){
                smeltMob = getConfigGUIItem("auto smelt mobs.enabled");
            }else{
                smeltMob = getConfigGUIItem("auto smelt mobs.disabled");
            }
        }else{
            smeltMob = getConfigGUIItem("auto smelt mobs.no permission");
        }



        gui.setItem(0, block);
        gui.setItem(1, mob);
        gui.setItem(2, exp);
        gui.setItem(3, smeltBlock);
        gui.setItem(4, smeltMob);

        gui.openGUI(player);

    }


    private ItemStack getConfigGUIItem(String path){
        FileConfiguration config = this.plugin.getConfigYaml().getAccess();
        return MPItemStacks.newItemStack(
                Material.valueOf(config.getString("config.GUI."+path+".item")),
                1,
                config.getString("config.GUI."+path+".name"),
                config.getStringList("config.GUI."+path+".lore")
        );
    }



}
