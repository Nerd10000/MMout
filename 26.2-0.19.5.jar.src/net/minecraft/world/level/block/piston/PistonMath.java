/*    */ package net.minecraft.world.level.block.piston;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class PistonMath {
/*    */   public static AABB getMovementArea(AABB aabb, Direction direction, double amount) {
/* 11 */     double delta = amount * direction.getAxisDirection().getStep();
/* 12 */     double min = Math.min(delta, 0.0D);
/* 13 */     double max = Math.max(delta, 0.0D);
/* 14 */     switch (direction) {
/*    */       case WEST:
/* 16 */         return new AABB(aabb.minX + min, aabb.minY, aabb.minZ, aabb.minX + max, aabb.maxY, aabb.maxZ);
/*    */       case EAST:
/* 18 */         return new AABB(aabb.maxX + min, aabb.minY, aabb.minZ, aabb.maxX + max, aabb.maxY, aabb.maxZ);
/*    */       case DOWN:
/* 20 */         return new AABB(aabb.minX, aabb.minY + min, aabb.minZ, aabb.maxX, aabb.minY + max, aabb.maxZ);
/*    */       default:
/* 23 */         return new AABB(aabb.minX, aabb.maxY + min, aabb.minZ, aabb.maxX, aabb.maxY + max, aabb.maxZ);
/*    */       case NORTH:
/* 25 */         return new AABB(aabb.minX, aabb.minY, aabb.minZ + min, aabb.maxX, aabb.maxY, aabb.minZ + max);
/*    */       case SOUTH:
/*    */         break;
/*    */     } 
/* 27 */     return new AABB(aabb.minX, aabb.minY, aabb.maxZ + min, aabb.maxX, aabb.maxY, aabb.maxZ + max);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\piston\PistonMath.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */