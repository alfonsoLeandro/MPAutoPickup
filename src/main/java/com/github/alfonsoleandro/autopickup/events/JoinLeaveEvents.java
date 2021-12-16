package com.github.alfonsoleandro.autopickup.events;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.utils.Message;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveEvents implements Listener {

    private final AutoPickup plugin;
    private final MessageSender<Message> messageSender;

    public JoinLeaveEvents(AutoPickup plugin){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        this.plugin.getAutoPickupManager().loadPlayer(player.getName());

        if(player.isOp() && !this.plugin.getLatestVersion().equalsIgnoreCase(this.plugin.getVersion())){
            String exclamation = "&e&l(&4&l!&e&l)";
            this.messageSender.send(player, exclamation +" &4New version available &7(&e"+ this.plugin.getLatestVersion()+"&7)");
            this.messageSender.send(player, exclamation +" &ehttp://bit.ly/autopickupUpdate");
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        this.plugin.getAutoPickupManager().savePlayer(event.getPlayer().getName(), true);
    }

}
