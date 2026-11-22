/*     */ package net.minecraft.advancements.predicates.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*     */ import net.minecraft.advancements.predicates.DataComponentMatchers;
/*     */ import net.minecraft.advancements.predicates.DistancePredicate;
/*     */ import net.minecraft.advancements.predicates.LocationPredicate;
/*     */ import net.minecraft.advancements.predicates.MobEffectsPredicate;
/*     */ import net.minecraft.advancements.predicates.NbtPredicate;
/*     */ import net.minecraft.advancements.predicates.SlotsPredicate;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.component.DataComponentExactPredicate;
/*     */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public final class EntityPredicate {
/*     */   private static final Codec<Map<Codec<? extends EntitySubPredicate>, EntitySubPredicate>> MAP_CODEC;
/*     */   
/*     */   public static final Codec<EntityPredicate> CODEC;
/*     */   
/*     */   static {
/*  39 */     MAP_CODEC = Codec.dispatchedMap(BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE
/*  40 */         .byNameCodec(), c -> c);
/*  45 */     CODEC = MAP_CODEC.xmap(EntityPredicate::new, p -> p.parts);
/*     */   }
/*     */   
/*  50 */   public static final Codec<ContextAwarePredicate> ADVANCEMENT_CODEC = Codec.withAlternative(ContextAwarePredicate.CODEC, CODEC, EntityPredicate::wrap);
/*     */   
/*     */   private static final Comparator<Map.Entry<Codec<? extends EntitySubPredicate>, EntitySubPredicate>> PREDICATE_TYPE_ORDER;
/*     */   
/*     */   private final Map<Codec<? extends EntitySubPredicate>, EntitySubPredicate> parts;
/*     */   
/*     */   private final EntitySubPredicate combinedPart;
/*     */   
/*     */   static {
/*  57 */     PREDICATE_TYPE_ORDER = Map.Entry.comparingByKey(
/*  58 */         Comparator.comparing(codec -> (codec == EntityTypePredicate.CODEC) ? Integer.valueOf(-1) : (
/*     */           
/*  67 */           (codec == VehiclePredicate.CODEC || codec == PassengerPredicate.CODEC || codec == TargetedEntityPredicate.CODEC || codec == LightningBoltPredicate.CODEC || codec == PlayerPredicate.CODEC) ? Integer.valueOf(1) : ((codec == EntityNbtPredicate.CODEC) ? Integer.valueOf(2) : Integer.valueOf(0)))));
/*     */   }
/*     */   
/*     */   public EntityPredicate(Map<Codec<? extends EntitySubPredicate>, EntitySubPredicate> parts) {
/*  88 */     this.parts = parts;
/*  89 */     this.combinedPart = combine(parts);
/*     */   }
/*     */   
/*     */   public static ContextAwarePredicate wrap(Builder singlePredicate) {
/*  93 */     return wrap(singlePredicate.build());
/*     */   }
/*     */   
/*     */   public static Optional<ContextAwarePredicate> wrap(Optional<EntityPredicate> singlePredicate) {
/*  97 */     return singlePredicate.map(EntityPredicate::wrap);
/*     */   }
/*     */   
/*     */   public static List<ContextAwarePredicate> wrap(Builder... predicates) {
/* 101 */     return Stream.<Builder>of(predicates).map(EntityPredicate::wrap).toList();
/*     */   }
/*     */   
/*     */   public static ContextAwarePredicate wrap(EntityPredicate singlePredicate) {
/* 105 */     LootItemCondition asCondition = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, singlePredicate).build();
/* 106 */     return new ContextAwarePredicate(List.of(asCondition));
/*     */   }
/*     */   
/*     */   public boolean matches(ServerPlayer player, Entity entity) {
/* 110 */     return matches(player.level(), player.position(), entity);
/*     */   }
/*     */   
/*     */   public boolean matches(ServerLevel level, Vec3 position, Entity entity) {
/* 114 */     if (entity == null)
/* 115 */       return false; 
/* 118 */     return this.combinedPart.matches(entity, level, position);
/*     */   }
/*     */   
/*     */   public static LootContext createContext(ServerPlayer player, Entity entity) {
/* 125 */     LootParams lootParams = (new LootParams.Builder(player.level())).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ORIGIN, player.position()).create(LootContextParamSets.ADVANCEMENT_ENTITY);
/* 126 */     return (new LootContext.Builder(lootParams)).create(Optional.empty());
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 131 */     if (o != this) {
/* 131 */       if (o instanceof EntityPredicate) {
/* 131 */         EntityPredicate that = (EntityPredicate)o;
/* 131 */         if (Objects.equals(this.parts, that.parts));
/*     */       } 
/* 131 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 136 */     return this.parts.hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 141 */     return "EntityPredicate[parts=" + String.valueOf(this.parts) + "]";
/*     */   }
/*     */   
/*     */   private static EntitySubPredicate combine(Map<Codec<? extends EntitySubPredicate>, EntitySubPredicate> predicateMap) {
/* 146 */     if (predicateMap.isEmpty())
/* 147 */       return EntitySubPredicate.ALWAYS_TRUE; 
/* 149 */     if (predicateMap.size() == 1)
/* 150 */       return (EntitySubPredicate)Iterables.getOnlyElement(predicateMap.values()); 
/* 153 */     EntitySubPredicate[] predicates = (EntitySubPredicate[])predicateMap.entrySet().stream().sorted(PREDICATE_TYPE_ORDER).map(Map.Entry::getValue).toArray(x$0 -> new EntitySubPredicate[x$0]);
/* 155 */     if (predicates.length == 2)
/* 156 */       return predicates[0].and(predicates[1]); 
/* 159 */     return (entity, level, position) -> {
/*     */         for (EntitySubPredicate part : predicates) {
/*     */           if (!part.matches(entity, level, position))
/*     */             return false; 
/*     */         } 
/*     */         return true;
/*     */       };
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 170 */     private final ImmutableMap.Builder<Codec<? extends EntitySubPredicate>, EntitySubPredicate> parts = ImmutableMap.builder();
/*     */     
/*     */     public static Builder entity() {
/* 173 */       return new Builder();
/*     */     }
/*     */     
/*     */     public <T extends EntitySubPredicate> Builder put(Codec<T> key, T predicate) {
/* 177 */       this.parts.put(key, predicate);
/* 178 */       return this;
/*     */     }
/*     */     
/*     */     public Builder of(HolderGetter<EntityType<?>> lookup, EntityType<?> entityType) {
/* 182 */       return entityType(EntityTypePredicate.of(lookup, entityType));
/*     */     }
/*     */     
/*     */     public Builder of(HolderGetter<EntityType<?>> lookup, TagKey<EntityType<?>> entityTypeTag) {
/* 186 */       return entityType(EntityTypePredicate.of(lookup, entityTypeTag));
/*     */     }
/*     */     
/*     */     public Builder entityType(EntityTypePredicate entityType) {
/* 190 */       return put(EntityTypePredicate.CODEC, entityType);
/*     */     }
/*     */     
/*     */     public Builder distance(DistancePredicate distanceToPlayer) {
/* 194 */       return put(DistanceToPlayerPredicate.CODEC, new DistanceToPlayerPredicate(distanceToPlayer));
/*     */     }
/*     */     
/*     */     public Builder moving(MovementPredicate movement) {
/* 198 */       return put(MovementPredicate.CODEC, movement);
/*     */     }
/*     */     
/*     */     public Builder located(LocationPredicate.Builder location) {
/* 202 */       return put(EntityLocationPredicate.CODEC, new EntityLocationPredicate(location.build()));
/*     */     }
/*     */     
/*     */     public Builder steppingOn(LocationPredicate.Builder location) {
/* 206 */       return put(SteppingOnPredicate.CODEC, new SteppingOnPredicate(location.build()));
/*     */     }
/*     */     
/*     */     public Builder movementAffectedBy(LocationPredicate.Builder location) {
/* 210 */       return put(MovementAffectedByPredicate.CODEC, new MovementAffectedByPredicate(location.build()));
/*     */     }
/*     */     
/*     */     public Builder effects(MobEffectsPredicate.Builder effects) {
/* 214 */       return put(EntityEffectsPredicate.CODEC, new EntityEffectsPredicate(effects.build()));
/*     */     }
/*     */     
/*     */     public Builder nbt(NbtPredicate nbt) {
/* 218 */       return put(EntityNbtPredicate.CODEC, new EntityNbtPredicate(nbt));
/*     */     }
/*     */     
/*     */     public Builder flags(EntityFlagsPredicate.Builder flags) {
/* 222 */       return put(EntityFlagsPredicate.CODEC, flags.build());
/*     */     }
/*     */     
/*     */     public Builder equipment(EntityEquipmentPredicate.Builder equipment) {
/* 226 */       return put(EntityEquipmentPredicate.CODEC, equipment.build());
/*     */     }
/*     */     
/*     */     public Builder equipment(EntityEquipmentPredicate equipment) {
/* 230 */       return put(EntityEquipmentPredicate.CODEC, equipment);
/*     */     }
/*     */     
/*     */     public Builder periodicTick(int period) {
/* 234 */       return put(PeriodicEntityTickPredicate.CODEC, new PeriodicEntityTickPredicate(period));
/*     */     }
/*     */     
/*     */     public Builder vehicle(Builder vehicle) {
/* 238 */       return put(VehiclePredicate.CODEC, new VehiclePredicate(vehicle.build()));
/*     */     }
/*     */     
/*     */     public Builder passenger(Builder passenger) {
/* 242 */       return put(PassengerPredicate.CODEC, new PassengerPredicate(passenger.build()));
/*     */     }
/*     */     
/*     */     public Builder targetedEntity(Builder targetedEntity) {
/* 246 */       return put(TargetedEntityPredicate.CODEC, new TargetedEntityPredicate(targetedEntity.build()));
/*     */     }
/*     */     
/*     */     public Builder team(String team) {
/* 250 */       return put(TeamPredicate.CODEC, new TeamPredicate(team));
/*     */     }
/*     */     
/*     */     public Builder slots(SlotsPredicate slots) {
/* 254 */       return put(EntitySlotsPredicate.CODEC, new EntitySlotsPredicate(slots));
/*     */     }
/*     */     
/*     */     public Builder components(DataComponentMatchers components) {
/* 258 */       if (!components.exact().isEmpty())
/* 259 */         components(components.exact()); 
/* 261 */       if (!components.partial().isEmpty())
/* 262 */         components(components.partial()); 
/* 264 */       return this;
/*     */     }
/*     */     
/*     */     public Builder components(DataComponentExactPredicate components) {
/* 268 */       return put(EntityExactDataComponentsPredicate.CODEC, new EntityExactDataComponentsPredicate(components));
/*     */     }
/*     */     
/*     */     public Builder components(Map<DataComponentPredicate.Type<?>, DataComponentPredicate> components) {
/* 272 */       return put(EntityPartialComponentsPredicate.CODEC, new EntityPartialComponentsPredicate(components));
/*     */     }
/*     */     
/*     */     public Builder lightingBolt(LightningBoltPredicate lightningBolt) {
/* 276 */       return put(LightningBoltPredicate.CODEC, lightningBolt);
/*     */     }
/*     */     
/*     */     public Builder player(PlayerPredicate player) {
/* 280 */       return put(PlayerPredicate.CODEC, player);
/*     */     }
/*     */     
/*     */     public Builder sheep(SheepPredicate sheep) {
/* 284 */       return put(SheepPredicate.CODEC, sheep);
/*     */     }
/*     */     
/*     */     public Builder cubeMob(CubeMobPredicate cubeMob) {
/* 288 */       return put(CubeMobPredicate.CODEC, cubeMob);
/*     */     }
/*     */     
/*     */     public Builder raider(RaiderPredicate raider) {
/* 292 */       return put(RaiderPredicate.CODEC, raider);
/*     */     }
/*     */     
/*     */     public Builder fishingHook(FishingHookPredicate fishingHook) {
/* 296 */       return put(FishingHookPredicate.CODEC, fishingHook);
/*     */     }
/*     */     
/*     */     public EntityPredicate build() {
/* 300 */       return new EntityPredicate((Map<Codec<? extends EntitySubPredicate>, EntitySubPredicate>)this.parts.buildOrThrow());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntityPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */