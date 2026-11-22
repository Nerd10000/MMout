/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import net.minecraft.nbt.TagType;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ public final class UnexpectedTypeProblem extends Record implements ProblemReporter.Problem {
/*     */   private final String name;
/*     */   
/*     */   private final TagType<?> expected;
/*     */   
/*     */   private final TagType<?> actual;
/*     */   
/*     */   public UnexpectedTypeProblem(String name, TagType<?> expected, TagType<?> actual) {
/* 425 */     this.name = name;
/* 425 */     this.expected = expected;
/* 425 */     this.actual = actual;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #425	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #425	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #425	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedTypeProblem;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/* 425 */     return this.name;
/*     */   }
/*     */   
/*     */   public TagType<?> expected() {
/* 425 */     return this.expected;
/*     */   }
/*     */   
/*     */   public TagType<?> actual() {
/* 425 */     return this.actual;
/*     */   }
/*     */   
/*     */   public String description() {
/* 428 */     return "Expected field '" + this.name + "' to contain value of type " + this.expected.getName() + ", but got " + this.actual.getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueInput$UnexpectedTypeProblem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */