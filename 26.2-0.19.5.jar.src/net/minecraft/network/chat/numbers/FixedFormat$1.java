/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements NumberFormatType<FixedFormat> {
/* 14 */   private static final MapCodec<FixedFormat> CODEC = ComponentSerialization.CODEC.fieldOf("value").xmap(FixedFormat::new, FixedFormat::value);
/*    */   
/* 19 */   private static final StreamCodec<RegistryFriendlyByteBuf, FixedFormat> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, FixedFormat::value, FixedFormat::new);
/*    */   
/*    */   public MapCodec<FixedFormat> mapCodec() {
/* 26 */     return CODEC;
/*    */   }
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, FixedFormat> streamCodec() {
/* 31 */     return STREAM_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\numbers\FixedFormat$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */