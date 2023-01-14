package nl.Steffion.BlockHunt.Managers;

import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.W;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageM {
  public static void sendMessage(Player player, String message, String... vars) {
    if (player == null) {
      Bukkit.getConsoleSender().sendMessage(
          replaceAll(
            message.replaceAll("%player%", "Console"), vars));
    } else {
      player.sendMessage(replaceAll(
            message.replaceAll("%player%", player.getName()), vars));
    } 
  }
  
  public static void sendFMessage(Player player, ConfigC location, String... vars) {
    if (player == null) {
      Bukkit.getConsoleSender().sendMessage(
          replaceAll(
            location.config.getFile().get(location.location)
            .toString()
            .replaceAll("%player%", "Console"), vars));
    } else {
      player.sendMessage(replaceAll(
            location.config.getFile().get(location.location).toString()
            .replaceAll("%player%", player.getName()), vars));
    } 
  }
  
  public static void broadcastMessage(String message, String... vars) {
    for (Player player : Bukkit.getOnlinePlayers())
      player.sendMessage(replaceAll(
            message.replaceAll("%player%", player.getName()), vars)); 
    Bukkit.getConsoleSender().sendMessage(
        replaceAll(message.replaceAll("%player%", "Console"), 
          vars));
  }
  
  public static void broadcastFMessage(ConfigC location, String... vars) {
    for (Player player : Bukkit.getOnlinePlayers())
      player.sendMessage(replaceAll(
            location.config.getFile().get(location.location).toString()
            .replaceAll("%player%", player.getName()), vars)); 
    Bukkit.getConsoleSender().sendMessage(
        replaceAll(
          location.config.getFile().get(location.location)
          .toString().replaceAll("%player%", "Console"), 
          vars));
  }
  
  public static String replaceAll(String message, String... vars) {
    return replaceColours(replaceColourVars(
          replaceVars(message, vars)));
  }
  
  public static String replaceColours(String message) {
    return message.replaceAll("(&([a-fk-or0-9]))", "§$2");
  }
  
  public static String replaceColourVars(String message) {
    message = message.replaceAll("%N", CType.NORMAL());
    message = message.replaceAll("%W", CType.WARNING());
    message = message.replaceAll("%E", CType.ERROR());
    message = message.replaceAll("%A", CType.ARG());
    message = message.replaceAll("%H", CType.HEADER());
    message = message.replaceAll("%TAG", CType.TAG());
    return message;
  }
  
  public static String replaceVars(String message, String... vars) {
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = vars).length, b = 0; b < i; ) {
      String var = arrayOfString[b];
      String[] split = var.split("-");
      message = message.replaceAll("%" + split[0] + "%", split[1]);
      b++;
    } 
    return message;
  }
  
  public static class CType {
    public static String NORMAL() {
      return (String)W.config.get(ConfigC.chat_normal);
    }
    
    public static String WARNING() {
      return (String)W.config.get(ConfigC.chat_warning);
    }
    
    public static String ERROR() {
      return (String)W.config.get(ConfigC.chat_error);
    }
    
    public static String ARG() {
      return (String)W.config.get(ConfigC.chat_arg);
    }
    
    public static String HEADER() {
      return (String)W.config.get(ConfigC.chat_header);
    }
    
    public static String TAG() {
      return String.valueOf(W.config.get(ConfigC.chat_header)) + 
        (String)W.config.get(ConfigC.chat_tag) + 
        (String)W.config.get(ConfigC.chat_normal);
    }
  }
}