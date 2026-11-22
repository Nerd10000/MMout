/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ final class IndexPair extends Record {
/*     */   private final int firstLevelIndex;
/*     */   
/*     */   private final int secondLevelIndex;
/*     */   
/*     */   private IndexPair(int firstLevelIndex, int secondLevelIndex) {
/* 103 */     this.firstLevelIndex = firstLevelIndex;
/* 103 */     this.secondLevelIndex = secondLevelIndex;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #103	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #103	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #103	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/vertex/TlsfAllocator$IndexPair;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int firstLevelIndex() {
/* 103 */     return this.firstLevelIndex;
/*     */   }
/*     */   
/*     */   public int secondLevelIndex() {
/* 103 */     return this.secondLevelIndex;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\TlsfAllocator$IndexPair.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */