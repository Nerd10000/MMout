/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.platform.Transparency;
/*    */ import com.mojang.blaze3d.vertex.VertexFormat;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ 
/*    */ public enum ChunkSectionLayer {
/* 13 */   SOLID(RenderPipelines.SOLID_TERRAIN, 4194304, false),
/* 14 */   CUTOUT(RenderPipelines.CUTOUT_TERRAIN, 4194304, false),
/* 15 */   TRANSLUCENT(RenderPipelines.TRANSLUCENT_TERRAIN, 786432, true);
/*    */   
/*    */   private final RenderPipeline pipeline;
/*    */   
/*    */   private final int bufferSize;
/*    */   
/*    */   private final boolean translucent;
/*    */   
/*    */   private final String label;
/*    */   
/*    */   ChunkSectionLayer(RenderPipeline pipeline, int bufferSize, boolean translucent) {
/* 24 */     this.pipeline = pipeline;
/* 25 */     this.bufferSize = bufferSize;
/* 26 */     this.translucent = translucent;
/* 27 */     this.label = toString().toLowerCase(Locale.ROOT);
/*    */   }
/*    */   
/*    */   public static ChunkSectionLayer byTransparency(Transparency transparency) {
/* 31 */     if (transparency.hasTranslucent())
/* 32 */       return TRANSLUCENT; 
/* 33 */     if (transparency.hasTransparent())
/* 34 */       return CUTOUT; 
/* 36 */     return SOLID;
/*    */   }
/*    */   
/*    */   public RenderPipeline pipeline() {
/* 40 */     return this.pipeline;
/*    */   }
/*    */   
/*    */   public int bufferSize() {
/* 44 */     return this.bufferSize;
/*    */   }
/*    */   
/*    */   public String label() {
/* 48 */     return this.label;
/*    */   }
/*    */   
/*    */   public boolean translucent() {
/* 52 */     return this.translucent;
/*    */   }
/*    */   
/*    */   public VertexFormat vertexFormat() {
/* 56 */     return this.pipeline.getVertexFormatBinding(0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\chunk\ChunkSectionLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */