/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ 
/*     */ final class Data extends Record {
/*     */   private final LevelSettings levelSettings;
/*     */   
/*     */   private final WorldOptions options;
/*     */   
/*     */   private final Registry<LevelStem> existingDimensions;
/*     */   
/*     */   Data(LevelSettings levelSettings, WorldOptions options, Registry<LevelStem> existingDimensions) {
/* 168 */     this.levelSettings = levelSettings;
/* 168 */     this.options = options;
/* 168 */     this.existingDimensions = existingDimensions;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #168	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #168	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #168	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public LevelSettings levelSettings() {
/* 168 */     return this.levelSettings;
/*     */   }
/*     */   
/*     */   public WorldOptions options() {
/* 168 */     return this.options;
/*     */   }
/*     */   
/*     */   public Registry<LevelStem> existingDimensions() {
/* 168 */     return this.existingDimensions;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldOpenFlows$1Data.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */