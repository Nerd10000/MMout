/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ class null implements StreamCodec<RegistryFriendlyByteBuf, DataComponentPatch> {
/*     */   public DataComponentPatch decode(RegistryFriendlyByteBuf input) {
/*  86 */     int positiveCount = input.readVarInt();
/*  87 */     int negativeCount = input.readVarInt();
/*  88 */     if (positiveCount == 0 && negativeCount == 0)
/*  89 */       return DataComponentPatch.EMPTY; 
/*  92 */     int expectedSize = positiveCount + negativeCount;
/*  93 */     Reference2ObjectArrayMap reference2ObjectArrayMap = new Reference2ObjectArrayMap(Math.min(expectedSize, 65536));
/*     */     int i;
/*  94 */     for (i = 0; i < positiveCount; i++) {
/*  95 */       DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/*  96 */       Object value = codecGetter.<T>apply((DataComponentType)type).decode(input);
/*  97 */       reference2ObjectArrayMap.put(type, Optional.of(value));
/*     */     } 
/* 100 */     for (i = 0; i < negativeCount; i++) {
/* 101 */       DataComponentType<?> type = (DataComponentType)DataComponentType.STREAM_CODEC.decode(input);
/* 102 */       reference2ObjectArrayMap.put(type, Optional.empty());
/*     */     } 
/* 105 */     return new DataComponentPatch((Reference2ObjectMap<DataComponentType<?>, Optional<?>>)reference2ObjectArrayMap);
/*     */   }
/*     */   
/*     */   public void encode(RegistryFriendlyByteBuf output, DataComponentPatch patch) {
/* 110 */     if (patch.isEmpty()) {
/* 111 */       output.writeVarInt(0);
/* 112 */       output.writeVarInt(0);
/*     */       return;
/*     */     } 
/* 116 */     int positiveCount = 0;
/* 117 */     int negativeCount = 0;
/*     */     ObjectIterator<Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>>> objectIterator;
/* 118 */     for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) {
/* 118 */       Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 119 */       if (((Optional)entry.getValue()).isPresent()) {
/* 120 */         positiveCount++;
/*     */         continue;
/*     */       } 
/* 122 */       negativeCount++;
/*     */     } 
/* 126 */     output.writeVarInt(positiveCount);
/* 127 */     output.writeVarInt(negativeCount);
/* 128 */     for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) {
/* 128 */       Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 129 */       Optional<?> value = (Optional)entry.getValue();
/* 130 */       if (value.isPresent()) {
/* 131 */         DataComponentType<?> type = (DataComponentType)entry.getKey();
/* 132 */         DataComponentType.STREAM_CODEC.encode(output, type);
/* 133 */         encodeComponent(output, type, value.get());
/*     */       } 
/*     */     } 
/* 137 */     for (objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) {
/* 137 */       Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 138 */       if (((Optional)entry.getValue()).isEmpty()) {
/* 139 */         DataComponentType<?> type = (DataComponentType)entry.getKey();
/* 140 */         DataComponentType.STREAM_CODEC.encode(output, type);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T> void encodeComponent(RegistryFriendlyByteBuf output, DataComponentType<T> type, Object value) {
/* 147 */     codecGetter.<T>apply(type).encode(output, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentPatch$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */