/*     */ package net.minecraft.world.entity.animal.panda;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ 
/*     */ class PandaMoveControl<T extends Panda> extends MoveControl<T> {
/*     */   public PandaMoveControl(T mob) {
/* 811 */     super((Mob)mob);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 816 */     if (!((Panda)this.mob).canPerformAction())
/*     */       return; 
/* 820 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */