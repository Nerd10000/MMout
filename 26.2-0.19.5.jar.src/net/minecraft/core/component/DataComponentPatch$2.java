/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements DataComponentPatch.CodecGetter {
/*    */   public <T> StreamCodec<RegistryFriendlyByteBuf, T> apply(DataComponentType<T> type) {
/* 77 */     StreamCodec<RegistryFriendlyByteBuf, T> original = type.streamCodec().cast();
/* 78 */     return original.apply(ByteBufCodecs.registryFriendlyLengthPrefixed(2147483647));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentPatch$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */