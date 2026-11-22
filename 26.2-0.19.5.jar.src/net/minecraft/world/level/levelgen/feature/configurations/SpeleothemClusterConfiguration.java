/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function14;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.util.valueproviders.FloatProvider;
/*    */ import net.minecraft.util.valueproviders.FloatProviders;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public final class SpeleothemClusterConfiguration extends Record implements FeatureConfiguration {
/*    */   private final BlockState baseBlock;
/*    */   
/*    */   private final BlockState pointedBlock;
/*    */   
/*    */   private final HolderSet<Block> replaceableBlocks;
/*    */   
/*    */   private final int floorToCeilingSearchRange;
/*    */   
/*    */   private final IntProvider height;
/*    */   
/*    */   private final IntProvider radius;
/*    */   
/*    */   private final int maxStalagmiteStalactiteHeightDiff;
/*    */   
/*    */   private final int heightDeviation;
/*    */   
/*    */   private final IntProvider speleothemBlockLayerThickness;
/*    */   
/*    */   private final FloatProvider density;
/*    */   
/*    */   private final FloatProvider wetness;
/*    */   
/*    */   private final float chanceOfSpeleothemAtMaxDistanceFromCenter;
/*    */   
/*    */   private final int maxDistanceFromEdgeAffectingChanceOfSpeleothem;
/*    */   
/*    */   private final int maxDistanceFromCenterAffectingHeightBias;
/*    */   
/*    */   public static final Codec<SpeleothemClusterConfiguration> CODEC;
/*    */   
/*    */   public SpeleothemClusterConfiguration(BlockState baseBlock, BlockState pointedBlock, HolderSet<Block> replaceableBlocks, int floorToCeilingSearchRange, IntProvider height, IntProvider radius, int maxStalagmiteStalactiteHeightDiff, int heightDeviation, IntProvider speleothemBlockLayerThickness, FloatProvider density, FloatProvider wetness, float chanceOfSpeleothemAtMaxDistanceFromCenter, int maxDistanceFromEdgeAffectingChanceOfSpeleothem, int maxDistanceFromCenterAffectingHeightBias) {
/* 15 */     this.baseBlock = baseBlock;
/* 15 */     this.pointedBlock = pointedBlock;
/* 15 */     this.replaceableBlocks = replaceableBlocks;
/* 15 */     this.floorToCeilingSearchRange = floorToCeilingSearchRange;
/* 15 */     this.height = height;
/* 15 */     this.radius = radius;
/* 15 */     this.maxStalagmiteStalactiteHeightDiff = maxStalagmiteStalactiteHeightDiff;
/* 15 */     this.heightDeviation = heightDeviation;
/* 15 */     this.speleothemBlockLayerThickness = speleothemBlockLayerThickness;
/* 15 */     this.density = density;
/* 15 */     this.wetness = wetness;
/* 15 */     this.chanceOfSpeleothemAtMaxDistanceFromCenter = chanceOfSpeleothemAtMaxDistanceFromCenter;
/* 15 */     this.maxDistanceFromEdgeAffectingChanceOfSpeleothem = maxDistanceFromEdgeAffectingChanceOfSpeleothem;
/* 15 */     this.maxDistanceFromCenterAffectingHeightBias = maxDistanceFromCenterAffectingHeightBias;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemClusterConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemClusterConfiguration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemClusterConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemClusterConfiguration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemClusterConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SpeleothemClusterConfiguration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockState baseBlock() {
/* 15 */     return this.baseBlock;
/*    */   }
/*    */   
/*    */   public BlockState pointedBlock() {
/* 15 */     return this.pointedBlock;
/*    */   }
/*    */   
/*    */   public HolderSet<Block> replaceableBlocks() {
/* 15 */     return this.replaceableBlocks;
/*    */   }
/*    */   
/*    */   public int floorToCeilingSearchRange() {
/* 15 */     return this.floorToCeilingSearchRange;
/*    */   }
/*    */   
/*    */   public IntProvider height() {
/* 15 */     return this.height;
/*    */   }
/*    */   
/*    */   public IntProvider radius() {
/* 15 */     return this.radius;
/*    */   }
/*    */   
/*    */   public int maxStalagmiteStalactiteHeightDiff() {
/* 15 */     return this.maxStalagmiteStalactiteHeightDiff;
/*    */   }
/*    */   
/*    */   public int heightDeviation() {
/* 15 */     return this.heightDeviation;
/*    */   }
/*    */   
/*    */   public IntProvider speleothemBlockLayerThickness() {
/* 15 */     return this.speleothemBlockLayerThickness;
/*    */   }
/*    */   
/*    */   public FloatProvider density() {
/* 15 */     return this.density;
/*    */   }
/*    */   
/*    */   public FloatProvider wetness() {
/* 15 */     return this.wetness;
/*    */   }
/*    */   
/*    */   public float chanceOfSpeleothemAtMaxDistanceFromCenter() {
/* 15 */     return this.chanceOfSpeleothemAtMaxDistanceFromCenter;
/*    */   }
/*    */   
/*    */   public int maxDistanceFromEdgeAffectingChanceOfSpeleothem() {
/* 15 */     return this.maxDistanceFromEdgeAffectingChanceOfSpeleothem;
/*    */   }
/*    */   
/*    */   public int maxDistanceFromCenterAffectingHeightBias() {
/* 15 */     return this.maxDistanceFromCenterAffectingHeightBias;
/*    */   }
/*    */   
/*    */   static {
/* 31 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockState.CODEC.fieldOf("base_block").forGetter(()), (App)BlockState.CODEC.fieldOf("pointed_block").forGetter(()), (App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable_blocks").forGetter(()), (App)Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").forGetter(()), (App)IntProviders.codec(1, 128).fieldOf("height").forGetter(()), (App)IntProviders.codec(1, 128).fieldOf("radius").forGetter(()), (App)Codec.intRange(0, 64).fieldOf("max_stalagmite_stalactite_height_diff").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("height_deviation").forGetter(()), (App)IntProviders.codec(0, 128).fieldOf("speleothem_block_layer_thickness").forGetter(()), (App)FloatProviders.codec(0.0F, 2.0F).fieldOf("density").forGetter(()), (App)FloatProviders.codec(0.0F, 2.0F).fieldOf("wetness").forGetter(()), (App)Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_speleothem_at_max_distance_from_center").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("max_distance_from_edge_affecting_chance_of_speleothem").forGetter(()), (App)Codec.intRange(1, 64).fieldOf("max_distance_from_center_affecting_height_bias").forGetter(())).apply((Applicative)i, SpeleothemClusterConfiguration::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\SpeleothemClusterConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */