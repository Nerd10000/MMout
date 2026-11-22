/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexFormatElement;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import org.lwjgl.opengl.ARBVertexAttribBinding;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ 
/*     */ public abstract class VertexArrayCache {
/*     */   public static VertexArrayCache create(GLCapabilities capabilities, GlDebugLabel debugLabels, Set<String> enabledExtensions) {
/*  23 */     if (capabilities.GL_ARB_vertex_attrib_binding && GlDevice.USE_GL_ARB_vertex_attrib_binding) {
/*  24 */       enabledExtensions.add("GL_ARB_vertex_attrib_binding");
/*  25 */       return new Separate(debugLabels);
/*     */     } 
/*  27 */     return new Emulated(debugLabels);
/*     */   }
/*     */   
/*     */   public abstract VertexArray bindVertexArray(VertexFormat[] paramArrayOfVertexFormat, GpuBufferSlice[] paramArrayOfGpuBufferSlice, VertexArray paramVertexArray);
/*     */   
/*     */   private static class Emulated extends VertexArrayCache {
/*  33 */     private final Map<List<VertexFormat>, VertexArrayCache.VertexArray> cache = new HashMap<>();
/*     */     
/*     */     private final GlDebugLabel debugLabels;
/*     */     
/*     */     public Emulated(GlDebugLabel debugLabels) {
/*  37 */       this.debugLabels = debugLabels;
/*     */     }
/*     */     
/*     */     public VertexArrayCache.VertexArray bindVertexArray(VertexFormat[] vertexBindings, GpuBufferSlice[] vertexBuffers, VertexArrayCache.VertexArray lastBoundVertexArray) {
/*  42 */       List<VertexFormat> listBindings = Arrays.asList(vertexBindings);
/*  43 */       VertexArrayCache.VertexArray vertexArray = this.cache.get(listBindings);
/*  44 */       if (vertexArray == null) {
/*  47 */         int id = GlStateManager._glGenVertexArrays();
/*  48 */         GlStateManager._glBindVertexArray(id);
/*  49 */         setupCombinedAttributes(vertexBindings, true, vertexBuffers);
/*  51 */         VertexArrayCache.VertexArray vao = new VertexArrayCache.VertexArray(id, vertexBindings);
/*  52 */         this.debugLabels.applyLabel(vao);
/*  53 */         this.cache.put(listBindings, vao);
/*  54 */         return vao;
/*     */       } 
/*  58 */       GlStateManager._glBindVertexArray(vertexArray.id);
/*  61 */       if (vertexArray != lastBoundVertexArray)
/*  62 */         setupCombinedAttributes(vertexBindings, false, vertexBuffers); 
/*  64 */       return vertexArray;
/*     */     }
/*     */     
/*     */     private static void setupCombinedAttributes(VertexFormat[] vertexBindings, boolean enable, GpuBufferSlice[] vertexBuffers) {
/*  68 */       int attributeIndex = 0;
/*  69 */       for (int i = 0; i < vertexBindings.length; i++) {
/*  70 */         VertexFormat vertexBinding = vertexBindings[i];
/*  71 */         if (vertexBinding != null) {
/*  75 */           GlBuffer buffer = (GlBuffer)vertexBuffers[i].buffer();
/*  76 */           GlStateManager._glBindBuffer(34962, buffer.handle());
/*  77 */           int vertexSize = vertexBinding.getVertexSize();
/*  79 */           for (VertexFormatElement element : vertexBinding.getElements()) {
/*  80 */             long totalOffset = vertexBuffers[i].offset() + element.offset();
/*  81 */             int glExternalId = GlConst.toGlExternalId(element.format());
/*  82 */             int glType = GlConst.toGlType(element.format());
/*  83 */             boolean isIntegerFormat = GlConst.isGlFormatInteger(glExternalId);
/*  84 */             boolean isNormalizedFormat = GlConst.isFormatNormalized(element.format());
/*  85 */             int channelCount = GlConst.glFormatChannelCount(glExternalId);
/*  86 */             if (enable)
/*  87 */               GlStateManager._enableVertexAttribArray(attributeIndex); 
/*  89 */             if (isIntegerFormat) {
/*  90 */               GlStateManager._vertexAttribIPointer(attributeIndex, channelCount, glType, vertexSize, totalOffset);
/*     */             } else {
/*  92 */               GlStateManager._vertexAttribPointer(attributeIndex, channelCount, glType, isNormalizedFormat, vertexSize, totalOffset);
/*     */             } 
/*  94 */             GL33C.glVertexAttribDivisor(attributeIndex, vertexBinding.getStepRate());
/*  95 */             attributeIndex++;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Separate extends VertexArrayCache {
/* 102 */     private final Map<List<VertexFormat>, VertexArrayCache.VertexArray> cache = new HashMap<>();
/*     */     
/*     */     private final GlDebugLabel debugLabels;
/*     */     
/*     */     private final boolean needsMesaWorkaround;
/*     */     
/*     */     public Separate(GlDebugLabel debugLabels) {
/* 107 */       this.debugLabels = debugLabels;
/* 108 */       if ("Mesa".equals(GlStateManager._getString(7936))) {
/* 109 */         String version = GlStateManager._getString(7938);
/* 111 */         this.needsMesaWorkaround = (version.contains("25.0.0") || version.contains("25.0.1") || version.contains("25.0.2"));
/*     */       } else {
/* 113 */         this.needsMesaWorkaround = false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public VertexArrayCache.VertexArray bindVertexArray(VertexFormat[] vertexBindings, GpuBufferSlice[] vertexBuffers, VertexArrayCache.VertexArray lastBoundVertexArray) {
/* 120 */       List<VertexFormat> listBindings = Arrays.asList(vertexBindings);
/* 121 */       VertexArrayCache.VertexArray vertexArray = this.cache.get(listBindings);
/* 122 */       if (vertexArray == null) {
/* 123 */         int id = GlStateManager._glGenVertexArrays();
/* 124 */         GlStateManager._glBindVertexArray(id);
/* 126 */         int attribLocation = 0;
/*     */         int i;
/* 127 */         for (i = 0; i < vertexBindings.length; i++) {
/* 128 */           VertexFormat vertexBinding = vertexBindings[i];
/* 129 */           if (vertexBinding != null) {
/* 133 */             for (VertexFormatElement element : vertexBinding.getElements()) {
/* 134 */               if (element == null)
/*     */                 continue; 
/* 138 */               GlStateManager._enableVertexAttribArray(attribLocation);
/* 139 */               int glExternalId = GlConst.toGlExternalId(element.format());
/* 140 */               int glType = GlConst.toGlType(element.format());
/* 141 */               boolean isIntegerFormat = GlConst.isGlFormatInteger(glExternalId);
/* 142 */               boolean isNormalizedFormat = GlConst.isFormatNormalized(element.format());
/* 143 */               int channelCount = GlConst.glFormatChannelCount(glExternalId);
/* 144 */               if (isIntegerFormat) {
/* 145 */                 ARBVertexAttribBinding.glVertexAttribIFormat(attribLocation, channelCount, glType, element.offset());
/*     */               } else {
/* 147 */                 ARBVertexAttribBinding.glVertexAttribFormat(attribLocation, channelCount, glType, isNormalizedFormat, element.offset());
/*     */               } 
/* 149 */               ARBVertexAttribBinding.glVertexAttribBinding(attribLocation, i);
/* 150 */               attribLocation++;
/*     */             } 
/* 152 */             ARBVertexAttribBinding.glVertexBindingDivisor(i, vertexBinding.getStepRate());
/*     */           } 
/*     */         } 
/* 155 */         for (i = 0; i < vertexBuffers.length; i++) {
/* 156 */           GpuBufferSlice vertexBufferSlice = vertexBuffers[i];
/* 157 */           if (vertexBufferSlice != null) {
/* 158 */             GlBuffer vertexBuffer = (GlBuffer)vertexBufferSlice.buffer();
/* 159 */             ARBVertexAttribBinding.glBindVertexBuffer(i, vertexBuffer.handle(), vertexBufferSlice.offset(), vertexBindings[i].getVertexSize());
/*     */           } 
/*     */         } 
/* 163 */         VertexArrayCache.VertexArray vao = new VertexArrayCache.VertexArray(id, vertexBindings);
/* 164 */         this.debugLabels.applyLabel(vao);
/* 165 */         this.cache.put(listBindings, vao);
/* 166 */         return vao;
/*     */       } 
/* 170 */       GlStateManager._glBindVertexArray(vertexArray.id);
/* 174 */       if (vertexArray != lastBoundVertexArray)
/* 175 */         for (int i = 0; i < vertexBuffers.length; i++) {
/* 176 */           GpuBufferSlice vertexBufferSlice = vertexBuffers[i];
/* 177 */           if (vertexBufferSlice != null) {
/* 178 */             GlBuffer vertexBuffer = (GlBuffer)vertexBufferSlice.buffer();
/* 179 */             if (this.needsMesaWorkaround)
/* 183 */               ARBVertexAttribBinding.glBindVertexBuffer(i, 0, 0L, 0); 
/* 185 */             ARBVertexAttribBinding.glBindVertexBuffer(i, vertexBuffer.handle(), vertexBufferSlice.offset(), vertexBindings[i].getVertexSize());
/*     */           } 
/*     */         }  
/* 189 */       return vertexArray;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class VertexArray {
/*     */     @VisibleForDebug
/*     */     final int id;
/*     */     
/*     */     @VisibleForDebug
/*     */     final String formatName;
/*     */     
/*     */     private VertexArray(int id, VertexFormat[] vertexBindings) {
/* 203 */       this.id = id;
/* 204 */       this.formatName = Arrays.<VertexFormat>stream(vertexBindings).filter(Objects::nonNull).map(VertexFormat::toString).collect(Collectors.joining(", "));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\VertexArrayCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */