/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.npc.villager.VillagerProfession;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class VillagerGoalPackages {
/*     */   private static final float STROLL_SPEED_MODIFIER = 0.4F;
/*     */   
/*     */   public static final int INTERACT_DIST_SQR = 5;
/*     */   
/*     */   public static final int INTERACT_WALKUP_DIST = 2;
/*     */   
/*     */   public static final float INTERACT_SPEED_MODIFIER = 0.5F;
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getCorePackage(Holder<VillagerProfession> profession, float speedModifier) {
/*  42 */     return ImmutableList.of(
/*  43 */         Pair.of(Integer.valueOf(0), new Swim<>(0.8F)), 
/*  44 */         Pair.of(Integer.valueOf(0), InteractWithDoor.create()), 
/*  45 */         Pair.of(Integer.valueOf(0), new LookAtTargetSink(45, 90)), 
/*  46 */         Pair.of(Integer.valueOf(0), new VillagerPanicTrigger()), 
/*  47 */         Pair.of(Integer.valueOf(0), WakeUp.create()), 
/*  48 */         Pair.of(Integer.valueOf(0), ReactToBell.create()), 
/*  49 */         Pair.of(Integer.valueOf(0), SetRaidStatus.create()), 
/*  50 */         Pair.of(Integer.valueOf(0), ValidateNearbyPoi.create(((VillagerProfession)profession.value()).heldJobSite(), MemoryModuleType.JOB_SITE)), 
/*  51 */         Pair.of(Integer.valueOf(0), ValidateNearbyPoi.create(((VillagerProfession)profession.value()).acquirableJobSite(), MemoryModuleType.POTENTIAL_JOB_SITE)), 
/*  52 */         Pair.of(Integer.valueOf(1), new MoveToTargetSink()), 
/*  53 */         Pair.of(Integer.valueOf(2), PoiCompetitorScan.create()), 
/*  54 */         Pair.of(Integer.valueOf(3), new LookAndFollowTradingPlayerSink(speedModifier)), (Object[])new Pair[] { Pair.of(Integer.valueOf(5), GoToWantedItem.create(speedModifier, false, 4)), 
/*     */           
/*  57 */           Pair.of(Integer.valueOf(6), AcquirePoi.create(((VillagerProfession)profession.value()).acquirableJobSite(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty(), (l, p) -> true)), 
/*  58 */           Pair.of(Integer.valueOf(7), new GoToPotentialJobSite(speedModifier)), 
/*  59 */           Pair.of(Integer.valueOf(8), YieldJobSite.create(speedModifier)), 
/*  60 */           Pair.of(Integer.valueOf(10), AcquirePoi.create(p -> p.is(PoiTypes.HOME), MemoryModuleType.HOME, false, Optional.of(Byte.valueOf((byte)14)), VillagerGoalPackages::validateBedPoi)), 
/*  61 */           Pair.of(Integer.valueOf(10), AcquirePoi.create(p -> p.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT, true, Optional.of(Byte.valueOf((byte)14)))), 
/*  62 */           Pair.of(Integer.valueOf(10), AssignProfessionFromJobSite.create()), 
/*  63 */           Pair.of(Integer.valueOf(10), ResetProfession.create()) });
/*     */   }
/*     */   
/*     */   private static boolean validateBedPoi(ServerLevel level, BlockPos blockPos) {
/*  68 */     BlockState blockState = level.getBlockState(blockPos);
/*  69 */     return (blockState.is(BlockTags.BEDS) && !((Boolean)blockState.getValue((Property)BedBlock.OCCUPIED)).booleanValue());
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getWorkPackage(Holder<VillagerProfession> profession, float speedModifier) {
/*     */     WorkAtPoi workAtPoi;
/*  74 */     if (profession.is(VillagerProfession.FARMER)) {
/*  75 */       workAtPoi = new WorkAtComposter();
/*     */     } else {
/*  77 */       workAtPoi = new WorkAtPoi();
/*     */     } 
/*  80 */     return ImmutableList.of(
/*  81 */         getMinimalLookBehavior(), 
/*  82 */         Pair.of(Integer.valueOf(5), new RunOne<>((List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/*  83 */               Pair.of(workAtPoi, Integer.valueOf(7)), 
/*  84 */               Pair.of(StrollAroundPoi.create(MemoryModuleType.JOB_SITE, 0.4F, 4), Integer.valueOf(2)), 
/*  85 */               Pair.of(StrollToPoi.create(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), Integer.valueOf(5)), 
/*  86 */               Pair.of(StrollToPoiList.create(MemoryModuleType.SECONDARY_JOB_SITE, speedModifier, 1, 6, MemoryModuleType.JOB_SITE), Integer.valueOf(5)), 
/*  87 */               Pair.of(new HarvestFarmland(), Integer.valueOf(profession.is(VillagerProfession.FARMER) ? 2 : 5)), 
/*  88 */               Pair.of(new UseBonemeal(), Integer.valueOf(profession.is(VillagerProfession.FARMER) ? 4 : 7))))), 
/*     */         
/*  90 */         Pair.of(Integer.valueOf(10), new ShowTradesToPlayer(400, 1600)), 
/*  91 */         Pair.of(Integer.valueOf(10), SetLookAndInteract.create(EntityTypes.PLAYER, 4)), 
/*  92 */         Pair.of(Integer.valueOf(2), SetWalkTargetFromBlockMemory.create(MemoryModuleType.JOB_SITE, speedModifier, 9, 100, 1200)), 
/*  93 */         Pair.of(Integer.valueOf(3), new GiveGiftToHero(100)), 
/*  94 */         Pair.of(Integer.valueOf(99), UpdateActivityFromSchedule.create()));
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPlayPackage(float speedModifier) {
/*  99 */     return ImmutableList.of(
/* 100 */         Pair.of(Integer.valueOf(0), new MoveToTargetSink(80, 120)), 
/* 101 */         getFullLookBehavior(), 
/* 102 */         Pair.of(Integer.valueOf(5), PlayTagWithOtherKids.create()), 
/* 103 */         Pair.of(Integer.valueOf(5), new RunOne<>(
/* 104 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT), 
/*     */             
/* 108 */             (List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 109 */               Pair.of(InteractWith.of(EntityTypes.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, speedModifier, 2), Integer.valueOf(2)), 
/* 110 */               Pair.of(InteractWith.of(EntityTypes.CAT, 8, MemoryModuleType.INTERACTION_TARGET, speedModifier, 2), Integer.valueOf(1)), 
/* 111 */               Pair.of(VillageBoundRandomStroll.create(speedModifier), Integer.valueOf(1)), 
/* 112 */               Pair.of(SetWalkTargetFromLookTarget.create(speedModifier, 2), Integer.valueOf(1)), 
/* 113 */               Pair.of(new JumpOnBed(speedModifier), Integer.valueOf(2)), 
/* 114 */               Pair.of(new DoNothing(20, 40), Integer.valueOf(2))))), 
/*     */         
/* 117 */         Pair.of(Integer.valueOf(99), UpdateActivityFromSchedule.create()));
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getRestPackage(float speedModifier) {
/* 122 */     return ImmutableList.of(
/* 123 */         Pair.of(Integer.valueOf(2), SetWalkTargetFromBlockMemory.create(MemoryModuleType.HOME, speedModifier, 1, 150, 1200)), 
/* 124 */         Pair.of(Integer.valueOf(3), ValidateNearbyPoi.create(p -> p.is(PoiTypes.HOME), MemoryModuleType.HOME)), 
/* 125 */         Pair.of(Integer.valueOf(3), new SleepInBed()), 
/* 126 */         Pair.of(Integer.valueOf(5), new RunOne<>(
/* 127 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT), 
/*     */             
/* 131 */             (List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 132 */               Pair.of(SetClosestHomeAsWalkTarget.create(speedModifier), Integer.valueOf(1)), 
/* 133 */               Pair.of(InsideBrownianWalk.create(speedModifier), Integer.valueOf(4)), 
/* 134 */               Pair.of(GoToClosestVillage.create(speedModifier, 4), Integer.valueOf(2)), 
/* 135 */               Pair.of(new DoNothing(20, 40), Integer.valueOf(2))))), 
/*     */         
/* 138 */         getMinimalLookBehavior(), 
/* 139 */         Pair.of(Integer.valueOf(99), UpdateActivityFromSchedule.create()));
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getMeetPackage(float speedModifier) {
/* 144 */     return ImmutableList.of(
/* 145 */         Pair.of(Integer.valueOf(2), TriggerGate.triggerOneShuffled((List<Pair<? extends Trigger<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 146 */               Pair.of(StrollAroundPoi.create(MemoryModuleType.MEETING_POINT, 0.4F, 40), Integer.valueOf(2)), 
/* 147 */               Pair.of(SocializeAtBell.create(), Integer.valueOf(2))))), 
/*     */         
/* 149 */         Pair.of(Integer.valueOf(10), new ShowTradesToPlayer(400, 1600)), 
/* 150 */         Pair.of(Integer.valueOf(10), SetLookAndInteract.create(EntityTypes.PLAYER, 4)), 
/* 151 */         Pair.of(Integer.valueOf(2), SetWalkTargetFromBlockMemory.create(MemoryModuleType.MEETING_POINT, speedModifier, 6, 100, 200)), 
/* 152 */         Pair.of(Integer.valueOf(3), new GiveGiftToHero(100)), 
/* 153 */         Pair.of(Integer.valueOf(3), ValidateNearbyPoi.create(p -> p.is(PoiTypes.MEETING), MemoryModuleType.MEETING_POINT)), 
/* 154 */         Pair.of(Integer.valueOf(3), new GateBehavior<>(
/* 155 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), 
/* 156 */             (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, 
/*     */             
/* 159 */             (List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 160 */               Pair.of(new TradeWithVillager(), Integer.valueOf(1))))), 
/*     */         
/* 163 */         getFullLookBehavior(), 
/* 164 */         Pair.of(Integer.valueOf(99), UpdateActivityFromSchedule.create()));
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getIdlePackage(float speedModifier) {
/* 169 */     return ImmutableList.of(
/* 170 */         Pair.of(Integer.valueOf(2), new RunOne<>((List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 171 */               Pair.of(InteractWith.of(EntityTypes.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, speedModifier, 2), Integer.valueOf(2)), 
/* 172 */               Pair.of(InteractWith.of(EntityTypes.VILLAGER, 8, AgeableMob::canBreed, AgeableMob::canBreed, MemoryModuleType.BREED_TARGET, speedModifier, 2), Integer.valueOf(1)), 
/* 173 */               Pair.of(InteractWith.of(EntityTypes.CAT, 8, MemoryModuleType.INTERACTION_TARGET, speedModifier, 2), Integer.valueOf(1)), 
/* 174 */               Pair.of(VillageBoundRandomStroll.create(speedModifier), Integer.valueOf(1)), 
/* 175 */               Pair.of(SetWalkTargetFromLookTarget.create(speedModifier, 2), Integer.valueOf(1)), 
/* 176 */               Pair.of(new JumpOnBed(speedModifier), Integer.valueOf(1)), 
/* 177 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))), 
/*     */         
/* 179 */         Pair.of(Integer.valueOf(3), new GiveGiftToHero(100)), 
/* 180 */         Pair.of(Integer.valueOf(3), SetLookAndInteract.create(EntityTypes.PLAYER, 4)), 
/* 181 */         Pair.of(Integer.valueOf(3), new ShowTradesToPlayer(400, 1600)), 
/* 182 */         Pair.of(Integer.valueOf(3), new GateBehavior<>(
/* 183 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), 
/* 184 */             (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, 
/*     */             
/* 187 */             (List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 188 */               Pair.of(new TradeWithVillager(), Integer.valueOf(1))))), 
/*     */         
/* 191 */         Pair.of(Integer.valueOf(3), new GateBehavior<>(
/* 192 */             (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(), 
/* 193 */             (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.BREED_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, 
/*     */             
/* 196 */             (List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 197 */               Pair.of(new VillagerMakeLove(), Integer.valueOf(1))))), 
/*     */         
/* 200 */         getFullLookBehavior(), 
/* 201 */         Pair.of(Integer.valueOf(99), UpdateActivityFromSchedule.create()));
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPanicPackage(float speedModifier) {
/* 206 */     float runawaySpeed = speedModifier * 1.5F;
/* 208 */     return ImmutableList.of(
/* 209 */         Pair.of(Integer.valueOf(0), VillagerCalmDown.create()), 
/* 210 */         Pair.of(Integer.valueOf(1), SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_HOSTILE, runawaySpeed, 6, false)), 
/* 211 */         Pair.of(Integer.valueOf(1), SetWalkTargetAwayFrom.entity(MemoryModuleType.HURT_BY_ENTITY, runawaySpeed, 6, false)), 
/* 212 */         Pair.of(Integer.valueOf(3), VillageBoundRandomStroll.create(runawaySpeed, 2, 2)), 
/* 213 */         getMinimalLookBehavior());
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getPreRaidPackage(float speedModifier) {
/* 218 */     return ImmutableList.of(
/* 219 */         Pair.of(Integer.valueOf(0), RingBell.create()), 
/* 220 */         Pair.of(Integer.valueOf(0), TriggerGate.triggerOneShuffled((List<Pair<? extends Trigger<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 221 */               Pair.of(SetWalkTargetFromBlockMemory.create(MemoryModuleType.MEETING_POINT, speedModifier * 1.5F, 2, 150, 200), Integer.valueOf(6)), 
/* 222 */               Pair.of(VillageBoundRandomStroll.create(speedModifier * 1.5F), Integer.valueOf(2))))), 
/*     */         
/* 224 */         getMinimalLookBehavior(), 
/* 225 */         Pair.of(Integer.valueOf(99), ResetRaidStatus.create()));
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getRaidPackage(float speedModifier) {
/* 230 */     return ImmutableList.of(
/* 231 */         Pair.of(Integer.valueOf(0), BehaviorBuilder.sequence(
/* 232 */             BehaviorBuilder.triggerIf(VillagerGoalPackages::raidExistsAndNotVictory), 
/* 233 */             TriggerGate.triggerOneShuffled((List<Pair<? extends Trigger<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 234 */                 Pair.of(MoveToSkySeeingSpot.create(speedModifier), Integer.valueOf(5)), 
/* 235 */                 Pair.of(VillageBoundRandomStroll.create(speedModifier * 1.1F), Integer.valueOf(2)))))), 
/*     */         
/* 238 */         Pair.of(Integer.valueOf(0), new CelebrateVillagersSurvivedRaid(600, 600)), 
/* 239 */         Pair.of(Integer.valueOf(2), BehaviorBuilder.sequence(
/* 240 */             BehaviorBuilder.triggerIf(VillagerGoalPackages::raidExistsAndActive), 
/* 241 */             LocateHidingPlace.create(24, speedModifier * 1.4F, 1))), 
/*     */         
/* 243 */         getMinimalLookBehavior(), 
/* 244 */         Pair.of(Integer.valueOf(99), ResetRaidStatus.create()));
/*     */   }
/*     */   
/*     */   public static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getHidePackage(float speedModifier) {
/* 249 */     int closeEnoughDist = 2;
/* 250 */     return ImmutableList.of(
/* 251 */         Pair.of(Integer.valueOf(0), SetHiddenState.create(15, 3)), 
/* 252 */         Pair.of(Integer.valueOf(1), LocateHidingPlace.create(32, speedModifier * 1.25F, 2)), 
/* 253 */         getMinimalLookBehavior());
/*     */   }
/*     */   
/*     */   private static Pair<Integer, BehaviorControl<LivingEntity>> getFullLookBehavior() {
/* 258 */     return Pair.of(Integer.valueOf(5), new RunOne<>((List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 259 */             Pair.of(SetEntityLookTarget.create(EntityTypes.CAT, 8.0F), Integer.valueOf(8)), 
/* 260 */             Pair.of(SetEntityLookTarget.create(EntityTypes.VILLAGER, 8.0F), Integer.valueOf(2)), 
/* 261 */             Pair.of(SetEntityLookTarget.create(EntityTypes.PLAYER, 8.0F), Integer.valueOf(2)), 
/* 262 */             Pair.of(SetEntityLookTarget.create(MobCategory.CREATURE, 8.0F), Integer.valueOf(1)), 
/* 263 */             Pair.of(SetEntityLookTarget.create(MobCategory.WATER_CREATURE, 8.0F), Integer.valueOf(1)), 
/*     */             
/* 265 */             Pair.of(SetEntityLookTarget.create(MobCategory.AXOLOTLS, 8.0F), Integer.valueOf(1)), 
/* 266 */             Pair.of(SetEntityLookTarget.create(MobCategory.UNDERGROUND_WATER_CREATURE, 8.0F), Integer.valueOf(1)), 
/* 267 */             Pair.of(SetEntityLookTarget.create(MobCategory.WATER_AMBIENT, 8.0F), Integer.valueOf(1)), 
/* 268 */             Pair.of(SetEntityLookTarget.create(MobCategory.MONSTER, 8.0F), Integer.valueOf(1)), 
/* 269 */             Pair.of(new DoNothing(30, 60), Integer.valueOf(2)))));
/*     */   }
/*     */   
/*     */   private static Pair<Integer, BehaviorControl<LivingEntity>> getMinimalLookBehavior() {
/* 274 */     return Pair.of(Integer.valueOf(5), new RunOne<>((List<Pair<? extends BehaviorControl<? super LivingEntity>, Integer>>)ImmutableList.of(
/* 275 */             Pair.of(SetEntityLookTarget.create(EntityTypes.VILLAGER, 8.0F), Integer.valueOf(2)), 
/* 276 */             Pair.of(SetEntityLookTarget.create(EntityTypes.PLAYER, 8.0F), Integer.valueOf(2)), 
/* 277 */             Pair.of(new DoNothing(30, 60), Integer.valueOf(8)))));
/*     */   }
/*     */   
/*     */   private static boolean raidExistsAndActive(ServerLevel level, LivingEntity body) {
/* 282 */     Raid currentRaid = level.getRaidAt(body.blockPosition());
/* 283 */     return (currentRaid != null && currentRaid.isActive() && !currentRaid.isVictory() && !currentRaid.isLoss());
/*     */   }
/*     */   
/*     */   private static boolean raidExistsAndNotVictory(ServerLevel level, LivingEntity body) {
/* 287 */     Raid currentRaid = level.getRaidAt(body.blockPosition());
/* 288 */     return (currentRaid != null && currentRaid.isVictory());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\VillagerGoalPackages.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */