/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexFormatElement;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.lwjgl.opengl.ARBVertexAttribBinding;
/*     */ 
/*     */ class Separate extends VertexArrayCache {
/* 102 */   private final Map<List<VertexFormat>, VertexArrayCache.VertexArray> cache = new HashMap<>();
/*     */   
/*     */   private final GlDebugLabel debugLabels;
/*     */   
/*     */   private final boolean needsMesaWorkaround;
/*     */   
/*     */   public Separate(GlDebugLabel debugLabels) {
/* 107 */     this.debugLabels = debugLabels;
/* 108 */     if ("Mesa".equals(GlStateManager._getString(7936))) {
/* 109 */       String version = GlStateManager._getString(7938);
/* 111 */       this.needsMesaWorkaround = (version.contains("25.0.0") || version.contains("25.0.1") || version.contains("25.0.2"));
/*     */     } else {
/* 113 */       this.needsMesaWorkaround = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public VertexArrayCache.VertexArray bindVertexArray(VertexFormat[] vertexBindings, GpuBufferSlice[] vertexBuffers, VertexArrayCache.VertexArray lastBoundVertexArray) {
/* 120 */     List<VertexFormat> listBindings = Arrays.asList(vertexBindings);
/* 121 */     VertexArrayCache.VertexArray vertexArray = this.cache.get(listBindings);
/* 122 */     if (vertexArray == null) {
/* 123 */       int id = GlStateManager._glGenVertexArrays();
/* 124 */       GlStateManager._glBindVertexArray(id);
/* 126 */       int attribLocation = 0;
/*     */       int i;
/* 127 */       for (i = 0; i < vertexBindings.length; i++) {
/* 128 */         VertexFormat vertexBinding = vertexBindings[i];
/* 129 */         if (vertexBinding != null) {
/* 133 */           for (VertexFormatElement element : vertexBinding.getElements()) {
/* 134 */             if (element == null)
/*     */               continue; 
/* 138 */             GlStateManager._enableVertexAttribArray(attribLocation);
/* 139 */             int glExternalId = GlConst.toGlExternalId(element.format());
/* 140 */             int glType = GlConst.toGlType(element.format());
/* 141 */             boolean isIntegerFormat = GlConst.isGlFormatInteger(glExternalId);
/* 142 */             boolean isNormalizedFormat = GlConst.isFormatNormalized(element.format());
/* 143 */             int channelCount = GlConst.glFormatChannelCount(glExternalId);
/* 144 */             if (isIntegerFormat) {
/* 145 */               ARBVertexAttribBinding.glVertexAttribIFormat(attribLocation, channelCount, glType, element.offset());
/*     */             } else {
/* 147 */               ARBVertexAttribBinding.glVertexAttribFormat(attribLocation, channelCount, glType, isNormalizedFormat, element.offset());
/*     */             } 
/* 149 */             ARBVertexAttribBinding.glVertexAttribBinding(attribLocation, i);
/* 150 */             attribLocation++;
/*     */           } 
/* 152 */           ARBVertexAttribBinding.glVertexBindingDivisor(i, vertexBinding.getStepRate());
/*     */         } 
/*     */       } 
/* 155 */       for (i = 0; i < vertexBuffers.length; i++) {
/* 156 */         GpuBufferSlice vertexBufferSlice = vertexBuffers[i];
/* 157 */         if (vertexBufferSlice != null) {
/* 158 */           GlBuffer vertexBuffer = (GlBuffer)vertexBufferSlice.buffer();
/* 159 */           ARBVertexAttribBinding.glBindVertexBuffer(i, vertexBuffer.handle(), vertexBufferSlice.offset(), vertexBindings[i].getVertexSize());
/*     */         } 
/*     */       } 
/* 163 */       VertexArrayCache.VertexArray vao = new VertexArrayCache.VertexArray(id, vertexBindings);
/* 164 */       this.debugLabels.applyLabel(vao);
/* 165 */       this.cache.put(listBindings, vao);
/* 166 */       return vao;
/*     */     } 
/* 170 */     GlStateManager._glBindVertexArray(vertexArray.id);
/* 174 */     if (vertexArray != lastBoundVertexArray)
/* 175 */       for (int i = 0; i < vertexBuffers.length; i++) {
/* 176 */         GpuBufferSlice vertexBufferSlice = vertexBuffers[i];
/* 177 */         if (vertexBufferSlice != null) {
/* 178 */           GlBuffer vertexBuffer = (GlBuffer)vertexBufferSlice.buffer();
/* 179 */           if (this.needsMesaWorkaround)
/* 183 */             ARBVertexAttribBinding.glBindVertexBuffer(i, 0, 0L, 0); 
/* 185 */           ARBVertexAttribBinding.glBindVertexBuffer(i, vertexBuffer.handle(), vertexBufferSlice.offset(), vertexBindings[i].getVertexSize());
/*     */         } 
/*     */       }  
/* 189 */     return vertexArray;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\VertexArrayCache$Separate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */