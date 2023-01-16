package nl.Steffion.BlockHunt.Commands;

import nl.Steffion.BlockHunt.Commands.arena.*;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.W;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    //用于主指令的tab列表
    public static DefaultCMD commandHelp = null;
    //储存所有子指令
    public static ArrayList<DefaultCMD> commands = new ArrayList<>();

    private static void registerCMD(DefaultCMD cmd){
        commands.add(cmd);
    }

    public static void regCMDs(){
        registerCMD(new CMDinfo("BlockHunt", "BlockHunt", null, null, PermissionsC.Permissions.info, ConfigC.help_info, (Boolean) W.config.get(ConfigC.commandEnabled_info), null));
        registerCMD(new CMDinfo("BlockHunt INFO", "BlockHunt", "info", "i", PermissionsC.Permissions.info, ConfigC.help_info, (Boolean) W.config.get(ConfigC.commandEnabled_info),  "/BlockHunt [info|i]"));
        commandHelp = new CMDhelp("BlockHunt HELP", "BlockHunt", "help", "h", PermissionsC.Permissions.help, ConfigC.help_help, (Boolean) W.config.get(ConfigC.commandEnabled_help), "/BlockHunt <help|h> [page number]");
        registerCMD(commandHelp);
        registerCMD(new CMDreload("BlockHunt RELOAD", "BlockHunt", "reload", "r", PermissionsC.Permissions.reload, ConfigC.help_reload, (Boolean) W.config.get(ConfigC.commandEnabled_reload), "/BlockHunt <reload|r>"));
        registerCMD(new CMDjoin("BlockHunt JOIN", "BlockHunt", "join", "j", PermissionsC.Permissions.join, ConfigC.help_join, (Boolean) W.config.get(ConfigC.commandEnabled_join), "/BlockHunt <join|j> <arenaname>"));
        registerCMD(new CMDleave("BlockHunt LEAVE", "BlockHunt", "leave", "l", PermissionsC.Permissions.leave, ConfigC.help_leave, (Boolean) W.config.get(ConfigC.commandEnabled_leave), "/BlockHunt <leave|l>"));
        registerCMD(new CMDlist("BlockHunt LIST", "BlockHunt", "list", "li", PermissionsC.Permissions.list, ConfigC.help_list, (Boolean) W.config.get(ConfigC.commandEnabled_list),  "/BlockHunt <list|li>"));
        registerCMD(new CMDshop("BlockHunt SHOP", "BlockHunt", "shop", "sh", PermissionsC.Permissions.shop, ConfigC.help_shop, (Boolean) W.config.get(ConfigC.commandEnabled_shop),  "/BlockHunt <shop|sh>"));
        registerCMD(new CMDstart("BlockHunt START", "BlockHunt", "start", "go", PermissionsC.Permissions.start, ConfigC.help_start, (Boolean) W.config.get(ConfigC.commandEnabled_start),"/BlockHunt <start|go> <arenaname>"));
        if (W.config.getFile().getBoolean("wandEnabled"))
            registerCMD(new CMDwand("BlockHunt WAND", "BlockHunt", "wand", "w", PermissionsC.Permissions.create, ConfigC.help_wand, (Boolean) W.config.get(ConfigC.commandEnabled_wand), "/BlockHunt <wand|w>"));
        registerCMD(new CMDcreate("BlockHunt CREATE", "BlockHunt", "create", "c", PermissionsC.Permissions.create, ConfigC.help_create, (Boolean) W.config.get(ConfigC.commandEnabled_create), "/BlockHunt <create|c> <arenaname>"));
        registerCMD(new CMDset("BlockHunt SET", "BlockHunt", "set", "s", PermissionsC.Permissions.set, ConfigC.help_set, (Boolean) W.config.get(ConfigC.commandEnabled_set), "/BlockHunt <set|s> <arenaname>"));
        registerCMD(new CMDsetwarp("BlockHunt SETWARP", "BlockHunt", "setwarp", "sw", PermissionsC.Permissions.setwarp, ConfigC.help_setwarp, (Boolean) W.config.get(ConfigC.commandEnabled_setwarp), "/BlockHunt <setwarp|sw> <lobby|hiders|seekers|spawn> <arenaname>"));
        registerCMD(new CMDremove("BlockHunt REMOVE", "BlockHunt", "remove", "delete", PermissionsC.Permissions.remove, ConfigC.help_remove, (Boolean) W.config.get(ConfigC.commandEnabled_remove), "/BlockHunt <remove|delete> <arenaname>"));
        registerCMD(new CMDtokens("BlockHunt TOKENS", "BlockHunt", "tokens", "t", PermissionsC.Permissions.tokens, ConfigC.help_tokens, (Boolean) W.config.get(ConfigC.commandEnabled_tokens), "/BlockHunt <tokens|t> <set|add|take> <playername> <amount>"));
    }

}
