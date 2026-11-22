/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class CountDownCooldownTicks extends Behavior<LivingEntity> {
/*    */   private final MemoryModuleType<Integer> cooldownTicks;
/*    */   
/*    */   public CountDownCooldownTicks(MemoryModuleType<Integer> cooldownTicks) {
/* 16 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(cooldownTicks, MemoryStatus.VALUE_PRESENT));
/* 19 */     this.cooldownTicks = cooldownTicks;
/*    */   }
/*    */   
/*    */   private Optional<Integer> getCooldownTickMemory(LivingEntity body) {
/* 23 */     return body.getBrain().getMemory(this.cooldownTicks);
/*    */   }
/*    */   
/*    */   protected boolean timedOut(long timestamp) {
/* 28 */     return false;
/*    */   }
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, LivingEntity body, long timestamp) {
/* 33 */     Optional<Integer> calmDownTicks = getCooldownTickMemory(body);
/* 34 */     return (calmDownTicks.isPresent() && ((Integer)calmDownTicks.get()).intValue() > 0);
/*    */   }
/*    */   
/*    */   protected void tick(ServerLevel level, LivingEntity body, long timestamp) {
/* 39 */     Optional<Integer> calmDownTicks = getCooldownTickMemory(body);
/* 40 */     body.getBrain().setMemory(this.cooldownTicks, Integer.valueOf(((Integer)calmDownTicks.get()).intValue() - 1));
/*    */   }
/*    */   
/*    */   protected void stop(ServerLevel level, LivingEntity body, long timestamp) {
/* 45 */     body.getBrain().eraseMemory(this.cooldownTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\CountDownCooldownTicks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */