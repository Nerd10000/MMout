/*     */ package net.minecraft.world.entity.monster.breeze;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ public class SlideToTargetSink extends MoveToTargetSink {
/*     */   @VisibleForTesting
/*     */   public SlideToTargetSink(int minTimeout, int maxTimeout) {
/*  97 */     super(minTimeout, maxTimeout);
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Mob body, long timestamp) {
/* 102 */     super.start(level, body, timestamp);
/* 103 */     body.playSound(SoundEvents.BREEZE_SLIDE);
/* 104 */     body.setPose(Pose.SLIDING);
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Mob body, long timestamp) {
/* 109 */     super.stop(level, body, timestamp);
/* 110 */     body.setPose(Pose.STANDING);
/* 113 */     if (body.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
/* 114 */       body.getBrain().setMemoryWithExpiry(MemoryModuleType.BREEZE_SHOOT, Unit.INSTANCE, 60L); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\breeze\BreezeAi$SlideToTargetSink.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */