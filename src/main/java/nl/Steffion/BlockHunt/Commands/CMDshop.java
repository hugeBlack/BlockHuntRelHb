package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.InventoryHandler;
import nl.Steffion.BlockHunt.PermissionsC;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class CMDshop extends DefaultCMD {
  public CMDshop(String name, String label, String args, String argsalias, PermissionsC.Permissions permission, ConfigC help, Boolean enabled, String usage) {
    super(name, label, args, argsalias, permission, help, enabled, usage);
  }

  public boolean execute(Player player, Command cmd, String label, String[] args) {
    InventoryHandler.openShop(player);
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDshop.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */