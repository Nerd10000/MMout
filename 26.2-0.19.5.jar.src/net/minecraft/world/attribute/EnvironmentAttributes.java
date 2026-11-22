/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.TriState;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.MoonPhase;
/*     */ 
/*     */ public interface EnvironmentAttributes {
/*  23 */   public static final EnvironmentAttribute<Integer> FOG_COLOR = register("visual/fog_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/*  24 */       .defaultValue(Integer.valueOf(0))
/*  25 */       .spatiallyInterpolated()
/*  26 */       .syncable());
/*     */   
/*  27 */   public static final EnvironmentAttribute<Float> FOG_START_DISTANCE = register("visual/fog_start_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  28 */       .defaultValue(Float.valueOf(0.0F))
/*     */       
/*  30 */       .spatiallyInterpolated()
/*  31 */       .syncable());
/*     */   
/*  32 */   public static final EnvironmentAttribute<Float> FOG_END_DISTANCE = register("visual/fog_end_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  33 */       .defaultValue(Float.valueOf(1024.0F))
/*  34 */       .valueRange(AttributeRange.NON_NEGATIVE_FLOAT)
/*  35 */       .spatiallyInterpolated()
/*  36 */       .syncable());
/*     */   
/*  37 */   public static final EnvironmentAttribute<Float> SKY_FOG_END_DISTANCE = register("visual/sky_fog_end_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  38 */       .defaultValue(Float.valueOf(512.0F))
/*  39 */       .valueRange(AttributeRange.NON_NEGATIVE_FLOAT)
/*  40 */       .spatiallyInterpolated()
/*  41 */       .syncable());
/*     */   
/*  42 */   public static final EnvironmentAttribute<Float> CLOUD_FOG_END_DISTANCE = register("visual/cloud_fog_end_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  43 */       .defaultValue(Float.valueOf(2048.0F))
/*  44 */       .valueRange(AttributeRange.NON_NEGATIVE_FLOAT)
/*  45 */       .spatiallyInterpolated()
/*  46 */       .syncable());
/*     */   
/*  47 */   public static final EnvironmentAttribute<Integer> WATER_FOG_COLOR = register("visual/water_fog_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/*  48 */       .defaultValue(Integer.valueOf(-16448205))
/*  49 */       .spatiallyInterpolated()
/*  50 */       .syncable());
/*     */   
/*  51 */   public static final EnvironmentAttribute<Float> WATER_FOG_START_DISTANCE = register("visual/water_fog_start_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  52 */       .defaultValue(Float.valueOf(-8.0F))
/*  53 */       .spatiallyInterpolated()
/*  54 */       .syncable());
/*     */   
/*  55 */   public static final EnvironmentAttribute<Float> WATER_FOG_END_DISTANCE = register("visual/water_fog_end_distance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  56 */       .defaultValue(Float.valueOf(96.0F))
/*  57 */       .valueRange(AttributeRange.NON_NEGATIVE_FLOAT)
/*  58 */       .spatiallyInterpolated()
/*  59 */       .syncable());
/*     */   
/*  62 */   public static final EnvironmentAttribute<Integer> SKY_COLOR = register("visual/sky_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/*  63 */       .defaultValue(Integer.valueOf(0))
/*  64 */       .spatiallyInterpolated()
/*  65 */       .syncable());
/*     */   
/*  66 */   public static final EnvironmentAttribute<Integer> SUNRISE_SUNSET_COLOR = register("visual/sunrise_sunset_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.ARGB_COLOR)
/*  67 */       .defaultValue(Integer.valueOf(0))
/*  68 */       .spatiallyInterpolated()
/*  69 */       .syncable());
/*     */   
/*  70 */   public static final EnvironmentAttribute<Integer> CLOUD_COLOR = register("visual/cloud_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.ARGB_COLOR)
/*  71 */       .defaultValue(Integer.valueOf(0))
/*  72 */       .spatiallyInterpolated()
/*  73 */       .syncable());
/*     */   
/*  74 */   public static final EnvironmentAttribute<Float> CLOUD_HEIGHT = register("visual/cloud_height", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  75 */       .defaultValue(Float.valueOf(192.33F))
/*  76 */       .spatiallyInterpolated()
/*  77 */       .syncable());
/*     */   
/*  78 */   public static final EnvironmentAttribute<Float> SUN_ANGLE = register("visual/sun_angle", EnvironmentAttribute.<Float>builder(AttributeTypes.ANGLE_DEGREES)
/*  79 */       .defaultValue(Float.valueOf(0.0F))
/*  80 */       .spatiallyInterpolated()
/*  81 */       .syncable());
/*     */   
/*  82 */   public static final EnvironmentAttribute<Float> MOON_ANGLE = register("visual/moon_angle", EnvironmentAttribute.<Float>builder(AttributeTypes.ANGLE_DEGREES)
/*  83 */       .defaultValue(Float.valueOf(0.0F))
/*  84 */       .spatiallyInterpolated()
/*  85 */       .syncable());
/*     */   
/*  86 */   public static final EnvironmentAttribute<Float> STAR_ANGLE = register("visual/star_angle", EnvironmentAttribute.<Float>builder(AttributeTypes.ANGLE_DEGREES)
/*  87 */       .defaultValue(Float.valueOf(0.0F))
/*  88 */       .spatiallyInterpolated()
/*  89 */       .syncable());
/*     */   
/*  90 */   public static final EnvironmentAttribute<MoonPhase> MOON_PHASE = register("visual/moon_phase", EnvironmentAttribute.<MoonPhase>builder(AttributeTypes.MOON_PHASE)
/*  91 */       .defaultValue(MoonPhase.FULL_MOON)
/*  92 */       .syncable());
/*     */   
/*  93 */   public static final EnvironmentAttribute<Float> STAR_BRIGHTNESS = register("visual/star_brightness", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/*  94 */       .defaultValue(Float.valueOf(0.0F))
/*  95 */       .valueRange(AttributeRange.UNIT_FLOAT)
/*  96 */       .spatiallyInterpolated()
/*  97 */       .syncable());
/*     */   
/* 100 */   public static final EnvironmentAttribute<Integer> BLOCK_LIGHT_TINT = register("visual/block_light_tint", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/* 101 */       .defaultValue(Integer.valueOf(-10100))
/* 102 */       .spatiallyInterpolated()
/* 103 */       .syncable());
/*     */   
/* 104 */   public static final EnvironmentAttribute<Integer> SKY_LIGHT_COLOR = register("visual/sky_light_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/* 105 */       .defaultValue(Integer.valueOf(-1))
/* 106 */       .spatiallyInterpolated()
/* 107 */       .syncable());
/*     */   
/* 108 */   public static final EnvironmentAttribute<Float> SKY_LIGHT_FACTOR = register("visual/sky_light_factor", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 109 */       .defaultValue(Float.valueOf(1.0F))
/* 110 */       .valueRange(AttributeRange.UNIT_FLOAT)
/* 111 */       .spatiallyInterpolated()
/* 112 */       .syncable());
/*     */   
/* 113 */   public static final EnvironmentAttribute<Integer> NIGHT_VISION_COLOR = register("visual/night_vision_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/* 114 */       .defaultValue(Integer.valueOf(-6710887))
/* 115 */       .spatiallyInterpolated()
/* 116 */       .syncable());
/*     */   
/* 117 */   public static final EnvironmentAttribute<Integer> AMBIENT_LIGHT_COLOR = register("visual/ambient_light_color", EnvironmentAttribute.<Integer>builder(AttributeTypes.RGB_COLOR)
/* 118 */       .defaultValue(Integer.valueOf(-16777216))
/* 119 */       .spatiallyInterpolated()
/* 120 */       .syncable());
/*     */   
/* 123 */   public static final EnvironmentAttribute<ParticleOptions> DEFAULT_DRIPSTONE_PARTICLE = register("visual/default_dripstone_particle", EnvironmentAttribute.builder(AttributeTypes.PARTICLE)
/* 124 */       .defaultValue(ParticleTypes.DRIPPING_DRIPSTONE_WATER)
/* 125 */       .syncable());
/*     */   
/* 126 */   public static final EnvironmentAttribute<List<AmbientParticle>> AMBIENT_PARTICLES = register("visual/ambient_particles", EnvironmentAttribute.<List<AmbientParticle>>builder(AttributeTypes.AMBIENT_PARTICLES)
/* 127 */       .defaultValue(List.of())
/* 128 */       .syncable());
/*     */   
/* 131 */   public static final EnvironmentAttribute<BackgroundMusic> BACKGROUND_MUSIC = register("audio/background_music", EnvironmentAttribute.<BackgroundMusic>builder(AttributeTypes.BACKGROUND_MUSIC)
/* 132 */       .defaultValue(BackgroundMusic.EMPTY)
/* 133 */       .syncable());
/*     */   
/* 134 */   public static final EnvironmentAttribute<Float> MUSIC_VOLUME = register("audio/music_volume", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 135 */       .defaultValue(Float.valueOf(1.0F))
/* 136 */       .valueRange(AttributeRange.UNIT_FLOAT)
/* 137 */       .syncable());
/*     */   
/* 138 */   public static final EnvironmentAttribute<AmbientSounds> AMBIENT_SOUNDS = register("audio/ambient_sounds", EnvironmentAttribute.<AmbientSounds>builder(AttributeTypes.AMBIENT_SOUNDS)
/* 139 */       .defaultValue(AmbientSounds.EMPTY)
/* 140 */       .syncable());
/*     */   
/* 141 */   public static final EnvironmentAttribute<Boolean> FIREFLY_BUSH_SOUNDS = register("audio/firefly_bush_sounds", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 142 */       .defaultValue(Boolean.valueOf(false))
/* 143 */       .syncable());
/*     */   
/* 146 */   public static final EnvironmentAttribute<Float> SKY_LIGHT_LEVEL = register("gameplay/sky_light_level", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 147 */       .defaultValue(Float.valueOf(15.0F))
/* 148 */       .valueRange(AttributeRange.ofFloat(0.0F, 15.0F))
/* 149 */       .notPositional()
/* 150 */       .syncable());
/*     */   
/* 151 */   public static final EnvironmentAttribute<Boolean> CAN_START_RAID = register("gameplay/can_start_raid", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 152 */       .defaultValue(Boolean.valueOf(true)));
/*     */   
/* 153 */   public static final EnvironmentAttribute<Boolean> WATER_EVAPORATES = register("gameplay/water_evaporates", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 154 */       .defaultValue(Boolean.valueOf(false))
/* 155 */       .syncable());
/*     */   
/* 158 */   public static final EnvironmentAttribute<BedRule> BED_RULE = register("gameplay/bed_rule", EnvironmentAttribute.<BedRule>builder(AttributeTypes.BED_RULE)
/* 159 */       .defaultValue(BedRule.CAN_SLEEP_WHEN_DARK));
/*     */   
/* 160 */   public static final EnvironmentAttribute<Boolean> RESPAWN_ANCHOR_WORKS = register("gameplay/respawn_anchor_works", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 161 */       .defaultValue(Boolean.valueOf(false)));
/*     */   
/* 162 */   public static final EnvironmentAttribute<Boolean> NETHER_PORTAL_SPAWNS_PIGLINS = register("gameplay/nether_portal_spawns_piglin", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 163 */       .defaultValue(Boolean.valueOf(false)));
/*     */   
/* 164 */   public static final EnvironmentAttribute<Boolean> FAST_LAVA = register("gameplay/fast_lava", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 165 */       .defaultValue(Boolean.valueOf(false))
/* 166 */       .notPositional()
/* 167 */       .syncable());
/*     */   
/* 168 */   public static final EnvironmentAttribute<Boolean> INCREASED_FIRE_BURNOUT = register("gameplay/increased_fire_burnout", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 169 */       .defaultValue(Boolean.valueOf(false)));
/*     */   
/* 170 */   public static final EnvironmentAttribute<TriState> EYEBLOSSOM_OPEN = register("gameplay/eyeblossom_open", EnvironmentAttribute.<TriState>builder(AttributeTypes.TRI_STATE)
/* 171 */       .defaultValue(TriState.DEFAULT));
/*     */   
/* 172 */   public static final EnvironmentAttribute<Float> TURTLE_EGG_HATCH_CHANCE = register("gameplay/turtle_egg_hatch_chance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 173 */       .defaultValue(Float.valueOf(0.002F))
/* 174 */       .valueRange(AttributeRange.UNIT_FLOAT));
/*     */   
/* 177 */   public static final EnvironmentAttribute<Boolean> PIGLINS_ZOMBIFY = register("gameplay/piglins_zombify", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 178 */       .defaultValue(Boolean.valueOf(true))
/* 179 */       .syncable());
/*     */   
/* 180 */   public static final EnvironmentAttribute<Boolean> SNOW_GOLEM_MELTS = register("gameplay/snow_golem_melts", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 181 */       .defaultValue(Boolean.valueOf(false)));
/*     */   
/* 182 */   public static final EnvironmentAttribute<Boolean> CREAKING_ACTIVE = register("gameplay/creaking_active", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 183 */       .defaultValue(Boolean.valueOf(false))
/* 184 */       .syncable());
/*     */   
/* 185 */   public static final EnvironmentAttribute<Float> SURFACE_SLIME_SPAWN_CHANCE = register("gameplay/surface_slime_spawn_chance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 186 */       .defaultValue(Float.valueOf(0.0F))
/* 187 */       .valueRange(AttributeRange.UNIT_FLOAT));
/*     */   
/* 188 */   public static final EnvironmentAttribute<Float> CAT_WAKING_UP_GIFT_CHANCE = register("gameplay/cat_waking_up_gift_chance", EnvironmentAttribute.<Float>builder(AttributeTypes.FLOAT)
/* 189 */       .defaultValue(Float.valueOf(0.0F))
/* 190 */       .valueRange(AttributeRange.UNIT_FLOAT));
/*     */   
/* 191 */   public static final EnvironmentAttribute<Boolean> BEES_STAY_IN_HIVE = register("gameplay/bees_stay_in_hive", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 192 */       .defaultValue(Boolean.valueOf(false)));
/*     */   
/* 193 */   public static final EnvironmentAttribute<Boolean> MONSTERS_BURN = register("gameplay/monsters_burn", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 194 */       .defaultValue(Boolean.valueOf(false)));
/*     */   
/* 195 */   public static final EnvironmentAttribute<Boolean> CAN_PILLAGER_PATROL_SPAWN = register("gameplay/can_pillager_patrol_spawn", EnvironmentAttribute.<Boolean>builder(AttributeTypes.BOOLEAN)
/* 196 */       .defaultValue(Boolean.valueOf(true)));
/*     */   
/* 199 */   public static final EnvironmentAttribute<Activity> VILLAGER_ACTIVITY = register("gameplay/villager_activity", EnvironmentAttribute.<Activity>builder(AttributeTypes.ACTIVITY)
/* 200 */       .defaultValue(Activity.IDLE));
/*     */   
/* 201 */   public static final EnvironmentAttribute<Activity> BABY_VILLAGER_ACTIVITY = register("gameplay/baby_villager_activity", EnvironmentAttribute.<Activity>builder(AttributeTypes.ACTIVITY)
/* 202 */       .defaultValue(Activity.IDLE));
/*     */   
/* 204 */   public static final Codec<EnvironmentAttribute<?>> CODEC = BuiltInRegistries.ENVIRONMENT_ATTRIBUTE.byNameCodec();
/*     */   
/*     */   static EnvironmentAttribute<?> bootstrap(Registry<EnvironmentAttribute<?>> registry) {
/* 207 */     return RESPAWN_ANCHOR_WORKS;
/*     */   }
/*     */   
/*     */   private static <Value> EnvironmentAttribute<Value> register(String id, EnvironmentAttribute.Builder<Value> attributeBuilder) {
/* 211 */     EnvironmentAttribute<Value> attribute = attributeBuilder.build();
/* 212 */     Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, Identifier.withDefaultNamespace(id), attribute);
/* 213 */     return attribute;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\EnvironmentAttributes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */