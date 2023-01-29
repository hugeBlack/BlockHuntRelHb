package nl.Steffion.BlockHunt.Listeners;

import nl.Steffion.BlockHunt.Arena;
import nl.Steffion.BlockHunt.W;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class OnProjectileLauncherListener implements Listener {
    @EventHandler
    public void projectileLaunch(ProjectileLaunchEvent e) {
        Projectile projectile = e.getEntity();
        //投射物需要是雪球、鸡蛋、箭，来源必须是玩家
        if ((((projectile instanceof Snowball)) || ((projectile instanceof Egg)) || ((projectile instanceof Arrow))) && ((projectile.getShooter() instanceof Player))) {
            Player player = (Player) projectile.getShooter();
            Arena arena= W.playerArenaMap.get(player);
            if(arena==null) return;
            //投射物加入对应jjc的map中
            arena.projectileOwnerMap.put(projectile,player);
        }
    }
}
