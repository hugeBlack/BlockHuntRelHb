package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.Managers.PermissionsM;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import nl.Steffion.BlockHunt.SignsHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnSignChangeEvent implements Listener {
  @EventHandler(priority = EventPriority.NORMAL)
  public void onSignChangeEvent(SignChangeEvent event) {
    Player player = event.getPlayer();
    String[] lines = event.getLines();
    if (lines[0] != null)
      if (lines[0].equalsIgnoreCase("[" + BlockHunt.pdfFile.getName() + 
          "]") && 
        PermissionsM.hasPerm(player, PermissionsC.Permissions.signcreate, Boolean.valueOf(true)))
        SignsHandler.createSign(event, lines, 
            new LocationSerializable(event.getBlock()
              .getLocation()));  
  }
}