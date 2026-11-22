/*    */ package net.minecraft.client.renderer.block.dispatch;
/*    */ 
/*    */ import net.minecraft.client.resources.model.ModelBaker;
/*    */ 
/*    */ class null implements ModelBaker.SharedOperationKey<BlockStateModel> {
/*    */   public BlockStateModel compute(ModelBaker modelBakery) {
/* 92 */     return BlockStateModel.SimpleCachedUnbakedRoot.this.contents.bake(modelBakery);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\BlockStateModel$SimpleCachedUnbakedRoot$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */