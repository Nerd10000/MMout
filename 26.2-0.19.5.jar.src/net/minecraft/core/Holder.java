/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ public interface Holder<T> {
/*     */   default String getRegisteredName() {
/*  56 */     return unwrapKey().<String>map(key -> key.identifier().toString()).orElse("[unregistered]");
/*     */   }
/*     */   
/*     */   public enum Kind {
/*  60 */     REFERENCE, DIRECT;
/*     */   }
/*     */   
/*     */   static <T> Holder<T> direct(T value) {
/*  64 */     return new Direct<>(value, DataComponentMap.EMPTY);
/*     */   }
/*     */   
/*     */   static <T> Holder<T> direct(T value, DataComponentMap components) {
/*  68 */     return new Direct<>(value, components);
/*     */   }
/*     */   
/*     */   T value();
/*     */   
/*     */   boolean isBound();
/*     */   
/*     */   boolean areComponentsBound();
/*     */   
/*     */   boolean is(Identifier paramIdentifier);
/*     */   
/*     */   boolean is(ResourceKey<T> paramResourceKey);
/*     */   
/*     */   boolean is(Predicate<ResourceKey<T>> paramPredicate);
/*     */   
/*     */   boolean is(TagKey<T> paramTagKey);
/*     */   
/*     */   @Deprecated
/*     */   boolean is(Holder<T> paramHolder);
/*     */   
/*     */   Stream<TagKey<T>> tags();
/*     */   
/*     */   DataComponentMap components();
/*     */   
/*     */   Either<ResourceKey<T>, T> unwrap();
/*     */   
/*     */   Optional<ResourceKey<T>> unwrapKey();
/*     */   
/*     */   Kind kind();
/*     */   
/*     */   boolean canSerializeIn(HolderOwner<T> paramHolderOwner);
/*     */   
/*     */   public static final class Direct<T> extends Record implements Holder<T> {
/*     */     private final T value;
/*     */     
/*     */     private final DataComponentMap components;
/*     */     
/*     */     public Direct(T value, DataComponentMap components) {
/*  71 */       this.value = value;
/*  71 */       this.components = components;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/Holder$Direct;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/Holder$Direct;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/Holder$Direct<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/Holder$Direct;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/Holder$Direct;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/core/Holder$Direct<TT;>;
/*     */     }
/*     */     
/*     */     public T value() {
/*  71 */       return this.value;
/*     */     }
/*     */     
/*     */     public DataComponentMap components() {
/*  71 */       return this.components;
/*     */     }
/*     */     
/*     */     public boolean isBound() {
/*  74 */       return true;
/*     */     }
/*     */     
/*     */     public boolean areComponentsBound() {
/*  79 */       return true;
/*     */     }
/*     */     
/*     */     public boolean is(Identifier key) {
/*  84 */       return false;
/*     */     }
/*     */     
/*     */     public boolean is(ResourceKey<T> key) {
/*  89 */       return false;
/*     */     }
/*     */     
/*     */     public boolean is(TagKey<T> tag) {
/*  94 */       return false;
/*     */     }
/*     */     
/*     */     public boolean is(Holder<T> holder) {
/*  99 */       return this.value.equals(holder.value());
/*     */     }
/*     */     
/*     */     public boolean is(Predicate<ResourceKey<T>> predicate) {
/* 104 */       return false;
/*     */     }
/*     */     
/*     */     public Either<ResourceKey<T>, T> unwrap() {
/* 109 */       return Either.right(this.value);
/*     */     }
/*     */     
/*     */     public Optional<ResourceKey<T>> unwrapKey() {
/* 114 */       return Optional.empty();
/*     */     }
/*     */     
/*     */     public Holder.Kind kind() {
/* 119 */       return Holder.Kind.DIRECT;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 124 */       return "Direct{" + String.valueOf(this.value) + "}";
/*     */     }
/*     */     
/*     */     public boolean canSerializeIn(HolderOwner<T> registry) {
/* 129 */       return true;
/*     */     }
/*     */     
/*     */     public Stream<TagKey<T>> tags() {
/* 134 */       return Stream.of((TagKey<T>[])new TagKey[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Reference<T> implements Holder<T> {
/*     */     private final HolderOwner<T> owner;
/*     */     
/*     */     private Set<TagKey<T>> tags;
/*     */     
/*     */     private DataComponentMap components;
/*     */     
/*     */     private final Type type;
/*     */     
/*     */     private ResourceKey<T> key;
/*     */     
/*     */     private T value;
/*     */     
/*     */     protected enum Type {
/* 141 */       STAND_ALONE, INTRUSIVE;
/*     */     }
/*     */     
/*     */     protected Reference(Type type, HolderOwner<T> owner, ResourceKey<T> key, T value) {
/* 155 */       this.owner = owner;
/* 156 */       this.type = type;
/* 157 */       this.key = key;
/* 158 */       this.value = value;
/*     */     }
/*     */     
/*     */     public static <T> Reference<T> createStandAlone(HolderOwner<T> owner, ResourceKey<T> key) {
/* 163 */       return new Reference<>(Type.STAND_ALONE, owner, key, null);
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public static <T> Reference<T> createIntrusive(HolderOwner<T> owner, T value) {
/* 170 */       return new Reference<>(Type.INTRUSIVE, owner, null, value);
/*     */     }
/*     */     
/*     */     public ResourceKey<T> key() {
/* 174 */       if (this.key == null)
/* 175 */         throw new IllegalStateException("Trying to access unbound value '" + String.valueOf(this.value) + "' from registry " + String.valueOf(this.owner)); 
/* 177 */       return this.key;
/*     */     }
/*     */     
/*     */     public T value() {
/* 182 */       if (this.value == null)
/* 183 */         throw new IllegalStateException("Trying to access unbound value '" + String.valueOf(this.key) + "' from registry " + String.valueOf(this.owner)); 
/* 185 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean is(Identifier key) {
/* 190 */       return key().identifier().equals(key);
/*     */     }
/*     */     
/*     */     public boolean is(ResourceKey<T> key) {
/* 195 */       return (key() == key);
/*     */     }
/*     */     
/*     */     private Set<TagKey<T>> boundTags() {
/* 199 */       if (this.tags == null)
/* 200 */         throw new IllegalStateException("Tags not bound"); 
/* 202 */       return this.tags;
/*     */     }
/*     */     
/*     */     public boolean is(TagKey<T> tag) {
/* 207 */       return boundTags().contains(tag);
/*     */     }
/*     */     
/*     */     public boolean is(Holder<T> holder) {
/* 212 */       return holder.is(key());
/*     */     }
/*     */     
/*     */     public boolean is(Predicate<ResourceKey<T>> predicate) {
/* 217 */       return predicate.test(key());
/*     */     }
/*     */     
/*     */     public boolean canSerializeIn(HolderOwner<T> context) {
/* 222 */       return this.owner.canSerializeIn(context);
/*     */     }
/*     */     
/*     */     public Either<ResourceKey<T>, T> unwrap() {
/* 227 */       return Either.left(key());
/*     */     }
/*     */     
/*     */     public Optional<ResourceKey<T>> unwrapKey() {
/* 232 */       return Optional.of(key());
/*     */     }
/*     */     
/*     */     public Holder.Kind kind() {
/* 237 */       return Holder.Kind.REFERENCE;
/*     */     }
/*     */     
/*     */     public boolean isBound() {
/* 242 */       return (this.key != null && this.value != null);
/*     */     }
/*     */     
/*     */     public boolean areComponentsBound() {
/* 247 */       return (this.components != null);
/*     */     }
/*     */     
/*     */     void bindKey(ResourceKey<T> key) {
/* 252 */       if (this.key != null && key != this.key)
/* 253 */         throw new IllegalStateException("Can't change holder key: existing=" + String.valueOf(this.key) + ", new=" + String.valueOf(key)); 
/* 255 */       this.key = key;
/*     */     }
/*     */     
/*     */     protected void bindValue(T value) {
/* 259 */       if (this.type == Type.INTRUSIVE && this.value != value)
/* 260 */         throw new IllegalStateException("Can't change holder " + String.valueOf(this.key) + " value: existing=" + String.valueOf(this.value) + ", new=" + String.valueOf(value)); 
/* 262 */       this.value = value;
/*     */     }
/*     */     
/*     */     void bindTags(Collection<TagKey<T>> tags) {
/* 267 */       this.tags = Set.copyOf(tags);
/*     */     }
/*     */     
/*     */     public void bindComponents(DataComponentMap components) {
/* 271 */       this.components = components;
/*     */     }
/*     */     
/*     */     public Stream<TagKey<T>> tags() {
/* 276 */       return boundTags().stream();
/*     */     }
/*     */     
/*     */     public DataComponentMap components() {
/* 281 */       return Objects.<DataComponentMap>requireNonNull(this.components, "Components not bound yet");
/*     */     }
/*     */     
/*     */     public String toString() {
/* 286 */       return "Reference{" + String.valueOf(this.key) + "=" + String.valueOf(this.value) + "}";
/*     */     }
/*     */   }
/*     */   
/*     */   protected enum Type {
/*     */     STAND_ALONE, INTRUSIVE;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Holder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */