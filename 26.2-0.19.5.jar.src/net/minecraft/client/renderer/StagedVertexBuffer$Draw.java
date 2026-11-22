/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.PrimitiveTopology;
/*     */ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
/*     */ import com.mojang.blaze3d.vertex.MeshData;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexSorting;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Draw {
/*     */   private final VertexFormat format;
/*     */   
/*     */   private final PrimitiveTopology primitiveTopology;
/*     */   
/*     */   private final VertexSorting quadSorting;
/*     */   
/* 338 */   private final List<ByteBufferBuilder.Result> vertexBufferSlices = new ArrayList<>();
/*     */   
/*     */   private int vertexBufferSize;
/*     */   
/*     */   private int vertexCount;
/*     */   
/*     */   private int indexCount;
/*     */   
/*     */   private int vertexOffset;
/*     */   
/*     */   private int indexOffset;
/*     */   
/*     */   private Draw(VertexFormat format, PrimitiveTopology primitiveTopology, VertexSorting quadSorting) {
/* 348 */     this.format = format;
/* 349 */     this.primitiveTopology = primitiveTopology;
/* 350 */     this.quadSorting = quadSorting;
/*     */   }
/*     */   
/*     */   private void append(MeshData mesh) {
/* 354 */     assert mesh.indexBuffer() == null;
/* 355 */     this.vertexBufferSlices.add(mesh.vertexBufferSlice());
/* 356 */     this.vertexBufferSize += mesh.vertexBuffer().remaining();
/* 357 */     this.vertexCount += mesh.drawState().vertexCount();
/* 358 */     this.indexCount += mesh.drawState().indexCount();
/*     */   }
/*     */   
/*     */   private IndexType indexType() {
/* 362 */     return IndexType.least(this.vertexCount);
/*     */   }
/*     */   
/*     */   private void freeVertexData() {
/* 366 */     this.vertexBufferSlices.forEach(ByteBufferBuilder.Result::close);
/* 367 */     this.vertexBufferSlices.clear();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 371 */     return (this.vertexCount == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\StagedVertexBuffer$Draw.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */