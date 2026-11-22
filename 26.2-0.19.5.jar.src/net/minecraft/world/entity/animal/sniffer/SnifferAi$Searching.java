/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ 
/*     */ class Searching extends Behavior<Sniffer> {
/*     */   private Searching() {
/* 191 */     super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_PRESENT), 600);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) {
/* 202 */     return sniffer.canSniff();
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 207 */     if (!sniffer.canSniff()) {
/* 208 */       sniffer.transitionTo(Sniffer.State.IDLING);
/* 209 */       return false;
/*     */     } 
/* 215 */     Optional<BlockPos> walkTarget = sniffer.getBrain().getMemory(MemoryModuleType.WALK_TARGET).map(WalkTarget::getTarget).map(PositionTracker::currentBlockPosition);
/* 217 */     Optional<BlockPos> sniffingTarget = sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
/* 219 */     if (walkTarget.isEmpty() || sniffingTarget.isEmpty())
/* 220 */       return false; 
/* 223 */     return ((BlockPos)sniffingTarget.get()).equals(walkTarget.get());
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 228 */     sniffer.transitionTo(Sniffer.State.SEARCHING);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 234 */     if (sniffer.canDig() && sniffer.canSniff())
/* 235 */       sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_DIGGING, Boolean.valueOf(true)); 
/* 239 */     sniffer.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 240 */     sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$Searching.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */