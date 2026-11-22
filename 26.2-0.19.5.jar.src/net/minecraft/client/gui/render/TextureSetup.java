/*    */ package net.minecraft.client.gui.render;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public final class TextureSetup extends Record {
/*    */   private final GpuTextureView texure0;
/*    */   
/*    */   private final GpuTextureView texure1;
/*    */   
/*    */   private final GpuTextureView texure2;
/*    */   
/*    */   private final GpuSampler sampler0;
/*    */   
/*    */   private final GpuSampler sampler1;
/*    */   
/*    */   private final GpuSampler sampler2;
/*    */   
/*    */   public TextureSetup(GpuTextureView texure0, GpuTextureView texure1, GpuTextureView texure2, GpuSampler sampler0, GpuSampler sampler1, GpuSampler sampler2) {
/* 11 */     this.texure0 = texure0;
/* 11 */     this.texure1 = texure1;
/* 11 */     this.texure2 = texure2;
/* 11 */     this.sampler0 = sampler0;
/* 11 */     this.sampler1 = sampler1;
/* 11 */     this.sampler2 = sampler2;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/TextureSetup;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/TextureSetup;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/TextureSetup;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/TextureSetup;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/TextureSetup;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/render/TextureSetup;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GpuTextureView texure0() {
/* 11 */     return this.texure0;
/*    */   }
/*    */   
/*    */   public GpuTextureView texure1() {
/* 11 */     return this.texure1;
/*    */   }
/*    */   
/*    */   public GpuTextureView texure2() {
/* 11 */     return this.texure2;
/*    */   }
/*    */   
/*    */   public GpuSampler sampler0() {
/* 11 */     return this.sampler0;
/*    */   }
/*    */   
/*    */   public GpuSampler sampler1() {
/* 11 */     return this.sampler1;
/*    */   }
/*    */   
/*    */   public GpuSampler sampler2() {
/* 11 */     return this.sampler2;
/*    */   }
/*    */   
/* 12 */   private static final TextureSetup NO_TEXTURE_SETUP = new TextureSetup(null, null, null, null, null, null);
/*    */   
/*    */   private static int sortKeySeed;
/*    */   
/*    */   public static TextureSetup singleTexture(GpuTextureView texture, GpuSampler sampler) {
/* 16 */     return new TextureSetup(texture, null, null, sampler, null, null);
/*    */   }
/*    */   
/*    */   public static TextureSetup singleTextureWithLightmap(GpuTextureView texture, GpuSampler sampler) {
/* 20 */     return new TextureSetup(texture, null, (Minecraft.getInstance()).gameRenderer.lightmap(), sampler, null, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
/*    */   }
/*    */   
/*    */   public static TextureSetup doubleTexture(GpuTextureView texture0, GpuSampler sampler0, GpuTextureView texture1, GpuSampler sampler1) {
/* 24 */     return new TextureSetup(texture0, texture1, null, sampler0, sampler1, null);
/*    */   }
/*    */   
/*    */   public static TextureSetup noTexture() {
/* 28 */     return NO_TEXTURE_SETUP;
/*    */   }
/*    */   
/*    */   public int getSortKey() {
/* 32 */     return SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER ? (hashCode() * (sortKeySeed + 1)) : hashCode();
/*    */   }
/*    */   
/*    */   public static void updateSortKeySeed() {
/* 36 */     sortKeySeed = Math.round(100000.0F * (float)Math.random());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\TextureSetup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */