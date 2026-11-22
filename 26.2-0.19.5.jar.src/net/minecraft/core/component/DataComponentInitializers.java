/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public class DataComponentInitializers {
/*  18 */   private final List<InitializerEntry<?>> initializers = new ArrayList<>();
/*     */   
/*     */   public <T> void add(ResourceKey<T> key, Initializer<T> initializer) {
/*  21 */     this.initializers.add(new InitializerEntry(key, initializer));
/*     */   }
/*     */   
/*     */   private Map<ResourceKey<?>, DataComponentMap.Builder> runInitializers(HolderLookup.Provider context) {
/*  25 */     Map<ResourceKey<?>, DataComponentMap.Builder> results = new HashMap<>();
/*  27 */     for (InitializerEntry<?> initializer : this.initializers) {
/*  28 */       DataComponentMap.Builder builder = results.computeIfAbsent(initializer.key, k -> DataComponentMap.builder());
/*  29 */       initializer.run(builder, context);
/*     */     } 
/*  32 */     return results;
/*     */   }
/*     */   
/*     */   private static <T> void registryEmpty(Map<ResourceKey<? extends Registry<?>>, PendingComponentBuilders<?>> buildersByRegistry, ResourceKey<? extends Registry<? extends T>> registryKey) {
/*  38 */     ResourceKey<? extends Registry<? extends T>> resourceKey = registryKey;
/*  39 */     buildersByRegistry.put(registryKey, new PendingComponentBuilders(resourceKey, new HashMap<>()));
/*     */   }
/*     */   
/*     */   private static <T> void addBuilder(Map<ResourceKey<? extends Registry<?>>, PendingComponentBuilders<?>> buildersByRegistry, ResourceKey<T> key, DataComponentMap.Builder builder) {
/*  43 */     PendingComponentBuilders<T> buildersForRegistry = (PendingComponentBuilders<T>)buildersByRegistry.get(key.registryKey());
/*  44 */     buildersForRegistry.builders.put(key, builder);
/*     */   }
/*     */   
/*     */   public List<PendingComponents<?>> build(HolderLookup.Provider context) {
/*  48 */     Map<ResourceKey<? extends Registry<?>>, PendingComponentBuilders<?>> buildersByRegistry = new HashMap<>();
/*  51 */     context.listRegistryKeys().forEach(registryKey -> registryEmpty(buildersByRegistry, registryKey));
/*  52 */     runInitializers(context).forEach((key, builder) -> addBuilder(buildersByRegistry, key, builder));
/*  53 */     return (List<PendingComponents<?>>)buildersByRegistry.values().stream().map(elementBuilders -> createInitializerForRegistry(context, elementBuilders)).collect(Collectors.toUnmodifiableList());
/*     */   }
/*     */   
/*     */   private static <T> PendingComponents<T> createInitializerForRegistry(HolderLookup.Provider context, PendingComponentBuilders<T> elementBuilders) {
/*  57 */     final List<BakedEntry<T>> entries = new ArrayList<>();
/*  59 */     final ResourceKey<? extends Registry<T>> registryKey = elementBuilders.registryKey;
/*  60 */     HolderLookup.RegistryLookup<T> registry = context.lookupOrThrow(registryKey);
/*  62 */     Set<Holder.Reference<T>> elementsWithComponents = Sets.newIdentityHashSet();
/*  64 */     elementBuilders.builders.forEach((elementKey, elementBuilder) -> {
/*     */           Holder.Reference<T> element = registry.getOrThrow(elementKey);
/*     */           DataComponentMap components = elementBuilder.build();
/*     */           entries.add(new BakedEntry<>(element, components));
/*     */           elementsWithComponents.add(element);
/*     */         });
/*  73 */     registry.listElements().filter(e -> !elementsWithComponents.contains(e)).forEach(elementWithoutComponents -> entries.add(new BakedEntry(elementWithoutComponents, DataComponentMap.EMPTY)));
/*  77 */     return new PendingComponents<T>() {
/*     */         public ResourceKey<? extends Registry<? extends T>> key() {
/*  80 */           return registryKey;
/*     */         }
/*     */         
/*     */         public void forEach(BiConsumer<Holder.Reference<T>, DataComponentMap> output) {
/*  85 */           entries.forEach(e -> output.accept(e.element, e.components));
/*     */         }
/*     */         
/*     */         public void apply() {
/*  90 */           entries.forEach(DataComponentInitializers.BakedEntry::apply);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static final class InitializerEntry<T> extends Record {
/*     */     private final ResourceKey<T> key;
/*     */     
/*     */     private final DataComponentInitializers.Initializer<T> initializer;
/*     */     
/*     */     private InitializerEntry(ResourceKey<T> key, DataComponentInitializers.Initializer<T> initializer) {
/*  95 */       this.key = key;
/*  95 */       this.initializer = initializer;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #95	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry<TT;>;
/*     */     }
/*     */     
/*     */     public ResourceKey<T> key() {
/*  95 */       return this.key;
/*     */     }
/*     */     
/*     */     public DataComponentInitializers.Initializer<T> initializer() {
/*  95 */       return this.initializer;
/*     */     }
/*     */     
/*     */     public void run(DataComponentMap.Builder components, HolderLookup.Provider context) {
/* 100 */       this.initializer.run(components, context, this.key);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class BakedEntry<T> extends Record {
/*     */     private final Holder.Reference<T> element;
/*     */     
/*     */     private final DataComponentMap components;
/*     */     
/*     */     private BakedEntry(Holder.Reference<T> element, DataComponentMap components) {
/* 104 */       this.element = element;
/* 104 */       this.components = components;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$BakedEntry<TT;>;
/*     */     }
/*     */     
/*     */     public Holder.Reference<T> element() {
/* 104 */       return this.element;
/*     */     }
/*     */     
/*     */     public DataComponentMap components() {
/* 104 */       return this.components;
/*     */     }
/*     */     
/*     */     public void apply() {
/* 109 */       this.element.bindComponents(this.components);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Initializer<T> {
/*     */     default Initializer<T> andThen(Initializer<T> other) {
/* 118 */       return (components, context, key) -> {
/*     */           run(components, context, key);
/*     */           other.run(components, context, key);
/*     */         };
/*     */     }
/*     */     
/*     */     default <C> Initializer<T> add(DataComponentType<C> type, C value) {
/* 125 */       return andThen((components, context, key) -> components.set(type, value));
/*     */     }
/*     */     
/*     */     void run(DataComponentMap.Builder param1Builder, HolderLookup.Provider param1Provider, ResourceKey<T> param1ResourceKey);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SingleComponentInitializer<C> {
/*     */     C create(HolderLookup.Provider param1Provider);
/*     */     
/*     */     default <T> DataComponentInitializers.Initializer<T> asInitializer(DataComponentType<C> type) {
/* 134 */       return (components, context, key) -> components.set(type, create(context));
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class PendingComponentBuilders<T> extends Record {
/*     */     private final ResourceKey<? extends Registry<T>> registryKey;
/*     */     
/*     */     private final Map<ResourceKey<T>, DataComponentMap.Builder> builders;
/*     */     
/*     */     private PendingComponentBuilders(ResourceKey<? extends Registry<T>> registryKey, Map<ResourceKey<T>, DataComponentMap.Builder> builders) {
/* 146 */       this.registryKey = registryKey;
/* 146 */       this.builders = builders;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #146	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #146	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #146	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$PendingComponentBuilders<TT;>;
/*     */     }
/*     */     
/*     */     public ResourceKey<? extends Registry<T>> registryKey() {
/* 146 */       return this.registryKey;
/*     */     }
/*     */     
/*     */     public Map<ResourceKey<T>, DataComponentMap.Builder> builders() {
/* 146 */       return this.builders;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface PendingComponents<T> {
/*     */     ResourceKey<? extends Registry<? extends T>> key();
/*     */     
/*     */     void forEach(BiConsumer<Holder.Reference<T>, DataComponentMap> param1BiConsumer);
/*     */     
/*     */     void apply();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentInitializers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */