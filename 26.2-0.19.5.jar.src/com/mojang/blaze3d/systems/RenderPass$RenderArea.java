/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ 
/*     */ public final class RenderArea extends Record {
/*     */   private final int x;
/*     */   
/*     */   private final int y;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   public RenderArea(int x, int y, int width, int height) {
/* 319 */     this.x = x;
/* 319 */     this.y = y;
/* 319 */     this.width = width;
/* 319 */     this.height = height;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #319	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #319	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #319	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPass$RenderArea;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int x() {
/* 319 */     return this.x;
/*     */   }
/*     */   
/*     */   public int y() {
/* 319 */     return this.y;
/*     */   }
/*     */   
/*     */   public int width() {
/* 319 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/* 319 */     return this.height;
/*     */   }
/*     */   
/*     */   public boolean fillsTexture(GpuTextureView texture) {
/* 321 */     return (this.x == 0 && this.y == 0 && this.width == texture.getWidth(0) && this.height == texture.getHeight(0));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\RenderPass$RenderArea.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */