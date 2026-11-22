/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ final class ValueWrapper<T> extends Record {
/*     */   private final T value;
/*     */   
/*     */   private final long expiresAfterTime;
/*     */   
/*     */   private static final long NO_EXPIRY = -1L;
/*     */   
/*     */   private ValueWrapper(T value, long expiresAfterTime) {
/* 297 */     this.value = value;
/* 297 */     this.expiresAfterTime = expiresAfterTime;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #297	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #297	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #297	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientDebugSubscriber$ValueWrapper<TT;>;
/*     */   }
/*     */   
/*     */   public T value() {
/* 297 */     return this.value;
/*     */   }
/*     */   
/*     */   public long expiresAfterTime() {
/* 297 */     return this.expiresAfterTime;
/*     */   }
/*     */   
/*     */   public boolean hasExpired(long gameTime) {
/* 304 */     if (this.expiresAfterTime == -1L)
/* 305 */       return false; 
/* 307 */     return (gameTime >= this.expiresAfterTime);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientDebugSubscriber$ValueWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */