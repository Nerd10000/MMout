/*     */ package net.minecraft.world.entity.animal.fox;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ 
/*     */ class FoxMoveControl<T extends Fox> extends MoveControl<T> {
/*     */   public FoxMoveControl(T fox) {
/* 841 */     super((Mob)fox);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 846 */     if (((Fox)this.mob).canMove())
/* 847 */       super.tick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */