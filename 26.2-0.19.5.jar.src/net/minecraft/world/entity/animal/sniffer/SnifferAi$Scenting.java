/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ class Scenting extends Behavior<Sniffer> {
/*     */   private Scenting(int min, int max) {
/* 346 */     super(Map.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_DIGGING, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_HAPPY, MemoryStatus.VALUE_ABSENT, MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), min, max);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Sniffer sniffer) {
/* 358 */     return !sniffer.isTempted();
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 363 */     return true;
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 368 */     sniffer.transitionTo(Sniffer.State.SCENTING);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 373 */     sniffer.transitionTo(Sniffer.State.IDLING);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$Scenting.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */