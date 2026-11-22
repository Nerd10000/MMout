/*   */ package com.mojang.blaze3d.vulkan.glsl;
/*   */ 
/*   */ final class SpvSampler extends Record {
/*   */   private final String name;
/*   */   
/*   */   private final int bindingOffset;
/*   */   
/*   */   private final int dimensions;
/*   */   
/*   */   public int dimensions() {
/* 5 */     return this.dimensions;
/*   */   }
/*   */   
/*   */   public int bindingOffset() {
/* 5 */     return this.bindingOffset;
/*   */   }
/*   */   
/*   */   public String name() {
/* 5 */     return this.name;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/glsl/SpvSampler;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/glsl/SpvSampler;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/glsl/SpvSampler;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/glsl/SpvSampler;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/glsl/SpvSampler;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/glsl/SpvSampler;
/*   */   }
/*   */   
/*   */   SpvSampler(String name, int bindingOffset, int dimensions) {
/* 6 */     this.name = name;
/* 6 */     this.bindingOffset = bindingOffset;
/* 6 */     this.dimensions = dimensions;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\glsl\SpvSampler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */