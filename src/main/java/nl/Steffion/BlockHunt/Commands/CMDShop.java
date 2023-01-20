package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.InventoryHandler;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.W;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDShop extends DefaultCMD {
    public CMDShop() {
        super("shop", "sh", PermissionsC.Permissions.shop, ConfigC.help_shop, (Boolean) W.config.get(ConfigC.commandEnabled_shop), "/BlockHunt <shop|sh>");
    }

    public boolean execute(Player player, Command cmd, String label, String[] args) {
        InventoryHandler.openShop(player);
        return true;
    }
}