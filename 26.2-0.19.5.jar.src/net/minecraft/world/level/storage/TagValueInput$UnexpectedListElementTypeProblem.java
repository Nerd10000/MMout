/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import net.minecraft.nbt.TagType;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ public final class UnexpectedListElementTypeProblem extends Record implements ProblemReporter.Problem {
/*     */   private final String name;
/*     */   
/*     */   private final int index;
/*     */   
/*     */   private final TagType<?> expected;
/*     */   
/*     */   private final TagType<?> actual;
/*     */   
/*     */   public UnexpectedListElementTypeProblem(String name, int index, TagType<?> expected, TagType<?> actual) {
/* 439 */     this.name = name;
/* 439 */     this.index = index;
/* 439 */     this.expected = expected;
/* 439 */     this.actual = actual;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #439	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #439	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #439	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$UnexpectedListElementTypeProblem;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/* 439 */     return this.name;
/*     */   }
/*     */   
/*     */   public int index() {
/* 439 */     return this.index;
/*     */   }
/*     */   
/*     */   public TagType<?> expected() {
/* 439 */     return this.expected;
/*     */   }
/*     */   
/*     */   public TagType<?> actual() {
/* 439 */     return this.actual;
/*     */   }
/*     */   
/*     */   public String description() {
/* 442 */     return "Expected list '" + this.name + "' to contain at index " + this.index + " value of type " + this.expected.getName() + ", but got " + this.actual.getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueInput$UnexpectedListElementTypeProblem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */