/*     */ package net.minecraft.world.entity.monster.creaking;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.JumpControl;
/*     */ 
/*     */ class CreakingJumpControl extends JumpControl {
/*     */   public CreakingJumpControl(Creaking creaking) {
/* 555 */     super((Mob)creaking);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 560 */     if (Creaking.this.canMove()) {
/* 561 */       super.tick();
/*     */     } else {
/* 563 */       Creaking.this.setJumping(false);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\creaking\Creaking$CreakingJumpControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */