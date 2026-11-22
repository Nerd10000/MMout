/*   */ package net.minecraft.client.resources.metadata.animation;
/*   */ 
/*   */ public final class FrameSize extends Record {
/*   */   private final int width;
/*   */   
/*   */   private final int height;
/*   */   
/*   */   public FrameSize(int width, int height) {
/* 3 */     this.width = width;
/* 3 */     this.height = height;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/animation/FrameSize;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/FrameSize;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/animation/FrameSize;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/FrameSize;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/animation/FrameSize;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/animation/FrameSize;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public int width() {
/* 3 */     return this.width;
/*   */   }
/*   */   
/*   */   public int height() {
/* 3 */     return this.height;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\metadata\animation\FrameSize.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */