/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public interface PositionalRandomFactory {
/*    */   default RandomSource at(BlockPos pos) {
/* 16 */     return at(pos.getX(), pos.getY(), pos.getZ());
/*    */   }
/*    */   
/*    */   default RandomSource fromHashOf(Identifier name) {
/* 20 */     return fromHashOf(name.toString());
/*    */   }
/*    */   
/*    */   RandomSource fromHashOf(String paramString);
/*    */   
/*    */   RandomSource fromSeed(long paramLong);
/*    */   
/*    */   RandomSource at(int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   @VisibleForTesting
/*    */   void parityConfigString(StringBuilder paramStringBuilder);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\PositionalRandomFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */