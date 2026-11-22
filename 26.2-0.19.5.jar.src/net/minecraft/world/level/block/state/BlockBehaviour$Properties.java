/*     */ package net.minecraft.world.level.block.state;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.SoundType;
/*     */ import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
/*     */ import net.minecraft.world.level.material.MapColor;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Properties {
/* 439 */   public static final Codec<Properties> CODEC = MapCodec.unitCodec(() -> of());
/*     */   
/*     */   private Function<BlockState, MapColor> mapColor = state -> MapColor.NONE;
/*     */   
/*     */   private boolean hasCollision = true;
/*     */   
/* 444 */   private SoundType soundType = SoundType.STONE;
/*     */   
/*     */   private ToIntFunction<BlockState> lightEmission = state -> 0;
/*     */   
/*     */   private float explosionResistance;
/*     */   
/*     */   private float destroyTime;
/*     */   
/*     */   private boolean requiresCorrectToolForDrops;
/*     */   
/*     */   private boolean isRandomlyTicking;
/*     */   
/* 450 */   private float friction = 0.6F;
/*     */   
/* 451 */   private float speedFactor = 1.0F;
/*     */   
/* 452 */   private float jumpFactor = 1.0F;
/*     */   
/*     */   private float bounceRestitution;
/*     */   
/*     */   private ResourceKey<Block> id;
/*     */   
/*     */   private DependantName<Block, Optional<ResourceKey<LootTable>>> drops;
/*     */   
/*     */   private DependantName<Block, String> descriptionId;
/*     */   
/*     */   private boolean canOcclude;
/*     */   
/*     */   private boolean isAir;
/*     */   
/*     */   private boolean ignitedByLava;
/*     */   
/*     */   @Deprecated
/*     */   private boolean liquid;
/*     */   
/*     */   @Deprecated
/*     */   private boolean forceSolidOff;
/*     */   
/*     */   private boolean forceSolidOn;
/*     */   
/*     */   private PushReaction pushReaction;
/*     */   
/*     */   private boolean spawnTerrainParticles;
/*     */   
/*     */   private NoteBlockInstrument instrument;
/*     */   
/*     */   private boolean replaceable;
/*     */   
/*     */   private BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn;
/*     */   
/*     */   private BlockBehaviour.StatePredicate isRedstoneConductor;
/*     */   
/*     */   private BlockBehaviour.StatePredicate isSuffocating;
/*     */   
/*     */   private BlockBehaviour.StatePredicate isViewBlocking;
/*     */   
/*     */   private BlockBehaviour.PostProcess postProcess;
/*     */   
/*     */   private Predicate<BlockState> emissiveRendering;
/*     */   
/*     */   private boolean dynamicShape;
/*     */   
/*     */   private FeatureFlagSet requiredFeatures;
/*     */   
/*     */   private BlockBehaviour.OffsetFunction offsetFunction;
/*     */   
/*     */   private Properties() {
/* 456 */     this.drops = (id -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("blocks/"))));
/* 457 */     this.descriptionId = (id -> Util.makeDescriptionId("block", id.identifier()));
/* 458 */     this.canOcclude = true;
/* 466 */     this.pushReaction = PushReaction.NORMAL;
/* 467 */     this.spawnTerrainParticles = true;
/* 468 */     this.instrument = NoteBlockInstrument.HARP;
/* 471 */     this.isValidSpawn = ((state, level, pos, entityType) -> 
/* 472 */       (state.isFaceSturdy(level, pos, Direction.UP) && state.getLightEmission() < 14));
/* 474 */     this.isRedstoneConductor = BlockBehaviour.BlockStateBase::isCollisionShapeFullBlock;
/* 476 */     this.isSuffocating = ((state, level, pos) -> 
/* 477 */       (state.blocksMotion() && state.isCollisionShapeFullBlock(level, pos)));
/* 479 */     this.isViewBlocking = this.isSuffocating;
/* 480 */     this.postProcess = ((state, level, pos) -> null);
/* 481 */     this.emissiveRendering = (paramBlockState -> false);
/* 484 */     this.requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */   }
/*     */   
/*     */   public static Properties of() {
/* 492 */     return new Properties();
/*     */   }
/*     */   
/*     */   public static Properties ofFullCopy(BlockBehaviour block) {
/* 502 */     Properties copyTo = ofLegacyCopy(block);
/* 503 */     Properties copyFrom = block.properties;
/* 505 */     copyTo.jumpFactor = copyFrom.jumpFactor;
/* 506 */     copyTo.isRedstoneConductor = copyFrom.isRedstoneConductor;
/* 507 */     copyTo.isValidSpawn = copyFrom.isValidSpawn;
/* 508 */     copyTo.postProcess = copyFrom.postProcess;
/* 509 */     copyTo.isSuffocating = copyFrom.isSuffocating;
/* 510 */     copyTo.isViewBlocking = copyFrom.isViewBlocking;
/* 511 */     copyTo.drops = copyFrom.drops;
/* 512 */     copyTo.descriptionId = copyFrom.descriptionId;
/* 514 */     return copyTo;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static Properties ofLegacyCopy(BlockBehaviour block) {
/* 521 */     Properties copyTo = new Properties();
/* 522 */     Properties copyFrom = block.properties;
/* 524 */     copyTo.destroyTime = copyFrom.destroyTime;
/* 525 */     copyTo.explosionResistance = copyFrom.explosionResistance;
/* 526 */     copyTo.hasCollision = copyFrom.hasCollision;
/* 527 */     copyTo.isRandomlyTicking = copyFrom.isRandomlyTicking;
/* 528 */     copyTo.lightEmission = copyFrom.lightEmission;
/* 529 */     copyTo.mapColor = copyFrom.mapColor;
/* 530 */     copyTo.soundType = copyFrom.soundType;
/* 531 */     copyTo.friction = copyFrom.friction;
/* 532 */     copyTo.speedFactor = copyFrom.speedFactor;
/* 533 */     copyTo.bounceRestitution = copyFrom.bounceRestitution;
/* 534 */     copyTo.dynamicShape = copyFrom.dynamicShape;
/* 535 */     copyTo.canOcclude = copyFrom.canOcclude;
/* 536 */     copyTo.isAir = copyFrom.isAir;
/* 537 */     copyTo.ignitedByLava = copyFrom.ignitedByLava;
/* 538 */     copyTo.liquid = copyFrom.liquid;
/* 539 */     copyTo.forceSolidOff = copyFrom.forceSolidOff;
/* 540 */     copyTo.forceSolidOn = copyFrom.forceSolidOn;
/* 541 */     copyTo.pushReaction = copyFrom.pushReaction;
/* 542 */     copyTo.requiresCorrectToolForDrops = copyFrom.requiresCorrectToolForDrops;
/* 543 */     copyTo.offsetFunction = copyFrom.offsetFunction;
/* 544 */     copyTo.spawnTerrainParticles = copyFrom.spawnTerrainParticles;
/* 545 */     copyTo.requiredFeatures = copyFrom.requiredFeatures;
/* 546 */     copyTo.emissiveRendering = copyFrom.emissiveRendering;
/* 547 */     copyTo.instrument = copyFrom.instrument;
/* 548 */     copyTo.replaceable = copyFrom.replaceable;
/* 550 */     return copyTo;
/*     */   }
/*     */   
/*     */   public Properties mapColor(DyeColor dyeColor) {
/* 554 */     this.mapColor = (state -> dyeColor.getMapColor());
/* 555 */     return this;
/*     */   }
/*     */   
/*     */   public Properties mapColor(MapColor mapColor) {
/* 559 */     this.mapColor = (state -> mapColor);
/* 560 */     return this;
/*     */   }
/*     */   
/*     */   public Properties mapColor(Function<BlockState, MapColor> mapColor) {
/* 564 */     this.mapColor = mapColor;
/* 565 */     return this;
/*     */   }
/*     */   
/*     */   public Properties noCollision() {
/* 569 */     this.hasCollision = false;
/* 570 */     this.canOcclude = false;
/* 571 */     return this;
/*     */   }
/*     */   
/*     */   public Properties noOcclusion() {
/* 575 */     this.canOcclude = false;
/* 576 */     return this;
/*     */   }
/*     */   
/*     */   public Properties friction(float friction) {
/* 580 */     this.friction = friction;
/* 581 */     return this;
/*     */   }
/*     */   
/*     */   public Properties speedFactor(float speedFactor) {
/* 585 */     this.speedFactor = speedFactor;
/* 586 */     return this;
/*     */   }
/*     */   
/*     */   public Properties jumpFactor(float jumpFactor) {
/* 590 */     this.jumpFactor = jumpFactor;
/* 591 */     return this;
/*     */   }
/*     */   
/*     */   public Properties bounceRestitution(float bounceRestitution) {
/* 595 */     this.bounceRestitution = bounceRestitution;
/* 596 */     return this;
/*     */   }
/*     */   
/*     */   public Properties sound(SoundType soundType) {
/* 600 */     this.soundType = soundType;
/* 601 */     return this;
/*     */   }
/*     */   
/*     */   public Properties lightLevel(ToIntFunction<BlockState> lightEmission) {
/* 605 */     this.lightEmission = lightEmission;
/* 606 */     return this;
/*     */   }
/*     */   
/*     */   public Properties strength(float destroyTime, float explosionResistance) {
/* 610 */     return destroyTime(destroyTime).explosionResistance(explosionResistance);
/*     */   }
/*     */   
/*     */   public Properties instabreak() {
/* 614 */     return strength(0.0F);
/*     */   }
/*     */   
/*     */   public Properties strength(float destroyTime) {
/* 618 */     strength(destroyTime, destroyTime);
/* 619 */     return this;
/*     */   }
/*     */   
/*     */   public Properties randomTicks() {
/* 623 */     this.isRandomlyTicking = true;
/* 624 */     return this;
/*     */   }
/*     */   
/*     */   public Properties dynamicShape() {
/* 628 */     this.dynamicShape = true;
/* 629 */     return this;
/*     */   }
/*     */   
/*     */   public Properties noLootTable() {
/* 635 */     this.drops = DependantName.fixed(Optional.empty());
/* 636 */     return this;
/*     */   }
/*     */   
/*     */   public Properties overrideLootTable(Optional<ResourceKey<LootTable>> table) {
/* 640 */     this.drops = DependantName.fixed(table);
/* 641 */     return this;
/*     */   }
/*     */   
/*     */   protected Optional<ResourceKey<LootTable>> effectiveDrops() {
/* 645 */     return (Optional<ResourceKey<LootTable>>)this.drops.get(Objects.<ResourceKey>requireNonNull(this.id, "Block id not set"));
/*     */   }
/*     */   
/*     */   public Properties ignitedByLava() {
/* 649 */     this.ignitedByLava = true;
/* 650 */     return this;
/*     */   }
/*     */   
/*     */   public Properties liquid() {
/* 654 */     this.liquid = true;
/* 655 */     return this;
/*     */   }
/*     */   
/*     */   public Properties forceSolidOn() {
/* 660 */     this.forceSolidOn = true;
/* 661 */     return this;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Properties forceSolidOff() {
/* 668 */     this.forceSolidOff = true;
/* 669 */     return this;
/*     */   }
/*     */   
/*     */   public Properties pushReaction(PushReaction pushReaction) {
/* 673 */     this.pushReaction = pushReaction;
/* 674 */     return this;
/*     */   }
/*     */   
/*     */   public Properties air() {
/* 678 */     this.isAir = true;
/* 679 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn) {
/* 683 */     this.isValidSpawn = isValidSpawn;
/* 684 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isRedstoneConductor(BlockBehaviour.StatePredicate isRedstoneConductor) {
/* 688 */     this.isRedstoneConductor = isRedstoneConductor;
/* 689 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isSuffocating(BlockBehaviour.StatePredicate isSuffocating) {
/* 693 */     this.isSuffocating = isSuffocating;
/* 694 */     return this;
/*     */   }
/*     */   
/*     */   public Properties isViewBlocking(BlockBehaviour.StatePredicate isViewBlocking) {
/* 698 */     this.isViewBlocking = isViewBlocking;
/* 699 */     return this;
/*     */   }
/*     */   
/*     */   public Properties postProcess(BlockBehaviour.PostProcess postProcess) {
/* 704 */     this.postProcess = postProcess;
/* 705 */     return this;
/*     */   }
/*     */   
/*     */   public Properties emissiveRendering(Predicate<BlockState> emissiveRendering) {
/* 709 */     this.emissiveRendering = emissiveRendering;
/* 710 */     return this;
/*     */   }
/*     */   
/*     */   public Properties requiresCorrectToolForDrops() {
/* 714 */     this.requiresCorrectToolForDrops = true;
/* 715 */     return this;
/*     */   }
/*     */   
/*     */   public Properties destroyTime(float destroyTime) {
/* 719 */     this.destroyTime = destroyTime;
/* 720 */     return this;
/*     */   }
/*     */   
/*     */   public Properties explosionResistance(float explosionResistance) {
/* 724 */     this.explosionResistance = Math.max(0.0F, explosionResistance);
/* 725 */     return this;
/*     */   }
/*     */   
/*     */   public Properties offsetType(BlockBehaviour.OffsetType offsetType) {
/* 729 */     switch (offsetType.ordinal()) {
/*     */       default:
/* 729 */         throw new MatchException(null, null);
/*     */       case 0:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 1:
/*     */         break;
/*     */     } 
/* 729 */     this.offsetFunction = ((state, pos) -> {
/*     */         Block block = state.getBlock();
/*     */         long seed = Mth.getSeed(pos.getX(), 0, pos.getZ());
/*     */         float maxHorizontalOffset = block.getMaxHorizontalOffset();
/*     */         double x = Mth.clamp((((float)(seed & 0xFL) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
/*     */         double z = Mth.clamp((((float)(seed >> 8L & 0xFL) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
/*     */         return new Vec3(x, 0.0D, z);
/*     */       });
/* 751 */     return this;
/*     */   }
/*     */   
/*     */   public Properties noTerrainParticles() {
/* 755 */     this.spawnTerrainParticles = false;
/* 756 */     return this;
/*     */   }
/*     */   
/*     */   public Properties requiredFeatures(FeatureFlag... flags) {
/* 760 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 761 */     return this;
/*     */   }
/*     */   
/*     */   public Properties instrument(NoteBlockInstrument instrument) {
/* 765 */     this.instrument = instrument;
/* 766 */     return this;
/*     */   }
/*     */   
/*     */   public Properties replaceable() {
/* 770 */     this.replaceable = true;
/* 771 */     return this;
/*     */   }
/*     */   
/*     */   public Properties setId(ResourceKey<Block> id) {
/* 775 */     this.id = id;
/* 776 */     return this;
/*     */   }
/*     */   
/*     */   public Properties overrideDescription(String descriptionId) {
/* 780 */     this.descriptionId = DependantName.fixed(descriptionId);
/* 781 */     return this;
/*     */   }
/*     */   
/*     */   protected String effectiveDescriptionId() {
/* 785 */     return (String)this.descriptionId.get(Objects.<ResourceKey>requireNonNull(this.id, "Block id not set"));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\BlockBehaviour$Properties.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */