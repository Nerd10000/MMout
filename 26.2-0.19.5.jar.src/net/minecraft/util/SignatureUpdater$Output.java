package net.minecraft.util;

import java.security.SignatureException;

@FunctionalInterface
public interface Output {
  void update(byte[] paramArrayOfbyte) throws SignatureException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\SignatureUpdater$Output.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */