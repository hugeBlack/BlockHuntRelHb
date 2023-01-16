package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.util.List;

public class CMDtokens extends DefaultCMD {
    public CMDtokens(String name, String label, String args, String argsAlias, PermissionsC.Permissions permission, ConfigC help, Boolean enabled, String usage) {
        super(name, label, args, argsAlias, permission, help, enabled, usage);
    }

    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if (args.length <= 3) {
            MessageM.sendFMessage(player, ConfigC.error_notEnoughArguments, "syntax-" + usage);
        } else {
            String option = args[1];
            String playerName = args[2];
            int amount = 0;
            try {
                amount = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                MessageM.sendFMessage(player, ConfigC.error_notANumber, "1-" + args[3]);
                return true;
            }
            Player tokenPlayer = Bukkit.getPlayer(playerName);
            if (tokenPlayer == null) {
                MessageM.sendFMessage(player, ConfigC.error_tokensPlayerNotOnline, "playername-" + playerName);
                return true;
            }
            String name = "§8Console";
            if (player != null)
                name = player.getName();
            if (option.equalsIgnoreCase("set")) {
                W.shop.getFile().set(tokenPlayer.getName() + ".tokens", amount);
                W.shop.save();
                MessageM.sendFMessage(player, ConfigC.normal_tokensChanged, "option-Set", "playername-" + tokenPlayer.getName(), "option2-to", "amount-" + amount);
                MessageM.sendFMessage(tokenPlayer, ConfigC.normal_tokensChangedPerson, "option-set", "playername-" + name, "option2-to", "amount-" + amount);
            } else if (option.equalsIgnoreCase("add")) {
                int tokens = 0;
                if (W.shop.getFile().getInt(tokenPlayer.getName() + ".tokens") != 0)
                    tokens = W.shop.getFile().getInt(tokenPlayer.getName() + ".tokens");
                W.shop.getFile().set(tokenPlayer.getName() + ".tokens", tokens + amount);
                W.shop.save();
                MessageM.sendFMessage(player, ConfigC.normal_tokensChanged, "option-Added", "playername-" + tokenPlayer.getName(), "option2-to", "amount-" + amount);
                MessageM.sendFMessage(tokenPlayer, ConfigC.normal_tokensChangedPerson, "option-added", "playername-" + name, "option2-to", "amount-" + amount);
            } else if (option.equalsIgnoreCase("take")) {
                int tokens = 0;
                if (W.shop.getFile().getInt(tokenPlayer.getName() + ".tokens") != 0)
                    tokens = W.shop.getFile().getInt(tokenPlayer.getName() + ".tokens");
                W.shop.getFile().set(tokenPlayer.getName() + ".tokens", tokens - amount);
                W.shop.save();
                MessageM.sendFMessage(player, ConfigC.normal_tokensChanged, "option-Took", "playername-" + tokenPlayer.getName(), "option2-from", "amount-" + amount);
                MessageM.sendFMessage(tokenPlayer, ConfigC.normal_tokensChangedPerson, "option-took", "playername-" + name, "option2-from", "amount-" + amount);
            } else {
                MessageM.sendFMessage(player, ConfigC.error_tokensUnknownsetting, "option-" + option);
            }
        }
        return true;
    }
}