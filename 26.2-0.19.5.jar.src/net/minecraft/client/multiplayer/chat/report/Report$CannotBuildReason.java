/*     */ package net.minecraft.client.multiplayer.chat.report;
/*     */ 
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public final class CannotBuildReason extends Record {
/*     */   private final Component message;
/*     */   
/*     */   public CannotBuildReason(Component message) {
/*  93 */     this.message = message;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #93	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/Report$CannotBuildReason;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Component message() {
/*  93 */     return this.message;
/*     */   }
/*     */   
/*  94 */   public static final CannotBuildReason NO_REASON = new CannotBuildReason((Component)Component.translatable("gui.abuseReport.send.no_reason"));
/*     */   
/*  95 */   public static final CannotBuildReason NO_REPORTED_MESSAGES = new CannotBuildReason((Component)Component.translatable("gui.chatReport.send.no_reported_messages"));
/*     */   
/*  96 */   public static final CannotBuildReason TOO_MANY_MESSAGES = new CannotBuildReason((Component)Component.translatable("gui.chatReport.send.too_many_messages"));
/*     */   
/*  97 */   public static final CannotBuildReason COMMENT_TOO_LONG = new CannotBuildReason((Component)Component.translatable("gui.abuseReport.send.comment_too_long"));
/*     */   
/*  98 */   public static final CannotBuildReason NOT_ATTESTED = new CannotBuildReason((Component)Component.translatable("gui.abuseReport.send.not_attested"));
/*     */   
/*     */   public Tooltip tooltip() {
/* 101 */     return Tooltip.create(this.message);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\report\Report$CannotBuildReason.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */