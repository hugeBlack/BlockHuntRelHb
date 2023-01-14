package nl.Steffion.BlockHunt.Managers;

import java.util.List;
import nl.Steffion.BlockHunt.Commands.DefaultCMD;
import nl.Steffion.BlockHunt.ConfigC;
import nl.Steffion.BlockHunt.PermissionsC;
import nl.Steffion.BlockHunt.W;

public class CommandM {
  public String name;
  
  public String label;
  
  public String args;
  
  public String argsalias;
  
  public PermissionsC.Permissions permission;
  
  public ConfigC help;
  
  public boolean enabled;
  
  public List<String> mainTABlist;
  
  public DefaultCMD CMD;
  
  public String usage;
  
  public CommandM(String name, String label, String args, String argsalias, PermissionsC.Permissions permission, ConfigC help, Boolean enabled, List<String> mainTABlist, DefaultCMD CMD, String usage) {
    this.name = name;
    this.label = label;
    this.args = args;
    this.argsalias = argsalias;
    this.permission = permission;
    this.help = help;
    this.enabled = enabled.booleanValue();
    this.mainTABlist = mainTABlist;
    this.CMD = CMD;
    this.usage = usage;
    W.commands.add(this);
  }
}