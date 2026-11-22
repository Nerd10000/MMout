/*    */ package net.minecraft.data.worldgen.placement;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.data.worldgen.BootstrapContext;
/*    */ import net.minecraft.data.worldgen.features.PileFeatures;
/*    */ import net.minecraft.data.worldgen.features.TreeFeatures;
/*    */ import net.minecraft.data.worldgen.features.VegetationFeatures;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*    */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*    */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*    */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*    */ 
/*    */ public class VillagePlacements {
/* 23 */   public static final ResourceKey<PlacedFeature> PILE_HAY_VILLAGE = PlacementUtils.createKey("pile_hay");
/*    */   
/* 24 */   public static final ResourceKey<PlacedFeature> PILE_MELON_VILLAGE = PlacementUtils.createKey("pile_melon");
/*    */   
/* 25 */   public static final ResourceKey<PlacedFeature> PILE_SNOW_VILLAGE = PlacementUtils.createKey("pile_snow");
/*    */   
/* 26 */   public static final ResourceKey<PlacedFeature> PILE_ICE_VILLAGE = PlacementUtils.createKey("pile_ice");
/*    */   
/* 27 */   public static final ResourceKey<PlacedFeature> PILE_PUMPKIN_VILLAGE = PlacementUtils.createKey("pile_pumpkin");
/*    */   
/* 29 */   public static final ResourceKey<PlacedFeature> OAK_VILLAGE = PlacementUtils.createKey("oak");
/*    */   
/* 30 */   public static final ResourceKey<PlacedFeature> ACACIA_VILLAGE = PlacementUtils.createKey("acacia");
/*    */   
/* 31 */   public static final ResourceKey<PlacedFeature> SPRUCE_VILLAGE = PlacementUtils.createKey("spruce");
/*    */   
/* 32 */   public static final ResourceKey<PlacedFeature> PINE_VILLAGE = PlacementUtils.createKey("pine");
/*    */   
/* 34 */   public static final ResourceKey<PlacedFeature> PATCH_CACTUS_VILLAGE = PlacementUtils.createKey("patch_cactus");
/*    */   
/* 35 */   public static final ResourceKey<PlacedFeature> FLOWER_PLAIN_VILLAGE = PlacementUtils.createKey("flower_plain");
/*    */   
/* 36 */   public static final ResourceKey<PlacedFeature> PATCH_TAIGA_GRASS_VILLAGE = PlacementUtils.createKey("patch_taiga_grass");
/*    */   
/* 37 */   public static final ResourceKey<PlacedFeature> PATCH_BERRY_BUSH_VILLAGE = PlacementUtils.createKey("patch_berry_bush");
/*    */   
/*    */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/* 40 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/* 41 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(PileFeatures.PILE_HAY);
/* 42 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(PileFeatures.PILE_MELON);
/* 43 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(PileFeatures.PILE_SNOW);
/* 44 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(PileFeatures.PILE_ICE);
/* 45 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(PileFeatures.PILE_PUMPKIN);
/* 46 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(TreeFeatures.OAK);
/* 47 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(TreeFeatures.ACACIA);
/* 48 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(TreeFeatures.SPRUCE);
/* 49 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(TreeFeatures.PINE);
/* 50 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(VegetationFeatures.CACTUS);
/* 51 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(VegetationFeatures.FLOWER_PLAIN);
/* 52 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(VegetationFeatures.TAIGA_GRASS);
/* 53 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(VegetationFeatures.BERRY_BUSH);
/* 55 */     PlacementUtils.register(context, PILE_HAY_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[0]);
/* 56 */     PlacementUtils.register(context, PILE_MELON_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference2, new PlacementModifier[0]);
/* 57 */     PlacementUtils.register(context, PILE_SNOW_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference3, new PlacementModifier[0]);
/* 58 */     PlacementUtils.register(context, PILE_ICE_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference4, new PlacementModifier[0]);
/* 59 */     PlacementUtils.register(context, PILE_PUMPKIN_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[0]);
/* 61 */     PlacementUtils.register(context, OAK_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] { (PlacementModifier)PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING) });
/* 62 */     PlacementUtils.register(context, ACACIA_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] { (PlacementModifier)PlacementUtils.filteredByBlockSurvival(Blocks.ACACIA_SAPLING) });
/* 63 */     PlacementUtils.register(context, SPRUCE_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference8, new PlacementModifier[] { (PlacementModifier)PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING) });
/* 64 */     PlacementUtils.register(context, PINE_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference9, new PlacementModifier[] { (PlacementModifier)PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING) });
/* 66 */     PlacementUtils.register(context, PATCH_CACTUS_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference10, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(10), 
/* 68 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 69 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*    */               
/* 71 */               BlockPredicate.wouldSurvive(Blocks.CACTUS.defaultBlockState(), (Vec3i)BlockPos.ZERO))) });
/* 74 */     PlacementUtils.register(context, FLOWER_PLAIN_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference11, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(64), 
/* 76 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(6, 2), 
/* 77 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 79 */     PlacementUtils.register(context, PATCH_TAIGA_GRASS_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(32), 
/* 81 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 82 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 84 */     PlacementUtils.register(context, PATCH_BERRY_BUSH_VILLAGE, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(96), 
/* 86 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 87 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*    */               
/* 89 */               BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { Blocks.GRASS_BLOCK }))) });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\placement\VillagePlacements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */