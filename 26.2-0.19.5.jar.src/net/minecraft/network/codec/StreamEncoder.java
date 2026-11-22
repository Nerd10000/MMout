package net.minecraft.network.codec;

@FunctionalInterface
public interface StreamEncoder<O, T> {
  void encode(O paramO, T paramT);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamEncoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */