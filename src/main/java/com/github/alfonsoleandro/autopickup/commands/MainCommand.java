package com.github.alfonsoleandro.autopickup.commands;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupSettings;
import com.github.alfonsoleandro.mputils.guis.SimpleGUI;
import com.github.alfonsoleandro.mputils.itemstacks.MPItemStacks;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
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

public class MainCommand extends Reloadable implements CommandExecutor {

    private final AutoPickup plugin;
    //Translatable messages
    private String prefix;
    private String noPerm;
    private String cannotConsole;

    public MainCommand(AutoPickup plugin){
        super(plugin);
        this.plugin = plugin;
        this.loadMessages();
    }


    private void send(CommandSender sender, String msg){
        sender.sendMessage(StringUtils.colorizeString('&',prefix+" "+msg));
    }

    private void loadMessages(){
        FileConfiguration config = plugin.getConfig();

        this.prefix = config.getString("config.prefix");
        this.noPerm = config.getString("config.messages.no permission");
        this.cannotConsole = config.getString("config.messages.cannot send from console");
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
            send(sender, "&6List of commands");
            send(sender, "&f/"+label+" help");
            send(sender, "&f/"+label+" version");
            send(sender, "&f/"+label+" reload");
            send(sender, "&f/"+label+" toggle");


        }else if(args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("autoPickup.reload")) {
                send(sender, noPerm);
                return true;
            }
            plugin.reload(false);
            this.loadMessages();
            send(sender, "&aFiles reloaded");


        }else if(args[0].equalsIgnoreCase("version")) {
            if(!sender.hasPermission("autoPickup.version")) {
                send(sender, noPerm);
                return true;
            }
            if(!plugin.getVersion().equals(plugin.getLatestVersion())) {
                send(sender, "&fVersion: &e" + plugin.getVersion() + "&f. &cUpdate available!");
                send(sender, "&fDownload here: http://bit.ly/2Pl4Rg7");
                return true;
            }
            send(sender, "&fVersion: &e" + plugin.getVersion() + "&f. &aUp to date!");


        }else if(args[0].equalsIgnoreCase("toggle")){
            if(sender instanceof ConsoleCommandSender){
                send(sender, cannotConsole);
                return true;
            }
            openToggleGUI((Player) sender);




            //unknown command
        }else {
            send(sender, "&cUnknown command, try &e/"+label+" help");
        }
        return true;
    }


    private void openToggleGUI(Player player){
        FileConfiguration config = plugin.getConfig();
        SimpleGUI gui = new SimpleGUI(
                StringUtils.colorizeString('&', config.getString("config.GUI.title")),
                9,
                "MPAutoPickup"
        );

        AutoPickupSettings settings = plugin.getAutoPickupManager().getPlayer(player);

        ItemStack block;
        ItemStack mob;
        ItemStack exp;
        ItemStack smeltBlock;
        ItemStack smeltMob;

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
        FileConfiguration config = plugin.getConfigYaml().getAccess();
        return MPItemStacks.newItemStack(
                Material.valueOf(config.getString("config.GUI."+path+".item")),
                1,
                config.getString("config.GUI."+path+".name"),
                config.getStringList("config.GUI."+path+".lore")
        );
    }

    @Override
    public void reload(boolean deep){
        this.loadMessages();
    }



}
