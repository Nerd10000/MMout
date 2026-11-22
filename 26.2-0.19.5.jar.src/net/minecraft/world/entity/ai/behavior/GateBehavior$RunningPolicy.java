/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ public enum RunningPolicy {
/* 126 */   RUN_ONE {
/*     */     public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) {
/* 129 */       behaviors
/* 130 */         .filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED))
/* 131 */         .filter(goal -> goal.tryStart(level, body, timestamp))
/* 132 */         .findFirst();
/*     */     }
/*     */   },
/* 135 */   TRY_ALL {
/*     */     public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) {
/* 138 */       behaviors
/* 139 */         .filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED))
/* 140 */         .forEach(goal -> goal.tryStart(level, body, timestamp));
/*     */     }
/*     */   };
/*     */   
/*     */   public abstract <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> paramStream, ServerLevel paramServerLevel, E paramE, long paramLong);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\GateBehavior$RunningPolicy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */