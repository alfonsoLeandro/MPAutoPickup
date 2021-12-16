package com.github.alfonsoleandro.autopickup;

import com.github.alfonsoleandro.autopickup.commands.MainCommand;
import com.github.alfonsoleandro.autopickup.commands.MainCommandTabCompleter;
import com.github.alfonsoleandro.autopickup.events.*;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupManager;
import com.github.alfonsoleandro.autopickup.utils.Message;
import com.github.alfonsoleandro.autopickup.utils.PAPIPlaceholders;
import com.github.alfonsoleandro.autopickup.utils.Settings;
import com.github.alfonsoleandro.mputils.files.YamlFile;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import com.github.alfonsoleandro.mputils.metrics.Metrics;
import com.github.alfonsoleandro.mputils.reloadable.ReloaderPlugin;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class AutoPickup extends ReloaderPlugin {

    private final PluginDescriptionFile pdfFile = getDescription();
    private final String version = this.pdfFile.getVersion();
    private String latestVersion;
    private final char color = '2';
    //Ex: 1.8.9 ->  8 = discriminant
    private final int serverVersionDiscriminant = Integer.parseInt(
            getServer().getBukkitVersion().split("\\.")[1].split("-")[0]);
    private AutoPickupManager autoPickupManager;
    private Settings settings;
    private MessageSender<Message> messageSender;
    private YamlFile configYaml;
    private YamlFile playersYaml;
    private PAPIPlaceholders papiExpansion;

    @Override
    public void onEnable() {
        registerFiles();
        this.messageSender = new MessageSender<>(this, Message.values(), this.configYaml, "config.prefix");
        this.messageSender.send("&aEnabled&f. Version: &e" + this.version);
        this.messageSender.send("&fThank you for using my plugin! &" + this.color + this.pdfFile.getName() + "&f By " + this.pdfFile.getAuthors().get(0));
        this.messageSender.send("&fJoin my discord server at &chttps://discordapp.com/invite/ZznhQud");
        this.messageSender.send("Please consider subscribing to my yt channel: &c" + this.pdfFile.getWebsite());
        this.autoPickupManager = new AutoPickupManager(this);
        this.settings = new Settings(this);
        registerEvents();
        registerCommands();
        updateChecker();
        startMetrics();
        registerPAPIPlaceholder();
    }

    @Override
    public void onDisable() {
        this.messageSender.send("&cDisabled&f. Version: &e" + this.version);
        this.messageSender.send("&fThank you for using my plugin! &" + this.color + this.pdfFile.getName() + "&f By " + this.pdfFile.getAuthors().get(0));
        this.messageSender.send("&fJoin my discord server at &chttps://discordapp.com/invite/ZznhQud");
        this.messageSender.send("Please consider subscribing to my yt channel: &c" + this.pdfFile.getWebsite());
        this.autoPickupManager.saveAll();
        unRegisterPAPIPlaceholder();
    }


    /**
     * Starts the metrics process.
     */
    private void startMetrics(){
        if(getConfig().getBoolean("config.use metrics")){
            new Metrics(this, 8883);
        }else{
            this.messageSender.send("&cPlease consider setting &ause metrics &cto &atrue &cin config!");
            this.messageSender.send("&cIt really helps the developer! :D");
        }
    }


    /**
     * Checks for new versions available on spigot.
     */
    private void updateChecker(){
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=80867").openConnection();
            final int timed_out = 1250;
            con.setConnectTimeout(timed_out);
            con.setReadTimeout(timed_out);
            this.latestVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (this.latestVersion.length() <= 7) {
                if(!this.version.equals(this.latestVersion)){
                    String exclamation = "&e&l(&4&l!&e&l)";
                    this.messageSender.send(exclamation +" &cThere is a new version available. &e(&7"+ this.latestVersion +"&e)");
                    this.messageSender.send(exclamation +" &cDownload it here: &ehttps://bit.ly/autopickupUpdate");
                }
            }
        } catch (Exception ex) {
            this.messageSender.send("&cThere was an error while checking for updates");
        }
    }


    public void registerPAPIPlaceholder(){
        Plugin papi = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if(papi != null && papi.isEnabled()){
            this.messageSender.send("&aPlaceholderAPI found, the placeholder has been registered successfully");
            this.papiExpansion = new PAPIPlaceholders(this);
            this.papiExpansion.register();
        }else{
            this.messageSender.send("&cPlaceholderAPI not found, the placeholder was not registered");
        }
    }

    private void unRegisterPAPIPlaceholder(){
        Plugin papi = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if(papi != null && papi.isEnabled() && this.papiExpansion != null){
            this.papiExpansion.unregister();
        }
    }

    /**
     * Registers the main command for this plugin.
     */
    private void registerCommands() {
        PluginCommand mainCommand = getCommand("autoPickup");

        if(mainCommand == null){
            this.messageSender.send("&cThere was an error while trying to register this plugin's main command");
            this.messageSender.send("&cPlease check this plugin's plugin.yml file is intact.");
            setEnabled(false);
            return;
        }

        mainCommand.setExecutor(new MainCommand(this));
        mainCommand.setTabCompleter(new MainCommandTabCompleter());
    }

    /**
     * Reloads the plugin and its files.
     */
    @Override
    public void reload(boolean deep){
        reloadFiles();
        super.reload(deep);
    }


    /**
     * Registers the files for this plugin.
     */
    private void registerFiles(){
        this.playersYaml = new YamlFile(this, "players.yml");
        boolean firstConfig = !new File(getDataFolder(), "config.yml").exists();
        this.configYaml = new YamlFile(this, "config.yml");
        if(firstConfig){
            checkLegacyFields();
        }
    }

    /**
     * Registers and reloads the files for this plugin.
     */
    private void reloadFiles(){
        this.playersYaml = new YamlFile(this, "players.yml");
        this.configYaml = new YamlFile(this, "config.yml");
    }


    /**
     * Checks for server version to adjust some version sensible fields like sounds names.
     */
    private void checkLegacyFields(){
        FileConfiguration config = getConfig();
        if(this.serverVersionDiscriminant < 9) {
            config.set("config.sound.block.sound name", "ITEM_PICKUP");
            config.set("config.sound.mob.sound name", "ITEM_PICKUP");
            config.set("config.sound.exp.sound name", "ORB_PICKUP");
            config.set("config.sound.full inv.sound name", "VILLAGER_NO");
        }
        if(this.serverVersionDiscriminant < 13) {

            config.set("config.GUI.auto pickup mob drops.enabled.item", "SKULL_ITEM");
            config.set("config.GUI.auto pickup mob drops.disabled.item", "SKULL_ITEM");

            config.set("config.GUI.auto pickup exp.enabled.item", "EXP_BOTTLE");
            config.set("config.GUI.auto pickup exp.disabled.item", "EXP_BOTTLE");

            config.set("config.silk touch.blocks.GRASS_BLOCK", null);
            getConfigYaml().save(true);
        }

    }

    /**
     * Registers this plugin's Event listeners.
     */
    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JoinLeaveEvents(this), this);
        pm.registerEvents(new GUIClickListener(this), this);
        if(Bukkit.getPluginManager().isPluginEnabled("BetterBackpacks")) {
            Bukkit.broadcastMessage("bbp entcontrao");
            pm.registerEvents(new BetterBackpacksListener(this), this);
        }else{
            Bukkit.broadcastMessage("bbp NOOOO entcontrao");
        }

        AutoPickupEventsListener listeners = new AutoPickupEventsListener(this, this.serverVersionDiscriminant);
        String[] priorities = new String[]{"LOWEST", "LOW", "NORMAL", "HIGH", "HIGHEST"};
        String blockBreakPriority = Arrays.stream(priorities).anyMatch(
                k -> k.equalsIgnoreCase(this.configYaml.getAccess().getString("config.event priorities.block break")))
                ?
                this.configYaml.getAccess().getString("config.event priorities.block break")
                :
                "HIGHEST";
        String entityDeathPriority = Arrays.stream(priorities).anyMatch(
                k -> k.equalsIgnoreCase(this.configYaml.getAccess().getString("config.event priorities.entity death")))
                ?
                this.configYaml.getAccess().getString("config.event priorities.entity death")
                :
                "HIGHEST";
        pm.registerEvent(BlockBreakEvent.class,
                listeners,
                EventPriority.valueOf(blockBreakPriority),
                listeners,
                this);
        this.messageSender.send("&aBlock break event registered with priority: &c"+blockBreakPriority);
        pm.registerEvent(EntityDeathEvent.class,
                listeners,
                EventPriority.valueOf(entityDeathPriority),
                listeners,
                this);
        this.messageSender.send("&aEntity death event registered with priority: &c"+entityDeathPriority);
    }

    /**
     * Gets the plugin's current version.
     * @return This plugin's current version.
     */
    public String getVersion(){
        return this.version;
    }

    /**
     * Gets the latest plugin version from spigot.
     * @return This plugin's latest version.
     */
    public String getLatestVersion(){
        return this.latestVersion;
    }

    /**
     * Gets the auto pickup manager object.
     * @return The auto pickup manager object.
     */
    public AutoPickupManager getAutoPickupManager(){
        return this.autoPickupManager;
    }


    /**
     * Gets the settings object, where a lot of settings related to AutoPickup are located.
     * @return The settings object.
     */
    public Settings getSettings(){
        return this.settings;
    }

    /**
     * Gets the MessageSender object, the only way this plugin sends messages to players and the console.
     * @return The MessageSenderObject.
     */
    public MessageSender<Message> getMessageSender(){
        return this.messageSender;
    }


    @Override
    public @NotNull FileConfiguration getConfig(){
        return this.configYaml.getAccess();
    }

    /**
     * Gets the YamlFile object containing the config FileConfiguration object.
     * @return The config YamlFile.
     */
    public YamlFile getConfigYaml(){
        return this.configYaml;
    }

    /**
     * Gets the YamlFile object containing the players FileConfiguration object.
     * @return The players YamlFile.
     */
    public YamlFile getPlayersYaml(){
        return this.playersYaml;
    }

}
