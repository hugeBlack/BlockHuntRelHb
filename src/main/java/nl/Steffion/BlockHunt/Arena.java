package nl.Steffion.BlockHunt;

import java.util.*;

import it.unimi.dsi.fastutil.Hash;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import nl.Steffion.BlockHunt.Serializables.M;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

@SerializableAs("BlockHuntArena")
public class Arena implements ConfigurationSerializable {
    public String arenaName;

    public LocationSerializable pos1;

    public LocationSerializable pos2;

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

    public LocationSerializable spawnWarp;

    public List<String> seekersWinCommands;

    public List<String> hidersWinCommands;

    public List<String> allowedCommands;

    public int seekersTokenWin;

    public int hidersTokenWin;

    public int killTokens;

    public HashSet<Player> playersInArena;

    public ArenaState gameState;

    public int timer;

    public HashSet<Player> seekers;

    public Scoreboard scoreboard;

    public Arena(String arenaName, LocationSerializable pos1, LocationSerializable pos2, int maxPlayers, int minPlayers, int amountSeekersOnStart, int timeInLobbyUntilStart, int waitingTimeSeeker, int gameTime, int timeUntilHidersSword, ArrayList<ItemStack> disguiseBlocks, LocationSerializable lobbyWarp, LocationSerializable hidersWarp, LocationSerializable seekersWarp, LocationSerializable spawnWarp, List<String> seekersWinCommands, List<String> hidersWinCommands, List<String> allowedCommands, int seekersTokenWin, int hidersTokenWin, int killTokens, ArenaState gameState, int timer, Scoreboard scoreboard) {
        this.arenaName = arenaName;
        this.pos1 = pos1;
        this.pos2 = pos2;
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
        this.spawnWarp = spawnWarp;
        this.seekersWinCommands = seekersWinCommands;
        this.hidersWinCommands = hidersWinCommands;
        this.allowedCommands = allowedCommands;
        this.seekersTokenWin = seekersTokenWin;
        this.hidersTokenWin = hidersTokenWin;
        this.killTokens = killTokens;
        this.gameState = gameState;
        this.timer = timer;
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
        map.put("spawnWarp", this.spawnWarp);
        map.put("seekersWinCommands", this.seekersWinCommands);
        map.put("hidersWinCommands", this.hidersWinCommands);
        map.put("allowedCommands", this.allowedCommands);
        map.put("seekersTokenWin", this.seekersTokenWin);
        map.put("hidersTokenWin", this.hidersTokenWin);
        map.put("killTokens", this.killTokens);
        return map;
    }

    public static Arena deserialize(Map<String, Object> map) {
        LocationSerializable loc = new LocationSerializable(
                Bukkit.getWorld("world"), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        return new Arena((String) M.g(map, "arenaName", "UNKNOWN_NAME"),
                (LocationSerializable) M.g(map, "pos1", loc),
                (LocationSerializable) M.g(map, "pos2", loc), (Integer) M.g(
                map, "maxPlayers", 12), (Integer) M.g(map,
                "minPlayers", 3), (Integer) M.g(map,
                "amountSeekersOnStart", 1), (Integer) M.g(map,
                "timeInLobbyUntilStart", 90), (Integer) M.g(map,
                "waitingTimeSeeker", 20), (Integer) M.g(map,
                "gameTime", 200), (Integer) M.g(map,
                "timeUntilHidersSword", 30),
                (ArrayList<ItemStack>) M.g(map, "disguiseBlocks", new ArrayList()),
                (LocationSerializable) M.g(map, "lobbyWarp", loc),
                (LocationSerializable) M.g(map, "hidersWarp", loc),
                (LocationSerializable) M.g(map, "seekersWarp", loc),
                (LocationSerializable) M.g(map, "spawnWarp", loc),
                (ArrayList) M.g(map, "seekersWinCommands", new ArrayList()),
                (ArrayList) M.g(map, "hidersWinCommands", new ArrayList()),
                (ArrayList) M.g(map, "allowedCommands", new ArrayList()),
                (Integer) M.g(map, "seekersTokenWin", 10),
                (Integer) M.g(map, "hidersTokenWin", 50),
                (Integer) M.g(map, "killTokens", 8),
                ArenaState.WAITING,
                0,
                Bukkit.getScoreboardManager()
                        .getNewScoreboard());
    }

    private void sendGameStartCountDownMsg() {
        //处理倒计时应该显示的信息
        if (timer > 0) {
            if (timer == 60) {
                ArenaHandler.sendFMessage(this, ConfigC.normal_lobbyArenaIsStarting, "1-60");
            } else if (timer == 30) {
                ArenaHandler.sendFMessage(this, ConfigC.normal_lobbyArenaIsStarting, "1-30");
            } else if (timer == 10) {
                ArenaHandler.sendFMessage(this, ConfigC.normal_lobbyArenaIsStarting, "1-10");
            } else if (timer == 5) {
                for (Player pl : playersInArena)
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.0F);
                ArenaHandler.sendFMessage(this, ConfigC.normal_lobbyArenaIsStarting, "1-5");
            } else if (timer == 4) {
                for (Player pl : playersInArena)
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.0F);
                ArenaHandler.sendFMessage(this, ConfigC.normal_lobbyArenaIsStarting, "1-4");
            } else if (timer == 3) {
                for (Player pl : playersInArena)
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                ArenaHandler.sendFMessage(this, ConfigC.normal_lobbyArenaIsStarting, "1-3");
            } else if (timer == 2) {
                for (Player pl : playersInArena)
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                ArenaHandler.sendFMessage(this, ConfigC.normal_lobbyArenaIsStarting, "1-2");
            } else if (timer == 1) {
                for (Player pl : playersInArena)
                    pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 2.0F);
                ArenaHandler.sendFMessage(this, ConfigC.normal_lobbyArenaIsStarting, "1-1");
            }
        }
    }

    private void sendGameEndCountDownMsg() {
        if (timer == 190) {
            ArenaHandler.sendFMessage(this, ConfigC.normal_ingameArenaEnd, "1-190");
        } else if (timer == 60) {
            ArenaHandler.sendFMessage(this, ConfigC.normal_ingameArenaEnd, "1-60");
        } else if (timer == 30) {
            ArenaHandler.sendFMessage(this, ConfigC.normal_ingameArenaEnd, "1-30");
        } else if (timer == 10) {
            ArenaHandler.sendFMessage(this, ConfigC.normal_ingameArenaEnd, "1-10");
        } else if (timer == 5) {
            lobbyWarp.getWorld().playSound(lobbyWarp, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.0F);
            ArenaHandler.sendFMessage(this, ConfigC.normal_ingameArenaEnd, "1-5");
        } else if (timer == 4) {
            lobbyWarp.getWorld().playSound(lobbyWarp, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 0.0F);
            ArenaHandler.sendFMessage(this, ConfigC.normal_ingameArenaEnd, "1-4");
        } else if (timer == 3) {
            lobbyWarp.getWorld().playSound(lobbyWarp, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            ArenaHandler.sendFMessage(this, ConfigC.normal_ingameArenaEnd, "1-3");
        } else if (timer == 2) {
            lobbyWarp.getWorld().playSound(lobbyWarp, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            ArenaHandler.sendFMessage(this, ConfigC.normal_ingameArenaEnd, "1-2");
        } else if (timer == 1) {
            lobbyWarp.getWorld().playSound(lobbyWarp, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 2.0F);
            ArenaHandler.sendFMessage(this, ConfigC.normal_ingameArenaEnd, "1-1");
        }
    }

    /**
     * 用蓄水池抽样选取从list中随机选取数量最多为count个元素
     */
    private ArrayList<Player> reservoirSampling(Set<Player> list, int count) {
        ArrayList<Player> ans = new ArrayList<>();
        Iterator<Player> it = list.iterator();
        for (int i = 0; i < count && it.hasNext(); i++) {
            ans.add(it.next());
        }
        int elementsSeen = count;
        int j;
        while (it.hasNext()) {
            elementsSeen++;
            j = W.random.nextInt(elementsSeen);
            if (j < count) {
                ans.set(j, it.next());
            } else {
                it.next();
            }
        }
        return ans;
    }

    /**
     * 用于分配seeker和hider
     */
    private void assignTeam() {
        HashSet<Player> tmpSeekers = new HashSet<>();
        HashSet<Player> tmpHiders = new HashSet<>();
        HashSet<Player> tmpNeutral = new HashSet<>();
        for (Player player : playersInArena) {
            if (W.choosenSeeker.containsKey(player)) {
                if (W.choosenSeeker.get(player)) tmpSeekers.add(player);
                else tmpHiders.add(player);
            } else {
                tmpNeutral.add(player);
            }
        }
        //最多选amount个
        seekers.addAll(reservoirSampling(tmpSeekers, amountSeekersOnStart));
        if (seekers.size() < amountSeekersOnStart) {
            //seekers不够
            if (tmpNeutral.size() + seekers.size() < amountSeekersOnStart) {
                //seeker和neutral加起来还不都，则需要从hiders里调人，至少保证hider有1人
                int playerNeed = amountSeekersOnStart - seekers.size() - tmpNeutral.size();
                seekers.addAll(tmpNeutral);
                seekers.addAll(reservoirSampling(tmpHiders, playerNeed < tmpHiders.size() ? playerNeed : tmpHiders.size() - 1));
            } else {
                //光neutral就够了
                seekers.addAll(reservoirSampling(tmpNeutral, amountSeekersOnStart - seekers.size()));
            }
        }
    }

    /**
     * 初始化seeker身上的物品，每局开始时调用
     */
    private void setSeekerInventory(Player player) {
        player.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD, 1));
        player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET, 1));
        player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
        player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
        player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0F, 1.0F);
    }

    private void setHiderInventory(Player player) {
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
        ArenaHandler.sendFMessage(this, ConfigC.normal_lobbyArenaStarted, "secs-" + waitingTimeSeeker);
        //选取玩家作为seekers
        assignTeam();
        for (Player nowPlayer : playersInArena) {
            nowPlayer.getInventory().clear();
            nowPlayer.updateInventory();
            if (seekers.contains(nowPlayer)) {
                ArenaHandler.sendFMessage(this, ConfigC.normal_ingameSeekerChoosen, "seeker-" + nowPlayer.getName());
                //将seeker传送走
                nowPlayer.teleport(seekersWarp);
                setSeekerInventory(nowPlayer);
            } else {
                //将hider初始化
                ItemStack block;
                if (W.choosenBlock.get(nowPlayer) != null) {
                    //玩家选择了就用选择的方块
                    block = W.choosenBlock.get(nowPlayer);
                    W.choosenBlock.remove(nowPlayer);
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
     * 每刻执行，处理jjc在每秒(20tick)需要做的事情
     */
    public void tick() {
        if (gameState == ArenaState.WAITING) {
            //如果等待中的玩家足够了就将jjc置为即将开始的状态
            if (playersInArena.size() >= minPlayers) {
                gameState = ArenaState.STARTING;
                timer = timeInLobbyUntilStart;
                ArenaHandler.sendFMessage(this, ConfigC.normal_lobbyArenaIsStarting, "1-" + timeInLobbyUntilStart);
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
            //经过waitingTimeSeeker后放出seeker
            if (timer == gameTime - waitingTimeSeeker) {
                for (Player player : seekers) {
                    player.teleport(hidersWarp);
                    ArenaHandler.sendFMessage(this, ConfigC.normal_ingameSeekerSpawned, "playername-" + player.getName());
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
                ArenaHandler.hidersWin(this);
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

}