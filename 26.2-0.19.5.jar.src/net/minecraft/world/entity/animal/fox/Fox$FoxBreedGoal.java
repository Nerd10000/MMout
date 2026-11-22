/*     */ package net.minecraft.world.entity.animal.fox;
/*     */ 
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ class FoxBreedGoal extends BreedGoal {
/*     */   public FoxBreedGoal(Fox paramFox, double speedModifier) {
/* 933 */     super(paramFox, speedModifier);
/*     */   }
/*     */   
/*     */   public void start() {
/* 938 */     ((Fox)this.animal).clearStates();
/* 939 */     ((Fox)this.partner).clearStates();
/* 940 */     super.start();
/*     */   }
/*     */   
/*     */   protected void breed() {
/* 945 */     Fox offspring = (Fox)this.animal.getBreedOffspring(this.level, (AgeableMob)this.partner);
/* 946 */     if (offspring == null)
/*     */       return; 
/* 950 */     ServerPlayer animalLoveCause = this.animal.getLoveCause();
/* 951 */     ServerPlayer partnerLoveCause = this.partner.getLoveCause();
/* 952 */     ServerPlayer loveCause = animalLoveCause;
/* 954 */     if (animalLoveCause != null) {
/* 955 */       offspring.addTrustedEntity((LivingEntity)animalLoveCause);
/*     */     } else {
/* 957 */       loveCause = partnerLoveCause;
/*     */     } 
/* 960 */     if (partnerLoveCause != null && animalLoveCause != partnerLoveCause)
/* 961 */       offspring.addTrustedEntity((LivingEntity)partnerLoveCause); 
/* 964 */     if (loveCause != null) {
/* 965 */       loveCause.awardStat(Stats.ANIMALS_BRED);
/* 966 */       CriteriaTriggers.BRED_ANIMALS.trigger(loveCause, this.animal, this.partner, (AgeableMob)offspring);
/*     */     } 
/* 969 */     this.animal.setAge(6000);
/* 970 */     this.partner.setAge(6000);
/* 971 */     this.animal.resetLove();
/* 972 */     this.partner.resetLove();
/* 973 */     offspring.setAge(-24000);
/* 974 */     offspring.snapTo(this.animal.getX(), this.animal.getY(), this.animal.getZ(), 0.0F, 0.0F);
/* 975 */     this.level.addFreshEntityWithPassengers((Entity)offspring);
/* 977 */     this.level.broadcastEntityEvent((Entity)this.animal, (byte)18);
/* 979 */     if (((Boolean)this.level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/* 980 */       this.level.addFreshEntity((Entity)new ExperienceOrb((Level)this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), this.animal.getRandom().nextInt(7) + 1)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxBreedGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */