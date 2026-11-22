/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import org.joml.Matrix4fc;
/*     */ 
/*     */ public final class Matrix4x4Uniform extends Record implements UniformValue {
/*     */   private final Matrix4fc value;
/*     */   
/*     */   public Matrix4x4Uniform(Matrix4fc value) {
/* 164 */     this.value = value;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #164	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #164	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #164	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$Matrix4x4Uniform;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Matrix4fc value() {
/* 164 */     return this.value;
/*     */   }
/*     */   
/* 165 */   public static final Codec<Matrix4x4Uniform> CODEC = ExtraCodecs.MATRIX4F.xmap(Matrix4x4Uniform::new, Matrix4x4Uniform::value);
/*     */   
/*     */   public void writeTo(Std140Builder builder) {
/* 169 */     builder.putMat4f(this.value);
/*     */   }
/*     */   
/*     */   public void addSize(Std140SizeCalculator calculator) {
/* 174 */     calculator.putMat4f();
/*     */   }
/*     */   
/*     */   public UniformValue.Type type() {
/* 179 */     return UniformValue.Type.MATRIX4X4;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\UniformValue$Matrix4x4Uniform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */