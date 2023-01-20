package nl.Steffion.BlockHunt;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class KitHandler {
    public static HashMap<String,Kit> kitMap = new HashMap<>();
    public static NamespacedKey kitMenuIconKey = new NamespacedKey(BlockHunt.plugin,"kitMenuItem");
    public static void newKit(Player player, String kitName) {
        File dataDirectory = BlockHunt.plugin.getDataFolder();
        File kitsDirectory = new File(dataDirectory, "kits");
        if (!kitsDirectory.exists()) {
            if (!kitsDirectory.mkdirs()) {
                return;
            }
        }

        File kitFile = new File(kitsDirectory, kitName + ".yml");
        FileConfiguration storage = YamlConfiguration.loadConfiguration(kitFile);

        ItemStack[] inventory = player.getInventory().getContents();
        storage.set("inventory", inventory);
        ItemStack[] armor = player.getInventory().getArmorContents();
        storage.set("armor", armor);

        storage.set("requirePermission", false);
        storage.set("icon", new ItemStack(Material.SNOW_BLOCK, 1));
        storage.set("lockedIcon", new ItemStack(Material.BARRIER, 1));
        storage.set("position", 0);
        storage.set("page", 1);
        storage.set("name", kitName);
        storage.set("enabled", false);

        storage.set("lores.unlocked", Lists.newArrayList("Lore line 1"));
        storage.set("lores.locked", "&cA permission is required to unlock this kit!");

        storage.set("gameSettings.noRegen", false);
        storage.set("gameSettings.noPvp", false);
        storage.set("gameSettings.soupPvp", false);
        storage.set("gameSettings.noFallDamage", false);

        storage.set("filename", kitFile.getName().substring(0, kitFile.getName().lastIndexOf('.')));

        try {
            storage.save(kitFile);
        } catch (IOException e) {
            BlockHunt.plugin.getLogger().info("Failed to save new kit file!");
        }
        kitMap.put(kitName,new Kit(kitFile));
    }

    public static void saveKit(Kit kit) {
        File dataDirectory = BlockHunt.plugin.getDataFolder();
        File kitsDirectory = new File(dataDirectory, "kits");
        if (!kitsDirectory.exists()) {
            if (!kitsDirectory.mkdirs()) {
                return;
            }
        }

        File kitFile = new File(kitsDirectory, kit.name + ".yml");
        FileConfiguration storage = YamlConfiguration.loadConfiguration(kitFile);

        storage.set("inventory", kit.getInventory());
        storage.set("armor", kit.armor);

        storage.set("requirePermission", kit.requirePermission);
        storage.set("icon", kit.icon);
        storage.set("lockedIcon", kit.lIcon);
        storage.set("position", kit.position);
        storage.set("page", kit.page);
        storage.set("name", kit.name);
        storage.set("enabled", kit.enabled);

        storage.set("lores.unlocked", kit.lore);
        storage.set("lores.locked", kit.lockedLore);
        storage.set("filename", kit.filename);

        try {
            storage.save(kitFile);
        } catch (IOException e) {
            BlockHunt.plugin.getLogger().info("Failed to save kit file!");
        }
    }

    public static void loadKits() {
        kitMap.clear();
        File dataDirectory = BlockHunt.plugin.getDataFolder();
        File kitsDirectory = new File(dataDirectory, "kits");

        if (!kitsDirectory.exists()) {
            if (!kitsDirectory.mkdirs()) {
                return;
            }
        }

        File[] kitsFiles = kitsDirectory.listFiles();
        if (kitsFiles == null) {
            return;
        }

        for (File kitFile : kitsFiles) {
            if (!kitFile.getName().endsWith(".yml")) {
                continue;
            }

            String name = kitFile.getName().replace(".yml", "");

            if (!name.isEmpty()) {
                Kit newKit= new Kit(kitFile);
                kitMap.put(newKit.name,newKit);
            }
        }
    }

    /**
     * 获取所有kit的名称
     * @return kit名称链表
     */
    public static List<String> getKitNames(){
        LinkedList<String> ans = new LinkedList<>();
        for(Kit kit:kitMap.values()){
            ans.add(kit.name);
        }
        return ans;
    }

}
