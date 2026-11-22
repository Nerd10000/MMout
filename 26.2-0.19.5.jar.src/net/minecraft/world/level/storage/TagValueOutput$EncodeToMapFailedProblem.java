/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ public final class EncodeToMapFailedProblem extends Record implements ProblemReporter.Problem {
/*     */   private final Object value;
/*     */   
/*     */   private final DataResult.Error<?> error;
/*     */   
/*     */   public EncodeToMapFailedProblem(Object value, DataResult.Error<?> error) {
/* 225 */     this.value = value;
/* 225 */     this.error = error;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #225	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToMapFailedProblem;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Object value() {
/* 225 */     return this.value;
/*     */   }
/*     */   
/*     */   public DataResult.Error<?> error() {
/* 225 */     return this.error;
/*     */   }
/*     */   
/*     */   public String description() {
/* 228 */     return "Failed to merge value '" + String.valueOf(this.value) + "' to an object: " + this.error.message();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueOutput$EncodeToMapFailedProblem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */