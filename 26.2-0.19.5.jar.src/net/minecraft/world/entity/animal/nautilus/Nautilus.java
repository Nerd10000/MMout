/*     */ package net.minecraft.world.entity.animal.nautilus;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class Nautilus extends AbstractNautilus {
/*     */   private static final int NAUTILUS_TOTAL_AIR_SUPPLY = 300;
/*     */   
/*  28 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.NAUTILUS.getDimensions()
/*  29 */     .scale(0.5F)
/*  30 */     .withAttachments(EntityAttachments.builder()
/*  31 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.5F, 0.0F));
/*     */   
/*  33 */   private static final Brain.Provider<Nautilus> BRAIN_PROVIDER = Brain.provider(
/*  34 */       List.of(MemoryModuleType.ANGRY_AT, MemoryModuleType.ATTACK_TARGET_COOLDOWN), 
/*     */       
/*  38 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NAUTILUS_TEMPTATIONS), paramNautilus -> NautilusAi.getActivities());
/*     */   
/*     */   public Nautilus(EntityType<? extends Nautilus> type, Level level) {
/*  49 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected Brain<Nautilus> makeBrain(Brain.Packed packedBrain) {
/*  54 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Nautilus> getBrain() {
/*  60 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   public Nautilus getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  65 */     Nautilus baby = (Nautilus)EntityTypes.NAUTILUS.create((Level)level, EntitySpawnReason.BREEDING);
/*  66 */     if (baby != null && 
/*  67 */       isTame()) {
/*  68 */       baby.setOwnerReference(getOwnerReference());
/*  69 */       baby.setTame(true, true);
/*     */     } 
/*  72 */     return baby;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/*  77 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/*  82 */     ProfilerFiller profiler = Profiler.get();
/*  83 */     profiler.push("nautilusBrain");
/*  84 */     getBrain().tick(level, (LivingEntity)this);
/*  85 */     profiler.pop();
/*  87 */     profiler.push("nautilusActivityUpdate");
/*  88 */     NautilusAi.updateActivity(this);
/*  89 */     profiler.pop();
/*  90 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  95 */     if (isBaby())
/*  96 */       return isUnderWater() ? SoundEvents.BABY_NAUTILUS_AMBIENT : SoundEvents.BABY_NAUTILUS_AMBIENT_ON_LAND; 
/*  98 */     return isUnderWater() ? SoundEvents.NAUTILUS_AMBIENT : SoundEvents.NAUTILUS_AMBIENT_ON_LAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 103 */     if (isBaby())
/* 104 */       return isUnderWater() ? SoundEvents.BABY_NAUTILUS_HURT : SoundEvents.BABY_NAUTILUS_HURT_ON_LAND; 
/* 106 */     return isUnderWater() ? SoundEvents.NAUTILUS_HURT : SoundEvents.NAUTILUS_HURT_ON_LAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 111 */     if (isBaby())
/* 112 */       return isUnderWater() ? SoundEvents.BABY_NAUTILUS_DEATH : SoundEvents.BABY_NAUTILUS_DEATH_ON_LAND; 
/* 114 */     return isUnderWater() ? SoundEvents.NAUTILUS_DEATH : SoundEvents.NAUTILUS_DEATH_ON_LAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashSound() {
/* 119 */     return isUnderWater() ? SoundEvents.NAUTILUS_DASH : SoundEvents.NAUTILUS_DASH_ON_LAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashReadySound() {
/* 124 */     return isUnderWater() ? SoundEvents.NAUTILUS_DASH_READY : SoundEvents.NAUTILUS_DASH_READY_ON_LAND;
/*     */   }
/*     */   
/*     */   protected void playEatingSound() {
/* 129 */     SoundEvent nautilusEatSound = isBaby() ? SoundEvents.BABY_NAUTILUS_EAT : SoundEvents.NAUTILUS_EAT;
/* 130 */     makeSound(nautilusEatSound);
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 135 */     return isBaby() ? SoundEvents.BABY_NAUTILUS_SWIM : SoundEvents.NAUTILUS_SWIM;
/*     */   }
/*     */   
/*     */   public int getMaxAirSupply() {
/* 140 */     return 300;
/*     */   }
/*     */   
/*     */   protected void handleAirSupply(ServerLevel level, int preTickAirSupply) {
/* 144 */     if (isAlive() && !isInWater()) {
/* 145 */       setAirSupply(preTickAirSupply - 1);
/* 146 */       if (getAirSupply() <= -20) {
/* 147 */         setAirSupply(0);
/* 148 */         hurtServer(level, damageSources().dryOut(), 2.0F);
/*     */       } 
/*     */     } else {
/* 151 */       setAirSupply(300);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void baseTick() {
/* 157 */     int airSupply = getAirSupply();
/* 158 */     super.baseTick();
/* 159 */     if (!isNoAi()) {
/* 159 */       Level level = level();
/* 159 */       if (level instanceof ServerLevel) {
/* 159 */         ServerLevel serverLevel = (ServerLevel)level;
/* 160 */         handleAirSupply(serverLevel, airSupply);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/* 166 */     return !isAggravated();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\nautilus\Nautilus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */