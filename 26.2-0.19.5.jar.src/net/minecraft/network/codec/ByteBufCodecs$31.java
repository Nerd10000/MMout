/*     */ package net.minecraft.network.codec;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.VarInt;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ class null implements StreamCodec<RegistryFriendlyByteBuf, HolderSet<T>> {
/*     */   private static final int NAMED_SET = -1;
/*     */   
/* 662 */   private final StreamCodec<RegistryFriendlyByteBuf, Holder<T>> holderCodec = ByteBufCodecs.holderRegistry(registryKey);
/*     */   
/*     */   public HolderSet<T> decode(RegistryFriendlyByteBuf input) {
/* 666 */     int count = VarInt.read((ByteBuf)input) - 1;
/* 667 */     if (count == -1) {
/* 668 */       Registry<T> registry = input.registryAccess().lookupOrThrow(registryKey);
/* 669 */       return registry.get(TagKey.create(registryKey, Identifier.STREAM_CODEC.decode(input))).orElseThrow();
/*     */     } 
/* 671 */     List<Holder<T>> holders = new ArrayList<>(Math.min(count, 65536));
/* 672 */     for (int i = 0; i < count; i++)
/* 673 */       holders.add(this.holderCodec.decode(input)); 
/* 675 */     return (HolderSet<T>)HolderSet.direct(holders);
/*     */   }
/*     */   
/*     */   public void encode(RegistryFriendlyByteBuf output, HolderSet<T> value) {
/* 680 */     Optional<TagKey<T>> key = value.unwrapKey();
/* 681 */     if (key.isPresent()) {
/* 682 */       VarInt.write((ByteBuf)output, 0);
/* 683 */       Identifier.STREAM_CODEC.encode(output, ((TagKey)key.get()).location());
/*     */     } else {
/* 685 */       VarInt.write((ByteBuf)output, value.size() + 1);
/* 686 */       for (Holder<T> holder : value)
/* 687 */         this.holderCodec.encode(output, holder); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\codec\ByteBufCodecs$31.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */