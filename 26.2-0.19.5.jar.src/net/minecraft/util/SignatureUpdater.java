package net.minecraft.util;

import java.security.SignatureException;

@FunctionalInterface
public interface SignatureUpdater {
  void update(Output paramOutput) throws SignatureException;
  
  @FunctionalInterface
  public static interface Output {
    void update(byte[] param1ArrayOfbyte) throws SignatureException;
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\SignatureUpdater.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */