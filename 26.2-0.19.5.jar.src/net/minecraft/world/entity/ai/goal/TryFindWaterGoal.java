/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ 
/*    */ public class TryFindWaterGoal extends Goal {
/*    */   private final PathfinderMob mob;
/*    */   
/*    */   public TryFindWaterGoal(PathfinderMob mob) {
/* 12 */     this.mob = mob;
/*    */   }
/*    */   
/*    */   public boolean canUse() {
/* 17 */     return (this.mob.onGround() && !this.mob.level().getFluidState(this.mob.blockPosition()).is(FluidTags.WATER));
/*    */   }
/*    */   
/*    */   public void start() {
/* 22 */     BlockPos waterPos = null;
/* 24 */     Iterable<BlockPos> between = BlockPos.betweenClosed(
/* 25 */         Mth.floor(this.mob.getX() - 2.0D), 
/* 26 */         Mth.floor(this.mob.getY() - 2.0D), 
/* 27 */         Mth.floor(this.mob.getZ() - 2.0D), 
/* 28 */         Mth.floor(this.mob.getX() + 2.0D), this.mob
/* 29 */         .getBlockY(), 
/* 30 */         Mth.floor(this.mob.getZ() + 2.0D));
/* 33 */     for (BlockPos pos : between) {
/* 34 */       if (this.mob.level().getFluidState(pos).is(FluidTags.WATER)) {
/* 35 */         waterPos = pos;
/*    */         break;
/*    */       } 
/*    */     } 
/* 40 */     if (waterPos != null)
/* 41 */       this.mob.getMoveControl().setWantedPosition(waterPos.getX(), waterPos.getY(), waterPos.getZ(), 1.0D); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\goal\TryFindWaterGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */