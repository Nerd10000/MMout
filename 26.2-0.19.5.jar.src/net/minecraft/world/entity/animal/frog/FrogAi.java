/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.Croak;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpMidJump;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpToPreferredBlock;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpToRandomPos;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.TryFindLand;
/*     */ import net.minecraft.world.entity.ai.behavior.TryFindLandNearWater;
/*     */ import net.minecraft.world.entity.ai.behavior.TryLaySpawnOnFluidNearLand;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.PathfindingContext;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ 
/*     */ public class FrogAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_ON_LAND = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_IN_WATER = 0.75F;
/*     */   
/*  59 */   private static final UniformInt TIME_BETWEEN_LONG_JUMPS = UniformInt.of(100, 140);
/*     */   
/*     */   private static final int MAX_LONG_JUMP_HEIGHT = 2;
/*     */   
/*     */   private static final int MAX_LONG_JUMP_WIDTH = 4;
/*     */   
/*     */   private static final float MAX_JUMP_VELOCITY_MULTIPLIER = 3.5714288F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*     */   
/*     */   protected static void initMemories(Frog body, RandomSource random) {
/*  66 */     body.getBrain().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, Integer.valueOf(TIME_BETWEEN_LONG_JUMPS.sample(random)));
/*     */   }
/*     */   
/*     */   protected static List<ActivityData<Frog>> getActivities() {
/*  70 */     return List.of(
/*  71 */         initCoreActivity(), 
/*  72 */         initIdleActivity(), 
/*  73 */         initSwimActivity(), 
/*  74 */         initLaySpawnActivity(), 
/*  75 */         initTongueActivity(), 
/*  76 */         initJumpActivity());
/*     */   }
/*     */   
/*     */   private static ActivityData<Frog> initCoreActivity() {
/*  81 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Frog> initIdleActivity() {
/*  91 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/*  92 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/*  93 */           Pair.of(Integer.valueOf(0), new AnimalMakeLove(EntityTypes.FROG)), 
/*  94 */           Pair.of(Integer.valueOf(1), new FollowTemptation(s -> Float.valueOf(1.25F))), 
/*  95 */           Pair.of(Integer.valueOf(2), StartAttacking.create((level, body) -> canAttack((Mob)body), (level, body) -> body.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/*  96 */           Pair.of(Integer.valueOf(3), TryFindLand.create(6, 1.0F)), 
/*  97 */           Pair.of(Integer.valueOf(4), new RunOne(
/*  98 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */               
/* 101 */               (List)ImmutableList.of(
/* 102 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(1)), 
/* 103 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)), 
/* 104 */                 Pair.of(new Croak(), Integer.valueOf(3)), 
/* 105 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), Integer.valueOf(2)))))), 
/*     */         
/* 108 */         (Set)ImmutableSet.of(
/* 109 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT), 
/* 110 */           Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Frog> initSwimActivity() {
/* 115 */     return ActivityData.create(Activity.SWIM, ImmutableList.of(
/* 116 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 117 */           Pair.of(Integer.valueOf(1), new FollowTemptation(s -> Float.valueOf(1.25F))), 
/* 118 */           Pair.of(Integer.valueOf(2), StartAttacking.create((level, body) -> canAttack((Mob)body), (level, body) -> body.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/* 119 */           Pair.of(Integer.valueOf(3), TryFindLand.create(8, 1.5F)), 
/* 120 */           Pair.of(Integer.valueOf(5), new GateBehavior(
/* 121 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */               
/* 124 */               (Set)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*     */               
/* 127 */               (List)ImmutableList.of(
/* 128 */                 Pair.of(RandomStroll.swim(0.75F), Integer.valueOf(1)), 
/* 129 */                 Pair.of(RandomStroll.stroll(1.0F, true), Integer.valueOf(1)), 
/* 130 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)), 
/* 131 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), Integer.valueOf(5)))))), 
/*     */         
/* 134 */         (Set)ImmutableSet.of(
/* 135 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT), 
/* 136 */           Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Frog> initLaySpawnActivity() {
/* 141 */     return ActivityData.create(Activity.LAY_SPAWN, ImmutableList.of(
/* 142 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 143 */           Pair.of(Integer.valueOf(1), StartAttacking.create((level, body) -> canAttack((Mob)body), (level, body) -> body.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/* 144 */           Pair.of(Integer.valueOf(2), TryFindLandNearWater.create(8, 1.0F)), 
/* 145 */           Pair.of(Integer.valueOf(3), TryLaySpawnOnFluidNearLand.create(Blocks.FROGSPAWN)), 
/* 146 */           Pair.of(Integer.valueOf(4), new RunOne(
/* 147 */               (List)ImmutableList.of(
/* 148 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(2)), 
/* 149 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)), 
/* 150 */                 Pair.of(new Croak(), Integer.valueOf(2)), 
/* 151 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), Integer.valueOf(1)))))), 
/*     */         
/* 154 */         (Set)ImmutableSet.of(
/* 155 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT), 
/* 156 */           Pair.of(MemoryModuleType.IS_PREGNANT, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Frog> initJumpActivity() {
/* 161 */     return ActivityData.create(Activity.LONG_JUMP, ImmutableList.of(
/* 162 */           Pair.of(Integer.valueOf(0), new LongJumpMidJump(TIME_BETWEEN_LONG_JUMPS, SoundEvents.FROG_STEP)), 
/* 163 */           Pair.of(Integer.valueOf(1), new LongJumpToPreferredBlock(TIME_BETWEEN_LONG_JUMPS, 2, 4, 3.5714288F, paramFrog -> SoundEvents.FROG_LONG_JUMP, BlockTags.FROG_PREFER_JUMP_TO, 0.5F, FrogAi::isAcceptableLandingSpot))), 
/*     */         
/* 173 */         (Set)ImmutableSet.of(
/* 174 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 175 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 176 */           Pair.of(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT), 
/* 177 */           Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Frog> initTongueActivity() {
/* 182 */     return ActivityData.create(Activity.TONGUE, 0, ImmutableList.of(
/* 183 */           StopAttackingIfTargetInvalid.create(), new ShootTongue(SoundEvents.FROG_TONGUE, SoundEvents.FROG_EAT)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */   
/*     */   private static <E extends Mob> boolean isAcceptableLandingSpot(E body, BlockPos targetPos) {
/* 189 */     Level level = body.level();
/* 190 */     BlockPos below = targetPos.below();
/* 191 */     if (!level.getFluidState(targetPos).isEmpty() || 
/* 192 */       !level.getFluidState(below).isEmpty() || 
/* 193 */       !level.getFluidState(targetPos.above()).isEmpty())
/* 194 */       return false; 
/* 196 */     BlockState bs = level.getBlockState(targetPos);
/* 197 */     BlockState bsBelow = level.getBlockState(below);
/* 198 */     if (bs.is(BlockTags.FROG_PREFER_JUMP_TO) || bsBelow.is(BlockTags.FROG_PREFER_JUMP_TO))
/* 199 */       return true; 
/* 201 */     PathfindingContext context = new PathfindingContext((CollisionGetter)body.level(), (Mob)body);
/* 202 */     PathType pathType = WalkNodeEvaluator.getPathTypeStatic(context, targetPos.mutable());
/* 203 */     PathType pathTypeBelow = WalkNodeEvaluator.getPathTypeStatic(context, below.mutable());
/* 204 */     if (pathType == PathType.TRAPDOOR || (bs.isAir() && pathTypeBelow == PathType.TRAPDOOR))
/* 205 */       return true; 
/* 207 */     return LongJumpToRandomPos.defaultAcceptableLandingSpot((Mob)body, targetPos);
/*     */   }
/*     */   
/*     */   private static boolean canAttack(Mob mob) {
/* 211 */     return !BehaviorUtils.isBreeding((LivingEntity)mob);
/*     */   }
/*     */   
/*     */   public static void updateActivity(Frog body) {
/* 215 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.TONGUE, Activity.LAY_SPAWN, Activity.LONG_JUMP, Activity.SWIM, Activity.IDLE));
/*     */   }
/*     */   
/*     */   public static Predicate<ItemStack> getTemptations() {
/* 225 */     return i -> i.is(ItemTags.FROG_FOOD);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\frog\FrogAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */