/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.Noises;
/*     */ import net.minecraft.world.level.levelgen.SurfaceRules;
/*     */ import net.minecraft.world.level.levelgen.VerticalAnchor;
/*     */ 
/*     */ public class SurfaceRuleData {
/*  23 */   private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);
/*     */   
/*  25 */   private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
/*     */   
/*  26 */   private static final SurfaceRules.RuleSource WHITE_TERRACOTTA = makeStateRule((Block)Blocks.DYED_TERRACOTTA.white());
/*     */   
/*  27 */   private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = makeStateRule((Block)Blocks.DYED_TERRACOTTA.orange());
/*     */   
/*  28 */   private static final SurfaceRules.RuleSource TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
/*     */   
/*  29 */   private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
/*     */   
/*  30 */   private static final SurfaceRules.RuleSource RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
/*     */   
/*  31 */   private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
/*     */   
/*  32 */   private static final SurfaceRules.RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);
/*     */   
/*  33 */   private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
/*     */   
/*  34 */   private static final SurfaceRules.RuleSource PODZOL = makeStateRule(Blocks.PODZOL);
/*     */   
/*  35 */   private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);
/*     */   
/*  36 */   private static final SurfaceRules.RuleSource MYCELIUM = makeStateRule(Blocks.MYCELIUM);
/*     */   
/*  37 */   private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
/*     */   
/*  38 */   private static final SurfaceRules.RuleSource CALCITE = makeStateRule(Blocks.CALCITE);
/*     */   
/*  39 */   private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
/*     */   
/*  40 */   private static final SurfaceRules.RuleSource SAND = makeStateRule(Blocks.SAND);
/*     */   
/*  41 */   private static final SurfaceRules.RuleSource SANDSTONE = makeStateRule(Blocks.SANDSTONE);
/*     */   
/*  42 */   private static final SurfaceRules.RuleSource PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);
/*     */   
/*  43 */   private static final SurfaceRules.RuleSource SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
/*     */   
/*  44 */   private static final SurfaceRules.RuleSource MUD = makeStateRule(Blocks.MUD);
/*     */   
/*  45 */   private static final SurfaceRules.RuleSource POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
/*     */   
/*  46 */   private static final SurfaceRules.RuleSource ICE = makeStateRule(Blocks.ICE);
/*     */   
/*  47 */   private static final SurfaceRules.RuleSource WATER = makeStateRule(Blocks.WATER);
/*     */   
/*  49 */   private static final SurfaceRules.RuleSource LAVA = makeStateRule(Blocks.LAVA);
/*     */   
/*  50 */   private static final SurfaceRules.RuleSource NETHERRACK = makeStateRule(Blocks.NETHERRACK);
/*     */   
/*  51 */   private static final SurfaceRules.RuleSource SOUL_SAND = makeStateRule(Blocks.SOUL_SAND);
/*     */   
/*  52 */   private static final SurfaceRules.RuleSource SOUL_SOIL = makeStateRule(Blocks.SOUL_SOIL);
/*     */   
/*  53 */   private static final SurfaceRules.RuleSource BASALT = makeStateRule(Blocks.BASALT);
/*     */   
/*  54 */   private static final SurfaceRules.RuleSource BLACKSTONE = makeStateRule(Blocks.BLACKSTONE);
/*     */   
/*  55 */   private static final SurfaceRules.RuleSource WARPED_WART_BLOCK = makeStateRule(Blocks.WARPED_WART_BLOCK);
/*     */   
/*  56 */   private static final SurfaceRules.RuleSource WARPED_NYLIUM = makeStateRule(Blocks.WARPED_NYLIUM);
/*     */   
/*  57 */   private static final SurfaceRules.RuleSource NETHER_WART_BLOCK = makeStateRule(Blocks.NETHER_WART_BLOCK);
/*     */   
/*  58 */   private static final SurfaceRules.RuleSource CRIMSON_NYLIUM = makeStateRule(Blocks.CRIMSON_NYLIUM);
/*     */   
/*  60 */   private static final SurfaceRules.RuleSource ENDSTONE = makeStateRule(Blocks.END_STONE);
/*     */   
/*  62 */   private static final SurfaceRules.RuleSource CINNABAR = makeStateRule(Blocks.CINNABAR);
/*     */   
/*  63 */   private static final SurfaceRules.RuleSource SULFUR = makeStateRule(Blocks.SULFUR);
/*     */   
/*     */   private static SurfaceRules.RuleSource makeStateRule(Block block) {
/*  66 */     return SurfaceRules.state(block.defaultBlockState());
/*     */   }
/*     */   
/*     */   public static SurfaceRules.RuleSource overworld(HolderGetter<Biome> biomes) {
/*  70 */     return overworldLike(biomes, true, false, true);
/*     */   }
/*     */   
/*     */   public static SurfaceRules.RuleSource overworldLike(HolderGetter<Biome> biomes, boolean doPreliminarySurfaceCheck, boolean bedrockRoof, boolean bedrockFloor) {
/*  74 */     SurfaceRules.ConditionSource woodedBadlandsTop = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(97), 2);
/*  75 */     SurfaceRules.ConditionSource badlandsTop = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(256), 0);
/*  76 */     SurfaceRules.ConditionSource badlandsHeightCondition = SurfaceRules.yStartCheck(VerticalAnchor.absolute(63), -1);
/*  77 */     SurfaceRules.ConditionSource badlandsMid = SurfaceRules.yStartCheck(VerticalAnchor.absolute(74), 1);
/*  79 */     SurfaceRules.ConditionSource mangroveSwampPuddleLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0);
/*  80 */     SurfaceRules.ConditionSource swampPuddleLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(62), 0);
/*  81 */     SurfaceRules.ConditionSource aboveOverworldSeaLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(63), 0);
/*  85 */     SurfaceRules.ConditionSource notUnderwater = SurfaceRules.waterBlockCheck(-1, 0);
/*  86 */     SurfaceRules.ConditionSource aboveWater = SurfaceRules.waterBlockCheck(0, 0);
/*  88 */     SurfaceRules.ConditionSource notUnderDeepWater = SurfaceRules.waterStartCheck(-6, -1);
/*  90 */     SurfaceRules.ConditionSource hole = SurfaceRules.hole();
/*  91 */     SurfaceRules.ConditionSource frozenOcean = SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN });
/*  92 */     SurfaceRules.ConditionSource steep = SurfaceRules.steep();
/*  94 */     SurfaceRules.RuleSource grassOrDirtIfUnderwater = SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(aboveWater, GRASS_BLOCK), DIRT });
/*  99 */     SurfaceRules.RuleSource sandOrSandstoneIfCeiling = SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, SANDSTONE), SAND });
/* 104 */     SurfaceRules.RuleSource gravelOrStoneIfCeiling = SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL });
/* 109 */     SurfaceRules.ConditionSource biomesWithSandAndSandstone = SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.WARM_OCEAN, Biomes.BEACH, Biomes.SNOWY_BEACH });
/* 110 */     SurfaceRules.ConditionSource biomesWithSandAndVeryDeepSandstone = SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.DESERT });
/* 111 */     SurfaceRules.RuleSource sulfurCaveBands = SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.noiseCondition3d(Noises.SULFUR_CAVE_GRADIENT, -0.4000000059604645D, -0.10000000149011612D), CINNABAR), 
/* 113 */           SurfaceRules.ifTrue(SurfaceRules.noiseCondition3d(Noises.SULFUR_CAVE_GRADIENT, 0.0D, 0.4000000059604645D), SULFUR), 
/* 114 */           SurfaceRules.ifTrue(SurfaceRules.noiseCondition3d(Noises.SULFUR_CAVE_GRADIENT, 0.4000000059604645D), CINNABAR) });
/* 117 */     SurfaceRules.RuleSource commonSurfaceAndUnderRules = SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.STONY_PEAKS }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.noiseCondition2d(Noises.CALCITE, -0.0125D, 0.0125D), CALCITE), STONE })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.STONY_SHORE }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.noiseCondition2d(Noises.GRAVEL, -0.05D, 0.05D), gravelOrStoneIfCeiling), STONE })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.WINDSWEPT_HILLS }), SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE)), 
/* 127 */           SurfaceRules.ifTrue(biomesWithSandAndSandstone, sandOrSandstoneIfCeiling), 
/* 128 */           SurfaceRules.ifTrue(biomesWithSandAndVeryDeepSandstone, sandOrSandstoneIfCeiling), 
/* 129 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.DRIPSTONE_CAVES }), STONE), 
/* 130 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.SULFUR_CAVES }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { sulfurCaveBands, STONE })) });
/* 135 */     SurfaceRules.RuleSource powderSnowUnderRule = SurfaceRules.ifTrue(SurfaceRules.noiseCondition2d(Noises.POWDER_SNOW, 0.45D, 0.58D), SurfaceRules.ifTrue(aboveWater, POWDER_SNOW));
/* 136 */     SurfaceRules.RuleSource powderSnowSurfaceRule = SurfaceRules.ifTrue(SurfaceRules.noiseCondition2d(Noises.POWDER_SNOW, 0.35D, 0.6D), SurfaceRules.ifTrue(aboveWater, POWDER_SNOW));
/* 138 */     SurfaceRules.RuleSource biomeUnderSurfaceRule = SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.FROZEN_PEAKS }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(steep, PACKED_ICE), 
/* 141 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition2d(Noises.PACKED_ICE, -0.5D, 0.2D), PACKED_ICE), 
/* 142 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition2d(Noises.ICE, -0.0625D, 0.025D), ICE), 
/* 143 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK) })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.SNOWY_SLOPES }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(steep, STONE), powderSnowUnderRule, 
/*     */                 
/* 148 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK) })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.JAGGED_PEAKS }), STONE), 
/* 151 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.GROVE }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { powderSnowUnderRule, DIRT })), commonSurfaceAndUnderRules, 
/*     */           
/* 156 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.WINDSWEPT_SAVANNA }), SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE)), 
/* 157 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.WINDSWEPT_GRAVELLY_HILLS }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), gravelOrStoneIfCeiling), 
/* 159 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), 
/* 160 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), DIRT), gravelOrStoneIfCeiling })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.MANGROVE_SWAMP }), MUD), DIRT });
/* 167 */     SurfaceRules.RuleSource biomeSurfaceRule = SurfaceRules.sequence(new SurfaceRules.RuleSource[] { 
/* 168 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.FROZEN_PEAKS }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(steep, PACKED_ICE), 
/* 170 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition2d(Noises.PACKED_ICE, 0.0D, 0.2D), PACKED_ICE), 
/* 171 */                 SurfaceRules.ifTrue(SurfaceRules.noiseCondition2d(Noises.ICE, 0.0D, 0.025D), ICE), 
/* 172 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK) })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.SNOWY_SLOPES }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(steep, STONE), powderSnowSurfaceRule, 
/*     */                 
/* 177 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK) })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.JAGGED_PEAKS }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(steep, STONE), 
/* 181 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK) })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.GROVE }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { powderSnowSurfaceRule, 
/*     */                 
/* 185 */                 SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK) })), commonSurfaceAndUnderRules, SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.WINDSWEPT_SAVANNA }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), STONE), 
/* 190 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-0.5D), COARSE_DIRT) })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.WINDSWEPT_GRAVELLY_HILLS }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(surfaceNoiseAbove(2.0D), gravelOrStoneIfCeiling), 
/* 194 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(1.0D), STONE), 
/* 195 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-1.0D), grassOrDirtIfUnderwater), gravelOrStoneIfCeiling })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(surfaceNoiseAbove(1.75D), COARSE_DIRT), 
/* 200 */                 SurfaceRules.ifTrue(surfaceNoiseAbove(-0.95D), PODZOL) })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.ICE_SPIKES }), SurfaceRules.ifTrue(aboveWater, SNOW_BLOCK)), 
/* 203 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.MANGROVE_SWAMP }), MUD), 
/* 204 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.MUSHROOM_FIELDS }), MYCELIUM), grassOrDirtIfUnderwater });
/* 208 */     SurfaceRules.ConditionSource clayBand1 = SurfaceRules.noiseCondition2d(Noises.SURFACE, -0.909D, -0.5454D);
/* 209 */     SurfaceRules.ConditionSource clayBand2 = SurfaceRules.noiseCondition2d(Noises.SURFACE, -0.1818D, 0.1818D);
/* 210 */     SurfaceRules.ConditionSource clayBand3 = SurfaceRules.noiseCondition2d(Noises.SURFACE, 0.5454D, 0.909D);
/* 212 */     SurfaceRules.RuleSource mainRuleCloseToSurface = SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.WOODED_BADLANDS }), SurfaceRules.ifTrue(woodedBadlandsTop, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(clayBand1, COARSE_DIRT), 
/* 216 */                         SurfaceRules.ifTrue(clayBand2, COARSE_DIRT), 
/* 217 */                         SurfaceRules.ifTrue(clayBand3, COARSE_DIRT), grassOrDirtIfUnderwater }))), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.SWAMP }), SurfaceRules.ifTrue(swampPuddleLevel, SurfaceRules.ifTrue(SurfaceRules.not(aboveOverworldSeaLevel), SurfaceRules.ifTrue(SurfaceRules.noiseCondition2d(Noises.SWAMP, 0.0D), WATER)))), 
/* 221 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.MANGROVE_SWAMP }), SurfaceRules.ifTrue(mangroveSwampPuddleLevel, SurfaceRules.ifTrue(SurfaceRules.not(aboveOverworldSeaLevel), SurfaceRules.ifTrue(SurfaceRules.noiseCondition2d(Noises.SWAMP, 0.0D), WATER)))) })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(badlandsTop, ORANGE_TERRACOTTA), 
/* 226 */                       SurfaceRules.ifTrue(badlandsMid, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(clayBand1, TERRACOTTA), 
/* 228 */                             SurfaceRules.ifTrue(clayBand2, TERRACOTTA), 
/* 229 */                             SurfaceRules.ifTrue(clayBand3, TERRACOTTA), 
/* 230 */                             SurfaceRules.bandlands() })), SurfaceRules.ifTrue(notUnderwater, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, RED_SANDSTONE), RED_SAND })), SurfaceRules.ifTrue(SurfaceRules.not(hole), ORANGE_TERRACOTTA), 
/* 237 */                       SurfaceRules.ifTrue(notUnderDeepWater, WHITE_TERRACOTTA), gravelOrStoneIfCeiling })), SurfaceRules.ifTrue(badlandsHeightCondition, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(aboveOverworldSeaLevel, SurfaceRules.ifTrue(SurfaceRules.not(badlandsMid), ORANGE_TERRACOTTA)), 
/* 242 */                       SurfaceRules.bandlands() })), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(notUnderDeepWater, WHITE_TERRACOTTA)) })), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(notUnderwater, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(frozenOcean, SurfaceRules.ifTrue(hole, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(aboveWater, AIR), 
/* 249 */                           SurfaceRules.ifTrue(SurfaceRules.temperature(), ICE), WATER }))), biomeSurfaceRule }))), SurfaceRules.ifTrue(notUnderDeepWater, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(frozenOcean, SurfaceRules.ifTrue(hole, WATER))), 
/* 256 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, biomeUnderSurfaceRule), 
/* 257 */                 SurfaceRules.ifTrue(biomesWithSandAndSandstone, SurfaceRules.ifTrue(SurfaceRules.DEEP_UNDER_FLOOR, SANDSTONE)), 
/* 258 */                 SurfaceRules.ifTrue(biomesWithSandAndVeryDeepSandstone, SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, SANDSTONE)) })), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS }), STONE), 
/* 262 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN }), sandOrSandstoneIfCeiling), gravelOrStoneIfCeiling })) });
/* 267 */     ImmutableList.Builder<SurfaceRules.RuleSource> builder = ImmutableList.builder();
/* 269 */     if (bedrockRoof)
/* 270 */       builder.add(SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK)); 
/* 272 */     if (bedrockFloor)
/* 273 */       builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK)); 
/* 275 */     SurfaceRules.RuleSource ruleAbovePreliminarySurface = SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), mainRuleCloseToSurface);
/* 276 */     builder.add(doPreliminarySurfaceCheck ? ruleAbovePreliminarySurface : mainRuleCloseToSurface);
/* 277 */     builder.add(SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.SULFUR_CAVES }), sulfurCaveBands));
/* 278 */     builder.add(SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), DEEPSLATE));
/* 280 */     return SurfaceRules.sequence((SurfaceRules.RuleSource[])builder.build().toArray(x$0 -> new SurfaceRules.RuleSource[x$0]));
/*     */   }
/*     */   
/*     */   public static SurfaceRules.RuleSource nether(HolderGetter<Biome> biomes) {
/* 284 */     SurfaceRules.ConditionSource aboveNetherLavaLevel = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(31), 0);
/* 285 */     SurfaceRules.ConditionSource aboveNetherLavaSurface = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(32), 0);
/* 287 */     SurfaceRules.ConditionSource netherBandAroundLavaLevelBottom = SurfaceRules.yStartCheck(VerticalAnchor.absolute(30), 0);
/* 288 */     SurfaceRules.ConditionSource netherBandAroundLavaLevelTop = SurfaceRules.not(SurfaceRules.yStartCheck(VerticalAnchor.absolute(35), 0));
/* 290 */     SurfaceRules.ConditionSource closeToCeiling = SurfaceRules.yBlockCheck(VerticalAnchor.belowTop(5), 0);
/* 292 */     SurfaceRules.ConditionSource hole = SurfaceRules.hole();
/* 294 */     SurfaceRules.ConditionSource soulSandLayer = SurfaceRules.noiseCondition2d(Noises.SOUL_SAND_LAYER, -0.012D);
/* 295 */     SurfaceRules.ConditionSource gravelLayer = SurfaceRules.noiseCondition2d(Noises.GRAVEL_LAYER, -0.012D);
/* 296 */     SurfaceRules.ConditionSource patch = SurfaceRules.noiseCondition2d(Noises.PATCH, -0.012D);
/* 297 */     SurfaceRules.ConditionSource netherrack = SurfaceRules.noiseCondition2d(Noises.NETHERRACK, 0.54D);
/* 298 */     SurfaceRules.ConditionSource netherWart = SurfaceRules.noiseCondition2d(Noises.NETHER_WART, 1.17D);
/* 299 */     SurfaceRules.ConditionSource netherStateSelector = SurfaceRules.noiseCondition2d(Noises.NETHER_STATE_SELECTOR, 0.0D);
/* 301 */     SurfaceRules.RuleSource gravelPatch = SurfaceRules.ifTrue(patch, SurfaceRules.ifTrue(netherBandAroundLavaLevelBottom, SurfaceRules.ifTrue(netherBandAroundLavaLevelTop, GRAVEL)));
/* 303 */     return SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5)), BEDROCK), 
/* 305 */           SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top())), BEDROCK), 
/* 306 */           SurfaceRules.ifTrue(closeToCeiling, NETHERRACK), 
/* 307 */           SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.BASALT_DELTAS }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, BASALT), 
/* 309 */                 SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { gravelPatch, SurfaceRules.ifTrue(netherStateSelector, BASALT), BLACKSTONE })) })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.SOUL_SAND_VALLEY }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.UNDER_CEILING, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(netherStateSelector, SOUL_SAND), SOUL_SOIL })), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { gravelPatch, SurfaceRules.ifTrue(netherStateSelector, SOUL_SAND), SOUL_SOIL })) })), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.not(aboveNetherLavaSurface), SurfaceRules.ifTrue(hole, LAVA)), 
/* 328 */                 SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.WARPED_FOREST }), SurfaceRules.ifTrue(SurfaceRules.not(netherrack), SurfaceRules.ifTrue(aboveNetherLavaLevel, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(netherWart, WARPED_WART_BLOCK), WARPED_NYLIUM })))), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.CRIMSON_FOREST }), SurfaceRules.ifTrue(SurfaceRules.not(netherrack), SurfaceRules.ifTrue(aboveNetherLavaLevel, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(netherWart, NETHER_WART_BLOCK), CRIMSON_NYLIUM })))) })), SurfaceRules.ifTrue(SurfaceRules.isBiome(biomes, new ResourceKey[] { Biomes.NETHER_WASTES }), SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.ifTrue(soulSandLayer, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(SurfaceRules.not(hole), SurfaceRules.ifTrue(netherBandAroundLavaLevelBottom, SurfaceRules.ifTrue(netherBandAroundLavaLevelTop, SOUL_SAND))), NETHERRACK }))), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.ifTrue(aboveNetherLavaLevel, SurfaceRules.ifTrue(netherBandAroundLavaLevelTop, SurfaceRules.ifTrue(gravelLayer, SurfaceRules.sequence(new SurfaceRules.RuleSource[] { SurfaceRules.ifTrue(aboveNetherLavaSurface, GRAVEL), 
/* 344 */                             SurfaceRules.ifTrue(SurfaceRules.not(hole), GRAVEL) }))))) })), NETHERRACK });
/*     */   }
/*     */   
/*     */   public static SurfaceRules.RuleSource end() {
/* 352 */     return ENDSTONE;
/*     */   }
/*     */   
/*     */   public static SurfaceRules.RuleSource air() {
/* 356 */     return AIR;
/*     */   }
/*     */   
/*     */   private static SurfaceRules.ConditionSource surfaceNoiseAbove(double threshold) {
/* 360 */     return SurfaceRules.noiseCondition2d(Noises.SURFACE, threshold / 8.25D, Double.MAX_VALUE);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\SurfaceRuleData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */