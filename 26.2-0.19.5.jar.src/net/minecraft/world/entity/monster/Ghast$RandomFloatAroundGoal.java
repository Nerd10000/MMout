/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class RandomFloatAroundGoal extends Goal {
/*     */   private static final int MAX_ATTEMPTS = 64;
/*     */   
/*     */   private final Mob ghast;
/*     */   
/*     */   private final int distanceToBlocks;
/*     */   
/*     */   public RandomFloatAroundGoal(Mob ghast) {
/* 298 */     this(ghast, 0);
/*     */   }
/*     */   
/*     */   public RandomFloatAroundGoal(Mob ghast, int distanceToBlocks) {
/* 302 */     this.ghast = ghast;
/* 303 */     this.distanceToBlocks = distanceToBlocks;
/* 304 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 309 */     MoveControl moveControl = this.ghast.getMoveControl();
/* 310 */     if (!moveControl.hasWanted())
/* 311 */       return true; 
/* 314 */     double xd = moveControl.getWantedX() - this.ghast.getX();
/* 315 */     double yd = moveControl.getWantedY() - this.ghast.getY();
/* 316 */     double zd = moveControl.getWantedZ() - this.ghast.getZ();
/* 318 */     double dd = xd * xd + yd * yd + zd * zd;
/* 320 */     return (dd < 1.0D || dd > 3600.0D);
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 325 */     return false;
/*     */   }
/*     */   
/*     */   public void start() {
/* 330 */     Vec3 result = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
/* 331 */     this.ghast.getMoveControl().setWantedPosition(result.x(), result.y(), result.z(), 1.0D);
/*     */   }
/*     */   
/*     */   public static Vec3 getSuitableFlyToPosition(Mob mob, int distanceToBlocks) {
/* 335 */     Level level = mob.level();
/* 336 */     RandomSource random = mob.getRandom();
/* 337 */     Vec3 center = mob.position();
/* 338 */     Vec3 result = null;
/* 339 */     for (int i = 0; i < 64; i++) {
/* 340 */       result = chooseRandomPositionWithRestriction(mob, center, random);
/* 341 */       if (result != null && isGoodTarget(level, result, distanceToBlocks))
/* 342 */         return result; 
/*     */     } 
/* 345 */     if (result == null)
/* 346 */       result = chooseRandomPosition(center, random); 
/* 350 */     BlockPos pos = BlockPos.containing((Position)result);
/* 351 */     int heightY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ());
/* 352 */     if (heightY < pos.getY() && heightY > level.getMinY())
/* 353 */       result = new Vec3(result.x(), mob.getY() - Math.abs(mob.getY() - result.y()), result.z()); 
/* 355 */     return result;
/*     */   }
/*     */   
/*     */   private static boolean isGoodTarget(Level level, Vec3 target, int distanceToBlocks) {
/* 359 */     if (distanceToBlocks <= 0)
/* 360 */       return true; 
/* 362 */     BlockPos pos = BlockPos.containing((Position)target);
/* 363 */     if (!level.getBlockState(pos).isAir())
/* 364 */       return false; 
/* 367 */     for (Direction dir : Direction.values()) {
/* 368 */       for (int i = 1; i < distanceToBlocks; i++) {
/* 369 */         BlockPos offset = pos.relative(dir, i);
/* 370 */         if (!level.getBlockState(offset).isAir())
/* 371 */           return true; 
/*     */       } 
/*     */     } 
/* 375 */     return false;
/*     */   }
/*     */   
/*     */   private static Vec3 chooseRandomPosition(Vec3 center, RandomSource random) {
/* 379 */     double xTarget = center.x() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 380 */     double yTarget = center.y() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 381 */     double zTarget = center.z() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 382 */     return new Vec3(xTarget, yTarget, zTarget);
/*     */   }
/*     */   
/*     */   private static Vec3 chooseRandomPositionWithRestriction(Mob mob, Vec3 center, RandomSource random) {
/* 386 */     Vec3 target = chooseRandomPosition(center, random);
/* 387 */     if (mob.hasHome() && !mob.isWithinHome(target))
/* 388 */       return null; 
/* 390 */     return target;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Ghast$RandomFloatAroundGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */