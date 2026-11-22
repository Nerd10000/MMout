/*    */ package net.minecraft.client.color.block;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.renderer.block.BlockAndTintGetter;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public interface BlockTintSource {
/*    */   int color(BlockState paramBlockState);
/*    */   
/*    */   default int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 15 */     return color(state);
/*    */   }
/*    */   
/*    */   default int colorAsTerrainParticle(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 20 */     return colorInWorld(state, level, pos);
/*    */   }
/*    */   
/*    */   default Set<Property<?>> relevantProperties() {
/* 24 */     return Set.of();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\block\BlockTintSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */