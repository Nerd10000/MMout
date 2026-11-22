/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ 
/*     */ public final class Direct<T> extends Record implements Holder<T> {
/*     */   private final T value;
/*     */   
/*     */   private final DataComponentMap components;
/*     */   
/*     */   public Direct(T value, DataComponentMap components) {
/*  71 */     this.value = value;
/*  71 */     this.components = components;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/Holder$Direct;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #71	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/Holder$Direct;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/Holder$Direct<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/Holder$Direct;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #71	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/Holder$Direct;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/core/Holder$Direct<TT;>;
/*     */   }
/*     */   
/*     */   public T value() {
/*  71 */     return this.value;
/*     */   }
/*     */   
/*     */   public DataComponentMap components() {
/*  71 */     return this.components;
/*     */   }
/*     */   
/*     */   public boolean isBound() {
/*  74 */     return true;
/*     */   }
/*     */   
/*     */   public boolean areComponentsBound() {
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public boolean is(Identifier key) {
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   public boolean is(ResourceKey<T> key) {
/*  89 */     return false;
/*     */   }
/*     */   
/*     */   public boolean is(TagKey<T> tag) {
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   public boolean is(Holder<T> holder) {
/*  99 */     return this.value.equals(holder.value());
/*     */   }
/*     */   
/*     */   public boolean is(Predicate<ResourceKey<T>> predicate) {
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   public Either<ResourceKey<T>, T> unwrap() {
/* 109 */     return Either.right(this.value);
/*     */   }
/*     */   
/*     */   public Optional<ResourceKey<T>> unwrapKey() {
/* 114 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public Holder.Kind kind() {
/* 119 */     return Holder.Kind.DIRECT;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 124 */     return "Direct{" + String.valueOf(this.value) + "}";
/*     */   }
/*     */   
/*     */   public boolean canSerializeIn(HolderOwner<T> registry) {
/* 129 */     return true;
/*     */   }
/*     */   
/*     */   public Stream<TagKey<T>> tags() {
/* 134 */     return Stream.of((TagKey<T>[])new TagKey[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Holder$Direct.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */