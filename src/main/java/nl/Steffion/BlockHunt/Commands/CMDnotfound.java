package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDnotfound {
  public static boolean exectue(Player player, Command cmd, String label, String[] args) {
    MessageM.sendFMessage(player, ConfigC.error_commandNotFound, new String[0]);
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDnotfound.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */