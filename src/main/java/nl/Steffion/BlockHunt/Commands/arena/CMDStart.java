package nl.Steffion.BlockHunt.Commands.arena;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Commands.DefaultCMD;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CMDStart extends DefaultCMD {


    public CMDStart() {
        super("start", "go", PermissionsC.Permissions.start, ConfigC.help_start, (Boolean) W.config.get(ConfigC.commandEnabled_start), "/BlockHunt <start|go> <arenaname>");
    }

    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            if (args.length <= 1) {
                MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + usage);
            } else {
                Arena arena = ArenaHandler.getArenaByName(args[1]);
                if (arena != null) {
                    if (arena.gameState.equals(Arena.ArenaState.WAITING)) {
                        if (arena.playersInArena.size() >= 2) {
                            arena.timer = 11;
                            arena.gameState = Arena.ArenaState.STARTING;
                            MessageM.sendFMessage(player, ConfigC.normal_startForced, "arenaname-" + arena.arenaName);
                        } else {
                            MessageM.sendFMessage(player, ConfigC.warning_lobbyNeedAtleast, "1-2");
                        }
                    } else if (arena.gameState.equals(Arena.ArenaState.STARTING)) {
                        if (arena.playersInArena.size() < arena.maxPlayers) {
                            if (arena.timer >= 10)
                                arena.timer = 11;
                        } else {
                            arena.timer = 1;
                        }
                        MessageM.sendFMessage(player,
                                ConfigC.normal_startForced, "arenaname-" + arena.arenaName);
                    }
                } else {
                    MessageM.sendFMessage(player, ConfigC.error_noArena, "name-" + args[1]);
                }
            }
        } else {
            MessageM.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }
    @Override
    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==2) return ArenaHandler.getArenaNames();
        return null;
    }
}