/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BlockStateModelSet {
/*    */   private final Map<BlockState, BlockStateModel> modelByState;
/*    */   
/*    */   private final BlockStateModel missingModel;
/*    */   
/*    */   public BlockStateModelSet(Map<BlockState, BlockStateModel> modelByState, BlockStateModel missingModel) {
/* 14 */     this.modelByState = modelByState;
/* 15 */     this.missingModel = missingModel;
/*    */   }
/*    */   
/*    */   public BlockStateModel get(BlockState state) {
/* 19 */     return this.modelByState.getOrDefault(state, this.missingModel);
/*    */   }
/*    */   
/*    */   public BlockStateModel missingModel() {
/* 23 */     return this.missingModel;
/*    */   }
/*    */   
/*    */   public Material.Baked getParticleMaterial(BlockState blockState) {
/* 27 */     return get(blockState).particleMaterial();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockStateModelSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */