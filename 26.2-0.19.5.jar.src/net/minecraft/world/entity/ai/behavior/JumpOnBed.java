/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ 
/*     */ public class JumpOnBed extends Behavior<Mob> {
/*     */   private static final int MAX_TIME_TO_REACH_BED = 100;
/*     */   
/*     */   private static final int MIN_JUMPS = 3;
/*     */   
/*     */   private static final int MAX_JUMPS = 6;
/*     */   
/*     */   private static final int COOLDOWN_BETWEEN_JUMPS = 5;
/*     */   
/*     */   private final float speedModifier;
/*     */   
/*     */   private BlockPos targetBed;
/*     */   
/*     */   private int remainingTimeToReachBed;
/*     */   
/*     */   private int remainingJumps;
/*     */   
/*     */   private int remainingCooldownUntilNextJump;
/*     */   
/*     */   public JumpOnBed(float speedModifier) {
/*  32 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.NEAREST_BED, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*  36 */     this.speedModifier = speedModifier;
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Mob body) {
/*  41 */     return (body.isBaby() && nearBed(level, body));
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Mob body, long timestamp) {
/*  46 */     super.start(level, body, timestamp);
/*  48 */     getNearestBed(body).ifPresent(targetBed -> {
/*     */           this.targetBed = targetBed;
/*     */           this.remainingTimeToReachBed = 100;
/*     */           this.remainingJumps = 3 + level.getRandom().nextInt(4);
/*     */           this.remainingCooldownUntilNextJump = 0;
/*     */           startWalkingTowardsBed(body, targetBed);
/*     */         });
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Mob body, long timestamp) {
/*  59 */     super.stop(level, body, timestamp);
/*  61 */     this.targetBed = null;
/*  62 */     this.remainingTimeToReachBed = 0;
/*  63 */     this.remainingJumps = 0;
/*  64 */     this.remainingCooldownUntilNextJump = 0;
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) {
/*  69 */     return (body.isBaby() && this.targetBed != null && 
/*     */       
/*  71 */       isBed(level, this.targetBed) && 
/*  72 */       !tiredOfWalking(level, body) && 
/*  73 */       !tiredOfJumping(level, body));
/*     */   }
/*     */   
/*     */   protected boolean timedOut(long timestamp) {
/*  78 */     return false;
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, Mob body, long timestamp) {
/*  83 */     if (!onOrOverBed(level, body)) {
/*  84 */       this.remainingTimeToReachBed--;
/*     */       return;
/*     */     } 
/*  88 */     if (this.remainingCooldownUntilNextJump > 0) {
/*  89 */       this.remainingCooldownUntilNextJump--;
/*     */       return;
/*     */     } 
/*  93 */     if (onBedSurface(level, body)) {
/*  94 */       body.getJumpControl().jump();
/*  95 */       this.remainingJumps--;
/*  96 */       this.remainingCooldownUntilNextJump = 5;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void startWalkingTowardsBed(Mob body, BlockPos bedPos) {
/* 101 */     body.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(bedPos, this.speedModifier, 0));
/*     */   }
/*     */   
/*     */   private boolean nearBed(ServerLevel level, Mob body) {
/* 105 */     return (onOrOverBed(level, body) || getNearestBed(body).isPresent());
/*     */   }
/*     */   
/*     */   private boolean onOrOverBed(ServerLevel level, Mob body) {
/* 110 */     BlockPos bodyPos = body.blockPosition();
/* 111 */     BlockPos oneBelow = bodyPos.below();
/* 112 */     return (isBed(level, bodyPos) || isBed(level, oneBelow));
/*     */   }
/*     */   
/*     */   private boolean onBedSurface(ServerLevel level, Mob body) {
/* 116 */     return isBed(level, body.blockPosition());
/*     */   }
/*     */   
/*     */   private boolean isBed(ServerLevel level, BlockPos bodyPos) {
/* 120 */     return level.getBlockState(bodyPos).is(BlockTags.BEDS);
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> getNearestBed(Mob body) {
/* 124 */     return body.getBrain().getMemory(MemoryModuleType.NEAREST_BED);
/*     */   }
/*     */   
/*     */   private boolean tiredOfWalking(ServerLevel level, Mob body) {
/* 128 */     return (!onOrOverBed(level, body) && this.remainingTimeToReachBed <= 0);
/*     */   }
/*     */   
/*     */   private boolean tiredOfJumping(ServerLevel level, Mob body) {
/* 132 */     return (onOrOverBed(level, body) && this.remainingJumps <= 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\JumpOnBed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */