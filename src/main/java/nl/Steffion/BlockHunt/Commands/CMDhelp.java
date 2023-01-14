package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.CommandM;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Managers.PermissionsM;
import nl.Steffion.BlockHunt.W;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDhelp extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    int amountCommands = 0;
    for (CommandM command : W.commands) {
      if (command.usage != null)
        amountCommands++; 
    } 
    int maxPages = Math.round((amountCommands / 3));
    if (maxPages <= 0)
      maxPages = 1; 
    if (args.length == 1) {
      int page = 1;
      MessageM.sendFMessage(player, ConfigC.chat_headerhigh, new String[] { "header-" + 
            BlockHunt.pdfFile.getName() + " %Nhelp page %A" + 
            page + "%N/%A" + maxPages });
      int i = 1;
      for (CommandM command : W.commands) {
        if (i <= 4 && 
          command.usage != null) {
          if (PermissionsM.hasPerm(player, command.permission, 
              Boolean.valueOf(false))) {
            MessageM.sendMessage(
                player, 
                "%A" + 
                command.usage + 
                "%N - " + 
                W.messages.getFile().get(
                  command.help.location), new String[0]);
          } else {
            MessageM.sendMessage(
                player, 
                "%W" + 
                command.usage + 
                "%N - " + 
                W.messages.getFile().get(
                  command.help.location), new String[0]);
          } 
          i++;
        } 
      } 
      MessageM.sendFMessage(player, ConfigC.chat_headerhigh, new String[] { "header-&oHelp Page" });
    } else {
      int page = 1;
      try {
        page = Integer.valueOf(args[1]).intValue();
      } catch (NumberFormatException e) {
        page = 1;
      } 
      if (maxPages < page)
        maxPages = page; 
      MessageM.sendFMessage(player, ConfigC.chat_headerhigh, new String[] { "header-" + 
            BlockHunt.pdfFile.getName() + " %Nhelp page %A" + 
            page + "%N/%A" + maxPages });
      int i = 1;
      for (CommandM command : W.commands) {
        if (i <= page * 4 + 4 && 
          command.usage != null) {
          if (i >= (page - 1) * 4 + 1 && 
            i <= (page - 1) * 4 + 4)
            if (PermissionsM.hasPerm(player, 
                command.permission, Boolean.valueOf(false))) {
              MessageM.sendMessage(
                  player, 
                  "%A" + 
                  command.usage + 
                  "%N - " + 
                  W.messages.getFile().get(
                    command.help.location), new String[0]);
            } else {
              MessageM.sendMessage(
                  player, 
                  "%W" + 
                  command.usage + 
                  "%N - " + 
                  W.messages.getFile().get(
                    command.help.location), new String[0]);
            }  
          i++;
        } 
      } 
      MessageM.sendFMessage(player, ConfigC.chat_headerhigh, new String[] { "header-&oHelp Page" });
    } 
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDhelp.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */