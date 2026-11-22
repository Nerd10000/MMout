/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class PiglinBrute extends AbstractPiglin {
/*     */   private static final int MAX_HEALTH = 50;
/*     */   
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;
/*     */   
/*     */   private static final int ATTACK_DAMAGE = 7;
/*     */   
/*     */   private static final double TARGETING_RANGE = 12.0D;
/*     */   
/*  40 */   public static final Brain.Provider<PiglinBrute> BRAIN_PROVIDER = Brain.provider(
/*  41 */       List.of(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS), 
/*     */       
/*  44 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR), PiglinBruteAi::getActivities);
/*     */   
/*     */   public PiglinBrute(EntityType<? extends PiglinBrute> type, Level level) {
/*  55 */     super((EntityType)type, level);
/*  56 */     this.xpReward = 20;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  60 */     return Monster.createMonsterAttributes()
/*  61 */       .add(Attributes.MAX_HEALTH, 50.0D)
/*  62 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/*  63 */       .add(Attributes.ATTACK_DAMAGE, 7.0D)
/*  64 */       .add(Attributes.FOLLOW_RANGE, 12.0D);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*  69 */     PiglinBruteAi.initMemories(this);
/*  70 */     populateDefaultEquipmentSlots(level.getRandom(), difficulty);
/*  71 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/*  76 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.GOLDEN_AXE));
/*     */   }
/*     */   
/*     */   protected Brain<PiglinBrute> makeBrain(Brain.Packed packedBrain) {
/*  81 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<PiglinBrute> getBrain() {
/*  87 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   public boolean canHunt() {
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/*  97 */     if (itemStack.is(Items.GOLDEN_AXE))
/*  98 */       return super.wantsToPickUp(level, itemStack); 
/* 100 */     return false;
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 105 */     ProfilerFiller profiler = Profiler.get();
/* 106 */     profiler.push("piglinBruteBrain");
/* 107 */     getBrain().tick(level, (LivingEntity)this);
/* 108 */     profiler.pop();
/* 110 */     PiglinBruteAi.updateActivity(this);
/* 111 */     PiglinBruteAi.maybePlayActivitySound(this);
/* 113 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public PiglinArmPose getArmPose() {
/* 118 */     if (isAggressive() && isHoldingMeleeWeapon())
/* 119 */       return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON; 
/* 121 */     return PiglinArmPose.DEFAULT;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 127 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 128 */     if (wasHurt) {
/* 128 */       Entity entity = source.getEntity();
/* 128 */       if (entity instanceof LivingEntity) {
/* 128 */         LivingEntity sourceEntity = (LivingEntity)entity;
/* 129 */         PiglinBruteAi.wasHurtBy(level, this, sourceEntity);
/*     */       } 
/*     */     } 
/* 131 */     return wasHurt;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 136 */     return SoundEvents.PIGLIN_BRUTE_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 141 */     return SoundEvents.PIGLIN_BRUTE_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 146 */     return SoundEvents.PIGLIN_BRUTE_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 151 */     playSound(SoundEvents.PIGLIN_BRUTE_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playAngrySound() {
/* 155 */     makeSound(SoundEvents.PIGLIN_BRUTE_ANGRY);
/*     */   }
/*     */   
/*     */   protected void playConvertedSound() {
/* 160 */     makeSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\piglin\PiglinBrute.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */