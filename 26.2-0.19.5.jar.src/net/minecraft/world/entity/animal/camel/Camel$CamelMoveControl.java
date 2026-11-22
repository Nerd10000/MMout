/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ 
/*     */ class CamelMoveControl<T extends Camel> extends MoveControl<T> {
/*     */   public CamelMoveControl(T camel) {
/* 721 */     super((Mob)camel);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 726 */     if (this.operation == MoveControl.Operation.MOVE_TO && !((Camel)this.mob).isLeashed() && ((Camel)this.mob).isCamelSitting() && !((Camel)this.mob).isInPoseTransition() && ((Camel)this.mob).canCamelChangePose())
/* 727 */       ((Camel)this.mob).standUp(); 
/* 729 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\camel\Camel$CamelMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */