/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ public final class PatchedDataComponentMap implements DataComponentMap {
/*     */   private final DataComponentMap prototype;
/*     */   
/*     */   private Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch;
/*     */   
/*     */   private boolean copyOnWrite;
/*     */   
/*     */   public PatchedDataComponentMap(DataComponentMap prototype) {
/*  31 */     this(prototype, Reference2ObjectMaps.emptyMap(), true);
/*     */   }
/*     */   
/*     */   private PatchedDataComponentMap(DataComponentMap prototype, Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch, boolean copyOnWrite) {
/*  35 */     this.prototype = prototype;
/*  36 */     this.patch = patch;
/*  37 */     this.copyOnWrite = copyOnWrite;
/*     */   }
/*     */   
/*     */   public static PatchedDataComponentMap fromPatch(DataComponentMap prototype, DataComponentPatch patch) {
/*  41 */     if (isPatchSanitized(prototype, patch.map))
/*  43 */       return new PatchedDataComponentMap(prototype, patch.map, true); 
/*  46 */     PatchedDataComponentMap map = new PatchedDataComponentMap(prototype);
/*  47 */     map.applyPatch(patch);
/*  48 */     return map;
/*     */   }
/*     */   
/*     */   private static boolean isPatchSanitized(DataComponentMap prototype, Reference2ObjectMap<DataComponentType<?>, Optional<?>> patch) {
/*  52 */     for (ObjectIterator<Map.Entry<DataComponentType<?>, Optional<?>>> objectIterator = Reference2ObjectMaps.fastIterable(patch).iterator(); objectIterator.hasNext(); ) {
/*  52 */       Map.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/*  53 */       Object defaultValue = prototype.get(entry.getKey());
/*  54 */       Optional<?> value = entry.getValue();
/*  55 */       if (value.isPresent() && value.get().equals(defaultValue))
/*  56 */         return false; 
/*  57 */       if (value.isEmpty() && defaultValue == null)
/*  58 */         return false; 
/*     */     } 
/*  61 */     return true;
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/*  67 */     return DataComponentPatch.getFromPatchAndPrototype(this.patch, this.prototype, type);
/*     */   }
/*     */   
/*     */   public boolean hasNonDefault(DataComponentType<?> type) {
/*  71 */     return this.patch.containsKey(type);
/*     */   }
/*     */   
/*     */   public <T> T set(DataComponentType<T> type, T value) {
/*     */     Optional<T> lastValue;
/*  86 */     ensureMapOwnership();
/*  87 */     T defaultValue = this.prototype.get(type);
/*  89 */     if (Objects.equals(value, defaultValue)) {
/*  90 */       lastValue = (Optional<T>)this.patch.remove(type);
/*     */     } else {
/*  92 */       lastValue = (Optional<T>)this.patch.put(type, Optional.ofNullable(value));
/*     */     } 
/*  94 */     if (lastValue != null)
/*  95 */       return lastValue.orElse(defaultValue); 
/*  97 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public <T> T set(TypedDataComponent<T> value) {
/* 101 */     return set(value.type(), value.value());
/*     */   }
/*     */   
/*     */   public <T> T remove(DataComponentType<? extends T> type) {
/*     */     Optional<? extends T> lastValue;
/* 111 */     ensureMapOwnership();
/* 112 */     T defaultValue = this.prototype.get(type);
/* 114 */     if (defaultValue != null) {
/* 115 */       lastValue = (Optional<? extends T>)this.patch.put(type, Optional.empty());
/*     */     } else {
/* 117 */       lastValue = (Optional<? extends T>)this.patch.remove(type);
/*     */     } 
/* 119 */     if (lastValue != null)
/* 120 */       return lastValue.orElse(null); 
/* 122 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public void applyPatch(DataComponentPatch patch) {
/* 126 */     ensureMapOwnership();
/* 127 */     for (ObjectIterator<Map.Entry<DataComponentType<?>, Optional<?>>> objectIterator = Reference2ObjectMaps.fastIterable(patch.map).iterator(); objectIterator.hasNext(); ) {
/* 127 */       Map.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 128 */       applyPatch(entry.getKey(), entry.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applyPatch(DataComponentType<?> type, Optional<?> value) {
/* 133 */     Object defaultValue = this.prototype.get(type);
/* 134 */     if (value.isPresent()) {
/* 135 */       if (value.get().equals(defaultValue)) {
/* 136 */         this.patch.remove(type);
/*     */       } else {
/* 138 */         this.patch.put(type, value);
/*     */       } 
/* 141 */     } else if (defaultValue != null) {
/* 142 */       this.patch.put(type, Optional.empty());
/*     */     } else {
/* 144 */       this.patch.remove(type);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void restorePatch(DataComponentPatch patch) {
/* 150 */     ensureMapOwnership();
/* 151 */     this.patch.clear();
/* 152 */     this.patch.putAll((Map)patch.map);
/*     */   }
/*     */   
/*     */   public void clearPatch() {
/* 156 */     ensureMapOwnership();
/* 157 */     this.patch.clear();
/*     */   }
/*     */   
/*     */   public void setAll(DataComponentMap components) {
/* 161 */     for (TypedDataComponent<?> entry : (Iterable<TypedDataComponent<?>>)components)
/* 162 */       entry.applyTo(this); 
/*     */   }
/*     */   
/*     */   private void ensureMapOwnership() {
/* 167 */     if (this.copyOnWrite) {
/* 168 */       this.patch = (Reference2ObjectMap<DataComponentType<?>, Optional<?>>)new Reference2ObjectArrayMap(this.patch);
/* 169 */       this.copyOnWrite = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<DataComponentType<?>> keySet() {
/* 176 */     if (this.patch.isEmpty())
/* 177 */       return this.prototype.keySet(); 
/* 179 */     ReferenceArraySet<DataComponentType> referenceArraySet = new ReferenceArraySet(this.prototype.keySet());
/* 180 */     for (ObjectIterator<Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>>> objectIterator = Reference2ObjectMaps.fastIterable(this.patch).iterator(); objectIterator.hasNext(); ) {
/* 180 */       Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 181 */       Optional<?> value = (Optional)entry.getValue();
/* 182 */       if (value.isPresent()) {
/* 183 */         referenceArraySet.add((DataComponentType)entry.getKey());
/*     */         continue;
/*     */       } 
/* 185 */       referenceArraySet.remove(entry.getKey());
/*     */     } 
/* 188 */     return (Set)referenceArraySet;
/*     */   }
/*     */   
/*     */   public Iterator<TypedDataComponent<?>> iterator() {
/* 193 */     if (this.patch.isEmpty())
/* 194 */       return this.prototype.iterator(); 
/* 196 */     List<TypedDataComponent<?>> components = new ArrayList<>(this.patch.size() + this.prototype.size());
/* 197 */     for (ObjectIterator<Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>>> objectIterator = Reference2ObjectMaps.fastIterable(this.patch).iterator(); objectIterator.hasNext(); ) {
/* 197 */       Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 198 */       if (((Optional)entry.getValue()).isPresent())
/* 199 */         components.add(TypedDataComponent.createUnchecked((DataComponentType)entry.getKey(), ((Optional)entry.getValue()).get())); 
/*     */     } 
/* 202 */     for (TypedDataComponent<?> component : (Iterable<TypedDataComponent<?>>)this.prototype) {
/* 203 */       if (!this.patch.containsKey(component.type()))
/* 204 */         components.add(component); 
/*     */     } 
/* 207 */     return components.iterator();
/*     */   }
/*     */   
/*     */   public int size() {
/* 212 */     int size = this.prototype.size();
/* 213 */     for (ObjectIterator<Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>>> objectIterator = Reference2ObjectMaps.fastIterable(this.patch).iterator(); objectIterator.hasNext(); ) {
/* 213 */       Reference2ObjectMap.Entry<DataComponentType<?>, Optional<?>> entry = objectIterator.next();
/* 214 */       boolean inPatch = ((Optional)entry.getValue()).isPresent();
/* 215 */       boolean inPrototype = this.prototype.has((DataComponentType)entry.getKey());
/* 216 */       if (inPatch != inPrototype)
/* 217 */         size += inPatch ? 1 : -1; 
/*     */     } 
/* 220 */     return size;
/*     */   }
/*     */   
/*     */   public DataComponentPatch asPatch() {
/* 224 */     if (this.patch.isEmpty())
/* 225 */       return DataComponentPatch.EMPTY; 
/* 227 */     this.copyOnWrite = true;
/* 228 */     return new DataComponentPatch(this.patch);
/*     */   }
/*     */   
/*     */   public PatchedDataComponentMap copy() {
/* 232 */     this.copyOnWrite = true;
/* 233 */     return new PatchedDataComponentMap(this.prototype, this.patch, true);
/*     */   }
/*     */   
/*     */   public DataComponentMap toImmutableMap() {
/* 237 */     if (this.patch.isEmpty())
/* 238 */       return this.prototype; 
/* 240 */     return copy();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 245 */     if (this == obj)
/* 246 */       return true; 
/* 248 */     if (obj instanceof PatchedDataComponentMap) {
/* 248 */       PatchedDataComponentMap otherMap = (PatchedDataComponentMap)obj;
/* 248 */       if (this.prototype.equals(otherMap.prototype) && this.patch.equals(otherMap.patch));
/*     */     } 
/* 248 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 253 */     return this.prototype.hashCode() + this.patch.hashCode() * 31;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 258 */     return "{" + (String)stream().<CharSequence>map(TypedDataComponent::toString).collect(Collectors.joining(", ")) + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\PatchedDataComponentMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */