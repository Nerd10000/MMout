/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ExperienceOrb extends Entity {
/*  33 */   protected static final EntityDataAccessor<Integer> DATA_VALUE = SynchedEntityData.defineId(ExperienceOrb.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int LIFETIME = 6000;
/*     */   
/*     */   private static final int ENTITY_SCAN_PERIOD = 20;
/*     */   
/*     */   private static final int MAX_FOLLOW_DIST = 8;
/*     */   
/*     */   private static final int ORB_GROUPS_PER_AREA = 40;
/*     */   
/*     */   private static final double ORB_MERGE_DISTANCE = 0.5D;
/*     */   
/*     */   private static final short DEFAULT_HEALTH = 5;
/*     */   
/*     */   private static final short DEFAULT_AGE = 0;
/*     */   
/*     */   private static final short DEFAULT_VALUE = 0;
/*     */   
/*     */   private static final int DEFAULT_COUNT = 1;
/*     */   
/*  44 */   private int age = 0;
/*     */   
/*  45 */   private int health = 5;
/*     */   
/*  46 */   private int count = 1;
/*     */   
/*     */   private Player followingPlayer;
/*     */   
/*  48 */   private final InterpolationHandler interpolation = new InterpolationHandler(this);
/*     */   
/*     */   public ExperienceOrb(Level level, double x, double y, double z, int value) {
/*  51 */     this(level, new Vec3(x, y, z), Vec3.ZERO, value);
/*     */   }
/*     */   
/*     */   public ExperienceOrb(Level level, Vec3 pos, Vec3 roughly, int value) {
/*  55 */     this(EntityTypes.EXPERIENCE_ORB, level);
/*  56 */     setPos(pos);
/*  57 */     if (!level.isClientSide()) {
/*  58 */       setYRot(this.random.nextFloat() * 360.0F);
/*  62 */       Vec3 randomMovement = new Vec3((this.random.nextDouble() * 0.2D - 0.1D) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * 0.2D - 0.1D) * 2.0D);
/*  64 */       if (roughly.lengthSqr() > 0.0D && roughly.dot(randomMovement) < 0.0D)
/*  65 */         randomMovement = randomMovement.scale(-1.0D); 
/*  68 */       double size = getBoundingBox().getSize();
/*  69 */       setPos(pos.add(roughly.normalize().scale(size * 0.5D)));
/*  70 */       setDeltaMovement(randomMovement);
/*  72 */       if (!level.noCollision(getBoundingBox()))
/*  73 */         unstuckIfPossible(size); 
/*     */     } 
/*  76 */     setValue(value);
/*     */   }
/*     */   
/*     */   public ExperienceOrb(EntityType<? extends ExperienceOrb> type, Level level) {
/*  80 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void unstuckIfPossible(double maxDistance) {
/*  84 */     Vec3 center = position().add(0.0D, getBbHeight() / 2.0D, 0.0D);
/*  85 */     VoxelShape allowedCenters = Shapes.create(AABB.ofSize(center, maxDistance, maxDistance, maxDistance));
/*  86 */     level().findFreePosition(this, allowedCenters, center, getBbWidth(), getBbHeight(), getBbWidth())
/*  87 */       .ifPresent(pos -> setPos(pos.add(0.0D, -getBbHeight() / 2.0D, 0.0D)));
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/*  92 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  97 */     entityData.define(DATA_VALUE, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   protected double getDefaultGravity() {
/* 102 */     return 0.03D;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 107 */     this.interpolation.interpolate();
/* 108 */     if (this.firstTick && level().isClientSide()) {
/* 109 */       this.firstTick = false;
/*     */       return;
/*     */     } 
/* 113 */     super.tick();
/* 115 */     boolean colliding = !level().noCollision(getBoundingBox());
/* 117 */     if (isEyeInFluid(FluidTags.WATER)) {
/* 118 */       setUnderwaterMovement();
/* 119 */     } else if (!colliding) {
/* 120 */       applyGravity();
/*     */     } 
/* 123 */     if (level().getFluidState(blockPosition()).is(FluidTags.LAVA))
/* 124 */       setDeltaMovement(((this.random
/* 125 */           .nextFloat() - this.random.nextFloat()) * 0.2F), 0.20000000298023224D, ((this.random
/*     */           
/* 127 */           .nextFloat() - this.random.nextFloat()) * 0.2F)); 
/* 131 */     if (this.tickCount % 20 == 1)
/* 132 */       scanForMerges(); 
/* 135 */     followNearbyPlayer();
/* 137 */     if (this.followingPlayer == null && !level().isClientSide() && colliding) {
/* 138 */       boolean nextColliding = !level().noCollision(getBoundingBox().move(getDeltaMovement()));
/* 139 */       if (nextColliding) {
/* 140 */         moveTowardsClosestSpace(getX(), ((getBoundingBox()).minY + (getBoundingBox()).maxY) / 2.0D, getZ());
/* 141 */         this.needsSync = true;
/*     */       } 
/*     */     } 
/* 145 */     double fallSpeed = (getDeltaMovement()).y;
/* 146 */     move(MoverType.SELF, getDeltaMovement());
/* 147 */     applyEffectsFromBlocks();
/* 149 */     float friction = getAirDrag();
/* 150 */     if (onGround())
/* 151 */       friction *= level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction(); 
/* 154 */     setDeltaMovement(getDeltaMovement().scale(friction));
/* 155 */     if (this.verticalCollisionBelow && fallSpeed < -getGravity())
/* 156 */       setDeltaMovement(new Vec3((getDeltaMovement()).x, -fallSpeed * 0.4D, (getDeltaMovement()).z)); 
/* 159 */     this.age++;
/* 160 */     if (this.age >= 6000)
/* 161 */       discard(); 
/*     */   }
/*     */   
/*     */   protected float getAirDrag() {
/* 167 */     return 0.98F;
/*     */   }
/*     */   
/*     */   private void followNearbyPlayer() {
/* 171 */     if (this.followingPlayer == null || this.followingPlayer
/* 172 */       .isSpectator() || this.followingPlayer
/* 173 */       .distanceToSqr(this) > 64.0D) {
/* 174 */       Player nearestPlayer = level().getNearestPlayer(this, 8.0D);
/* 175 */       if (nearestPlayer != null && !nearestPlayer.isSpectator() && !nearestPlayer.isDeadOrDying()) {
/* 176 */         this.followingPlayer = nearestPlayer;
/*     */       } else {
/* 178 */         this.followingPlayer = null;
/*     */       } 
/*     */     } 
/* 182 */     if (this.followingPlayer != null) {
/* 186 */       Vec3 delta = new Vec3(this.followingPlayer.getX() - getX(), this.followingPlayer.getY() + this.followingPlayer.getEyeHeight() / 2.0D - getY(), this.followingPlayer.getZ() - getZ());
/* 188 */       double length = delta.lengthSqr();
/* 189 */       double power = 1.0D - Math.sqrt(length) / 8.0D;
/* 190 */       setDeltaMovement(getDeltaMovement().add(delta.normalize().scale(power * power * 0.1D)));
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockPos getBlockPosBelowThatAffectsMyMovement() {
/* 197 */     return getOnPos(0.999999F);
/*     */   }
/*     */   
/*     */   private void scanForMerges() {
/* 201 */     if (level() instanceof ServerLevel) {
/* 202 */       List<ExperienceOrb> orbs = level().getEntities(EntityTypeTest.forClass(ExperienceOrb.class), getBoundingBox().inflate(0.5D), this::canMerge);
/* 203 */       for (ExperienceOrb orb : orbs)
/* 204 */         merge(orb); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void award(ServerLevel level, Vec3 pos, int amount) {
/* 210 */     awardWithDirection(level, pos, Vec3.ZERO, amount);
/*     */   }
/*     */   
/*     */   public static void awardWithDirection(ServerLevel level, Vec3 pos, Vec3 roughDirection, int amount) {
/* 214 */     while (amount > 0) {
/* 215 */       int newCount = getExperienceValue(amount);
/* 216 */       amount -= newCount;
/* 217 */       if (!tryMergeToExisting(level, pos, newCount))
/* 218 */         level.addFreshEntity(new ExperienceOrb((Level)level, pos, roughDirection, newCount)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean tryMergeToExisting(ServerLevel level, Vec3 pos, int value) {
/* 224 */     AABB box = AABB.ofSize(pos, 1.0D, 1.0D, 1.0D);
/* 225 */     int id = level.getRandom().nextInt(40);
/* 226 */     List<ExperienceOrb> orbs = level.getEntities(EntityTypeTest.forClass(ExperienceOrb.class), box, orb -> canMerge(orb, id, value));
/* 227 */     if (!orbs.isEmpty()) {
/* 228 */       ExperienceOrb orb = orbs.get(0);
/* 229 */       orb.count++;
/* 230 */       orb.age = 0;
/* 231 */       return true;
/*     */     } 
/* 233 */     return false;
/*     */   }
/*     */   
/*     */   private boolean canMerge(ExperienceOrb orb) {
/* 237 */     return (orb != this && 
/* 238 */       canMerge(orb, getId(), getValue()));
/*     */   }
/*     */   
/*     */   private static boolean canMerge(ExperienceOrb orb, int id, int value) {
/* 242 */     return (!orb.isRemoved() && (orb
/* 243 */       .getId() - id) % 40 == 0 && orb
/* 244 */       .getValue() == value);
/*     */   }
/*     */   
/*     */   private void merge(ExperienceOrb orb) {
/* 248 */     this.count += orb.count;
/* 249 */     this.age = Math.min(this.age, orb.age);
/* 250 */     orb.discard();
/*     */   }
/*     */   
/*     */   private void setUnderwaterMovement() {
/* 255 */     Vec3 movement = getDeltaMovement();
/* 257 */     setDeltaMovement(movement.x * 0.9900000095367432D, 
/*     */         
/* 259 */         Math.min(movement.y + 5.000000237487257E-4D, 0.05999999865889549D), movement.z * 0.9900000095367432D);
/*     */   }
/*     */   
/*     */   protected void doWaterSplashEffect() {}
/*     */   
/*     */   public final boolean hurtClient(DamageSource source) {
/* 270 */     return !isInvulnerableToBase(source);
/*     */   }
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 275 */     if (isInvulnerableToBase(source))
/* 276 */       return false; 
/* 278 */     markHurt();
/* 279 */     this.health = (int)(this.health - damage);
/* 280 */     if (this.health <= 0)
/* 281 */       discard(); 
/* 283 */     return true;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 288 */     output.putShort("Health", (short)this.health);
/* 289 */     output.putShort("Age", (short)this.age);
/* 290 */     output.putShort("Value", (short)getValue());
/* 291 */     output.putInt("Count", this.count);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 296 */     this.health = input.getShortOr("Health", (short)5);
/* 297 */     this.age = input.getShortOr("Age", (short)0);
/* 298 */     setValue(input.getShortOr("Value", (short)0));
/* 299 */     this.count = ((Integer)input.read("Count", ExtraCodecs.POSITIVE_INT).orElse(Integer.valueOf(1))).intValue();
/*     */   }
/*     */   
/*     */   public void playerTouch(Player player) {
/*     */     ServerPlayer serverPlayer;
/* 304 */     if (player instanceof ServerPlayer) {
/* 304 */       serverPlayer = (ServerPlayer)player;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 308 */     if (player.takeXpDelay == 0) {
/* 309 */       player.takeXpDelay = 2;
/* 310 */       player.take(this, 1);
/* 311 */       int remaining = repairPlayerItems(serverPlayer, getValue());
/* 312 */       if (remaining > 0)
/* 313 */         player.giveExperiencePoints(remaining); 
/* 315 */       this.count--;
/* 316 */       if (this.count == 0)
/* 317 */         discard(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int repairPlayerItems(ServerPlayer player, int amount) {
/* 323 */     Optional<EnchantedItemInUse> selected = EnchantmentHelper.getRandomItemWith(EnchantmentEffectComponents.REPAIR_WITH_XP, (LivingEntity)player, ItemStack::isDamaged);
/* 324 */     if (selected.isPresent()) {
/* 325 */       ItemStack itemStack = ((EnchantedItemInUse)selected.get()).itemStack();
/* 326 */       int toRepairFromXpAmount = EnchantmentHelper.modifyDurabilityToRepairFromXp(player.level(), itemStack, amount);
/* 327 */       int repair = Math.min(toRepairFromXpAmount, itemStack.getDamageValue());
/* 328 */       itemStack.setDamageValue(itemStack.getDamageValue() - repair);
/* 330 */       if (repair > 0) {
/* 332 */         int remaining = amount - repair * amount / toRepairFromXpAmount;
/* 333 */         if (remaining > 0)
/* 334 */           return repairPlayerItems(player, remaining); 
/*     */       } 
/* 337 */       return 0;
/*     */     } 
/* 339 */     return amount;
/*     */   }
/*     */   
/*     */   public int getValue() {
/* 343 */     return ((Integer)this.entityData.get(DATA_VALUE)).intValue();
/*     */   }
/*     */   
/*     */   private void setValue(int value) {
/* 347 */     this.entityData.set(DATA_VALUE, Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getIcon() {
/* 351 */     int value = getValue();
/* 352 */     if (value >= 2477)
/* 353 */       return 10; 
/* 354 */     if (value >= 1237)
/* 355 */       return 9; 
/* 356 */     if (value >= 617)
/* 357 */       return 8; 
/* 358 */     if (value >= 307)
/* 359 */       return 7; 
/* 360 */     if (value >= 149)
/* 361 */       return 6; 
/* 362 */     if (value >= 73)
/* 363 */       return 5; 
/* 364 */     if (value >= 37)
/* 365 */       return 4; 
/* 366 */     if (value >= 17)
/* 367 */       return 3; 
/* 368 */     if (value >= 7)
/* 369 */       return 2; 
/* 370 */     if (value >= 3)
/* 371 */       return 1; 
/* 374 */     return 0;
/*     */   }
/*     */   
/*     */   public static int getExperienceValue(int maxValue) {
/* 384 */     if (maxValue >= 2477)
/* 385 */       return 2477; 
/* 386 */     if (maxValue >= 1237)
/* 387 */       return 1237; 
/* 388 */     if (maxValue >= 617)
/* 389 */       return 617; 
/* 390 */     if (maxValue >= 307)
/* 391 */       return 307; 
/* 392 */     if (maxValue >= 149)
/* 393 */       return 149; 
/* 394 */     if (maxValue >= 73)
/* 395 */       return 73; 
/* 396 */     if (maxValue >= 37)
/* 397 */       return 37; 
/* 398 */     if (maxValue >= 17)
/* 399 */       return 17; 
/* 400 */     if (maxValue >= 7)
/* 401 */       return 7; 
/* 402 */     if (maxValue >= 3)
/* 403 */       return 3; 
/* 406 */     return 1;
/*     */   }
/*     */   
/*     */   public boolean isAttackable() {
/* 411 */     return false;
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 416 */     return SoundSource.AMBIENT;
/*     */   }
/*     */   
/*     */   public InterpolationHandler getInterpolation() {
/* 421 */     return this.interpolation;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ExperienceOrb.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */