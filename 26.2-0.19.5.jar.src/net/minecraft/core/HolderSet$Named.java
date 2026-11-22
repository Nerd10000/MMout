/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ public class Named<T> extends HolderSet.ListBacked<T> {
/*     */   private final HolderOwner<T> owner;
/*     */   
/*     */   private final TagKey<T> key;
/*     */   
/*     */   private List<Holder<T>> contents;
/*     */   
/*     */   Named(HolderOwner<T> owner, TagKey<T> key) {
/* 145 */     this.owner = owner;
/* 146 */     this.key = key;
/*     */   }
/*     */   
/*     */   void bind(List<Holder<T>> contents) {
/* 151 */     this.contents = List.copyOf(contents);
/*     */   }
/*     */   
/*     */   public TagKey<T> key() {
/* 155 */     return this.key;
/*     */   }
/*     */   
/*     */   protected List<Holder<T>> contents() {
/* 160 */     if (this.contents == null)
/* 161 */       throw new IllegalStateException("Trying to access unbound tag '" + String.valueOf(this.key) + "' from registry " + String.valueOf(this.owner)); 
/* 163 */     return this.contents;
/*     */   }
/*     */   
/*     */   public boolean isBound() {
/* 168 */     return (this.contents != null);
/*     */   }
/*     */   
/*     */   public Either<TagKey<T>, List<Holder<T>>> unwrap() {
/* 173 */     return Either.left(this.key);
/*     */   }
/*     */   
/*     */   public Optional<TagKey<T>> unwrapKey() {
/* 178 */     return Optional.of(this.key);
/*     */   }
/*     */   
/*     */   public boolean contains(Holder<T> value) {
/* 183 */     return value.is(this.key);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 188 */     return "NamedSet(" + String.valueOf(this.key) + ")[" + String.valueOf(this.contents) + "]";
/*     */   }
/*     */   
/*     */   public boolean canSerializeIn(HolderOwner<T> context) {
/* 193 */     return this.owner.canSerializeIn(context);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderSet$Named.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */