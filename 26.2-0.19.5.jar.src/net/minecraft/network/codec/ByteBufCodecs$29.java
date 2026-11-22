/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ class null implements StreamCodec<RegistryFriendlyByteBuf, R> {
/*     */   private IdMap<R> getRegistryOrThrow(RegistryFriendlyByteBuf input) {
/* 598 */     return mapExtractor.apply(input.registryAccess().lookupOrThrow(registryKey));
/*     */   }
/*     */   
/*     */   public R decode(RegistryFriendlyByteBuf input) {
/* 603 */     int id = VarInt.read((ByteBuf)input);
/* 604 */     return (R)getRegistryOrThrow(input).byIdOrThrow(id);
/*     */   }
/*     */   
/*     */   public void encode(RegistryFriendlyByteBuf output, R value) {
/* 609 */     int id = getRegistryOrThrow(output).getIdOrThrow(value);
/* 610 */     VarInt.write((ByteBuf)output, id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$29.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */