/*     */ package net.minecraft.tags;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class null implements TagEntry.Lookup<T> {
/*     */   public T element(Identifier key, boolean required) {
/* 112 */     return TagLoader.this.elementLookup.get(key, required).orElse(null);
/*     */   }
/*     */   
/*     */   public Collection<T> tag(Identifier key) {
/* 117 */     return (Collection<T>)newTags.get(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagLoader$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */