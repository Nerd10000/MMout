/*    */ package net.minecraft.core;
/*    */ 
/*    */ enum null {
/*    */   public int cycle(int x, int y, int z, Direction.Axis axis) {
/*  7 */     return axis.choose(x, y, z);
/*    */   }
/*    */   
/*    */   public double cycle(double x, double y, double z, Direction.Axis axis) {
/* 12 */     return axis.choose(x, y, z);
/*    */   }
/*    */   
/*    */   public Direction.Axis cycle(Direction.Axis axis) {
/* 17 */     return axis;
/*    */   }
/*    */   
/*    */   public AxisCycle inverse() {
/* 22 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\AxisCycle$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */