/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public final class Packed extends Record {
/*    */   private final String content;
/*    */   
/*    */   private final Instant timeStamp;
/*    */   
/*    */   private final long salt;
/*    */   
/*    */   private final LastSeenMessages.Packed lastSeen;
/*    */   
/*    */   public Packed(String content, Instant timeStamp, long salt, LastSeenMessages.Packed lastSeen) {
/* 43 */     this.content = content;
/* 43 */     this.timeStamp = timeStamp;
/* 43 */     this.salt = salt;
/* 43 */     this.lastSeen = lastSeen;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/SignedMessageBody$Packed;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageBody$Packed;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/SignedMessageBody$Packed;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/SignedMessageBody$Packed;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/SignedMessageBody$Packed;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #43	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/SignedMessageBody$Packed;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String content() {
/* 43 */     return this.content;
/*    */   }
/*    */   
/*    */   public Instant timeStamp() {
/* 43 */     return this.timeStamp;
/*    */   }
/*    */   
/*    */   public long salt() {
/* 43 */     return this.salt;
/*    */   }
/*    */   
/*    */   public LastSeenMessages.Packed lastSeen() {
/* 43 */     return this.lastSeen;
/*    */   }
/*    */   
/*    */   public Packed(FriendlyByteBuf input) {
/* 45 */     this(input.readUtf(256), input.readInstant(), input.readLong(), new LastSeenMessages.Packed(input));
/*    */   }
/*    */   
/*    */   public void write(FriendlyByteBuf output) {
/* 49 */     output.writeUtf(this.content, 256);
/* 50 */     output.writeInstant(this.timeStamp);
/* 51 */     output.writeLong(this.salt);
/* 52 */     this.lastSeen.write(output);
/*    */   }
/*    */   
/*    */   public Optional<SignedMessageBody> unpack(MessageSignatureCache cache) {
/* 56 */     return this.lastSeen.unpack(cache).map(lastSeen -> new SignedMessageBody(this.content, this.timeStamp, this.salt, lastSeen));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\SignedMessageBody$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */