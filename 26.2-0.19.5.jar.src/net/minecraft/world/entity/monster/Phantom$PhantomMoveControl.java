/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class PhantomMoveControl<T extends Mob> extends MoveControl<T> {
/* 223 */   private float speed = 0.1F;
/*     */   
/*     */   public PhantomMoveControl(T mob) {
/* 226 */     super((Mob)mob);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 231 */     if (Phantom.this.horizontalCollision) {
/* 233 */       Phantom.this.setYRot(Phantom.this.getYRot() + 180.0F);
/* 234 */       this.speed = 0.1F;
/*     */     } 
/* 238 */     double tdx = Phantom.this.moveTargetPoint.x - Phantom.this.getX();
/* 239 */     double tdy = Phantom.this.moveTargetPoint.y - Phantom.this.getY();
/* 240 */     double tdz = Phantom.this.moveTargetPoint.z - Phantom.this.getZ();
/* 241 */     double sd = Math.sqrt(tdx * tdx + tdz * tdz);
/* 244 */     if (Math.abs(sd) > 9.999999747378752E-6D) {
/* 245 */       double yRelativeScale = 1.0D - Math.abs(tdy * 0.699999988079071D) / sd;
/* 246 */       tdx *= yRelativeScale;
/* 247 */       tdz *= yRelativeScale;
/* 248 */       sd = Math.sqrt(tdx * tdx + tdz * tdz);
/* 249 */       double sd2 = Math.sqrt(tdx * tdx + tdz * tdz + tdy * tdy);
/* 252 */       float prev = Phantom.this.getYRot();
/* 253 */       float angle = (float)Mth.atan2(tdz, tdx);
/* 254 */       float a = Mth.wrapDegrees(Phantom.this.getYRot() + 90.0F);
/* 255 */       float b = Mth.wrapDegrees(angle * 57.295776F);
/* 256 */       Phantom.this.setYRot(Mth.approachDegrees(a, b, 4.0F) - 90.0F);
/* 257 */       Phantom.this.yBodyRot = Phantom.this.getYRot();
/* 259 */       if (Mth.degreesDifferenceAbs(prev, Phantom.this.getYRot()) < 3.0F) {
/* 260 */         this.speed = Mth.approach(this.speed, 1.8F, 0.005F * 1.8F / this.speed);
/*     */       } else {
/* 262 */         this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
/*     */       } 
/* 265 */       float xRotD = (float)-(Mth.atan2(-tdy, sd) * 57.2957763671875D);
/* 266 */       Phantom.this.setXRot(xRotD);
/* 268 */       float moveAngle = Phantom.this.getYRot() + 90.0F;
/* 269 */       double txd = (this.speed * Mth.cos((moveAngle * 0.017453292F))) * Math.abs(tdx / sd2);
/* 270 */       double tzd = (this.speed * Mth.sin((moveAngle * 0.017453292F))) * Math.abs(tdz / sd2);
/* 271 */       double tyd = (this.speed * Mth.sin((xRotD * 0.017453292F))) * Math.abs(tdy / sd2);
/* 273 */       Vec3 movement = Phantom.this.getDeltaMovement();
/* 274 */       Phantom.this.setDeltaMovement(movement.add((new Vec3(txd, tyd, tzd)).subtract(movement).scale(0.2D)));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Phantom$PhantomMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */