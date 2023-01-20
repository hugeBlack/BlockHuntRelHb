package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.Managers.PermissionsM;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class OnBlockBreakEvent implements Listener {
  @EventHandler(priority = EventPriority.NORMAL)
  public void onBlockBreakEvent(BlockBreakEvent event) {
    Player player = event.getPlayer();
    if(W.playerArenaMap.containsKey(player)) event.setCancelled(true);
    if ((event.getBlock().equals(Material.OAK_SIGN) || 
      event.getBlock().equals(Material.OAK_WALL_SIGN)) && 
      !PermissionsM.hasPerm(player, PermissionsC.Permissions.signcreate, Boolean.valueOf(true)))
      event.setCancelled(true); 
  }
}
