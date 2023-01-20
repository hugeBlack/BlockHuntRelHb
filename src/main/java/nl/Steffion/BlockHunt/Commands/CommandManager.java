package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.Commands.arena.*;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Managers.PermissionsM;
import nl.Steffion.BlockHunt.W;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {
    //用于主指令的tab列表
    public static DefaultCMD commandHelp = null;
    //储存所有子指令
    public static ArrayList<DefaultCMD> commands = new ArrayList<>();

    private static void registerCMD(DefaultCMD cmd) {
        commands.add(cmd);
    }

    static  {
        registerCMD(new CMDinfo());
        commandHelp = new CMDhelp();
        registerCMD(commandHelp);
        registerCMD(new CMDreload());
        registerCMD(new CMDjoin());
        registerCMD(new CMDleave());
        registerCMD(new CMDlist());
        registerCMD(new CMDshop());
        registerCMD(new CMDstart());
        if (W.config.getFile().getBoolean("wandEnabled"))
            registerCMD(new CMDwand());
        registerCMD(new CMDcreate());
        registerCMD(new CMDset());
        registerCMD(new CMDsetwarp());
        registerCMD(new CMDremove());
        registerCMD(new CMDtokens());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player)
            player = (Player) sender;
        for (DefaultCMD command : CommandManager.commands) {
            String[] argsSplit = null;
            String[] argsSplitAlias = null;
            //判断子指令的别名，用/分隔
            if (command.args != null && command.argsAlias != null) {
                argsSplit = command.args.split("/");
                argsSplitAlias = command.argsAlias.split("/");
            }

            boolean equals = true;
            if (argsSplit == null) {
                if (args.length == 0) {
                    equals = true;
                } else {
                    equals = false;
                }
            } else if (args.length >= argsSplit.length) {
                for (int i2 = argsSplit.length - 1; i2 >= 0; i2--) {
                    int loc = argsSplit.length - i2 - 1;
                    if (!argsSplit[loc].equalsIgnoreCase(args[loc]) && !argsSplitAlias[loc].equalsIgnoreCase(args[loc]))
                        equals = false;
                }
            } else {
                equals = false;
            }
            if (equals) {
                if (PermissionsM.hasPerm(player, command.permission, Boolean.TRUE))
                    if (command.enabled) {
                        command.execute(player, cmd, label, args);
                    } else {
                        MessageM.sendFMessage(player, ConfigC.error_commandNotEnabled);
                    }
                return true;
            }

        }
        MessageM.sendFMessage(player, ConfigC.error_commandNotFound);
        return true;
    }

}
