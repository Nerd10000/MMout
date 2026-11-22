/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum PistonType implements StringRepresentable {
/*  6 */   DEFAULT("normal"),
/*  7 */   STICKY("sticky");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   PistonType(String name) {
/* 12 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 17 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 22 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\PistonType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */