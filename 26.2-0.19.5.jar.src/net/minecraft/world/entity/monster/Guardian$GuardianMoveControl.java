/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class GuardianMoveControl<T extends Guardian> extends MoveControl<T> {
/*     */   public GuardianMoveControl(T guardian) {
/* 459 */     super((Mob)guardian);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 464 */     if (this.operation != MoveControl.Operation.MOVE_TO || ((Guardian)this.mob).getNavigation().isDone()) {
/* 466 */       ((Guardian)this.mob).setSpeed(0.0F);
/* 467 */       ((Guardian)this.mob).setMoving(false);
/*     */       return;
/*     */     } 
/* 475 */     Vec3 delta = new Vec3(this.wantedX - ((Guardian)this.mob).getX(), this.wantedY - ((Guardian)this.mob).getY(), this.wantedZ - ((Guardian)this.mob).getZ());
/* 477 */     double length = delta.length();
/* 479 */     double xd = delta.x / length;
/* 480 */     double yd = delta.y / length;
/* 481 */     double zd = delta.z / length;
/* 483 */     float yRotD = (float)(Mth.atan2(delta.z, delta.x) * 57.2957763671875D) - 90.0F;
/* 485 */     ((Guardian)this.mob).setYRot(rotlerp(((Guardian)this.mob).getYRot(), yRotD, 90.0F));
/* 486 */     ((Guardian)this.mob).yBodyRot = ((Guardian)this.mob).getYRot();
/* 488 */     float targetSpeed = (float)(this.speedModifier * ((Guardian)this.mob).getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 489 */     float newSpeed = Mth.lerp(0.125F, ((Guardian)this.mob).getSpeed(), targetSpeed);
/* 490 */     ((Guardian)this.mob).setSpeed(newSpeed);
/* 491 */     double push = Math.sin((((Guardian)this.mob).tickCount + ((Guardian)this.mob).getId()) * 0.5D) * 0.05D;
/* 492 */     double cos = Math.cos((((Guardian)this.mob).getYRot() * 0.017453292F));
/* 493 */     double sin = Math.sin((((Guardian)this.mob).getYRot() * 0.017453292F));
/* 494 */     double yPush = Math.sin((((Guardian)this.mob).tickCount + ((Guardian)this.mob).getId()) * 0.75D) * 0.05D;
/* 496 */     ((Guardian)this.mob).setDeltaMovement(((Guardian)this.mob).getDeltaMovement().add(push * cos, yPush * (sin + cos) * 0.25D + newSpeed * yd * 0.1D, push * sin));
/* 502 */     LookControl control = ((Guardian)this.mob).getLookControl();
/* 503 */     double newLookX = ((Guardian)this.mob).getX() + xd * 2.0D;
/* 504 */     double newLookY = ((Guardian)this.mob).getEyeY() + yd / length;
/* 505 */     double newLookZ = ((Guardian)this.mob).getZ() + zd * 2.0D;
/* 506 */     double oldLookX = control.getWantedX();
/* 507 */     double oldLookY = control.getWantedY();
/* 508 */     double oldLookZ = control.getWantedZ();
/* 509 */     if (!control.isLookingAtTarget()) {
/* 510 */       oldLookX = newLookX;
/* 511 */       oldLookY = newLookY;
/* 512 */       oldLookZ = newLookZ;
/*     */     } 
/* 514 */     ((Guardian)this.mob).getLookControl().setLookAt(Mth.lerp(0.125D, oldLookX, newLookX), Mth.lerp(0.125D, oldLookY, newLookY), Mth.lerp(0.125D, oldLookZ, newLookZ), 10.0F, 40.0F);
/* 515 */     ((Guardian)this.mob).setMoving(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Guardian$GuardianMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */