package nl.Steffion.BlockHunt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.libraryaddict.disguise.DisguiseAPI;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Managers.PermissionsM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class ArenaHandler {


    public static void loadArenas() {
        W.arenaList.clear();
        for (String arenaName : W.arenas.getFile().getKeys(false))
            W.arenaList.add((Arena) W.arenas.getFile().get(arenaName));
        for (Arena arena : W.arenaList)
            ScoreboardHandler.createScoreboard(arena);
    }

    public static void playerJoinArena(Player player, String arenaName) {
        Arena arena = null;
        //玩家已经在jjc的话需要退出，顺便找到要加入的jjc
        for (Arena nowArena : W.arenaList) {
            if (nowArena.arenaName.equalsIgnoreCase(arenaName)) {
                arena = nowArena;
            }
            if (nowArena.playersInArena != null && nowArena.playersInArena.contains(player)) {
                MessageM.sendFMessage(player, ConfigC.error_joinAlreadyJoined);
                return;
            }
        }
        //jjc找不到，提示并return
        if (arena == null) {
            MessageM.sendFMessage(player, ConfigC.error_noArena, "name-" + arenaName);
            return;
        }
        //找到jjc，交给jjc处理
        arena.playerJoinHandler(player);
        //更新牌子
        SignsHandler.updateSigns();
    }

    public static void playerLeaveArena(Player player, boolean message, boolean cleanup) {
        Arena arena = null;
        for (Arena arena2 : W.arenaList) {
            if (arena2.playersInArena != null && arena2.playersInArena.contains(player))
                arena = arena2;
        }
        if (arena != null)
            //找到jjc，交给jjc处理
            arena.playerLeaveHandler(player,message,cleanup);
            else {
            if (message)
                MessageM.sendFMessage(player, ConfigC.error_leaveNotInArena);
            return;
        }
        SignsHandler.updateSigns();
    }
    public static void sendMessage(Arena arena, String message, String... vars) {
        for (Player player : arena.playersInArena) {
            String pMessage = message.replaceAll("%player%", player.getName());
            player.sendMessage(MessageM.replaceAll(pMessage, vars));
        }
    }

    public static List<String> getArenaNames(){
        List<String> response = new ArrayList<>();
        for(Arena arena:W.arenaList){
            response.add(arena.arenaName);
        }
        return response;
    }
}