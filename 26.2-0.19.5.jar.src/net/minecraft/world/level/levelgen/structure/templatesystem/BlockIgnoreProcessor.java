/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockIgnoreProcessor implements StructureProcessor {
/* 19 */   private static final Codec<Block> WEIRD_BLOCK_STATE_CODEC = BlockState.CODEC.xmap(BlockBehaviour.BlockStateBase::getBlock, Block::defaultBlockState);
/*    */   
/*    */   public static final MapCodec<BlockIgnoreProcessor> MAP_CODEC;
/*    */   
/*    */   static {
/* 21 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WEIRD_BLOCK_STATE_CODEC.listOf().fieldOf("blocks").forGetter(())).apply((Applicative)i, BlockIgnoreProcessor::new));
/*    */   }
/*    */   
/* 25 */   public static final BlockIgnoreProcessor STRUCTURE_BLOCK = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.STRUCTURE_BLOCK));
/*    */   
/* 26 */   public static final BlockIgnoreProcessor AIR = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.AIR));
/*    */   
/* 27 */   public static final BlockIgnoreProcessor STRUCTURE_AND_AIR = new BlockIgnoreProcessor((List<Block>)ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK));
/*    */   
/*    */   private final ImmutableList<Block> toIgnore;
/*    */   
/*    */   public BlockIgnoreProcessor(List<Block> toIgnore) {
/* 32 */     this.toIgnore = ImmutableList.copyOf(toIgnore);
/*    */   }
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 37 */     if (this.toIgnore.contains(processedBlockInfo.state().getBlock()))
/* 38 */       return null; 
/* 40 */     return processedBlockInfo;
/*    */   }
/*    */   
/*    */   public MapCodec<BlockIgnoreProcessor> codec() {
/* 45 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlockIgnoreProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */