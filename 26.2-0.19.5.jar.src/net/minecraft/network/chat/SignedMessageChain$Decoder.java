/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import java.util.function.BooleanSupplier;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface Decoder {
/*    */   static Decoder unsigned(UUID profileId, BooleanSupplier enforcesSecureChat) {
/* 93 */     return (signature, body) -> {
/*    */         if (enforcesSecureChat.getAsBoolean())
/*    */           throw new SignedMessageChain.DecodeException(SignedMessageChain.DecodeException.MISSING_PROFILE_KEY); 
/*    */         return PlayerChatMessage.unsigned(profileId, body.content());
/*    */       };
/*    */   }
/*    */   
/*    */   default void setChainBroken() {}
/*    */   
/*    */   PlayerChatMessage unpack(MessageSignature paramMessageSignature, SignedMessageBody paramSignedMessageBody) throws SignedMessageChain.DecodeException;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\SignedMessageChain$Decoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */