/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EntityType<T extends Entity> implements EntityTypeTest<Entity, T>, FeatureElement {
/*  56 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  57 */   private final Holder.Reference<EntityType<?>> builtInRegistryHolder = BuiltInRegistries.ENTITY_TYPE.createIntrusiveHolder(this);
/*     */   
/*  59 */   public static final Codec<EntityType<?>> CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec();
/*     */   
/*  60 */   public static final StreamCodec<RegistryFriendlyByteBuf, EntityType<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.ENTITY_TYPE);
/*     */   
/*     */   private final EntityFactory<T> factory;
/*     */   
/*     */   private final MobCategory category;
/*     */   
/*     */   private final TagKey<Block> immuneTo;
/*     */   
/*     */   private final boolean serialize;
/*     */   
/*     */   private final boolean summon;
/*     */   
/*     */   private final boolean fireImmune;
/*     */   
/*     */   private final boolean canSpawnFarFromPlayer;
/*     */   
/*     */   private final int clientTrackingRange;
/*     */   
/*     */   private final int updateInterval;
/*     */   
/*     */   private final String descriptionId;
/*     */   
/*     */   private Component description;
/*     */   
/*     */   private final Optional<ResourceKey<LootTable>> lootTable;
/*     */   
/*     */   private final EntityDimensions dimensions;
/*     */   
/*     */   private final float spawnDimensionsScale;
/*     */   
/*     */   private final FeatureFlagSet requiredFeatures;
/*     */   
/*     */   private final boolean allowedInPeaceful;
/*     */   
/*     */   public static Identifier getKey(EntityType<?> type) {
/*  63 */     return BuiltInRegistries.ENTITY_TYPE.getKey(type);
/*     */   }
/*     */   
/*     */   public EntityType(EntityFactory<T> factory, MobCategory category, boolean serialize, boolean summon, boolean fireImmune, boolean canSpawnFarFromPlayer, TagKey<Block> immuneTo, EntityDimensions dimensions, float spawnDimensionsScale, int clientTrackingRange, int updateInterval, String descriptionId, Optional<ResourceKey<LootTable>> lootTable, FeatureFlagSet requiredFeatures, boolean allowedInPeaceful) {
/*  84 */     this.factory = factory;
/*  85 */     this.category = category;
/*  86 */     this.canSpawnFarFromPlayer = canSpawnFarFromPlayer;
/*  87 */     this.serialize = serialize;
/*  88 */     this.summon = summon;
/*  89 */     this.fireImmune = fireImmune;
/*  90 */     this.immuneTo = immuneTo;
/*  91 */     this.dimensions = dimensions;
/*  92 */     this.spawnDimensionsScale = spawnDimensionsScale;
/*  93 */     this.clientTrackingRange = clientTrackingRange;
/*  94 */     this.updateInterval = updateInterval;
/*  95 */     this.descriptionId = descriptionId;
/*  96 */     this.lootTable = lootTable;
/*  97 */     this.requiredFeatures = requiredFeatures;
/*  98 */     this.allowedInPeaceful = allowedInPeaceful;
/*     */   }
/*     */   
/*     */   public T spawn(ServerLevel level, ItemStack itemStack, LivingEntity user, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp) {
/*     */     PostSpawnProcessor<T> postSpawnConfig;
/* 103 */     if (itemStack != null) {
/* 104 */       postSpawnConfig = createDefaultStackConfig((Level)level, itemStack, user);
/*     */     } else {
/* 106 */       postSpawnConfig = PostSpawnProcessor.nop();
/*     */     } 
/* 109 */     return spawn(level, postSpawnConfig, spawnPos, spawnReason, tryMoveDown, movedUp);
/*     */   }
/*     */   
/*     */   public static <T extends Entity> PostSpawnProcessor<T> createDefaultStackConfig(Level level, ItemStack itemStack, LivingEntity user) {
/* 116 */     return appendDefaultStackConfig(PostSpawnProcessor.nop(), level, itemStack, user);
/*     */   }
/*     */   
/*     */   public static <T extends Entity> PostSpawnProcessor<T> appendDefaultStackConfig(PostSpawnProcessor<T> initialConfig, Level level, ItemStack itemStack, LivingEntity user) {
/* 120 */     return appendCustomEntityStackConfig(appendComponentsConfig(initialConfig, itemStack), level, itemStack, user);
/*     */   }
/*     */   
/*     */   public static <T extends Entity> PostSpawnProcessor<T> appendComponentsConfig(PostSpawnProcessor<T> initialConfig, ItemStack itemStack) {
/* 124 */     return initialConfig.andThen(entity -> entity.applyComponentsFromItemStack(itemStack));
/*     */   }
/*     */   
/*     */   public static <T extends Entity> PostSpawnProcessor<T> appendCustomEntityStackConfig(PostSpawnProcessor<T> initialConfig, Level level, ItemStack itemStack, LivingEntity user) {
/* 128 */     TypedEntityData<EntityType<?>> entityData = (TypedEntityData<EntityType<?>>)itemStack.get(DataComponents.ENTITY_DATA);
/* 129 */     if (entityData != null)
/* 130 */       return initialConfig.andThen(entity -> updateCustomEntityTag(level, user, entity, entityData)); 
/* 132 */     return initialConfig;
/*     */   }
/*     */   
/*     */   public T spawn(ServerLevel level, BlockPos spawnPos, EntitySpawnReason spawnReason) {
/* 136 */     return spawn(level, null, spawnPos, spawnReason, false, false);
/*     */   }
/*     */   
/*     */   public T spawn(ServerLevel level, PostSpawnProcessor<T> postSpawnConfig, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp) {
/* 140 */     T entity = create(level, postSpawnConfig, spawnPos, spawnReason, tryMoveDown, movedUp);
/* 141 */     if (entity != null) {
/* 142 */       level.addFreshEntityWithPassengers((Entity)entity);
/* 143 */       if (entity instanceof Mob) {
/* 143 */         Mob mob = (Mob)entity;
/* 144 */         mob.playAmbientSound();
/*     */       } 
/*     */     } 
/* 147 */     return entity;
/*     */   }
/*     */   
/*     */   public T create(ServerLevel level, PostSpawnProcessor<T> postSpawnConfig, BlockPos spawnPos, EntitySpawnReason spawnReason, boolean tryMoveDown, boolean movedUp) {
/*     */     double yOff;
/* 152 */     T entity = create((Level)level, spawnReason);
/* 154 */     if (entity == null)
/* 155 */       return null; 
/* 159 */     if (tryMoveDown) {
/* 160 */       entity.setPos(spawnPos.getX() + 0.5D, (spawnPos.getY() + 1), spawnPos.getZ() + 0.5D);
/* 162 */       yOff = getYOffset((LevelReader)level, spawnPos, movedUp, entity.getBoundingBox());
/*     */     } else {
/* 164 */       yOff = 0.0D;
/*     */     } 
/* 167 */     entity.snapTo(spawnPos.getX() + 0.5D, spawnPos.getY() + yOff, spawnPos.getZ() + 0.5D, Mth.wrapDegrees(level.getRandom().nextFloat() * 360.0F), 0.0F);
/* 170 */     if (entity instanceof Mob) {
/* 170 */       Mob mob = (Mob)entity;
/* 171 */       mob.yHeadRot = mob.getYRot();
/* 172 */       mob.yBodyRot = mob.getYRot();
/* 174 */       mob.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(mob.blockPosition()), spawnReason, (SpawnGroupData)null);
/*     */     } 
/* 177 */     if (postSpawnConfig != null)
/* 178 */       postSpawnConfig.apply(entity); 
/* 181 */     return entity;
/*     */   }
/*     */   
/*     */   protected static double getYOffset(LevelReader level, BlockPos spawnPos, boolean movedUp, AABB entityBox) {
/* 185 */     AABB aabb = new AABB(spawnPos);
/* 186 */     if (movedUp)
/* 187 */       aabb = aabb.expandTowards(0.0D, -1.0D, 0.0D); 
/* 189 */     Iterable<VoxelShape> shapes = level.getCollisions(null, aabb);
/* 191 */     return 1.0D + Shapes.collide(Direction.Axis.Y, entityBox, shapes, movedUp ? -2.0D : -1.0D);
/*     */   }
/*     */   
/*     */   public static void updateCustomEntityTag(Level level, LivingEntity user, Entity entity, TypedEntityData<EntityType<?>> entityData) {
/* 195 */     MinecraftServer server = level.getServer();
/* 196 */     if (server == null || entity == null)
/*     */       return; 
/* 200 */     if (entity.getType() != entityData.type())
/*     */       return; 
/* 204 */     if (!level.isClientSide() && entity.getType().onlyOpCanSetNbt())
/* 204 */       if (user instanceof Player) {
/* 204 */         Player player = (Player)user;
/* 204 */         if (!server.getPlayerList().isOp(player.nameAndId()))
/*     */           return; 
/*     */       } else {
/*     */         return;
/*     */       }  
/* 208 */     entityData.loadInto(entity);
/*     */   }
/*     */   
/*     */   public boolean canSerialize() {
/* 212 */     return this.serialize;
/*     */   }
/*     */   
/*     */   public boolean canSummon() {
/* 216 */     return this.summon;
/*     */   }
/*     */   
/*     */   public boolean fireImmune() {
/* 220 */     return this.fireImmune;
/*     */   }
/*     */   
/*     */   public boolean canSpawnFarFromPlayer() {
/* 224 */     return this.canSpawnFarFromPlayer;
/*     */   }
/*     */   
/*     */   public MobCategory getCategory() {
/* 228 */     return this.category;
/*     */   }
/*     */   
/*     */   public String getDescriptionId() {
/* 232 */     return this.descriptionId;
/*     */   }
/*     */   
/*     */   public Component getDescription() {
/* 236 */     if (this.description == null)
/* 237 */       this.description = (Component)Component.translatable(getDescriptionId()); 
/* 239 */     return this.description;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 244 */     return getDescriptionId();
/*     */   }
/*     */   
/*     */   public String toShortString() {
/* 248 */     int dot = getDescriptionId().lastIndexOf('.');
/* 249 */     return (dot == -1) ? getDescriptionId() : getDescriptionId().substring(dot + 1);
/*     */   }
/*     */   
/*     */   public Optional<ResourceKey<LootTable>> getDefaultLootTable() {
/* 253 */     return this.lootTable;
/*     */   }
/*     */   
/*     */   public float getWidth() {
/* 257 */     return this.dimensions.width();
/*     */   }
/*     */   
/*     */   public float getHeight() {
/* 261 */     return this.dimensions.height();
/*     */   }
/*     */   
/*     */   public FeatureFlagSet requiredFeatures() {
/* 266 */     return this.requiredFeatures;
/*     */   }
/*     */   
/*     */   public boolean canSpawn(Level level) {
/* 270 */     if (!isEnabled(level.enabledFeatures()))
/* 271 */       return false; 
/* 273 */     if (!isAllowedInPeaceful() && level.getDifficulty() == Difficulty.PEACEFUL)
/* 274 */       return false; 
/* 276 */     return true;
/*     */   }
/*     */   
/*     */   public T create(Level level, EntitySpawnReason reason) {
/* 281 */     return create(level, new EntitySpawnRequest(reason, false));
/*     */   }
/*     */   
/*     */   public T create(Level level, EntitySpawnRequest request) {
/* 286 */     if (!request.ignoreChecks() && !canSpawn(level))
/* 287 */       return null; 
/* 290 */     return this.factory.create(this, level);
/*     */   }
/*     */   
/*     */   public static Optional<Entity> create(ValueInput input, Level level, EntitySpawnRequest request) {
/* 294 */     return Util.ifElse(by(input).map(type -> type.create(level, request)), entity -> entity.load(input), () -> LOGGER.warn("Skipping Entity with id {}", input.getStringOr("id", "[invalid]")));
/*     */   }
/*     */   
/*     */   public static Optional<Entity> create(EntityType<?> type, ValueInput input, Level level, EntitySpawnReason reason) {
/* 301 */     Optional<Entity> entity = Optional.ofNullable((Entity)type.create(level, reason));
/* 302 */     entity.ifPresent(e -> e.load(input));
/* 303 */     return entity;
/*     */   }
/*     */   
/*     */   public AABB getSpawnAABB(double x, double y, double z) {
/* 307 */     float halfWidth = this.spawnDimensionsScale * getWidth() / 2.0F;
/* 308 */     float height = this.spawnDimensionsScale * getHeight();
/* 309 */     return new AABB(x - halfWidth, y, z - halfWidth, x + halfWidth, y + height, z + halfWidth);
/*     */   }
/*     */   
/*     */   public boolean isBlockDangerous(BlockState state) {
/* 320 */     if (state.is(this.immuneTo))
/* 321 */       return false; 
/* 323 */     if (!this.fireImmune && NodeEvaluator.isBurningBlock(state))
/* 324 */       return true; 
/* 326 */     return (state.is(Blocks.WITHER_ROSE) || state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.CACTUS) || state.is(Blocks.POWDER_SNOW));
/*     */   }
/*     */   
/*     */   public EntityDimensions getDimensions() {
/* 330 */     return this.dimensions;
/*     */   }
/*     */   
/*     */   public static Optional<EntityType<?>> by(ValueInput input) {
/* 334 */     return input.read("id", CODEC);
/*     */   }
/*     */   
/*     */   public static Entity loadEntityRecursive(CompoundTag tag, Level level, EntitySpawnRequest request, EntityProcessor postLoad) {
/* 338 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER);
/*     */     try {
/* 339 */       Entity entity = loadEntityRecursive(TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)level.registryAccess(), tag), level, request, postLoad);
/* 340 */       reporter.close();
/*     */       return entity;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Entity loadEntityRecursive(EntityType<?> type, CompoundTag tag, Level level, EntitySpawnReason reason, EntityProcessor postLoad) {
/* 344 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER);
/*     */     try {
/* 345 */       Entity entity = loadEntityRecursive(type, TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)level.registryAccess(), tag), level, reason, postLoad);
/* 346 */       reporter.close();
/*     */       return entity;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Entity loadEntityRecursive(ValueInput input, Level level, EntitySpawnReason reason, EntityProcessor postLoad) {
/* 350 */     return loadEntityRecursive(input, level, new EntitySpawnRequest(reason, false), postLoad);
/*     */   }
/*     */   
/*     */   public static Entity loadEntityRecursive(ValueInput input, Level level, EntitySpawnRequest request, EntityProcessor postLoad) {
/* 355 */     Objects.requireNonNull(postLoad);
/* 355 */     return loadStaticEntity(input, level, request).map(postLoad::process)
/* 356 */       .map(entity -> loadPassengersRecursive(entity, input, level, request, postLoad))
/* 357 */       .orElse(null);
/*     */   }
/*     */   
/*     */   public static Entity loadEntityRecursive(EntityType<?> type, ValueInput input, Level level, EntitySpawnReason reason, EntityProcessor postLoad) {
/* 362 */     Objects.requireNonNull(postLoad);
/* 362 */     return loadStaticEntity(type, input, level, reason).map(postLoad::process)
/* 363 */       .map(entity -> loadPassengersRecursive(entity, input, level, new EntitySpawnRequest(reason, false), postLoad))
/* 364 */       .orElse(null);
/*     */   }
/*     */   
/*     */   private static Entity loadPassengersRecursive(Entity entity, ValueInput input, Level level, EntitySpawnRequest request, EntityProcessor postLoad) {
/* 368 */     for (ValueInput passengerTag : input.childrenListOrEmpty("Passengers")) {
/* 369 */       Entity passenger = loadEntityRecursive(passengerTag, level, request, postLoad);
/* 370 */       if (passenger != null)
/* 371 */         passenger.startRiding(entity, true, false); 
/*     */     } 
/* 374 */     return entity;
/*     */   }
/*     */   
/*     */   public static Stream<Entity> loadEntitiesRecursive(ValueInput.ValueInputList entities, Level level, EntitySpawnReason reason) {
/* 378 */     return entities.stream()
/* 379 */       .mapMulti((tag, output) -> loadEntityRecursive(tag, level, reason, ()));
/*     */   }
/*     */   
/*     */   private static Optional<Entity> loadStaticEntity(ValueInput input, Level level, EntitySpawnRequest request) {
/*     */     try {
/* 389 */       return create(input, level, request);
/* 390 */     } catch (RuntimeException e) {
/* 391 */       LOGGER.warn("Exception loading entity: ", e);
/* 392 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<Entity> loadStaticEntity(EntityType<?> type, ValueInput input, Level level, EntitySpawnReason reason) {
/*     */     try {
/* 398 */       return create(type, input, level, reason);
/* 399 */     } catch (RuntimeException e) {
/* 400 */       LOGGER.warn("Exception loading entity: ", e);
/* 401 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public int clientTrackingRange() {
/* 406 */     return this.clientTrackingRange;
/*     */   }
/*     */   
/*     */   public int updateInterval() {
/* 410 */     return this.updateInterval;
/*     */   }
/*     */   
/*     */   public boolean trackDeltas() {
/* 415 */     return (this != EntityTypes.PLAYER && this != EntityTypes.LLAMA_SPIT && this != EntityTypes.WITHER && this != EntityTypes.BAT && this != EntityTypes.ITEM_FRAME && this != EntityTypes.GLOW_ITEM_FRAME && this != EntityTypes.LEASH_KNOT && this != EntityTypes.PAINTING && this != EntityTypes.END_CRYSTAL && this != EntityTypes.EVOKER_FANGS);
/*     */   }
/*     */   
/*     */   public T tryCast(Entity entity) {
/* 431 */     return (entity.getType() == this) ? (T)entity : null;
/*     */   }
/*     */   
/*     */   public Class<? extends Entity> getBaseClass() {
/* 436 */     return Entity.class;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Holder.Reference<EntityType<?>> builtInRegistryHolder() {
/* 442 */     return this.builtInRegistryHolder;
/*     */   }
/*     */   
/*     */   public boolean isAllowedInPeaceful() {
/* 446 */     return this.allowedInPeaceful;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface EntityFactory<T extends Entity> {
/*     */     T create(EntityType<T> param1EntityType, Level param1Level);
/*     */   }
/*     */   
/*     */   public static class Builder<T extends Entity> {
/*     */     private final EntityType.EntityFactory<T> factory;
/*     */     
/*     */     private final MobCategory category;
/*     */     
/* 452 */     private TagKey<Block> immuneTo = BlockTags.DEFAULT_IMMUNE_TO;
/*     */     
/*     */     private boolean serialize = true;
/*     */     
/*     */     private boolean summon = true;
/*     */     
/*     */     private boolean fireImmune;
/*     */     
/*     */     private boolean canSpawnFarFromPlayer;
/*     */     
/* 457 */     private int clientTrackingRange = 5;
/*     */     
/* 458 */     private int updateInterval = 3;
/*     */     
/* 459 */     private EntityDimensions dimensions = EntityDimensions.scalable(0.6F, 1.8F);
/*     */     
/* 460 */     private float spawnDimensionsScale = 1.0F;
/*     */     
/* 461 */     private EntityAttachments.Builder attachments = EntityAttachments.builder();
/*     */     
/* 462 */     private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */     
/*     */     private DependantName<EntityType<?>, Optional<ResourceKey<LootTable>>> lootTable;
/*     */     
/*     */     private final DependantName<EntityType<?>, String> descriptionId;
/*     */     
/*     */     private boolean allowedInPeaceful;
/*     */     
/*     */     private Builder(EntityType.EntityFactory<T> factory, MobCategory category) {
/* 463 */       this.lootTable = (id -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("entities/"))));
/* 464 */       this.descriptionId = (id -> Util.makeDescriptionId("entity", id.identifier()));
/* 465 */       this.allowedInPeaceful = true;
/* 468 */       this.factory = factory;
/* 469 */       this.category = category;
/* 470 */       this.canSpawnFarFromPlayer = (category == MobCategory.CREATURE || category == MobCategory.MISC);
/*     */     }
/*     */     
/*     */     public static <T extends Entity> Builder<T> of(EntityType.EntityFactory<T> factory, MobCategory category) {
/* 474 */       return new Builder<>(factory, category);
/*     */     }
/*     */     
/*     */     public static <T extends Entity> Builder<T> createNothing(MobCategory category) {
/* 478 */       return new Builder<>((t, l) -> null, category);
/*     */     }
/*     */     
/*     */     public Builder<T> sized(float width, float height) {
/* 482 */       this.dimensions = EntityDimensions.scalable(width, height);
/* 483 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> spawnDimensionsScale(float scale) {
/* 487 */       this.spawnDimensionsScale = scale;
/* 488 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> eyeHeight(float eyeHeight) {
/* 492 */       this.dimensions = this.dimensions.withEyeHeight(eyeHeight);
/* 493 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> passengerAttachments(float... offsetYs) {
/* 497 */       for (float offsetY : offsetYs)
/* 498 */         this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, 0.0F, offsetY, 0.0F); 
/* 500 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> passengerAttachments(Vec3... points) {
/* 504 */       for (Vec3 point : points)
/* 505 */         this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, point); 
/* 507 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> vehicleAttachment(Vec3 point) {
/* 511 */       return attach(EntityAttachment.VEHICLE, point);
/*     */     }
/*     */     
/*     */     public Builder<T> ridingOffset(float ridingOffset) {
/* 515 */       return attach(EntityAttachment.VEHICLE, 0.0F, -ridingOffset, 0.0F);
/*     */     }
/*     */     
/*     */     public Builder<T> nameTagOffset(float nameTagOffset) {
/* 519 */       return attach(EntityAttachment.NAME_TAG, 0.0F, nameTagOffset, 0.0F);
/*     */     }
/*     */     
/*     */     public Builder<T> attach(EntityAttachment attachment, float x, float y, float z) {
/* 523 */       this.attachments = this.attachments.attach(attachment, x, y, z);
/* 524 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> attach(EntityAttachment attachment, Vec3 point) {
/* 528 */       this.attachments = this.attachments.attach(attachment, point);
/* 529 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> noSummon() {
/* 533 */       this.summon = false;
/* 534 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> noSave() {
/* 538 */       this.serialize = false;
/* 539 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> fireImmune() {
/* 543 */       this.fireImmune = true;
/* 544 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> immuneTo(TagKey<Block> tag) {
/* 548 */       this.immuneTo = tag;
/* 549 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> canSpawnFarFromPlayer() {
/* 553 */       this.canSpawnFarFromPlayer = true;
/* 554 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> clientTrackingRange(int clientChunkRange) {
/* 558 */       this.clientTrackingRange = clientChunkRange;
/* 559 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> updateInterval(int updateInterval) {
/* 563 */       this.updateInterval = updateInterval;
/* 564 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> requiredFeatures(FeatureFlag... flags) {
/* 568 */       this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 569 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> noLootTable() {
/* 573 */       this.lootTable = DependantName.fixed(Optional.empty());
/* 574 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> notInPeaceful() {
/* 578 */       this.allowedInPeaceful = false;
/* 579 */       return this;
/*     */     }
/*     */     
/*     */     public EntityType<T> build(ResourceKey<EntityType<?>> name) {
/* 583 */       if (this.serialize)
/* 584 */         Util.fetchChoiceType(References.ENTITY_TREE, name.identifier().toString()); 
/* 587 */       return new EntityType<>(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions.withAttachments(this.attachments), this.spawnDimensionsScale, this.clientTrackingRange, this.updateInterval, (String)this.descriptionId.get(name), (Optional<ResourceKey<LootTable>>)this.lootTable.get(name), this.requiredFeatures, this.allowedInPeaceful);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onlyOpCanSetNbt() {
/* 592 */     return EntityTypes.OP_ONLY_CUSTOM_DATA.contains(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */