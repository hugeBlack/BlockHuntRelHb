package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Managers.ConfigM;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class CMDreload extends DefaultCMD {
    public CMDreload() {
        super("reload", "r", PermissionsC.Permissions.reload, ConfigC.help_reload, (Boolean) W.config.get(ConfigC.commandEnabled_reload), "/BlockHunt <reload|r>");
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
        MessageM.sendFMessage(player, ConfigC.normal_reloadedConfigs);
        return true;
    }
}
