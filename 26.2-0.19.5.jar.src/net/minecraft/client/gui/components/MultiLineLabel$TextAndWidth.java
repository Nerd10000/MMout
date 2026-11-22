/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ 
/*     */ public final class TextAndWidth extends Record {
/*     */   private final FormattedCharSequence text;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   public TextAndWidth(FormattedCharSequence text, int width) {
/* 112 */     this.text = text;
/* 112 */     this.width = width;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/components/MultiLineLabel$TextAndWidth;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public FormattedCharSequence text() {
/* 112 */     return this.text;
/*     */   }
/*     */   
/*     */   public int width() {
/* 112 */     return this.width;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\MultiLineLabel$TextAndWidth.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */