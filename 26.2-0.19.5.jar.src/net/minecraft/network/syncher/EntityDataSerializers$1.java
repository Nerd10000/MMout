/*    */ package net.minecraft.network.syncher;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ class null implements EntityDataSerializer<ItemStack> {
/*    */   public StreamCodec<? super RegistryFriendlyByteBuf, ItemStack> codec() {
/* 72 */     return ItemStack.OPTIONAL_STREAM_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack copy(ItemStack value) {
/* 77 */     return value.copy();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\syncher\EntityDataSerializers$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */