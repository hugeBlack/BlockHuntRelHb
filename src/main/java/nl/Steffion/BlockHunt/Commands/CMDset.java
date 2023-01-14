package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.InventoryHandler;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDset extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    if (player != null) {
      if (args.length <= 1) {
        MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, new String[] { "syntax-" + BlockHunt.CMDset.usage });
      } else {
        String arenaname = args[1];
        InventoryHandler.openPanel(player, arenaname);
      } 
    } else {
      MessageM.sendFMessage(player, ConfigC.error_onlyIngame, new String[0]);
    } 
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDset.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */