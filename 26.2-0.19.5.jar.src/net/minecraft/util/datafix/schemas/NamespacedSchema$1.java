/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.codecs.PrimitiveCodec;
/*    */ 
/*    */ class null implements PrimitiveCodec<String> {
/*    */   public <T> DataResult<String> read(DynamicOps<T> ops, T input) {
/* 28 */     return ops
/* 29 */       .getStringValue(input)
/* 30 */       .map(NamespacedSchema::ensureNamespaced);
/*    */   }
/*    */   
/*    */   public <T> T write(DynamicOps<T> ops, String value) {
/* 35 */     return (T)ops.createString(value);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 40 */     return "NamespacedString";
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\NamespacedSchema$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */