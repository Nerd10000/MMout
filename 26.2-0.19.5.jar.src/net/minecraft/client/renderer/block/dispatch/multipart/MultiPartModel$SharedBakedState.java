/*    */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.BitSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*    */ import net.minecraft.client.resources.model.sprite.Material;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ final class SharedBakedState {
/*    */   private final List<MultiPartModel.Selector<BlockStateModel>> selectors;
/*    */   
/*    */   private final Material.Baked particleMaterial;
/*    */   
/*    */   private final int materialFlags;
/*    */   
/* 67 */   private final Map<BitSet, List<BlockStateModel>> subsets = new ConcurrentHashMap<>();
/*    */   
/*    */   private static BlockStateModel getFirstModel(List<MultiPartModel.Selector<BlockStateModel>> selectors) {
/* 70 */     if (selectors.isEmpty())
/* 71 */       throw new IllegalArgumentException("Model must have at least one selector"); 
/* 73 */     return ((MultiPartModel.Selector<BlockStateModel>)selectors.getFirst()).model();
/*    */   }
/*    */   
/*    */   private static int computeMaterialFlags(List<MultiPartModel.Selector<BlockStateModel>> selectors) {
/* 77 */     int flags = 0;
/* 78 */     for (MultiPartModel.Selector<BlockStateModel> selector : selectors)
/* 79 */       flags |= ((BlockStateModel)selector.model).materialFlags(); 
/* 81 */     return flags;
/*    */   }
/*    */   
/*    */   public SharedBakedState(List<MultiPartModel.Selector<BlockStateModel>> selectors) {
/* 85 */     this.selectors = selectors;
/* 86 */     BlockStateModel firstModel = getFirstModel(selectors);
/* 87 */     this.particleMaterial = firstModel.particleMaterial();
/* 88 */     this.materialFlags = computeMaterialFlags(selectors);
/*    */   }
/*    */   
/*    */   public List<BlockStateModel> selectModels(BlockState state) {
/* 92 */     BitSet selectedModels = new BitSet();
/* 93 */     for (int i = 0; i < this.selectors.size(); i++) {
/* 94 */       if (((MultiPartModel.Selector)this.selectors.get(i)).condition.test(state))
/* 95 */         selectedModels.set(i); 
/*    */     } 
/* 99 */     return this.subsets.computeIfAbsent(selectedModels, selected -> {
/*    */           ImmutableList.Builder<BlockStateModel> result = ImmutableList.builder();
/*    */           for (int i = 0; i < this.selectors.size(); i++) {
/*    */             if (selected.get(i))
/*    */               result.add((BlockStateModel)((MultiPartModel.Selector)this.selectors.get(i)).model); 
/*    */           } 
/*    */           return (List)result.build();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\MultiPartModel$SharedBakedState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */