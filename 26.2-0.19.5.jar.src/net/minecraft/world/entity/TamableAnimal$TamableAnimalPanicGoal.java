/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ 
/*     */ public class TamableAnimalPanicGoal extends PanicGoal {
/*     */   public TamableAnimalPanicGoal(double speedModifier, TagKey<DamageType> panicCausingDamageTypes) {
/* 300 */     super((PathfinderMob)TamableAnimal.this, speedModifier, panicCausingDamageTypes);
/*     */   }
/*     */   
/*     */   public TamableAnimalPanicGoal(double speedModifier) {
/* 304 */     super((PathfinderMob)TamableAnimal.this, speedModifier);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 309 */     if (!TamableAnimal.this.unableToMoveToOwner() && TamableAnimal.this.shouldTryTeleportToOwner())
/* 310 */       TamableAnimal.this.tryToTeleportToOwner(); 
/* 312 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\TamableAnimal$TamableAnimalPanicGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */