/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.model.animal.squid.SquidModel;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.renderer.entity.player.AvatarRenderer;
/*     */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*     */ import net.minecraft.world.entity.animal.squid.Squid;
/*     */ import net.minecraft.world.entity.player.PlayerModelType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EntityRenderers {
/*  22 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  24 */   private static final Map<EntityType<?>, EntityRendererProvider<?>> PROVIDERS = (Map<EntityType<?>, EntityRendererProvider<?>>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   static {
/*  27 */     register(EntityTypes.ACACIA_BOAT, context -> new BoatRenderer(context, ModelLayers.ACACIA_BOAT));
/*  28 */     register(EntityTypes.ACACIA_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.ACACIA_CHEST_BOAT));
/*  29 */     register(EntityTypes.ALLAY, AllayRenderer::new);
/*  30 */     register(EntityTypes.AREA_EFFECT_CLOUD, NoopRenderer::new);
/*  31 */     register(EntityTypes.ARMADILLO, ArmadilloRenderer::new);
/*  32 */     register(EntityTypes.ARMOR_STAND, ArmorStandRenderer::new);
/*  33 */     register(EntityTypes.ARROW, TippableArrowRenderer::new);
/*  34 */     register(EntityTypes.AXOLOTL, AxolotlRenderer::new);
/*  35 */     register(EntityTypes.BAMBOO_CHEST_RAFT, context -> new RaftRenderer(context, ModelLayers.BAMBOO_CHEST_RAFT));
/*  36 */     register(EntityTypes.BAMBOO_RAFT, context -> new RaftRenderer(context, ModelLayers.BAMBOO_RAFT));
/*  37 */     register(EntityTypes.BAT, BatRenderer::new);
/*  38 */     register(EntityTypes.BEE, BeeRenderer::new);
/*  39 */     register(EntityTypes.BIRCH_BOAT, context -> new BoatRenderer(context, ModelLayers.BIRCH_BOAT));
/*  40 */     register(EntityTypes.BIRCH_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.BIRCH_CHEST_BOAT));
/*  41 */     register(EntityTypes.BLAZE, BlazeRenderer::new);
/*  42 */     register(EntityTypes.BLOCK_DISPLAY, BlockDisplayRenderer::new);
/*  43 */     register(EntityTypes.BOGGED, BoggedRenderer::new);
/*  44 */     register(EntityTypes.BREEZE, BreezeRenderer::new);
/*  45 */     register(EntityTypes.BREEZE_WIND_CHARGE, WindChargeRenderer::new);
/*  46 */     register(EntityTypes.CAMEL, CamelRenderer::new);
/*  47 */     register(EntityTypes.CAMEL_HUSK, CamelHuskRenderer::new);
/*  48 */     register(EntityTypes.CAT, CatRenderer::new);
/*  49 */     register(EntityTypes.CAVE_SPIDER, CaveSpiderRenderer::new);
/*  50 */     register(EntityTypes.CHERRY_BOAT, context -> new BoatRenderer(context, ModelLayers.CHERRY_BOAT));
/*  51 */     register(EntityTypes.CHERRY_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.CHERRY_CHEST_BOAT));
/*  52 */     register(EntityTypes.CHEST_MINECART, context -> new MinecartRenderer(context, ModelLayers.CHEST_MINECART));
/*  53 */     register(EntityTypes.CHICKEN, ChickenRenderer::new);
/*  54 */     register(EntityTypes.COD, CodRenderer::new);
/*  55 */     register(EntityTypes.COMMAND_BLOCK_MINECART, context -> new MinecartRenderer(context, ModelLayers.COMMAND_BLOCK_MINECART));
/*  56 */     register(EntityTypes.COPPER_GOLEM, CopperGolemRenderer::new);
/*  57 */     register(EntityTypes.COW, CowRenderer::new);
/*  58 */     register(EntityTypes.CREAKING, CreakingRenderer::new);
/*  59 */     register(EntityTypes.CREEPER, CreeperRenderer::new);
/*  60 */     register(EntityTypes.DARK_OAK_BOAT, context -> new BoatRenderer(context, ModelLayers.DARK_OAK_BOAT));
/*  61 */     register(EntityTypes.DARK_OAK_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.DARK_OAK_CHEST_BOAT));
/*  62 */     register(EntityTypes.DOLPHIN, DolphinRenderer::new);
/*  63 */     register(EntityTypes.DONKEY, context -> new DonkeyRenderer<>(context, EquipmentClientInfo.LayerType.DONKEY_SADDLE, ModelLayers.DONKEY_SADDLE, DonkeyRenderer.Type.DONKEY, DonkeyRenderer.Type.DONKEY_BABY));
/*  64 */     register(EntityTypes.DRAGON_FIREBALL, DragonFireballRenderer::new);
/*  65 */     register(EntityTypes.DROWNED, DrownedRenderer::new);
/*  66 */     register(EntityTypes.EGG, ThrownItemRenderer::new);
/*  67 */     register(EntityTypes.ELDER_GUARDIAN, ElderGuardianRenderer::new);
/*  68 */     register(EntityTypes.ENDERMAN, EndermanRenderer::new);
/*  69 */     register(EntityTypes.ENDERMITE, EndermiteRenderer::new);
/*  70 */     register(EntityTypes.ENDER_DRAGON, EnderDragonRenderer::new);
/*  71 */     register(EntityTypes.ENDER_PEARL, ThrownItemRenderer::new);
/*  72 */     register(EntityTypes.END_CRYSTAL, EndCrystalRenderer::new);
/*  73 */     register(EntityTypes.EVOKER, EvokerRenderer::new);
/*  74 */     register(EntityTypes.EVOKER_FANGS, EvokerFangsRenderer::new);
/*  75 */     register(EntityTypes.EXPERIENCE_BOTTLE, ThrownItemRenderer::new);
/*  76 */     register(EntityTypes.EXPERIENCE_ORB, ExperienceOrbRenderer::new);
/*  77 */     register(EntityTypes.EYE_OF_ENDER, context -> new ThrownItemRenderer<>(context, 1.0F, true));
/*  78 */     register(EntityTypes.FALLING_BLOCK, FallingBlockRenderer::new);
/*  79 */     register(EntityTypes.FIREBALL, context -> new ThrownItemRenderer<>(context, 3.0F, true));
/*  80 */     register(EntityTypes.FIREWORK_ROCKET, FireworkEntityRenderer::new);
/*  81 */     register(EntityTypes.FISHING_BOBBER, FishingHookRenderer::new);
/*  82 */     register(EntityTypes.FOX, FoxRenderer::new);
/*  83 */     register(EntityTypes.FROG, FrogRenderer::new);
/*  84 */     register(EntityTypes.FURNACE_MINECART, context -> new MinecartRenderer(context, ModelLayers.FURNACE_MINECART));
/*  85 */     register(EntityTypes.GHAST, GhastRenderer::new);
/*  86 */     register(EntityTypes.HAPPY_GHAST, HappyGhastRenderer::new);
/*  87 */     register(EntityTypes.GIANT, context -> new GiantMobRenderer(context, 6.0F));
/*  88 */     register(EntityTypes.GLOW_ITEM_FRAME, ItemFrameRenderer::new);
/*  89 */     register(EntityTypes.GLOW_SQUID, context -> new GlowSquidRenderer(context, new SquidModel(context.bakeLayer(ModelLayers.GLOW_SQUID)), new SquidModel(context.bakeLayer(ModelLayers.GLOW_SQUID_BABY))));
/*  90 */     register(EntityTypes.GOAT, GoatRenderer::new);
/*  91 */     register(EntityTypes.GUARDIAN, GuardianRenderer::new);
/*  92 */     register(EntityTypes.HOGLIN, HoglinRenderer::new);
/*  93 */     register(EntityTypes.HOPPER_MINECART, context -> new MinecartRenderer(context, ModelLayers.HOPPER_MINECART));
/*  94 */     register(EntityTypes.HORSE, HorseRenderer::new);
/*  95 */     register(EntityTypes.HUSK, HuskRenderer::new);
/*  96 */     register(EntityTypes.ILLUSIONER, IllusionerRenderer::new);
/*  97 */     register(EntityTypes.INTERACTION, NoopRenderer::new);
/*  98 */     register(EntityTypes.IRON_GOLEM, IronGolemRenderer::new);
/*  99 */     register(EntityTypes.ITEM, ItemEntityRenderer::new);
/* 100 */     register(EntityTypes.ITEM_DISPLAY, ItemDisplayRenderer::new);
/* 101 */     register(EntityTypes.ITEM_FRAME, ItemFrameRenderer::new);
/* 102 */     register(EntityTypes.JUNGLE_BOAT, context -> new BoatRenderer(context, ModelLayers.JUNGLE_BOAT));
/* 103 */     register(EntityTypes.JUNGLE_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.JUNGLE_CHEST_BOAT));
/* 104 */     register(EntityTypes.LEASH_KNOT, LeashKnotRenderer::new);
/* 105 */     register(EntityTypes.LIGHTNING_BOLT, LightningBoltRenderer::new);
/* 106 */     register(EntityTypes.LINGERING_POTION, ThrownItemRenderer::new);
/* 107 */     register(EntityTypes.LLAMA, context -> new LlamaRenderer(context, ModelLayers.LLAMA, ModelLayers.LLAMA_BABY));
/* 108 */     register(EntityTypes.LLAMA_SPIT, LlamaSpitRenderer::new);
/* 109 */     register(EntityTypes.MAGMA_CUBE, MagmaCubeRenderer::new);
/* 110 */     register(EntityTypes.MANGROVE_BOAT, context -> new BoatRenderer(context, ModelLayers.MANGROVE_BOAT));
/* 111 */     register(EntityTypes.MANGROVE_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.MANGROVE_CHEST_BOAT));
/* 112 */     register(EntityTypes.MARKER, NoopRenderer::new);
/* 113 */     register(EntityTypes.MINECART, context -> new MinecartRenderer(context, ModelLayers.MINECART));
/* 114 */     register(EntityTypes.MOOSHROOM, MushroomCowRenderer::new);
/* 115 */     register(EntityTypes.MULE, context -> new DonkeyRenderer<>(context, EquipmentClientInfo.LayerType.MULE_SADDLE, ModelLayers.MULE_SADDLE, DonkeyRenderer.Type.MULE, DonkeyRenderer.Type.MULE_BABY));
/* 116 */     register(EntityTypes.NAUTILUS, NautilusRenderer::new);
/* 117 */     register(EntityTypes.OAK_BOAT, context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
/* 118 */     register(EntityTypes.OAK_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
/* 119 */     register(EntityTypes.OCELOT, OcelotRenderer::new);
/* 120 */     register(EntityTypes.OMINOUS_ITEM_SPAWNER, OminousItemSpawnerRenderer::new);
/* 121 */     register(EntityTypes.PAINTING, PaintingRenderer::new);
/* 122 */     register(EntityTypes.PALE_OAK_BOAT, context -> new BoatRenderer(context, ModelLayers.PALE_OAK_BOAT));
/* 123 */     register(EntityTypes.PALE_OAK_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.PALE_OAK_CHEST_BOAT));
/* 124 */     register(EntityTypes.PANDA, PandaRenderer::new);
/* 125 */     register(EntityTypes.PARCHED, ParchedRenderer::new);
/* 126 */     register(EntityTypes.PARROT, ParrotRenderer::new);
/* 127 */     register(EntityTypes.PHANTOM, PhantomRenderer::new);
/* 128 */     register(EntityTypes.PIG, PigRenderer::new);
/* 129 */     register(EntityTypes.PIGLIN, context -> new PiglinRenderer(context, ModelLayers.PIGLIN, ModelLayers.PIGLIN_BABY, ModelLayers.PIGLIN_ARMOR, ModelLayers.PIGLIN_BABY_ARMOR));
/* 130 */     register(EntityTypes.PIGLIN_BRUTE, context -> new PiglinRenderer(context, ModelLayers.PIGLIN_BRUTE, ModelLayers.PIGLIN_BRUTE, ModelLayers.PIGLIN_BRUTE_ARMOR, ModelLayers.PIGLIN_BRUTE_ARMOR));
/* 131 */     register(EntityTypes.PILLAGER, PillagerRenderer::new);
/* 132 */     register(EntityTypes.POLAR_BEAR, PolarBearRenderer::new);
/* 133 */     register(EntityTypes.PUFFERFISH, PufferfishRenderer::new);
/* 134 */     register(EntityTypes.RABBIT, RabbitRenderer::new);
/* 135 */     register(EntityTypes.RAVAGER, RavagerRenderer::new);
/* 136 */     register(EntityTypes.SALMON, SalmonRenderer::new);
/* 137 */     register(EntityTypes.SHEEP, SheepRenderer::new);
/* 138 */     register(EntityTypes.SHULKER, ShulkerRenderer::new);
/* 139 */     register(EntityTypes.SHULKER_BULLET, ShulkerBulletRenderer::new);
/* 140 */     register(EntityTypes.SILVERFISH, SilverfishRenderer::new);
/* 141 */     register(EntityTypes.SKELETON, SkeletonRenderer::new);
/* 142 */     register(EntityTypes.SKELETON_HORSE, context -> new UndeadHorseRenderer(context, EquipmentClientInfo.LayerType.SKELETON_HORSE_SADDLE, ModelLayers.SKELETON_HORSE_SADDLE, UndeadHorseRenderer.Type.SKELETON, UndeadHorseRenderer.Type.SKELETON_BABY));
/* 143 */     register(EntityTypes.SLIME, SlimeRenderer::new);
/* 144 */     register(EntityTypes.SMALL_FIREBALL, context -> new ThrownItemRenderer<>(context, 0.75F, true));
/* 145 */     register(EntityTypes.SNIFFER, SnifferRenderer::new);
/* 146 */     register(EntityTypes.SNOWBALL, ThrownItemRenderer::new);
/* 147 */     register(EntityTypes.SNOW_GOLEM, SnowGolemRenderer::new);
/* 148 */     register(EntityTypes.SPAWNER_MINECART, context -> new MinecartRenderer(context, ModelLayers.SPAWNER_MINECART));
/* 149 */     register(EntityTypes.SPECTRAL_ARROW, SpectralArrowRenderer::new);
/* 150 */     register(EntityTypes.SPIDER, SpiderRenderer::new);
/* 151 */     register(EntityTypes.SPLASH_POTION, ThrownItemRenderer::new);
/* 152 */     register(EntityTypes.SPRUCE_BOAT, context -> new BoatRenderer(context, ModelLayers.SPRUCE_BOAT));
/* 153 */     register(EntityTypes.SPRUCE_CHEST_BOAT, context -> new BoatRenderer(context, ModelLayers.SPRUCE_CHEST_BOAT));
/* 154 */     register(EntityTypes.SQUID, context -> new SquidRenderer<>(context, new SquidModel(context.bakeLayer(ModelLayers.SQUID)), new SquidModel(context.bakeLayer(ModelLayers.SQUID_BABY))));
/* 155 */     register(EntityTypes.STRAY, StrayRenderer::new);
/* 156 */     register(EntityTypes.STRIDER, StriderRenderer::new);
/* 157 */     register(EntityTypes.SULFUR_CUBE, SulfurCubeRenderer::new);
/* 158 */     register(EntityTypes.TADPOLE, TadpoleRenderer::new);
/* 159 */     register(EntityTypes.TEXT_DISPLAY, TextDisplayRenderer::new);
/* 160 */     register(EntityTypes.TNT, TntRenderer::new);
/* 161 */     register(EntityTypes.TNT_MINECART, TntMinecartRenderer::new);
/* 162 */     register(EntityTypes.TRADER_LLAMA, context -> new LlamaRenderer(context, ModelLayers.TRADER_LLAMA, ModelLayers.TRADER_LLAMA_BABY));
/* 163 */     register(EntityTypes.TRIDENT, ThrownTridentRenderer::new);
/* 164 */     register(EntityTypes.TROPICAL_FISH, TropicalFishRenderer::new);
/* 165 */     register(EntityTypes.TURTLE, TurtleRenderer::new);
/* 166 */     register(EntityTypes.VEX, VexRenderer::new);
/* 167 */     register(EntityTypes.VILLAGER, VillagerRenderer::new);
/* 168 */     register(EntityTypes.VINDICATOR, VindicatorRenderer::new);
/* 169 */     register(EntityTypes.WANDERING_TRADER, WanderingTraderRenderer::new);
/* 170 */     register(EntityTypes.WARDEN, WardenRenderer::new);
/* 171 */     register(EntityTypes.WIND_CHARGE, WindChargeRenderer::new);
/* 172 */     register(EntityTypes.WITCH, WitchRenderer::new);
/* 173 */     register(EntityTypes.WITHER, WitherBossRenderer::new);
/* 174 */     register(EntityTypes.WITHER_SKELETON, WitherSkeletonRenderer::new);
/* 175 */     register(EntityTypes.WITHER_SKULL, WitherSkullRenderer::new);
/* 176 */     register(EntityTypes.WOLF, WolfRenderer::new);
/* 177 */     register(EntityTypes.ZOGLIN, ZoglinRenderer::new);
/* 178 */     register(EntityTypes.ZOMBIE, ZombieRenderer::new);
/* 179 */     register(EntityTypes.ZOMBIE_HORSE, context -> new UndeadHorseRenderer(context, EquipmentClientInfo.LayerType.ZOMBIE_HORSE_SADDLE, ModelLayers.ZOMBIE_HORSE_SADDLE, UndeadHorseRenderer.Type.ZOMBIE, UndeadHorseRenderer.Type.ZOMBIE_BABY));
/* 180 */     register(EntityTypes.ZOMBIE_NAUTILUS, ZombieNautilusRenderer::new);
/* 181 */     register(EntityTypes.ZOMBIE_VILLAGER, ZombieVillagerRenderer::new);
/* 182 */     register(EntityTypes.ZOMBIFIED_PIGLIN, context -> new ZombifiedPiglinRenderer(context, ModelLayers.ZOMBIFIED_PIGLIN, ModelLayers.ZOMBIFIED_PIGLIN_BABY, ModelLayers.ZOMBIFIED_PIGLIN_ARMOR, ModelLayers.ZOMBIFIED_PIGLIN_BABY_ARMOR));
/*     */   }
/*     */   
/*     */   private static <T extends Entity> void register(EntityType<? extends T> type, EntityRendererProvider<T> renderer) {
/* 186 */     PROVIDERS.put(type, renderer);
/*     */   }
/*     */   
/*     */   public static Map<EntityType<?>, EntityRenderer<?, ?>> createEntityRenderers(EntityRendererProvider.Context context) {
/* 190 */     ImmutableMap.Builder<EntityType<?>, EntityRenderer<?, ?>> result = ImmutableMap.builder();
/* 191 */     PROVIDERS.forEach((type, provider) -> {
/*     */           try {
/*     */             result.put(type, provider.create(context));
/* 194 */           } catch (Exception e) {
/*     */             throw new IllegalArgumentException("Failed to create model for " + String.valueOf(BuiltInRegistries.ENTITY_TYPE.getKey(type)), e);
/*     */           } 
/*     */         });
/* 198 */     return (Map<EntityType<?>, EntityRenderer<?, ?>>)result.build();
/*     */   }
/*     */   
/*     */   public static <T extends net.minecraft.world.entity.Avatar & net.minecraft.client.entity.ClientAvatarEntity> Map<PlayerModelType, AvatarRenderer<T>> createAvatarRenderers(EntityRendererProvider.Context context) {
/*     */     try {
/* 203 */       return Map.of(PlayerModelType.WIDE, new AvatarRenderer(context, false), PlayerModelType.SLIM, new AvatarRenderer(context, true));
/* 207 */     } catch (Exception e) {
/* 208 */       throw new IllegalArgumentException("Failed to create avatar models", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean validateRegistrations() {
/* 213 */     boolean hasAllModels = true;
/* 214 */     for (EntityType<?> type : (Iterable<EntityType<?>>)BuiltInRegistries.ENTITY_TYPE) {
/* 215 */       if (type == EntityTypes.PLAYER || type == EntityTypes.MANNEQUIN)
/*     */         continue; 
/* 218 */       if (!PROVIDERS.containsKey(type)) {
/* 219 */         LOGGER.warn("No renderer registered for {}", BuiltInRegistries.ENTITY_TYPE.getKey(type));
/* 220 */         hasAllModels = false;
/*     */       } 
/*     */     } 
/* 223 */     return !hasAllModels;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\EntityRenderers.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */