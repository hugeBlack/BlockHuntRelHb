package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.PermissionsC;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class CMDjoin extends DefaultCMD {
  public CMDjoin(String name, String label, String args, String argsalias, PermissionsC.Permissions permission, ConfigC help, Boolean enabled, String usage) {
    super(name, label, args, argsalias, permission, help, enabled, usage);
  }

  public boolean execute(Player player, Command cmd, String label, String[] args) {
    if (player != null) {
      if (args.length <= 1) {
        MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, new String[] { "syntax-" + usage });
      } else {
        ArenaHandler.playerJoinArena(player, args[1]);
      } 
    } else {
      MessageM.sendFMessage(player, ConfigC.error_onlyIngame, new String[0]);
    } 
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDjoin.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */