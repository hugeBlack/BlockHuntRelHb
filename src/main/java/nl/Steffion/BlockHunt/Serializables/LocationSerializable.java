package nl.Steffion.BlockHunt.Serializables;

import java.util.HashMap;
import java.util.Map;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("BlockHuntLocation")
public class LocationSerializable extends Location implements ConfigurationSerializable {
  public LocationSerializable(World world, double x, double y, double z, float yaw, float pitch) {
    super(world, x, y, z, yaw, pitch);
  }
  
  public LocationSerializable(Location loc) {
    super(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
  }
  
  public boolean equals(Object o) {
    if (o instanceof LocationSerializable || o instanceof Location) {
      Location loc = (Location)o;
      return (loc.getWorld().getName().equals(getWorld().getName()) && 
        loc.getX() == getX() && loc.getY() == getY() && 
        loc.getZ() == getZ() && loc.getYaw() == getYaw() && 
        loc.getPitch() == getPitch());
    } 
    return false;
  }
  
  public Map<String, Object> serialize() {
    Map<String, Object> map = new HashMap<>();
    map.put("w", getWorld().getName());
    map.put("x", Double.valueOf(getX()));
    map.put("y", Double.valueOf(getY()));
    map.put("z", Double.valueOf(getZ()));
    if (getYaw() != 0.0D)
      map.put("a", Float.valueOf(getYaw())); 
    if (getPitch() != 0.0D)
      map.put("p", Float.valueOf(getPitch())); 
    return map;
  }
  
  public static LocationSerializable deserialize(Map<String, Object> map) {
    World w = Bukkit.getWorld((String)M.g(map, "w", ""));
    if (w == null) {
      MessageM.sendMessage(
          null, 
          "%EError deserializing LocationSerializable - world not found! (%A%w%%E)", new String[] { "w-" + w });
      return null;
    } 
    return new LocationSerializable(w, ((Double)M.g(map, "x", Double.valueOf(0.0D))).doubleValue(), (
        (Double)M.g(map, "y", Double.valueOf(0.0D))).doubleValue(), ((Double)M.g(map, "z", Double.valueOf(0.0D))).doubleValue(), (
        (Double)M.g(map, "a", Double.valueOf(0.0D))).floatValue(), ((Double)M.g(map, 
          "p", Double.valueOf(0.0D))).floatValue());
  }
}