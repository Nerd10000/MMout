/*    */ package net.minecraft.tags;
/*    */ 
/*    */ public final class EntryWithSource extends Record {
/*    */   private final TagEntry entry;
/*    */   
/*    */   private final String source;
/*    */   
/*    */   public EntryWithSource(TagEntry entry, String source) {
/* 76 */     this.entry = entry;
/* 76 */     this.source = source;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagLoader$EntryWithSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/tags/TagLoader$EntryWithSource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagLoader$EntryWithSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #76	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/tags/TagLoader$EntryWithSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public TagEntry entry() {
/* 76 */     return this.entry;
/*    */   }
/*    */   
/*    */   public String source() {
/* 76 */     return this.source;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 79 */     return String.valueOf(this.entry) + " (from " + String.valueOf(this.entry) + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagLoader$EntryWithSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */