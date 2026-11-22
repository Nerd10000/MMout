/*     */ package net.minecraft.client.gui.render;
/*     */ 
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ 
/*     */ public final class SlotView extends Record {
/*     */   private final GpuTextureView textureView;
/*     */   
/*     */   private final float u0;
/*     */   
/*     */   private final float v0;
/*     */   
/*     */   private final float u1;
/*     */   
/*     */   private final float v1;
/*     */   
/*     */   public SlotView(GpuTextureView textureView, float u0, float v0, float u1, float v1) {
/* 154 */     this.textureView = textureView;
/* 154 */     this.u0 = u0;
/* 154 */     this.v0 = v0;
/* 154 */     this.u1 = u1;
/* 154 */     this.v1 = v1;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #154	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/render/GuiItemAtlas$SlotView;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public GpuTextureView textureView() {
/* 154 */     return this.textureView;
/*     */   }
/*     */   
/*     */   public float u0() {
/* 154 */     return this.u0;
/*     */   }
/*     */   
/*     */   public float v0() {
/* 154 */     return this.v0;
/*     */   }
/*     */   
/*     */   public float u1() {
/* 154 */     return this.u1;
/*     */   }
/*     */   
/*     */   public float v1() {
/* 154 */     return this.v1;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\GuiItemAtlas$SlotView.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */