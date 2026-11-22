/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Vector3fc> {
/*     */   public Vector3fc decode(ByteBuf input) {
/* 369 */     return (Vector3fc)FriendlyByteBuf.readVector3f(input);
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Vector3fc value) {
/* 374 */     FriendlyByteBuf.writeVector3f(output, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$20.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */