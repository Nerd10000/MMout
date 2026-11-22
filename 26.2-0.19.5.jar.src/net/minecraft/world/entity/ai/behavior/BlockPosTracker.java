/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BlockPosTracker implements PositionTracker {
/*    */   private final BlockPos blockPos;
/*    */   
/*    */   private final Vec3 centerPosition;
/*    */   
/*    */   public BlockPosTracker(BlockPos blockPos) {
/* 12 */     this.blockPos = blockPos.immutable();
/* 13 */     this.centerPosition = Vec3.atCenterOf((Vec3i)blockPos);
/*    */   }
/*    */   
/*    */   public BlockPosTracker(Vec3 vec) {
/* 17 */     this.blockPos = BlockPos.containing((Position)vec);
/* 18 */     this.centerPosition = vec;
/*    */   }
/*    */   
/*    */   public Vec3 currentPosition() {
/* 23 */     return this.centerPosition;
/*    */   }
/*    */   
/*    */   public BlockPos currentBlockPosition() {
/* 28 */     return this.blockPos;
/*    */   }
/*    */   
/*    */   public boolean isVisibleBy(LivingEntity body) {
/* 33 */     return true;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 38 */     return "BlockPosTracker{blockPos=" + String.valueOf(this.blockPos) + ", centerPosition=" + String.valueOf(this.centerPosition) + "}";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\BlockPosTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */