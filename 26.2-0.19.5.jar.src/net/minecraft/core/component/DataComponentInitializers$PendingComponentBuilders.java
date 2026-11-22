/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ final class PendingComponentBuilders<T> extends Record {
/*     */   private final ResourceKey<? extends Registry<T>> registryKey;
/*     */   
/*     */   private final Map<ResourceKey<T>, DataComponentMap.Builder> builders;
/*     */   
/*     */   private PendingComponentBuilders(ResourceKey<? extends Registry<T>> registryKey, Map<ResourceKey<T>, DataComponentMap.Builder> builders) {
/* 146 */     this.registryKey = registryKey;
/* 146 */     this.builders = builders;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #146	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #146	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #146	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders<TT;>;
/*     */   }
/*     */   
/*     */   public ResourceKey<? extends Registry<T>> registryKey() {
/* 146 */     return this.registryKey;
/*     */   }
/*     */   
/*     */   public Map<ResourceKey<T>, DataComponentMap.Builder> builders() {
/* 146 */     return this.builders;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentInitializers$PendingComponentBuilders.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */