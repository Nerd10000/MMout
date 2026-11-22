/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectList;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponentLookup;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class MappedRegistry<T> implements WritableRegistry<T> {
/*     */   private final ResourceKey<? extends Registry<T>> key;
/*     */   
/*  35 */   private final ObjectList<Holder.Reference<T>> byId = (ObjectList<Holder.Reference<T>>)new ObjectArrayList(256);
/*     */   
/*     */   private final Reference2IntMap<T> toId;
/*     */   
/*     */   private final Map<Identifier, Holder.Reference<T>> byLocation;
/*     */   
/*     */   private final Map<ResourceKey<T>, Holder.Reference<T>> byKey;
/*     */   
/*     */   private final Map<T, Holder.Reference<T>> byValue;
/*     */   
/*     */   private final Map<ResourceKey<T>, RegistrationInfo> registrationInfos;
/*     */   
/*     */   private Lifecycle registryLifecycle;
/*     */   
/*     */   private final Map<TagKey<T>, HolderSet.Named<T>> frozenTags;
/*     */   
/*     */   private TagSet<T> allTags;
/*     */   
/*     */   private DataComponentLookup<T> componentLookup;
/*     */   
/*     */   private boolean frozen;
/*     */   
/*     */   private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
/*     */   
/*     */   public MappedRegistry(ResourceKey<? extends Registry<T>> key, Lifecycle initialLifecycle, boolean intrusiveHolders) {
/*  36 */     this.toId = (Reference2IntMap<T>)Util.make(new Reference2IntOpenHashMap(), t -> t.defaultReturnValue(-1));
/*  38 */     this.byLocation = new HashMap<>();
/*  39 */     this.byKey = new HashMap<>();
/*  40 */     this.byValue = new IdentityHashMap<>();
/*  42 */     this.registrationInfos = new IdentityHashMap<>();
/*  45 */     this.frozenTags = new IdentityHashMap<>();
/*  47 */     this.allTags = TagSet.unbound();
/*  65 */     this.key = key;
/*  66 */     this.registryLifecycle = initialLifecycle;
/*  67 */     if (intrusiveHolders)
/*  68 */       this.unregisteredIntrusiveHolders = new IdentityHashMap<>(); 
/*     */   }
/*     */   
/*     */   public Stream<HolderSet.Named<T>> listTags() {
/*     */     return getTags();
/*     */   }
/*     */   
/*     */   public MappedRegistry(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle) {
/*     */     this(key, lifecycle, false);
/*     */   }
/*     */   
/*     */   public ResourceKey<? extends Registry<T>> key() {
/*  74 */     return this.key;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  79 */     return "Registry[" + String.valueOf(this.key) + " (" + String.valueOf(this.registryLifecycle) + ")]";
/*     */   }
/*     */   
/*     */   private void validateWrite() {
/*  83 */     if (this.frozen)
/*  84 */       throw new IllegalStateException("Registry is already frozen"); 
/*     */   }
/*     */   
/*     */   private void validateWrite(ResourceKey<T> key) {
/*  89 */     if (this.frozen)
/*  90 */       throw new IllegalStateException("Registry is already frozen (trying to add key " + String.valueOf(key) + ")"); 
/*     */   }
/*     */   
/*     */   public Holder.Reference<T> register(ResourceKey<T> key, T value, RegistrationInfo registrationInfo) {
/*     */     Holder.Reference<T> holder;
/*  96 */     validateWrite(key);
/*  97 */     Objects.requireNonNull(key);
/*  98 */     Objects.requireNonNull(value);
/* 100 */     if (this.byLocation.containsKey(key.identifier()))
/* 101 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Adding duplicate key '" + String.valueOf(key) + "' to registry")); 
/* 104 */     if (this.byValue.containsKey(value))
/* 105 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Adding duplicate value '" + String.valueOf(value) + "' to registry")); 
/* 109 */     if (this.unregisteredIntrusiveHolders != null) {
/* 111 */       holder = this.unregisteredIntrusiveHolders.remove(value);
/* 112 */       if (holder == null)
/* 113 */         throw new AssertionError("Missing intrusive holder for " + String.valueOf(key) + ":" + String.valueOf(value)); 
/* 115 */       holder.bindKey(key);
/*     */     } else {
/* 118 */       holder = this.byKey.computeIfAbsent(key, k -> Holder.Reference.createStandAlone(this, k));
/*     */     } 
/* 121 */     this.byKey.put(key, holder);
/* 122 */     this.byLocation.put(key.identifier(), holder);
/* 123 */     this.byValue.put(value, holder);
/* 125 */     int newId = this.byId.size();
/* 126 */     this.byId.add(holder);
/* 127 */     this.toId.put(value, newId);
/* 129 */     this.registrationInfos.put(key, registrationInfo);
/* 130 */     this.registryLifecycle = this.registryLifecycle.add(registrationInfo.lifecycle());
/* 131 */     return holder;
/*     */   }
/*     */   
/*     */   public Identifier getKey(T thing) {
/* 136 */     Holder.Reference<T> holder = this.byValue.get(thing);
/* 137 */     return (holder != null) ? holder.key().identifier() : null;
/*     */   }
/*     */   
/*     */   public Optional<ResourceKey<T>> getResourceKey(T thing) {
/* 142 */     return Optional.<Holder.Reference>ofNullable(this.byValue.get(thing)).map(Holder.Reference::key);
/*     */   }
/*     */   
/*     */   public int getId(T thing) {
/* 147 */     return this.toId.getInt(thing);
/*     */   }
/*     */   
/*     */   public T getValue(ResourceKey<T> key) {
/* 152 */     return getValueFromNullable(this.byKey.get(key));
/*     */   }
/*     */   
/*     */   public T byId(int id) {
/* 157 */     if (id < 0 || id >= this.byId.size())
/* 158 */       return null; 
/* 160 */     return ((Holder.Reference<T>)this.byId.get(id)).value();
/*     */   }
/*     */   
/*     */   public Optional<Holder.Reference<T>> get(int id) {
/* 165 */     if (id < 0 || id >= this.byId.size())
/* 166 */       return Optional.empty(); 
/* 168 */     return Optional.ofNullable((Holder.Reference<T>)this.byId.get(id));
/*     */   }
/*     */   
/*     */   public Optional<Holder.Reference<T>> get(Identifier id) {
/* 173 */     return Optional.ofNullable(this.byLocation.get(id));
/*     */   }
/*     */   
/*     */   public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 178 */     return Optional.ofNullable(this.byKey.get(id));
/*     */   }
/*     */   
/*     */   public Optional<Holder.Reference<T>> getAny() {
/* 183 */     return this.byId.isEmpty() ? Optional.<Holder.Reference<T>>empty() : Optional.<Holder.Reference<T>>of((Holder.Reference<T>)this.byId.getFirst());
/*     */   }
/*     */   
/*     */   public Holder<T> wrapAsHolder(T value) {
/* 188 */     Holder.Reference<T> existingHolder = this.byValue.get(value);
/* 189 */     return (existingHolder != null) ? existingHolder : Holder.<T>direct(value);
/*     */   }
/*     */   
/*     */   private Holder.Reference<T> getOrCreateHolderOrThrow(ResourceKey<T> key) {
/* 193 */     return this.byKey.computeIfAbsent(key, id -> {
/*     */           if (this.unregisteredIntrusiveHolders != null)
/*     */             throw new IllegalStateException("This registry can't create new holders without value"); 
/*     */           validateWrite(id);
/*     */           return Holder.Reference.createStandAlone(this, id);
/*     */         });
/*     */   }
/*     */   
/*     */   public int size() {
/* 204 */     return this.byKey.size();
/*     */   }
/*     */   
/*     */   public Optional<RegistrationInfo> registrationInfo(ResourceKey<T> element) {
/* 209 */     return Optional.ofNullable(this.registrationInfos.get(element));
/*     */   }
/*     */   
/*     */   public Lifecycle registryLifecycle() {
/* 214 */     return this.registryLifecycle;
/*     */   }
/*     */   
/*     */   public Iterator<T> iterator() {
/* 219 */     return Iterators.transform((Iterator)this.byId.iterator(), Holder::value);
/*     */   }
/*     */   
/*     */   public T getValue(Identifier key) {
/* 224 */     Holder.Reference<T> result = this.byLocation.get(key);
/* 225 */     return getValueFromNullable(result);
/*     */   }
/*     */   
/*     */   private static <T> T getValueFromNullable(Holder.Reference<T> result) {
/* 229 */     return (result != null) ? result.value() : null;
/*     */   }
/*     */   
/*     */   public Set<Identifier> keySet() {
/* 234 */     return Collections.unmodifiableSet(this.byLocation.keySet());
/*     */   }
/*     */   
/*     */   public Set<ResourceKey<T>> registryKeySet() {
/* 239 */     return Collections.unmodifiableSet(this.byKey.keySet());
/*     */   }
/*     */   
/*     */   public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
/* 244 */     return Collections.unmodifiableSet(Util.mapValuesLazy(this.byKey, Holder::value).entrySet());
/*     */   }
/*     */   
/*     */   public Stream<Holder.Reference<T>> listElements() {
/* 249 */     return this.byId.stream();
/*     */   }
/*     */   
/*     */   public Stream<HolderSet.Named<T>> getTags() {
/* 254 */     return this.allTags.getTags();
/*     */   }
/*     */   
/*     */   private HolderSet.Named<T> getOrCreateTagForRegistration(TagKey<T> tag) {
/* 258 */     return this.frozenTags.computeIfAbsent(tag, this::createTag);
/*     */   }
/*     */   
/*     */   private HolderSet.Named<T> createTag(TagKey<T> tag) {
/* 262 */     return new HolderSet.Named<>(this, tag);
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 267 */     return this.byKey.isEmpty();
/*     */   }
/*     */   
/*     */   public Optional<Holder.Reference<T>> getRandom(RandomSource random) {
/* 272 */     return Util.getRandomSafe((List)this.byId, random);
/*     */   }
/*     */   
/*     */   public boolean containsKey(Identifier key) {
/* 277 */     return this.byLocation.containsKey(key);
/*     */   }
/*     */   
/*     */   public boolean containsKey(ResourceKey<T> key) {
/* 282 */     return this.byKey.containsKey(key);
/*     */   }
/*     */   
/*     */   public DataComponentLookup<T> componentLookup() {
/* 287 */     return Objects.<DataComponentLookup<T>>requireNonNull(this.componentLookup, "Registry not frozen yet");
/*     */   }
/*     */   
/*     */   public Registry<T> freeze() {
/* 292 */     if (this.frozen)
/* 293 */       return this; 
/* 295 */     this.frozen = true;
/* 296 */     this.byValue.forEach((value, holder) -> holder.bindValue(value));
/* 298 */     List<Identifier> unboundEntries = this.byKey.entrySet().stream().filter(e -> !((Holder.Reference)e.getValue()).isBound()).map(e -> ((ResourceKey)e.getKey()).identifier()).sorted().toList();
/* 299 */     if (!unboundEntries.isEmpty())
/* 300 */       throw new IllegalStateException("Unbound values in registry " + String.valueOf(key()) + ": " + String.valueOf(unboundEntries)); 
/* 302 */     if (this.unregisteredIntrusiveHolders != null) {
/* 303 */       if (!this.unregisteredIntrusiveHolders.isEmpty())
/* 304 */         throw new IllegalStateException("Some intrusive holders were not registered: " + String.valueOf(this.unregisteredIntrusiveHolders.values())); 
/* 306 */       this.unregisteredIntrusiveHolders = null;
/*     */     } 
/* 309 */     if (this.allTags.isBound())
/* 311 */       throw new IllegalStateException("Tags already present before freezing"); 
/* 313 */     List<Identifier> unboundTags = this.frozenTags.entrySet().stream().filter(e -> !((HolderSet.Named)e.getValue()).isBound()).map(e -> ((TagKey)e.getKey()).location()).sorted().toList();
/* 314 */     if (!unboundTags.isEmpty())
/* 315 */       throw new IllegalStateException("Unbound tags in registry " + String.valueOf(key()) + ": " + String.valueOf(unboundTags)); 
/* 319 */     this.componentLookup = new DataComponentLookup((Iterable)this.byId);
/* 320 */     this.allTags = TagSet.fromMap(this.frozenTags);
/* 321 */     refreshTagsInHolders();
/* 322 */     return this;
/*     */   }
/*     */   
/*     */   public Holder.Reference<T> createIntrusiveHolder(T value) {
/* 327 */     if (this.unregisteredIntrusiveHolders == null)
/* 328 */       throw new IllegalStateException("This registry can't create intrusive holders"); 
/* 330 */     validateWrite();
/* 331 */     return this.unregisteredIntrusiveHolders.computeIfAbsent(value, v -> Holder.Reference.createIntrusive(this, (T)v));
/*     */   }
/*     */   
/*     */   public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 336 */     return this.allTags.get(id);
/*     */   }
/*     */   
/*     */   private Holder.Reference<T> validateAndUnwrapTagElement(TagKey<T> id, Holder<T> value) {
/* 340 */     if (!value.canSerializeIn(this))
/* 341 */       throw new IllegalStateException("Can't create named set " + String.valueOf(id) + " containing value " + String.valueOf(value) + " from outside registry " + String.valueOf(this)); 
/* 346 */     if (value instanceof Holder.Reference) {
/* 346 */       Holder.Reference<T> reference = (Holder.Reference<T>)value;
/* 347 */       return reference;
/*     */     } 
/* 349 */     throw new IllegalStateException("Found direct holder " + String.valueOf(value) + " value in tag " + String.valueOf(id));
/*     */   }
/*     */   
/*     */   public void bindTags(Map<TagKey<T>, List<Holder<T>>> pendingTags) {
/* 355 */     validateWrite();
/* 356 */     pendingTags.forEach((id, values) -> getOrCreateTagForRegistration(id).bind(values));
/*     */   }
/*     */   
/*     */   private void refreshTagsInHolders() {
/* 360 */     Map<Holder.Reference<T>, List<TagKey<T>>> tagsForElement = new IdentityHashMap<>();
/* 361 */     this.byKey.values().forEach(h -> tagsForElement.put(h, new ArrayList()));
/* 362 */     this.allTags.forEach((id, values) -> {
/*     */           for (Holder<T> value : (Iterable<Holder<T>>)values) {
/*     */             Holder.Reference<T> reference = validateAndUnwrapTagElement(id, value);
/*     */             ((List<TagKey<T>>)tagsForElement.get(reference)).add(id);
/*     */           } 
/*     */         });
/* 368 */     tagsForElement.forEach(Holder.Reference::bindTags);
/*     */   }
/*     */   
/*     */   public void bindAllTagsToEmpty() {
/* 373 */     validateWrite();
/* 374 */     this.frozenTags.values().forEach(e -> e.bind(List.of()));
/*     */   }
/*     */   
/*     */   public HolderGetter<T> createRegistrationLookup() {
/* 379 */     validateWrite();
/* 380 */     return new HolderGetter<T>() {
/*     */         public Optional<Holder.Reference<T>> get(ResourceKey<T> id) {
/* 383 */           return Optional.of(getOrThrow(id));
/*     */         }
/*     */         
/*     */         public Holder.Reference<T> getOrThrow(ResourceKey<T> id) {
/* 388 */           return MappedRegistry.this.getOrCreateHolderOrThrow(id);
/*     */         }
/*     */         
/*     */         public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 393 */           return Optional.of(getOrThrow(id));
/*     */         }
/*     */         
/*     */         public HolderSet.Named<T> getOrThrow(TagKey<T> id) {
/* 398 */           return MappedRegistry.this.getOrCreateTagForRegistration(id);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Registry.PendingTags<T> prepareTagReload(TagLoader.LoadResult<T> tags) {
/* 405 */     if (!this.frozen)
/* 406 */       throw new IllegalStateException("Invalid method used for tag loading"); 
/* 414 */     ImmutableMap.Builder<TagKey<T>, HolderSet.Named<T>> pendingTagsBuilder = ImmutableMap.builder();
/* 415 */     final Map<TagKey<T>, List<Holder<T>>> pendingContents = new HashMap<>();
/* 417 */     tags.tags().forEach((id, contents) -> {
/*     */           HolderSet.Named<T> tagToAdd = this.frozenTags.get(id);
/*     */           if (tagToAdd == null)
/*     */             tagToAdd = createTag(id); 
/*     */           pendingTagsBuilder.put(id, tagToAdd);
/*     */           pendingContents.put(id, List.copyOf(contents));
/*     */         });
/* 426 */     final ImmutableMap<TagKey<T>, HolderSet.Named<T>> pendingTags = pendingTagsBuilder.build();
/* 428 */     final HolderLookup.RegistryLookup<T> patchedHolder = new HolderLookup.RegistryLookup.Delegate<T>() {
/*     */         public HolderLookup.RegistryLookup<T> parent() {
/* 431 */           return MappedRegistry.this;
/*     */         }
/*     */         
/*     */         public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 436 */           return Optional.ofNullable((HolderSet.Named<T>)pendingTags.get(id));
/*     */         }
/*     */         
/*     */         public Stream<HolderSet.Named<T>> listTags() {
/* 441 */           return pendingTags.values().stream();
/*     */         }
/*     */       };
/* 445 */     return new Registry.PendingTags<T>() {
/*     */         public ResourceKey<? extends Registry<? extends T>> key() {
/* 448 */           return MappedRegistry.this.key();
/*     */         }
/*     */         
/*     */         public int size() {
/* 453 */           return pendingContents.size();
/*     */         }
/*     */         
/*     */         public HolderLookup.RegistryLookup<T> lookup() {
/* 458 */           return patchedHolder;
/*     */         }
/*     */         
/*     */         public void apply() {
/* 463 */           pendingTags.forEach((id, tag) -> {
/*     */                 List<Holder<T>> values = (List<Holder<T>>)pendingContents.getOrDefault(id, List.of());
/*     */                 tag.bind(values);
/*     */               });
/* 467 */           MappedRegistry.this.allTags = MappedRegistry.TagSet.fromMap((Map<TagKey<?>, HolderSet.Named<?>>)pendingTags);
/* 468 */           MappedRegistry.this.refreshTagsInHolders();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static interface TagSet<T> {
/*     */     static <T> TagSet<T> unbound() {
/* 475 */       return new TagSet<T>() {
/*     */           public boolean isBound() {
/* 478 */             return false;
/*     */           }
/*     */           
/*     */           public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 483 */             throw new IllegalStateException("Tags not bound, trying to access " + String.valueOf(id));
/*     */           }
/*     */           
/*     */           public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/* 488 */             throw new IllegalStateException("Tags not bound");
/*     */           }
/*     */           
/*     */           public Stream<HolderSet.Named<T>> getTags() {
/* 493 */             throw new IllegalStateException("Tags not bound");
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     static <T> TagSet<T> fromMap(final Map<TagKey<T>, HolderSet.Named<T>> tags) {
/* 499 */       return new TagSet<T>() {
/*     */           public boolean isBound() {
/* 502 */             return true;
/*     */           }
/*     */           
/*     */           public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/* 507 */             return Optional.ofNullable((HolderSet.Named<T>)tags.get(id));
/*     */           }
/*     */           
/*     */           public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/* 512 */             tags.forEach(action);
/*     */           }
/*     */           
/*     */           public Stream<HolderSet.Named<T>> getTags() {
/* 517 */             return tags.values().stream();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     boolean isBound();
/*     */     
/*     */     Optional<HolderSet.Named<T>> get(TagKey<T> param1TagKey);
/*     */     
/*     */     void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> param1BiConsumer);
/*     */     
/*     */     Stream<HolderSet.Named<T>> getTags();
/*     */   }
/*     */   
/*     */   class null implements TagSet<T> {
/*     */     public boolean isBound() {
/*     */       return false;
/*     */     }
/*     */     
/*     */     public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/*     */       throw new IllegalStateException("Tags not bound, trying to access " + String.valueOf(id));
/*     */     }
/*     */     
/*     */     public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/*     */       throw new IllegalStateException("Tags not bound");
/*     */     }
/*     */     
/*     */     public Stream<HolderSet.Named<T>> getTags() {
/*     */       throw new IllegalStateException("Tags not bound");
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements TagSet<T> {
/*     */     public boolean isBound() {
/*     */       return true;
/*     */     }
/*     */     
/*     */     public Optional<HolderSet.Named<T>> get(TagKey<T> id) {
/*     */       return Optional.ofNullable((HolderSet.Named<T>)tags.get(id));
/*     */     }
/*     */     
/*     */     public void forEach(BiConsumer<? super TagKey<T>, ? super HolderSet.Named<T>> action) {
/*     */       tags.forEach(action);
/*     */     }
/*     */     
/*     */     public Stream<HolderSet.Named<T>> getTags() {
/* 517 */       return tags.values().stream();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\MappedRegistry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */