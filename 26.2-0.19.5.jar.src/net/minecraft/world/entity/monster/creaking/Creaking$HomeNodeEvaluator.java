/*     */ package net.minecraft.world.entity.monster.creaking;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.PathfindingContext;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ 
/*     */ class HomeNodeEvaluator extends WalkNodeEvaluator {
/*     */   private static final int MAX_DISTANCE_TO_HOME_SQ = 1024;
/*     */   
/*     */   public PathType getPathType(PathfindingContext context, int x, int y, int z) {
/* 587 */     BlockPos homePos = Creaking.this.getHomePos();
/* 589 */     if (homePos == null)
/* 590 */       return super.getPathType(context, x, y, z); 
/* 593 */     double homeDistance = homePos.distSqr(new Vec3i(x, y, z));
/* 594 */     if (homeDistance > 1024.0D && homeDistance >= homePos.distSqr((Vec3i)context.mobPosition()))
/* 595 */       return PathType.BLOCKED; 
/* 597 */     return super.getPathType(context, x, y, z);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\creaking\Creaking$HomeNodeEvaluator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */