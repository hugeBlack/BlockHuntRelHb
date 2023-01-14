package nl.Steffion.BlockHunt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import nl.Steffion.BlockHunt.Managers.CommandM;
import nl.Steffion.BlockHunt.Managers.ConfigM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class W {
  public static ArrayList<String> newFiles = new ArrayList<>();
  
  public static ArrayList<CommandM> commands = new ArrayList<>();
  
  public static ConfigM config = new ConfigM("config");
  
  public static ConfigM messages = new ConfigM("messages");
  
  public static ConfigM arenas = new ConfigM("arenas");
  
  public static ConfigM signs = new ConfigM("signs");
  
  public static ConfigM shop = new ConfigM("shop");
  
  public static HashMap<Player, LocationSerializable> pos1 = new HashMap<>();
  
  public static HashMap<Player, LocationSerializable> pos2 = new HashMap<>();
  
  public static ArrayList<Arena> arenaList = new ArrayList<>();
  
  public static Random random = new Random();
  
  public static HashMap<Player, Integer> seekertime = new HashMap<>();
  
  public static HashMap<Player, PlayerArenaData> pData = new HashMap<>();
  
  public static HashMap<Player, ItemStack> choosenBlock = new HashMap<>();
  
  public static HashMap<Player, Boolean> choosenSeeker = new HashMap<>();
  
  public static HashMap<Player, ItemStack> pBlock = new HashMap<>();
  
  public static HashMap<Player, Location> moveLoc = new HashMap<>();
  
  public static HashMap<Player, Location> hiddenLoc = new HashMap<>();
  
  public static HashMap<Player, Boolean> hiddenLocWater = new HashMap<>();
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\W.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */