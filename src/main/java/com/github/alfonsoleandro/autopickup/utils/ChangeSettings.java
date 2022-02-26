package com.github.alfonsoleandro.autopickup.utils;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupSettings;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import org.bukkit.entity.Player;
import java.util.Arrays;
import java.util.List;

public class ChangeSettings {
    private final AutoPickup plugin;
    private final MessageSender<Message> messageSender;

    public ChangeSettings(AutoPickup plugin){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
    }

    public boolean settingExists(String setting) {
        String[] Settings = {"block-auto-pickup", "mob-auto-pickup", "exp-auto-pickup", "block-auto-smelt", "mob-auto-smelt", "careful-break", "careful-smelt"};
        List<String> settingsList = Arrays.asList(Settings);
        return settingsList.contains(setting);
    }

    public void enableSetting(String setting, Player player, boolean forOther){
        AutoPickupSettings playerSettings = this.plugin.getAutoPickupManager().getPlayer(player.getName());
        switch (setting) {
            case "block-auto-pickup" -> {
                if(forOther && !player.hasPermission("autopickup.autopickup.blocks.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.autopickup.blocks")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setAutoPickupBlocks(true);
                messageSender.send(player, Message.AP_BLOCKS_ENABLED);
            }
            case "mob-auto-pickup" -> {
                if(forOther && !player.hasPermission("autopickup.autopickup.mobs.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.autopickup.mobs")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setAutoPickupMobDrops(true);
                messageSender.send(player, Message.AP_MOB_ENABLED);
            }
            case "exp-auto-pickup" -> {
                if(forOther && !player.hasPermission("autopickup.autopickup.exp.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.autopickup.exp")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setAutoPickupExp(true);
                messageSender.send(player, Message.AP_EXP_ENABLED);
            }
            case "block-auto-smelt" -> {
                if(forOther && !player.hasPermission("autopickup.autosmelt.blocks.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.autosmelt.blocks")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setAutoSmeltBlocks(true);
                messageSender.send(player, Message.AS_BLOCKS_ENABLED);
            }
            case "mob-auto-smelt" -> {
                if(forOther && !player.hasPermission("autopickup.autosmelt.mobs.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.autosmelt.mobs")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setAutoSmeltMobs(true);
                messageSender.send(player, Message.AS_MOB_ENABLED);
            }
            case "careful-break" -> {
                if(forOther && !player.hasPermission("autopickup.carefulbreak.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.carefulbreak")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setCarefulBreak(true);
                messageSender.send(player, Message.CAREFUL_BREAK_ENABLED);
            }
            case "careful-smelt" -> {
                if(forOther && !player.hasPermission("autopickup.carefulsmelt.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.carefulsmelt")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setCarefulSmelt(true);
                messageSender.send(player, Message.CAREFUL_SMELT_ENABLED);
            }
        }
    }

    public void disableSetting(String setting, Player player, boolean forOther){
        AutoPickupSettings playerSettings = this.plugin.getAutoPickupManager().getPlayer(player.getName());
        switch (setting) {
            case "block-auto-pickup" -> {
                if(forOther && !player.hasPermission("autopickup.autopickup.blocks.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.autopickup.blocks")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setAutoPickupBlocks(false);
                messageSender.send(player, Message.AP_BLOCKS_DISABLED);
            }
            case "mob-auto-pickup" -> {
                if(forOther && !player.hasPermission("autopickup.autopickup.mobs.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.autopickup.mobs")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setAutoPickupMobDrops(false);
                messageSender.send(player, Message.AP_MOB_DISABLED);
            }
            case "exp-auto-pickup" -> {
                if(forOther && !player.hasPermission("autopickup.autopickup.exp.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.autopickup.exp")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setAutoPickupExp(false);
                messageSender.send(player, Message.AP_EXP_DISABLED);
            }
            case "block-auto-smelt" -> {
                if(forOther && !player.hasPermission("autopickup.autosmelt.blocks.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.autosmelt.blocks")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setAutoSmeltBlocks(false);
                messageSender.send(player, Message.AS_BLOCKS_DISABLED);
            }
            case "mob-auto-smelt" -> {
                if(forOther && !player.hasPermission("autopickup.autosmelt.mobs.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.autosmelt.mobs")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setAutoSmeltMobs(false);
                messageSender.send(player, Message.AS_MOB_DISABLED);
            }
            case "careful-break" -> {
                if(forOther && !player.hasPermission("autopickup.carefulbreak.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.carefulbreak")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setCarefulBreak(false);
                messageSender.send(player, Message.CAREFUL_BREAK_DISABLED);
            }
            case "careful-smelt" -> {
                if(forOther && !player.hasPermission("autopickup.carefulsmelt.others")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if(!player.hasPermission("autopickup.carefulsmelt")){
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                playerSettings.setCarefulSmelt(false);
                messageSender.send(player, Message.CAREFUL_SMELT_DISABLED);
            }
        }
    }

    public void toggleSetting(String setting, Player player, boolean forOther) {
        AutoPickupSettings playerSettings = this.plugin.getAutoPickupManager().getPlayer(player.getName());
        switch (setting) {
            case "block-auto-pickup" -> {
                if (forOther && !player.hasPermission("autopickup.autopickup.blocks.others")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if (!player.hasPermission("autopickup.autopickup.blocks")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                boolean wasEnabled = playerSettings.autoPickupBlocksEnabled();
                playerSettings.setAutoPickupBlocks(!wasEnabled);
                messageSender.send(player, wasEnabled ? Message.AP_BLOCKS_DISABLED : Message.AP_BLOCKS_ENABLED);
            }
            case "mob-auto-pickup" -> {
                if (forOther && !player.hasPermission("autopickup.autopickup.mobs.others")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if (!player.hasPermission("autopickup.autopickup.mobs")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                boolean wasEnabled = playerSettings.autoPickupMobDropsEnabled();
                playerSettings.setAutoPickupMobDrops(!wasEnabled);
                messageSender.send(player, wasEnabled ? Message.AP_MOB_DISABLED : Message.AP_MOB_ENABLED);
            }
            case "exp-auto-pickup" -> {
                if (forOther && !player.hasPermission("autopickup.autopickup.exp.others")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if (!player.hasPermission("autopickup.autopickup.exp")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                boolean wasEnabled = playerSettings.autoPickupExpEnabled();
                playerSettings.setAutoPickupExp(!wasEnabled);
                messageSender.send(player, wasEnabled ? Message.AP_EXP_DISABLED : Message.AP_EXP_ENABLED);
            }
            case "block-auto-smelt" -> {
                if (forOther && !player.hasPermission("autopickup.autosmelt.blocks.others")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if (!player.hasPermission("autopickup.autosmelt.blocks")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                boolean wasEnabled = playerSettings.autoSmeltBlocksEnabled();
                playerSettings.setAutoSmeltBlocks(!wasEnabled);
                messageSender.send(player, wasEnabled ? Message.AS_BLOCKS_DISABLED : Message.AS_BLOCKS_ENABLED);
            }
            case "mob-auto-smelt" -> {
                if (forOther && !player.hasPermission("autopickup.autosmelt.mobs.others")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if (!player.hasPermission("autopickup.autosmelt.mobs")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                boolean wasEnabled = playerSettings.autoSmeltMobEnabled();
                playerSettings.setAutoSmeltMobs(!wasEnabled);
                messageSender.send(player, wasEnabled ? Message.AS_MOB_DISABLED : Message.AS_MOB_ENABLED);
            }
            case "careful-break" -> {
                if (forOther && !player.hasPermission("autopickup.carefulbreak.others")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if (!player.hasPermission("autopickup.carefulbreak")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                boolean wasEnabled = playerSettings.carefulBreakEnabled();
                playerSettings.setCarefulBreak(!wasEnabled);
                messageSender.send(player, wasEnabled ? Message.CAREFUL_BREAK_DISABLED : Message.CAREFUL_BREAK_ENABLED);
            }
            case "careful-smelt" -> {
                if (forOther && !player.hasPermission("autopickup.carefulsmelt.others")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                if (!player.hasPermission("autopickup.carefulsmelt")) {
                    messageSender.send(player, Message.NO_PERMISSION);
                    break;
                }
                boolean wasEnabled = playerSettings.carefulSmeltEnabled();
                playerSettings.setCarefulSmelt(!wasEnabled);
                messageSender.send(player, wasEnabled ? Message.CAREFUL_SMELT_DISABLED : Message.CAREFUL_SMELT_ENABLED);
            }
        }
    }
}
