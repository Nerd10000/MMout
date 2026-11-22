/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class DynamicUniforms implements AutoCloseable {
/*  16 */   private static final Vector4fc WHITE = (Vector4fc)new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   
/*  17 */   private static final Vector3fc NO_OFFSET = (Vector3fc)new Vector3f();
/*     */   
/*  18 */   private static final Matrix4fc IDENTITY_TEXTURE_TRANSFORM = (Matrix4fc)new Matrix4f();
/*     */   
/*  20 */   public static final int TRANSFORM_UBO_SIZE = (new Std140SizeCalculator())
/*  21 */     .putMat4f()
/*  22 */     .putVec4()
/*  23 */     .putVec3()
/*  24 */     .putMat4f()
/*  25 */     .get();
/*     */   
/*  26 */   public static final int CHUNK_SECTION_UBO_SIZE = (new Std140SizeCalculator())
/*  27 */     .putMat4f()
/*  28 */     .putFloat()
/*  29 */     .putIVec2()
/*  30 */     .putIVec3()
/*  31 */     .get();
/*     */   
/*     */   private static final int INITIAL_CAPACITY = 2;
/*     */   
/*  38 */   private final DynamicUniformStorage<Transform> transforms = new DynamicUniformStorage<>("Dynamic Transforms UBO", TRANSFORM_UBO_SIZE, 2);
/*     */   
/*  39 */   private final DynamicUniformStorage<ChunkSectionInfo> chunkSections = new DynamicUniformStorage<>("Chunk Sections UBO", CHUNK_SECTION_UBO_SIZE, 2);
/*     */   
/*     */   public void reset() {
/*  43 */     this.transforms.endFrame();
/*  44 */     this.chunkSections.endFrame();
/*     */   }
/*     */   
/*     */   public void close() {
/*  49 */     this.transforms.close();
/*  50 */     this.chunkSections.close();
/*     */   }
/*     */   
/*     */   public GpuBufferSlice writeTransform(Matrix4f modelView) {
/*  55 */     return writeTransform(new Transform((Matrix4fc)modelView, WHITE, NO_OFFSET, IDENTITY_TEXTURE_TRANSFORM));
/*     */   }
/*     */   
/*     */   public GpuBufferSlice writeTransform(Matrix4f modelView, Vector4f colorModulator) {
/*  60 */     return writeTransform(new Transform((Matrix4fc)modelView, (Vector4fc)colorModulator, NO_OFFSET, IDENTITY_TEXTURE_TRANSFORM));
/*     */   }
/*     */   
/*     */   public GpuBufferSlice writeTransform(Matrix4f modelView, Matrix4f textureMatrix) {
/*  65 */     return writeTransform(new Transform((Matrix4fc)modelView, WHITE, NO_OFFSET, (Matrix4fc)textureMatrix));
/*     */   }
/*     */   
/*     */   public GpuBufferSlice writeTransform(Matrix4f modelView, Vector4f colorModulator, Vector3f modelOffset, Matrix4f textureMatrix) {
/*  70 */     return writeTransform(new Transform((Matrix4fc)modelView, (Vector4fc)colorModulator, (Vector3fc)modelOffset, (Matrix4fc)textureMatrix));
/*     */   }
/*     */   
/*     */   public GpuBufferSlice writeTransform(Transform uniform) {
/*  74 */     return this.transforms.writeUniform(uniform);
/*     */   }
/*     */   
/*     */   public GpuBufferSlice[] writeTransforms(Transform... transforms) {
/*  78 */     return this.transforms.writeUniforms(transforms);
/*     */   }
/*     */   
/*     */   public GpuBufferSlice[] writeChunkSections(ChunkSectionInfo... infos) {
/*  82 */     return this.chunkSections.writeUniforms(infos);
/*     */   }
/*     */   
/*     */   public static final class Transform extends Record implements DynamicUniformStorage.DynamicUniform {
/*     */     private final Matrix4fc modelView;
/*     */     
/*     */     private final Vector4fc colorModulator;
/*     */     
/*     */     private final Vector3fc modelOffset;
/*     */     
/*     */     private final Matrix4fc textureMatrix;
/*     */     
/*     */     public Transform(Matrix4fc modelView, Vector4fc colorModulator, Vector3fc modelOffset, Matrix4fc textureMatrix) {
/*  85 */       this.modelView = modelView;
/*  85 */       this.colorModulator = colorModulator;
/*  85 */       this.modelOffset = modelOffset;
/*  85 */       this.textureMatrix = textureMatrix;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/DynamicUniforms$Transform;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$Transform;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/DynamicUniforms$Transform;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$Transform;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/DynamicUniforms$Transform;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$Transform;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Matrix4fc modelView() {
/*  85 */       return this.modelView;
/*     */     }
/*     */     
/*     */     public Vector4fc colorModulator() {
/*  85 */       return this.colorModulator;
/*     */     }
/*     */     
/*     */     public Vector3fc modelOffset() {
/*  85 */       return this.modelOffset;
/*     */     }
/*     */     
/*     */     public Matrix4fc textureMatrix() {
/*  85 */       return this.textureMatrix;
/*     */     }
/*     */     
/*     */     public void write(ByteBuffer buffer) {
/*  88 */       Std140Builder.intoBuffer(buffer)
/*  89 */         .putMat4f(this.modelView)
/*  90 */         .putVec4(this.colorModulator)
/*  91 */         .putVec3(this.modelOffset)
/*  92 */         .putMat4f(this.textureMatrix);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ChunkSectionInfo extends Record implements DynamicUniformStorage.DynamicUniform {
/*     */     private final Matrix4fc modelView;
/*     */     
/*     */     private final int x;
/*     */     
/*     */     private final int y;
/*     */     
/*     */     private final int z;
/*     */     
/*     */     private final float visibility;
/*     */     
/*     */     private final int textureAtlasWidth;
/*     */     
/*     */     private final int textureAtlasHeight;
/*     */     
/*     */     public ChunkSectionInfo(Matrix4fc modelView, int x, int y, int z, float visibility, int textureAtlasWidth, int textureAtlasHeight) {
/*  96 */       this.modelView = modelView;
/*  96 */       this.x = x;
/*  96 */       this.y = y;
/*  96 */       this.z = z;
/*  96 */       this.visibility = visibility;
/*  96 */       this.textureAtlasWidth = textureAtlasWidth;
/*  96 */       this.textureAtlasHeight = textureAtlasHeight;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #96	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Matrix4fc modelView() {
/*  96 */       return this.modelView;
/*     */     }
/*     */     
/*     */     public int x() {
/*  96 */       return this.x;
/*     */     }
/*     */     
/*     */     public int y() {
/*  96 */       return this.y;
/*     */     }
/*     */     
/*     */     public int z() {
/*  96 */       return this.z;
/*     */     }
/*     */     
/*     */     public float visibility() {
/*  96 */       return this.visibility;
/*     */     }
/*     */     
/*     */     public int textureAtlasWidth() {
/*  96 */       return this.textureAtlasWidth;
/*     */     }
/*     */     
/*     */     public int textureAtlasHeight() {
/*  96 */       return this.textureAtlasHeight;
/*     */     }
/*     */     
/*     */     public void write(ByteBuffer buffer) {
/*  99 */       Std140Builder.intoBuffer(buffer)
/* 100 */         .putMat4f(this.modelView)
/* 101 */         .putFloat(this.visibility)
/* 102 */         .putIVec2(this.textureAtlasWidth, this.textureAtlasHeight)
/* 103 */         .putIVec3(this.x, this.y, this.z);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\DynamicUniforms.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */