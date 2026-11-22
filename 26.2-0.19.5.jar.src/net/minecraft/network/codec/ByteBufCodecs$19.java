/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ 
/*     */ class null implements StreamCodec<ByteBuf, Optional<CompoundTag>> {
/*     */   public Optional<CompoundTag> decode(ByteBuf input) {
/* 357 */     return Optional.ofNullable(FriendlyByteBuf.readNbt(input));
/*     */   }
/*     */   
/*     */   public void encode(ByteBuf output, Optional<CompoundTag> value) {
/* 362 */     FriendlyByteBuf.writeNbt(output, (Tag)value.orElse(null));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$19.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */