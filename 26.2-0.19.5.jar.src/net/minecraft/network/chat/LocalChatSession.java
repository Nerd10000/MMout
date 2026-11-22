/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.util.Signer;
/*    */ import net.minecraft.world.entity.player.ProfileKeyPair;
/*    */ 
/*    */ public final class LocalChatSession extends Record {
/*    */   private final UUID sessionId;
/*    */   
/*    */   private final ProfileKeyPair keyPair;
/*    */   
/*    */   public LocalChatSession(UUID sessionId, ProfileKeyPair keyPair) {
/*  9 */     this.sessionId = sessionId;
/*  9 */     this.keyPair = keyPair;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/LocalChatSession;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LocalChatSession;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/LocalChatSession;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/LocalChatSession;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/LocalChatSession;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/LocalChatSession;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public UUID sessionId() {
/*  9 */     return this.sessionId;
/*    */   }
/*    */   
/*    */   public ProfileKeyPair keyPair() {
/*  9 */     return this.keyPair;
/*    */   }
/*    */   
/*    */   public static LocalChatSession create(ProfileKeyPair keyPair) {
/* 11 */     return new LocalChatSession(UUID.randomUUID(), keyPair);
/*    */   }
/*    */   
/*    */   public SignedMessageChain.Encoder createMessageEncoder(UUID profileId) {
/* 15 */     return (new SignedMessageChain(profileId, this.sessionId)).encoder(Signer.from(this.keyPair.privateKey(), "SHA256withRSA"));
/*    */   }
/*    */   
/*    */   public RemoteChatSession asRemote() {
/* 19 */     return new RemoteChatSession(this.sessionId, this.keyPair.publicKey());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\LocalChatSession.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */