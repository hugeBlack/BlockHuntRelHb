package nl.Steffion.BlockHunt;

import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler {
  public static void createScoreboard(Arena arena) {
    if (((Boolean)W.config.get(ConfigC.scoreboard_enabled)).booleanValue()) {
      Scoreboard board = arena.scoreboard;
      if (board.getObjective(arena.arenaName) != null) {
        updateScoreboard(arena);
        return;
      } 
      Objective object = board.registerNewObjective(
          BlockHunt.cutString(arena.arenaName, 32), "dummy", "BlockHunt");
      object.setDisplaySlot(DisplaySlot.SIDEBAR);
      object.setDisplayName(BlockHunt.cutString(
            MessageM.replaceAll((String)W.config
              .get(ConfigC.scoreboard_title), new String[0]), 32));
      Score timeleft = object.getScore(
          BlockHunt.cutString(MessageM.replaceAll((String)W.config
              .get(ConfigC.scoreboard_timeleft), new String[0]), 32));
      timeleft.setScore(arena.timer);
      Score seekers = object.getScore(
          BlockHunt.cutString(MessageM.replaceAll((String)W.config
              .get(ConfigC.scoreboard_seekers), new String[0]), 32));
      seekers.setScore(arena.seekers.size());
      Score hiders = object.getScore(
          BlockHunt.cutString(MessageM.replaceAll((String)W.config
              .get(ConfigC.scoreboard_hiders), new String[0]), 32));
      hiders.setScore(arena.playersInArena.size() - arena.seekers.size());
      if (arena.gameState == Arena.ArenaState.INGAME) {
        for (Player pl : arena.playersInArena)
          pl.setScoreboard(board); 
      } else {
        for (Player pl : arena.playersInArena)
          pl.setScoreboard(Bukkit.getScoreboardManager()
              .getNewScoreboard()); 
      } 
    } 
  }
  
  public static void updateScoreboard(Arena arena) {
    if (((Boolean)W.config.get(ConfigC.scoreboard_enabled)).booleanValue()) {
      Scoreboard board = arena.scoreboard;
      Objective object = board.getObjective(DisplaySlot.SIDEBAR);
      object.setDisplayName(BlockHunt.cutString(
            MessageM.replaceAll((String)W.config
              .get(ConfigC.scoreboard_title), new String[0]), 32));
      Score timeleft = object.getScore(
          BlockHunt.cutString(MessageM.replaceAll((String)W.config
              .get(ConfigC.scoreboard_timeleft), new String[0]), 32));
      timeleft.setScore(arena.timer);
      Score seekers = object.getScore(
          BlockHunt.cutString(MessageM.replaceAll((String)W.config
              .get(ConfigC.scoreboard_seekers), new String[0]), 32));
      seekers.setScore(arena.seekers.size());
      Score hiders = object.getScore(
          BlockHunt.cutString(MessageM.replaceAll((String)W.config
              .get(ConfigC.scoreboard_hiders), new String[0]), 32));
      hiders.setScore(arena.playersInArena.size() - arena.seekers.size());
      if (arena.gameState == Arena.ArenaState.INGAME) {
        for (Player pl : arena.playersInArena)
          pl.setScoreboard(board); 
      } else {
        for (Player pl : arena.playersInArena)
          pl.setScoreboard(Bukkit.getScoreboardManager()
              .getNewScoreboard()); 
      } 
    } 
  }
  
  public static void removeScoreboard(Player player) {
    player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
  }
}