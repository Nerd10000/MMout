/*     */ package net.minecraft.client.data.models.blockstates;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.data.models.MultiVariant;
/*     */ import net.minecraft.client.renderer.block.dispatch.VariantMutator;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public abstract class PropertyDispatch<V> {
/*  19 */   private final Map<PropertyValueList, V> values = new HashMap<>();
/*     */   
/*     */   protected void putValue(PropertyValueList key, V variant) {
/*  22 */     V previous = this.values.put(key, variant);
/*  23 */     if (previous != null)
/*  24 */       throw new IllegalStateException("Value " + String.valueOf(key) + " is already defined"); 
/*     */   }
/*     */   
/*     */   Map<PropertyValueList, V> getEntries() {
/*  30 */     verifyComplete();
/*  31 */     return Map.copyOf(this.values);
/*     */   }
/*     */   
/*     */   private void verifyComplete() {
/*  35 */     List<Property<?>> properties = getDefinedProperties();
/*  36 */     Stream<PropertyValueList> valuesToCover = Stream.of(PropertyValueList.EMPTY);
/*  37 */     for (Property<?> property : properties) {
/*  38 */       valuesToCover = valuesToCover.flatMap(current -> {
/*     */             Objects.requireNonNull(current);
/*     */             return property.getAllValues().map(current::extend);
/*     */           });
/*     */     } 
/*  40 */     List<PropertyValueList> undefinedCombinations = valuesToCover.filter(f -> !this.values.containsKey(f)).toList();
/*  41 */     if (!undefinedCombinations.isEmpty())
/*  42 */       throw new IllegalStateException("Missing definition for properties: " + String.valueOf(undefinedCombinations)); 
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>> C1<MultiVariant, T1> initial(Property<T1> property1) {
/*  50 */     return new C1<>(property1);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> C2<MultiVariant, T1, T2> initial(Property<T1> property1, Property<T2> property2) {
/*  54 */     return new C2<>(property1, property2);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> C3<MultiVariant, T1, T2, T3> initial(Property<T1> property1, Property<T2> property2, Property<T3> property3) {
/*  58 */     return new C3<>(property1, property2, property3);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> C4<MultiVariant, T1, T2, T3, T4> initial(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4) {
/*  62 */     return new C4<>(property1, property2, property3, property4);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> C5<MultiVariant, T1, T2, T3, T4, T5> initial(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4, Property<T5> property5) {
/*  66 */     return new C5<>(property1, property2, property3, property4, property5);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>> C1<VariantMutator, T1> modify(Property<T1> property1) {
/*  70 */     return new C1<>(property1);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>> C2<VariantMutator, T1, T2> modify(Property<T1> property1, Property<T2> property2) {
/*  74 */     return new C2<>(property1, property2);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> C3<VariantMutator, T1, T2, T3> modify(Property<T1> property1, Property<T2> property2, Property<T3> property3) {
/*  78 */     return new C3<>(property1, property2, property3);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> C4<VariantMutator, T1, T2, T3, T4> modify(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4) {
/*  82 */     return new C4<>(property1, property2, property3, property4);
/*     */   }
/*     */   
/*     */   public static <T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> C5<VariantMutator, T1, T2, T3, T4, T5> modify(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4, Property<T5> property5) {
/*  86 */     return new C5<>(property1, property2, property3, property4, property5);
/*     */   }
/*     */   
/*     */   abstract List<Property<?>> getDefinedProperties();
/*     */   
/*     */   public static class C1<V, T1 extends Comparable<T1>> extends PropertyDispatch<V> {
/*     */     private final Property<T1> property1;
/*     */     
/*     */     private C1(Property<T1> property1) {
/*  93 */       this.property1 = property1;
/*     */     }
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/*  98 */       return List.of(this.property1);
/*     */     }
/*     */     
/*     */     public C1<V, T1> select(T1 value1, V variants) {
/* 102 */       PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 103 */             .value((Comparable)value1) });
/* 105 */       putValue(key, variants);
/* 106 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch<V> generate(Function<T1, V> generator) {
/* 110 */       this.property1.getPossibleValues().forEach(value1 -> select((T1)value1, generator.apply(value1)));
/* 113 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C2<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>> extends PropertyDispatch<V> {
/*     */     private final Property<T1> property1;
/*     */     
/*     */     private final Property<T2> property2;
/*     */     
/*     */     private C2(Property<T1> property1, Property<T2> property2) {
/* 122 */       this.property1 = property1;
/* 123 */       this.property2 = property2;
/*     */     }
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 128 */       return List.of(this.property1, this.property2);
/*     */     }
/*     */     
/*     */     public C2<V, T1, T2> select(T1 value1, T2 value2, V variants) {
/* 132 */       PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 133 */             .value((Comparable)value1), this.property2
/* 134 */             .value((Comparable)value2) });
/* 136 */       putValue(key, variants);
/* 137 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch<V> generate(BiFunction<T1, T2, V> generator) {
/* 141 */       this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/* 146 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C3<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>> extends PropertyDispatch<V> {
/*     */     private final Property<T1> property1;
/*     */     
/*     */     private final Property<T2> property2;
/*     */     
/*     */     private final Property<T3> property3;
/*     */     
/*     */     private C3(Property<T1> property1, Property<T2> property2, Property<T3> property3) {
/* 156 */       this.property1 = property1;
/* 157 */       this.property2 = property2;
/* 158 */       this.property3 = property3;
/*     */     }
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 163 */       return List.of(this.property1, this.property2, this.property3);
/*     */     }
/*     */     
/*     */     public C3<V, T1, T2, T3> select(T1 value1, T2 value2, T3 value3, V variants) {
/* 167 */       PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 168 */             .value((Comparable)value1), this.property2
/* 169 */             .value((Comparable)value2), this.property3
/* 170 */             .value((Comparable)value3) });
/* 172 */       putValue(key, variants);
/* 173 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch<V> generate(Function3<T1, T2, T3, V> generator) {
/* 177 */       this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/* 184 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C4<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>> extends PropertyDispatch<V> {
/*     */     private final Property<T1> property1;
/*     */     
/*     */     private final Property<T2> property2;
/*     */     
/*     */     private final Property<T3> property3;
/*     */     
/*     */     private final Property<T4> property4;
/*     */     
/*     */     private C4(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4) {
/* 195 */       this.property1 = property1;
/* 196 */       this.property2 = property2;
/* 197 */       this.property3 = property3;
/* 198 */       this.property4 = property4;
/*     */     }
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 203 */       return List.of(this.property1, this.property2, this.property3, this.property4);
/*     */     }
/*     */     
/*     */     public C4<V, T1, T2, T3, T4> select(T1 value1, T2 value2, T3 value3, T4 value4, V variants) {
/* 207 */       PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 208 */             .value((Comparable)value1), this.property2
/* 209 */             .value((Comparable)value2), this.property3
/* 210 */             .value((Comparable)value3), this.property4
/* 211 */             .value((Comparable)value4) });
/* 213 */       putValue(key, variants);
/* 214 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch<V> generate(Function4<T1, T2, T3, T4, V> generator) {
/* 218 */       this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/* 227 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C5<V, T1 extends Comparable<T1>, T2 extends Comparable<T2>, T3 extends Comparable<T3>, T4 extends Comparable<T4>, T5 extends Comparable<T5>> extends PropertyDispatch<V> {
/*     */     private final Property<T1> property1;
/*     */     
/*     */     private final Property<T2> property2;
/*     */     
/*     */     private final Property<T3> property3;
/*     */     
/*     */     private final Property<T4> property4;
/*     */     
/*     */     private final Property<T5> property5;
/*     */     
/*     */     private C5(Property<T1> property1, Property<T2> property2, Property<T3> property3, Property<T4> property4, Property<T5> property5) {
/* 239 */       this.property1 = property1;
/* 240 */       this.property2 = property2;
/* 241 */       this.property3 = property3;
/* 242 */       this.property4 = property4;
/* 243 */       this.property5 = property5;
/*     */     }
/*     */     
/*     */     public List<Property<?>> getDefinedProperties() {
/* 248 */       return List.of(this.property1, this.property2, this.property3, this.property4, this.property5);
/*     */     }
/*     */     
/*     */     public C5<V, T1, T2, T3, T4, T5> select(T1 value1, T2 value2, T3 value3, T4 value4, T5 value5, V variants) {
/* 252 */       PropertyValueList key = PropertyValueList.of((Property.Value<?>[])new Property.Value[] { this.property1
/* 253 */             .value((Comparable)value1), this.property2
/* 254 */             .value((Comparable)value2), this.property3
/* 255 */             .value((Comparable)value3), this.property4
/* 256 */             .value((Comparable)value4), this.property5
/* 257 */             .value((Comparable)value5) });
/* 259 */       putValue(key, variants);
/* 260 */       return this;
/*     */     }
/*     */     
/*     */     public PropertyDispatch<V> generate(Function5<T1, T2, T3, T4, T5, V> generator) {
/* 264 */       this.property1.getPossibleValues().forEach(value1 -> this.property2.getPossibleValues().forEach(()));
/* 275 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\data\models\blockstates\PropertyDispatch.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */