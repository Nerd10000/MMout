/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ public abstract class Behavior<E extends LivingEntity> implements BehaviorControl<E> {
/*     */   public static final int DEFAULT_DURATION = 60;
/*     */   
/*     */   protected final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;
/*     */   
/*  17 */   private Status status = Status.STOPPED;
/*     */   
/*     */   private long endTimestamp;
/*     */   
/*     */   private final int minDuration;
/*     */   
/*     */   private final int maxDuration;
/*     */   
/*     */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
/*  23 */     this(entryCondition, 60);
/*     */   }
/*     */   
/*     */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int timeOutDuration) {
/*  27 */     this(entryCondition, timeOutDuration, timeOutDuration);
/*     */   }
/*     */   
/*     */   public Behavior(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int minDuration, int maxDuration) {
/*  32 */     this.minDuration = minDuration;
/*  33 */     this.maxDuration = maxDuration;
/*  34 */     this.entryCondition = entryCondition;
/*     */   }
/*     */   
/*     */   public Status getStatus() {
/*  39 */     return this.status;
/*     */   }
/*     */   
/*     */   public Set<MemoryModuleType<?>> getRequiredMemories() {
/*  44 */     return this.entryCondition.keySet();
/*     */   }
/*     */   
/*     */   public final boolean tryStart(ServerLevel level, E body, long timestamp) {
/*  49 */     if (hasRequiredMemories(body) && checkExtraStartConditions(level, body)) {
/*  50 */       this.status = Status.RUNNING;
/*  51 */       int duration = this.minDuration + level.getRandom().nextInt(this.maxDuration + 1 - this.minDuration);
/*  52 */       this.endTimestamp = timestamp + duration;
/*  53 */       start(level, body, timestamp);
/*  54 */       return true;
/*     */     } 
/*  56 */     return false;
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, E body, long timestamp) {}
/*     */   
/*     */   public final void tickOrStop(ServerLevel level, E body, long timestamp) {
/*  65 */     if (!timedOut(timestamp) && canStillUse(level, body, timestamp)) {
/*  66 */       tick(level, body, timestamp);
/*     */     } else {
/*  68 */       doStop(level, body, timestamp);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, E body, long timestamp) {}
/*     */   
/*     */   public final void doStop(ServerLevel level, E body, long timestamp) {
/*  78 */     this.status = Status.STOPPED;
/*  79 */     stop(level, body, timestamp);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, E body, long timestamp) {}
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) {
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean timedOut(long timestamp) {
/*  98 */     return (timestamp > this.endTimestamp);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, E body) {
/* 104 */     return true;
/*     */   }
/*     */   
/*     */   public String debugString() {
/* 109 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   protected boolean hasRequiredMemories(E body) {
/* 113 */     for (Map.Entry<MemoryModuleType<?>, MemoryStatus> entry : this.entryCondition.entrySet()) {
/* 114 */       MemoryModuleType<?> memoryType = entry.getKey();
/* 115 */       MemoryStatus requiredStatus = entry.getValue();
/* 116 */       if (!body.getBrain().checkMemory(memoryType, requiredStatus))
/* 117 */         return false; 
/*     */     } 
/* 121 */     return true;
/*     */   }
/*     */   
/*     */   public enum Status {
/* 125 */     STOPPED, RUNNING;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\Behavior.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */