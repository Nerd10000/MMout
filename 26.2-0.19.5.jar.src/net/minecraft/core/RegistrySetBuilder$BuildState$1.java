/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ class null implements BootstrapContext<T> {
/*     */   public Holder.Reference<T> register(ResourceKey<T> key, T value, Lifecycle lifecycle) {
/* 156 */     RegistrySetBuilder.RegisteredValue<?> previousValue = RegistrySetBuilder.BuildState.this.registeredValues.put(key, new RegistrySetBuilder.RegisteredValue(value, lifecycle));
/* 157 */     if (previousValue != null)
/* 158 */       RegistrySetBuilder.BuildState.this.errors.add(new IllegalStateException("Duplicate registration for " + String.valueOf(key) + ", new=" + String.valueOf(value) + ", old=" + String.valueOf(previousValue.value))); 
/* 160 */     return RegistrySetBuilder.BuildState.this.lookup.getOrCreate(key);
/*     */   }
/*     */   
/*     */   public <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> key) {
/* 166 */     return (HolderGetter<S>)RegistrySetBuilder.BuildState.this.registries.getOrDefault(key.identifier(), RegistrySetBuilder.BuildState.this.lookup);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$BuildState$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */