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
        CommandManager bhCmdManager = new CommandManager();
        BHTabCompleter bhTabCompleter = new BHTabCompleter();
        getCommand("blockhunt").setExecutor(bhCmdManager);
        getCommand("blockhunt").setTabCompleter(bhTabCompleter);
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
        KitHandler.loadKits();
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
        MessageM.sendFMessage(null, ConfigC.log_disabledPlugin, "name-" + pdfFile.getName(), "version-" + pdfFile.getVersion(), "autors-" + pdfFile.getAuthors().get(0));
    }

    public static String stringBuilder(String[] input, int startArg) {
        if (input.length - startArg <= 0)
            return null;
        StringBuilder sb = new StringBuilder(input[startArg]);
        for (int i = ++startArg; i < input.length; i++)
            sb.append(' ').append(input[i]);
        return sb.toString();
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