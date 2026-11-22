/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.SpearUseGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class ZombifiedPiglin extends Zombie implements NeutralMob {
/*  49 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.49F, 0.98F)
/*  50 */     .withEyeHeight(0.78F)
/*  51 */     .withAttachments(EntityAttachments.builder()
/*  52 */       .attach(EntityAttachment.VEHICLE, 0.0F, 0.1875F, 0.0F));
/*     */   
/*  54 */   private static final Identifier SPEED_MODIFIER_ATTACKING_ID = Identifier.withDefaultNamespace("attacking");
/*     */   
/*  55 */   private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_ID, 0.05D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*  57 */   private static final UniformInt FIRST_ANGER_SOUND_DELAY = TimeUtil.rangeOfSeconds(0, 1);
/*     */   
/*     */   private int playFirstAngerSoundIn;
/*     */   
/*  60 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   
/*     */   private long persistentAngerEndTime;
/*     */   
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*     */   private static final int ALERT_RANGE_Y = 10;
/*     */   
/*  65 */   private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);
/*     */   
/*     */   private int ticksUntilNextAlert;
/*     */   
/*     */   public ZombifiedPiglin(EntityType<? extends ZombifiedPiglin> type, Level level) {
/*  69 */     super((EntityType)type, level);
/*  70 */     setPathfindingMalus(PathType.LAVA, 8.0F);
/*     */   }
/*     */   
/*     */   protected void addBehaviourGoals() {
/*  75 */     this.goalSelector.addGoal(1, (Goal)new SpearUseGoal(this, 1.0D, 1.0D, 10.0F, 2.0F));
/*  76 */     this.goalSelector.addGoal(2, (Goal)new ZombieAttackGoal(this, 1.0D, false));
/*  77 */     this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/*  79 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[0])).setAlertOthers(new Class[0]));
/*  80 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/*  81 */     this.targetSelector.addGoal(3, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  85 */     return Zombie.createAttributes()
/*  86 */       .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0D)
/*  87 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
/*  88 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/*  93 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   protected boolean convertsInWater() {
/*  98 */     return false;
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 103 */     AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/* 104 */     if (isAngry()) {
/* 105 */       if (!isBaby() && !speed.hasModifier(SPEED_MODIFIER_ATTACKING_ID))
/* 106 */         speed.addTransientModifier(SPEED_MODIFIER_ATTACKING); 
/* 108 */       maybePlayFirstAngerSound();
/* 109 */     } else if (speed.hasModifier(SPEED_MODIFIER_ATTACKING_ID)) {
/* 110 */       speed.removeModifier(SPEED_MODIFIER_ATTACKING_ID);
/*     */     } 
/* 113 */     updatePersistentAnger(level, true);
/* 114 */     if (getTarget() != null)
/* 115 */       maybeAlertOthers(); 
/* 118 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   private void maybePlayFirstAngerSound() {
/* 122 */     if (this.playFirstAngerSoundIn > 0) {
/* 123 */       this.playFirstAngerSoundIn--;
/* 124 */       if (this.playFirstAngerSoundIn == 0)
/* 125 */         playAngerSound(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void maybeAlertOthers() {
/* 133 */     if (this.ticksUntilNextAlert > 0) {
/* 134 */       this.ticksUntilNextAlert--;
/*     */       return;
/*     */     } 
/* 137 */     if (getSensing().hasLineOfSight((Entity)getTarget()))
/* 138 */       alertOthers(); 
/* 140 */     this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
/*     */   }
/*     */   
/*     */   private void alertOthers() {
/* 144 */     double within = getAttributeValue(Attributes.FOLLOW_RANGE);
/* 145 */     AABB searchAabb = AABB.unitCubeFromLowerCorner(position()).inflate(within, 10.0D, within);
/* 146 */     level().getEntitiesOfClass(ZombifiedPiglin.class, searchAabb, EntitySelector.NO_SPECTATORS).stream()
/* 147 */       .filter(other -> (other != this))
/* 148 */       .filter(other -> (other.getTarget() == null))
/* 149 */       .filter(other -> !other.isAlliedTo((Entity)getTarget()))
/* 150 */       .forEach(other -> other.setTarget(getTarget()));
/*     */   }
/*     */   
/*     */   private void playAngerSound() {
/* 154 */     playSound(SoundEvents.ZOMBIFIED_PIGLIN_ANGRY, getSoundVolume() * 2.0F, getVoicePitch() * 1.8F);
/*     */   }
/*     */   
/*     */   public void setTarget(LivingEntity target) {
/* 159 */     if (getTarget() == null && target != null) {
/* 162 */       this.playFirstAngerSoundIn = FIRST_ANGER_SOUND_DELAY.sample(this.random);
/* 163 */       this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
/*     */     } 
/* 165 */     super.setTarget(target);
/*     */   }
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 170 */     setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
/*     */   }
/*     */   
/*     */   public static boolean checkZombifiedPiglinSpawnRules(EntityType<ZombifiedPiglin> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 174 */     return (level.getDifficulty() != Difficulty.PEACEFUL && !level.getBlockState(pos.below()).is(Blocks.NETHER_WART_BLOCK));
/*     */   }
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 179 */     return (level.isUnobstructed((Entity)this) && !level.containsAnyLiquid(getBoundingBox()));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 184 */     super.addAdditionalSaveData(output);
/* 185 */     addPersistentAngerSaveData(output);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 190 */     super.readAdditionalSaveData(input);
/* 191 */     readPersistentAngerSaveData(level(), input);
/*     */   }
/*     */   
/*     */   public void setPersistentAngerEndTime(long endTime) {
/* 196 */     this.persistentAngerEndTime = endTime;
/*     */   }
/*     */   
/*     */   public long getPersistentAngerEndTime() {
/* 201 */     return this.persistentAngerEndTime;
/*     */   }
/*     */   
/*     */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) {
/* 206 */     this.persistentAngerTarget = persistentAngerTarget;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 211 */     return isAngry() ? SoundEvents.ZOMBIFIED_PIGLIN_ANGRY : SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 216 */     return SoundEvents.ZOMBIFIED_PIGLIN_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 221 */     return SoundEvents.ZOMBIFIED_PIGLIN_DEATH;
/*     */   }
/*     */   
/*     */   public void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 226 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((random.nextInt(20) == 0) ? (ItemLike)Items.GOLDEN_SPEAR : (ItemLike)Items.GOLDEN_SWORD));
/*     */   }
/*     */   
/*     */   protected void randomizeReinforcementsChance() {
/* 231 */     getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(0.0D);
/*     */   }
/*     */   
/*     */   public EntityReference<LivingEntity> getPersistentAngerTarget() {
/* 236 */     return this.persistentAngerTarget;
/*     */   }
/*     */   
/*     */   public boolean isPreventingPlayerRest(ServerLevel level, Player player) {
/* 241 */     return isAngryAt((LivingEntity)player, level);
/*     */   }
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 246 */     return canHoldItem(itemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\ZombifiedPiglin.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */