/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class DataComponentMatchers extends Record implements Predicate<DataComponentGetter> {
/*    */   private final DataComponentExactPredicate exact;
/*    */   
/*    */   private final Map<DataComponentPredicate.Type<?>, DataComponentPredicate> partial;
/*    */   
/*    */   public DataComponentMatchers(DataComponentExactPredicate exact, Map<DataComponentPredicate.Type<?>, DataComponentPredicate> partial) {
/* 16 */     this.exact = exact;
/* 16 */     this.partial = partial;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/predicates/DataComponentMatchers;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/DataComponentMatchers;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/predicates/DataComponentMatchers;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/predicates/DataComponentMatchers;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/predicates/DataComponentMatchers;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/predicates/DataComponentMatchers;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public DataComponentExactPredicate exact() {
/* 16 */     return this.exact;
/*    */   }
/*    */   
/*    */   public Map<DataComponentPredicate.Type<?>, DataComponentPredicate> partial() {
/* 16 */     return this.partial;
/*    */   }
/*    */   
/* 20 */   public static final DataComponentMatchers ANY = new DataComponentMatchers(DataComponentExactPredicate.EMPTY, Map.of());
/*    */   
/*    */   public static final MapCodec<DataComponentMatchers> CODEC;
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DataComponentExactPredicate.CODEC.optionalFieldOf("components", DataComponentExactPredicate.EMPTY).forGetter(DataComponentMatchers::exact), (App)DataComponentPredicate.CODEC.optionalFieldOf("predicates", Map.of()).forGetter(DataComponentMatchers::partial)).apply((Applicative)i, DataComponentMatchers::new));
/*    */   }
/*    */   
/* 27 */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentMatchers> STREAM_CODEC = StreamCodec.composite(DataComponentExactPredicate.STREAM_CODEC, DataComponentMatchers::exact, DataComponentPredicate.STREAM_CODEC, DataComponentMatchers::partial, DataComponentMatchers::new);
/*    */   
/*    */   public boolean test(DataComponentGetter values) {
/* 35 */     if (!this.exact.test(values))
/* 36 */       return false; 
/* 39 */     for (DataComponentPredicate predicate : this.partial.values()) {
/* 40 */       if (!predicate.matches(values))
/* 41 */         return false; 
/*    */     } 
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 49 */     return (this.exact.isEmpty() && this.partial.isEmpty());
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 53 */     private DataComponentExactPredicate exact = DataComponentExactPredicate.EMPTY;
/*    */     
/* 54 */     private final ImmutableMap.Builder<DataComponentPredicate.Type<?>, DataComponentPredicate> partial = ImmutableMap.builder();
/*    */     
/*    */     public static Builder components() {
/* 60 */       return new Builder();
/*    */     }
/*    */     
/*    */     public <T extends DataComponentType<?>> Builder any(DataComponentType<?> type) {
/* 64 */       DataComponentPredicate.AnyValueType predicateType = DataComponentPredicate.AnyValueType.create(type);
/* 65 */       this.partial.put(predicateType, predicateType.predicate());
/* 66 */       return this;
/*    */     }
/*    */     
/*    */     public <T extends DataComponentPredicate> Builder partial(DataComponentPredicate.Type<T> type, T predicate) {
/* 70 */       this.partial.put(type, predicate);
/* 71 */       return this;
/*    */     }
/*    */     
/*    */     public Builder exact(DataComponentExactPredicate exact) {
/* 75 */       this.exact = exact;
/* 76 */       return this;
/*    */     }
/*    */     
/*    */     public DataComponentMatchers build() {
/* 80 */       return new DataComponentMatchers(this.exact, (Map<DataComponentPredicate.Type<?>, DataComponentPredicate>)this.partial.buildOrThrow());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\DataComponentMatchers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */