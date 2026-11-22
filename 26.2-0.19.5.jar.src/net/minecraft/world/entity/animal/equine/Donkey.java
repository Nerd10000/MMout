/*    */ package net.minecraft.world.entity.animal.equine;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.AgeableMob;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.animal.Animal;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Donkey extends AbstractChestedHorse {
/*    */   public Donkey(EntityType<? extends Donkey> type, Level level) {
/* 17 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 22 */     return SoundEvents.DONKEY_AMBIENT;
/*    */   }
/*    */   
/*    */   protected SoundEvent getAngrySound() {
/* 27 */     return SoundEvents.DONKEY_ANGRY;
/*    */   }
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 32 */     return SoundEvents.DONKEY_DEATH;
/*    */   }
/*    */   
/*    */   protected SoundEvent getEatingSound() {
/* 37 */     return SoundEvents.DONKEY_EAT;
/*    */   }
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource source) {
/* 42 */     return SoundEvents.DONKEY_HURT;
/*    */   }
/*    */   
/*    */   public boolean canMate(Animal partner) {
/* 47 */     if (partner == this)
/* 48 */       return false; 
/* 51 */     if (partner instanceof Donkey || partner instanceof Horse)
/* 52 */       return (canParent() && ((AbstractHorse)partner).canParent()); 
/* 55 */     return false;
/*    */   }
/*    */   
/*    */   protected void playJumpSound() {
/* 60 */     playSound(SoundEvents.DONKEY_JUMP, 0.4F, 1.0F);
/*    */   }
/*    */   
/*    */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 65 */     EntityType<? extends AbstractHorse> babyType = (partner instanceof Horse) ? EntityTypes.MULE : EntityTypes.DONKEY;
/* 66 */     AbstractHorse baby = (AbstractHorse)babyType.create((Level)level, EntitySpawnReason.BREEDING);
/* 68 */     if (baby != null)
/* 69 */       setOffspringAttributes(partner, baby); 
/* 72 */     return (AgeableMob)baby;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\Donkey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */