/*    */ package net.minecraft.core;
/*    */ 
/*    */ enum null {
/*    */   public int cycle(int x, int y, int z, Direction.Axis axis) {
/* 29 */     return axis.choose(z, x, y);
/*    */   }
/*    */   
/*    */   public double cycle(double x, double y, double z, Direction.Axis axis) {
/* 34 */     return axis.choose(z, x, y);
/*    */   }
/*    */   
/*    */   public Direction.Axis cycle(Direction.Axis axis) {
/* 39 */     return AXIS_VALUES[Math.floorMod(axis.ordinal() + 1, 3)];
/*    */   }
/*    */   
/*    */   public AxisCycle inverse() {
/* 44 */     return BACKWARD;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\AxisCycle$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */