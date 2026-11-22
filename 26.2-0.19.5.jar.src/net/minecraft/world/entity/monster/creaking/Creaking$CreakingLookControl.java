/*     */ package net.minecraft.world.entity.monster.creaking;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ 
/*     */ class CreakingLookControl extends LookControl {
/*     */   public CreakingLookControl(Creaking creaking) {
/* 529 */     super((Mob)creaking);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 534 */     if (Creaking.this.canMove())
/* 535 */       super.tick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\creaking\Creaking$CreakingLookControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */