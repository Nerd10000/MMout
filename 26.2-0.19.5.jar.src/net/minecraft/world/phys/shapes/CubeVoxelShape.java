/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public final class CubeVoxelShape extends VoxelShape {
/*    */   public CubeVoxelShape(DiscreteVoxelShape shape) {
/*  9 */     super(shape);
/*    */   }
/*    */   
/*    */   public DoubleList getCoords(Direction.Axis axis) {
/* 14 */     return (DoubleList)new CubePointRange(this.shape.getSize(axis));
/*    */   }
/*    */   
/*    */   protected int findIndex(Direction.Axis axis, double coord) {
/* 19 */     int size = this.shape.getSize(axis);
/* 20 */     return Mth.floor(Mth.clamp(coord * size, -1.0D, size));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\CubeVoxelShape.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */