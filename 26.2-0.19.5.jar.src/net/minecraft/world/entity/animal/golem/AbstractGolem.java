/*    */ package net.minecraft.world.entity.animal.golem;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class AbstractGolem extends PathfinderMob {
/*    */   protected AbstractGolem(EntityType<? extends AbstractGolem> type, Level level) {
/* 12 */     super(type, level);
/*    */   }
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 17 */     return null;
/*    */   }
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource source) {
/* 22 */     return null;
/*    */   }
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 27 */     return null;
/*    */   }
/*    */   
/*    */   public int getAmbientSoundInterval() {
/* 32 */     return 120;
/*    */   }
/*    */   
/*    */   public boolean removeWhenFarAway(double distSqr) {
/* 37 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\golem\AbstractGolem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */