/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ public final class Configuration extends Record {
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final GpuSurface.PresentMode presentMode;
/*     */   
/*     */   public Configuration(int width, int height, GpuSurface.PresentMode presentMode) {
/* 102 */     this.width = width;
/* 102 */     this.height = height;
/* 102 */     this.presentMode = presentMode;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #102	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #102	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #102	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/systems/GpuSurface$Configuration;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int width() {
/* 102 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/* 102 */     return this.height;
/*     */   }
/*     */   
/*     */   public GpuSurface.PresentMode presentMode() {
/* 102 */     return this.presentMode;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\GpuSurface$Configuration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */