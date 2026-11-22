/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public abstract class StateHolder<O, S> {
/*     */   private static final int VALUE_NOT_FOUND = -1;
/*     */   
/*     */   public static final String NAME_TAG = "Name";
/*     */   
/*     */   public static final String PROPERTIES_TAG = "Properties";
/*     */   
/*     */   protected final O owner;
/*     */   
/*     */   private final Property<?>[] propertyKeys;
/*     */   
/*     */   private final Comparable<?>[] propertyValues;
/*     */   
/*     */   private S[][] neighbors;
/*     */   
/*     */   protected StateHolder(O owner, Property<?>[] propertyKeys, Comparable<?>[] propertyValues) {
/*  32 */     assert propertyKeys.length == propertyValues.length;
/*  33 */     this.owner = owner;
/*  34 */     this.propertyKeys = propertyKeys;
/*  35 */     this.propertyValues = propertyValues;
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> S cycle(Property<T> property) {
/*  39 */     return setValue(property, findNextInCollection(property.getPossibleValues(), getValue(property)));
/*     */   }
/*     */   
/*     */   protected static <T> T findNextInCollection(List<T> values, T current) {
/*  43 */     int nextIndex = values.indexOf(current) + 1;
/*  44 */     return (nextIndex == values.size()) ? values.getFirst() : values.get(nextIndex);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  49 */     StringBuilder builder = new StringBuilder();
/*  50 */     builder.append(this.owner);
/*  52 */     if (!isSingletonState()) {
/*  53 */       builder.append('[');
/*  54 */       builder.append(getValues().<CharSequence>map(Property.Value::toString).collect(Collectors.joining(",")));
/*  55 */       builder.append(']');
/*     */     } 
/*  58 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public final boolean equals(Object obj) {
/*  64 */     return super.equals(obj);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  70 */     return super.hashCode();
/*     */   }
/*     */   
/*     */   public Collection<Property<?>> getProperties() {
/*  74 */     return List.of(this.propertyKeys);
/*     */   }
/*     */   
/*     */   private int valueIndex(Property<?> property) {
/*  78 */     for (int i = 0; i < this.propertyKeys.length; i++) {
/*  80 */       if (this.propertyKeys[i] == property)
/*  81 */         return i; 
/*     */     } 
/*  84 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean hasProperty(Property<?> property) {
/*  88 */     return (valueIndex(property) != -1);
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>> T getNullableValue(Property<T> property) {
/*  92 */     int index = valueIndex(property);
/*  93 */     if (index == -1)
/*  94 */       return null; 
/*  96 */     return (T)property.getValueClass().cast(this.propertyValues[index]);
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> T getValue(Property<T> property) {
/* 100 */     T value = getNullableValue(property);
/* 101 */     if (value == null)
/* 102 */       throw new IllegalArgumentException("Cannot get property " + String.valueOf(property) + " as it does not exist in " + String.valueOf(this.owner)); 
/* 104 */     return value;
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> Optional<T> getOptionalValue(Property<T> property) {
/* 108 */     return Optional.ofNullable(getNullableValue(property));
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>> T getValueOrElse(Property<T> property, T defaultValue) {
/* 112 */     return (T)Objects.<Comparable>requireNonNullElse(getNullableValue(property), (Comparable)defaultValue);
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>, V extends T> S setValue(Property<T> property, V value) {
/* 120 */     int index = valueIndex(property);
/* 121 */     if (index == -1)
/* 122 */       throw new IllegalArgumentException("Cannot set property " + String.valueOf(property) + " as it does not exist in " + String.valueOf(this.owner)); 
/* 125 */     return setValueInternal(property, index, value);
/*     */   }
/*     */   
/*     */   public <T extends Comparable<T>, V extends T> S trySetValue(Property<T> property, V value) {
/* 134 */     int index = valueIndex(property);
/* 135 */     if (index == -1)
/* 136 */       return (S)this; 
/* 139 */     return setValueInternal(property, index, value);
/*     */   }
/*     */   
/*     */   private <T extends Comparable<T>, V extends T> S setValueInternal(Property<T> property, int propertyIndex, V value) {
/* 143 */     int valueIndex = property.getInternalIndex((Comparable)value);
/* 144 */     if (valueIndex < 0)
/* 145 */       throw new IllegalArgumentException("Cannot set property " + String.valueOf(property) + " to " + String.valueOf(value) + " on " + String.valueOf(this.owner) + ", it is not an allowed value"); 
/* 147 */     return this.neighbors[propertyIndex][valueIndex];
/*     */   }
/*     */   
/*     */   void initializeNeighbors(S[][] neighbors) {
/* 152 */     if (this.neighbors != null)
/* 153 */       throw new IllegalStateException(); 
/* 155 */     this.neighbors = neighbors;
/*     */   }
/*     */   
/*     */   public boolean isSingletonState() {
/* 159 */     return (this.propertyKeys.length == 0);
/*     */   }
/*     */   
/*     */   public Stream<Property.Value<?>> getValues() {
/* 163 */     int length = this.propertyKeys.length;
/* 164 */     if (length == 0)
/* 165 */       return Stream.empty(); 
/* 167 */     return IntStream.range(0, length).mapToObj(i -> createValue(this.propertyKeys[i], this.propertyValues[i]));
/*     */   }
/*     */   
/*     */   private static <T extends Comparable<T>> Property.Value<T> createValue(Property<T> propertyKey, Comparable<?> propertyValue) {
/* 172 */     return new Property.Value(propertyKey, propertyValue);
/*     */   }
/*     */   
/*     */   protected static <O, S extends StateHolder<O, S>> Codec<S> codec(Codec<O> ownerCodec, Function<O, S> defaultState, Function<O, StateDefinition<O, S>> stateDefinition) {
/* 176 */     return ownerCodec.dispatch("Name", s -> s.owner, o -> {
/*     */           StateDefinition<O, S> definition = stateDefinition.apply(o);
/*     */           StateHolder stateHolder = defaultState.apply(o);
/*     */           return definition.isSingletonState() ? MapCodec.unit(stateHolder) : definition.propertiesCodec().codec().lenientOptionalFieldOf("Properties").xmap((), Optional::of);
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\StateHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */