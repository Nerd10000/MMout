/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class SkeletonHorse extends AbstractHorse {
/*  33 */   private final SkeletonTrapGoal skeletonTrapGoal = new SkeletonTrapGoal(this);
/*     */   
/*     */   private static final int TRAP_MAX_LIFE = 18000;
/*     */   
/*     */   private static final boolean DEFAULT_IS_TRAP = false;
/*     */   
/*     */   private static final int DEFAULT_TRAP_TIME = 0;
/*     */   
/*  38 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.SKELETON_HORSE.getDimensions()
/*  39 */     .withAttachments(EntityAttachments.builder()
/*  40 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityTypes.SKELETON_HORSE.getHeight() - 0.25F, 0.0F))
/*     */     
/*  42 */     .scale(0.7F);
/*     */   
/*     */   private boolean isTrap = false;
/*     */   
/*  45 */   private int trapTime = 0;
/*     */   
/*     */   public SkeletonHorse(EntityType<? extends SkeletonHorse> type, Level level) {
/*  48 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  52 */     return createBaseHorseAttributes()
/*  53 */       .add(Attributes.MAX_HEALTH, 15.0D)
/*  54 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */   
/*     */   public static boolean checkSkeletonHorseSpawnRules(EntityType<? extends Animal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  58 */     if (EntitySpawnReason.isSpawner(spawnReason))
/*  59 */       return (EntitySpawnReason.ignoresLightRequirements(spawnReason) || isBrightEnoughToSpawn((BlockAndLightGetter)level, pos)); 
/*  61 */     return Animal.checkAnimalSpawnRules(type, level, spawnReason, pos, random);
/*     */   }
/*     */   
/*     */   protected void randomizeAttributes(RandomSource random) {
/*  66 */     Objects.requireNonNull(random);
/*  66 */     getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateJumpStrength(random::nextDouble));
/*     */   }
/*     */   
/*     */   protected void addBehaviourGoals() {}
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  76 */     if (isEyeInFluid(FluidTags.WATER))
/*  77 */       return SoundEvents.SKELETON_HORSE_AMBIENT_WATER; 
/*  79 */     return SoundEvents.SKELETON_HORSE_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  85 */     return SoundEvents.SKELETON_HORSE_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  90 */     return SoundEvents.SKELETON_HORSE_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/*  95 */     if (onGround())
/*  96 */       if (isVehicle()) {
/*  97 */         this.gallopSoundCounter++;
/*  98 */         if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0)
/*  99 */           return SoundEvents.SKELETON_HORSE_GALLOP_WATER; 
/* 100 */         if (this.gallopSoundCounter <= 5)
/* 101 */           return SoundEvents.SKELETON_HORSE_STEP_WATER; 
/*     */       } else {
/* 104 */         return SoundEvents.SKELETON_HORSE_STEP_WATER;
/*     */       }  
/* 107 */     return SoundEvents.SKELETON_HORSE_SWIM;
/*     */   }
/*     */   
/*     */   protected void playSwimSound(float volume) {
/* 112 */     if (onGround()) {
/* 113 */       super.playSwimSound(0.3F);
/*     */     } else {
/* 115 */       super.playSwimSound(Math.min(0.1F, volume * 25.0F));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void playJumpSound() {
/* 121 */     if (isInWater()) {
/* 122 */       playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
/*     */     } else {
/* 124 */       super.playJumpSound();
/*     */     } 
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 130 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 135 */     super.aiStep();
/* 137 */     if (!isPersistenceRequired() && isTrap() && this.trapTime++ >= 18000)
/* 138 */       discard(); 
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 144 */     super.addAdditionalSaveData(output);
/* 146 */     output.putBoolean("SkeletonTrap", isTrap());
/* 147 */     output.putInt("SkeletonTrapTime", this.trapTime);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 152 */     super.readAdditionalSaveData(input);
/* 154 */     setTrap(input.getBooleanOr("SkeletonTrap", false));
/* 155 */     this.trapTime = input.getIntOr("SkeletonTrapTime", 0);
/*     */   }
/*     */   
/*     */   protected float getWaterSlowDown() {
/* 160 */     return 0.96F;
/*     */   }
/*     */   
/*     */   public boolean isTrap() {
/* 164 */     return this.isTrap;
/*     */   }
/*     */   
/*     */   public void setTrap(boolean trap) {
/* 168 */     if (trap == this.isTrap)
/*     */       return; 
/* 172 */     this.isTrap = trap;
/* 173 */     if (trap) {
/* 174 */       this.goalSelector.addGoal(1, this.skeletonTrapGoal);
/*     */     } else {
/* 176 */       this.goalSelector.removeGoal(this.skeletonTrapGoal);
/*     */     } 
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 182 */     return (AgeableMob)EntityTypes.SKELETON_HORSE.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 187 */     if (!isTamed())
/* 188 */       return (InteractionResult)InteractionResult.PASS; 
/* 190 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 195 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canAgeUp() {
/* 200 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\SkeletonHorse.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */