/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ public final class ItemDisplayParameters extends Record {
/*     */   private final FeatureFlagSet enabledFeatures;
/*     */   
/*     */   private final boolean hasPermissions;
/*     */   
/*     */   private final HolderLookup.Provider holders;
/*     */   
/*     */   public ItemDisplayParameters(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders) {
/* 113 */     this.enabledFeatures = enabledFeatures;
/* 113 */     this.hasPermissions = hasPermissions;
/* 113 */     this.holders = holders;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #113	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #113	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #113	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public FeatureFlagSet enabledFeatures() {
/* 113 */     return this.enabledFeatures;
/*     */   }
/*     */   
/*     */   public boolean hasPermissions() {
/* 113 */     return this.hasPermissions;
/*     */   }
/*     */   
/*     */   public HolderLookup.Provider holders() {
/* 113 */     return this.holders;
/*     */   }
/*     */   
/*     */   public boolean needsUpdate(FeatureFlagSet enabledFeatures, boolean hasPermissions, HolderLookup.Provider holders) {
/* 115 */     return (!this.enabledFeatures.equals(enabledFeatures) || this.hasPermissions != hasPermissions || this.holders != holders);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\CreativeModeTab$ItemDisplayParameters.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */