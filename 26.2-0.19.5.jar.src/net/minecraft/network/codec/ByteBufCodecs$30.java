/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.IdMap;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ class null implements StreamCodec<RegistryFriendlyByteBuf, Holder<T>> {
/*     */   private static final int DIRECT_HOLDER_ID = 0;
/*     */   
/*     */   private IdMap<Holder<T>> getRegistryOrThrow(RegistryFriendlyByteBuf input) {
/* 628 */     return input.registryAccess().lookupOrThrow(registryKey).asHolderIdMap();
/*     */   }
/*     */   
/*     */   public Holder<T> decode(RegistryFriendlyByteBuf input) {
/* 635 */     int id = VarInt.read((ByteBuf)input);
/* 636 */     if (id == 0)
/* 637 */       return Holder.direct(directCodec.decode(input)); 
/* 639 */     return (Holder<T>)getRegistryOrThrow(input).byIdOrThrow(id - 1);
/*     */   }
/*     */   
/*     */   public void encode(RegistryFriendlyByteBuf output, Holder<T> holder) {
/*     */     int id;
/* 644 */     switch (ByteBufCodecs.null.$SwitchMap$net$minecraft$core$Holder$Kind[holder.kind().ordinal()]) {
/*     */       case 1:
/* 646 */         id = getRegistryOrThrow(output).getIdOrThrow(holder);
/* 647 */         VarInt.write((ByteBuf)output, id + 1);
/*     */         break;
/*     */       case 2:
/* 650 */         VarInt.write((ByteBuf)output, 0);
/* 651 */         directCodec.encode(output, holder.value());
/*     */         break;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$30.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */