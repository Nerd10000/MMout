/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ public final class Line extends Record {
/*    */   private final GuiMessage parent;
/*    */   
/*    */   private final FormattedCharSequence content;
/*    */   
/*    */   private final boolean endOfEntry;
/*    */   
/*    */   public Line(GuiMessage parent, FormattedCharSequence content, boolean endOfEntry) {
/* 15 */     this.parent = parent;
/* 15 */     this.content = content;
/* 15 */     this.endOfEntry = endOfEntry;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/GuiMessage$Line;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GuiMessage parent() {
/* 15 */     return this.parent;
/*    */   }
/*    */   
/*    */   public FormattedCharSequence content() {
/* 15 */     return this.content;
/*    */   }
/*    */   
/*    */   public boolean endOfEntry() {
/* 15 */     return this.endOfEntry;
/*    */   }
/*    */   
/*    */   public int getTagIconLeft(Font font) {
/* 17 */     return font.width(this.content) + 4;
/*    */   }
/*    */   
/*    */   public GuiMessageTag tag() {
/* 21 */     return this.parent.tag;
/*    */   }
/*    */   
/*    */   public int addedTime() {
/* 25 */     return this.parent.addedTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\GuiMessage$Line.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */