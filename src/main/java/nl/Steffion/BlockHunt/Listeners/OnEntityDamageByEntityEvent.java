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
            player = (Player) event.getEntity();
        Player damager = null;
        if (event.getDamager() instanceof Player)
            damager = (Player) event.getDamager();
        //damager和damagee都要存在
        if (player == null) return;
        Arena arena = W.playerArenaMap.get(player);
        //玩家在竞技场中
        if (arena == null) return;
        //jjc还没开始，不能造成伤害
        if (arena.gameState == Arena.ArenaState.WAITING || arena.gameState == Arena.ArenaState.STARTING) {
            event.setCancelled(true);
            return;
        }
        //防止seekers互相伤害
        if (arena.seekers.contains(player) && arena.seekers.contains(damager)) {
            event.setCancelled(true);
            return;
        }
        //防止hiders互相伤害
        if (arena.playersInArena.contains(player) && arena.playersInArena.contains(damager) &&
                !arena.seekers.contains(damager) && !arena.seekers.contains(player)) {
            event.setCancelled(true);
            return;
        }
        //玩家将死
        if (event.getDamage() >= player.getHealth()) {
            event.setCancelled(true);
            arena.playerAboutToDieHandler(player,damager);
        }
    }
}