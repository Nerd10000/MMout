/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.time.Instant;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FontDescription;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum ChatTrustLevel implements StringRepresentable {
/* 15 */   SECURE("secure"),
/* 16 */   MODIFIED("modified"),
/* 17 */   NOT_SECURE("not_secure");
/*    */   
/*    */   public static final Codec<ChatTrustLevel> CODEC;
/*    */   
/*    */   private final String serializedName;
/*    */   
/*    */   static {
/* 20 */     CODEC = (Codec<ChatTrustLevel>)StringRepresentable.fromEnum(ChatTrustLevel::values);
/*    */   }
/*    */   
/*    */   ChatTrustLevel(String serializedName) {
/* 25 */     this.serializedName = serializedName;
/*    */   }
/*    */   
/*    */   public static ChatTrustLevel evaluate(PlayerChatMessage message, Component decoratedMessage, Instant received) {
/* 29 */     if (!message.hasSignature() || message.hasExpiredClient(received))
/* 30 */       return NOT_SECURE; 
/* 33 */     if (isModified(message, decoratedMessage))
/* 34 */       return MODIFIED; 
/* 37 */     return SECURE;
/*    */   }
/*    */   
/*    */   private static boolean isModified(PlayerChatMessage message, Component decoratedMessage) {
/* 41 */     if (!decoratedMessage.getString().contains(message.signedContent()))
/* 42 */       return true; 
/* 45 */     Component decoratedContent = message.unsignedContent();
/* 46 */     if (decoratedContent == null)
/* 47 */       return false; 
/* 50 */     return containsModifiedStyle(decoratedContent);
/*    */   }
/*    */   
/*    */   private static boolean containsModifiedStyle(Component decoratedContent) {
/* 54 */     return ((Boolean)decoratedContent.visit((style, contents) -> isModifiedStyle(style) ? Optional.of(Boolean.valueOf(true)) : Optional.empty(), Style.EMPTY)
/*    */       
/* 59 */       .orElse(Boolean.valueOf(false))).booleanValue();
/*    */   }
/*    */   
/*    */   private static boolean isModifiedStyle(Style style) {
/* 63 */     return !style.getFont().equals(FontDescription.DEFAULT);
/*    */   }
/*    */   
/*    */   public boolean isNotSecure() {
/* 67 */     return (this == NOT_SECURE);
/*    */   }
/*    */   
/*    */   public GuiMessageTag createTag(PlayerChatMessage message) {
/* 71 */     switch (ordinal()) {
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */     } 
/* 71 */     return 
/*    */       
/* 74 */       null;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 80 */     return this.serializedName;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\ChatTrustLevel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */