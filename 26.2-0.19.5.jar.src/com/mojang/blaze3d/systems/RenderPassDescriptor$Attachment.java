/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ 
/*    */ public final class Attachment<T> extends Record {
/*    */   private final GpuTextureView textureView;
/*    */   
/*    */   private final T clearValue;
/*    */   
/*    */   public Attachment(GpuTextureView textureView, T clearValue) {
/* 71 */     this.textureView = textureView;
/* 71 */     this.clearValue = clearValue;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #71	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #71	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #71	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderPassDescriptor$Attachment<TT;>;
/*    */   }
/*    */   
/*    */   public GpuTextureView textureView() {
/* 71 */     return this.textureView;
/*    */   }
/*    */   
/*    */   public T clearValue() {
/* 71 */     return this.clearValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\RenderPassDescriptor$Attachment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */