/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.arguments.BoolArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameRules {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static Codec<GameRules> codec(FeatureFlagSet enabledFeatures) {
/*  27 */     return GameRuleMap.CODEC.xmap(map -> new GameRules(enabledFeatures, map), gameRules -> gameRules.rules);
/*     */   }
/*     */   
/*  30 */   public static final GameRule<Boolean> ADVANCE_TIME = registerBoolean("advance_time", GameRuleCategory.UPDATES, !SharedConstants.DEBUG_WORLD_RECREATE);
/*     */   
/*  31 */   public static final GameRule<Boolean> ADVANCE_WEATHER = registerBoolean("advance_weather", GameRuleCategory.UPDATES, !SharedConstants.DEBUG_WORLD_RECREATE);
/*     */   
/*  32 */   public static final GameRule<Boolean> ALLOW_ENTERING_NETHER_USING_PORTALS = registerBoolean("allow_entering_nether_using_portals", GameRuleCategory.MISC, true);
/*     */   
/*  33 */   public static final GameRule<Boolean> BLOCK_DROPS = registerBoolean("block_drops", GameRuleCategory.DROPS, true);
/*     */   
/*  34 */   public static final GameRule<Boolean> BLOCK_EXPLOSION_DROP_DECAY = registerBoolean("block_explosion_drop_decay", GameRuleCategory.DROPS, true);
/*     */   
/*  35 */   public static final GameRule<Boolean> COMMAND_BLOCKS_WORK = registerBoolean("command_blocks_work", GameRuleCategory.MISC, true);
/*     */   
/*  36 */   public static final GameRule<Boolean> COMMAND_BLOCK_OUTPUT = registerBoolean("command_block_output", GameRuleCategory.CHAT, true);
/*     */   
/*  37 */   public static final GameRule<Boolean> DROWNING_DAMAGE = registerBoolean("drowning_damage", GameRuleCategory.PLAYER, true);
/*     */   
/*  38 */   public static final GameRule<Boolean> ELYTRA_MOVEMENT_CHECK = registerBoolean("elytra_movement_check", GameRuleCategory.PLAYER, true);
/*     */   
/*  39 */   public static final GameRule<Boolean> ENDER_PEARLS_VANISH_ON_DEATH = registerBoolean("ender_pearls_vanish_on_death", GameRuleCategory.PLAYER, true);
/*     */   
/*  40 */   public static final GameRule<Boolean> ENTITY_DROPS = registerBoolean("entity_drops", GameRuleCategory.DROPS, true);
/*     */   
/*  41 */   public static final GameRule<Boolean> FALL_DAMAGE = registerBoolean("fall_damage", GameRuleCategory.PLAYER, true);
/*     */   
/*  42 */   public static final GameRule<Boolean> FIRE_DAMAGE = registerBoolean("fire_damage", GameRuleCategory.PLAYER, true);
/*     */   
/*  43 */   public static final GameRule<Integer> FIRE_SPREAD_RADIUS_AROUND_PLAYER = registerInteger("fire_spread_radius_around_player", GameRuleCategory.UPDATES, 128, -1);
/*     */   
/*  44 */   public static final GameRule<Boolean> FORGIVE_DEAD_PLAYERS = registerBoolean("forgive_dead_players", GameRuleCategory.MOBS, true);
/*     */   
/*  45 */   public static final GameRule<Boolean> FREEZE_DAMAGE = registerBoolean("freeze_damage", GameRuleCategory.PLAYER, true);
/*     */   
/*  46 */   public static final GameRule<Boolean> GLOBAL_SOUND_EVENTS = registerBoolean("global_sound_events", GameRuleCategory.MISC, true);
/*     */   
/*  47 */   public static final GameRule<Boolean> IMMEDIATE_RESPAWN = registerBoolean("immediate_respawn", GameRuleCategory.PLAYER, false);
/*     */   
/*  48 */   public static final GameRule<Boolean> KEEP_INVENTORY = registerBoolean("keep_inventory", GameRuleCategory.PLAYER, false);
/*     */   
/*  49 */   public static final GameRule<Boolean> LAVA_SOURCE_CONVERSION = registerBoolean("lava_source_conversion", GameRuleCategory.UPDATES, false);
/*     */   
/*  50 */   public static final GameRule<Boolean> LIMITED_CRAFTING = registerBoolean("limited_crafting", GameRuleCategory.PLAYER, false);
/*     */   
/*  51 */   public static final GameRule<Boolean> LOCATOR_BAR = registerBoolean("locator_bar", GameRuleCategory.PLAYER, true);
/*     */   
/*  52 */   public static final GameRule<Boolean> LOG_ADMIN_COMMANDS = registerBoolean("log_admin_commands", GameRuleCategory.CHAT, true);
/*     */   
/*  53 */   public static final GameRule<Integer> MAX_BLOCK_MODIFICATIONS = registerInteger("max_block_modifications", GameRuleCategory.MISC, 32768, 1);
/*     */   
/*  54 */   public static final GameRule<Integer> MAX_COMMAND_FORKS = registerInteger("max_command_forks", GameRuleCategory.MISC, 65536, 0);
/*     */   
/*  55 */   public static final GameRule<Integer> MAX_COMMAND_SEQUENCE_LENGTH = registerInteger("max_command_sequence_length", GameRuleCategory.MISC, 65536, 0);
/*     */   
/*  56 */   public static final GameRule<Integer> MAX_ENTITY_CRAMMING = registerInteger("max_entity_cramming", GameRuleCategory.MOBS, 24, 0);
/*     */   
/*  57 */   public static final GameRule<Integer> MAX_MINECART_SPEED = registerInteger("max_minecart_speed", GameRuleCategory.MISC, 8, 1, 1000, FeatureFlagSet.of(FeatureFlags.MINECART_IMPROVEMENTS));
/*     */   
/*  58 */   public static final GameRule<Integer> MAX_SNOW_ACCUMULATION_HEIGHT = registerInteger("max_snow_accumulation_height", GameRuleCategory.UPDATES, 1, 0, 8);
/*     */   
/*  59 */   public static final GameRule<Boolean> MOB_DROPS = registerBoolean("mob_drops", GameRuleCategory.DROPS, true);
/*     */   
/*  60 */   public static final GameRule<Boolean> MOB_EXPLOSION_DROP_DECAY = registerBoolean("mob_explosion_drop_decay", GameRuleCategory.DROPS, true);
/*     */   
/*  61 */   public static final GameRule<Boolean> MOB_GRIEFING = registerBoolean("mob_griefing", GameRuleCategory.MOBS, true);
/*     */   
/*  62 */   public static final GameRule<Boolean> NATURAL_HEALTH_REGENERATION = registerBoolean("natural_health_regeneration", GameRuleCategory.PLAYER, true);
/*     */   
/*  63 */   public static final GameRule<Boolean> PLAYER_MOVEMENT_CHECK = registerBoolean("player_movement_check", GameRuleCategory.PLAYER, true);
/*     */   
/*  64 */   public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_CREATIVE_DELAY = registerInteger("players_nether_portal_creative_delay", GameRuleCategory.PLAYER, 0, 0);
/*     */   
/*  65 */   public static final GameRule<Integer> PLAYERS_NETHER_PORTAL_DEFAULT_DELAY = registerInteger("players_nether_portal_default_delay", GameRuleCategory.PLAYER, 80, 0);
/*     */   
/*  66 */   public static final GameRule<Integer> PLAYERS_SLEEPING_PERCENTAGE = registerInteger("players_sleeping_percentage", GameRuleCategory.PLAYER, 100, 0);
/*     */   
/*  67 */   public static final GameRule<Boolean> PROJECTILES_CAN_BREAK_BLOCKS = registerBoolean("projectiles_can_break_blocks", GameRuleCategory.DROPS, true);
/*     */   
/*  68 */   public static final GameRule<Boolean> PVP = registerBoolean("pvp", GameRuleCategory.PLAYER, true);
/*     */   
/*  69 */   public static final GameRule<Boolean> RAIDS = registerBoolean("raids", GameRuleCategory.MOBS, true);
/*     */   
/*  70 */   public static final GameRule<Integer> RANDOM_TICK_SPEED = registerInteger("random_tick_speed", GameRuleCategory.UPDATES, 3, 0);
/*     */   
/*  71 */   public static final GameRule<Boolean> REDUCED_DEBUG_INFO = registerBoolean("reduced_debug_info", GameRuleCategory.MISC, false);
/*     */   
/*  72 */   public static final GameRule<Integer> RESPAWN_RADIUS = registerInteger("respawn_radius", GameRuleCategory.PLAYER, 10, 0);
/*     */   
/*  73 */   public static final GameRule<Boolean> SEND_COMMAND_FEEDBACK = registerBoolean("send_command_feedback", GameRuleCategory.CHAT, true);
/*     */   
/*  74 */   public static final GameRule<Boolean> SHOW_ADVANCEMENT_MESSAGES = registerBoolean("show_advancement_messages", GameRuleCategory.CHAT, true);
/*     */   
/*  75 */   public static final GameRule<Boolean> SHOW_DEATH_MESSAGES = registerBoolean("show_death_messages", GameRuleCategory.CHAT, true);
/*     */   
/*  76 */   public static final GameRule<Boolean> SPAWNER_BLOCKS_WORK = registerBoolean("spawner_blocks_work", GameRuleCategory.MISC, true);
/*     */   
/*  77 */   public static final GameRule<Boolean> SPAWN_MOBS = registerBoolean("spawn_mobs", GameRuleCategory.SPAWNING, true);
/*     */   
/*  78 */   public static final GameRule<Boolean> SPAWN_MONSTERS = registerBoolean("spawn_monsters", GameRuleCategory.SPAWNING, true);
/*     */   
/*  79 */   public static final GameRule<Boolean> SPAWN_PATROLS = registerBoolean("spawn_patrols", GameRuleCategory.SPAWNING, true);
/*     */   
/*  80 */   public static final GameRule<Boolean> SPAWN_PHANTOMS = registerBoolean("spawn_phantoms", GameRuleCategory.SPAWNING, true);
/*     */   
/*  81 */   public static final GameRule<Boolean> SPAWN_WANDERING_TRADERS = registerBoolean("spawn_wandering_traders", GameRuleCategory.SPAWNING, true);
/*     */   
/*  82 */   public static final GameRule<Boolean> SPAWN_WARDENS = registerBoolean("spawn_wardens", GameRuleCategory.SPAWNING, true);
/*     */   
/*  83 */   public static final GameRule<Boolean> SPECTATORS_GENERATE_CHUNKS = registerBoolean("spectators_generate_chunks", GameRuleCategory.PLAYER, true);
/*     */   
/*  84 */   public static final GameRule<Boolean> SPREAD_VINES = registerBoolean("spread_vines", GameRuleCategory.UPDATES, true);
/*     */   
/*  85 */   public static final GameRule<Boolean> TNT_EXPLODES = registerBoolean("tnt_explodes", GameRuleCategory.MISC, true);
/*     */   
/*  86 */   public static final GameRule<Boolean> TNT_EXPLOSION_DROP_DECAY = registerBoolean("tnt_explosion_drop_decay", GameRuleCategory.DROPS, false);
/*     */   
/*  87 */   public static final GameRule<Boolean> UNIVERSAL_ANGER = registerBoolean("universal_anger", GameRuleCategory.MOBS, false);
/*     */   
/*  88 */   public static final GameRule<Boolean> WATER_SOURCE_CONVERSION = registerBoolean("water_source_conversion", GameRuleCategory.UPDATES, true);
/*     */   
/*     */   private final GameRuleMap rules;
/*     */   
/*     */   public GameRules(FeatureFlagSet enabledFeatures, GameRuleMap map) {
/*  93 */     BuiltInRegistries.GAME_RULE.stream().forEach(gameRule -> {
/*     */           if (gameRule.isEnabled(enabledFeatures)) {
/*     */             if (!map.has(gameRule))
/*     */               map.reset(gameRule); 
/*     */           } else if (map.has(gameRule)) {
/*     */             map.remove(gameRule);
/*     */           } 
/*     */         });
/* 102 */     this.rules = map;
/*     */   }
/*     */   
/*     */   public GameRules(FeatureFlagSet enabledFeatures) {
/* 106 */     this.rules = GameRuleMap.of(BuiltInRegistries.GAME_RULE.filterFeatures(enabledFeatures).listElements().map(Holder::value));
/*     */   }
/*     */   
/*     */   public GameRules(List<GameRule<?>> rules) {
/* 110 */     this.rules = GameRuleMap.of(rules.stream());
/*     */   }
/*     */   
/*     */   public Stream<GameRule<?>> availableRules() {
/* 114 */     return this.rules.keySet().stream();
/*     */   }
/*     */   
/*     */   public <T> T get(GameRule<T> gameRule) {
/* 118 */     T value = this.rules.get(gameRule);
/* 119 */     if (value == null)
/* 120 */       throw new IllegalArgumentException("Tried to access invalid game rule " + String.valueOf(gameRule.getIdentifierWithFallback())); 
/* 122 */     return value;
/*     */   }
/*     */   
/*     */   public <T> void set(GameRule<T> gameRule, T value, MinecraftServer server) {
/* 126 */     if (!this.rules.has(gameRule)) {
/* 127 */       LOGGER.warn("Tried to set invalid game rule '{}' to value '{}'", gameRule.getIdentifierWithFallback(), value);
/*     */       return;
/*     */     } 
/* 130 */     this.rules.set(gameRule, value);
/* 131 */     if (server != null)
/* 132 */       server.onGameRuleChanged(gameRule, value); 
/*     */   }
/*     */   
/*     */   public GameRules copy(FeatureFlagSet enabledFeatures) {
/* 137 */     return new GameRules(enabledFeatures, GameRuleMap.copyOf(this.rules));
/*     */   }
/*     */   
/*     */   public void setAll(GameRules other, MinecraftServer server) {
/* 141 */     setAll(other.rules, server);
/*     */   }
/*     */   
/*     */   public void setAll(GameRuleMap gameRulesMap, MinecraftServer server) {
/* 145 */     gameRulesMap.keySet().forEach(gameRule -> setFromOther(gameRulesMap, gameRule, server));
/*     */   }
/*     */   
/*     */   private <T> void setFromOther(GameRuleMap gameRulesMap, GameRule<T> gameRule, MinecraftServer server) {
/* 149 */     set(gameRule, Objects.requireNonNull(gameRulesMap.get(gameRule)), server);
/*     */   }
/*     */   
/*     */   public void visitGameRuleTypes(GameRuleTypeVisitor visitor) {
/* 157 */     this.rules.keySet().forEach(gameRule -> {
/*     */           visitor.visit(gameRule);
/*     */           gameRule.callVisitor(visitor);
/*     */         });
/*     */   }
/*     */   
/*     */   private static GameRule<Boolean> registerBoolean(String id, GameRuleCategory category, boolean defaultValue) {
/* 164 */     return register(id, category, GameRuleType.BOOL, 
/*     */         
/* 168 */         (ArgumentType<Boolean>)BoolArgumentType.bool(), (Codec<Boolean>)Codec.BOOL, 
/*     */         
/* 170 */         Boolean.valueOf(defaultValue), 
/* 171 */         FeatureFlagSet.of(), GameRuleTypeVisitor::visitBoolean, b -> b.booleanValue() ? 1 : 0);
/*     */   }
/*     */   
/*     */   private static GameRule<Integer> registerInteger(String id, GameRuleCategory category, int defaultValue, int min) {
/* 178 */     return registerInteger(id, category, defaultValue, min, 2147483647, FeatureFlagSet.of());
/*     */   }
/*     */   
/*     */   private static GameRule<Integer> registerInteger(String id, GameRuleCategory category, int defaultValue, int min, int max) {
/* 182 */     return registerInteger(id, category, defaultValue, min, max, FeatureFlagSet.of());
/*     */   }
/*     */   
/*     */   private static GameRule<Integer> registerInteger(String id, GameRuleCategory category, int defaultValue, int min, int max, FeatureFlagSet requiredFeatures) {
/* 186 */     return register(id, category, GameRuleType.INT, 
/*     */         
/* 190 */         (ArgumentType<Integer>)IntegerArgumentType.integer(min, max), 
/* 191 */         Codec.intRange(min, max), 
/* 192 */         Integer.valueOf(defaultValue), requiredFeatures, GameRuleTypeVisitor::visitInteger, i -> i.intValue());
/*     */   }
/*     */   
/*     */   private static <T> GameRule<T> register(String id, GameRuleCategory category, GameRuleType typeHint, ArgumentType<T> argumentType, Codec<T> codec, T defaultValue, FeatureFlagSet requiredFeatures, VisitorCaller<T> visitorCaller, ToIntFunction<T> commandResultFunction) {
/* 210 */     return (GameRule<T>)Registry.register(BuiltInRegistries.GAME_RULE, id, new GameRule<>(category, typeHint, argumentType, visitorCaller, codec, commandResultFunction, defaultValue, requiredFeatures));
/*     */   }
/*     */   
/*     */   public static GameRule<?> bootstrap(Registry<GameRule<?>> registry) {
/* 214 */     return ADVANCE_TIME;
/*     */   }
/*     */   
/*     */   public <T> String getAsString(GameRule<T> gameRule) {
/* 218 */     return gameRule.serialize(get(gameRule));
/*     */   }
/*     */   
/*     */   public static interface VisitorCaller<T> {
/*     */     void call(GameRuleTypeVisitor param1GameRuleTypeVisitor, GameRule<T> param1GameRule);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gamerules\GameRules.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */