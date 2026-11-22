/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.random.Weighted;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ 
/*    */ public final class WeightedRandomFeatureConfiguration extends Record implements FeatureConfiguration {
/*    */   private final WeightedList<Holder<PlacedFeature>> features;
/*    */   
/*    */   public static final Codec<WeightedRandomFeatureConfiguration> CODEC;
/*    */   
/*    */   public WeightedRandomFeatureConfiguration(WeightedList<Holder<PlacedFeature>> features) {
/* 12 */     this.features = features;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/WeightedRandomFeatureConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/WeightedRandomFeatureConfiguration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/WeightedRandomFeatureConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/WeightedRandomFeatureConfiguration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/WeightedRandomFeatureConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/WeightedRandomFeatureConfiguration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public WeightedList<Holder<PlacedFeature>> features() {
/* 12 */     return this.features;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)WeightedList.codec(PlacedFeature.CODEC).fieldOf("features").forGetter(WeightedRandomFeatureConfiguration::features)).apply((Applicative)i, WeightedRandomFeatureConfiguration::new));
/*    */   }
/*    */   
/*    */   public Stream<Holder<ConfiguredFeature<?, ?>>> getSubFeatures() {
/* 19 */     return this.features.unwrap().stream().flatMap(weighted -> ((PlacedFeature)((Holder)weighted.value()).value()).getFeatures());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\WeightedRandomFeatureConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */