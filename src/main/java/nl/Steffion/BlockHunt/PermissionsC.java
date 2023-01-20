package nl.Steffion.BlockHunt;

public class PermissionsC {
  public static String main = BlockHunt.pdfFile.getName().toLowerCase() + ".";
  
  public enum PType {
    ALL, PLAYER, MODERATOR, ADMIN, OP;
  }
  
  public enum Permissions {
    info("info", PermissionsC.PType.ALL),
    help("help", PermissionsC.PType.ALL),
    reload("reload", PermissionsC.PType.ADMIN),
    join("join", PermissionsC.PType.PLAYER),
    joinfull("joinfull", PermissionsC.PType.MODERATOR),
    joinsign("joinsign", PermissionsC.PType.PLAYER),
    leave("leave", PermissionsC.PType.PLAYER),
    list("list", PermissionsC.PType.PLAYER),
    shop("shop", PermissionsC.PType.PLAYER),
    shopBlockChooser("shop.blockchooser", PermissionsC.PType.ADMIN),
    shopBlockHuntPass("shop.blockhuntpass", PermissionsC.PType.ADMIN),
    shopKitSelector("shop.kitselector", PermissionsC.PType.ADMIN),
    start("start", PermissionsC.PType.MODERATOR),
    create("create", PermissionsC.PType.ADMIN),
    set("set", PermissionsC.PType.MODERATOR),
    setwarp("setwarp", PermissionsC.PType.MODERATOR),
    signcreate("signcreate", PermissionsC.PType.MODERATOR),
    remove("remove", PermissionsC.PType.ADMIN),
    tokens("tokens", PermissionsC.PType.ADMIN),
    allcommands("allcommands", PermissionsC.PType.OP);
    
    public String perm;
    
    public PermissionsC.PType type;
    
    Permissions(String perm, PermissionsC.PType type) {
      this.perm = perm;
      this.type = type;
    }
  }
}
