/*    */ package net.minecraft.client.resources.model;
/*    */ 
/*    */ public enum GuiLight {
/* 38 */   FRONT("front"),
/* 39 */   SIDE("side");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   GuiLight(String name) {
/* 44 */     this.name = name;
/*    */   }
/*    */   
/*    */   public static GuiLight getByName(String name) {
/* 48 */     for (GuiLight target : values()) {
/* 49 */       if (target.name.equals(name))
/* 50 */         return target; 
/*    */     } 
/* 53 */     throw new IllegalArgumentException("Invalid gui light: " + name);
/*    */   }
/*    */   
/*    */   public boolean lightLikeBlock() {
/* 57 */     return (this == SIDE);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\UnbakedModel$GuiLight.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */