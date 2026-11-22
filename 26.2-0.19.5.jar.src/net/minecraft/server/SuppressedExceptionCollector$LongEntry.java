/*    */ package net.minecraft.server;
/*    */ 
/*    */ final class LongEntry extends Record {
/*    */   private final long timestampMs;
/*    */   
/*    */   private final String location;
/*    */   
/*    */   private final Class<? extends Throwable> cls;
/*    */   
/*    */   private final String message;
/*    */   
/*    */   private LongEntry(long timestampMs, String location, Class<? extends Throwable> cls, String message) {
/* 79 */     this.timestampMs = timestampMs;
/* 79 */     this.location = location;
/* 79 */     this.cls = cls;
/* 79 */     this.message = message;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #79	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$LongEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long timestampMs() {
/* 79 */     return this.timestampMs;
/*    */   }
/*    */   
/*    */   public String location() {
/* 79 */     return this.location;
/*    */   }
/*    */   
/*    */   public Class<? extends Throwable> cls() {
/* 79 */     return this.cls;
/*    */   }
/*    */   
/*    */   public String message() {
/* 79 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\SuppressedExceptionCollector$LongEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */