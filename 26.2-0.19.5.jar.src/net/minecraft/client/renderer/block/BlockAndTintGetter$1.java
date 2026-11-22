/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.CardinalLighting;
/*    */ import net.minecraft.world.level.ColorResolver;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ class null implements BlockAndTintGetter {
/*    */   public CardinalLighting cardinalLighting() {
/* 20 */     return CardinalLighting.DEFAULT;
/*    */   }
/*    */   
/*    */   public LevelLightEngine getLightEngine() {
/* 25 */     return LevelLightEngine.EMPTY;
/*    */   }
/*    */   
/*    */   public int getBlockTint(BlockPos pos, ColorResolver color) {
/* 30 */     return -1;
/*    */   }
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 35 */     return null;
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 40 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */   
/*    */   public FluidState getFluidState(BlockPos pos) {
/* 45 */     return Fluids.EMPTY.defaultFluidState();
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 50 */     return 0;
/*    */   }
/*    */   
/*    */   public int getMinY() {
/* 55 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockAndTintGetter$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */