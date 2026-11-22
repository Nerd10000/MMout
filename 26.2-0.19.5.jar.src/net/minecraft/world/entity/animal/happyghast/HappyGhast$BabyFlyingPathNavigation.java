/*     */ package net.minecraft.world.entity.animal.happyghast;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class BabyFlyingPathNavigation extends FlyingPathNavigation {
/*     */   public BabyFlyingPathNavigation(HappyGhast mob, Level level) {
/* 587 */     super((Mob)mob, level);
/* 588 */     setCanOpenDoors(false);
/* 589 */     setCanFloat(true);
/* 590 */     setRequiredPathLength(48.0F);
/*     */   }
/*     */   
/*     */   protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) {
/* 595 */     return isClearForMovementBetween(this.mob, startPos, stopPos, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\happyghast\HappyGhast$BabyFlyingPathNavigation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */