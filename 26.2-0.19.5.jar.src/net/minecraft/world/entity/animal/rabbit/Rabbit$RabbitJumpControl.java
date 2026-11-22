/*     */ package net.minecraft.world.entity.animal.rabbit;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.JumpControl;
/*     */ 
/*     */ public class RabbitJumpControl extends JumpControl {
/*     */   private final Rabbit rabbit;
/*     */   
/*     */   private boolean canJump;
/*     */   
/*     */   public RabbitJumpControl(Rabbit rabbit) {
/* 588 */     super((Mob)rabbit);
/* 589 */     this.rabbit = rabbit;
/*     */   }
/*     */   
/*     */   public boolean wantJump() {
/* 593 */     return this.jump;
/*     */   }
/*     */   
/*     */   public boolean canJump() {
/* 597 */     return this.canJump;
/*     */   }
/*     */   
/*     */   public void setCanJump(boolean canJump) {
/* 601 */     this.canJump = canJump;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 606 */     if (this.jump) {
/* 607 */       this.rabbit.startJumping();
/* 608 */       this.jump = false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\rabbit\Rabbit$RabbitJumpControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */