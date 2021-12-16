package com.github.alfonsoleandro.autopickup.events;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupSettings;
import com.github.alfonsoleandro.autopickup.utils.Message;
import com.github.alfonsoleandro.mputils.guis.SimpleGUI;
import com.github.alfonsoleandro.mputils.guis.events.GUIClickEvent;
import com.github.alfonsoleandro.mputils.guis.utils.GUIType;
import com.github.alfonsoleandro.mputils.itemstacks.MPItemStacks;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import com.github.alfonsoleandro.mputils.string.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class GUIClickListener implements Listener {

    private final AutoPickup plugin;
    private final MessageSender<Message> messageSender;


    public GUIClickListener(AutoPickup plugin){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
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

        //TODO: load gui items in settings class
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
