package nl.Steffion.BlockHunt.Commands;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class BHTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length<=1){
            List<String> responses = Lists.newArrayList();
            for(DefaultCMD cmd: CommandManager.commands){
                responses.add(cmd.name);
            }
            return responses;
        }else{
            for(DefaultCMD cmd: CommandManager.commands){
                if(cmd.name.equals(args[0]) || cmd.argsAlias.equals(args[0])) return cmd.tabCompleter(sender, command, label, args);
            }
            return null;
        }
    }
}
