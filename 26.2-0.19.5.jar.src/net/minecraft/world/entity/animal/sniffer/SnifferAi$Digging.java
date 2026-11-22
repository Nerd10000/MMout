/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ class Digging extends Behavior<Sniffer> {
/*     */   private Digging(int min, int max) {
/* 246 */     super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_PRESENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_ABSENT), min, max);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) {
/* 256 */     return sniffer.canSniff();
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 261 */     return (sniffer.getBrain().getMemory(MemoryModuleType.SNIFFER_DIGGING).isPresent() && sniffer.canDig() && !sniffer.isInLove());
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 266 */     sniffer.transitionTo(Sniffer.State.DIGGING);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 271 */     boolean finished = timedOut(timestamp);
/* 273 */     if (finished) {
/* 274 */       sniffer.getBrain().setMemoryWithExpiry(MemoryModuleType.SNIFF_COOLDOWN, Unit.INSTANCE, 9600L);
/*     */     } else {
/* 276 */       SnifferAi.resetSniffing(sniffer);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$Digging.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */