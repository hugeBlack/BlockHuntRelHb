package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Managers.PermissionsM;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.W;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CMDhelp extends DefaultCMD {
    public CMDhelp(String name, String label, String args, String argsalias, PermissionsC.Permissions permission, ConfigC help, Boolean enabled, String usage) {
        super(name, label, args, argsalias, permission, help, enabled, usage);
    }

    public boolean execute(Player player, Command cmd, String label, String[] args) {
        int amountCommands = 0;
        for (DefaultCMD command : CommandManager.commands) {
            if (command.usage != null)
                amountCommands++;
        }
        int maxPages = Math.round((amountCommands / 3));
        if (maxPages <= 0)
            maxPages = 1;
        if (args.length == 1) {
            int page = 1;
            MessageM.sendFMessage(player, ConfigC.chat_headerhigh, "header-" + BlockHunt.pdfFile.getName() + " %Nhelp page %A" + page + "%N/%A" + maxPages);
            int i = 1;
            for (DefaultCMD command : CommandManager.commands) {
                if (i <= 4 &&
                        command.usage != null) {
                    if (PermissionsM.hasPerm(player, command.permission, Boolean.FALSE)) {
                        MessageM.sendMessage(player, "%A" + command.usage + "%N - " + W.messages.getFile().get(command.help.fileKey));
                    } else {
                        MessageM.sendMessage(player, "%W" + command.usage + "%N - " + W.messages.getFile().get(command.help.fileKey));
                    }
                    i++;
                }
            }
            MessageM.sendFMessage(player, ConfigC.chat_headerhigh, "header-&oHelp Page");
        } else {
            int page = 1;
            try {
                page = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                page = 1;
            }
            if (maxPages < page)
                maxPages = page;
            MessageM.sendFMessage(player, ConfigC.chat_headerhigh, "header-" +
                    BlockHunt.pdfFile.getName() + " %Nhelp page %A" +
                    page + "%N/%A" + maxPages);
            int i = 1;
            for (DefaultCMD command : CommandManager.commands) {
                if (i <= page * 4 + 4 &&
                        command.usage != null) {
                    if (i >= (page - 1) * 4 + 1 &&
                            i <= (page - 1) * 4 + 4)
                        if (PermissionsM.hasPerm(player, command.permission, Boolean.FALSE)) {
                            MessageM.sendMessage(player, "%A" + command.usage + "%N - " + W.messages.getFile().get(command.help.fileKey));
                        } else {
                            MessageM.sendMessage(player, "%W" + command.usage + "%N - " + W.messages.getFile().get(command.help.fileKey));
                        }
                    i++;
                }
            }
            MessageM.sendFMessage(player, ConfigC.chat_headerhigh, "header-&oHelp Page");
        }
        return true;
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        return Arrays.asList("1", "2", "3", "4");
    }
}