/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public final class MemoryMap implements Iterable<MemoryMap.Value<?>> {
/*    */   private static final Codec<MemoryModuleType<?>> SERIALIZABLE_MEMORY_MODULE_CODEC;
/*    */   
/*    */   public static final Codec<MemoryMap> CODEC;
/*    */   
/*    */   static {
/* 17 */     SERIALIZABLE_MEMORY_MODULE_CODEC = BuiltInRegistries.MEMORY_MODULE_TYPE.byNameCodec().validate(type -> type.canSerialize() ? DataResult.success(type) : DataResult.error(()));
/* 24 */     CODEC = Codec.dispatchedMap(SERIALIZABLE_MEMORY_MODULE_CODEC, type -> (Codec)type.getCodec().orElseThrow()).xmap(MemoryMap::new, m -> m.memories);
/*    */   }
/*    */   
/* 26 */   public static final MemoryMap EMPTY = new MemoryMap(Map.of());
/*    */   
/*    */   private final Map<MemoryModuleType<?>, ExpirableValue<?>> memories;
/*    */   
/*    */   private MemoryMap(Map<MemoryModuleType<?>, ExpirableValue<?>> memories) {
/* 31 */     this.memories = Map.copyOf(memories);
/*    */   }
/*    */   
/*    */   public static MemoryMap of(Stream<Value<?>> memories) {
/* 35 */     return new MemoryMap(memories.collect(Collectors.toMap(Value::type, Value::value)));
/*    */   }
/*    */   
/*    */   public <U> ExpirableValue<U> get(MemoryModuleType<U> type) {
/* 40 */     return (ExpirableValue<U>)this.memories.get(type);
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 45 */     if (obj instanceof MemoryMap) {
/* 45 */       MemoryMap map = (MemoryMap)obj;
/* 45 */       if (this.memories.equals(map.memories));
/*    */     } 
/* 45 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 50 */     return this.memories.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 55 */     return this.memories.toString();
/*    */   }
/*    */   
/*    */   public Iterator<Value<?>> iterator() {
/* 60 */     return Iterators.transform(this.memories.entrySet().iterator(), entry -> Value.createUnchecked((MemoryModuleType)entry.getKey(), (ExpirableValue)entry.getValue()));
/*    */   }
/*    */   
/*    */   public static final class Value<U> extends Record {
/*    */     private final MemoryModuleType<U> type;
/*    */     
/*    */     private final ExpirableValue<U> value;
/*    */     
/*    */     public Value(MemoryModuleType<U> type, ExpirableValue<U> value) {
/* 65 */       this.type = type;
/* 65 */       this.value = value;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value<TU;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value<TU;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #65	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/ai/memory/MemoryMap$Value<TU;>;
/*    */     }
/*    */     
/*    */     public MemoryModuleType<U> type() {
/* 65 */       return this.type;
/*    */     }
/*    */     
/*    */     public ExpirableValue<U> value() {
/* 65 */       return this.value;
/*    */     }
/*    */     
/*    */     public static <U> Value<U> createUnchecked(MemoryModuleType<U> type, ExpirableValue<?> value) {
/* 68 */       return new Value<>(type, (ExpirableValue)value);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Builder {
/*    */     private final ImmutableMap.Builder<MemoryModuleType<?>, ExpirableValue<?>> builder;
/*    */     
/*    */     public Builder() {
/* 73 */       this.builder = ImmutableMap.builder();
/*    */     }
/*    */     
/*    */     public <U> Builder add(MemoryModuleType<U> type, ExpirableValue<U> value) {
/* 76 */       this.builder.put(type, value);
/* 77 */       return this;
/*    */     }
/*    */     
/*    */     public MemoryMap build() {
/* 81 */       return new MemoryMap((Map<MemoryModuleType<?>, ExpirableValue<?>>)this.builder.buildOrThrow());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\memory\MemoryMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */