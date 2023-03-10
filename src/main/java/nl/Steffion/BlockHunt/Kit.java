package nl.Steffion.BlockHunt;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Kit {
    public ItemStack[] inventory;
    public ItemStack[] armor;
    public ItemStack icon;
    public ItemStack lIcon;
    public String name;
    public String filename;
    public int position;
    public int page;
    public List<String> lore;
    public String lockedLore;
    public boolean enabled;
    public boolean isSeekerKit;
    public boolean requirePermission;

    /**
     * 构造一个kit
     * @param kitFile 要读取或者新建的kit文件实例
     */
    Kit(File kitFile){
        FileConfiguration storage = YamlConfiguration.loadConfiguration(kitFile);

        List<ItemStack> inventoryItems = (List<ItemStack>) storage.getList("inventory");
        if (inventoryItems != null) {
            inventory = inventoryItems.toArray(new ItemStack[0]);
        }

        List<ItemStack> armorItems = (List<ItemStack>) storage.getList("armor");

        if (armorItems != null) {
            armor = armorItems.toArray(new ItemStack[0]);
        }

        icon = storage.getItemStack("icon");
        lIcon = storage.getItemStack("lockedIcon");
        name = storage.getString("name");
        position = storage.getInt("position");
        page = storage.getInt("page");

        if (page == 0) {
            page = 1;
            storage.set("page", page);
        }
        lore = storage.getStringList("lores.unlocked");
        lockedLore = storage.getString("lores.locked", "");
        enabled = storage.getBoolean("enabled");
        isSeekerKit = storage.getBoolean("isSeekerKit",false);
        requirePermission = storage.getBoolean("requirePermission");
        filename = storage.getString("filename");

        try {
            storage.save(kitFile);
        } catch (IOException e) {
            BlockHunt.plugin.getLogger().info("Failed to save kit file!");
        }
    }

    /**
     * 给予player当前kit
     * @param player
     */
    public void give(Player player){
        player.getInventory().clear();

            for (int i = 0; i < 36; i++) {
                if (i <= inventory.length-1) {
                    player.getInventory().setItem(i, inventory[i]);
                }
                else {
                    player.getInventory().setItem(i, null);
                }
            }
            player.getInventory().setArmorContents(armor);
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public void setInventory(ItemStack[] inv) {
        this.inventory = inv.clone();
    }

    public void save() {
        File dataDirectory = BlockHunt.plugin.getDataFolder();
        File kitsDirectory = new File(dataDirectory, "kits");
        if (!kitsDirectory.exists()) {
            if (!kitsDirectory.mkdirs()) {
                return;
            }
        }

        File kitFile = new File(kitsDirectory, filename + ".yml");
        FileConfiguration storage = YamlConfiguration.loadConfiguration(kitFile);

        storage.set("inventory", inventory);
        storage.set("armor", armor);

        storage.set("requirePermission", requirePermission);
        storage.set("icon", icon);
        storage.set("lockedIcon", lIcon);
        storage.set("position", position);
        storage.set("page", page);
        storage.set("name", name);
        storage.set("enabled", enabled);
        storage.set("isSeekerKit", isSeekerKit);
        storage.set("lores.unlocked", lore);
        storage.set("lores.locked", lockedLore);
        storage.set("filename", filename);

        try {
            storage.save(kitFile);
        } catch (IOException e) {
            BlockHunt.plugin.getLogger().info("Failed to save kit file!");
        }
    }
}
