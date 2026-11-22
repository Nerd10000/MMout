/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.RegistryFileCodec;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public final class ConfiguredFeature<FC extends FeatureConfiguration, F extends Feature<FC>> extends Record {
/*    */   private final F feature;
/*    */   
/*    */   private final FC config;
/*    */   
/*    */   public static final Codec<ConfiguredFeature<?, ?>> DIRECT_CODEC;
/*    */   
/*    */   public ConfiguredFeature(F feature, FC config) {
/* 18 */     this.feature = feature;
/* 18 */     this.config = config;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature<TFC;TF;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature<TFC;TF;>;
/*    */   }
/*    */   
/*    */   public F feature() {
/* 18 */     return this.feature;
/*    */   }
/*    */   
/*    */   public FC config() {
/* 18 */     return this.config;
/*    */   }
/*    */   
/*    */   static {
/* 22 */     DIRECT_CODEC = BuiltInRegistries.FEATURE.byNameCodec().dispatch(f -> f.feature, Feature::configuredCodec);
/*    */   }
/*    */   
/* 24 */   public static final Codec<Holder<ConfiguredFeature<?, ?>>> CODEC = (Codec<Holder<ConfiguredFeature<?, ?>>>)RegistryFileCodec.create(Registries.CONFIGURED_FEATURE, DIRECT_CODEC);
/*    */   
/* 25 */   public static final Codec<HolderSet<ConfiguredFeature<?, ?>>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.CONFIGURED_FEATURE, DIRECT_CODEC);
/*    */   
/*    */   public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
/* 28 */     return this.feature.place(this.config, level, chunkGenerator, random, origin);
/*    */   }
/*    */   
/*    */   public Stream<Holder<ConfiguredFeature<?, ?>>> getSubFeatures() {
/* 32 */     return this.config.getSubFeatures();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 37 */     return "Configured: " + String.valueOf(this.feature) + ": " + String.valueOf(this.config);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\ConfiguredFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */