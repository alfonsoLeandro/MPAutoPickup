package com.github.alfonsoleandro.autopickup.events;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupSettings;
import com.github.alfonsoleandro.autopickup.utils.Message;
import com.github.alfonsoleandro.autopickup.utils.Settings;
import com.github.alfonsoleandro.mputils.guis.SimpleGUI;
import com.github.alfonsoleandro.mputils.guis.events.GUIClickEvent;
import com.github.alfonsoleandro.mputils.guis.utils.GUIType;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import com.github.alfonsoleandro.mputils.string.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class GUIClickListener implements Listener {

    private final AutoPickup plugin;
    private final MessageSender<Message> messageSender;
    private final Settings settings;


    public GUIClickListener(AutoPickup plugin){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        this.settings = plugin.getSettings();
    }


    @EventHandler
    public void onGuiClick(GUIClickEvent event){
        if(event.getGuiType().equals(GUIType.PAGINATED)) return;
        if(!event.getGui().getGuiTags().equalsIgnoreCase("MPAutoPickup")) return;
        event.setCancelled(true);
        int clickedSlot = event.getRawSlot();
        if(clickedSlot > 4 || clickedSlot < 0) return;
        Player player = (Player) event.getWhoClicked();
        AutoPickupSettings settings = this.plugin.getAutoPickupManager().getPlayer(player.getName());


        if(clickedSlot == 0){
            if(player.hasPermission("autoPickup.autoPickup.block")) {
                boolean wasEnabled = settings.autoPickupBlocksEnabled();
                settings.setAutoPickupBlocks(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AP_BLOCKS_DISABLED : Message.AP_BLOCKS_ENABLED);
                openGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }else if(clickedSlot == 1){
            if(player.hasPermission("autoPickup.autoPickup.mob")) {
                boolean wasEnabled = settings.autoPickupMobDropsEnabled();
                settings.setAutoPickupMobDrops(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AP_MOB_DISABLED : Message.AP_MOB_ENABLED);
                openGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }else if(clickedSlot == 2){
            if(player.hasPermission("autoPickup.autoPickup.exp")) {
                boolean wasEnabled = settings.autoPickupExpEnabled();
                settings.setAutoPickupExp(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AP_EXP_DISABLED : Message.AP_EXP_ENABLED);
                openGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }else if(clickedSlot == 3){
            if(player.hasPermission("autoPickup.autoSmelt.blocks")) {
                boolean wasEnabled = settings.autoSmeltBlocksEnabled();
                settings.setAutoSmeltBlocks(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AS_BLOCKS_DISABLED : Message.AS_BLOCKS_ENABLED);
                openGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }else {
            if(player.hasPermission("autoPickup.autoSmelt.mob")) {
                boolean wasEnabled = settings.autoSmeltMobEnabled();
                settings.setAutoSmeltMobs(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AS_MOB_DISABLED : Message.AS_MOB_ENABLED);
                openGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }
    }


    private void openGUI(Player player){
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

        if(player.hasPermission("autoPickup.autoPickup.block")){
            if(settings.autoPickupBlocksEnabled()){
                block = this.settings.getaPBlockEnabled();
            }else{
                block = this.settings.getaPBlockDisabled();
            }
        }else{
            block = this.settings.getaPBlockNoPermission();
        }

        if(player.hasPermission("autoPickup.autoPickup.mob")){
            if(settings.autoPickupMobDropsEnabled()){
                mob = this.settings.getaPMobEnabled();
            }else{
                mob = this.settings.getaPMobDisabled();
            }
        }else{
            mob = this.settings.getaPMobNoPermission();
        }

        if(player.hasPermission("autoPickup.autoPickup.exp")){
            if(settings.autoPickupExpEnabled()){
                exp = this.settings.getaPExpEnabled();
            }else{
                exp = this.settings.getaPExpDisabled();
            }
        }else{
            exp = this.settings.getaPExpNoPermission();
        }

        if(player.hasPermission("autoPickup.autoSmelt.blocks")){
            if(settings.autoSmeltBlocksEnabled()){
                smeltBlock = this.settings.getaSBlockEnabled();
            }else{
                smeltBlock = this.settings.getaSBlockDisabled();
            }
        }else{
            smeltBlock = this.settings.getaSBlockNoPermission();
        }

        if(player.hasPermission("autoPickup.autoSmelt.mobs")){
            if(settings.autoSmeltMobEnabled()){
                smeltMob = this.settings.getaPMobEnabled();
            }else{
                smeltMob = this.settings.getaPMobDisabled();
            }
        }else{
            smeltMob = this.settings.getaPMobNoPermission();
        }



        gui.setItem(0, block);
        gui.setItem(1, mob);
        gui.setItem(2, exp);
        gui.setItem(3, smeltBlock);
        gui.setItem(4, smeltMob);

        gui.openGUI(player);


    }


}
