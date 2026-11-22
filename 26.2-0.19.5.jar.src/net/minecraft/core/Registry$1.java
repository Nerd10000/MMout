/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ class null implements IdMap<Holder<T>> {
/*     */   public int getId(Holder<T> thing) {
/* 158 */     return Registry.this.getId(thing.value());
/*     */   }
/*     */   
/*     */   public Holder<T> byId(int id) {
/* 163 */     return Registry.this.get(id).orElse(null);
/*     */   }
/*     */   
/*     */   public int size() {
/* 168 */     return Registry.this.size();
/*     */   }
/*     */   
/*     */   public Iterator<Holder<T>> iterator() {
/* 173 */     return Registry.this.listElements().map(e -> e).iterator();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Registry$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */