/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ final class FeatureData extends Record {
/*    */   private final int featureIndex;
/*    */   
/*    */   private final int step;
/*    */   
/*    */   private final PlacedFeature feature;
/*    */   
/*    */   FeatureData(int featureIndex, int step, PlacedFeature feature) {
/* 42 */     this.featureIndex = featureIndex;
/* 42 */     this.step = step;
/* 42 */     this.feature = feature;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$1FeatureData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int featureIndex() {
/* 42 */     return this.featureIndex;
/*    */   }
/*    */   
/*    */   public int step() {
/* 42 */     return this.step;
/*    */   }
/*    */   
/*    */   public PlacedFeature feature() {
/* 42 */     return this.feature;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\FeatureSorter$1FeatureData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */