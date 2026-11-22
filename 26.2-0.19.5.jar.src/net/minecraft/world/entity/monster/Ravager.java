/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ColorParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.raid.Raider;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Ravager extends Raider {
/*     */   private static final Predicate<Entity> ROAR_TARGET_WITH_GRIEFING;
/*     */   
/*     */   private static final Predicate<Entity> ROAR_TARGET_WITHOUT_GRIEFING;
/*     */   
/*     */   private static final Predicate<LivingEntity> ROAR_TARGET_ON_CLIENT;
/*     */   
/*     */   private static final double BASE_MOVEMENT_SPEED = 0.3D;
/*     */   
/*     */   private static final double ATTACK_MOVEMENT_SPEED = 0.35D;
/*     */   
/*     */   private static final int STUNNED_COLOR = 8356754;
/*     */   
/*     */   private static final float STUNNED_COLOR_BLUE = 0.57254905F;
/*     */   
/*     */   private static final float STUNNED_COLOR_GREEN = 0.5137255F;
/*     */   
/*     */   private static final float STUNNED_COLOR_RED = 0.49803922F;
/*     */   
/*     */   public static final int ATTACK_DURATION = 10;
/*     */   
/*     */   public static final int STUN_DURATION = 40;
/*     */   
/*     */   private static final int DEFAULT_ATTACK_TICK = 0;
/*     */   
/*     */   private static final int DEFAULT_STUN_TICK = 0;
/*     */   
/*     */   private static final int DEFAULT_ROAR_TICK = 0;
/*     */   
/*     */   static {
/*  52 */     ROAR_TARGET_WITH_GRIEFING = (entity -> (!(entity instanceof Ravager) && entity.isAlive()));
/*  54 */     ROAR_TARGET_WITHOUT_GRIEFING = (entity -> (ROAR_TARGET_WITH_GRIEFING.test(entity) && !entity.is(EntityTypes.ARMOR_STAND)));
/*  56 */     ROAR_TARGET_ON_CLIENT = (e -> (!(e instanceof Ravager) && e.isAlive() && e.isLocalInstanceAuthoritative()));
/*     */   }
/*     */   
/*  71 */   private int attackTick = 0;
/*     */   
/*  72 */   private int stunnedTick = 0;
/*     */   
/*  73 */   private int roarTick = 0;
/*     */   
/*     */   public Ravager(EntityType<? extends Ravager> type, Level level) {
/*  76 */     super(type, level);
/*  78 */     this.xpReward = 20;
/*  80 */     setPathfindingMalus(PathType.LEAVES, 0.0F);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  85 */     super.registerGoals();
/*  87 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  88 */     this.goalSelector.addGoal(4, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.0D, true));
/*  89 */     this.goalSelector.addGoal(5, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.4D));
/*  90 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  91 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Mob.class, 8.0F));
/*  93 */     this.targetSelector.addGoal(2, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Raider.class })).setAlertOthers(new Class[0]));
/*  94 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*  95 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, true, (target, level) -> !target.isBaby()));
/*  96 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*     */   }
/*     */   
/*     */   protected void updateControlFlags() {
/* 101 */     boolean noController = (!(getControllingPassenger() instanceof Mob) || getControllingPassenger().is(EntityTypeTags.RAIDERS));
/* 102 */     boolean notInBoat = !(getVehicle() instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat);
/* 103 */     this.goalSelector.setControlFlag(Goal.Flag.MOVE, noController);
/* 104 */     this.goalSelector.setControlFlag(Goal.Flag.JUMP, (noController && notInBoat));
/* 105 */     this.goalSelector.setControlFlag(Goal.Flag.LOOK, noController);
/* 106 */     this.goalSelector.setControlFlag(Goal.Flag.TARGET, noController);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 110 */     return Monster.createMonsterAttributes()
/* 111 */       .add(Attributes.MAX_HEALTH, 100.0D)
/* 112 */       .add(Attributes.MOVEMENT_SPEED, 0.3D)
/* 113 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
/* 114 */       .add(Attributes.ATTACK_DAMAGE, 12.0D)
/* 115 */       .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
/* 116 */       .add(Attributes.FOLLOW_RANGE, 32.0D)
/* 117 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 122 */     super.addAdditionalSaveData(output);
/* 124 */     output.putInt("AttackTick", this.attackTick);
/* 125 */     output.putInt("StunTick", this.stunnedTick);
/* 126 */     output.putInt("RoarTick", this.roarTick);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 131 */     super.readAdditionalSaveData(input);
/* 133 */     this.attackTick = input.getIntOr("AttackTick", 0);
/* 134 */     this.stunnedTick = input.getIntOr("StunTick", 0);
/* 135 */     this.roarTick = input.getIntOr("RoarTick", 0);
/*     */   }
/*     */   
/*     */   public SoundEvent getCelebrateSound() {
/* 140 */     return SoundEvents.RAVAGER_CELEBRATE;
/*     */   }
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 145 */     return 45;
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 150 */     super.aiStep();
/* 152 */     if (!isAlive())
/*     */       return; 
/* 156 */     if (isImmobile()) {
/* 157 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
/*     */     } else {
/* 159 */       double maxSpeed = (getTarget() != null) ? 0.35D : 0.3D;
/* 160 */       double baseValue = getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
/* 161 */       getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(Mth.lerp(0.1D, baseValue, maxSpeed));
/*     */     } 
/* 164 */     Level level = level();
/* 164 */     if (level instanceof ServerLevel) {
/* 164 */       ServerLevel serverLevel = (ServerLevel)level;
/* 165 */       if (this.horizontalCollision && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 166 */         boolean destroyedBlock = false;
/* 167 */         AABB bb = getBoundingBox().inflate(0.2D);
/* 168 */         for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(bb.minX), Mth.floor(bb.minY), Mth.floor(bb.minZ), Mth.floor(bb.maxX), Mth.floor(bb.maxY), Mth.floor(bb.maxZ))) {
/* 169 */           BlockState state = serverLevel.getBlockState(pos);
/* 170 */           Block block = state.getBlock();
/* 171 */           if (block instanceof net.minecraft.world.level.block.LeavesBlock)
/* 172 */             destroyedBlock = (serverLevel.destroyBlock(pos, true, (Entity)this) || destroyedBlock); 
/*     */         } 
/* 176 */         if (!destroyedBlock && onGround())
/* 177 */           jumpFromGround(); 
/*     */       } 
/*     */     } 
/* 182 */     if (this.roarTick > 0) {
/* 183 */       this.roarTick--;
/* 185 */       if (this.roarTick == 10)
/* 186 */         roar(); 
/*     */     } 
/* 189 */     if (this.attackTick > 0)
/* 190 */       this.attackTick--; 
/* 192 */     if (this.stunnedTick > 0) {
/* 193 */       this.stunnedTick--;
/* 194 */       stunEffect();
/* 196 */       if (this.stunnedTick == 0) {
/* 197 */         playSound(SoundEvents.RAVAGER_ROAR, 1.0F, 1.0F);
/* 198 */         this.roarTick = 20;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void stunEffect() {
/* 204 */     if (this.random.nextInt(6) == 0) {
/* 205 */       double headX = getX() - getBbWidth() * Math.sin((this.yBodyRot * 0.017453292F)) + this.random.nextDouble() * 0.6D - 0.3D;
/* 206 */       double headY = getY() + getBbHeight() - 0.3D;
/* 207 */       double headZ = getZ() + getBbWidth() * Math.cos((this.yBodyRot * 0.017453292F)) + this.random.nextDouble() * 0.6D - 0.3D;
/* 209 */       level().addParticle((ParticleOptions)ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0.49803922F, 0.5137255F, 0.57254905F), headX, headY, headZ, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isImmobile() {
/* 215 */     return (super.isImmobile() || this.attackTick > 0 || this.stunnedTick > 0 || this.roarTick > 0);
/*     */   }
/*     */   
/*     */   public boolean hasLineOfSight(Entity target) {
/* 220 */     if (this.stunnedTick > 0 || this.roarTick > 0)
/* 221 */       return false; 
/* 223 */     return super.hasLineOfSight(target);
/*     */   }
/*     */   
/*     */   protected void blockedByItem(LivingEntity defender, DamageSource source, float damage) {
/* 228 */     if (this.roarTick == 0) {
/* 229 */       if (this.random.nextDouble() < 0.5D) {
/* 230 */         this.stunnedTick = 40;
/* 231 */         playSound(SoundEvents.RAVAGER_STUNNED, 1.0F, 1.0F);
/* 232 */         level().broadcastEntityEvent((Entity)this, (byte)39);
/* 234 */         defender.push((Entity)this);
/*     */       } else {
/* 236 */         strongKnockback((Entity)defender);
/*     */       } 
/* 238 */       defender.hurtMarked = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void roar() {
/* 243 */     if (isAlive()) {
/* 243 */       Level level = level();
/* 243 */       if (level instanceof ServerLevel) {
/* 243 */         ServerLevel serverLevel = (ServerLevel)level;
/* 244 */         Predicate<Entity> targetSelector = ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() ? ROAR_TARGET_WITH_GRIEFING : ROAR_TARGET_WITHOUT_GRIEFING;
/* 245 */         List<? extends LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D), targetSelector);
/* 246 */         for (LivingEntity entity : entities) {
/* 247 */           if (!(entity instanceof net.minecraft.world.entity.monster.illager.AbstractIllager))
/* 248 */             entity.hurtServer(serverLevel, damageSources().mobAttack((LivingEntity)this), 6.0F); 
/* 251 */           if (!(entity instanceof Player))
/* 252 */             strongKnockback((Entity)entity); 
/*     */         } 
/* 256 */         gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 257 */         serverLevel.broadcastEntityEvent((Entity)this, (byte)69);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applyRoarKnockbackClient() {
/* 265 */     List<? extends LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D), ROAR_TARGET_ON_CLIENT);
/* 266 */     for (LivingEntity entity : entities)
/* 267 */       strongKnockback((Entity)entity); 
/*     */   }
/*     */   
/*     */   private void strongKnockback(Entity entity) {
/* 273 */     double xd = entity.getX() - getX();
/* 274 */     double zd = entity.getZ() - getZ();
/* 275 */     double dd = Math.max(xd * xd + zd * zd, 0.001D);
/* 276 */     entity.push(xd / dd * 4.0D, 0.2D, zd / dd * 4.0D);
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 281 */     if (id == 4) {
/* 282 */       this.attackTick = 10;
/* 283 */       playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
/* 284 */     } else if (id == 39) {
/* 285 */       this.stunnedTick = 40;
/* 286 */     } else if (id == 69) {
/* 287 */       addRoarParticleEffects();
/* 288 */       applyRoarKnockbackClient();
/*     */     } 
/* 290 */     super.handleEntityEvent(id);
/*     */   }
/*     */   
/*     */   private void addRoarParticleEffects() {
/* 294 */     Vec3 center = getBoundingBox().getCenter();
/* 295 */     for (int i = 0; i < 40; i++) {
/* 296 */       double velocityX = this.random.nextGaussian() * 0.2D;
/* 297 */       double velocityY = this.random.nextGaussian() * 0.2D;
/* 298 */       double velocityZ = this.random.nextGaussian() * 0.2D;
/* 299 */       level().addParticle((ParticleOptions)ParticleTypes.POOF, center.x, center.y, center.z, velocityX, velocityY, velocityZ);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getAttackTick() {
/* 304 */     return this.attackTick;
/*     */   }
/*     */   
/*     */   public int getStunnedTick() {
/* 308 */     return this.stunnedTick;
/*     */   }
/*     */   
/*     */   public int getRoarTick() {
/* 312 */     return this.roarTick;
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 317 */     this.attackTick = 10;
/* 318 */     level.broadcastEntityEvent((Entity)this, (byte)4);
/* 319 */     playSound(SoundEvents.RAVAGER_ATTACK, 1.0F, 1.0F);
/* 321 */     return super.doHurtTarget(level, target);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 326 */     return SoundEvents.RAVAGER_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 331 */     return SoundEvents.RAVAGER_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 336 */     return SoundEvents.RAVAGER_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 341 */     playSound(SoundEvents.RAVAGER_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 346 */     return !level.containsAnyLiquid(getBoundingBox());
/*     */   }
/*     */   
/*     */   public void applyRaidBuffs(ServerLevel level, int wave, boolean isCaptain) {}
/*     */   
/*     */   public boolean canBeLeader() {
/* 355 */     return false;
/*     */   }
/*     */   
/*     */   protected AABB getAttackBoundingBox(double horizontalExpansion) {
/* 361 */     AABB defaultBB = super.getAttackBoundingBox(horizontalExpansion);
/* 362 */     return defaultBB.deflate(0.05D, 0.0D, 0.05D);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Ravager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */