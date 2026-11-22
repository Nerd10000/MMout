/*    */ package net.minecraft.client.telemetry;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ class null extends MapCodec<TelemetryPropertyMap> {
/*    */   public <T> RecordBuilder<T> encode(TelemetryPropertyMap input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 31 */     RecordBuilder<T> result = prefix;
/* 32 */     for (TelemetryProperty<?> property : (Iterable<TelemetryProperty<?>>)properties)
/* 33 */       result = encodeProperty(input, result, property); 
/* 35 */     return result;
/*    */   }
/*    */   
/*    */   private <T, V> RecordBuilder<T> encodeProperty(TelemetryPropertyMap input, RecordBuilder<T> result, TelemetryProperty<V> property) {
/* 39 */     V value = input.get(property);
/* 40 */     if (value != null)
/* 41 */       return result.add(property.id(), value, (Encoder)property.codec()); 
/* 43 */     return result;
/*    */   }
/*    */   
/*    */   public <T> DataResult<TelemetryPropertyMap> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 48 */     DataResult<TelemetryPropertyMap.Builder> result = DataResult.success(new TelemetryPropertyMap.Builder());
/* 49 */     for (TelemetryProperty<?> property : (Iterable<TelemetryProperty<?>>)properties)
/* 50 */       result = decodeProperty(result, ops, input, property); 
/* 52 */     return result.map(TelemetryPropertyMap.Builder::build);
/*    */   }
/*    */   
/*    */   private <T, V> DataResult<TelemetryPropertyMap.Builder> decodeProperty(DataResult<TelemetryPropertyMap.Builder> result, DynamicOps<T> ops, MapLike<T> input, TelemetryProperty<V> property) {
/* 56 */     T value = (T)input.get(property.id());
/* 57 */     if (value != null) {
/* 58 */       DataResult<V> parse = property.codec().parse(ops, value);
/* 59 */       return result.apply2stable((b, v) -> b.put(property, v), parse);
/*    */     } 
/* 61 */     return result;
/*    */   }
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 66 */     Objects.requireNonNull(ops);
/* 66 */     return properties.stream().map(TelemetryProperty::id).map(ops::createString);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\TelemetryPropertyMap$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */