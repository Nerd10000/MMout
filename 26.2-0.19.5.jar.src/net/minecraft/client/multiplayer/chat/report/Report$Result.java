/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ 
/*    */ import com.mojang.authlib.minecraft.report.AbuseReport;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public final class Result extends Record {
/*    */   private final UUID id;
/*    */   
/*    */   private final ReportType reportType;
/*    */   
/*    */   private final AbuseReport report;
/*    */   
/*    */   public Result(UUID id, ReportType reportType, AbuseReport report) {
/* 90 */     this.id = id;
/* 90 */     this.reportType = reportType;
/* 90 */     this.report = report;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/Report$Result;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #90	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$Result;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/Report$Result;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #90	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$Result;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/Report$Result;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #90	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$Result;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public UUID id() {
/* 90 */     return this.id;
/*    */   }
/*    */   
/*    */   public ReportType reportType() {
/* 90 */     return this.reportType;
/*    */   }
/*    */   
/*    */   public AbuseReport report() {
/* 90 */     return this.report;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\report\Report$Result.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */