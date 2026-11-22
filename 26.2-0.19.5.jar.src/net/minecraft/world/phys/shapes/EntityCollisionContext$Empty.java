/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class Empty extends EntityCollisionContext {
/* 88 */   protected static final CollisionContext WITHOUT_FLUID_COLLISIONS = new Empty(false);
/*    */   
/* 89 */   protected static final CollisionContext WITH_FLUID_COLLISIONS = new Empty(true);
/*    */   
/*    */   public Empty(boolean alwaysCollideWithFluid) {
/* 92 */     super(false, false, -1.7976931348623157E308D, ItemStack.EMPTY, alwaysCollideWithFluid, null);
/*    */   }
/*    */   
/*    */   public boolean isAbove(VoxelShape shape, BlockPos pos, boolean defaultValue) {
/* 97 */     return defaultValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\EntityCollisionContext$Empty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */