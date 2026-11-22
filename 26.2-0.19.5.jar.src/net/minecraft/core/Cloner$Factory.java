/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class Factory {
/* 28 */   private final Map<ResourceKey<? extends Registry<?>>, Cloner<?>> codecs = new HashMap<>();
/*    */   
/*    */   public <T> Factory addCodec(ResourceKey<? extends Registry<? extends T>> key, Codec<T> codec) {
/* 31 */     this.codecs.put(key, new Cloner(codec));
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public <T> Cloner<T> cloner(ResourceKey<? extends Registry<? extends T>> key) {
/* 37 */     return (Cloner<T>)this.codecs.get(key);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Cloner$Factory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */