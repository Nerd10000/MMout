/*     */ package net.minecraft.data.registries;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistrySetBuilder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.worldgen.Carvers;
/*     */ import net.minecraft.data.worldgen.DimensionTypes;
/*     */ import net.minecraft.data.worldgen.NoiseData;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.data.worldgen.ProcessorLists;
/*     */ import net.minecraft.data.worldgen.StructureSets;
/*     */ import net.minecraft.data.worldgen.Structures;
/*     */ import net.minecraft.data.worldgen.biome.BiomeData;
/*     */ import net.minecraft.data.worldgen.features.FeatureUtils;
/*     */ import net.minecraft.data.worldgen.placement.PlacementUtils;
/*     */ import net.minecraft.gametest.framework.GameTestEnvironments;
/*     */ import net.minecraft.gametest.framework.GameTestInstances;
/*     */ import net.minecraft.network.chat.ChatType;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.dialog.Dialogs;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.clock.WorldClocks;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.entity.SulfurCubeArchetypes;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenSoundVariants;
/*     */ import net.minecraft.world.entity.animal.chicken.ChickenVariants;
/*     */ import net.minecraft.world.entity.animal.cow.CowSoundVariants;
/*     */ import net.minecraft.world.entity.animal.cow.CowVariants;
/*     */ import net.minecraft.world.entity.animal.feline.CatSoundVariants;
/*     */ import net.minecraft.world.entity.animal.feline.CatVariants;
/*     */ import net.minecraft.world.entity.animal.frog.FrogVariants;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariants;
/*     */ import net.minecraft.world.entity.animal.pig.PigSoundVariants;
/*     */ import net.minecraft.world.entity.animal.pig.PigVariants;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;
/*     */ import net.minecraft.world.entity.animal.wolf.WolfVariants;
/*     */ import net.minecraft.world.entity.decoration.painting.PaintingVariants;
/*     */ import net.minecraft.world.item.Instruments;
/*     */ import net.minecraft.world.item.JukeboxSongs;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterials;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPatterns;
/*     */ import net.minecraft.world.item.trading.TradeSets;
/*     */ import net.minecraft.world.item.trading.VillagerTrades;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
/*     */ import net.minecraft.world.level.block.entity.BannerPatterns;
/*     */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfigs;
/*     */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.NoiseRouterData;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
/*     */ import net.minecraft.world.level.levelgen.placement.BiomeFilter;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.timeline.Timelines;
/*     */ 
/*     */ public class VanillaRegistries {
/*  69 */   private static final RegistrySetBuilder BUILDER = (new RegistrySetBuilder())
/*  70 */     .add(Registries.DIMENSION_TYPE, DimensionTypes::bootstrap)
/*  71 */     .add(Registries.CONFIGURED_CARVER, Carvers::bootstrap)
/*  72 */     .add(Registries.CONFIGURED_FEATURE, FeatureUtils::bootstrap)
/*  73 */     .add(Registries.PLACED_FEATURE, PlacementUtils::bootstrap)
/*  74 */     .add(Registries.STRUCTURE, Structures::bootstrap)
/*  75 */     .add(Registries.STRUCTURE_SET, StructureSets::bootstrap)
/*  76 */     .add(Registries.PROCESSOR_LIST, ProcessorLists::bootstrap)
/*  77 */     .add(Registries.TEMPLATE_POOL, Pools::bootstrap)
/*  78 */     .add(Registries.BIOME, BiomeData::bootstrap)
/*  79 */     .add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, MultiNoiseBiomeSourceParameterLists::bootstrap)
/*  80 */     .add(Registries.NOISE, NoiseData::bootstrap)
/*  81 */     .add(Registries.DENSITY_FUNCTION, NoiseRouterData::bootstrap)
/*  82 */     .add(Registries.NOISE_SETTINGS, NoiseGeneratorSettings::bootstrap)
/*  83 */     .add(Registries.WORLD_PRESET, WorldPresets::bootstrap)
/*  84 */     .add(Registries.FLAT_LEVEL_GENERATOR_PRESET, FlatLevelGeneratorPresets::bootstrap)
/*  85 */     .add(Registries.CHAT_TYPE, ChatType::bootstrap)
/*  86 */     .add(Registries.TRIM_PATTERN, TrimPatterns::bootstrap)
/*  87 */     .add(Registries.TRIM_MATERIAL, TrimMaterials::bootstrap)
/*  88 */     .add(Registries.TRIAL_SPAWNER_CONFIG, TrialSpawnerConfigs::bootstrap)
/*  89 */     .add(Registries.WOLF_VARIANT, WolfVariants::bootstrap)
/*  90 */     .add(Registries.WOLF_SOUND_VARIANT, WolfSoundVariants::bootstrap)
/*  91 */     .add(Registries.PAINTING_VARIANT, PaintingVariants::bootstrap)
/*  92 */     .add(Registries.DAMAGE_TYPE, DamageTypes::bootstrap)
/*  93 */     .add(Registries.BANNER_PATTERN, BannerPatterns::bootstrap)
/*  94 */     .add(Registries.ENCHANTMENT, Enchantments::bootstrap)
/*  95 */     .add(Registries.ENCHANTMENT_PROVIDER, VanillaEnchantmentProviders::bootstrap)
/*  96 */     .add(Registries.JUKEBOX_SONG, JukeboxSongs::bootstrap)
/*  97 */     .add(Registries.INSTRUMENT, Instruments::bootstrap)
/*  98 */     .add(Registries.PIG_VARIANT, PigVariants::bootstrap)
/*  99 */     .add(Registries.PIG_SOUND_VARIANT, PigSoundVariants::bootstrap)
/* 100 */     .add(Registries.COW_VARIANT, CowVariants::bootstrap)
/* 101 */     .add(Registries.COW_SOUND_VARIANT, CowSoundVariants::bootstrap)
/* 102 */     .add(Registries.CHICKEN_VARIANT, ChickenVariants::bootstrap)
/* 103 */     .add(Registries.CHICKEN_SOUND_VARIANT, ChickenSoundVariants::bootstrap)
/* 104 */     .add(Registries.ZOMBIE_NAUTILUS_VARIANT, ZombieNautilusVariants::bootstrap)
/* 105 */     .add(Registries.SULFUR_CUBE_ARCHETYPE, SulfurCubeArchetypes::bootstrap)
/* 106 */     .add(Registries.TEST_ENVIRONMENT, GameTestEnvironments::bootstrap)
/* 107 */     .add(Registries.TEST_INSTANCE, GameTestInstances::bootstrap)
/* 108 */     .add(Registries.FROG_VARIANT, FrogVariants::bootstrap)
/* 109 */     .add(Registries.CAT_VARIANT, CatVariants::bootstrap)
/* 110 */     .add(Registries.CAT_SOUND_VARIANT, CatSoundVariants::bootstrap)
/* 111 */     .add(Registries.DIALOG, Dialogs::bootstrap)
/* 112 */     .add(Registries.WORLD_CLOCK, WorldClocks::bootstrap)
/* 113 */     .add(Registries.TIMELINE, Timelines::bootstrap)
/* 114 */     .add(Registries.VILLAGER_TRADE, VillagerTrades::bootstrap)
/* 115 */     .add(Registries.TRADE_SET, TradeSets::bootstrap);
/*     */   
/*     */   private static void validateThatAllBiomeFeaturesHaveBiomeFilter(HolderLookup.Provider provider) {
/* 118 */     validateThatAllBiomeFeaturesHaveBiomeFilter((HolderGetter<PlacedFeature>)provider.lookupOrThrow(Registries.PLACED_FEATURE), (HolderLookup<Biome>)provider.lookupOrThrow(Registries.BIOME));
/*     */   }
/*     */   
/*     */   public static void validateThatAllBiomeFeaturesHaveBiomeFilter(HolderGetter<PlacedFeature> placedFeatures, HolderLookup<Biome> biomes) {
/* 125 */     biomes.listElements().forEach(biome -> {
/*     */           Identifier biomeKey = biome.key().identifier();
/*     */           List<HolderSet<PlacedFeature>> biomeFeatures = ((Biome)biome.value()).getGenerationSettings().features();
/*     */           biomeFeatures.stream().flatMap(HolderSet::stream).forEach(());
/*     */         });
/*     */   }
/*     */   
/*     */   private static boolean validatePlacedFeature(PlacedFeature value) {
/* 146 */     return value.placement().contains(BiomeFilter.biome());
/*     */   }
/*     */   
/*     */   public static HolderLookup.Provider createLookup() {
/* 150 */     RegistryAccess.Frozen staticRegistries = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
/* 151 */     HolderLookup.Provider newRegistries = BUILDER.build((RegistryAccess)staticRegistries);
/* 152 */     validateThatAllBiomeFeaturesHaveBiomeFilter(newRegistries);
/* 153 */     return newRegistries;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\registries\VanillaRegistries.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */