/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ class TurtleBreedGoal extends BreedGoal {
/*     */   private final Turtle turtle;
/*     */   
/*     */   public TurtleBreedGoal(Turtle turtle, double speedModifier) {
/* 503 */     super(turtle, speedModifier);
/* 504 */     this.turtle = turtle;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 509 */     return (super.canUse() && !this.turtle.hasEgg());
/*     */   }
/*     */   
/*     */   protected void breed() {
/* 514 */     ServerPlayer loveCause = this.animal.getLoveCause();
/* 515 */     if (loveCause == null && this.partner.getLoveCause() != null)
/* 516 */       loveCause = this.partner.getLoveCause(); 
/* 519 */     if (loveCause != null) {
/* 520 */       loveCause.awardStat(Stats.ANIMALS_BRED);
/* 521 */       CriteriaTriggers.BRED_ANIMALS.trigger(loveCause, this.animal, this.partner, null);
/*     */     } 
/* 524 */     this.turtle.setHasEgg(true);
/* 525 */     this.animal.setAge(6000);
/* 526 */     this.partner.setAge(6000);
/* 527 */     this.animal.resetLove();
/* 528 */     this.partner.resetLove();
/* 530 */     RandomSource random = this.animal.getRandom();
/* 531 */     if (((Boolean)getServerLevel((Level)this.level).getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/* 532 */       this.level.addFreshEntity((Entity)new ExperienceOrb((Level)this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtleBreedGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */