/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalDouble;
/*    */ import java.util.function.Supplier;
/*    */ import org.joml.Vector4fc;
/*    */ 
/*    */ public class RenderPassDescriptor {
/*    */   private final Supplier<String> label;
/*    */   
/* 16 */   public List<Attachment<Optional<Vector4fc>>> colorAttachments = new ArrayList<>();
/*    */   
/*    */   public Attachment<OptionalDouble> depthAttachment;
/*    */   
/*    */   public RenderPass.RenderArea renderArea;
/*    */   
/*    */   public static RenderPassDescriptor create(Supplier<String> label) {
/* 22 */     return new RenderPassDescriptor(label);
/*    */   }
/*    */   
/*    */   private RenderPassDescriptor(Supplier<String> label) {
/* 26 */     this.label = label;
/*    */   }
/*    */   
/*    */   public RenderPassDescriptor withColorAttachment(GpuTextureView textureView) {
/* 30 */     this.colorAttachments.add(new Attachment<>(textureView, Optional.empty()));
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public RenderPassDescriptor withColorAttachment(GpuTextureView textureView, Optional<Vector4fc> clearValue) {
/* 35 */     this.colorAttachments.add(new Attachment<>(textureView, clearValue));
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public RenderPassDescriptor withUnusedColorAttachment() {
/* 40 */     this.colorAttachments.add(null);
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public RenderPassDescriptor withDepthAttachment(GpuTextureView textureView) {
/* 45 */     this.depthAttachment = new Attachment<>(textureView, OptionalDouble.empty());
/* 46 */     return this;
/*    */   }
/*    */   
/*    */   public RenderPassDescriptor withDepthAttachment(GpuTextureView textureView, OptionalDouble clearValue) {
/* 50 */     this.depthAttachment = new Attachment<>(textureView, clearValue);
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public RenderPassDescriptor withRenderArea(RenderPass.RenderArea renderArea) {
/* 55 */     this.renderArea = renderArea;
/* 56 */     return this;
/*    */   }
/*    */   
/*    */   public Supplier<String> label() {
/* 60 */     return this.label;
/*    */   }
/*    */   
/*    */   public List<Attachment<Optional<Vector4fc>>> colorAttachments() {
/* 64 */     return this.colorAttachments;
/*    */   }
/*    */   
/*    */   public Attachment<OptionalDouble> depthAttachment() {
/* 68 */     return this.depthAttachment;
/*    */   }
/*    */   
/*    */   public static final class Attachment<T> extends Record {
/*    */     private final GpuTextureView textureView;
/*    */     
/*    */     private final T clearValue;
/*    */     
/*    */     public Attachment(GpuTextureView textureView, T clearValue) {
/* 71 */       this.textureView = textureView;
/* 71 */       this.clearValue = clearValue;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment<TT;>;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment<TT;>;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #71	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment<TT;>;
/*    */     }
/*    */     
/*    */     public GpuTextureView textureView() {
/* 71 */       return this.textureView;
/*    */     }
/*    */     
/*    */     public T clearValue() {
/* 71 */       return this.clearValue;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\RenderPassDescriptor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */