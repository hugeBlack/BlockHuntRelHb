package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CMDtokens extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    if (args.length <= 3) {
      MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, new String[] { "syntax-" + BlockHunt.CMDtokens.usage });
    } else {
      String option = args[1];
      String playerName = args[2];
      int amount = 0;
      try {
        amount = Integer.valueOf(args[3]).intValue();
      } catch (NumberFormatException e) {
        MessageM.sendFMessage(player, ConfigC.error_notANumber, new String[] { "1-" + 
              args[3] });
        return true;
      } 
      Player tokenPlayer = Bukkit.getPlayer(playerName);
      if (tokenPlayer == null) {
        MessageM.sendFMessage(player, 
            ConfigC.error_tokensPlayerNotOnline, new String[] { "playername-" + 
              playerName });
        return true;
      } 
      String name = "§8Console";
      if (player != null)
        name = player.getName(); 
      if (option.equalsIgnoreCase("set")) {
        W.shop.getFile().set(String.valueOf(tokenPlayer.getName()) + ".tokens", Integer.valueOf(amount));
        W.shop.save();
        MessageM.sendFMessage(player, ConfigC.normal_tokensChanged, new String[] { "option-Set", "playername-" + tokenPlayer.getName(), 
              "option2-to", "amount-" + amount });
        MessageM.sendFMessage(tokenPlayer, 
            ConfigC.normal_tokensChangedPerson, new String[] { "option-set", 
              "playername-" + name, "option2-to", 
              "amount-" + amount });
      } else if (option.equalsIgnoreCase("add")) {
        int tokens = 0;
        if (W.shop.getFile().getInt(String.valueOf(tokenPlayer.getName()) + ".tokens") != 0)
          tokens = W.shop.getFile().getInt(
              String.valueOf(tokenPlayer.getName()) + ".tokens"); 
        W.shop.getFile().set(String.valueOf(tokenPlayer.getName()) + ".tokens", 
            Integer.valueOf(tokens + amount));
        W.shop.save();
        MessageM.sendFMessage(player, ConfigC.normal_tokensChanged, new String[] { "option-Added", "playername-" + tokenPlayer.getName(), 
              "option2-to", "amount-" + amount });
        MessageM.sendFMessage(tokenPlayer, 
            ConfigC.normal_tokensChangedPerson, new String[] { "option-added", 
              "playername-" + name, "option2-to", 
              "amount-" + amount });
      } else if (option.equalsIgnoreCase("take")) {
        int tokens = 0;
        if (W.shop.getFile().getInt(String.valueOf(tokenPlayer.getName()) + ".tokens") != 0)
          tokens = W.shop.getFile().getInt(
              String.valueOf(tokenPlayer.getName()) + ".tokens"); 
        W.shop.getFile().set(String.valueOf(tokenPlayer.getName()) + ".tokens", 
            Integer.valueOf(tokens - amount));
        W.shop.save();
        MessageM.sendFMessage(player, ConfigC.normal_tokensChanged, new String[] { "option-Took", "playername-" + tokenPlayer.getName(), 
              "option2-from", "amount-" + amount });
        MessageM.sendFMessage(tokenPlayer, 
            ConfigC.normal_tokensChangedPerson, new String[] { "option-took", 
              "playername-" + name, "option2-from", 
              "amount-" + amount });
      } else {
        MessageM.sendFMessage(player, 
            ConfigC.error_tokensUnknownsetting, new String[] { "option-" + option });
      } 
    } 
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDtokens.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */