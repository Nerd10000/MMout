/*     */ package net.minecraft.world.entity.monster.creaking;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ 
/*     */ class CreakingMoveControl<T extends Creaking> extends MoveControl<T> {
/*     */   public CreakingMoveControl(T creaking) {
/* 542 */     super((Mob)creaking);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 547 */     if (((Creaking)this.mob).canMove())
/* 548 */       super.tick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\creaking\Creaking$CreakingMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */