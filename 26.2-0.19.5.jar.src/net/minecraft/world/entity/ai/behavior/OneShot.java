/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ 
/*    */ public abstract class OneShot<E extends LivingEntity> implements BehaviorControl<E>, Trigger<E> {
/*  9 */   private Behavior.Status status = Behavior.Status.STOPPED;
/*    */   
/*    */   public final Behavior.Status getStatus() {
/* 13 */     return this.status;
/*    */   }
/*    */   
/*    */   public final boolean tryStart(ServerLevel level, E body, long timestamp) {
/* 18 */     if (trigger(level, (LivingEntity)body, timestamp)) {
/* 19 */       this.status = Behavior.Status.RUNNING;
/* 20 */       return true;
/*    */     } 
/* 22 */     return false;
/*    */   }
/*    */   
/*    */   public final void tickOrStop(ServerLevel level, E body, long timestamp) {
/* 27 */     doStop(level, body, timestamp);
/*    */   }
/*    */   
/*    */   public final void doStop(ServerLevel level, E body, long timestamp) {
/* 32 */     this.status = Behavior.Status.STOPPED;
/*    */   }
/*    */   
/*    */   public String debugString() {
/* 37 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\OneShot.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */