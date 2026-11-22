/*     */ package net.minecraft.tags;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ public final class LoadResult<T> extends Record {
/*     */   private final ResourceKey<? extends Registry<T>> key;
/*     */   
/*     */   private final Map<TagKey<T>, List<Holder<T>>> tags;
/*     */   
/*     */   public LoadResult(ResourceKey<? extends Registry<T>> key, Map<TagKey<T>, List<Holder<T>>> tags) {
/* 186 */     this.key = key;
/* 186 */     this.tags = tags;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/tags/TagLoader$LoadResult;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #186	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagLoader$LoadResult;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #186	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/tags/TagLoader$LoadResult<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagLoader$LoadResult;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #186	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/tags/TagLoader$LoadResult;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/tags/TagLoader$LoadResult<TT;>;
/*     */   }
/*     */   
/*     */   public ResourceKey<? extends Registry<T>> key() {
/* 186 */     return this.key;
/*     */   }
/*     */   
/*     */   public Map<TagKey<T>, List<Holder<T>>> tags() {
/* 186 */     return this.tags;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\tags\TagLoader$LoadResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */