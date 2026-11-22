/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum SpeleothemThickness implements StringRepresentable {
/*  6 */   TIP_MERGE("tip_merge"),
/*  7 */   TIP("tip"),
/*  8 */   FRUSTUM("frustum"),
/*  9 */   MIDDLE("middle"),
/* 10 */   BASE("base");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   SpeleothemThickness(String name) {
/* 15 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 20 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 25 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\SpeleothemThickness.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */