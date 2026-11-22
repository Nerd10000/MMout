/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.resources.HolderSetCodec;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.resources.RegistryFixedCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public class RegistryCodecs {
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec) {
/* 11 */     return homogeneousList(registryKey, elementCodec, false);
/*    */   }
/*    */   
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, Codec<E> elementCodec, boolean alwaysUseList) {
/* 15 */     return HolderSetCodec.create(registryKey, (Codec)RegistryFileCodec.create(registryKey, elementCodec), alwaysUseList);
/*    */   }
/*    */   
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey) {
/* 19 */     return homogeneousList(registryKey, false);
/*    */   }
/*    */   
/*    */   public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, boolean alwaysUseList) {
/* 23 */     return HolderSetCodec.create(registryKey, (Codec)RegistryFixedCodec.create(registryKey), alwaysUseList);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistryCodecs.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */