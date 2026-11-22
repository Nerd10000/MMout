/*     */ package net.minecraft.world.entity.ai.behavior.declarative;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ final class Constant<E extends LivingEntity, A> extends BehaviorBuilder<E, A> {
/*     */   private Constant(A a) {
/* 179 */     this(a, () -> "C[" + String.valueOf(a) + "]");
/*     */   }
/*     */   
/*     */   private Constant(A a, Supplier<String> debugString) {
/* 183 */     super(new BehaviorBuilder.TriggerWithResult<E, A>(a, debugString) {
/*     */           public A tryTrigger(ServerLevel level, E body, long timestamp) {
/* 186 */             return (A)a;
/*     */           }
/*     */           
/*     */           public Set<MemoryModuleType<?>> memories() {
/* 191 */             return Set.of();
/*     */           }
/*     */           
/*     */           public String debugString() {
/* 196 */             return debugString.get();
/*     */           }
/*     */           
/*     */           public String toString() {
/* 201 */             return debugString();
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$Constant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */