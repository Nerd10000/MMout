/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Tilt implements StringRepresentable {
/*  6 */   NONE("none", true),
/*  7 */   UNSTABLE("unstable", false),
/*  8 */   PARTIAL("partial", true),
/*  9 */   FULL("full", true);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final boolean causesVibration;
/*    */   
/*    */   Tilt(String name, boolean causesVibration) {
/* 16 */     this.name = name;
/* 17 */     this.causesVibration = causesVibration;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 22 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean causesVibration() {
/* 26 */     return this.causesVibration;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\Tilt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */