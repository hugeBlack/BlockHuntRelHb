package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.*;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CMDKit extends DefaultCMD {
    public CMDKit() {
        //TODO 权限节点
        super("kit", "k", PermissionsC.Permissions.start, ConfigC.help_start, (Boolean) W.config.get(ConfigC.commandEnabled_start), "/BlockHunt <kit|k> <create|update|load> <kitName>");
    }

    @Override
    public boolean execute(Player player, Command cmd, String label, String[] args) {
        Kit kit = KitHandler.kitMap.get(args[2]);
        if(args[1].equals("create")){
            if(kit!=null){
                MessageM.sendFMessage(player, ConfigC.error_kitAlreadyExist, "name-" + args[1]);
                return true;
            }
            KitHandler.newKit(player,args[2]);
            MessageM.sendFMessage(player, ConfigC.normal_kitCreated, "name-" + args[1]);
            return true;
        }
        if(kit==null){
            MessageM.sendFMessage(player, ConfigC.error_kitDoNotExist, "name-" + args[1]);
            return true;
        }
        if(args[1].equals("update")){
            kit.armor = player.getInventory().getArmorContents().clone();
            kit.inventory = player.getInventory().getContents().clone();
            KitHandler.saveKit(kit);
            MessageM.sendFMessage(player, ConfigC.normal_kitUpdated, "name-" + args[1]);
            return true;
        }
        if(args[1].equals("load")){
            kit.give(player);
            MessageM.sendFMessage(player, ConfigC.normal_kitLoaded, "name-" + args[1]);
            return true;
        }
        MessageM.sendFMessage(player, ConfigC.error_kitOperationNotAvailable, "name-" + args[1]);
        return true;
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==2) return new LinkedList<>(Arrays.asList("create", "update", "load"));
        if(args.length==3 && !args[1].equals("create")) return KitHandler.getKitNames();
        return null;
    }
}
