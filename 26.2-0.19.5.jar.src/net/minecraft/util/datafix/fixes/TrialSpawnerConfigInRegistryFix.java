/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.TagParser;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TrialSpawnerConfigInRegistryFix extends NamedEntityFix {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public TrialSpawnerConfigInRegistryFix(Schema outputSchema) {
/*  26 */     super(outputSchema, false, "TrialSpawnerConfigInRegistryFix", References.BLOCK_ENTITY, "minecraft:trial_spawner");
/*     */   }
/*     */   
/*     */   public Dynamic<?> fixTag(Dynamic<Tag> input) {
/*  30 */     Optional<Dynamic<Tag>> normalConfig = input.get("normal_config").result();
/*  31 */     if (normalConfig.isEmpty())
/*  32 */       return input; 
/*  35 */     Optional<Dynamic<Tag>> ominousConfig = input.get("ominous_config").result();
/*  36 */     if (ominousConfig.isEmpty())
/*  37 */       return input; 
/*  40 */     Identifier registryLocation = VanillaTrialChambers.CONFIGS_TO_KEY.get(Pair.of(normalConfig.get(), ominousConfig.get()));
/*  42 */     if (registryLocation == null)
/*  43 */       return input; 
/*  46 */     return input
/*  47 */       .set("normal_config", input.createString(registryLocation.withSuffix("/normal").toString()))
/*  48 */       .set("ominous_config", input.createString(registryLocation.withSuffix("/ominous").toString()));
/*     */   }
/*     */   
/*     */   protected Typed<?> fix(Typed<?> entity) {
/*  53 */     return entity.update(DSL.remainderFinder(), input -> {
/*     */           DynamicOps<?> inputType = input.getOps();
/*     */           Dynamic<?> result = fixTag(input.convert((DynamicOps)NbtOps.INSTANCE));
/*     */           return result.convert(inputType);
/*     */         });
/*     */   }
/*     */   
/*     */   private static final class VanillaTrialChambers {
/*  62 */     public static final Map<Pair<Dynamic<Tag>, Dynamic<Tag>>, Identifier> CONFIGS_TO_KEY = new HashMap<>();
/*     */     
/*     */     static {
/*  65 */       register(Identifier.withDefaultNamespace("trial_chamber/breeze"), "{simultaneous_mobs: 1.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:breeze\"}}, weight: 1}], ticks_between_spawn: 20, total_mobs: 2.0f, total_mobs_added_per_player: 1.0f}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], simultaneous_mobs: 2.0f, total_mobs: 4.0f}");
/*  72 */       register(Identifier.withDefaultNamespace("trial_chamber/melee/husk"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:husk\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {id: \"minecraft:husk\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_melee\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*  78 */       register(Identifier.withDefaultNamespace("trial_chamber/melee/spider"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:spider\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}],simultaneous_mobs: 4.0f, total_mobs: 12.0f}");
/*  84 */       register(Identifier.withDefaultNamespace("trial_chamber/melee/zombie"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:zombie\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}],spawn_potentials: [{data: {entity: {id: \"minecraft:zombie\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_melee\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*  90 */       register(Identifier.withDefaultNamespace("trial_chamber/ranged/poison_skeleton"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:bogged\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}],spawn_potentials: [{data: {entity: {id: \"minecraft:bogged\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/*  96 */       register(Identifier.withDefaultNamespace("trial_chamber/ranged/skeleton"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:skeleton\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {id: \"minecraft:skeleton\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/* 102 */       register(Identifier.withDefaultNamespace("trial_chamber/ranged/stray"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:stray\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {id: \"minecraft:stray\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/* 108 */       register(Identifier.withDefaultNamespace("trial_chamber/slow_ranged/poison_skeleton"), "{simultaneous_mobs: 4.0f, simultaneous_mobs_added_per_player: 2.0f, spawn_potentials: [{data: {entity: {id: \"minecraft:bogged\"}}, weight: 1}], ticks_between_spawn: 160}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {id: \"minecraft:bogged\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/* 114 */       register(Identifier.withDefaultNamespace("trial_chamber/slow_ranged/skeleton"), "{simultaneous_mobs: 4.0f, simultaneous_mobs_added_per_player: 2.0f, spawn_potentials: [{data: {entity: {id: \"minecraft:skeleton\"}}, weight: 1}], ticks_between_spawn: 160}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {id: \"minecraft:skeleton\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/* 120 */       register(Identifier.withDefaultNamespace("trial_chamber/slow_ranged/stray"), "{simultaneous_mobs: 4.0f, simultaneous_mobs_added_per_player: 2.0f, spawn_potentials: [{data: {entity: {id: \"minecraft:stray\"}}, weight: 1}], ticks_between_spawn: 160}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}],spawn_potentials: [{data: {entity: {id: \"minecraft:stray\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_ranged\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/* 126 */       register(Identifier.withDefaultNamespace("trial_chamber/small_melee/baby_zombie"), "{simultaneous_mobs: 2.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {IsBaby: 1b, id: \"minecraft:zombie\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], spawn_potentials: [{data: {entity: {IsBaby: 1b, id: \"minecraft:zombie\"}, equipment: {loot_table: \"minecraft:equipment/trial_chamber_melee\", slot_drop_chances: 0.0f}}, weight: 1}]}");
/* 134 */       register(Identifier.withDefaultNamespace("trial_chamber/small_melee/cave_spider"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:cave_spider\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], simultaneous_mobs: 4.0f, total_mobs: 12.0f}");
/* 141 */       register(Identifier.withDefaultNamespace("trial_chamber/small_melee/silverfish"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {id: \"minecraft:silverfish\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], simultaneous_mobs: 4.0f, total_mobs: 12.0f}");
/* 148 */       register(Identifier.withDefaultNamespace("trial_chamber/small_melee/slime"), "{simultaneous_mobs: 3.0f, simultaneous_mobs_added_per_player: 0.5f, spawn_potentials: [{data: {entity: {Size: 1, id: \"minecraft:slime\"}}, weight: 3}, {data: {entity: {Size: 2, id: \"minecraft:slime\"}}, weight: 1}], ticks_between_spawn: 20}", "{loot_tables_to_eject: [{data: \"minecraft:spawners/ominous/trial_chamber/key\", weight: 3}, {data: \"minecraft:spawners/ominous/trial_chamber/consumables\", weight: 7}], simultaneous_mobs: 4.0f, total_mobs: 12.0f}");
/*     */     }
/*     */     
/*     */     private static void register(Identifier location, String normalNbt, String ominousNbt) {
/*     */       try {
/* 158 */         CompoundTag normalTag = parse(normalNbt);
/* 159 */         CompoundTag ominousTag = parse(ominousNbt);
/* 161 */         CompoundTag ominousMergedTag = normalTag.copy().merge(ominousTag);
/* 162 */         CompoundTag ominousMergedTagDefaultsOmitted = removeDefaults(ominousMergedTag.copy());
/* 164 */         Dynamic<Tag> dynamicNormal = asDynamic(normalTag);
/* 165 */         CONFIGS_TO_KEY.put(Pair.of(dynamicNormal, asDynamic(ominousTag)), location);
/* 166 */         CONFIGS_TO_KEY.put(Pair.of(dynamicNormal, asDynamic(ominousMergedTag)), location);
/* 167 */         CONFIGS_TO_KEY.put(Pair.of(dynamicNormal, asDynamic(ominousMergedTagDefaultsOmitted)), location);
/* 168 */       } catch (RuntimeException e) {
/* 169 */         throw new IllegalStateException("Failed to parse NBT for " + String.valueOf(location), e);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static Dynamic<Tag> asDynamic(CompoundTag normalTag) {
/* 174 */       return new Dynamic((DynamicOps)NbtOps.INSTANCE, normalTag);
/*     */     }
/*     */     
/*     */     private static CompoundTag parse(String nbt) {
/*     */       try {
/* 179 */         return TagParser.parseCompoundFully(nbt);
/* 180 */       } catch (CommandSyntaxException e) {
/* 181 */         throw new IllegalArgumentException("Failed to parse Trial Spawner NBT config: " + nbt, e);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static CompoundTag removeDefaults(CompoundTag tag) {
/* 188 */       if (tag.getIntOr("spawn_range", 0) == 4)
/* 189 */         tag.remove("spawn_range"); 
/* 192 */       if (tag.getFloatOr("total_mobs", 0.0F) == 6.0F)
/* 193 */         tag.remove("total_mobs"); 
/* 196 */       if (tag.getFloatOr("simultaneous_mobs", 0.0F) == 2.0F)
/* 197 */         tag.remove("simultaneous_mobs"); 
/* 200 */       if (tag.getFloatOr("total_mobs_added_per_player", 0.0F) == 2.0F)
/* 201 */         tag.remove("total_mobs_added_per_player"); 
/* 204 */       if (tag.getFloatOr("simultaneous_mobs_added_per_player", 0.0F) == 1.0F)
/* 205 */         tag.remove("simultaneous_mobs_added_per_player"); 
/* 208 */       if (tag.getIntOr("ticks_between_spawn", 0) == 40)
/* 209 */         tag.remove("ticks_between_spawn"); 
/* 217 */       return tag;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\TrialSpawnerConfigInRegistryFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */