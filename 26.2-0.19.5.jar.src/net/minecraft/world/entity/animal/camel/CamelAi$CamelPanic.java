/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ 
/*     */ public class CamelPanic extends AnimalPanic<Camel> {
/*     */   public CamelPanic(float speedMultiplier) {
/*  95 */     super(speedMultiplier);
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, Camel body) {
/* 100 */     return (super.checkExtraStartConditions(level, (PathfinderMob)body) && !body.isMobControlled());
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Camel camel, long timestamp) {
/* 105 */     camel.standUpInstantly();
/* 106 */     super.start(level, (PathfinderMob)camel, timestamp);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\camel\CamelAi$CamelPanic.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */