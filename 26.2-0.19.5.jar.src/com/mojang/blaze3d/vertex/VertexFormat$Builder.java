/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class Builder {
/*  78 */   private final List<VertexFormatElement> elements = new ArrayList<>(16);
/*     */   
/*  79 */   private int offset = 0;
/*     */   
/*     */   private final int stepRate;
/*     */   
/*     */   private Builder(int stepRate) {
/*  83 */     this.stepRate = stepRate;
/*     */   }
/*     */   
/*     */   private void createAttribute(String name, int offset, GpuFormat elementFormat) {
/*  87 */     if (this.elements.size() >= 16)
/*  88 */       throw new IllegalArgumentException("Having more than 16 attributes are not supported"); 
/*  90 */     if (!Mth.isMultipleOf(offset, elementFormat.byteAlignment()))
/*  91 */       throw new IllegalArgumentException(name + " is not aligned to " + name + " as required by " + elementFormat.byteAlignment()); 
/*  94 */     VertexFormatElement element = new VertexFormatElement(name, offset, elementFormat);
/*  95 */     this.elements.add(element);
/*     */   }
/*     */   
/*     */   private void validateUniqueName(String name) {
/*  99 */     for (VertexFormatElement element : this.elements) {
/* 100 */       if (element.name().equals(name))
/* 101 */         throw new IllegalArgumentException("Another vertex attribute exists with the name " + name); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Builder addAttribute(String name, GpuFormat elementFormat) {
/* 108 */     validateUniqueName(name);
/* 109 */     createAttribute(name, this.offset, elementFormat);
/* 110 */     this.offset += elementFormat.blockSize();
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addAttribute(String name, int stride, GpuFormat elementFormat) {
/* 116 */     validateUniqueName(name);
/* 117 */     createAttribute(name, this.offset, elementFormat);
/* 118 */     this.offset += stride;
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addAttribute(String name, GpuFormat elementFormat, int columnCount) {
/* 124 */     validateUniqueName(name);
/* 125 */     for (int i = 0; i < columnCount; i++) {
/* 126 */       createAttribute(name, this.offset, elementFormat);
/* 127 */       this.offset += elementFormat.blockSize();
/*     */     } 
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addAttribute(String name, int offset, int stride, GpuFormat elementFormat, int columnCount) {
/* 135 */     validateUniqueName(name);
/* 136 */     int offsetTracker = offset;
/* 137 */     for (int i = 0; i < columnCount; i++) {
/* 138 */       createAttribute(name, offsetTracker, elementFormat);
/* 139 */       offsetTracker += stride;
/*     */     } 
/* 141 */     this.offset = Math.max(this.offset, offsetTracker);
/* 142 */     return this;
/*     */   }
/*     */   
/*     */   public VertexFormat build() {
/* 146 */     int vertexSize = this.offset;
/* 147 */     if (!Mth.isMultipleOf(vertexSize, 4))
/* 148 */       throw new IllegalStateException("Vertex size must be a multiple of 4, was " + vertexSize); 
/* 151 */     return new VertexFormat(this.elements, vertexSize, this.stepRate);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\VertexFormat$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */