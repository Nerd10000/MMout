/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import net.minecraft.nbt.Tag;
/*    */ 
/*    */ class null implements Codec<A> {
/*    */   public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 34 */     return codec.decode(ops, input);
/*    */   }
/*    */   
/*    */   public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 40 */     return ((DataResult)EncoderCache.this.cache.getUnchecked(new EncoderCache.Key<>(codec, input, ops))).map(value -> {
/*    */           if (value instanceof Tag) {
/*    */             Tag tag = (Tag)value;
/*    */             return tag.copy();
/*    */           } 
/*    */           return value;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\EncoderCache$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */