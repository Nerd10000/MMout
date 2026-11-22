/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class OffsetDoubleList extends AbstractDoubleList {
/*    */   private final DoubleList delegate;
/*    */   
/*    */   private final double offset;
/*    */   
/*    */   public OffsetDoubleList(DoubleList delegate, double offset) {
/* 11 */     this.delegate = delegate;
/* 12 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public double getDouble(int index) {
/* 17 */     return this.delegate.getDouble(index) + this.offset;
/*    */   }
/*    */   
/*    */   public int size() {
/* 22 */     return this.delegate.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\phys\shapes\OffsetDoubleList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */