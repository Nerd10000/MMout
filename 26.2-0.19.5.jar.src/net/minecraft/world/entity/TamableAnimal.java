/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageType;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ public abstract class TamableAnimal extends Animal implements OwnableEntity {
/*     */   public static final int TELEPORT_WHEN_DISTANCE_IS_SQ = 144;
/*     */   
/*     */   private static final int MIN_HORIZONTAL_DISTANCE_FROM_TARGET_AFTER_TELEPORTING = 2;
/*     */   
/*     */   private static final int MAX_HORIZONTAL_DISTANCE_FROM_TARGET_AFTER_TELEPORTING = 3;
/*     */   
/*     */   private static final int MAX_VERTICAL_DISTANCE_FROM_TARGET_AFTER_TELEPORTING = 1;
/*     */   
/*     */   private static final boolean DEFAULT_ORDERED_TO_SIT = false;
/*     */   
/*  42 */   protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TamableAnimal.class, EntityDataSerializers.BYTE);
/*     */   
/*  43 */   protected static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(TamableAnimal.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
/*     */   
/*     */   private boolean orderedToSit = false;
/*     */   
/*     */   protected TamableAnimal(EntityType<? extends TamableAnimal> type, Level level) {
/*  48 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  53 */     super.defineSynchedData(entityData);
/*  54 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*  55 */     entityData.define(DATA_OWNERUUID_ID, Optional.empty());
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  60 */     super.addAdditionalSaveData(output);
/*  61 */     EntityReference<LivingEntity> owner = getOwnerReference();
/*  62 */     EntityReference.store(owner, output, "Owner");
/*  63 */     output.putBoolean("Sitting", this.orderedToSit);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  68 */     super.readAdditionalSaveData(input);
/*  69 */     EntityReference<LivingEntity> owner = EntityReference.readWithOldOwnerConversion(input, "Owner", level());
/*  70 */     if (owner != null) {
/*     */       try {
/*  72 */         this.entityData.set(DATA_OWNERUUID_ID, Optional.of(owner));
/*  73 */         setTame(true, false);
/*  74 */       } catch (Throwable ignored) {
/*  75 */         setTame(false, true);
/*     */       } 
/*     */     } else {
/*  78 */       this.entityData.set(DATA_OWNERUUID_ID, Optional.empty());
/*  79 */       setTame(false, true);
/*     */     } 
/*  81 */     this.orderedToSit = input.getBooleanOr("Sitting", false);
/*  82 */     setInSittingPose(this.orderedToSit);
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   protected void spawnTamingParticles(boolean success) {
/*  91 */     SimpleParticleType simpleParticleType = ParticleTypes.HEART;
/*  92 */     if (!success)
/*  93 */       simpleParticleType = ParticleTypes.SMOKE; 
/*  95 */     for (int i = 0; i < 7; i++) {
/*  96 */       double xa = this.random.nextGaussian() * 0.02D;
/*  97 */       double ya = this.random.nextGaussian() * 0.02D;
/*  98 */       double za = this.random.nextGaussian() * 0.02D;
/*  99 */       level().addParticle((ParticleOptions)simpleParticleType, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xa, ya, za);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 105 */     if (id == 7) {
/* 106 */       spawnTamingParticles(true);
/* 107 */     } else if (id == 6) {
/* 108 */       spawnTamingParticles(false);
/*     */     } else {
/* 110 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isTame() {
/* 115 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x4) != 0);
/*     */   }
/*     */   
/*     */   public void setTame(boolean isTame, boolean includeSideEffects) {
/* 119 */     byte current = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 120 */     if (isTame) {
/* 121 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current | 0x4)));
/*     */     } else {
/* 123 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current & 0xFFFFFFFB)));
/*     */     } 
/* 125 */     if (includeSideEffects)
/* 126 */       applyTamingSideEffects(); 
/*     */   }
/*     */   
/*     */   protected void applyTamingSideEffects() {}
/*     */   
/*     */   protected void feed(Player player, InteractionHand hand, ItemStack itemStack, float healingFactor, float defaultHeal) {
/* 134 */     FoodProperties foodProperties = (FoodProperties)itemStack.get(DataComponents.FOOD);
/* 135 */     usePlayerItem(player, hand, itemStack);
/* 136 */     heal((foodProperties != null) ? (healingFactor * foodProperties.nutrition()) : defaultHeal);
/* 137 */     playEatingSound();
/*     */   }
/*     */   
/*     */   public boolean isInSittingPose() {
/* 141 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setInSittingPose(boolean value) {
/* 145 */     byte current = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 146 */     if (value) {
/* 147 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current | 0x1)));
/*     */     } else {
/* 149 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(current & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */   
/*     */   public EntityReference<LivingEntity> getOwnerReference() {
/* 155 */     return ((Optional<EntityReference<LivingEntity>>)this.entityData.get(DATA_OWNERUUID_ID)).orElse(null);
/*     */   }
/*     */   
/*     */   public void setOwner(LivingEntity owner) {
/* 159 */     this.entityData.set(DATA_OWNERUUID_ID, Optional.<LivingEntity>ofNullable(owner).map(EntityReference::of));
/*     */   }
/*     */   
/*     */   public void setOwnerReference(EntityReference<LivingEntity> owner) {
/* 163 */     this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(owner));
/*     */   }
/*     */   
/*     */   public void tame(Player player) {
/* 167 */     setTame(true, true);
/* 168 */     setOwner((LivingEntity)player);
/* 169 */     if (player instanceof ServerPlayer) {
/* 169 */       ServerPlayer serverPlayer = (ServerPlayer)player;
/* 170 */       CriteriaTriggers.TAME_ANIMAL.trigger(serverPlayer, this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canAttack(LivingEntity target) {
/* 176 */     if (isOwnedBy(target))
/* 177 */       return false; 
/* 179 */     return super.canAttack(target);
/*     */   }
/*     */   
/*     */   public boolean isOwnedBy(LivingEntity entity) {
/* 183 */     return (entity == getOwner());
/*     */   }
/*     */   
/*     */   public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
/* 187 */     return true;
/*     */   }
/*     */   
/*     */   public PlayerTeam getTeam() {
/* 192 */     PlayerTeam ownTeam = super.getTeam();
/* 193 */     if (ownTeam != null)
/* 194 */       return ownTeam; 
/* 196 */     if (isTame()) {
/* 197 */       LivingEntity owner = getRootOwner();
/* 198 */       if (owner != null)
/* 199 */         return owner.getTeam(); 
/*     */     } 
/* 202 */     return null;
/*     */   }
/*     */   
/*     */   protected boolean considersEntityAsAlly(Entity other) {
/* 207 */     if (isTame()) {
/* 208 */       LivingEntity owner = getRootOwner();
/* 209 */       if (other == owner)
/* 210 */         return true; 
/* 212 */       if (owner != null)
/* 213 */         return owner.considersEntityAsAlly(other); 
/*     */     } 
/* 216 */     return super.considersEntityAsAlly(other);
/*     */   }
/*     */   
/*     */   public void die(DamageSource source) {
/* 221 */     Level level = level();
/* 221 */     if (level instanceof ServerLevel) {
/* 221 */       ServerLevel serverLevel = (ServerLevel)level;
/* 221 */       if (((Boolean)serverLevel.getGameRules().get(GameRules.SHOW_DEATH_MESSAGES)).booleanValue()) {
/* 222 */         LivingEntity livingEntity = getOwner();
/* 222 */         if (livingEntity instanceof ServerPlayer) {
/* 222 */           ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
/* 223 */           serverPlayer.sendSystemMessage(getCombatTracker().getDeathMessage());
/*     */         } 
/*     */       } 
/*     */     } 
/* 226 */     super.die(source);
/*     */   }
/*     */   
/*     */   public boolean isOrderedToSit() {
/* 230 */     return this.orderedToSit;
/*     */   }
/*     */   
/*     */   public void setOrderedToSit(boolean orderedToSit) {
/* 234 */     this.orderedToSit = orderedToSit;
/*     */   }
/*     */   
/*     */   public void tryToTeleportToOwner() {
/* 238 */     LivingEntity owner = getOwner();
/* 239 */     if (owner != null)
/* 240 */       teleportToAroundBlockPos(owner.blockPosition()); 
/*     */   }
/*     */   
/*     */   public boolean shouldTryTeleportToOwner() {
/* 245 */     LivingEntity owner = getOwner();
/* 246 */     return (owner != null && distanceToSqr(getOwner()) >= 144.0D);
/*     */   }
/*     */   
/*     */   private void teleportToAroundBlockPos(BlockPos targetPos) {
/* 250 */     for (int attempt = 0; attempt < 10; attempt++) {
/* 251 */       int xd = this.random.nextIntBetweenInclusive(-3, 3);
/* 252 */       int zd = this.random.nextIntBetweenInclusive(-3, 3);
/* 253 */       if (Math.abs(xd) >= 2 || Math.abs(zd) >= 2) {
/* 257 */         int yd = this.random.nextIntBetweenInclusive(-1, 1);
/* 258 */         if (maybeTeleportTo(targetPos.getX() + xd, targetPos.getY() + yd, targetPos.getZ() + zd))
/*     */           return; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean maybeTeleportTo(int x, int y, int z) {
/* 265 */     if (!canTeleportTo(new BlockPos(x, y, z)))
/* 266 */       return false; 
/* 268 */     snapTo(x + 0.5D, y, z + 0.5D, getYRot(), getXRot());
/* 269 */     this.navigation.stop();
/* 270 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canTeleportTo(BlockPos pos) {
/* 274 */     PathType pathType = WalkNodeEvaluator.getPathTypeStatic((Mob)this, pos);
/* 275 */     if (pathType != PathType.WALKABLE)
/* 276 */       return false; 
/* 279 */     BlockState blockStateBelow = level().getBlockState(pos.below());
/* 280 */     if (!canFlyToOwner() && blockStateBelow.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock)
/* 282 */       return false; 
/* 285 */     BlockPos delta = pos.subtract((Vec3i)blockPosition());
/* 287 */     return level().noCollision((Entity)this, getBoundingBox().move(delta));
/*     */   }
/*     */   
/*     */   public final boolean unableToMoveToOwner() {
/* 291 */     return (isOrderedToSit() || isPassenger() || mayBeLeashed() || (getOwner() != null && getOwner().isSpectator()));
/*     */   }
/*     */   
/*     */   protected boolean canFlyToOwner() {
/* 295 */     return false;
/*     */   }
/*     */   
/*     */   public class TamableAnimalPanicGoal extends PanicGoal {
/*     */     public TamableAnimalPanicGoal(double speedModifier, TagKey<DamageType> panicCausingDamageTypes) {
/* 300 */       super((PathfinderMob)TamableAnimal.this, speedModifier, panicCausingDamageTypes);
/*     */     }
/*     */     
/*     */     public TamableAnimalPanicGoal(double speedModifier) {
/* 304 */       super((PathfinderMob)TamableAnimal.this, speedModifier);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 309 */       if (!TamableAnimal.this.unableToMoveToOwner() && TamableAnimal.this.shouldTryTeleportToOwner())
/* 310 */         TamableAnimal.this.tryToTeleportToOwner(); 
/* 312 */       super.tick();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\TamableAnimal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */