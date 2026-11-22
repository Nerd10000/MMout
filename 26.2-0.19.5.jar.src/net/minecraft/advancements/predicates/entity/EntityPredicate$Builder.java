/*     */ package net.minecraft.advancements.predicates.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Map;
/*     */ import net.minecraft.advancements.predicates.DataComponentMatchers;
/*     */ import net.minecraft.advancements.predicates.DistancePredicate;
/*     */ import net.minecraft.advancements.predicates.LocationPredicate;
/*     */ import net.minecraft.advancements.predicates.MobEffectsPredicate;
/*     */ import net.minecraft.advancements.predicates.NbtPredicate;
/*     */ import net.minecraft.advancements.predicates.SlotsPredicate;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ 
/*     */ public class Builder {
/* 170 */   private final ImmutableMap.Builder<Codec<? extends EntitySubPredicate>, EntitySubPredicate> parts = ImmutableMap.builder();
/*     */   
/*     */   public static Builder entity() {
/* 173 */     return new Builder();
/*     */   }
/*     */   
/*     */   public <T extends EntitySubPredicate> Builder put(Codec<T> key, T predicate) {
/* 177 */     this.parts.put(key, predicate);
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public Builder of(HolderGetter<EntityType<?>> lookup, EntityType<?> entityType) {
/* 182 */     return entityType(EntityTypePredicate.of(lookup, entityType));
/*     */   }
/*     */   
/*     */   public Builder of(HolderGetter<EntityType<?>> lookup, TagKey<EntityType<?>> entityTypeTag) {
/* 186 */     return entityType(EntityTypePredicate.of(lookup, entityTypeTag));
/*     */   }
/*     */   
/*     */   public Builder entityType(EntityTypePredicate entityType) {
/* 190 */     return put(EntityTypePredicate.CODEC, entityType);
/*     */   }
/*     */   
/*     */   public Builder distance(DistancePredicate distanceToPlayer) {
/* 194 */     return put(DistanceToPlayerPredicate.CODEC, new DistanceToPlayerPredicate(distanceToPlayer));
/*     */   }
/*     */   
/*     */   public Builder moving(MovementPredicate movement) {
/* 198 */     return put(MovementPredicate.CODEC, movement);
/*     */   }
/*     */   
/*     */   public Builder located(LocationPredicate.Builder location) {
/* 202 */     return put(EntityLocationPredicate.CODEC, new EntityLocationPredicate(location.build()));
/*     */   }
/*     */   
/*     */   public Builder steppingOn(LocationPredicate.Builder location) {
/* 206 */     return put(SteppingOnPredicate.CODEC, new SteppingOnPredicate(location.build()));
/*     */   }
/*     */   
/*     */   public Builder movementAffectedBy(LocationPredicate.Builder location) {
/* 210 */     return put(MovementAffectedByPredicate.CODEC, new MovementAffectedByPredicate(location.build()));
/*     */   }
/*     */   
/*     */   public Builder effects(MobEffectsPredicate.Builder effects) {
/* 214 */     return put(EntityEffectsPredicate.CODEC, new EntityEffectsPredicate(effects.build()));
/*     */   }
/*     */   
/*     */   public Builder nbt(NbtPredicate nbt) {
/* 218 */     return put(EntityNbtPredicate.CODEC, new EntityNbtPredicate(nbt));
/*     */   }
/*     */   
/*     */   public Builder flags(EntityFlagsPredicate.Builder flags) {
/* 222 */     return put(EntityFlagsPredicate.CODEC, flags.build());
/*     */   }
/*     */   
/*     */   public Builder equipment(EntityEquipmentPredicate.Builder equipment) {
/* 226 */     return put(EntityEquipmentPredicate.CODEC, equipment.build());
/*     */   }
/*     */   
/*     */   public Builder equipment(EntityEquipmentPredicate equipment) {
/* 230 */     return put(EntityEquipmentPredicate.CODEC, equipment);
/*     */   }
/*     */   
/*     */   public Builder periodicTick(int period) {
/* 234 */     return put(PeriodicEntityTickPredicate.CODEC, new PeriodicEntityTickPredicate(period));
/*     */   }
/*     */   
/*     */   public Builder vehicle(Builder vehicle) {
/* 238 */     return put(VehiclePredicate.CODEC, new VehiclePredicate(vehicle.build()));
/*     */   }
/*     */   
/*     */   public Builder passenger(Builder passenger) {
/* 242 */     return put(PassengerPredicate.CODEC, new PassengerPredicate(passenger.build()));
/*     */   }
/*     */   
/*     */   public Builder targetedEntity(Builder targetedEntity) {
/* 246 */     return put(TargetedEntityPredicate.CODEC, new TargetedEntityPredicate(targetedEntity.build()));
/*     */   }
/*     */   
/*     */   public Builder team(String team) {
/* 250 */     return put(TeamPredicate.CODEC, new TeamPredicate(team));
/*     */   }
/*     */   
/*     */   public Builder slots(SlotsPredicate slots) {
/* 254 */     return put(EntitySlotsPredicate.CODEC, new EntitySlotsPredicate(slots));
/*     */   }
/*     */   
/*     */   public Builder components(DataComponentMatchers components) {
/* 258 */     if (!components.exact().isEmpty())
/* 259 */       components(components.exact()); 
/* 261 */     if (!components.partial().isEmpty())
/* 262 */       components(components.partial()); 
/* 264 */     return this;
/*     */   }
/*     */   
/*     */   public Builder components(DataComponentExactPredicate components) {
/* 268 */     return put(EntityExactDataComponentsPredicate.CODEC, new EntityExactDataComponentsPredicate(components));
/*     */   }
/*     */   
/*     */   public Builder components(Map<DataComponentPredicate.Type<?>, DataComponentPredicate> components) {
/* 272 */     return put(EntityPartialComponentsPredicate.CODEC, new EntityPartialComponentsPredicate(components));
/*     */   }
/*     */   
/*     */   public Builder lightingBolt(LightningBoltPredicate lightningBolt) {
/* 276 */     return put(LightningBoltPredicate.CODEC, lightningBolt);
/*     */   }
/*     */   
/*     */   public Builder player(PlayerPredicate player) {
/* 280 */     return put(PlayerPredicate.CODEC, player);
/*     */   }
/*     */   
/*     */   public Builder sheep(SheepPredicate sheep) {
/* 284 */     return put(SheepPredicate.CODEC, sheep);
/*     */   }
/*     */   
/*     */   public Builder cubeMob(CubeMobPredicate cubeMob) {
/* 288 */     return put(CubeMobPredicate.CODEC, cubeMob);
/*     */   }
/*     */   
/*     */   public Builder raider(RaiderPredicate raider) {
/* 292 */     return put(RaiderPredicate.CODEC, raider);
/*     */   }
/*     */   
/*     */   public Builder fishingHook(FishingHookPredicate fishingHook) {
/* 296 */     return put(FishingHookPredicate.CODEC, fishingHook);
/*     */   }
/*     */   
/*     */   public EntityPredicate build() {
/* 300 */     return new EntityPredicate((Map<Codec<? extends EntitySubPredicate>, EntitySubPredicate>)this.parts.buildOrThrow());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntityPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */