/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistrySynchronization;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ class ContentsCollector {
/* 54 */   private final Map<ResourceKey<? extends Registry<?>>, List<RegistrySynchronization.PackedRegistryEntry>> elements = new HashMap<>();
/*    */   
/*    */   public void append(ResourceKey<? extends Registry<?>> registry, List<RegistrySynchronization.PackedRegistryEntry> elementData) {
/* 58 */     ((List<RegistrySynchronization.PackedRegistryEntry>)this.elements.computeIfAbsent(registry, ignore -> new ArrayList())).addAll(elementData);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\RegistryDataCollector$ContentsCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */