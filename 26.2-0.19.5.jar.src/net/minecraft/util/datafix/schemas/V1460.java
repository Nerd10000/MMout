/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ 
/*     */ public class V1460 extends NamespacedSchema {
/*     */   public V1460(int versionKey, Schema parent) {
/*  83 */     super(versionKey, parent);
/*     */   }
/*     */   
/*     */   protected static void registerMob(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/*  87 */     schema.registerSimple(map, name);
/*     */   }
/*     */   
/*     */   protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/*  91 */     schema.register(map, name, () -> V1458.nameableInventory(schema));
/*     */   }
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/*  96 */     Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
/*  98 */     schema.register(map, "minecraft:area_effect_cloud", name -> DSL.optionalFields("Particle", References.PARTICLE.in(schema)));
/* 101 */     registerMob(schema, map, "minecraft:armor_stand");
/* 102 */     schema.register(map, "minecraft:arrow", name -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema)));
/* 105 */     registerMob(schema, map, "minecraft:bat");
/* 106 */     registerMob(schema, map, "minecraft:blaze");
/* 107 */     schema.registerSimple(map, "minecraft:boat");
/* 108 */     registerMob(schema, map, "minecraft:cave_spider");
/* 109 */     schema.register(map, "minecraft:chest_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
/* 113 */     registerMob(schema, map, "minecraft:chicken");
/* 114 */     schema.register(map, "minecraft:commandblock_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "LastOutput", References.TEXT_COMPONENT.in(schema)));
/* 118 */     registerMob(schema, map, "minecraft:cow");
/* 119 */     registerMob(schema, map, "minecraft:creeper");
/* 120 */     schema.register(map, "minecraft:donkey", name -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema)));
/* 124 */     schema.registerSimple(map, "minecraft:dragon_fireball");
/* 125 */     schema.registerSimple(map, "minecraft:egg");
/* 126 */     registerMob(schema, map, "minecraft:elder_guardian");
/* 127 */     schema.registerSimple(map, "minecraft:ender_crystal");
/* 128 */     registerMob(schema, map, "minecraft:ender_dragon");
/* 129 */     schema.register(map, "minecraft:enderman", name -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(schema)));
/* 132 */     registerMob(schema, map, "minecraft:endermite");
/* 133 */     schema.registerSimple(map, "minecraft:ender_pearl");
/* 134 */     schema.registerSimple(map, "minecraft:evocation_fangs");
/* 135 */     registerMob(schema, map, "minecraft:evocation_illager");
/* 136 */     schema.registerSimple(map, "minecraft:eye_of_ender_signal");
/* 137 */     schema.register(map, "minecraft:falling_block", name -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema)));
/* 141 */     schema.registerSimple(map, "minecraft:fireball");
/* 142 */     schema.register(map, "minecraft:fireworks_rocket", name -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema)));
/* 145 */     schema.register(map, "minecraft:furnace_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
/* 148 */     registerMob(schema, map, "minecraft:ghast");
/* 149 */     registerMob(schema, map, "minecraft:giant");
/* 150 */     registerMob(schema, map, "minecraft:guardian");
/* 151 */     schema.register(map, "minecraft:hopper_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
/* 155 */     schema.register(map, "minecraft:horse", name -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema)));
/* 159 */     registerMob(schema, map, "minecraft:husk");
/* 160 */     registerMob(schema, map, "minecraft:illusion_illager");
/* 161 */     schema.register(map, "minecraft:item", name -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/* 164 */     schema.register(map, "minecraft:item_frame", name -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/* 167 */     schema.registerSimple(map, "minecraft:leash_knot");
/* 168 */     schema.register(map, "minecraft:llama", name -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), "DecorItem", References.ITEM_STACK.in(schema)));
/* 173 */     schema.registerSimple(map, "minecraft:llama_spit");
/* 174 */     registerMob(schema, map, "minecraft:magma_cube");
/* 175 */     schema.register(map, "minecraft:minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
/* 178 */     registerMob(schema, map, "minecraft:mooshroom");
/* 179 */     schema.register(map, "minecraft:mule", name -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema)));
/* 183 */     registerMob(schema, map, "minecraft:ocelot");
/* 184 */     schema.registerSimple(map, "minecraft:painting");
/* 185 */     registerMob(schema, map, "minecraft:parrot");
/* 186 */     registerMob(schema, map, "minecraft:pig");
/* 187 */     registerMob(schema, map, "minecraft:polar_bear");
/* 188 */     schema.register(map, "minecraft:potion", name -> DSL.optionalFields("Potion", References.ITEM_STACK.in(schema)));
/* 191 */     registerMob(schema, map, "minecraft:rabbit");
/* 192 */     registerMob(schema, map, "minecraft:sheep");
/* 193 */     registerMob(schema, map, "minecraft:shulker");
/* 194 */     schema.registerSimple(map, "minecraft:shulker_bullet");
/* 195 */     registerMob(schema, map, "minecraft:silverfish");
/* 196 */     registerMob(schema, map, "minecraft:skeleton");
/* 197 */     schema.register(map, "minecraft:skeleton_horse", name -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema)));
/* 200 */     registerMob(schema, map, "minecraft:slime");
/* 201 */     schema.registerSimple(map, "minecraft:small_fireball");
/* 202 */     schema.registerSimple(map, "minecraft:snowball");
/* 203 */     registerMob(schema, map, "minecraft:snowman");
/* 204 */     schema.register(map, "minecraft:spawner_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), References.UNTAGGED_SPAWNER.in(schema)));
/* 208 */     schema.register(map, "minecraft:spectral_arrow", name -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema)));
/* 211 */     registerMob(schema, map, "minecraft:spider");
/* 212 */     registerMob(schema, map, "minecraft:squid");
/* 213 */     registerMob(schema, map, "minecraft:stray");
/* 214 */     schema.registerSimple(map, "minecraft:tnt");
/* 215 */     schema.register(map, "minecraft:tnt_minecart", name -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
/* 218 */     registerMob(schema, map, "minecraft:vex");
/* 219 */     schema.register(map, "minecraft:villager", name -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(schema)))));
/* 225 */     registerMob(schema, map, "minecraft:villager_golem");
/* 226 */     registerMob(schema, map, "minecraft:vindication_illager");
/* 227 */     registerMob(schema, map, "minecraft:witch");
/* 228 */     registerMob(schema, map, "minecraft:wither");
/* 229 */     registerMob(schema, map, "minecraft:wither_skeleton");
/* 230 */     schema.registerSimple(map, "minecraft:wither_skull");
/* 231 */     registerMob(schema, map, "minecraft:wolf");
/* 232 */     schema.registerSimple(map, "minecraft:xp_bottle");
/* 233 */     schema.registerSimple(map, "minecraft:xp_orb");
/* 234 */     registerMob(schema, map, "minecraft:zombie");
/* 235 */     schema.register(map, "minecraft:zombie_horse", name -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema)));
/* 238 */     registerMob(schema, map, "minecraft:zombie_pigman");
/* 239 */     schema.register(map, "minecraft:zombie_villager", name -> DSL.optionalFields("Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(schema)))));
/* 245 */     return map;
/*     */   }
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 250 */     Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
/* 252 */     registerInventory(schema, map, "minecraft:furnace");
/* 253 */     registerInventory(schema, map, "minecraft:chest");
/* 254 */     registerInventory(schema, map, "minecraft:trapped_chest");
/* 255 */     schema.registerSimple(map, "minecraft:ender_chest");
/* 256 */     schema.register(map, "minecraft:jukebox", name -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema)));
/* 259 */     registerInventory(schema, map, "minecraft:dispenser");
/* 260 */     registerInventory(schema, map, "minecraft:dropper");
/* 261 */     schema.register(map, "minecraft:sign", () -> V99.sign(schema));
/* 262 */     schema.register(map, "minecraft:mob_spawner", name -> References.UNTAGGED_SPAWNER.in(schema));
/* 263 */     schema.register(map, "minecraft:piston", name -> DSL.optionalFields("blockState", References.BLOCK_STATE.in(schema)));
/* 266 */     registerInventory(schema, map, "minecraft:brewing_stand");
/* 267 */     schema.register(map, "minecraft:enchanting_table", () -> V1458.nameable(schema));
/* 268 */     schema.registerSimple(map, "minecraft:end_portal");
/* 269 */     schema.register(map, "minecraft:beacon", () -> V1458.nameable(schema));
/* 270 */     schema.register(map, "minecraft:skull", () -> DSL.optionalFields("custom_name", References.TEXT_COMPONENT.in(schema)));
/* 273 */     schema.registerSimple(map, "minecraft:daylight_detector");
/* 274 */     registerInventory(schema, map, "minecraft:hopper");
/* 275 */     schema.registerSimple(map, "minecraft:comparator");
/* 276 */     schema.register(map, "minecraft:banner", () -> V1458.nameable(schema));
/* 277 */     schema.registerSimple(map, "minecraft:structure_block");
/* 278 */     schema.registerSimple(map, "minecraft:end_gateway");
/* 279 */     schema.register(map, "minecraft:command_block", () -> DSL.optionalFields("LastOutput", References.TEXT_COMPONENT.in(schema)));
/* 282 */     registerInventory(schema, map, "minecraft:shulker_box");
/* 283 */     schema.registerSimple(map, "minecraft:bed");
/* 285 */     return map;
/*     */   }
/*     */   
/*     */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 290 */     schema.registerType(false, References.LEVEL, () -> DSL.optionalFields("CustomBossEvents", DSL.compoundList(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema))), References.LIGHTWEIGHT_LEVEL.in(schema)));
/* 296 */     schema.registerType(false, References.LIGHTWEIGHT_LEVEL, DSL::remainder);
/* 297 */     schema.registerType(false, References.RECIPE, () -> DSL.constType(namespacedString()));
/* 298 */     schema.registerType(false, References.PLAYER, () -> DSL.optionalFields(new Pair[] { Pair.of("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(schema))), Pair.of("ender_pearls", DSL.list(References.ENTITY_TREE.in(schema))), Pair.of("Inventory", DSL.list(References.ITEM_STACK.in(schema))), Pair.of("EnderItems", DSL.list(References.ITEM_STACK.in(schema))), Pair.of("ShoulderEntityLeft", References.ENTITY_TREE.in(schema)), Pair.of("ShoulderEntityRight", References.ENTITY_TREE.in(schema)), Pair.of("recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(schema)), "toBeDisplayed", DSL.list(References.RECIPE.in(schema)))) }));
/* 314 */     schema.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))), "Sections", DSL.list(DSL.optionalFields("Palette", DSL.list(References.BLOCK_STATE.in(schema)))))));
/* 324 */     schema.registerType(true, References.BLOCK_ENTITY, () -> DSL.optionalFields("components", References.DATA_COMPONENTS.in(schema), (TypeTemplate)DSL.taggedChoiceLazy("id", namespacedString(), blockEntityTypes)));
/* 328 */     schema.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Passengers", DSL.list(References.ENTITY_TREE.in(schema)), References.ENTITY.in(schema)));
/* 332 */     schema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT.in(schema), DSL.optionalFields("CustomName", References.TEXT_COMPONENT.in(schema), (TypeTemplate)DSL.taggedChoiceLazy("id", namespacedString(), entityTypes))));
/* 339 */     schema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(schema), "tag", V99.itemStackTag(schema)), V705.ADD_NAMES, Hook.HookFunction.IDENTITY));
/* 343 */     schema.registerType(false, References.HOTBAR, () -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(schema))));
/* 344 */     schema.registerType(false, References.OPTIONS, DSL::remainder);
/* 345 */     schema.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(schema))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(schema))), "palette", DSL.list(References.BLOCK_STATE.in(schema))));
/* 350 */     schema.registerType(false, References.BLOCK_NAME, () -> DSL.constType(namespacedString()));
/* 351 */     schema.registerType(false, References.ITEM_NAME, () -> DSL.constType(namespacedString()));
/* 352 */     schema.registerType(false, References.BLOCK_STATE, DSL::remainder);
/* 353 */     schema.registerType(false, References.FLAT_BLOCK_STATE, DSL::remainder);
/* 355 */     Supplier<TypeTemplate> itemStats = () -> DSL.compoundList(References.ITEM_NAME.in(schema), DSL.constType(DSL.intType()));
/* 357 */     schema.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields(new Pair[] { Pair.of("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:crafted", itemStats.get()), Pair.of("minecraft:used", itemStats.get()), Pair.of("minecraft:broken", itemStats.get()), Pair.of("minecraft:picked_up", itemStats.get()), Pair.of("minecraft:dropped", itemStats.get()), Pair.of("minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:custom", DSL.compoundList(DSL.constType(namespacedString()), DSL.constType(DSL.intType()))) })));
/* 370 */     schema.registerType(false, References.SAVED_DATA_COMMAND_STORAGE, DSL::remainder);
/* 371 */     schema.registerType(false, References.SAVED_DATA_CUSTOM_BOSS_EVENTS, () -> DSL.optionalFields("data", DSL.compoundList(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema)))));
/* 376 */     schema.registerType(false, References.SAVED_DATA_ENDER_DRAGON_FIGHT, DSL::remainder);
/* 377 */     schema.registerType(false, References.SAVED_DATA_GAME_RULES, DSL::remainder);
/* 378 */     schema.registerType(false, References.SAVED_DATA_TICKETS, DSL::remainder);
/* 379 */     schema.registerType(false, References.SAVED_DATA_MAP_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("banners", DSL.list(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema))))));
/* 386 */     schema.registerType(false, References.SAVED_DATA_MAP_INDEX, DSL::remainder);
/* 387 */     schema.registerType(false, References.SAVED_DATA_RAIDS, DSL::remainder);
/* 388 */     schema.registerType(false, References.SAVED_DATA_RANDOM_SEQUENCES, DSL::remainder);
/* 389 */     schema.registerType(false, References.SAVED_DATA_SCHEDULED_EVENTS, DSL::remainder);
/* 390 */     schema.registerType(false, References.SAVED_DATA_SCOREBOARD, () -> DSL.optionalFields("data", DSL.optionalFields("Objectives", DSL.list(References.OBJECTIVE.in(schema)), "Teams", DSL.list(References.TEAM.in(schema)), "PlayerScores", DSL.list(DSL.optionalFields("display", References.TEXT_COMPONENT.in(schema))))));
/* 399 */     schema.registerType(false, References.SAVED_DATA_STOPWATCHES, DSL::remainder);
/* 400 */     schema.registerType(false, References.SAVED_DATA_STRUCTURE_FEATURE_INDICES, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema)))));
/* 405 */     schema.registerType(false, References.SAVED_DATA_WANDERING_TRADER, DSL::remainder);
/* 406 */     schema.registerType(false, References.SAVED_DATA_WEATHER, DSL::remainder);
/* 407 */     schema.registerType(false, References.SAVED_DATA_WORLD_BORDER, DSL::remainder);
/* 408 */     schema.registerType(false, References.SAVED_DATA_WORLD_CLOCKS, DSL::remainder);
/* 409 */     schema.registerType(false, References.SAVED_DATA_WORLD_GEN_SETTINGS, () -> DSL.fields("data", References.WORLD_GEN_SETTINGS.in(schema)));
/* 412 */     schema.registerType(false, References.DEBUG_PROFILE, DSL::remainder);
/* 413 */     schema.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
/* 415 */     Map<String, Supplier<TypeTemplate>> criterionTypes = V1451_6.createCriterionTypes(schema);
/* 416 */     schema.registerType(false, References.OBJECTIVE, () -> DSL.hook(DSL.optionalFields("CriteriaType", (TypeTemplate)DSL.taggedChoiceLazy("type", DSL.string(), criterionTypes), "DisplayName", References.TEXT_COMPONENT.in(schema)), V1451_6.UNPACK_OBJECTIVE_ID, V1451_6.REPACK_OBJECTIVE_ID));
/* 424 */     schema.registerType(false, References.TEAM, () -> DSL.optionalFields("MemberNamePrefix", References.TEXT_COMPONENT.in(schema), "MemberNameSuffix", References.TEXT_COMPONENT.in(schema), "DisplayName", References.TEXT_COMPONENT.in(schema)));
/* 429 */     schema.registerType(true, References.UNTAGGED_SPAWNER, () -> DSL.optionalFields("SpawnPotentials", DSL.list(DSL.fields("Entity", References.ENTITY_TREE.in(schema))), "SpawnData", References.ENTITY_TREE.in(schema)));
/* 435 */     schema.registerType(false, References.ADVANCEMENTS, () -> DSL.optionalFields("minecraft:adventure/adventuring_time", DSL.optionalFields("criteria", DSL.compoundList(References.BIOME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_a_mob", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_all_mobs", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:husbandry/bred_all_animals", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string())))));
/* 449 */     schema.registerType(false, References.BIOME, () -> DSL.constType(namespacedString()));
/* 450 */     schema.registerType(false, References.ENTITY_NAME, () -> DSL.constType(namespacedString()));
/* 451 */     schema.registerType(false, References.POI_CHUNK, DSL::remainder);
/* 452 */     schema.registerType(false, References.WORLD_GEN_SETTINGS, DSL::remainder);
/* 453 */     schema.registerType(false, References.ENTITY_CHUNK, () -> DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema))));
/* 456 */     schema.registerType(true, References.DATA_COMPONENTS, DSL::remainder);
/* 457 */     schema.registerType(true, References.VILLAGER_TRADE, () -> DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)));
/* 462 */     schema.registerType(true, References.PARTICLE, () -> DSL.constType(DSL.string()));
/* 463 */     schema.registerType(true, References.TEXT_COMPONENT, () -> DSL.constType(DSL.string()));
/* 464 */     schema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.and(DSL.optional(DSL.field("ArmorItems", DSL.list(References.ITEM_STACK.in(schema)))), new TypeTemplate[] { DSL.optional(DSL.field("HandItems", DSL.list(References.ITEM_STACK.in(schema)))), DSL.optional(DSL.field("body_armor_item", References.ITEM_STACK.in(schema))), DSL.optional(DSL.field("saddle", References.ITEM_STACK.in(schema))) }));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V1460.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */