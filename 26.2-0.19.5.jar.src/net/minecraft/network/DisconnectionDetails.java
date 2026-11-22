/*    */ package net.minecraft.network;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class DisconnectionDetails extends Record {
/*    */   private final Component reason;
/*    */   
/*    */   private final Optional<Path> report;
/*    */   
/*    */   private final Optional<URI> bugReportLink;
/*    */   
/*    */   public Optional<URI> bugReportLink() {
/*  9 */     return this.bugReportLink;
/*    */   }
/*    */   
/*    */   public Optional<Path> report() {
/*  9 */     return this.report;
/*    */   }
/*    */   
/*    */   public Component reason() {
/*  9 */     return this.reason;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/DisconnectionDetails;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/DisconnectionDetails;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/DisconnectionDetails;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/DisconnectionDetails;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/DisconnectionDetails;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/DisconnectionDetails;
/*    */   }
/*    */   
/*    */   public DisconnectionDetails(Component reason, Optional<Path> report, Optional<URI> bugReportLink) {
/*  9 */     this.reason = reason;
/*  9 */     this.report = report;
/*  9 */     this.bugReportLink = bugReportLink;
/*    */   }
/*    */   
/*    */   public DisconnectionDetails(Component reason) {
/* 11 */     this(reason, Optional.empty(), Optional.empty());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\DisconnectionDetails.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */