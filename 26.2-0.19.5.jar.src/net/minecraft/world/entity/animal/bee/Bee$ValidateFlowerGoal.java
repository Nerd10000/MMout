/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.util.Mth;
/*      */ 
/*      */ class ValidateFlowerGoal extends Bee.BaseBeeGoal {
/*      */   private final int validateFlowerCooldown;
/*      */   
/*      */   private long lastValidateTick;
/*      */   
/*      */   private ValidateFlowerGoal() {
/* 1474 */     this.validateFlowerCooldown = Mth.nextInt(Bee.access$3000(Bee.this), 20, 40);
/* 1476 */     this.lastValidateTick = -1L;
/*      */   }
/*      */   
/*      */   public void start() {
/* 1481 */     if (Bee.this.savedFlowerPos != null && Bee.this.level().isLoaded(Bee.this.savedFlowerPos) && !isFlower(Bee.this.savedFlowerPos))
/* 1482 */       Bee.this.dropFlower(); 
/* 1484 */     this.lastValidateTick = Bee.this.level().getGameTime();
/*      */   }
/*      */   
/*      */   public boolean canBeeUse() {
/* 1489 */     return (Bee.this.level().getGameTime() > this.lastValidateTick + this.validateFlowerCooldown);
/*      */   }
/*      */   
/*      */   public boolean canBeeContinueToUse() {
/* 1494 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isFlower(BlockPos flowerPos) {
/* 1498 */     return Bee.attractsBees(Bee.this.level().getBlockState(flowerPos));
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$ValidateFlowerGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */