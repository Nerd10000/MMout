/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ class null extends HolderSet.Named<T> {
/*     */   null(HolderOwner<T> owner, TagKey<T> key) {
/* 203 */     super(owner, key);
/*     */   }
/*     */   
/*     */   protected List<Holder<T>> contents() {
/* 206 */     throw new UnsupportedOperationException("Tag " + String.valueOf(key()) + " can't be dereferenced during construction");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderSet$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */