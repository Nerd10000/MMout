/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ class null implements StreamCodec<RegistryFriendlyByteBuf, ItemStack> {
/*     */   public ItemStack decode(RegistryFriendlyByteBuf input) {
/* 146 */     int count = input.readVarInt();
/* 147 */     if (count <= 0)
/* 148 */       return ItemStack.EMPTY; 
/* 151 */     Holder<Item> item = (Holder<Item>)Item.STREAM_CODEC.decode(input);
/* 152 */     DataComponentPatch patch = (DataComponentPatch)patchCodec.decode(input);
/* 153 */     return new ItemStack(item, count, patch);
/*     */   }
/*     */   
/*     */   public void encode(RegistryFriendlyByteBuf output, ItemStack itemStack) {
/* 158 */     if (itemStack.isEmpty()) {
/* 159 */       output.writeVarInt(0);
/*     */       return;
/*     */     } 
/* 163 */     output.writeVarInt(itemStack.getCount());
/* 164 */     Item.STREAM_CODEC.encode(output, itemStack.typeHolder());
/* 165 */     patchCodec.encode(output, itemStack.components.asPatch());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\ItemStack$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */