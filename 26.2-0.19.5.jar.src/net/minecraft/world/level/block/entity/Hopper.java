/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.world.Container;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public interface Hopper extends Container {
/*  8 */   public static final AABB SUCK_AABB = Block.column(16.0D, 11.0D, 32.0D).toAabbs().get(0);
/*    */   
/*    */   default AABB getSuckAabb() {
/* 11 */     return SUCK_AABB;
/*    */   }
/*    */   
/*    */   double getLevelX();
/*    */   
/*    */   double getLevelY();
/*    */   
/*    */   double getLevelZ();
/*    */   
/*    */   boolean isGridAligned();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\Hopper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */