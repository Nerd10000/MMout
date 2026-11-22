/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.mojang.serialization.DataResult;
/*    */ 
/*    */ class null extends CacheLoader<EncoderCache.Key<?, ?>, DataResult<?>> {
/*    */   null(EncoderCache this$0) {}
/*    */   
/*    */   public DataResult<?> load(EncoderCache.Key<?, ?> key) {
/* 25 */     return key.resolve();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\EncoderCache$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */