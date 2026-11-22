/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class GuiMessageTag extends Record {
/*    */   private final int indicatorColor;
/*    */   
/*    */   private final Icon icon;
/*    */   
/*    */   private final Component text;
/*    */   
/*    */   private final String logTag;
/*    */   
/*    */   public GuiMessageTag(int indicatorColor, Icon icon, Component text, String logTag) {
/* 11 */     this.indicatorColor = indicatorColor;
/* 11 */     this.icon = icon;
/* 11 */     this.text = text;
/* 11 */     this.logTag = logTag;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int indicatorColor() {
/* 11 */     return this.indicatorColor;
/*    */   }
/*    */   
/*    */   public Icon icon() {
/* 11 */     return this.icon;
/*    */   }
/*    */   
/*    */   public Component text() {
/* 11 */     return this.text;
/*    */   }
/*    */   
/*    */   public String logTag() {
/* 11 */     return this.logTag;
/*    */   }
/*    */   
/* 12 */   private static final Component SYSTEM_TEXT = (Component)Component.translatable("chat.tag.system");
/*    */   
/* 13 */   private static final Component SYSTEM_TEXT_SINGLE_PLAYER = (Component)Component.translatable("chat.tag.system_single_player");
/*    */   
/* 14 */   private static final Component CHAT_NOT_SECURE_TEXT = (Component)Component.translatable("chat.tag.not_secure");
/*    */   
/* 15 */   private static final Component CHAT_MODIFIED_TEXT = (Component)Component.translatable("chat.tag.modified");
/*    */   
/* 16 */   private static final Component CHAT_ERROR_TEXT = (Component)Component.translatable("chat.tag.error");
/*    */   
/*    */   private static final int CHAT_NOT_SECURE_INDICATOR_COLOR = 13684944;
/*    */   
/*    */   private static final int CHAT_MODIFIED_INDICATOR_COLOR = 6316128;
/*    */   
/* 21 */   private static final GuiMessageTag SYSTEM = new GuiMessageTag(13684944, null, SYSTEM_TEXT, "System");
/*    */   
/* 22 */   private static final GuiMessageTag SYSTEM_SINGLE_PLAYER = new GuiMessageTag(13684944, null, SYSTEM_TEXT_SINGLE_PLAYER, "System");
/*    */   
/* 23 */   private static final GuiMessageTag CHAT_NOT_SECURE = new GuiMessageTag(13684944, null, CHAT_NOT_SECURE_TEXT, "Not Secure");
/*    */   
/* 24 */   private static final GuiMessageTag CHAT_ERROR = new GuiMessageTag(16733525, null, CHAT_ERROR_TEXT, "Chat Error");
/*    */   
/*    */   public static GuiMessageTag system() {
/* 27 */     return SYSTEM;
/*    */   }
/*    */   
/*    */   public static GuiMessageTag systemSinglePlayer() {
/* 31 */     return SYSTEM_SINGLE_PLAYER;
/*    */   }
/*    */   
/*    */   public static GuiMessageTag chatNotSecure() {
/* 35 */     return CHAT_NOT_SECURE;
/*    */   }
/*    */   
/*    */   public static GuiMessageTag chatModified(String originalContent) {
/* 39 */     MutableComponent mutableComponent1 = Component.literal(originalContent).withStyle(ChatFormatting.GRAY);
/* 40 */     MutableComponent mutableComponent2 = Component.empty().append(CHAT_MODIFIED_TEXT).append(CommonComponents.NEW_LINE).append((Component)mutableComponent1);
/* 41 */     return new GuiMessageTag(6316128, Icon.CHAT_MODIFIED, (Component)mutableComponent2, "Modified");
/*    */   }
/*    */   
/*    */   public static GuiMessageTag chatError() {
/* 45 */     return CHAT_ERROR;
/*    */   }
/*    */   
/*    */   public enum Icon {
/* 49 */     CHAT_MODIFIED((String)Identifier.withDefaultNamespace("icon/chat_modified"), 9, 9);
/*    */     
/*    */     public final int height;
/*    */     
/*    */     public final int width;
/*    */     
/*    */     public final Identifier sprite;
/*    */     
/*    */     Icon(Identifier sprite, int width, int height) {
/* 57 */       this.sprite = sprite;
/* 58 */       this.width = width;
/* 59 */       this.height = height;
/*    */     }
/*    */     
/*    */     public void extractRenderState(GuiGraphicsExtractor graphics, int x, int y) {
/* 63 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, x, y, this.width, this.height);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\GuiMessageTag.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */