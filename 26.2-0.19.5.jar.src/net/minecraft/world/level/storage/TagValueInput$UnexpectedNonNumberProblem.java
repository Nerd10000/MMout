/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import net.minecraft.nbt.TagType;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ public final class UnexpectedNonNumberProblem extends Record implements ProblemReporter.Problem {
/*     */   private final String name;
/*     */   
/*     */   private final TagType<?> actual;
/*     */   
/*     */   public UnexpectedNonNumberProblem(String name, TagType<?> actual) {
/* 432 */     this.name = name;
/* 432 */     this.actual = actual;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #432	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #432	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #432	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedNonNumberProblem;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/* 432 */     return this.name;
/*     */   }
/*     */   
/*     */   public TagType<?> actual() {
/* 432 */     return this.actual;
/*     */   }
/*     */   
/*     */   public String description() {
/* 435 */     return "Expected field '" + this.name + "' to contain number, but got " + this.actual.getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueInput$UnexpectedNonNumberProblem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */