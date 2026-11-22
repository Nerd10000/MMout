/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public final class DataComponentExactPredicate implements Predicate<DataComponentGetter> {
/*     */   public static final Codec<DataComponentExactPredicate> CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentExactPredicate> STREAM_CODEC;
/*     */   
/*     */   static {
/*  17 */     CODEC = DataComponentType.VALUE_MAP_CODEC.xmap(map -> new DataComponentExactPredicate((List<TypedDataComponent<?>>)map.entrySet().stream().map(TypedDataComponent::fromEntryUnchecked).collect(Collectors.toList())), predicate -> (Map)predicate.expectedComponents.stream().filter(()).collect(Collectors.toMap(TypedDataComponent::type, TypedDataComponent::value)));
/*  23 */     STREAM_CODEC = TypedDataComponent.STREAM_CODEC.apply(ByteBufCodecs.list()).map(DataComponentExactPredicate::new, predicate -> predicate.expectedComponents);
/*     */   }
/*     */   
/*  25 */   public static final DataComponentExactPredicate EMPTY = new DataComponentExactPredicate(List.of());
/*     */   
/*     */   private final List<TypedDataComponent<?>> expectedComponents;
/*     */   
/*     */   private DataComponentExactPredicate(List<TypedDataComponent<?>> expectedComponents) {
/*  30 */     this.expectedComponents = expectedComponents;
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/*  34 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static <T> DataComponentExactPredicate expect(DataComponentType<T> type, T value) {
/*  38 */     return new DataComponentExactPredicate(List.of(new TypedDataComponent(type, value)));
/*     */   }
/*     */   
/*     */   public static DataComponentExactPredicate allOf(DataComponentMap components) {
/*  42 */     return new DataComponentExactPredicate((List<TypedDataComponent<?>>)ImmutableList.copyOf(components));
/*     */   }
/*     */   
/*     */   public static DataComponentExactPredicate someOf(DataComponentMap components, DataComponentType<?>... types) {
/*  46 */     Builder result = new Builder();
/*  47 */     for (DataComponentType<?> type : types) {
/*  48 */       TypedDataComponent<?> value = components.getTyped(type);
/*  49 */       if (value != null)
/*  50 */         result.expect(value); 
/*     */     } 
/*  53 */     return result.build();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  57 */     return this.expectedComponents.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/*  62 */     if (obj instanceof DataComponentExactPredicate) {
/*  62 */       DataComponentExactPredicate predicate = (DataComponentExactPredicate)obj;
/*  62 */       if (this.expectedComponents.equals(predicate.expectedComponents));
/*     */     } 
/*  62 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  67 */     return this.expectedComponents.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/*  72 */     return this.expectedComponents.toString();
/*     */   }
/*     */   
/*     */   public boolean test(DataComponentGetter actualComponents) {
/*  77 */     for (TypedDataComponent<?> expected : this.expectedComponents) {
/*  78 */       Object actual = actualComponents.get(expected.type());
/*  79 */       if (!Objects.equals(expected.value(), actual))
/*  80 */         return false; 
/*     */     } 
/*  83 */     return true;
/*     */   }
/*     */   
/*     */   public boolean alwaysMatches() {
/*  87 */     return this.expectedComponents.isEmpty();
/*     */   }
/*     */   
/*     */   public DataComponentPatch asPatch() {
/*  91 */     return DataComponentPatch.builder().set(this.expectedComponents).build();
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  95 */     private final List<TypedDataComponent<?>> expectedComponents = new ArrayList<>();
/*     */     
/*     */     public <T> Builder expect(TypedDataComponent<T> value) {
/* 101 */       return expect(value.type(), value.value());
/*     */     }
/*     */     
/*     */     public <T> Builder expect(DataComponentType<? super T> type, T value) {
/* 105 */       for (TypedDataComponent<?> component : this.expectedComponents) {
/* 106 */         if (component.type() == type)
/* 107 */           throw new IllegalArgumentException("Predicate already has component of type: '" + String.valueOf(type) + "'"); 
/*     */       } 
/* 110 */       this.expectedComponents.add(new TypedDataComponent(type, value));
/* 111 */       return this;
/*     */     }
/*     */     
/*     */     public DataComponentExactPredicate build() {
/* 115 */       return new DataComponentExactPredicate(List.copyOf(this.expectedComponents));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentExactPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */