package com.github.alfonsoleandro.autopickup.listeners.autopickup;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class AutoPickupEventsListener extends AbstractAutoPickupEventsListener {

    public AutoPickupEventsListener(AutoPickup plugin, int serverVersionDiscriminant){
        super(plugin, serverVersionDiscriminant);
    }

    /**
     * Calls the {@link BlockDropItemEvent} that was going to be called by the {@link BlockBreakEvent} but wasn't,
     * because the block break was cancelled, or items were not dropped.
     * @param block The block that caused the event.
     * @param player The player breaking the block.
     * @param drops The list of dropped items.
     */
    @Override
    protected List<ItemStack> triggerBlockDropItemEvent(Block block, Player player, List<ItemStack> drops){
        List<Item> items = new ArrayList<>();
        for(ItemStack drop : drops){
            if(drop.getType().isAir()) continue;
            Item dropped = block.getWorld().dropItem(block.getLocation(), drop);
            dropped.remove();
            items.add(dropped);
        }
        if(items.isEmpty()) return drops;
        BlockDropItemEvent blockDropItemEvent = new BlockDropItemEvent(block, block.getState(), player, items);
        Bukkit.getPluginManager().callEvent(blockDropItemEvent);
        return blockDropItemEvent.getItems().stream().map(Item::getItemStack).collect(Collectors.toList());
    }

}
