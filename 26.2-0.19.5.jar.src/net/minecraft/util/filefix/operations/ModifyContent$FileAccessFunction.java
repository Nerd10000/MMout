package net.minecraft.util.filefix.operations;

import net.minecraft.util.filefix.access.FileAccessProvider;

@FunctionalInterface
public interface FileAccessFunction {
  ModifyContent.FixFunction make(FileAccessProvider paramFileAccessProvider);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\operations\ModifyContent$FileAccessFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */