/*     */ package net.minecraft.world.entity.monster.creaking;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ 
/*     */ class CreakingBodyRotationControl extends BodyRotationControl {
/*     */   public CreakingBodyRotationControl(Creaking creaking) {
/* 570 */     super((Mob)creaking);
/*     */   }
/*     */   
/*     */   public void clientTick() {
/* 575 */     if (Creaking.this.canMove())
/* 576 */       super.clientTick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\creaking\Creaking$CreakingBodyRotationControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */