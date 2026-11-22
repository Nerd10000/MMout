/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ 
/*     */ import java.util.UUID;
/*     */ 
/*     */ final class Heading extends Record {
/*     */   private final UUID sender;
/*     */   
/*     */   private final ChatSelectionScreen.ChatSelectionList.Entry entry;
/*     */   
/*     */   private Heading(UUID sender, ChatSelectionScreen.ChatSelectionList.Entry entry) {
/* 234 */     this.sender = sender;
/* 234 */     this.entry = entry;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #234	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #234	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #234	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/reporting/ChatSelectionScreen$ChatSelectionList$Heading;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public UUID sender() {
/* 234 */     return this.sender;
/*     */   }
/*     */   
/*     */   public ChatSelectionScreen.ChatSelectionList.Entry entry() {
/* 234 */     return this.entry;
/*     */   }
/*     */   
/*     */   public boolean canCombine(Heading other) {
/* 236 */     return other.sender.equals(this.sender);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\ChatSelectionScreen$ChatSelectionList$Heading.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */