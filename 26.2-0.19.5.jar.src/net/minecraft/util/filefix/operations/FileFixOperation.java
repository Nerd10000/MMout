package net.minecraft.util.filefix.operations;

import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.util.worldupdate.UpgradeProgress;

public interface FileFixOperation {
  void fix(Path paramPath, UpgradeProgress paramUpgradeProgress) throws IOException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\operations\FileFixOperation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */