/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ public enum Direction {
/* 847 */   DOWN(AxisDirection.NEGATIVE, Axis.Y),
/* 848 */   UP(AxisDirection.POSITIVE, Axis.Y),
/* 849 */   NORTH(AxisDirection.NEGATIVE, Axis.Z),
/* 850 */   SOUTH(AxisDirection.POSITIVE, Axis.Z),
/* 851 */   WEST(AxisDirection.NEGATIVE, Axis.X),
/* 852 */   EAST(AxisDirection.POSITIVE, Axis.X);
/*     */   
/*     */   private final Axis axis;
/*     */   
/*     */   private final AxisDirection axisDirection;
/*     */   
/*     */   Direction(AxisDirection axisDirection, Axis axis) {
/* 859 */     this.axis = axis;
/* 860 */     this.axisDirection = axisDirection;
/*     */   }
/*     */   
/*     */   public AxisDirection getAxisDirection() {
/* 864 */     return this.axisDirection;
/*     */   }
/*     */   
/*     */   public Axis getAxis() {
/* 868 */     return this.axis;
/*     */   }
/*     */   
/*     */   public enum Axis {
/* 872 */     X, Y, Z;
/*     */   }
/*     */   
/*     */   public enum AxisDirection {
/* 878 */     POSITIVE(1),
/* 879 */     NEGATIVE(-1);
/*     */     
/*     */     private final int step;
/*     */     
/*     */     AxisDirection(int step) {
/* 885 */       this.step = step;
/*     */     }
/*     */     
/*     */     public int getStep() {
/* 889 */       return this.step;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkPalettedStorageFix$Direction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */