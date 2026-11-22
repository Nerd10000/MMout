/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class ArrayVoxelShape extends VoxelShape {
/*    */   private final DoubleList xs;
/*    */   
/*    */   private final DoubleList ys;
/*    */   
/*    */   private final DoubleList zs;
/*    */   
/*    */   ArrayVoxelShape(DiscreteVoxelShape shape, double[] xs, double[] ys, double[] zs) {
/* 19 */     this(shape, 
/*    */         
/* 21 */         (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(xs, shape.getXSize() + 1)), 
/* 22 */         (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(ys, shape.getYSize() + 1)), 
/* 23 */         (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(zs, shape.getZSize() + 1)));
/*    */   }
/*    */   
/*    */   ArrayVoxelShape(DiscreteVoxelShape shape, DoubleList xs, DoubleList ys, DoubleList zs) {
/* 29 */     super(shape);
/* 30 */     int xSize = shape.getXSize() + 1;
/* 31 */     int ySize = shape.getYSize() + 1;
/* 32 */     int zSize = shape.getZSize() + 1;
/* 33 */     if (xSize != xs.size() || ySize != ys.size() || zSize != zs.size())
/* 34 */       throw (IllegalArgumentException)Util.pauseInIde(new IllegalArgumentException("Lengths of point arrays must be consistent with the size of the VoxelShape.")); 
/* 36 */     this.xs = xs;
/* 37 */     this.ys = ys;
/* 38 */     this.zs = zs;
/*    */   }
/*    */   
/*    */   public DoubleList getCoords(Direction.Axis axis) {
/* 43 */     switch (axis) {
/*    */       default:
/* 43 */         throw new MatchException(null, null);
/*    */       case X:
/*    */       
/*    */       case Y:
/*    */       
/*    */       case Z:
/*    */         break;
/*    */     } 
/* 43 */     return 
/*    */       
/* 46 */       this.zs;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\ArrayVoxelShape.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */