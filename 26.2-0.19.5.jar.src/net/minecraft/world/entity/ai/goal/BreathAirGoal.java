/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.MoverType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BreathAirGoal extends Goal {
/*    */   private final PathfinderMob mob;
/*    */   
/*    */   public BreathAirGoal(PathfinderMob mob) {
/* 19 */     this.mob = mob;
/* 20 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */   
/*    */   public boolean canUse() {
/* 25 */     return (this.mob.getAirSupply() < 140);
/*    */   }
/*    */   
/*    */   public boolean canContinueToUse() {
/* 30 */     return canUse();
/*    */   }
/*    */   
/*    */   public boolean isInterruptable() {
/* 35 */     return false;
/*    */   }
/*    */   
/*    */   public void start() {
/* 40 */     this.mob.getNavigation().stop();
/* 41 */     findAirPosition();
/*    */   }
/*    */   
/*    */   private void findAirPosition() {
/* 45 */     Iterable<BlockPos> between = BlockPos.neighborColumn(this.mob
/* 46 */         .getBlockX(), this.mob
/* 47 */         .getBlockY(), this.mob
/* 48 */         .getBlockZ(), this.mob
/* 49 */         .getBlockY() + 8);
/* 52 */     BlockPos destinationPos = null;
/* 53 */     for (BlockPos pos : between) {
/* 54 */       if (givesAir((LevelReader)this.mob.level(), pos)) {
/* 55 */         destinationPos = pos;
/*    */         break;
/*    */       } 
/*    */     } 
/* 60 */     if (destinationPos == null)
/* 61 */       destinationPos = BlockPos.containing(this.mob.getX(), this.mob.getY() + 8.0D, this.mob.getZ()); 
/* 64 */     this.mob.getNavigation().moveTo(destinationPos.getX(), (destinationPos.getY() + 1), destinationPos.getZ(), 1.0D);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 69 */     findAirPosition();
/* 71 */     this.mob.moveRelative(0.02F, new Vec3(this.mob.xxa, this.mob.yya, this.mob.zza));
/* 72 */     this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
/*    */   }
/*    */   
/*    */   private boolean givesAir(LevelReader level, BlockPos pos) {
/* 76 */     BlockState state = level.getBlockState(pos);
/* 77 */     return ((level.getFluidState(pos).isEmpty() || state.is(Blocks.BUBBLE_COLUMN)) && state.isPathfindable(PathComputationType.LAND));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\BreathAirGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */