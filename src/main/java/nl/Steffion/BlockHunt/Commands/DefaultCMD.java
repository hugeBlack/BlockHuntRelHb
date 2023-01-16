package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.PermissionsC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class DefaultCMD {
    public String name;
    public String label;
    public String args;
    public String argsAlias;
    public PermissionsC.Permissions permission;
    public ConfigC help;
    public boolean enabled;
    public String usage;

    public DefaultCMD(String name, String label, String args, String argsAlias, PermissionsC.Permissions permission, ConfigC help, Boolean enabled, String usage) {
        this.name = name;
        this.label = label;
        this.args = args;
        this.argsAlias = argsAlias;
        this.permission = permission;
        this.help = help;
        this.enabled = enabled;
        this.usage = usage;
    }

    public abstract boolean execute(Player player, Command cmd, String label, String[] args);

    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}