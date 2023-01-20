package nl.Steffion.BlockHunt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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

    public static void playerJoinArena(Player player, Arena arena) {
        //玩家已经在jjc的话需要退出
        if(W.playerArenaMap.containsKey(player)){
            MessageM.sendFMessage(player, ConfigC.error_joinAlreadyJoined);
            return;
        }
        //找到jjc，交给jjc处理
        arena.playerJoinHandler(player);
        //更新牌子
        SignsHandler.updateSigns();
    }
    public static void playerJoinArena(Player player, String arenaName) {
        //玩家已经在jjc的话需要退出
        if(W.playerArenaMap.containsKey(player)){
            MessageM.sendFMessage(player, ConfigC.error_joinAlreadyJoined);
            return;
        }
        Arena arena = getArenaByName(arenaName);
        //jjc找不到，提示并return
        if (arena == null) {
            MessageM.sendFMessage(player, ConfigC.error_noArena, "name-" + arena.arenaName);
            return;
        }
        playerJoinArena(player,arena);
    }

    public static void playerLeaveArena(Player player, boolean message, boolean cleanup) {
        Arena arena = W.playerArenaMap.get(player);
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

    /**
     * 获取所有jjc的名称
     * @return jjc名称链表
     */
    public static List<String> getArenaNames(){
        LinkedList<String> ans = new LinkedList<>();
        for(Arena arena:W.arenaList){
            ans.add(arena.arenaName);
        }
        return ans;
    }

    /**
     * 通过arenaName查找对应的jjc
     * @param arenaName 要查找的jjc名称，忽略大小写
     * @return jjc实例或者null
     */
    public static Arena getArenaByName(String arenaName){
        for (Arena arena2 : W.arenaList) {
            if (arena2.arenaName.equalsIgnoreCase(arenaName))
                return arena2;
        }
        return null;
    }
}