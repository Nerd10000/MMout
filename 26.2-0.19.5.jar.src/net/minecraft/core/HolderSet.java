/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public interface HolderSet<T> extends Iterable<Holder<T>> {
/*     */   Stream<Holder<T>> stream();
/*     */   
/*     */   int size();
/*     */   
/*     */   boolean isBound();
/*     */   
/*     */   Either<TagKey<T>, List<Holder<T>>> unwrap();
/*     */   
/*     */   Optional<Holder<T>> getRandomElement(RandomSource paramRandomSource);
/*     */   
/*     */   Holder<T> get(int paramInt);
/*     */   
/*     */   boolean contains(Holder<T> paramHolder);
/*     */   
/*     */   boolean canSerializeIn(HolderOwner<T> paramHolderOwner);
/*     */   
/*     */   Optional<TagKey<T>> unwrapKey();
/*     */   
/*     */   public static abstract class ListBacked<T> implements HolderSet<T> {
/*     */     protected abstract List<Holder<T>> contents();
/*     */     
/*     */     public int size() {
/*  44 */       return contents().size();
/*     */     }
/*     */     
/*     */     public Spliterator<Holder<T>> spliterator() {
/*  49 */       return contents().spliterator();
/*     */     }
/*     */     
/*     */     public Iterator<Holder<T>> iterator() {
/*  54 */       return contents().iterator();
/*     */     }
/*     */     
/*     */     public Stream<Holder<T>> stream() {
/*  59 */       return contents().stream();
/*     */     }
/*     */     
/*     */     public Optional<Holder<T>> getRandomElement(RandomSource random) {
/*  64 */       return Util.getRandomSafe(contents(), random);
/*     */     }
/*     */     
/*     */     public Holder<T> get(int index) {
/*  69 */       return contents().get(index);
/*     */     }
/*     */     
/*     */     public boolean canSerializeIn(HolderOwner<T> owner) {
/*  74 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Direct<T> extends ListBacked<T> {
/*  79 */     private static final Direct<?> EMPTY = new Direct(List.of());
/*     */     
/*     */     private final List<Holder<T>> contents;
/*     */     
/*     */     private Set<Holder<T>> contentsSet;
/*     */     
/*     */     private Direct(List<Holder<T>> contents) {
/*  87 */       this.contents = contents;
/*     */     }
/*     */     
/*     */     protected List<Holder<T>> contents() {
/*  92 */       return this.contents;
/*     */     }
/*     */     
/*     */     public boolean isBound() {
/*  97 */       return true;
/*     */     }
/*     */     
/*     */     public Either<TagKey<T>, List<Holder<T>>> unwrap() {
/* 102 */       return Either.right(this.contents);
/*     */     }
/*     */     
/*     */     public Optional<TagKey<T>> unwrapKey() {
/* 107 */       return Optional.empty();
/*     */     }
/*     */     
/*     */     public boolean contains(Holder<T> value) {
/* 112 */       if (this.contentsSet == null)
/* 113 */         this.contentsSet = Set.copyOf(this.contents); 
/* 115 */       return this.contentsSet.contains(value);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 120 */       return "DirectSet[" + String.valueOf(this.contents) + "]";
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 125 */       if (this == obj)
/* 126 */         return true; 
/* 128 */       if (obj instanceof Direct) {
/* 128 */         Direct<?> direct = (Direct)obj;
/* 128 */         if (this.contents.equals(direct.contents));
/*     */       } 
/* 128 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 133 */       return this.contents.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Named<T> extends ListBacked<T> {
/*     */     private final HolderOwner<T> owner;
/*     */     
/*     */     private final TagKey<T> key;
/*     */     
/*     */     private List<Holder<T>> contents;
/*     */     
/*     */     Named(HolderOwner<T> owner, TagKey<T> key) {
/* 145 */       this.owner = owner;
/* 146 */       this.key = key;
/*     */     }
/*     */     
/*     */     void bind(List<Holder<T>> contents) {
/* 151 */       this.contents = List.copyOf(contents);
/*     */     }
/*     */     
/*     */     public TagKey<T> key() {
/* 155 */       return this.key;
/*     */     }
/*     */     
/*     */     protected List<Holder<T>> contents() {
/* 160 */       if (this.contents == null)
/* 161 */         throw new IllegalStateException("Trying to access unbound tag '" + String.valueOf(this.key) + "' from registry " + String.valueOf(this.owner)); 
/* 163 */       return this.contents;
/*     */     }
/*     */     
/*     */     public boolean isBound() {
/* 168 */       return (this.contents != null);
/*     */     }
/*     */     
/*     */     public Either<TagKey<T>, List<Holder<T>>> unwrap() {
/* 173 */       return Either.left(this.key);
/*     */     }
/*     */     
/*     */     public Optional<TagKey<T>> unwrapKey() {
/* 178 */       return Optional.of(this.key);
/*     */     }
/*     */     
/*     */     public boolean contains(Holder<T> value) {
/* 183 */       return value.is(this.key);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 188 */       return "NamedSet(" + String.valueOf(this.key) + ")[" + String.valueOf(this.contents) + "]";
/*     */     }
/*     */     
/*     */     public boolean canSerializeIn(HolderOwner<T> context) {
/* 193 */       return this.owner.canSerializeIn(context);
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   @VisibleForTesting
/*     */   static <T> Named<T> emptyNamed(HolderOwner<T> owner, TagKey<T> key) {
/* 203 */     return new Named<T>(owner, key) {
/*     */         protected List<Holder<T>> contents() {
/* 206 */           throw new UnsupportedOperationException("Tag " + String.valueOf(key()) + " can't be dereferenced during construction");
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <T> HolderSet<T> empty() {
/* 213 */     return (HolderSet)Direct.EMPTY;
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   static <T> Direct<T> direct(Holder<T>... values) {
/* 218 */     return new Direct<>(List.of(values));
/*     */   }
/*     */   
/*     */   static <T> Direct<T> direct(List<? extends Holder<T>> values) {
/* 222 */     return new Direct<>(List.copyOf(values));
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   static <E, T> Direct<T> direct(Function<E, Holder<T>> holderGetter, E... elements) {
/* 227 */     return direct(Stream.<E>of(elements).<Holder<T>>map(holderGetter).toList());
/*     */   }
/*     */   
/*     */   static <E, T> Direct<T> direct(Function<E, Holder<T>> holderGetter, Collection<E> elements) {
/* 231 */     return direct(elements.stream().<Holder<T>>map(holderGetter).toList());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\HolderSet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */