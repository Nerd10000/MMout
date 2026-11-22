/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ class null implements BehaviorBuilder.TriggerWithResult<E, Unit> {
/*     */   public Unit tryTrigger(ServerLevel level, E body, long timestamp) {
/* 212 */     return dependentTrigger.trigger(level, body, timestamp) ? Unit.INSTANCE : null;
/*     */   }
/*     */   
/*     */   public Set<MemoryModuleType<?>> memories() {
/* 217 */     return Set.of();
/*     */   }
/*     */   
/*     */   public String debugString() {
/* 222 */     return "T[" + String.valueOf(dependentTrigger) + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$TriggerWrapper$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */