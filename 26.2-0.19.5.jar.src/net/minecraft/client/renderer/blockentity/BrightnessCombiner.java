/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2IntFunction;
/*    */ import net.minecraft.util.LightCoordsUtil;
/*    */ import net.minecraft.world.level.BlockAndLightGetter;
/*    */ import net.minecraft.world.level.block.DoubleBlockCombiner;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ 
/*    */ public class BrightnessCombiner<S extends BlockEntity> implements DoubleBlockCombiner.Combiner<S, Int2IntFunction> {
/*    */   public Int2IntFunction acceptDouble(S first, S second) {
/* 11 */     return i -> LightCoordsUtil.max(LightCoordsUtil.getLightCoords((BlockAndLightGetter)first.getLevel(), first.getBlockPos()), LightCoordsUtil.getLightCoords((BlockAndLightGetter)second.getLevel(), second.getBlockPos()));
/*    */   }
/*    */   
/*    */   public Int2IntFunction acceptSingle(S single) {
/* 19 */     return i -> i;
/*    */   }
/*    */   
/*    */   public Int2IntFunction acceptNone() {
/* 24 */     return i -> i;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\BrightnessCombiner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */