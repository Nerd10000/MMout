/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ 
/*     */ class FeelingHappy extends Behavior<Sniffer> {
/*     */   private FeelingHappy(int min, int max) {
/* 322 */     super(Map.of(MemoryModuleType.SNIFFER_HAPPY, MemoryStatus.VALUE_PRESENT), min, max);
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 329 */     return true;
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 334 */     sniffer.transitionTo(Sniffer.State.FEELING_HAPPY);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Sniffer sniffer, long timestamp) {
/* 339 */     sniffer.transitionTo(Sniffer.State.IDLING);
/* 340 */     sniffer.getBrain().eraseMemory(MemoryModuleType.SNIFFER_HAPPY);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$FeelingHappy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */