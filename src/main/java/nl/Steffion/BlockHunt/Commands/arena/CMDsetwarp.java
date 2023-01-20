package nl.Steffion.BlockHunt.Commands.arena;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Commands.DefaultCMD;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CMDsetwarp extends DefaultCMD {
    public CMDsetwarp() {
        super("setwarp", "sw", PermissionsC.Permissions.setwarp, ConfigC.help_setwarp, (Boolean) W.config.get(ConfigC.commandEnabled_setwarp), "/BlockHunt <setwarp|sw> <lobby|hiders|seekers|spawn> <arenaname>");
    }

    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            if (args.length <= 2) {
                MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + usage);
            } else {
                String arenaname = args[2];
                String warpname = args[1];
                Arena arena = null;
                for (Arena arena2 : W.arenaList) {
                    if (arena2.arenaName.equalsIgnoreCase(arenaname))
                        arena = arena2;
                }
                if (arena != null) {
                    LocationSerializable loc = new LocationSerializable(player.getLocation());
                    if (warpname.equalsIgnoreCase("lobby")) {
                        arena.lobbyWarp = loc;
                        save(arena);
                        MessageM.sendFMessage(player,
                                ConfigC.normal_setwarpWarpSet, "warp-" + warpname);
                    } else if (warpname.equalsIgnoreCase("hiders")) {
                        arena.hidersWarp = loc;
                        save(arena);
                        MessageM.sendFMessage(player,
                                ConfigC.normal_setwarpWarpSet, "warp-" + warpname);
                    } else if (warpname.equalsIgnoreCase("seekers")) {
                        arena.seekersWarp = loc;
                        save(arena);
                        MessageM.sendFMessage(player,
                                ConfigC.normal_setwarpWarpSet, "warp-" + warpname);
                    } else if (warpname.equalsIgnoreCase("spawn")) {
                        arena.spawnWarp = loc;
                        save(arena);
                        MessageM.sendFMessage(player, ConfigC.normal_setwarpWarpSet, "warp-" + warpname);
                    } else {
                        MessageM.sendFMessage(player, ConfigC.error_setwarpWarpNotFound, "warp-" + warpname);
                    }
                } else {
                    MessageM.sendFMessage(player, ConfigC.error_noArena, "name-" + arenaname);
                }
            }
        } else {
            MessageM.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }

    private void save(Arena arena) {
        W.arenas.getFile().set(arena.arenaName, arena);
        W.arenas.save();
        ArenaHandler.loadArenas();
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==2) return new LinkedList<>(Arrays.asList("lobby", "hiders", "seekers", "spawn"));
        if(args.length==3) return ArenaHandler.getArenaNames();
        return null;
    }
}
