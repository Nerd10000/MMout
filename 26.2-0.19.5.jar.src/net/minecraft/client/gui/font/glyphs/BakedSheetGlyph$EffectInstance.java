/*     */ package net.minecraft.client.gui.font.glyphs;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ final class EffectInstance extends Record implements TextRenderable {
/*     */   private final BakedSheetGlyph glyph;
/*     */   
/*     */   private final float x0;
/*     */   
/*     */   private final float y0;
/*     */   
/*     */   private final float x1;
/*     */   
/*     */   private final float y1;
/*     */   
/*     */   private final float depth;
/*     */   
/*     */   private final int color;
/*     */   
/*     */   private final int shadowColor;
/*     */   
/*     */   private final float shadowOffset;
/*     */   
/*     */   private EffectInstance(BakedSheetGlyph glyph, float x0, float y0, float x1, float y1, float depth, int color, int shadowColor, float shadowOffset) {
/* 208 */     this.glyph = glyph;
/* 208 */     this.x0 = x0;
/* 208 */     this.y0 = y0;
/* 208 */     this.x1 = x1;
/* 208 */     this.y1 = y1;
/* 208 */     this.depth = depth;
/* 208 */     this.color = color;
/* 208 */     this.shadowColor = shadowColor;
/* 208 */     this.shadowOffset = shadowOffset;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #208	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #208	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #208	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$EffectInstance;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public BakedSheetGlyph glyph() {
/* 208 */     return this.glyph;
/*     */   }
/*     */   
/*     */   public float x0() {
/* 208 */     return this.x0;
/*     */   }
/*     */   
/*     */   public float y0() {
/* 208 */     return this.y0;
/*     */   }
/*     */   
/*     */   public float x1() {
/* 208 */     return this.x1;
/*     */   }
/*     */   
/*     */   public float y1() {
/* 208 */     return this.y1;
/*     */   }
/*     */   
/*     */   public float depth() {
/* 208 */     return this.depth;
/*     */   }
/*     */   
/*     */   public int color() {
/* 208 */     return this.color;
/*     */   }
/*     */   
/*     */   public int shadowColor() {
/* 208 */     return this.shadowColor;
/*     */   }
/*     */   
/*     */   public float shadowOffset() {
/* 208 */     return this.shadowOffset;
/*     */   }
/*     */   
/*     */   public float left() {
/* 211 */     return this.x0;
/*     */   }
/*     */   
/*     */   public float top() {
/* 216 */     return this.y0;
/*     */   }
/*     */   
/*     */   public float right() {
/* 221 */     return this.x1 + (hasShadow() ? this.shadowOffset : 0.0F);
/*     */   }
/*     */   
/*     */   public float bottom() {
/* 226 */     return this.y1 + (hasShadow() ? this.shadowOffset : 0.0F);
/*     */   }
/*     */   
/*     */   private boolean hasShadow() {
/* 230 */     return (shadowColor() != 0);
/*     */   }
/*     */   
/*     */   public void render(Matrix4fc pose, VertexConsumer buffer, int packedLightCoords, boolean flat) {
/* 235 */     this.glyph.renderEffect(this, pose, buffer, packedLightCoords, false);
/*     */   }
/*     */   
/*     */   public RenderType renderType(Font.DisplayMode displayMode) {
/* 240 */     return this.glyph.renderTypes.select(displayMode);
/*     */   }
/*     */   
/*     */   public GpuTextureView textureView() {
/* 245 */     return this.glyph.textureView;
/*     */   }
/*     */   
/*     */   public RenderPipeline guiPipeline() {
/* 250 */     return this.glyph.renderTypes.guiPipeline();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\glyphs\BakedSheetGlyph$EffectInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */