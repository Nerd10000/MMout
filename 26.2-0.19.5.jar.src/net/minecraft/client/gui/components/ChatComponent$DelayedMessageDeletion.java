/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.network.chat.MessageSignature;
/*     */ 
/*     */ final class DelayedMessageDeletion extends Record {
/*     */   private final MessageSignature signature;
/*     */   
/*     */   private final int deletableAfter;
/*     */   
/*     */   private DelayedMessageDeletion(MessageSignature signature, int deletableAfter) {
/* 507 */     this.signature = signature;
/* 507 */     this.deletableAfter = deletableAfter;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #507	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #507	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #507	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/components/ChatComponent$DelayedMessageDeletion;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public MessageSignature signature() {
/* 507 */     return this.signature;
/*     */   }
/*     */   
/*     */   public int deletableAfter() {
/* 507 */     return this.deletableAfter;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\ChatComponent$DelayedMessageDeletion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */