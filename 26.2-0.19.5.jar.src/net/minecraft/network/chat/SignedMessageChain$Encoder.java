/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface Encoder {
/*    */   public static final Encoder UNSIGNED = body -> null;
/*    */   
/*    */   MessageSignature pack(SignedMessageBody paramSignedMessageBody);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\SignedMessageChain$Encoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */