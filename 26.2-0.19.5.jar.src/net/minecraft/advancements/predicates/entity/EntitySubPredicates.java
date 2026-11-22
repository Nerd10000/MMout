/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.core.Registry;
/*    */ 
/*    */ public class EntitySubPredicates {
/*    */   public static Codec<? extends EntitySubPredicate> bootstrap(Registry<Codec<? extends EntitySubPredicate>> registry) {
/*  8 */     Registry.register(registry, "entity_type", EntityTypePredicate.CODEC);
/* 10 */     Registry.register(registry, "location", EntityLocationPredicate.CODEC);
/* 11 */     Registry.register(registry, "stepping_on", SteppingOnPredicate.CODEC);
/* 12 */     Registry.register(registry, "movement_affected_by", MovementAffectedByPredicate.CODEC);
/* 13 */     Registry.register(registry, "distance", DistanceToPlayerPredicate.CODEC);
/* 14 */     Registry.register(registry, "movement", MovementPredicate.CODEC);
/* 15 */     Registry.register(registry, "effects", EntityEffectsPredicate.CODEC);
/* 16 */     Registry.register(registry, "nbt", EntityNbtPredicate.CODEC);
/* 17 */     Registry.register(registry, "flags", EntityFlagsPredicate.CODEC);
/* 18 */     Registry.register(registry, "equipment", EntityEquipmentPredicate.CODEC);
/* 19 */     Registry.register(registry, "periodic_tick", PeriodicEntityTickPredicate.CODEC);
/* 20 */     Registry.register(registry, "vehicle", VehiclePredicate.CODEC);
/* 21 */     Registry.register(registry, "passenger", PassengerPredicate.CODEC);
/* 22 */     Registry.register(registry, "targeted_entity", TargetedEntityPredicate.CODEC);
/* 23 */     Registry.register(registry, "team", TeamPredicate.CODEC);
/* 24 */     Registry.register(registry, "slots", EntitySlotsPredicate.CODEC);
/* 25 */     Registry.register(registry, "components", EntityExactDataComponentsPredicate.CODEC);
/* 26 */     Registry.register(registry, "predicates", EntityPartialComponentsPredicate.CODEC);
/* 27 */     Registry.register(registry, "entity_tags", EntityTagPredicate.CODEC);
/* 29 */     Registry.register(registry, "type_specific/lightning", LightningBoltPredicate.CODEC);
/* 30 */     Registry.register(registry, "type_specific/fishing_hook", FishingHookPredicate.CODEC);
/* 31 */     Registry.register(registry, "type_specific/player", PlayerPredicate.CODEC);
/* 32 */     Registry.register(registry, "type_specific/cube_mob", CubeMobPredicate.CODEC);
/* 33 */     Registry.register(registry, "type_specific/raider", RaiderPredicate.CODEC);
/* 34 */     return (Codec<? extends EntitySubPredicate>)Registry.register(registry, "type_specific/sheep", SheepPredicate.CODEC);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\EntitySubPredicates.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */