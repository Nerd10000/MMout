/*    */ package net.minecraft.network.codec;
/*    */ 
/*    */ final class Entry<B, V, T> extends Record {
/*    */   private final StreamCodec<? super B, ? extends V> serializer;
/*    */   
/*    */   private final T type;
/*    */   
/*    */   private Entry(StreamCodec<? super B, ? extends V> serializer, T type) {
/* 98 */     this.serializer = serializer;
/* 98 */     this.type = type;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/codec/IdDispatchCodec$Entry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #98	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry<TB;TV;TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/codec/IdDispatchCodec$Entry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #98	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry<TB;TV;TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/codec/IdDispatchCodec$Entry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #98	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/network/codec/IdDispatchCodec$Entry<TB;TV;TT;>;
/*    */   }
/*    */   
/*    */   public StreamCodec<? super B, ? extends V> serializer() {
/* 98 */     return this.serializer;
/*    */   }
/*    */   
/*    */   public T type() {
/* 98 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\IdDispatchCodec$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */