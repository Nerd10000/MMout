/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ 
/*     */ class SulfurCubeLookControl extends LookControl {
/*     */   private SulfurCubeLookControl() {
/* 784 */     super((Mob)paramSulfurCube);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 789 */     if (!SulfurCube.this.hasBodyItem()) {
/* 790 */       super.tick();
/*     */       return;
/*     */     } 
/* 793 */     float closeAngle = Mth.wrapDegrees90(SulfurCube.this.getYRot());
/* 794 */     SulfurCube.this.setYRot(SulfurCube.this.getYRot() - closeAngle);
/* 795 */     SulfurCube.this.setYHeadRot(SulfurCube.this.getYRot());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\SulfurCube$SulfurCubeLookControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */