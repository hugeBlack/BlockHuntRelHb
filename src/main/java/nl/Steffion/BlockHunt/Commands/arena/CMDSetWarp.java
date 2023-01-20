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

public class CMDSetWarp extends DefaultCMD {
    public CMDSetWarp() {
        super("setwarp", "sw", PermissionsC.Permissions.setwarp, ConfigC.help_setwarp, (Boolean) W.config.get(ConfigC.commandEnabled_setwarp), "/BlockHunt <setwarp|sw> <lobby|hiders|seekers|spawn> <arenaname>");
    }

    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            if (args.length <= 2) {
                MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + usage);
            } else {
                String warpName = args[1];
                Arena arena = ArenaHandler.getArenaByName(args[2]);
                if (arena != null) {
                    LocationSerializable loc = new LocationSerializable(player.getLocation());
                    if (warpName.equalsIgnoreCase("lobby")) {
                        arena.lobbyWarp = loc;
                        save(arena);
                        MessageM.sendFMessage(player,
                                ConfigC.normal_setwarpWarpSet, "warp-" + warpName);
                    } else if (warpName.equalsIgnoreCase("hiders")) {
                        arena.hidersWarp = loc;
                        save(arena);
                        MessageM.sendFMessage(player,
                                ConfigC.normal_setwarpWarpSet, "warp-" + warpName);
                    } else if (warpName.equalsIgnoreCase("seekers")) {
                        arena.seekersWarp = loc;
                        save(arena);
                        MessageM.sendFMessage(player,
                                ConfigC.normal_setwarpWarpSet, "warp-" + warpName);
                    } else if (warpName.equalsIgnoreCase("spawn")) {
                        arena.spawnWarp = loc;
                        save(arena);
                        MessageM.sendFMessage(player, ConfigC.normal_setwarpWarpSet, "warp-" + warpName);
                    } else {
                        MessageM.sendFMessage(player, ConfigC.error_setwarpWarpNotFound, "warp-" + warpName);
                    }
                } else {
                    MessageM.sendFMessage(player, ConfigC.error_noArena, "name-" + args[2]);
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
