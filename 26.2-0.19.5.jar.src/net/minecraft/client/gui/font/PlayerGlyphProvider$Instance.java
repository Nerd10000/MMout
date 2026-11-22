/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ final class Instance extends Record implements PlainTextRenderable {
/*     */   private final Supplier<PlayerSkinRenderCache.RenderInfo> skin;
/*     */   
/*     */   private final boolean hat;
/*     */   
/*     */   private final float x;
/*     */   
/*     */   private final float y;
/*     */   
/*     */   private final int color;
/*     */   
/*     */   private final int shadowColor;
/*     */   
/*     */   private final float shadowOffset;
/*     */   
/*     */   private final Style style;
/*     */   
/*     */   private Instance(Supplier<PlayerSkinRenderCache.RenderInfo> skin, boolean hat, float x, float y, int color, int shadowColor, float shadowOffset, Style style) {
/*  56 */     this.skin = skin;
/*  56 */     this.hat = hat;
/*  56 */     this.x = x;
/*  56 */     this.y = y;
/*  56 */     this.color = color;
/*  56 */     this.shadowColor = shadowColor;
/*  56 */     this.shadowOffset = shadowOffset;
/*  56 */     this.style = style;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #56	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #56	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #56	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/font/PlayerGlyphProvider$Instance;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Supplier<PlayerSkinRenderCache.RenderInfo> skin() {
/*  56 */     return this.skin;
/*     */   }
/*     */   
/*     */   public boolean hat() {
/*  56 */     return this.hat;
/*     */   }
/*     */   
/*     */   public float x() {
/*  56 */     return this.x;
/*     */   }
/*     */   
/*     */   public float y() {
/*  56 */     return this.y;
/*     */   }
/*     */   
/*     */   public int color() {
/*  56 */     return this.color;
/*     */   }
/*     */   
/*     */   public int shadowColor() {
/*  56 */     return this.shadowColor;
/*     */   }
/*     */   
/*     */   public float shadowOffset() {
/*  56 */     return this.shadowOffset;
/*     */   }
/*     */   
/*     */   public Style style() {
/*  56 */     return this.style;
/*     */   }
/*     */   
/*     */   public void renderSprite(Matrix4fc pose, VertexConsumer buffer, int packedLightCoords, float offsetX, float offsetY, float z, int color) {
/*  59 */     float x0 = offsetX + left();
/*  60 */     float x1 = offsetX + right();
/*  61 */     float y0 = offsetY + top();
/*  62 */     float y1 = offsetY + bottom();
/*  64 */     renderQuad(pose, buffer, packedLightCoords, x0, x1, y0, y1, z, color, 8.0F, 8.0F, 8, 8, 64, 64);
/*  77 */     if (this.hat)
/*  78 */       renderQuad(pose, buffer, packedLightCoords, x0, x1, y0, y1, z, color, 40.0F, 8.0F, 8, 8, 64, 64); 
/*     */   }
/*     */   
/*     */   private static void renderQuad(Matrix4fc pose, VertexConsumer buffer, int packedLightCoords, float x0, float x1, float y0, float y1, float z, int color, float u, float v, int srcWidth, int srcHeight, int textureWidth, int textureHeight) {
/*  94 */     float u0 = (u + 0.0F) / textureWidth;
/*  95 */     float u1 = (u + srcWidth) / textureWidth;
/*  96 */     float v0 = (v + 0.0F) / textureHeight;
/*  97 */     float v1 = (v + srcHeight) / textureHeight;
/*  99 */     buffer.addVertex(pose, x0, y0, z).setUv(u0, v0).setColor(color).setLight(packedLightCoords);
/* 100 */     buffer.addVertex(pose, x0, y1, z).setUv(u0, v1).setColor(color).setLight(packedLightCoords);
/* 101 */     buffer.addVertex(pose, x1, y1, z).setUv(u1, v1).setColor(color).setLight(packedLightCoords);
/* 102 */     buffer.addVertex(pose, x1, y0, z).setUv(u1, v0).setColor(color).setLight(packedLightCoords);
/*     */   }
/*     */   
/*     */   public RenderType renderType(Font.DisplayMode displayMode) {
/* 107 */     return ((PlayerSkinRenderCache.RenderInfo)this.skin.get()).glyphRenderTypes().select(displayMode);
/*     */   }
/*     */   
/*     */   public RenderPipeline guiPipeline() {
/* 112 */     return ((PlayerSkinRenderCache.RenderInfo)this.skin.get()).glyphRenderTypes().guiPipeline();
/*     */   }
/*     */   
/*     */   public GpuTextureView textureView() {
/* 117 */     return ((PlayerSkinRenderCache.RenderInfo)this.skin.get()).textureView();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\PlayerGlyphProvider$Instance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */