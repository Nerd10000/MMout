/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ 
/*     */ public class CubeMobMoveControl<T extends AbstractCubeMob> extends MoveControl<T> {
/*     */   private float yRot;
/*     */   
/*     */   private int jumpDelay;
/*     */   
/*     */   private boolean isAggressive;
/*     */   
/*     */   public CubeMobMoveControl(T cubeMob) {
/* 273 */     super((Mob)cubeMob);
/* 274 */     this.yRot = 180.0F * cubeMob.getYRot() / 3.1415927F;
/*     */   }
/*     */   
/*     */   public void setDirection(float yRot, boolean isAggressive) {
/* 278 */     this.yRot = yRot;
/* 279 */     this.isAggressive = isAggressive;
/*     */   }
/*     */   
/*     */   public void setWantedMovement(double speedModifier) {
/* 283 */     this.speedModifier = speedModifier;
/* 284 */     this.operation = MoveControl.Operation.MOVE_TO;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 289 */     ((AbstractCubeMob)this.mob).setYRot(rotlerp(((AbstractCubeMob)this.mob).getYRot(), this.yRot, 90.0F));
/* 290 */     ((AbstractCubeMob)this.mob).yHeadRot = ((AbstractCubeMob)this.mob).getYRot();
/* 291 */     ((AbstractCubeMob)this.mob).yBodyRot = ((AbstractCubeMob)this.mob).getYRot();
/* 293 */     if (this.operation != MoveControl.Operation.MOVE_TO) {
/* 294 */       ((AbstractCubeMob)this.mob).setZza(0.0F);
/*     */       return;
/*     */     } 
/* 297 */     this.operation = MoveControl.Operation.WAIT;
/* 299 */     if (((AbstractCubeMob)this.mob).onGround()) {
/* 300 */       ((AbstractCubeMob)this.mob).setSpeed((float)(this.speedModifier * ((AbstractCubeMob)this.mob).getAttributeValue(Attributes.MOVEMENT_SPEED)));
/* 301 */       if (this.jumpDelay-- <= 0) {
/* 302 */         this.jumpDelay = ((AbstractCubeMob)this.mob).getJumpDelay();
/* 303 */         if (this.isAggressive)
/* 304 */           this.jumpDelay /= 3; 
/* 306 */         ((AbstractCubeMob)this.mob).getJumpControl().jump();
/* 307 */         if (((AbstractCubeMob)this.mob).doPlayJumpSound())
/* 308 */           ((AbstractCubeMob)this.mob).playSound(((AbstractCubeMob)this.mob).getJumpSound(), ((AbstractCubeMob)this.mob).getSoundVolume(), ((AbstractCubeMob)this.mob).getSoundPitch()); 
/*     */       } else {
/* 311 */         ((AbstractCubeMob)this.mob).xxa = 0.0F;
/* 312 */         ((AbstractCubeMob)this.mob).zza = 0.0F;
/* 313 */         ((AbstractCubeMob)this.mob).setSpeed(0.0F);
/*     */       } 
/*     */     } else {
/* 316 */       ((AbstractCubeMob)this.mob).setSpeed((float)(this.speedModifier * ((AbstractCubeMob)this.mob).getAttributeValue(Attributes.MOVEMENT_SPEED)));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\AbstractCubeMob$CubeMobMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */