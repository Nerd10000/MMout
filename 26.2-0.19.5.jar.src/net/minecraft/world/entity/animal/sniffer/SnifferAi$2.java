/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ 
/*     */ class null extends AnimalMakeLove {
/*     */   null(EntityType<? extends Animal> partnerType) {
/* 111 */     super(partnerType);
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Animal body, long timestamp) {
/* 114 */     SnifferAi.resetSniffing((Sniffer)body);
/* 115 */     super.start(level, body, timestamp);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\SnifferAi$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */