/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ class InterpolationData {
/*    */   protected int steps;
/*    */   
/*    */   private Vec3 position;
/*    */   
/*    */   private float yRot;
/*    */   
/*    */   private float xRot;
/*    */   
/*    */   private InterpolationData(int steps, Vec3 position, float yRot, float xRot) {
/* 22 */     this.steps = steps;
/* 23 */     this.position = position;
/* 24 */     this.yRot = yRot;
/* 25 */     this.xRot = xRot;
/*    */   }
/*    */   
/*    */   public void decrease() {
/* 29 */     this.steps--;
/*    */   }
/*    */   
/*    */   public void addDelta(Vec3 delta) {
/* 33 */     this.position = this.position.add(delta);
/*    */   }
/*    */   
/*    */   public void addRotation(float yRot, float xRot) {
/* 37 */     this.yRot += yRot;
/* 38 */     this.xRot += xRot;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\InterpolationHandler$InterpolationData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */