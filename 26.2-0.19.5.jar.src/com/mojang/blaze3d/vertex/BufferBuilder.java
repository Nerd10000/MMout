/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public class BufferBuilder implements VertexConsumer {
/*     */   private static final int MAX_VERTEX_COUNT = 16777215;
/*     */   
/*     */   private static final long NOT_BUILDING = -1L;
/*     */   
/*     */   private static final long UNKNOWN_ELEMENT = -1L;
/*     */   
/*  21 */   private static final boolean IS_LITTLE_ENDIAN = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN);
/*     */   
/*     */   private final ByteBufferBuilder buffer;
/*     */   
/*  25 */   private long vertexPointer = -1L;
/*     */   
/*     */   private int vertices;
/*     */   
/*     */   private final VertexFormat format;
/*     */   
/*     */   private final PrimitiveTopology primitiveTopology;
/*     */   
/*     */   private final boolean blockFormat;
/*     */   
/*     */   private final boolean entityFormat;
/*     */   
/*     */   private final int vertexSize;
/*     */   
/*     */   private final int initialElementsToFill;
/*     */   
/*     */   private int elementsToFill;
/*     */   
/*     */   private boolean building = true;
/*     */   
/*     */   private static final int POSITION_SEMANTIC_ID = 0;
/*     */   
/*     */   private static final int COLOR_SEMANTIC_ID = 1;
/*     */   
/*     */   private static final int UV0_SEMANTIC_ID = 2;
/*     */   
/*     */   private static final int UV1_SEMANTIC_ID = 3;
/*     */   
/*     */   private static final int UV2_SEMANTIC_ID = 4;
/*     */   
/*     */   private static final int NORMAL_SEMANTIC_ID = 5;
/*     */   
/*     */   private static final int LINE_WIDTH_SEMANTIC_ID = 6;
/*     */   
/*  46 */   private static final String[] elementNames = new String[] { "Position", "Color", "UV0", "UV1", "UV2", "Normal", "LineWidth" };
/*     */   
/*  55 */   private final VertexFormatElement[] elements = new VertexFormatElement[elementNames.length];
/*     */   
/*     */   public BufferBuilder(ByteBufferBuilder buffer, PrimitiveTopology primitiveTopology, VertexFormat format) {
/*  58 */     if (!format.contains("Position"))
/*  59 */       throw new IllegalArgumentException("Cannot build mesh with no position element"); 
/*  62 */     this.buffer = buffer;
/*  63 */     this.primitiveTopology = primitiveTopology;
/*  64 */     this.format = format;
/*  66 */     this.vertexSize = format.getVertexSize();
/*  68 */     int elementsMask = 0;
/*  69 */     for (int i = 0; i < elementNames.length; i++) {
/*  70 */       String elementName = elementNames[i];
/*  71 */       VertexFormatElement element = format.getElement(elementName);
/*  72 */       if (element != null)
/*  73 */         elementsMask |= 1 << i; 
/*  75 */       this.elements[i] = element;
/*     */     } 
/*  78 */     this.initialElementsToFill = elementsMask & 0xFFFFFFFE;
/*  80 */     this.blockFormat = (format == DefaultVertexFormat.BLOCK);
/*  81 */     this.entityFormat = (format == DefaultVertexFormat.ENTITY);
/*     */   }
/*     */   
/*     */   public MeshData build() {
/*  86 */     ensureBuilding();
/*  87 */     endLastVertex();
/*  88 */     MeshData mesh = storeMesh();
/*  89 */     this.building = false;
/*  90 */     this.vertexPointer = -1L;
/*  91 */     return mesh;
/*     */   }
/*     */   
/*     */   public MeshData buildOrThrow() {
/*  95 */     MeshData buffer = build();
/*  96 */     if (buffer == null)
/*  97 */       throw new IllegalStateException("BufferBuilder was empty"); 
/*  99 */     return buffer;
/*     */   }
/*     */   
/*     */   private void ensureBuilding() {
/* 103 */     if (!this.building)
/* 104 */       throw new IllegalStateException("Not building!"); 
/*     */   }
/*     */   
/*     */   private MeshData storeMesh() {
/* 109 */     if (this.vertices == 0)
/* 110 */       return null; 
/* 112 */     ByteBufferBuilder.Result vertexBuffer = this.buffer.build();
/* 113 */     if (vertexBuffer == null)
/* 114 */       return null; 
/* 116 */     int indices = this.primitiveTopology.indexCount(this.vertices);
/* 117 */     IndexType indexType = IndexType.least(this.vertices);
/* 118 */     return new MeshData(vertexBuffer, new MeshData.DrawState(this.format, this.vertices, indices, this.primitiveTopology, indexType));
/*     */   }
/*     */   
/*     */   private long beginVertex() {
/* 122 */     ensureBuilding();
/* 123 */     endLastVertex();
/* 124 */     if (this.vertices >= 16777215)
/* 125 */       throw new IllegalStateException("Trying to write too many vertices (>16777215) into BufferBuilder"); 
/* 127 */     this.vertices++;
/* 128 */     long pointer = this.buffer.reserve(this.vertexSize);
/* 129 */     this.vertexPointer = pointer;
/* 130 */     return pointer;
/*     */   }
/*     */   
/*     */   private long beginElement(int semanticID) {
/* 134 */     int oldElements = this.elementsToFill;
/* 135 */     int newElements = oldElements & (1 << semanticID ^ 0xFFFFFFFF);
/* 136 */     VertexFormatElement element = this.elements[semanticID];
/* 137 */     if (newElements == oldElements || element == null)
/* 138 */       return -1L; 
/* 140 */     this.elementsToFill = newElements;
/* 142 */     long vertexPointer = this.vertexPointer;
/* 143 */     if (vertexPointer == -1L)
/* 144 */       throw new IllegalArgumentException("Not currently building vertex"); 
/* 146 */     return vertexPointer + element.offset();
/*     */   }
/*     */   
/*     */   private void endLastVertex() {
/* 150 */     if (this.vertices == 0)
/*     */       return; 
/* 154 */     if (this.elementsToFill != 0) {
/* 156 */       String missingElements = IntStream.range(0, elementNames.length).filter(i -> ((this.elementsToFill & i) != 0)).<CharSequence>mapToObj(i -> elementNames[i]).collect(Collectors.joining(", "));
/* 157 */       throw new IllegalStateException("Missing elements in vertex: " + missingElements);
/*     */     } 
/* 162 */     if (this.primitiveTopology == PrimitiveTopology.LINES) {
/* 163 */       long pointer = this.buffer.reserve(this.vertexSize);
/* 164 */       MemoryUtil.memCopy(pointer - this.vertexSize, pointer, this.vertexSize);
/* 165 */       this.vertices++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void putRgba(long pointer, int argb) {
/* 170 */     int abgr = ARGB.toABGR(argb);
/* 172 */     MemoryUtil.memPutInt(pointer, IS_LITTLE_ENDIAN ? abgr : Integer.reverseBytes(abgr));
/*     */   }
/*     */   
/*     */   private static void putPackedUv(long pointer, int packedUv) {
/* 176 */     if (IS_LITTLE_ENDIAN) {
/* 177 */       MemoryUtil.memPutInt(pointer, packedUv);
/*     */     } else {
/* 179 */       MemoryUtil.memPutShort(pointer, (short)(packedUv & 0xFFFF));
/* 180 */       MemoryUtil.memPutShort(pointer + 2L, (short)(packedUv >> 16 & 0xFFFF));
/*     */     } 
/*     */   }
/*     */   
/*     */   public VertexConsumer addVertex(float x, float y, float z) {
/* 186 */     VertexFormatElement positionElement = this.elements[0];
/* 187 */     long pointer = beginVertex() + positionElement.offset();
/* 188 */     this.elementsToFill = this.initialElementsToFill;
/* 189 */     putVec3f(pointer, x, y, z);
/* 190 */     return this;
/*     */   }
/*     */   
/*     */   public VertexConsumer setColor(int r, int g, int b, int a) {
/* 195 */     long pointer = beginElement(1);
/* 196 */     if (pointer != -1L) {
/* 197 */       MemoryUtil.memPutByte(pointer, (byte)r);
/* 198 */       MemoryUtil.memPutByte(pointer + 1L, (byte)g);
/* 199 */       MemoryUtil.memPutByte(pointer + 2L, (byte)b);
/* 200 */       MemoryUtil.memPutByte(pointer + 3L, (byte)a);
/*     */     } 
/* 202 */     return this;
/*     */   }
/*     */   
/*     */   public VertexConsumer setColor(int color) {
/* 207 */     long pointer = beginElement(1);
/* 208 */     if (pointer != -1L)
/* 209 */       putRgba(pointer, color); 
/* 211 */     return this;
/*     */   }
/*     */   
/*     */   public VertexConsumer setUv(float u, float v) {
/* 216 */     long pointer = beginElement(2);
/* 217 */     if (pointer != -1L) {
/* 218 */       MemoryUtil.memPutFloat(pointer, u);
/* 219 */       MemoryUtil.memPutFloat(pointer + 4L, v);
/*     */     } 
/* 221 */     return this;
/*     */   }
/*     */   
/*     */   public VertexConsumer setUv1(int u, int v) {
/* 226 */     return uvShort((short)u, (short)v, 3);
/*     */   }
/*     */   
/*     */   public VertexConsumer setOverlay(int packedOverlayCoords) {
/* 231 */     long pointer = beginElement(3);
/* 232 */     if (pointer != -1L)
/* 233 */       putPackedUv(pointer, packedOverlayCoords); 
/* 235 */     return this;
/*     */   }
/*     */   
/*     */   public VertexConsumer setUv2(int u, int v) {
/* 240 */     return uvShort((short)u, (short)v, 4);
/*     */   }
/*     */   
/*     */   public VertexConsumer setLight(int packedLightCoords) {
/* 245 */     long pointer = beginElement(4);
/* 246 */     if (pointer != -1L)
/* 247 */       putPackedUv(pointer, packedLightCoords); 
/* 249 */     return this;
/*     */   }
/*     */   
/*     */   private VertexConsumer uvShort(short u, short v, int semanticID) {
/* 253 */     long pointer = beginElement(semanticID);
/* 254 */     if (pointer != -1L) {
/* 255 */       MemoryUtil.memPutShort(pointer, u);
/* 256 */       MemoryUtil.memPutShort(pointer + 2L, v);
/*     */     } 
/* 258 */     return this;
/*     */   }
/*     */   
/*     */   public VertexConsumer setNormal(float x, float y, float z) {
/* 263 */     long pointer = beginElement(5);
/* 264 */     if (pointer != -1L)
/* 265 */       putNormals(pointer, x, y, z); 
/* 267 */     return this;
/*     */   }
/*     */   
/*     */   public VertexConsumer setLineWidth(float width) {
/* 272 */     long pointer = beginElement(6);
/* 273 */     if (pointer != -1L)
/* 274 */       MemoryUtil.memPutFloat(pointer, width); 
/* 276 */     return this;
/*     */   }
/*     */   
/*     */   private static byte normalIntValue(float c) {
/* 280 */     return (byte)((int)(Mth.clamp(c, -1.0F, 1.0F) * 127.0F) & 0xFF);
/*     */   }
/*     */   
/*     */   private static void putVec3f(long pointer, float x, float y, float z) {
/* 284 */     MemoryUtil.memPutFloat(pointer, x);
/* 285 */     MemoryUtil.memPutFloat(pointer + 4L, y);
/* 286 */     MemoryUtil.memPutFloat(pointer + 8L, z);
/*     */   }
/*     */   
/*     */   private static void putNormals(long pointer, float nx, float ny, float nz) {
/* 290 */     MemoryUtil.memPutByte(pointer, normalIntValue(nx));
/* 291 */     MemoryUtil.memPutByte(pointer + 1L, normalIntValue(ny));
/* 292 */     MemoryUtil.memPutByte(pointer + 2L, normalIntValue(nz));
/*     */   }
/*     */   
/*     */   public void addVertex(float x, float y, float z, int color, float u, float v, int overlayCoords, int lightCoords, float nx, float ny, float nz) {
/* 297 */     if (this.blockFormat) {
/* 298 */       long pointer = beginVertex();
/* 300 */       putVec3f(pointer, x, y, z);
/* 301 */       putRgba(pointer + 12L, color);
/* 303 */       MemoryUtil.memPutFloat(pointer + 16L, u);
/* 304 */       MemoryUtil.memPutFloat(pointer + 20L, v);
/* 306 */       putPackedUv(pointer + 24L, lightCoords);
/* 307 */     } else if (this.entityFormat) {
/* 308 */       long pointer = beginVertex();
/* 310 */       putVec3f(pointer, x, y, z);
/* 311 */       putRgba(pointer + 12L, color);
/* 313 */       MemoryUtil.memPutFloat(pointer + 16L, u);
/* 314 */       MemoryUtil.memPutFloat(pointer + 20L, v);
/* 316 */       putPackedUv(pointer + 24L, overlayCoords);
/* 317 */       putPackedUv(pointer + 28L, lightCoords);
/* 319 */       putNormals(pointer + 32L, nx, ny, nz);
/*     */     } else {
/* 321 */       super.addVertex(x, y, z, color, u, v, overlayCoords, lightCoords, nx, ny, nz);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\BufferBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */