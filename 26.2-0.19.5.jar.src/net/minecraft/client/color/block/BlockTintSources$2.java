/*    */ package net.minecraft.client.color.block;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.renderer.BiomeColors;
/*    */ import net.minecraft.client.renderer.block.BlockAndTintGetter;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.GrassColor;
/*    */ import net.minecraft.world.level.block.DoublePlantBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ class null implements BlockTintSource {
/*    */   public int color(BlockState state) {
/* 43 */     return GrassColor.getDefaultColor();
/*    */   }
/*    */   
/*    */   public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
/* 48 */     return BiomeColors.getAverageGrassColor(level, (state.getValue((Property)DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) ? pos.below() : pos);
/*    */   }
/*    */   
/*    */   public Set<Property<?>> relevantProperties() {
/* 53 */     return (Set)Set.of(DoublePlantBlock.HALF);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\block\BlockTintSources$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */