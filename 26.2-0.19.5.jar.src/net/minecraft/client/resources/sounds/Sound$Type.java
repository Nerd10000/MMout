/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ public enum Type {
/* 84 */   FILE("file"),
/* 85 */   SOUND_EVENT("event");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   Type(String name) {
/* 90 */     this.name = name;
/*    */   }
/*    */   
/*    */   public static Type getByName(String name) {
/* 94 */     for (Type type : values()) {
/* 95 */       if (type.name.equals(name))
/* 96 */         return type; 
/*    */     } 
/* 99 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\sounds\Sound$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */