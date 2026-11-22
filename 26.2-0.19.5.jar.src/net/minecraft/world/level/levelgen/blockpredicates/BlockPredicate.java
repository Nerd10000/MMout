/*     */ package net.minecraft.world.level.levelgen.blockpredicates;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.BiPredicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ public interface BlockPredicate extends BiPredicate<WorldGenLevel, BlockPos> {
/*  25 */   public static final Codec<BlockPredicate> CODEC = BuiltInRegistries.BLOCK_PREDICATE_TYPE.byNameCodec().dispatch(BlockPredicate::type, BlockPredicateType::codec);
/*     */   
/*  28 */   public static final BlockPredicate ONLY_IN_AIR_PREDICATE = matchesTag(BlockTags.AIR);
/*     */   
/*  29 */   public static final BlockPredicate ONLY_IN_AIR_OR_WATER_PREDICATE = anyOf(ONLY_IN_AIR_PREDICATE, matchesBlocks(new Block[] { Blocks.WATER }));
/*     */   
/*     */   BlockPredicateType<?> type();
/*     */   
/*     */   static BlockPredicate allOf(List<BlockPredicate> predicates) {
/*  34 */     return new AllOfPredicate(predicates);
/*     */   }
/*     */   
/*     */   static BlockPredicate allOf(BlockPredicate... predicates) {
/*  38 */     return allOf(List.of(predicates));
/*     */   }
/*     */   
/*     */   static BlockPredicate allOf(BlockPredicate a, BlockPredicate b) {
/*  42 */     return allOf(List.of(a, b));
/*     */   }
/*     */   
/*     */   static BlockPredicate anyOf(List<BlockPredicate> predicates) {
/*  46 */     return new AnyOfPredicate(predicates);
/*     */   }
/*     */   
/*     */   static BlockPredicate anyOf(BlockPredicate... predicates) {
/*  50 */     return anyOf(List.of(predicates));
/*     */   }
/*     */   
/*     */   static BlockPredicate anyOf(BlockPredicate a, BlockPredicate b) {
/*  54 */     return anyOf(List.of(a, b));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(Vec3i offset, List<Block> blocks) {
/*  58 */     return new MatchingBlocksPredicate(offset, (HolderSet<Block>)HolderSet.direct(Block::builtInRegistryHolder, blocks));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(List<Block> blocks) {
/*  62 */     return matchesBlocks(Vec3i.ZERO, blocks);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(Vec3i offset, Block... blocks) {
/*  66 */     return matchesBlocks(offset, List.of(blocks));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBlocks(Block... blocks) {
/*  70 */     return matchesBlocks(Vec3i.ZERO, blocks);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesTag(Vec3i offset, TagKey<Block> tag) {
/*  74 */     return new MatchingBlockTagPredicate(offset, tag);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesTag(TagKey<Block> tag) {
/*  78 */     return matchesTag(Vec3i.ZERO, tag);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesFluids(Vec3i offset, List<Fluid> fluids) {
/*  82 */     return new MatchingFluidsPredicate(offset, (HolderSet<Fluid>)HolderSet.direct(Fluid::builtInRegistryHolder, fluids));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesFluids(Vec3i offset, Fluid... fluids) {
/*  86 */     return matchesFluids(offset, List.of(fluids));
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesFluids(Fluid... fluids) {
/*  90 */     return matchesFluids(Vec3i.ZERO, fluids);
/*     */   }
/*     */   
/*     */   static BlockPredicate matchesBiomes(HolderSet<Biome> biomes) {
/*  94 */     return new MatchingBiomesPredicate(biomes);
/*     */   }
/*     */   
/*     */   static BlockPredicate not(BlockPredicate predicate) {
/*  98 */     return new NotPredicate(predicate);
/*     */   }
/*     */   
/*     */   static BlockPredicate replaceable(Vec3i offset) {
/* 102 */     return new ReplaceablePredicate(offset);
/*     */   }
/*     */   
/*     */   static BlockPredicate replaceable() {
/* 106 */     return replaceable(Vec3i.ZERO);
/*     */   }
/*     */   
/*     */   static BlockPredicate wouldSurvive(BlockState state, Vec3i offset) {
/* 110 */     return new WouldSurvivePredicate(offset, state);
/*     */   }
/*     */   
/*     */   static BlockPredicate hasSturdyFace(Vec3i offset, Direction direction) {
/* 114 */     return new HasSturdyFacePredicate(offset, direction);
/*     */   }
/*     */   
/*     */   static BlockPredicate hasSturdyFace(Direction direction) {
/* 118 */     return hasSturdyFace(Vec3i.ZERO, direction);
/*     */   }
/*     */   
/*     */   static BlockPredicate solid(Vec3i offset) {
/* 122 */     return new SolidPredicate(offset);
/*     */   }
/*     */   
/*     */   static BlockPredicate solid() {
/* 126 */     return solid(Vec3i.ZERO);
/*     */   }
/*     */   
/*     */   static BlockPredicate noFluid() {
/* 130 */     return noFluid(Vec3i.ZERO);
/*     */   }
/*     */   
/*     */   static BlockPredicate noFluid(Vec3i offset) {
/* 134 */     return matchesFluids(offset, new Fluid[] { Fluids.EMPTY });
/*     */   }
/*     */   
/*     */   static BlockPredicate insideWorld(Vec3i offset) {
/* 138 */     return new InsideWorldBoundsPredicate(offset);
/*     */   }
/*     */   
/*     */   static BlockPredicate alwaysTrue() {
/* 142 */     return TrueBlockPredicate.INSTANCE;
/*     */   }
/*     */   
/*     */   static BlockPredicate unobstructed(Vec3i offset) {
/* 146 */     return new UnobstructedPredicate(offset);
/*     */   }
/*     */   
/*     */   static BlockPredicate unobstructed() {
/* 150 */     return unobstructed(Vec3i.ZERO);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\BlockPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */