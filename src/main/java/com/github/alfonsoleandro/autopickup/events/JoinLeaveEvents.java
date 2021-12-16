package com.github.alfonsoleandro.autopickup.events;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvents implements Listener {

    private final AutoPickup plugin;

    public JoinLeaveEvents(AutoPickup plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        plugin.getAutoPickupManager().loadPlayer(player.getName());

        if(player.isOp() && !plugin.getLatestVersion().equalsIgnoreCase(plugin.getVersion())){
            String exclamation = "&e&l(&4&l!&e&l)";
            String prefix = plugin.getConfig().getString("config.prefix");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" "+exclamation+" &4New version available &7(&e"+plugin.getLatestVersion()+"&7)"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+" "+exclamation+" &ehttp://bit.ly/autopickupUpdate") );
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        plugin.getAutoPickupManager().savePlayer(event.getPlayer().getName(), true);
    }

}
