/*     */ package net.minecraft.world.entity.animal.axolotl;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ 
/*     */ class AxolotlLookControl extends SmoothSwimmingLookControl {
/*     */   public AxolotlLookControl(Axolotl axolotl, int maxYRotFromCenter) {
/* 688 */     super((Mob)axolotl, maxYRotFromCenter);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 693 */     if (!Axolotl.this.isPlayingDead())
/* 694 */       super.tick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\axolotl\Axolotl$AxolotlLookControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */