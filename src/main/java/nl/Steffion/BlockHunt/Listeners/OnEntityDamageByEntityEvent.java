package nl.Steffion.BlockHunt.Listeners;

import me.libraryaddict.disguise.DisguiseAPI;
import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.W;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class OnEntityDamageByEntityEvent implements Listener {
  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
    Player player = null;
    if (event.getEntity() instanceof Player)
      player = (Player)event.getEntity(); 
    Player damager = null;
    if (event.getDamager() instanceof Player)
      damager = (Player)event.getDamager(); 
    if (player != null)
      for (Arena arena : W.arenaList) {
        if (arena.playersInArena.contains(player)) {
          if (arena.gameState == Arena.ArenaState.WAITING || arena.gameState == Arena.ArenaState.STARTING) {
            event.setCancelled(true);
            continue;
          } 
          if (arena.seekers.contains(player) && arena.seekers.contains(event.getDamager())) {
            event.setCancelled(true);
            continue;
          } 
          if (arena.playersInArena.contains(player) && 
            arena.playersInArena.contains(event.getDamager()) && 
            !arena.seekers.contains(event.getDamager()) && !arena.seekers.contains(player)) {
            event.setCancelled(true);
            continue;
          } 
          player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
          if (event.getDamage() >= player.getHealth()) {
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
            event.setCancelled(true);
            DisguiseAPI.undisguiseToAll((Entity)player);
            W.pBlock.remove(player);
            if (!arena.seekers.contains(player)) {
              if (W.shop.getFile().get(String.valueOf(damager.getName()) + ".tokens") == null) {
                W.shop.getFile().set(String.valueOf(damager.getName()) + ".tokens", Integer.valueOf(0));
                W.shop.save();
              } 
              int damagerTokens = W.shop.getFile().getInt(String.valueOf(damager.getName()) + ".tokens");
              W.shop.getFile().set(String.valueOf(damager.getName()) + ".tokens", 
                  Integer.valueOf(damagerTokens + arena.killTokens));
              W.shop.save();
              MessageM.sendFMessage(damager, ConfigC.normal_addedToken, new String[] { "amount-" + arena.killTokens });
              if (W.shop.getFile().get(String.valueOf(player.getName()) + ".tokens") == null) {
                W.shop.getFile().set(String.valueOf(player.getName()) + ".tokens", Integer.valueOf(0));
                W.shop.save();
              } 
              int playerTokens = W.shop.getFile().getInt(String.valueOf(player.getName()) + ".tokens");
              float addingTokens = arena.hidersTokenWin - 
                arena.timer / arena.gameTime * arena.hidersTokenWin;
              W.shop.getFile().set(String.valueOf(player.getName()) + ".tokens", 
                  Integer.valueOf(playerTokens + (int)addingTokens));
              W.shop.save();
              MessageM.sendFMessage(player, ConfigC.normal_addedToken, new String[] { "amount-" + (int)addingTokens });
              arena.seekers.add(player);
              ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameHiderDied, new String[] { "playername-" + player.getName(), 
                    "left-" + (arena.playersInArena.size() - arena.seekers.size()) });
            } else {
              ArenaHandler.sendFMessage(arena, ConfigC.normal_ingameSeekerDied, new String[] { "playername-" + player.getName(), "secs-" + arena.waitingTimeSeeker });
            } 
            player.getInventory().clear();
            if (arena.seekers.size() >= arena.playersInArena.size()) {
              ArenaHandler.seekersWin(arena);
              continue;
            } 
            DisguiseAPI.undisguiseToAll((Entity)player);
            W.seekertime.put(player, Integer.valueOf(arena.waitingTimeSeeker));
            player.teleport((Location)arena.seekersWarp);
            player.setGameMode(GameMode.SURVIVAL);
            player.setWalkSpeed(0.25F);
          } 
        } 
      }  
  }
}