/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SnifferAi {
/*  41 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_LOOK_DISTANCE = 6;
/*     */   
/*     */   private static final int SNIFFING_COOLDOWN_TICKS = 9600;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_SNIFFING = 1.25F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*     */   
/*     */   public static List<ActivityData<Sniffer>> getActivities() {
/*  53 */     return List.of(
/*  54 */         initCoreActivity(), 
/*  55 */         initIdleActivity(), 
/*  56 */         initSniffingActivity(), 
/*  57 */         initDigActivity());
/*     */   }
/*     */   
/*     */   private static Sniffer resetSniffing(Sniffer body) {
/*  63 */     body.getBrain().eraseMemory(MemoryModuleType.SNIFFER_DIGGING);
/*  64 */     body.getBrain().eraseMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
/*  66 */     return body.transitionTo(Sniffer.State.IDLING);
/*     */   }
/*     */   
/*     */   private static ActivityData<Sniffer> initCoreActivity() {
/*  70 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic<Sniffer>(2.0F) {
/*     */             protected void start(ServerLevel level, Sniffer body, long timestamp) {
/*  75 */               SnifferAi.resetSniffing(body);
/*  76 */               super.start(level, (PathfinderMob)body, timestamp);
/*     */             }
/*     */           }new MoveToTargetSink(500, 700), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Sniffer> initSniffingActivity() {
/*  85 */     return ActivityData.create(Activity.SNIFF, 
/*  86 */         ImmutableList.of(
/*  87 */           Pair.of(Integer.valueOf(0), new Searching())), 
/*  88 */         Set.of(
/*  89 */           Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT), 
/*  90 */           Pair.of(MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_PRESENT), 
/*  91 */           Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Sniffer> initDigActivity() {
/*  96 */     return ActivityData.create(Activity.DIG, 
/*  97 */         ImmutableList.of(
/*  98 */           Pair.of(Integer.valueOf(0), new Digging(160, 180)), 
/*  99 */           Pair.of(Integer.valueOf(0), new FinishedDigging(40))), 
/*     */         
/* 101 */         Set.of(
/* 102 */           Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT), 
/* 103 */           Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 104 */           Pair.of(MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Sniffer> initIdleActivity() {
/* 110 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/* 111 */           Pair.of(Integer.valueOf(0), new AnimalMakeLove(EntityTypes.SNIFFER) {
/*     */               protected void start(ServerLevel level, Animal body, long timestamp) {
/* 114 */                 SnifferAi.resetSniffing((Sniffer)body);
/* 115 */                 super.start(level, body, timestamp);
/*     */               }
/* 118 */             }), Pair.of(Integer.valueOf(1), new FollowTemptation(sniffer -> Float.valueOf(1.25F), sniffer -> Double.valueOf(sniffer.isBaby() ? 2.5D : 3.5D)) {
/*     */               protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/* 122 */                 SnifferAi.resetSniffing((Sniffer)body);
/* 123 */                 super.start(level, body, timestamp);
/*     */               }
/* 126 */             }), Pair.of(Integer.valueOf(2), new LookAtTargetSink(45, 90)), 
/* 127 */           Pair.of(Integer.valueOf(3), new FeelingHappy(40, 100)), 
/* 128 */           Pair.of(Integer.valueOf(4), new RunOne((List)ImmutableList.of(
/* 129 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(2)), 
/* 130 */                 Pair.of(new Scenting(40, 80), Integer.valueOf(1)), 
/* 131 */                 Pair.of(new Sniffing(40, 80), Integer.valueOf(1)), 
/* 132 */                 Pair.of(SetEntityLookTarget.create(EntityTypes.PLAYER, 6.0F), Integer.valueOf(1)), 
/* 133 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(1)), 
/* 134 */                 Pair.of(new DoNothing(5, 20), Integer.valueOf(2)))))), 
/*     */         
/* 137 */         Set.of(
/* 138 */           Pair.of(MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   public static void updateActivity(Sniffer body) {
/* 143 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.DIG, Activity.SNIFF, Activity.IDLE));
/*     */   }
/*     */   
/*     */   private static class Sniffing extends Behavior<Sniffer> {
/*     */     private Sniffing(int min, int max) {
/* 152 */       super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_ABSENT), min, max);
/*     */     }
/*     */     
/*     */     protected boolean checkExtraStartConditions(ServerLevel level, Sniffer body) {
/* 162 */       return (!body.isBaby() && body.canSniff());
/*     */     }
/*     */     
/*     */     protected boolean canStillUse(ServerLevel level, Sniffer body, long timestamp) {
/* 167 */       return body.canSniff();
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 172 */       sniffer.transitionTo(Sniffer.State.SNIFFING);
/*     */     }
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 177 */       boolean finished = timedOut(timestamp);
/* 178 */       sniffer.transitionTo(Sniffer.State.IDLING);
/* 180 */       if (finished)
/* 181 */         sniffer.calculateDigPosition().ifPresent(position -> {
/*     */               sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET, position);
/*     */               sniffer.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(position, 1.25F, 0));
/*     */             }); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Searching extends Behavior<Sniffer> {
/*     */     private Searching() {
/* 191 */       super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_PRESENT), 600);
/*     */     }
/*     */     
/*     */     protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) {
/* 202 */       return sniffer.canSniff();
/*     */     }
/*     */     
/*     */     protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 207 */       if (!sniffer.canSniff()) {
/* 208 */         sniffer.transitionTo(Sniffer.State.IDLING);
/* 209 */         return false;
/*     */       } 
/* 215 */       Optional<BlockPos> walkTarget = sniffer.getBrain().getMemory(MemoryModuleType.WALK_TARGET).map(WalkTarget::getTarget).map(PositionTracker::currentBlockPosition);
/* 217 */       Optional<BlockPos> sniffingTarget = sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
/* 219 */       if (walkTarget.isEmpty() || sniffingTarget.isEmpty())
/* 220 */         return false; 
/* 223 */       return ((BlockPos)sniffingTarget.get()).equals(walkTarget.get());
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 228 */       sniffer.transitionTo(Sniffer.State.SEARCHING);
/*     */     }
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 234 */       if (sniffer.canDig() && sniffer.canSniff())
/* 235 */         sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_DIGGING, Boolean.valueOf(true)); 
/* 239 */       sniffer.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 240 */       sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Digging extends Behavior<Sniffer> {
/*     */     private Digging(int min, int max) {
/* 246 */       super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_ABSENT), min, max);
/*     */     }
/*     */     
/*     */     protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) {
/* 256 */       return sniffer.canSniff();
/*     */     }
/*     */     
/*     */     protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 261 */       return (sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_DIGGING).isPresent() && sniffer.canDig() && !sniffer.isInLove());
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 266 */       sniffer.transitionTo(Sniffer.State.DIGGING);
/*     */     }
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 271 */       boolean finished = timedOut(timestamp);
/* 273 */       if (finished) {
/* 274 */         sniffer.getBrain().setMemoryWithExpiry(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, 9600L);
/*     */       } else {
/* 276 */         SnifferAi.resetSniffing(sniffer);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FinishedDigging extends Behavior<Sniffer> {
/*     */     private FinishedDigging(int duration) {
/* 283 */       super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_PRESENT), duration, duration);
/*     */     }
/*     */     
/*     */     protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) {
/* 293 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 298 */       return sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_DIGGING).isPresent();
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 303 */       sniffer.transitionTo(Sniffer.State.RISING);
/*     */     }
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 308 */       boolean finished = timedOut(timestamp);
/* 310 */       sniffer
/* 311 */         .transitionTo(Sniffer.State.IDLING)
/* 312 */         .onDiggingComplete(finished);
/* 315 */       sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_DIGGING);
/* 316 */       sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_HAPPY, Boolean.valueOf(true));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FeelingHappy extends Behavior<Sniffer> {
/*     */     private FeelingHappy(int min, int max) {
/* 322 */       super(Map.of(MemoryModuleType.SNIFFER_HAPPY, MemoryStatus.VALUE_PRESENT), min, max);
/*     */     }
/*     */     
/*     */     protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 329 */       return true;
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 334 */       sniffer.transitionTo(Sniffer.State.FEELING_HAPPY);
/*     */     }
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 339 */       sniffer.transitionTo(Sniffer.State.IDLING);
/* 340 */       sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_HAPPY);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Scenting extends Behavior<Sniffer> {
/*     */     private Scenting(int min, int max) {
/* 346 */       super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_HAPPY, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), min, max);
/*     */     }
/*     */     
/*     */     protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) {
/* 358 */       return !sniffer.isTempted();
/*     */     }
/*     */     
/*     */     protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 363 */       return true;
/*     */     }
/*     */     
/*     */     protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 368 */       sniffer.transitionTo(Sniffer.State.SCENTING);
/*     */     }
/*     */     
/*     */     protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 373 */       sniffer.transitionTo(Sniffer.State.IDLING);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */