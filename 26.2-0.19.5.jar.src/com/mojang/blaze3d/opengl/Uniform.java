/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ 
/*    */ public interface Uniform extends AutoCloseable {
/*    */   default void close() {}
/*    */   
/*    */   public static final class Ubo extends Record implements Uniform {
/*    */     private final int blockBinding;
/*    */     
/*    */     public Ubo(int blockBinding) {
/* 10 */       this.blockBinding = blockBinding;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/Uniform$Ubo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #10	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Ubo;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/Uniform$Ubo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #10	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Ubo;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/Uniform$Ubo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #10	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Ubo;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int blockBinding() {
/* 10 */       return this.blockBinding;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Utb extends Record implements Uniform {
/*    */     private final int location;
/*    */     
/*    */     private final int samplerIndex;
/*    */     
/*    */     private final GpuFormat format;
/*    */     
/*    */     private final int texture;
/*    */     
/*    */     public Utb(int location, int samplerIndex, GpuFormat format, int texture) {
/* 13 */       this.location = location;
/* 13 */       this.samplerIndex = samplerIndex;
/* 13 */       this.format = format;
/* 13 */       this.texture = texture;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/Uniform$Utb;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Utb;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/Uniform$Utb;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Utb;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/Uniform$Utb;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Utb;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int location() {
/* 13 */       return this.location;
/*    */     }
/*    */     
/*    */     public int samplerIndex() {
/* 13 */       return this.samplerIndex;
/*    */     }
/*    */     
/*    */     public GpuFormat format() {
/* 13 */       return this.format;
/*    */     }
/*    */     
/*    */     public int texture() {
/* 13 */       return this.texture;
/*    */     }
/*    */     
/*    */     public Utb(int location, int samplerIndex, GpuFormat format) {
/* 15 */       this(location, samplerIndex, format, GlStateManager._genTexture());
/*    */     }
/*    */     
/*    */     public void close() {
/* 20 */       GlStateManager._deleteTexture(this.texture);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Sampler extends Record implements Uniform {
/*    */     private final int location;
/*    */     
/*    */     private final int samplerIndex;
/*    */     
/*    */     public Sampler(int location, int samplerIndex) {
/* 24 */       this.location = location;
/* 24 */       this.samplerIndex = samplerIndex;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/Uniform$Sampler;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Sampler;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/Uniform$Sampler;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Sampler;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/Uniform$Sampler;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Sampler;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int location() {
/* 24 */       return this.location;
/*    */     }
/*    */     
/*    */     public int samplerIndex() {
/* 24 */       return this.samplerIndex;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\Uniform.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */