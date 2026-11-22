/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class VexMoveControl<T extends Mob> extends MoveControl<T> {
/*     */   public VexMoveControl(T vex) {
/* 232 */     super((Mob)vex);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 237 */     if (this.operation != MoveControl.Operation.MOVE_TO)
/*     */       return; 
/* 245 */     Vec3 delta = new Vec3(this.wantedX - Vex.this.getX(), this.wantedY - Vex.this.getY(), this.wantedZ - Vex.this.getZ());
/* 248 */     double deltaLength = delta.length();
/* 249 */     if (deltaLength < Vex.this.getBoundingBox().getSize()) {
/* 250 */       this.operation = MoveControl.Operation.WAIT;
/* 251 */       Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().scale(0.5D));
/*     */     } else {
/* 253 */       Vex.this.setDeltaMovement(Vex.this.getDeltaMovement().add(delta.scale(this.speedModifier * 0.05D / deltaLength)));
/* 255 */       if (Vex.this.getTarget() == null) {
/* 256 */         Vec3 movement = Vex.this.getDeltaMovement();
/* 257 */         Vex.this.setYRot(-((float)Mth.atan2(movement.x, movement.z)) * 57.295776F);
/* 258 */         Vex.this.yBodyRot = Vex.this.getYRot();
/*     */       } else {
/* 261 */         double tx = Vex.this.getTarget().getX() - Vex.this.getX();
/* 262 */         double tz = Vex.this.getTarget().getZ() - Vex.this.getZ();
/* 263 */         Vex.this.setYRot(-((float)Mth.atan2(tx, tz)) * 57.295776F);
/* 264 */         Vex.this.yBodyRot = Vex.this.getYRot();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Vex$VexMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */