/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.ToIntFunction;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public final class StepFeatureData extends Record {
/*    */   private final List<PlacedFeature> features;
/*    */   
/*    */   private final ToIntFunction<PlacedFeature> indexMapping;
/*    */   
/*    */   public ToIntFunction<PlacedFeature> indexMapping() {
/* 28 */     return this.indexMapping;
/*    */   }
/*    */   
/*    */   public List<PlacedFeature> features() {
/* 28 */     return this.features;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/biome/FeatureSorter$StepFeatureData;
/*    */   }
/*    */   
/*    */   public StepFeatureData(List<PlacedFeature> features, ToIntFunction<PlacedFeature> indexMapping) {
/* 28 */     this.features = features;
/* 28 */     this.indexMapping = indexMapping;
/*    */   }
/*    */   
/*    */   private StepFeatureData(List<PlacedFeature> features) {
/* 33 */     this(features, Util.createIndexIdentityLookup(features));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\FeatureSorter$StepFeatureData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */