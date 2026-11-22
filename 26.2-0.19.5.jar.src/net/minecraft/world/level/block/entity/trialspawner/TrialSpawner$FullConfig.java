/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ 
/*     */ public final class FullConfig extends Record {
/*     */   private final Holder<TrialSpawnerConfig> normal;
/*     */   
/*     */   private final Holder<TrialSpawnerConfig> ominous;
/*     */   
/*     */   private final int targetCooldownLength;
/*     */   
/*     */   private final int requiredPlayerRange;
/*     */   
/*     */   public static final MapCodec<FullConfig> MAP_CODEC;
/*     */   
/*     */   public FullConfig(Holder<TrialSpawnerConfig> normal, Holder<TrialSpawnerConfig> ominous, int targetCooldownLength, int requiredPlayerRange) {
/* 405 */     this.normal = normal;
/* 405 */     this.ominous = ominous;
/* 405 */     this.targetCooldownLength = targetCooldownLength;
/* 405 */     this.requiredPlayerRange = requiredPlayerRange;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #405	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #405	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #405	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawner$FullConfig;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<TrialSpawnerConfig> normal() {
/* 405 */     return this.normal;
/*     */   }
/*     */   
/*     */   public Holder<TrialSpawnerConfig> ominous() {
/* 405 */     return this.ominous;
/*     */   }
/*     */   
/*     */   public int targetCooldownLength() {
/* 405 */     return this.targetCooldownLength;
/*     */   }
/*     */   
/*     */   public int requiredPlayerRange() {
/* 405 */     return this.requiredPlayerRange;
/*     */   }
/*     */   
/*     */   static {
/* 411 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)TrialSpawnerConfig.CODEC.optionalFieldOf("normal_config", Holder.direct(TrialSpawnerConfig.DEFAULT)).forGetter(FullConfig::normal), (App)TrialSpawnerConfig.CODEC.optionalFieldOf("ominous_config", Holder.direct(TrialSpawnerConfig.DEFAULT)).forGetter(FullConfig::ominous), (App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("target_cooldown_length", Integer.valueOf(36000)).forGetter(FullConfig::targetCooldownLength), (App)Codec.intRange(1, 128).optionalFieldOf("required_player_range", Integer.valueOf(14)).forGetter(FullConfig::requiredPlayerRange)).apply((Applicative)i, FullConfig::new));
/*     */   }
/*     */   
/* 418 */   public static final FullConfig DEFAULT = new FullConfig(
/* 419 */       Holder.direct(TrialSpawnerConfig.DEFAULT), 
/* 420 */       Holder.direct(TrialSpawnerConfig.DEFAULT), 36000, 14);
/*     */   
/*     */   public FullConfig overrideEntity(EntityType<?> type) {
/* 426 */     return new FullConfig(
/* 427 */         Holder.direct(((TrialSpawnerConfig)this.normal.value()).withSpawning(type)), 
/* 428 */         Holder.direct(((TrialSpawnerConfig)this.ominous.value()).withSpawning(type)), this.targetCooldownLength, this.requiredPlayerRange);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawner$FullConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */