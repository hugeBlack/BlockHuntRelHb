package nl.Steffion.BlockHunt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import net.milkbowl.vault.economy.Economy;
import nl.Steffion.BlockHunt.Commands.*;
import nl.Steffion.BlockHunt.Listeners.*;
import nl.Steffion.BlockHunt.Managers.*;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockHunt extends JavaPlugin implements Listener {
    public static PluginDescriptionFile pdfFile;

    public static BlockHunt plugin;

    public static Economy econ = null;

    public static List<String> BlockHuntCMD = new ArrayList<>();

    public static CommandM CMD;

    public static CommandM CMDinfo;

    public static CommandM CMDhelp;

    public static CommandM CMDreload;

    public static CommandM CMDjoin;

    public static CommandM CMDleave;

    public static CommandM CMDlist;

    public static CommandM CMDshop;

    public static CommandM CMDstart;

    public static CommandM CMDwand;

    public static CommandM CMDcreate;

    public static CommandM CMDset;

    public static CommandM CMDsetwarp;

    public static CommandM CMDremove;

    public static CommandM CMDtokens;

    public void onEnable() {
        //<editor-fold desc="注册监听器">
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new OnBlockBreakEvent(), this);
        getServer().getPluginManager().registerEvents(new OnBlockPlaceEvent(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDamageByEntityEvent(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDamageEvent(), this);
        getServer().getPluginManager().registerEvents(new OnFoodLevelChangeEvent(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryClickEvent(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryCloseEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerCommandPreprocessEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDropItemEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerInteractEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuitEvent(), this);
        getServer().getPluginManager().registerEvents(new OnSignChangeEvent(), this);
        ConfigurationSerialization.registerClass(LocationSerializable.class, "BlockHuntLocation");
        ConfigurationSerialization.registerClass(Arena.class, "BlockHuntArena");
        //</editor-fold>
        pdfFile = getDescription();
        plugin = this;
        ConfigM.newFiles();
        //<editor-fold desc="注册指令">
        CMD = new CommandM("BlockHunt", "BlockHunt", null, null,
                PermissionsC.Permissions.info, ConfigC.help_info,
                (Boolean) W.config.get(ConfigC.commandEnabled_info),
                BlockHuntCMD, new CMDinfo(), null);
        CMDinfo = new CommandM("BlockHunt INFO", "BlockHunt", "info", "i",
                PermissionsC.Permissions.info, ConfigC.help_info,
                (Boolean) W.config.get(ConfigC.commandEnabled_info),
                BlockHuntCMD, new CMDinfo(), "/BlockHunt [info|i]");
        CMDhelp = new CommandM("BlockHunt HELP", "BlockHunt", "help", "h",
                PermissionsC.Permissions.help, ConfigC.help_help,
                (Boolean) W.config.get(ConfigC.commandEnabled_help),
                BlockHuntCMD, new CMDhelp(),
                "/BlockHunt <help|h> [page number]");
        CMDreload = new CommandM("BlockHunt RELOAD", "BlockHunt", "reload",
                "r", PermissionsC.Permissions.reload, ConfigC.help_reload,
                (Boolean) W.config.get(ConfigC.commandEnabled_reload),
                BlockHuntCMD, new CMDreload(), "/BlockHunt <reload|r>");
        CMDjoin = new CommandM("BlockHunt JOIN", "BlockHunt", "join", "j",
                PermissionsC.Permissions.join, ConfigC.help_join,
                (Boolean) W.config.get(ConfigC.commandEnabled_join),
                BlockHuntCMD, new CMDjoin(), "/BlockHunt <join|j> <arenaname>");
        CMDleave = new CommandM("BlockHunt LEAVE", "BlockHunt", "leave", "l",
                PermissionsC.Permissions.leave, ConfigC.help_leave,
                (Boolean) W.config.get(ConfigC.commandEnabled_leave),
                BlockHuntCMD, new CMDleave(), "/BlockHunt <leave|l>");
        CMDlist = new CommandM("BlockHunt LIST", "BlockHunt", "list", "li",
                PermissionsC.Permissions.list, ConfigC.help_list,
                (Boolean) W.config.get(ConfigC.commandEnabled_list),
                BlockHuntCMD, new CMDlist(), "/BlockHunt <list|li>");
        CMDshop = new CommandM("BlockHunt SHOP", "BlockHunt", "shop", "sh",
                PermissionsC.Permissions.shop, ConfigC.help_shop,
                (Boolean) W.config.get(ConfigC.commandEnabled_shop),
                BlockHuntCMD, new CMDshop(), "/BlockHunt <shop|sh>");
        CMDstart = new CommandM("BlockHunt START", "BlockHunt", "start", "go",
                PermissionsC.Permissions.start, ConfigC.help_start,
                (Boolean) W.config.get(ConfigC.commandEnabled_start),
                BlockHuntCMD, new CMDstart(),
                "/BlockHunt <start|go> <arenaname>");
        if (W.config.getFile().getBoolean("wandEnabled"))
            CMDwand = new CommandM("BlockHunt WAND", "BlockHunt", "wand", "w",
                    PermissionsC.Permissions.create, ConfigC.help_wand,
                    (Boolean) W.config.get(ConfigC.commandEnabled_wand),
                    BlockHuntCMD, new CMDwand(), "/BlockHunt <wand|w>");
        CMDcreate = new CommandM("BlockHunt CREATE", "BlockHunt", "create",
                "c", PermissionsC.Permissions.create, ConfigC.help_create,
                (Boolean) W.config.get(ConfigC.commandEnabled_create),
                BlockHuntCMD, new CMDcreate(),
                "/BlockHunt <create|c> <arenaname>");
        CMDset = new CommandM("BlockHunt SET", "BlockHunt", "set", "s",
                PermissionsC.Permissions.set, ConfigC.help_set,
                (Boolean) W.config.get(ConfigC.commandEnabled_set),
                BlockHuntCMD, new CMDset(), "/BlockHunt <set|s> <arenaname>");
        CMDsetwarp = new CommandM("BlockHunt SETWARP", "BlockHunt", "setwarp",
                "sw", PermissionsC.Permissions.setwarp, ConfigC.help_setwarp,
                (Boolean) W.config.get(ConfigC.commandEnabled_setwarp),
                BlockHuntCMD, new CMDsetwarp(),
                "/BlockHunt <setwarp|sw> <lobby|hiders|seekers|spawn> <arenaname>");
        CMDremove = new CommandM("BlockHunt REMOVE", "BlockHunt", "remove",
                "delete", PermissionsC.Permissions.remove, ConfigC.help_remove,
                (Boolean) W.config.get(ConfigC.commandEnabled_remove),
                BlockHuntCMD, new CMDremove(),
                "/BlockHunt <remove|delete> <arenaname>");
        CMDtokens = new CommandM("BlockHunt TOKENS", "BlockHunt", "tokens",
                "t", PermissionsC.Permissions.tokens, ConfigC.help_tokens,
                (Boolean) W.config.get(ConfigC.commandEnabled_tokens),
                BlockHuntCMD, new CMDtokens(),
                "/BlockHunt <tokens|t> <set|add|take> <playername> <amount>");
        //</editor-fold>
        //<editor-fold desc="检查依赖">
        if (!getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            MessageM.broadcastFMessage(ConfigC.error_libsDisguisesNotInstalled);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
            MessageM.broadcastFMessage(ConfigC.error_protocolLibNotInstalled);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (W.config.getFile().getBoolean("vaultSupport")) {
            if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
                MessageM.broadcastFMessage(ConfigC.error_trueVaultNull);
                return;
            }
            MessageM.broadcastFMessage(ConfigC.warning_usingVault);
        } else {
            MessageM.broadcastFMessage(ConfigC.warning_noVault);
        }
        setupEconomy();
      //</editor-fold>
        ConfigurationSerialization.registerClass(Arena.class);
        ArenaHandler.loadArenas();
        //<editor-fold desc="发送统计信息">
        /*Bukkit.getScheduler().runTaskTimer(this, () -> {
            try {
                Metrics metrics = new Metrics(BlockHunt.plugin);
                if (metrics.isOptOut())
                    return;
                Metrics.Graph playersPlayingBlockHunt = metrics
                        .createGraph("Players playing BlockHunt");
                playersPlayingBlockHunt.addPlotter(new Metrics.Plotter(
                        "Playing") {
                    public int getValue() {
                        int playersPlaying = 0;
                        for (Arena arena : W.arenaList)
                            playersPlaying += arena.playersInArena.size();
                        return playersPlaying;
                    }
                });
                playersPlayingBlockHunt.addPlotter(new Metrics.Plotter(
                        "Not playing") {
                    public int getValue() {
                        int playersPlaying = 0;
                        for (Arena arena : W.arenaList)
                            playersPlaying += arena.playersInArena.size();
                        return Bukkit.getOnlinePlayers().size() - playersPlaying;
                    }
                });
            } catch (Exception e) {
                MessageM.sendMessage(null, "%TAG%EUnable to send %AMCStats %Eto the server. Something went wrong ;(!");
            }
        }, 0L, 6000L);
        try {
            Metrics metrics = new Metrics(plugin);
            if (!metrics.isOptOut()) {
                metrics.start();
                MessageM.sendMessage(null, "%TAG%NSending %AMCStats%N to the server...");
            } else {
                MessageM.sendMessage(null, "%TAG%EUnable to send %AMCStats %Eto the server. %AMCStats%E is disabled?");
            }
        } catch (Exception e) {
            MessageM.sendMessage(null, "%TAG%EUnable to send %AMCStats %Eto the server. Something went wrong ;(!");
        }*/
        //</editor-fold>

        MessageM.sendFMessage(null, ConfigC.log_enabledPlugin, "name-" + pdfFile.getName(), "version-" + pdfFile.getVersion(), "autors-" + pdfFile.getAuthors().get(0));
        //每秒执行一次，对每个jjc进行检查
        getServer().getScheduler().runTaskTimer(this, () -> {
            for (Arena arena : W.arenaList)
                arena.tick();
            SignsHandler.updateSigns();
        }, 0L, 20L);
    }

    public void onDisable() {
        for (Arena arena : W.arenaList)
            ArenaHandler.stopArena(arena);
        MessageM.sendFMessage(null, ConfigC.log_disabledPlugin, "name-" +
                pdfFile.getName(),
                "version-" + pdfFile.getVersion(), "autors-" +
                (String) pdfFile.getAuthors().get(0));
    }

    public static String stringBuilder(String[] input, int startArg) {
        if (input.length - startArg <= 0)
            return null;
        StringBuilder sb = new StringBuilder(input[startArg]);
        for (int i = ++startArg; i < input.length; i++)
            sb.append(' ').append(input[i]);
        return sb.toString();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player)
            player = (Player) sender;
        for (CommandM command : W.commands) {
            String[] argsSplit = null;
            String[] argsSplitAlias = null;
            if (command.args != null && command.argsalias != null) {
                argsSplit = command.args.split("/");
                argsSplitAlias = command.argsalias.split("/");
            }
            if (cmd.getName().equalsIgnoreCase(command.label)) {
                boolean equals = true;
                if (argsSplit == null) {
                    if (args.length == 0) {
                        equals = true;
                    } else {
                        equals = false;
                    }
                } else if (args.length >= argsSplit.length) {
                    for (int i2 = argsSplit.length - 1; i2 >= 0; i2--) {
                        int loc = argsSplit.length - i2 - 1;
                        if (!argsSplit[loc].equalsIgnoreCase(args[loc]) &&

                                !argsSplitAlias[loc].equalsIgnoreCase(args[loc]))
                            equals = false;
                    }
                } else {
                    equals = false;
                }
                if (equals) {
                    if (PermissionsM.hasPerm(player, command.permission, Boolean.TRUE))
                        if (command.enabled) {
                            command.CMD.exectue(player, cmd, label, args);
                        } else {
                            MessageM.sendFMessage(player,
                                    ConfigC.error_commandNotEnabled);
                        }
                    return true;
                }
            }
        }
        CMDnotfound.exectue(player, cmd, label, args);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        for (CommandM command : W.commands) {
            if (cmd.getName().equalsIgnoreCase(command.label) &&
                    args.length == 1)
                return command.mainTABlist;
        }
        return null;
    }

    public static String cutString(String string, int maxLenght) {
        if (string.length() > maxLenght)
            string = string.substring(0, maxLenght);
        return string;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp = getServer()
                .getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
            return false;
        econ = rsp.getProvider();
        return (econ != null);
    }
}