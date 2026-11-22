package net.minecraft.util.eventlog;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

public interface File {
  Path path();
  
  EventLogDirectory.FileId id();
  
  Reader openReader() throws IOException;
  
  EventLogDirectory.CompressedFile compress() throws IOException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\eventlog\EventLogDirectory$File.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */