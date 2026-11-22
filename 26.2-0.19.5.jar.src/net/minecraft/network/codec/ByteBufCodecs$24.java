/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Collection;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ class null implements StreamCodec<B, C> {
/*     */   public C decode(B input) {
/* 447 */     int count = ByteBufCodecs.readCount((ByteBuf)input, maxSize);
/* 448 */     Collection collection = constructor.apply(Math.min(count, 65536));
/* 449 */     for (int i = 0; i < count; i++)
/* 450 */       collection.add(elementCodec.decode(input)); 
/* 452 */     return (C)collection;
/*     */   }
/*     */   
/*     */   public void encode(B output, C value) {
/* 457 */     ByteBufCodecs.writeCount((ByteBuf)output, value.size(), maxSize);
/* 458 */     for (V element : value)
/* 459 */       elementCodec.encode(output, element); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$24.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */