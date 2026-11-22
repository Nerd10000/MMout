/*     */ package net.minecraft.client.renderer.block;
/*     */ 
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ enum AmbientVertexRemap {
/* 348 */   DOWN(0, 1, 2, 3),
/* 349 */   UP(2, 3, 0, 1),
/* 350 */   NORTH(3, 0, 1, 2),
/* 351 */   SOUTH(0, 1, 2, 3),
/* 352 */   WEST(3, 0, 1, 2),
/* 353 */   EAST(1, 2, 3, 0);
/*     */   
/*     */   private final int vert0;
/*     */   
/*     */   private final int vert1;
/*     */   
/*     */   private final int vert2;
/*     */   
/*     */   private final int vert3;
/*     */   
/*     */   private static final AmbientVertexRemap[] BY_FACING;
/*     */   
/*     */   static {
/* 360 */     BY_FACING = (AmbientVertexRemap[])Util.make(new AmbientVertexRemap[6], map -> {
/*     */           map[Direction.DOWN.get3DDataValue()] = DOWN;
/*     */           map[Direction.UP.get3DDataValue()] = UP;
/*     */           map[Direction.NORTH.get3DDataValue()] = NORTH;
/*     */           map[Direction.SOUTH.get3DDataValue()] = SOUTH;
/*     */           map[Direction.WEST.get3DDataValue()] = WEST;
/*     */           map[Direction.EAST.get3DDataValue()] = EAST;
/*     */         });
/*     */   }
/*     */   
/*     */   AmbientVertexRemap(int vert0, int vert1, int vert2, int vert3) {
/* 370 */     this.vert0 = vert0;
/* 371 */     this.vert1 = vert1;
/* 372 */     this.vert2 = vert2;
/* 373 */     this.vert3 = vert3;
/*     */   }
/*     */   
/*     */   public static AmbientVertexRemap fromFacing(Direction direction) {
/* 377 */     return BY_FACING[direction.get3DDataValue()];
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockModelLighter$AmbientVertexRemap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */