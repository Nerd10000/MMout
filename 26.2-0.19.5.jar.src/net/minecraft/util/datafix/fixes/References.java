/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ 
/*     */ public class References {
/*     */   public static DSL.TypeReference reference(final String id) {
/*   7 */     return new DSL.TypeReference() {
/*     */         public String typeName() {
/*  10 */           return id;
/*     */         }
/*     */         
/*     */         public String toString() {
/*  15 */           return "@" + id;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*  21 */   public static final DSL.TypeReference LEVEL = reference("level");
/*     */   
/*  23 */   public static final DSL.TypeReference LIGHTWEIGHT_LEVEL = reference("lightweight_level");
/*     */   
/*  24 */   public static final DSL.TypeReference PLAYER = reference("player");
/*     */   
/*  25 */   public static final DSL.TypeReference CHUNK = reference("chunk");
/*     */   
/*  26 */   public static final DSL.TypeReference HOTBAR = reference("hotbar");
/*     */   
/*  27 */   public static final DSL.TypeReference OPTIONS = reference("options");
/*     */   
/*  28 */   public static final DSL.TypeReference STRUCTURE = reference("structure");
/*     */   
/*  29 */   public static final DSL.TypeReference STATS = reference("stats");
/*     */   
/*  30 */   public static final DSL.TypeReference SAVED_DATA_COMMAND_STORAGE = reference("saved_data/command_storage");
/*     */   
/*  31 */   public static final DSL.TypeReference SAVED_DATA_CUSTOM_BOSS_EVENTS = reference("saved_data/custom_boss_events");
/*     */   
/*  32 */   public static final DSL.TypeReference SAVED_DATA_ENDER_DRAGON_FIGHT = reference("saved_data/ender_dragon_fight");
/*     */   
/*  33 */   public static final DSL.TypeReference SAVED_DATA_GAME_RULES = reference("saved_data/game_rules");
/*     */   
/*  34 */   public static final DSL.TypeReference SAVED_DATA_TICKETS = reference("saved_data/tickets");
/*     */   
/*  35 */   public static final DSL.TypeReference SAVED_DATA_MAP_DATA = reference("saved_data/map_data");
/*     */   
/*  36 */   public static final DSL.TypeReference SAVED_DATA_MAP_INDEX = reference("saved_data/idcounts");
/*     */   
/*  37 */   public static final DSL.TypeReference SAVED_DATA_RAIDS = reference("saved_data/raids");
/*     */   
/*  38 */   public static final DSL.TypeReference SAVED_DATA_RANDOM_SEQUENCES = reference("saved_data/random_sequences");
/*     */   
/*  39 */   public static final DSL.TypeReference SAVED_DATA_SCHEDULED_EVENTS = reference("saved_data/scheduled_events");
/*     */   
/*  40 */   public static final DSL.TypeReference SAVED_DATA_SCOREBOARD = reference("saved_data/scoreboard");
/*     */   
/*  41 */   public static final DSL.TypeReference SAVED_DATA_STOPWATCHES = reference("saved_data/stopwatches");
/*     */   
/*  42 */   public static final DSL.TypeReference SAVED_DATA_STRUCTURE_FEATURE_INDICES = reference("saved_data/structure_feature_indices");
/*     */   
/*  43 */   public static final DSL.TypeReference SAVED_DATA_WANDERING_TRADER = reference("saved_data/wandering_trader");
/*     */   
/*  44 */   public static final DSL.TypeReference SAVED_DATA_WEATHER = reference("saved_data/weather");
/*     */   
/*  45 */   public static final DSL.TypeReference SAVED_DATA_WORLD_BORDER = reference("saved_data/world_border");
/*     */   
/*  46 */   public static final DSL.TypeReference SAVED_DATA_WORLD_CLOCKS = reference("saved_data/world_clocks");
/*     */   
/*  47 */   public static final DSL.TypeReference SAVED_DATA_WORLD_GEN_SETTINGS = reference("saved_data/world_gen_settings");
/*     */   
/*  48 */   public static final DSL.TypeReference ADVANCEMENTS = reference("advancements");
/*     */   
/*  49 */   public static final DSL.TypeReference POI_CHUNK = reference("poi_chunk");
/*     */   
/*  50 */   public static final DSL.TypeReference ENTITY_CHUNK = reference("entity_chunk");
/*     */   
/*  51 */   public static final DSL.TypeReference DEBUG_PROFILE = reference("debug_profile");
/*     */   
/*  54 */   public static final DSL.TypeReference BLOCK_ENTITY = reference("block_entity");
/*     */   
/*  55 */   public static final DSL.TypeReference ITEM_STACK = reference("item_stack");
/*     */   
/*  56 */   public static final DSL.TypeReference BLOCK_STATE = reference("block_state");
/*     */   
/*  59 */   public static final DSL.TypeReference FLAT_BLOCK_STATE = reference("flat_block_state");
/*     */   
/*  60 */   public static final DSL.TypeReference DATA_COMPONENTS = reference("data_components");
/*     */   
/*  61 */   public static final DSL.TypeReference VILLAGER_TRADE = reference("villager_trade");
/*     */   
/*  62 */   public static final DSL.TypeReference PARTICLE = reference("particle");
/*     */   
/*  63 */   public static final DSL.TypeReference TEXT_COMPONENT = reference("text_component");
/*     */   
/*  64 */   public static final DSL.TypeReference ENTITY_EQUIPMENT = reference("entity_equipment");
/*     */   
/*  67 */   public static final DSL.TypeReference ENTITY_NAME = reference("entity_name");
/*     */   
/*  70 */   public static final DSL.TypeReference ENTITY_TREE = reference("entity_tree");
/*     */   
/*  72 */   public static final DSL.TypeReference ENTITY = reference("entity");
/*     */   
/*  75 */   public static final DSL.TypeReference BLOCK_NAME = reference("block_name");
/*     */   
/*  76 */   public static final DSL.TypeReference ITEM_NAME = reference("item_name");
/*     */   
/*  77 */   public static final DSL.TypeReference GAME_EVENT_NAME = reference("game_event_name");
/*     */   
/*  80 */   public static final DSL.TypeReference UNTAGGED_SPAWNER = reference("untagged_spawner");
/*     */   
/*  83 */   public static final DSL.TypeReference STRUCTURE_FEATURE = reference("structure_feature");
/*     */   
/*  86 */   public static final DSL.TypeReference OBJECTIVE = reference("objective");
/*     */   
/*  89 */   public static final DSL.TypeReference TEAM = reference("team");
/*     */   
/*  92 */   public static final DSL.TypeReference RECIPE = reference("recipe");
/*     */   
/*  95 */   public static final DSL.TypeReference BIOME = reference("biome");
/*     */   
/*  98 */   public static final DSL.TypeReference MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST = reference("multi_noise_biome_source_parameter_list");
/*     */   
/* 101 */   public static final DSL.TypeReference WORLD_GEN_SETTINGS = reference("world_gen_settings");
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\References.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */