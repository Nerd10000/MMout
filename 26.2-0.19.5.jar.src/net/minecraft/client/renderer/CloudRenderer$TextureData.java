/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ public final class TextureData extends Record {
/*     */   private final long[] cells;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   public TextureData(long[] cells, int width, int height) {
/* 354 */     this.cells = cells;
/* 354 */     this.width = width;
/* 354 */     this.height = height;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/CloudRenderer$TextureData;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #354	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/CloudRenderer$TextureData;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/CloudRenderer$TextureData;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #354	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/CloudRenderer$TextureData;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/CloudRenderer$TextureData;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #354	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/CloudRenderer$TextureData;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public long[] cells() {
/* 354 */     return this.cells;
/*     */   }
/*     */   
/*     */   public int width() {
/* 354 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/* 354 */     return this.height;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\CloudRenderer$TextureData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */