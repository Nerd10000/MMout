/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ final class RegistryStub<T> extends Record {
/*     */   private final ResourceKey<? extends Registry<T>> key;
/*     */   
/*     */   private final Lifecycle lifecycle;
/*     */   
/*     */   private final RegistrySetBuilder.RegistryBootstrap<T> bootstrap;
/*     */   
/*     */   private RegistryStub(ResourceKey<? extends Registry<T>> key, Lifecycle lifecycle, RegistrySetBuilder.RegistryBootstrap<T> bootstrap) {
/* 196 */     this.key = key;
/* 196 */     this.lifecycle = lifecycle;
/* 196 */     this.bootstrap = bootstrap;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #196	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #196	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #196	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryStub<TT;>;
/*     */   }
/*     */   
/*     */   public ResourceKey<? extends Registry<T>> key() {
/* 196 */     return this.key;
/*     */   }
/*     */   
/*     */   public Lifecycle lifecycle() {
/* 196 */     return this.lifecycle;
/*     */   }
/*     */   
/*     */   public RegistrySetBuilder.RegistryBootstrap<T> bootstrap() {
/* 196 */     return this.bootstrap;
/*     */   }
/*     */   
/*     */   private void apply(RegistrySetBuilder.BuildState state) {
/* 198 */     this.bootstrap.run(state.bootstrapContext());
/*     */   }
/*     */   
/*     */   public RegistrySetBuilder.RegistryContents<T> collectRegisteredValues(RegistrySetBuilder.BuildState state) {
/* 202 */     Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> result = new HashMap<>();
/* 204 */     Iterator<Map.Entry<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>>> iterator = state.registeredValues.entrySet().iterator();
/* 205 */     while (iterator.hasNext()) {
/* 206 */       Map.Entry<ResourceKey<?>, RegistrySetBuilder.RegisteredValue<?>> entry = iterator.next();
/* 207 */       ResourceKey<?> key = entry.getKey();
/* 208 */       if (key.isFor(this.key)) {
/* 209 */         ResourceKey<T> castKey = (ResourceKey)key;
/* 210 */         RegistrySetBuilder.RegisteredValue<T> value = (RegistrySetBuilder.RegisteredValue<T>)entry.getValue();
/* 211 */         Holder.Reference<T> holder = (Holder.Reference<T>)state.lookup.holders.remove(key);
/* 212 */         result.put(castKey, new RegistrySetBuilder.ValueAndHolder<>(value, Optional.ofNullable(holder)));
/* 214 */         iterator.remove();
/*     */       } 
/*     */     } 
/* 217 */     return new RegistrySetBuilder.RegistryContents<>(this.key, this.lifecycle, result);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$RegistryStub.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */