/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.framegraph.FramePass;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class TargetInput extends Record implements PostPass.Input {
/*     */   private final String samplerName;
/*     */   
/*     */   private final Identifier targetId;
/*     */   
/*     */   private final boolean depthBuffer;
/*     */   
/*     */   private final boolean bilinear;
/*     */   
/*     */   public TargetInput(String samplerName, Identifier targetId, boolean depthBuffer, boolean bilinear) {
/* 157 */     this.samplerName = samplerName;
/* 157 */     this.targetId = targetId;
/* 157 */     this.depthBuffer = depthBuffer;
/* 157 */     this.bilinear = bilinear;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/PostPass$TargetInput;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #157	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostPass$TargetInput;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/PostPass$TargetInput;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #157	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/PostPass$TargetInput;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/PostPass$TargetInput;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #157	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/PostPass$TargetInput;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String samplerName() {
/* 157 */     return this.samplerName;
/*     */   }
/*     */   
/*     */   public Identifier targetId() {
/* 157 */     return this.targetId;
/*     */   }
/*     */   
/*     */   public boolean depthBuffer() {
/* 157 */     return this.depthBuffer;
/*     */   }
/*     */   
/*     */   public boolean bilinear() {
/* 157 */     return this.bilinear;
/*     */   }
/*     */   
/*     */   private ResourceHandle<RenderTarget> getHandle(Map<Identifier, ResourceHandle<RenderTarget>> targets) {
/* 159 */     ResourceHandle<RenderTarget> handle = targets.get(this.targetId);
/* 160 */     if (handle == null)
/* 161 */       throw new IllegalStateException("Missing handle for target " + String.valueOf(this.targetId)); 
/* 163 */     return handle;
/*     */   }
/*     */   
/*     */   public void addToPass(FramePass pass, Map<Identifier, ResourceHandle<RenderTarget>> targets) {
/* 168 */     pass.reads(getHandle(targets));
/*     */   }
/*     */   
/*     */   public GpuTextureView texture(Map<Identifier, ResourceHandle<RenderTarget>> targets) {
/* 173 */     ResourceHandle<RenderTarget> handle = getHandle(targets);
/* 174 */     RenderTarget target = (RenderTarget)handle.get();
/* 175 */     GpuTextureView textureView = this.depthBuffer ? target.getDepthTextureView() : target.getColorTextureView();
/* 176 */     if (textureView == null)
/* 177 */       throw new IllegalStateException("Missing " + (this.depthBuffer ? "depth" : "color") + "texture for target " + String.valueOf(this.targetId)); 
/* 179 */     return textureView;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostPass$TargetInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */