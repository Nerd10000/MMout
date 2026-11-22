/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.Map;
/*     */ 
/*     */ class NbtRecordBuilder extends RecordBuilder.AbstractStringBuilder<Tag, CompoundTag> {
/*     */   protected NbtRecordBuilder(NbtOps paramNbtOps) {
/* 379 */     super(paramNbtOps);
/*     */   }
/*     */   
/*     */   protected CompoundTag initBuilder() {
/* 384 */     return new CompoundTag();
/*     */   }
/*     */   
/*     */   protected CompoundTag append(String key, Tag value, CompoundTag builder) {
/* 389 */     builder.put(key, value);
/* 390 */     return builder;
/*     */   }
/*     */   
/*     */   protected DataResult<Tag> build(CompoundTag builder, Tag prefix) {
/* 395 */     if (prefix == null || prefix == EndTag.INSTANCE)
/* 396 */       return DataResult.success(builder); 
/* 398 */     if (prefix instanceof CompoundTag) {
/* 398 */       CompoundTag compound = (CompoundTag)prefix;
/* 399 */       CompoundTag result = compound.shallowCopy();
/* 400 */       for (Map.Entry<String, Tag> entry : builder.entrySet())
/* 401 */         result.put(entry.getKey(), entry.getValue()); 
/* 403 */       return DataResult.success(result);
/*     */     } 
/* 405 */     return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(prefix), prefix);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NbtOps$NbtRecordBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */