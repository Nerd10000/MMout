/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ final class IntegerSuffix extends Record {
/*     */   private final SnbtGrammar.SignedPrefix signed;
/*     */   
/*     */   private final SnbtGrammar.TypeSuffix type;
/*     */   
/*     */   private IntegerSuffix(SnbtGrammar.SignedPrefix signed, SnbtGrammar.TypeSuffix type) {
/* 137 */     this.signed = signed;
/* 137 */     this.type = type;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #137	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/SnbtGrammar$IntegerSuffix;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public SnbtGrammar.SignedPrefix signed() {
/* 137 */     return this.signed;
/*     */   }
/*     */   
/*     */   public SnbtGrammar.TypeSuffix type() {
/* 137 */     return this.type;
/*     */   }
/*     */   
/* 141 */   public static final IntegerSuffix EMPTY = new IntegerSuffix(null, null);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\SnbtGrammar$IntegerSuffix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */