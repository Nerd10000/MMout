/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.framegraph.FramePass;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class TextureInput extends Record implements PostPass.Input {
/*     */   private final String samplerName;
/*     */   
/*     */   private final AbstractTexture texture;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final boolean bilinear;
/*     */   
/*     */   public TextureInput(String samplerName, AbstractTexture texture, int width, int height, boolean bilinear) {
/* 140 */     this.samplerName = samplerName;
/* 140 */     this.texture = texture;
/* 140 */     this.width = width;
/* 140 */     this.height = height;
/* 140 */     this.bilinear = bilinear;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostPass$TextureInput;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #140	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostPass$TextureInput;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostPass$TextureInput;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #140	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostPass$TextureInput;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostPass$TextureInput;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #140	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/PostPass$TextureInput;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String samplerName() {
/* 140 */     return this.samplerName;
/*     */   }
/*     */   
/*     */   public AbstractTexture texture() {
/* 140 */     return this.texture;
/*     */   }
/*     */   
/*     */   public int width() {
/* 140 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/* 140 */     return this.height;
/*     */   }
/*     */   
/*     */   public void addToPass(FramePass pass, Map<Identifier, ResourceHandle<RenderTarget>> targets) {}
/*     */   
/*     */   public GpuTextureView texture(Map<Identifier, ResourceHandle<RenderTarget>> targets) {
/* 148 */     return this.texture.getTextureView();
/*     */   }
/*     */   
/*     */   public boolean bilinear() {
/* 153 */     return this.bilinear;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostPass$TextureInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */