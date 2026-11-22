/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ public final class Direct<T> extends HolderSet.ListBacked<T> {
/*  79 */   private static final Direct<?> EMPTY = new Direct(List.of());
/*     */   
/*     */   private final List<Holder<T>> contents;
/*     */   
/*     */   private Set<Holder<T>> contentsSet;
/*     */   
/*     */   private Direct(List<Holder<T>> contents) {
/*  87 */     this.contents = contents;
/*     */   }
/*     */   
/*     */   protected List<Holder<T>> contents() {
/*  92 */     return this.contents;
/*     */   }
/*     */   
/*     */   public boolean isBound() {
/*  97 */     return true;
/*     */   }
/*     */   
/*     */   public Either<TagKey<T>, List<Holder<T>>> unwrap() {
/* 102 */     return Either.right(this.contents);
/*     */   }
/*     */   
/*     */   public Optional<TagKey<T>> unwrapKey() {
/* 107 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public boolean contains(Holder<T> value) {
/* 112 */     if (this.contentsSet == null)
/* 113 */       this.contentsSet = Set.copyOf(this.contents); 
/* 115 */     return this.contentsSet.contains(value);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 120 */     return "DirectSet[" + String.valueOf(this.contents) + "]";
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 125 */     if (this == obj)
/* 126 */       return true; 
/* 128 */     if (obj instanceof Direct) {
/* 128 */       Direct<?> direct = (Direct)obj;
/* 128 */       if (this.contents.equals(direct.contents));
/*     */     } 
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 133 */     return this.contents.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderSet$Direct.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */