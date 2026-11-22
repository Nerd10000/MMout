/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ class FinishedDigging extends Behavior<Sniffer> {
/*     */   private FinishedDigging(int duration) {
/* 283 */     super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_PRESENT), duration, duration);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) {
/* 293 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 298 */     return sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_DIGGING).isPresent();
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 303 */     sniffer.transitionTo(Sniffer.State.RISING);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 308 */     boolean finished = timedOut(timestamp);
/* 310 */     sniffer
/* 311 */       .transitionTo(Sniffer.State.IDLING)
/* 312 */       .onDiggingComplete(finished);
/* 315 */     sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_DIGGING);
/* 316 */     sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_HAPPY, Boolean.valueOf(true));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$FinishedDigging.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */