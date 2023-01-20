package nl.Steffion.BlockHunt.Commands.arena;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Commands.DefaultCMD;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CMDSet extends DefaultCMD {
    public CMDSet() {
        super("set", "s", PermissionsC.Permissions.set, ConfigC.help_set, (Boolean) W.config.get(ConfigC.commandEnabled_set), "/BlockHunt <set|s> <arenaname>");
    }

    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            if (args.length <= 1) {
                MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + usage);
            } else {
                String arenaname = args[1];
                InventoryHandler.openPanel(player, arenaname);
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
