/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.NonNullList;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class Containers {
/*    */   public static void dropContents(Level level, BlockPos pos, Container container) {
/* 15 */     dropContents(level, pos.getX(), pos.getY(), pos.getZ(), container);
/*    */   }
/*    */   
/*    */   public static void dropContents(Level level, Entity entity, Container container) {
/* 19 */     dropContents(level, entity.getX(), entity.getY(), entity.getZ(), container);
/*    */   }
/*    */   
/*    */   private static void dropContents(Level level, double x, double y, double z, Container container) {
/* 23 */     for (int i = 0; i < container.getContainerSize(); i++)
/* 24 */       dropItemStack(level, x, y, z, container.getItem(i)); 
/*    */   }
/*    */   
/*    */   public static void dropContents(Level level, BlockPos pos, NonNullList<ItemStack> list) {
/* 29 */     list.forEach(itemStack -> dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), itemStack));
/*    */   }
/*    */   
/*    */   public static void dropItemStack(Level level, double x, double y, double z, ItemStack itemStack) {
/* 33 */     double size = EntityTypes.ITEM.getWidth();
/* 34 */     double centerRange = 1.0D - size;
/* 35 */     double halfSize = size / 2.0D;
/* 36 */     RandomSource random = level.getRandom();
/* 37 */     double xo = Math.floor(x) + random.nextDouble() * centerRange + halfSize;
/* 38 */     double yo = Math.floor(y) + random.nextDouble() * centerRange;
/* 39 */     double zo = Math.floor(z) + random.nextDouble() * centerRange + halfSize;
/* 41 */     while (!itemStack.isEmpty()) {
/* 42 */       ItemEntity entity = new ItemEntity(level, xo, yo, zo, itemStack.split(random.nextInt(21) + 10));
/* 44 */       float pow = 0.05F;
/* 45 */       entity.setDeltaMovement(random
/* 46 */           .triangle(0.0D, 0.11485000171139836D), random
/* 47 */           .triangle(0.2D, 0.11485000171139836D), random
/* 48 */           .triangle(0.0D, 0.11485000171139836D));
/* 51 */       level.addFreshEntity((Entity)entity);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void updateNeighboursAfterDestroy(BlockState state, Level level, BlockPos pos) {
/* 56 */     level.updateNeighbourForOutputSignal(pos, state.getBlock());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\Containers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */