/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.color.block.BlockColors;
/*     */ import net.minecraft.client.renderer.block.model.BlockModel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ class Builder {
/*     */   private final BlockColors blockColors;
/*     */   
/* 303 */   private final Map<BlockState, BlockModel.Unbaked> result = new HashMap<>();
/*     */   
/*     */   private Builder(BlockColors blockColors) {
/* 306 */     this.blockColors = blockColors;
/*     */   }
/*     */   
/*     */   private void put(BuiltInBlockModels.ModelFactory factory, Block a, Block b) {
/* 310 */     put(factory, a);
/* 311 */     put(factory, b);
/*     */   }
/*     */   
/*     */   private void put(BlockModel.Unbaked specialModel, Block block) {
/* 315 */     put(paramBlockState -> specialModel, block);
/*     */   }
/*     */   
/*     */   private void put(BuiltInBlockModels.ModelFactory factory, Block block) {
/* 319 */     for (UnmodifiableIterator<BlockState> unmodifiableIterator = block.getStateDefinition().getPossibleStates().iterator(); unmodifiableIterator.hasNext(); ) {
/* 319 */       BlockState blockState = unmodifiableIterator.next();
/* 320 */       this.result.put(blockState, factory.create(this.blockColors, blockState));
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map<BlockState, BlockModel.Unbaked> build() {
/* 325 */     return Map.copyOf(this.result);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BuiltInBlockModels$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */