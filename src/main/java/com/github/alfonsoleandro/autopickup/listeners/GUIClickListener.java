package com.github.alfonsoleandro.autopickup.listeners;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupSettings;
import com.github.alfonsoleandro.autopickup.utils.Message;
import com.github.alfonsoleandro.autopickup.utils.Settings;
import com.github.alfonsoleandro.mputils.guis.events.GUIClickEvent;
import com.github.alfonsoleandro.mputils.guis.utils.GUIType;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
        if(clickedSlot > 8 || clickedSlot < 0) return;
        Player player = (Player) event.getWhoClicked();
        AutoPickupSettings settings = this.plugin.getAutoPickupManager().getPlayer(player.getName());


        if(clickedSlot == 0){
            if(player.hasPermission("autoPickup.autoPickup.block")) {
                boolean wasEnabled = settings.autoPickupBlocksEnabled();
                settings.setAutoPickupBlocks(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AP_BLOCKS_DISABLED : Message.AP_BLOCKS_ENABLED);
                this.settings.openToggleGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }else if(clickedSlot == 1){
            if(player.hasPermission("autoPickup.autoPickup.mob")) {
                boolean wasEnabled = settings.autoPickupMobDropsEnabled();
                settings.setAutoPickupMobDrops(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AP_MOB_DISABLED : Message.AP_MOB_ENABLED);
                this.settings.openToggleGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }else if(clickedSlot == 2){
            if(player.hasPermission("autoPickup.autoPickup.exp")) {
                boolean wasEnabled = settings.autoPickupExpEnabled();
                settings.setAutoPickupExp(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AP_EXP_DISABLED : Message.AP_EXP_ENABLED);
                this.settings.openToggleGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }else if(clickedSlot == 3){
            if(player.hasPermission("autoPickup.autoSmelt.blocks")) {
                boolean wasEnabled = settings.autoSmeltBlocksEnabled();
                settings.setAutoSmeltBlocks(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AS_BLOCKS_DISABLED : Message.AS_BLOCKS_ENABLED);
                this.settings.openToggleGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }else if(clickedSlot == 4){
            if(player.hasPermission("autoPickup.autoSmelt.mob")) {
                boolean wasEnabled = settings.autoSmeltMobEnabled();
                settings.setAutoSmeltMobs(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AS_MOB_DISABLED : Message.AS_MOB_ENABLED);
                this.settings.openToggleGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }else if(clickedSlot == 7){
            if(this.settings.isGlobalCarefulBreakDisabled()){
                this.messageSender.send(player, Message.CAREFUL_BREAK_DISABLED_IN_CONFIG);
                return;
            }
            if(player.hasPermission("autoPickup.carefulBreak")) {
                boolean wasEnabled = settings.carefulBreakEnabled();
                settings.setCarefulBreak(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.CAREFUL_BREAK_DISABLED : Message.CAREFUL_BREAK_ENABLED);
                this.settings.openToggleGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }

        }else if(clickedSlot == 8){
            if(this.settings.isGlobalCarefulSmeltDisabled()){
                this.messageSender.send(player, Message.CAREFUL_SMELT_DISABLED_IN_CONFIG);
                return;
            }
            if(player.hasPermission("autoPickup.carefulSmelt")) {
                boolean wasEnabled = settings.carefulSmeltEnabled();
                settings.setCarefulSmelt(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.CAREFUL_SMELT_DISABLED : Message.CAREFUL_SMELT_ENABLED);
                this.settings.openToggleGUI(player);
            }else{
                this.messageSender.send(player, Message.NO_PERMISSION);
            }
        }
    }



}
