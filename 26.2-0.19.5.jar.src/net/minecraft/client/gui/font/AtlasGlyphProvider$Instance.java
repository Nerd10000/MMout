/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import org.joml.Matrix4fc;
/*    */ 
/*    */ final class Instance extends Record implements PlainTextRenderable {
/*    */   private final GlyphRenderTypes renderTypes;
/*    */   
/*    */   private final GpuTextureView textureView;
/*    */   
/*    */   private final TextureAtlasSprite sprite;
/*    */   
/*    */   private final float x;
/*    */   
/*    */   private final float y;
/*    */   
/*    */   private final int color;
/*    */   
/*    */   private final int shadowColor;
/*    */   
/*    */   private final float shadowOffset;
/*    */   
/*    */   private final Style style;
/*    */   
/*    */   private Instance(GlyphRenderTypes renderTypes, GpuTextureView textureView, TextureAtlasSprite sprite, float x, float y, int color, int shadowColor, float shadowOffset, Style style) {
/* 65 */     this.renderTypes = renderTypes;
/* 65 */     this.textureView = textureView;
/* 65 */     this.sprite = sprite;
/* 65 */     this.x = x;
/* 65 */     this.y = y;
/* 65 */     this.color = color;
/* 65 */     this.shadowColor = shadowColor;
/* 65 */     this.shadowOffset = shadowOffset;
/* 65 */     this.style = style;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/AtlasGlyphProvider$Instance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/AtlasGlyphProvider$Instance;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/AtlasGlyphProvider$Instance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/AtlasGlyphProvider$Instance;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/AtlasGlyphProvider$Instance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #65	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/AtlasGlyphProvider$Instance;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GlyphRenderTypes renderTypes() {
/* 65 */     return this.renderTypes;
/*    */   }
/*    */   
/*    */   public TextureAtlasSprite sprite() {
/* 65 */     return this.sprite;
/*    */   }
/*    */   
/*    */   public float x() {
/* 65 */     return this.x;
/*    */   }
/*    */   
/*    */   public float y() {
/* 65 */     return this.y;
/*    */   }
/*    */   
/*    */   public int color() {
/* 65 */     return this.color;
/*    */   }
/*    */   
/*    */   public int shadowColor() {
/* 65 */     return this.shadowColor;
/*    */   }
/*    */   
/*    */   public float shadowOffset() {
/* 65 */     return this.shadowOffset;
/*    */   }
/*    */   
/*    */   public Style style() {
/* 65 */     return this.style;
/*    */   }
/*    */   
/*    */   public void renderSprite(Matrix4fc pose, VertexConsumer buffer, int packedLightCoords, float offsetX, float offsetY, float z, int color) {
/* 68 */     float x0 = offsetX + left();
/* 69 */     float x1 = offsetX + right();
/* 70 */     float y0 = offsetY + top();
/* 71 */     float y1 = offsetY + bottom();
/* 73 */     buffer.addVertex(pose, x0, y0, z).setUv(this.sprite.getU0(), this.sprite.getV0()).setColor(color).setLight(packedLightCoords);
/* 74 */     buffer.addVertex(pose, x0, y1, z).setUv(this.sprite.getU0(), this.sprite.getV1()).setColor(color).setLight(packedLightCoords);
/* 75 */     buffer.addVertex(pose, x1, y1, z).setUv(this.sprite.getU1(), this.sprite.getV1()).setColor(color).setLight(packedLightCoords);
/* 76 */     buffer.addVertex(pose, x1, y0, z).setUv(this.sprite.getU1(), this.sprite.getV0()).setColor(color).setLight(packedLightCoords);
/*    */   }
/*    */   
/*    */   public RenderType renderType(Font.DisplayMode displayMode) {
/* 81 */     return this.renderTypes.select(displayMode);
/*    */   }
/*    */   
/*    */   public RenderPipeline guiPipeline() {
/* 86 */     return this.renderTypes.guiPipeline();
/*    */   }
/*    */   
/*    */   public GpuTextureView textureView() {
/* 91 */     return this.textureView;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\AtlasGlyphProvider$Instance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */