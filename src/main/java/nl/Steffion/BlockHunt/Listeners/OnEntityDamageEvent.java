package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.logging.Level;

public class OnEntityDamageEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        Entity ent = event.getEntity();
        if (ent instanceof Player) {
            Player player = (Player) event.getEntity();
              //玩家只能受到虚空或者其他玩家的伤害
            if (W.playerArenaMap.containsKey(player) )
                if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){

                }else if((event.getCause() == EntityDamageEvent.DamageCause.VOID)) {
                    W.playerArenaMap.get(player).playerAboutToDieHandler(player,null);
                    event.setCancelled(true);
                }else if(event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE){

                }else{
                    event.setCancelled(true);
                }
            }
    }
}