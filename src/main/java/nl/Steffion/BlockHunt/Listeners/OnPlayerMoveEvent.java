package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMoveEvent implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Arena arena = W.playerArenaMap.get(player);
        if (arena != null && arena.playersInArena.contains(player) && arena.gameState == Arena.ArenaState.INGAME) {
            W.moveLoc.put(player, player.getLocation());
            if (!W.config.getFile().getBoolean("wandEnabled"))
                return;
            Location loc = player.getLocation();
            if (!arena.isLocationInArena(loc)) {
                event.setCancelled(true);
                player.playEffect(loc, Effect.ENDER_SIGNAL, null);
                player.playSound(loc, Sound.ENTITY_GHAST_SHOOT, 1.0F, 1.0F);
                player.teleport(arena.hidersWarp);
            }
        }

    }
}