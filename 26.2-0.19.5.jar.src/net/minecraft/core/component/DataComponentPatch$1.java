/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements DataComponentPatch.CodecGetter {
/*    */   public <T> StreamCodec<RegistryFriendlyByteBuf, T> apply(DataComponentType<T> type) {
/* 68 */     return type.streamCodec().cast();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentPatch$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */