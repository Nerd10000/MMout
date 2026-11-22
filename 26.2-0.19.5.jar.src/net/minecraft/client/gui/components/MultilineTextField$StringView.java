/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ public final class StringView extends Record {
/*     */   private final int beginIndex;
/*     */   
/*     */   private final int endIndex;
/*     */   
/*     */   protected StringView(int beginIndex, int endIndex) {
/* 421 */     this.beginIndex = beginIndex;
/* 421 */     this.endIndex = endIndex;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/MultilineTextField$StringView;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #421	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/MultilineTextField$StringView;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/MultilineTextField$StringView;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #421	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/MultilineTextField$StringView;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/MultilineTextField$StringView;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #421	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/components/MultilineTextField$StringView;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int beginIndex() {
/* 421 */     return this.beginIndex;
/*     */   }
/*     */   
/*     */   public int endIndex() {
/* 421 */     return this.endIndex;
/*     */   }
/*     */   
/* 422 */   private static final StringView EMPTY = new StringView(0, 0);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\MultilineTextField$StringView.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */