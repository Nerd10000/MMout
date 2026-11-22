/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ 
/*     */ class FrogLookControl extends LookControl {
/*     */   public FrogLookControl(Mob mob) {
/* 293 */     super(mob);
/*     */   }
/*     */   
/*     */   protected boolean resetXRotOnTick() {
/* 298 */     return Frog.this.getTongueTarget().isEmpty();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\frog\Frog$FrogLookControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */