/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ 
/*     */ public final class NarratableSearchResult extends Record {
/*     */   private final NarratableEntry entry;
/*     */   
/*     */   private final int index;
/*     */   
/*     */   private final NarratableEntry.NarrationPriority priority;
/*     */   
/*     */   public NarratableSearchResult(NarratableEntry entry, int index, NarratableEntry.NarrationPriority priority) {
/* 600 */     this.entry = entry;
/* 600 */     this.index = index;
/* 600 */     this.priority = priority;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #600	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #600	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #600	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/Screen$NarratableSearchResult;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public NarratableEntry entry() {
/* 600 */     return this.entry;
/*     */   }
/*     */   
/*     */   public int index() {
/* 600 */     return this.index;
/*     */   }
/*     */   
/*     */   public NarratableEntry.NarrationPriority priority() {
/* 600 */     return this.priority;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\Screen$NarratableSearchResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */