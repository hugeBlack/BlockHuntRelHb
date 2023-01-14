package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.W;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDlist extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    MessageM.sendFMessage(player, ConfigC.chat_headerhigh, new String[] { "header-" + 
          BlockHunt.pdfFile.getName() });
    if (W.arenaList.size() >= 1) {
      MessageM.sendMessage(player, "&7Available arena(s):", new String[0]);
      for (Arena arena : W.arenaList)
        MessageM.sendMessage(player, "%A" + arena.arenaName, new String[0]); 
    } else {
      MessageM.sendMessage(player, "&7&oNo arenas available...", new String[0]);
      MessageM.sendMessage(player, "&7&oCreate an arena first please.", new String[0]);
    } 
    MessageM.sendFMessage(player, ConfigC.chat_headerhigh, new String[] { "header-&oArenas list" });
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDlist.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */