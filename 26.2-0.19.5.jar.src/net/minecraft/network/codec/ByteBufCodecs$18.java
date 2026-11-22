/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ 
/*     */ class null implements StreamCodec<RegistryFriendlyByteBuf, T> {
/*     */   public T decode(RegistryFriendlyByteBuf input) {
/* 340 */     Tag tag = tagCodec.decode(input);
/* 341 */     RegistryOps<Tag> ops = input.registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 342 */     return (T)codec.parse((DynamicOps)ops, tag).getOrThrow(msg -> new DecoderException("Failed to decode: " + msg + " " + String.valueOf(tag)));
/*     */   }
/*     */   
/*     */   public void encode(RegistryFriendlyByteBuf output, T value) {
/* 347 */     RegistryOps<Tag> ops = output.registryAccess().createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 348 */     Tag tag = (Tag)codec.encodeStart((DynamicOps)ops, value).getOrThrow(msg -> new EncoderException("Failed to encode: " + msg + " " + String.valueOf(value)));
/* 349 */     tagCodec.encode(output, tag);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$18.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */