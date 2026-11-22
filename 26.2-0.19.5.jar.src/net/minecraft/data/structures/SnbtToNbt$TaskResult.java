/*    */ package net.minecraft.data.structures;
/*    */ 
/*    */ import com.google.common.hash.HashCode;
/*    */ 
/*    */ final class TaskResult extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final byte[] payload;
/*    */   
/*    */   private final HashCode hash;
/*    */   
/*    */   private TaskResult(String name, byte[] payload, HashCode hash) {
/* 58 */     this.name = name;
/* 58 */     this.payload = payload;
/* 58 */     this.hash = hash;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #58	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/data/structures/SnbtToNbt$TaskResult;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 58 */     return this.name;
/*    */   }
/*    */   
/*    */   public byte[] payload() {
/* 58 */     return this.payload;
/*    */   }
/*    */   
/*    */   public HashCode hash() {
/* 58 */     return this.hash;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\structures\SnbtToNbt$TaskResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */