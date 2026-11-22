/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ final class InitializerEntry<T> extends Record {
/*     */   private final ResourceKey<T> key;
/*     */   
/*     */   private final DataComponentInitializers.Initializer<T> initializer;
/*     */   
/*     */   private InitializerEntry(ResourceKey<T> key, DataComponentInitializers.Initializer<T> initializer) {
/*  95 */     this.key = key;
/*  95 */     this.initializer = initializer;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #95	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #95	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #95	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/core/component/DataComponentInitializers$InitializerEntry<TT;>;
/*     */   }
/*     */   
/*     */   public ResourceKey<T> key() {
/*  95 */     return this.key;
/*     */   }
/*     */   
/*     */   public DataComponentInitializers.Initializer<T> initializer() {
/*  95 */     return this.initializer;
/*     */   }
/*     */   
/*     */   public void run(DataComponentMap.Builder components, HolderLookup.Provider context) {
/* 100 */     this.initializer.run(components, context, this.key);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentInitializers$InitializerEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */