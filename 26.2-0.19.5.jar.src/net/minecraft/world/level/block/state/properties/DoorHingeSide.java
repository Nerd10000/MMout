/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DoorHingeSide implements StringRepresentable {
/*  6 */   LEFT, RIGHT;
/*    */   
/*    */   public String toString() {
/* 12 */     return getSerializedName();
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 17 */     return (this == LEFT) ? "left" : "right";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\DoorHingeSide.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */