/*    */ package net.minecraft.core;
/*    */ 
/*    */ public enum AxisCycle {
/*  4 */   NONE {
/*    */     public int cycle(int x, int y, int z, Direction.Axis axis) {
/*  7 */       return axis.choose(x, y, z);
/*    */     }
/*    */     
/*    */     public double cycle(double x, double y, double z, Direction.Axis axis) {
/* 12 */       return axis.choose(x, y, z);
/*    */     }
/*    */     
/*    */     public Direction.Axis cycle(Direction.Axis axis) {
/* 17 */       return axis;
/*    */     }
/*    */     
/*    */     public AxisCycle inverse() {
/* 22 */       return this;
/*    */     }
/*    */   },
/* 26 */   FORWARD {
/*    */     public int cycle(int x, int y, int z, Direction.Axis axis) {
/* 29 */       return axis.choose(z, x, y);
/*    */     }
/*    */     
/*    */     public double cycle(double x, double y, double z, Direction.Axis axis) {
/* 34 */       return axis.choose(z, x, y);
/*    */     }
/*    */     
/*    */     public Direction.Axis cycle(Direction.Axis axis) {
/* 39 */       return AXIS_VALUES[Math.floorMod(axis.ordinal() + 1, 3)];
/*    */     }
/*    */     
/*    */     public AxisCycle inverse() {
/* 44 */       return BACKWARD;
/*    */     }
/*    */   },
/* 47 */   BACKWARD {
/*    */     public int cycle(int x, int y, int z, Direction.Axis axis) {
/* 50 */       return axis.choose(y, z, x);
/*    */     }
/*    */     
/*    */     public double cycle(double x, double y, double z, Direction.Axis axis) {
/* 55 */       return axis.choose(y, z, x);
/*    */     }
/*    */     
/*    */     public Direction.Axis cycle(Direction.Axis axis) {
/* 60 */       return AXIS_VALUES[Math.floorMod(axis.ordinal() - 1, 3)];
/*    */     }
/*    */     
/*    */     public AxisCycle inverse() {
/* 65 */       return FORWARD;
/*    */     }
/*    */   };
/*    */   
/*    */   public static final Direction.Axis[] AXIS_VALUES;
/*    */   
/*    */   public static final AxisCycle[] VALUES;
/*    */   
/*    */   static {
/* 70 */     AXIS_VALUES = Direction.Axis.values();
/* 71 */     VALUES = values();
/*    */   }
/*    */   
/*    */   public static AxisCycle between(Direction.Axis from, Direction.Axis to) {
/* 84 */     return VALUES[Math.floorMod(to.ordinal() - from.ordinal(), 3)];
/*    */   }
/*    */   
/*    */   public abstract int cycle(int paramInt1, int paramInt2, int paramInt3, Direction.Axis paramAxis);
/*    */   
/*    */   public abstract double cycle(double paramDouble1, double paramDouble2, double paramDouble3, Direction.Axis paramAxis);
/*    */   
/*    */   public abstract Direction.Axis cycle(Direction.Axis paramAxis);
/*    */   
/*    */   public abstract AxisCycle inverse();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\AxisCycle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */