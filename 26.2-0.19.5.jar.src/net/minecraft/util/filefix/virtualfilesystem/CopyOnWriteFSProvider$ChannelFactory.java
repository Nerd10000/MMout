package net.minecraft.util.filefix.virtualfilesystem;

import java.io.IOException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

@FunctionalInterface
interface ChannelFactory<C> {
  C newChannel(Path paramPath, Set<? extends OpenOption> paramSet, FileAttribute<?>... paramVarArgs) throws IOException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\virtualfilesystem\CopyOnWriteFSProvider$ChannelFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */