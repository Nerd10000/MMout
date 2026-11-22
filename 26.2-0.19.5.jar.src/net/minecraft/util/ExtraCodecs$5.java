/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ 
/*     */ class null implements Decoder<A> {
/*     */   public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/*     */     try {
/* 565 */       return codec.decode(ops, input);
/* 566 */     } catch (Exception e) {
/* 567 */       return DataResult.error(() -> "Caught exception decoding " + String.valueOf(input) + ": " + e.getMessage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ExtraCodecs$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */