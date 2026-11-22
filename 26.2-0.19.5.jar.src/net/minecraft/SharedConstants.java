/*     */ package net.minecraft;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import io.netty.util.ResourceLeakDetector;
/*     */ import java.time.Duration;
/*     */ import net.minecraft.commands.BrigadierExceptions;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ @SuppressForbidden(reason = "System.out needed before bootstrap")
/*     */ public class SharedConstants {
/*     */   @Deprecated
/*     */   public static final boolean SNAPSHOT = false;
/*     */   
/*     */   @Deprecated
/*     */   public static final int WORLD_VERSION = 4903;
/*     */   
/*     */   @Deprecated
/*     */   public static final String SERIES = "main";
/*     */   
/*     */   @Deprecated
/*     */   public static final int RELEASE_NETWORK_PROTOCOL_VERSION = 776;
/*     */   
/*     */   @Deprecated
/*     */   public static final int SNAPSHOT_NETWORK_PROTOCOL_VERSION = 322;
/*     */   
/*     */   public static final int SNBT_NAG_VERSION = 4882;
/*     */   
/*     */   private static final int SNAPSHOT_PROTOCOL_BIT = 30;
/*     */   
/*     */   public static final boolean CRASH_EAGERLY = false;
/*     */   
/*     */   @Deprecated
/*     */   public static final int RESOURCE_PACK_FORMAT_MAJOR = 88;
/*     */   
/*     */   @Deprecated
/*     */   public static final int RESOURCE_PACK_FORMAT_MINOR = 0;
/*     */   
/*     */   @Deprecated
/*     */   public static final int DATA_PACK_FORMAT_MAJOR = 107;
/*     */   
/*     */   @Deprecated
/*     */   public static final int DATA_PACK_FORMAT_MINOR = 1;
/*     */   
/*     */   public static final String RPC_MANAGEMENT_SERVER_API_VERSION = "3.0.0";
/*     */   
/*     */   @Deprecated
/*     */   public static final int LANGUAGE_FORMAT = 1;
/*     */   
/*     */   public static final int REPORT_FORMAT_VERSION = 1;
/*     */   
/*     */   public static final String DATA_VERSION_TAG = "DataVersion";
/*     */   
/*     */   public static final String DEBUG_FLAG_PREFIX = "MC_DEBUG_";
/*     */   
/*     */   private static String prefixDebugFlagName(String name) {
/*  67 */     return "MC_DEBUG_" + name;
/*     */   }
/*     */   
/*     */   private static boolean booleanProperty(String name) {
/*  71 */     String value = System.getProperty(name);
/*  73 */     return (value != null && (value.isEmpty() || Boolean.parseBoolean(value)));
/*     */   }
/*     */   
/*  76 */   public static final boolean DEBUG_ENABLED = booleanProperty(prefixDebugFlagName("ENABLED"));
/*     */   
/*  77 */   private static final boolean DEBUG_PRINT_PROPERTIES = booleanProperty(prefixDebugFlagName("PRINT_PROPERTIES"));
/*     */   
/*     */   public static final boolean FIX_TNT_DUPE = false;
/*     */   
/*     */   public static final boolean FIX_SAND_DUPE = false;
/*     */   
/*     */   private static boolean debugFlag(String name) {
/*  81 */     if (!DEBUG_ENABLED)
/*  82 */       return false; 
/*  84 */     String prefixedName = prefixDebugFlagName(name);
/*  85 */     if (DEBUG_PRINT_PROPERTIES)
/*  86 */       System.out.println("Debug property available: " + prefixedName + ": bool"); 
/*  88 */     return booleanProperty(prefixedName);
/*     */   }
/*     */   
/*     */   private static int debugIntValue(String name) {
/*  93 */     if (!DEBUG_ENABLED)
/*  94 */       return 0; 
/*  96 */     String prefixedName = prefixDebugFlagName(name);
/*  97 */     if (DEBUG_PRINT_PROPERTIES)
/*  98 */       System.out.println("Debug property available: " + prefixedName + ": int"); 
/* 100 */     return Integer.parseInt(System.getProperty(prefixedName, "0"));
/*     */   }
/*     */   
/* 105 */   public static final boolean DEBUG_OPEN_INCOMPATIBLE_WORLDS = debugFlag("OPEN_INCOMPATIBLE_WORLDS");
/*     */   
/* 106 */   public static final boolean DEBUG_ALLOW_LOW_SIM_DISTANCE = debugFlag("ALLOW_LOW_SIM_DISTANCE");
/*     */   
/* 107 */   public static final boolean DEBUG_HOTKEYS = debugFlag("HOTKEYS");
/*     */   
/* 108 */   public static final boolean DEBUG_UI_NARRATION = debugFlag("UI_NARRATION");
/*     */   
/* 109 */   public static final boolean DEBUG_SHUFFLE_UI_RENDERING_ORDER = debugFlag("SHUFFLE_UI_RENDERING_ORDER");
/*     */   
/* 110 */   public static final boolean DEBUG_SHUFFLE_MODELS = debugFlag("SHUFFLE_MODELS");
/*     */   
/* 111 */   public static final boolean DEBUG_RENDER_UI_LAYERING_RECTANGLES = debugFlag("RENDER_UI_LAYERING_RECTANGLES");
/*     */   
/* 112 */   public static final boolean DEBUG_PATHFINDING = debugFlag("PATHFINDING");
/*     */   
/* 113 */   public static final boolean DEBUG_SHOW_LOCAL_SERVER_ENTITY_HIT_BOXES = debugFlag("SHOW_LOCAL_SERVER_ENTITY_HIT_BOXES");
/*     */   
/* 114 */   public static final boolean DEBUG_SHAPES = debugFlag("SHAPES");
/*     */   
/* 115 */   public static final boolean DEBUG_NEIGHBORSUPDATE = debugFlag("NEIGHBORSUPDATE");
/*     */   
/* 116 */   public static final boolean DEBUG_EXPERIMENTAL_REDSTONEWIRE_UPDATE_ORDER = debugFlag("EXPERIMENTAL_REDSTONEWIRE_UPDATE_ORDER");
/*     */   
/* 117 */   public static final boolean DEBUG_STRUCTURES = debugFlag("STRUCTURES");
/*     */   
/* 118 */   public static final boolean DEBUG_GAME_EVENT_LISTENERS = debugFlag("GAME_EVENT_LISTENERS");
/*     */   
/* 119 */   public static final boolean DEBUG_DUMP_TEXTURE_ATLAS = debugFlag("DUMP_TEXTURE_ATLAS");
/*     */   
/* 120 */   public static final boolean DEBUG_STRUCTURE_EDIT_MODE = debugFlag("STRUCTURE_EDIT_MODE");
/*     */   
/* 121 */   public static final boolean DEBUG_SAVE_STRUCTURES_AS_SNBT = debugFlag("SAVE_STRUCTURES_AS_SNBT");
/*     */   
/* 122 */   public static final boolean DEBUG_SYNCHRONOUS_GL_LOGS = debugFlag("SYNCHRONOUS_GL_LOGS");
/*     */   
/* 123 */   public static final boolean DEBUG_VERBOSE_SERVER_EVENTS = debugFlag("VERBOSE_SERVER_EVENTS");
/*     */   
/* 124 */   public static final boolean DEBUG_NAMED_RUNNABLES = debugFlag("NAMED_RUNNABLES");
/*     */   
/* 125 */   public static final boolean DEBUG_GOAL_SELECTOR = debugFlag("GOAL_SELECTOR");
/*     */   
/* 126 */   public static final boolean DEBUG_VILLAGE_SECTIONS = debugFlag("VILLAGE_SECTIONS");
/*     */   
/* 127 */   public static final boolean DEBUG_BRAIN = debugFlag("BRAIN");
/*     */   
/* 128 */   public static final boolean DEBUG_POI = debugFlag("POI");
/*     */   
/* 129 */   public static final boolean DEBUG_BEES = debugFlag("BEES");
/*     */   
/* 130 */   public static final boolean DEBUG_RAIDS = debugFlag("RAIDS");
/*     */   
/* 131 */   public static final boolean DEBUG_BLOCK_BREAK = debugFlag("BLOCK_BREAK");
/*     */   
/* 132 */   public static final boolean DEBUG_MONITOR_TICK_TIMES = debugFlag("MONITOR_TICK_TIMES");
/*     */   
/* 133 */   public static final boolean DEBUG_KEEP_JIGSAW_BLOCKS_DURING_STRUCTURE_GEN = debugFlag("KEEP_JIGSAW_BLOCKS_DURING_STRUCTURE_GEN");
/*     */   
/* 134 */   public static final boolean DEBUG_DONT_SAVE_WORLD = debugFlag("DONT_SAVE_WORLD");
/*     */   
/* 135 */   public static final boolean DEBUG_LARGE_DRIPSTONE = debugFlag("LARGE_DRIPSTONE");
/*     */   
/* 136 */   public static final boolean DEBUG_CARVERS = debugFlag("CARVERS");
/*     */   
/* 137 */   public static final boolean DEBUG_ORE_VEINS = debugFlag("ORE_VEINS");
/*     */   
/* 138 */   public static final boolean DEBUG_SCULK_CATALYST = debugFlag("SCULK_CATALYST");
/*     */   
/* 139 */   public static final boolean DEBUG_BYPASS_REALMS_VERSION_CHECK = debugFlag("BYPASS_REALMS_VERSION_CHECK");
/*     */   
/* 140 */   public static final boolean DEBUG_SOCIAL_INTERACTIONS = debugFlag("SOCIAL_INTERACTIONS");
/*     */   
/* 141 */   public static final boolean DEBUG_CHAT_DISABLED = debugFlag("CHAT_DISABLED");
/*     */   
/* 142 */   public static final boolean DEBUG_CHAT_FRIENDS_ONLY = debugFlag("CHAT_FRIENDS_ONLY");
/*     */   
/* 143 */   public static final boolean DEBUG_VALIDATE_RESOURCE_PATH_CASE = debugFlag("VALIDATE_RESOURCE_PATH_CASE");
/*     */   
/* 144 */   public static final boolean DEBUG_UNLOCK_ALL_TRADES = debugFlag("UNLOCK_ALL_TRADES");
/*     */   
/* 145 */   public static final boolean DEBUG_BREEZE_MOB = debugFlag("BREEZE_MOB");
/*     */   
/* 146 */   public static final boolean DEBUG_TRIAL_SPAWNER_DETECTS_SHEEP_AS_PLAYERS = debugFlag("TRIAL_SPAWNER_DETECTS_SHEEP_AS_PLAYERS");
/*     */   
/* 147 */   public static final boolean DEBUG_VAULT_DETECTS_SHEEP_AS_PLAYERS = debugFlag("VAULT_DETECTS_SHEEP_AS_PLAYERS");
/*     */   
/* 148 */   public static final boolean DEBUG_FORCE_ONBOARDING_SCREEN = debugFlag("FORCE_ONBOARDING_SCREEN");
/*     */   
/* 149 */   public static final boolean DEBUG_CURSOR_POS = debugFlag("CURSOR_POS");
/*     */   
/* 150 */   public static final boolean DEBUG_DEFAULT_SKIN_OVERRIDE = debugFlag("DEFAULT_SKIN_OVERRIDE");
/*     */   
/* 151 */   public static final boolean DEBUG_PANORAMA_SCREENSHOT = debugFlag("PANORAMA_SCREENSHOT");
/*     */   
/* 152 */   public static final boolean DEBUG_CHASE_COMMAND = debugFlag("CHASE_COMMAND");
/*     */   
/* 153 */   public static final boolean DEBUG_VERBOSE_COMMAND_ERRORS = debugFlag("VERBOSE_COMMAND_ERRORS");
/*     */   
/* 154 */   public static final boolean DEBUG_DEV_COMMANDS = debugFlag("DEV_COMMANDS");
/*     */   
/* 155 */   public static final boolean DEBUG_ACTIVE_TEXT_AREAS = debugFlag("ACTIVE_TEXT_AREAS");
/*     */   
/* 156 */   public static final boolean DEBUG_PREFER_WAYLAND = debugFlag("PREFER_WAYLAND");
/*     */   
/* 157 */   public static final boolean DEBUG_SIMULATE_LIBRARY_LOAD_FAILURE = debugFlag("SIMULATE_LIBRARY_LOAD_FAILURE");
/*     */   
/* 159 */   public static final boolean DEBUG_IGNORE_LOCAL_MOB_CAP = debugFlag("IGNORE_LOCAL_MOB_CAP");
/*     */   
/* 161 */   public static final boolean DEBUG_DISABLE_LIQUID_SPREADING = debugFlag("DISABLE_LIQUID_SPREADING");
/*     */   
/* 162 */   public static final boolean DEBUG_AQUIFERS = debugFlag("AQUIFERS");
/*     */   
/* 164 */   public static final boolean DEBUG_JFR_PROFILING_ENABLE_LEVEL_LOADING = debugFlag("JFR_PROFILING_ENABLE_LEVEL_LOADING");
/*     */   
/* 165 */   public static final boolean DEBUG_ENTITY_BLOCK_INTERSECTION = debugFlag("ENTITY_BLOCK_INTERSECTION");
/*     */   
/* 170 */   public static boolean debugGenerateSquareTerrainWithoutNoise = debugFlag("GENERATE_SQUARE_TERRAIN_WITHOUT_NOISE");
/*     */   
/* 174 */   public static final boolean DEBUG_ONLY_GENERATE_HALF_THE_WORLD = debugFlag("ONLY_GENERATE_HALF_THE_WORLD");
/*     */   
/* 175 */   public static final boolean DEBUG_DISABLE_FLUID_GENERATION = debugFlag("DISABLE_FLUID_GENERATION");
/*     */   
/* 176 */   public static final boolean DEBUG_DISABLE_AQUIFERS = debugFlag("DISABLE_AQUIFERS");
/*     */   
/* 177 */   public static final boolean DEBUG_DISABLE_SURFACE = debugFlag("DISABLE_SURFACE");
/*     */   
/* 178 */   public static final boolean DEBUG_DISABLE_CARVERS = debugFlag("DISABLE_CARVERS");
/*     */   
/* 179 */   public static final boolean DEBUG_DISABLE_STRUCTURES = debugFlag("DISABLE_STRUCTURES");
/*     */   
/* 180 */   public static final boolean DEBUG_DISABLE_FEATURES = debugFlag("DISABLE_FEATURES");
/*     */   
/* 181 */   public static final boolean DEBUG_DISABLE_ORE_VEINS = debugFlag("DISABLE_ORE_VEINS");
/*     */   
/* 182 */   public static final boolean DEBUG_DISABLE_BLENDING = debugFlag("DISABLE_BLENDING");
/*     */   
/* 183 */   public static final boolean DEBUG_DISABLE_BELOW_ZERO_RETROGENERATION = debugFlag("DISABLE_BELOW_ZERO_RETROGENERATION");
/*     */   
/*     */   public static final int DEFAULT_MINECRAFT_PORT = 25565;
/*     */   
/* 186 */   public static final boolean DEBUG_SUBTITLES = debugFlag("SUBTITLES");
/*     */   
/* 187 */   public static final int DEBUG_FAKE_LATENCY_MS = debugIntValue("FAKE_LATENCY_MS");
/*     */   
/* 188 */   public static final int DEBUG_FAKE_JITTER_MS = debugIntValue("FAKE_JITTER_MS");
/*     */   
/* 190 */   public static final ResourceLeakDetector.Level NETTY_LEAK_DETECTION = ResourceLeakDetector.Level.DISABLED;
/*     */   
/* 191 */   public static final boolean COMMAND_STACK_TRACES = debugFlag("COMMAND_STACK_TRACES");
/*     */   
/* 192 */   public static final boolean DEBUG_WORLD_RECREATE = debugFlag("WORLD_RECREATE");
/*     */   
/* 193 */   public static final boolean DEBUG_SHOW_SERVER_DEBUG_VALUES = debugFlag("SHOW_SERVER_DEBUG_VALUES");
/*     */   
/* 195 */   public static final boolean DEBUG_FEATURE_COUNT = debugFlag("FEATURE_COUNT");
/*     */   
/* 197 */   public static final boolean DEBUG_FORCE_TELEMETRY = debugFlag("FORCE_TELEMETRY");
/*     */   
/* 198 */   public static final boolean DEBUG_DONT_SEND_TELEMETRY_TO_BACKEND = debugFlag("DONT_SEND_TELEMETRY_TO_BACKEND");
/*     */   
/* 200 */   public static final long MAXIMUM_TICK_TIME_NANOS = Duration.ofMillis(300L).toNanos();
/*     */   
/*     */   public static final float MAXIMUM_BLOCK_EXPLOSION_RESISTANCE = 3600000.0F;
/*     */   
/*     */   public static final boolean USE_DEVONLY = false;
/*     */   
/*     */   public static boolean CHECK_DATA_FIXER_SCHEMA = true;
/*     */   
/*     */   public static boolean IS_RUNNING_IN_IDE;
/*     */   
/*     */   public static final int WORLD_RESOLUTION = 16;
/*     */   
/*     */   public static final int MAX_CHAT_LENGTH = 256;
/*     */   
/*     */   public static final int MAX_USER_INPUT_COMMAND_LENGTH = 32500;
/*     */   
/*     */   public static final int MAX_FUNCTION_COMMAND_LENGTH = 2000000;
/*     */   
/*     */   public static final int MAX_PLAYER_NAME_LENGTH = 16;
/*     */   
/*     */   public static final int MAX_CHAINED_NEIGHBOR_UPDATES = 1000000;
/*     */   
/*     */   public static final int MAX_RENDER_DISTANCE = 32;
/*     */   
/*     */   public static final int MAX_CLOUD_DISTANCE = 128;
/*     */   
/* 219 */   public static final char[] ILLEGAL_FILE_CHARACTERS = new char[] { 
/* 219 */       '/', '\n', '\r', '\t', Character.MIN_VALUE, '\f', '`', '?', '*', '\\', 
/* 219 */       '<', '>', '|', '"', ':' };
/*     */   
/*     */   public static final int TICKS_PER_SECOND = 20;
/*     */   
/*     */   public static final int MILLIS_PER_TICK = 50;
/*     */   
/*     */   public static final int TICKS_PER_MINUTE = 1200;
/*     */   
/*     */   public static final int TICKS_PER_GAME_DAY = 24000;
/*     */   
/*     */   public static final int DEFAULT_RANDOM_TICK_SPEED = 3;
/*     */   
/*     */   public static final float AVERAGE_GAME_TICKS_PER_RANDOM_TICK_PER_BLOCK = 1365.3334F;
/*     */   
/*     */   public static final float AVERAGE_RANDOM_TICKS_PER_BLOCK_PER_MINUTE = 0.87890625F;
/*     */   
/*     */   public static final float AVERAGE_RANDOM_TICKS_PER_BLOCK_PER_GAME_DAY = 17.578125F;
/*     */   
/*     */   public static final int WORLD_ICON_SIZE = 64;
/*     */   
/*     */   private static WorldVersion CURRENT_VERSION;
/*     */   
/*     */   public static void setVersion(WorldVersion version) {
/* 238 */     if (CURRENT_VERSION == null) {
/* 239 */       CURRENT_VERSION = version;
/* 240 */     } else if (version != CURRENT_VERSION) {
/* 241 */       throw new IllegalStateException("Cannot override the current game version!");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void tryDetectVersion() {
/* 246 */     if (CURRENT_VERSION == null)
/* 247 */       CURRENT_VERSION = DetectedVersion.tryDetectVersion(); 
/*     */   }
/*     */   
/*     */   public static WorldVersion getCurrentVersion() {
/* 252 */     if (CURRENT_VERSION == null)
/* 253 */       throw new IllegalStateException("Game version not set"); 
/* 255 */     return CURRENT_VERSION;
/*     */   }
/*     */   
/*     */   public static int getProtocolVersion() {
/* 262 */     return 776;
/*     */   }
/*     */   
/*     */   static {
/* 266 */     ResourceLeakDetector.setLevel(NETTY_LEAK_DETECTION);
/* 267 */     CommandSyntaxException.ENABLE_COMMAND_STACK_TRACES = COMMAND_STACK_TRACES;
/* 268 */     CommandSyntaxException.BUILT_IN_EXCEPTIONS = (BuiltInExceptionProvider)new BrigadierExceptions();
/*     */   }
/*     */   
/*     */   public static boolean debugVoidTerrain(ChunkPos pos) {
/* 273 */     int posX = pos.getMinBlockX();
/* 274 */     int posZ = pos.getMinBlockZ();
/* 275 */     if (DEBUG_ONLY_GENERATE_HALF_THE_WORLD)
/* 276 */       return (posZ < 0); 
/* 279 */     if (debugGenerateSquareTerrainWithoutNoise)
/* 280 */       return (posX > 8192 || posX < 0 || posZ > 1024 || posZ < 0); 
/* 282 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\SharedConstants.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */