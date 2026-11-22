/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ public interface NumericTag extends PrimitiveTag {
/*    */   byte byteValue();
/*    */   
/*    */   short shortValue();
/*    */   
/*    */   int intValue();
/*    */   
/*    */   long longValue();
/*    */   
/*    */   float floatValue();
/*    */   
/*    */   double doubleValue();
/*    */   
/*    */   Number box();
/*    */   
/*    */   default Optional<Number> asNumber() {
/* 22 */     return Optional.of(box());
/*    */   }
/*    */   
/*    */   default Optional<Byte> asByte() {
/* 27 */     return Optional.of(Byte.valueOf(byteValue()));
/*    */   }
/*    */   
/*    */   default Optional<Short> asShort() {
/* 32 */     return Optional.of(Short.valueOf(shortValue()));
/*    */   }
/*    */   
/*    */   default Optional<Integer> asInt() {
/* 37 */     return Optional.of(Integer.valueOf(intValue()));
/*    */   }
/*    */   
/*    */   default Optional<Long> asLong() {
/* 42 */     return Optional.of(Long.valueOf(longValue()));
/*    */   }
/*    */   
/*    */   default Optional<Float> asFloat() {
/* 47 */     return Optional.of(Float.valueOf(floatValue()));
/*    */   }
/*    */   
/*    */   default Optional<Double> asDouble() {
/* 52 */     return Optional.of(Double.valueOf(doubleValue()));
/*    */   }
/*    */   
/*    */   default Optional<Boolean> asBoolean() {
/* 57 */     return Optional.of(Boolean.valueOf((byteValue() != 0)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NumericTag.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */