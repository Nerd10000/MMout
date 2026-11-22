/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ public final class RunnableWithPriority extends Record implements Runnable {
/*    */   private final int priority;
/*    */   
/*    */   private final Runnable task;
/*    */   
/*    */   public RunnableWithPriority(int priority, Runnable task) {
/* 47 */     this.priority = priority;
/* 47 */     this.task = task;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/thread/StrictQueue$RunnableWithPriority;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int priority() {
/* 47 */     return this.priority;
/*    */   }
/*    */   
/*    */   public Runnable task() {
/* 47 */     return this.task;
/*    */   }
/*    */   
/*    */   public void run() {
/* 50 */     this.task.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\StrictQueue$RunnableWithPriority.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */