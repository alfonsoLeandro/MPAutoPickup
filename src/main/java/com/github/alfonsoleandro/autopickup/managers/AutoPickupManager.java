package com.github.alfonsoleandro.autopickup.managers;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AutoPickupManager {

    private final AutoPickup plugin;
    private final Map<String, AutoPickupSettings> players = new HashMap<>();

    public AutoPickupManager(AutoPickup plugin){
        this.plugin = plugin;
    }

    public @NotNull AutoPickupSettings getPlayer(String playerName){
        if(!players.containsKey(playerName)){
            loadPlayer(playerName);
        }
        return players.get(playerName);
    }

    public @NotNull AutoPickupSettings getPlayer(Player player){
        return getPlayer(player.getName());
    }



    public void loadPlayer(String playerName){
        FileConfiguration players = plugin.getPlayersYaml().getAccess();

        if(players.contains("players."+playerName)){
            this.players.put(playerName, new AutoPickupSettings(
                    players.getBoolean("players."+playerName+".blocks"),
                    players.getBoolean("players."+playerName+".mob"),
                    players.getBoolean("players."+playerName+".exp"),
                    players.getBoolean("players."+playerName+".smelt blocks"),
                    players.getBoolean("players."+playerName+".smelt mobs")
            ));
        }else{
            FileConfiguration config = plugin.getConfig();
            this.players.put(playerName, new AutoPickupSettings(
                    config.getBoolean("config.default values.autoPickup blocks"),
                    config.getBoolean("config.default values.autoPickup mobs"),
                    config.getBoolean("config.default values.autoPickup exp"),
                    config.getBoolean("config.default values.autoSmelt blocks"),
                    config.getBoolean("config.default values.autoSmelt mobs")
            ));
        }

    }

    public void savePlayer(String playerName, boolean async){
        if(!players.containsKey(playerName)) return;
        FileConfiguration players = plugin.getPlayersYaml().getAccess();
        AutoPickupSettings settings = this.players.get(playerName);

        players.set("players."+playerName+".blocks", settings.autoPickupBlocksEnabled());
        players.set("players."+playerName+".mob", settings.autoPickupMobDropsEnabled());
        players.set("players."+playerName+".exp", settings.autoPickupExpEnabled());
        players.set("players."+playerName+".smelt blocks", settings.autoSmeltBlocksEnabled());
        players.set("players."+playerName+".smelt mobs", settings.autoSmeltMobEnabled());

        plugin.getPlayersYaml().save(async);
    }

    public void saveAll() {
        for (String playerName : this.players.keySet()) {
            savePlayer(playerName, false);
        }
    }


}
