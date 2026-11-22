/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.network.VarInt;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, T> {
/*     */   public T decode(ByteBuf input) {
/* 579 */     int id = VarInt.read(input);
/* 580 */     return byId.apply(id);
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, T value) {
/* 585 */     int id = toId.applyAsInt(value);
/* 586 */     VarInt.write(output, id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$28.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */