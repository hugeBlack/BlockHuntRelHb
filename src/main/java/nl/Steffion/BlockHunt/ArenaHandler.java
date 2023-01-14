package nl.Steffion.BlockHunt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.libraryaddict.disguise.DisguiseAPI;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Managers.PermissionsM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class ArenaHandler {
  public static void hidersWin(Arena arena) {
    StringBuilder hidersLeft = new StringBuilder();
    for (Player player : arena.playersInArena) {
      if (!arena.seekers.contains(player))
        hidersLeft.append(player.getName()).append(", ");
    } 
    hidersLeft = new StringBuilder(hidersLeft.substring(0, hidersLeft.length() - 2));
    sendFMessage(arena, ConfigC.normal_winHiders, "names-" + hidersLeft);
    for (Player player : arena.playersInArena) {
      if (arena.seekers.contains(player) && 
        arena.hidersWinCommands != null) {
        for (String command : arena.hidersWinCommands)
          Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
              command.replaceAll("%player%", player.getName())); 
        if (W.config.getFile().getBoolean("vaultSupport")) {
          if (BlockHunt.econ != null && 
            arena.seekers.contains(player)) {
            BlockHunt.econ.depositPlayer(player.getName(), arena.hidersTokenWin);
            MessageM.sendFMessage(player, ConfigC.normal_addedVaultBalance, "amount-" + arena.hidersTokenWin);
          } 
          continue;
        } 
        if (W.shop.getFile().get(player.getName() + ".tokens") == null) {
          W.shop.getFile().set(player.getName() + ".tokens", 0);
          W.shop.save();
        } 
        int playerTokens = W.shop.getFile().getInt(player.getName() + ".tokens");
        W.shop.getFile().set(player.getName() + ".tokens", playerTokens + arena.hidersTokenWin);
        W.shop.save();
        MessageM.sendFMessage(player, ConfigC.normal_addedToken, "amount-" + arena.hidersTokenWin);
      } 
    } 
    arena.seekers.clear();
    for (Player player : arena.playersInArena) {
      playerLeaveArena(player, false, false);
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
    } 
    arena.gameState = Arena.ArenaState.WAITING;
    arena.timer = 0;
    arena.playersInArena.clear();
  }
  
  public static void loadArenas() {
    W.arenaList.clear();
    for (String arenaName : W.arenas.getFile().getKeys(false))
      W.arenaList.add((Arena)W.arenas.getFile().get(arenaName)); 
    for (Arena arena : W.arenaList)
      ScoreboardHandler.createScoreboard(arena); 
  }
  
  public static void playerJoinArena(Player player, String arenaname) {
    boolean found = false;
    boolean alreadyJoined = false;
    for (Arena arena : W.arenaList) {
      if (arena.playersInArena != null && 
        arena.playersInArena.contains(player))
        alreadyJoined = true; 
    } 
    if (!alreadyJoined) {
      for (Arena arena : W.arenaList) {
        if (arena.arenaName.equalsIgnoreCase(arenaname)) {
          found = true;
          if (arena.disguiseBlocks.isEmpty()) {
            MessageM.sendFMessage(player, ConfigC.error_joinNoBlocksSet);
            continue;
          } 
          boolean inventoryempty = true;
          for (ItemStack invitem : player.getInventory()) {
            if (invitem != null && 
              invitem.getType() != Material.AIR)
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
          LocationSerializable zero = new LocationSerializable(
              Bukkit.getWorld(player.getWorld().getName()), 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
          if (arena.lobbyWarp != null && arena.hidersWarp != null && arena.seekersWarp != null && 
            arena.spawnWarp != null) {
            if (!arena.lobbyWarp.equals(zero) && !arena.hidersWarp.equals(zero) && 
              !arena.seekersWarp.equals(zero) && !arena.spawnWarp.equals(zero)) {
              if (arena.gameState == Arena.ArenaState.WAITING || 
                arena.gameState == Arena.ArenaState.STARTING) {
                if (arena.playersInArena.size() >= arena.maxPlayers && 
                  !PermissionsM.hasPerm(player, PermissionsC.Permissions.joinfull, Boolean.FALSE)) {
                  MessageM.sendFMessage(player, ConfigC.error_joinFull);
                  return;
                } 
                arena.playersInArena.add(player);
                PlayerArenaData pad = new PlayerArenaData(player.getLocation(), 
                    player.getGameMode(), player.getInventory().getContents(), 
                    player.getInventory().getArmorContents(), player.getExp(),
                        player.getLevel(), player.getHealth(), player.getFoodLevel(),
                    player.getActivePotionEffects(), player.getAllowFlight());
                W.pData.put(player, pad);
                player.teleport(arena.lobbyWarp);
                player.setGameMode(GameMode.SURVIVAL);
                for (PotionEffect pe : player.getActivePotionEffects())
                  player.removePotionEffect(pe.getType()); 
                player.setFoodLevel(20);
                player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                player.setLevel(arena.timer);
                player.setExp(0.0F);
                player.getInventory().clear();
                player.getInventory().setHelmet(new ItemStack(Material.AIR));
                player.getInventory().setChestplate(new ItemStack(Material.AIR));
                player.getInventory().setLeggings(new ItemStack(Material.AIR));
                player.getInventory().setBoots(new ItemStack(Material.AIR));
                player.setFlying(false);
                player.setAllowFlight(false);
                player.setWalkSpeed(0.25F);
                for (Player otherplayer : arena.playersInArena) {
                  if (otherplayer.canSee(player))
                    otherplayer.showPlayer(player); 
                  if (player.canSee(otherplayer))
                    player.showPlayer(otherplayer); 
                } 
                if ((Boolean) W.config.get(ConfigC.shop_blockChooserv1Enabled) && (
                  W.shop.getFile().get(player.getName() + ".blockchooser") != null ||
                  PermissionsM.hasPerm(player, PermissionsC.Permissions.shopblockchooser, Boolean.FALSE))) {
                  ItemStack shopBlockChooser = new ItemStack(Material.getMaterial((String)W.config.get(ConfigC.shop_blockChooserv1IDname)), 1);
                  ItemMeta shopBlockChooser_IM = shopBlockChooser.getItemMeta();
                  shopBlockChooser_IM.setDisplayName(MessageM.replaceAll(
                        (String)W.config.get(ConfigC.shop_blockChooserv1Name)));
                  List<String> lores = W.config.getFile()
                    .getStringList(ConfigC.shop_blockChooserv1Description.location);
                  List<String> lores2 = new ArrayList<>();
                  for (String lore : lores)
                    lores2.add(MessageM.replaceAll(lore));
                  shopBlockChooser_IM.setLore(lores2);
                  shopBlockChooser.setItemMeta(shopBlockChooser_IM);
                  player.getInventory().addItem(shopBlockChooser);
                } 
                if ((Boolean) W.config.get(ConfigC.shop_BlockHuntPassv2Enabled) &&
                  W.shop.getFile().getInt(player.getName() + ".blockhuntpass") != 0) {
                  ItemStack shopBlockHuntPass = new ItemStack(Material.getMaterial((String)W.config.get(ConfigC.shop_BlockHuntPassv2IDName)), 1);
                  ItemMeta shopBlockHuntPass_IM = shopBlockHuntPass.getItemMeta();
                  shopBlockHuntPass_IM.setDisplayName(MessageM.replaceAll(
                        (String)W.config.get(ConfigC.shop_BlockHuntPassv2Name)));
                  List<String> lores = W.config.getFile()
                    .getStringList(ConfigC.shop_BlockHuntPassv2Description.location);
                  List<String> lores2 = new ArrayList<>();
                  for (String lore : lores)
                    lores2.add(MessageM.replaceAll(lore));
                  shopBlockHuntPass_IM.setLore(lores2);
                  shopBlockHuntPass.setItemMeta(shopBlockHuntPass_IM);
                  shopBlockHuntPass.setAmount(
                      W.shop.getFile().getInt(player.getName() + ".blockhuntpass"));
                  player.getInventory().addItem(shopBlockHuntPass);
                } 
                DisguiseAPI.undisguiseToAll(player);
                sendFMessage(arena, ConfigC.normal_joinJoinedArena, "playername-" + player.getName(), "1-" + arena.playersInArena.size(),
                        "2-" + arena.maxPlayers);
                if (arena.playersInArena.size() < arena.minPlayers)
                  sendFMessage(arena, ConfigC.warning_lobbyNeedAtleast, "1-" + arena.minPlayers);
                continue;
              } 
              MessageM.sendFMessage(player, ConfigC.error_joinArenaIngame);
              continue;
            } 
            MessageM.sendFMessage(player, ConfigC.error_joinWarpsNotSet);
            continue;
          } 
          MessageM.sendFMessage(player, ConfigC.error_joinWarpsNotSet);
        } 
      } 
    } else {
      MessageM.sendFMessage(player, ConfigC.error_joinAlreadyJoined);
      return;
    } 
    if (!found)
      MessageM.sendFMessage(player, ConfigC.error_noArena, "name-" + arenaname);
    SignsHandler.updateSigns();
  }
  
  public static void playerLeaveArena(Player player, boolean message, boolean cleanup) {
    Arena arena = null;
    for (Arena arena2 : W.arenaList) {
      if (arena2.playersInArena != null && 
        arena2.playersInArena.contains(player))
        arena = arena2; 
    } 
    if (arena != null) {
      if (cleanup) {
        arena.playersInArena.remove(player);
        arena.seekers.remove(player);
        if (arena.playersInArena.size() < arena.minPlayers && arena.gameState.equals(Arena.ArenaState.STARTING)) {
          arena.gameState = Arena.ArenaState.WAITING;
          arena.timer = 0;
          sendFMessage(arena, ConfigC.warning_lobbyNeedAtleast, "1-" + arena.minPlayers);
        } 
        if (arena.playersInArena.size() <= 2 && arena.gameState == Arena.ArenaState.INGAME)
          if (arena.seekers.size() >= arena.playersInArena.size()) {
            seekersWin(arena);
          } else {
            hidersWin(arena);
          }  
        if (arena.seekers.size() >= arena.playersInArena.size())
          seekersWin(arena); 
        if (arena.seekers.size() <= 0 && arena.gameState == Arena.ArenaState.INGAME) {
          Iterator<Player> it = arena.playersInArena.iterator();
          int playeri = W.random.nextInt(arena.playersInArena.size());
          for(int i=0;i<playeri-1;i++){
            it.next();
          }
          Player seeker = it.next();
          sendFMessage(arena, ConfigC.warning_ingameNEWSeekerChoosen, "seeker-" + seeker.getName());
          sendFMessage(arena, ConfigC.normal_ingameSeekerChoosen, "seeker-" + seeker.getName());
          DisguiseAPI.undisguiseToAll(seeker);
          for (Player pl : Bukkit.getOnlinePlayers())
            pl.showPlayer(seeker); 
          seeker.getInventory().clear();
          arena.seekers.add(seeker);
          seeker.teleport(arena.seekersWarp);
          W.seekertime.put(seeker, arena.waitingTimeSeeker);
          seeker.setWalkSpeed(0.25F);
          for (Player otherplayer : arena.playersInArena) {
            if (otherplayer.canSee(player))
              otherplayer.showPlayer(player); 
            if (player.canSee(otherplayer))
              player.showPlayer(otherplayer); 
          } 
        } 
      } 
      PlayerArenaData pad = new PlayerArenaData(null, null, null, null, null, null, null, null, null, false);
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
      player.teleport(arena.spawnWarp);
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
            pl.sendBlockChange(pBlock.getLocation(), Material.WATER, (byte)0);
          } else {
            pl.sendBlockChange(pBlock.getLocation(), Material.AIR, (byte)0);
          } 
        } 
        DisguiseAPI.undisguiseToAll(player);
      } 
      ScoreboardHandler.removeScoreboard(player);
      MessageM.sendFMessage(player, ConfigC.normal_leaveYouLeft);
      if (message)
        sendFMessage(arena, ConfigC.normal_leaveLeftArena, "playername-" + player.getName(),
                "1-" + arena.playersInArena.size(), "2-" + arena.maxPlayers);
    } else {
      if (message)
        MessageM.sendFMessage(player, ConfigC.error_leaveNotInArena);
      return;
    } 
    SignsHandler.updateSigns();
  }
  
  public static void seekersWin(Arena arena) {
    sendFMessage(arena, ConfigC.normal_winSeekers);
    for (Player player : arena.playersInArena) {
      if (arena.seekersWinCommands != null) {
        for (String command : arena.seekersWinCommands)
          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName()));
        if (W.config.getFile().getBoolean("vaultSupport")) {
          if (BlockHunt.econ != null) {
            BlockHunt.econ.depositPlayer(player.getName(), arena.seekersTokenWin);
            MessageM.sendFMessage(player, ConfigC.normal_addedVaultBalance, "amount-" + arena.seekersTokenWin);
          } 
          continue;
        } 
        if (W.shop.getFile().get(player.getName() + ".tokens") == null) {
          W.shop.getFile().set(player.getName() + ".tokens", 0);
          W.shop.save();
        } 
        int playerTokens = W.shop.getFile().getInt(player.getName() + ".tokens");
        W.shop.getFile().set(player.getName() + ".tokens", playerTokens + arena.seekersTokenWin);
        W.shop.save();
        MessageM.sendFMessage(player, ConfigC.normal_addedToken, "amount-" + arena.seekersTokenWin);
      } 
    } 
    arena.seekers.clear();
    for (Player player : arena.playersInArena) {
      playerLeaveArena(player, false, false);
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
    } 
    arena.gameState = Arena.ArenaState.WAITING;
    arena.timer = 0;
    arena.playersInArena.clear();
  }
  
  public static void sendFMessage(Arena arena, ConfigC location, String... vars) {
    for (Player player : arena.playersInArena) {
      String pMessage = location.config.getFile().get(location.location).toString().replaceAll("%player%", 
          player.getName());
      player.sendMessage(MessageM.replaceAll(pMessage, vars));
    } 
  }
  
  public static void sendMessage(Arena arena, String message, String... vars) {
    for (Player player : arena.playersInArena) {
      String pMessage = message.replaceAll("%player%", player.getName());
      player.sendMessage(MessageM.replaceAll(pMessage, vars));
    } 
  }
  
  public static void stopArena(Arena arena) {
    sendFMessage(arena, ConfigC.warning_arenaStopped, new String[0]);
    arena.seekers.clear();
    for (Player player : arena.playersInArena) {
      playerLeaveArena(player, false, false);
      player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
    } 
    arena.gameState = Arena.ArenaState.WAITING;
    arena.timer = 0;
    arena.playersInArena.clear();
  }
}