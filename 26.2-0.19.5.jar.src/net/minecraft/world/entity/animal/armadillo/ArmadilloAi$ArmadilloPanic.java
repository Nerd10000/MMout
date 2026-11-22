/*     */ package net.minecraft.world.entity.animal.armadillo;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ 
/*     */ public class ArmadilloPanic extends AnimalPanic<Armadillo> {
/*     */   public ArmadilloPanic(float speedMultiplier) {
/* 190 */     super(speedMultiplier, mob -> DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES);
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, Armadillo armadillo, long timestamp) {
/* 195 */     armadillo.rollOut();
/* 196 */     super.start(level, (PathfinderMob)armadillo, timestamp);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\armadillo\ArmadilloAi$ArmadilloPanic.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */