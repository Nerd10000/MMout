package net.minecraft.network.chat.contents.objects;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.chat.FontDescription;

public interface ObjectInfo {
  FontDescription fontDescription();
  
  String defaultFallback();
  
  MapCodec<? extends ObjectInfo> codec();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\objects\ObjectInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */