/*     */ package net.minecraft.world.entity.animal.rabbit;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ 
/*     */ class RabbitMoveControl<T extends Rabbit> extends MoveControl<T> {
/*     */   private double nextJumpSpeed;
/*     */   
/*     */   public RabbitMoveControl(T rabbit) {
/* 617 */     super((Mob)rabbit);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 622 */     if (((Rabbit)this.mob).onGround() && !Rabbit.access$000((Rabbit)this.mob) && !((Rabbit.RabbitJumpControl)Rabbit.access$100((Rabbit)this.mob)).wantJump()) {
/* 623 */       ((Rabbit)this.mob).setSpeedModifier(0.0D);
/* 624 */     } else if (hasWanted() || this.operation == MoveControl.Operation.JUMPING) {
/* 625 */       ((Rabbit)this.mob).setSpeedModifier(this.nextJumpSpeed);
/*     */     } 
/* 627 */     super.tick();
/*     */   }
/*     */   
/*     */   public void setWantedPosition(double x, double y, double z, double speedModifier) {
/* 632 */     if (((Rabbit)this.mob).isInWater())
/* 633 */       speedModifier = 1.5D; 
/* 636 */     super.setWantedPosition(x, y, z, speedModifier);
/* 637 */     if (speedModifier > 0.0D)
/* 638 */       this.nextJumpSpeed = speedModifier; 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\rabbit\Rabbit$RabbitMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */