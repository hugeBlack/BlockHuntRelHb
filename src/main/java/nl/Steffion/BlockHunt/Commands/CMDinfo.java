package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.W;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class CMDinfo extends DefaultCMD {
  public CMDinfo() {
    super("info", "i", PermissionsC.Permissions.info, ConfigC.help_info, (Boolean) W.config.get(ConfigC.commandEnabled_info), "/BlockHunt [info|i]");
  }

  public boolean execute(Player player, Command cmd, String label, String[] args) {
    MessageM.sendFMessage(player, ConfigC.chat_headerhigh, "header-" +
          BlockHunt.pdfFile.getName());
    MessageM.sendMessage(player, "%A%name%%N made by %A%autors%%N.", "name-" + BlockHunt.pdfFile.getName(), "autors-" +
            BlockHunt.pdfFile.getAuthors().get(0));
    MessageM.sendMessage(player, "%NVersion: %A%version%%N.", "version-" +
          BlockHunt.pdfFile.getVersion());
    player.sendMessage("§b§oRaw version: §e§ov" + BlockHunt.pdfFile.getVersion());
    MessageM.sendMessage(player, "%NType %A%helpusage% %Nfor help.", "helpusage-" + CommandManager.commandHelp.usage);
    MessageM.sendMessage(player, "%NDev-Page: %Ahttps://dev.bukkit.org/projects/blockhuntrel/");
    MessageM.sendMessage(player, "%NDonations are welcome!");
    MessageM.sendMessage(player, "%NOriginal written by %ASteffion%A: %Ahttps://www.curseforge.com/members/steffion55/projects/");
    MessageM.sendFMessage(player, ConfigC.chat_headerhigh, "header-&oInfo Page");
    return true;
  }
}