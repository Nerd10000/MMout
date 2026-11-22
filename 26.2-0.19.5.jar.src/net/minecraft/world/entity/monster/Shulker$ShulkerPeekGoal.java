/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class ShulkerPeekGoal extends Goal {
/*     */   private int peekTime;
/*     */   
/*     */   public boolean canUse() {
/* 650 */     return (Shulker.this.getTarget() == null && Shulker.access$000(Shulker.this).nextInt(reducedTickDelay(40)) == 0 && Shulker.this.canStayAt(Shulker.this.blockPosition(), Shulker.this.getAttachFace()));
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 655 */     return (Shulker.this.getTarget() == null && this.peekTime > 0);
/*     */   }
/*     */   
/*     */   public void start() {
/* 660 */     this.peekTime = adjustedTickDelay(20 * (1 + Shulker.access$100(Shulker.this).nextInt(3)));
/* 661 */     Shulker.this.setRawPeekAmount(30);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 666 */     if (Shulker.this.getTarget() == null)
/* 667 */       Shulker.this.setRawPeekAmount(0); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 673 */     this.peekTime--;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Shulker$ShulkerPeekGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */