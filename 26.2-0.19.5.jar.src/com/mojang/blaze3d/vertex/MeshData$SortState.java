/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import it.unimi.dsi.fastutil.ints.IntConsumer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.lang3.mutable.MutableLong;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public final class SortState extends Record {
/*     */   private final CompactVectorArray centroids;
/*     */   
/*     */   private final IndexType indexType;
/*     */   
/*     */   public SortState(CompactVectorArray centroids, IndexType indexType) {
/*  87 */     this.centroids = centroids;
/*  87 */     this.indexType = indexType;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/MeshData$SortState;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/MeshData$SortState;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/MeshData$SortState;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #87	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public CompactVectorArray centroids() {
/*  87 */     return this.centroids;
/*     */   }
/*     */   
/*     */   public IndexType indexType() {
/*  87 */     return this.indexType;
/*     */   }
/*     */   
/*     */   public ByteBufferBuilder.Result buildSortedIndexBuffer(ByteBufferBuilder target, VertexSorting sorting) {
/*  92 */     int[] startIndices = sorting.sort(this.centroids);
/*  94 */     long pointer = target.reserve(startIndices.length * 6 * this.indexType.bytes);
/*  95 */     writeIndices(startIndices, indexWriter(pointer, this.indexType));
/*  97 */     return target.build();
/*     */   }
/*     */   
/*     */   public void writeSortedIndexBuffer(ByteBuffer target, VertexSorting sorting) {
/* 101 */     switch (MeshData.null.$SwitchMap$com$mojang$blaze3d$IndexType[this.indexType.ordinal()]) {
/*     */       default:
/* 101 */         throw new MatchException(null, null);
/*     */       case 1:
/*     */       
/*     */       case 2:
/* 103 */         Objects.requireNonNull(target);
/*     */         break;
/*     */     } 
/* 103 */     IntConsumer output = target::putInt;
/* 105 */     writeIndices(sorting.sort(this.centroids), output);
/*     */   }
/*     */   
/*     */   private static void writeIndices(int[] startIndices, IntConsumer output) {
/* 109 */     for (int startIndex : startIndices) {
/* 110 */       output.accept(startIndex * 4 + 0);
/* 111 */       output.accept(startIndex * 4 + 1);
/* 112 */       output.accept(startIndex * 4 + 2);
/* 113 */       output.accept(startIndex * 4 + 2);
/* 114 */       output.accept(startIndex * 4 + 3);
/* 115 */       output.accept(startIndex * 4 + 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private IntConsumer indexWriter(long pointer, IndexType indexType) {
/* 120 */     MutableLong nextIndex = new MutableLong(pointer);
/* 121 */     switch (MeshData.null.$SwitchMap$com$mojang$blaze3d$IndexType[indexType.ordinal()]) {
/*     */       default:
/* 121 */         throw new MatchException(null, null);
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */         break;
/*     */     } 
/* 121 */     return value -> MemoryUtil.memPutInt(nextIndex.getAndAdd(4L), value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\MeshData$SortState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */