/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ final class LastUpdate extends Record {
/*    */   private final int count;
/*    */   
/*    */   private final int age;
/*    */   
/*    */   private LastUpdate(int count, int age) {
/* 45 */     this.count = count;
/* 45 */     this.age = age;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/debug/NeighborsUpdateRenderer$LastUpdate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int count() {
/* 45 */     return this.count;
/*    */   }
/*    */   
/*    */   public int age() {
/* 45 */     return this.age;
/*    */   }
/*    */   
/* 46 */   private static final LastUpdate NONE = new LastUpdate(0, 2147483647);
/*    */   
/*    */   public LastUpdate tryCount(int age) {
/* 50 */     if (age == this.age)
/* 51 */       return new LastUpdate(this.count + 1, age); 
/* 54 */     if (age < this.age)
/* 55 */       return new LastUpdate(1, age); 
/* 57 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\NeighborsUpdateRenderer$LastUpdate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */