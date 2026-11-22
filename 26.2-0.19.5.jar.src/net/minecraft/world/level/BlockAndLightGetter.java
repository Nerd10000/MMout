/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*    */ 
/*    */ public interface BlockAndLightGetter extends BlockGetter {
/*    */   LevelLightEngine getLightEngine();
/*    */   
/*    */   default int getBrightness(LightLayer layer, BlockPos pos) {
/* 11 */     return getLightEngine().getLayerListener(layer).getLightValue(pos);
/*    */   }
/*    */   
/*    */   default int getRawBrightness(BlockPos pos, int darkening) {
/* 15 */     return getLightEngine().getRawBrightness(pos, darkening);
/*    */   }
/*    */   
/*    */   default boolean canSeeSky(BlockPos pos) {
/* 19 */     return (getBrightness(LightLayer.SKY, pos) >= 15);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\BlockAndLightGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */