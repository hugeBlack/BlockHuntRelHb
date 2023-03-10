package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.W;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDLeave extends DefaultCMD {
    public CMDLeave() {
        super("leave", "l", PermissionsC.Permissions.leave, ConfigC.help_leave, (Boolean) W.config.get(ConfigC.commandEnabled_leave), "/BlockHunt <leave|l>");
    }

    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (player != null) {
            ArenaHandler.playerLeaveArena(player, true, true);
        } else {
            MessageM.sendFMessage(player, ConfigC.error_onlyIngame);
        }
        return true;
    }
}
