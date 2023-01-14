package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDremove extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    if (player != null) {
      if (args.length <= 1) {
        MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, new String[] { "syntax-" + BlockHunt.CMDremove.usage });
      } else {
        for (Arena arena : W.arenaList) {
          if (args[1].equalsIgnoreCase(arena.arenaName)) {
            MessageM.sendFMessage(player, 
                ConfigC.normal_removeRemovedArena, new String[] { "name-" + 
                  args[1] });
            W.arenas.getFile().set(args[1], null);
            for (String sign : W.signs.getFile().getKeys(false)) {
              if (W.signs.getFile().get(String.valueOf(sign) + ".arenaName")
                .toString().equalsIgnoreCase(args[1])) {
                LocationSerializable signLoc = new LocationSerializable(
                    (Location)W.signs.getFile().get(
                      String.valueOf(sign) + ".location"));
                signLoc.getBlock().setType(Material.AIR);
                signLoc.getWorld().playEffect((Location)signLoc, 
                    Effect.MOBSPAWNER_FLAMES, 0);
                signLoc.getWorld().playSound((Location)signLoc, 
                    Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F);
                W.signs.getFile().set(sign, null);
              } 
            } 
            W.arenas.save();
            W.signs.load();
            W.arenaList.remove(arena);
            return true;
          } 
        } 
        MessageM.sendFMessage(player, ConfigC.error_noArena, new String[] { "name-" + 
              args[1] });
      } 
    } else {
      MessageM.sendFMessage(player, ConfigC.error_onlyIngame, new String[0]);
    } 
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDremove.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */