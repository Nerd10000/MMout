/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class BinaryAnimator {
/*    */   private final int animationLength;
/*    */   
/*    */   private final EasingType easing;
/*    */   
/*    */   private int ticks;
/*    */   
/*    */   private int ticksOld;
/*    */   
/*    */   public BinaryAnimator(int animationLength, EasingType easing) {
/* 11 */     this.animationLength = animationLength;
/* 12 */     this.easing = easing;
/*    */   }
/*    */   
/*    */   public BinaryAnimator(int animationLength) {
/* 16 */     this(animationLength, EasingType.LINEAR);
/*    */   }
/*    */   
/*    */   public void tick(boolean active) {
/* 20 */     this.ticksOld = this.ticks;
/* 21 */     if (active) {
/* 22 */       if (this.ticks < this.animationLength)
/* 23 */         this.ticks++; 
/* 26 */     } else if (this.ticks > 0) {
/* 27 */       this.ticks--;
/*    */     } 
/*    */   }
/*    */   
/*    */   public float getFactor(float partialTicks) {
/* 33 */     float factor = Mth.lerp(partialTicks, this.ticksOld, this.ticks) / this.animationLength;
/* 34 */     return this.easing.apply(factor);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\BinaryAnimator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */