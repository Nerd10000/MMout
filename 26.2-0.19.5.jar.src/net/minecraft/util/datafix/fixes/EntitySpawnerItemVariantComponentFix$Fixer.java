/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ @FunctionalInterface
/*    */ interface Fixer extends Function<Typed<?>, Typed<?>> {
/*    */   default Typed<?> apply(Typed<?> components) {
/* 56 */     return components.update(DSL.remainderFinder(), this::fixRemainder);
/*    */   }
/*    */   
/*    */   default <T> Dynamic<T> fixRemainder(Dynamic<T> remainder) {
/* 60 */     return remainder.get("minecraft:bucket_entity_data").result().map(bucketData -> fixRemainder(remainder, bucketData)).orElse(remainder);
/*    */   }
/*    */   
/*    */   <T> Dynamic<T> fixRemainder(Dynamic<T> paramDynamic1, Dynamic<T> paramDynamic2);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntitySpawnerItemVariantComponentFix$Fixer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */