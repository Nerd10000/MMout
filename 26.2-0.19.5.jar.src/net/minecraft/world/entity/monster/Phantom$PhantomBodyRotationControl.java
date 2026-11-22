/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ 
/*     */ class PhantomBodyRotationControl extends BodyRotationControl {
/*     */   public PhantomBodyRotationControl(Mob mob) {
/* 281 */     super(mob);
/*     */   }
/*     */   
/*     */   public void clientTick() {
/* 286 */     Phantom.this.yHeadRot = Phantom.this.yBodyRot;
/* 287 */     Phantom.this.yBodyRot = Phantom.this.getYRot();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Phantom$PhantomBodyRotationControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */