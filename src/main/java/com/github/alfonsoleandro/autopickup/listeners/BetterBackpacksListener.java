package com.github.alfonsoleandro.autopickup.listeners;

import com.alonsoaliaga.betterbackpacks.api.events.BackpackOpenEvent;
import com.alonsoaliaga.betterbackpacks.others.BackpackHolder;
import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupManager;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class BetterBackpacksListener implements Listener {

    private final AutoPickup plugin;
    private final AutoPickupManager apm;

    public BetterBackpacksListener(AutoPickup plugin){
        this.plugin = plugin;
        this.apm = plugin.getAutoPickupManager();
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBackpackOpen(BackpackOpenEvent event){
        if(!this.plugin.getSettings().isBetterBackpacksSupport()) return;
        if(event.isCancelled()) return;
        Player player = event.getPlayer();
        if(!this.apm.getPlayer(player).autoPickupBlocksEnabled()) return;

        new BukkitRunnable(){
            public void run(){
                if(!player.getCanPickupItems()) return;
                Inventory inv = player.getOpenInventory().getTopInventory();
                InventoryHolder holder = inv.getHolder();

                if(!(holder instanceof BackpackHolder)) return;
                if(((BackpackHolder)holder).getType() != 3) return;

                List<Entity> items = player.getNearbyEntities(1.1, 1.1, 1.1).stream()
                        .filter(e -> e instanceof Item).collect(Collectors.toList());

                for(Entity entity : items){
                    Item item = (Item)entity;
                    if(!canAdd(inv, item.getItemStack())) continue;

                    InventoryPickupItemEvent iPItemEvent = new InventoryPickupItemEvent(inv, item);
                    Bukkit.getPluginManager().callEvent(iPItemEvent);

                    if(iPItemEvent.isCancelled()) continue;
                    inv.addItem(item.getItemStack());
                    item.remove();
                }
            }

        }.runTask(this.plugin);
    }

    /**
     * Checks to see if a given inventory can be added a given item.
     * @param inv The inventory to check for space to add the item.
     * @param item The item to add.
     * @return true if the inventory can contain the item.
     */
    private boolean canAdd(Inventory inv, ItemStack item){
        if(inv.firstEmpty() != -1) return true;

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack it = inv.getItem(i);
            if(it == null) return true;
            if(it.isSimilar(item) && (it.getAmount() + item.getAmount() < it.getMaxStackSize())) return true;
        }
        return false;
    }
}
