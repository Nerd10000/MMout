package net.minecraft.util;

interface ByteArrayToKeyFunction<T extends java.security.Key> {
  T apply(byte[] paramArrayOfbyte) throws CryptException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Crypt$ByteArrayToKeyFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */