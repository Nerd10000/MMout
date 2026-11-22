/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.vertex.VertexFormat;
/*    */ import com.mojang.blaze3d.vertex.VertexFormatElement;
/*    */ import java.util.Arrays;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.lwjgl.opengl.GL33C;
/*    */ 
/*    */ class Emulated extends VertexArrayCache {
/* 33 */   private final Map<List<VertexFormat>, VertexArrayCache.VertexArray> cache = new HashMap<>();
/*    */   
/*    */   private final GlDebugLabel debugLabels;
/*    */   
/*    */   public Emulated(GlDebugLabel debugLabels) {
/* 37 */     this.debugLabels = debugLabels;
/*    */   }
/*    */   
/*    */   public VertexArrayCache.VertexArray bindVertexArray(VertexFormat[] vertexBindings, GpuBufferSlice[] vertexBuffers, VertexArrayCache.VertexArray lastBoundVertexArray) {
/* 42 */     List<VertexFormat> listBindings = Arrays.asList(vertexBindings);
/* 43 */     VertexArrayCache.VertexArray vertexArray = this.cache.get(listBindings);
/* 44 */     if (vertexArray == null) {
/* 47 */       int id = GlStateManager._glGenVertexArrays();
/* 48 */       GlStateManager._glBindVertexArray(id);
/* 49 */       setupCombinedAttributes(vertexBindings, true, vertexBuffers);
/* 51 */       VertexArrayCache.VertexArray vao = new VertexArrayCache.VertexArray(id, vertexBindings);
/* 52 */       this.debugLabels.applyLabel(vao);
/* 53 */       this.cache.put(listBindings, vao);
/* 54 */       return vao;
/*    */     } 
/* 58 */     GlStateManager._glBindVertexArray(vertexArray.id);
/* 61 */     if (vertexArray != lastBoundVertexArray)
/* 62 */       setupCombinedAttributes(vertexBindings, false, vertexBuffers); 
/* 64 */     return vertexArray;
/*    */   }
/*    */   
/*    */   private static void setupCombinedAttributes(VertexFormat[] vertexBindings, boolean enable, GpuBufferSlice[] vertexBuffers) {
/* 68 */     int attributeIndex = 0;
/* 69 */     for (int i = 0; i < vertexBindings.length; i++) {
/* 70 */       VertexFormat vertexBinding = vertexBindings[i];
/* 71 */       if (vertexBinding != null) {
/* 75 */         GlBuffer buffer = (GlBuffer)vertexBuffers[i].buffer();
/* 76 */         GlStateManager._glBindBuffer(34962, buffer.handle());
/* 77 */         int vertexSize = vertexBinding.getVertexSize();
/* 79 */         for (VertexFormatElement element : vertexBinding.getElements()) {
/* 80 */           long totalOffset = vertexBuffers[i].offset() + element.offset();
/* 81 */           int glExternalId = GlConst.toGlExternalId(element.format());
/* 82 */           int glType = GlConst.toGlType(element.format());
/* 83 */           boolean isIntegerFormat = GlConst.isGlFormatInteger(glExternalId);
/* 84 */           boolean isNormalizedFormat = GlConst.isFormatNormalized(element.format());
/* 85 */           int channelCount = GlConst.glFormatChannelCount(glExternalId);
/* 86 */           if (enable)
/* 87 */             GlStateManager._enableVertexAttribArray(attributeIndex); 
/* 89 */           if (isIntegerFormat) {
/* 90 */             GlStateManager._vertexAttribIPointer(attributeIndex, channelCount, glType, vertexSize, totalOffset);
/*    */           } else {
/* 92 */             GlStateManager._vertexAttribPointer(attributeIndex, channelCount, glType, isNormalizedFormat, vertexSize, totalOffset);
/*    */           } 
/* 94 */           GL33C.glVertexAttribDivisor(attributeIndex, vertexBinding.getStepRate());
/* 95 */           attributeIndex++;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\VertexArrayCache$Emulated.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */