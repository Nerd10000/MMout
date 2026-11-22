/*    */ package net.minecraft.server;
/*    */ 
/*    */ final class ShortEntry extends Record {
/*    */   private final String location;
/*    */   
/*    */   private final Class<? extends Throwable> cls;
/*    */   
/*    */   private ShortEntry(String location, Class<? extends Throwable> cls) {
/* 87 */     this.location = location;
/* 87 */     this.cls = cls;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #87	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #87	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #87	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/SuppressedExceptionCollector$ShortEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String location() {
/* 87 */     return this.location;
/*    */   }
/*    */   
/*    */   public Class<? extends Throwable> cls() {
/* 87 */     return this.cls;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\SuppressedExceptionCollector$ShortEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */