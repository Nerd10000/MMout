package net.minecraft.commands.synchronization;

import com.google.gson.JsonObject;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.network.FriendlyByteBuf;

public interface ArgumentTypeInfo<A extends com.mojang.brigadier.arguments.ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> {
  void serializeToNetwork(T paramT, FriendlyByteBuf paramFriendlyByteBuf);
  
  T deserializeFromNetwork(FriendlyByteBuf paramFriendlyByteBuf);
  
  void serializeToJson(T paramT, JsonObject paramJsonObject);
  
  T unpack(A paramA);
  
  public static interface Template<A extends com.mojang.brigadier.arguments.ArgumentType<?>> {
    A instantiate(CommandBuildContext param1CommandBuildContext);
    
    ArgumentTypeInfo<A, ?> type();
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\synchronization\ArgumentTypeInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */