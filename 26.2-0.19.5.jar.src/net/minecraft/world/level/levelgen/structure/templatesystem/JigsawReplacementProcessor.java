/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.commands.arguments.blocks.BlockStateParser;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class JigsawReplacementProcessor implements StructureProcessor {
/* 18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 19 */   public static final MapCodec<JigsawReplacementProcessor> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 21 */   public static final JigsawReplacementProcessor INSTANCE = new JigsawReplacementProcessor();
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 28 */     BlockState blockState, state = processedBlockInfo.state();
/* 29 */     if (!state.is(Blocks.JIGSAW) || SharedConstants.DEBUG_KEEP_JIGSAW_BLOCKS_DURING_STRUCTURE_GEN)
/* 30 */       return processedBlockInfo; 
/* 33 */     if (processedBlockInfo.nbt() == null) {
/* 34 */       LOGGER.warn("Jigsaw block at {} is missing nbt, will not replace", targetPosition);
/* 35 */       return processedBlockInfo;
/*    */     } 
/* 38 */     String stateString = processedBlockInfo.nbt().getStringOr("final_state", "minecraft:air");
/*    */     try {
/* 41 */       BlockStateParser.BlockResult result = BlockStateParser.parseForBlock(level.holderLookup(Registries.BLOCK), stateString, true);
/* 42 */       blockState = result.blockState();
/* 43 */     } catch (CommandSyntaxException e) {
/* 44 */       LOGGER.error("Failed to parse jigsaw replacement state '{}' at {}: {}", new Object[] { stateString, targetPosition, e.getMessage() });
/* 45 */       return null;
/*    */     } 
/* 47 */     if (blockState.is(Blocks.STRUCTURE_VOID))
/* 48 */       return null; 
/* 50 */     return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), blockState, null);
/*    */   }
/*    */   
/*    */   public MapCodec<JigsawReplacementProcessor> codec() {
/* 55 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\JigsawReplacementProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */