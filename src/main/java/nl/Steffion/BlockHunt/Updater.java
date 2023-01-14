package nl.Steffion.BlockHunt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import nl.Steffion.BlockHunt.Managers.MessageM;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Updater {
  private Plugin plugin;
  
  private UpdateType type;
  
  private String versionTitle;
  
  private String versionLink;
  
  private long totalSize;
  
  private int sizeLine;
  
  private int multiplier;
  
  private boolean announce;
  
  private URL url;
  
  private File file;
  
  private Thread thread;
  
  private static final String DBOUrl = "http://dev.bukkit.org/bukkit-plugins/";
  
  private String[] noUpdateTag = new String[] { "-DEV", "-PRE", "-SNAPSHOT" };
  
  private static final int BYTE_SIZE = 1024;
  
  private String updateFolder = YamlConfiguration.loadConfiguration(
      new File("bukkit.yml")).getString("settings.update-folder");
  
  private UpdateResult result = UpdateResult.SUCCESS;
  
  private static final String TITLE = "title";
  
  private static final String LINK = "link";
  
  private static final String ITEM = "item";
  
  public enum UpdateResult {
    SUCCESS, NO_UPDATE, FAIL_DOWNLOAD, FAIL_DBO, FAIL_NOVERSION, FAIL_BADSLUG, UPDATE_AVAILABLE;
  }
  
  public enum UpdateType {
    DEFAULT, NO_VERSION_CHECK, NO_DOWNLOAD;
  }
  
  public Updater(Plugin plugin, String slug, File file, UpdateType type, boolean announce) {
    this.plugin = plugin;
    this.type = type;
    this.announce = announce;
    this.file = file;
    try {
      this.url = new URL("http://dev.bukkit.org/bukkit-plugins/" + slug + "/files.rss");
    } catch (MalformedURLException ex) {
      MessageM.sendMessage(null, "%TAG%WThe author of this plugin (" + 
          (String)plugin.getDescription().getAuthors().get(0) + 
          ") has misconfigured their Auto Update system", new String[0]);
      MessageM.sendMessage(null, "%TAG%WThe project slug given ('" + slug + 
          "') is invalid. Please nag the author about this.", new String[0]);
      this.result = UpdateResult.FAIL_BADSLUG;
    } 
    this.thread = new Thread(new UpdateRunnable());
    this.thread.start();
  }
  
  public UpdateResult getResult() {
    waitForThread();
    return this.result;
  }
  
  public long getFileSize() {
    waitForThread();
    return this.totalSize;
  }
  
  public String getLatestVersionString() {
    waitForThread();
    return this.versionTitle;
  }
  
  public void waitForThread() {
    if (this.thread.isAlive())
      try {
        this.thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }  
  }
  
  private void saveFile(File folder, String file, String u) {
    if (!folder.exists())
      folder.mkdir(); 
    BufferedInputStream in = null;
    FileOutputStream fout = null;
    try {
      URL url = new URL(u);
      int fileLength = url.openConnection().getContentLength();
      in = new BufferedInputStream(url.openStream());
      fout = new FileOutputStream(String.valueOf(folder.getAbsolutePath()) + "/" + file);
      byte[] data = new byte[1024];
      if (this.announce)
        MessageM.sendMessage(null, 
            "%TAG%NAbout to download a new update: " + this.versionTitle, new String[0]); 
      long downloaded = 0L;
      int count;
      while ((count = in.read(data, 0, 1024)) != -1) {
        downloaded += count;
        fout.write(data, 0, count);
        int percent = (int)(downloaded * 100L / fileLength);
        if (this.announce && (percent % 10 == 0))
          MessageM.sendMessage(null, "%TAG%NDownloading update: " + 
              percent + "% of " + fileLength + " bytes.", new String[0]); 
      } 
      byte b;
      int i;
      File[] arrayOfFile;
      for (i = (arrayOfFile = (new File("plugins/" + this.updateFolder)).listFiles()).length, b = 0; b < i; ) {
        File xFile = arrayOfFile[b];
        if (xFile.getName().endsWith(".zip"))
          xFile.delete(); 
        b++;
      } 
      File dFile = new File(String.valueOf(folder.getAbsolutePath()) + "/" + file);
      if (dFile.getName().endsWith(".zip"))
        unzip(dFile.getCanonicalPath()); 
      if (this.announce)
        MessageM.sendMessage(null, "%TAG%NFinished updating.", new String[0]); 
    } catch (Exception ex) {
      MessageM.sendMessage(null, 
          "%TAG%WThe auto-updater tried to download a new update, but was unsuccessful.", new String[0]);
      this.result = UpdateResult.FAIL_DOWNLOAD;
    } finally {
      try {
        if (in != null)
          in.close(); 
        if (fout != null)
          fout.close(); 
      } catch (Exception exception) {}
    } 
  }
  
  private void unzip(String file) {
    try {
      File fSourceZip = new File(file);
      String zipPath = file.substring(0, file.length() - 4);
      ZipFile zipFile = new ZipFile(fSourceZip);
      Enumeration<? extends ZipEntry> e = zipFile.entries();
      while (e.hasMoreElements()) {
        ZipEntry entry = e.nextElement();
        File destinationFilePath = new File(zipPath, entry.getName());
        destinationFilePath.getParentFile().mkdirs();
        if (entry.isDirectory())
          continue; 
        BufferedInputStream bis = new BufferedInputStream(
            zipFile.getInputStream(entry));
        byte[] buffer = new byte[1024];
        FileOutputStream fos = new FileOutputStream(
            destinationFilePath);
        BufferedOutputStream bos = new BufferedOutputStream(fos, 
            1024);
        int j;
        while ((j = bis.read(buffer, 0, 1024)) != -1)
          bos.write(buffer, 0, j); 
        bos.flush();
        bos.close();
        bis.close();
        String name = destinationFilePath.getName();
        if (name.endsWith(".jar") && pluginFile(name))
          destinationFilePath.renameTo(new File("plugins/" + 
                this.updateFolder + "/" + name)); 
        entry = null;
        destinationFilePath = null;
      } 
      e = null;
      zipFile.close();
      zipFile = null;
      byte b;
      int i;
      File[] arrayOfFile;
      for (i = (arrayOfFile = (new File(zipPath)).listFiles()).length, b = 0; b < i; ) {
        File dFile = arrayOfFile[b];
        if (dFile.isDirectory() && 
          pluginFile(dFile.getName())) {
          File oFile = new File("plugins/" + dFile.getName());
          File[] contents = oFile.listFiles();
          byte b1;
          int j;
          File[] arrayOfFile1;
          for (j = (arrayOfFile1 = dFile.listFiles()).length, b1 = 0; b1 < j; ) {
            File cFile = arrayOfFile1[b1];
            boolean found = false;
            byte b2;
            int k;
            File[] arrayOfFile2;
            for (k = (arrayOfFile2 = contents).length, b2 = 0; b2 < k; ) {
              File xFile = arrayOfFile2[b2];
              if (xFile.getName().equals(cFile.getName())) {
                found = true;
                break;
              } 
              b2++;
            } 
            if (!found) {
              cFile.renameTo(new File(oFile
                    .getCanonicalFile() + 
                    "/" + 
                    cFile.getName()));
            } else {
              cFile.delete();
            } 
            b1++;
          } 
        } 
        dFile.delete();
        b++;
      } 
      (new File(zipPath)).delete();
      fSourceZip.delete();
    } catch (IOException ex) {
      ex.printStackTrace();
      MessageM.sendMessage(
          null, 
          "%TAG%WThe auto-updater tried to unzip a new update file, but was unsuccessful.", new String[0]);
      this.result = UpdateResult.FAIL_DOWNLOAD;
    } 
    (new File(file)).delete();
  }
  
  public boolean pluginFile(String name) {
    byte b;
    int i;
    File[] arrayOfFile;
    for (i = (arrayOfFile = (new File("plugins")).listFiles()).length, b = 0; b < i; ) {
      File file = arrayOfFile[b];
      if (file.getName().equals(name))
        return true; 
      b++;
    } 
    return false;
  }
  
  private String getFile(String link) {
    String download = null;
    /*try {
      URL url = new URL(link);
      URLConnection urlConn = url.openConnection();
      InputStreamReader inStream = new InputStreamReader(
          urlConn.getInputStream());
      BufferedReader buff = new BufferedReader(inStream);
      int counter = 0;
      String line;
      while ((line = buff.readLine()) != null) {
        counter++;
        if (line.contains("<li class=\"user-action user-action-download\">")) {
          download = line.split("<a href=\"")[1]
            .split("\">Download</a>")[0];
          continue;
        } 
        if (line.contains("<dt>Size</dt>")) {
          this.sizeLine = counter + 1;
          continue;
        } 
        if (counter == this.sizeLine) {
          String size = line.replaceAll("<dd>", "").replaceAll(
              "</dd>", "");
          this.multiplier = size.contains("MiB") ? 1048576 : 1024;
          size = size.replace(" KiB", "").replace(" MiB", "");
          this.totalSize = (long)(Double.parseDouble(size) * this.multiplier);
        } 
      } 
      urlConn = null;
      inStream = null;
      buff.close();
      buff = null;
    } catch (Exception ex) {
      ex.printStackTrace();
      MessageM.sendMessage(null, 
          "%TAG%WThe auto-updater tried to contact dev.bukkit.org, but was unsuccessful.", new String[0]);
      this.result = UpdateResult.FAIL_DBO;
      return null;
    } 
    return download;*/
    this.result = UpdateResult.FAIL_DBO;
    return null;
  }
  
  private boolean versionCheck(String title) {
    if (this.type != UpdateType.NO_VERSION_CHECK) {
      String version = this.plugin.getDescription().getVersion();
      if ((title.split(" v")).length == 2) {
        String remoteVersion = title.split(" v")[1].split(" ")[0];
        int remVer = -1, curVer = 0;
        try {
          remVer = calVer(remoteVersion).intValue();
          curVer = calVer(version).intValue();
        } catch (NumberFormatException nfe) {
          remVer = -1;
        } 
        if (hasTag(version) || version.equalsIgnoreCase(remoteVersion) || 
          curVer >= remVer) {
          this.result = UpdateResult.NO_UPDATE;
          return false;
        } 
      } else {
        MessageM.sendMessage(null, "%TAG%WThe author of this plugin (" + 
            (String)this.plugin.getDescription().getAuthors().get(0) + 
            ") has misconfigured their Auto Update system", new String[0]);
        MessageM.sendMessage(
            null, 
            "%TAG%WFiles uploaded to BukkitDev should contain the version number, seperated from the name by a 'v', such as PluginName v1.0", new String[0]);
        this.result = UpdateResult.FAIL_NOVERSION;
        return false;
      } 
    } 
    return true;
  }
  
  private Integer calVer(String s) throws NumberFormatException {
    if (s.contains(".")) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < s.length(); i++) {
        Character c = Character.valueOf(s.charAt(i));
        if (Character.isLetterOrDigit(c.charValue()))
          sb.append(c); 
      } 
      return Integer.valueOf(Integer.parseInt(sb.toString()));
    } 
    return Integer.valueOf(Integer.parseInt(s));
  }
  
  private boolean hasTag(String version) {
    byte b;
    int i;
    String[] arrayOfString;
    for (i = (arrayOfString = this.noUpdateTag).length, b = 0; b < i; ) {
      String string = arrayOfString[b];
      if (version.contains(string))
        return true; 
      b++;
    } 
    return false;
  }
  
  private boolean readFeed() {
    try {
      String title = "";
      String link = "";
      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      InputStream in = read();
      if (in != null) {
        XMLEventReader eventReader = inputFactory
          .createXMLEventReader(in);
        while (eventReader.hasNext()) {
          XMLEvent event = eventReader.nextEvent();
          if (event.isStartElement()) {
            if (event.asStartElement().getName().getLocalPart()
              .equals("title")) {
              event = eventReader.nextEvent();
              title = event.asCharacters().getData();
              continue;
            } 
            if (event.asStartElement().getName().getLocalPart()
              .equals("link")) {
              event = eventReader.nextEvent();
              link = event.asCharacters().getData();
            } 
            continue;
          } 
          if (event.isEndElement() && 
            event.asEndElement().getName().getLocalPart()
            .equals("item")) {
            this.versionTitle = title;
            this.versionLink = link;
            break;
          } 
        } 
        return true;
      } 
      return false;
    } catch (XMLStreamException e) {
      MessageM.sendMessage(null, 
          "%TAG%WCould not reach dev.bukkit.org for update checking. Is it offline?", new String[0]);
      return false;
    } 
  }
  
  private InputStream read() {
    try {
      return this.url.openStream();
    } catch (IOException e) {
      MessageM.sendMessage(
          null, 
          "%TAG%WCould not reach BukkitDev file stream for update checking. Is dev.bukkit.org offline?", new String[0]);
      return null;
    } 
  }
  
  private class UpdateRunnable implements Runnable {
    public void run() {
      if (Updater.this.url != null)
        if (Updater.this.readFeed() && 
          Updater.this.versionCheck(Updater.this.versionTitle)) {
          String fileLink = Updater.this.getFile(Updater.this.versionLink);
          if (fileLink != null && Updater.this.type != Updater.UpdateType.NO_DOWNLOAD) {
            String name = Updater.this.file.getName();
            if (fileLink.endsWith(".zip")) {
              String[] split = fileLink.split("/");
              name = split[split.length - 1];
            } 
            Updater.this.saveFile(new File("plugins/" + Updater.this.updateFolder), name, 
                fileLink);
          } else {
            Updater.this.result = Updater.UpdateResult.UPDATE_AVAILABLE;
          } 
        }  
    }
  }
}


/* Location:              E:\minecraft\hideandSeek\plugins\BlockHuntRel-1.18.2.jar!\nl\Steffion\BlockHunt\Updater.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */