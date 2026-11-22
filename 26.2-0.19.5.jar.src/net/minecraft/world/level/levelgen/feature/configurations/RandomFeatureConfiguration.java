/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ @Deprecated
/*    */ public final class RandomFeatureConfiguration extends Record implements FeatureConfiguration {
/*    */   private final List<WeightedPlacedFeature> features;
/*    */   
/*    */   private final Holder<PlacedFeature> defaultFeature;
/*    */   
/*    */   public static final Codec<RandomFeatureConfiguration> CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<WeightedPlacedFeature> features() {
/* 14 */     return this.features;
/*    */   }
/*    */   
/*    */   public Holder<PlacedFeature> defaultFeature() {
/* 14 */     return this.defaultFeature;
/*    */   }
/*    */   
/*    */   public RandomFeatureConfiguration(List<WeightedPlacedFeature> features, Holder<PlacedFeature> defaultFeature) {
/* 15 */     this.features = features;
/* 15 */     this.defaultFeature = defaultFeature;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.apply2(RandomFeatureConfiguration::new, (App)WeightedPlacedFeature.CODEC.listOf().fieldOf("features").forGetter(()), (App)PlacedFeature.CODEC.fieldOf("default").forGetter(())));
/*    */   }
/*    */   
/*    */   public Stream<Holder<ConfiguredFeature<?, ?>>> getSubFeatures() {
/* 24 */     return Stream.concat(this.features.stream().flatMap(weighted -> ((PlacedFeature)weighted.feature().value()).getFeatures()), ((PlacedFeature)this.defaultFeature.value()).getFeatures());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\RandomFeatureConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */