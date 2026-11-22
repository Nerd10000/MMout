/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public final class Success<T> extends Record implements ChunkResult<T> {
/*    */   private final T value;
/*    */   
/*    */   public Success(T value) {
/* 39 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkResult$Success;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkResult$Success;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Success<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkResult$Success;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #39	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Success;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Success<TT;>;
/*    */   }
/*    */   
/*    */   public T value() {
/* 39 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean isSuccess() {
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   public T orElse(T orElse) {
/* 47 */     return this.value;
/*    */   }
/*    */   
/*    */   public String getError() {
/* 52 */     return null;
/*    */   }
/*    */   
/*    */   public ChunkResult<T> ifSuccess(Consumer<T> consumer) {
/* 57 */     consumer.accept(this.value);
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public <R> ChunkResult<R> map(Function<T, R> map) {
/* 63 */     return new Success((T)map.apply(this.value));
/*    */   }
/*    */   
/*    */   public <E extends Throwable> T orElseThrow(Supplier<E> exceptionSupplier) throws E {
/* 68 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkResult$Success.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */