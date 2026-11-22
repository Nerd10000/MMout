/*     */ package net.minecraft.util.datafix.schemas;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.templates.Hook;
/*     */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class V99 extends Schema {
/*  83 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final Map<String, String> ITEM_TO_BLOCKENTITY;
/*     */   
/*     */   public V99(int versionKey, Schema parent) {
/*  86 */     super(versionKey, parent);
/*     */   }
/*     */   
/*     */   protected static void registerThrowableProjectile(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/*  90 */     schema.register(map, name, () -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
/*     */   }
/*     */   
/*     */   protected static void registerMinecart(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/*  97 */     schema.register(map, name, () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema)));
/*     */   }
/*     */   
/*     */   protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String name) {
/* 103 */     schema.register(map, name, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
/*     */   }
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 110 */     Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
/* 112 */     schema.register(map, "Item", name -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/* 115 */     schema.registerSimple(map, "XPOrb");
/* 116 */     registerThrowableProjectile(schema, map, "ThrownEgg");
/* 117 */     schema.registerSimple(map, "LeashKnot");
/* 118 */     schema.registerSimple(map, "Painting");
/* 119 */     schema.register(map, "Arrow", name -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
/* 122 */     schema.register(map, "TippedArrow", name -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
/* 125 */     schema.register(map, "SpectralArrow", name -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
/* 128 */     registerThrowableProjectile(schema, map, "Snowball");
/* 129 */     registerThrowableProjectile(schema, map, "Fireball");
/* 130 */     registerThrowableProjectile(schema, map, "SmallFireball");
/* 131 */     registerThrowableProjectile(schema, map, "ThrownEnderpearl");
/* 132 */     schema.registerSimple(map, "EyeOfEnderSignal");
/* 133 */     schema.register(map, "ThrownPotion", name -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema), "Potion", References.ITEM_STACK.in(schema)));
/* 137 */     registerThrowableProjectile(schema, map, "ThrownExpBottle");
/* 138 */     schema.register(map, "ItemFrame", name -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
/* 141 */     registerThrowableProjectile(schema, map, "WitherSkull");
/* 142 */     schema.registerSimple(map, "PrimedTnt");
/* 143 */     schema.register(map, "FallingSand", name -> DSL.optionalFields("Block", References.BLOCK_NAME.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema)));
/* 147 */     schema.register(map, "FireworksRocketEntity", name -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema)));
/* 150 */     schema.registerSimple(map, "Boat");
/* 153 */     schema.register(map, "Minecart", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
/* 157 */     registerMinecart(schema, map, "MinecartRideable");
/* 158 */     schema.register(map, "MinecartChest", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
/* 162 */     registerMinecart(schema, map, "MinecartFurnace");
/* 163 */     registerMinecart(schema, map, "MinecartTNT");
/* 164 */     schema.register(map, "MinecartSpawner", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), References.UNTAGGED_SPAWNER.in(schema)));
/* 168 */     schema.register(map, "MinecartHopper", name -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
/* 172 */     schema.register(map, "MinecartCommandBlock", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "LastOutput", References.TEXT_COMPONENT.in(schema)));
/* 180 */     schema.registerSimple(map, "ArmorStand");
/* 181 */     schema.registerSimple(map, "Creeper");
/* 182 */     schema.registerSimple(map, "Skeleton");
/* 183 */     schema.registerSimple(map, "Spider");
/* 184 */     schema.registerSimple(map, "Giant");
/* 185 */     schema.registerSimple(map, "Zombie");
/* 186 */     schema.registerSimple(map, "Slime");
/* 187 */     schema.registerSimple(map, "Ghast");
/* 188 */     schema.registerSimple(map, "PigZombie");
/* 189 */     schema.register(map, "Enderman", name -> DSL.optionalFields("carried", References.BLOCK_NAME.in(schema)));
/* 192 */     schema.registerSimple(map, "CaveSpider");
/* 193 */     schema.registerSimple(map, "Silverfish");
/* 194 */     schema.registerSimple(map, "Blaze");
/* 195 */     schema.registerSimple(map, "LavaSlime");
/* 196 */     schema.registerSimple(map, "EnderDragon");
/* 197 */     schema.registerSimple(map, "WitherBoss");
/* 198 */     schema.registerSimple(map, "Bat");
/* 199 */     schema.registerSimple(map, "Witch");
/* 200 */     schema.registerSimple(map, "Endermite");
/* 201 */     schema.registerSimple(map, "Guardian");
/* 202 */     schema.registerSimple(map, "Pig");
/* 203 */     schema.registerSimple(map, "Sheep");
/* 204 */     schema.registerSimple(map, "Cow");
/* 205 */     schema.registerSimple(map, "Chicken");
/* 206 */     schema.registerSimple(map, "Squid");
/* 207 */     schema.registerSimple(map, "Wolf");
/* 208 */     schema.registerSimple(map, "MushroomCow");
/* 209 */     schema.registerSimple(map, "SnowMan");
/* 210 */     schema.registerSimple(map, "Ozelot");
/* 211 */     schema.registerSimple(map, "VillagerGolem");
/* 212 */     schema.register(map, "EntityHorse", name -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema)));
/* 217 */     schema.registerSimple(map, "Rabbit");
/* 218 */     schema.register(map, "Villager", name -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(schema)))));
/* 224 */     schema.registerSimple(map, "EnderCrystal");
/* 228 */     schema.register(map, "AreaEffectCloud", name -> DSL.optionalFields("Particle", References.PARTICLE.in(schema)));
/* 231 */     schema.registerSimple(map, "ShulkerBullet");
/* 232 */     schema.registerSimple(map, "DragonFireball");
/* 233 */     schema.registerSimple(map, "Shulker");
/* 235 */     return map;
/*     */   }
/*     */   
/*     */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 240 */     Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
/* 242 */     registerInventory(schema, map, "Furnace");
/* 243 */     registerInventory(schema, map, "Chest");
/* 244 */     schema.registerSimple(map, "EnderChest");
/* 245 */     schema.register(map, "RecordPlayer", name -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema)));
/* 248 */     registerInventory(schema, map, "Trap");
/* 249 */     registerInventory(schema, map, "Dropper");
/* 250 */     schema.register(map, "Sign", () -> sign(schema));
/* 251 */     schema.register(map, "MobSpawner", name -> References.UNTAGGED_SPAWNER.in(schema));
/* 252 */     schema.registerSimple(map, "Music");
/* 253 */     schema.registerSimple(map, "Piston");
/* 254 */     registerInventory(schema, map, "Cauldron");
/* 255 */     schema.registerSimple(map, "EnchantTable");
/* 256 */     schema.registerSimple(map, "Airportal");
/* 257 */     schema.register(map, "Control", () -> DSL.optionalFields("LastOutput", References.TEXT_COMPONENT.in(schema)));
/* 260 */     schema.registerSimple(map, "Beacon");
/* 261 */     schema.register(map, "Skull", () -> DSL.optionalFields("custom_name", References.TEXT_COMPONENT.in(schema)));
/* 264 */     schema.registerSimple(map, "DLDetector");
/* 265 */     registerInventory(schema, map, "Hopper");
/* 266 */     schema.registerSimple(map, "Comparator");
/* 267 */     schema.register(map, "FlowerPot", name -> DSL.optionalFields("Item", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema))));
/* 270 */     schema.register(map, "Banner", () -> DSL.optionalFields("CustomName", References.TEXT_COMPONENT.in(schema)));
/* 276 */     schema.registerSimple(map, "Structure");
/* 277 */     schema.registerSimple(map, "EndGateway");
/* 278 */     return map;
/*     */   }
/*     */   
/*     */   public static TypeTemplate sign(Schema schema) {
/* 282 */     return DSL.optionalFields(new Pair[] { Pair.of("Text1", References.TEXT_COMPONENT.in(schema)), 
/* 284 */           Pair.of("Text2", References.TEXT_COMPONENT.in(schema)), 
/* 285 */           Pair.of("Text3", References.TEXT_COMPONENT.in(schema)), 
/* 286 */           Pair.of("Text4", References.TEXT_COMPONENT.in(schema)), 
/* 287 */           Pair.of("FilteredText1", References.TEXT_COMPONENT.in(schema)), 
/* 288 */           Pair.of("FilteredText2", References.TEXT_COMPONENT.in(schema)), 
/* 289 */           Pair.of("FilteredText3", References.TEXT_COMPONENT.in(schema)), 
/* 290 */           Pair.of("FilteredText4", References.TEXT_COMPONENT.in(schema)) });
/*     */   }
/*     */   
/*     */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 296 */     schema.registerType(false, References.LEVEL, () -> DSL.optionalFields("CustomBossEvents", DSL.compoundList(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema))), References.LIGHTWEIGHT_LEVEL.in(schema)));
/* 302 */     schema.registerType(false, References.LIGHTWEIGHT_LEVEL, DSL::remainder);
/* 303 */     schema.registerType(false, References.PLAYER, () -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "EnderItems", DSL.list(References.ITEM_STACK.in(schema))));
/* 307 */     schema.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))))));
/* 314 */     schema.registerType(true, References.BLOCK_ENTITY, () -> DSL.optionalFields("components", References.DATA_COMPONENTS.in(schema), (TypeTemplate)DSL.taggedChoiceLazy("id", DSL.string(), blockEntityTypes)));
/* 320 */     schema.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Riding", References.ENTITY_TREE.in(schema), References.ENTITY.in(schema)));
/* 324 */     schema.registerType(false, References.ENTITY_NAME, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 325 */     schema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT.in(schema), DSL.optionalFields("CustomName", DSL.constType(DSL.string()), (TypeTemplate)DSL.taggedChoiceLazy("id", DSL.string(), entityTypes))));
/* 332 */     schema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema)), "tag", itemStackTag(schema)), ADD_NAMES, Hook.HookFunction.IDENTITY));
/* 336 */     schema.registerType(false, References.OPTIONS, DSL::remainder);
/* 337 */     schema.registerType(false, References.BLOCK_NAME, () -> DSL.or(DSL.constType(DSL.intType()), DSL.constType(NamespacedSchema.namespacedString())));
/* 338 */     schema.registerType(false, References.ITEM_NAME, () -> DSL.constType(NamespacedSchema.namespacedString()));
/* 339 */     schema.registerType(false, References.STATS, DSL::remainder);
/* 340 */     schema.registerType(false, References.SAVED_DATA_COMMAND_STORAGE, DSL::remainder);
/* 341 */     schema.registerType(false, References.SAVED_DATA_CUSTOM_BOSS_EVENTS, () -> DSL.optionalFields("data", DSL.compoundList(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema)))));
/* 346 */     schema.registerType(false, References.SAVED_DATA_ENDER_DRAGON_FIGHT, DSL::remainder);
/* 347 */     schema.registerType(false, References.SAVED_DATA_GAME_RULES, DSL::remainder);
/* 348 */     schema.registerType(false, References.SAVED_DATA_TICKETS, DSL::remainder);
/* 349 */     schema.registerType(false, References.SAVED_DATA_MAP_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("banners", DSL.list(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema))))));
/* 356 */     schema.registerType(false, References.SAVED_DATA_MAP_INDEX, DSL::remainder);
/* 357 */     schema.registerType(false, References.SAVED_DATA_RAIDS, DSL::remainder);
/* 358 */     schema.registerType(false, References.SAVED_DATA_RANDOM_SEQUENCES, DSL::remainder);
/* 359 */     schema.registerType(false, References.SAVED_DATA_SCHEDULED_EVENTS, DSL::remainder);
/* 360 */     schema.registerType(false, References.SAVED_DATA_SCOREBOARD, () -> DSL.optionalFields("data", DSL.optionalFields("Objectives", DSL.list(References.OBJECTIVE.in(schema)), "Teams", DSL.list(References.TEAM.in(schema)), "PlayerScores", DSL.list(DSL.optionalFields("display", References.TEXT_COMPONENT.in(schema))))));
/* 369 */     schema.registerType(false, References.SAVED_DATA_STOPWATCHES, DSL::remainder);
/* 370 */     schema.registerType(false, References.SAVED_DATA_STRUCTURE_FEATURE_INDICES, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema)))));
/* 375 */     schema.registerType(false, References.SAVED_DATA_WANDERING_TRADER, DSL::remainder);
/* 376 */     schema.registerType(false, References.SAVED_DATA_WEATHER, DSL::remainder);
/* 377 */     schema.registerType(false, References.SAVED_DATA_WORLD_BORDER, DSL::remainder);
/* 378 */     schema.registerType(false, References.SAVED_DATA_WORLD_CLOCKS, DSL::remainder);
/* 379 */     schema.registerType(false, References.SAVED_DATA_WORLD_GEN_SETTINGS, () -> DSL.fields("data", References.WORLD_GEN_SETTINGS.in(schema)));
/* 382 */     schema.registerType(false, References.DEBUG_PROFILE, DSL::remainder);
/* 383 */     schema.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
/* 384 */     schema.registerType(false, References.OBJECTIVE, DSL::remainder);
/* 385 */     schema.registerType(false, References.TEAM, () -> DSL.optionalFields("MemberNamePrefix", References.TEXT_COMPONENT.in(schema), "MemberNameSuffix", References.TEXT_COMPONENT.in(schema), "DisplayName", References.TEXT_COMPONENT.in(schema)));
/* 391 */     schema.registerType(true, References.UNTAGGED_SPAWNER, DSL::remainder);
/* 392 */     schema.registerType(false, References.POI_CHUNK, DSL::remainder);
/* 393 */     schema.registerType(false, References.WORLD_GEN_SETTINGS, DSL::remainder);
/* 394 */     schema.registerType(false, References.ENTITY_CHUNK, () -> DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema))));
/* 397 */     schema.registerType(true, References.DATA_COMPONENTS, DSL::remainder);
/* 398 */     schema.registerType(true, References.VILLAGER_TRADE, () -> DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)));
/* 403 */     schema.registerType(true, References.PARTICLE, () -> DSL.constType(DSL.string()));
/* 404 */     schema.registerType(true, References.TEXT_COMPONENT, () -> DSL.constType(DSL.string()));
/* 405 */     schema.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(schema))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(schema))), "palette", DSL.list(References.BLOCK_STATE.in(schema))));
/* 410 */     schema.registerType(false, References.BLOCK_STATE, DSL::remainder);
/* 411 */     schema.registerType(false, References.FLAT_BLOCK_STATE, DSL::remainder);
/* 412 */     schema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.optional(DSL.field("Equipment", DSL.list(References.ITEM_STACK.in(schema)))));
/*     */   }
/*     */   
/*     */   public static TypeTemplate itemStackTag(Schema schema) {
/* 416 */     return DSL.optionalFields(new Pair[] { Pair.of("EntityTag", References.ENTITY_TREE.in(schema)), 
/* 418 */           Pair.of("BlockEntityTag", References.BLOCK_ENTITY.in(schema)), 
/* 419 */           Pair.of("CanDestroy", DSL.list(References.BLOCK_NAME.in(schema))), 
/* 420 */           Pair.of("CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema))), 
/* 421 */           Pair.of("Items", DSL.list(References.ITEM_STACK.in(schema))), 
/* 422 */           Pair.of("ChargedProjectiles", DSL.list(References.ITEM_STACK.in(schema))), 
/*     */           
/* 425 */           Pair.of("pages", DSL.list(References.TEXT_COMPONENT.in(schema))), 
/* 426 */           Pair.of("filtered_pages", DSL.compoundList(References.TEXT_COMPONENT.in(schema))), 
/* 427 */           Pair.of("display", DSL.optionalFields("Name", References.TEXT_COMPONENT
/* 428 */               .in(schema), "Lore", 
/* 429 */               DSL.list(References.TEXT_COMPONENT.in(schema)))) });
/*     */   }
/*     */   
/*     */   static {
/* 434 */     ITEM_TO_BLOCKENTITY = (Map<String, String>)DataFixUtils.make(Maps.newHashMap(), map -> {
/*     */           map.put("minecraft:furnace", "Furnace");
/*     */           map.put("minecraft:lit_furnace", "Furnace");
/*     */           map.put("minecraft:chest", "Chest");
/*     */           map.put("minecraft:trapped_chest", "Chest");
/*     */           map.put("minecraft:ender_chest", "EnderChest");
/*     */           map.put("minecraft:jukebox", "RecordPlayer");
/*     */           map.put("minecraft:dispenser", "Trap");
/*     */           map.put("minecraft:dropper", "Dropper");
/*     */           map.put("minecraft:sign", "Sign");
/*     */           map.put("minecraft:mob_spawner", "MobSpawner");
/*     */           map.put("minecraft:noteblock", "Music");
/*     */           map.put("minecraft:brewing_stand", "Cauldron");
/*     */           map.put("minecraft:enhanting_table", "EnchantTable");
/*     */           map.put("minecraft:command_block", "CommandBlock");
/*     */           map.put("minecraft:beacon", "Beacon");
/*     */           map.put("minecraft:skull", "Skull");
/*     */           map.put("minecraft:daylight_detector", "DLDetector");
/*     */           map.put("minecraft:hopper", "Hopper");
/*     */           map.put("minecraft:banner", "Banner");
/*     */           map.put("minecraft:flower_pot", "FlowerPot");
/*     */           map.put("minecraft:repeating_command_block", "CommandBlock");
/*     */           map.put("minecraft:chain_command_block", "CommandBlock");
/*     */           map.put("minecraft:standing_sign", "Sign");
/*     */           map.put("minecraft:wall_sign", "Sign");
/*     */           map.put("minecraft:piston_head", "Piston");
/*     */           map.put("minecraft:daylight_detector_inverted", "DLDetector");
/*     */           map.put("minecraft:unpowered_comparator", "Comparator");
/*     */           map.put("minecraft:powered_comparator", "Comparator");
/*     */           map.put("minecraft:wall_banner", "Banner");
/*     */           map.put("minecraft:standing_banner", "Banner");
/*     */           map.put("minecraft:structure_block", "Structure");
/*     */           map.put("minecraft:end_portal", "Airportal");
/*     */           map.put("minecraft:end_gateway", "EndGateway");
/*     */           map.put("minecraft:shield", "Banner");
/*     */         });
/*     */   }
/*     */   
/* 476 */   public static final Map<String, String> ITEM_TO_ENTITY = Map.of("minecraft:armor_stand", "ArmorStand", "minecraft:painting", "Painting");
/*     */   
/* 481 */   protected static final Hook.HookFunction ADD_NAMES = new Hook.HookFunction() {
/*     */       public <T> T apply(DynamicOps<T> ops, T value) {
/* 484 */         return V99.addNames(new Dynamic(ops, value), V99.ITEM_TO_BLOCKENTITY, V99.ITEM_TO_ENTITY);
/*     */       }
/*     */     };
/*     */   
/*     */   protected static <T> T addNames(Dynamic<T> input, Map<String, String> itemToBlockEntityMap, Map<String, String> itemToEntityMap) {
/* 489 */     return (T)input.update("tag", itemStackTag -> itemStackTag.update("BlockEntityTag", ()).update("EntityTag", ()))
/*     */       
/* 512 */       .getValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V99.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */