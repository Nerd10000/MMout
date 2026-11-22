/*    */ package com.mojang.realmsclient.gui.task;
/*    */ 
/*    */ final class SuccessfulComputationResult<T> extends Record {
/*    */   private final T value;
/*    */   
/*    */   private final long time;
/*    */   
/*    */   private SuccessfulComputationResult(T value, long time) {
/* 47 */     this.value = value;
/* 47 */     this.time = time;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #47	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$SuccessfulComputationResult<TT;>;
/*    */   }
/*    */   
/*    */   public T value() {
/* 47 */     return this.value;
/*    */   }
/*    */   
/*    */   public long time() {
/* 47 */     return this.time;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\task\DataFetcher$SuccessfulComputationResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */