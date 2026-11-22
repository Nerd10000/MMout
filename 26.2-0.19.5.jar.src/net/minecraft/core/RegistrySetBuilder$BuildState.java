/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ final class BuildState extends Record {
/*     */   private final RegistrySetBuilder.UniversalOwner owner;
/*     */   
/*     */   private final RegistrySetBuilder.UniversalLookup lookup;
/*     */   
/*     */   private final Map<Identifier, HolderGetter<?>> registries;
/*     */   
/*     */   private final Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues;
/*     */   
/*     */   private final List<RuntimeException> errors;
/*     */   
/*     */   private BuildState(RegistrySetBuilder.UniversalOwner owner, RegistrySetBuilder.UniversalLookup lookup, Map<Identifier, HolderGetter<?>> registries, Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues, List<RuntimeException> errors) {
/* 127 */     this.owner = owner;
/* 127 */     this.lookup = lookup;
/* 127 */     this.registries = registries;
/* 127 */     this.registeredValues = registeredValues;
/* 127 */     this.errors = errors;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$BuildState;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$BuildState;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$BuildState;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$BuildState;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$BuildState;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #127	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$BuildState;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public RegistrySetBuilder.UniversalOwner owner() {
/* 127 */     return this.owner;
/*     */   }
/*     */   
/*     */   public RegistrySetBuilder.UniversalLookup lookup() {
/* 127 */     return this.lookup;
/*     */   }
/*     */   
/*     */   public Map<Identifier, HolderGetter<?>> registries() {
/* 127 */     return this.registries;
/*     */   }
/*     */   
/*     */   public Map<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> registeredValues() {
/* 127 */     return this.registeredValues;
/*     */   }
/*     */   
/*     */   public List<RuntimeException> errors() {
/* 127 */     return this.errors;
/*     */   }
/*     */   
/*     */   public static BuildState create(RegistryAccess context, Stream<ResourceKey<? extends Registry<?>>> newRegistries) {
/* 135 */     RegistrySetBuilder.UniversalOwner owner = new RegistrySetBuilder.UniversalOwner();
/* 136 */     List<RuntimeException> errors = new ArrayList<>();
/* 137 */     RegistrySetBuilder.UniversalLookup lookup = new RegistrySetBuilder.UniversalLookup(owner);
/* 139 */     ImmutableMap.Builder<Identifier, HolderGetter<?>> registries = ImmutableMap.builder();
/* 140 */     context.registries().forEach(contextRegistry -> registries.put(contextRegistry.key().identifier(), RegistrySetBuilder.wrapContextLookup(contextRegistry.value())));
/* 141 */     newRegistries.forEach(newRegistry -> registries.put(newRegistry.identifier(), lookup));
/* 143 */     return new BuildState(owner, lookup, (Map<Identifier, HolderGetter<?>>)registries
/*     */         
/* 146 */         .build(), new HashMap<>(), errors);
/*     */   }
/*     */   
/*     */   public <T> BootstrapContext<T> bootstrapContext() {
/* 153 */     return new BootstrapContext<T>() {
/*     */         public Holder.Reference<T> register(ResourceKey<T> key, T value, Lifecycle lifecycle) {
/* 156 */           RegistrySetBuilder.RegisteredValue<?> previousValue = RegistrySetBuilder.BuildState.this.registeredValues.put(key, new RegistrySetBuilder.RegisteredValue(value, lifecycle));
/* 157 */           if (previousValue != null)
/* 158 */             RegistrySetBuilder.BuildState.this.errors.add(new IllegalStateException("Duplicate registration for " + String.valueOf(key) + ", new=" + String.valueOf(value) + ", old=" + String.valueOf(previousValue.value))); 
/* 160 */           return RegistrySetBuilder.BuildState.this.lookup.getOrCreate(key);
/*     */         }
/*     */         
/*     */         public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> key) {
/* 166 */           return (HolderGetter<S>)RegistrySetBuilder.BuildState.this.registries.getOrDefault(key.identifier(), RegistrySetBuilder.BuildState.this.lookup);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public void reportUnclaimedRegisteredValues() {
/* 172 */     this.registeredValues.forEach((key, registeredValue) -> this.errors.add(new IllegalStateException("Orpaned value " + String.valueOf(registeredValue.value) + " for key " + String.valueOf(key))));
/*     */   }
/*     */   
/*     */   public void reportNotCollectedHolders() {
/* 178 */     for (ResourceKey<Object> key : this.lookup.holders.keySet())
/* 179 */       this.errors.add(new IllegalStateException("Unreferenced key: " + String.valueOf(key))); 
/*     */   }
/*     */   
/*     */   public void throwOnError() {
/* 184 */     if (!this.errors.isEmpty()) {
/* 185 */       IllegalStateException result = new IllegalStateException("Errors during registry creation");
/* 186 */       for (RuntimeException error : this.errors)
/* 187 */         result.addSuppressed(error); 
/* 189 */       throw result;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$BuildState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */