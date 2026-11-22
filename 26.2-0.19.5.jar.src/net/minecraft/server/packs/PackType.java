/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ public enum PackType {
/*  4 */   CLIENT_RESOURCES("assets"),
/*  5 */   SERVER_DATA("data");
/*    */   
/*    */   private final String directory;
/*    */   
/*    */   PackType(String directory) {
/* 11 */     this.directory = directory;
/*    */   }
/*    */   
/*    */   public String getDirectory() {
/* 15 */     return this.directory;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\PackType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */