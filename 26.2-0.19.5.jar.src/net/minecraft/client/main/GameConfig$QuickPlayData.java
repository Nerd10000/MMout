/*     */ package net.minecraft.client.main;
/*     */ 
/*     */ public final class QuickPlayData extends Record {
/*     */   private final String logPath;
/*     */   
/*     */   private final GameConfig.QuickPlayVariant variant;
/*     */   
/*     */   public QuickPlayData(String logPath, GameConfig.QuickPlayVariant variant) {
/* 117 */     this.logPath = logPath;
/* 117 */     this.variant = variant;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayData;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayData;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayData;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayData;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayData;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #117	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayData;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String logPath() {
/* 117 */     return this.logPath;
/*     */   }
/*     */   
/*     */   public GameConfig.QuickPlayVariant variant() {
/* 117 */     return this.variant;
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 119 */     return this.variant.isEnabled();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\main\GameConfig$QuickPlayData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */