/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ 
/*     */ class SulfurCubeSearchForItemsGoal extends Goal {
/*     */   private final SulfurCube sulfurCube;
/*     */   
/*     */   private ItemEntity targetItem;
/*     */   
/*     */   public SulfurCubeSearchForItemsGoal(SulfurCube sulfurCube) {
/* 643 */     setFlags(EnumSet.of(Goal.Flag.LOOK));
/* 644 */     this.sulfurCube = sulfurCube;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 649 */     if (this.sulfurCube.isBaby() || this.sulfurCube.pickupTimer > 0)
/* 650 */       return false; 
/* 653 */     this.targetItem = (ItemEntity)getServerLevel((Entity)this.sulfurCube).getNearestEntity(this.sulfurCube.level().getEntitiesOfClass(ItemEntity.class, this.sulfurCube.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), SulfurCube.ALLOWED_ITEMS), this.sulfurCube.getX(), this.sulfurCube.getY(), this.sulfurCube.getZ());
/* 654 */     return (this.targetItem != null);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 659 */     SulfurCube.this.lookAt((Entity)this.targetItem, 10.0F, 10.0F);
/* 660 */     MoveControl moveControl = SulfurCube.this.getMoveControl();
/* 660 */     if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 660 */       AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 661 */       cubeMobMoveControl.setDirection(SulfurCube.this.getYRot(), true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\SulfurCube$SulfurCubeSearchForItemsGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */