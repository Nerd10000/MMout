/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class PhantomCircleAroundAnchorGoal extends Phantom.PhantomMoveTargetGoal {
/*     */   private float angle;
/*     */   
/*     */   private float distance;
/*     */   
/*     */   private float height;
/*     */   
/*     */   private float clockwise;
/*     */   
/*     */   public boolean canUse() {
/* 319 */     return (Phantom.this.getTarget() == null || Phantom.this.attackPhase == Phantom.AttackPhase.CIRCLE);
/*     */   }
/*     */   
/*     */   public void start() {
/* 324 */     this.distance = 5.0F + Phantom.access$000(Phantom.this).nextFloat() * 10.0F;
/* 325 */     this.height = -4.0F + Phantom.access$100(Phantom.this).nextFloat() * 9.0F;
/* 326 */     this.clockwise = Phantom.access$200(Phantom.this).nextBoolean() ? 1.0F : -1.0F;
/* 327 */     selectNext();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 332 */     if (Phantom.access$300(Phantom.this).nextInt(adjustedTickDelay(350)) == 0)
/* 333 */       this.height = -4.0F + Phantom.access$400(Phantom.this).nextFloat() * 9.0F; 
/* 335 */     if (Phantom.access$500(Phantom.this).nextInt(adjustedTickDelay(250)) == 0) {
/* 336 */       this.distance++;
/* 337 */       if (this.distance > 15.0F) {
/* 338 */         this.distance = 5.0F;
/* 339 */         this.clockwise = -this.clockwise;
/*     */       } 
/*     */     } 
/* 342 */     if (Phantom.access$600(Phantom.this).nextInt(adjustedTickDelay(450)) == 0) {
/* 343 */       this.angle = Phantom.access$700(Phantom.this).nextFloat() * 2.0F * 3.1415927F;
/* 344 */       selectNext();
/*     */     } 
/* 346 */     if (touchingTarget())
/* 347 */       selectNext(); 
/* 350 */     if (Phantom.this.moveTargetPoint.y < Phantom.this.getY() && !Phantom.this.level().isEmptyBlock(Phantom.this.blockPosition().below(1))) {
/* 351 */       this.height = Math.max(1.0F, this.height);
/* 352 */       selectNext();
/*     */     } 
/* 355 */     if (Phantom.this.moveTargetPoint.y > Phantom.this.getY() && !Phantom.this.level().isEmptyBlock(Phantom.this.blockPosition().above(1))) {
/* 356 */       this.height = Math.min(-1.0F, this.height);
/* 357 */       selectNext();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void selectNext() {
/* 362 */     if (Phantom.this.anchorPoint == null)
/* 363 */       Phantom.this.anchorPoint = Phantom.this.blockPosition(); 
/* 365 */     this.angle += this.clockwise * 15.0F * 0.017453292F;
/* 366 */     Phantom.this.moveTargetPoint = Vec3.atLowerCornerOf((Vec3i)Phantom.this.anchorPoint).add((this.distance * Mth.cos(this.angle)), (-4.0F + this.height), (this.distance * Mth.sin(this.angle)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Phantom$PhantomCircleAroundAnchorGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */