/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.util.NullOps;
/*     */ import net.minecraft.util.Unit;
/*     */ 
/*     */ class null implements StreamCodec<RegistryFriendlyByteBuf, ItemStack> {
/*     */   public ItemStack decode(RegistryFriendlyByteBuf input) {
/* 199 */     ItemStack itemStack = (ItemStack)codec.decode(input);
/* 200 */     if (!itemStack.isEmpty()) {
/* 201 */       RegistryOps<Unit> ops = input.registryAccess().createSerializationContext((DynamicOps)NullOps.INSTANCE);
/* 202 */       ItemStack.CODEC.encodeStart((DynamicOps)ops, itemStack).getOrThrow(io.netty.handler.codec.DecoderException::new);
/*     */     } 
/* 204 */     return itemStack;
/*     */   }
/*     */   
/*     */   public void encode(RegistryFriendlyByteBuf output, ItemStack value) {
/* 209 */     codec.encode(output, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ItemStack$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */