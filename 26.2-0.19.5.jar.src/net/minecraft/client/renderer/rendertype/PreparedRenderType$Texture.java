/*    */ package net.minecraft.client.renderer.rendertype;
/*    */ 
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ 
/*    */ public final class Texture extends Record {
/*    */   private final String name;
/*    */   
/*    */   private final GpuTextureView textureView;
/*    */   
/*    */   private final GpuSampler sampler;
/*    */   
/*    */   public Texture(String name, GpuTextureView textureView, GpuSampler sampler) {
/* 53 */     this.name = name;
/* 53 */     this.textureView = textureView;
/* 53 */     this.sampler = sampler;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #53	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/rendertype/PreparedRenderType$Texture;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String name() {
/* 53 */     return this.name;
/*    */   }
/*    */   
/*    */   public GpuTextureView textureView() {
/* 53 */     return this.textureView;
/*    */   }
/*    */   
/*    */   public GpuSampler sampler() {
/* 53 */     return this.sampler;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\PreparedRenderType$Texture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */