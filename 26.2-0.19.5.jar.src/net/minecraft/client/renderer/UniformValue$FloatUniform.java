/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public final class FloatUniform extends Record implements UniformValue {
/*     */   private final float value;
/*     */   
/*     */   public FloatUniform(float value) {
/*  88 */     this.value = value;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$FloatUniform;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$FloatUniform;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$FloatUniform;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$FloatUniform;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$FloatUniform;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$FloatUniform;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public float value() {
/*  88 */     return this.value;
/*     */   }
/*     */   
/*  89 */   public static final Codec<FloatUniform> CODEC = Codec.FLOAT.xmap(FloatUniform::new, FloatUniform::value);
/*     */   
/*     */   public void writeTo(Std140Builder builder) {
/*  93 */     builder.putFloat(this.value);
/*     */   }
/*     */   
/*     */   public void addSize(Std140SizeCalculator calculator) {
/*  98 */     calculator.putFloat();
/*     */   }
/*     */   
/*     */   public UniformValue.Type type() {
/* 103 */     return UniformValue.Type.FLOAT;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\UniformValue$FloatUniform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */