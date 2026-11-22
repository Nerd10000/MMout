/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ 
/*     */ public class RandomSitting extends Behavior<Camel> {
/*     */   private final int minimalPoseTicks;
/*     */   
/*     */   public RandomSitting(int minimalPoseTimeSec) {
/* 114 */     super((Map)ImmutableMap.of());
/* 115 */     this.minimalPoseTicks = minimalPoseTimeSec * 20;
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Camel body) {
/* 120 */     return (!body.isInWater() && body
/* 121 */       .getPoseTime() >= this.minimalPoseTicks && 
/* 122 */       !body.isLeashed() && body
/* 123 */       .onGround() && 
/* 124 */       !body.hasControllingPassenger() && body
/* 125 */       .canCamelChangePose());
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Camel body, long timestamp) {
/* 130 */     if (body.isCamelSitting()) {
/* 131 */       body.standUp();
/* 132 */     } else if (!body.isPanicking()) {
/* 133 */       body.sitDown();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\camel\CamelAi$RandomSitting.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */