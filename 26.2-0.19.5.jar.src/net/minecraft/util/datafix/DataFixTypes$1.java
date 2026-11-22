/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ 
/*    */ class null implements Codec<A> {
/*    */   public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 67 */     return codec.encode(input, ops, prefix).flatMap(data -> ops.mergeToMap(data, ops.createString("DataVersion"), ops.createInt(DataFixTypes.currentVersion())));
/*    */   }
/*    */   
/*    */   public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 75 */     Objects.requireNonNull(ops);
/* 78 */     int fromVersion = ((Integer)ops.get(input, "DataVersion").flatMap(ops::getNumberValue).map(Number::intValue).result().orElse(Integer.valueOf(defaultVersion))).intValue();
/* 79 */     Dynamic<T> dataWithoutVersion = new Dynamic(ops, ops.remove(input, "DataVersion"));
/* 80 */     Dynamic<T> fixedData = DataFixTypes.this.updateToCurrentVersion(dataFixer, dataWithoutVersion, fromVersion);
/* 81 */     return codec.decode(fixedData);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\DataFixTypes$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */