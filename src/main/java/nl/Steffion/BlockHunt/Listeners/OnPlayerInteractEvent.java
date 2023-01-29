package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Managers.PermissionsM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class OnPlayerInteractEvent implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        //过滤掉压力板等interaction
        if (event.getAction() == Action.PHYSICAL) return;

        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        //判断手上的物品是圈地法杖
        if (PermissionsM.hasPerm(player, PermissionsC.Permissions.create, Boolean.FALSE)) {
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR && item.getItemMeta().hasDisplayName()) {
                ItemMeta im = item.getItemMeta();
                if (im.getDisplayName().equals(MessageM.replaceAll((String) W.config.get(ConfigC.wandName)))) {
                    Action action = event.getAction();
                    if (event.hasBlock()) {
                        //以上4层if分别确认了玩家有创建竞技场的权限、手上物品名为法杖名、玩家点击了一个方块
                        LocationSerializable location = new LocationSerializable(event.getClickedBlock().getLocation());
                        //玩家左键
                        if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                            event.setCancelled(true);
                            if (W.pos1.get(player) == null ||
                                    !W.pos1.get(player).equals(location)) {
                                MessageM.sendFMessage(
                                        player,
                                        ConfigC.normal_wandSetPosition, "number-1",
                                        "pos-%N(%A" + location.getBlockX() +
                                                "%N, %A" +
                                                location.getBlockY() +
                                                "%N, %A" +
                                                location.getBlockZ() +
                                                "%N)",
                                        "x-" + location.getBlockX(), "y-" +
                                                location.getBlockY(),
                                        "z-" + location.getBlockZ());
                                W.pos1.put(player, location);
                            }
                            //玩家右键
                        } else if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                            event.setCancelled(true);
                            if (W.pos2.get(player) == null ||
                                    !W.pos2.get(player).equals(location)) {
                                MessageM.sendFMessage(
                                        player,
                                        ConfigC.normal_wandSetPosition, "number-2",
                                        "pos-%N(%A" + location.getBlockX() +
                                                "%N, %A" +
                                                location.getBlockY() +
                                                "%N, %A" +
                                                location.getBlockZ() +
                                                "%N)",
                                        "x-" + location.getBlockX(), "y-" +
                                                location.getBlockY(),
                                        "z-" + location.getBlockZ());
                                W.pos2.put(player, location);
                            }
                        }
                    }
                }
            }
        }
        //判断玩家点击牌子,interface Sign可以代表所有类型的牌子
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getState() instanceof Sign)
            //判断确实是插件创建的牌子
            if (SignsHandler.isSign(new LocationSerializable(event.getClickedBlock().getLocation()))) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                if (sign.getLine(1) != null)
                    if (sign.getLine(1)
                            .equals(
                                    MessageM.replaceAll(W.config
                                            .getFile()
                                            .getStringList(
                                                    ConfigC.sign_LEAVE.fileKey)
                                            .get(1)))) {
                        if (PermissionsM.hasPerm(player,
                                PermissionsC.Permissions.joinsign, Boolean.TRUE))
                            ArenaHandler.playerLeaveArena(player, true,
                                    true);
                    } else if (sign.getLine(1).equals(
                            MessageM.replaceAll(W.config
                                    .getFile()
                                    .getStringList(
                                            ConfigC.sign_SHOP.fileKey)
                                    .get(1), new String[0]))) {
                        if (PermissionsM.hasPerm(player,
                                PermissionsC.Permissions.shop, Boolean.TRUE))
                            InventoryHandler.openShop(player);
                    } else {
                        for (Arena arena : W.arenaList) {
                            if (sign.getLines()[1].contains(arena.arenaName) && PermissionsM.hasPerm(player, PermissionsC.Permissions.joinsign, Boolean.TRUE))
                                ArenaHandler.playerJoinArena(player, arena.arenaName);
                        }
                    }
            }
        //如果玩家在游戏中试图和方块交互则取消事件
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK ||
                event.getAction() == Action.LEFT_CLICK_BLOCK) &&
                event.getClickedBlock().getType() != Material.AIR && (
                event.getClickedBlock().getType()
                        .equals(Material.ENCHANTING_TABLE) ||
                        event.getClickedBlock().getType()
                                .equals(Material.CRAFTING_TABLE) ||
                        event.getClickedBlock().getType()
                                .equals(Material.FURNACE) ||
                        event.getClickedBlock().getType()
                                .equals(Material.CHEST) ||
                        event.getClickedBlock().getType()
                                .equals(Material.ANVIL) ||
                        event.getClickedBlock().getType()
                                .equals(Material.ENDER_CHEST) ||
                        event.getClickedBlock().getType()
                                .equals(Material.JUKEBOX) ||
                        block.getFace(block).equals(Material.FIRE)))
            if (W.playerArenaMap.containsKey(player)) event.setCancelled(true);
        //判断是不是打到了躲起来的玩家
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Arena arena = W.playerArenaMap.get(player);
            if (arena != null && arena.seekers.contains(player))
                for (Player pl : arena.playersInArena) {
                    if (W.hiddenLoc.get(pl) != null) {
                        Block pLoc = event.getClickedBlock();
                        Block moveLocBlock = W.hiddenLoc.get(pl).getBlock();
                        if (Helpers.isBlockLocEquals(pLoc.getLocation(),moveLocBlock.getLocation())) {
                            W.moveLoc.put(pl, new Location(pl.getWorld(), 0.0D, 0.0D, 0.0D));
                            pl.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
                            SolidBlockHandler.makePlayerUnsolid(pl);
                        }
                    }
                }
        }
        //遍历每个正在等待或者倒计时的竞技场
        Arena arena = W.playerArenaMap.get(player);
        if (arena != null && arena.playersInArena.contains(player) && (arena.gameState.equals(Arena.ArenaState.WAITING) || arena.gameState.equals(Arena.ArenaState.STARTING))) {
            event.setCancelled(true);
            //主手物品
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType() != Material.AIR && item.getItemMeta().getDisplayName() != null) {
                //检查主手物品是否与blockChooser名称一致
                if (item.getItemMeta().getDisplayName().equals(MessageM.replaceAll((String) W.config.get(ConfigC.shop_blockChooserv1Name)))) {
                    //创建并为玩家打开blockChooser
                    Inventory blockChooser = Bukkit.createInventory(null, 36, MessageM.replaceAll("§r" + W.config.get(ConfigC.shop_blockChooserv1Name)));
                    if (arena.disguiseBlocks != null)
                        for (int i = arena.disguiseBlocks.size(); i > 0; i--)
                            blockChooser.setItem(i - 1, arena.disguiseBlocks.get(i - 1));
                    player.openInventory(blockChooser);
                }

                //打开blockHuntPass
                if (item.getItemMeta().getDisplayName().equals(MessageM.replaceAll((String) W.config.get(ConfigC.shop_BlockHuntPassv2Name)))) {
                    Inventory BlockHuntPass = Bukkit.createInventory(null, 9, MessageM.replaceAll("§r" + W.config.get(ConfigC.shop_BlockHuntPassv2Name)));
                    ItemStack BlockHuntPassSEEKER = new ItemStack(Material.RED_WOOL);
                    ItemMeta BlockHuntPassIM = BlockHuntPassSEEKER.getItemMeta();
                    BlockHuntPassIM.setDisplayName(MessageM.replaceAll("&eSEEKER"));
                    BlockHuntPassSEEKER.setItemMeta(BlockHuntPassIM);
                    BlockHuntPass.setItem(1, BlockHuntPassSEEKER);
                    ItemStack BlockHuntPassRANDOM = new ItemStack(Material.WHITE_WOOL);
                    BlockHuntPassIM.setDisplayName(MessageM.replaceAll("&eRANDOM"));
                    BlockHuntPassRANDOM.setItemMeta(BlockHuntPassIM);
                    BlockHuntPass.setItem(4, BlockHuntPassRANDOM);
                    ItemStack BlockHuntPassHIDER = new ItemStack(Material.BLUE_WOOL);
                    BlockHuntPassIM.setDisplayName(MessageM.replaceAll("&eHIDER"));
                    BlockHuntPassHIDER.setItemMeta(BlockHuntPassIM);
                    BlockHuntPass.setItem(7, BlockHuntPassHIDER);
                    player.openInventory(BlockHuntPass);
                    return;
                }

                //打开两个职业的kit选择器
                if (item.getItemMeta().getDisplayName().equals(MessageM.replaceAll((String) W.config.get(ConfigC.shop_KitSelectorHiderName)))) {
                    Inventory inv = Bukkit.createInventory(null, 54, MessageM.replaceAll("§r" + W.config.get(ConfigC.shop_KitSelectorHiderName)));
                    putKitIconsInMenu(inv,false,player);
                    player.openInventory(inv);
                    return;
                }
                if (item.getItemMeta().getDisplayName().equals(MessageM.replaceAll((String) W.config.get(ConfigC.shop_KitSelectorSeekerName)))) {
                    Inventory inv = Bukkit.createInventory(null, 54, MessageM.replaceAll("§r" + W.config.get(ConfigC.shop_KitSelectorSeekerName)));
                    putKitIconsInMenu(inv,true,player);
                    player.openInventory(inv);
                    return;
                }
            }
        }

    }

    private void putKitIconsInMenu(Inventory inv,boolean isSeeker,Player player){
        for(Kit kit:KitHandler.kitMap.values()){
            if(kit.isSeekerKit != isSeeker) continue;
            ItemStack stack = Helpers.getItemStack(kit.icon,kit.lore,kit.name);
            ItemMeta im = stack.getItemMeta();
            im.getPersistentDataContainer().set(KitHandler.kitMenuIconKey, PersistentDataType.STRING,kit.name);
            stack.setItemMeta(im);
            inv.setItem(kit.position,stack);
        }
    }
}