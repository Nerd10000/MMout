/*     */ package net.minecraft.data.loot;
/*     */ 
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public final class MissingTableProblem extends Record implements ProblemReporter.Problem {
/*     */   private final ResourceKey<LootTable> id;
/*     */   
/*     */   public MissingTableProblem(ResourceKey<LootTable> id) {
/* 115 */     this.id = id;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #115	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/data/loot/LootTableProvider$MissingTableProblem;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ResourceKey<LootTable> id() {
/* 115 */     return this.id;
/*     */   }
/*     */   
/*     */   public String description() {
/* 118 */     return "Missing built-in table: " + String.valueOf(this.id.identifier());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\LootTableProvider$MissingTableProblem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */