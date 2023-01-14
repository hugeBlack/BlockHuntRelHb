package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDinfo extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    MessageM.sendFMessage(player, ConfigC.chat_headerhigh, new String[] { "header-" + 
          BlockHunt.pdfFile.getName() });
    MessageM.sendMessage(player, "%A%name%%N made by %A%autors%%N.", new String[] { "name-" + BlockHunt.pdfFile.getName(), "autors-" + 
          (String)BlockHunt.pdfFile.getAuthors().get(0) });
    MessageM.sendMessage(player, "%NVersion: %A%version%%N.", new String[] { "version-" + 
          BlockHunt.pdfFile.getVersion() });
    player.sendMessage("§b§oRaw version: §e§ov" + BlockHunt.pdfFile.getVersion());
    MessageM.sendMessage(player, "%NType %A%helpusage% %Nfor help.", new String[] { "helpusage-" + BlockHunt.CMDhelp.usage });
    MessageM.sendMessage(player, "%NDev-Page: %Ahttps://dev.bukkit.org/projects/blockhuntrel/", new String[0]);
    MessageM.sendMessage(player, "%NDonations are welcome!", new String[0]);
    MessageM.sendMessage(player, "%NOriginal written by %ASteffion%A: %Ahttps://www.curseforge.com/members/steffion55/projects/", new String[0]);
    MessageM.sendFMessage(player, ConfigC.chat_headerhigh, new String[] { "header-&oInfo Page" });
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDinfo.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */