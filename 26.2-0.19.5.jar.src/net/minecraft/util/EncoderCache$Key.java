/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ 
/*    */ final class Key<A, T> extends Record {
/*    */   private final Codec<A> codec;
/*    */   
/*    */   private final A value;
/*    */   
/*    */   private final DynamicOps<T> ops;
/*    */   
/*    */   private Key(Codec<A> codec, A value, DynamicOps<T> ops) {
/* 51 */     this.codec = codec;
/* 51 */     this.value = value;
/* 51 */     this.ops = ops;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/EncoderCache$Key;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #51	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/EncoderCache$Key;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/EncoderCache$Key<TA;TT;>;
/*    */   }
/*    */   
/*    */   public Codec<A> codec() {
/* 51 */     return this.codec;
/*    */   }
/*    */   
/*    */   public A value() {
/* 51 */     return this.value;
/*    */   }
/*    */   
/*    */   public DynamicOps<T> ops() {
/* 51 */     return this.ops;
/*    */   }
/*    */   
/*    */   public DataResult<T> resolve() {
/* 53 */     return this.codec.encodeStart(this.ops, this.value);
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 58 */     if (this == obj)
/* 59 */       return true; 
/* 61 */     if (obj instanceof Key) {
/* 61 */       Key<?, ?> key = (Key<?, ?>)obj;
/* 62 */       return (this.codec == key.codec && this.value.equals(key.value) && this.ops.equals(key.ops));
/*    */     } 
/* 64 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 69 */     int result = System.identityHashCode(this.codec);
/* 70 */     result = 31 * result + this.value.hashCode();
/* 71 */     result = 31 * result + this.ops.hashCode();
/* 72 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\EncoderCache$Key.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */