package nl.Steffion.BlockHunt;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

/**
 * 储存需要用到的，与游戏性关联不大的静态方法
 */
public class Helpers {
    /**
     * 用蓄水池抽样选取从list中随机选取数量最多为count个元素
     */
    public static ArrayList<Player> reservoirSampling(Set<Player> list, int count) {
        ArrayList<Player> ans = new ArrayList<>();
        Iterator<Player> it = list.iterator();
        for (int i = 0; i < count && it.hasNext(); i++) {
            ans.add(it.next());
        }
        int elementsSeen = count;
        int j;
        while (it.hasNext()) {
            elementsSeen++;
            j = W.random.nextInt(elementsSeen);
            if (j < count) {
                ans.set(j, it.next());
            } else {
                it.next();
            }
        }
        return ans;
    }

    /**
     * 尝试从file中读取yaml配置
     * @param file 配置文件实例
     * @return 读取到的配置，或者新的配置文件
     */
    public static YamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");
        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException var3) {
        } catch (IOException var4) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var4);
        } catch (InvalidConfigurationException var5) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, var5);
        }

        return config;
    }
}

