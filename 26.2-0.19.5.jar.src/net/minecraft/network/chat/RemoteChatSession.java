/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.time.Duration;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.util.SignatureValidator;
/*    */ import net.minecraft.world.entity.player.ProfilePublicKey;
/*    */ 
/*    */ public final class RemoteChatSession extends Record {
/*    */   private final UUID sessionId;
/*    */   
/*    */   private final ProfilePublicKey profilePublicKey;
/*    */   
/*    */   public RemoteChatSession(UUID sessionId, ProfilePublicKey profilePublicKey) {
/* 11 */     this.sessionId = sessionId;
/* 11 */     this.profilePublicKey = profilePublicKey;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/RemoteChatSession;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/RemoteChatSession;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/RemoteChatSession;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/RemoteChatSession;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/RemoteChatSession;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/RemoteChatSession;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public UUID sessionId() {
/* 11 */     return this.sessionId;
/*    */   }
/*    */   
/*    */   public ProfilePublicKey profilePublicKey() {
/* 11 */     return this.profilePublicKey;
/*    */   }
/*    */   
/*    */   public SignedMessageValidator createMessageValidator(Duration gracePeriod) {
/* 13 */     return new SignedMessageValidator.KeyBased(this.profilePublicKey
/* 14 */         .createSignatureValidator(), () -> this.profilePublicKey.data().hasExpired(gracePeriod));
/*    */   }
/*    */   
/*    */   public SignedMessageChain.Decoder createMessageDecoder(UUID profileId) {
/* 20 */     return (new SignedMessageChain(profileId, this.sessionId)).decoder(this.profilePublicKey);
/*    */   }
/*    */   
/*    */   public Data asData() {
/* 24 */     return new Data(this.sessionId, this.profilePublicKey.data());
/*    */   }
/*    */   
/*    */   public boolean hasExpired() {
/* 28 */     return this.profilePublicKey.data().hasExpired();
/*    */   }
/*    */   
/*    */   public static final class Data extends Record {
/*    */     private final UUID sessionId;
/*    */     
/*    */     private final ProfilePublicKey.Data profilePublicKey;
/*    */     
/*    */     public Data(UUID sessionId, ProfilePublicKey.Data profilePublicKey) {
/* 31 */       this.sessionId = sessionId;
/* 31 */       this.profilePublicKey = profilePublicKey;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/RemoteChatSession$Data;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/RemoteChatSession$Data;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/RemoteChatSession$Data;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/chat/RemoteChatSession$Data;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/RemoteChatSession$Data;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/chat/RemoteChatSession$Data;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public UUID sessionId() {
/* 31 */       return this.sessionId;
/*    */     }
/*    */     
/*    */     public ProfilePublicKey.Data profilePublicKey() {
/* 31 */       return this.profilePublicKey;
/*    */     }
/*    */     
/*    */     public static Data read(FriendlyByteBuf input) {
/* 33 */       return new Data(input.readUUID(), new ProfilePublicKey.Data(input));
/*    */     }
/*    */     
/*    */     public static void write(FriendlyByteBuf output, Data data) {
/* 37 */       output.writeUUID(data.sessionId);
/* 38 */       data.profilePublicKey.write(output);
/*    */     }
/*    */     
/*    */     public RemoteChatSession validate(GameProfile profile, SignatureValidator serviceSignatureValidator) throws ProfilePublicKey.ValidationException {
/* 42 */       return new RemoteChatSession(this.sessionId, ProfilePublicKey.createValidated(serviceSignatureValidator, profile.id(), this.profilePublicKey));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\RemoteChatSession.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */