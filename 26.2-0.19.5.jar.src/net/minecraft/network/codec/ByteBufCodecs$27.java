/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.network.VarInt;
/*     */ 
/*     */ class null implements StreamCodec<B, V> {
/*     */   public V decode(B input) {
/* 538 */     int size = VarInt.read((ByteBuf)input);
/* 539 */     if (size > maxSize)
/* 540 */       throw new DecoderException("Buffer size " + size + " is larger than allowed limit of " + maxSize); 
/* 542 */     int index = input.readerIndex();
/* 543 */     ByteBuf byteBuf = decorator.apply(input, input.slice(index, size));
/* 544 */     input.readerIndex(index + size);
/* 545 */     return original.decode(byteBuf);
/*     */   }
/*     */   
/*     */   public void encode(B output, V value) {
/* 550 */     ByteBuf byteBuf = decorator.apply(output, output.alloc().buffer());
/*     */     try {
/* 552 */       original.encode(byteBuf, value);
/* 553 */       int size = byteBuf.readableBytes();
/* 554 */       if (size > maxSize)
/* 555 */         throw new EncoderException("Buffer size " + size + " is  larger than allowed limit of " + maxSize); 
/* 557 */       VarInt.write((ByteBuf)output, size);
/* 558 */       output.writeBytes(byteBuf);
/*     */     } finally {
/* 560 */       byteBuf.release();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$27.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */