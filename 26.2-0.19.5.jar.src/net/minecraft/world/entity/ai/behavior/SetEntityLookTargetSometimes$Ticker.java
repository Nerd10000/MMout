/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ 
/*    */ public final class Ticker {
/*    */   private final UniformInt interval;
/*    */   
/*    */   private int ticksUntilNextStart;
/*    */   
/*    */   public Ticker(UniformInt interval) {
/* 54 */     if (interval.minInclusive() <= 1)
/* 55 */       throw new IllegalArgumentException(); 
/* 57 */     this.interval = interval;
/*    */   }
/*    */   
/*    */   public boolean tickDownAndCheck(RandomSource random) {
/* 61 */     if (this.ticksUntilNextStart == 0) {
/* 62 */       this.ticksUntilNextStart = this.interval.sample(random) - 1;
/* 63 */       return false;
/*    */     } 
/* 66 */     return (--this.ticksUntilNextStart == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\SetEntityLookTargetSometimes$Ticker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */