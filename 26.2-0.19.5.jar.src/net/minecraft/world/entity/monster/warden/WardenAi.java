/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*     */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToTargetLocation;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.Digging;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.Emerging;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.ForceUnmount;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.Roar;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.SetRoarTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.SetWardenLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.Sniffing;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.TryToSniff;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ public class WardenAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.5F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_INVESTIGATING = 0.7F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FIGHTING = 1.2F;
/*     */   
/*     */   private static final int MELEE_ATTACK_COOLDOWN = 18;
/*     */   
/*  57 */   private static final int DIGGING_DURATION = Mth.ceil(100.0F);
/*     */   
/*  58 */   public static final int EMERGE_DURATION = Mth.ceil(133.59999F);
/*     */   
/*  59 */   public static final int ROAR_DURATION = Mth.ceil(84.0F);
/*     */   
/*  60 */   private static final int SNIFFING_DURATION = Mth.ceil(83.2F);
/*     */   
/*     */   public static final int DIGGING_COOLDOWN = 1200;
/*     */   
/*     */   private static final int DISTURBANCE_LOCATION_EXPIRY_TIME = 100;
/*     */   
/*     */   private static final BehaviorControl<Warden> DIG_COOLDOWN_SETTER;
/*     */   
/*     */   static {
/*  66 */     DIG_COOLDOWN_SETTER = (BehaviorControl<Warden>)BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.DIG_COOLDOWN)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   protected static List<ActivityData<Warden>> getActivities(Warden body) {
/*  76 */     return List.of(
/*  77 */         initCoreActivity(), 
/*  78 */         initEmergeActivity(), 
/*  79 */         initDiggingActivity(), 
/*  80 */         initIdleActivity(), 
/*  81 */         initRoarActivity(), 
/*  82 */         initFightActivity(body), 
/*  83 */         initInvestigateActivity(), 
/*  84 */         initSniffingActivity());
/*     */   }
/*     */   
/*     */   public static void updateActivity(Brain<Warden> brain) {
/*  89 */     brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.EMERGE, Activity.DIG, Activity.ROAR, Activity.FIGHT, Activity.INVESTIGATE, Activity.SNIFF, Activity.IDLE));
/*     */   }
/*     */   
/*     */   private static ActivityData<Warden> initCoreActivity() {
/* 101 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), 
/*     */           
/* 103 */           SetWardenLookTarget.create(), new LookAtTargetSink(45, 90), new MoveToTargetSink()));
/*     */   }
/*     */   
/*     */   private static ActivityData<Warden> initEmergeActivity() {
/* 110 */     return ActivityData.create(Activity.EMERGE, 5, ImmutableList.of(new Emerging(EMERGE_DURATION)), MemoryModuleType.IS_EMERGING);
/*     */   }
/*     */   
/*     */   private static ActivityData<Warden> initDiggingActivity() {
/* 116 */     return ActivityData.create(Activity.DIG, ImmutableList.of(
/* 117 */           Pair.of(Integer.valueOf(0), new ForceUnmount()), 
/* 118 */           Pair.of(Integer.valueOf(1), new Digging(DIGGING_DURATION))), 
/* 119 */         (Set)ImmutableSet.of(
/* 120 */           Pair.of(MemoryModuleType.ROAR_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 121 */           Pair.of(MemoryModuleType.DIG_COOLDOWN, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */   
/*     */   private static ActivityData<Warden> initIdleActivity() {
/* 126 */     return ActivityData.create(Activity.IDLE, 10, ImmutableList.of(
/* 127 */           SetRoarTarget.create(Warden::getEntityAngryAt), 
/* 128 */           TryToSniff.create(), new RunOne(
/* 129 */             (Map)ImmutableMap.of(MemoryModuleType.IS_SNIFFING, MemoryStatus.VALUE_ABSENT), 
/*     */             
/* 131 */             (List)ImmutableList.of(
/* 132 */               Pair.of(RandomStroll.stroll(0.5F), Integer.valueOf(2)), 
/* 133 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))));
/*     */   }
/*     */   
/*     */   private static ActivityData<Warden> initInvestigateActivity() {
/* 139 */     return ActivityData.create(Activity.INVESTIGATE, 5, ImmutableList.of(
/* 140 */           SetRoarTarget.create(Warden::getEntityAngryAt), 
/* 141 */           GoToTargetLocation.create(MemoryModuleType.DISTURBANCE_LOCATION, 2, 0.7F)), MemoryModuleType.DISTURBANCE_LOCATION);
/*     */   }
/*     */   
/*     */   private static ActivityData<Warden> initSniffingActivity() {
/* 146 */     return ActivityData.create(Activity.SNIFF, 5, ImmutableList.of(
/* 147 */           SetRoarTarget.create(Warden::getEntityAngryAt), new Sniffing(SNIFFING_DURATION)), MemoryModuleType.IS_SNIFFING);
/*     */   }
/*     */   
/*     */   private static ActivityData<Warden> initRoarActivity() {
/* 153 */     return ActivityData.create(Activity.ROAR, 10, ImmutableList.of(new Roar()), MemoryModuleType.ROAR_TARGET);
/*     */   }
/*     */   
/*     */   private static ActivityData<Warden> initFightActivity(Warden body) {
/* 159 */     return ActivityData.create(Activity.FIGHT, 10, ImmutableList.of(DIG_COOLDOWN_SETTER, 
/*     */           
/* 161 */           StopAttackingIfTargetInvalid.create((level, target) -> (!body.getAngerLevel().isAngry() || !body.canTargetEntity((Entity)target)), WardenAi::onTargetInvalid, false), 
/* 162 */           SetEntityLookTarget.create(entity -> isTarget(body, entity), (float)body.getAttributeValue(Attributes.FOLLOW_RANGE)), 
/* 163 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.2F), new SonicBoom(), 
/*     */           
/* 165 */           MeleeAttack.create(18)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */   
/*     */   private static boolean isTarget(Warden body, LivingEntity living) {
/* 170 */     return body.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(e -> (e == living)).isPresent();
/*     */   }
/*     */   
/*     */   private static void onTargetInvalid(ServerLevel level, Warden body, LivingEntity attackTarget) {
/* 174 */     if (!body.canTargetEntity((Entity)attackTarget))
/* 175 */       body.clearAnger((Entity)attackTarget); 
/* 179 */     setDigCooldown((LivingEntity)body);
/*     */   }
/*     */   
/*     */   public static void setDigCooldown(LivingEntity body) {
/* 183 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.DIG_COOLDOWN))
/* 184 */       body.getBrain().setMemoryWithExpiry(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 1200L); 
/*     */   }
/*     */   
/*     */   public static void setDisturbanceLocation(Warden body, BlockPos position) {
/* 189 */     if (!body.level().getWorldBorder().isWithinBounds(position) || body
/* 190 */       .getEntityAngryAt().isPresent() || body
/* 191 */       .getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent())
/*     */       return; 
/* 195 */     setDigCooldown((LivingEntity)body);
/* 196 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, 100L);
/* 197 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(position), 100L);
/* 198 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.DISTURBANCE_LOCATION, position, 100L);
/* 199 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\warden\WardenAi.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */