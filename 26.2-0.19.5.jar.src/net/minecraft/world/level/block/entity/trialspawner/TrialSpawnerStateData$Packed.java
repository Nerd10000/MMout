/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public final class Packed extends Record {
/*     */   private final Set<UUID> detectedPlayers;
/*     */   
/*     */   private final Set<UUID> currentMobs;
/*     */   
/*     */   private final long cooldownEndsAt;
/*     */   
/*     */   private final long nextMobSpawnsAt;
/*     */   
/*     */   private final int totalMobsSpawned;
/*     */   
/*     */   private final Optional<SpawnData> nextSpawnData;
/*     */   
/*     */   private final Optional<ResourceKey<LootTable>> ejectingLootTable;
/*     */   
/*     */   public static final MapCodec<Packed> MAP_CODEC;
/*     */   
/*     */   public Packed(Set<UUID> detectedPlayers, Set<UUID> currentMobs, long cooldownEndsAt, long nextMobSpawnsAt, int totalMobsSpawned, Optional<SpawnData> nextSpawnData, Optional<ResourceKey<LootTable>> ejectingLootTable) {
/* 340 */     this.detectedPlayers = detectedPlayers;
/* 340 */     this.currentMobs = currentMobs;
/* 340 */     this.cooldownEndsAt = cooldownEndsAt;
/* 340 */     this.nextMobSpawnsAt = nextMobSpawnsAt;
/* 340 */     this.totalMobsSpawned = totalMobsSpawned;
/* 340 */     this.nextSpawnData = nextSpawnData;
/* 340 */     this.ejectingLootTable = ejectingLootTable;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #340	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #340	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #340	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Set<UUID> detectedPlayers() {
/* 340 */     return this.detectedPlayers;
/*     */   }
/*     */   
/*     */   public Set<UUID> currentMobs() {
/* 340 */     return this.currentMobs;
/*     */   }
/*     */   
/*     */   public long cooldownEndsAt() {
/* 340 */     return this.cooldownEndsAt;
/*     */   }
/*     */   
/*     */   public long nextMobSpawnsAt() {
/* 340 */     return this.nextMobSpawnsAt;
/*     */   }
/*     */   
/*     */   public int totalMobsSpawned() {
/* 340 */     return this.totalMobsSpawned;
/*     */   }
/*     */   
/*     */   public Optional<SpawnData> nextSpawnData() {
/* 340 */     return this.nextSpawnData;
/*     */   }
/*     */   
/*     */   public Optional<ResourceKey<LootTable>> ejectingLootTable() {
/* 340 */     return this.ejectingLootTable;
/*     */   }
/*     */   
/*     */   static {
/* 349 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)UUIDUtil.CODEC_SET.lenientOptionalFieldOf("registered_players", Set.of()).forGetter(Packed::detectedPlayers), (App)UUIDUtil.CODEC_SET.lenientOptionalFieldOf("current_mobs", Set.of()).forGetter(Packed::currentMobs), (App)Codec.LONG.lenientOptionalFieldOf("cooldown_ends_at", Long.valueOf(0L)).forGetter(Packed::cooldownEndsAt), (App)Codec.LONG.lenientOptionalFieldOf("next_mob_spawns_at", Long.valueOf(0L)).forGetter(Packed::nextMobSpawnsAt), (App)Codec.intRange(0, 2147483647).lenientOptionalFieldOf("total_mobs_spawned", Integer.valueOf(0)).forGetter(Packed::totalMobsSpawned), (App)SpawnData.CODEC.lenientOptionalFieldOf("spawn_data").forGetter(Packed::nextSpawnData), (App)LootTable.KEY_CODEC.lenientOptionalFieldOf("ejecting_loot_table").forGetter(Packed::ejectingLootTable)).apply((Applicative)i, Packed::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerStateData$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */