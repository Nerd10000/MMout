/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.components.ComponentRenderUtils;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.MessageSignature;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ public final class GuiMessage extends Record {
/*    */   private final int addedTime;
/*    */   
/*    */   private final Component content;
/*    */   
/*    */   private final MessageSignature signature;
/*    */   
/*    */   private final GuiMessageSource source;
/*    */   
/*    */   private final GuiMessageTag tag;
/*    */   
/*    */   private static final int MESSAGE_TAG_MARGIN_LEFT = 4;
/*    */   
/*    */   public GuiMessage(int addedTime, Component content, MessageSignature signature, GuiMessageSource source, GuiMessageTag tag) {
/* 12 */     this.addedTime = addedTime;
/* 12 */     this.content = content;
/* 12 */     this.signature = signature;
/* 12 */     this.source = source;
/* 12 */     this.tag = tag;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/GuiMessage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessage;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/GuiMessage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessage;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/GuiMessage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessage;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int addedTime() {
/* 12 */     return this.addedTime;
/*    */   }
/*    */   
/*    */   public Component content() {
/* 12 */     return this.content;
/*    */   }
/*    */   
/*    */   public MessageSignature signature() {
/* 12 */     return this.signature;
/*    */   }
/*    */   
/*    */   public GuiMessageSource source() {
/* 12 */     return this.source;
/*    */   }
/*    */   
/*    */   public GuiMessageTag tag() {
/* 12 */     return this.tag;
/*    */   }
/*    */   
/*    */   public static final class Line extends Record {
/*    */     private final GuiMessage parent;
/*    */     
/*    */     private final FormattedCharSequence content;
/*    */     
/*    */     private final boolean endOfEntry;
/*    */     
/*    */     public Line(GuiMessage parent, FormattedCharSequence content, boolean endOfEntry) {
/* 15 */       this.parent = parent;
/* 15 */       this.content = content;
/* 15 */       this.endOfEntry = endOfEntry;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #15	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #15	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #15	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public GuiMessage parent() {
/* 15 */       return this.parent;
/*    */     }
/*    */     
/*    */     public FormattedCharSequence content() {
/* 15 */       return this.content;
/*    */     }
/*    */     
/*    */     public boolean endOfEntry() {
/* 15 */       return this.endOfEntry;
/*    */     }
/*    */     
/*    */     public int getTagIconLeft(Font font) {
/* 17 */       return font.width(this.content) + 4;
/*    */     }
/*    */     
/*    */     public GuiMessageTag tag() {
/* 21 */       return this.parent.tag;
/*    */     }
/*    */     
/*    */     public int addedTime() {
/* 25 */       return this.parent.addedTime;
/*    */     }
/*    */   }
/*    */   
/*    */   public List<FormattedCharSequence> splitLines(Font font, int maxWidth) {
/* 31 */     if (this.tag != null && this.tag.icon() != null)
/* 32 */       maxWidth -= (this.tag.icon()).width + 4 + 2; 
/* 35 */     return ComponentRenderUtils.wrapComponents((FormattedText)this.content, maxWidth, font);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\GuiMessage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */