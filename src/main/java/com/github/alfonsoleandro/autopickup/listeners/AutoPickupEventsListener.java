package com.github.alfonsoleandro.autopickup.listeners;

import com.github.alfonsoleandro.autopickup.AutoPickup;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupManager;
import com.github.alfonsoleandro.autopickup.managers.AutoPickupSettings;
import com.github.alfonsoleandro.autopickup.utils.Message;
import com.github.alfonsoleandro.autopickup.utils.Settings;
import com.github.alfonsoleandro.autopickup.utils.SoundSettings;
import com.github.alfonsoleandro.mputils.managers.MessageSender;
import com.vk2gpz.vkbackpack.VKBackPack;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Snow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AutoPickupEventsListener implements Listener, EventExecutor {

    private final Random r = new Random();
    private final Set<String> alertedPlayers = new HashSet<>();
    private final Set<Material> materialsWithoutStatistics = new HashSet<>();

    private final AutoPickup plugin;
    private final MessageSender<Message> messageSender;
    private final AutoPickupManager apm;
    private final Settings settings;
    private final int serverVersionDiscriminant;


    public AutoPickupEventsListener(AutoPickup plugin, int serverVersionDiscriminant){
        this.plugin = plugin;
        this.messageSender = plugin.getMessageSender();
        this.apm = plugin.getAutoPickupManager();
        this.settings = plugin.getSettings();
        this.serverVersionDiscriminant = serverVersionDiscriminant;
    }


    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(!player.getGameMode().equals(GameMode.SURVIVAL)) return;
        if(this.serverVersionDiscriminant > 11 && !event.isDropItems()) return;
        if(this.settings.getBlockBlackList().contains(event.getBlock().getType())) return;


        AutoPickupSettings playerSettings = this.apm.getPlayer(player);
        Block block = event.getBlock();
        Collection<ItemStack> drops;
        ItemStack inHand = this.serverVersionDiscriminant > 8 ?
                event.getPlayer().getInventory().getItemInMainHand() :
                event.getPlayer().getItemInHand();


        //Apply enchantments
        if(this.settings.isUseVanillaEnchantments()){
            // Trigger the event that would trigger if AutoPickup weren't enabled on the server
            drops = triggerBlockDropItemEvent(block, player, block.getDrops(inHand).stream().toList());
        }else{
            // Trigger the event that would trigger if AutoPickup weren't enabled on the server
            drops = triggerBlockDropItemEvent(block, player, block.getDrops(new ItemStack(inHand.getType())).stream().toList());
            applySilkTouch(drops, inHand, block);
            applyFortune(drops, inHand);
        }

        // Tested in: 1.8(DONE, no need), 1.10(DONE, NO NEED), 1.11(DONE, NO NEED),
        // 1.12.2(NEEDS), 1.13(NEEDS) and 1.16(NEEDS)
        // Adds adjacent blocks that break when the block they are holding on to breaks, to the dropped items
        if(this.serverVersionDiscriminant > 11) {
            addAdjacentBlocks(drops, block);
        }

        //Check for blocks that have two parts.
        if(block.getDrops().isEmpty() &&
                (block.getType().toString().contains("DOOR") || block.getType().toString().contains("BED"))){
            drops.add(new ItemStack(block.getType()));
        }

        //Check if block has items in it.
        if(block.getState() instanceof InventoryHolder && !(block.getType().toString().contains("SHULKER"))) {
            Inventory blockInv = ((InventoryHolder) block.getState()).getInventory();
            drops.addAll(Arrays.stream(blockInv.getContents()).filter(Objects::nonNull).toList());
            blockInv.clear();
            block.getState().update();
        }

        //Check if block is snow layer(s)
        if(drops.isEmpty() && block.getType().equals(Material.SNOW)){
            int layers = this.serverVersionDiscriminant >= 13 ?
                    ((Snow)block.getBlockData()).getLayers()
                    :
                    block.getData()+1;
            drops.add(new ItemStack(this.serverVersionDiscriminant >= 13 ?
                    Material.SNOWBALL : Material.valueOf("SNOW_BALL"),
                    layers));
        }


        //Apply autoSmelt
        if(playerSettings.autoSmeltBlocksEnabled()
                && (player.isSneaking() ||
                this.settings.isGlobalCarefulSmeltDisabled() ||
                !playerSettings.carefulSmeltEnabled())){
            applyAutoSmelt(drops);
        }

        //Finally, auto pickup or drop items
        if(playerSettings.autoPickupBlocksEnabled() &&
                !this.settings.getBlockBlackList().contains(block.getType())
                && (player.isSneaking() ||
                this.settings.isGlobalCarefulBreakDisabled() ||
                !playerSettings.carefulBreakEnabled())){
            autoPickup(drops, player, Settings.AutoPickupSounds.BLOCKS);
        }else{
            Location loc = event.getBlock().getLocation();
            for (ItemStack item : drops) {
                dropItem(item, loc);
            }
        }

        //AutoPickup experience
        if(playerSettings.autoPickupExpEnabled() && event.getExpToDrop() > 0){
            player.giveExp(event.getExpToDrop());
            event.setExpToDrop(0);
            playSound(player, Settings.AutoPickupSounds.EXP);
        }


        //Prevent non wanted drops and add statistics
        addMineBlockStatistics(player, block.getType());

        if(this.serverVersionDiscriminant > 11) {
            event.setDropItems(false);
        }else {
            event.setCancelled(true);
            event.getBlock().getLocation().getBlock().setType(Material.AIR);
            String type = inHand.getType().toString();
            if(type.contains("SWORD") ||
                    type.contains("AXE") ||
                    type.contains("SHOVEL")
                    || type.equalsIgnoreCase("SHEARS")) {
                //Durability the way Minecraft implements it
                if(!inHand.containsEnchantment(Enchantment.DURABILITY) ||
                        (100/(inHand.getEnchantmentLevel(Enchantment.DURABILITY)+1)) > this.r.nextInt(100)) {
                    inHand.setDurability((short) (inHand.getDurability() + 1));
                }
                if(inHand.getDurability() > inHand.getType().getMaxDurability()) {
                    // "After this event, the item's amount will be set to item amount - 1
                    // and its durability will be reset to 0."
                    PlayerItemBreakEvent itemBreakEvent = new PlayerItemBreakEvent(player, inHand);
                    Bukkit.getPluginManager().callEvent(itemBreakEvent);
                    inHand.setDurability((short) 0);
                    inHand.setAmount(inHand.getAmount() - 1);
                    if(inHand.getAmount() <= 0) {
                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), null);
                    }
                }
            }
        }

    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerKillEntity(EntityDeathEvent event){
        Player player = event.getEntity().getKiller();
        if(player == null || (event.getEntity().getType().equals(EntityType.PLAYER) && !this.settings.isAutoPickupPlayerDrops())) return;

        AutoPickupSettings playerSettings = this.apm.getPlayer(player);

        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        event.getDrops().clear();

        //Apply autoSmelt
        if(playerSettings.autoSmeltMobEnabled()
                && (player.isSneaking() ||
                this.settings.isGlobalCarefulSmeltDisabled() ||
                !playerSettings.carefulSmeltEnabled())){
            applyAutoSmelt(drops);
        }

        //Finally, auto pickup or drop items
        if(playerSettings.autoPickupMobDropsEnabled() &&
                !this.settings.getEntityBlackList().contains(event.getEntityType())
                && (player.isSneaking() ||
                this.settings.isGlobalCarefulBreakDisabled() ||
                !playerSettings.carefulBreakEnabled())){
            autoPickup(drops, player, Settings.AutoPickupSounds.MOBS);
        }else{
            Location loc = event.getEntity().getLocation();
            for (ItemStack item : drops) {
                dropItem(item, loc);
            }
        }

        //AutoPickup experience
        if(playerSettings.autoPickupExpEnabled() && event.getDroppedExp() > 0){
            player.giveExp(event.getDroppedExp());
            event.setDroppedExp(0);
            playSound(player, Settings.AutoPickupSounds.EXP);
        }
    }



    /**
     * Applies silk touch to a block's drops if it h.
     * @param items The original collection of items to drop.
     * @param inHand The item the player breaking the block had equipped.
     * @param block The block being broken.
     */
    private void applySilkTouch(Collection<ItemStack> items, ItemStack inHand, Block block){
        Material blockType = block.getType();
        if(inHand.containsEnchantment(Enchantment.SILK_TOUCH) && this.settings.isCustomSilkTouchEnabled() && this.settings.getSilkTouchMaterials().get(blockType) != null) {
            items.removeAll(block.getDrops(new ItemStack(inHand.getType())));
            items.add(new ItemStack(this.settings.getSilkTouchMaterials().get(blockType)));
        }
    }

    /**
     * Applies fortune to
     * @param items The original collection of items.
     * @param inHand The item the player breaking the block had equipped when breaking the block.
     */
    private void applyFortune(Collection<ItemStack> items, ItemStack inHand){
        if(inHand.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) &&
                this.settings.isCustomFortuneEnabled()) {
            for (ItemStack item : items) {
                if(this.settings.getFortuneMaterials().contains(item.getType())){
                    int toAdd = this.settings.getFortuneAmount(inHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS));
                    item.setAmount(item.getAmount()+toAdd);
                }
            }
        }
    }


    /**
     * Applies auto-smelt to a collection of items.
     * @param items The collection of items to try to auto-smelt.
     */
    private void applyAutoSmelt(Collection<ItemStack> items){
        if(this.settings.isAutoSmeltEnabled()){
            Map<Material,Material> autoSmeltMaterials = this.settings.getAutoSmeltMaterials();
            for(ItemStack drop : items) {
                if(autoSmeltMaterials.containsKey(drop.getType())){
                    drop.setType(autoSmeltMaterials.get(drop.getType()));
                }
            }
        }
    }

    /**
     * Tries to add a collection of items to a given player's inventory.
     * @param items The items to be added.
     * @param player The player to add the items to.
     * @param sound The sound to play if the operation succeeds.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void autoPickup(Collection<ItemStack> items, Player player, Settings.AutoPickupSounds sound){
        PlayerInventory inv = player.getInventory();
        boolean addedAll = true;

        if(items.isEmpty()) return;

        for(ItemStack item : items){
            if(this.settings.isVkBackpacksSupport() && hasEmptyBackPackSpace(item, player)){
                VKBackPack.addItemToBackPack(player, item);
            }else if(hasEmptySpace(item ,inv)){
                if(this.settings.getItemBlackList().contains(item.getType())){
                    dropItem(item, player.getLocation());
                }else {
                    inv.addItem(item);
                }
            }else{
                addedAll = false;
                if(!this.settings.isRemoveItemsWhenFullInv()) dropItem(item, player.getLocation());
            }
        }

        if(addedAll){
            playSound(player, sound);
        }else{
            if(!playerHasBeenAlerted(player)){
                this.messageSender.send(player, this.settings.isRemoveItemsWhenFullInv()
                        ? Message.FULL_INV_ITEMS_REMOVED : Message.FULL_INV);
                playSound(player, Settings.AutoPickupSounds.FULL_INV);
                addAlertedPlayer(player.getName());
            }

        }
    }

    /**
     * Drops an item to the floor, with a natural bounce @see {@link org.bukkit.World#dropItemNaturally(Location, ItemStack)}
     * @param item The item to drop.
     * @param loc The location where the item will be dropped.
     */
    private void dropItem(ItemStack item, Location loc){
        Objects.requireNonNull(loc.getWorld()).dropItemNaturally(loc, item);
    }

    /**
     * Adds some adjacent blocks attached to the broken block to the drops list.
     * For example: torches, ladders, etc.
     * @param drops The list of drops.
     * @param block The broken block.
     */
    @SuppressWarnings("deprecation") //block data in 1.12-
    private void addAdjacentBlocks(Collection<ItemStack> drops, Block block){
        for(BlockFace face : new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN}) {
            Block relative = block.getRelative(face);
            String type = relative.getType().toString();
            if(type.contains("AIR")) continue;
            if(face.equals(BlockFace.UP)) {
                if(type.contains("TORCH") || type.contains("SAPLING") || type.contains("GRASS")
                        || type.contains("FERN") || type.contains("BUSH") || type.contains("PICKLE")
                        || type.contains("DANDELION") || type.contains("POPPY") || type.contains("ORCHID")
                        || type.contains("ALLIUM") || type.contains("BLUET") || type.contains("TULIP")
                        || type.contains("DAISY") || type.contains("LILY") || type.contains("ROSE")
                        || type.contains("RAIL") || type.contains("FLOWER") || type.contains("ROOTS")
                        || type.contains("CARPET") || type.contains("LILAC") || type.contains("PEONY")
                        || type.equalsIgnoreCase("REDSTONE") || type.contains("LEVER")
                        || type.contains("REPEATER") || type.contains("COMPARATOR") || type.contains("BUTTON")
                        || type.contains("PRESSURE") || type.contains("DOOR") || type.contains("BANNER")
                        || type.contains("BELL") || type.contains("LANTERN")) {
                    if(type.contains("WALL")) continue;
                    if(type.contains("BLOCK")) continue;
                    if(type.contains("HANGING")) continue;
                    if(type.contains("TRAPDOOR")) continue;
                    drops.addAll(relative.getDrops());

                }else if(type.contains("CLUSTER")){
                    //It's okay to use block data here because amethysts clusters are only 1.18+
                    if(!((Directional) relative.getBlockData()).getFacing().equals(face)) continue;
                    drops.addAll(relative.getDrops());
                }

            }else if(face.equals(BlockFace.DOWN)){
                if(type.contains("BLOSSOM") || type.contains("HANGING")){
                    drops.addAll(relative.getDrops());
                }

            } else {
                if(type.contains("TORCH") || type.contains("SIGN") || type.contains("BANNER")
                        || type.contains("FAN") || type.contains("LADDER") || type.contains("TRIPWIRE")
                        || type.contains("BUTTON") || type.contains("LEVER")) {
                    if(this.serverVersionDiscriminant < 13){
                        byte data = relative.getData();
                        if(type.contains("TORCH") || type.contains("BUTTON") || type.contains("LEVER")) {
                            switch (face){
                                case EAST:
                                    if(data == 1 || data == 9){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                                case WEST:
                                    if(data == 2 || data == 10){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                                case SOUTH:
                                    if(data == 3 || data == 11){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                                case NORTH:
                                    if(data == 4 || data == 12){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                            }


                        }else if(type.contains("SIGN") || type.contains("BANNER") || type.contains("LADDER")){
                            if(!type.contains("WALL")) continue;
                            switch (face){
                                case EAST:
                                    if(data == 5){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                                case WEST:
                                    if(data == 4){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                                case SOUTH:
                                    if(data == 3){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                                case NORTH:
                                    if(data == 2){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                            }
                        }else if(type.contains("TRIPWIRE")){
                            switch (face){
                                case EAST:
                                    if(data == 3 || data == 7 || data == 15){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                                case WEST:
                                    if(data == 1 || data == 5 || data == 13){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                                case SOUTH:
                                    if(data == 0 || data == 4 || data == 12){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                                case NORTH:
                                    if(data == 2 || data == 6 || data == 14){
                                        drops.addAll(relative.getDrops());
                                    }
                                    break;
                            }

                        }
                    }else {
                        if(!type.contains("WALL") || !((Directional) relative.getBlockData()).getFacing().equals(face))
                            continue;
                        drops.addAll(relative.getDrops());
                    }
                }
            }
        }
    }


    /**
     * Checks if the player has an empty space for a given item to fit in.
     * @param item The item
     * @param inv The inventory to check for a space.
     * @return true if the given item can be added.
     */
    private boolean hasEmptySpace(ItemStack item, PlayerInventory inv){
        if(inv.firstEmpty() != -1) {
            return true;
        }else{
            for(int j = 0; j <= 35 ; j++) {
                ItemStack inJ = inv.getItem(j);
                assert inJ != null;
                if(inJ.isSimilar(item) && inJ.getAmount() < 64) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if the player has an empty space for a given item to fit in.
     * @param item The item
     * @param player The player to check for a backpack space.
     * @return true if the given item can be added.
     */
    @SuppressWarnings("ConstantConditions")
    private boolean hasEmptyBackPackSpace(ItemStack item, Player player){
        if(!VKBackPack.hasBackPack(player)) return false;

        for(Inventory inv : VKBackPack.getAllBackPackInventories(player)) {
            if(inv.firstEmpty() != -1) {
                return true;
            } else {
                for (int j = 0; j < inv.getSize(); j++) {
                    ItemStack inJ = inv.getItem(j);
                    assert inJ != null;
                    if(inJ.isSimilar(item) && inJ.getAmount() < 64) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Adds 1 to the {@link Statistic#MINE_BLOCK} with the given block type.
     * @param player The player to modify the statistic for.
     * @param type The type of the block mined.
     */
    private void addMineBlockStatistics(Player player, Material type){
        if(type.equals(Material.AIR)) return;
        if(this.materialsWithoutStatistics.contains(type)) return;
        try {
            int previous = player.getStatistic(Statistic.MINE_BLOCK, type);
            PlayerStatisticIncrementEvent statisticEvent =
                    new PlayerStatisticIncrementEvent(player, Statistic.MINE_BLOCK,
                            previous, previous + 1, type);
            Bukkit.getPluginManager().callEvent(statisticEvent);
            if(statisticEvent.isCancelled()) return;
            player.setStatistic(Statistic.MINE_BLOCK, type, player.getStatistic(Statistic.MINE_BLOCK, type) + 1);

        }catch (IllegalArgumentException e){
            this.materialsWithoutStatistics.add(type);
        }
    }


    /**
     * Tries to play a sound for a player.
     * @param player The player to play the sound for.
     * @param sound The type of sound to play for the player.
     */
    private void playSound(Player player, Settings.AutoPickupSounds sound){
        SoundSettings ss = this.settings.getSound(sound);
        if(ss != null && ss.isEnabled()){
            player.playSound(player.getLocation(), ss.getSound(),  ss.getVolume(), ss.getPitch());
        }
    }



    private void addAlertedPlayer(String playerName){
        if(this.alertedPlayers.contains(playerName)) return;
        this.alertedPlayers.add(playerName);
        new BukkitRunnable(){
            @Override
            public void run(){
                AutoPickupEventsListener.this.alertedPlayers.remove(playerName);
            }
        }.runTaskLater(this.plugin, this.settings.getTicksBeforeAlert());
    }

    private boolean playerHasBeenAlerted(Player player){
        return this.alertedPlayers.contains(player.getName());
    }

    /**
     * Calls the {@link BlockDropItemEvent} that was going to be called by the {@link BlockBreakEvent} but wasn't,
     * because the block break was cancelled, or items were not dropped.
     * @param block The block that caused the event.
     * @param player The player breaking the block.
     * @param drops The list of dropped items.
     */
    private List<ItemStack> triggerBlockDropItemEvent(Block block, Player player, List<ItemStack> drops){
        List<Item> items = new ArrayList<>();
        for(ItemStack drop : drops){
            Item dropped = block.getWorld().dropItem(block.getLocation(), drop);
            dropped.remove();
            items.add(dropped);
        }
        BlockDropItemEvent blockDropItemEvent = new BlockDropItemEvent(block, block.getState(), player, items);
        Bukkit.getPluginManager().callEvent(blockDropItemEvent);
        return blockDropItemEvent.getItems().stream().map(Item::getItemStack).toList();
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        if(event instanceof BlockBreakEvent && !((BlockBreakEvent) event).isCancelled()) {
            this.onPlayerBreakBlock((BlockBreakEvent) event);
        }else if(event instanceof EntityDeathEvent){
            this.onPlayerKillEntity((EntityDeathEvent) event);
        }

    }
}
