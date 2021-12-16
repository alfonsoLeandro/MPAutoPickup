package com.github.alfonsoleandro.autopickup.events;

import com.alonsoaliaga.betterbackpacks.api.events.BackpackOpenEvent;
import com.alonsoaliaga.betterbackpacks.others.BackpackHolder;
import com.github.alfonsoleandro.autopickup.AutoPickup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class BetterBackpacksListener implements Listener {

    private final AutoPickup plugin;

    public BetterBackpacksListener(AutoPickup plugin){
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBackpackOpen(BackpackOpenEvent event){
        Bukkit.broadcastMessage("ABRIDO");
        if(event.isCancelled()) return;
        Player player = event.getPlayer();

        new BukkitRunnable(){
            public void run(){
                if(!player.getCanPickupItems()) return;
                Inventory inv = player.getOpenInventory().getTopInventory();
                InventoryHolder holder = inv.getHolder();

                if(!(holder instanceof BackpackHolder)) return;
                if(((BackpackHolder)holder).getType() != 3) return;

                List<Entity> items = player.getNearbyEntities(1.5,2,1.5).stream()
                        .filter(e -> e instanceof Item)
                        .collect(Collectors.toList());

                for(Entity entity : items){
                    Item item = (Item)entity;
                    InventoryPickupItemEvent iPItemEvent;

                    Bukkit.getPluginManager().callEvent(iPItemEvent =
                            new InventoryPickupItemEvent(inv, item));

                    if(iPItemEvent.isCancelled()) continue;
                    inv.addItem(item.getItemStack());
                }
            }

        }.runTask(this.plugin);
    }
}
