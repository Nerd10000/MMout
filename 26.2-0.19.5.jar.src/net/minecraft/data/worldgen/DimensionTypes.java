/*     */ package net.minecraft.data.worldgen;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.biome.OverworldBiomes;
/*     */ import net.minecraft.sounds.Musics;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.TimelineTags;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.valueproviders.ConstantInt;
/*     */ import net.minecraft.util.valueproviders.IntProvider;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.attribute.AmbientSounds;
/*     */ import net.minecraft.world.attribute.BackgroundMusic;
/*     */ import net.minecraft.world.attribute.BedRule;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ import net.minecraft.world.clock.WorldClocks;
/*     */ import net.minecraft.world.level.CardinalLighting;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ import net.minecraft.world.timeline.Timelines;
/*     */ 
/*     */ public class DimensionTypes {
/*     */   public static void bootstrap(BootstrapContext<DimensionType> context) {
/*  34 */     HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
/*  35 */     HolderGetter<Timeline> timelines = context.lookup(Registries.TIMELINE);
/*  36 */     HolderGetter<WorldClock> clocks = context.lookup(Registries.WORLD_CLOCK);
/*  49 */     EnvironmentAttributeMap overworldAttributes = EnvironmentAttributeMap.builder().set(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-4138753)).set(EnvironmentAttributes.SKY_COLOR, Integer.valueOf(OverworldBiomes.calculateSkyColor(0.8F))).set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, Integer.valueOf(-16119286)).set(EnvironmentAttributes.CLOUD_COLOR, Integer.valueOf(ARGB.white(0.8F))).set(EnvironmentAttributes.CLOUD_HEIGHT, Float.valueOf(192.33F)).set(EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.OVERWORLD).set(EnvironmentAttributes.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK).set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, Boolean.valueOf(false)).set(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, Boolean.valueOf(true)).set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS).build();
/*  50 */     context.register(BuiltinDimensionTypes.OVERWORLD, new DimensionType(false, true, false, false, 1.0D, -64, 384, 384, (HolderSet)blocks
/*     */           
/*  59 */           .getOrThrow(BlockTags.INFINIBURN_OVERWORLD), 0.0F, new DimensionType.MonsterSettings(
/*     */             
/*  62 */             (IntProvider)UniformInt.of(0, 7), 0), DimensionType.Skybox.OVERWORLD, CardinalLighting.Type.DEFAULT, overworldAttributes, (HolderSet)timelines
/*     */           
/*  68 */           .getOrThrow(TimelineTags.IN_OVERWORLD), 
/*  69 */           Optional.of(clocks.getOrThrow(WorldClocks.OVERWORLD))));
/*  72 */     context.register(BuiltinDimensionTypes.NETHER, new DimensionType(true, false, true, false, 8.0D, 0, 256, 128, (HolderSet)blocks
/*     */           
/*  81 */           .getOrThrow(BlockTags.INFINIBURN_NETHER), 0.1F, new DimensionType.MonsterSettings(
/*     */             
/*  84 */             (IntProvider)ConstantInt.of(7), 15), DimensionType.Skybox.NONE, CardinalLighting.Type.NETHER, 
/*     */           
/*  89 */           EnvironmentAttributeMap.builder()
/*  90 */           .set(EnvironmentAttributes.FOG_START_DISTANCE, Float.valueOf(10.0F))
/*  91 */           .set(EnvironmentAttributes.FOG_END_DISTANCE, Float.valueOf(96.0F))
/*  92 */           .set(EnvironmentAttributes.SKY_LIGHT_COLOR, Integer.valueOf(Timelines.NIGHT_SKY_LIGHT_COLOR))
/*  93 */           .set(EnvironmentAttributes.SKY_LIGHT_LEVEL, Float.valueOf(4.0F))
/*  94 */           .set(EnvironmentAttributes.SKY_LIGHT_FACTOR, Float.valueOf(0.0F))
/*  95 */           .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, Integer.valueOf(-13621215))
/*  96 */           .set(EnvironmentAttributes.DEFAULT_DRIPSTONE_PARTICLE, ParticleTypes.DRIPPING_DRIPSTONE_LAVA)
/*  97 */           .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
/*  98 */           .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, Boolean.valueOf(true))
/*  99 */           .set(EnvironmentAttributes.WATER_EVAPORATES, Boolean.valueOf(true))
/* 100 */           .set(EnvironmentAttributes.FAST_LAVA, Boolean.valueOf(true))
/* 101 */           .set(EnvironmentAttributes.PIGLINS_ZOMBIFY, Boolean.valueOf(false))
/* 102 */           .set(EnvironmentAttributes.CAN_START_RAID, Boolean.valueOf(false))
/* 103 */           .set(EnvironmentAttributes.SNOW_GOLEM_MELTS, Boolean.valueOf(true))
/* 104 */           .build(), (HolderSet)timelines
/* 105 */           .getOrThrow(TimelineTags.IN_NETHER), 
/* 106 */           Optional.empty()));
/* 109 */     context.register(BuiltinDimensionTypes.END, new DimensionType(true, true, false, true, 1.0D, 0, 256, 256, (HolderSet)blocks
/*     */           
/* 118 */           .getOrThrow(BlockTags.INFINIBURN_END), 0.25F, new DimensionType.MonsterSettings(
/*     */             
/* 121 */             (IntProvider)ConstantInt.of(15), 0), DimensionType.Skybox.END, CardinalLighting.Type.DEFAULT, 
/*     */           
/* 126 */           EnvironmentAttributeMap.builder()
/* 127 */           .set(EnvironmentAttributes.FOG_COLOR, Integer.valueOf(-15199464))
/* 128 */           .set(EnvironmentAttributes.SKY_LIGHT_COLOR, Integer.valueOf(-5480243))
/* 129 */           .set(EnvironmentAttributes.SKY_COLOR, Integer.valueOf(-16777216))
/* 130 */           .set(EnvironmentAttributes.SKY_LIGHT_FACTOR, Float.valueOf(0.0F))
/* 131 */           .set(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, Integer.valueOf(-12630209))
/* 132 */           .set(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(Musics.END))
/* 133 */           .set(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.LEGACY_CAVE_SETTINGS)
/* 134 */           .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
/* 135 */           .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, Boolean.valueOf(false))
/* 136 */           .build(), (HolderSet)timelines
/* 137 */           .getOrThrow(TimelineTags.IN_END), 
/* 138 */           Optional.of(clocks.getOrThrow(WorldClocks.THE_END))));
/* 142 */     context.register(BuiltinDimensionTypes.OVERWORLD_CAVES, new DimensionType(false, true, true, false, 1.0D, -64, 384, 384, (HolderSet)blocks
/*     */           
/* 151 */           .getOrThrow(BlockTags.INFINIBURN_OVERWORLD), 0.0F, new DimensionType.MonsterSettings(
/*     */             
/* 154 */             (IntProvider)UniformInt.of(0, 7), 0), DimensionType.Skybox.OVERWORLD, CardinalLighting.Type.DEFAULT, overworldAttributes, (HolderSet)timelines
/*     */           
/* 160 */           .getOrThrow(TimelineTags.IN_OVERWORLD), 
/* 161 */           Optional.of(clocks.getOrThrow(WorldClocks.OVERWORLD))));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\worldgen\DimensionTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */