/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ 
/*     */ public final class DataComponentPatch {
/*  24 */   public static final DataComponentPatch EMPTY = new DataComponentPatch(Reference2ObjectMaps.emptyMap());
/*     */   
/*     */   public static final Codec<DataComponentPatch> CODEC;
/*     */   
/*     */   static {
/*  26 */     CODEC = Codec.dispatchedMap(PatchKey.CODEC, PatchKey::valueCodec).xmap(data -> {
/*     */           if (data.isEmpty())
/*     */             return EMPTY; 
/*     */           Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(data.size());
/*     */           for (Map.Entry<PatchKey, ?> entry : (Iterable<Map.Entry<PatchKey, ?>>)data.entrySet()) {
/*     */             PatchKey key = entry.getKey();
/*     */             if (key.removed()) {
/*     */               reference2ObjectArrayMap.put(key.type(), Optional.empty());
/*     */               continue;
/*     */             } 
/*     */             reference2ObjectArrayMap.put(key.type(), Optional.of(entry.getValue()));
/*     */           } 
/*     */           return new DataComponentPatch((Reference2ObjectMap<DataComponentType<?>, Optional<?>>)reference2ObjectArrayMap);
/*     */         }patch -> {
/*     */           Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(patch.map.size());
/*     */           ObjectIterator<Map.Entry<DataComponentType<?>, Optional<?>>> objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator();
/*     */           while (objectIterator.hasNext()) {
/*     */             Map.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/*     */             DataComponentType<?> type = entry.getKey();
/*     */             if (type.isTransient())
/*     */               continue; 
/*     */             Optional<?> value = entry.getValue();
/*     */             if (value.isPresent()) {
/*     */               reference2ObjectArrayMap.put(new PatchKey(type, false), value.get());
/*     */               continue;
/*     */             } 
/*     */             reference2ObjectArrayMap.put(new PatchKey(type, true), Unit.INSTANCE);
/*     */           } 
/*     */           return (Map)reference2ObjectArrayMap;
/*     */         });
/*     */   }
/*     */   
/*  65 */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> STREAM_CODEC = createStreamCodec(new CodecGetter() {
/*     */         public <T> StreamCodec<RegistryFriendlyByteBuf, T> apply(DataComponentType<T> type) {
/*  68 */           return type.streamCodec().cast();
/*     */         }
/*     */       });
/*     */   
/*  74 */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> DELIMITED_STREAM_CODEC = createStreamCodec(new CodecGetter() {
/*     */         public <T> StreamCodec<RegistryFriendlyByteBuf, T> apply(DataComponentType<T> type) {
/*  77 */           StreamCodec<RegistryFriendlyByteBuf, T> original = type.streamCodec().cast();
/*  78 */           return original.apply(ByteBufCodecs.registryFriendlyLengthPrefixed(2147483647));
/*     */         }
/*     */       });
/*     */   
/*     */   private static final String REMOVED_PREFIX = "!";
/*     */   
/*     */   final Reference2ObjectMap<DataComponentType<?>, Optional<?>> map;
/*     */   
/*     */   private static StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> createStreamCodec(final CodecGetter codecGetter) {
/*  83 */     return new StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch>() {
/*     */         public DataComponentPatch decode(RegistryFriendlyByteBuf input) {
/*  86 */           int positiveCount = input.readVarInt();
/*  87 */           int negativeCount = input.readVarInt();
/*  88 */           if (positiveCount == 0 && negativeCount == 0)
/*  89 */             return DataComponentPatch.EMPTY; 
/*  92 */           int expectedSize = positiveCount + negativeCount;
/*  93 */           Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(Math.min(expectedSize, 65536));
/*     */           int i;
/*  94 */           for (i = 0; i < positiveCount; i++) {
/*  95 */             DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/*  96 */             Object value = codecGetter.<T>apply((DataComponentType)type).decode(input);
/*  97 */             reference2ObjectArrayMap.put(type, Optional.of(value));
/*     */           } 
/* 100 */           for (i = 0; i < negativeCount; i++) {
/* 101 */             DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/* 102 */             reference2ObjectArrayMap.put(type, Optional.empty());
/*     */           } 
/* 105 */           return new DataComponentPatch((Reference2ObjectMap<DataComponentType<?>, Optional<?>>)reference2ObjectArrayMap);
/*     */         }
/*     */         
/*     */         public void encode(RegistryFriendlyByteBuf output, DataComponentPatch patch) {
/* 110 */           if (patch.isEmpty()) {
/* 111 */             output.writeVarInt(0);
/* 112 */             output.writeVarInt(0);
/*     */             return;
/*     */           } 
/* 116 */           int positiveCount = 0;
/* 117 */           int negativeCount = 0;
/*     */           ObjectIterator<Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>>> objectIterator;
/* 118 */           for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) {
/* 118 */             Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 119 */             if (((Optional)entry.getValue()).isPresent()) {
/* 120 */               positiveCount++;
/*     */               continue;
/*     */             } 
/* 122 */             negativeCount++;
/*     */           } 
/* 126 */           output.writeVarInt(positiveCount);
/* 127 */           output.writeVarInt(negativeCount);
/* 128 */           for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) {
/* 128 */             Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 129 */             Optional<?> value = (Optional)entry.getValue();
/* 130 */             if (value.isPresent()) {
/* 131 */               DataComponentType<?> type = (DataComponentType)entry.getKey();
/* 132 */               DataComponentType.STREAM_CODEC.encode(output, type);
/* 133 */               encodeComponent(output, type, value.get());
/*     */             } 
/*     */           } 
/* 137 */           for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) {
/* 137 */             Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 138 */             if (((Optional)entry.getValue()).isEmpty()) {
/* 139 */               DataComponentType<?> type = (DataComponentType)entry.getKey();
/* 140 */               DataComponentType.STREAM_CODEC.encode(output, type);
/*     */             } 
/*     */           } 
/*     */         }
/*     */         
/*     */         private <T> void encodeComponent(RegistryFriendlyByteBuf output, DataComponentType<T> type, Object value) {
/* 147 */           codecGetter.<T>apply(type).encode(output, value);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   DataComponentPatch(Reference2ObjectMap<DataComponentType<?>, Optional<?>> map) {
/* 159 */     this.map = map;
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/* 163 */     return new Builder();
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentGetter prototype, DataComponentType<? extends T> type) {
/* 167 */     return getFromPatchAndPrototype(this.map, prototype, type);
/*     */   }
/*     */   
/*     */   static <T> T getFromPatchAndPrototype(Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch, DataComponentGetter prototype, DataComponentType<? extends T> type) {
/* 173 */     Optional<? extends T> value = (Optional<? extends T>)patch.get(type);
/* 174 */     if (value != null)
/* 175 */       return value.orElse(null); 
/* 177 */     return prototype.get(type);
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<DataComponentType<?>, Optional<?>>> entrySet() {
/* 181 */     return (Set<Map.Entry<DataComponentType<?>, Optional<?>>>)this.map.entrySet();
/*     */   }
/*     */   
/*     */   public int size() {
/* 185 */     return this.map.size();
/*     */   }
/*     */   
/*     */   public DataComponentPatch forget(Predicate<DataComponentType<?>> test) {
/* 189 */     if (isEmpty())
/* 190 */       return EMPTY; 
/* 193 */     Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(this.map);
/* 194 */     reference2ObjectArrayMap.keySet().removeIf(test);
/* 196 */     if (reference2ObjectArrayMap.isEmpty())
/* 197 */       return EMPTY; 
/* 199 */     return new DataComponentPatch((Reference2ObjectMap<DataComponentType<?>, Optional<?>>)reference2ObjectArrayMap);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 203 */     return this.map.isEmpty();
/*     */   }
/*     */   
/*     */   public static final class SplitResult extends Record {
/*     */     private final DataComponentMap added;
/*     */     
/*     */     private final Set<DataComponentType<?>> removed;
/*     */     
/*     */     public SplitResult(DataComponentMap added, Set<DataComponentType<?>> removed) {
/* 206 */       this.added = added;
/* 206 */       this.removed = removed;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentPatch$SplitResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #206	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$SplitResult;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentPatch$SplitResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #206	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$SplitResult;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentPatch$SplitResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #206	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentPatch$SplitResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public DataComponentMap added() {
/* 206 */       return this.added;
/*     */     }
/*     */     
/*     */     public Set<DataComponentType<?>> removed() {
/* 206 */       return this.removed;
/*     */     }
/*     */     
/* 207 */     public static final SplitResult EMPTY = new SplitResult(DataComponentMap.EMPTY, Set.of());
/*     */   }
/*     */   
/*     */   public SplitResult split() {
/* 211 */     if (isEmpty())
/* 212 */       return SplitResult.EMPTY; 
/* 215 */     DataComponentMap.Builder added = DataComponentMap.builder();
/* 216 */     Set<DataComponentType<?>> removed = Sets.newIdentityHashSet();
/* 218 */     this.map.forEach((type, optionalValue) -> {
/*     */           if (optionalValue.isPresent()) {
/*     */             added.setUnchecked(type, optionalValue.get());
/*     */           } else {
/*     */             removed.add(type);
/*     */           } 
/*     */         });
/* 226 */     return new SplitResult(added.build(), removed);
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 231 */     if (this == obj)
/* 232 */       return true; 
/* 234 */     if (obj instanceof DataComponentPatch) {
/* 234 */       DataComponentPatch patch = (DataComponentPatch)obj;
/* 234 */       if (this.map.equals(patch.map));
/*     */     } 
/* 234 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 239 */     return this.map.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 244 */     return toString(this.map);
/*     */   }
/*     */   
/*     */   static String toString(Reference2ObjectMap<DataComponentType<?>, Optional<?>> map) {
/* 249 */     StringBuilder builder = new StringBuilder();
/* 250 */     builder.append('{');
/* 251 */     boolean first = true;
/* 252 */     for (ObjectIterator<Map.Entry<DataComponentType<?>, Optional<?>>> objectIterator = Reference2ObjectMaps.fastIterable(map).iterator(); objectIterator.hasNext(); ) {
/* 252 */       Map.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 253 */       if (first) {
/* 254 */         first = false;
/*     */       } else {
/* 256 */         builder.append(", ");
/*     */       } 
/* 258 */       Optional<?> value = entry.getValue();
/* 259 */       if (value.isPresent()) {
/* 260 */         builder.append(entry.getKey());
/* 261 */         builder.append("=>");
/* 262 */         builder.append(value.get());
/*     */         continue;
/*     */       } 
/* 264 */       builder.append("!");
/* 265 */       builder.append(entry.getKey());
/*     */     } 
/* 268 */     builder.append('}');
/* 269 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private static final class PatchKey extends Record {
/*     */     private final DataComponentType<?> type;
/*     */     
/*     */     private final boolean removed;
/*     */     
/*     */     public static final Codec<PatchKey> CODEC;
/*     */     
/*     */     private PatchKey(DataComponentType<?> type, boolean removed) {
/* 272 */       this.type = type;
/* 272 */       this.removed = removed;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentPatch$PatchKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #272	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$PatchKey;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentPatch$PatchKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #272	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentPatch$PatchKey;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentPatch$PatchKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #272	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentPatch$PatchKey;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public DataComponentType<?> type() {
/* 272 */       return this.type;
/*     */     }
/*     */     
/*     */     public boolean removed() {
/* 272 */       return this.removed;
/*     */     }
/*     */     
/*     */     static {
/* 273 */       CODEC = Codec.STRING.flatXmap(string -> {
/*     */             boolean removed = string.startsWith("!");
/*     */             if (removed)
/*     */               string = string.substring("!".length()); 
/*     */             Identifier id = Identifier.tryParse(string);
/*     */             DataComponentType<?> type = (DataComponentType)BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(id);
/*     */             return (type == null) ? DataResult.error(()) : (type.isTransient() ? DataResult.error(()) : DataResult.success(new PatchKey(type, removed)));
/*     */           }key -> {
/*     */             DataComponentType<?> type = key.type();
/*     */             Identifier id = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
/*     */             return (id == null) ? DataResult.error(()) : DataResult.success(key.removed() ? ("!" + String.valueOf(id)) : id.toString());
/*     */           });
/*     */     }
/*     */     
/*     */     public Codec<?> valueCodec() {
/* 300 */       return this.removed ? Codec.EMPTY.codec() : this.type.codecOrThrow();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 305 */     private final Reference2ObjectMap<DataComponentType<?>, Optional<?>> map = (Reference2ObjectMap<DataComponentType<?>, Optional<?>>)new Reference2ObjectArrayMap();
/*     */     
/*     */     public <T> Builder set(DataComponentType<T> type, T value) {
/* 311 */       this.map.put(type, Optional.of(value));
/* 312 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder remove(DataComponentType<T> type) {
/* 316 */       this.map.put(type, Optional.empty());
/* 317 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder set(TypedDataComponent<T> component) {
/* 321 */       return set(component.type(), component.value());
/*     */     }
/*     */     
/*     */     public <T> Builder set(Iterable<TypedDataComponent<?>> components) {
/* 325 */       for (TypedDataComponent<?> component : components)
/* 326 */         set(component); 
/* 328 */       return this;
/*     */     }
/*     */     
/*     */     public DataComponentPatch build() {
/* 332 */       if (this.map.isEmpty())
/* 333 */         return DataComponentPatch.EMPTY; 
/* 335 */       return new DataComponentPatch(this.map);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface CodecGetter {
/*     */     <T> StreamCodec<? super RegistryFriendlyByteBuf, T> apply(DataComponentType<T> param1DataComponentType);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentPatch.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */