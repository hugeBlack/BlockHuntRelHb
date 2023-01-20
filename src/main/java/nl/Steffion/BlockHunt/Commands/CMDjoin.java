package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CMDjoin extends DefaultCMD {
    public CMDjoin() {
        super("join", "j", PermissionsC.Permissions.join, ConfigC.help_join, (Boolean) W.config.get(ConfigC.commandEnabled_join), "/BlockHunt <join|j> <arenaname>");
    }

    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            if (args.length <= 1) {
                MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + usage);
            } else {
                ArenaHandler.playerJoinArena(player, args[1]);
            }
        } else {
            MessageM.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }

    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==2)
            return Helpers.listArenas();
        else
            return null;
    }
}
