/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ 
/*    */ public final class State extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final AtomicLong preparationNanos;
/*    */   
/*    */   private final AtomicLong preparationCount;
/*    */   
/*    */   private final AtomicLong reloadNanos;
/*    */   
/*    */   private final AtomicLong reloadCount;
/*    */   
/*    */   public State(String name, AtomicLong preparationNanos, AtomicLong preparationCount, AtomicLong reloadNanos, AtomicLong reloadCount) {
/* 91 */     this.name = name;
/* 91 */     this.preparationNanos = preparationNanos;
/* 91 */     this.preparationCount = preparationCount;
/* 91 */     this.reloadNanos = reloadNanos;
/* 91 */     this.reloadCount = reloadCount;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #91	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/resources/ProfiledReloadInstance$State;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 91 */     return this.name;
/*    */   }
/*    */   
/*    */   public AtomicLong preparationNanos() {
/* 91 */     return this.preparationNanos;
/*    */   }
/*    */   
/*    */   public AtomicLong preparationCount() {
/* 91 */     return this.preparationCount;
/*    */   }
/*    */   
/*    */   public AtomicLong reloadNanos() {
/* 91 */     return this.reloadNanos;
/*    */   }
/*    */   
/*    */   public AtomicLong reloadCount() {
/* 91 */     return this.reloadCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\resources\ProfiledReloadInstance$State.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */