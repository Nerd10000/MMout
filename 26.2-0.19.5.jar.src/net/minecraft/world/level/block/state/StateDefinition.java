/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSortedMap;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class StateDefinition<O, S extends StateHolder<O, S>> {
/*  28 */   private static final Pattern NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
/*     */   
/*  29 */   private static final Comparable<?>[] EMPTY_VALUES = (Comparable<?>[])new Comparable[0];
/*     */   
/*  30 */   private static final Property<?>[] EMPTY_KEYS = (Property<?>[])new Property[0];
/*     */   
/*  31 */   private static final StateHolder<?, ?>[][] EMPTY_NEIGHBORS = (StateHolder<?, ?>[][])new StateHolder[0][];
/*     */   
/*     */   private final O owner;
/*     */   
/*     */   private final ImmutableSortedMap<String, Property<?>> propertiesByName;
/*     */   
/*     */   private final ImmutableList<S> states;
/*     */   
/*     */   private final MapCodec<S> propertiesCodec;
/*     */   
/*     */   protected StateDefinition(Function<O, S> defaultState, O owner, Factory<O, S> factory, Map<String, Property<?>> properties) {
/*  40 */     this.owner = owner;
/*  42 */     int propertyCount = properties.size();
/*  43 */     if (propertyCount == 0) {
/*  44 */       this.propertiesByName = ImmutableSortedMap.of();
/*  45 */       this.propertiesCodec = createCodec(owner, defaultState, (Map<String, Property<?>>)this.propertiesByName);
/*  46 */       this.states = createSingletonState(owner, factory);
/*     */     } else {
/*  48 */       this.propertiesByName = ImmutableSortedMap.copyOf(properties);
/*  49 */       this.propertiesCodec = createCodec(owner, defaultState, (Map<String, Property<?>>)this.propertiesByName);
/*  50 */       if (propertyCount == 1) {
/*  51 */         this.states = createSinglePropertyStates(owner, factory, (Map<String, Property<?>>)this.propertiesByName);
/*     */       } else {
/*  53 */         this.states = createMultiPropertyStates(owner, factory, (Map<String, Property<?>>)this.propertiesByName);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <O, S extends StateHolder<O, S>> MapCodec<S> createCodec(O owner, Function<O, S> defaultState, Map<String, Property<?>> propertiesByName) {
/*  59 */     Supplier<S> defaultSupplier = () -> (StateHolder)defaultState.apply(owner);
/*  60 */     MapCodec<S> codec = MapCodec.unit(defaultSupplier);
/*  61 */     for (Map.Entry<String, Property<?>> entry : propertiesByName.entrySet())
/*  62 */       codec = appendPropertyCodec(codec, defaultSupplier, entry.getKey(), (Property<Comparable>)entry.getValue()); 
/*  64 */     return codec;
/*     */   }
/*     */   
/*     */   private static <O, S extends StateHolder<O, S>> ImmutableList<S> createSingletonState(O owner, Factory<O, S> factory) {
/*  68 */     StateHolder stateHolder = (StateHolder)factory.create(owner, EMPTY_KEYS, EMPTY_VALUES);
/*  69 */     stateHolder.initializeNeighbors(emptyNeighbors());
/*  70 */     return ImmutableList.of(stateHolder);
/*     */   }
/*     */   
/*     */   private static <O, S extends StateHolder<O, S>> ImmutableList<S> createSinglePropertyStates(O owner, Factory<O, S> factory, Map<String, Property<?>> propertiesByName) {
/*  74 */     return createSinglePropertyStates(owner, factory, (Property<Comparable>)Iterables.getOnlyElement(propertiesByName.values()));
/*     */   }
/*     */   
/*     */   private static <O, S extends StateHolder<O, S>, T extends Comparable<T>> ImmutableList<S> createSinglePropertyStates(O owner, Factory<O, S> factory, Property<T> property) {
/*  78 */     Property[] arrayOfProperty = { property };
/*  79 */     List<T> propertyValues = property.getPossibleValues();
/*  80 */     int valueCount = propertyValues.size();
/*  81 */     ImmutableList.Builder<S> states = ImmutableList.builderWithExpectedSize(valueCount);
/*  83 */     StateHolder[] arrayOfStateHolder = new StateHolder[valueCount];
/*  85 */     StateHolder[][] arrayOfStateHolder1 = { arrayOfStateHolder };
/*  87 */     for (int i = 0; i < valueCount; i++) {
/*  88 */       Comparable comparable = (Comparable)propertyValues.get(i);
/*  89 */       assert property.getInternalIndex(comparable) == i;
/*  90 */       StateHolder stateHolder = (StateHolder)factory.create(owner, (Property<?>[])arrayOfProperty, (Comparable<?>[])new Comparable[] { comparable });
/*  91 */       states.add(stateHolder);
/*  92 */       arrayOfStateHolder[i] = stateHolder;
/*  93 */       stateHolder.initializeNeighbors((Object[][])arrayOfStateHolder1);
/*     */     } 
/*  95 */     return states.build();
/*     */   }
/*     */   
/*     */   private static <O, S extends StateHolder<O, S>> ImmutableList<S> createMultiPropertyStates(O owner, Factory<O, S> factory, Map<String, Property<?>> propertiesByName) {
/*  99 */     Property[] arrayOfProperty = (Property[])propertiesByName.values().toArray((Object[])EMPTY_KEYS);
/* 100 */     List<List<? extends Comparable<?>>> allPropertyValues = new ArrayList<>(arrayOfProperty.length);
/* 101 */     for (Property<?> property : arrayOfProperty)
/* 102 */       allPropertyValues.add(property.getPossibleValues()); 
/* 104 */     List<List<Comparable<?>>> stateValues = Lists.cartesianProduct(allPropertyValues);
/* 107 */     Map<List<Comparable<?>>, S> statesByValues = new HashMap<>();
/* 108 */     ImmutableList.Builder<S> states = ImmutableList.builderWithExpectedSize(stateValues.size());
/* 110 */     for (List<Comparable<?>> values : stateValues) {
/* 112 */       List<Comparable<?>> valuesCopy = List.copyOf(values);
/* 113 */       StateHolder stateHolder = (StateHolder)factory.create(owner, (Property<?>[])arrayOfProperty, (Comparable<?>[])valuesCopy.<Comparable>toArray((Comparable[])EMPTY_VALUES));
/* 114 */       statesByValues.put(valuesCopy, (S)stateHolder);
/* 115 */       states.add(stateHolder);
/*     */     } 
/* 118 */     StateCollection<S> stateCollection = (StateCollection)new StateCollection<>(statesByValues, (Map)new HashMap<>());
/* 119 */     statesByValues.forEach((values, state) -> state.initializeNeighbors(stateCollection.fillNeighborsForState((Property<?>[])propertyKeys, values)));
/* 122 */     return states.build();
/*     */   }
/*     */   
/*     */   private static <S extends StateHolder<?, ?>> S[][] emptyNeighbors() {
/* 127 */     return (S[][])EMPTY_NEIGHBORS;
/*     */   }
/*     */   
/*     */   private static <S extends StateHolder<?, S>, T extends Comparable<T>> MapCodec<S> appendPropertyCodec(MapCodec<S> codec, Supplier<S> defaultSupplier, String name, Property<T> property) {
/* 131 */     return Codec.mapPair(codec, property
/*     */         
/* 133 */         .valueCodec().fieldOf(name).orElseGet(paramString -> {
/*     */           
/* 134 */           }() -> property.value(defaultSupplier.get()))).xmap(pair -> (StateHolder)((StateHolder)pair.getFirst()).setValue(property, ((Property.Value)pair.getSecond()).value()), state -> Pair.of(state, property.value(state)));
/*     */   }
/*     */   
/*     */   public ImmutableList<S> getPossibleStates() {
/* 141 */     return this.states;
/*     */   }
/*     */   
/*     */   public S any() {
/* 145 */     return (S)this.states.getFirst();
/*     */   }
/*     */   
/*     */   public MapCodec<S> propertiesCodec() {
/* 149 */     return this.propertiesCodec;
/*     */   }
/*     */   
/*     */   public O getOwner() {
/* 153 */     return this.owner;
/*     */   }
/*     */   
/*     */   public Collection<Property<?>> getProperties() {
/* 157 */     return (Collection<Property<?>>)this.propertiesByName.values();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 162 */     return MoreObjects.toStringHelper(this)
/* 163 */       .add("block", this.owner)
/* 164 */       .add("properties", this.propertiesByName.values().stream().map(Property::getName).collect(Collectors.toList()))
/* 165 */       .toString();
/*     */   }
/*     */   
/*     */   public Property<?> getProperty(String name) {
/* 169 */     return (Property)this.propertiesByName.get(name);
/*     */   }
/*     */   
/*     */   public boolean isSingletonState() {
/* 173 */     return this.propertiesByName.isEmpty();
/*     */   }
/*     */   
/*     */   public static class Builder<O, S extends StateHolder<O, S>> {
/*     */     private final O owner;
/*     */     
/* 182 */     private final Map<String, Property<?>> properties = Maps.newHashMap();
/*     */     
/*     */     public Builder(O owner) {
/* 185 */       this.owner = owner;
/*     */     }
/*     */     
/*     */     public Builder<O, S> add(Property<?>... properties) {
/* 189 */       for (Property<?> property : properties) {
/* 190 */         validateProperty(property);
/* 191 */         this.properties.put(property.getName(), property);
/*     */       } 
/* 193 */       return this;
/*     */     }
/*     */     
/*     */     private <T extends Comparable<T>> void validateProperty(Property<T> property) {
/* 197 */       String name = property.getName();
/* 198 */       if (!StateDefinition.NAME_PATTERN.matcher(name).matches())
/* 199 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " has invalidly named property: " + String.valueOf(this.owner)); 
/* 202 */       Collection<T> values = property.getPossibleValues();
/* 203 */       if (values.size() <= 1)
/* 204 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " attempted use property " + String.valueOf(this.owner) + " with <= 1 possible values"); 
/* 207 */       for (Comparable comparable : values) {
/* 208 */         String valueName = property.getName(comparable);
/* 209 */         if (!StateDefinition.NAME_PATTERN.matcher(valueName).matches())
/* 210 */           throw new IllegalArgumentException(String.valueOf(this.owner) + " has property: " + String.valueOf(this.owner) + " with invalidly named value: " + name); 
/*     */       } 
/* 214 */       if (this.properties.containsKey(name))
/* 215 */         throw new IllegalArgumentException(String.valueOf(this.owner) + " has duplicate property: " + String.valueOf(this.owner)); 
/*     */     }
/*     */     
/*     */     public StateDefinition<O, S> create(Function<O, S> defaultState, StateDefinition.Factory<O, S> factory) {
/* 220 */       return new StateDefinition<>(defaultState, this.owner, factory, this.properties);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class StateCollection<S extends StateHolder<?, ?>> extends Record {
/*     */     private final Map<List<Comparable<?>>, S> statesByValues;
/*     */     
/*     */     private final Map<List<Comparable<?>>, S[]> statesByPivotCache;
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection<TS;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection<TS;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #225	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/state/StateDefinition$StateCollection<TS;>;
/*     */     }
/*     */     
/*     */     public Map<List<Comparable<?>>, S> statesByValues() {
/* 225 */       return this.statesByValues;
/*     */     }
/*     */     
/*     */     public Map<List<Comparable<?>>, S[]> statesByPivotCache() {
/* 225 */       return this.statesByPivotCache;
/*     */     }
/*     */     
/*     */     StateCollection(Map<List<Comparable<?>>, S> statesByValues, Map<List<Comparable<?>>, S[]> statesByPivotCache) {
/* 226 */       this.statesByValues = statesByValues;
/* 226 */       this.statesByPivotCache = statesByPivotCache;
/*     */     }
/*     */     
/*     */     public S[][] fillNeighborsForState(Property<?>[] propertyKeys, List<Comparable<?>> propertyValues) {
/* 232 */       StateHolder[][] arrayOfStateHolder = new StateHolder[propertyKeys.length][];
/* 233 */       List<Comparable<?>> valuesKey = new ArrayList<>(propertyValues);
/* 234 */       for (int i = 0; i < propertyKeys.length; i++)
/* 235 */         arrayOfStateHolder[i] = fillStatesForPivot(valuesKey, propertyKeys[i], i); 
/* 237 */       return (S[][])arrayOfStateHolder;
/*     */     }
/*     */     
/*     */     private <T extends Comparable<T>> S[] fillStatesForPivot(List<Comparable<?>> valuesKey, Property<T> pivot, int pivotIndex) {
/* 242 */       Comparable<?> ownPivotValue = valuesKey.set(pivotIndex, Wildcard.INSTANCE);
/*     */       try {
/* 245 */         StateHolder[] arrayOfStateHolder = (StateHolder[])this.statesByPivotCache.get(valuesKey);
/* 246 */         if (arrayOfStateHolder != null)
/* 247 */           return (S[])arrayOfStateHolder; 
/* 250 */         S[] neighbourStatesForPivot = computeStatesForPivot(valuesKey, pivot, pivotIndex);
/* 252 */         valuesKey.set(pivotIndex, Wildcard.INSTANCE);
/* 253 */         this.statesByPivotCache.put(List.copyOf(valuesKey), neighbourStatesForPivot);
/* 254 */         return neighbourStatesForPivot;
/*     */       } finally {
/* 257 */         valuesKey.set(pivotIndex, ownPivotValue);
/*     */       } 
/*     */     }
/*     */     
/*     */     private <T extends Comparable<T>> S[] computeStatesForPivot(List<Comparable<?>> valuesKey, Property<T> pivot, int pivotIndex) {
/* 262 */       List<T> possiblePivotValues = pivot.getPossibleValues();
/* 263 */       int pivotValuesCount = possiblePivotValues.size();
/* 264 */       StateHolder[] arrayOfStateHolder = new StateHolder[pivotValuesCount];
/* 265 */       for (int pivotValueIndex = 0; pivotValueIndex < pivotValuesCount; pivotValueIndex++) {
/* 266 */         Comparable<?> comparable = (Comparable)possiblePivotValues.get(pivotValueIndex);
/* 267 */         assert pivot.getInternalIndex(comparable) == pivotValueIndex;
/* 268 */         valuesKey.set(pivotIndex, comparable);
/* 269 */         StateHolder stateHolder = Objects.<StateHolder>requireNonNull((StateHolder)this.statesByValues.get(valuesKey));
/* 270 */         arrayOfStateHolder[pivotValueIndex] = stateHolder;
/*     */       } 
/* 272 */       return (S[])arrayOfStateHolder;
/*     */     }
/*     */     
/*     */     private enum Wildcard {
/* 276 */       INSTANCE;
/*     */     }
/*     */   }
/*     */   
/*     */   private enum Wildcard {
/* 276 */     INSTANCE;
/*     */   }
/*     */   
/*     */   public static interface Factory<O, S> {
/*     */     S create(O param1O, Property<?>[] param1ArrayOfProperty, Comparable<?>[] param1ArrayOfComparable);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\StateDefinition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */