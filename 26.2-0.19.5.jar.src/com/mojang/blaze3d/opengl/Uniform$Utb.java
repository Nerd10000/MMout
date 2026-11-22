/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ 
/*    */ public final class Utb extends Record implements Uniform {
/*    */   private final int location;
/*    */   
/*    */   private final int samplerIndex;
/*    */   
/*    */   private final GpuFormat format;
/*    */   
/*    */   private final int texture;
/*    */   
/*    */   public Utb(int location, int samplerIndex, GpuFormat format, int texture) {
/* 13 */     this.location = location;
/* 13 */     this.samplerIndex = samplerIndex;
/* 13 */     this.format = format;
/* 13 */     this.texture = texture;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/Uniform$Utb;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Utb;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/Uniform$Utb;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Utb;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/Uniform$Utb;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Utb;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int location() {
/* 13 */     return this.location;
/*    */   }
/*    */   
/*    */   public int samplerIndex() {
/* 13 */     return this.samplerIndex;
/*    */   }
/*    */   
/*    */   public GpuFormat format() {
/* 13 */     return this.format;
/*    */   }
/*    */   
/*    */   public int texture() {
/* 13 */     return this.texture;
/*    */   }
/*    */   
/*    */   public Utb(int location, int samplerIndex, GpuFormat format) {
/* 15 */     this(location, samplerIndex, format, GlStateManager._genTexture());
/*    */   }
/*    */   
/*    */   public void close() {
/* 20 */     GlStateManager._deleteTexture(this.texture);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\Uniform$Utb.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */