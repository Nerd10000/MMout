/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ 
/*    */ public interface StructureProcessor {
/*    */   default StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 19 */     return processedBlockInfo;
/*    */   }
/*    */   
/*    */   MapCodec<? extends StructureProcessor> codec();
/*    */   
/*    */   default List<StructureTemplate.StructureBlockInfo> finalizeProcessing(ServerLevelAccessor level, BlockPos position, BlockPos referencePos, List<StructureTemplate.StructureBlockInfo> originalBlockInfoList, List<StructureTemplate.StructureBlockInfo> processedBlockInfoList, StructurePlaceSettings settings) {
/* 26 */     return processedBlockInfoList;
/*    */   }
/*    */   
/*    */   default boolean evaluatesEntirePieceState() {
/* 33 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */