/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ 
/*     */ class null extends BodyRotationControl {
/*     */   null(Mob mob) {
/* 415 */     super(mob);
/*     */   }
/*     */   
/*     */   public void clientTick() {
/* 418 */     if (!Armadillo.this.isScared())
/* 419 */       super.clientTick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\armadillo\Armadillo$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */