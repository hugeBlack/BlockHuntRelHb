package nl.Steffion.BlockHunt;

import java.util.ArrayList;
import java.util.Objects;

import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.SignChangeEvent;

public class SignsHandler {
  public static void createSign(SignChangeEvent event, String[] lines, LocationSerializable location) {
    if (lines[1] != null)
      if (lines[1].equalsIgnoreCase("leave")) {
        boolean saved = false;
        int number = 1;
        while (!saved) {
          if (W.signs.getFile().get("leave_" + number) == null) {
            W.signs.getFile().set("leave_" + number + ".arenaName", 
                "leave");
            W.signs.getFile().set("leave_" + number + ".location", 
                location);
            W.signs.save();
            saved = true;
            continue;
          } 
          number++;
        } 
      } else if (lines[1].equalsIgnoreCase("shop")) {
        boolean saved = false;
        int number = 1;
        while (!saved) {
          if (W.signs.getFile().get("shop_" + number) == null) {
            W.signs.getFile().set("shop_" + number + ".arenaName", 
                "shop");
            W.signs.getFile().set("shop_" + number + ".location", 
                location);
            W.signs.save();
            saved = true;
            continue;
          } 
          number++;
        } 
      } else {
        boolean saved = false;
        for (Arena arena : W.arenaList) {
          if (lines[1].equals(arena.arenaName)) {
            int number = 1;
            while (!saved) {
              if (W.signs.getFile().get(arena.arenaName + "_" + number) == null) {
                W.signs.getFile().set(arena.arenaName + "_" + number + ".arenaName", lines[1]);
                W.signs.getFile().set(arena.arenaName + "_" + number + ".location", location);
                W.signs.save();
                saved = true;
                continue;
              } 
              number++;
            } 
          } 
        } 
        if (!saved)
          MessageM.sendFMessage(event.getPlayer(), 
              ConfigC.error_noArena, new String[] { "name-" + lines[1] }); 
      }  
  }
  
  public static void removeSign(LocationSerializable location) {
    for (String sign : W.signs.getFile().getKeys(false)) {
      LocationSerializable loc = new LocationSerializable(
          (Location) Objects.requireNonNull(W.signs.getFile().get(sign + ".location")));
      if (loc.equals(location)) {
        W.signs.getFile().set(sign, null);
        W.signs.save();
      } 
    } 
  }
  
  public static boolean isSign(LocationSerializable location) {
    for (String sign : W.signs.getFile().getKeys(false)) {
      LocationSerializable loc = new LocationSerializable(
          (Location)W.signs.getFile().get(String.valueOf(sign) + ".location"));
      if (loc.equals(location))
        return true; 
    } 
    return false;
  }
  
  public static void updateSigns() {
    W.signs.load();
    for (String sign : W.signs.getFile().getKeys(false)) {
      LocationSerializable loc = new LocationSerializable(
          (Location)W.signs.getFile().get(
            String.valueOf(sign) + ".location"));
      if (loc.getBlock().getType().equals(Material.OAK_SIGN) || 
        loc.getBlock().getType().equals(Material.OAK_WALL_SIGN)) {
        Sign signblock = (Sign)loc.getBlock().getState();
        String[] lines = signblock.getLines();
        if (sign.contains("leave")) {
          ArrayList<String> signLines = (ArrayList<String>)W.config
            .getFile().getList(ConfigC.sign_LEAVE.fileKey);
          int linecount = 0;
          for (String line : signLines) {
            if (linecount <= 3)
              signblock.setLine(linecount, 
                  MessageM.replaceAll(line, new String[0])); 
            linecount++;
          } 
          signblock.update();
          continue;
        } 
        if (sign.contains("shop")) {
          ArrayList<String> signLines = (ArrayList<String>)W.config
            .getFile().getList(ConfigC.sign_SHOP.fileKey);
          int linecount = 0;
          for (String line : signLines) {
            if (linecount <= 3)
              signblock.setLine(linecount, 
                  MessageM.replaceAll(line, new String[0])); 
            linecount++;
          } 
          signblock.update();
          continue;
        } 
        for (Arena arena : W.arenaList) {
          if (lines[1].endsWith(arena.arenaName)) {
            if (arena.gameState.equals(Arena.ArenaState.WAITING)) {
              ArrayList<String> signLines = (ArrayList<String>)W.config
                .getFile().getList(
                  ConfigC.sign_WAITING.fileKey);
              int linecount = 0;
              if (signLines != null)
                for (String line : signLines) {
                  if (linecount <= 3)
                    signblock
                      .setLine(
                        linecount, 
                        MessageM.replaceAll(
                          line, "arenaname-" +
                            arena.arenaName,
                                "players-" +
                                arena.playersInArena
                                .size(),
                                "maxplayers-" +
                                arena.maxPlayers,
                                "timeleft-" +
                                arena.timer));
                  linecount++;
                }  
              signblock.update();
              continue;
            } 
            if (arena.gameState
              .equals(Arena.ArenaState.STARTING)) {
              ArrayList<String> signLines = (ArrayList<String>)W.config
                .getFile().getList(
                  ConfigC.sign_STARTING.fileKey);
              int linecount = 0;
              if (signLines != null)
                for (String line : signLines) {
                  if (linecount <= 3)
                    signblock
                      .setLine(
                        linecount, 
                        MessageM.replaceAll(
                          line, new String[] { "arenaname-" + 
                            arena.arenaName, 
                            "players-" + 
                            arena.playersInArena
                            .size(), 
                            "maxplayers-" + 
                            arena.maxPlayers, 
                            "timeleft-" + 
                            arena.timer })); 
                  linecount++;
                }  
              signblock.update();
              continue;
            } 
            if (arena.gameState
              .equals(Arena.ArenaState.INGAME)) {
              ArrayList<String> signLines = (ArrayList<String>)W.config
                .getFile().getList(
                  ConfigC.sign_INGAME.fileKey);
              int linecount = 0;
              if (signLines != null)
                for (String line : signLines) {
                  if (linecount <= 3)
                    signblock
                      .setLine(
                        linecount, 
                        MessageM.replaceAll(
                          line, new String[] { "arenaname-" + 
                            arena.arenaName, 
                            "players-" + 
                            arena.playersInArena
                            .size(), 
                            "maxplayers-" + 
                            arena.maxPlayers, 
                            "timeleft-" + 
                            arena.timer })); 
                  linecount++;
                }  
              signblock.update();
            } 
          } 
        } 
        continue;
      } 
      removeSign(loc);
    } 
  }
}