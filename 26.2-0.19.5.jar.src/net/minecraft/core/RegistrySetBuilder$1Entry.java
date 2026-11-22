/*     */ package net.minecraft.core;
/*     */ 
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ 
/*     */ final class Entry<T> extends Record {
/*     */   private final HolderLookup.RegistryLookup<T> lookup;
/*     */   
/*     */   private final RegistryOps.RegistryInfo<T> opsInfo;
/*     */   
/*     */   Entry(HolderLookup.RegistryLookup<T> lookup, RegistryOps.RegistryInfo<T> opsInfo) {
/* 279 */     this.lookup = lookup;
/* 279 */     this.opsInfo = opsInfo;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrySetBuilder$1Entry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #279	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$1Entry;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$1Entry<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrySetBuilder$1Entry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #279	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$1Entry;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/RegistrySetBuilder$1Entry<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrySetBuilder$1Entry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #279	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$1Entry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/core/RegistrySetBuilder$1Entry<TT;>;
/*     */   }
/*     */   
/*     */   public HolderLookup.RegistryLookup<T> lookup() {
/* 279 */     return this.lookup;
/*     */   }
/*     */   
/*     */   public RegistryOps.RegistryInfo<T> opsInfo() {
/* 279 */     return this.opsInfo;
/*     */   }
/*     */   
/*     */   public static <T> Entry<T> createForContextRegistry(HolderLookup.RegistryLookup<T> registryLookup) {
/* 283 */     return new Entry<>(new RegistrySetBuilder.EmptyTagLookupWrapper<>(registryLookup, registryLookup), RegistryOps.RegistryInfo.fromRegistryLookup(registryLookup));
/*     */   }
/*     */   
/*     */   public static <T> Entry<T> createForNewRegistry(RegistrySetBuilder.UniversalOwner owner, HolderLookup.RegistryLookup<T> registryLookup) {
/* 287 */     return new Entry<>(new RegistrySetBuilder.EmptyTagLookupWrapper<>(owner.cast(), registryLookup), new RegistryOps.RegistryInfo(owner.cast(), registryLookup, registryLookup.registryLifecycle()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$1Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */