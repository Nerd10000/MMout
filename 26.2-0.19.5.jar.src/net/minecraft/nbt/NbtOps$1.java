/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ class null implements MapLike<Tag> {
/*     */   public Tag get(Tag key) {
/* 252 */     if (key instanceof StringTag) {
/* 252 */       StringTag stringTag = (StringTag)key;
/*     */       try {
/* 252 */         String str1 = stringTag.value(), stringKey = str1;
/* 253 */         return tag.get(stringKey);
/*     */       } catch (Throwable throwable) {
/*     */         throw new MatchException(throwable.toString(), throwable);
/*     */       } 
/*     */     } 
/* 255 */     throw new UnsupportedOperationException("Cannot get map entry with non-string key: " + String.valueOf(key));
/*     */   }
/*     */   
/*     */   public Tag get(String key) {
/* 260 */     return tag.get(key);
/*     */   }
/*     */   
/*     */   public Stream<Pair<Tag, Tag>> entries() {
/* 265 */     return tag.entrySet().stream().map(entry -> Pair.of(NbtOps.this.createString((String)entry.getKey()), entry.getValue()));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 270 */     return "MapLike[" + String.valueOf(tag) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\nbt\NbtOps$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */