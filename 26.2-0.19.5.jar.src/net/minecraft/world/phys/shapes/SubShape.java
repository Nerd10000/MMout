/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class SubShape extends DiscreteVoxelShape {
/*    */   private final DiscreteVoxelShape parent;
/*    */   
/*    */   private final int startX;
/*    */   
/*    */   private final int startY;
/*    */   
/*    */   private final int startZ;
/*    */   
/*    */   private final int endX;
/*    */   
/*    */   private final int endY;
/*    */   
/*    */   private final int endZ;
/*    */   
/*    */   SubShape(DiscreteVoxelShape parent, int startX, int startY, int startZ, int endX, int endY, int endZ) {
/* 18 */     super(endX - startX, endY - startY, endZ - startZ);
/* 19 */     this.parent = parent;
/* 20 */     this.startX = startX;
/* 21 */     this.startY = startY;
/* 22 */     this.startZ = startZ;
/* 23 */     this.endX = endX;
/* 24 */     this.endY = endY;
/* 25 */     this.endZ = endZ;
/*    */   }
/*    */   
/*    */   public boolean isFull(int x, int y, int z) {
/* 30 */     return this.parent.isFull(this.startX + x, this.startY + y, this.startZ + z);
/*    */   }
/*    */   
/*    */   public void fill(int x, int y, int z) {
/* 35 */     this.parent.fill(this.startX + x, this.startY + y, this.startZ + z);
/*    */   }
/*    */   
/*    */   public int firstFull(Direction.Axis axis) {
/* 40 */     return clampToShape(axis, this.parent.firstFull(axis));
/*    */   }
/*    */   
/*    */   public int lastFull(Direction.Axis axis) {
/* 45 */     return clampToShape(axis, this.parent.lastFull(axis));
/*    */   }
/*    */   
/*    */   private int clampToShape(Direction.Axis axis, int parentResult) {
/* 49 */     int start = axis.choose(this.startX, this.startY, this.startZ);
/* 50 */     int end = axis.choose(this.endX, this.endY, this.endZ);
/* 51 */     return Mth.clamp(parentResult, start, end) - start;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\SubShape.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */