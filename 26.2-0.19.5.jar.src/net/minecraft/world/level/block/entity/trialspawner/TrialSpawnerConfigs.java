/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentTable;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class TrialSpawnerConfigs {
/*  24 */   private static final Keys TRIAL_CHAMBER_BREEZE = Keys.of("trial_chamber/breeze");
/*     */   
/*  25 */   private static final Keys TRIAL_CHAMBER_MELEE_HUSK = Keys.of("trial_chamber/melee/husk");
/*     */   
/*  26 */   private static final Keys TRIAL_CHAMBER_MELEE_SPIDER = Keys.of("trial_chamber/melee/spider");
/*     */   
/*  27 */   private static final Keys TRIAL_CHAMBER_MELEE_ZOMBIE = Keys.of("trial_chamber/melee/zombie");
/*     */   
/*  28 */   private static final Keys TRIAL_CHAMBER_RANGED_POISON_SKELETON = Keys.of("trial_chamber/ranged/poison_skeleton");
/*     */   
/*  29 */   private static final Keys TRIAL_CHAMBER_RANGED_SKELETON = Keys.of("trial_chamber/ranged/skeleton");
/*     */   
/*  30 */   private static final Keys TRIAL_CHAMBER_RANGED_STRAY = Keys.of("trial_chamber/ranged/stray");
/*     */   
/*  31 */   private static final Keys TRIAL_CHAMBER_SLOW_RANGED_POISON_SKELETON = Keys.of("trial_chamber/slow_ranged/poison_skeleton");
/*     */   
/*  32 */   private static final Keys TRIAL_CHAMBER_SLOW_RANGED_SKELETON = Keys.of("trial_chamber/slow_ranged/skeleton");
/*     */   
/*  33 */   private static final Keys TRIAL_CHAMBER_SLOW_RANGED_STRAY = Keys.of("trial_chamber/slow_ranged/stray");
/*     */   
/*  34 */   private static final Keys TRIAL_CHAMBER_SMALL_MELEE_BABY_ZOMBIE = Keys.of("trial_chamber/small_melee/baby_zombie");
/*     */   
/*  35 */   private static final Keys TRIAL_CHAMBER_SMALL_MELEE_CAVE_SPIDER = Keys.of("trial_chamber/small_melee/cave_spider");
/*     */   
/*  36 */   private static final Keys TRIAL_CHAMBER_SMALL_MELEE_SILVERFISH = Keys.of("trial_chamber/small_melee/silverfish");
/*     */   
/*  37 */   private static final Keys TRIAL_CHAMBER_SMALL_MELEE_SLIME = Keys.of("trial_chamber/small_melee/slime");
/*     */   
/*     */   public static void bootstrap(BootstrapContext<TrialSpawnerConfig> context) {
/*  40 */     register(context, TRIAL_CHAMBER_BREEZE, 
/*  41 */         TrialSpawnerConfig.builder()
/*  42 */         .simultaneousMobs(1.0F)
/*  43 */         .simultaneousMobsAddedPerPlayer(0.5F)
/*  44 */         .ticksBetweenSpawn(20)
/*  45 */         .totalMobs(2.0F)
/*  46 */         .totalMobsAddedPerPlayer(1.0F)
/*  47 */         .spawnPotentialsDefinition(
/*  48 */           WeightedList.of(spawnData(EntityTypes.BREEZE)))
/*     */         
/*  50 */         .build(), 
/*     */         
/*  52 */         TrialSpawnerConfig.builder()
/*  53 */         .simultaneousMobsAddedPerPlayer(0.5F)
/*  54 */         .ticksBetweenSpawn(20)
/*  55 */         .totalMobs(4.0F)
/*  56 */         .totalMobsAddedPerPlayer(1.0F)
/*  57 */         .spawnPotentialsDefinition(
/*  58 */           WeightedList.of(spawnData(EntityTypes.BREEZE)))
/*     */         
/*  60 */         .lootTablesToEject(
/*  61 */           WeightedList.builder()
/*  62 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/*  63 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/*  64 */           .build())
/*     */         
/*  66 */         .build());
/*  69 */     register(context, TRIAL_CHAMBER_MELEE_HUSK, 
/*  70 */         trialChamberBase()
/*  71 */         .spawnPotentialsDefinition(
/*  72 */           WeightedList.of(spawnData(EntityTypes.HUSK)))
/*     */         
/*  74 */         .build(), 
/*     */         
/*  76 */         trialChamberBase()
/*  77 */         .spawnPotentialsDefinition(
/*  78 */           WeightedList.of(spawnDataWithEquipment(EntityTypes.HUSK, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_MELEE)))
/*     */         
/*  80 */         .lootTablesToEject(
/*  81 */           WeightedList.builder()
/*  82 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/*  83 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/*  84 */           .build())
/*     */         
/*  86 */         .build());
/*  89 */     register(context, TRIAL_CHAMBER_MELEE_SPIDER, 
/*  90 */         trialChamberBase()
/*  91 */         .spawnPotentialsDefinition(
/*  92 */           WeightedList.of(spawnData(EntityTypes.SPIDER)))
/*     */         
/*  94 */         .build(), 
/*     */         
/*  96 */         trialChamberMeleeOminous()
/*  97 */         .spawnPotentialsDefinition(
/*  98 */           WeightedList.of(spawnData(EntityTypes.SPIDER)))
/*     */         
/* 100 */         .lootTablesToEject(
/* 101 */           WeightedList.builder()
/* 102 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 103 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 104 */           .build())
/*     */         
/* 106 */         .build());
/* 109 */     register(context, TRIAL_CHAMBER_MELEE_ZOMBIE, 
/* 110 */         trialChamberBase()
/* 111 */         .spawnPotentialsDefinition(
/* 112 */           WeightedList.of(spawnData(EntityTypes.ZOMBIE)))
/*     */         
/* 114 */         .build(), 
/*     */         
/* 116 */         trialChamberBase()
/* 117 */         .lootTablesToEject(
/* 118 */           WeightedList.builder()
/* 119 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 120 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 121 */           .build())
/*     */         
/* 123 */         .spawnPotentialsDefinition(
/* 124 */           WeightedList.of(spawnDataWithEquipment(EntityTypes.ZOMBIE, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_MELEE)))
/*     */         
/* 126 */         .build());
/* 129 */     register(context, TRIAL_CHAMBER_RANGED_POISON_SKELETON, 
/* 130 */         trialChamberBase()
/* 131 */         .spawnPotentialsDefinition(
/* 132 */           WeightedList.of(spawnData(EntityTypes.BOGGED)))
/*     */         
/* 134 */         .build(), 
/*     */         
/* 136 */         trialChamberBase()
/* 137 */         .lootTablesToEject(
/* 138 */           WeightedList.builder()
/* 139 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 140 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 141 */           .build())
/*     */         
/* 143 */         .spawnPotentialsDefinition(
/* 144 */           WeightedList.of(spawnDataWithEquipment(EntityTypes.BOGGED, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 146 */         .build());
/* 149 */     register(context, TRIAL_CHAMBER_RANGED_SKELETON, 
/* 150 */         trialChamberBase()
/* 151 */         .spawnPotentialsDefinition(
/* 152 */           WeightedList.of(spawnData(EntityTypes.SKELETON)))
/*     */         
/* 154 */         .build(), 
/*     */         
/* 156 */         trialChamberBase()
/* 157 */         .lootTablesToEject(
/* 158 */           WeightedList.builder()
/* 159 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 160 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 161 */           .build())
/*     */         
/* 163 */         .spawnPotentialsDefinition(
/* 164 */           WeightedList.of(spawnDataWithEquipment(EntityTypes.SKELETON, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 166 */         .build());
/* 169 */     register(context, TRIAL_CHAMBER_RANGED_STRAY, 
/* 170 */         trialChamberBase()
/* 171 */         .spawnPotentialsDefinition(
/* 172 */           WeightedList.of(spawnData(EntityTypes.STRAY)))
/*     */         
/* 174 */         .build(), 
/*     */         
/* 176 */         trialChamberBase()
/* 177 */         .lootTablesToEject(
/* 178 */           WeightedList.builder()
/* 179 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 180 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 181 */           .build())
/*     */         
/* 183 */         .spawnPotentialsDefinition(
/* 184 */           WeightedList.of(spawnDataWithEquipment(EntityTypes.STRAY, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 186 */         .build());
/* 189 */     register(context, TRIAL_CHAMBER_SLOW_RANGED_POISON_SKELETON, 
/* 190 */         trialChamberSlowRanged()
/* 191 */         .spawnPotentialsDefinition(
/* 192 */           WeightedList.of(spawnData(EntityTypes.BOGGED)))
/*     */         
/* 194 */         .build(), 
/*     */         
/* 196 */         trialChamberSlowRanged()
/* 197 */         .lootTablesToEject(
/* 198 */           WeightedList.builder()
/* 199 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 200 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 201 */           .build())
/*     */         
/* 203 */         .spawnPotentialsDefinition(
/* 204 */           WeightedList.of(spawnDataWithEquipment(EntityTypes.BOGGED, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 206 */         .build());
/* 209 */     register(context, TRIAL_CHAMBER_SLOW_RANGED_SKELETON, 
/* 210 */         trialChamberSlowRanged()
/* 211 */         .spawnPotentialsDefinition(
/* 212 */           WeightedList.of(spawnData(EntityTypes.SKELETON)))
/*     */         
/* 214 */         .build(), 
/*     */         
/* 216 */         trialChamberSlowRanged()
/* 217 */         .lootTablesToEject(
/* 218 */           WeightedList.builder()
/* 219 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 220 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 221 */           .build())
/*     */         
/* 223 */         .spawnPotentialsDefinition(
/* 224 */           WeightedList.of(spawnDataWithEquipment(EntityTypes.SKELETON, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 226 */         .build());
/* 229 */     register(context, TRIAL_CHAMBER_SLOW_RANGED_STRAY, 
/* 230 */         trialChamberSlowRanged()
/* 231 */         .spawnPotentialsDefinition(
/* 232 */           WeightedList.of(spawnData(EntityTypes.STRAY)))
/*     */         
/* 234 */         .build(), 
/*     */         
/* 236 */         trialChamberSlowRanged()
/* 237 */         .lootTablesToEject(
/* 238 */           WeightedList.builder()
/* 239 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 240 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 241 */           .build())
/*     */         
/* 243 */         .spawnPotentialsDefinition(
/* 244 */           WeightedList.of(spawnDataWithEquipment(EntityTypes.STRAY, BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_RANGED)))
/*     */         
/* 246 */         .build());
/* 249 */     register(context, TRIAL_CHAMBER_SMALL_MELEE_BABY_ZOMBIE, 
/* 250 */         TrialSpawnerConfig.builder()
/* 251 */         .simultaneousMobsAddedPerPlayer(0.5F)
/* 252 */         .ticksBetweenSpawn(20)
/* 253 */         .spawnPotentialsDefinition(
/* 254 */           WeightedList.of(customSpawnDataWithEquipment(EntityTypes.ZOMBIE, tag -> tag.putBoolean("IsBaby", true), null)))
/*     */         
/* 256 */         .build(), 
/*     */         
/* 258 */         TrialSpawnerConfig.builder()
/* 259 */         .simultaneousMobsAddedPerPlayer(0.5F)
/* 260 */         .ticksBetweenSpawn(20)
/* 261 */         .lootTablesToEject(
/* 262 */           WeightedList.builder()
/* 263 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 264 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 265 */           .build())
/*     */         
/* 267 */         .spawnPotentialsDefinition(
/* 268 */           WeightedList.of(customSpawnDataWithEquipment(EntityTypes.ZOMBIE, tag -> tag.putBoolean("IsBaby", true), BuiltInLootTables.EQUIPMENT_TRIAL_CHAMBER_MELEE)))
/*     */         
/* 270 */         .build());
/* 272 */     register(context, TRIAL_CHAMBER_SMALL_MELEE_CAVE_SPIDER, 
/* 273 */         trialChamberBase()
/* 274 */         .spawnPotentialsDefinition(
/* 275 */           WeightedList.of(spawnData(EntityTypes.CAVE_SPIDER)))
/*     */         
/* 277 */         .build(), 
/*     */         
/* 279 */         trialChamberMeleeOminous()
/* 280 */         .lootTablesToEject(
/* 281 */           WeightedList.builder()
/* 282 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 283 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 284 */           .build())
/*     */         
/* 286 */         .spawnPotentialsDefinition(
/* 287 */           WeightedList.of(spawnData(EntityTypes.CAVE_SPIDER)))
/*     */         
/* 289 */         .build());
/* 292 */     register(context, TRIAL_CHAMBER_SMALL_MELEE_SILVERFISH, 
/* 293 */         trialChamberBase()
/* 294 */         .spawnPotentialsDefinition(
/* 295 */           WeightedList.of(spawnData(EntityTypes.SILVERFISH)))
/*     */         
/* 297 */         .build(), 
/*     */         
/* 299 */         trialChamberMeleeOminous()
/* 300 */         .lootTablesToEject(
/* 301 */           WeightedList.builder()
/* 302 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 303 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 304 */           .build())
/*     */         
/* 306 */         .spawnPotentialsDefinition(
/* 307 */           WeightedList.of(spawnData(EntityTypes.SILVERFISH)))
/*     */         
/* 309 */         .build());
/* 312 */     register(context, TRIAL_CHAMBER_SMALL_MELEE_SLIME, 
/* 313 */         trialChamberBase()
/* 314 */         .spawnPotentialsDefinition(
/* 315 */           WeightedList.builder()
/* 316 */           .add(customSpawnData(EntityTypes.SLIME, tag -> tag.putByte("Size", (byte)1)), 3)
/* 317 */           .add(customSpawnData(EntityTypes.SLIME, tag -> tag.putByte("Size", (byte)2)), 1)
/* 318 */           .build())
/*     */         
/* 320 */         .build(), 
/*     */         
/* 322 */         trialChamberMeleeOminous()
/* 323 */         .lootTablesToEject(
/* 324 */           WeightedList.builder()
/* 325 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY, 3)
/* 326 */           .add(BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES, 7)
/* 327 */           .build())
/*     */         
/* 329 */         .spawnPotentialsDefinition(
/* 330 */           WeightedList.builder()
/* 331 */           .add(customSpawnData(EntityTypes.SLIME, tag -> tag.putByte("Size", (byte)1)), 3)
/* 332 */           .add(customSpawnData(EntityTypes.SLIME, tag -> tag.putByte("Size", (byte)2)), 1)
/* 333 */           .build())
/*     */         
/* 335 */         .build());
/*     */   }
/*     */   
/*     */   private static <T extends net.minecraft.world.entity.Entity> SpawnData spawnData(EntityType<T> type) {
/* 340 */     return customSpawnDataWithEquipment(type, tag -> {
/*     */         
/* 340 */         }null);
/*     */   }
/*     */   
/*     */   private static <T extends net.minecraft.world.entity.Entity> SpawnData customSpawnData(EntityType<T> type, Consumer<CompoundTag> tagModifier) {
/* 344 */     return customSpawnDataWithEquipment(type, tagModifier, null);
/*     */   }
/*     */   
/*     */   private static <T extends net.minecraft.world.entity.Entity> SpawnData spawnDataWithEquipment(EntityType<T> type, ResourceKey<LootTable> equipmentLootTable) {
/* 348 */     return customSpawnDataWithEquipment(type, tag -> {
/*     */         
/* 348 */         }equipmentLootTable);
/*     */   }
/*     */   
/*     */   private static <T extends net.minecraft.world.entity.Entity> SpawnData customSpawnDataWithEquipment(EntityType<T> type, Consumer<CompoundTag> tagModifier, ResourceKey<LootTable> equipmentLootTable) {
/* 352 */     CompoundTag tag = new CompoundTag();
/* 353 */     tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(type).toString());
/* 354 */     tagModifier.accept(tag);
/* 355 */     Optional<EquipmentTable> table = Optional.<ResourceKey<LootTable>>ofNullable(equipmentLootTable).map(lootTable -> new EquipmentTable(lootTable, 0.0F));
/* 356 */     return new SpawnData(tag, Optional.empty(), table);
/*     */   }
/*     */   
/*     */   private static void register(BootstrapContext<TrialSpawnerConfig> context, Keys keys, TrialSpawnerConfig normalConfig, TrialSpawnerConfig ominousConfig) {
/* 360 */     context.register(keys.normal, normalConfig);
/* 361 */     context.register(keys.ominous, ominousConfig);
/*     */   }
/*     */   
/*     */   private static ResourceKey<TrialSpawnerConfig> registryKey(String id) {
/* 365 */     return ResourceKey.create(Registries.TRIAL_SPAWNER_CONFIG, Identifier.withDefaultNamespace(id));
/*     */   }
/*     */   
/*     */   private static final class Keys extends Record {
/*     */     private final ResourceKey<TrialSpawnerConfig> normal;
/*     */     
/*     */     private final ResourceKey<TrialSpawnerConfig> ominous;
/*     */     
/*     */     private Keys(ResourceKey<TrialSpawnerConfig> normal, ResourceKey<TrialSpawnerConfig> ominous) {
/* 368 */       this.normal = normal;
/* 368 */       this.ominous = ominous;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #368	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #368	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #368	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerConfigs$Keys;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResourceKey<TrialSpawnerConfig> normal() {
/* 368 */       return this.normal;
/*     */     }
/*     */     
/*     */     public ResourceKey<TrialSpawnerConfig> ominous() {
/* 368 */       return this.ominous;
/*     */     }
/*     */     
/*     */     public static Keys of(String id) {
/* 370 */       return new Keys(TrialSpawnerConfigs.registryKey(id + "/normal"), TrialSpawnerConfigs.registryKey(id + "/ominous"));
/*     */     }
/*     */   }
/*     */   
/*     */   private static TrialSpawnerConfig.Builder trialChamberMeleeOminous() {
/* 377 */     return TrialSpawnerConfig.builder()
/* 378 */       .simultaneousMobs(4.0F)
/* 379 */       .simultaneousMobsAddedPerPlayer(0.5F)
/* 380 */       .ticksBetweenSpawn(20)
/* 381 */       .totalMobs(12.0F);
/*     */   }
/*     */   
/*     */   private static TrialSpawnerConfig.Builder trialChamberSlowRanged() {
/* 385 */     return TrialSpawnerConfig.builder()
/* 386 */       .simultaneousMobs(4.0F)
/* 387 */       .simultaneousMobsAddedPerPlayer(2.0F)
/* 388 */       .ticksBetweenSpawn(160);
/*     */   }
/*     */   
/*     */   private static TrialSpawnerConfig.Builder trialChamberBase() {
/* 392 */     return TrialSpawnerConfig.builder()
/* 393 */       .simultaneousMobs(3.0F)
/* 394 */       .simultaneousMobsAddedPerPlayer(0.5F)
/* 395 */       .ticksBetweenSpawn(20);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerConfigs.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */