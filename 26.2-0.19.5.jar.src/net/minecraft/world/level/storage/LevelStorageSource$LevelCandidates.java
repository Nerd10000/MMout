/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class LevelCandidates extends Record implements Iterable<LevelStorageSource.LevelDirectory> {
/*     */   private final List<LevelStorageSource.LevelDirectory> levels;
/*     */   
/*     */   public LevelCandidates(List<LevelStorageSource.LevelDirectory> levels) {
/* 687 */     this.levels = levels;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #687	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #687	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #687	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public List<LevelStorageSource.LevelDirectory> levels() {
/* 687 */     return this.levels;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 689 */     return this.levels.isEmpty();
/*     */   }
/*     */   
/*     */   public Iterator<LevelStorageSource.LevelDirectory> iterator() {
/* 694 */     return this.levels.iterator();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelStorageSource$LevelCandidates.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */