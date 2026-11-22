package net.minecraft.world.level.chunk;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;

public interface Palette<T> {
  int idFor(T paramT, PaletteResize<T> paramPaletteResize);
  
  boolean maybeHas(Predicate<T> paramPredicate);
  
  T valueFor(int paramInt);
  
  void read(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap);
  
  void write(FriendlyByteBuf paramFriendlyByteBuf, IdMap<T> paramIdMap);
  
  int getSerializedSize(IdMap<T> paramIdMap);
  
  int getSize();
  
  Palette<T> copy();
  
  public static interface Factory {
    <A> Palette<A> create(int param1Int, List<A> param1List);
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\Palette.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */