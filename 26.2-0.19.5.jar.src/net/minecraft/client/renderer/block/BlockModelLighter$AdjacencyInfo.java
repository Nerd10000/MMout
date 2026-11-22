/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ enum AdjacencyInfo {
/* 405 */   DOWN(new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH }, 0.5F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.SOUTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.SOUTH }),
/* 411 */   UP(new Direction[] { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH }, 1.0F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.SOUTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.SOUTH }),
/* 417 */   NORTH(new Direction[] { Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST }, 0.8F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_WEST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_EAST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_EAST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_WEST }),
/* 423 */   SOUTH(new Direction[] { Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP }, 0.8F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.WEST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_WEST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.WEST, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.WEST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.EAST }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_EAST, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.EAST, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.EAST }),
/* 429 */   WEST(new Direction[] { Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH }, 0.6F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.SOUTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.SOUTH }),
/* 435 */   EAST(new Direction[] { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH }, 0.6F, true, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.SOUTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.FLIP_DOWN, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.DOWN, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.NORTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_NORTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.NORTH }, new BlockModelLighter.SizeInfo[] { BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.SOUTH, BlockModelLighter.SizeInfo.FLIP_UP, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.FLIP_SOUTH, BlockModelLighter.SizeInfo.UP, BlockModelLighter.SizeInfo.SOUTH });
/*     */   
/*     */   private final Direction[] corners;
/*     */   
/*     */   private final boolean doNonCubicWeight;
/*     */   
/*     */   private final BlockModelLighter.SizeInfo[] vert0Weights;
/*     */   
/*     */   private final BlockModelLighter.SizeInfo[] vert1Weights;
/*     */   
/*     */   private final BlockModelLighter.SizeInfo[] vert2Weights;
/*     */   
/*     */   private final BlockModelLighter.SizeInfo[] vert3Weights;
/*     */   
/*     */   private static final AdjacencyInfo[] BY_FACING;
/*     */   
/*     */   static {
/* 449 */     BY_FACING = (AdjacencyInfo[])Util.make(new AdjacencyInfo[6], map -> {
/*     */           map[Direction.DOWN.get3DDataValue()] = DOWN;
/*     */           map[Direction.UP.get3DDataValue()] = UP;
/*     */           map[Direction.NORTH.get3DDataValue()] = NORTH;
/*     */           map[Direction.SOUTH.get3DDataValue()] = SOUTH;
/*     */           map[Direction.WEST.get3DDataValue()] = WEST;
/*     */           map[Direction.EAST.get3DDataValue()] = EAST;
/*     */         });
/*     */   }
/*     */   
/*     */   AdjacencyInfo(Direction[] corners, float shadeWeight, boolean doNonCubicWeight, BlockModelLighter.SizeInfo[] vert0Weights, BlockModelLighter.SizeInfo[] vert1Weights, BlockModelLighter.SizeInfo[] vert2Weights, BlockModelLighter.SizeInfo[] vert3Weights) {
/* 459 */     this.corners = corners;
/* 460 */     this.doNonCubicWeight = doNonCubicWeight;
/* 461 */     this.vert0Weights = vert0Weights;
/* 462 */     this.vert1Weights = vert1Weights;
/* 463 */     this.vert2Weights = vert2Weights;
/* 464 */     this.vert3Weights = vert3Weights;
/*     */   }
/*     */   
/*     */   public static AdjacencyInfo fromFacing(Direction direction) {
/* 468 */     return BY_FACING[direction.get3DDataValue()];
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockModelLighter$AdjacencyInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */