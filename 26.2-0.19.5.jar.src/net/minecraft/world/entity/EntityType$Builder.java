/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.DependantName;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Builder<T extends Entity> {
/*     */   private final EntityType.EntityFactory<T> factory;
/*     */   
/*     */   private final MobCategory category;
/*     */   
/* 452 */   private TagKey<Block> immuneTo = BlockTags.DEFAULT_IMMUNE_TO;
/*     */   
/*     */   private boolean serialize = true;
/*     */   
/*     */   private boolean summon = true;
/*     */   
/*     */   private boolean fireImmune;
/*     */   
/*     */   private boolean canSpawnFarFromPlayer;
/*     */   
/* 457 */   private int clientTrackingRange = 5;
/*     */   
/* 458 */   private int updateInterval = 3;
/*     */   
/* 459 */   private EntityDimensions dimensions = EntityDimensions.scalable(0.6F, 1.8F);
/*     */   
/* 460 */   private float spawnDimensionsScale = 1.0F;
/*     */   
/* 461 */   private EntityAttachments.Builder attachments = EntityAttachments.builder();
/*     */   
/* 462 */   private FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;
/*     */   
/*     */   private DependantName<EntityType<?>, Optional<ResourceKey<LootTable>>> lootTable;
/*     */   
/*     */   private final DependantName<EntityType<?>, String> descriptionId;
/*     */   
/*     */   private boolean allowedInPeaceful;
/*     */   
/*     */   private Builder(EntityType.EntityFactory<T> factory, MobCategory category) {
/* 463 */     this.lootTable = (id -> Optional.of(ResourceKey.create(Registries.LOOT_TABLE, id.identifier().withPrefix("entities/"))));
/* 464 */     this.descriptionId = (id -> Util.makeDescriptionId("entity", id.identifier()));
/* 465 */     this.allowedInPeaceful = true;
/* 468 */     this.factory = factory;
/* 469 */     this.category = category;
/* 470 */     this.canSpawnFarFromPlayer = (category == MobCategory.CREATURE || category == MobCategory.MISC);
/*     */   }
/*     */   
/*     */   public static <T extends Entity> Builder<T> of(EntityType.EntityFactory<T> factory, MobCategory category) {
/* 474 */     return new Builder<>(factory, category);
/*     */   }
/*     */   
/*     */   public static <T extends Entity> Builder<T> createNothing(MobCategory category) {
/* 478 */     return new Builder<>((t, l) -> null, category);
/*     */   }
/*     */   
/*     */   public Builder<T> sized(float width, float height) {
/* 482 */     this.dimensions = EntityDimensions.scalable(width, height);
/* 483 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> spawnDimensionsScale(float scale) {
/* 487 */     this.spawnDimensionsScale = scale;
/* 488 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> eyeHeight(float eyeHeight) {
/* 492 */     this.dimensions = this.dimensions.withEyeHeight(eyeHeight);
/* 493 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> passengerAttachments(float... offsetYs) {
/* 497 */     for (float offsetY : offsetYs)
/* 498 */       this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, 0.0F, offsetY, 0.0F); 
/* 500 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> passengerAttachments(Vec3... points) {
/* 504 */     for (Vec3 point : points)
/* 505 */       this.attachments = this.attachments.attach(EntityAttachment.PASSENGER, point); 
/* 507 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> vehicleAttachment(Vec3 point) {
/* 511 */     return attach(EntityAttachment.VEHICLE, point);
/*     */   }
/*     */   
/*     */   public Builder<T> ridingOffset(float ridingOffset) {
/* 515 */     return attach(EntityAttachment.VEHICLE, 0.0F, -ridingOffset, 0.0F);
/*     */   }
/*     */   
/*     */   public Builder<T> nameTagOffset(float nameTagOffset) {
/* 519 */     return attach(EntityAttachment.NAME_TAG, 0.0F, nameTagOffset, 0.0F);
/*     */   }
/*     */   
/*     */   public Builder<T> attach(EntityAttachment attachment, float x, float y, float z) {
/* 523 */     this.attachments = this.attachments.attach(attachment, x, y, z);
/* 524 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> attach(EntityAttachment attachment, Vec3 point) {
/* 528 */     this.attachments = this.attachments.attach(attachment, point);
/* 529 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> noSummon() {
/* 533 */     this.summon = false;
/* 534 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> noSave() {
/* 538 */     this.serialize = false;
/* 539 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> fireImmune() {
/* 543 */     this.fireImmune = true;
/* 544 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> immuneTo(TagKey<Block> tag) {
/* 548 */     this.immuneTo = tag;
/* 549 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> canSpawnFarFromPlayer() {
/* 553 */     this.canSpawnFarFromPlayer = true;
/* 554 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> clientTrackingRange(int clientChunkRange) {
/* 558 */     this.clientTrackingRange = clientChunkRange;
/* 559 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> updateInterval(int updateInterval) {
/* 563 */     this.updateInterval = updateInterval;
/* 564 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> requiredFeatures(FeatureFlag... flags) {
/* 568 */     this.requiredFeatures = FeatureFlags.REGISTRY.subset(flags);
/* 569 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> noLootTable() {
/* 573 */     this.lootTable = DependantName.fixed(Optional.empty());
/* 574 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> notInPeaceful() {
/* 578 */     this.allowedInPeaceful = false;
/* 579 */     return this;
/*     */   }
/*     */   
/*     */   public EntityType<T> build(ResourceKey<EntityType<?>> name) {
/* 583 */     if (this.serialize)
/* 584 */       Util.fetchChoiceType(References.ENTITY_TREE, name.identifier().toString()); 
/* 587 */     return new EntityType<>(this.factory, this.category, this.serialize, this.summon, this.fireImmune, this.canSpawnFarFromPlayer, this.immuneTo, this.dimensions.withAttachments(this.attachments), this.spawnDimensionsScale, this.clientTrackingRange, this.updateInterval, (String)this.descriptionId.get(name), (Optional<ResourceKey<LootTable>>)this.lootTable.get(name), this.requiredFeatures, this.allowedInPeaceful);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityType$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */