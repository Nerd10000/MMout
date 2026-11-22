/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.EndTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Tag> {
/*     */   public Tag decode(ByteBuf input) {
/* 264 */     Tag result = FriendlyByteBuf.readNbt(input, accounter.get());
/* 265 */     if (result == null)
/* 266 */       throw new DecoderException("Expected non-null compound tag"); 
/* 268 */     return result;
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Tag value) {
/* 273 */     if (value == EndTag.INSTANCE)
/* 274 */       throw new EncoderException("Expected non-null compound tag"); 
/* 276 */     FriendlyByteBuf.writeNbt(output, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$16.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */