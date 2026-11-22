/*     */ package net.minecraft.client.renderer.block.dispatch;
/*     */ 
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ import net.minecraft.client.resources.model.ResolvableModel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class SimpleCachedUnbakedRoot implements BlockStateModel.UnbakedRoot {
/*     */   private final BlockStateModel.Unbaked contents;
/*     */   
/*  89 */   private final ModelBaker.SharedOperationKey<BlockStateModel> bakingKey = new ModelBaker.SharedOperationKey<BlockStateModel>() {
/*     */       public BlockStateModel compute(ModelBaker modelBakery) {
/*  92 */         return BlockStateModel.SimpleCachedUnbakedRoot.this.contents.bake(modelBakery);
/*     */       }
/*     */     };
/*     */   
/*     */   public SimpleCachedUnbakedRoot(BlockStateModel.Unbaked contents) {
/*  97 */     this.contents = contents;
/*     */   }
/*     */   
/*     */   public void resolveDependencies(ResolvableModel.Resolver resolver) {
/* 102 */     this.contents.resolveDependencies(resolver);
/*     */   }
/*     */   
/*     */   public BlockStateModel bake(BlockState blockState, ModelBaker modelBakery) {
/* 107 */     return (BlockStateModel)modelBakery.compute(this.bakingKey);
/*     */   }
/*     */   
/*     */   public Object visualEqualityGroup(BlockState blockState) {
/* 113 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\BlockStateModel$SimpleCachedUnbakedRoot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */