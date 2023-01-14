package nl.Steffion.BlockHunt.Commands;

import java.util.ArrayList;
import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.ScoreboardHandler;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDcreate extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    if (player != null) {
      if (args.length <= 1) {
        MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, new String[] { "syntax-" + BlockHunt.CMDcreate.usage });
      } else if ((W.pos1.get(player) != null && W.pos2.get(player) != null) || 
        !W.config.getFile().getBoolean("wandEnabled")) {
        Arena arena;
        if (W.config.getFile().getBoolean("wandEnabled")) {
          arena = new Arena(args[1], (LocationSerializable)W.pos1.get(player), (LocationSerializable)W.pos2.get(player), 12, 3, 1, 50, 20, 300, 30, 
              new ArrayList(), null, null, null, null, new ArrayList(), 
              new ArrayList(), new ArrayList(), 10, 50, 8, new ArrayList(), 
              Arena.ArenaState.WAITING, 0, new ArrayList(), 
              Bukkit.getScoreboardManager().getNewScoreboard());
        } else {
          arena = new Arena(args[1], null, null, 12, 3, 1, 50, 20, 300, 30, new ArrayList(), 
              null, null, null, null, new ArrayList(), new ArrayList(), 
              new ArrayList(), 10, 50, 8, new ArrayList(), Arena.ArenaState.WAITING, 0, 
              new ArrayList(), Bukkit.getScoreboardManager().getNewScoreboard());
        } 
        W.arenas.getFile().set(args[1], arena);
        W.arenas.save();
        W.signs.load();
        W.arenaList.add(arena);
        ScoreboardHandler.createScoreboard(arena);
        MessageM.sendFMessage(player, ConfigC.normal_createCreatedArena, new String[] { "name-" + args[1] });
      } else {
        MessageM.sendFMessage(player, ConfigC.error_createSelectionFirst, new String[0]);
      } 
    } else {
      MessageM.sendFMessage(player, ConfigC.error_onlyIngame, new String[0]);
    } 
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDcreate.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */