/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class PlayTagWithOtherKids {
/*     */   private static final int MAX_FLEE_XZ_DIST = 20;
/*     */   
/*     */   private static final int MAX_FLEE_Y_DIST = 8;
/*     */   
/*     */   private static final float FLEE_SPEED_MODIFIER = 0.6F;
/*     */   
/*     */   private static final float CHASE_SPEED_MODIFIER = 0.6F;
/*     */   
/*     */   private static final int MAX_CHASERS_PER_TARGET = 5;
/*     */   
/*     */   private static final int AVERAGE_WAIT_TIME_BETWEEN_RUNS = 10;
/*     */   
/*     */   public static BehaviorControl<PathfinderMob> create() {
/*  32 */     return BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.VISIBLE_VILLAGER_BABIES), (App)i.absent(MemoryModuleType.WALK_TARGET), (App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.registered(MemoryModuleType.INTERACTION_TARGET)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   private static void chaseKid(MemoryAccessor<?, LivingEntity> interactionTarget, MemoryAccessor<?, PositionTracker> lookTarget, MemoryAccessor<?, WalkTarget> walkTarget, LivingEntity kidToChase) {
/*  74 */     interactionTarget.set(kidToChase);
/*  75 */     lookTarget.set(new EntityTracker((Entity)kidToChase, true));
/*  76 */     walkTarget.set(new WalkTarget(new EntityTracker((Entity)kidToChase, false), 0.6F, 1));
/*     */   }
/*     */   
/*     */   private static Optional<LivingEntity> findSomeoneBeingChased(List<LivingEntity> friendsNearby) {
/*  81 */     Map<LivingEntity, Integer> chasedKids = checkHowManyChasersEachFriendHas(friendsNearby);
/*  86 */     return chasedKids.entrySet().stream()
/*  87 */       .sorted(Comparator.comparingInt(Map.Entry::getValue))
/*  88 */       .filter(entry -> (((Integer)entry.getValue()).intValue() > 0 && ((Integer)entry.getValue()).intValue() <= 5))
/*  89 */       .map(Map.Entry::getKey)
/*  90 */       .findFirst();
/*     */   }
/*     */   
/*     */   private static Map<LivingEntity, Integer> checkHowManyChasersEachFriendHas(List<LivingEntity> friendsNearby) {
/*  94 */     Map<LivingEntity, Integer> chasedKids = Maps.newHashMap();
/*  96 */     friendsNearby.stream()
/*  97 */       .filter(PlayTagWithOtherKids::isChasingSomeone)
/*  98 */       .forEach(chaser -> chasedKids.compute(whoAreYouChasing(chaser), ()));
/* 102 */     return chasedKids;
/*     */   }
/*     */   
/*     */   private static LivingEntity whoAreYouChasing(LivingEntity friend) {
/* 106 */     return friend.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*     */   }
/*     */   
/*     */   private static boolean isChasingSomeone(LivingEntity friend) {
/* 110 */     return friend.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent();
/*     */   }
/*     */   
/*     */   private static boolean isFriendChasingMe(LivingEntity me, LivingEntity friend) {
/* 114 */     return friend.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET)
/* 115 */       .filter(mob -> (mob == me))
/* 116 */       .isPresent();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\PlayTagWithOtherKids.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */