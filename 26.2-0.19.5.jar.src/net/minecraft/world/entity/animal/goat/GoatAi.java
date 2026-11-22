/*     */ package net.minecraft.world.entity.animal.goat;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpMidJump;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpToRandomPos;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.PrepareRamNearestTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.RamTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class GoatAi {
/*     */   public static final int RAM_PREPARE_TIME = 20;
/*     */   
/*     */   public static final int RAM_MAX_DISTANCE = 7;
/*     */   
/*  40 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 1.25F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PREPARING_TO_RAM = 1.25F;
/*     */   
/*  46 */   private static final UniformInt TIME_BETWEEN_LONG_JUMPS = UniformInt.of(600, 1200);
/*     */   
/*     */   public static final int MAX_LONG_JUMP_HEIGHT = 5;
/*     */   
/*     */   public static final int MAX_LONG_JUMP_WIDTH = 5;
/*     */   
/*     */   public static final float MAX_JUMP_VELOCITY_MULTIPLIER = 3.5714288F;
/*     */   
/*  50 */   private static final UniformInt TIME_BETWEEN_RAMS = UniformInt.of(600, 6000);
/*     */   
/*  51 */   private static final UniformInt TIME_BETWEEN_RAMS_SCREAMER = UniformInt.of(100, 300);
/*     */   
/*     */   private static final TargetingConditions RAM_TARGET_CONDITIONS;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_RAMMING = 3.0F;
/*     */   
/*     */   public static final int RAM_MIN_DISTANCE = 4;
/*     */   
/*     */   public static final float ADULT_RAM_KNOCKBACK_FORCE = 2.5F;
/*     */   
/*     */   public static final float BABY_RAM_KNOCKBACK_FORCE = 1.0F;
/*     */   
/*     */   static {
/*  52 */     RAM_TARGET_CONDITIONS = TargetingConditions.forCombat().selector((target, level) -> 
/*  53 */         (!target.is(EntityTypes.GOAT) && (((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() || !target.is(EntityTypes.ARMOR_STAND)) && level.getWorldBorder().isWithinBounds(target.getBoundingBox())));
/*     */   }
/*     */   
/*     */   protected static void initMemories(Goat body, RandomSource random) {
/*  63 */     body.getBrain().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, Integer.valueOf(TIME_BETWEEN_LONG_JUMPS.sample(random)));
/*  64 */     body.getBrain().setMemory(MemoryModuleType.RAM_COOLDOWN_TICKS, Integer.valueOf(TIME_BETWEEN_RAMS.sample(random)));
/*     */   }
/*     */   
/*     */   protected static List<ActivityData<Goat>> getActivities() {
/*  68 */     return List.of(
/*  69 */         initCoreActivity(), 
/*  70 */         initIdleActivity(), 
/*  71 */         initLongJumpActivity(), 
/*  72 */         initRamActivity());
/*     */   }
/*     */   
/*     */   private static ActivityData<Goat> initCoreActivity() {
/*  77 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.RAM_COOLDOWN_TICKS)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Goat> initIdleActivity() {
/*  89 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/*  90 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityTypes.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/*  91 */           Pair.of(Integer.valueOf(0), new AnimalMakeLove(EntityTypes.GOAT)), 
/*  92 */           Pair.of(Integer.valueOf(1), new FollowTemptation(s -> Float.valueOf(1.25F))), 
/*  93 */           Pair.of(Integer.valueOf(2), BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.25F)), 
/*  94 */           Pair.of(Integer.valueOf(3), new RunOne((List)ImmutableList.of(
/*  95 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(2)), 
/*  96 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(2)), 
/*  97 */                 Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))))), 
/*     */         
/*  99 */         (Set)ImmutableSet.of(
/* 100 */           Pair.of(MemoryModuleType.RAM_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 101 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Goat> initLongJumpActivity() {
/* 106 */     return ActivityData.create(Activity.LONG_JUMP, ImmutableList.of(
/* 107 */           Pair.of(Integer.valueOf(0), new LongJumpMidJump(TIME_BETWEEN_LONG_JUMPS, SoundEvents.GOAT_STEP)), 
/* 108 */           Pair.of(Integer.valueOf(1), new LongJumpToRandomPos(TIME_BETWEEN_LONG_JUMPS, 5, 5, 3.5714288F, goat -> goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_LONG_JUMP : SoundEvents.GOAT_LONG_JUMP))), 
/*     */         
/* 115 */         (Set)ImmutableSet.of(
/* 116 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 117 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 118 */           Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 119 */           Pair.of(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Goat> initRamActivity() {
/* 124 */     return ActivityData.create(Activity.RAM, ImmutableList.of(
/* 125 */           Pair.of(Integer.valueOf(0), new RamTarget(goat -> goat.isScreamingGoat() ? TIME_BETWEEN_RAMS_SCREAMER : TIME_BETWEEN_RAMS, RAM_TARGET_CONDITIONS, 3.0F, goat -> goat.isBaby() ? 1.0D : 2.5D, goat -> goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_RAM_IMPACT : SoundEvents.GOAT_RAM_IMPACT, paramGoat -> SoundEvents.GOAT_HORN_BREAK)), 
/*     */           
/* 132 */           Pair.of(Integer.valueOf(1), new PrepareRamNearestTarget(mob -> mob.isScreamingGoat() ? TIME_BETWEEN_RAMS_SCREAMER.minInclusive() : TIME_BETWEEN_RAMS.minInclusive(), 4, 7, 1.25F, RAM_TARGET_CONDITIONS, 20, goat -> goat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_PREPARE_RAM : SoundEvents.GOAT_PREPARE_RAM))), 
/*     */         
/* 140 */         (Set)ImmutableSet.of(
/* 141 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 142 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 143 */           Pair.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   public static void updateActivity(Goat body) {
/* 148 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.RAM, Activity.LONG_JUMP, Activity.IDLE));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\goat\GoatAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */