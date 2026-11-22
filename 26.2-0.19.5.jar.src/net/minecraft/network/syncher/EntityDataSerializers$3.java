/*     */ package net.minecraft.network.syncher;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, OptionalInt> {
/*     */   public OptionalInt decode(ByteBuf input) {
/* 129 */     int v = VarInt.read(input);
/* 130 */     return (v == 0) ? OptionalInt.empty() : OptionalInt.of(v - 1);
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, OptionalInt value) {
/* 135 */     VarInt.write(output, value.orElse(-1) + 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\syncher\EntityDataSerializers$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */