/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ class VerticalGradientCondition extends SurfaceRules.LazyYCondition {
/*     */   private VerticalGradientCondition(SurfaceRules.VerticalGradientConditionSource this$0) {
/* 761 */     super(paramContext);
/*     */   }
/*     */   
/*     */   protected boolean compute() {
/* 766 */     int blockY = this.context.blockY;
/* 767 */     if (blockY <= trueAtAndBelow)
/* 768 */       return true; 
/* 770 */     if (blockY >= falseAtAndAbove)
/* 771 */       return false; 
/* 773 */     double probability = Mth.map(blockY, trueAtAndBelow, falseAtAndAbove, 1.0D, 0.0D);
/* 774 */     RandomSource random = randomFactory.at(this.context.blockX, blockY, this.context.blockZ);
/* 775 */     return (random.nextFloat() < probability);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$VerticalGradientConditionSource$1VerticalGradientCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */