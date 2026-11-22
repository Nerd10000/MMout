/*     */ package net.minecraft.client;
/*     */ 
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class NearPlane {
/*     */   private final Vec3 forward;
/*     */   
/*     */   private final Vec3 left;
/*     */   
/*     */   private final Vec3 up;
/*     */   
/*     */   private NearPlane(Vec3 forward, Vec3 left, Vec3 up) {
/* 524 */     this.forward = forward;
/* 525 */     this.left = left;
/* 526 */     this.up = up;
/*     */   }
/*     */   
/*     */   public Vec3 getTopLeft() {
/* 530 */     return this.forward.add(this.up).add(this.left);
/*     */   }
/*     */   
/*     */   public Vec3 getTopRight() {
/* 534 */     return this.forward.add(this.up).subtract(this.left);
/*     */   }
/*     */   
/*     */   public Vec3 getBottomLeft() {
/* 538 */     return this.forward.subtract(this.up).add(this.left);
/*     */   }
/*     */   
/*     */   public Vec3 getBottomRight() {
/* 542 */     return this.forward.subtract(this.up).subtract(this.left);
/*     */   }
/*     */   
/*     */   public Vec3 getPointOnPlane(float x, float y) {
/* 546 */     return this.forward.add(this.up.scale(y)).subtract(this.left.scale(x));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\Camera$NearPlane.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */