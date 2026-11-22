/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ public class SulfurCubeMobMoveControl<T extends SulfurCube> extends AbstractCubeMob.CubeMobMoveControl<T> {
/*     */   public SulfurCubeMobMoveControl(T cubeMob) {
/* 801 */     super(cubeMob);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 806 */     if (!((SulfurCube)this.mob).hasBodyItem())
/* 807 */       super.tick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\SulfurCube$SulfurCubeMobMoveControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */