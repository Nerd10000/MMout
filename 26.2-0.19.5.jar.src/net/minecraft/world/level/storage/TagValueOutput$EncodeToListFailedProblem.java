/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ public final class EncodeToListFailedProblem extends Record implements ProblemReporter.Problem {
/*     */   private final String name;
/*     */   
/*     */   private final Object value;
/*     */   
/*     */   private final DataResult.Error<?> error;
/*     */   
/*     */   public EncodeToListFailedProblem(String name, Object value, DataResult.Error<?> error) {
/* 218 */     this.name = name;
/* 218 */     this.value = value;
/* 218 */     this.error = error;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #218	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #218	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #218	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueOutput$EncodeToListFailedProblem;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/* 218 */     return this.name;
/*     */   }
/*     */   
/*     */   public Object value() {
/* 218 */     return this.value;
/*     */   }
/*     */   
/*     */   public DataResult.Error<?> error() {
/* 218 */     return this.error;
/*     */   }
/*     */   
/*     */   public String description() {
/* 221 */     return "Failed to append value '" + String.valueOf(this.value) + "' to list '" + this.name + "': " + this.error.message();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueOutput$EncodeToListFailedProblem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */