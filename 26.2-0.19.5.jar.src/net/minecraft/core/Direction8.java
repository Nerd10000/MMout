/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Arrays;
/*    */ import java.util.Set;
/*    */ 
/*    */ public enum Direction8 {
/*  9 */   NORTH(new Direction[] { Direction.NORTH }),
/* 10 */   NORTH_EAST(new Direction[] { Direction.NORTH, Direction.EAST }),
/* 11 */   EAST(new Direction[] { Direction.EAST }),
/* 12 */   SOUTH_EAST(new Direction[] { Direction.SOUTH, Direction.EAST }),
/* 13 */   SOUTH(new Direction[] { Direction.SOUTH }),
/* 14 */   SOUTH_WEST(new Direction[] { Direction.SOUTH, Direction.WEST }),
/* 15 */   WEST(new Direction[] { Direction.WEST }),
/* 16 */   NORTH_WEST(new Direction[] { Direction.NORTH, Direction.WEST });
/*    */   
/*    */   private final Set<Direction> directions;
/*    */   
/*    */   private final Vec3i step;
/*    */   
/*    */   Direction8(Direction... directions) {
/* 22 */     this.directions = (Set<Direction>)Sets.immutableEnumSet(Arrays.asList(directions));
/* 24 */     this.step = new Vec3i(0, 0, 0);
/* 25 */     for (Direction direction : directions)
/* 26 */       this.step.setX(this.step.getX() + direction.getStepX()).setY(this.step.getY() + direction.getStepY()).setZ(this.step.getZ() + direction.getStepZ()); 
/*    */   }
/*    */   
/*    */   public Set<Direction> getDirections() {
/* 31 */     return this.directions;
/*    */   }
/*    */   
/*    */   public int getStepX() {
/* 35 */     return this.step.getX();
/*    */   }
/*    */   
/*    */   public int getStepZ() {
/* 39 */     return this.step.getZ();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Direction8.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */