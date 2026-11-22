/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.color.block.BlockColors;
/*    */ import net.minecraft.client.color.block.BlockTintSource;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.client.renderer.block.model.BlockModel;
/*    */ import net.minecraft.client.renderer.block.model.BlockStateModelWrapper;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ public class BlockModelSet {
/* 17 */   private static final Matrix4fc IDENTITY = (Matrix4fc)new Matrix4f();
/*    */   
/*    */   private final BlockStateModelSet fallback;
/*    */   
/*    */   private final BlockColors blockColors;
/*    */   
/* 22 */   private final Map<BlockState, BlockModel> blockModelByStateCache = new HashMap<>();
/*    */   
/*    */   public BlockModelSet(BlockStateModelSet fallback, Map<BlockState, BlockModel> blockModelByState, BlockColors blockColors) {
/* 25 */     this.fallback = fallback;
/* 26 */     this.blockModelByStateCache.putAll(blockModelByState);
/* 27 */     this.blockColors = blockColors;
/*    */   }
/*    */   
/*    */   public BlockModel get(BlockState blockState) {
/* 32 */     return this.blockModelByStateCache.computeIfAbsent(blockState, this::createFallbackModel);
/*    */   }
/*    */   
/*    */   private BlockModel createFallbackModel(BlockState blockState) {
/* 37 */     List<BlockTintSource> tints = this.blockColors.getTintSources(blockState);
/* 38 */     BlockStateModel plainModel = this.fallback.get(blockState);
/* 39 */     return (BlockModel)new BlockStateModelWrapper(plainModel, tints, IDENTITY);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockModelSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */