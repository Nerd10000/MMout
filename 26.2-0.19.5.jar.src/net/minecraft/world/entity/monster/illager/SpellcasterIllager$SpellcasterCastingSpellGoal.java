/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ public class SpellcasterCastingSpellGoal extends Goal {
/*     */   public SpellcasterCastingSpellGoal() {
/* 124 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 129 */     return (SpellcasterIllager.this.getSpellCastingTime() > 0);
/*     */   }
/*     */   
/*     */   public void start() {
/* 134 */     super.start();
/* 135 */     SpellcasterIllager.access$000(SpellcasterIllager.this).stop();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 140 */     super.stop();
/* 141 */     SpellcasterIllager.this.setIsCastingSpell(SpellcasterIllager.IllagerSpell.NONE);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 146 */     if (SpellcasterIllager.this.getTarget() != null)
/* 147 */       SpellcasterIllager.this.getLookControl().setLookAt((Entity)SpellcasterIllager.this.getTarget(), SpellcasterIllager.this.getMaxHeadYRot(), SpellcasterIllager.this.getMaxHeadXRot()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\SpellcasterIllager$SpellcasterCastingSpellGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */