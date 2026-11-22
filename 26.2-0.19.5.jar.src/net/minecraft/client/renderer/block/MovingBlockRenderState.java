/*    */ package net.minecraft.client.renderer.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.CardinalLighting;
/*    */ import net.minecraft.world.level.ColorResolver;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class MovingBlockRenderState implements BlockAndTintGetter {
/* 17 */   public BlockPos randomSeedPos = BlockPos.ZERO;
/*    */   
/* 18 */   public BlockPos blockPos = BlockPos.ZERO;
/*    */   
/* 19 */   public BlockState blockState = Blocks.AIR.defaultBlockState();
/*    */   
/*    */   public Holder<Biome> biome;
/*    */   
/* 21 */   public CardinalLighting cardinalLighting = CardinalLighting.DEFAULT;
/*    */   
/* 22 */   public LevelLightEngine lightEngine = LevelLightEngine.EMPTY;
/*    */   
/*    */   public CardinalLighting cardinalLighting() {
/* 26 */     return this.cardinalLighting;
/*    */   }
/*    */   
/*    */   public LevelLightEngine getLightEngine() {
/* 31 */     return this.lightEngine;
/*    */   }
/*    */   
/*    */   public int getBlockTint(BlockPos pos, ColorResolver color) {
/* 36 */     if (this.biome == null)
/* 37 */       return -1; 
/* 39 */     return color.getColor((Biome)this.biome.value(), pos.getX(), pos.getZ());
/*    */   }
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 49 */     if (pos.equals(this.blockPos))
/* 50 */       return this.blockState; 
/* 52 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */   
/*    */   public FluidState getFluidState(BlockPos pos) {
/* 57 */     return getBlockState(pos).getFluidState();
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 62 */     return 1;
/*    */   }
/*    */   
/*    */   public int getMinY() {
/* 67 */     return this.blockPos.getY();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\MovingBlockRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */