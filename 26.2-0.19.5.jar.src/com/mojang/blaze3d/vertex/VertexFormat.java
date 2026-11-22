/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class VertexFormat {
/*     */   private static final int VERTEX_ALIGNMENT = 4;
/*     */   
/*     */   public static final int MAX_VERTEX_ELEMENTS = 16;
/*     */   
/*  19 */   private final Map<String, VertexFormatElement> elements = (Map<String, VertexFormatElement>)new Object2ObjectArrayMap(16);
/*     */   
/*     */   private final int vertexSize;
/*     */   
/*     */   private final int stepRate;
/*     */   
/*     */   private final List<VertexFormatElement> elementValues;
/*     */   
/*     */   private VertexFormat(List<VertexFormatElement> elements, int vertexSize, int stepRate) {
/*  25 */     this.vertexSize = vertexSize;
/*  26 */     this.stepRate = stepRate;
/*  27 */     for (VertexFormatElement element : elements)
/*  28 */       this.elements.putIfAbsent(element.name(), element); 
/*  30 */     this.elementValues = elements;
/*     */   }
/*     */   
/*     */   public static Builder builder(int stepRate) {
/*  34 */     return new Builder(stepRate);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  39 */     return "VertexFormat" + (String)this.elementValues.stream().map(VertexFormatElement::name).collect(Collectors.joining(", ", "[", "]"));
/*     */   }
/*     */   
/*     */   public int getVertexSize() {
/*  43 */     return this.vertexSize;
/*     */   }
/*     */   
/*     */   public int getStepRate() {
/*  47 */     return this.stepRate;
/*     */   }
/*     */   
/*     */   public List<VertexFormatElement> getElements() {
/*  51 */     return this.elementValues;
/*     */   }
/*     */   
/*     */   public VertexFormatElement getElement(String attributeName) {
/*  55 */     return this.elements.get(attributeName);
/*     */   }
/*     */   
/*     */   public boolean contains(String attributeName) {
/*  59 */     return this.elements.containsKey(attributeName);
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: if_acmpne -> 7
/*     */     //   5: iconst_1
/*     */     //   6: ireturn
/*     */     //   7: aload_1
/*     */     //   8: instanceof com/mojang/blaze3d/vertex/VertexFormat
/*     */     //   11: ifeq -> 50
/*     */     //   14: aload_1
/*     */     //   15: checkcast com/mojang/blaze3d/vertex/VertexFormat
/*     */     //   18: astore_2
/*     */     //   19: aload_0
/*     */     //   20: getfield elements : Ljava/util/Map;
/*     */     //   23: aload_2
/*     */     //   24: getfield elements : Ljava/util/Map;
/*     */     //   27: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   32: ifeq -> 50
/*     */     //   35: aload_0
/*     */     //   36: getfield vertexSize : I
/*     */     //   39: aload_2
/*     */     //   40: getfield vertexSize : I
/*     */     //   43: if_icmpne -> 50
/*     */     //   46: iconst_1
/*     */     //   47: goto -> 51
/*     */     //   50: iconst_0
/*     */     //   51: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #64	-> 0
/*     */     //   #65	-> 5
/*     */     //   #69	-> 7
/*     */     //   #67	-> 14
/*     */     //   #68	-> 27
/*     */     //   #67	-> 51
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   19	31	2	format	Lcom/mojang/blaze3d/vertex/VertexFormat;
/*     */     //   0	52	0	this	Lcom/mojang/blaze3d/vertex/VertexFormat;
/*     */     //   0	52	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  74 */     return this.elementValues.hashCode();
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  78 */     private final List<VertexFormatElement> elements = new ArrayList<>(16);
/*     */     
/*  79 */     private int offset = 0;
/*     */     
/*     */     private final int stepRate;
/*     */     
/*     */     private Builder(int stepRate) {
/*  83 */       this.stepRate = stepRate;
/*     */     }
/*     */     
/*     */     private void createAttribute(String name, int offset, GpuFormat elementFormat) {
/*  87 */       if (this.elements.size() >= 16)
/*  88 */         throw new IllegalArgumentException("Having more than 16 attributes are not supported"); 
/*  90 */       if (!Mth.isMultipleOf(offset, elementFormat.byteAlignment()))
/*  91 */         throw new IllegalArgumentException(name + " is not aligned to " + name + " as required by " + elementFormat.byteAlignment()); 
/*  94 */       VertexFormatElement element = new VertexFormatElement(name, offset, elementFormat);
/*  95 */       this.elements.add(element);
/*     */     }
/*     */     
/*     */     private void validateUniqueName(String name) {
/*  99 */       for (VertexFormatElement element : this.elements) {
/* 100 */         if (element.name().equals(name))
/* 101 */           throw new IllegalArgumentException("Another vertex attribute exists with the name " + name); 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Builder addAttribute(String name, GpuFormat elementFormat) {
/* 108 */       validateUniqueName(name);
/* 109 */       createAttribute(name, this.offset, elementFormat);
/* 110 */       this.offset += elementFormat.blockSize();
/* 111 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addAttribute(String name, int stride, GpuFormat elementFormat) {
/* 116 */       validateUniqueName(name);
/* 117 */       createAttribute(name, this.offset, elementFormat);
/* 118 */       this.offset += stride;
/* 119 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addAttribute(String name, GpuFormat elementFormat, int columnCount) {
/* 124 */       validateUniqueName(name);
/* 125 */       for (int i = 0; i < columnCount; i++) {
/* 126 */         createAttribute(name, this.offset, elementFormat);
/* 127 */         this.offset += elementFormat.blockSize();
/*     */       } 
/* 129 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addAttribute(String name, int offset, int stride, GpuFormat elementFormat, int columnCount) {
/* 135 */       validateUniqueName(name);
/* 136 */       int offsetTracker = offset;
/* 137 */       for (int i = 0; i < columnCount; i++) {
/* 138 */         createAttribute(name, offsetTracker, elementFormat);
/* 139 */         offsetTracker += stride;
/*     */       } 
/* 141 */       this.offset = Math.max(this.offset, offsetTracker);
/* 142 */       return this;
/*     */     }
/*     */     
/*     */     public VertexFormat build() {
/* 146 */       int vertexSize = this.offset;
/* 147 */       if (!Mth.isMultipleOf(vertexSize, 4))
/* 148 */         throw new IllegalStateException("Vertex size must be a multiple of 4, was " + vertexSize); 
/* 151 */       return new VertexFormat(this.elements, vertexSize, this.stepRate);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\VertexFormat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */