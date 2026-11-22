/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ 
/*     */ class Sniffing extends Behavior<Sniffer> {
/*     */   private Sniffing(int min, int max) {
/* 152 */     super(Map.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFFER_SNIFFING_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SNIFF_COOLDOWN, MemoryStatus.VALUE_ABSENT), min, max);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Sniffer body) {
/* 162 */     return (!body.isBaby() && body.canSniff());
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Sniffer body, long timestamp) {
/* 167 */     return body.canSniff();
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 172 */     sniffer.transitionTo(Sniffer.State.SNIFFING);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 177 */     boolean finished = timedOut(timestamp);
/* 178 */     sniffer.transitionTo(Sniffer.State.IDLING);
/* 180 */     if (finished)
/* 181 */       sniffer.calculateDigPosition().ifPresent(position -> {
/*     */             sniffer.getBrain().setMemory(MemoryModuleType.SNIFFER_SNIFFING_TARGET, position);
/*     */             sniffer.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(position, 1.25F, 0));
/*     */           }); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$Sniffing.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */