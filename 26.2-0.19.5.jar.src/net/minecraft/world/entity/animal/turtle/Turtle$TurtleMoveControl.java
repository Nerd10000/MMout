/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ 
/*     */ class TurtleMoveControl<T extends Turtle> extends MoveControl<T> {
/*     */   public TurtleMoveControl(T turtle) {
/* 654 */     super((Mob)turtle);
/*     */   }
/*     */   
/*     */   private void updateSpeed() {
/* 658 */     if (((Turtle)this.mob).isInWater()) {
/* 660 */       ((Turtle)this.mob).setDeltaMovement(((Turtle)this.mob).getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/* 662 */       if (!((Turtle)this.mob).getHomePos().closerToCenterThan((Position)((Turtle)this.mob).position(), 16.0D))
/* 663 */         ((Turtle)this.mob).setSpeed(Math.max(((Turtle)this.mob).getSpeed() / 2.0F, 0.08F)); 
/* 666 */       if (((Turtle)this.mob).isBaby())
/* 667 */         ((Turtle)this.mob).setSpeed(Math.max(((Turtle)this.mob).getSpeed() / 3.0F, 0.06F)); 
/* 669 */     } else if (((Turtle)this.mob).onGround()) {
/* 670 */       ((Turtle)this.mob).setSpeed(Math.max(((Turtle)this.mob).getSpeed() / 2.0F, 0.06F));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 676 */     updateSpeed();
/* 678 */     if (this.operation != MoveControl.Operation.MOVE_TO || ((Turtle)this.mob).getNavigation().isDone()) {
/* 679 */       ((Turtle)this.mob).setSpeed(0.0F);
/*     */       return;
/*     */     } 
/* 683 */     double xd = this.wantedX - ((Turtle)this.mob).getX();
/* 684 */     double yd = this.wantedY - ((Turtle)this.mob).getY();
/* 685 */     double zd = this.wantedZ - ((Turtle)this.mob).getZ();
/* 686 */     double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
/* 687 */     if (dd < 9.999999747378752E-6D) {
/* 688 */       ((Turtle)this.mob).setSpeed(0.0F);
/*     */       return;
/*     */     } 
/* 692 */     yd /= dd;
/* 694 */     float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 695 */     ((Turtle)this.mob).setYRot(rotlerp(((Turtle)this.mob).getYRot(), yRotD, 90.0F));
/* 696 */     ((Turtle)this.mob).yBodyRot = ((Turtle)this.mob).getYRot();
/* 698 */     float targetSpeed = (float)(this.speedModifier * ((Turtle)this.mob).getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 699 */     ((Turtle)this.mob).setSpeed(Mth.lerp(0.125F, ((Turtle)this.mob).getSpeed(), targetSpeed));
/* 701 */     ((Turtle)this.mob).setDeltaMovement(((Turtle)this.mob).getDeltaMovement().add(0.0D, ((Turtle)this.mob).getSpeed() * yd * 0.1D, 0.0D));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtleMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */