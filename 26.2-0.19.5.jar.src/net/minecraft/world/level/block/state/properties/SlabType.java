/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum SlabType implements StringRepresentable {
/*  6 */   TOP("top"),
/*  7 */   BOTTOM("bottom"),
/*  8 */   DOUBLE("double");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   SlabType(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 19 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 24 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\SlabType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */