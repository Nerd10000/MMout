/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ class null implements BehaviorBuilder.TriggerWithResult<E, MemoryAccessor<F, Value>> {
/*     */   public MemoryAccessor<F, Value> tryTrigger(ServerLevel level, E body, long timestamp) {
/* 151 */     Brain<?> brain = body.getBrain();
/* 152 */     Optional<Value> value = brain.getMemoryInternal(condition.memory());
/* 153 */     if (value == null)
/* 154 */       return null; 
/* 156 */     return condition.createAccessor(brain, value);
/*     */   }
/*     */   
/*     */   public Set<MemoryModuleType<?>> memories() {
/* 161 */     return Set.of(condition.memory());
/*     */   }
/*     */   
/*     */   public String debugString() {
/* 166 */     return "M[" + String.valueOf(condition) + "]";
/*     */   }
/*     */   
/*     */   public String toString() {
/* 171 */     return debugString();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$PureMemory$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */