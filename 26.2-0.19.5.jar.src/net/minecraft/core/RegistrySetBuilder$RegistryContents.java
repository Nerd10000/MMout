/*     */ package net.minecraft.core;
/*     */ 
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ final class RegistryContents<T> extends Record {
/*     */   private final ResourceKey<? extends Registry<? extends T>> key;
/*     */   
/*     */   private final Lifecycle lifecycle;
/*     */   
/*     */   private final Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> values;
/*     */   
/*     */   private RegistryContents(ResourceKey<? extends Registry<? extends T>> key, Lifecycle lifecycle, Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> values) {
/* 245 */     this.key = key;
/* 245 */     this.lifecycle = lifecycle;
/* 245 */     this.values = values;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #245	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #245	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #245	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$RegistryContents<TT;>;
/*     */   }
/*     */   
/*     */   public ResourceKey<? extends Registry<? extends T>> key() {
/* 245 */     return this.key;
/*     */   }
/*     */   
/*     */   public Lifecycle lifecycle() {
/* 245 */     return this.lifecycle;
/*     */   }
/*     */   
/*     */   public Map<ResourceKey<T>, RegistrySetBuilder.ValueAndHolder<T>> values() {
/* 245 */     return this.values;
/*     */   }
/*     */   
/*     */   public HolderLookup.RegistryLookup<T> buildAsLookup(RegistrySetBuilder.UniversalOwner owner) {
/* 247 */     Map<ResourceKey<T>, Holder.Reference<T>> entries = (Map<ResourceKey<T>, Holder.Reference<T>>)this.values.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> {
/*     */             RegistrySetBuilder.ValueAndHolder<T> entry = (RegistrySetBuilder.ValueAndHolder<T>)e.getValue();
/*     */             Holder.Reference<T> holder = entry.holder().orElseGet(());
/*     */             holder.bindValue(entry.value().value());
/*     */             return holder;
/*     */           }));
/* 257 */     return RegistrySetBuilder.lookupFromMap(this.key, this.lifecycle, owner.cast(), entries);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$RegistryContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */