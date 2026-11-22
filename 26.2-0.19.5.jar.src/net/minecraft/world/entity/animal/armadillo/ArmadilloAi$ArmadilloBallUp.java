/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ 
/*     */ public class ArmadilloBallUp extends Behavior<Armadillo> {
/* 116 */   private static final int BALL_UP_STAY_IN_STATE = 5 * TimeUtil.SECONDS_PER_MINUTE * 20;
/*     */   
/*     */   private static final int TICKS_DELAY_TO_DETERMINE_IF_DANGER_IS_STILL_AROUND = 5;
/*     */   
/*     */   private static final int DANGER_DETECTED_RECENTLY_DANGER_THRESHOLD = 75;
/*     */   
/* 120 */   private int nextPeekTimer = 0;
/*     */   
/*     */   private boolean dangerWasAround;
/*     */   
/*     */   public ArmadilloBallUp() {
/* 124 */     super(Map.of(), BALL_UP_STAY_IN_STATE);
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, Armadillo body, long timestamp) {
/* 129 */     super.tick(level, (LivingEntity)body, timestamp);
/* 130 */     if (this.nextPeekTimer > 0)
/* 131 */       this.nextPeekTimer--; 
/* 133 */     if (body.shouldSwitchToScaredState()) {
/* 134 */       body.switchToState(Armadillo.ArmadilloState.SCARED);
/* 135 */       if (body.onGround())
/* 136 */         body.playSound(SoundEvents.ARMADILLO_LAND); 
/*     */       return;
/*     */     } 
/* 140 */     Armadillo.ArmadilloState state = body.getState();
/* 141 */     long dangerTickCounter = body.getBrain().getTimeUntilExpiry(MemoryModuleType.DANGER_DETECTED_RECENTLY);
/* 142 */     boolean dangerIsAround = (dangerTickCounter > 75L);
/* 143 */     if (dangerIsAround != this.dangerWasAround)
/* 144 */       this.nextPeekTimer = pickNextPeekTimer(body); 
/* 146 */     this.dangerWasAround = dangerIsAround;
/* 147 */     if (state == Armadillo.ArmadilloState.SCARED) {
/* 148 */       if (this.nextPeekTimer == 0 && body.onGround() && dangerIsAround) {
/* 149 */         level.broadcastEntityEvent((Entity)body, (byte)64);
/* 150 */         this.nextPeekTimer = pickNextPeekTimer(body);
/*     */       } 
/* 154 */       if (dangerTickCounter < Armadillo.ArmadilloState.UNROLLING.animationDuration()) {
/* 155 */         body.playSound(SoundEvents.ARMADILLO_UNROLL_START);
/* 156 */         body.switchToState(Armadillo.ArmadilloState.UNROLLING);
/*     */       } 
/* 158 */     } else if (state == Armadillo.ArmadilloState.UNROLLING && dangerTickCounter > Armadillo.ArmadilloState.UNROLLING.animationDuration()) {
/* 159 */       body.switchToState(Armadillo.ArmadilloState.SCARED);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int pickNextPeekTimer(Armadillo body) {
/* 164 */     return Armadillo.ArmadilloState.SCARED.animationDuration() + body.getRandom().nextIntBetweenInclusive(100, 400);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Armadillo body) {
/* 168 */     return body.onGround();
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, Armadillo body, long timestamp) {
/* 173 */     return body.getState().isThreatened();
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Armadillo body, long timestamp) {
/* 177 */     body.rollUp();
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, Armadillo body, long timestamp) {
/* 182 */     if (!body.canStayRolledUp())
/* 183 */       body.rollOut(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\armadillo\ArmadilloAi$ArmadilloBallUp.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */