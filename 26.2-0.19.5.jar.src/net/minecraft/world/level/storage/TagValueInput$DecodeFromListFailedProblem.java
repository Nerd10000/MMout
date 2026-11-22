/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.mojang.serialization.DataResult;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ public final class DecodeFromListFailedProblem extends Record implements ProblemReporter.Problem {
/*     */   private final String name;
/*     */   
/*     */   private final int index;
/*     */   
/*     */   private final Tag tag;
/*     */   
/*     */   private final DataResult.Error<?> error;
/*     */   
/*     */   public DecodeFromListFailedProblem(String name, int index, Tag tag, DataResult.Error<?> error) {
/* 411 */     this.name = name;
/* 411 */     this.index = index;
/* 411 */     this.tag = tag;
/* 411 */     this.error = error;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #411	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #411	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #411	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/TagValueInput$DecodeFromListFailedProblem;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/* 411 */     return this.name;
/*     */   }
/*     */   
/*     */   public int index() {
/* 411 */     return this.index;
/*     */   }
/*     */   
/*     */   public Tag tag() {
/* 411 */     return this.tag;
/*     */   }
/*     */   
/*     */   public DataResult.Error<?> error() {
/* 411 */     return this.error;
/*     */   }
/*     */   
/*     */   public String description() {
/* 414 */     return "Failed to decode value '" + String.valueOf(this.tag) + "' from field '" + this.name + "' at index " + this.index + "': " + this.error.message();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueInput$DecodeFromListFailedProblem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */