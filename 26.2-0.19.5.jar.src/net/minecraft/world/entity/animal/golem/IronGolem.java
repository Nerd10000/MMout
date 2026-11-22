/*     */ package net.minecraft.world.entity.animal.golem;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Crackiness;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.OfferFlowerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class IronGolem extends AbstractGolem implements NeutralMob {
/*  55 */   protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(IronGolem.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final int IRON_INGOT_HEAL_AMOUNT = 25;
/*     */   
/*     */   private static final boolean DEFAULT_PLAYER_CREATED = false;
/*     */   
/*     */   private int attackAnimationTick;
/*     */   
/*     */   private int offerFlowerTick;
/*     */   
/*  62 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   
/*     */   private long persistentAngerEndTime;
/*     */   
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*     */   public IronGolem(EntityType<? extends IronGolem> type, Level level) {
/*  67 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  72 */     this.goalSelector.addGoal(1, (Goal)new MeleeAttackGoal(this, 1.0D, true));
/*  73 */     this.goalSelector.addGoal(2, (Goal)new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
/*  74 */     this.goalSelector.addGoal(2, (Goal)new MoveBackToVillageGoal(this, 0.6D, false));
/*  75 */     this.goalSelector.addGoal(4, (Goal)new GolemRandomStrollInVillageGoal(this, 0.6D));
/*  76 */     this.goalSelector.addGoal(5, (Goal)new OfferFlowerGoal(this));
/*  77 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  78 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*  80 */     this.targetSelector.addGoal(1, (Goal)new DefendVillageTargetGoal(this));
/*  81 */     this.targetSelector.addGoal(2, (Goal)new HurtByTargetGoal(this, new Class[0]));
/*  82 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/*  83 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Mob.class, 5, false, false, (target, paramServerLevel) -> (target instanceof net.minecraft.world.entity.monster.Enemy && !(target instanceof net.minecraft.world.entity.monster.Creeper))));
/*  84 */     this.targetSelector.addGoal(4, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  89 */     super.defineSynchedData(entityData);
/*  90 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  94 */     return Mob.createMobAttributes()
/*  95 */       .add(Attributes.MAX_HEALTH, 100.0D)
/*  96 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/*  97 */       .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
/*  98 */       .add(Attributes.ATTACK_DAMAGE, 15.0D)
/*  99 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */   
/*     */   protected int decreaseAirSupply(int currentSupply) {
/* 105 */     return currentSupply;
/*     */   }
/*     */   
/*     */   protected void doPush(Entity entity) {
/* 110 */     if (entity instanceof net.minecraft.world.entity.monster.Enemy && !(entity instanceof net.minecraft.world.entity.monster.Creeper) && 
/* 111 */       getRandom().nextInt(20) == 0)
/* 112 */       setTarget((LivingEntity)entity); 
/* 115 */     super.doPush(entity);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 120 */     super.aiStep();
/* 122 */     if (this.attackAnimationTick > 0)
/* 123 */       this.attackAnimationTick--; 
/* 125 */     if (this.offerFlowerTick > 0)
/* 126 */       this.offerFlowerTick--; 
/* 129 */     if (!level().isClientSide())
/* 130 */       updatePersistentAnger((ServerLevel)level(), true); 
/*     */   }
/*     */   
/*     */   public boolean canSpawnSprintParticle() {
/* 136 */     return (getDeltaMovement().horizontalDistanceSqr() > 2.500000277905201E-7D && this.random.nextInt(5) == 0);
/*     */   }
/*     */   
/*     */   public boolean canAttack(LivingEntity target) {
/* 141 */     if (isPlayerCreated() && target.is(EntityTypes.PLAYER))
/* 142 */       return false; 
/* 144 */     if (target.is(EntityTypes.CREEPER))
/* 145 */       return false; 
/* 147 */     return super.canAttack(target);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 152 */     super.addAdditionalSaveData(output);
/* 153 */     output.putBoolean("PlayerCreated", isPlayerCreated());
/* 154 */     addPersistentAngerSaveData(output);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 159 */     super.readAdditionalSaveData(input);
/* 160 */     setPlayerCreated(input.getBooleanOr("PlayerCreated", false));
/* 161 */     readPersistentAngerSaveData(level(), input);
/*     */   }
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 166 */     setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
/*     */   }
/*     */   
/*     */   public void setPersistentAngerEndTime(long endTime) {
/* 171 */     this.persistentAngerEndTime = endTime;
/*     */   }
/*     */   
/*     */   public long getPersistentAngerEndTime() {
/* 176 */     return this.persistentAngerEndTime;
/*     */   }
/*     */   
/*     */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) {
/* 181 */     this.persistentAngerTarget = persistentAngerTarget;
/*     */   }
/*     */   
/*     */   public EntityReference<LivingEntity> getPersistentAngerTarget() {
/* 186 */     return this.persistentAngerTarget;
/*     */   }
/*     */   
/*     */   private float getAttackDamage() {
/* 190 */     return (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 195 */     this.attackAnimationTick = 10;
/* 196 */     level.broadcastEntityEvent((Entity)this, (byte)4);
/* 197 */     float attackDamage = getAttackDamage();
/* 198 */     float damage = ((int)attackDamage > 0) ? (attackDamage / 2.0F + this.random.nextInt((int)attackDamage)) : attackDamage;
/* 199 */     DamageSource damageSource = damageSources().mobAttack((LivingEntity)this);
/* 200 */     boolean hurt = target.hurtServer(level, damageSource, damage);
/* 201 */     if (hurt) {
/* 202 */       LivingEntity livingEntity = (LivingEntity)target;
/* 202 */       double knockbackResistance = (target instanceof LivingEntity) ? livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0.0D;
/* 203 */       double scale = Math.max(0.0D, 1.0D - knockbackResistance);
/* 205 */       target.setDeltaMovement(target.getDeltaMovement().add(0.0D, 0.4000000059604645D * scale, 0.0D));
/* 206 */       EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/*     */     } 
/* 208 */     playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
/* 209 */     return hurt;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 214 */     Crackiness.Level previousCrackiness = getCrackiness();
/* 215 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 216 */     if (wasHurt && getCrackiness() != previousCrackiness)
/* 217 */       playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F); 
/* 219 */     return wasHurt;
/*     */   }
/*     */   
/*     */   public Crackiness.Level getCrackiness() {
/* 224 */     return Crackiness.GOLEM.byFraction(getHealth() / getMaxHealth());
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 229 */     if (id == 4) {
/* 230 */       this.attackAnimationTick = 10;
/* 231 */       playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
/* 232 */     } else if (id == 11) {
/* 233 */       this.offerFlowerTick = 400;
/* 234 */     } else if (id == 34) {
/* 235 */       this.offerFlowerTick = 0;
/*     */     } else {
/* 237 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getAttackAnimationTick() {
/* 242 */     return this.attackAnimationTick;
/*     */   }
/*     */   
/*     */   public void offerFlower(boolean offer) {
/* 246 */     if (offer) {
/* 247 */       this.offerFlowerTick = 400;
/* 248 */       level().broadcastEntityEvent((Entity)this, (byte)11);
/*     */     } else {
/* 250 */       this.offerFlowerTick = 0;
/* 251 */       level().broadcastEntityEvent((Entity)this, (byte)34);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 257 */     return SoundEvents.IRON_GOLEM_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 262 */     return SoundEvents.IRON_GOLEM_DEATH;
/*     */   }
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 267 */     ItemStack itemStack = player.getItemInHand(hand);
/* 268 */     if (!itemStack.is(Items.IRON_INGOT))
/* 269 */       return (InteractionResult)InteractionResult.PASS; 
/* 272 */     float healthBefore = getHealth();
/* 273 */     heal(25.0F);
/* 274 */     if (getHealth() == healthBefore)
/* 275 */       return (InteractionResult)InteractionResult.PASS; 
/* 278 */     float pitch = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
/* 279 */     playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, pitch);
/* 281 */     itemStack.consume(1, (LivingEntity)player);
/* 282 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 287 */     playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public int getOfferFlowerTick() {
/* 291 */     return this.offerFlowerTick;
/*     */   }
/*     */   
/*     */   public boolean isPlayerCreated() {
/* 295 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setPlayerCreated(boolean value) {
/* 299 */     byte current = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 300 */     if (value) {
/* 301 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current | 0x1)));
/*     */     } else {
/* 303 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void die(DamageSource source) {
/* 310 */     super.die(source);
/*     */   }
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 316 */     BlockPos pos = blockPosition();
/* 317 */     BlockPos belowPos = pos.below();
/* 318 */     BlockState below = level.getBlockState(belowPos);
/* 319 */     if (below.entityCanStandOn((BlockGetter)level, belowPos, (Entity)this)) {
/* 320 */       for (int i = 1; i < 3; i++) {
/* 321 */         BlockPos abovePos = pos.above(i);
/* 322 */         BlockState above = level.getBlockState(abovePos);
/* 323 */         if (!NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)level, abovePos, above, above.getFluidState(), EntityTypes.IRON_GOLEM))
/* 324 */           return false; 
/*     */       } 
/* 327 */       return (NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)level, pos, level.getBlockState(pos), Fluids.EMPTY.defaultFluidState(), EntityTypes.IRON_GOLEM) && level
/* 328 */         .isUnobstructed((Entity)this));
/*     */     } 
/* 330 */     return false;
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 335 */     return new Vec3(0.0D, (0.875F * getEyeHeight()), (getBbWidth() * 0.4F));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\golem\IronGolem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */