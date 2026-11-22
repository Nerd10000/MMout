/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.io.Reader;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderOwner;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.RegistrationInfo;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.WritableRegistry;
/*     */ import net.minecraft.core.registries.ConcurrentHolderGetter;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ 
/*     */ public abstract class RegistryLoadTask<T> {
/*  31 */   private final Object registryWriteLock = new Object();
/*     */   
/*     */   protected final RegistryDataLoader.RegistryData<T> data;
/*     */   
/*     */   private final WritableRegistry<T> registry;
/*     */   
/*     */   protected final ConcurrentHolderGetter<T> concurrentRegistrationGetter;
/*     */   
/*     */   protected final Map<ResourceKey<?>, Exception> loadingErrors;
/*     */   
/*     */   private volatile boolean elementsRegistered;
/*     */   
/*     */   protected RegistryLoadTask(RegistryDataLoader.RegistryData<T> data, Lifecycle lifecycle, Map<ResourceKey<?>, Exception> loadingErrors) {
/*  41 */     this.data = data;
/*  42 */     this.registry = (WritableRegistry<T>)new MappedRegistry(data.key(), lifecycle);
/*  43 */     this.loadingErrors = loadingErrors;
/*  45 */     this.concurrentRegistrationGetter = new ConcurrentHolderGetter(this.registryWriteLock, this.registry.createRegistrationLookup());
/*     */   }
/*     */   
/*     */   protected ResourceKey<? extends Registry<T>> registryKey() {
/*  49 */     return this.registry.key();
/*     */   }
/*     */   
/*     */   protected Registry<T> readOnlyRegistry() {
/*  56 */     if (!this.elementsRegistered)
/*  57 */       throw new IllegalStateException("Elements not registered"); 
/*  59 */     return (Registry<T>)this.registry;
/*     */   }
/*     */   
/*     */   public RegistryOps.RegistryInfo<?> createRegistryInfo() {
/*  65 */     return new RegistryOps.RegistryInfo((HolderOwner<?>)this.registry, (HolderGetter<?>)this.concurrentRegistrationGetter, this.registry.registryLifecycle());
/*     */   }
/*     */   
/*     */   protected void registerElements(Stream<PendingRegistration<T>> elements) {
/*  69 */     synchronized (this.registryWriteLock) {
/*  70 */       elements.forEach(element -> element.value.ifLeft(()).ifRight(()));
/*  75 */       this.elementsRegistered = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void registerTags(Map<TagKey<T>, List<Holder<T>>> pendingTags) {
/*  80 */     synchronized (this.registryWriteLock) {
/*  81 */       this.registry.bindTags(pendingTags);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean freezeRegistry(Map<ResourceKey<?>, Exception> loadingErrors) {
/*     */     try {
/*  87 */       this.registry.freeze();
/*  88 */       return true;
/*  89 */     } catch (Exception e) {
/*  90 */       loadingErrors.put(this.registry.key(), e);
/*  91 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Optional<Registry<T>> validateRegistry(Map<ResourceKey<?>, Exception> loadingErrors) {
/*  96 */     Map<ResourceKey<?>, Exception> registryErrors = new HashMap<>();
/*  97 */     this.data.validator().validate((Registry<T>)this.registry, registryErrors);
/*  98 */     if (registryErrors.isEmpty())
/*  99 */       return (Optional)Optional.of(this.registry); 
/* 101 */     loadingErrors.putAll(registryErrors);
/* 102 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public abstract CompletableFuture<?> load(RegistryOps.RegistryInfoLookup paramRegistryInfoLookup, Executor paramExecutor);
/*     */   
/*     */   protected static final class PendingRegistration<T> extends Record {
/*     */     private final ResourceKey<T> key;
/*     */     
/*     */     private final Either<T, Exception> value;
/*     */     
/*     */     private final RegistrationInfo registrationInfo;
/*     */     
/*     */     protected PendingRegistration(ResourceKey<T> key, Either<T, Exception> value, RegistrationInfo registrationInfo) {
/* 105 */       this.key = key;
/* 105 */       this.value = value;
/* 105 */       this.registrationInfo = registrationInfo;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/resources/RegistryLoadTask$PendingRegistration;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryLoadTask$PendingRegistration;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryLoadTask$PendingRegistration<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/resources/RegistryLoadTask$PendingRegistration;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryLoadTask$PendingRegistration;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/resources/RegistryLoadTask$PendingRegistration<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/resources/RegistryLoadTask$PendingRegistration;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/resources/RegistryLoadTask$PendingRegistration;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/resources/RegistryLoadTask$PendingRegistration<TT;>;
/*     */     }
/*     */     
/*     */     public ResourceKey<T> key() {
/* 105 */       return this.key;
/*     */     }
/*     */     
/*     */     public Either<T, Exception> value() {
/* 105 */       return this.value;
/*     */     }
/*     */     
/*     */     public RegistrationInfo registrationInfo() {
/* 105 */       return this.registrationInfo;
/*     */     }
/*     */     
/*     */     public static <T> Either<T, Exception> loadFromResource(Decoder<T> elementDecoder, RegistryOps<JsonElement> ops, ResourceKey<T> elementKey, Resource thunk) {
/*     */       try {
/* 111 */         Reader reader = thunk.openAsReader();
/*     */         try {
/* 112 */           JsonElement json = StrictJsonParser.parse(reader);
/* 113 */           Either<T, Exception> either = Either.left(elementDecoder.parse(ops, json).getOrThrow());
/* 114 */           if (reader != null)
/* 114 */             reader.close(); 
/*     */           return either;
/*     */         } catch (Throwable throwable) {
/*     */           if (reader != null)
/*     */             try {
/*     */               reader.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 114 */       } catch (Exception e) {
/* 115 */         return Either.right(new IllegalStateException(String.format(Locale.ROOT, "Failed to parse %s from pack %s", new Object[] { elementKey.identifier(), thunk.sourcePackId() }), e));
/*     */       } 
/*     */     }
/*     */     
/*     */     public static <T> Either<T, Exception> findAndLoadFromResource(Decoder<T> elementDecoder, RegistryOps<JsonElement> ops, ResourceKey<T> elementKey, FileToIdConverter converter, ResourceProvider resourceProvider) {
/* 120 */       Identifier resourceId = converter.idToFile(elementKey.identifier());
/* 121 */       return resourceProvider.getResource(resourceId)
/* 122 */         .map(resource -> loadFromResource(elementDecoder, ops, elementKey, resource))
/* 123 */         .orElseGet(() -> Either.right(new IllegalStateException(String.format(Locale.ROOT, "Failed to find resource %s for element %s", new Object[] { resourceId, elementKey.identifier() }))));
/*     */     }
/*     */     
/*     */     public static <T> Either<T, Exception> loadFromNetwork(Decoder<T> elementDecoder, RegistryOps<Tag> ops, ResourceKey<T> elementKey, Tag contents) {
/*     */       try {
/* 128 */         DataResult<T> parseResult = elementDecoder.parse(ops, contents);
/* 129 */         return Either.left(parseResult.getOrThrow());
/* 130 */       } catch (Exception e) {
/* 131 */         return Either.right(new IllegalStateException(String.format(Locale.ROOT, "Failed to parse value %s for key %s from server", new Object[] { contents, elementKey.identifier() }), e));
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryLoadTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */