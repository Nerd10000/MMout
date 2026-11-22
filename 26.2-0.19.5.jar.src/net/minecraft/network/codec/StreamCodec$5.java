/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ 
/*     */ class null implements StreamCodec<O, V> {
/*     */   public V decode(O input) {
/*  94 */     B wrappedStream = operation.apply(input);
/*  95 */     return StreamCodec.this.decode(wrappedStream);
/*     */   }
/*     */   
/*     */   public void encode(O output, V value) {
/* 100 */     B wrappedStream = operation.apply(output);
/* 101 */     StreamCodec.this.encode(wrappedStream, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\StreamCodec$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */