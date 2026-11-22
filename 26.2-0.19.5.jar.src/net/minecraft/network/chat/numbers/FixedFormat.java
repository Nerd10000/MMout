/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class FixedFormat extends Record implements NumberFormat {
/*    */   private final Component value;
/*    */   
/*    */   public FixedFormat(Component value) {
/* 10 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/numbers/FixedFormat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/FixedFormat;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/numbers/FixedFormat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/numbers/FixedFormat;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/numbers/FixedFormat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/numbers/FixedFormat;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component value() {
/* 10 */     return this.value;
/*    */   }
/*    */   
/* 13 */   public static final NumberFormatType<FixedFormat> TYPE = new NumberFormatType<FixedFormat>() {
/* 14 */       private static final MapCodec<FixedFormat> CODEC = ComponentSerialization.CODEC.fieldOf("value").xmap(FixedFormat::new, FixedFormat::value);
/*    */       
/* 19 */       private static final StreamCodec<RegistryFriendlyByteBuf, FixedFormat> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_STREAM_CODEC, FixedFormat::value, FixedFormat::new);
/*    */       
/*    */       public MapCodec<FixedFormat> mapCodec() {
/* 26 */         return CODEC;
/*    */       }
/*    */       
/*    */       public StreamCodec<RegistryFriendlyByteBuf, FixedFormat> streamCodec() {
/* 31 */         return STREAM_CODEC;
/*    */       }
/*    */     };
/*    */   
/*    */   public MutableComponent format(int value) {
/* 37 */     return this.value.copy();
/*    */   }
/*    */   
/*    */   public NumberFormatType<FixedFormat> type() {
/* 42 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\numbers\FixedFormat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */