/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public class LavaSubmergedBlockProcessor implements StructureProcessor {
/* 13 */   public static final MapCodec<LavaSubmergedBlockProcessor> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 14 */   public static final LavaSubmergedBlockProcessor INSTANCE = new LavaSubmergedBlockProcessor();
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 18 */     BlockPos pos = processedBlockInfo.pos();
/* 19 */     boolean wasLavaBefore = level.getBlockState(pos).is(Blocks.LAVA);
/* 20 */     if (wasLavaBefore && !Block.isShapeFullBlock(processedBlockInfo.state().getShape((BlockGetter)level, pos)))
/* 21 */       return new StructureTemplate.StructureBlockInfo(pos, Blocks.LAVA.defaultBlockState(), processedBlockInfo.nbt()); 
/* 23 */     return processedBlockInfo;
/*    */   }
/*    */   
/*    */   public MapCodec<LavaSubmergedBlockProcessor> codec() {
/* 28 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\LavaSubmergedBlockProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */