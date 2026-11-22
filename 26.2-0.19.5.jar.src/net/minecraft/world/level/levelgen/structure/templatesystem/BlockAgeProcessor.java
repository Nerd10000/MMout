/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Half;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class BlockAgeProcessor implements StructureProcessor {
/*     */   public static final MapCodec<BlockAgeProcessor> MAP_CODEC;
/*     */   
/*     */   private static final float PROBABILITY_OF_REPLACING_FULL_BLOCK = 0.5F;
/*     */   
/*     */   private static final float PROBABILITY_OF_REPLACING_STAIRS = 0.5F;
/*     */   
/*     */   private static final float PROBABILITY_OF_REPLACING_OBSIDIAN = 0.15F;
/*     */   
/*     */   static {
/*  20 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("mossiness").forGetter(())).apply((Applicative)i, BlockAgeProcessor::new));
/*     */   }
/*     */   
/*  28 */   private static final BlockState[] NON_MOSSY_REPLACEMENTS = new BlockState[] { Blocks.STONE_SLAB
/*  29 */       .defaultBlockState(), Blocks.STONE_BRICK_SLAB
/*  30 */       .defaultBlockState() };
/*     */   
/*     */   private final float mossiness;
/*     */   
/*     */   public BlockAgeProcessor(float mossiness) {
/*  36 */     this.mossiness = mossiness;
/*     */   }
/*     */   
/*     */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/*  41 */     RandomSource random = settings.getRandom(processedBlockInfo.pos());
/*  43 */     BlockState state = processedBlockInfo.state();
/*  44 */     BlockPos pos = processedBlockInfo.pos();
/*  45 */     BlockState newState = null;
/*  46 */     if (state.is(Blocks.STONE_BRICKS) || state.is(Blocks.STONE) || state.is(Blocks.CHISELED_STONE_BRICKS)) {
/*  47 */       newState = maybeReplaceFullStoneBlock(random);
/*  48 */     } else if (state.is(BlockTags.STAIRS)) {
/*  49 */       newState = maybeReplaceStairs(state, random);
/*  50 */     } else if (state.is(BlockTags.SLABS)) {
/*  51 */       newState = maybeReplaceSlab(state, random);
/*  52 */     } else if (state.is(BlockTags.WALLS)) {
/*  53 */       newState = maybeReplaceWall(state, random);
/*  54 */     } else if (state.is(Blocks.OBSIDIAN)) {
/*  55 */       newState = maybeReplaceObsidian(random);
/*     */     } 
/*  57 */     if (newState != null)
/*  58 */       return new StructureTemplate.StructureBlockInfo(pos, newState, processedBlockInfo.nbt()); 
/*  60 */     return processedBlockInfo;
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceFullStoneBlock(RandomSource random) {
/*  64 */     if (random.nextFloat() >= 0.5F)
/*  65 */       return null; 
/*  69 */     BlockState[] nonMossyReplacements = { Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(random, Blocks.STONE_BRICK_STAIRS) };
/*  73 */     BlockState[] mossyReplacements = { Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(random, Blocks.MOSSY_STONE_BRICK_STAIRS) };
/*  76 */     return getRandomBlock(random, nonMossyReplacements, mossyReplacements);
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceStairs(BlockState blockState, RandomSource random) {
/*  80 */     if (random.nextFloat() >= 0.5F)
/*  81 */       return null; 
/*  86 */     BlockState[] mossyReplacements = { Blocks.MOSSY_STONE_BRICK_STAIRS.withPropertiesOf(blockState), Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState() };
/*  89 */     return getRandomBlock(random, NON_MOSSY_REPLACEMENTS, mossyReplacements);
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceSlab(BlockState blockState, RandomSource random) {
/*  93 */     if (random.nextFloat() < this.mossiness)
/*  94 */       return Blocks.MOSSY_STONE_BRICK_SLAB.withPropertiesOf(blockState); 
/*  96 */     return null;
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceWall(BlockState blockState, RandomSource random) {
/* 100 */     if (random.nextFloat() < this.mossiness)
/* 101 */       return Blocks.MOSSY_STONE_BRICK_WALL.withPropertiesOf(blockState); 
/* 103 */     return null;
/*     */   }
/*     */   
/*     */   private BlockState maybeReplaceObsidian(RandomSource random) {
/* 107 */     if (random.nextFloat() < 0.15F)
/* 108 */       return Blocks.CRYING_OBSIDIAN.defaultBlockState(); 
/* 110 */     return null;
/*     */   }
/*     */   
/*     */   private static BlockState getRandomFacingStairs(RandomSource random, Block stairBlock) {
/* 114 */     return (BlockState)((BlockState)stairBlock.defaultBlockState()
/* 115 */       .setValue((Property)StairBlock.FACING, (Comparable)Direction.Plane.HORIZONTAL.getRandomDirection(random)))
/* 116 */       .setValue((Property)StairBlock.HALF, (Comparable)Util.getRandom((Object[])Half.values(), random));
/*     */   }
/*     */   
/*     */   private BlockState getRandomBlock(RandomSource random, BlockState[] nonMossyBlocks, BlockState[] mossyBlocks) {
/* 120 */     if (random.nextFloat() < this.mossiness)
/* 121 */       return getRandomBlock(random, mossyBlocks); 
/* 123 */     return getRandomBlock(random, nonMossyBlocks);
/*     */   }
/*     */   
/*     */   private static BlockState getRandomBlock(RandomSource random, BlockState[] blocks) {
/* 128 */     return blocks[random.nextInt(blocks.length)];
/*     */   }
/*     */   
/*     */   public MapCodec<BlockAgeProcessor> codec() {
/* 133 */     return MAP_CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockAgeProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */