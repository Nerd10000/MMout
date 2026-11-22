/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ public final class DecodeFromMapFailedProblem extends Record implements ProblemReporter.Problem {
/*     */   private final DataResult.Error<?> error;
/*     */   
/*     */   public DecodeFromMapFailedProblem(DataResult.Error<?> error) {
/* 418 */     this.error = error;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #418	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #418	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #418	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromMapFailedProblem;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public DataResult.Error<?> error() {
/* 418 */     return this.error;
/*     */   }
/*     */   
/*     */   public String description() {
/* 421 */     return "Failed to decode from map: " + this.error.message();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueInput$DecodeFromMapFailedProblem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */