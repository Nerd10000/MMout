/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagNetworkSerialization;
/*     */ 
/*     */ class TagCollector {
/* 176 */   private final Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags = new HashMap<>();
/*     */   
/*     */   public void append(ResourceKey<? extends Registry<?>> registry, TagNetworkSerialization.NetworkPayload tagData) {
/* 179 */     this.tags.put(registry, tagData);
/*     */   }
/*     */   
/*     */   public void forEach(BiConsumer<? super ResourceKey<? extends Registry<?>>, ? super TagNetworkSerialization.NetworkPayload> action) {
/* 183 */     this.tags.forEach(action);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\RegistryDataCollector$TagCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */