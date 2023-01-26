package nl.Steffion.BlockHunt.Commands.arena;

import java.util.ArrayList;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Commands.DefaultCMD;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDCreate extends DefaultCMD {
  public CMDCreate() {
    super("create", "c", PermissionsC.Permissions.create, ConfigC.help_create, (Boolean) W.config.get(ConfigC.commandEnabled_create), "/BlockHunt <create|c> <arenaname>");
  }

  public boolean execute(Player player, Command cmd, String label, String[] args) {
    if (player != null) {
      if (args.length <= 1) {
        MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + usage);
      } else if ((W.pos1.get(player) != null && W.pos2.get(player) != null) || 
        !W.config.getFile().getBoolean("wandEnabled")) {
        Arena arena;
        if (W.config.getFile().getBoolean("wandEnabled")) {
          arena = new Arena(args[1], W.pos1.get(player), W.pos2.get(player), 12, 3, 1, 50, 20, 300, 30,
              new ArrayList(), null, null, null,  10, 50, 8,
              Arena.ArenaState.WAITING,
              Bukkit.getScoreboardManager().getNewScoreboard());
        } else {
          arena = new Arena(args[1], null, null, 12, 3, 1, 50, 20, 300, 30, new ArrayList(), 
              null, null,  null,  10, 50, 8, Arena.ArenaState.WAITING,
              Bukkit.getScoreboardManager().getNewScoreboard());
        } 
        W.arenas.getFile().set(args[1], arena);
        W.arenas.save();
        W.signs.load();
        W.arenaList.add(arena);
        ScoreboardHandler.createScoreboard(arena);
        MessageM.sendFMessage(player, ConfigC.normal_createCreatedArena, "name-" + args[1]);
      } else {
        MessageM.sendFMessage(player, ConfigC.error_createSelectionFirst);
      } 
    } else {
      MessageM.sendFMessage(player, ConfigC.error_onlyIngame);
    } 
    return true;
  }
}