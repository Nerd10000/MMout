/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class FoundRectangle {
/*    */   public final BlockPos minCorner;
/*    */   
/*    */   public final int axis1Size;
/*    */   
/*    */   public final int axis2Size;
/*    */   
/*    */   public FoundRectangle(BlockPos minCorner, int axis1Size, int axis2Size) {
/* 42 */     this.minCorner = minCorner;
/* 43 */     this.axis1Size = axis1Size;
/* 44 */     this.axis2Size = axis2Size;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\BlockUtil$FoundRectangle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */