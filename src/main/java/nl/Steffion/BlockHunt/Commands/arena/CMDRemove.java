package nl.Steffion.BlockHunt.Commands.arena;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Commands.DefaultCMD;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class CMDRemove extends DefaultCMD {
    public CMDRemove() {
        super("remove", "del", PermissionsC.Permissions.remove, ConfigC.help_remove, (Boolean) W.config.get(ConfigC.commandEnabled_remove), "/BlockHunt <remove|del> <arenaname>");
    }

    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            if (args.length <= 1) {
                MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + usage);
            } else {
                String arenaName = args[1];
                Arena arena = ArenaHandler.getArenaByName(arenaName);
                if (arena != null) {
                    MessageM.sendFMessage(player, ConfigC.normal_removeRemovedArena, "name-" + arenaName);
                    W.arenas.getFile().set(arenaName, null);
                    for (String sign : W.signs.getFile().getKeys(false)) {
                        if (W.signs.getFile().get(sign + ".arenaName").toString().equalsIgnoreCase(arenaName)) {
                            LocationSerializable signLoc = new LocationSerializable((Location) Objects.requireNonNull(W.signs.getFile().get(sign + ".location")));
                            signLoc.getBlock().setType(Material.AIR);
                            signLoc.getWorld().playEffect(signLoc, Effect.MOBSPAWNER_FLAMES, 0);
                            signLoc.getWorld().playSound(signLoc, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F);
                            W.signs.getFile().set(sign, null);
                        }
                    }
                    W.arenas.save();
                    W.signs.load();
                    W.arenaList.remove(arena);
                    return true;

                }
                MessageM.sendFMessage(player, ConfigC.error_noArena, "name-" + arenaName);
            }
        } else {
            MessageM.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 2) return null;
        return ArenaHandler.getArenaNames();
    }
}