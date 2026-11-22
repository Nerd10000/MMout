/*    */ package net.minecraft.world.phys;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public abstract class HitResult {
/*    */   protected final Vec3 location;
/*    */   
/*    */   public enum Type {
/*  7 */     MISS, BLOCK, ENTITY;
/*    */   }
/*    */   
/*    */   protected HitResult(Vec3 location) {
/* 13 */     this.location = location;
/*    */   }
/*    */   
/*    */   public double distanceTo(Entity entity) {
/* 17 */     double xd = this.location.x - entity.getX();
/* 18 */     double yd = this.location.y - entity.getY();
/* 19 */     double zd = this.location.z - entity.getZ();
/* 20 */     return xd * xd + yd * yd + zd * zd;
/*    */   }
/*    */   
/*    */   public abstract Type getType();
/*    */   
/*    */   public Vec3 getLocation() {
/* 26 */     return this.location;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\HitResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */