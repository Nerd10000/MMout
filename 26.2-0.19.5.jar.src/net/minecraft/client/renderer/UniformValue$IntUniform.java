/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.Std140Builder;
/*    */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class IntUniform extends Record implements UniformValue {
/*    */   private final int value;
/*    */   
/*    */   public IntUniform(int value) {
/* 50 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$IntUniform;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$IntUniform;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$IntUniform;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$IntUniform;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$IntUniform;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$IntUniform;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int value() {
/* 50 */     return this.value;
/*    */   }
/*    */   
/* 51 */   public static final Codec<IntUniform> CODEC = Codec.INT.xmap(IntUniform::new, IntUniform::value);
/*    */   
/*    */   public void writeTo(Std140Builder builder) {
/* 55 */     builder.putInt(this.value);
/*    */   }
/*    */   
/*    */   public void addSize(Std140SizeCalculator calculator) {
/* 60 */     calculator.putInt();
/*    */   }
/*    */   
/*    */   public UniformValue.Type type() {
/* 65 */     return UniformValue.Type.INT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\UniformValue$IntUniform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */