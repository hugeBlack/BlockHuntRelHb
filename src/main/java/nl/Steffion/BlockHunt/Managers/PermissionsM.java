package nl.Steffion.BlockHunt.Managers;

import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.PermissionsC;
import org.bukkit.entity.Player;

public class PermissionsM {
  public static boolean hasPerm(Player player, PermissionsC.Permissions perm, Boolean message) {
    PermissionsC.PType type = perm.type;
    if (player == null)
      return true; 
    if (type == PermissionsC.PType.ALL)
      return true; 
    if (type == PermissionsC.PType.OP) {
      if (player.isOp())
        return true; 
    } else if (type == PermissionsC.PType.ADMIN) {
      if (player.hasPermission(String.valueOf(PermissionsC.main) + "admin"))
        return true; 
    } else if (type == PermissionsC.PType.MODERATOR) {
      if (player.hasPermission(String.valueOf(PermissionsC.main) + "moderator"))
        return true; 
    } else if (type == PermissionsC.PType.PLAYER && 
      player.hasPermission(String.valueOf(PermissionsC.main) + "player")) {
      return true;
    } 
    if (player.hasPermission("*"))
      return true; 
    if (player.hasPermission(String.valueOf(PermissionsC.main) + "*"))
      return true; 
    if (player.hasPermission(String.valueOf(PermissionsC.main) + perm.perm))
      return true; 
    if (player.hasPermission(String.valueOf(PermissionsC.main) + perm.perm + ".*"))
      return true; 
    if (message.booleanValue())
      MessageM.sendFMessage(player, ConfigC.error_noPermission, new String[0]); 
    return false;
  }
  
  public static boolean hasRawPerm(Player player, PermissionsC.PType type, String perm, Boolean message) {
    if (player == null)
      return true; 
    if (type == PermissionsC.PType.ALL)
      return true; 
    if (type == PermissionsC.PType.OP) {
      if (player.isOp())
        return true; 
    } else if (type == PermissionsC.PType.ADMIN) {
      if (player.hasPermission(String.valueOf(PermissionsC.main) + "admin"))
        return true; 
    } else if (type == PermissionsC.PType.MODERATOR) {
      if (player.hasPermission(String.valueOf(PermissionsC.main) + "moderator"))
        return true; 
    } else if (type == PermissionsC.PType.PLAYER && 
      player.hasPermission(String.valueOf(PermissionsC.main) + "player")) {
      return true;
    } 
    if (player.hasPermission("*"))
      return true; 
    if (player.hasPermission(String.valueOf(PermissionsC.main) + "*"))
      return true; 
    if (player.hasPermission(perm))
      return true; 
    if (player.hasPermission(perm))
      return true; 
    if (message.booleanValue())
      MessageM.sendFMessage(player, ConfigC.error_noPermission, new String[0]); 
    return false;
  }
}