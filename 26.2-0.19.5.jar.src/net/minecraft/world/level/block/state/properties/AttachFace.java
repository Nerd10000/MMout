/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum AttachFace implements StringRepresentable {
/*  6 */   FLOOR("floor"),
/*  7 */   WALL("wall"),
/*  8 */   CEILING("ceiling");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   AttachFace(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 19 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\AttachFace.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */