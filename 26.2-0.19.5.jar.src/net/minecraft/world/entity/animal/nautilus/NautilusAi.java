/*     */ package net.minecraft.world.entity.animal.nautilus;
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
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.ChargeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class NautilusAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.3F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.4F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 1.6F;
/*     */   
/*  48 */   private static final UniformInt TIME_BETWEEN_NON_PLAYER_ATTACKS = UniformInt.of(2400, 3600);
/*     */   
/*     */   private static final float SPEED_WHEN_ATTACKING = 0.6F;
/*     */   
/*     */   private static final float ATTACK_KNOCKBACK_FORCE = 2.0F;
/*     */   
/*     */   private static final int ANGER_DURATION = 400;
/*     */   
/*     */   private static final int TIME_BETWEEN_ATTACKS = 80;
/*     */   
/*     */   private static final double MAX_CHARGE_DISTANCE = 12.0D;
/*     */   
/*     */   private static final double MAX_TARGET_DETECTION_DISTANCE = 11.0D;
/*     */   
/*     */   protected static final TargetingConditions ATTACK_TARGET_CONDITIONS;
/*     */   
/*     */   static {
/*  56 */     ATTACK_TARGET_CONDITIONS = TargetingConditions.forCombat().selector((target, level) -> 
/*  57 */         ((((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() || !target.is(EntityTypes.ARMOR_STAND)) && level.getWorldBorder().isWithinBounds(target.getBoundingBox())));
/*     */   }
/*     */   
/*     */   protected static void initMemories(AbstractNautilus body, RandomSource random) {
/*  61 */     body.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET_COOLDOWN, Integer.valueOf(TIME_BETWEEN_NON_PLAYER_ATTACKS.sample(random)));
/*     */   }
/*     */   
/*     */   public static List<ActivityData<Nautilus>> getActivities() {
/*  65 */     return List.of(
/*  66 */         initCoreActivity(), 
/*  67 */         initIdleActivity(), 
/*  68 */         initFightActivity());
/*     */   }
/*     */   
/*     */   private static ActivityData<Nautilus> initCoreActivity() {
/*  73 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(1.6F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.CHARGE_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.ATTACK_TARGET_COOLDOWN)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Nautilus> initIdleActivity() {
/*  84 */     return ActivityData.create(Activity.IDLE, ImmutableList.of(
/*  85 */           Pair.of(Integer.valueOf(1), new AnimalMakeLove(EntityTypes.NAUTILUS, 0.4F, 2)), 
/*  86 */           Pair.of(Integer.valueOf(2), new FollowTemptation(mob -> Float.valueOf(1.3F), mob -> Double.valueOf(mob.isBaby() ? 2.5D : 3.5D))), 
/*  87 */           Pair.of(Integer.valueOf(3), StartAttacking.create(NautilusAi::findNearestValidAttackTarget)), 
/*  88 */           Pair.of(Integer.valueOf(4), new GateBehavior(
/*  89 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */               
/*  92 */               (Set)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*     */               
/*  95 */               (List)ImmutableList.of(
/*  96 */                 Pair.of(RandomStroll.swim(1.0F), Integer.valueOf(2)), 
/*  97 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(3)))))));
/*     */   }
/*     */   
/*     */   private static ActivityData<Nautilus> initFightActivity() {
/* 105 */     return ActivityData.create(Activity.FIGHT, ImmutableList.of(
/* 106 */           Pair.of(Integer.valueOf(0), new ChargeAttack(80, ATTACK_TARGET_CONDITIONS, 0.6F, 2.0F, 12.0D, 11.0D, SoundEvents.NAUTILUS_DASH))), 
/*     */         
/* 114 */         (Set)ImmutableSet.of(
/* 115 */           Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 
/* 116 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 117 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 118 */           Pair.of(MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   protected static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, AbstractNautilus body) {
/* 123 */     if (BehaviorUtils.isBreeding((LivingEntity)body) || !body.isInWater() || body.isBaby() || body.isTame())
/* 124 */       return Optional.empty(); 
/* 128 */     Optional<LivingEntity> angryAt = BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)body, MemoryModuleType.ANGRY_AT).filter(entity -> (entity.isInWater() && Sensor.isEntityAttackableIgnoringLineOfSight(level, (LivingEntity)body, entity)));
/* 129 */     if (angryAt.isPresent())
/* 130 */       return angryAt; 
/* 133 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET_COOLDOWN))
/* 134 */       return Optional.empty(); 
/* 137 */     RandomSource random = level.getRandom();
/* 138 */     body.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET_COOLDOWN, Integer.valueOf(TIME_BETWEEN_NON_PLAYER_ATTACKS.sample(random)));
/* 140 */     if (random.nextFloat() < 0.5F)
/* 141 */       return Optional.empty(); 
/* 146 */     Optional<LivingEntity> target = ((NearestVisibleLivingEntities)body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty())).findClosest(NautilusAi::isHostileTarget);
/* 148 */     return target;
/*     */   }
/*     */   
/*     */   protected static void setAngerTarget(ServerLevel level, AbstractNautilus body, LivingEntity target) {
/* 152 */     if (Sensor.isEntityAttackableIgnoringLineOfSight(level, (LivingEntity)body, target)) {
/* 153 */       body.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 154 */       body.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 400L);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isHostileTarget(LivingEntity mob) {
/* 159 */     return (mob.isInWater() && mob.is(EntityTypeTags.NAUTILUS_HOSTILES));
/*     */   }
/*     */   
/*     */   public static void updateActivity(Nautilus body) {
/* 163 */     body.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */   }
/*     */   
/*     */   public static Predicate<ItemStack> getTemptations() {
/* 167 */     return i -> i.is(ItemTags.NAUTILUS_FOOD);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\nautilus\NautilusAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */