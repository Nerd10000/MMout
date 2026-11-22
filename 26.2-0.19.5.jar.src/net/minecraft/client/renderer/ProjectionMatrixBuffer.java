/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.buffers.Std140Builder;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ 
/*    */ public class ProjectionMatrixBuffer implements AutoCloseable {
/*    */   private final GpuBuffer buffer;
/*    */   
/*    */   private final GpuBufferSlice bufferSlice;
/*    */   
/*    */   private Projection lastUploadedProjection;
/*    */   
/*    */   private long projectionMatrixVersion;
/*    */   
/* 21 */   private final Matrix4f tempMatrix = new Matrix4f();
/*    */   
/*    */   public ProjectionMatrixBuffer(String name) {
/* 24 */     this.lastUploadedProjection = null;
/* 25 */     this.projectionMatrixVersion = -1L;
/* 26 */     GpuDevice device = RenderSystem.getDevice();
/* 27 */     this.buffer = device.createBuffer(() -> "Camera projection matrix UBO " + name, 136, RenderSystem.PROJECTION_MATRIX_UBO_SIZE);
/* 28 */     this.bufferSlice = this.buffer.slice(0L, RenderSystem.PROJECTION_MATRIX_UBO_SIZE);
/*    */   }
/*    */   
/*    */   public GpuBufferSlice getBuffer(Projection projection) {
/* 32 */     assert projection.getMatrixVersion() != -1L;
/* 33 */     if (this.lastUploadedProjection != projection || projection.getMatrixVersion() != this.projectionMatrixVersion) {
/* 34 */       this.lastUploadedProjection = projection;
/* 35 */       this.projectionMatrixVersion = projection.getMatrixVersion();
/* 36 */       return writeBuffer(projection.getMatrix(this.tempMatrix));
/*    */     } 
/* 38 */     return this.bufferSlice;
/*    */   }
/*    */   
/*    */   public GpuBufferSlice getBuffer(Matrix4f projectionMatrix) {
/* 42 */     this.lastUploadedProjection = null;
/* 43 */     this.projectionMatrixVersion = -1L;
/* 44 */     return writeBuffer(projectionMatrix);
/*    */   }
/*    */   
/*    */   private GpuBufferSlice writeBuffer(Matrix4f projectionMatrix) {
/* 48 */     MemoryStack stack = MemoryStack.stackPush();
/*    */     try {
/* 49 */       ByteBuffer byteBuffer = Std140Builder.onStack(stack, RenderSystem.PROJECTION_MATRIX_UBO_SIZE).putMat4f((Matrix4fc)projectionMatrix).get();
/* 50 */       RenderSystem.getDevice().createCommandEncoder().writeToBuffer(this.buffer.slice(), byteBuffer);
/* 51 */       if (stack != null)
/* 51 */         stack.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (stack != null)
/*    */         try {
/*    */           stack.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/* 52 */     return this.bufferSlice;
/*    */   }
/*    */   
/*    */   public void close() {
/* 57 */     this.buffer.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ProjectionMatrixBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */