/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ final class ChunkPathElement extends Record implements ProblemReporter.PathElement {
/*     */   private final ChunkPos pos;
/*     */   
/*     */   private ChunkPathElement(ChunkPos pos) {
/* 478 */     this.pos = pos;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #478	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #478	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #478	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/ChunkAccess$ChunkPathElement;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ChunkPos pos() {
/* 478 */     return this.pos;
/*     */   }
/*     */   
/*     */   public String get() {
/* 481 */     return "chunk@" + String.valueOf(this.pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\ChunkAccess$ChunkPathElement.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */