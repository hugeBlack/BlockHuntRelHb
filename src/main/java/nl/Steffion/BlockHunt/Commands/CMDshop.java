package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.InventoryHandler;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDshop extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    InventoryHandler.openShop(player);
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDshop.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */