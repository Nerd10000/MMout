/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CamelHusk extends Camel {
/*     */   public CamelHusk(EntityType<? extends Camel> type, Level level) {
/*  26 */     super(type, level);
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/*  31 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isMobControlled() {
/*  36 */     return getFirstPassenger() instanceof net.minecraft.world.entity.Mob;
/*     */   }
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/*  41 */     setPersistenceRequired();
/*  42 */     return super.interact(player, hand, location);
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/*  47 */     return !isMobControlled();
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/*  52 */     return itemStack.is(ItemTags.CAMEL_HUSK_FOOD);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  57 */     return SoundEvents.CAMEL_HUSK_AMBIENT;
/*     */   }
/*     */   
/*     */   public boolean canMate(Animal partner) {
/*  62 */     return false;
/*     */   }
/*     */   
/*     */   public Camel getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  67 */     return null;
/*     */   }
/*     */   
/*     */   public boolean canFallInLove() {
/*  72 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canBeABaby() {
/*  77 */     return false;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  82 */     return SoundEvents.CAMEL_HUSK_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  87 */     return SoundEvents.CAMEL_HUSK_HURT;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/*  92 */     if (blockState.is(BlockTags.CAMEL_SAND_STEP_SOUND_BLOCKS)) {
/*  93 */       playSound(SoundEvents.CAMEL_HUSK_STEP_SAND, 0.4F, 1.0F);
/*     */     } else {
/*  95 */       playSound(SoundEvents.CAMEL_HUSK_STEP, 0.4F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashingSound() {
/* 101 */     return SoundEvents.CAMEL_HUSK_DASH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashReadySound() {
/* 106 */     return SoundEvents.CAMEL_HUSK_DASH_READY;
/*     */   }
/*     */   
/*     */   protected SoundEvent getEatingSound() {
/* 111 */     return SoundEvents.CAMEL_HUSK_EAT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getStandUpSound() {
/* 116 */     return SoundEvents.CAMEL_HUSK_STAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSitDownSound() {
/* 121 */     return SoundEvents.CAMEL_HUSK_SIT;
/*     */   }
/*     */   
/*     */   protected Holder.Reference<SoundEvent> getSaddleSound() {
/* 126 */     return SoundEvents.CAMEL_HUSK_SADDLE;
/*     */   }
/*     */   
/*     */   public float chargeSpeedModifier() {
/* 131 */     return 4.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\camel\CamelHusk.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */