package com.github.alfonsoleandro.autopickup.events;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupSettings;
import com.github.alfonsoleandro.mputils.guis.SimpleGUI;
import com.github.alfonsoleandro.mputils.guis.events.GUIClickEvent;
import com.github.alfonsoleandro.mputils.guis.utils.GUIType;
import com.github.alfonsoleandro.mputils.itemstacks.MPItemStacks;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
import com.github.alfonsoleandro.mputils.string.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class GUIClickListener extends Reloadable implements Listener {

    private final AutoPickup plugin;
    // Translatable messages
    private String prefix;
    private String noPerm;
    private String blocksEnabled;
    private String blocksDisabled;
    private String mobsEnabled;
    private String mobsDisabled;
    private String expEnabled;
    private String expDisabled;
    private String smeltBlockEnabled;
    private String smeltBlockDisabled;
    private String smeltMobEnabled;
    private String smeltMobDisabled;


    public GUIClickListener(AutoPickup plugin){
        super(plugin);
        this.plugin = plugin;
        loadMessages();
    }

    private void loadMessages(){
        FileConfiguration config = plugin.getConfigYaml().getAccess();

        prefix = config.getString("config.prefix");
        blocksEnabled = config.getString("config.messages.autoPickup.blocks.enabled");
        blocksDisabled = config.getString("config.messages.autoPickup.blocks.disabled");
        mobsEnabled = config.getString("config.messages.autoPickup.mob.enabled");
        mobsDisabled = config.getString("config.messages.autoPickup.mob.disabled");
        expEnabled = config.getString("config.messages.autoPickup.exp.enabled");
        expDisabled = config.getString("config.messages.autoPickup.exp.disabled");
        smeltBlockEnabled = config.getString("config.messages.autoSmelt.blocks.enabled");
        smeltBlockDisabled = config.getString("config.messages.autoSmelt.blocks.disabled");
        smeltMobEnabled = config.getString("config.messages.autoSmelt.mob.enabled");
        smeltMobDisabled = config.getString("config.messages.autoSmelt.mob.disabled");
        noPerm = config.getString("config.messages.no permission");
    }


    @EventHandler
    public void onGuiClick(GUIClickEvent event){
        if(event.getGuiType().equals(GUIType.PAGINATED)) return;
        if(!event.getGui().getGuiTags().equalsIgnoreCase("MPAutoPickup")) return;
        event.setCancelled(true);
        int clickedSlot = event.getRawSlot();
        if(clickedSlot > 4 || clickedSlot < 0) return;
        Player player = (Player) event.getWhoClicked();
        AutoPickupSettings settings = plugin.getAutoPickupManager().getPlayer(player.getName());


        if(clickedSlot == 0){
            if(player.hasPermission("autoPickup.autoPickup.block")) {
                boolean wasEnabled = settings.autoPickupBlocksEnabled();
                settings.setAutoPickupBlocks(!wasEnabled);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + " " + (wasEnabled ? blocksDisabled : blocksEnabled)));
                openGUI(player);
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + " " + noPerm));
            }

        }else if(clickedSlot == 1){
            if(player.hasPermission("autoPickup.autoPickup.mob")) {
                boolean wasEnabled = settings.autoPickupMobDropsEnabled();
                settings.setAutoPickupMobDrops(!wasEnabled);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + " " + (wasEnabled ? mobsDisabled : mobsEnabled)));
                openGUI(player);
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + " " + noPerm));
            }

        }else if(clickedSlot == 2){
            if(player.hasPermission("autoPickup.autoPickup.exp")) {
                boolean wasEnabled = settings.autoPickupExpEnabled();
                settings.setAutoPickupExp(!wasEnabled);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + " " + (wasEnabled ? expDisabled : expEnabled)));
                openGUI(player);
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + " " + noPerm));
            }

        }else if(clickedSlot == 3){
            if(player.hasPermission("autoPickup.autoSmelt.blocks")) {
                boolean wasEnabled = settings.autoSmeltBlocksEnabled();
                settings.setAutoSmeltBlocks(!wasEnabled);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + " " + (wasEnabled ? smeltBlockDisabled : smeltBlockEnabled)));
                openGUI(player);
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + " " + noPerm));
            }

        }else {
            if(player.hasPermission("autoPickup.autoSmelt.mob")) {
                boolean wasEnabled = settings.autoSmeltMobEnabled();
                settings.setAutoSmeltMobs(!wasEnabled);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + " " + (wasEnabled ? smeltMobDisabled : smeltMobEnabled)));
                openGUI(player);
            }else{
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        prefix + " " + noPerm));
            }

        }
    }


    private void openGUI(Player player){
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
        loadMessages();
    }

}
