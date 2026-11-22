/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ final class Container<K, U, V> extends Record {
/*     */   private final BiFunction<K, U, V> operation;
/*     */   
/*     */   private final Object[] keys;
/*     */   
/*     */   private final Object[] values;
/*     */   
/*     */   private Container(BiFunction<K, U, V> operation, Object[] keys, Object[] values) {
/*  52 */     this.operation = operation;
/*  52 */     this.keys = keys;
/*  52 */     this.values = values;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #52	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #52	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/thread/ParallelMapTransform$Container;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #52	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/thread/ParallelMapTransform$Container<TK;TU;TV;>;
/*     */   }
/*     */   
/*     */   public BiFunction<K, U, V> operation() {
/*  52 */     return this.operation;
/*     */   }
/*     */   
/*     */   public Object[] keys() {
/*  52 */     return this.keys;
/*     */   }
/*     */   
/*     */   public Object[] values() {
/*  52 */     return this.values;
/*     */   }
/*     */   
/*     */   public Container(BiFunction<K, U, V> operation, int size) {
/*  60 */     this(operation, new Object[size], new Object[size]);
/*     */   }
/*     */   
/*     */   public void put(int index, K key, U input) {
/*  68 */     this.keys[index] = key;
/*  69 */     this.values[index] = input;
/*     */   }
/*     */   
/*     */   private K key(int index) {
/*  74 */     return (K)this.keys[index];
/*     */   }
/*     */   
/*     */   private V output(int index) {
/*  79 */     return (V)this.values[index];
/*     */   }
/*     */   
/*     */   private U input(int index) {
/*  84 */     return (U)this.values[index];
/*     */   }
/*     */   
/*     */   public void applyOperation(int index) {
/*  89 */     this.values[index] = this.operation.apply(key(index), input(index));
/*     */   }
/*     */   
/*     */   public void copyOut(int index, Map<K, V> output) {
/*  93 */     V value = output(index);
/*  94 */     if (value != null) {
/*  95 */       K key = key(index);
/*  96 */       output.put(key, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int size() {
/* 101 */     return this.keys.length;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\thread\ParallelMapTransform$Container.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */