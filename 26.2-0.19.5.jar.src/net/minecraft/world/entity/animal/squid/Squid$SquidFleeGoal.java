/*     */ package net.minecraft.world.entity.animal.squid;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class SquidFleeGoal extends Goal {
/*     */   private static final float SQUID_FLEE_SPEED = 3.0F;
/*     */   
/*     */   private static final float SQUID_FLEE_MIN_DISTANCE = 5.0F;
/*     */   
/*     */   private static final float SQUID_FLEE_MAX_DISTANCE = 10.0F;
/*     */   
/*     */   private int fleeTicks;
/*     */   
/*     */   public boolean canUse() {
/* 288 */     LivingEntity entity = Squid.this.getLastHurtByMob();
/* 289 */     if (Squid.this.isInWater() && entity != null)
/* 290 */       return (Squid.this.distanceToSqr((Entity)entity) < 100.0D); 
/* 293 */     return false;
/*     */   }
/*     */   
/*     */   public void start() {
/* 298 */     this.fleeTicks = 0;
/*     */   }
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 303 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 308 */     this.fleeTicks++;
/* 310 */     LivingEntity lastHurtByMob = Squid.this.getLastHurtByMob();
/* 311 */     if (lastHurtByMob == null)
/*     */       return; 
/* 315 */     Vec3 fleeTo = new Vec3(Squid.this.getX() - lastHurtByMob.getX(), Squid.this.getY() - lastHurtByMob.getY(), Squid.this.getZ() - lastHurtByMob.getZ());
/* 317 */     BlockState blockState = Squid.this.level().getBlockState(BlockPos.containing(Squid.this.getX() + fleeTo.x, Squid.this.getY() + fleeTo.y, Squid.this.getZ() + fleeTo.z));
/* 318 */     FluidState fluidState = Squid.this.level().getFluidState(BlockPos.containing(Squid.this.getX() + fleeTo.x, Squid.this.getY() + fleeTo.y, Squid.this.getZ() + fleeTo.z));
/* 319 */     if (fluidState.is(FluidTags.WATER) || blockState.isAir()) {
/* 320 */       double length = fleeTo.length();
/* 321 */       if (length > 0.0D) {
/* 322 */         fleeTo.normalize();
/* 324 */         double avoidSpeed = 3.0D;
/* 325 */         if (length > 5.0D)
/* 326 */           avoidSpeed -= (length - 5.0D) / 5.0D; 
/* 329 */         if (avoidSpeed > 0.0D)
/* 330 */           fleeTo = fleeTo.scale(avoidSpeed); 
/*     */       } 
/* 334 */       if (blockState.isAir())
/* 335 */         fleeTo = fleeTo.subtract(0.0D, fleeTo.y, 0.0D); 
/* 338 */       Squid.this.movementVector = new Vec3(fleeTo.x / 20.0D, fleeTo.y / 20.0D, fleeTo.z / 20.0D);
/*     */     } 
/* 345 */     if (this.fleeTicks % 10 == 5)
/* 346 */       Squid.this.level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, Squid.this.getX(), Squid.this.getY(), Squid.this.getZ(), 0.0D, 0.0D, 0.0D); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\squid\Squid$SquidFleeGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */