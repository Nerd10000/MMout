/*     */ package net.minecraft.world.timeline;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.BootstrapContext;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.EasingType;
/*     */ import net.minecraft.util.KeyframeTrack;
/*     */ import net.minecraft.util.TriState;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ import net.minecraft.world.attribute.modifier.BooleanModifier;
/*     */ import net.minecraft.world.attribute.modifier.ColorModifier;
/*     */ import net.minecraft.world.attribute.modifier.FloatModifier;
/*     */ import net.minecraft.world.clock.ClockTimeMarkers;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ import net.minecraft.world.clock.WorldClocks;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.MoonPhase;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ 
/*     */ public interface Timelines {
/*  27 */   public static final ResourceKey<Timeline> OVERWORLD_DAY = key("day");
/*     */   
/*  28 */   public static final ResourceKey<Timeline> MOON = key("moon");
/*     */   
/*  29 */   public static final ResourceKey<Timeline> VILLAGER_SCHEDULE = key("villager_schedule");
/*     */   
/*  30 */   public static final ResourceKey<Timeline> EARLY_GAME = key("early_game");
/*     */   
/*     */   public static final float DAY_SKY_LIGHT_LEVEL = 15.0F;
/*     */   
/*     */   public static final float NIGHT_SKY_LIGHT_LEVEL = 4.0F;
/*     */   
/*  35 */   public static final int NIGHT_SKY_LIGHT_COLOR = ARGB.colorFromFloat(1.0F, 0.48F, 0.48F, 1.0F);
/*     */   
/*     */   public static final float NIGHT_SKY_LIGHT_FACTOR = 0.24F;
/*     */   
/*     */   public static final int NIGHT_SKY_COLOR_MULTIPLIER = -16777216;
/*     */   
/*  39 */   public static final int NIGHT_FOG_COLOR_MULTIPLIER_START = ARGB.colorFromFloat(1.0F, 0.05F, 0.05F, 0.09F);
/*     */   
/*  40 */   public static final int NIGHT_FOG_COLOR_MULTIPLIER_END = ARGB.colorFromFloat(1.0F, 0.09F, 0.09F, 0.09F);
/*     */   
/*  41 */   public static final int NIGHT_CLOUD_COLOR_MULTIPLIER = ARGB.colorFromFloat(1.0F, 0.1F, 0.1F, 0.15F);
/*     */   
/*     */   static void bootstrap(BootstrapContext<Timeline> context) {
/*  44 */     HolderGetter<WorldClock> clocks = context.lookup(Registries.WORLD_CLOCK);
/*  45 */     Holder.Reference<WorldClock> overworldClock = clocks.getOrThrow(WorldClocks.OVERWORLD);
/*  48 */     EasingType skyAngleEase = EasingType.symmetricCubicBezier(0.362F, 0.241F);
/*  50 */     int nightStart = 12600;
/*  51 */     int nightEnd = 23401;
/*  52 */     int noon = 6000;
/*  54 */     context.register(OVERWORLD_DAY, Timeline.builder((Holder<WorldClock>)overworldClock)
/*  55 */         .setPeriodTicks(24000)
/*  56 */         .addTimeMarker(ClockTimeMarkers.DAY, 1000, true)
/*  57 */         .addTimeMarker(ClockTimeMarkers.NOON, 6000, true)
/*  58 */         .addTimeMarker(ClockTimeMarkers.NIGHT, 13000, true)
/*  59 */         .addTimeMarker(ClockTimeMarkers.MIDNIGHT, 18000, true)
/*  60 */         .addTimeMarker(ClockTimeMarkers.WAKE_UP_FROM_SLEEP, 0)
/*  61 */         .addTimeMarker(ClockTimeMarkers.ROLL_VILLAGE_SIEGE, 18000)
/*  62 */         .addTrack(EnvironmentAttributes.SUN_ANGLE, track -> track.setEasing(skyAngleEase).addKeyframe(6000, Float.valueOf(360.0F)).addKeyframe(6000, Float.valueOf(0.0F)))
/*     */         
/*  67 */         .addTrack(EnvironmentAttributes.MOON_ANGLE, track -> track.setEasing(skyAngleEase).addKeyframe(6000, Float.valueOf(540.0F)).addKeyframe(6000, Float.valueOf(180.0F)))
/*     */         
/*  72 */         .addTrack(EnvironmentAttributes.STAR_ANGLE, track -> track.setEasing(skyAngleEase).addKeyframe(6000, Float.valueOf(360.0F)).addKeyframe(6000, Float.valueOf(0.0F)))
/*     */         
/*  77 */         .addModifierTrack(EnvironmentAttributes.FIREFLY_BUSH_SOUNDS, (AttributeModifier<?, ?>)BooleanModifier.OR, track -> track.addKeyframe(12600, Boolean.valueOf(true)).addKeyframe(23401, Boolean.valueOf(false)))
/*     */         
/*  81 */         .addModifierTrack(EnvironmentAttributes.FOG_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_RGB, track -> track.addKeyframe(133, Integer.valueOf(-1)).addKeyframe(11867, Integer.valueOf(-1)).addKeyframe(13670, Integer.valueOf(NIGHT_FOG_COLOR_MULTIPLIER_START)).addKeyframe(22330, Integer.valueOf(NIGHT_FOG_COLOR_MULTIPLIER_END)))
/*     */         
/*  87 */         .addModifierTrack(EnvironmentAttributes.SKY_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_RGB, track -> track.addKeyframe(133, Integer.valueOf(-1)).addKeyframe(11867, Integer.valueOf(-1)).addKeyframe(13670, Integer.valueOf(-16777216)).addKeyframe(22330, Integer.valueOf(-16777216)))
/*     */         
/*  93 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_RGB, track -> track.addKeyframe(730, Integer.valueOf(-1)).addKeyframe(11270, Integer.valueOf(-1)).addKeyframe(13140, Integer.valueOf(NIGHT_SKY_LIGHT_COLOR)).addKeyframe(22860, Integer.valueOf(NIGHT_SKY_LIGHT_COLOR)))
/*     */         
/*  99 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_FACTOR, (AttributeModifier<?, ?>)FloatModifier.MULTIPLY, track -> track.addKeyframe(730, Float.valueOf(1.0F)).addKeyframe(11270, Float.valueOf(1.0F)).addKeyframe(13140, Float.valueOf(0.24F)).addKeyframe(22860, Float.valueOf(0.24F)))
/*     */         
/* 105 */         .addModifierTrack(EnvironmentAttributes.SKY_LIGHT_LEVEL, (AttributeModifier<?, ?>)FloatModifier.MULTIPLY, track -> track.addKeyframe(133, Float.valueOf(1.0F)).addKeyframe(11867, Float.valueOf(1.0F)).addKeyframe(13670, Float.valueOf(0.26666668F)).addKeyframe(22330, Float.valueOf(0.26666668F)))
/*     */         
/* 111 */         .addTrack(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, track -> track.addKeyframe(71, Integer.valueOf(1609540403)).addKeyframe(310, Integer.valueOf(703969843)).addKeyframe(565, Integer.valueOf(117167155)).addKeyframe(730, Integer.valueOf(16770355)).addKeyframe(11270, Integer.valueOf(16770355)).addKeyframe(11397, Integer.valueOf(83679283)).addKeyframe(11522, Integer.valueOf(268028723)).addKeyframe(11690, Integer.valueOf(703969843)).addKeyframe(11929, Integer.valueOf(1609540403)).addKeyframe(12243, Integer.valueOf(-1310226637)).addKeyframe(12358, Integer.valueOf(-857440717)).addKeyframe(12512, Integer.valueOf(-371166669)).addKeyframe(12613, Integer.valueOf(-153261261)).addKeyframe(12732, Integer.valueOf(-19242189)).addKeyframe(12841, Integer.valueOf(-19440589)).addKeyframe(13035, Integer.valueOf(-321760973)).addKeyframe(13252, Integer.valueOf(-1043577037)).addKeyframe(13775, Integer.valueOf(918435635)).addKeyframe(13888, Integer.valueOf(532362547)).addKeyframe(14039, Integer.valueOf(163001139)).addKeyframe(14192, Integer.valueOf(11744051)).addKeyframe(21807, Integer.valueOf(11678515)).addKeyframe(21961, Integer.valueOf(163001139)).addKeyframe(22112, Integer.valueOf(532362547)).addKeyframe(22225, Integer.valueOf(918435635)).addKeyframe(22748, Integer.valueOf(-1043577037)).addKeyframe(22965, Integer.valueOf(-321760973)).addKeyframe(23159, Integer.valueOf(-19440589)).addKeyframe(23272, Integer.valueOf(-19242189)).addKeyframe(23488, Integer.valueOf(-371166669)).addKeyframe(23642, Integer.valueOf(-857440717)).addKeyframe(23757, Integer.valueOf(-1310226637)))
/*     */         
/* 145 */         .addModifierTrack(EnvironmentAttributes.STAR_BRIGHTNESS, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, track -> track.addKeyframe(92, Float.valueOf(0.037F)).addKeyframe(627, Float.valueOf(0.0F)).addKeyframe(11373, Float.valueOf(0.0F)).addKeyframe(11732, Float.valueOf(0.016F)).addKeyframe(11959, Float.valueOf(0.044F)).addKeyframe(12399, Float.valueOf(0.143F)).addKeyframe(12729, Float.valueOf(0.258F)).addKeyframe(13228, Float.valueOf(0.5F)).addKeyframe(22772, Float.valueOf(0.5F)).addKeyframe(23032, Float.valueOf(0.364F)).addKeyframe(23356, Float.valueOf(0.225F)).addKeyframe(23758, Float.valueOf(0.101F)))
/*     */         
/* 159 */         .addModifierTrack(EnvironmentAttributes.CLOUD_COLOR, (AttributeModifier<?, ?>)ColorModifier.MULTIPLY_ARGB, track -> track.addKeyframe(133, Integer.valueOf(-1)).addKeyframe(11867, Integer.valueOf(-1)).addKeyframe(13670, Integer.valueOf(NIGHT_CLOUD_COLOR_MULTIPLIER)).addKeyframe(22330, Integer.valueOf(NIGHT_CLOUD_COLOR_MULTIPLIER)))
/*     */         
/* 165 */         .addTrack(EnvironmentAttributes.EYEBLOSSOM_OPEN, track -> track.addKeyframe(12600, TriState.TRUE).addKeyframe(23401, TriState.FALSE))
/*     */         
/* 169 */         .addModifierTrack(EnvironmentAttributes.CREAKING_ACTIVE, (AttributeModifier<?, ?>)BooleanModifier.OR, track -> track.addKeyframe(12600, Boolean.valueOf(true)).addKeyframe(23401, Boolean.valueOf(false)))
/*     */         
/* 173 */         .addModifierTrack(EnvironmentAttributes.TURTLE_EGG_HATCH_CHANCE, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, track -> track.setEasing(EasingType.CONSTANT).addKeyframe(21062, Float.valueOf(1.0F)).addKeyframe(21905, Float.valueOf(0.002F)))
/*     */         
/* 178 */         .addModifierTrack(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, track -> track.setEasing(EasingType.CONSTANT).addKeyframe(362, Float.valueOf(0.0F)).addKeyframe(23667, Float.valueOf(0.7F)))
/*     */         
/* 183 */         .addModifierTrack(EnvironmentAttributes.BEES_STAY_IN_HIVE, (AttributeModifier<?, ?>)BooleanModifier.OR, track -> track.addKeyframe(12542, Boolean.valueOf(true)).addKeyframe(23460, Boolean.valueOf(false)))
/*     */         
/* 187 */         .addModifierTrack(EnvironmentAttributes.MONSTERS_BURN, (AttributeModifier<?, ?>)BooleanModifier.OR, track -> track.addKeyframe(12542, Boolean.valueOf(false)).addKeyframe(23460, Boolean.valueOf(true)))
/*     */         
/* 191 */         .build());
/* 201 */     Timeline.Builder moonPhases = Timeline.builder((Holder<WorldClock>)overworldClock).setPeriodTicks(24000 * MoonPhase.COUNT).addTrack(EnvironmentAttributes.MOON_PHASE, track -> {
/*     */           for (MoonPhase phase : MoonPhase.values())
/*     */             track.addKeyframe(phase.startTick(), phase); 
/* 201 */         }).addModifierTrack(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE, (AttributeModifier<?, ?>)FloatModifier.MAXIMUM, track -> {
/*     */           track.setEasing(EasingType.CONSTANT);
/*     */           for (MoonPhase phase : MoonPhase.values())
/*     */             track.addKeyframe(phase.startTick(), Float.valueOf(DimensionType.MOON_BRIGHTNESS_PER_PHASE[phase.index()] * 0.5F)); 
/*     */         });
/* 208 */     context.register(MOON, moonPhases.build());
/* 210 */     int workStartTime = 2000;
/* 211 */     int totalWorkTime = 7000;
/* 213 */     context.register(VILLAGER_SCHEDULE, Timeline.builder((Holder<WorldClock>)overworldClock)
/* 214 */         .setPeriodTicks(24000)
/* 215 */         .addTrack(EnvironmentAttributes.VILLAGER_ACTIVITY, track -> track.addKeyframe(10, Activity.IDLE).addKeyframe(2000, Activity.WORK).addKeyframe(9000, Activity.MEET).addKeyframe(11000, Activity.IDLE).addKeyframe(12000, Activity.REST))
/*     */         
/* 222 */         .addTrack(EnvironmentAttributes.BABY_VILLAGER_ACTIVITY, track -> track.addKeyframe(10, Activity.IDLE).addKeyframe(3000, Activity.PLAY).addKeyframe(6000, Activity.IDLE).addKeyframe(10000, Activity.PLAY).addKeyframe(12000, Activity.REST))
/*     */         
/* 229 */         .build());
/* 232 */     context.register(EARLY_GAME, Timeline.builder((Holder<WorldClock>)overworldClock)
/* 233 */         .addModifierTrack(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, (AttributeModifier<?, ?>)BooleanModifier.AND, track -> track.addKeyframe(0, Boolean.valueOf(false)).addKeyframe(120000, Boolean.valueOf(true)))
/*     */         
/* 237 */         .build());
/*     */   }
/*     */   
/*     */   private static ResourceKey<Timeline> key(String id) {
/* 242 */     return ResourceKey.create(Registries.TIMELINE, Identifier.withDefaultNamespace(id));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\timeline\Timelines.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */