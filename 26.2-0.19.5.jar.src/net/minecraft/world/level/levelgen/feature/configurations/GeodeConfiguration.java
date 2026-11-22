/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function13;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.util.valueproviders.IntProviders;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.level.levelgen.GeodeBlockSettings;
/*    */ import net.minecraft.world.level.levelgen.GeodeCrackSettings;
/*    */ import net.minecraft.world.level.levelgen.GeodeLayerSettings;
/*    */ 
/*    */ public final class GeodeConfiguration extends Record implements FeatureConfiguration {
/*    */   private final GeodeBlockSettings geodeBlockSettings;
/*    */   
/*    */   private final GeodeLayerSettings geodeLayerSettings;
/*    */   
/*    */   private final GeodeCrackSettings geodeCrackSettings;
/*    */   
/*    */   private final double usePotentialPlacementsChance;
/*    */   
/*    */   private final double useAlternateLayer0Chance;
/*    */   
/*    */   private final boolean placementsRequireLayer0Alternate;
/*    */   
/*    */   private final IntProvider outerWallDistance;
/*    */   
/*    */   private final IntProvider distributionPoints;
/*    */   
/*    */   private final IntProvider pointOffset;
/*    */   
/*    */   private final int minGenOffset;
/*    */   
/*    */   private final int maxGenOffset;
/*    */   
/*    */   private final double noiseMultiplier;
/*    */   
/*    */   private final int invalidBlocksThreshold;
/*    */   
/*    */   public GeodeConfiguration(GeodeBlockSettings geodeBlockSettings, GeodeLayerSettings geodeLayerSettings, GeodeCrackSettings geodeCrackSettings, double usePotentialPlacementsChance, double useAlternateLayer0Chance, boolean placementsRequireLayer0Alternate, IntProvider outerWallDistance, IntProvider distributionPoints, IntProvider pointOffset, int minGenOffset, int maxGenOffset, double noiseMultiplier, int invalidBlocksThreshold) {
/* 16 */     this.geodeBlockSettings = geodeBlockSettings;
/* 16 */     this.geodeLayerSettings = geodeLayerSettings;
/* 16 */     this.geodeCrackSettings = geodeCrackSettings;
/* 16 */     this.usePotentialPlacementsChance = usePotentialPlacementsChance;
/* 16 */     this.useAlternateLayer0Chance = useAlternateLayer0Chance;
/* 16 */     this.placementsRequireLayer0Alternate = placementsRequireLayer0Alternate;
/* 16 */     this.outerWallDistance = outerWallDistance;
/* 16 */     this.distributionPoints = distributionPoints;
/* 16 */     this.pointOffset = pointOffset;
/* 16 */     this.minGenOffset = minGenOffset;
/* 16 */     this.maxGenOffset = maxGenOffset;
/* 16 */     this.noiseMultiplier = noiseMultiplier;
/* 16 */     this.invalidBlocksThreshold = invalidBlocksThreshold;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/GeodeConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/GeodeConfiguration;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/GeodeConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/GeodeConfiguration;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/GeodeConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/GeodeConfiguration;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GeodeBlockSettings geodeBlockSettings() {
/* 16 */     return this.geodeBlockSettings;
/*    */   }
/*    */   
/*    */   public GeodeLayerSettings geodeLayerSettings() {
/* 16 */     return this.geodeLayerSettings;
/*    */   }
/*    */   
/*    */   public GeodeCrackSettings geodeCrackSettings() {
/* 16 */     return this.geodeCrackSettings;
/*    */   }
/*    */   
/*    */   public double usePotentialPlacementsChance() {
/* 16 */     return this.usePotentialPlacementsChance;
/*    */   }
/*    */   
/*    */   public double useAlternateLayer0Chance() {
/* 16 */     return this.useAlternateLayer0Chance;
/*    */   }
/*    */   
/*    */   public boolean placementsRequireLayer0Alternate() {
/* 16 */     return this.placementsRequireLayer0Alternate;
/*    */   }
/*    */   
/*    */   public IntProvider outerWallDistance() {
/* 16 */     return this.outerWallDistance;
/*    */   }
/*    */   
/*    */   public IntProvider distributionPoints() {
/* 16 */     return this.distributionPoints;
/*    */   }
/*    */   
/*    */   public IntProvider pointOffset() {
/* 16 */     return this.pointOffset;
/*    */   }
/*    */   
/*    */   public int minGenOffset() {
/* 16 */     return this.minGenOffset;
/*    */   }
/*    */   
/*    */   public int maxGenOffset() {
/* 16 */     return this.maxGenOffset;
/*    */   }
/*    */   
/*    */   public double noiseMultiplier() {
/* 16 */     return this.noiseMultiplier;
/*    */   }
/*    */   
/*    */   public int invalidBlocksThreshold() {
/* 16 */     return this.invalidBlocksThreshold;
/*    */   }
/*    */   
/* 31 */   public static final Codec<Double> CHANCE_RANGE = Codec.doubleRange(0.0D, 1.0D);
/*    */   
/*    */   public static final Codec<GeodeConfiguration> CODEC;
/*    */   
/*    */   static {
/* 33 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)GeodeBlockSettings.CODEC.fieldOf("blocks").forGetter(GeodeConfiguration::geodeBlockSettings), (App)GeodeLayerSettings.CODEC.fieldOf("layers").forGetter(GeodeConfiguration::geodeLayerSettings), (App)GeodeCrackSettings.CODEC.fieldOf("crack").forGetter(GeodeConfiguration::geodeCrackSettings), (App)CHANCE_RANGE.optionalFieldOf("use_potential_placements_chance", Double.valueOf(0.35D)).forGetter(GeodeConfiguration::usePotentialPlacementsChance), (App)CHANCE_RANGE.optionalFieldOf("use_alternate_layer0_chance", Double.valueOf(0.0D)).forGetter(GeodeConfiguration::useAlternateLayer0Chance), (App)Codec.BOOL.optionalFieldOf("placements_require_layer0_alternate", Boolean.valueOf(true)).forGetter(GeodeConfiguration::placementsRequireLayer0Alternate), (App)IntProviders.codec(1, 20).optionalFieldOf("outer_wall_distance", UniformInt.of(4, 5)).forGetter(GeodeConfiguration::outerWallDistance), (App)IntProviders.codec(1, 20).optionalFieldOf("distribution_points", UniformInt.of(3, 4)).forGetter(GeodeConfiguration::distributionPoints), (App)IntProviders.codec(0, 10).optionalFieldOf("point_offset", UniformInt.of(1, 2)).forGetter(GeodeConfiguration::pointOffset), (App)Codec.INT.optionalFieldOf("min_gen_offset", Integer.valueOf(-16)).forGetter(GeodeConfiguration::minGenOffset), (App)Codec.INT.optionalFieldOf("max_gen_offset", Integer.valueOf(16)).forGetter(GeodeConfiguration::maxGenOffset), (App)CHANCE_RANGE.optionalFieldOf("noise_multiplier", Double.valueOf(0.05D)).forGetter(GeodeConfiguration::noiseMultiplier), (App)Codec.INT.fieldOf("invalid_blocks_threshold").forGetter(GeodeConfiguration::invalidBlocksThreshold)).apply((Applicative)i, GeodeConfiguration::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\configurations\GeodeConfiguration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */