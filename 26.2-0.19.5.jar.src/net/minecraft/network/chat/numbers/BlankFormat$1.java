/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements NumberFormatType<BlankFormat> {
/* 13 */   private static final MapCodec<BlankFormat> CODEC = MapCodec.unit(BlankFormat.INSTANCE);
/*    */   
/* 15 */   private static final StreamCodec<RegistryFriendlyByteBuf, BlankFormat> STREAM_CODEC = StreamCodec.unit(BlankFormat.INSTANCE);
/*    */   
/*    */   public MapCodec<BlankFormat> mapCodec() {
/* 19 */     return CODEC;
/*    */   }
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, BlankFormat> streamCodec() {
/* 24 */     return STREAM_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\numbers\BlankFormat$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */