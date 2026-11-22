/*    */ package com.mojang.blaze3d.resource;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*    */ import com.mojang.blaze3d.pipeline.TextureTarget;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import org.joml.Vector4fc;
/*    */ 
/*    */ public final class RenderTargetDescriptor extends Record implements ResourceDescriptor<RenderTarget> {
/*    */   private final int width;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   private final boolean useDepth;
/*    */   
/*    */   private final Vector4fc clearColor;
/*    */   
/*    */   private final GpuFormat format;
/*    */   
/*    */   public RenderTargetDescriptor(int width, int height, boolean useDepth, Vector4fc clearColor, GpuFormat format) {
/*  9 */     this.width = width;
/*  9 */     this.height = height;
/*  9 */     this.useDepth = useDepth;
/*  9 */     this.clearColor = clearColor;
/*  9 */     this.format = format;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/resource/RenderTargetDescriptor;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/resource/RenderTargetDescriptor;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/resource/RenderTargetDescriptor;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/resource/RenderTargetDescriptor;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/resource/RenderTargetDescriptor;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/resource/RenderTargetDescriptor;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int width() {
/*  9 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/*  9 */     return this.height;
/*    */   }
/*    */   
/*    */   public boolean useDepth() {
/*  9 */     return this.useDepth;
/*    */   }
/*    */   
/*    */   public Vector4fc clearColor() {
/*  9 */     return this.clearColor;
/*    */   }
/*    */   
/*    */   public GpuFormat format() {
/*  9 */     return this.format;
/*    */   }
/*    */   
/*    */   public RenderTarget allocate() {
/* 12 */     return (RenderTarget)new TextureTarget(null, this.width, this.height, this.useDepth, this.format);
/*    */   }
/*    */   
/*    */   public void prepare(RenderTarget resource) {
/* 17 */     if (this.useDepth) {
/* 18 */       RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(resource.getColorTexture(), this.clearColor, resource.getDepthTexture(), 0.0D);
/*    */     } else {
/* 20 */       RenderSystem.getDevice().createCommandEncoder().clearColorTexture(resource.getColorTexture(), this.clearColor);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void free(RenderTarget resource) {
/* 26 */     resource.destroyBuffers();
/*    */   }
/*    */   
/*    */   public boolean canUsePhysicalResource(ResourceDescriptor<?> other) {
/* 32 */     if (other instanceof RenderTargetDescriptor) {
/* 32 */       RenderTargetDescriptor descriptor = (RenderTargetDescriptor)other;
/* 33 */       return (this.width == descriptor.width && this.height == descriptor.height && this.useDepth == descriptor.useDepth && this.format == descriptor.format);
/*    */     } 
/* 35 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\resource\RenderTargetDescriptor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */