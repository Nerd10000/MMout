/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class StyledFormat extends Record implements NumberFormat {
/*    */   private final Style style;
/*    */   
/*    */   public StyledFormat(Style style) {
/* 11 */     this.style = style;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/numbers/StyledFormat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/StyledFormat;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/numbers/StyledFormat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/StyledFormat;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/numbers/StyledFormat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/numbers/StyledFormat;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Style style() {
/* 11 */     return this.style;
/*    */   }
/*    */   
/* 14 */   public static final NumberFormatType<StyledFormat> TYPE = new NumberFormatType<StyledFormat>() {
/* 15 */       private static final MapCodec<StyledFormat> CODEC = Style.Serializer.MAP_CODEC.xmap(StyledFormat::new, StyledFormat::style);
/*    */       
/* 17 */       private static final StreamCodec<RegistryFriendlyByteBuf, StyledFormat> STREAM_CODEC = StreamCodec.composite(Style.Serializer.TRUSTED_STREAM_CODEC, StyledFormat::style, StyledFormat::new);
/*    */       
/*    */       public MapCodec<StyledFormat> mapCodec() {
/* 24 */         return CODEC;
/*    */       }
/*    */       
/*    */       public StreamCodec<RegistryFriendlyByteBuf, StyledFormat> streamCodec() {
/* 29 */         return STREAM_CODEC;
/*    */       }
/*    */     };
/*    */   
/* 33 */   public static final StyledFormat NO_STYLE = new StyledFormat(Style.EMPTY);
/*    */   
/* 34 */   public static final StyledFormat SIDEBAR_DEFAULT = new StyledFormat(Style.EMPTY.withColor(ChatFormatting.RED));
/*    */   
/* 35 */   public static final StyledFormat PLAYER_LIST_DEFAULT = new StyledFormat(Style.EMPTY.withColor(ChatFormatting.YELLOW));
/*    */   
/*    */   public MutableComponent format(int value) {
/* 39 */     return Component.literal(Integer.toString(value)).withStyle(this.style);
/*    */   }
/*    */   
/*    */   public NumberFormatType<StyledFormat> type() {
/* 44 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\numbers\StyledFormat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */