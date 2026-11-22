/*    */ package net.minecraft.network.chat.contents;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ 
/*    */ public final class LiteralContents extends Record implements PlainTextContents {
/*    */   private final String text;
/*    */   
/*    */   public LiteralContents(String text) {
/* 34 */     this.text = text;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #34	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #34	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/PlainTextContents$LiteralContents;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String text() {
/* 34 */     return this.text;
/*    */   }
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.ContentConsumer<T> output) {
/* 37 */     return output.accept(this.text);
/*    */   }
/*    */   
/*    */   public <T> Optional<T> visit(FormattedText.StyledContentConsumer<T> output, Style currentStyle) {
/* 42 */     return output.accept(currentStyle, this.text);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 47 */     return "literal{" + this.text + "}";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\contents\PlainTextContents$LiteralContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */