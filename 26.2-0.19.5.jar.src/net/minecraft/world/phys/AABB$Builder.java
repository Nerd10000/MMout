/*     */ package net.minecraft.world.phys;
/*     */ 
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class Builder {
/* 470 */   private float minX = Float.POSITIVE_INFINITY;
/*     */   
/* 471 */   private float minY = Float.POSITIVE_INFINITY;
/*     */   
/* 472 */   private float minZ = Float.POSITIVE_INFINITY;
/*     */   
/* 474 */   private float maxX = Float.NEGATIVE_INFINITY;
/*     */   
/* 475 */   private float maxY = Float.NEGATIVE_INFINITY;
/*     */   
/* 476 */   private float maxZ = Float.NEGATIVE_INFINITY;
/*     */   
/*     */   private boolean defined;
/*     */   
/*     */   public void include(Vector3fc v) {
/* 481 */     this.minX = Math.min(this.minX, v.x());
/* 482 */     this.minY = Math.min(this.minY, v.y());
/* 483 */     this.minZ = Math.min(this.minZ, v.z());
/* 485 */     this.maxX = Math.max(this.maxX, v.x());
/* 486 */     this.maxY = Math.max(this.maxY, v.y());
/* 487 */     this.maxZ = Math.max(this.maxZ, v.z());
/* 488 */     this.defined = true;
/*     */   }
/*     */   
/*     */   public boolean isDefined() {
/* 492 */     return this.defined;
/*     */   }
/*     */   
/*     */   public AABB build() {
/* 496 */     if (!this.defined)
/* 497 */       throw new IllegalStateException("Cannot build an undefined AABB. Include at least one point."); 
/* 499 */     return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\AABB$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */