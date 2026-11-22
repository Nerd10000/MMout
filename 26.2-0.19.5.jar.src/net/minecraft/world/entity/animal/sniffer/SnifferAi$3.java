/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ 
/*     */ class null extends FollowTemptation {
/*     */   null(Function<LivingEntity, Float> speedModifier, Function<LivingEntity, Double> closeEnoughDistance) {
/* 119 */     super(speedModifier, closeEnoughDistance);
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/* 122 */     SnifferAi.resetSniffing((Sniffer)body);
/* 123 */     super.start(level, body, timestamp);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */