/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public final class Vec3Uniform extends Record implements UniformValue {
/*     */   private final Vector3fc value;
/*     */   
/*     */   public Vec3Uniform(Vector3fc value) {
/* 126 */     this.value = value;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #126	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #126	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #126	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$Vec3Uniform;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Vector3fc value() {
/* 126 */     return this.value;
/*     */   }
/*     */   
/* 127 */   public static final Codec<Vec3Uniform> CODEC = ExtraCodecs.VECTOR3F.xmap(Vec3Uniform::new, Vec3Uniform::value);
/*     */   
/*     */   public void writeTo(Std140Builder builder) {
/* 131 */     builder.putVec3(this.value);
/*     */   }
/*     */   
/*     */   public void addSize(Std140SizeCalculator calculator) {
/* 136 */     calculator.putVec3();
/*     */   }
/*     */   
/*     */   public UniformValue.Type type() {
/* 141 */     return UniformValue.Type.VEC3;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\UniformValue$Vec3Uniform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */