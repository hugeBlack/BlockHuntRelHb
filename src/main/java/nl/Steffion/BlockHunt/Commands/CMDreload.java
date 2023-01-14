package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.ConfigM;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.W;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDreload extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    ConfigM.newFiles();
    W.config.load();
    W.messages.load();
    W.arenas.load();
    W.signs.load();
    W.shop.load();
    for (Arena arena : W.arenaList)
      ArenaHandler.stopArena(arena); 
    ArenaHandler.loadArenas();
    MessageM.sendFMessage(player, ConfigC.normal_reloadedConfigs, new String[0]);
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDreload.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */