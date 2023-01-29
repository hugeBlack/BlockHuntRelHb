package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CMDDebug extends DefaultCMD {
    public CMDDebug() {
        super("debug", "db", PermissionsC.Permissions.create, ConfigC.help_create, (Boolean) W.config.get(ConfigC.commandEnabled_create), "/BlockHunt <debug> <sth>");
    }

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        if(!player.isOp()) return true;
        if(args[1].equals("hideme")){
            for (Player playerShow : Bukkit.getOnlinePlayers())
                playerShow.hidePlayer(player);
        }else if(args[1].equals("showme")){
            for (Player playerShow : Bukkit.getOnlinePlayers())
                playerShow.showPlayer(player);
        }else if(args[1].equals("invs")){
            player.setInvisible(!player.isInvisible());
        }else if(args[1].equals("t3")){
            W.playerArenaMap.get(player).timer = 3;
        }else if(args[1].equals("t281")){
            W.playerArenaMap.get(player).timer = 281;
        }
        return true;
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==2)
            return new LinkedList<>(Arrays.asList("hideme", "showme", "invs", "t3","t281"));
        else
            return null;
    }
}
