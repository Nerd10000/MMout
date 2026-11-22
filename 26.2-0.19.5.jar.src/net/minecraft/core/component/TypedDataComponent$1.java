/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ class null implements StreamCodec<RegistryFriendlyByteBuf, TypedDataComponent<?>> {
/*    */   public TypedDataComponent<?> decode(RegistryFriendlyByteBuf input) {
/* 16 */     DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/* 17 */     return decodeTyped(input, type);
/*    */   }
/*    */   
/*    */   private static <T> TypedDataComponent<T> decodeTyped(RegistryFriendlyByteBuf input, DataComponentType<T> type) {
/* 21 */     return new TypedDataComponent<>(type, (T)type.streamCodec().decode(input));
/*    */   }
/*    */   
/*    */   public void encode(RegistryFriendlyByteBuf output, TypedDataComponent<?> value) {
/* 26 */     encodeCap(output, value);
/*    */   }
/*    */   
/*    */   private static <T> void encodeCap(RegistryFriendlyByteBuf output, TypedDataComponent<T> component) {
/* 30 */     DataComponentType.STREAM_CODEC.encode(output, component.type());
/* 31 */     component.type().streamCodec().encode(output, component.value());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\TypedDataComponent$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */