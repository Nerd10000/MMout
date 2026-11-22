/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public final class GlyphRenderTypes extends Record {
/*    */   private final RenderType normal;
/*    */   
/*    */   private final RenderType seeThrough;
/*    */   
/*    */   private final RenderType polygonOffset;
/*    */   
/*    */   private final RenderPipeline guiPipeline;
/*    */   
/*    */   public GlyphRenderTypes(RenderType normal, RenderType seeThrough, RenderType polygonOffset, RenderPipeline guiPipeline) {
/* 10 */     this.normal = normal;
/* 10 */     this.seeThrough = seeThrough;
/* 10 */     this.polygonOffset = polygonOffset;
/* 10 */     this.guiPipeline = guiPipeline;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/GlyphRenderTypes;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/GlyphRenderTypes;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/GlyphRenderTypes;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/GlyphRenderTypes;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/GlyphRenderTypes;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/GlyphRenderTypes;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public RenderType normal() {
/* 10 */     return this.normal;
/*    */   }
/*    */   
/*    */   public RenderType seeThrough() {
/* 10 */     return this.seeThrough;
/*    */   }
/*    */   
/*    */   public RenderType polygonOffset() {
/* 10 */     return this.polygonOffset;
/*    */   }
/*    */   
/*    */   public RenderPipeline guiPipeline() {
/* 10 */     return this.guiPipeline;
/*    */   }
/*    */   
/*    */   public static GlyphRenderTypes createForGrayscaleTexture(Identifier name) {
/* 17 */     return new GlyphRenderTypes(
/* 18 */         RenderTypes.textGrayscale(name), 
/* 19 */         RenderTypes.textGrayscaleSeeThrough(name), 
/* 20 */         RenderTypes.textGrayscalePolygonOffset(name), RenderPipelines.GUI_TEXT_GRAYSCALE);
/*    */   }
/*    */   
/*    */   public static GlyphRenderTypes createForColorTexture(Identifier name) {
/* 26 */     return new GlyphRenderTypes(
/* 27 */         RenderTypes.text(name), 
/* 28 */         RenderTypes.textSeeThrough(name), 
/* 29 */         RenderTypes.textPolygonOffset(name), RenderPipelines.GUI_TEXT);
/*    */   }
/*    */   
/*    */   public RenderType select(Font.DisplayMode mode) {
/* 35 */     switch (mode) {
/*    */       default:
/* 35 */         throw new MatchException(null, null);
/*    */       case NORMAL:
/*    */       
/*    */       case SEE_THROUGH:
/*    */       
/*    */       case POLYGON_OFFSET:
/*    */         break;
/*    */     } 
/* 35 */     return 
/*    */       
/* 38 */       this.polygonOffset;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\GlyphRenderTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */