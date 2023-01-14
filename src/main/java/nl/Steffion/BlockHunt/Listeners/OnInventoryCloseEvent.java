package nl.Steffion.BlockHunt.Listeners;

import java.util.ArrayList;
import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.ArenaHandler;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OnInventoryCloseEvent implements Listener {
  @EventHandler(priority = EventPriority.NORMAL)
  public void onInventoryCloseEvent(InventoryCloseEvent event) {
    Inventory inv = event.getInventory();
    //判断关闭的是配置伪装方块菜单，且是虚拟的
    if (inv.getType().equals(InventoryType.CHEST) && inv.getHolder() == null && event.getView().getTitle().contains("DisguiseBlocks")) {
      String arenaname = inv
        .getItem(0)
        .getItemMeta()
        .getDisplayName()
        .replaceAll(
          MessageM.replaceAll("%NDisguiseBlocks of arena: %A", new String[0]), 
          "");
      Arena arena = null;
      for (Arena arena2 : W.arenaList) {
        if (arena2.arenaName.equalsIgnoreCase(arenaname))
          arena = arena2; 
      } 
      ArrayList<ItemStack> blocks = new ArrayList<>();
      byte b;
      int i;
      ItemStack[] arrayOfItemStack;
      for (i = (arrayOfItemStack = inv.getContents()).length, b = 0; b < i; ) {
        ItemStack item = arrayOfItemStack[b];
        if (item != null && 
          !item.getType().equals(Material.PAPER))
          if (item.getType().equals(Material.FLOWER_POT)) {
            blocks.add(new ItemStack(Material.FLOWER_POT));
          } else {
            blocks.add(item);
          }  
        b++;
      } 
      arena.disguiseBlocks = blocks;
      save(arena);
    } 
  }
  
  public void save(Arena arena) {
    W.arenas.getFile().set(arena.arenaName, arena);
    W.arenas.save();
    ArenaHandler.loadArenas();
  }
}