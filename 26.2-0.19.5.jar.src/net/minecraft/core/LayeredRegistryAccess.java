/*     */ package net.minecraft.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class LayeredRegistryAccess<T> {
/*     */   private final List<T> keys;
/*     */   
/*     */   private final List<RegistryAccess.Frozen> values;
/*     */   
/*     */   private final RegistryAccess.Frozen composite;
/*     */   
/*     */   public LayeredRegistryAccess(List<T> keys) {
/*  21 */     this(keys, 
/*     */         
/*  23 */         (List<RegistryAccess.Frozen>)Util.make(() -> {
/*     */             RegistryAccess.Frozen[] layers = new RegistryAccess.Frozen[keys.size()];
/*     */             Arrays.fill((Object[])layers, RegistryAccess.EMPTY);
/*     */             return Arrays.asList(layers);
/*     */           }));
/*     */   }
/*     */   
/*     */   private LayeredRegistryAccess(List<T> keys, List<RegistryAccess.Frozen> values) {
/*  32 */     this.keys = List.copyOf(keys);
/*  33 */     this.values = List.copyOf(values);
/*  34 */     this.composite = (new RegistryAccess.ImmutableRegistryAccess(collectRegistries(values.stream()))).freeze();
/*     */   }
/*     */   
/*     */   private int getLayerIndexOrThrow(T layer) {
/*  38 */     int index = this.keys.indexOf(layer);
/*  39 */     if (index == -1)
/*  40 */       throw new IllegalStateException("Can't find " + String.valueOf(layer) + " inside " + String.valueOf(this.keys)); 
/*  42 */     return index;
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getLayer(T layer) {
/*  46 */     int index = getLayerIndexOrThrow(layer);
/*  47 */     return this.values.get(index);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getAccessForLoading(T forLayer) {
/*  51 */     int index = getLayerIndexOrThrow(forLayer);
/*  52 */     return getCompositeAccessForLayers(0, index);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen getAccessFrom(T forLayer) {
/*  56 */     int index = getLayerIndexOrThrow(forLayer);
/*  57 */     return getCompositeAccessForLayers(index, this.values.size());
/*     */   }
/*     */   
/*     */   private RegistryAccess.Frozen getCompositeAccessForLayers(int from, int to) {
/*  61 */     return (new RegistryAccess.ImmutableRegistryAccess(collectRegistries(this.values.subList(from, to).stream()))).freeze();
/*     */   }
/*     */   
/*     */   public LayeredRegistryAccess<T> replaceFrom(T fromLayer, RegistryAccess.Frozen... layers) {
/*  65 */     return replaceFrom(fromLayer, Arrays.asList(layers));
/*     */   }
/*     */   
/*     */   public LayeredRegistryAccess<T> replaceFrom(T fromLayer, List<RegistryAccess.Frozen> layers) {
/*  69 */     int index = getLayerIndexOrThrow(fromLayer);
/*  71 */     if (layers.size() > this.values.size() - index)
/*  72 */       throw new IllegalStateException("Too many values to replace"); 
/*  75 */     List<RegistryAccess.Frozen> newValues = new ArrayList<>();
/*  77 */     for (int i = 0; i < index; i++)
/*  78 */       newValues.add(this.values.get(i)); 
/*  81 */     newValues.addAll(layers);
/*  83 */     while (newValues.size() < this.values.size())
/*  84 */       newValues.add(RegistryAccess.EMPTY); 
/*  86 */     return new LayeredRegistryAccess(this.keys, newValues);
/*     */   }
/*     */   
/*     */   public RegistryAccess.Frozen compositeAccess() {
/*  90 */     return this.composite;
/*     */   }
/*     */   
/*     */   private static Map<ResourceKey<? extends Registry<?>>, Registry<?>> collectRegistries(Stream<? extends RegistryAccess> registries) {
/*  94 */     Map<ResourceKey<? extends Registry<?>>, Registry<?>> result = new HashMap<>();
/*  96 */     registries.forEach(access -> access.registries().forEach(()));
/* 104 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\LayeredRegistryAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */