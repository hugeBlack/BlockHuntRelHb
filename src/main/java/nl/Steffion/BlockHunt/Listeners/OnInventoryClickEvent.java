package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.InventoryHandler;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class OnInventoryClickEvent implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();
        for (Arena arena : W.arenaList) {
            if (arena.playersInArena.contains(player) && !arena.seekers.contains(player) && (event.getSlot() == 8 || event.getSlot() == 39))
                event.setCancelled(true);
        }
        Inventory inv = event.getInventory();
        //判断物品栏是虚拟的
        if (inv.getType().equals(InventoryType.CHEST) && inv.getHolder() == null) {
            //配置伪装方块界面
            if (event.getView().getTitle().contains("DisguiseBlocks")) {
                //玩家放入非方块就提醒玩家这不是个方块，花盆被归为非方块但是可以作为伪装
                if (event.getCurrentItem() != null && !event.getCurrentItem().getType().isBlock() && event.getCurrentItem().getType() != Material.FLOWER_POT) {
                    event.setCancelled(true);
                    MessageM.sendFMessage(player, ConfigC.error_setNotABlock);
                }
                return;
            }
            //认为$r开头的可能是其他界面
            if (event.getView().getTitle().startsWith("§r")) {
                event.setCancelled(true);
                if (event.getView().getTitle().equals(MessageM.replaceAll("§r" + W.config.get(ConfigC.shop_title))))
                    ShopHandler(event);
                else if (event.getView().getTitle().contains(MessageM.replaceAll((String) W.config.get(ConfigC.shop_BlockHuntPassv2Name))))
                    BlockHuntPassHandler(event);
                else if (event.getView().getTitle().contains(MessageM.replaceAll((String) W.config.get(ConfigC.shop_blockChooserv1Name))))
                    BlockChooserHandler(event);
                else
                    ArenaManageHandler(event);
            }
        }
    }

    public void save(Arena arena) {
        W.arenas.getFile().set(arena.arenaName, arena);
        W.arenas.save();
        ArenaHandler.loadArenas();
    }

    public static void updownButton(Player player, ItemStack item, Arena arena, Arena.ArenaType at, int option, int max, int min, int add, int remove) {
        if (item.getItemMeta().getDisplayName()
                .contains((String) W.messages.get(ConfigC.button_add2))) {
            if (option < max) {
                switch (at) {
                    case maxPlayers:
                        arena.maxPlayers = option + add;
                        break;
                    case minPlayers:
                        arena.minPlayers = option + add;
                        break;
                    case timeInLobbyUntilStart:
                        arena.timeInLobbyUntilStart = option + add;
                        break;
                    case waitingTimeSeeker:
                        arena.waitingTimeSeeker = option + add;
                        break;
                    case gameTime:
                        arena.gameTime = option + add;
                        break;
                    case timeUntilHidersSword:
                        arena.timeUntilHidersSword = option + add;
                        break;
                    case hidersTokenWin:
                        arena.hidersTokenWin = option + add;
                        break;
                    case seekersTokenWin:
                        arena.seekersTokenWin = option + add;
                        break;
                    case killTokens:
                        arena.killTokens = option + add;
                        break;
                    default:
                        arena.amountSeekersOnStart = option + add;
                        break;
                }
            } else {
                MessageM.sendFMessage(player, ConfigC.error_setTooHighNumber, "max-" + max);
            }
        } else if (item.getItemMeta().getDisplayName()
                .contains((String) W.messages.get(ConfigC.button_remove2))) {
            if (option > min) {
                switch (at) {
                    case maxPlayers:
                        arena.maxPlayers = option - remove;
                        break;
                    case minPlayers:
                        arena.minPlayers = option - remove;
                        break;
                    case timeInLobbyUntilStart:
                        arena.timeInLobbyUntilStart = option - remove;
                        break;
                    case waitingTimeSeeker:
                        arena.waitingTimeSeeker = option - remove;
                        break;
                    case gameTime:
                        arena.gameTime = option - remove;
                        break;
                    case timeUntilHidersSword:
                        arena.timeUntilHidersSword = option - remove;
                        break;
                    case hidersTokenWin:
                        arena.hidersTokenWin = option - remove;
                        break;
                    case seekersTokenWin:
                        arena.seekersTokenWin = option - remove;
                        break;
                    case killTokens:
                        arena.killTokens = option - remove;
                        break;
                    default:
                        arena.amountSeekersOnStart = option - remove;
                        break;
                }
            } else {
                MessageM.sendFMessage(player, ConfigC.error_setTooLowNumber, "min-" + min);
            }
        }
    }

    /**
     * 处理点击BlockHuntPass界面的情况
     */
    private void BlockHuntPassHandler(InventoryClickEvent event){
        Inventory inv = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        int i = 0;
        Arena nowArena = null;
        //先找到玩家所处的竞技场
        for (Arena arena : W.arenaList)
            if (arena.playersInArena.contains(player)) {
                nowArena = arena;
                break;
            }
        //玩家不在竞技场就关闭菜单
        if (nowArena == null) {
            inv.clear();
            player.closeInventory();
            return;
        }
        if (event.getCurrentItem().getType() == Material.BLUE_WOOL) {
            //点击hiders物品，蓝色羊毛
            //已经是hider就直接返回
            if (W.choosenSeeker.containsKey(player) && !W.choosenSeeker.get(player)){
                MessageM.sendFMessage(player, ConfigC.normal_shopAlreadyHiders);
                return;
            }
            //满足条件，可以加入hiders阵营
            W.choosenSeeker.put(player, Boolean.FALSE);
            MessageM.sendFMessage(player, ConfigC.normal_shopChoosenHiders);
        } else if (event.getCurrentItem().getType() == Material.RED_WOOL) {
            //点击seekers物品，红色羊毛
            //已经是seeker就直接返回
            if (W.choosenSeeker.containsKey(player) && W.choosenSeeker.get(player)){
                MessageM.sendFMessage(player, ConfigC.normal_shopAlreadySeeker);
                return;
            }
            //满足条件，可以加入seeker阵营
            W.choosenSeeker.put(player, Boolean.TRUE);
            MessageM.sendFMessage(player, ConfigC.normal_shopChoosenSeeker);
        } else if (event.getCurrentItem().getType() == Material.WHITE_WOOL){
            //选择中立状态
            W.choosenSeeker.remove(player);
            MessageM.sendFMessage(player, ConfigC.normal_shopChooseCancel);
        }
        player.closeInventory();
        /*if (isPassUsed) {
            //扫尾，扣掉一张pass
            inv.clear();
            if (W.shop.getFile()
                    .getInt(player.getName() +
                            ".blockhuntpass") == 1) {
                W.shop.getFile().set(
                        player.getName() +
                                ".blockhuntpass",
                        null);
                W.shop.save();
            }
            W.shop.getFile()
                    .set(player.getName() +
                                    ".blockhuntpass",
                            W.shop.getFile()
                                    .getInt(
                                            player.getName() +
                                                    ".blockhuntpass") - 1);
            W.shop.save();
        }*/


    }

    /**
     * 处理点击商店界面的情况
     */
    private void ShopHandler(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        //当前点击的物品
        ItemStack item = event.getCurrentItem();
        //给新玩家设置token初始值
        if (W.shop.getFile().get(player.getName() + ".tokens") == null) {
            W.shop.getFile().set(player.getName() + ".tokens", 0);
            W.shop.save();
        }
        //获取玩家token
        int playerTokens = W.shop.getFile().getInt(player.getName() + ".tokens");
        if (item == null || item.getItemMeta() == null)
            return;
        if (item.getType().equals(Material.AIR))
            return;
        if (!item.getItemMeta().hasDisplayName())
            return;
        if (item.getItemMeta().getDisplayName().equals(MessageM.replaceAll(W.config.get(ConfigC.shop_blockChooserv1Name).toString()))) {
            if (W.config.getFile().getBoolean("vaultSupport")) {
                if (BlockHunt.econ != null) {
                    int vaultBalance = (int) BlockHunt.econ.getBalance(player.getName());
                    if (vaultBalance >= W.config.getFile().getInt("blockChooserPrice")) {
                        W.shop.getFile().set(player.getName() + ".blockchooser", Boolean.TRUE);
                        BlockHunt.econ.depositPlayer(player.getName(), W.config.getFile().getInt("blockChooserPrice"));
                        W.shop.save();
                        MessageM.sendFMessage(player, ConfigC.normal_shopBoughtItem, "itemname-" + W.config.get(ConfigC.shop_blockChooserv1Name));
                    } else {
                        MessageM.sendFMessage(player, ConfigC.error_shopNeedMoreMoney);
                    }
                }
            } else if (playerTokens >= (Integer) W.config.get(ConfigC.shop_blockChooserv1Price)) {
                W.shop.getFile().set(player.getName() + ".blockchooser", Boolean.TRUE);
                W.shop.getFile().set(player.getName() + ".tokens", playerTokens - W.config.getFile().getInt("blockChooserPrice"));
                W.shop.save();
                MessageM.sendFMessage(player, ConfigC.normal_shopBoughtItem, "itemname-" + W.config.get(ConfigC.shop_blockChooserv1Name));
            } else {
                MessageM.sendFMessage(player, ConfigC.error_shopNeedMoreTokens);
            }
        } else if (item
                .getItemMeta()
                .getDisplayName()
                .equals(MessageM.replaceAll(W.config.get(ConfigC.shop_BlockHuntPassv2Name).toString()))) {
            if (W.config.getFile().getBoolean("vaultSupport")) {
                if (BlockHunt.econ != null) {
                    int vaultBalance = (int) BlockHunt.econ.getBalance(player.getName());
                    if (vaultBalance >= W.config.getFile().getInt("seekerHiderPrice")) {
                        if (W.shop.getFile().get(player.getName() + ".blockhuntpass") == null) {
                            W.shop.getFile().set(player.getName() + ".blockhuntpass", 0);
                            W.shop.save();
                        }
                        W.shop.getFile().set(player.getName() + ".blockhuntpass", (Integer) W.shop.getFile().get(player.getName() + ".blockhuntpass") + 1);
                        BlockHunt.econ.depositPlayer(player.getName(), W.config.getFile().getInt("seekerHiderPrice"));
                        W.shop.save();
                        MessageM.sendFMessage(player, ConfigC.normal_shopBoughtItem, "itemname-" + W.config.get(ConfigC.shop_BlockHuntPassv2Name));
                    } else {
                        MessageM.sendFMessage(player, ConfigC.error_shopNeedMoreMoney);
                    }
                }
            } else if (playerTokens >= (Integer) W.config
                    .get(ConfigC.shop_BlockHuntPassv2Price)) {
                if (W.shop.getFile().get(player.getName() + ".blockhuntpass") == null) {
                    W.shop.getFile().set(player.getName() + ".blockhuntpass", 0);
                    W.shop.save();
                }
                W.shop.getFile().set(player.getName() + ".blockhuntpass", (Integer) W.shop.getFile().get(player.getName() + ".blockhuntpass") + 1);
                W.shop.getFile().set(player.getName() + ".tokens", playerTokens - W.config.getFile().getInt("seekerHiderPrice"));
                W.shop.save();
                MessageM.sendFMessage(player, ConfigC.normal_shopBoughtItem, "itemname-" + W.config.get(ConfigC.shop_BlockHuntPassv2Name));
            } else {
                MessageM.sendFMessage(player, ConfigC.error_shopNeedMoreTokens);
            }
        }
        InventoryHandler.openShop(player);
    }

    /**
     * 处理点击方块选择器界面的情况
     */
    private void BlockChooserHandler(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem().getType() != Material.AIR)
            if (event.getCurrentItem().getType().isBlock()) {
                W.choosenBlock.put(player, event.getCurrentItem());
                MessageM.sendFMessage(player,
                        ConfigC.normal_shopChoosenBlock, "block-" +
                                event.getCurrentItem().getType()
                                        .toString()
                                        .replaceAll("_", "")
                                        .replaceAll("BLOCK", "")
                                        .toLowerCase());
            } else {
                MessageM.sendFMessage(player,
                        ConfigC.error_setNotABlock);
            }
    }

    /**
     * 处理点击jjc管理界面的情况
     */
    private void ArenaManageHandler(InventoryClickEvent event){
        Inventory inv = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        String arenaName = Objects.requireNonNull(inv
                        .getItem(0))
                .getItemMeta()
                .getDisplayName()
                .replaceAll(
                        MessageM.replaceAll("%NSettings of arena: %A"), "");
        Arena arena = null;
        for (Arena arena2 : W.arenaList) {
            if (arena2.arenaName.equalsIgnoreCase(arenaName))
                arena = arena2;
        }
        if (item == null || arena == null)
            return;
        if (item.getType().equals(Material.AIR))
            return;
        if (!item.getItemMeta().hasDisplayName())
            return;
        if (item.getType().equals(Material.GOLD_NUGGET)) {
            if (item.getItemMeta().getDisplayName()
                    .contains("maxPlayers")) {
                updownButton(player, item, arena,
                        Arena.ArenaType.maxPlayers, arena.maxPlayers,
                        Bukkit.getMaxPlayers(), 2, 1, 1);
            } else if (item.getItemMeta().getDisplayName()
                    .contains("minPlayers")) {
                updownButton(player, item, arena,
                        Arena.ArenaType.minPlayers, arena.minPlayers,
                        Bukkit.getMaxPlayers() - 1, 2, 1, 1);
            } else if (item.getItemMeta().getDisplayName()
                    .contains("amountSeekersOnStart")) {
                updownButton(player, item, arena,
                        Arena.ArenaType.amountSeekersOnStart,
                        arena.amountSeekersOnStart,
                        arena.maxPlayers - 1, 1, 1, 1);
            } else if (item.getItemMeta().getDisplayName()
                    .contains("timeInLobbyUntilStart")) {
                updownButton(player, item, arena,
                        Arena.ArenaType.timeInLobbyUntilStart,
                        arena.timeInLobbyUntilStart, 1000, 5, 1, 1);
            } else if (item.getItemMeta().getDisplayName()
                    .contains("waitingTimeSeeker")) {
                updownButton(player, item, arena,
                        Arena.ArenaType.waitingTimeSeeker,
                        arena.waitingTimeSeeker, 1000, 5, 1, 1);
            } else if (item.getItemMeta().getDisplayName()
                    .contains("gameTime")) {
                updownButton(player, item, arena,
                        Arena.ArenaType.gameTime, arena.gameTime, 1000,
                        5, 1, 1);
            } else if (item.getItemMeta().getDisplayName()
                    .contains("timeUntilHidersSword")) {
                updownButton(player, item, arena,
                        Arena.ArenaType.timeUntilHidersSword,
                        arena.timeUntilHidersSword, 1000, 0, 1, 1);
            } else if (item.getItemMeta().getDisplayName()
                    .contains("hidersTokenWin")) {
                updownButton(player, item, arena,
                        Arena.ArenaType.hidersTokenWin,
                        arena.hidersTokenWin, 1000, 0, 1, 1);
            } else if (item.getItemMeta().getDisplayName()
                    .contains("seekersTokenWin")) {
                updownButton(player, item, arena,
                        Arena.ArenaType.seekersTokenWin,
                        arena.seekersTokenWin, 1000, 0, 1, 1);
            } else if (item.getItemMeta().getDisplayName()
                    .contains("killTokens")) {
                updownButton(player, item, arena,
                        Arena.ArenaType.killTokens, arena.killTokens,
                        1000, 0, 1, 1);
            }
            save(arena);
            InventoryHandler.openPanel(player, arena.arenaName);
        } else if (item.getType().equals(Material.BOOK) &&
                item.getItemMeta().getDisplayName()
                        .contains("disguiseBlocks")) {
            InventoryHandler.openDisguiseBlocks(arena, player);
        }
    }
}