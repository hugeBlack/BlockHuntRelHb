package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import nl.Steffion.BlockHunt.W;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDsetwarp extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    if (player != null) {
      if (args.length <= 2) {
        MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, new String[] { "syntax-" + BlockHunt.CMDsetwarp.usage });
      } else {
        String arenaname = args[2];
        String warpname = args[1];
        Arena arena = null;
        for (Arena arena2 : W.arenaList) {
          if (arena2.arenaName.equalsIgnoreCase(arenaname))
            arena = arena2; 
        } 
        if (arena != null) {
          LocationSerializable loc = new LocationSerializable(
              player.getLocation());
          if (warpname.equalsIgnoreCase("lobby")) {
            arena.lobbyWarp = loc;
            save(arena);
            MessageM.sendFMessage(player, 
                ConfigC.normal_setwarpWarpSet, new String[] { "warp-" + 
                  warpname });
          } else if (warpname.equalsIgnoreCase("hiders")) {
            arena.hidersWarp = loc;
            save(arena);
            MessageM.sendFMessage(player, 
                ConfigC.normal_setwarpWarpSet, new String[] { "warp-" + 
                  warpname });
          } else if (warpname.equalsIgnoreCase("seekers")) {
            arena.seekersWarp = loc;
            save(arena);
            MessageM.sendFMessage(player, 
                ConfigC.normal_setwarpWarpSet, new String[] { "warp-" + 
                  warpname });
          } else if (warpname.equalsIgnoreCase("spawn")) {
            arena.spawnWarp = loc;
            save(arena);
            MessageM.sendFMessage(player, 
                ConfigC.normal_setwarpWarpSet, new String[] { "warp-" + 
                  warpname });
          } else {
            MessageM.sendFMessage(player, 
                ConfigC.error_setwarpWarpNotFound, new String[] { "warp-" + 
                  warpname });
          } 
        } else {
          MessageM.sendFMessage(player, ConfigC.error_noArena, new String[] { "name-" + arenaname });
        } 
      } 
    } else {
      MessageM.sendFMessage(player, ConfigC.error_onlyIngame, new String[0]);
    } 
    return true;
  }
  
  public void save(Arena arena) {
    W.arenas.getFile().set(arena.arenaName, arena);
    W.arenas.save();
    ArenaHandler.loadArenas();
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDsetwarp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */