package nl.Steffion.BlockHunt.Commands;

import java.util.ArrayList;
import java.util.List;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CMDwand extends DefaultCMD {
  public boolean exectue(Player player, Command cmd, String label, String[] args) {
    if (player != null) {
      ItemStack wand = new ItemStack(
          Material.getMaterial((String)W.config
            .get(ConfigC.wandIDname)));
      ItemMeta im = wand.getItemMeta();
      im.setDisplayName(MessageM.replaceAll((String)W.config
            .get(ConfigC.wandName), new String[0]));
      W.config.load();
      List<String> lores = W.config.getFile().getStringList(
          ConfigC.wandDescription.location);
      List<String> lores2 = new ArrayList<>();
      for (String lore : lores)
        lores2.add(MessageM.replaceAll(lore, new String[0])); 
      im.setLore(lores2);
      wand.setItemMeta(im);
      player.getInventory().addItem(new ItemStack[] { wand });
      player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 5.0F, 0.0F);
      MessageM.sendFMessage(player, ConfigC.normal_wandGaveWand, new String[] { "type-" + 
            wand.getType().toString().replaceAll("_", " ")
            .toLowerCase() });
    } else {
      MessageM.sendFMessage(player, ConfigC.error_onlyIngame, new String[0]);
    } 
    return true;
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Commands\CMDwand.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */