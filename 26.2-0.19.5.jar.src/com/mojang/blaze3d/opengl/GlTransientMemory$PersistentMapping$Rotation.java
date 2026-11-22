/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ final class Rotation extends Record implements Runnable {
/*     */   private final Runnable staging;
/*     */   
/*     */   private final Runnable gpu;
/*     */   
/*     */   private final Runnable gpuMapped;
/*     */   
/*     */   private Rotation(Runnable staging, Runnable gpu, Runnable gpuMapped) {
/* 225 */     this.staging = staging;
/* 225 */     this.gpu = gpu;
/* 225 */     this.gpuMapped = gpuMapped;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlTransientMemory$PersistentMapping$Rotation;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Runnable staging() {
/* 225 */     return this.staging;
/*     */   }
/*     */   
/*     */   public Runnable gpu() {
/* 225 */     return this.gpu;
/*     */   }
/*     */   
/*     */   public Runnable gpuMapped() {
/* 225 */     return this.gpuMapped;
/*     */   }
/*     */   
/*     */   public void run() {
/* 229 */     this.staging.run();
/* 230 */     this.gpu.run();
/* 231 */     this.gpuMapped.run();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlTransientMemory$PersistentMapping$Rotation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */