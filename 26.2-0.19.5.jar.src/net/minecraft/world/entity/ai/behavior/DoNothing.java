/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class DoNothing implements BehaviorControl<LivingEntity> {
/*    */   private final int minDuration;
/*    */   
/*    */   private final int maxDuration;
/*    */   
/* 16 */   private Behavior.Status status = Behavior.Status.STOPPED;
/*    */   
/*    */   private long endTimestamp;
/*    */   
/*    */   public DoNothing(int minDuration, int maxDuration) {
/* 20 */     this.minDuration = minDuration;
/* 21 */     this.maxDuration = maxDuration;
/*    */   }
/*    */   
/*    */   public Behavior.Status getStatus() {
/* 26 */     return this.status;
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> getRequiredMemories() {
/* 31 */     return Set.of();
/*    */   }
/*    */   
/*    */   public final boolean tryStart(ServerLevel level, LivingEntity body, long timestamp) {
/* 36 */     this.status = Behavior.Status.RUNNING;
/* 37 */     int duration = this.minDuration + level.getRandom().nextInt(this.maxDuration + 1 - this.minDuration);
/* 38 */     this.endTimestamp = timestamp + duration;
/* 39 */     return true;
/*    */   }
/*    */   
/*    */   public final void tickOrStop(ServerLevel level, LivingEntity body, long timestamp) {
/* 44 */     if (timestamp > this.endTimestamp)
/* 45 */       doStop(level, body, timestamp); 
/*    */   }
/*    */   
/*    */   public final void doStop(ServerLevel level, LivingEntity body, long timestamp) {
/* 51 */     this.status = Behavior.Status.STOPPED;
/*    */   }
/*    */   
/*    */   public String debugString() {
/* 56 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\DoNothing.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */