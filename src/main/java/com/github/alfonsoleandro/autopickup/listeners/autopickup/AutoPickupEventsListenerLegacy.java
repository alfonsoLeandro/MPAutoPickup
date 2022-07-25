package com.github.alfonsoleandro.autopickup.listeners.autopickup;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AutoPickupEventsListenerLegacy extends AbstractAutoPickupEventsListener {

    public AutoPickupEventsListenerLegacy(AutoPickup plugin, int serverVersionDiscriminant){
        super(plugin, serverVersionDiscriminant);
    }

    /**
     * Returns the drops passed by parameter directly, as there is not BlockDropItemEvent in
     * versions earlier than 1.13.
     * @param block The block that caused the event.
     * @param player The player breaking the block.
     * @param drops The list of dropped items.
     */
    @Override
    protected List<ItemStack> triggerBlockDropItemEvent(Block block, Player player, List<ItemStack> drops){
        return drops;
    }

}
