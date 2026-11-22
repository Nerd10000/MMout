/*     */ package net.minecraft.client.renderer.rendertype;
/*     */ 
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class TextureBinding extends Record {
/*     */   private final Identifier location;
/*     */   
/*     */   private final Supplier<GpuSampler> sampler;
/*     */   
/*     */   public Supplier<GpuSampler> sampler() {
/* 177 */     return this.sampler;
/*     */   }
/*     */   
/*     */   public Identifier location() {
/* 177 */     return this.location;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #177	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #177	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #177	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/rendertype/RenderSetup$TextureBinding;
/*     */   }
/*     */   
/*     */   TextureBinding(Identifier location, Supplier<GpuSampler> sampler) {
/* 178 */     this.location = location;
/* 178 */     this.sampler = sampler;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\rendertype\RenderSetup$TextureBinding.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */