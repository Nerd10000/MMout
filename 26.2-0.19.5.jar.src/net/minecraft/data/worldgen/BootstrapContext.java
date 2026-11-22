/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface BootstrapContext<T> {
/*    */   Holder.Reference<T> register(ResourceKey<T> paramResourceKey, T paramT, Lifecycle paramLifecycle);
/*    */   
/*    */   default Holder.Reference<T> register(ResourceKey<T> key, T value) {
/* 13 */     return register(key, value, Lifecycle.stable());
/*    */   }
/*    */   
/*    */   <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> paramResourceKey);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\BootstrapContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */