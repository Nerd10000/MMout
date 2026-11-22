/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ 
/*     */ class DrownedMoveControl<T extends Drowned> extends MoveControl<T> {
/*     */   public DrownedMoveControl(T drowned) {
/* 541 */     super((Mob)drowned);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 546 */     LivingEntity target = ((Drowned)this.mob).getTarget();
/* 547 */     if (((Drowned)this.mob).wantsToSwim() && ((Drowned)this.mob).isInWater()) {
/* 548 */       if ((target != null && target.getY() > ((Drowned)this.mob).getY()) || ((Drowned)this.mob).isSearchingForLand())
/* 550 */         ((Drowned)this.mob).setDeltaMovement(((Drowned)this.mob).getDeltaMovement().add(0.0D, 0.002D, 0.0D)); 
/* 553 */       if (this.operation != MoveControl.Operation.MOVE_TO || ((Drowned)this.mob).getNavigation().isDone()) {
/* 554 */         ((Drowned)this.mob).setSpeed(0.0F);
/*     */         return;
/*     */       } 
/* 558 */       double xd = this.wantedX - ((Drowned)this.mob).getX();
/* 559 */       double yd = this.wantedY - ((Drowned)this.mob).getY();
/* 560 */       double zd = this.wantedZ - ((Drowned)this.mob).getZ();
/* 561 */       double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
/* 562 */       yd /= dd;
/* 564 */       float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 565 */       ((Drowned)this.mob).setYRot(rotlerp(((Drowned)this.mob).getYRot(), yRotD, 90.0F));
/* 566 */       ((Drowned)this.mob).yBodyRot = ((Drowned)this.mob).getYRot();
/* 568 */       float targetSpeed = (float)(this.speedModifier * ((Drowned)this.mob).getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 569 */       float newSpeed = Mth.lerp(0.125F, ((Drowned)this.mob).getSpeed(), targetSpeed);
/* 570 */       ((Drowned)this.mob).setSpeed(newSpeed);
/* 571 */       ((Drowned)this.mob).setDeltaMovement(((Drowned)this.mob).getDeltaMovement().add(newSpeed * xd * 0.005D, newSpeed * yd * 0.1D, newSpeed * zd * 0.005D));
/*     */     } else {
/* 577 */       if (!((Drowned)this.mob).onGround())
/* 578 */         ((Drowned)this.mob).setDeltaMovement(((Drowned)this.mob).getDeltaMovement().add(0.0D, -0.008D, 0.0D)); 
/* 580 */       super.tick();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Drowned$DrownedMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */