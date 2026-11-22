/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Optional<Tag>> {
/*     */   public Optional<Tag> decode(ByteBuf input) {
/* 250 */     return Optional.ofNullable(FriendlyByteBuf.readNbt(input, accounter.get()));
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Optional<Tag> value) {
/* 255 */     FriendlyByteBuf.writeNbt(output, value.orElse(null));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$15.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */