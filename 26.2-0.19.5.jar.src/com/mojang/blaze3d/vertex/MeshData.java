/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import it.unimi.dsi.fastutil.ints.IntConsumer;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.commons.lang3.mutable.MutableLong;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public class MeshData implements AutoCloseable {
/*     */   private final ByteBufferBuilder.Result vertexBuffer;
/*     */   
/*     */   private ByteBufferBuilder.Result indexBuffer;
/*     */   
/*     */   private final DrawState drawState;
/*     */   
/*     */   public MeshData(ByteBufferBuilder.Result vertexBuffer, DrawState drawState) {
/*  18 */     this.vertexBuffer = vertexBuffer;
/*  19 */     this.drawState = drawState;
/*     */   }
/*     */   
/*     */   public static void decodeQuadCentroids(ByteBuffer vertexBuffer, int vertexCount, VertexFormat format, CompactVectorArray output, int outputIndex) {
/*  23 */     VertexFormatElement positionElement = format.getElement("Position");
/*  24 */     if (positionElement == null)
/*  25 */       throw new IllegalArgumentException("Cannot identify quad centers with no position element"); 
/*  28 */     int positionOffset = vertexBuffer.position() + positionElement.offset();
/*  29 */     int vertexStride = format.getVertexSize();
/*  30 */     int quadStride = vertexStride * 4;
/*  31 */     int quadCount = vertexCount / 4;
/*  33 */     for (int i = 0; i < quadCount; i++) {
/*  34 */       int firstPosOffset = i * quadStride + positionOffset;
/*  35 */       int secondPosOffset = firstPosOffset + vertexStride * 2;
/*  36 */       float x0 = vertexBuffer.getFloat(firstPosOffset + 0);
/*  37 */       float y0 = vertexBuffer.getFloat(firstPosOffset + 4);
/*  38 */       float z0 = vertexBuffer.getFloat(firstPosOffset + 8);
/*  39 */       float x1 = vertexBuffer.getFloat(secondPosOffset + 0);
/*  40 */       float y1 = vertexBuffer.getFloat(secondPosOffset + 4);
/*  41 */       float z1 = vertexBuffer.getFloat(secondPosOffset + 8);
/*  42 */       float xMid = (x0 + x1) / 2.0F;
/*  43 */       float yMid = (y0 + y1) / 2.0F;
/*  44 */       float zMid = (z0 + z1) / 2.0F;
/*  45 */       output.set(outputIndex + i, xMid, yMid, zMid);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ByteBuffer vertexBuffer() {
/*  50 */     return this.vertexBuffer.byteBuffer();
/*     */   }
/*     */   
/*     */   public ByteBuffer indexBuffer() {
/*  54 */     return (this.indexBuffer != null) ? this.indexBuffer.byteBuffer() : null;
/*     */   }
/*     */   
/*     */   public ByteBufferBuilder.Result vertexBufferSlice() {
/*  58 */     return this.vertexBuffer;
/*     */   }
/*     */   
/*     */   public DrawState drawState() {
/*  62 */     return this.drawState;
/*     */   }
/*     */   
/*     */   public SortState sortQuads(ByteBufferBuilder indexBufferTarget, VertexSorting sorting) {
/*  66 */     if (this.drawState.primitiveTopology() != PrimitiveTopology.QUADS)
/*  67 */       return null; 
/*  69 */     CompactVectorArray centroids = new CompactVectorArray(this.drawState.vertexCount() / 4);
/*  70 */     decodeQuadCentroids(this.vertexBuffer.byteBuffer(), this.drawState.vertexCount(), this.drawState.format(), centroids, 0);
/*  71 */     SortState sortState = new SortState(centroids, this.drawState.indexType());
/*  72 */     this.indexBuffer = sortState.buildSortedIndexBuffer(indexBufferTarget, sorting);
/*  73 */     return sortState;
/*     */   }
/*     */   
/*     */   public void close() {
/*  78 */     this.vertexBuffer.close();
/*  79 */     if (this.indexBuffer != null)
/*  80 */       this.indexBuffer.close(); 
/*     */   }
/*     */   
/*     */   public static final class DrawState extends Record {
/*     */     private final VertexFormat format;
/*     */     
/*     */     private final int vertexCount;
/*     */     
/*     */     private final int indexCount;
/*     */     
/*     */     private final PrimitiveTopology primitiveTopology;
/*     */     
/*     */     private final IndexType indexType;
/*     */     
/*     */     public DrawState(VertexFormat format, int vertexCount, int indexCount, PrimitiveTopology primitiveTopology, IndexType indexType) {
/*  84 */       this.format = format;
/*  84 */       this.vertexCount = vertexCount;
/*  84 */       this.indexCount = indexCount;
/*  84 */       this.primitiveTopology = primitiveTopology;
/*  84 */       this.indexType = indexType;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/MeshData$DrawState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #84	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$DrawState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/MeshData$DrawState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #84	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$DrawState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/MeshData$DrawState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #84	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vertex/MeshData$DrawState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public VertexFormat format() {
/*  84 */       return this.format;
/*     */     }
/*     */     
/*     */     public int vertexCount() {
/*  84 */       return this.vertexCount;
/*     */     }
/*     */     
/*     */     public int indexCount() {
/*  84 */       return this.indexCount;
/*     */     }
/*     */     
/*     */     public PrimitiveTopology primitiveTopology() {
/*  84 */       return this.primitiveTopology;
/*     */     }
/*     */     
/*     */     public IndexType indexType() {
/*  84 */       return this.indexType;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class SortState extends Record {
/*     */     private final CompactVectorArray centroids;
/*     */     
/*     */     private final IndexType indexType;
/*     */     
/*     */     public SortState(CompactVectorArray centroids, IndexType indexType) {
/*  87 */       this.centroids = centroids;
/*  87 */       this.indexType = indexType;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/MeshData$SortState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/MeshData$SortState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/MeshData$SortState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public CompactVectorArray centroids() {
/*  87 */       return this.centroids;
/*     */     }
/*     */     
/*     */     public IndexType indexType() {
/*  87 */       return this.indexType;
/*     */     }
/*     */     
/*     */     public ByteBufferBuilder.Result buildSortedIndexBuffer(ByteBufferBuilder target, VertexSorting sorting) {
/*  92 */       int[] startIndices = sorting.sort(this.centroids);
/*  94 */       long pointer = target.reserve(startIndices.length * 6 * this.indexType.bytes);
/*  95 */       writeIndices(startIndices, indexWriter(pointer, this.indexType));
/*  97 */       return target.build();
/*     */     }
/*     */     
/*     */     public void writeSortedIndexBuffer(ByteBuffer target, VertexSorting sorting) {
/*     */       // Byte code:
/*     */       //   0: getstatic com/mojang/blaze3d/vertex/MeshData$1.$SwitchMap$com$mojang$blaze3d$IndexType : [I
/*     */       //   3: aload_0
/*     */       //   4: getfield indexType : Lcom/mojang/blaze3d/IndexType;
/*     */       //   7: invokevirtual ordinal : ()I
/*     */       //   10: iaload
/*     */       //   11: lookupswitch default -> 36, 1 -> 46, 2 -> 55
/*     */       //   36: new java/lang/MatchException
/*     */       //   39: dup
/*     */       //   40: aconst_null
/*     */       //   41: aconst_null
/*     */       //   42: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   45: athrow
/*     */       //   46: aload_1
/*     */       //   47: <illegal opcode> accept : (Ljava/nio/ByteBuffer;)Lit/unimi/dsi/fastutil/ints/IntConsumer;
/*     */       //   52: goto -> 66
/*     */       //   55: aload_1
/*     */       //   56: dup
/*     */       //   57: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   60: pop
/*     */       //   61: <illegal opcode> accept : (Ljava/nio/ByteBuffer;)Lit/unimi/dsi/fastutil/ints/IntConsumer;
/*     */       //   66: astore_3
/*     */       //   67: aload_2
/*     */       //   68: aload_0
/*     */       //   69: getfield centroids : Lcom/mojang/blaze3d/vertex/CompactVectorArray;
/*     */       //   72: invokeinterface sort : (Lcom/mojang/blaze3d/vertex/CompactVectorArray;)[I
/*     */       //   77: aload_3
/*     */       //   78: invokestatic writeIndices : ([ILit/unimi/dsi/fastutil/ints/IntConsumer;)V
/*     */       //   81: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #101	-> 0
/*     */       //   #102	-> 46
/*     */       //   #103	-> 55
/*     */       //   #101	-> 66
/*     */       //   #105	-> 67
/*     */       //   #106	-> 81
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	82	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState;
/*     */       //   0	82	1	target	Ljava/nio/ByteBuffer;
/*     */       //   0	82	2	sorting	Lcom/mojang/blaze3d/vertex/VertexSorting;
/*     */       //   67	15	3	output	Lit/unimi/dsi/fastutil/ints/IntConsumer;
/*     */     }
/*     */     
/*     */     private static void writeIndices(int[] startIndices, IntConsumer output) {
/* 109 */       for (int startIndex : startIndices) {
/* 110 */         output.accept(startIndex * 4 + 0);
/* 111 */         output.accept(startIndex * 4 + 1);
/* 112 */         output.accept(startIndex * 4 + 2);
/* 113 */         output.accept(startIndex * 4 + 2);
/* 114 */         output.accept(startIndex * 4 + 3);
/* 115 */         output.accept(startIndex * 4 + 0);
/*     */       } 
/*     */     }
/*     */     
/*     */     private IntConsumer indexWriter(long pointer, IndexType indexType) {
/*     */       // Byte code:
/*     */       //   0: new org/apache/commons/lang3/mutable/MutableLong
/*     */       //   3: dup
/*     */       //   4: lload_1
/*     */       //   5: invokespecial <init> : (J)V
/*     */       //   8: astore #4
/*     */       //   10: getstatic com/mojang/blaze3d/vertex/MeshData$1.$SwitchMap$com$mojang$blaze3d$IndexType : [I
/*     */       //   13: aload_3
/*     */       //   14: invokevirtual ordinal : ()I
/*     */       //   17: iaload
/*     */       //   18: lookupswitch default -> 44, 1 -> 54, 2 -> 64
/*     */       //   44: new java/lang/MatchException
/*     */       //   47: dup
/*     */       //   48: aconst_null
/*     */       //   49: aconst_null
/*     */       //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   53: athrow
/*     */       //   54: aload #4
/*     */       //   56: <illegal opcode> accept : (Lorg/apache/commons/lang3/mutable/MutableLong;)Lit/unimi/dsi/fastutil/ints/IntConsumer;
/*     */       //   61: goto -> 71
/*     */       //   64: aload #4
/*     */       //   66: <illegal opcode> accept : (Lorg/apache/commons/lang3/mutable/MutableLong;)Lit/unimi/dsi/fastutil/ints/IntConsumer;
/*     */       //   71: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #120	-> 0
/*     */       //   #121	-> 10
/*     */       //   #122	-> 54
/*     */       //   #123	-> 64
/*     */       //   #121	-> 71
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	72	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState;
/*     */       //   0	72	1	pointer	J
/*     */       //   0	72	3	indexType	Lcom/mojang/blaze3d/IndexType;
/*     */       //   10	62	4	nextIndex	Lorg/apache/commons/lang3/mutable/MutableLong;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\MeshData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */