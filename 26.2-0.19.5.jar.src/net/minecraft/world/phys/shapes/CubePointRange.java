/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ 
/*    */ public class CubePointRange extends AbstractDoubleList {
/*    */   private final int parts;
/*    */   
/*    */   public CubePointRange(int parts) {
/*  9 */     if (parts <= 0)
/* 10 */       throw new IllegalArgumentException("Need at least 1 part"); 
/* 12 */     this.parts = parts;
/*    */   }
/*    */   
/*    */   public double getDouble(int index) {
/* 17 */     return index / this.parts;
/*    */   }
/*    */   
/*    */   public int size() {
/* 22 */     return this.parts + 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\CubePointRange.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */