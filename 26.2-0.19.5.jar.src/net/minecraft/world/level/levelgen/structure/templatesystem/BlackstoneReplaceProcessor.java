/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SlabBlock;
/*    */ import net.minecraft.world.level.block.StairBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlackstoneReplaceProcessor implements StructureProcessor {
/* 19 */   public static final MapCodec<BlackstoneReplaceProcessor> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/* 21 */   public static final BlackstoneReplaceProcessor INSTANCE = new BlackstoneReplaceProcessor();
/*    */   
/*    */   private final Map<Block, Block> replacements;
/*    */   
/*    */   private BlackstoneReplaceProcessor() {
/* 26 */     this.replacements = (Map<Block, Block>)Util.make(Maps.newHashMap(), map -> {
/*    */           map.put(Blocks.COBBLESTONE, Blocks.BLACKSTONE);
/*    */           map.put(Blocks.MOSSY_COBBLESTONE, Blocks.BLACKSTONE);
/*    */           map.put(Blocks.STONE, Blocks.POLISHED_BLACKSTONE);
/*    */           map.put(Blocks.STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/*    */           map.put(Blocks.MOSSY_STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
/*    */           map.put(Blocks.COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
/*    */           map.put(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
/*    */           map.put(Blocks.STONE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
/*    */           map.put(Blocks.STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*    */           map.put(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
/*    */           map.put(Blocks.COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
/*    */           map.put(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
/*    */           map.put(Blocks.SMOOTH_STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
/*    */           map.put(Blocks.STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
/*    */           map.put(Blocks.STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/*    */           map.put(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
/*    */           map.put(Blocks.STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*    */           map.put(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
/*    */           map.put(Blocks.COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
/*    */           map.put(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
/*    */           map.put(Blocks.CHISELED_STONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE);
/*    */           map.put(Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
/*    */           map.put(Blocks.IRON_BARS, Blocks.IRON_CHAIN);
/*    */         });
/*    */   }
/*    */   
/*    */   public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
/* 66 */     Block newBlock = this.replacements.get(processedBlockInfo.state().getBlock());
/* 67 */     if (newBlock == null)
/* 68 */       return processedBlockInfo; 
/* 70 */     BlockState oldState = processedBlockInfo.state();
/* 71 */     BlockState newState = newBlock.defaultBlockState();
/* 72 */     if (oldState.hasProperty((Property)StairBlock.FACING))
/* 73 */       newState = (BlockState)newState.setValue((Property)StairBlock.FACING, oldState.getValue((Property)StairBlock.FACING)); 
/* 75 */     if (oldState.hasProperty((Property)StairBlock.HALF))
/* 76 */       newState = (BlockState)newState.setValue((Property)StairBlock.HALF, oldState.getValue((Property)StairBlock.HALF)); 
/* 78 */     if (oldState.hasProperty((Property)SlabBlock.TYPE))
/* 79 */       newState = (BlockState)newState.setValue((Property)SlabBlock.TYPE, oldState.getValue((Property)SlabBlock.TYPE)); 
/* 81 */     return new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), newState, processedBlockInfo.nbt());
/*    */   }
/*    */   
/*    */   public MapCodec<BlackstoneReplaceProcessor> codec() {
/* 86 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\BlackstoneReplaceProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */