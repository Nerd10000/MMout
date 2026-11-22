/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements NumberFormatType<StyledFormat> {
/* 15 */   private static final MapCodec<StyledFormat> CODEC = Style.Serializer.MAP_CODEC.xmap(StyledFormat::new, StyledFormat::style);
/*    */   
/* 17 */   private static final StreamCodec<RegistryFriendlyByteBuf, StyledFormat> STREAM_CODEC = StreamCodec.composite(Style.Serializer.TRUSTED_STREAM_CODEC, StyledFormat::style, StyledFormat::new);
/*    */   
/*    */   public MapCodec<StyledFormat> mapCodec() {
/* 24 */     return CODEC;
/*    */   }
/*    */   
/*    */   public StreamCodec<RegistryFriendlyByteBuf, StyledFormat> streamCodec() {
/* 29 */     return STREAM_CODEC;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\numbers\StyledFormat$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */