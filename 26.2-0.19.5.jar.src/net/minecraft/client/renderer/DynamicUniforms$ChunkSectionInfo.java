/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public final class ChunkSectionInfo extends Record implements DynamicUniformStorage.DynamicUniform {
/*     */   private final Matrix4fc modelView;
/*     */   
/*     */   private final int x;
/*     */   
/*     */   private final int y;
/*     */   
/*     */   private final int z;
/*     */   
/*     */   private final float visibility;
/*     */   
/*     */   private final int textureAtlasWidth;
/*     */   
/*     */   private final int textureAtlasHeight;
/*     */   
/*     */   public ChunkSectionInfo(Matrix4fc modelView, int x, int y, int z, float visibility, int textureAtlasWidth, int textureAtlasHeight) {
/*  96 */     this.modelView = modelView;
/*  96 */     this.x = x;
/*  96 */     this.y = y;
/*  96 */     this.z = z;
/*  96 */     this.visibility = visibility;
/*  96 */     this.textureAtlasWidth = textureAtlasWidth;
/*  96 */     this.textureAtlasHeight = textureAtlasHeight;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #96	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #96	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #96	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$ChunkSectionInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Matrix4fc modelView() {
/*  96 */     return this.modelView;
/*     */   }
/*     */   
/*     */   public int x() {
/*  96 */     return this.x;
/*     */   }
/*     */   
/*     */   public int y() {
/*  96 */     return this.y;
/*     */   }
/*     */   
/*     */   public int z() {
/*  96 */     return this.z;
/*     */   }
/*     */   
/*     */   public float visibility() {
/*  96 */     return this.visibility;
/*     */   }
/*     */   
/*     */   public int textureAtlasWidth() {
/*  96 */     return this.textureAtlasWidth;
/*     */   }
/*     */   
/*     */   public int textureAtlasHeight() {
/*  96 */     return this.textureAtlasHeight;
/*     */   }
/*     */   
/*     */   public void write(ByteBuffer buffer) {
/*  99 */     Std140Builder.intoBuffer(buffer)
/* 100 */       .putMat4f(this.modelView)
/* 101 */       .putFloat(this.visibility)
/* 102 */       .putIVec2(this.textureAtlasWidth, this.textureAtlasHeight)
/* 103 */       .putIVec3(this.x, this.y, this.z);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\DynamicUniforms$ChunkSectionInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */