/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ enum null {
/*     */   public <E extends LivingEntity> void apply(Stream<BehaviorControl<? super E>> behaviors, ServerLevel level, E body, long timestamp) {
/* 138 */     behaviors
/* 139 */       .filter(goal -> (goal.getStatus() == Behavior.Status.STOPPED))
/* 140 */       .forEach(goal -> goal.tryStart(level, body, timestamp));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\GateBehavior$RunningPolicy$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */