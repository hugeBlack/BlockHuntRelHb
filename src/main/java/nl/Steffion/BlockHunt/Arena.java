package nl.Steffion.BlockHunt;

import java.util.*;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Managers.PermissionsM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import nl.Steffion.BlockHunt.Serializables.M;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

@SerializableAs("BlockHuntArena")
public class Arena implements ConfigurationSerializable {
    public String arenaName;
    public Location pos1;
    public Location pos2;
    public int maxPlayers;
    public int minPlayers;
    public int amountSeekersOnStart;
    public int timeInLobbyUntilStart;
    public int waitingTimeSeeker;
    public int gameTime;
    public int timeUntilHidersSword;
    public ArrayList<ItemStack> disguiseBlocks;
    public LocationSerializable lobbyWarp;
    public LocationSerializable hidersWarp;
    public LocationSerializable seekersWarp;
    public int seekersTokenWin;
    public int hidersTokenWin;
    public int killTokens;
    public HashSet<Player> playersInArena;
    public ArenaState gameState;
    public int timer;
    public HashSet<Player> seekers;
    public Scoreboard scoreboard;
    public HashMap<Player,Integer> seekerTime = new HashMap<>();

    public Arena(String arenaName, LocationSerializable pos1, LocationSerializable pos2, int maxPlayers, int minPlayers, int amountSeekersOnStart, int timeInLobbyUntilStart, int waitingTimeSeeker, int gameTime, int timeUntilHidersSword, ArrayList<ItemStack> disguiseBlocks, LocationSerializable lobbyWarp, LocationSerializable hidersWarp, LocationSerializable seekersWarp, int seekersTokenWin, int hidersTokenWin, int killTokens, ArenaState gameState, Scoreboard scoreboard) {
        this.arenaName = arenaName;
        if(pos1!=null && pos2!=null){
            double maxX = Math.max(pos1.getX(), pos2.getX());
            double minX = Math.min(pos1.getX(), pos2.getX());
            double maxY = Math.max(pos1.getY(), pos2.getY());
            double minY = Math.min(pos1.getY(), pos2.getY());
            double maxZ = Math.max(pos1.getZ(), pos2.getZ());
            double minZ = Math.min(pos1.getZ(), pos2.getZ());
            //pos1是xyz中小的，pos2是xyz中大的
            this.pos1 = new Location(pos1.getWorld(),minX,minY,minZ);
            this.pos2 = new Location(pos2.getWorld(),maxX,maxY,maxZ);
        }

        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.amountSeekersOnStart = amountSeekersOnStart;
        this.timeInLobbyUntilStart = timeInLobbyUntilStart;
        this.waitingTimeSeeker = waitingTimeSeeker;
        this.gameTime = gameTime;
        this.timeUntilHidersSword = timeUntilHidersSword;
        this.disguiseBlocks = disguiseBlocks;
        this.lobbyWarp = lobbyWarp;
        this.hidersWarp = hidersWarp;
        this.seekersWarp = seekersWarp;
        this.seekersTokenWin = seekersTokenWin;
        this.hidersTokenWin = hidersTokenWin;
        this.killTokens = killTokens;
        this.gameState = gameState;
        this.timer = 0;
        this.scoreboard = scoreboard;
        this.seekers = new HashSet<>();
        this.playersInArena = new HashSet<>();
    }

    public enum ArenaType {
        maxPlayers, minPlayers, amountSeekersOnStart, timeInLobbyUntilStart, waitingTimeSeeker, gameTime, timeUntilHidersSword, hidersTokenWin, seekersTokenWin, killTokens;
    }

    public enum ArenaState {
        WAITING, STARTING, INGAME, RESTARTING, DISABLED;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("arenaName", this.arenaName);
        map.put("pos1", this.pos1);
        map.put("pos2", this.pos2);
        map.put("maxPlayers", this.maxPlayers);
        map.put("minPlayers", this.minPlayers);
        map.put("amountSeekersOnStart", this.amountSeekersOnStart);
        map.put("timeInLobbyUntilStart", this.timeInLobbyUntilStart);
        map.put("waitingTimeSeeker", this.waitingTimeSeeker);
        map.put("gameTime", this.gameTime);
        map.put("timeUntilHidersSword", this.timeUntilHidersSword);
        map.put("disguiseBlocks", this.disguiseBlocks);
        map.put("lobbyWarp", this.lobbyWarp);
        map.put("hidersWarp", this.hidersWarp);
        map.put("seekersWarp", this.seekersWarp);
        map.put("seekersTokenWin", this.seekersTokenWin);
        map.put("hidersTokenWin", this.hidersTokenWin);
        map.put("killTokens", this.killTokens);
        return map;
    }

    public static Arena deserialize(Map<String, Object> map) {
        //默认位置
        LocationSerializable loc = new LocationSerializable(Bukkit.getWorld("world"), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        return new Arena((String) M.g(map, "arenaName", "UNKNOWN_NAME"),
                (LocationSerializable) M.g(map, "pos1", loc),
                (LocationSerializable) M.g(map, "pos2", loc),
                (Integer) M.g(map, "maxPlayers", 12),
                (Integer) M.g(map, "minPlayers", 3),
                (Integer) M.g(map, "amountSeekersOnStart", 1),
                (Integer) M.g(map, "timeInLobbyUntilStart", 90),
                (Integer) M.g(map, "waitingTimeSeeker", 20),
                (Integer) M.g(map, "gameTime", 200),
                (Integer) M.g(map, "timeUntilHidersSword", 30),
                (ArrayList<ItemStack>) M.g(map, "disguiseBlocks", new ArrayList()),
                (LocationSerializable) M.g(map, "lobbyWarp", loc),
                (LocationSerializable) M.g(map, "hidersWarp", loc),
                (LocationSerializable) M.g(map, "seekersWarp", loc),
                (Integer) M.g(map, "seekersTokenWin", 10),
                (Integer) M.g(map, "hidersTokenWin", 50),
                (Integer) M.g(map, "killTokens", 8),
                ArenaState.WAITING,
                Bukkit.getScoreboardManager().getNewScoreboard());
    }

    /**
     * 检查x使否在[min{a,b},max{a,b}]间
     */
    public boolean isLocationInArena(Location location){
        return location.getWorld() == pos1.getWorld() && location.getX()>=pos1.getX() && location.getY()>=pos1.getY() && location.getZ()>=pos1.getZ() && location.getX()<=pos2.getX() && location.getY()<=pos2.getY()&& location.getZ()<=pos2.getZ();
    }

    private void sendGameStartCountDownMsg() {
        //处理倒计时应该显示的信息
        if (timer > 0) {
            if (timer == 60) {
                sendArenaMessage(ConfigC.normal_lobbyArenaIsStarting, "1-60");
            } else if (timer == 30) {
                sendArenaMessage(ConfigC.normal_lobbyArenaIsStarting, "1-30");
            } else if (timer == 10) {
                sendArenaMessage(ConfigC.normal_lobbyArenaIsStarting, "1-10");
            } else if (timer == 5) {
                for (Player pl : playersInArena)
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.0F);
                sendArenaMessage(ConfigC.normal_lobbyArenaIsStarting, "1-5");
            } else if (timer == 4) {
                for (Player pl : playersInArena)
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.0F);
                sendArenaMessage(ConfigC.normal_lobbyArenaIsStarting, "1-4");
            } else if (timer == 3) {
                for (Player pl : playersInArena)
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                sendArenaMessage(ConfigC.normal_lobbyArenaIsStarting, "1-3");
            } else if (timer == 2) {
                for (Player pl : playersInArena)
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                sendArenaMessage(ConfigC.normal_lobbyArenaIsStarting, "1-2");
            } else if (timer == 1) {
                for (Player pl : playersInArena)
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 2.0F);
                sendArenaMessage(ConfigC.normal_lobbyArenaIsStarting, "1-1");
            }
        }
    }

    private void sendGameEndCountDownMsg() {
        if (timer == 190) {
            sendArenaMessage(ConfigC.normal_ingameArenaEnd, "1-190");
        } else if (timer == 60) {
            sendArenaMessage(ConfigC.normal_ingameArenaEnd, "1-60");
        } else if (timer == 30) {
            sendArenaMessage(ConfigC.normal_ingameArenaEnd, "1-30");
        } else if (timer == 10) {
            sendArenaMessage(ConfigC.normal_ingameArenaEnd, "1-10");
        } else if (timer == 5) {
            lobbyWarp.getWorld().playSound(lobbyWarp, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.0F);
            sendArenaMessage(ConfigC.normal_ingameArenaEnd, "1-5");
        } else if (timer == 4) {
            lobbyWarp.getWorld().playSound(lobbyWarp, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.0F);
            sendArenaMessage(ConfigC.normal_ingameArenaEnd, "1-4");
        } else if (timer == 3) {
            lobbyWarp.getWorld().playSound(lobbyWarp, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            sendArenaMessage(ConfigC.normal_ingameArenaEnd, "1-3");
        } else if (timer == 2) {
            lobbyWarp.getWorld().playSound(lobbyWarp, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            sendArenaMessage(ConfigC.normal_ingameArenaEnd, "1-2");
        } else if (timer == 1) {
            lobbyWarp.getWorld().playSound(lobbyWarp, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 2.0F);
            sendArenaMessage(ConfigC.normal_ingameArenaEnd, "1-1");
        }
    }

    /**
     * 用于分配seeker和hider
     */
    private void assignTeam() {
        HashSet<Player> tmpSeekers = new HashSet<>();
        HashSet<Player> tmpHiders = new HashSet<>();
        HashSet<Player> tmpNeutral = new HashSet<>();
        for (Player player : playersInArena) {
            if (W.chosenSeeker.containsKey(player)) {
                if (W.chosenSeeker.get(player)) tmpSeekers.add(player);
                else tmpHiders.add(player);
            } else {
                tmpNeutral.add(player);
            }
        }
        //最多选amount个
        seekers.addAll(Helpers.reservoirSampling(tmpSeekers, amountSeekersOnStart));
        if (seekers.size() < amountSeekersOnStart) {
            //seekers不够
            if (tmpNeutral.size() + seekers.size() < amountSeekersOnStart) {
                //seeker和neutral加起来还不都，则需要从hiders里调人，至少保证hider有1人
                int playerNeed = amountSeekersOnStart - seekers.size() - tmpNeutral.size();
                seekers.addAll(tmpNeutral);
                seekers.addAll(Helpers.reservoirSampling(tmpHiders, playerNeed < tmpHiders.size() ? playerNeed : tmpHiders.size() - 1));
            } else {
                //光neutral就够了
                seekers.addAll(Helpers.reservoirSampling(tmpNeutral, amountSeekersOnStart - seekers.size()));
            }
        }
    }

    /**
     * 初始化seeker身上的物品，每局开始时调用
     */
    private void setSeekerInventory(Player player) {
        Kit kit= W.playerSeekerKitMap.get(player);
        if(kit!=null){
            kit.give(player);
            MessageM.sendFMessage(player, ConfigC.normal_kitLoaded, "name-" + kit.name);
            return;
        }
        player.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD, 1));
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
    }
    /**
     * 初始化hider身上的物品，每局开始时调用
     */
    private void setHiderInventory(Player player) {
        Kit kit= W.playerHiderKitMap.get(player);
        if(kit!=null){
            kit.give(player);
            MessageM.sendFMessage(player, ConfigC.normal_kitLoaded, "name-" + kit.name);
            return;
        }
        ItemStack sword = new ItemStack(Material.WOODEN_SWORD, 1);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        ItemStack fireworks = new ItemStack(Material.FIREWORK_ROCKET, 64);
        player.getInventory().addItem(sword);
        player.getInventory().addItem(fireworks);
    }

    /**
     * 初始化jjc，每局开始时调用
     */
    private void initArena() {
        gameState = ArenaState.INGAME;
        //gameTime置为一局的时间
        timer = gameTime;
        sendArenaMessage(ConfigC.normal_lobbyArenaStarted, "secs-" + waitingTimeSeeker);
        //选取玩家作为seekers
        assignTeam();
        for (Player nowPlayer : playersInArena) {
            nowPlayer.getInventory().clear();
            nowPlayer.updateInventory();
            if (seekers.contains(nowPlayer)) {
                sendArenaMessage(ConfigC.normal_ingameSeekerChoosen, "seeker-" + nowPlayer.getName());
                //将seeker传送走
                nowPlayer.teleport(seekersWarp);
                setSeekerInventory(nowPlayer);
                seekerTime.put(nowPlayer,gameTime-waitingTimeSeeker);
            } else {
                //将hider初始化
                ItemStack block;
                if (W.chosenBlock.get(nowPlayer) != null) {
                    //玩家选择了就用选择的方块
                    block = W.chosenBlock.get(nowPlayer);
                    W.chosenBlock.remove(nowPlayer);
                } else {
                    //随机选择一个方块
                    block = this.disguiseBlocks.get(W.random.nextInt(this.disguiseBlocks.size()));
                }
                MiscDisguise disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, block.getType());
                DisguiseAPI.disguiseToAll(nowPlayer, disguise);
                nowPlayer.teleport(this.hidersWarp);
                ItemStack blockCount = new ItemStack(block.getType(), 5);
                nowPlayer.getInventory().setItem(8, blockCount);
                nowPlayer.getInventory().setHelmet(new ItemStack(block));
                W.pBlock.put(nowPlayer, block);
                MessageM.sendFMessage(nowPlayer, ConfigC.normal_ingameBlock, "block-" + block.getType().name().replaceAll("_", "").replaceAll("BLOCK", "").toLowerCase());
            }
        }
    }

    /**
     * 处理玩家被打死的情况,damager可以是null
     */
    public void playerAboutToDieHandler(Player player,Player damager){
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
        //解除玩家伪装
        DisguiseAPI.undisguiseToAll(player);
        W.pBlock.remove(player);
        if (!seekers.contains(player)) {
            if(damager!=null){
                //被杀死的玩家是hider，则给damager，也就是seeker加钱
                if (W.shop.getFile().get(damager.getName() + ".tokens") == null) {
                    W.shop.getFile().set(damager.getName() + ".tokens", 0);
                    W.shop.save();
                }
                int damagerTokens = W.shop.getFile().getInt(damager.getName() + ".tokens");
                W.shop.getFile().set(damager.getName() + ".tokens", damagerTokens + killTokens);
                W.shop.save();
                MessageM.sendFMessage(damager, ConfigC.normal_addedToken, "amount-" + killTokens);
                if (W.shop.getFile().get(player.getName() + ".tokens") == null) {
                    W.shop.getFile().set(player.getName() + ".tokens", 0);
                    W.shop.save();
                }
            }
            //按照剩余时间给hider加钱
            int playerTokens = W.shop.getFile().getInt(player.getName() + ".tokens");
            float addingTokens = hidersTokenWin - timer / gameTime * hidersTokenWin;
            W.shop.getFile().set(player.getName() + ".tokens", playerTokens + (int) addingTokens);
            W.shop.save();
            MessageM.sendFMessage(player, ConfigC.normal_addedToken, "amount-" + (int) addingTokens);
            //被杀死的hider加入seeker阵营
            seekers.add(player);
            sendArenaMessage(ConfigC.normal_ingameHiderDied, "playername-" + player.getName(),
                    "left-" + (playersInArena.size() - seekers.size()));
        } else {
            //seeker被杀死不给hider加钱
            sendArenaMessage(ConfigC.normal_ingameSeekerDied, "playername-" + player.getName(), "secs-" + waitingTimeSeeker);
        }
        player.getInventory().clear();
        //所有人都变成seeker则seeker获胜
        if (seekers.size() >= playersInArena.size()) {
            seekersWin();
            return;
        }
        //不论如何玩家死后都会变成seeker
        setSeekerInventory(player);
        DisguiseAPI.undisguiseToAll(player);
        //重置seeker冷却
        seekerTime.put(player, timer-waitingTimeSeeker);
        player.teleport(seekersWarp);
        player.setGameMode(GameMode.SURVIVAL);
        player.setWalkSpeed(0.25F);
    }

    /**
     * 处理玩家中离开的情况
     */
    public void playerLeaveHandler(Player player, boolean message, boolean cleanup){
        {
            W.playerArenaMap.remove(player);
            //需要对jjc内变量进行清理，对应的是玩家中途跑了的情况
            if (cleanup) {
                playersInArena.remove(player);
                seekers.remove(player);
                //等待中如果人不够就停止倒计时
                if (playersInArena.size() < minPlayers && gameState.equals(Arena.ArenaState.STARTING)) {
                    gameState = Arena.ArenaState.WAITING;
                    timer = 0;
                    sendArenaMessage(ConfigC.warning_lobbyNeedAtleast, "1-" + minPlayers);
                }
                if(gameState == Arena.ArenaState.INGAME){
                    inGamePlayerLeaveHandler(player);
                }
            }
            PlayerArenaData pad = new PlayerArenaData();
            if (W.pData.get(player) != null)
                pad = W.pData.get(player);
            player.getInventory().clear();
            player.getInventory().setContents(pad.pInventory);
            player.getInventory().setArmorContents(pad.pArmor);
            player.updateInventory();
            player.setExp(pad.pEXP);
            player.setLevel(pad.pEXPL);
            player.setHealth(pad.pHealth);
            player.setFoodLevel(pad.pFood);
            player.addPotionEffects(pad.pPotionEffects);
            player.teleport((Location) W.config.get(ConfigC.lobbyPosition));
            player.setGameMode(pad.pGameMode);
            player.setAllowFlight(pad.pFlying);
            if (player.getAllowFlight())
                player.setFlying(true);
            player.setWalkSpeed(0.2F);
            W.pData.remove(player);
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.showPlayer(player);
                if (W.hiddenLoc.get(player) != null &&
                        W.hiddenLocWater.get(player) != null) {
                    Block pBlock = W.hiddenLoc.get(player).getBlock();
                    if (W.hiddenLocWater.get(player)) {
                        pl.sendBlockChange(pBlock.getLocation(), Material.WATER.createBlockData());
                    } else {
                        pl.sendBlockChange(pBlock.getLocation(), Material.AIR.createBlockData());
                    }
                }
                DisguiseAPI.undisguiseToAll(player);
            }
            ScoreboardHandler.removeScoreboard(player);
            MessageM.sendFMessage(player, ConfigC.normal_leaveYouLeft);
            if (message)
                sendArenaMessage(ConfigC.normal_leaveLeftArena, "playername-" + player.getName(), "1-" + playersInArena.size(), "2-" + maxPlayers);
        }
    }


    /**
     * 处理玩家在游戏中离开的情况
     * @param player
     */
    private void inGamePlayerLeaveHandler(Player player){
        if (playersInArena.size() <= 1 )
            if (seekers.size() >= playersInArena.size()) {
                seekersWin();
                return;
            } else {
                hidersWin();
                return;
            }
        //剩下的都是seekers了，则seeker赢
        if (seekers.size() >= playersInArena.size()) {
            seekersWin();
            return;
        }
        //seeker跑光了，指定新的seeker
        if (seekers.size() == 0) {
            Iterator<Player> it = playersInArena.iterator();
            int playeri = W.random.nextInt(playersInArena.size());
            for (int i = 0; i < playeri - 1; i++) {
                it.next();
            }
            Player seeker = it.next();
            sendArenaMessage(ConfigC.warning_ingameNEWSeekerChoosen, "seeker-" + seeker.getName());
            sendArenaMessage(ConfigC.normal_ingameSeekerChoosen, "seeker-" + seeker.getName());
            DisguiseAPI.undisguiseToAll(seeker);
            for (Player pl : Bukkit.getOnlinePlayers())
                pl.showPlayer(seeker);
            seeker.getInventory().clear();
            seekers.add(seeker);
            seeker.teleport(seekersWarp);
            seekerTime.put(seeker, timer-waitingTimeSeeker);
            seeker.setWalkSpeed(0.25F);
            for (Player otherplayer : playersInArena) {
                if (otherplayer.canSee(player))
                    otherplayer.showPlayer(player);
                if (player.canSee(otherplayer))
                    player.showPlayer(otherplayer);
            }
        }
    }
    /**
     * 处理玩家加入jjc的情况
     */
    public void playerJoinHandler(Player player){
        //jjc没有可用的隐藏方块也不行
        if (disguiseBlocks.isEmpty()) {
            MessageM.sendFMessage(player, ConfigC.error_joinNoBlocksSet);
            return;
        }
        LocationSerializable zero = new LocationSerializable(Bukkit.getWorld(player.getWorld().getName()), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        //jjc的四个传送点要有，全零也不行
        if (lobbyWarp == null || hidersWarp == null || seekersWarp == null || W.config.get(ConfigC.lobbyPosition) == null ||
                (lobbyWarp.equals(zero) && hidersWarp.equals(zero) && seekersWarp.equals(zero))) {
            MessageM.sendFMessage(player, ConfigC.error_joinWarpsNotSet);
            return;
        }
        //还要jjc在等待或者即将开始的状态
        if (gameState != Arena.ArenaState.WAITING && gameState != Arena.ArenaState.STARTING) {
            MessageM.sendFMessage(player, ConfigC.error_joinArenaIngame);
            return;
        }
        //人满了也不行
        if (playersInArena.size() >= maxPlayers && !PermissionsM.hasPerm(player, PermissionsC.Permissions.joinfull, Boolean.FALSE)) {
            MessageM.sendFMessage(player, ConfigC.error_joinFull);
            return;
        }
        //条件都满足了，将玩家加入jjc
        W.playerArenaMap.put(player,this);
        //保存物品栏
        boolean inventoryempty = true;
        for (ItemStack invItem : player.getInventory()) {
            if (invItem != null &&
                    invItem.getType() != Material.AIR)
                inventoryempty = false;
        }
        byte b;
        int i;
        ItemStack[] arrayOfItemStack;
        for (i = (arrayOfItemStack = player.getInventory().getArmorContents()).length, b = 0; b < i; ) {
            ItemStack invitem = arrayOfItemStack[b];
            if (invitem != null && invitem.getType() != Material.AIR)
                inventoryempty = false;
            b++;
        }
        if ((Boolean) W.config.get(ConfigC.requireInventoryClearOnJoin) && !inventoryempty) {
            MessageM.sendFMessage(player, ConfigC.error_joinInventoryNotEmpty);
            return;
        }
        //保存进入前状态
        playersInArena.add(player);
        PlayerArenaData pad = new PlayerArenaData(player.getLocation(),
                player.getGameMode(), player.getInventory().getContents(),
                player.getInventory().getArmorContents(), player.getExp(),
                player.getLevel(), player.getHealth(), player.getFoodLevel(),
                player.getActivePotionEffects(), player.getAllowFlight());
        W.pData.put(player, pad);
        //将玩家传送并清空物品，并设置初始状态
        player.teleport(lobbyWarp);
        player.setGameMode(GameMode.SURVIVAL);
        for (PotionEffect pe : player.getActivePotionEffects())
            player.removePotionEffect(pe.getType());
        player.setFoodLevel(20);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        player.setLevel(timer);
        player.setExp(0.0F);
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setWalkSpeed(0.25F);
        //让场内玩家可见
        for (Player otherPlayer : playersInArena) {
            if (otherPlayer.canSee(player))
                otherPlayer.showPlayer(player);
            if (player.canSee(otherPlayer))
                player.showPlayer(otherPlayer);
        }
        DisguiseAPI.undisguiseToAll(player);
        //给予blockChooser，需要检查是否启用，玩家是否购买或者直接就有权限
        if ((Boolean) W.config.get(ConfigC.shop_blockChooserv1Enabled) && (
                W.shop.getFile().get(player.getName() + ".blockchooser") != null ||
                        PermissionsM.hasPerm(player, PermissionsC.Permissions.shopBlockHuntPass, Boolean.FALSE))) {
            ItemStack shopBlockChooser = new ItemStack(Material.getMaterial((String) W.config.get(ConfigC.shop_blockChooserv1IDname)), 1);
            ItemMeta shopBlockChooser_IM = shopBlockChooser.getItemMeta();
            shopBlockChooser_IM.setDisplayName(MessageM.replaceAll((String) W.config.get(ConfigC.shop_blockChooserv1Name)));
            List<String> lores = W.config.getFile().getStringList(ConfigC.shop_blockChooserv1Description.fileKey);
            List<String> lores2 = new ArrayList<>();
            for (String lore : lores)
                lores2.add(MessageM.replaceAll(lore));
            shopBlockChooser_IM.setLore(lores2);
            shopBlockChooser.setItemMeta(shopBlockChooser_IM);
            player.getInventory().addItem(shopBlockChooser);
        }
        //给予blockHuntPass，需要检查是否启用，玩家是否购买或者直接就有权限
        int passCount = W.shop.getFile().getInt(player.getName() + ".blockhuntpass");
        if ((Boolean) W.config.get(ConfigC.shop_BlockHuntPassv2Enabled) &&
                (passCount != 0 || PermissionsM.hasPerm(player, PermissionsC.Permissions.shopBlockHuntPass, Boolean.FALSE))) {
            ItemStack shopBlockHuntPass = new ItemStack(Material.getMaterial((String) W.config.get(ConfigC.shop_BlockHuntPassv2IDName)), 1);
            ItemMeta shopBlockHuntPass_IM = shopBlockHuntPass.getItemMeta();
            shopBlockHuntPass_IM.setDisplayName(MessageM.replaceAll((String) W.config.get(ConfigC.shop_BlockHuntPassv2Name)));
            List<String> lores = W.config.getFile().getStringList(ConfigC.shop_BlockHuntPassv2Description.fileKey);
            List<String> lores2 = new ArrayList<>();
            for (String lore : lores)
                lores2.add(MessageM.replaceAll(lore));
            shopBlockHuntPass_IM.setLore(lores2);
            shopBlockHuntPass.setItemMeta(shopBlockHuntPass_IM);
            shopBlockHuntPass.setAmount(passCount==0?1:passCount);
            player.getInventory().addItem(shopBlockHuntPass);
        }
        //提示新玩家加入
        sendArenaMessage(ConfigC.normal_joinJoinedArena, "playername-" + player.getName(), "1-" + playersInArena.size(), "2-" + maxPlayers);
        //提示还需要多少玩家
        if (playersInArena.size() < minPlayers)
            sendArenaMessage(ConfigC.warning_lobbyNeedAtleast, "1-" + minPlayers);
        //给予两个kit选择器
        //TODO 权限+物品样式
        if ((Boolean) W.config.get(ConfigC.shop_KitEnabled) && PermissionsM.hasPerm(player, PermissionsC.Permissions.shopKitSelector, Boolean.FALSE)) {
            ItemStack shopBlockHuntPass = new ItemStack(Material.getMaterial((String) W.config.get(ConfigC.shop_KitSelectorHiderIdName)), 1);
            ItemMeta shopBlockHuntPass_IM = shopBlockHuntPass.getItemMeta();
            shopBlockHuntPass_IM.setDisplayName(MessageM.replaceAll((String) W.config.get(ConfigC.shop_KitSelectorHiderName)));
            List<String> lores = W.config.getFile().getStringList(ConfigC.shop_BlockHuntPassv2Description.fileKey);
            List<String> lores2 = new ArrayList<>();
            for (String lore : lores)
                lores2.add(MessageM.replaceAll(lore));
            shopBlockHuntPass_IM.setLore(lores2);
            shopBlockHuntPass.setItemMeta(shopBlockHuntPass_IM);
            shopBlockHuntPass.setAmount(1);
            player.getInventory().addItem(shopBlockHuntPass);
        }
        //TODO 权限+物品样式
        if ((Boolean) W.config.get(ConfigC.shop_KitEnabled) && PermissionsM.hasPerm(player, PermissionsC.Permissions.shopKitSelector, Boolean.FALSE)) {
            ItemStack shopBlockHuntPass = new ItemStack(Material.getMaterial((String) W.config.get(ConfigC.shop_KitSelectorSeekerIdName)), 1);
            ItemMeta shopBlockHuntPass_IM = shopBlockHuntPass.getItemMeta();
            shopBlockHuntPass_IM.setDisplayName(MessageM.replaceAll((String) W.config.get(ConfigC.shop_KitSelectorSeekerName)));
            List<String> lores = W.config.getFile().getStringList(ConfigC.shop_BlockHuntPassv2Description.fileKey);
            List<String> lores2 = new ArrayList<>();
            for (String lore : lores)
                lores2.add(MessageM.replaceAll(lore));
            shopBlockHuntPass_IM.setLore(lores2);
            shopBlockHuntPass.setItemMeta(shopBlockHuntPass_IM);
            shopBlockHuntPass.setAmount(1);
            player.getInventory().addItem(shopBlockHuntPass);
        }
    }
    
    /**
     * 每刻执行，处理jjc在每秒(20tick)需要做的事情
     */
    public void tick() {
        if (gameState == ArenaState.WAITING) {
            //如果等待中的玩家足够了就将jjc置为即将开始的状态
            if (playersInArena.size() >= minPlayers) {
                gameState = ArenaState.STARTING;
                timer = timeInLobbyUntilStart;
                sendArenaMessage(ConfigC.normal_lobbyArenaIsStarting, "1-" + timeInLobbyUntilStart);
            }
        } else if (gameState == ArenaState.STARTING) {
            if (timer <= 0) {
                initArena();
            } else {
                timer--;
                sendGameStartCountDownMsg();
            }
        } else if (gameState == ArenaState.INGAME) {
            timer--;
            //到达seekerTimer所指定的时间时放出seeker，用迭代器实现
            Iterator<Player> it = seekerTime.keySet().iterator();
            Player nowPlayer;
            while (it.hasNext()) {
                nowPlayer = it.next();
                if(timer==seekerTime.get(nowPlayer)){
                    nowPlayer.teleport(hidersWarp);
                    sendArenaMessage(ConfigC.normal_ingameSeekerSpawned, "playername-" + nowPlayer.getName());
                    it.remove();
                }
            }
            //经过timeUntilHidersSword后给hider装备
            if (timer == gameTime - timeUntilHidersSword) {
                for (Player arenaPlayer : playersInArena) {
                    if (!seekers.contains(arenaPlayer)) {
                        setHiderInventory(arenaPlayer);
                        MessageM.sendFMessage(arenaPlayer, ConfigC.normal_ingameGivenSword);
                    }
                }
            }
            sendGameEndCountDownMsg();
            if (timer <= 0) {
                hidersWin();
            }
            ScoreboardHandler.updateScoreboard(this);
            for (Player player : playersInArena) {
                player.setLevel(timer);
                player.setGameMode(GameMode.SURVIVAL);
                if (!seekers.contains(player)) {
                    Location pLoc = player.getLocation();
                    Location moveLoc = W.moveLoc.get(player);
                    ItemStack block = player.getInventory().getItem(8);
                    if (block == null && W.pBlock.get(player) != null) {
                        block = W.pBlock.get(player);
                        player.getInventory().setItem(8, block);
                        player.updateInventory();
                    }
                    if (moveLoc == null) continue;
                    //检查玩家是否没动
                    if (moveLoc.getX() == pLoc.getX() && moveLoc.getY() == pLoc.getY() && moveLoc.getZ() == pLoc.getZ()) {
                        //确实没动
                        if (block.getAmount() > 1) {
                            block.setAmount(block.getAmount() - 1);
                            continue;
                        }
                        Block pBlock = player.getLocation().getBlock();
                        //玩家可以躲在水里
                        if (pBlock.getType().equals(Material.AIR) || pBlock.getType().equals(Material.WATER)) {
                            if (pBlock.getType().equals(Material.WATER)) {
                                W.hiddenLocWater.put(player, Boolean.TRUE);
                            } else {
                                W.hiddenLocWater.put(player, Boolean.FALSE);
                            }
                            if (DisguiseAPI.isDisguised(player)) {
                                //解除玩家的伪装状态并隐身
                                DisguiseAPI.undisguiseToAll(player);
                                //设置物品栏最后一格的方块提示
                                block.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                                player.playSound(pLoc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                                W.hiddenLoc.put(player, moveLoc);
                                MessageM.sendFMessage(player, ConfigC.normal_ingameNowSolid, "block-" + block.getType().name().replaceAll("_", "").replaceAll("BLOCK", "").toLowerCase());
                            }
                            Iterator<Player> iterator = (Iterator<Player>) Bukkit.getOnlinePlayers().iterator();
                            while (iterator.hasNext()) {
                                Player pl = iterator.next();
                                if (!pl.equals(player)) {
                                    pl.hidePlayer(player);
                                    pl.sendBlockChange(pBlock.getLocation(), block.getType().createBlockData());
                                }
                            }
                            continue;
                        }
                        //不在空气或者水中不能变成固态方块，进行提示
                        MessageM.sendFMessage(player, ConfigC.warning_ingameNoSolidPlace);
                        continue;
                    }
                    //动了就解除伪装
                    block.setAmount(5);
                    if (!DisguiseAPI.isDisguised(player))
                        SolidBlockHandler.makePlayerUnsolid(player);
                }

            }
        }
    }

    public void seekersWin() {
        sendArenaMessage(ConfigC.normal_winSeekers);
        for (Player player : playersInArena) {
            if (W.config.getFile().getBoolean("vaultSupport")) {
                if (BlockHunt.econ != null) {
                    BlockHunt.econ.depositPlayer(player.getName(), seekersTokenWin);
                    MessageM.sendFMessage(player, ConfigC.normal_addedVaultBalance, "amount-" + seekersTokenWin);
                }
                continue;
            }
            if (W.shop.getFile().get(player.getName() + ".tokens") == null) {
                W.shop.getFile().set(player.getName() + ".tokens", 0);
                W.shop.save();
            }
            int playerTokens = W.shop.getFile().getInt(player.getName() + ".tokens");
            W.shop.getFile().set(player.getName() + ".tokens", playerTokens + seekersTokenWin);
            W.shop.save();
            MessageM.sendFMessage(player, ConfigC.normal_addedToken, "amount-" + seekersTokenWin);
        }
        cleanUp();
    }

    public void hidersWin() {
        StringBuilder hidersLeft = new StringBuilder();
        for (Player player : playersInArena) {
            if (!seekers.contains(player))
                hidersLeft.append(player.getName()).append(", ");
        }
        hidersLeft = new StringBuilder(hidersLeft.substring(0, hidersLeft.length() - 2));
        sendArenaMessage(ConfigC.normal_winHiders, "names-" + hidersLeft);
        for (Player player : playersInArena) {
            if (seekers.contains(player)) {
                if (W.config.getFile().getBoolean("vaultSupport")) {
                    if (BlockHunt.econ != null && seekers.contains(player)) {
                        BlockHunt.econ.depositPlayer(player.getName(), hidersTokenWin);
                        MessageM.sendFMessage(player, ConfigC.normal_addedVaultBalance, "amount-" + hidersTokenWin);
                    }
                    continue;
                }
                if (W.shop.getFile().get(player.getName() + ".tokens") == null) {
                    W.shop.getFile().set(player.getName() + ".tokens", 0);
                    W.shop.save();
                }
                int playerTokens = W.shop.getFile().getInt(player.getName() + ".tokens");
                W.shop.getFile().set(player.getName() + ".tokens", playerTokens + hidersTokenWin);
                W.shop.save();
                MessageM.sendFMessage(player, ConfigC.normal_addedToken, "amount-" + hidersTokenWin);
            }
        }
        cleanUp();
    }
    public void cleanUp() {
        seekers.clear();
        for (Player player : playersInArena) {
            playerLeaveHandler(player, false, false);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
        }
        gameState = Arena.ArenaState.WAITING;
        timer = 0;
        playersInArena.clear();
        seekers.clear();
        seekerTime.clear();
    }

    /**
     * 给jjc内的所有玩家发送消息
     * @param key 在ComfigC中的枚举类的键值
     * @param vars 参数
     */
    public void sendArenaMessage(ConfigC key, String... vars) {
        for (Player player : playersInArena) {
            String pMessage = key.config.getFile().get(key.fileKey).toString().replaceAll("%player%",
                    player.getName());
            player.sendMessage(MessageM.replaceAll(pMessage, vars));
        }
    }

    public void stop() {
        sendArenaMessage(ConfigC.warning_arenaStopped);
        cleanUp();
    }
}