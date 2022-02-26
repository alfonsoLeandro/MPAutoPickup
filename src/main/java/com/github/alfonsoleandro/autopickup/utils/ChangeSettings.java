package com.github.alfonsoleandro.autopickup.utils;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupSettings;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import org.bukkit.entity.Player;
import java.util.Locale;

public class ChangeSettings {
    private final AutoPickup plugin;
    private final MessageSender<Message> messageSender;

    public ChangeSettings(AutoPickup plugin){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
    }

    /**
     * Checks if a setting type exists or not in the list of available settings.
     * @param setting The setting to try to change.
     * @return true if the setting is invalid.
     */
    public boolean settingNotExists(String setting) {
        return SettingType.getByString(setting) == null;
    }

    /**
     * Checks if a player has the required permission for what they are about to do.
     * @param settingType The type of setting they are going to change.
     * @param player The player trying to change
     * @param forOther TODO
     * @return TODO
     */
    private boolean notHasPermission(SettingType settingType, Player player, boolean forOther){
        if(forOther && !player.hasPermission("autoPickup."+settingType.getPermNode()+".others")){
            this.messageSender.send(player, Message.NO_PERMISSION);
            return true;
        }
        if(!forOther && !player.hasPermission("autoPickup."+settingType.getPermNode())){
            this.messageSender.send(player, Message.NO_PERMISSION);
            return true;
        }
        return false;
    }

    public void enableSetting(String setting, Player player, boolean forOther){
        AutoPickupSettings playerSettings = this.plugin.getAutoPickupManager().getPlayer(player.getName());
        if(notHasPermission(SettingType.getByString(setting), player, forOther)) return;

        switch (setting) {
            case "block-auto-pickup" -> {
                playerSettings.setAutoPickupBlocks(true);
                this.messageSender.send(player, Message.AP_BLOCKS_ENABLED);
            }
            case "mob-auto-pickup" -> {
                playerSettings.setAutoPickupMobDrops(true);
                this.messageSender.send(player, Message.AP_MOB_ENABLED);
            }
            case "exp-auto-pickup" -> {
                playerSettings.setAutoPickupExp(true);
                this.messageSender.send(player, Message.AP_EXP_ENABLED);
            }
            case "block-auto-smelt" -> {
                playerSettings.setAutoSmeltBlocks(true);
                this.messageSender.send(player, Message.AS_BLOCKS_ENABLED);
            }
            case "mob-auto-smelt" -> {
                playerSettings.setAutoSmeltMobs(true);
                this.messageSender.send(player, Message.AS_MOB_ENABLED);
            }
            case "careful-break" -> {
                playerSettings.setCarefulBreak(true);
                this.messageSender.send(player, Message.CAREFUL_BREAK_ENABLED);
            }
            case "careful-smelt" -> {
                playerSettings.setCarefulSmelt(true);
                this.messageSender.send(player, Message.CAREFUL_SMELT_ENABLED);
            }
        }
    }

    public void disableSetting(String setting, Player player, boolean forOther){
        AutoPickupSettings playerSettings = this.plugin.getAutoPickupManager().getPlayer(player.getName());
        if(notHasPermission(SettingType.getByString(setting), player, forOther)) return;

        switch (setting) {
            case "block-auto-pickup" -> {
                playerSettings.setAutoPickupBlocks(false);
                this.messageSender.send(player, Message.AP_BLOCKS_DISABLED);
            }
            case "mob-auto-pickup" -> {
                playerSettings.setAutoPickupMobDrops(false);
                this.messageSender.send(player, Message.AP_MOB_DISABLED);
            }
            case "exp-auto-pickup" -> {
                playerSettings.setAutoPickupExp(false);
                this.messageSender.send(player, Message.AP_EXP_DISABLED);
            }
            case "block-auto-smelt" -> {
                playerSettings.setAutoSmeltBlocks(false);
                this.messageSender.send(player, Message.AS_BLOCKS_DISABLED);
            }
            case "mob-auto-smelt" -> {
                playerSettings.setAutoSmeltMobs(false);
                this.messageSender.send(player, Message.AS_MOB_DISABLED);
            }
            case "careful-break" -> {
                playerSettings.setCarefulBreak(false);
                this.messageSender.send(player, Message.CAREFUL_BREAK_DISABLED);
            }
            case "careful-smelt" -> {
                playerSettings.setCarefulSmelt(false);
                this.messageSender.send(player, Message.CAREFUL_SMELT_DISABLED);
            }
        }
    }

    public void toggleSetting(String setting, Player player, boolean forOther) {
        AutoPickupSettings playerSettings = this.plugin.getAutoPickupManager().getPlayer(player.getName());
        if(notHasPermission(SettingType.getByString(setting), player, forOther)) return;

        switch (setting) {
            case "block-auto-pickup" -> {
                boolean wasEnabled = playerSettings.autoPickupBlocksEnabled();
                playerSettings.setAutoPickupBlocks(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AP_BLOCKS_DISABLED : Message.AP_BLOCKS_ENABLED);
            }
            case "mob-auto-pickup" -> {
                boolean wasEnabled = playerSettings.autoPickupMobDropsEnabled();
                playerSettings.setAutoPickupMobDrops(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AP_MOB_DISABLED : Message.AP_MOB_ENABLED);
            }
            case "exp-auto-pickup" -> {
                boolean wasEnabled = playerSettings.autoPickupExpEnabled();
                playerSettings.setAutoPickupExp(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AP_EXP_DISABLED : Message.AP_EXP_ENABLED);
            }
            case "block-auto-smelt" -> {
                boolean wasEnabled = playerSettings.autoSmeltBlocksEnabled();
                playerSettings.setAutoSmeltBlocks(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AS_BLOCKS_DISABLED : Message.AS_BLOCKS_ENABLED);
            }
            case "mob-auto-smelt" -> {
                boolean wasEnabled = playerSettings.autoSmeltMobEnabled();
                playerSettings.setAutoSmeltMobs(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.AS_MOB_DISABLED : Message.AS_MOB_ENABLED);
            }
            case "careful-break" -> {
                boolean wasEnabled = playerSettings.carefulBreakEnabled();
                playerSettings.setCarefulBreak(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.CAREFUL_BREAK_DISABLED : Message.CAREFUL_BREAK_ENABLED);
            }
            case "careful-smelt" -> {
                boolean wasEnabled = playerSettings.carefulSmeltEnabled();
                playerSettings.setCarefulSmelt(!wasEnabled);
                this.messageSender.send(player, wasEnabled ? Message.CAREFUL_SMELT_DISABLED : Message.CAREFUL_SMELT_ENABLED);
            }
        }
    }


    enum SettingType{
        BLOCK_AUTO_PICKUP("autoPickup.blocks"),
        MOB_AUTO_PICKUP("autoPickup.mobs"),
        EXP_AUTO_PICKUP("autoPickup.exp"),
        BLOCK_AUTO_SMELT("autoSmelt.blocks"),
        MOB_AUTO_SMELT("autoSmelt.mobs"),
        CAREFUL_BREAK("carefulBreak"),
        CAREFUL_SMELT("carefulSmelt");
        
        
        private final String permNode;
        
        SettingType(String permNode){
            this.permNode = permNode;    
        }

        public String getPermNode() {
            return this.permNode;
        }

        public static SettingType getByString(String settingType){
            return switch (settingType.toLowerCase(Locale.ROOT)) {
                case "block-auto-pickup" -> BLOCK_AUTO_PICKUP;
                case "mob-auto-pickup" -> MOB_AUTO_PICKUP;
                case "exp-auto-pickup" -> EXP_AUTO_PICKUP;
                case "block-auto-smelt" -> BLOCK_AUTO_SMELT;
                case "mob-auto-smelt" -> MOB_AUTO_SMELT;
                case "careful-break" -> CAREFUL_BREAK;
                case "careful-smelt" -> CAREFUL_SMELT;
                default -> null;
            };
        }
    }
}
