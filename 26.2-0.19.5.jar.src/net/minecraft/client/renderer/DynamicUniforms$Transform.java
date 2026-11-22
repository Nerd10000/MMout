/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.Std140Builder;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Vector3fc;
/*    */ import org.joml.Vector4fc;
/*    */ 
/*    */ public final class Transform extends Record implements DynamicUniformStorage.DynamicUniform {
/*    */   private final Matrix4fc modelView;
/*    */   
/*    */   private final Vector4fc colorModulator;
/*    */   
/*    */   private final Vector3fc modelOffset;
/*    */   
/*    */   private final Matrix4fc textureMatrix;
/*    */   
/*    */   public Transform(Matrix4fc modelView, Vector4fc colorModulator, Vector3fc modelOffset, Matrix4fc textureMatrix) {
/* 85 */     this.modelView = modelView;
/* 85 */     this.colorModulator = colorModulator;
/* 85 */     this.modelOffset = modelOffset;
/* 85 */     this.textureMatrix = textureMatrix;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/DynamicUniforms$Transform;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$Transform;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/DynamicUniforms$Transform;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$Transform;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/DynamicUniforms$Transform;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #85	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/DynamicUniforms$Transform;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Matrix4fc modelView() {
/* 85 */     return this.modelView;
/*    */   }
/*    */   
/*    */   public Vector4fc colorModulator() {
/* 85 */     return this.colorModulator;
/*    */   }
/*    */   
/*    */   public Vector3fc modelOffset() {
/* 85 */     return this.modelOffset;
/*    */   }
/*    */   
/*    */   public Matrix4fc textureMatrix() {
/* 85 */     return this.textureMatrix;
/*    */   }
/*    */   
/*    */   public void write(ByteBuffer buffer) {
/* 88 */     Std140Builder.intoBuffer(buffer)
/* 89 */       .putMat4f(this.modelView)
/* 90 */       .putVec4(this.colorModulator)
/* 91 */       .putVec3(this.modelOffset)
/* 92 */       .putMat4f(this.textureMatrix);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\DynamicUniforms$Transform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */