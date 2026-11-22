/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ public enum ReportType {
/*  6 */   CHAT("chat"),
/*  7 */   SKIN("skin"),
/*  8 */   USERNAME("username");
/*    */   
/*    */   private final String backendName;
/*    */   
/*    */   ReportType(String name) {
/* 13 */     this.backendName = name.toUpperCase(Locale.ROOT);
/*    */   }
/*    */   
/*    */   public String backendName() {
/* 17 */     return this.backendName;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\report\ReportType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */