/*    */ package net.minecraft.util.profiling.jfr;
/*    */ 
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ 
/*    */ public enum Environment {
/*  6 */   CLIENT("client"),
/*  6 */   SERVER("server");
/*    */   
/*    */   private final String description;
/*    */   
/*    */   Environment(String description) {
/* 11 */     this.description = description;
/*    */   }
/*    */   
/*    */   public static Environment from(MinecraftServer server) {
/* 15 */     return server.isDedicatedServer() ? SERVER : CLIENT;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 19 */     return this.description;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\Environment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */