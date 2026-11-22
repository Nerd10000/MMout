package net.minecraft.network.codec;

@FunctionalInterface
public interface StreamDecoder<I, T> {
  T decode(I paramI);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamDecoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */