/*     */ package net.minecraft.world.entity.animal.axolotl;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ 
/*     */ class AxolotlMoveControl<T extends Axolotl> extends SmoothSwimmingMoveControl<T> {
/*     */   public AxolotlMoveControl(T axolotl) {
/* 675 */     super((Mob)axolotl, 85, 10, 0.1F, 0.5F, false);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 680 */     if (!((Axolotl)this.mob).isPlayingDead())
/* 681 */       super.tick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\axolotl\Axolotl$AxolotlMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */