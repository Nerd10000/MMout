/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class PositionCollisionContext implements CollisionContext {
/*    */   private final double y;
/*    */   
/*    */   public PositionCollisionContext(double y) {
/* 15 */     this.y = y;
/*    */   }
/*    */   
/*    */   public boolean isDescending() {
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isAbove(VoxelShape shape, BlockPos pos, boolean defaultValue) {
/* 25 */     return (this.y > pos.getY() + shape.max(Direction.Axis.Y) - 9.999999747378752E-6D);
/*    */   }
/*    */   
/*    */   public boolean isHoldingItem(Item item) {
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   public boolean alwaysCollideWithFluid() {
/* 35 */     return false;
/*    */   }
/*    */   
/*    */   public boolean canStandOnFluid(FluidState fluidStateAbove, FluidState fluid) {
/* 40 */     return false;
/*    */   }
/*    */   
/*    */   public VoxelShape getCollisionShape(BlockState state, CollisionGetter collisionGetter, BlockPos pos) {
/* 45 */     return state.getCollisionShape((BlockGetter)collisionGetter, pos, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\PositionCollisionContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */