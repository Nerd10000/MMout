/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ public class WallClimberNavigation extends GroundPathNavigation {
/*    */   private BlockPos pathToPosition;
/*    */   
/*    */   public WallClimberNavigation(Mob mob, Level level) {
/* 20 */     super(mob, level);
/*    */   }
/*    */   
/*    */   public Path createPath(BlockPos pos, int reachRange) {
/* 25 */     this.pathToPosition = pos;
/* 26 */     return super.createPath(pos, reachRange);
/*    */   }
/*    */   
/*    */   public Path createPath(Entity target, int reachRange) {
/* 31 */     this.pathToPosition = target.blockPosition();
/* 32 */     return super.createPath(target, reachRange);
/*    */   }
/*    */   
/*    */   public boolean moveTo(Entity target, double speedModifier) {
/* 37 */     Path newPath = createPath(target, 0);
/* 38 */     if (newPath != null)
/* 39 */       return moveTo(newPath, speedModifier); 
/* 41 */     this.pathToPosition = target.blockPosition();
/* 42 */     this.speedModifier = speedModifier;
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 49 */     if (isDone()) {
/* 50 */       if (this.pathToPosition != null)
/* 52 */         if (this.pathToPosition.closerToCenterThan((Position)this.mob.position(), this.mob.getBbWidth()) || (this.mob.getY() > this.pathToPosition.getY() && BlockPos.containing(this.pathToPosition.getX(), this.mob.getY(), this.pathToPosition.getZ()).closerToCenterThan((Position)this.mob.position(), this.mob.getBbWidth()))) {
/* 53 */           this.pathToPosition = null;
/*    */         } else {
/* 55 */           this.mob.getMoveControl().setWantedPosition(this.pathToPosition.getX(), this.pathToPosition.getY(), this.pathToPosition.getZ(), this.speedModifier);
/*    */         }  
/*    */       return;
/*    */     } 
/* 60 */     super.tick();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\navigation\WallClimberNavigation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */