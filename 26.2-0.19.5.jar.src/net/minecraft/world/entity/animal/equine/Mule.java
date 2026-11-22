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
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class Mule extends AbstractChestedHorse {
/*    */   public Mule(EntityType<? extends Mule> type, Level level) {
/* 16 */     super((EntityType)type, level);
/*    */   }
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 21 */     return SoundEvents.MULE_AMBIENT;
/*    */   }
/*    */   
/*    */   protected SoundEvent getAngrySound() {
/* 26 */     return SoundEvents.MULE_ANGRY;
/*    */   }
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 31 */     return SoundEvents.MULE_DEATH;
/*    */   }
/*    */   
/*    */   protected SoundEvent getEatingSound() {
/* 36 */     return SoundEvents.MULE_EAT;
/*    */   }
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource source) {
/* 41 */     return SoundEvents.MULE_HURT;
/*    */   }
/*    */   
/*    */   protected void playJumpSound() {
/* 46 */     playSound(SoundEvents.MULE_JUMP, 0.4F, 1.0F);
/*    */   }
/*    */   
/*    */   protected void playChestEquipsSound() {
/* 51 */     playSound(SoundEvents.MULE_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*    */   }
/*    */   
/*    */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 56 */     return (AgeableMob)EntityTypes.MULE.create((Level)level, EntitySpawnReason.BREEDING);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\Mule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */