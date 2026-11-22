/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MoveToTargetSink extends Behavior<Mob> {
/*     */   private static final int MAX_COOLDOWN_BEFORE_RETRYING = 40;
/*     */   
/*     */   private int remainingCooldown;
/*     */   
/*     */   private Path path;
/*     */   
/*     */   private BlockPos lastTargetPos;
/*     */   
/*     */   private float speedModifier;
/*     */   
/*     */   public MoveToTargetSink() {
/*  36 */     this(150, 250);
/*     */   }
/*     */   
/*     */   public MoveToTargetSink(int minTimeout, int maxTimeout) {
/*  40 */     super(
/*  41 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED, MemoryModuleType.PATH, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT), minTimeout, maxTimeout);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Mob body) {
/*  52 */     if (this.remainingCooldown > 0) {
/*  53 */       this.remainingCooldown--;
/*  54 */       return false;
/*     */     } 
/*  57 */     Brain<?> brain = body.getBrain();
/*  58 */     WalkTarget walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET).get();
/*  60 */     boolean reachedTarget = reachedTarget(body, walkTarget);
/*  61 */     if (!reachedTarget && tryComputePath(body, walkTarget, level.getGameTime())) {
/*  62 */       this.lastTargetPos = walkTarget.getTarget().currentBlockPosition();
/*  63 */       return true;
/*     */     } 
/*  68 */     brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/*  69 */     if (reachedTarget)
/*  70 */       brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE); 
/*  72 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) {
/*  77 */     if (this.path == null || this.lastTargetPos == null)
/*  78 */       return false; 
/*  81 */     Optional<WalkTarget> walkTarget = body.getBrain().getMemory(MemoryModuleType.WALK_TARGET);
/*  82 */     boolean isSpectator = ((Boolean)walkTarget.<Boolean>map(MoveToTargetSink::isWalkTargetSpectator).orElse(Boolean.valueOf(false))).booleanValue();
/*  84 */     PathNavigation navigation = body.getNavigation();
/*  85 */     return (!navigation.isDone() && walkTarget.isPresent() && !reachedTarget(body, walkTarget.get()) && !isSpectator);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Mob body, long timestamp) {
/*  90 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET) && !reachedTarget(body, body.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get()) && body.getNavigation().isStuck())
/*  92 */       this.remainingCooldown = level.getRandom().nextInt(40); 
/*  95 */     body.getNavigation().stop();
/*  96 */     body.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*  97 */     body.getBrain().eraseMemory(MemoryModuleType.PATH);
/*  98 */     this.path = null;
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Mob body, long timestamp) {
/* 103 */     body.getBrain().setMemory(MemoryModuleType.PATH, this.path);
/* 104 */     body.getNavigation().moveTo(this.path, this.speedModifier);
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, Mob body, long timestamp) {
/* 109 */     Path newPath = body.getNavigation().getPath();
/* 110 */     Brain<?> brain = body.getBrain();
/* 111 */     if (this.path != newPath) {
/* 112 */       this.path = newPath;
/* 113 */       brain.setMemory(MemoryModuleType.PATH, newPath);
/*     */     } 
/* 116 */     if (newPath == null || this.lastTargetPos == null)
/*     */       return; 
/* 120 */     WalkTarget walkTarget = brain.getMemory(MemoryModuleType.WALK_TARGET).get();
/* 122 */     if (walkTarget.getTarget().currentBlockPosition().distSqr((Vec3i)this.lastTargetPos) > 4.0D && 
/* 123 */       tryComputePath(body, walkTarget, level.getGameTime())) {
/* 124 */       this.lastTargetPos = walkTarget.getTarget().currentBlockPosition();
/* 125 */       start(level, body, timestamp);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean tryComputePath(Mob body, WalkTarget walkTarget, long timestamp) {
/* 132 */     BlockPos targetPos = walkTarget.getTarget().currentBlockPosition();
/* 133 */     this.path = body.getNavigation().createPath(targetPos, 0);
/* 134 */     this.speedModifier = walkTarget.getSpeedModifier();
/* 136 */     Brain<?> brain = body.getBrain();
/* 137 */     if (reachedTarget(body, walkTarget)) {
/* 138 */       brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/*     */     } else {
/* 140 */       boolean canReach = (this.path != null && this.path.canReach());
/* 141 */       if (canReach) {
/* 142 */         brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 143 */       } else if (!brain.hasMemoryValue(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)) {
/* 144 */         brain.setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, Long.valueOf(timestamp));
/*     */       } 
/* 147 */       if (this.path != null)
/* 148 */         return true; 
/* 151 */       Vec3 partialStep = DefaultRandomPos.getPosTowards((PathfinderMob)body, 10, 7, Vec3.atBottomCenterOf((Vec3i)targetPos), 1.5707963705062866D);
/* 152 */       if (partialStep != null) {
/* 153 */         this.path = body.getNavigation().createPath(partialStep.x, partialStep.y, partialStep.z, 0);
/* 154 */         return (this.path != null);
/*     */       } 
/*     */     } 
/* 157 */     return false;
/*     */   }
/*     */   
/*     */   private boolean reachedTarget(Mob body, WalkTarget walkTarget) {
/* 161 */     return (walkTarget.getTarget().currentBlockPosition().distManhattan((Vec3i)body.blockPosition()) <= walkTarget.getCloseEnoughDist());
/*     */   }
/*     */   
/*     */   private static boolean isWalkTargetSpectator(WalkTarget walkTarget) {
/* 165 */     PositionTracker target = walkTarget.getTarget();
/* 167 */     if (target instanceof EntityTracker) {
/* 167 */       EntityTracker entityTracker = (EntityTracker)target;
/* 168 */       return entityTracker.getEntity().isSpectator();
/*     */     } 
/* 170 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\MoveToTargetSink.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */