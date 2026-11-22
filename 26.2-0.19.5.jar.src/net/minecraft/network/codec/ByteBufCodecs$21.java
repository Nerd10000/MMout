/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Quaternionfc> {
/*     */   public Quaternionfc decode(ByteBuf input) {
/* 381 */     return (Quaternionfc)FriendlyByteBuf.readQuaternion(input);
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Quaternionfc value) {
/* 386 */     FriendlyByteBuf.writeQuaternion(output, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$21.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */