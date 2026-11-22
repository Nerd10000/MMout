/*     */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.resources.model.ModelBaker;
/*     */ 
/*     */ class null implements ModelBaker.SharedOperationKey<MultiPartModel.SharedBakedState> {
/*     */   public MultiPartModel.SharedBakedState compute(ModelBaker modelBakery) {
/* 118 */     ImmutableList.Builder<MultiPartModel.Selector<BlockStateModel>> selectors = ImmutableList.builderWithExpectedSize(MultiPartModel.Unbaked.this.selectors.size());
/* 119 */     for (MultiPartModel.Selector<BlockStateModel.Unbaked> selector : MultiPartModel.Unbaked.this.selectors)
/* 120 */       selectors.add(selector.with(((BlockStateModel.Unbaked)selector.model).bake(modelBakery))); 
/* 122 */     return new MultiPartModel.SharedBakedState((List<MultiPartModel.Selector<BlockStateModel>>)selectors.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\MultiPartModel$Unbaked$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */