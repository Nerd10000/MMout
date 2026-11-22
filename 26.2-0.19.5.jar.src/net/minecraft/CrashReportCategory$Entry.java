/*     */ package net.minecraft;
/*     */ 
/*     */ public final class Entry extends Record {
/*     */   private final String key;
/*     */   
/*     */   private final String value;
/*     */   
/*     */   public String value() {
/* 177 */     return this.value;
/*     */   }
/*     */   
/*     */   public String key() {
/* 177 */     return this.key;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/CrashReportCategory$Entry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #177	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/CrashReportCategory$Entry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/CrashReportCategory$Entry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #177	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/CrashReportCategory$Entry;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/CrashReportCategory$Entry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #177	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/CrashReportCategory$Entry;
/*     */   }
/*     */   
/*     */   public Entry(String key, String value) {
/* 177 */     this.key = key;
/* 177 */     this.value = value;
/*     */   }
/*     */   
/*     */   public Entry(String key, Object rawValue) {
/* 187 */     this(key, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\CrashReportCategory$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */