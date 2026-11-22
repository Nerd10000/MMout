/*    */ package net.minecraft.server.dialog.body;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class DialogBodyTypes {
/*    */   public static MapCodec<? extends DialogBody> bootstrap(Registry<MapCodec<? extends DialogBody>> registry) {
/*  9 */     Registry.register(registry, Identifier.withDefaultNamespace("item"), ItemBody.MAP_CODEC);
/* 10 */     return (MapCodec<? extends DialogBody>)Registry.register(registry, Identifier.withDefaultNamespace("plain_message"), PlainMessage.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\body\DialogBodyTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */