package nl.Steffion.BlockHunt.Managers;

import java.io.File;
import nl.Steffion.BlockHunt.BlockHunt;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.W;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigM {
  String fileName;
  
  File file;
  
  FileConfiguration fileC;
  
  ConfigurationSection fileCS;
  
  String fileLocation;
  
  public ConfigM(String fileName) {
    this.fileName = fileName;
    this.file = new File("plugins/" + BlockHunt.pdfFile.getName(), 
        String.valueOf(fileName) + ".yml");
    this.fileLocation = BlockHunt.pdfFile.getName();
    this.fileC = (FileConfiguration)new YamlConfiguration();
    checkFile();
    this.fileCS = this.fileC.getConfigurationSection("");
    load();
  }
  
  public ConfigM(String fileName, String fileLocation) {
    this.fileName = fileName;
    this.file = new File("plugins/" + BlockHunt.pdfFile.getName() + 
        "/" + fileLocation, String.valueOf(fileName) + ".yml");
    this.fileLocation = String.valueOf(BlockHunt.pdfFile.getName()) + "/" + 
      fileLocation;
    this.fileC = (FileConfiguration)new YamlConfiguration();
    checkFile();
    this.fileCS = this.fileC.getConfigurationSection("");
    load();
  }
  
  public static void newFiles() {
    setDefaults();
    for (String fileName : W.newFiles) {
      MessageM.sendMessage(
          null, 
          "%TAG%WCouldn't find '%A%fileName%.yml%W' creating new one.", new String[] { "fileName-" + fileName });
    } 
    W.newFiles.clear();
  }
  
  public static void setDefaults() {
    int b;
    int i;
    ConfigC[] arrayOfConfigC;
    for (i = (arrayOfConfigC = ConfigC.values()).length, b = 0; b < i; ) {
      ConfigC value = arrayOfConfigC[b];
      value.config.load();
      if (value.config.getFile().get(value.location) == null) {
        value.config.getFile().set(value.location, value.value);
        value.config.save();
      } 
      b++;
    } 
  }
  
  public void checkFile() {
    if (!this.file.exists())
      try {
        this.file.getParentFile().mkdirs();
        this.file.createNewFile();
        if (this.fileLocation == BlockHunt.pdfFile.getName()) {
          W.newFiles.add(this.fileName);
        } else {
          W.newFiles.add(String.valueOf(this.fileLocation) + this.fileName);
        } 
      } catch (Exception e) {
        e.printStackTrace();
      }  
  }
  
  public void save() {
    try {
      this.fileC.save(this.file);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void load() {
    checkFile();
    if (this.file.exists())
      try {
        this.fileC.load(this.file);
      } catch (Exception e) {
        e.printStackTrace();
      }  
  }
  
  public FileConfiguration getFile() {
    return this.fileC;
  }
  
  public Object get(ConfigC location) {
    return getFile().get(location.location);
  }
}