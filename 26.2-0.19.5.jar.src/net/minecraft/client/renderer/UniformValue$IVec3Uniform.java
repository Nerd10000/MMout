/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.Std140Builder;
/*    */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import org.joml.Vector3ic;
/*    */ 
/*    */ public final class IVec3Uniform extends Record implements UniformValue {
/*    */   private final Vector3ic value;
/*    */   
/*    */   public IVec3Uniform(Vector3ic value) {
/* 69 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/UniformValue$IVec3Uniform;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Vector3ic value() {
/* 69 */     return this.value;
/*    */   }
/*    */   
/* 70 */   public static final Codec<IVec3Uniform> CODEC = ExtraCodecs.VECTOR3I.xmap(IVec3Uniform::new, IVec3Uniform::value);
/*    */   
/*    */   public void writeTo(Std140Builder builder) {
/* 74 */     builder.putIVec3(this.value);
/*    */   }
/*    */   
/*    */   public void addSize(Std140SizeCalculator calculator) {
/* 79 */     calculator.putIVec3();
/*    */   }
/*    */   
/*    */   public UniformValue.Type type() {
/* 84 */     return UniformValue.Type.IVEC3;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\UniformValue$IVec3Uniform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */