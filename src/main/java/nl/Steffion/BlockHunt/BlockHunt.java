package nl.Steffion.BlockHunt;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;
import nl.Steffion.BlockHunt.Commands.*;
import nl.Steffion.BlockHunt.Listeners.*;
import nl.Steffion.BlockHunt.Managers.*;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockHunt extends JavaPlugin implements Listener {
    public static PluginDescriptionFile pdfFile;
    public static BlockHunt plugin;
    public static Economy econ = null;
    public static List<String> BlockHuntCMD = new ArrayList<>();
    public static CMDinfo CMD;

    public void onEnable() {
        W.thePlugin = this;
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
        CommandManager.regCMDs();
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
            arena.stop();
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
        for (DefaultCMD command : CommandManager.commands) {
            String[] argsSplit = null;
            String[] argsSplitAlias = null;
            if (command.args != null && command.argsAlias != null) {
                argsSplit = command.args.split("/");
                argsSplitAlias = command.argsAlias.split("/");
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
                            command.execute(player, cmd, label, args);
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
        for (DefaultCMD command : CommandManager.commands) {
            if (cmd.getName().equalsIgnoreCase(command.label) && args.length == 1)
                return command.tabCompleter(sender,cmd,label,args);
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