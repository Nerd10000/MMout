/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<B, Either<L, R>> {
/*     */   public Either<L, R> decode(B input) {
/* 514 */     if (input.readBoolean())
/* 515 */       return Either.left(leftCodec.decode(input)); 
/* 517 */     return Either.right(rightCodec.decode(input));
/*     */   }
/*     */   
/*     */   public void encode(B output, Either<L, R> value) {
/* 523 */     value.ifLeft(left -> {
/*     */           output.writeBoolean(true);
/*     */           leftCodec.encode(output, left);
/* 526 */         }).ifRight(right -> {
/*     */           output.writeBoolean(false);
/*     */           rightCodec.encode(output, right);
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$26.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */