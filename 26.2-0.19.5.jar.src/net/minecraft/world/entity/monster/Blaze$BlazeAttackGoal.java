/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class BlazeAttackGoal extends Goal {
/*     */   private final Blaze blaze;
/*     */   
/*     */   private int attackStep;
/*     */   
/*     */   private int attackTime;
/*     */   
/*     */   private int lastSeen;
/*     */   
/*     */   public BlazeAttackGoal(Blaze blaze) {
/* 163 */     this.blaze = blaze;
/* 165 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 170 */     LivingEntity target = this.blaze.getTarget();
/* 171 */     return (target != null && target.isAlive() && this.blaze.canAttack(target));
/*     */   }
/*     */   
/*     */   public void start() {
/* 176 */     this.attackStep = 0;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 181 */     this.blaze.setCharged(false);
/* 182 */     this.lastSeen = 0;
/*     */   }
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 187 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 192 */     this.attackTime--;
/* 194 */     LivingEntity target = this.blaze.getTarget();
/* 196 */     if (target == null)
/*     */       return; 
/* 200 */     boolean hasLineOfSight = this.blaze.getSensing().hasLineOfSight((Entity)target);
/* 202 */     if (hasLineOfSight) {
/* 203 */       this.lastSeen = 0;
/*     */     } else {
/* 205 */       this.lastSeen++;
/*     */     } 
/* 208 */     double distance = this.blaze.distanceToSqr((Entity)target);
/* 210 */     if (distance < 4.0D) {
/* 211 */       if (!hasLineOfSight)
/*     */         return; 
/* 215 */       if (this.attackTime <= 0) {
/* 216 */         this.attackTime = 20;
/* 217 */         this.blaze.doHurtTarget(getServerLevel((Entity)this.blaze), (Entity)target);
/*     */       } 
/* 219 */       this.blaze.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0D);
/* 220 */     } else if (distance < getFollowDistance() * getFollowDistance() && hasLineOfSight) {
/* 221 */       double xd = target.getX() - this.blaze.getX();
/* 222 */       double yd = target.getY(0.5D) - this.blaze.getY(0.5D);
/* 223 */       double zd = target.getZ() - this.blaze.getZ();
/* 225 */       if (this.attackTime <= 0) {
/* 226 */         this.attackStep++;
/* 227 */         if (this.attackStep == 1) {
/* 228 */           this.attackTime = 60;
/* 229 */           this.blaze.setCharged(true);
/* 230 */         } else if (this.attackStep <= 4) {
/* 231 */           this.attackTime = 6;
/*     */         } else {
/* 233 */           this.attackTime = 100;
/* 234 */           this.attackStep = 0;
/* 235 */           this.blaze.setCharged(false);
/*     */         } 
/* 238 */         if (this.attackStep > 1) {
/* 239 */           double sqd = Math.sqrt(Math.sqrt(distance)) * 0.5D;
/* 241 */           if (!this.blaze.isSilent())
/* 242 */             this.blaze.level().levelEvent(null, 1018, this.blaze.blockPosition(), 0); 
/* 244 */           for (int i = 0; i < 1; i++) {
/* 245 */             Vec3 direction = new Vec3(this.blaze.getRandom().triangle(xd, 2.297D * sqd), yd, this.blaze.getRandom().triangle(zd, 2.297D * sqd));
/* 246 */             SmallFireball entity = new SmallFireball(this.blaze.level(), (LivingEntity)this.blaze, direction.normalize());
/* 247 */             entity.setPos(entity.getX(), this.blaze.getY(0.5D) + 0.5D, entity.getZ());
/* 248 */             this.blaze.level().addFreshEntity((Entity)entity);
/*     */           } 
/*     */         } 
/*     */       } 
/* 252 */       this.blaze.getLookControl().setLookAt((Entity)target, 10.0F, 10.0F);
/* 254 */     } else if (this.lastSeen < 5) {
/* 255 */       this.blaze.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0D);
/*     */     } 
/* 259 */     super.tick();
/*     */   }
/*     */   
/*     */   private double getFollowDistance() {
/* 263 */     return this.blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Blaze$BlazeAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */