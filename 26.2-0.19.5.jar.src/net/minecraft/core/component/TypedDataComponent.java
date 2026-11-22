/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class TypedDataComponent<T> extends Record {
/*    */   private final DataComponentType<T> type;
/*    */   
/*    */   private final T value;
/*    */   
/*    */   public TypedDataComponent(DataComponentType<T> type, T value) {
/* 12 */     this.type = type;
/* 12 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/TypedDataComponent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/TypedDataComponent;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/TypedDataComponent<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/TypedDataComponent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/TypedDataComponent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/TypedDataComponent<TT;>;
/*    */   }
/*    */   
/*    */   public DataComponentType<T> type() {
/* 12 */     return this.type;
/*    */   }
/*    */   
/*    */   public T value() {
/* 12 */     return this.value;
/*    */   }
/*    */   
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, TypedDataComponent<?>> STREAM_CODEC = new StreamCodec<RegistryFriendlyByteBuf, TypedDataComponent<?>>() {
/*    */       public TypedDataComponent<?> decode(RegistryFriendlyByteBuf input) {
/* 16 */         DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/* 17 */         return decodeTyped(input, type);
/*    */       }
/*    */       
/*    */       private static <T> TypedDataComponent<T> decodeTyped(RegistryFriendlyByteBuf input, DataComponentType<T> type) {
/* 21 */         return new TypedDataComponent<>(type, (T)type.streamCodec().decode(input));
/*    */       }
/*    */       
/*    */       public void encode(RegistryFriendlyByteBuf output, TypedDataComponent<?> value) {
/* 26 */         encodeCap(output, value);
/*    */       }
/*    */       
/*    */       private static <T> void encodeCap(RegistryFriendlyByteBuf output, TypedDataComponent<T> component) {
/* 30 */         DataComponentType.STREAM_CODEC.encode(output, component.type());
/* 31 */         component.type().streamCodec().encode(output, component.value());
/*    */       }
/*    */     };
/*    */   
/*    */   static TypedDataComponent<?> fromEntryUnchecked(Map.Entry<DataComponentType<?>, Object> entry) {
/* 37 */     return createUnchecked(entry.getKey(), entry.getValue());
/*    */   }
/*    */   
/*    */   public static <T> TypedDataComponent<T> createUnchecked(DataComponentType<T> type, Object value) {
/* 42 */     return new TypedDataComponent<>(type, (T)value);
/*    */   }
/*    */   
/*    */   public void applyTo(PatchedDataComponentMap components) {
/* 46 */     components.set(this.type, this.value);
/*    */   }
/*    */   
/*    */   public <D> DataResult<D> encodeValue(DynamicOps<D> ops) {
/* 50 */     Codec<T> codec = this.type.codec();
/* 51 */     if (codec == null)
/* 52 */       return DataResult.error(() -> "Component of type " + String.valueOf(this.type) + " is not encodable"); 
/* 54 */     return codec.encodeStart(ops, this.value);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 59 */     return String.valueOf(this.type) + "=>" + String.valueOf(this.type);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\TypedDataComponent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */