/*     */ package net.minecraft.client.gui.components.debug;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class DebugScreenEntries {
/*  10 */   private static final Map<Identifier, DebugScreenEntry> ENTRIES_BY_ID = new HashMap<>();
/*     */   
/*  12 */   public static final Identifier GAME_VERSION = register("game_version", new DebugEntryVersion());
/*     */   
/*  13 */   public static final Identifier FPS = register("fps", new DebugEntryFps());
/*     */   
/*  14 */   public static final Identifier TPS = register("tps", new DebugEntryTps());
/*     */   
/*  15 */   public static final Identifier MEMORY = register("memory", new DebugEntryMemory());
/*     */   
/*  16 */   public static final Identifier DETAILED_MEMORY = register("detailed_memory", new DebugEntryDetailedMemory());
/*     */   
/*  17 */   public static final Identifier SYSTEM_SPECS = register("system_specs", new DebugEntrySystemSpecs());
/*     */   
/*  18 */   public static final Identifier LOOKING_AT_BLOCK_STATE = register("looking_at_block_state", new DebugEntryLookingAt.BlockStateInfo());
/*     */   
/*  19 */   public static final Identifier LOOKING_AT_BLOCK_TAGS = register("looking_at_block_tags", new DebugEntryLookingAt.BlockTagInfo());
/*     */   
/*  20 */   public static final Identifier LOOKING_AT_FLUID_STATE = register("looking_at_fluid_state", new DebugEntryLookingAt.FluidStateInfo());
/*     */   
/*  21 */   public static final Identifier LOOKING_AT_FLUID_TAGS = register("looking_at_fluid_tags", new DebugEntryLookingAt.FluidTagInfo());
/*     */   
/*  22 */   public static final Identifier LOOKING_AT_ENTITY = register("looking_at_entity", new DebugEntryLookingAtEntity());
/*     */   
/*  23 */   public static final Identifier LOOKING_AT_ENTITY_TAGS = register("looking_at_entity_tags", new DebugEntryLookingAtEntityTags());
/*     */   
/*  24 */   public static final Identifier CHUNK_RENDER_STATS = register("chunk_render_stats", new DebugEntryChunkRenderStats());
/*     */   
/*  25 */   public static final Identifier CHUNK_GENERATION_STATS = register("chunk_generation_stats", new DebugEntryChunkGeneration());
/*     */   
/*  26 */   public static final Identifier ENTITY_RENDER_STATS = register("entity_render_stats", new DebugEntryEntityRenderStats());
/*     */   
/*  27 */   public static final Identifier PARTICLE_RENDER_STATS = register("particle_render_stats", new DebugEntryParticleRenderStats());
/*     */   
/*  28 */   public static final Identifier CHUNK_SOURCE_STATS = register("chunk_source_stats", new DebugEntryChunkSourceStats());
/*     */   
/*  29 */   public static final Identifier PLAYER_POSITION = register("player_position", new DebugEntryPosition());
/*     */   
/*  30 */   public static final Identifier PLAYER_SECTION_POSITION = register("player_section_position", new DebugEntrySectionPosition());
/*     */   
/*  31 */   public static final Identifier LIGHT_LEVELS = register("light_levels", new DebugEntryLight());
/*     */   
/*  32 */   public static final Identifier HEIGHTMAP = register("heightmap", new DebugEntryHeightmap());
/*     */   
/*  33 */   public static final Identifier BIOME = register("biome", new DebugEntryBiome());
/*     */   
/*  34 */   public static final Identifier LOCAL_DIFFICULTY = register("local_difficulty", new DebugEntryLocalDifficulty());
/*     */   
/*  35 */   public static final Identifier DAY_COUNT = register("day_count", new DebugEntryDayCount());
/*     */   
/*  36 */   public static final Identifier ENTITY_SPAWN_COUNTS = register("entity_spawn_counts", new DebugEntrySpawnCounts());
/*     */   
/*  37 */   public static final Identifier SOUND_MOOD = register("sound_mood", new DebugEntrySoundMood());
/*     */   
/*  38 */   public static final Identifier SOUND_CACHE = register("sound_cache", new DebugEntrySoundCache());
/*     */   
/*  39 */   public static final Identifier POST_EFFECT = register("post_effect", new DebugEntryPostEffect());
/*     */   
/*  40 */   public static final Identifier ENTITY_HITBOXES = register("entity_hitboxes", new DebugEntryNoop());
/*     */   
/*  41 */   public static final Identifier CHUNK_BORDERS = register("chunk_borders", new DebugEntryNoop());
/*     */   
/*  42 */   public static final Identifier THREE_DIMENSIONAL_CROSSHAIR = register("3d_crosshair", new DebugEntryNoop());
/*     */   
/*  43 */   public static final Identifier CHUNK_SECTION_PATHS = register("chunk_section_paths", new DebugEntryNoop());
/*     */   
/*  44 */   public static final Identifier GPU_UTILIZATION = register("gpu_utilization", new DebugEntryGpuUtilization());
/*     */   
/*  45 */   public static final Identifier SIMPLE_PERFORMANCE_IMPACTORS = register("simple_performance_impactors", new DebugEntrySimplePerformanceImpactors());
/*     */   
/*  46 */   public static final Identifier CHUNK_SECTION_OCTREE = register("chunk_section_octree", new DebugEntryNoop());
/*     */   
/*  47 */   public static final Identifier VISUALIZE_WATER_LEVELS = register("visualize_water_levels", new DebugEntryNoop());
/*     */   
/*  48 */   public static final Identifier VISUALIZE_HEIGHTMAP = register("visualize_heightmap", new DebugEntryNoop());
/*     */   
/*  49 */   public static final Identifier VISUALIZE_COLLISION_BOXES = register("visualize_collision_boxes", new DebugEntryNoop());
/*     */   
/*  50 */   public static final Identifier VISUALIZE_ENTITY_SUPPORTING_BLOCKS = register("visualize_entity_supporting_blocks", new DebugEntryNoop());
/*     */   
/*  51 */   public static final Identifier VISUALIZE_BLOCK_LIGHT_LEVELS = register("visualize_block_light_levels", new DebugEntryNoop());
/*     */   
/*  52 */   public static final Identifier VISUALIZE_SKY_LIGHT_LEVELS = register("visualize_sky_light_levels", new DebugEntryNoop());
/*     */   
/*  53 */   public static final Identifier VISUALIZE_SOLID_FACES = register("visualize_solid_faces", new DebugEntryNoop());
/*     */   
/*  54 */   public static final Identifier VISUALIZE_CHUNKS_ON_SERVER = register("visualize_chunks_on_server", new DebugEntryNoop());
/*     */   
/*  55 */   public static final Identifier VISUALIZE_SKY_LIGHT_SECTIONS = register("visualize_sky_light_sections", new DebugEntryNoop());
/*     */   
/*  56 */   public static final Identifier CHUNK_SECTION_VISIBILITY = register("chunk_section_visibility", new DebugEntryNoop());
/*     */   
/*     */   public static final Map<DebugScreenProfile, Map<Identifier, DebugScreenEntryStatus>> PROFILES;
/*     */   
/*     */   static {
/*  62 */     Map<Identifier, DebugScreenEntryStatus> defaultProfile = Map.of(THREE_DIMENSIONAL_CROSSHAIR, DebugScreenEntryStatus.IN_OVERLAY, GAME_VERSION, DebugScreenEntryStatus.IN_OVERLAY, TPS, DebugScreenEntryStatus.IN_OVERLAY, FPS, DebugScreenEntryStatus.IN_OVERLAY, MEMORY, DebugScreenEntryStatus.IN_OVERLAY, SYSTEM_SPECS, DebugScreenEntryStatus.IN_OVERLAY, PLAYER_POSITION, DebugScreenEntryStatus.IN_OVERLAY, PLAYER_SECTION_POSITION, DebugScreenEntryStatus.IN_OVERLAY, SIMPLE_PERFORMANCE_IMPACTORS, DebugScreenEntryStatus.IN_OVERLAY);
/*  74 */     Map<Identifier, DebugScreenEntryStatus> performance = Map.of(TPS, DebugScreenEntryStatus.IN_OVERLAY, FPS, DebugScreenEntryStatus.ALWAYS_ON, GPU_UTILIZATION, DebugScreenEntryStatus.IN_OVERLAY, MEMORY, DebugScreenEntryStatus.IN_OVERLAY, SIMPLE_PERFORMANCE_IMPACTORS, DebugScreenEntryStatus.IN_OVERLAY);
/*  82 */     PROFILES = Map.of(DebugScreenProfile.DEFAULT, defaultProfile, DebugScreenProfile.PERFORMANCE, performance);
/*     */   }
/*     */   
/*     */   private static Identifier register(String id, DebugScreenEntry entry) {
/*  89 */     return register(Identifier.withDefaultNamespace(id), entry);
/*     */   }
/*     */   
/*     */   private static Identifier register(Identifier identifier, DebugScreenEntry entry) {
/*  93 */     ENTRIES_BY_ID.put(identifier, entry);
/*  94 */     return identifier;
/*     */   }
/*     */   
/*     */   public static Map<Identifier, DebugScreenEntry> allEntries() {
/*  98 */     return Map.copyOf(ENTRIES_BY_ID);
/*     */   }
/*     */   
/*     */   public static DebugScreenEntry getEntry(Identifier id) {
/* 102 */     return ENTRIES_BY_ID.get(id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugScreenEntries.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */