/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.block.BaseRailBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.RailShape;
/*    */ 
/*    */ public class MinecartCollisionContext extends EntityCollisionContext {
/*    */   private BlockPos ingoreBelow;
/*    */   
/*    */   private BlockPos slopeIgnore;
/*    */   
/*    */   protected MinecartCollisionContext(AbstractMinecart entity, boolean alwaysStandOnFluid) {
/* 16 */     super((Entity)entity, alwaysStandOnFluid, false);
/* 17 */     setupContext(entity);
/*    */   }
/*    */   
/*    */   private void setupContext(AbstractMinecart entity) {
/* 21 */     BlockPos currentRailPos = entity.getCurrentBlockPosOrRailBelow();
/* 22 */     BlockState currentState = entity.level().getBlockState(currentRailPos);
/* 23 */     boolean onRails = BaseRailBlock.isRail(currentState);
/* 24 */     if (onRails) {
/* 25 */       this.ingoreBelow = currentRailPos.below();
/* 26 */       RailShape shape = (RailShape)currentState.getValue(((BaseRailBlock)currentState.getBlock()).getShapeProperty());
/* 27 */       if (shape.isSlope()) {
/* 28 */         switch (shape) {
/*    */           case ASCENDING_EAST:
/*    */           
/*    */           case ASCENDING_WEST:
/*    */           
/*    */           case ASCENDING_NORTH:
/*    */           
/*    */           case ASCENDING_SOUTH:
/*    */           
/*    */           default:
/*    */             break;
/*    */         } 
/* 28 */         this.slopeIgnore = 
/*    */           
/* 33 */           null;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public VoxelShape getCollisionShape(BlockState state, CollisionGetter collisionGetter, BlockPos pos) {
/* 41 */     if (pos.equals(this.ingoreBelow) || pos.equals(this.slopeIgnore))
/* 42 */       return Shapes.empty(); 
/* 44 */     return super.getCollisionShape(state, collisionGetter, pos);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\MinecartCollisionContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */