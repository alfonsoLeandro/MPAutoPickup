package com.github.alfonsoleandro.autopickup.utils;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupSettings;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PAPIPlaceholders extends PlaceholderExpansion {

    private final AutoPickup plugin;

    public PAPIPlaceholders(AutoPickup plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public @NotNull String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion(){
        return plugin.getDescription().getVersion();
    }

    /*
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>The identifier has to be lowercase and can't contain _ or %
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public @NotNull String getIdentifier(){
        return "MPAutoPickup";
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier){
        if(player == null){
            return "";
        }
        AutoPickupSettings playerSettings = plugin.getAutoPickupManager().getPlayer(player);
        FileConfiguration config = plugin.getConfigYaml().getAccess();
        String enabled = config.getString("config.messages.placeholder status.enabled");
        String disabled = config.getString("config.messages.placeholder status.disabled");



        // %MPAutoPickup_ap_block%
        if(identifier.equalsIgnoreCase("ap_block")){
            return playerSettings.autoPickupBlocksEnabled() ? enabled : disabled;

        // %MPAutoPickup_ap_mob%
        }else if(identifier.equalsIgnoreCase("ap_mob")){
            return playerSettings.autoPickupMobDropsEnabled() ? enabled : disabled;

        // %MPAutoPickup_ap_exp%
        }else if(identifier.equalsIgnoreCase("ap_exp")){
            return playerSettings.autoPickupExpEnabled() ? enabled : disabled;

        // %MPAutoPickup_as_block%
        }else if(identifier.equalsIgnoreCase("as_block")){
            return playerSettings.autoSmeltBlocksEnabled() ? enabled : disabled;

        // %MPAutoPickup_as_mob%
        }else if(identifier.equalsIgnoreCase("as_mob")){
            return playerSettings.autoSmeltMobEnabled() ? enabled : disabled;
        }

        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
        return null;
    }


}