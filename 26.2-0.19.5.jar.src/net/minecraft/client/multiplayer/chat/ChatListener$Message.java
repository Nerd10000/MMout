/*     */ package net.minecraft.client.multiplayer.chat;
/*     */ 
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.network.chat.MessageSignature;
/*     */ 
/*     */ final class Message extends Record {
/*     */   private final MessageSignature signature;
/*     */   
/*     */   private final BooleanSupplier handler;
/*     */   
/*     */   private Message(MessageSignature signature, BooleanSupplier handler) {
/* 278 */     this.signature = signature;
/* 278 */     this.handler = handler;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #278	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #278	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #278	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/ChatListener$Message;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public MessageSignature signature() {
/* 278 */     return this.signature;
/*     */   }
/*     */   
/*     */   public BooleanSupplier handler() {
/* 278 */     return this.handler;
/*     */   }
/*     */   
/*     */   public boolean accept() {
/* 280 */     return this.handler.getAsBoolean();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\ChatListener$Message.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */