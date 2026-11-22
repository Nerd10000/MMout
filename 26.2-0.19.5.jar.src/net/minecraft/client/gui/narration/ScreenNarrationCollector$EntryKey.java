/*    */ package net.minecraft.client.gui.narration;
/*    */ 
/*    */ final class EntryKey extends Record {
/*    */   private final NarratedElementType type;
/*    */   
/*    */   private final int depth;
/*    */   
/*    */   private EntryKey(NarratedElementType type, int depth) {
/* 61 */     this.type = type;
/* 61 */     this.depth = depth;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #61	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/narration/ScreenNarrationCollector$EntryKey;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public NarratedElementType type() {
/* 61 */     return this.type;
/*    */   }
/*    */   
/*    */   public int depth() {
/* 61 */     return this.depth;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\narration\ScreenNarrationCollector$EntryKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */