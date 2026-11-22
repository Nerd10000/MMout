/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Rotations;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ArmorStand extends LivingEntity {
/*     */   public static final int WOBBLE_TIME = 5;
/*     */   
/*     */   private static final boolean ENABLE_ARMS = true;
/*     */   
/*  61 */   public static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);
/*     */   
/*  62 */   public static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);
/*     */   
/*  63 */   public static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(-10.0F, 0.0F, -10.0F);
/*     */   
/*  64 */   public static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(-15.0F, 0.0F, 10.0F);
/*     */   
/*  65 */   public static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(-1.0F, 0.0F, -1.0F);
/*     */   
/*  66 */   public static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(1.0F, 0.0F, 1.0F);
/*     */   
/*  68 */   private static final EntityDimensions MARKER_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);
/*     */   
/*  69 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.ARMOR_STAND.getDimensions().scale(0.5F).withEyeHeight(0.9875F);
/*     */   
/*     */   private static final double FEET_OFFSET = 0.1D;
/*     */   
/*     */   private static final double CHEST_OFFSET = 0.9D;
/*     */   
/*     */   private static final double LEGS_OFFSET = 0.4D;
/*     */   
/*     */   private static final double HEAD_OFFSET = 1.6D;
/*     */   
/*     */   public static final int DISABLE_TAKING_OFFSET = 8;
/*     */   
/*     */   public static final int DISABLE_PUTTING_OFFSET = 16;
/*     */   
/*     */   public static final int CLIENT_FLAG_SMALL = 1;
/*     */   
/*     */   public static final int CLIENT_FLAG_SHOW_ARMS = 4;
/*     */   
/*     */   public static final int CLIENT_FLAG_NO_BASEPLATE = 8;
/*     */   
/*     */   public static final int CLIENT_FLAG_MARKER = 16;
/*     */   
/*  84 */   public static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.BYTE);
/*     */   
/*  85 */   public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*     */   
/*  86 */   public static final EntityDataAccessor<Rotations> DATA_BODY_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*     */   
/*  87 */   public static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*     */   
/*  88 */   public static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*     */   
/*  89 */   public static final EntityDataAccessor<Rotations> DATA_LEFT_LEG_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*     */   
/*  90 */   public static final EntityDataAccessor<Rotations> DATA_RIGHT_LEG_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*     */   
/*     */   private static final Predicate<Entity> RIDABLE_MINECARTS;
/*     */   
/*     */   private static final boolean DEFAULT_INVISIBLE = false;
/*     */   
/*     */   private static final int DEFAULT_DISABLED_SLOTS = 0;
/*     */   
/*     */   private static final boolean DEFAULT_SMALL = false;
/*     */   
/*     */   private static final boolean DEFAULT_SHOW_ARMS = false;
/*     */   
/*     */   private static final boolean DEFAULT_NO_BASE_PLATE = false;
/*     */   
/*     */   private static final boolean DEFAULT_MARKER = false;
/*     */   
/*     */   static {
/*  92 */     RIDABLE_MINECARTS = (entity -> {
/*     */         if (entity instanceof AbstractMinecart) {
/*     */           AbstractMinecart minecart = (AbstractMinecart)entity;
/*     */           if (minecart.isRideable());
/*     */         } 
/*     */         return false;
/*     */       });
/*     */   }
/*     */   
/*     */   private boolean invisible = false;
/*     */   
/*     */   public long lastHit;
/*     */   
/* 103 */   private int disabledSlots = 0;
/*     */   
/*     */   public ArmorStand(EntityType<? extends ArmorStand> type, Level level) {
/* 106 */     super(type, level);
/*     */   }
/*     */   
/*     */   public ArmorStand(Level level, double x, double y, double z) {
/* 110 */     this(EntityTypes.ARMOR_STAND, level);
/* 111 */     setPos(x, y, z);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 115 */     return createLivingAttributes()
/* 116 */       .add(Attributes.STEP_HEIGHT, 0.0D);
/*     */   }
/*     */   
/*     */   public void refreshDimensions() {
/* 121 */     double oldX = getX();
/* 122 */     double oldY = getY();
/* 123 */     double oldZ = getZ();
/* 124 */     super.refreshDimensions();
/* 125 */     setPos(oldX, oldY, oldZ);
/*     */   }
/*     */   
/*     */   private boolean hasPhysics() {
/* 129 */     return (!isMarker() && !isNoGravity());
/*     */   }
/*     */   
/*     */   public boolean isEffectiveAi() {
/* 134 */     return (super.isEffectiveAi() && hasPhysics());
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 139 */     super.defineSynchedData(entityData);
/* 140 */     entityData.define(DATA_CLIENT_FLAGS, Byte.valueOf((byte)0));
/* 141 */     entityData.define(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
/* 142 */     entityData.define(DATA_BODY_POSE, DEFAULT_BODY_POSE);
/* 143 */     entityData.define(DATA_LEFT_ARM_POSE, DEFAULT_LEFT_ARM_POSE);
/* 144 */     entityData.define(DATA_RIGHT_ARM_POSE, DEFAULT_RIGHT_ARM_POSE);
/* 145 */     entityData.define(DATA_LEFT_LEG_POSE, DEFAULT_LEFT_LEG_POSE);
/* 146 */     entityData.define(DATA_RIGHT_LEG_POSE, DEFAULT_RIGHT_LEG_POSE);
/*     */   }
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 151 */     return (slot != EquipmentSlot.BODY && slot != EquipmentSlot.SADDLE && !isDisabled(slot));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 156 */     super.addAdditionalSaveData(output);
/* 158 */     output.putBoolean("Invisible", isInvisible());
/* 159 */     output.putBoolean("Small", isSmall());
/* 161 */     output.putBoolean("ShowArms", showArms());
/* 163 */     output.putInt("DisabledSlots", this.disabledSlots);
/* 164 */     output.putBoolean("NoBasePlate", !showBasePlate());
/* 165 */     if (isMarker())
/* 166 */       output.putBoolean("Marker", isMarker()); 
/* 168 */     output.store("Pose", ArmorStandPose.CODEC, getArmorStandPose());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 173 */     super.readAdditionalSaveData(input);
/* 175 */     setInvisible(input.getBooleanOr("Invisible", false));
/* 177 */     setSmall(input.getBooleanOr("Small", false));
/* 179 */     setShowArms(input.getBooleanOr("ShowArms", false));
/* 181 */     this.disabledSlots = input.getIntOr("DisabledSlots", 0);
/* 182 */     setNoBasePlate(input.getBooleanOr("NoBasePlate", false));
/* 183 */     setMarker(input.getBooleanOr("Marker", false));
/* 184 */     this.noPhysics = !hasPhysics();
/* 185 */     input.read("Pose", ArmorStandPose.CODEC).ifPresent(this::setArmorStandPose);
/*     */   }
/*     */   
/*     */   public boolean isPushable() {
/* 191 */     return false;
/*     */   }
/*     */   
/*     */   protected void doPush(Entity entity) {}
/*     */   
/*     */   protected void pushEntities() {
/* 202 */     List<Entity> entities = level().getEntities((Entity)this, getBoundingBox(), RIDABLE_MINECARTS);
/* 203 */     for (Entity entity : entities) {
/* 204 */       if (distanceToSqr(entity) <= 0.2D)
/* 205 */         entity.push((Entity)this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/* 212 */     ItemStack itemStack = player.getItemInHand(hand);
/* 213 */     if (isMarker() || itemStack.is(Items.NAME_TAG))
/* 214 */       return super.interact(player, hand, location); 
/* 216 */     if (player.isSpectator())
/* 217 */       return (InteractionResult)InteractionResult.SUCCESS; 
/* 220 */     if (player.level().isClientSide())
/* 221 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER; 
/* 224 */     EquipmentSlot itemInHandSlot = getEquipmentSlotForItem(itemStack);
/* 225 */     if (itemStack.isEmpty()) {
/* 226 */       EquipmentSlot clickedSlot = getClickedSlot(location);
/* 227 */       EquipmentSlot targetSlot = isDisabled(clickedSlot) ? itemInHandSlot : clickedSlot;
/* 228 */       if (hasItemInSlot(targetSlot) && swapItem(player, targetSlot, itemStack, hand))
/* 229 */         return (InteractionResult)InteractionResult.SUCCESS_SERVER; 
/*     */     } else {
/* 232 */       if (isDisabled(itemInHandSlot))
/* 233 */         return (InteractionResult)InteractionResult.FAIL; 
/* 235 */       if (itemInHandSlot.getType() == EquipmentSlot.Type.HAND && !showArms())
/* 236 */         return (InteractionResult)InteractionResult.FAIL; 
/* 238 */       if (swapItem(player, itemInHandSlot, itemStack, hand))
/* 239 */         return (InteractionResult)InteractionResult.SUCCESS_SERVER; 
/*     */     } 
/* 242 */     return super.interact(player, hand, location);
/*     */   }
/*     */   
/*     */   private EquipmentSlot getClickedSlot(Vec3 location) {
/* 246 */     EquipmentSlot slotClicked = EquipmentSlot.MAINHAND;
/* 247 */     boolean small = isSmall();
/* 249 */     double clickYPosition = location.y / (getScale() * getAgeScale());
/* 250 */     EquipmentSlot feet = EquipmentSlot.FEET;
/* 251 */     if (clickYPosition >= 0.1D && clickYPosition < 0.1D + (small ? 0.8D : 0.45D) && hasItemInSlot(feet)) {
/* 252 */       slotClicked = EquipmentSlot.FEET;
/* 253 */     } else if (clickYPosition >= 0.9D + (small ? 0.3D : 0.0D) && clickYPosition < 0.9D + (small ? 1.0D : 0.7D) && hasItemInSlot(EquipmentSlot.CHEST)) {
/* 254 */       slotClicked = EquipmentSlot.CHEST;
/* 255 */     } else if (clickYPosition >= 0.4D && clickYPosition < 0.4D + (small ? 1.0D : 0.8D) && hasItemInSlot(EquipmentSlot.LEGS)) {
/* 256 */       slotClicked = EquipmentSlot.LEGS;
/* 257 */     } else if (clickYPosition >= 1.6D && hasItemInSlot(EquipmentSlot.HEAD)) {
/* 258 */       slotClicked = EquipmentSlot.HEAD;
/* 259 */     } else if (!hasItemInSlot(EquipmentSlot.MAINHAND) && hasItemInSlot(EquipmentSlot.OFFHAND)) {
/* 260 */       slotClicked = EquipmentSlot.OFFHAND;
/*     */     } 
/* 263 */     return slotClicked;
/*     */   }
/*     */   
/*     */   private boolean isDisabled(EquipmentSlot slot) {
/* 267 */     return ((this.disabledSlots & 1 << slot.getFilterBit(0)) != 0 || (slot.getType() == EquipmentSlot.Type.HAND && !showArms()));
/*     */   }
/*     */   
/*     */   private boolean swapItem(Player player, EquipmentSlot slot, ItemStack playerItemStack, InteractionHand hand) {
/* 271 */     ItemStack itemStack = getItemBySlot(slot);
/* 273 */     if (!itemStack.isEmpty() && (this.disabledSlots & 1 << slot.getFilterBit(8)) != 0)
/* 274 */       return false; 
/* 277 */     if (itemStack.isEmpty() && (this.disabledSlots & 1 << slot.getFilterBit(16)) != 0)
/* 278 */       return false; 
/* 281 */     if (player.hasInfiniteMaterials() && itemStack.isEmpty() && !playerItemStack.isEmpty()) {
/* 282 */       setItemSlot(slot, playerItemStack.copyWithCount(1));
/* 283 */       return true;
/*     */     } 
/* 286 */     if (!playerItemStack.isEmpty() && playerItemStack.getCount() > 1) {
/* 287 */       if (!itemStack.isEmpty())
/* 288 */         return false; 
/* 290 */       setItemSlot(slot, playerItemStack.split(1));
/* 291 */       return true;
/*     */     } 
/* 294 */     setItemSlot(slot, playerItemStack);
/* 295 */     player.setItemInHand(hand, itemStack);
/* 296 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 301 */     if (isRemoved())
/* 302 */       return false; 
/* 304 */     if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && source.getEntity() instanceof net.minecraft.world.entity.Mob)
/* 305 */       return false; 
/* 307 */     if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
/* 308 */       kill(level);
/* 309 */       return false;
/*     */     } 
/* 311 */     if (isInvulnerableTo(level, source) || this.invisible || isMarker())
/* 312 */       return false; 
/* 314 */     if (source.is(DamageTypeTags.IS_EXPLOSION)) {
/* 315 */       brokenByAnything(level, source);
/* 316 */       kill(level);
/* 317 */       return false;
/*     */     } 
/* 319 */     if (source.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
/* 320 */       if (isOnFire()) {
/* 321 */         causeDamage(level, source, 0.15F);
/*     */       } else {
/* 323 */         igniteForSeconds(5.0F);
/*     */       } 
/* 325 */       return false;
/*     */     } 
/* 327 */     if (source.is(DamageTypeTags.BURNS_ARMOR_STANDS) && getHealth() > 0.5F) {
/* 328 */       causeDamage(level, source, 4.0F);
/* 329 */       return false;
/*     */     } 
/* 332 */     boolean allowIncrementalBreaking = source.is(DamageTypeTags.CAN_BREAK_ARMOR_STAND);
/* 333 */     boolean shouldKill = source.is(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS);
/* 334 */     if (!allowIncrementalBreaking && !shouldKill)
/* 335 */       return false; 
/* 337 */     Entity entity = source.getEntity();
/* 337 */     if (entity instanceof Player) {
/* 337 */       Player player = (Player)entity;
/* 337 */       if (!(player.getAbilities()).mayBuild)
/* 338 */         return false; 
/*     */     } 
/* 340 */     if (source.isCreativePlayer()) {
/* 341 */       playBrokenSound();
/* 342 */       showBreakingParticles();
/* 343 */       kill(level);
/* 344 */       return true;
/*     */     } 
/* 347 */     long time = level.getGameTime();
/* 348 */     if (time - this.lastHit <= 5L || shouldKill) {
/* 349 */       brokenByPlayer(level, source);
/* 350 */       showBreakingParticles();
/* 351 */       kill(level);
/*     */     } else {
/* 353 */       level.broadcastEntityEvent((Entity)this, (byte)32);
/* 354 */       gameEvent((Holder)GameEvent.ENTITY_DAMAGE, source.getEntity());
/* 355 */       this.lastHit = time;
/*     */     } 
/* 357 */     return true;
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 362 */     if (id == 32) {
/* 363 */       if (level().isClientSide()) {
/* 364 */         level().playLocalSound(getX(), getY(), getZ(), SoundEvents.ARMOR_STAND_HIT, getSoundSource(), 0.3F, 1.0F, false);
/* 365 */         this.lastHit = level().getGameTime();
/*     */       } 
/*     */     } else {
/* 368 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 374 */     double size = getBoundingBox().getSize() * 4.0D;
/* 375 */     if (Double.isNaN(size) || size == 0.0D)
/* 376 */       size = 4.0D; 
/* 378 */     size *= 64.0D;
/* 379 */     return (distance < size * size);
/*     */   }
/*     */   
/*     */   private void showBreakingParticles() {
/* 383 */     if (level() instanceof ServerLevel)
/* 384 */       ((ServerLevel)level()).sendParticles((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState()), getX(), getY(0.6666666666666666D), getZ(), 10, (getBbWidth() / 4.0F), (getBbHeight() / 4.0F), (getBbWidth() / 4.0F), 0.05D); 
/*     */   }
/*     */   
/*     */   private void causeDamage(ServerLevel level, DamageSource source, float dmg) {
/* 389 */     float health = getHealth();
/* 390 */     health -= dmg;
/* 391 */     if (health <= 0.5F) {
/* 392 */       brokenByAnything(level, source);
/* 393 */       kill(level);
/*     */     } else {
/* 395 */       setHealth(health);
/* 396 */       gameEvent((Holder)GameEvent.ENTITY_DAMAGE, source.getEntity());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void brokenByPlayer(ServerLevel level, DamageSource source) {
/* 401 */     ItemStack result = new ItemStack((ItemLike)Items.ARMOR_STAND);
/* 402 */     result.set(DataComponents.CUSTOM_NAME, getCustomName());
/* 403 */     Block.popResource(level(), blockPosition(), result);
/* 404 */     brokenByAnything(level, source);
/*     */   }
/*     */   
/*     */   private void brokenByAnything(ServerLevel level, DamageSource source) {
/* 408 */     playBrokenSound();
/* 409 */     dropAllDeathLoot(level, source);
/* 411 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 412 */       ItemStack itemStack = this.equipment.set(slot, ItemStack.EMPTY);
/* 413 */       if (!itemStack.isEmpty() && 
/* 414 */         !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP))
/* 415 */         Block.popResource(level(), blockPosition().above(), itemStack); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playBrokenSound() {
/* 422 */     level().playSound(null, getX(), getY(), getZ(), SoundEvents.ARMOR_STAND_BREAK, getSoundSource(), 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void tickHeadTurn(float yBodyRotT) {
/* 427 */     this.yBodyRotO = this.yRotO;
/* 428 */     this.yBodyRot = getYRot();
/*     */   }
/*     */   
/*     */   public void travel(Vec3 input) {
/* 433 */     if (!hasPhysics())
/*     */       return; 
/* 436 */     super.travel(input);
/*     */   }
/*     */   
/*     */   public void setYBodyRot(float yBodyRot) {
/* 441 */     this.yBodyRotO = this.yRotO = yBodyRot;
/* 442 */     this.yHeadRotO = this.yHeadRot = yBodyRot;
/*     */   }
/*     */   
/*     */   public void setYHeadRot(float yHeadRot) {
/* 447 */     this.yBodyRotO = this.yRotO = yHeadRot;
/* 448 */     this.yHeadRotO = this.yHeadRot = yHeadRot;
/*     */   }
/*     */   
/*     */   protected void updateInvisibilityStatus() {
/* 453 */     setInvisible(this.invisible);
/*     */   }
/*     */   
/*     */   public void setInvisible(boolean invisible) {
/* 458 */     this.invisible = invisible;
/* 459 */     super.setInvisible(invisible);
/*     */   }
/*     */   
/*     */   public boolean isBaby() {
/* 464 */     return isSmall();
/*     */   }
/*     */   
/*     */   public void kill(ServerLevel level) {
/* 470 */     remove(Entity.RemovalReason.KILLED);
/* 471 */     gameEvent((Holder)GameEvent.ENTITY_DIE);
/*     */   }
/*     */   
/*     */   public boolean ignoreExplosion(Explosion explosion) {
/* 476 */     if (explosion.shouldAffectBlocklikeEntities())
/* 477 */       return isInvisible(); 
/* 479 */     return true;
/*     */   }
/*     */   
/*     */   public PushReaction getPistonPushReaction() {
/* 484 */     if (isMarker())
/* 485 */       return PushReaction.IGNORE; 
/* 487 */     return super.getPistonPushReaction();
/*     */   }
/*     */   
/*     */   public boolean isIgnoringBlockTriggers() {
/* 492 */     return isMarker();
/*     */   }
/*     */   
/*     */   private void setSmall(boolean value) {
/* 496 */     this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 1, value)));
/*     */   }
/*     */   
/*     */   public boolean isSmall() {
/* 500 */     return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setShowArms(boolean value) {
/* 504 */     this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 4, value)));
/*     */   }
/*     */   
/*     */   public boolean showArms() {
/* 508 */     return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x4) != 0);
/*     */   }
/*     */   
/*     */   public void setNoBasePlate(boolean value) {
/* 512 */     this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 8, value)));
/*     */   }
/*     */   
/*     */   public boolean showBasePlate() {
/* 516 */     return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x8) == 0);
/*     */   }
/*     */   
/*     */   private void setMarker(boolean value) {
/* 520 */     this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 16, value)));
/*     */   }
/*     */   
/*     */   public boolean isMarker() {
/* 524 */     return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x10) != 0);
/*     */   }
/*     */   
/*     */   private byte setBit(byte data, int bit, boolean value) {
/* 528 */     if (value) {
/* 529 */       data = (byte)(data | bit);
/*     */     } else {
/* 531 */       data = (byte)(data & (bit ^ 0xFFFFFFFF));
/*     */     } 
/* 533 */     return data;
/*     */   }
/*     */   
/*     */   public void setHeadPose(Rotations headPose) {
/* 537 */     this.entityData.set(DATA_HEAD_POSE, headPose);
/*     */   }
/*     */   
/*     */   public void setBodyPose(Rotations bodyPose) {
/* 541 */     this.entityData.set(DATA_BODY_POSE, bodyPose);
/*     */   }
/*     */   
/*     */   public void setLeftArmPose(Rotations leftArmPose) {
/* 545 */     this.entityData.set(DATA_LEFT_ARM_POSE, leftArmPose);
/*     */   }
/*     */   
/*     */   public void setRightArmPose(Rotations rightArmPose) {
/* 549 */     this.entityData.set(DATA_RIGHT_ARM_POSE, rightArmPose);
/*     */   }
/*     */   
/*     */   public void setLeftLegPose(Rotations leftLegPose) {
/* 553 */     this.entityData.set(DATA_LEFT_LEG_POSE, leftLegPose);
/*     */   }
/*     */   
/*     */   public void setRightLegPose(Rotations rightLegPose) {
/* 557 */     this.entityData.set(DATA_RIGHT_LEG_POSE, rightLegPose);
/*     */   }
/*     */   
/*     */   public Rotations getHeadPose() {
/* 561 */     return (Rotations)this.entityData.get(DATA_HEAD_POSE);
/*     */   }
/*     */   
/*     */   public Rotations getBodyPose() {
/* 565 */     return (Rotations)this.entityData.get(DATA_BODY_POSE);
/*     */   }
/*     */   
/*     */   public Rotations getLeftArmPose() {
/* 569 */     return (Rotations)this.entityData.get(DATA_LEFT_ARM_POSE);
/*     */   }
/*     */   
/*     */   public Rotations getRightArmPose() {
/* 573 */     return (Rotations)this.entityData.get(DATA_RIGHT_ARM_POSE);
/*     */   }
/*     */   
/*     */   public Rotations getLeftLegPose() {
/* 577 */     return (Rotations)this.entityData.get(DATA_LEFT_LEG_POSE);
/*     */   }
/*     */   
/*     */   public Rotations getRightLegPose() {
/* 581 */     return (Rotations)this.entityData.get(DATA_RIGHT_LEG_POSE);
/*     */   }
/*     */   
/*     */   public boolean isPickable() {
/* 586 */     return (super.isPickable() && !isMarker());
/*     */   }
/*     */   
/*     */   public boolean skipAttackInteraction(Entity source) {
/* 591 */     if (source instanceof Player) {
/* 591 */       Player playerSource = (Player)source;
/* 591 */       if (!level().mayInteract((Entity)playerSource, blockPosition()));
/*     */     } 
/* 591 */     return false;
/*     */   }
/*     */   
/*     */   public HumanoidArm getMainArm() {
/* 596 */     return HumanoidArm.RIGHT;
/*     */   }
/*     */   
/*     */   public LivingEntity.Fallsounds getFallSounds() {
/* 601 */     return new LivingEntity.Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL);
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 606 */     return SoundEvents.ARMOR_STAND_HIT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 611 */     return SoundEvents.ARMOR_STAND_BREAK;
/*     */   }
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {}
/*     */   
/*     */   public boolean isAffectedByPotions() {
/* 620 */     return false;
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 625 */     if (DATA_CLIENT_FLAGS.equals(accessor)) {
/* 626 */       refreshDimensions();
/* 627 */       this.blocksBuilding = !isMarker();
/*     */     } 
/* 629 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   public boolean attackable() {
/* 634 */     return false;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 639 */     return getDimensionsMarker(isMarker());
/*     */   }
/*     */   
/*     */   private EntityDimensions getDimensionsMarker(boolean isMarker) {
/* 643 */     if (isMarker)
/* 644 */       return MARKER_DIMENSIONS; 
/* 646 */     return isBaby() ? BABY_DIMENSIONS : getType().getDimensions();
/*     */   }
/*     */   
/*     */   public Vec3 getLightProbePosition(float partialTickTime) {
/* 651 */     if (isMarker()) {
/* 652 */       AABB box = getDimensionsMarker(false).makeBoundingBox(position());
/* 654 */       BlockPos probePos = blockPosition();
/* 655 */       int brightestLight = Integer.MIN_VALUE;
/* 656 */       for (BlockPos pos : BlockPos.betweenClosed(BlockPos.containing(box.minX, box.minY, box.minZ), BlockPos.containing(box.maxX, box.maxY, box.maxZ))) {
/* 657 */         int blockBrightness = Math.max(level().getBrightness(LightLayer.BLOCK, pos), level().getBrightness(LightLayer.SKY, pos));
/* 658 */         if (blockBrightness == 15)
/* 659 */           return Vec3.atCenterOf((Vec3i)pos); 
/* 662 */         if (blockBrightness > brightestLight) {
/* 663 */           brightestLight = blockBrightness;
/* 664 */           probePos = pos.immutable();
/*     */         } 
/*     */       } 
/* 668 */       return Vec3.atCenterOf((Vec3i)probePos);
/*     */     } 
/* 671 */     return super.getLightProbePosition(partialTickTime);
/*     */   }
/*     */   
/*     */   public ItemStack getPickResult() {
/* 676 */     return new ItemStack((ItemLike)Items.ARMOR_STAND);
/*     */   }
/*     */   
/*     */   public boolean canBeSeenByAnyone() {
/* 681 */     return (!isInvisible() && !isMarker());
/*     */   }
/*     */   
/*     */   public void setArmorStandPose(ArmorStandPose pose) {
/* 685 */     setHeadPose(pose.head());
/* 686 */     setBodyPose(pose.body());
/* 687 */     setLeftArmPose(pose.leftArm());
/* 688 */     setRightArmPose(pose.rightArm());
/* 689 */     setLeftLegPose(pose.leftLeg());
/* 690 */     setRightLegPose(pose.rightLeg());
/*     */   }
/*     */   
/*     */   public ArmorStandPose getArmorStandPose() {
/* 694 */     return new ArmorStandPose(
/* 695 */         getHeadPose(), 
/* 696 */         getBodyPose(), 
/* 697 */         getLeftArmPose(), 
/* 698 */         getRightArmPose(), 
/* 699 */         getLeftLegPose(), 
/* 700 */         getRightLegPose());
/*     */   }
/*     */   
/*     */   public static final class ArmorStandPose extends Record {
/*     */     private final Rotations head;
/*     */     
/*     */     private final Rotations body;
/*     */     
/*     */     private final Rotations leftArm;
/*     */     
/*     */     private final Rotations rightArm;
/*     */     
/*     */     private final Rotations leftLeg;
/*     */     
/*     */     private final Rotations rightLeg;
/*     */     
/*     */     public ArmorStandPose(Rotations head, Rotations body, Rotations leftArm, Rotations rightArm, Rotations leftLeg, Rotations rightLeg) {
/* 704 */       this.head = head;
/* 704 */       this.body = body;
/* 704 */       this.leftArm = leftArm;
/* 704 */       this.rightArm = rightArm;
/* 704 */       this.leftLeg = leftLeg;
/* 704 */       this.rightLeg = rightLeg;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #704	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #704	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #704	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/entity/decoration/ArmorStand$ArmorStandPose;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Rotations head() {
/* 704 */       return this.head;
/*     */     }
/*     */     
/*     */     public Rotations body() {
/* 704 */       return this.body;
/*     */     }
/*     */     
/*     */     public Rotations leftArm() {
/* 704 */       return this.leftArm;
/*     */     }
/*     */     
/*     */     public Rotations rightArm() {
/* 704 */       return this.rightArm;
/*     */     }
/*     */     
/*     */     public Rotations leftLeg() {
/* 704 */       return this.leftLeg;
/*     */     }
/*     */     
/*     */     public Rotations rightLeg() {
/* 704 */       return this.rightLeg;
/*     */     }
/*     */     
/* 712 */     public static final ArmorStandPose DEFAULT = new ArmorStandPose(ArmorStand.DEFAULT_HEAD_POSE, ArmorStand.DEFAULT_BODY_POSE, ArmorStand.DEFAULT_LEFT_ARM_POSE, ArmorStand.DEFAULT_RIGHT_ARM_POSE, ArmorStand.DEFAULT_LEFT_LEG_POSE, ArmorStand.DEFAULT_RIGHT_LEG_POSE);
/*     */     
/*     */     public static final Codec<ArmorStandPose> CODEC;
/*     */     
/*     */     static {
/* 721 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Rotations.CODEC.optionalFieldOf("Head", ArmorStand.DEFAULT_HEAD_POSE).forGetter(ArmorStandPose::head), (App)Rotations.CODEC.optionalFieldOf("Body", ArmorStand.DEFAULT_BODY_POSE).forGetter(ArmorStandPose::body), (App)Rotations.CODEC.optionalFieldOf("LeftArm", ArmorStand.DEFAULT_LEFT_ARM_POSE).forGetter(ArmorStandPose::leftArm), (App)Rotations.CODEC.optionalFieldOf("RightArm", ArmorStand.DEFAULT_RIGHT_ARM_POSE).forGetter(ArmorStandPose::rightArm), (App)Rotations.CODEC.optionalFieldOf("LeftLeg", ArmorStand.DEFAULT_LEFT_LEG_POSE).forGetter(ArmorStandPose::leftLeg), (App)Rotations.CODEC.optionalFieldOf("RightLeg", ArmorStand.DEFAULT_RIGHT_LEG_POSE).forGetter(ArmorStandPose::rightLeg)).apply((Applicative)i, ArmorStandPose::new));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\decoration\ArmorStand.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */