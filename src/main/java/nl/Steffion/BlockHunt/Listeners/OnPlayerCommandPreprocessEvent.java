package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Managers.PermissionsM;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.W;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnPlayerCommandPreprocessEvent implements Listener {
  @EventHandler(priority = EventPriority.HIGH)
  public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
    Player player = event.getPlayer();
    Arena arena = W.playerArenaMap.get(player);
      if (arena!=null) {
        String m = event.getMessage();
        if (m.startsWith("/blockhunt") || m.startsWith("/bh") || 
          m.startsWith("/seekandfind") || 
          m.startsWith("/seekandfind") || m.startsWith("/saf") || 
          m.startsWith("/sf") || m.startsWith("/hideandseek") || 
          m.startsWith("/has") || m.startsWith("/hs") || 
          m.startsWith("/ban") || m.startsWith("/kick") || 
          m.startsWith("/tempban") || m.startsWith("/mute") || 
          m.startsWith("/reload"))
          return; 
        for (String command : arena.allowedCommands) {
          if (m.startsWith("/" + command))
            return; 
        } 
        if (PermissionsM.hasPerm(player, PermissionsC.Permissions.allcommands, Boolean.FALSE))
          return; 
        MessageM.sendFMessage(player, ConfigC.warning_unableToCommand);
        event.setCancelled(true);

    } 
  }
}