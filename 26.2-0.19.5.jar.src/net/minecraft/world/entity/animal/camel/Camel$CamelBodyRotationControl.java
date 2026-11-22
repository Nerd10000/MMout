/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ 
/*     */ class CamelBodyRotationControl extends BodyRotationControl {
/*     */   public CamelBodyRotationControl(Camel camel) {
/* 695 */     super((Mob)camel);
/*     */   }
/*     */   
/*     */   public void clientTick() {
/* 700 */     if (!Camel.this.refuseToMove())
/* 701 */       super.clientTick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\camel\Camel$CamelBodyRotationControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */