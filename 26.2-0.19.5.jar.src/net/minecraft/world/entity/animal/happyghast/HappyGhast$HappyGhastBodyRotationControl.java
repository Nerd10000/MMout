/*     */ package net.minecraft.world.entity.animal.happyghast;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ 
/*     */ class HappyGhastBodyRotationControl extends BodyRotationControl {
/*     */   public HappyGhastBodyRotationControl() {
/* 670 */     super((Mob)paramHappyGhast);
/*     */   }
/*     */   
/*     */   public void clientTick() {
/* 675 */     if (HappyGhast.this.isVehicle()) {
/* 676 */       HappyGhast.this.yHeadRot = HappyGhast.this.getYRot();
/* 677 */       HappyGhast.this.yBodyRot = HappyGhast.this.yHeadRot;
/*     */     } 
/* 679 */     super.clientTick();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\happyghast\HappyGhast$HappyGhastBodyRotationControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */