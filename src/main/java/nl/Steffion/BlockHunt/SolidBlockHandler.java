package nl.Steffion.BlockHunt;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SolidBlockHandler {
  public static void makePlayerUnsolid(Player player) {
    ItemStack block = player.getInventory().getItem(8);
    Block pBlock = player.getLocation().getBlock();
    if (W.hiddenLoc.get(player) != null)
      pBlock = ((Location)W.hiddenLoc.get(player)).getBlock(); 
    block.setAmount(5);
    for (Player pl : Bukkit.getOnlinePlayers()) {
      if (!pl.equals(player)) {
        if (W.hiddenLocWater.get(player) != null) {
          if (((Boolean)W.hiddenLocWater.get(player)).booleanValue()) {
            pl.sendBlockChange(pBlock.getLocation(), 
                Material.WATER, (byte)0);
          } else {
            pl.sendBlockChange(pBlock.getLocation(), Material.AIR, (byte)
                0);
          } 
        } else {
          pl.sendBlockChange(pBlock.getLocation(), Material.AIR, (byte)
              0);
        } 
        W.hiddenLocWater.remove(player);
      } 
    } 
    player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 1.0F, 1.0F);
    block.removeEnchantment(Enchantment.DURABILITY);
    for (Player playerShow : Bukkit.getOnlinePlayers())
      playerShow.showPlayer(player); 
    MiscDisguise disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, 
        block.getType(), block.getDurability());
    DisguiseAPI.disguiseToAll((Entity)player, (Disguise)disguise);
    MessageM.sendFMessage(player, ConfigC.normal_ingameNoMoreSolid, new String[0]);
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\SolidBlockHandler.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */