/*    */ package com.mojang.realmsclient.gui.task;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ 
/*    */ final class ComputationResult<T> extends Record {
/*    */   private final Either<T, Exception> value;
/*    */   
/*    */   private final long time;
/*    */   
/*    */   private ComputationResult(Either<T, Exception> value, long time) {
/* 45 */     this.value = value;
/* 45 */     this.time = time;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #45	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/gui/task/DataFetcher$ComputationResult<TT;>;
/*    */   }
/*    */   
/*    */   public Either<T, Exception> value() {
/* 45 */     return this.value;
/*    */   }
/*    */   
/*    */   public long time() {
/* 45 */     return this.time;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\task\DataFetcher$ComputationResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */