package net.minecraft.util.filefix.operations;

import java.io.IOException;
import net.minecraft.util.worldupdate.UpgradeProgress;

@FunctionalInterface
public interface FixFunction {
  void run(UpgradeProgress paramUpgradeProgress) throws IOException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\operations\ModifyContent$FixFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */