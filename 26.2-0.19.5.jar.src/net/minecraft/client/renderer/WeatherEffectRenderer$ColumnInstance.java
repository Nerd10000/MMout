/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ public final class ColumnInstance extends Record {
/*     */   private final int x;
/*     */   
/*     */   private final int z;
/*     */   
/*     */   private final int bottomY;
/*     */   
/*     */   private final int topY;
/*     */   
/*     */   private final float uOffset;
/*     */   
/*     */   private final float vOffset;
/*     */   
/*     */   private final int lightCoords;
/*     */   
/*     */   public ColumnInstance(int x, int z, int bottomY, int topY, float uOffset, float vOffset, int lightCoords) {
/* 207 */     this.x = x;
/* 207 */     this.z = z;
/* 207 */     this.bottomY = bottomY;
/* 207 */     this.topY = topY;
/* 207 */     this.uOffset = uOffset;
/* 207 */     this.vOffset = vOffset;
/* 207 */     this.lightCoords = lightCoords;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #207	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #207	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #207	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/WeatherEffectRenderer$ColumnInstance;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int x() {
/* 207 */     return this.x;
/*     */   }
/*     */   
/*     */   public int z() {
/* 207 */     return this.z;
/*     */   }
/*     */   
/*     */   public int bottomY() {
/* 207 */     return this.bottomY;
/*     */   }
/*     */   
/*     */   public int topY() {
/* 207 */     return this.topY;
/*     */   }
/*     */   
/*     */   public float uOffset() {
/* 207 */     return this.uOffset;
/*     */   }
/*     */   
/*     */   public float vOffset() {
/* 207 */     return this.vOffset;
/*     */   }
/*     */   
/*     */   public int lightCoords() {
/* 207 */     return this.lightCoords;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\WeatherEffectRenderer$ColumnInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */