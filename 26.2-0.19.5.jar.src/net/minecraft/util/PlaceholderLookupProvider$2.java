/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JavaOps;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ 
/*    */ class null implements RegistryContextSwapper {
/*    */   public <T> DataResult<T> swapTo(Codec<T> codec, T value, HolderLookup.Provider newContext) {
/* 53 */     return codec
/* 54 */       .encodeStart((DynamicOps)PlaceholderLookupProvider.this.createSerializationContext((DynamicOps<?>)JavaOps.INSTANCE), value)
/* 55 */       .flatMap(v -> codec.parse((DynamicOps)newContext.createSerializationContext((DynamicOps)JavaOps.INSTANCE), v));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\PlaceholderLookupProvider$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */