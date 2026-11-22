/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public final class TrialSpawnerConfig extends Record {
/*     */   private final int spawnRange;
/*     */   
/*     */   private final float totalMobs;
/*     */   
/*     */   private final float simultaneousMobs;
/*     */   
/*     */   private final float totalMobsAddedPerPlayer;
/*     */   
/*     */   private final float simultaneousMobsAddedPerPlayer;
/*     */   
/*     */   private final int ticksBetweenSpawn;
/*     */   
/*     */   private final WeightedList<SpawnData> spawnPotentialsDefinition;
/*     */   
/*     */   private final WeightedList<ResourceKey<LootTable>> lootTablesToEject;
/*     */   
/*     */   private final ResourceKey<LootTable> itemsToDropWhenOminous;
/*     */   
/*     */   public TrialSpawnerConfig(int spawnRange, float totalMobs, float simultaneousMobs, float totalMobsAddedPerPlayer, float simultaneousMobsAddedPerPlayer, int ticksBetweenSpawn, WeightedList<SpawnData> spawnPotentialsDefinition, WeightedList<ResourceKey<LootTable>> lootTablesToEject, ResourceKey<LootTable> itemsToDropWhenOminous) {
/*  22 */     this.spawnRange = spawnRange;
/*  22 */     this.totalMobs = totalMobs;
/*  22 */     this.simultaneousMobs = simultaneousMobs;
/*  22 */     this.totalMobsAddedPerPlayer = totalMobsAddedPerPlayer;
/*  22 */     this.simultaneousMobsAddedPerPlayer = simultaneousMobsAddedPerPlayer;
/*  22 */     this.ticksBetweenSpawn = ticksBetweenSpawn;
/*  22 */     this.spawnPotentialsDefinition = spawnPotentialsDefinition;
/*  22 */     this.lootTablesToEject = lootTablesToEject;
/*  22 */     this.itemsToDropWhenOminous = itemsToDropWhenOminous;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfig;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfig;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfig;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfig;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfig;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfig;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int spawnRange() {
/*  22 */     return this.spawnRange;
/*     */   }
/*     */   
/*     */   public float totalMobs() {
/*  22 */     return this.totalMobs;
/*     */   }
/*     */   
/*     */   public float simultaneousMobs() {
/*  22 */     return this.simultaneousMobs;
/*     */   }
/*     */   
/*     */   public float totalMobsAddedPerPlayer() {
/*  22 */     return this.totalMobsAddedPerPlayer;
/*     */   }
/*     */   
/*     */   public float simultaneousMobsAddedPerPlayer() {
/*  22 */     return this.simultaneousMobsAddedPerPlayer;
/*     */   }
/*     */   
/*     */   public int ticksBetweenSpawn() {
/*  22 */     return this.ticksBetweenSpawn;
/*     */   }
/*     */   
/*     */   public WeightedList<SpawnData> spawnPotentialsDefinition() {
/*  22 */     return this.spawnPotentialsDefinition;
/*     */   }
/*     */   
/*     */   public WeightedList<ResourceKey<LootTable>> lootTablesToEject() {
/*  22 */     return this.lootTablesToEject;
/*     */   }
/*     */   
/*     */   public ResourceKey<LootTable> itemsToDropWhenOminous() {
/*  22 */     return this.itemsToDropWhenOminous;
/*     */   }
/*     */   
/*  33 */   public static final TrialSpawnerConfig DEFAULT = builder().build();
/*     */   
/*     */   public static final Codec<TrialSpawnerConfig> DIRECT_CODEC;
/*     */   
/*     */   static {
/*  35 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.intRange(1, 128).optionalFieldOf("spawn_range", Integer.valueOf(DEFAULT.spawnRange)).forGetter(TrialSpawnerConfig::spawnRange), (App)Codec.floatRange(0.0F, Float.MAX_VALUE).optionalFieldOf("total_mobs", Float.valueOf(DEFAULT.totalMobs)).forGetter(TrialSpawnerConfig::totalMobs), (App)Codec.floatRange(0.0F, Float.MAX_VALUE).optionalFieldOf("simultaneous_mobs", Float.valueOf(DEFAULT.simultaneousMobs)).forGetter(TrialSpawnerConfig::simultaneousMobs), (App)Codec.floatRange(0.0F, Float.MAX_VALUE).optionalFieldOf("total_mobs_added_per_player", Float.valueOf(DEFAULT.totalMobsAddedPerPlayer)).forGetter(TrialSpawnerConfig::totalMobsAddedPerPlayer), (App)Codec.floatRange(0.0F, Float.MAX_VALUE).optionalFieldOf("simultaneous_mobs_added_per_player", Float.valueOf(DEFAULT.simultaneousMobsAddedPerPlayer)).forGetter(TrialSpawnerConfig::simultaneousMobsAddedPerPlayer), (App)Codec.intRange(0, 2147483647).optionalFieldOf("ticks_between_spawn", Integer.valueOf(DEFAULT.ticksBetweenSpawn)).forGetter(TrialSpawnerConfig::ticksBetweenSpawn), (App)SpawnData.LIST_CODEC.optionalFieldOf("spawn_potentials", WeightedList.of()).forGetter(TrialSpawnerConfig::spawnPotentialsDefinition), (App)WeightedList.codec(LootTable.KEY_CODEC).optionalFieldOf("loot_tables_to_eject", DEFAULT.lootTablesToEject).forGetter(TrialSpawnerConfig::lootTablesToEject), (App)LootTable.KEY_CODEC.optionalFieldOf("items_to_drop_when_ominous", DEFAULT.itemsToDropWhenOminous).forGetter(TrialSpawnerConfig::itemsToDropWhenOminous)).apply((Applicative)i, TrialSpawnerConfig::new));
/*     */   }
/*     */   
/*  47 */   public static final Codec<Holder<TrialSpawnerConfig>> CODEC = (Codec<Holder<TrialSpawnerConfig>>)RegistryFileCodec.create(Registries.TRIAL_SPAWNER_CONFIG, DIRECT_CODEC);
/*     */   
/*     */   public int calculateTargetTotalMobs(int additionalPlayers) {
/*  50 */     return (int)Math.floor((this.totalMobs + this.totalMobsAddedPerPlayer * additionalPlayers));
/*     */   }
/*     */   
/*     */   public int calculateTargetSimultaneousMobs(int additionalPlayers) {
/*  54 */     return (int)Math.floor((this.simultaneousMobs + this.simultaneousMobsAddedPerPlayer * additionalPlayers));
/*     */   }
/*     */   
/*     */   public long ticksBetweenItemSpawners() {
/*  58 */     return 160L;
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/*  62 */     return new Builder();
/*     */   }
/*     */   
/*     */   public TrialSpawnerConfig withSpawning(EntityType<?> type) {
/*  67 */     CompoundTag tag = new CompoundTag();
/*  68 */     tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
/*  69 */     SpawnData spawnData = new SpawnData(tag, Optional.empty(), Optional.empty());
/*  71 */     return new TrialSpawnerConfig(this.spawnRange, this.totalMobs, this.simultaneousMobs, this.totalMobsAddedPerPlayer, this.simultaneousMobsAddedPerPlayer, this.ticksBetweenSpawn, WeightedList.of(spawnData), this.lootTablesToEject, this.itemsToDropWhenOminous);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  75 */     private int spawnRange = 4;
/*     */     
/*  76 */     private float totalMobs = 6.0F;
/*     */     
/*  77 */     private float simultaneousMobs = 2.0F;
/*     */     
/*  78 */     private float totalMobsAddedPerPlayer = 2.0F;
/*     */     
/*  79 */     private float simultaneousMobsAddedPerPlayer = 1.0F;
/*     */     
/*  80 */     private int ticksBetweenSpawn = 40;
/*     */     
/*  81 */     private WeightedList<SpawnData> spawnPotentialsDefinition = WeightedList.of();
/*     */     
/*  82 */     private WeightedList<ResourceKey<LootTable>> lootTablesToEject = WeightedList.builder()
/*  83 */       .add(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES)
/*  84 */       .add(BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_KEY)
/*  85 */       .build();
/*     */     
/*  86 */     private ResourceKey<LootTable> itemsToDropWhenOminous = BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS;
/*     */     
/*     */     public Builder spawnRange(int spawnRange) {
/*  89 */       this.spawnRange = spawnRange;
/*  90 */       return this;
/*     */     }
/*     */     
/*     */     public Builder totalMobs(float totalMobs) {
/*  94 */       this.totalMobs = totalMobs;
/*  95 */       return this;
/*     */     }
/*     */     
/*     */     public Builder simultaneousMobs(float simultaneousMobs) {
/*  99 */       this.simultaneousMobs = simultaneousMobs;
/* 100 */       return this;
/*     */     }
/*     */     
/*     */     public Builder totalMobsAddedPerPlayer(float totalMobsAddedPerPlayer) {
/* 104 */       this.totalMobsAddedPerPlayer = totalMobsAddedPerPlayer;
/* 105 */       return this;
/*     */     }
/*     */     
/*     */     public Builder simultaneousMobsAddedPerPlayer(float simultaneousMobsAddedPerPlayer) {
/* 109 */       this.simultaneousMobsAddedPerPlayer = simultaneousMobsAddedPerPlayer;
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public Builder ticksBetweenSpawn(int ticksBetweenSpawn) {
/* 114 */       this.ticksBetweenSpawn = ticksBetweenSpawn;
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     public Builder spawnPotentialsDefinition(WeightedList<SpawnData> spawnPotentialsDefinition) {
/* 119 */       this.spawnPotentialsDefinition = spawnPotentialsDefinition;
/* 120 */       return this;
/*     */     }
/*     */     
/*     */     public Builder lootTablesToEject(WeightedList<ResourceKey<LootTable>> lootTablesToEject) {
/* 124 */       this.lootTablesToEject = lootTablesToEject;
/* 125 */       return this;
/*     */     }
/*     */     
/*     */     public Builder itemsToDropWhenOminous(ResourceKey<LootTable> itemsToDropWhenOminous) {
/* 129 */       this.itemsToDropWhenOminous = itemsToDropWhenOminous;
/* 130 */       return this;
/*     */     }
/*     */     
/*     */     public TrialSpawnerConfig build() {
/* 134 */       return new TrialSpawnerConfig(this.spawnRange, this.totalMobs, this.simultaneousMobs, this.totalMobsAddedPerPlayer, this.simultaneousMobsAddedPerPlayer, this.ticksBetweenSpawn, this.spawnPotentialsDefinition, this.lootTablesToEject, this.itemsToDropWhenOminous);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */