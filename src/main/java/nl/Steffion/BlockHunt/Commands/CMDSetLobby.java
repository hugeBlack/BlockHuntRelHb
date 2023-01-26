package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.ConfigM;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import nl.Steffion.BlockHunt.W;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDSetLobby extends DefaultCMD {

    public CMDSetLobby() {
        super("setlobby", "slo", PermissionsC.Permissions.create, ConfigC.help_reload, (Boolean) W.config.get(ConfigC.commandEnabled_reload), "/BlockHunt setlobby");
    }
    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        LocationSerializable loc = new LocationSerializable(player.getLocation());
        MessageM.sendFMessage(player, ConfigC.normal_setwarpWarpSet, "warp-spawn");
        ConfigC.lobbyPosition.value = loc;
        W.config.getFile().set("lobbyPosition",loc);
        W.config.save();
        return false;
    }
}
