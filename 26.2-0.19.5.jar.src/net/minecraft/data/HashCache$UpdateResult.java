/*     */ package net.minecraft.data;
/*     */ 
/*     */ public final class UpdateResult extends Record {
/*     */   private final String providerId;
/*     */   
/*     */   private final HashCache.ProviderCache cache;
/*     */   
/*     */   private final int writes;
/*     */   
/*     */   public UpdateResult(String providerId, HashCache.ProviderCache cache, int writes) {
/* 133 */     this.providerId = providerId;
/* 133 */     this.cache = cache;
/* 133 */     this.writes = writes;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/HashCache$UpdateResult;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #133	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/HashCache$UpdateResult;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/HashCache$UpdateResult;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #133	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/HashCache$UpdateResult;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/HashCache$UpdateResult;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #133	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/HashCache$UpdateResult;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String providerId() {
/* 133 */     return this.providerId;
/*     */   }
/*     */   
/*     */   public HashCache.ProviderCache cache() {
/* 133 */     return this.cache;
/*     */   }
/*     */   
/*     */   public int writes() {
/* 133 */     return this.writes;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\HashCache$UpdateResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */