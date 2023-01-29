package nl.Steffion.BlockHunt;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class SolidBlockHandler {
    public static void makePlayerUnsolid(Player player) {
        ItemStack block = W.pBlock.get(player);
        Block pBlock = player.getLocation().getBlock();
        if (W.hiddenLoc.get(player) != null)
            pBlock = W.hiddenLoc.get(player).getBlock();
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!pl.equals(player)) {
                if (W.hiddenLocWater.get(player) != null) {
                    if (W.hiddenLocWater.get(player)) {
                        pl.sendBlockChange(pBlock.getLocation(), Material.WATER.createBlockData());
                    } else {
                        pl.sendBlockChange(pBlock.getLocation(), Material.AIR.createBlockData());
                    }
                } else {
                    pl.sendBlockChange(pBlock.getLocation(), Material.AIR.createBlockData());
                }
                W.hiddenLocWater.remove(player);
            }
        }
        player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 1.0F, 1.0F);
        ItemStack playerInvBlock = player.getInventory().getItem(8);
        if(playerInvBlock!=null){
            playerInvBlock.removeEnchantment(Enchantment.DURABILITY);
            playerInvBlock.setAmount(5);
        }
        for (Player playerShow : Bukkit.getOnlinePlayers())
            playerShow.showPlayer(player);
        MiscDisguise disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, block.getType());
        DisguiseAPI.undisguiseToAll(player);
        DisguiseAPI.disguiseToAll(player, disguise);
        MessageM.sendFMessage(player, ConfigC.normal_ingameNoMoreSolid);
    }
}
