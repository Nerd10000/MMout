/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import io.netty.handler.codec.DecoderException;
/*     */ import io.netty.handler.codec.EncoderException;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ class null implements StreamCodec<RegistryFriendlyByteBuf, ItemStack> {
/*     */   public ItemStack decode(RegistryFriendlyByteBuf input) {
/* 173 */     ItemStack itemStack = (ItemStack)ItemStack.OPTIONAL_STREAM_CODEC.decode(input);
/* 174 */     if (itemStack.isEmpty())
/* 175 */       throw new DecoderException("Empty ItemStack not allowed"); 
/* 177 */     return itemStack;
/*     */   }
/*     */   
/*     */   public void encode(RegistryFriendlyByteBuf output, ItemStack itemStack) {
/* 182 */     if (itemStack.isEmpty())
/* 183 */       throw new EncoderException("Empty ItemStack not allowed"); 
/* 185 */     ItemStack.OPTIONAL_STREAM_CODEC.encode(output, itemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ItemStack$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */