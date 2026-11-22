/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ public final class Fail<T> extends Record implements ChunkResult<T> {
/*     */   private final Supplier<String> error;
/*     */   
/*     */   public Fail(Supplier<String> error) {
/*  72 */     this.error = error;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkResult$Fail;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #72	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkResult$Fail;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #72	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkResult$Fail;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #72	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Fail;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/server/level/ChunkResult$Fail<TT;>;
/*     */   }
/*     */   
/*     */   public Supplier<String> error() {
/*  72 */     return this.error;
/*     */   }
/*     */   
/*     */   public boolean isSuccess() {
/*  75 */     return false;
/*     */   }
/*     */   
/*     */   public T orElse(T orElse) {
/*  80 */     return orElse;
/*     */   }
/*     */   
/*     */   public String getError() {
/*  85 */     return this.error.get();
/*     */   }
/*     */   
/*     */   public ChunkResult<T> ifSuccess(Consumer<T> consumer) {
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public <R> ChunkResult<R> map(Function<T, R> map) {
/*  95 */     return new Fail(this.error);
/*     */   }
/*     */   
/*     */   public <E extends Throwable> T orElseThrow(Supplier<E> exceptionSupplier) throws E {
/* 100 */     throw (E)exceptionSupplier.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkResult$Fail.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */