/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.BackUpIfTooClose;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.CopyMemoryWithExpiry;
/*     */ import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.DismountOrSkipMounting;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToTargetLocation;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWith;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.Mount;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SpearApproach;
/*     */ import net.minecraft.world.entity.ai.behavior.SpearAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.SpearRetreat;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StartCelebratingIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.TriggerGate;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class PiglinAi {
/*     */   public static final int REPELLENT_DETECTION_RANGE_HORIZONTAL = 8;
/*     */   
/*     */   public static final int REPELLENT_DETECTION_RANGE_VERTICAL = 4;
/*     */   
/*  87 */   public static final Item BARTERING_ITEM = Items.GOLD_INGOT;
/*     */   
/*     */   private static final int PLAYER_ANGER_RANGE = 16;
/*     */   
/*     */   private static final int ANGER_DURATION = 600;
/*     */   
/*     */   private static final int ADMIRE_DURATION = 119;
/*     */   
/*     */   private static final int MAX_DISTANCE_TO_WALK_TO_ITEM = 9;
/*     */   
/*     */   private static final int MAX_TIME_TO_WALK_TO_ITEM = 200;
/*     */   
/*     */   private static final int HOW_LONG_TIME_TO_DISABLE_ADMIRE_WALKING_IF_CANT_REACH_ITEM = 200;
/*     */   
/*     */   private static final int CELEBRATION_TIME = 300;
/*     */   
/*     */   public static final int MAX_TIME_BETWEEN_HUNTS = 120;
/*     */   
/*  97 */   protected static final UniformInt TIME_BETWEEN_HUNTS = TimeUtil.rangeOfSeconds(30, 120);
/*     */   
/*     */   private static final int BABY_FLEE_DURATION_AFTER_GETTING_HIT = 100;
/*     */   
/*     */   private static final int HIT_BY_PLAYER_MEMORY_TIMEOUT = 400;
/*     */   
/*     */   private static final int MAX_WALK_DISTANCE_TO_START_RIDING = 8;
/*     */   
/* 101 */   private static final UniformInt RIDE_START_INTERVAL = TimeUtil.rangeOfSeconds(10, 40);
/*     */   
/* 102 */   private static final UniformInt RIDE_DURATION = TimeUtil.rangeOfSeconds(10, 30);
/*     */   
/* 103 */   private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
/*     */   
/*     */   private static final int MELEE_ATTACK_COOLDOWN = 20;
/*     */   
/*     */   private static final int EAT_COOLDOWN = 200;
/*     */   
/*     */   private static final int DESIRED_DISTANCE_FROM_ENTITY_WHEN_AVOIDING = 12;
/*     */   
/*     */   private static final int MAX_LOOK_DIST = 8;
/*     */   
/*     */   private static final int MAX_LOOK_DIST_FOR_PLAYER_HOLDING_LOVED_ITEM = 14;
/*     */   
/*     */   private static final int INTERACTION_RANGE = 8;
/*     */   
/*     */   private static final int MIN_DESIRED_DIST_FROM_TARGET_WHEN_HOLDING_CROSSBOW = 5;
/*     */   
/*     */   private static final float SPEED_WHEN_STRAFING_BACK_FROM_TARGET = 0.75F;
/*     */   
/*     */   private static final int DESIRED_DISTANCE_FROM_ZOMBIFIED = 6;
/*     */   
/* 113 */   private static final UniformInt AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
/*     */   
/* 114 */   private static final UniformInt BABY_AVOID_NEMESIS_DURATION = TimeUtil.rangeOfSeconds(5, 7);
/*     */   
/*     */   private static final float PROBABILITY_OF_CELEBRATION_DANCE = 0.1F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_AVOIDING = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_RETREATING = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MOUNTING = 0.8F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_GOING_TO_WANTED_ITEM = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_GOING_TO_CELEBRATE_LOCATION = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_DANCING = 0.6F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.6F;
/*     */   
/*     */   public static List<ActivityData<Piglin>> getActivities(Piglin piglin) {
/* 127 */     return List.of(
/* 128 */         initCoreActivity(), 
/* 129 */         initIdleActivity(), 
/* 130 */         initAdmireItemActivity(), 
/* 131 */         initFightActivity(piglin), 
/* 132 */         initCelebrateActivity(), 
/* 133 */         initRetreatActivity(), 
/* 134 */         initRideHoglinActivity());
/*     */   }
/*     */   
/*     */   protected static void initMemories(Piglin body, RandomSource random) {
/* 139 */     int delayUntilFirstHunt = TIME_BETWEEN_HUNTS.sample(random);
/* 140 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, Boolean.valueOf(true), delayUntilFirstHunt);
/*     */   }
/*     */   
/*     */   private static ActivityData<Piglin> initCoreActivity() {
/* 144 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), 
/*     */           
/* 147 */           InteractWithDoor.create(), 
/* 148 */           babyAvoidNemesis(), 
/* 149 */           avoidZombified(), 
/* 150 */           StopHoldingItemIfNoLongerAdmiring.create(), 
/* 151 */           StartAdmiringItemIfSeen.create(119), 
/* 152 */           StartCelebratingIfTargetDead.create(300, PiglinAi::wantsToDance), 
/* 153 */           StopBeingAngryIfTargetDead.create()));
/*     */   }
/*     */   
/*     */   private static ActivityData<Piglin> initIdleActivity() {
/* 158 */     return ActivityData.create(Activity.IDLE, 10, ImmutableList.of(
/* 159 */           SetEntityLookTarget.create(PiglinAi::isPlayerHoldingLovedItem, 14.0F), 
/* 160 */           StartAttacking.create((level, piglin) -> piglin.isAdult(), PiglinAi::findNearestValidAttackTarget), 
/* 161 */           BehaviorBuilder.triggerIf(Piglin::canHunt, StartHuntingHoglin.create()), 
/* 162 */           avoidRepellent(), 
/* 163 */           babySometimesRideBabyHoglin(), 
/* 164 */           createIdleLookBehaviors(), 
/* 165 */           createIdleMovementBehaviors(), 
/* 166 */           SetLookAndInteract.create(EntityTypes.PLAYER, 4)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Piglin> initFightActivity(Piglin body) {
/* 171 */     return ActivityData.create(Activity.FIGHT, 10, ImmutableList.of(
/* 172 */           StopAttackingIfTargetInvalid.create((level, target) -> !isNearestValidAttackTarget(level, body, target)), 
/* 173 */           BehaviorBuilder.triggerIf(PiglinAi::hasCrossbow, BackUpIfTooClose.create(5, 0.75F)), 
/* 174 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), new SpearApproach(1.0D, 10.0F), new SpearAttack(1.0D, 1.0D, 2.0F), new SpearRetreat(1.0D), 
/*     */           
/* 178 */           MeleeAttack.create(20), new CrossbowAttack(), 
/*     */           
/* 180 */           RememberIfHoglinWasKilled.create(), 
/* 181 */           EraseMemoryIf.create(PiglinAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */   
/*     */   private static ActivityData<Piglin> initCelebrateActivity() {
/* 186 */     return ActivityData.create(Activity.CELEBRATE, 10, ImmutableList.of(
/* 187 */           avoidRepellent(), 
/* 188 */           SetEntityLookTarget.create(PiglinAi::isPlayerHoldingLovedItem, 14.0F), 
/* 189 */           StartAttacking.create((level, piglin) -> piglin.isAdult(), PiglinAi::findNearestValidAttackTarget), 
/* 190 */           BehaviorBuilder.triggerIf(piglin -> !piglin.isDancing(), GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 2, 1.0F)), 
/* 191 */           BehaviorBuilder.triggerIf(Piglin::isDancing, GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 4, 0.6F)), new RunOne(
/* 192 */             (List)ImmutableList.of(
/* 193 */               Pair.of(SetEntityLookTarget.create(EntityTypes.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 194 */               Pair.of(RandomStroll.stroll(0.6F, 2, 1), Integer.valueOf(1)), 
/* 195 */               Pair.of(new DoNothing(10, 20), Integer.valueOf(1))))), MemoryModuleType.CELEBRATE_LOCATION);
/*     */   }
/*     */   
/*     */   private static ActivityData<Piglin> initAdmireItemActivity() {
/* 201 */     return ActivityData.create(Activity.ADMIRE_ITEM, 10, ImmutableList.of(
/* 202 */           GoToWantedItem.create(PiglinAi::isNotHoldingLovedItemInOffHand, 1.0F, true, 9), 
/* 203 */           StopAdmiringIfItemTooFarAway.create(9), 
/* 204 */           StopAdmiringIfTiredOfTryingToReachItem.create(200, 200)), MemoryModuleType.ADMIRING_ITEM);
/*     */   }
/*     */   
/*     */   private static ActivityData<Piglin> initRetreatActivity() {
/* 209 */     return ActivityData.create(Activity.AVOID, 10, ImmutableList.of(
/* 210 */           SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true), 
/* 211 */           createIdleLookBehaviors(), 
/* 212 */           createIdleMovementBehaviors(), 
/* 213 */           EraseMemoryIf.create(PiglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
/*     */   }
/*     */   
/*     */   private static ActivityData<Piglin> initRideHoglinActivity() {
/* 218 */     return ActivityData.create(Activity.RIDE, 10, ImmutableList.of(
/* 219 */           Mount.create(0.8F), 
/* 220 */           SetEntityLookTarget.create(PiglinAi::isPlayerHoldingLovedItem, 8.0F), 
/* 221 */           BehaviorBuilder.sequence(
/* 222 */             (Trigger)BehaviorBuilder.triggerIf(Entity::isPassenger), 
/* 223 */             TriggerGate.triggerOneShuffled(
/* 224 */               (List)ImmutableList.builder()
/* 225 */               .addAll((Iterable)createLookBehaviors())
/* 226 */               .add(Pair.of(BehaviorBuilder.triggerIf(e -> true), Integer.valueOf(1)))
/* 227 */               .build())), 
/*     */           
/* 230 */           DismountOrSkipMounting.create(8, PiglinAi::wantsToStopRiding)), MemoryModuleType.RIDE_TARGET);
/*     */   }
/*     */   
/*     */   private static ImmutableList<Pair<OneShot<LivingEntity>, Integer>> createLookBehaviors() {
/* 236 */     return ImmutableList.of(
/* 237 */         Pair.of(SetEntityLookTarget.create(EntityTypes.PLAYER, 8.0F), Integer.valueOf(1)), 
/* 238 */         Pair.of(SetEntityLookTarget.create(EntityTypes.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 239 */         Pair.of(SetEntityLookTarget.create(8.0F), Integer.valueOf(1)));
/*     */   }
/*     */   
/*     */   private static RunOne<LivingEntity> createIdleLookBehaviors() {
/* 244 */     return new RunOne(
/* 245 */         (List)ImmutableList.builder()
/* 246 */         .addAll((Iterable)createLookBehaviors())
/* 247 */         .add(Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))
/* 248 */         .build());
/*     */   }
/*     */   
/*     */   private static RunOne<Piglin> createIdleMovementBehaviors() {
/* 253 */     return new RunOne((List)ImmutableList.of(
/* 254 */           Pair.of(RandomStroll.stroll(0.6F), Integer.valueOf(2)), 
/*     */           
/* 256 */           Pair.of(InteractWith.of(EntityTypes.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 257 */           Pair.of(BehaviorBuilder.triggerIf(PiglinAi::doesntSeeAnyPlayerHoldingLovedItem, SetWalkTargetFromLookTarget.create(0.6F, 3)), Integer.valueOf(2)), 
/* 258 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */   
/*     */   private static BehaviorControl<PathfinderMob> avoidRepellent() {
/* 263 */     return SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false);
/*     */   }
/*     */   
/*     */   private static BehaviorControl<Piglin> babyAvoidNemesis() {
/* 267 */     return CopyMemoryWithExpiry.create(Piglin::isBaby, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.AVOID_TARGET, BABY_AVOID_NEMESIS_DURATION);
/*     */   }
/*     */   
/*     */   private static BehaviorControl<Piglin> avoidZombified() {
/* 271 */     return CopyMemoryWithExpiry.create(PiglinAi::isNearZombified, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.AVOID_TARGET, AVOID_ZOMBIFIED_DURATION);
/*     */   }
/*     */   
/*     */   protected static void updateActivity(Piglin body) {
/* 275 */     Brain<Piglin> brain = body.getBrain();
/* 277 */     Activity oldActivity = brain.getActiveNonCoreActivity().orElse(null);
/* 281 */     brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.RIDE, Activity.IDLE));
/* 290 */     Activity newActivity = brain.getActiveNonCoreActivity().orElse(null);
/* 291 */     if (oldActivity != newActivity) {
/* 293 */       Objects.requireNonNull(body);
/* 293 */       getSoundForCurrentActivity(body).ifPresent(body::makeSound);
/*     */     } 
/* 297 */     body.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/* 299 */     if (!brain.hasMemoryValue(MemoryModuleType.RIDE_TARGET) && isBabyRidingBaby(body))
/* 303 */       body.stopRiding(); 
/* 306 */     if (!brain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION))
/* 309 */       brain.eraseMemory(MemoryModuleType.DANCING); 
/* 311 */     body.setDancing(brain.hasMemoryValue(MemoryModuleType.DANCING));
/*     */   }
/*     */   
/*     */   private static boolean isBabyRidingBaby(Piglin body) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual isBaby : ()Z
/*     */     //   4: ifne -> 9
/*     */     //   7: iconst_0
/*     */     //   8: ireturn
/*     */     //   9: aload_0
/*     */     //   10: invokevirtual getVehicle : ()Lnet/minecraft/world/entity/Entity;
/*     */     //   13: astore_1
/*     */     //   14: aload_1
/*     */     //   15: instanceof net/minecraft/world/entity/monster/piglin/Piglin
/*     */     //   18: ifeq -> 33
/*     */     //   21: aload_1
/*     */     //   22: checkcast net/minecraft/world/entity/monster/piglin/Piglin
/*     */     //   25: astore_3
/*     */     //   26: aload_3
/*     */     //   27: invokevirtual isBaby : ()Z
/*     */     //   30: ifne -> 52
/*     */     //   33: aload_1
/*     */     //   34: instanceof net/minecraft/world/entity/monster/hoglin/Hoglin
/*     */     //   37: ifeq -> 56
/*     */     //   40: aload_1
/*     */     //   41: checkcast net/minecraft/world/entity/monster/hoglin/Hoglin
/*     */     //   44: astore_2
/*     */     //   45: aload_2
/*     */     //   46: invokevirtual isBaby : ()Z
/*     */     //   49: ifeq -> 56
/*     */     //   52: iconst_1
/*     */     //   53: goto -> 57
/*     */     //   56: iconst_0
/*     */     //   57: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #315	-> 0
/*     */     //   #316	-> 7
/*     */     //   #318	-> 9
/*     */     //   #320	-> 14
/*     */     //   #319	-> 21
/*     */     //   #320	-> 40
/*     */     //   #319	-> 57
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   26	7	3	riddenPiglin	Lnet/minecraft/world/entity/monster/piglin/Piglin;
/*     */     //   45	7	2	riddenHoglin	Lnet/minecraft/world/entity/monster/hoglin/Hoglin;
/*     */     //   0	58	0	body	Lnet/minecraft/world/entity/monster/piglin/Piglin;
/*     */     //   14	44	1	vehicle	Lnet/minecraft/world/entity/Entity;
/*     */   }
/*     */   
/*     */   protected static void pickUpItem(ServerLevel level, Piglin body, ItemEntity itemEntity) {
/*     */     ItemStack taken;
/* 324 */     stopWalking(body);
/* 328 */     if (itemEntity.getItem().is(Items.GOLD_NUGGET)) {
/* 331 */       body.take((Entity)itemEntity, itemEntity.getItem().getCount());
/* 332 */       taken = itemEntity.getItem();
/* 333 */       itemEntity.discard();
/*     */     } else {
/* 335 */       body.take((Entity)itemEntity, 1);
/* 336 */       taken = removeOneItemFromItemEntity(itemEntity);
/*     */     } 
/* 339 */     if (isLovedItem(taken)) {
/* 340 */       body.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
/* 341 */       holdInOffhand(level, body, taken);
/* 342 */       admireGoldItem((LivingEntity)body);
/*     */       return;
/*     */     } 
/* 346 */     if (isFood(taken) && !hasEatenRecently(body)) {
/* 347 */       eat(body);
/*     */       return;
/*     */     } 
/* 351 */     boolean itemEquipped = !body.equipItemIfPossible(level, taken).equals(ItemStack.EMPTY);
/* 352 */     if (itemEquipped)
/*     */       return; 
/* 356 */     putInInventory(body, taken);
/*     */   }
/*     */   
/*     */   private static void holdInOffhand(ServerLevel level, Piglin body, ItemStack itemStack) {
/* 360 */     if (isHoldingItemInOffHand(body))
/* 361 */       body.spawnAtLocation(level, body.getItemInHand(InteractionHand.OFF_HAND)); 
/* 363 */     body.holdInOffHand(itemStack);
/*     */   }
/*     */   
/*     */   private static ItemStack removeOneItemFromItemEntity(ItemEntity itemEntity) {
/* 367 */     ItemStack sourceStack = itemEntity.getItem();
/* 368 */     ItemStack removedStack = sourceStack.split(1);
/* 369 */     if (sourceStack.isEmpty()) {
/* 370 */       itemEntity.discard();
/*     */     } else {
/* 372 */       itemEntity.setItem(sourceStack);
/*     */     } 
/* 374 */     return removedStack;
/*     */   }
/*     */   
/*     */   protected static void stopHoldingOffHandItem(ServerLevel level, Piglin body, boolean barteringEnabled) {
/* 378 */     ItemStack itemStack = body.getItemInHand(InteractionHand.OFF_HAND);
/* 379 */     body.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
/* 381 */     if (body.isAdult()) {
/* 382 */       boolean barterCurrency = isBarterCurrency(itemStack);
/* 383 */       if (barteringEnabled && barterCurrency) {
/* 384 */         throwItems(body, getBarterResponseItems(body));
/* 385 */       } else if (!barterCurrency) {
/* 386 */         boolean equipped = !body.equipItemIfPossible(level, itemStack).isEmpty();
/* 387 */         if (!equipped)
/* 388 */           putInInventory(body, itemStack); 
/*     */       } 
/*     */     } else {
/* 392 */       boolean equipped = !body.equipItemIfPossible(level, itemStack).isEmpty();
/* 393 */       if (!equipped) {
/* 397 */         ItemStack mainHandItem = body.getMainHandItem();
/* 398 */         if (isLovedItem(mainHandItem)) {
/* 399 */           putInInventory(body, mainHandItem);
/*     */         } else {
/* 401 */           throwItems(body, Collections.singletonList(mainHandItem));
/*     */         } 
/* 403 */         body.holdInMainHand(itemStack);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static void cancelAdmiring(ServerLevel level, Piglin body) {
/* 409 */     if (isAdmiringItem(body) && !body.getOffhandItem().isEmpty()) {
/* 410 */       body.spawnAtLocation(level, body.getOffhandItem());
/* 411 */       body.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void putInInventory(Piglin body, ItemStack itemStack) {
/* 416 */     ItemStack stuffThatCouldntFitInMyInventory = body.addToInventory(itemStack);
/* 417 */     throwItemsTowardRandomPos(body, Collections.singletonList(stuffThatCouldntFitInMyInventory));
/*     */   }
/*     */   
/*     */   private static void throwItems(Piglin body, List<ItemStack> itemStacks) {
/* 421 */     Optional<Player> player = body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
/* 422 */     if (player.isPresent()) {
/* 423 */       throwItemsTowardPlayer(body, player.get(), itemStacks);
/*     */     } else {
/* 425 */       throwItemsTowardRandomPos(body, itemStacks);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void throwItemsTowardRandomPos(Piglin body, List<ItemStack> itemStacks) {
/* 430 */     throwItemsTowardPos(body, itemStacks, getRandomNearbyPos(body));
/*     */   }
/*     */   
/*     */   private static void throwItemsTowardPlayer(Piglin body, Player player, List<ItemStack> itemStacks) {
/* 434 */     throwItemsTowardPos(body, itemStacks, player.position());
/*     */   }
/*     */   
/*     */   private static void throwItemsTowardPos(Piglin body, List<ItemStack> itemStacks, Vec3 targetPos) {
/* 438 */     if (!itemStacks.isEmpty()) {
/* 439 */       body.swing(InteractionHand.OFF_HAND);
/* 440 */       for (ItemStack itemStack : itemStacks)
/* 441 */         BehaviorUtils.throwItem((LivingEntity)body, itemStack, targetPos.add(0.0D, 1.0D, 0.0D)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<ItemStack> getBarterResponseItems(Piglin body) {
/* 447 */     LootTable lootTable = body.level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.PIGLIN_BARTERING);
/* 448 */     return (List<ItemStack>)lootTable.getRandomItems((new LootParams.Builder((ServerLevel)body.level()))
/* 449 */         .withParameter(LootContextParams.THIS_ENTITY, body)
/* 450 */         .create(LootContextParamSets.PIGLIN_BARTER));
/*     */   }
/*     */   
/*     */   private static boolean wantsToDance(LivingEntity body, LivingEntity killedTarget) {
/* 455 */     if (!killedTarget.is(EntityTypes.HOGLIN))
/* 456 */       return false; 
/* 459 */     return (RandomSource.createThreadLocalInstance(body.level().getGameTime()).nextFloat() < 0.1F);
/*     */   }
/*     */   
/*     */   protected static boolean wantsToPickup(Piglin body, ItemStack itemStack) {
/* 466 */     if (body.isBaby() && itemStack.is(ItemTags.IGNORED_BY_PIGLIN_BABIES))
/* 467 */       return false; 
/* 470 */     if (itemStack.is(ItemTags.PIGLIN_REPELLENTS))
/* 471 */       return false; 
/* 473 */     if (isAdmiringDisabled(body) && body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
/* 474 */       return false; 
/* 476 */     if (isBarterCurrency(itemStack))
/* 477 */       return isNotHoldingLovedItemInOffHand(body); 
/* 480 */     boolean hasSpace = body.canAddToInventory(itemStack);
/* 481 */     if (itemStack.is(Items.GOLD_NUGGET))
/* 482 */       return hasSpace; 
/* 484 */     if (isFood(itemStack))
/* 485 */       return (!hasEatenRecently(body) && hasSpace); 
/* 487 */     if (isLovedItem(itemStack))
/* 488 */       return (isNotHoldingLovedItemInOffHand(body) && hasSpace); 
/* 490 */     return body.canReplaceCurrentItem(itemStack);
/*     */   }
/*     */   
/*     */   protected static boolean isLovedItem(ItemStack itemStack) {
/* 495 */     return itemStack.is(ItemTags.PIGLIN_LOVED);
/*     */   }
/*     */   
/*     */   private static boolean wantsToStopRiding(Piglin body, Entity entityBeingRidden) {
/* 499 */     if (entityBeingRidden instanceof Mob) {
/* 499 */       Mob mobBeingRidden = (Mob)entityBeingRidden;
/* 500 */       return (!mobBeingRidden.isBaby() || 
/* 501 */         !mobBeingRidden.isAlive() || 
/* 502 */         wasHurtRecently((LivingEntity)body) || 
/* 503 */         wasHurtRecently((LivingEntity)mobBeingRidden) || (mobBeingRidden instanceof Piglin && mobBeingRidden
/* 504 */         .getVehicle() == null));
/*     */     } 
/* 506 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isNearestValidAttackTarget(ServerLevel level, Piglin body, LivingEntity target) {
/* 510 */     return findNearestValidAttackTarget(level, body)
/* 511 */       .filter(nearestValidTarget -> (nearestValidTarget == target))
/* 512 */       .isPresent();
/*     */   }
/*     */   
/*     */   private static boolean isNearZombified(Piglin body) {
/* 516 */     Brain<Piglin> brain = body.getBrain();
/* 517 */     if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
/* 518 */       LivingEntity zombified = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
/* 519 */       return body.closerThan((Entity)zombified, 6.0D);
/*     */     } 
/* 521 */     return false;
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, Piglin body) {
/* 525 */     Brain<Piglin> brain = body.getBrain();
/* 527 */     if (isNearZombified(body))
/* 528 */       return Optional.empty(); 
/* 531 */     Optional<LivingEntity> angryAt = BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)body, MemoryModuleType.ANGRY_AT);
/* 532 */     if (angryAt.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(level, (LivingEntity)body, angryAt.get()))
/* 533 */       return angryAt; 
/* 536 */     if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
/* 537 */       Optional<Player> player = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/* 538 */       if (player.isPresent())
/* 539 */         return (Optional)player; 
/*     */     } 
/* 543 */     Optional<Mob> nemesis = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
/* 544 */     if (nemesis.isPresent())
/* 545 */       return (Optional)nemesis; 
/* 548 */     Optional<Player> playerNotWearingGold = brain.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
/* 549 */     if (playerNotWearingGold.isPresent() && Sensor.isEntityAttackable(level, (LivingEntity)body, (LivingEntity)playerNotWearingGold.get()))
/* 550 */       return (Optional)playerNotWearingGold; 
/* 553 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static void angerNearbyPiglins(ServerLevel level, Player player, boolean onlyIfTheySeeThePlayer) {
/* 557 */     List<Piglin> nearbyPiglins = player.level().getEntitiesOfClass(Piglin.class, player.getBoundingBox().inflate(16.0D));
/* 558 */     nearbyPiglins.stream()
/* 559 */       .filter(PiglinAi::isIdle)
/* 560 */       .filter(piglin -> (!onlyIfTheySeeThePlayer || BehaviorUtils.canSee((LivingEntity)piglin, (LivingEntity)player)))
/* 561 */       .forEach(piglin -> {
/*     */           if (((Boolean)level.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue()) {
/*     */             setAngerTargetToNearestTargetablePlayerIfFound(level, piglin, (LivingEntity)player);
/*     */           } else {
/*     */             setAngerTarget(level, piglin, (LivingEntity)player);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static InteractionResult mobInteract(ServerLevel level, Piglin body, Player player, InteractionHand hand) {
/* 571 */     ItemStack playerHeldItemStack = player.getItemInHand(hand);
/* 572 */     if (canAdmire(body, playerHeldItemStack)) {
/* 573 */       ItemStack taken = playerHeldItemStack.consumeAndReturn(1, (LivingEntity)player);
/* 574 */       holdInOffhand(level, body, taken);
/* 575 */       admireGoldItem((LivingEntity)body);
/* 576 */       stopWalking(body);
/* 577 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 579 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   protected static boolean canAdmire(Piglin body, ItemStack playerHeldItemStack) {
/* 583 */     return (!isAdmiringDisabled(body) && !isAdmiringItem(body) && body.isAdult() && isBarterCurrency(playerHeldItemStack));
/*     */   }
/*     */   
/*     */   protected static void wasHurtBy(ServerLevel level, Piglin body, LivingEntity attacker) {
/* 588 */     if (attacker instanceof Piglin)
/*     */       return; 
/* 593 */     if (isHoldingItemInOffHand(body))
/* 594 */       stopHoldingOffHandItem(level, body, false); 
/* 596 */     Brain<Piglin> brain = body.getBrain();
/* 597 */     brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
/* 598 */     brain.eraseMemory(MemoryModuleType.DANCING);
/* 599 */     brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
/* 601 */     if (attacker instanceof Player)
/* 603 */       brain.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, Boolean.valueOf(true), 400L); 
/* 606 */     getAvoidTarget(body).ifPresent(avoidTarget -> {
/*     */           if (avoidTarget.getType() != attacker.getType())
/*     */             brain.eraseMemory(MemoryModuleType.AVOID_TARGET); 
/*     */         });
/* 613 */     if (body.isBaby()) {
/* 615 */       brain.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, attacker, 100L);
/* 616 */       if (Sensor.isEntityAttackableIgnoringLineOfSight(level, (LivingEntity)body, attacker))
/* 617 */         broadcastAngerTarget(level, body, attacker); 
/*     */       return;
/*     */     } 
/* 622 */     if (attacker.is(EntityTypes.HOGLIN) && hoglinsOutnumberPiglins(body)) {
/* 624 */       setAvoidTargetAndDontHuntForAWhile(body, attacker);
/* 625 */       broadcastRetreat(body, attacker);
/*     */       return;
/*     */     } 
/* 629 */     maybeRetaliate(level, body, attacker);
/*     */   }
/*     */   
/*     */   protected static void maybeRetaliate(ServerLevel level, AbstractPiglin body, LivingEntity attacker) {
/* 633 */     if (body.getBrain().isActive(Activity.AVOID))
/*     */       return; 
/* 636 */     if (!Sensor.isEntityAttackableIgnoringLineOfSight(level, (LivingEntity)body, attacker))
/*     */       return; 
/* 639 */     if (BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget((LivingEntity)body, attacker, 4.0D))
/*     */       return; 
/* 645 */     if (attacker.is(EntityTypes.PLAYER) && ((Boolean)level.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue()) {
/* 648 */       setAngerTargetToNearestTargetablePlayerIfFound(level, body, attacker);
/* 649 */       broadcastUniversalAnger(level, body);
/*     */     } else {
/* 651 */       setAngerTarget(level, body, attacker);
/* 652 */       broadcastAngerTarget(level, body, attacker);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Optional<SoundEvent> getSoundForCurrentActivity(Piglin body) {
/* 657 */     return body.getBrain().getActiveNonCoreActivity().map(activity -> getSoundForActivity(body, activity));
/*     */   }
/*     */   
/*     */   private static SoundEvent getSoundForActivity(Piglin body, Activity activity) {
/* 661 */     if (activity == Activity.FIGHT)
/* 662 */       return SoundEvents.PIGLIN_ANGRY; 
/* 663 */     if (body.isConverting())
/* 664 */       return SoundEvents.PIGLIN_RETREAT; 
/* 665 */     if (activity == Activity.AVOID && isNearAvoidTarget(body))
/* 666 */       return SoundEvents.PIGLIN_RETREAT; 
/* 667 */     if (activity == Activity.ADMIRE_ITEM)
/* 668 */       return SoundEvents.PIGLIN_ADMIRING_ITEM; 
/* 669 */     if (activity == Activity.CELEBRATE)
/* 670 */       return SoundEvents.PIGLIN_CELEBRATE; 
/* 671 */     if (seesPlayerHoldingLovedItem((LivingEntity)body))
/* 672 */       return SoundEvents.PIGLIN_JEALOUS; 
/* 673 */     if (isNearRepellent(body))
/* 674 */       return SoundEvents.PIGLIN_RETREAT; 
/* 676 */     return SoundEvents.PIGLIN_AMBIENT;
/*     */   }
/*     */   
/*     */   private static boolean isNearAvoidTarget(Piglin body) {
/* 681 */     Brain<Piglin> brain = body.getBrain();
/* 682 */     if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET))
/* 683 */       return false; 
/* 685 */     return ((LivingEntity)brain.getMemory(MemoryModuleType.AVOID_TARGET).get()).closerThan((Entity)body, 12.0D);
/*     */   }
/*     */   
/*     */   protected static List<AbstractPiglin> getVisibleAdultPiglins(Piglin body) {
/* 689 */     return (List<AbstractPiglin>)body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
/*     */   }
/*     */   
/*     */   private static List<AbstractPiglin> getAdultPiglins(AbstractPiglin body) {
/* 693 */     return (List<AbstractPiglin>)body.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
/*     */   }
/*     */   
/*     */   public static boolean isWearingSafeArmor(LivingEntity livingEntity) {
/* 697 */     for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
/* 698 */       if (livingEntity.getItemBySlot(slot).is(ItemTags.PIGLIN_SAFE_ARMOR))
/* 699 */         return true; 
/*     */     } 
/* 702 */     return false;
/*     */   }
/*     */   
/*     */   private static void stopWalking(Piglin body) {
/* 706 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 707 */     body.getNavigation().stop();
/*     */   }
/*     */   
/*     */   private static BehaviorControl<LivingEntity> babySometimesRideBabyHoglin() {
/* 711 */     SetEntityLookTargetSometimes.Ticker ticker = new SetEntityLookTargetSometimes.Ticker(RIDE_START_INTERVAL);
/* 712 */     return CopyMemoryWithExpiry.create(e -> (e.isBaby() && ticker.tickDownAndCheck(e.level().getRandom())), MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.RIDE_TARGET, RIDE_DURATION);
/*     */   }
/*     */   
/*     */   protected static void broadcastAngerTarget(ServerLevel level, AbstractPiglin body, LivingEntity target) {
/* 716 */     getAdultPiglins(body).forEach(piglin -> {
/*     */           if (target instanceof Hoglin) {
/*     */             Hoglin hoglin = (Hoglin)target;
/*     */             if (!piglin.canHunt() || !hoglin.canBeHunted())
/*     */               return; 
/*     */           } 
/*     */           setAngerTargetIfCloserThanCurrent(level, piglin, target);
/*     */         });
/*     */   }
/*     */   
/*     */   protected static void broadcastUniversalAnger(ServerLevel level, AbstractPiglin body) {
/* 725 */     getAdultPiglins(body).forEach(piglin -> getNearestVisibleTargetablePlayer(piglin).ifPresent(()));
/*     */   }
/*     */   
/*     */   protected static void setAngerTarget(ServerLevel level, AbstractPiglin body, LivingEntity target) {
/* 729 */     if (!Sensor.isEntityAttackableIgnoringLineOfSight(level, (LivingEntity)body, target))
/*     */       return; 
/* 733 */     body.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 734 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 600L);
/* 735 */     if (target.is(EntityTypes.HOGLIN) && body.canHunt())
/* 736 */       dontKillAnyMoreHoglinsForAWhile(body); 
/* 738 */     if (target.is(EntityTypes.PLAYER) && ((Boolean)level.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue())
/* 739 */       body.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, Boolean.valueOf(true), 600L); 
/*     */   }
/*     */   
/*     */   private static void setAngerTargetToNearestTargetablePlayerIfFound(ServerLevel level, AbstractPiglin body, LivingEntity targetIfNoPlayerFound) {
/* 744 */     Optional<Player> nearestPlayer = getNearestVisibleTargetablePlayer(body);
/* 745 */     if (nearestPlayer.isPresent()) {
/* 746 */       setAngerTarget(level, body, (LivingEntity)nearestPlayer.get());
/*     */     } else {
/* 748 */       setAngerTarget(level, body, targetIfNoPlayerFound);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void setAngerTargetIfCloserThanCurrent(ServerLevel level, AbstractPiglin body, LivingEntity newTarget) {
/* 753 */     Optional<LivingEntity> currentTarget = getAngerTarget(body);
/* 754 */     LivingEntity nearest = BehaviorUtils.getNearestTarget((LivingEntity)body, currentTarget, newTarget);
/* 755 */     if (currentTarget.isPresent() && currentTarget.get() == nearest)
/*     */       return; 
/* 758 */     setAngerTarget(level, body, nearest);
/*     */   }
/*     */   
/*     */   private static Optional<LivingEntity> getAngerTarget(AbstractPiglin body) {
/* 762 */     return BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)body, MemoryModuleType.ANGRY_AT);
/*     */   }
/*     */   
/*     */   public static Optional<LivingEntity> getAvoidTarget(Piglin body) {
/* 766 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET))
/* 767 */       return body.getBrain().getMemory(MemoryModuleType.AVOID_TARGET); 
/* 769 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static Optional<Player> getNearestVisibleTargetablePlayer(AbstractPiglin body) {
/* 773 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER))
/* 774 */       return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER); 
/* 776 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static void broadcastRetreat(Piglin body, LivingEntity target) {
/* 780 */     getVisibleAdultPiglins(body)
/* 781 */       .forEach(abstractPiglin -> {
/*     */           if (abstractPiglin instanceof Piglin) {
/*     */             Piglin piglin = (Piglin)abstractPiglin;
/*     */             retreatFromNearestTarget(piglin, target);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static void retreatFromNearestTarget(Piglin body, LivingEntity newAvoidTarget) {
/* 789 */     Brain<Piglin> brain = body.getBrain();
/* 790 */     LivingEntity nearest = newAvoidTarget;
/* 791 */     nearest = BehaviorUtils.getNearestTarget((LivingEntity)body, brain.getMemory(MemoryModuleType.AVOID_TARGET), nearest);
/* 792 */     nearest = BehaviorUtils.getNearestTarget((LivingEntity)body, brain.getMemory(MemoryModuleType.ATTACK_TARGET), nearest);
/* 793 */     setAvoidTargetAndDontHuntForAWhile(body, nearest);
/*     */   }
/*     */   
/*     */   private static boolean wantsToStopFleeing(Piglin body) {
/* 797 */     Brain<Piglin> brain = body.getBrain();
/* 798 */     if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET))
/* 799 */       return true; 
/* 801 */     LivingEntity avoidedEntity = brain.getMemory(MemoryModuleType.AVOID_TARGET).get();
/* 803 */     if (avoidedEntity.is(EntityTypes.HOGLIN))
/* 804 */       return piglinsEqualOrOutnumberHoglins(body); 
/* 806 */     if (isZombified((Entity)avoidedEntity))
/* 807 */       return !brain.isMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, avoidedEntity); 
/* 809 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean piglinsEqualOrOutnumberHoglins(Piglin body) {
/* 813 */     return !hoglinsOutnumberPiglins(body);
/*     */   }
/*     */   
/*     */   private static boolean hoglinsOutnumberPiglins(Piglin body) {
/* 817 */     int piglinCount = ((Integer)body.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(Integer.valueOf(0))).intValue() + 1;
/* 818 */     int hoglinCount = ((Integer)body.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(Integer.valueOf(0))).intValue();
/* 819 */     return (hoglinCount > piglinCount);
/*     */   }
/*     */   
/*     */   private static void setAvoidTargetAndDontHuntForAWhile(Piglin body, LivingEntity target) {
/* 823 */     body.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
/* 824 */     body.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 825 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 826 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, target, RETREAT_DURATION.sample(body.level().getRandom()));
/* 827 */     dontKillAnyMoreHoglinsForAWhile(body);
/*     */   }
/*     */   
/*     */   protected static void dontKillAnyMoreHoglinsForAWhile(AbstractPiglin body) {
/* 831 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, Boolean.valueOf(true), TIME_BETWEEN_HUNTS.sample(body.level().getRandom()));
/*     */   }
/*     */   
/*     */   private static void eat(Piglin body) {
/* 835 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, Boolean.valueOf(true), 200L);
/*     */   }
/*     */   
/*     */   private static Vec3 getRandomNearbyPos(Piglin body) {
/* 839 */     Vec3 targetVec = LandRandomPos.getPos((PathfinderMob)body, 4, 2);
/* 840 */     return (targetVec == null) ? body.position() : targetVec;
/*     */   }
/*     */   
/*     */   private static boolean hasEatenRecently(Piglin body) {
/* 844 */     return body.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY);
/*     */   }
/*     */   
/*     */   protected static boolean isIdle(AbstractPiglin body) {
/* 848 */     return body.getBrain().isActive(Activity.IDLE);
/*     */   }
/*     */   
/*     */   private static boolean hasCrossbow(LivingEntity body) {
/* 852 */     return body.isHolding(Items.CROSSBOW);
/*     */   }
/*     */   
/*     */   private static void admireGoldItem(LivingEntity body) {
/* 856 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, Boolean.valueOf(true), 119L);
/*     */   }
/*     */   
/*     */   private static boolean isAdmiringItem(Piglin body) {
/* 860 */     return body.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean isBarterCurrency(ItemStack itemStack) {
/* 864 */     return itemStack.is(BARTERING_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean isFood(ItemStack itemStack) {
/* 868 */     return itemStack.is(ItemTags.PIGLIN_FOOD);
/*     */   }
/*     */   
/*     */   private static boolean isNearRepellent(Piglin body) {
/* 872 */     return body.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
/*     */   }
/*     */   
/*     */   private static boolean seesPlayerHoldingLovedItem(LivingEntity body) {
/* 876 */     return body.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean doesntSeeAnyPlayerHoldingLovedItem(LivingEntity body) {
/* 880 */     return !seesPlayerHoldingLovedItem(body);
/*     */   }
/*     */   
/*     */   public static boolean isPlayerHoldingLovedItem(LivingEntity entity) {
/* 884 */     return (entity.is(EntityTypes.PLAYER) && entity.isHolding(PiglinAi::isLovedItem));
/*     */   }
/*     */   
/*     */   private static boolean isAdmiringDisabled(Piglin body) {
/* 888 */     return body.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
/*     */   }
/*     */   
/*     */   private static boolean wasHurtRecently(LivingEntity body) {
/* 892 */     return body.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
/*     */   }
/*     */   
/*     */   private static boolean isHoldingItemInOffHand(Piglin body) {
/* 896 */     return !body.getOffhandItem().isEmpty();
/*     */   }
/*     */   
/*     */   private static boolean isNotHoldingLovedItemInOffHand(Piglin body) {
/* 900 */     return (body.getOffhandItem().isEmpty() || !isLovedItem(body.getOffhandItem()));
/*     */   }
/*     */   
/*     */   public static boolean isZombified(Entity entity) {
/* 904 */     return (entity.is(EntityTypes.ZOMBIFIED_PIGLIN) || entity.is(EntityTypes.ZOGLIN));
/*     */   }
/*     */   
/*     */   public static List<AbstractPiglin> findNearbyAdultPiglins(Brain<?> brain) {
/* 908 */     List<LivingEntity> livingEntities = brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(List.of());
/* 909 */     List<AbstractPiglin> adultPiglins = new ArrayList<>();
/* 910 */     for (LivingEntity entity : livingEntities) {
/* 911 */       if (entity instanceof AbstractPiglin) {
/* 911 */         AbstractPiglin piglin = (AbstractPiglin)entity;
/* 911 */         if (piglin.isAdult())
/* 912 */           adultPiglins.add(piglin); 
/*     */       } 
/*     */     } 
/* 915 */     return adultPiglins;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\piglin\PiglinAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */