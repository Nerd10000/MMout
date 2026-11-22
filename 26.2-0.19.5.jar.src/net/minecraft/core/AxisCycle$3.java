/*    */ package net.minecraft.core;
/*    */ 
/*    */ enum null {
/*    */   public int cycle(int x, int y, int z, Direction.Axis axis) {
/* 50 */     return axis.choose(y, z, x);
/*    */   }
/*    */   
/*    */   public double cycle(double x, double y, double z, Direction.Axis axis) {
/* 55 */     return axis.choose(y, z, x);
/*    */   }
/*    */   
/*    */   public Direction.Axis cycle(Direction.Axis axis) {
/* 60 */     return AXIS_VALUES[Math.floorMod(axis.ordinal() - 1, 3)];
/*    */   }
/*    */   
/*    */   public AxisCycle inverse() {
/* 65 */     return FORWARD;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\AxisCycle$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */