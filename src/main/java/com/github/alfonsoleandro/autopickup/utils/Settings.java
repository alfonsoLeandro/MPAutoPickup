package com.github.alfonsoleandro.autopickup.utils;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.mputils.reloadable.Reloadable;
import com.github.alfonsoleandro.mputils.time.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.mariuszgromada.math.mxparser.Expression;

import java.util.*;

public class Settings extends Reloadable {

    private final AutoPickup plugin;
    private final Random r;

    //Fields
    private boolean useVanillaEnchantments;
    private boolean customSilkTouchEnabled;
    private boolean customFortuneEnabled;
    private boolean autoSmeltEnabled;
    private boolean autoPickupPlayerDrops;
    private boolean vkBackPacksSupport;

    private int ticksBeforeAlert;

    private String fortuneAmount;

    private Set<Material> fortuneMaterials;
    private Set<Material> itemBlackList;
    private Set<Material> blockBlackList;
    private Set<EntityType> entityBlackList;

    private Map<Material, Material> silkTouchMaterials;
    private Map<Material, Material> autoSmeltMaterials;
    private Map<AutoPickupSounds, SoundSettings> sounds;

    public Settings(AutoPickup plugin){
        super(plugin);
        this.plugin = plugin;
        this.r = new Random();
        loadFields();
    }

    private void loadFields(){
        FileConfiguration config = this.plugin.getConfig();

        this.useVanillaEnchantments = config.getBoolean("config.use vanilla enchantments");
        this.customSilkTouchEnabled = config.getBoolean("config.silk touch.enabled");
        this.customFortuneEnabled = config.getBoolean("config.fortune.enabled");
        this.autoSmeltEnabled = config.getBoolean("config.auto smelt.enabled");
        this.autoPickupPlayerDrops = config.getBoolean("config.auto pickup player drops");
        Plugin vkBp = Bukkit.getPluginManager().getPlugin("VKBackPack");
        this.vkBackPacksSupport = config.getBoolean("config.vkBackPacks support") &&
                vkBp != null && vkBp.isEnabled();

        String timeString = config.getString("config.time before full inv alert", "0s");
        this.ticksBeforeAlert = TimeUtils.getTicks(
                Integer.parseInt(timeString.substring(0, timeString.length()-1)),
                timeString.charAt(timeString.length()-1));


        this.fortuneAmount = config.getString("config.fortune.amount");

        this.fortuneMaterials = new HashSet<>();
        for(String material : config.getStringList("config.fortune.items")){
            this.fortuneMaterials.add(Material.valueOf(material));
        }
        this.itemBlackList = new HashSet<>();
        for(String material : config.getStringList("config.item blacklist")){
            this.itemBlackList.add(Material.valueOf(material));
        }
        this.blockBlackList = new HashSet<>();
        for(String material : config.getStringList("config.block blacklist")){
            this.blockBlackList.add(Material.valueOf(material));
        }
        this.entityBlackList = new HashSet<>();
        for(String material : config.getStringList("config.entity blacklist")){
            this.entityBlackList.add(EntityType.valueOf(material));
        }


        this.silkTouchMaterials = new HashMap<>();
        for(String material : Objects.requireNonNull(config.getConfigurationSection("config.silk touch.blocks")).getKeys(false)){
            this.silkTouchMaterials.put(Material.valueOf(material),
                    Material.valueOf(config.getString("config.silk touch.blocks."+material)));
        }
        this.autoSmeltMaterials = new HashMap<>();
        for(String material : Objects.requireNonNull(config.getConfigurationSection("config.auto smelt.materials")).getKeys(false)){
            this.autoSmeltMaterials.put(Material.valueOf(material),
                    Material.valueOf(config.getString("config.auto smelt.materials."+material)));
        }

        this.sounds = new HashMap<>();
        try {
            this.sounds.put(AutoPickupSounds.BLOCKS, new SoundSettings(
                    config.getBoolean("config.sound.block.enabled"),
                    Sound.valueOf(config.getString("config.sound.block.sound name")),
                    Objects.requireNonNull(config.getString("config.sound.block.volume")),
                    Objects.requireNonNull(config.getString("config.sound.block.pitch"))));
        }catch (Exception ex){
            errorLoadingSound("block");
        }

        try {
            this.sounds.put(AutoPickupSounds.MOBS, new SoundSettings(
                    config.getBoolean("config.sound.mob.enabled"),
                    Sound.valueOf(config.getString("config.sound.mob.sound name")),
                    Objects.requireNonNull(config.getString("config.sound.mob.volume")),
                    Objects.requireNonNull(config.getString("config.sound.mob.pitch"))));
        }catch (Exception ex){
            errorLoadingSound("mob");
        }

        try {
            this.sounds.put(AutoPickupSounds.EXP, new SoundSettings(
                    config.getBoolean("config.sound.exp.enabled"),
                    Sound.valueOf(config.getString("config.sound.exp.sound name")),
                    Objects.requireNonNull(config.getString("config.sound.exp.volume")),
                    Objects.requireNonNull(config.getString("config.sound.exp.pitch"))));
        }catch (Exception ex){
            errorLoadingSound("exp");
        }

        try {
            this.sounds.put(AutoPickupSounds.FULL_INV, new SoundSettings(
                    config.getBoolean("config.sound.full inv.enabled"),
                    Sound.valueOf(config.getString("config.sound.full inv.sound name")),
                    Objects.requireNonNull(config.getString("config.sound.full inv.volume")),
                    Objects.requireNonNull(config.getString("config.sound.full inv.pitch"))));
        }catch (Exception ex){
            errorLoadingSound("full inv");
        }

    }

    //Getters

    public boolean isUseVanillaEnchantments() {
        return this.useVanillaEnchantments;
    }

    public boolean isCustomSilkTouchEnabled() {
        return this.customSilkTouchEnabled;
    }

    public boolean isCustomFortuneEnabled() {
        return this.customFortuneEnabled;
    }

    public boolean isAutoSmeltEnabled() {
        return this.autoSmeltEnabled;
    }

    public boolean isAutoPickupPlayerDrops() {
        return this.autoPickupPlayerDrops;
    }

    public boolean isVkBackPacksSupport() {
        return this.vkBackPacksSupport;
    }


    public int getTicksBeforeAlert(){
        return this.ticksBeforeAlert;
    }

    public int getFortuneAmount(int fortuneLevel){
        String[] values = this.fortuneAmount.replace("%level%", String.valueOf(fortuneLevel)).split(";");
        double v1 = new Expression(values[0]).calculate();
        double v2 = new Expression(values[1]).calculate();

        return (int) (this.r.nextInt((int) (v2-v1+1))+v1);
    }


    public SoundSettings getSound(AutoPickupSounds sound){
        return this.sounds.get(sound);
    }


    public Set<Material> getFortuneMaterials() {
        return this.fortuneMaterials;
    }

    public Set<Material> getItemBlackList() {
        return this.itemBlackList;
    }

    public Set<Material> getBlockBlackList() {
        return this.blockBlackList;
    }

    public Set<EntityType> getEntityBlackList() {
        return this.entityBlackList;
    }


    public Map<Material, Material> getSilkTouchMaterials() {
        return this.silkTouchMaterials;
    }

    public Map<Material, Material> getAutoSmeltMaterials() {
        return this.autoSmeltMaterials;
    }


    /**
     * Sends a message to the console whenever an error when trying to register a sound occurs.
     * @param sound The sound name in config.
     */
    private void errorLoadingSound(String sound){
        this.plugin.getMessageSender().send("&cThere has been an error while trying to load the \"&e"+sound+"&c\" sound from config. please verify your fields.");
     }


    @Override
    public void reload(boolean deep) {
        loadFields();
    }


    public enum AutoPickupSounds{
        BLOCKS,
        MOBS,
        EXP,
        FULL_INV
    }
}
