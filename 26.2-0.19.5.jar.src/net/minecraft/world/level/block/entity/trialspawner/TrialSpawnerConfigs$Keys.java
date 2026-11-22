/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ 
/*     */ final class Keys extends Record {
/*     */   private final ResourceKey<TrialSpawnerConfig> normal;
/*     */   
/*     */   private final ResourceKey<TrialSpawnerConfig> ominous;
/*     */   
/*     */   private Keys(ResourceKey<TrialSpawnerConfig> normal, ResourceKey<TrialSpawnerConfig> ominous) {
/* 368 */     this.normal = normal;
/* 368 */     this.ominous = ominous;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #368	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #368	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #368	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ResourceKey<TrialSpawnerConfig> normal() {
/* 368 */     return this.normal;
/*     */   }
/*     */   
/*     */   public ResourceKey<TrialSpawnerConfig> ominous() {
/* 368 */     return this.ominous;
/*     */   }
/*     */   
/*     */   public static Keys of(String id) {
/* 370 */     return new Keys(TrialSpawnerConfigs.registryKey(id + "/normal"), TrialSpawnerConfigs.registryKey(id + "/ominous"));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerConfigs$Keys.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */