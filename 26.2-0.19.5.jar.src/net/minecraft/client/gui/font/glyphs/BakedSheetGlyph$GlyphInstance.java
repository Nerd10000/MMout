/*     */ package net.minecraft.client.gui.font.glyphs;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ final class GlyphInstance extends Record implements TextRenderable.Styled {
/*     */   private final float x;
/*     */   
/*     */   private final float y;
/*     */   
/*     */   private final int color;
/*     */   
/*     */   private final int shadowColor;
/*     */   
/*     */   private final BakedSheetGlyph glyph;
/*     */   
/*     */   private final Style style;
/*     */   
/*     */   private final float boldOffset;
/*     */   
/*     */   private final float shadowOffset;
/*     */   
/*     */   private GlyphInstance(float x, float y, int color, int shadowColor, BakedSheetGlyph glyph, Style style, float boldOffset, float shadowOffset) {
/* 157 */     this.x = x;
/* 157 */     this.y = y;
/* 157 */     this.color = color;
/* 157 */     this.shadowColor = shadowColor;
/* 157 */     this.glyph = glyph;
/* 157 */     this.style = style;
/* 157 */     this.boldOffset = boldOffset;
/* 157 */     this.shadowOffset = shadowOffset;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #157	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #157	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #157	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/glyphs/BakedSheetGlyph$GlyphInstance;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float x() {
/* 157 */     return this.x;
/*     */   }
/*     */   
/*     */   public float y() {
/* 157 */     return this.y;
/*     */   }
/*     */   
/*     */   public int color() {
/* 157 */     return this.color;
/*     */   }
/*     */   
/*     */   public int shadowColor() {
/* 157 */     return this.shadowColor;
/*     */   }
/*     */   
/*     */   public BakedSheetGlyph glyph() {
/* 157 */     return this.glyph;
/*     */   }
/*     */   
/*     */   public Style style() {
/* 157 */     return this.style;
/*     */   }
/*     */   
/*     */   public float boldOffset() {
/* 157 */     return this.boldOffset;
/*     */   }
/*     */   
/*     */   public float shadowOffset() {
/* 157 */     return this.shadowOffset;
/*     */   }
/*     */   
/*     */   public float left() {
/* 160 */     return this.glyph.left(this);
/*     */   }
/*     */   
/*     */   public float top() {
/* 165 */     return this.glyph.top(this);
/*     */   }
/*     */   
/*     */   public float right() {
/* 170 */     return this.glyph.right(this);
/*     */   }
/*     */   
/*     */   public float activeRight() {
/* 175 */     return this.x + this.glyph.info.getAdvance(this.style.isBold());
/*     */   }
/*     */   
/*     */   public float bottom() {
/* 180 */     return this.glyph.bottom(this);
/*     */   }
/*     */   
/*     */   private boolean hasShadow() {
/* 184 */     return (shadowColor() != 0);
/*     */   }
/*     */   
/*     */   public void render(Matrix4fc pose, VertexConsumer buffer, int packedLightCoords, boolean flat) {
/* 189 */     this.glyph.renderChar(this, pose, buffer, packedLightCoords, flat);
/*     */   }
/*     */   
/*     */   public RenderType renderType(Font.DisplayMode displayMode) {
/* 194 */     return this.glyph.renderTypes.select(displayMode);
/*     */   }
/*     */   
/*     */   public GpuTextureView textureView() {
/* 199 */     return this.glyph.textureView;
/*     */   }
/*     */   
/*     */   public RenderPipeline guiPipeline() {
/* 204 */     return this.glyph.renderTypes.guiPipeline();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\glyphs\BakedSheetGlyph$GlyphInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */