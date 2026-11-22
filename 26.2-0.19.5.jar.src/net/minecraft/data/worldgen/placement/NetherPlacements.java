/*     */ package net.minecraft.data.worldgen.placement;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.data.worldgen.features.NetherFeatures;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.valueproviders.BiasedToBottomInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
/*     */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.CountPlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacementModifier;
/*     */ import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
/*     */ 
/*     */ public class NetherPlacements {
/*  28 */   public static final ResourceKey<PlacedFeature> DELTA = PlacementUtils.createKey("delta");
/*     */   
/*  29 */   public static final ResourceKey<PlacedFeature> SMALL_BASALT_COLUMNS = PlacementUtils.createKey("small_basalt_columns");
/*     */   
/*  30 */   public static final ResourceKey<PlacedFeature> LARGE_BASALT_COLUMNS = PlacementUtils.createKey("large_basalt_columns");
/*     */   
/*  31 */   public static final ResourceKey<PlacedFeature> BASALT_BLOBS = PlacementUtils.createKey("basalt_blobs");
/*     */   
/*  32 */   public static final ResourceKey<PlacedFeature> BLACKSTONE_BLOBS = PlacementUtils.createKey("blackstone_blobs");
/*     */   
/*  34 */   public static final ResourceKey<PlacedFeature> GLOWSTONE_EXTRA = PlacementUtils.createKey("glowstone_extra");
/*     */   
/*  35 */   public static final ResourceKey<PlacedFeature> GLOWSTONE = PlacementUtils.createKey("glowstone");
/*     */   
/*  37 */   public static final ResourceKey<PlacedFeature> CRIMSON_FOREST_VEGETATION = PlacementUtils.createKey("crimson_forest_vegetation");
/*     */   
/*  38 */   public static final ResourceKey<PlacedFeature> WARPED_FOREST_VEGETATION = PlacementUtils.createKey("warped_forest_vegetation");
/*     */   
/*  39 */   public static final ResourceKey<PlacedFeature> NETHER_SPROUTS = PlacementUtils.createKey("nether_sprouts");
/*     */   
/*  40 */   public static final ResourceKey<PlacedFeature> TWISTING_VINES = PlacementUtils.createKey("twisting_vines");
/*     */   
/*  41 */   public static final ResourceKey<PlacedFeature> WEEPING_VINES = PlacementUtils.createKey("weeping_vines");
/*     */   
/*  42 */   public static final ResourceKey<PlacedFeature> PATCH_CRIMSON_ROOTS = PlacementUtils.createKey("patch_crimson_roots");
/*     */   
/*  44 */   public static final ResourceKey<PlacedFeature> BASALT_PILLAR = PlacementUtils.createKey("basalt_pillar");
/*     */   
/*  46 */   public static final ResourceKey<PlacedFeature> SPRING_DELTA = PlacementUtils.createKey("spring_delta");
/*     */   
/*  47 */   public static final ResourceKey<PlacedFeature> SPRING_CLOSED = PlacementUtils.createKey("spring_closed");
/*     */   
/*  48 */   public static final ResourceKey<PlacedFeature> SPRING_CLOSED_DOUBLE = PlacementUtils.createKey("spring_closed_double");
/*     */   
/*  49 */   public static final ResourceKey<PlacedFeature> SPRING_OPEN = PlacementUtils.createKey("spring_open");
/*     */   
/*  51 */   public static final ResourceKey<PlacedFeature> PATCH_SOUL_FIRE = PlacementUtils.createKey("patch_soul_fire");
/*     */   
/*  52 */   public static final ResourceKey<PlacedFeature> PATCH_FIRE = PlacementUtils.createKey("patch_fire");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<PlacedFeature> context) {
/*  55 */     HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
/*  56 */     Holder.Reference reference1 = configuredFeatures.getOrThrow(NetherFeatures.DELTA);
/*  57 */     Holder.Reference reference2 = configuredFeatures.getOrThrow(NetherFeatures.SMALL_BASALT_COLUMNS);
/*  58 */     Holder.Reference reference3 = configuredFeatures.getOrThrow(NetherFeatures.LARGE_BASALT_COLUMNS);
/*  59 */     Holder.Reference reference4 = configuredFeatures.getOrThrow(NetherFeatures.BASALT_BLOBS);
/*  60 */     Holder.Reference reference5 = configuredFeatures.getOrThrow(NetherFeatures.BLACKSTONE_BLOBS);
/*  61 */     Holder.Reference reference6 = configuredFeatures.getOrThrow(NetherFeatures.GLOWSTONE_EXTRA);
/*  62 */     Holder.Reference reference7 = configuredFeatures.getOrThrow(NetherFeatures.CRIMSON_FOREST_VEGETATION);
/*  63 */     Holder.Reference reference8 = configuredFeatures.getOrThrow(NetherFeatures.WARPED_FOREST_VEGETION);
/*  64 */     Holder.Reference reference9 = configuredFeatures.getOrThrow(NetherFeatures.NETHER_SPROUTS);
/*  65 */     Holder.Reference reference10 = configuredFeatures.getOrThrow(NetherFeatures.TWISTING_VINES);
/*  66 */     Holder.Reference reference11 = configuredFeatures.getOrThrow(NetherFeatures.WEEPING_VINES);
/*  67 */     Holder.Reference reference12 = configuredFeatures.getOrThrow(NetherFeatures.CRIMSON_ROOTS);
/*  68 */     Holder.Reference reference13 = configuredFeatures.getOrThrow(NetherFeatures.BASALT_PILLAR);
/*  69 */     Holder.Reference reference14 = configuredFeatures.getOrThrow(NetherFeatures.SPRING_LAVA_NETHER);
/*  70 */     Holder.Reference reference15 = configuredFeatures.getOrThrow(NetherFeatures.SPRING_NETHER_CLOSED);
/*  71 */     Holder.Reference reference16 = configuredFeatures.getOrThrow(NetherFeatures.SPRING_NETHER_OPEN);
/*  72 */     Holder.Reference reference17 = configuredFeatures.getOrThrow(NetherFeatures.SOUL_FIRE);
/*  73 */     Holder.Reference reference18 = configuredFeatures.getOrThrow(NetherFeatures.FIRE);
/*  75 */     PlacementUtils.register(context, DELTA, (Holder<ConfiguredFeature<?, ?>>)reference1, new PlacementModifier[] { (PlacementModifier)CountOnEveryLayerPlacement.of(40), 
/*  77 */           (PlacementModifier)BiomeFilter.biome() });
/*  79 */     PlacementUtils.register(context, SMALL_BASALT_COLUMNS, (Holder<ConfiguredFeature<?, ?>>)reference2, new PlacementModifier[] { (PlacementModifier)CountOnEveryLayerPlacement.of(4), 
/*  81 */           (PlacementModifier)BiomeFilter.biome() });
/*  83 */     PlacementUtils.register(context, LARGE_BASALT_COLUMNS, (Holder<ConfiguredFeature<?, ?>>)reference3, new PlacementModifier[] { (PlacementModifier)CountOnEveryLayerPlacement.of(2), 
/*  85 */           (PlacementModifier)BiomeFilter.biome() });
/*  87 */     PlacementUtils.register(context, BASALT_BLOBS, (Holder<ConfiguredFeature<?, ?>>)reference4, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(75), 
/*  89 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/*  91 */           (PlacementModifier)BiomeFilter.biome() });
/*  93 */     PlacementUtils.register(context, BLACKSTONE_BLOBS, (Holder<ConfiguredFeature<?, ?>>)reference5, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(25), 
/*  95 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/*  97 */           (PlacementModifier)BiomeFilter.biome() });
/* 100 */     PlacementUtils.register(context, GLOWSTONE_EXTRA, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] { (PlacementModifier)CountPlacement.of((IntProvider)BiasedToBottomInt.of(0, 9)), 
/* 102 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */           
/* 104 */           (PlacementModifier)BiomeFilter.biome() });
/* 106 */     PlacementUtils.register(context, GLOWSTONE, (Holder<ConfiguredFeature<?, ?>>)reference6, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(10), 
/* 108 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 110 */           (PlacementModifier)BiomeFilter.biome() });
/* 113 */     PlacementUtils.register(context, CRIMSON_FOREST_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference7, new PlacementModifier[] { (PlacementModifier)CountOnEveryLayerPlacement.of(6), 
/* 115 */           (PlacementModifier)BiomeFilter.biome() });
/* 117 */     PlacementUtils.register(context, WARPED_FOREST_VEGETATION, (Holder<ConfiguredFeature<?, ?>>)reference8, new PlacementModifier[] { (PlacementModifier)CountOnEveryLayerPlacement.of(5), 
/* 119 */           (PlacementModifier)BiomeFilter.biome() });
/* 121 */     PlacementUtils.register(context, NETHER_SPROUTS, (Holder<ConfiguredFeature<?, ?>>)reference9, new PlacementModifier[] { (PlacementModifier)CountOnEveryLayerPlacement.of(4), 
/* 123 */           (PlacementModifier)BiomeFilter.biome() });
/* 125 */     PlacementUtils.register(context, TWISTING_VINES, (Holder<ConfiguredFeature<?, ?>>)reference10, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(10), 
/* 127 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 129 */           (PlacementModifier)BiomeFilter.biome() });
/* 131 */     PlacementUtils.register(context, WEEPING_VINES, (Holder<ConfiguredFeature<?, ?>>)reference11, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(10), 
/* 133 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 135 */           (PlacementModifier)BiomeFilter.biome() });
/* 137 */     PlacementUtils.register(context, PATCH_CRIMSON_ROOTS, (Holder<ConfiguredFeature<?, ?>>)reference12, new PlacementModifier[] { PlacementUtils.FULL_RANGE, 
/*     */           
/* 139 */           (PlacementModifier)BiomeFilter.biome(), 
/* 140 */           (PlacementModifier)CountPlacement.of(96), 
/* 141 */           (PlacementModifier)RandomOffsetPlacement.ofTriangle(7, 3), 
/* 142 */           (PlacementModifier)BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE) });
/* 145 */     PlacementUtils.register(context, BASALT_PILLAR, (Holder<ConfiguredFeature<?, ?>>)reference13, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(10), 
/* 147 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, 
/*     */           
/* 149 */           (PlacementModifier)BiomeFilter.biome() });
/* 152 */     PlacementUtils.register(context, SPRING_DELTA, (Holder<ConfiguredFeature<?, ?>>)reference14, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(16), 
/* 154 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */           
/* 156 */           (PlacementModifier)BiomeFilter.biome() });
/* 158 */     PlacementUtils.register(context, SPRING_CLOSED, (Holder<ConfiguredFeature<?, ?>>)reference15, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(16), 
/* 160 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_10_10, 
/*     */           
/* 162 */           (PlacementModifier)BiomeFilter.biome() });
/* 164 */     PlacementUtils.register(context, SPRING_CLOSED_DOUBLE, (Holder<ConfiguredFeature<?, ?>>)reference15, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(32), 
/* 166 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_10_10, 
/*     */           
/* 168 */           (PlacementModifier)BiomeFilter.biome() });
/* 170 */     PlacementUtils.register(context, SPRING_OPEN, (Holder<ConfiguredFeature<?, ?>>)reference16, new PlacementModifier[] { (PlacementModifier)CountPlacement.of(8), 
/* 172 */           (PlacementModifier)InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */           
/* 174 */           (PlacementModifier)BiomeFilter.biome() });
/* 178 */     PlacementUtils.register(context, PATCH_SOUL_FIRE, (Holder<ConfiguredFeature<?, ?>>)reference17, 
/* 179 */         firePlacement(Blocks.SOUL_SOIL));
/* 181 */     PlacementUtils.register(context, PATCH_FIRE, (Holder<ConfiguredFeature<?, ?>>)reference18, 
/* 182 */         firePlacement(Blocks.NETHERRACK));
/*     */   }
/*     */   
/*     */   private static List<PlacementModifier> firePlacement(Block onlyOnBlock) {
/* 187 */     return (List)List.of(
/* 188 */         CountPlacement.of((IntProvider)UniformInt.of(0, 5)), 
/* 189 */         InSquarePlacement.spread(), PlacementUtils.RANGE_4_4, 
/*     */         
/* 191 */         BiomeFilter.biome(), 
/* 192 */         CountPlacement.of(96), 
/* 193 */         RandomOffsetPlacement.ofTriangle(7, 3), 
/* 194 */         BlockPredicateFilter.forPredicate(BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, 
/*     */             
/* 196 */             BlockPredicate.matchesBlocks(Direction.DOWN.getUnitVec3i(), new Block[] { onlyOnBlock }))));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\placement\NetherPlacements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */