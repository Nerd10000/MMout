/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum ComparatorMode implements StringRepresentable {
/*  6 */   COMPARE("compare"),
/*  7 */   SUBTRACT("subtract");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   ComparatorMode(String name) {
/* 13 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 18 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 23 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\ComparatorMode.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */