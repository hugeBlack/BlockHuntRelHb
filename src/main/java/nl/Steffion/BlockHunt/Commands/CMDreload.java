package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Managers.ConfigM;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class CMDreload extends DefaultCMD {
  public CMDreload(String name, String label, String args, String argsalias, PermissionsC.Permissions permission, ConfigC help, Boolean enabled, String usage) {
    super(name, label, args, argsalias, permission, help, enabled, usage);
  }

  public boolean execute(Player player, Command cmd, String label, String[] args) {
    ConfigM.newFiles();
    W.config.load();
    W.messages.load();
    W.arenas.load();
    W.signs.load();
    W.shop.load();
    for (Arena arena : W.arenaList)
      arena.stop();
    ArenaHandler.loadArenas();
    MessageM.sendFMessage(player, ConfigC.normal_reloadedConfigs, new String[0]);
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDreload.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */