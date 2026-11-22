/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class Tracker {
/*     */   private double height;
/*     */   
/*     */   private boolean eyesInside;
/*     */   
/* 170 */   private Vec3 accumulatedCurrent = Vec3.ZERO;
/*     */   
/*     */   private int currentCount;
/*     */   
/*     */   public void reset() {
/* 174 */     this.height = 0.0D;
/* 175 */     this.eyesInside = false;
/* 176 */     this.accumulatedCurrent = Vec3.ZERO;
/* 177 */     this.currentCount = 0;
/*     */   }
/*     */   
/*     */   public void accumulateCurrent(Vec3 flow) {
/* 181 */     this.accumulatedCurrent = this.accumulatedCurrent.add(flow);
/* 182 */     this.currentCount++;
/*     */   }
/*     */   
/*     */   public void applyCurrentTo(Entity entity, double scale) {
/* 186 */     if (this.currentCount == 0 || this.accumulatedCurrent.lengthSqr() < 9.999999747378752E-6D)
/*     */       return; 
/* 191 */     if (!(entity instanceof net.minecraft.world.entity.player.Player)) {
/* 192 */       impulse = this.accumulatedCurrent.normalize();
/*     */     } else {
/* 194 */       impulse = this.accumulatedCurrent.scale(1.0D / this.currentCount);
/*     */     } 
/* 197 */     Vec3 oldMovement = entity.getDeltaMovement();
/* 198 */     Vec3 impulse = impulse.scale(scale);
/* 201 */     double min = 0.003D;
/* 202 */     if (Math.abs(oldMovement.x) < 0.003D && Math.abs(oldMovement.z) < 0.003D && impulse.length() < 0.0045000000000000005D)
/* 203 */       impulse = impulse.normalize().scale(0.0045000000000000005D); 
/* 206 */     entity.addDeltaMovement(impulse);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityFluidInteraction$Tracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */