/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.InterpolationHandler;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FishingHook extends Projectile {
/*  56 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  58 */   private final RandomSource syncronizedRandom = RandomSource.create();
/*     */   
/*     */   private boolean biting;
/*     */   
/*     */   private int outOfWaterTime;
/*     */   
/*     */   private static final int MAX_OUT_OF_WATER_TIME = 10;
/*     */   
/*     */   private enum FishHookState {
/*  62 */     FLYING, HOOKED_IN_ENTITY, BOBBING;
/*     */   }
/*     */   
/*  66 */   private static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY = SynchedEntityData.defineId(FishingHook.class, EntityDataSerializers.INT);
/*     */   
/*  67 */   private static final EntityDataAccessor<Boolean> DATA_BITING = SynchedEntityData.defineId(FishingHook.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private int life;
/*     */   
/*     */   private int nibble;
/*     */   
/*     */   private int timeUntilLured;
/*     */   
/*     */   private int timeUntilHooked;
/*     */   
/*     */   private float fishAngle;
/*     */   
/*     */   private boolean openWater = true;
/*     */   
/*     */   private Entity hookedIn;
/*     */   
/*  77 */   private FishHookState currentState = FishHookState.FLYING;
/*     */   
/*     */   private final int luck;
/*     */   
/*     */   private final int lureSpeed;
/*     */   
/*  81 */   private final InterpolationHandler interpolationHandler = new InterpolationHandler(this);
/*     */   
/*     */   private FishingHook(EntityType<? extends FishingHook> type, Level level, int luck, int lureSpeed) {
/*  84 */     super((EntityType)type, level);
/*  85 */     this.luck = Math.max(0, luck);
/*  86 */     this.lureSpeed = Math.max(0, lureSpeed);
/*     */   }
/*     */   
/*     */   public FishingHook(EntityType<? extends FishingHook> type, Level level) {
/*  90 */     this(type, level, 0, 0);
/*     */   }
/*     */   
/*     */   public FishingHook(Player player, Level level, int luck, int lureSpeed) {
/*  94 */     this(EntityTypes.FISHING_BOBBER, level, luck, lureSpeed);
/*  95 */     setOwner((Entity)player);
/*  96 */     float xRot1 = player.getXRot();
/*  97 */     float yRot1 = player.getYRot();
/*  99 */     float yCos = Mth.cos((-yRot1 * 0.017453292F - 3.1415927F));
/* 100 */     float ySin = Mth.sin((-yRot1 * 0.017453292F - 3.1415927F));
/* 101 */     float xCos = -Mth.cos((-xRot1 * 0.017453292F));
/* 102 */     float xSin = Mth.sin((-xRot1 * 0.017453292F));
/* 104 */     double x1 = player.getX() - ySin * 0.3D;
/* 105 */     double y1 = player.getEyeY();
/* 106 */     double z1 = player.getZ() - yCos * 0.3D;
/* 108 */     snapTo(x1, y1, z1, yRot1, xRot1);
/* 113 */     Vec3 newMovement = new Vec3(-ySin, Mth.clamp(-(xSin / xCos), -5.0F, 5.0F), -yCos);
/* 117 */     double dist = newMovement.length();
/* 118 */     newMovement = newMovement.multiply(0.6D / dist + this.random
/* 119 */         .triangle(0.5D, 0.0103365D), 0.6D / dist + this.random
/* 120 */         .triangle(0.5D, 0.0103365D), 0.6D / dist + this.random
/* 121 */         .triangle(0.5D, 0.0103365D));
/* 123 */     setDeltaMovement(newMovement);
/* 125 */     setYRot((float)(Mth.atan2(newMovement.x, newMovement.z) * 57.2957763671875D));
/* 126 */     setXRot((float)(Mth.atan2(newMovement.y, newMovement.horizontalDistance()) * 57.2957763671875D));
/* 127 */     this.yRotO = getYRot();
/* 128 */     this.xRotO = getXRot();
/*     */   }
/*     */   
/*     */   public InterpolationHandler getInterpolation() {
/* 133 */     return this.interpolationHandler;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 138 */     entityData.define(DATA_HOOKED_ENTITY, Integer.valueOf(0));
/* 139 */     entityData.define(DATA_BITING, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected boolean shouldBounceOnWorldBorder() {
/* 144 */     return true;
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 149 */     if (DATA_HOOKED_ENTITY.equals(accessor)) {
/* 150 */       int id = ((Integer)getEntityData().get(DATA_HOOKED_ENTITY)).intValue();
/* 151 */       this.hookedIn = (id > 0) ? level().getEntity(id - 1) : null;
/*     */     } 
/* 154 */     if (DATA_BITING.equals(accessor)) {
/* 155 */       this.biting = ((Boolean)getEntityData().get(DATA_BITING)).booleanValue();
/* 156 */       if (this.biting)
/* 157 */         setDeltaMovement((getDeltaMovement()).x, (-0.4F * Mth.nextFloat(this.syncronizedRandom, 0.6F, 1.0F)), (getDeltaMovement()).z); 
/*     */     } 
/* 160 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 165 */     double size = 64.0D;
/* 166 */     return (distance < 4096.0D);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 172 */     this.syncronizedRandom.setSeed(getUUID().getLeastSignificantBits() ^ level().getGameTime());
/* 174 */     getInterpolation().interpolate();
/* 175 */     super.tick();
/* 177 */     Player owner = getPlayerOwner();
/* 178 */     if (owner == null) {
/* 179 */       discard();
/*     */       return;
/*     */     } 
/* 182 */     if (!level().isClientSide() && 
/* 183 */       shouldStopFishing(owner))
/*     */       return; 
/* 187 */     if (onGround()) {
/* 188 */       this.life++;
/* 189 */       if (this.life >= 1200) {
/* 190 */         discard();
/*     */         return;
/*     */       } 
/*     */     } else {
/* 194 */       this.life = 0;
/*     */     } 
/* 197 */     float liquidHeight = 0.0F;
/* 198 */     BlockPos blockPos = blockPosition();
/* 200 */     FluidState fluidState = level().getFluidState(blockPos);
/* 201 */     if (fluidState.is(FluidTags.WATER))
/* 202 */       liquidHeight = fluidState.getHeight((BlockGetter)level(), blockPos); 
/* 205 */     boolean isInWater = (liquidHeight > 0.0F);
/* 206 */     if (this.currentState == FishHookState.FLYING) {
/* 207 */       if (this.hookedIn != null) {
/* 208 */         setDeltaMovement(Vec3.ZERO);
/* 210 */         this.currentState = FishHookState.HOOKED_IN_ENTITY;
/*     */         return;
/*     */       } 
/* 214 */       if (isInWater) {
/* 215 */         setDeltaMovement(getDeltaMovement().multiply(0.3D, 0.2D, 0.3D));
/* 217 */         this.currentState = FishHookState.BOBBING;
/*     */         return;
/*     */       } 
/* 221 */       checkCollision();
/*     */     } else {
/* 222 */       if (this.currentState == FishHookState.HOOKED_IN_ENTITY) {
/* 223 */         if (this.hookedIn != null)
/* 224 */           if (this.hookedIn.isRemoved() || !this.hookedIn.canInteractWithLevel() || this.hookedIn.level().dimension() != level().dimension()) {
/* 225 */             setHookedEntity((Entity)null);
/* 226 */             this.currentState = FishHookState.FLYING;
/*     */           } else {
/* 228 */             setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8D), this.hookedIn.getZ());
/*     */           }  
/*     */         return;
/*     */       } 
/* 232 */       if (this.currentState == FishHookState.BOBBING) {
/* 233 */         Vec3 movement = getDeltaMovement();
/* 234 */         double force = getY() + movement.y - blockPos.getY() - liquidHeight;
/* 235 */         if (Math.abs(force) < 0.01D)
/* 236 */           force += Math.signum(force) * 0.1D; 
/* 239 */         setDeltaMovement(movement.x * 0.9D, movement.y - force * this.random
/*     */             
/* 242 */             .nextFloat() * 0.2D, movement.z * 0.9D);
/* 245 */         if (this.nibble > 0 || this.timeUntilHooked > 0) {
/* 246 */           this.openWater = (this.openWater && this.outOfWaterTime < 10 && calculateOpenWater(blockPos));
/*     */         } else {
/* 248 */           this.openWater = true;
/*     */         } 
/* 251 */         if (isInWater) {
/* 252 */           this.outOfWaterTime = Math.max(0, this.outOfWaterTime - 1);
/* 253 */           if (this.biting)
/* 254 */             setDeltaMovement(getDeltaMovement().add(0.0D, -0.1D * this.syncronizedRandom.nextFloat() * this.syncronizedRandom.nextFloat(), 0.0D)); 
/* 256 */           if (!level().isClientSide())
/* 257 */             catchingFish(blockPos); 
/*     */         } else {
/* 260 */           this.outOfWaterTime = Math.min(10, this.outOfWaterTime + 1);
/*     */         } 
/*     */       } 
/*     */     } 
/* 264 */     if (!fluidState.is(FluidTags.WATER) && !onGround() && this.hookedIn == null)
/* 265 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.03D, 0.0D)); 
/* 268 */     move(MoverType.SELF, getDeltaMovement());
/* 269 */     applyEffectsFromBlocks();
/* 270 */     updateRotation();
/* 272 */     if (this.currentState == FishHookState.FLYING && (
/* 273 */       onGround() || this.horizontalCollision))
/* 274 */       setDeltaMovement(Vec3.ZERO); 
/* 278 */     double inertia = 0.92D;
/* 279 */     setDeltaMovement(getDeltaMovement().scale(0.92D));
/* 281 */     reapplyPosition();
/*     */   }
/*     */   
/*     */   private boolean shouldStopFishing(Player owner) {
/* 285 */     if (owner.canInteractWithLevel()) {
/* 286 */       ItemStack selectedItem = owner.getMainHandItem();
/* 287 */       ItemStack selectedItemOffHand = owner.getOffhandItem();
/* 288 */       boolean mainHandIsFishing = selectedItem.is(Items.FISHING_ROD);
/* 289 */       boolean offHandIsFishing = selectedItemOffHand.is(Items.FISHING_ROD);
/* 291 */       if ((mainHandIsFishing || offHandIsFishing) && distanceToSqr((Entity)owner) <= 1024.0D)
/* 292 */         return false; 
/*     */     } 
/* 295 */     discard();
/* 296 */     return true;
/*     */   }
/*     */   
/*     */   private void checkCollision() {
/* 300 */     HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
/* 301 */     hitTargetOrDeflectSelf(hitResult);
/*     */   }
/*     */   
/*     */   protected boolean canHitEntity(Entity entity) {
/* 306 */     return (super.canHitEntity(entity) || (entity.isAlive() && entity instanceof ItemEntity));
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/* 311 */     super.onHitEntity(hitResult);
/* 312 */     if (!level().isClientSide())
/* 313 */       setHookedEntity(hitResult.getEntity()); 
/*     */   }
/*     */   
/*     */   protected void onHitBlock(BlockHitResult hitResult) {
/* 319 */     super.onHitBlock(hitResult);
/* 320 */     setDeltaMovement(getDeltaMovement().normalize().scale(hitResult.distanceTo(this)));
/*     */   }
/*     */   
/*     */   private void setHookedEntity(Entity hookedIn) {
/* 324 */     this.hookedIn = hookedIn;
/* 325 */     getEntityData().set(DATA_HOOKED_ENTITY, Integer.valueOf((hookedIn == null) ? 0 : (hookedIn.getId() + 1)));
/*     */   }
/*     */   
/*     */   private void catchingFish(BlockPos blockPos) {
/* 329 */     ServerLevel serverLevel = (ServerLevel)level();
/* 331 */     int fishingSpeed = 1;
/* 332 */     BlockPos above = blockPos.above();
/* 333 */     if (this.random.nextFloat() < 0.25F && level().isRainingAt(above))
/* 334 */       fishingSpeed++; 
/* 336 */     if (this.random.nextFloat() < 0.5F && !level().canSeeSky(above))
/* 337 */       fishingSpeed--; 
/* 340 */     if (this.nibble > 0) {
/* 341 */       this.nibble--;
/* 343 */       if (this.nibble <= 0) {
/* 344 */         this.timeUntilLured = 0;
/* 345 */         this.timeUntilHooked = 0;
/* 346 */         getEntityData().set(DATA_BITING, Boolean.valueOf(false));
/*     */       } 
/* 348 */     } else if (this.timeUntilHooked > 0) {
/* 349 */       this.timeUntilHooked -= fishingSpeed;
/* 351 */       if (this.timeUntilHooked > 0) {
/* 352 */         this.fishAngle += (float)this.random.triangle(0.0D, 9.188D);
/* 354 */         float angle = this.fishAngle * 0.017453292F;
/* 355 */         float angleSin = Mth.sin(angle);
/* 356 */         float angleCos = Mth.cos(angle);
/* 357 */         double fishX = getX() + (angleSin * this.timeUntilHooked * 0.1F);
/* 358 */         double fishY = (Mth.floor(getY()) + 1.0F);
/* 359 */         double fishZ = getZ() + (angleCos * this.timeUntilHooked * 0.1F);
/* 361 */         BlockState splashBlockState = serverLevel.getBlockState(BlockPos.containing(fishX, fishY - 1.0D, fishZ));
/* 362 */         if (splashBlockState.is(Blocks.WATER)) {
/* 363 */           if (this.random.nextFloat() < 0.15F)
/* 364 */             serverLevel.sendParticles((ParticleOptions)ParticleTypes.BUBBLE, fishX, fishY - 0.10000000149011612D, fishZ, 1, angleSin, 0.1D, angleCos, 0.0D); 
/* 367 */           float particleXMovement = angleSin * 0.04F;
/* 368 */           float particleZMovement = angleCos * 0.04F;
/* 370 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.FISHING, fishX, fishY, fishZ, 0, particleZMovement, 0.01D, -particleXMovement, 1.0D);
/* 371 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.FISHING, fishX, fishY, fishZ, 0, -particleZMovement, 0.01D, particleXMovement, 1.0D);
/*     */         } 
/*     */       } else {
/* 374 */         playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/* 375 */         double y = getY() + 0.5D;
/* 376 */         serverLevel.sendParticles((ParticleOptions)ParticleTypes.BUBBLE, getX(), y, getZ(), (int)(1.0F + getBbWidth() * 20.0F), getBbWidth(), 0.0D, getBbWidth(), 0.20000000298023224D);
/* 377 */         serverLevel.sendParticles((ParticleOptions)ParticleTypes.FISHING, getX(), y, getZ(), (int)(1.0F + getBbWidth() * 20.0F), getBbWidth(), 0.0D, getBbWidth(), 0.20000000298023224D);
/* 379 */         this.nibble = Mth.nextInt(this.random, 20, 40);
/* 380 */         getEntityData().set(DATA_BITING, Boolean.valueOf(true));
/*     */       } 
/* 382 */     } else if (this.timeUntilLured > 0) {
/* 383 */       this.timeUntilLured -= fishingSpeed;
/* 385 */       float teaseChance = 0.15F;
/* 386 */       if (this.timeUntilLured < 20) {
/* 387 */         teaseChance += (20 - this.timeUntilLured) * 0.05F;
/* 388 */       } else if (this.timeUntilLured < 40) {
/* 389 */         teaseChance += (40 - this.timeUntilLured) * 0.02F;
/* 390 */       } else if (this.timeUntilLured < 60) {
/* 391 */         teaseChance += (60 - this.timeUntilLured) * 0.01F;
/*     */       } 
/* 394 */       if (this.random.nextFloat() < teaseChance) {
/* 395 */         float angle = Mth.nextFloat(this.random, 0.0F, 360.0F) * 0.017453292F;
/* 396 */         float dist = Mth.nextFloat(this.random, 25.0F, 60.0F);
/* 397 */         double fishX = getX() + (Mth.sin(angle) * dist) * 0.1D;
/* 398 */         double fishY = (Mth.floor(getY()) + 1.0F);
/* 399 */         double fishZ = getZ() + (Mth.cos(angle) * dist) * 0.1D;
/* 400 */         BlockState splashBlockState = serverLevel.getBlockState(BlockPos.containing(fishX, fishY - 1.0D, fishZ));
/* 401 */         if (splashBlockState.is(Blocks.WATER))
/* 402 */           serverLevel.sendParticles((ParticleOptions)ParticleTypes.SPLASH, fishX, fishY, fishZ, 2 + this.random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D); 
/*     */       } 
/* 406 */       if (this.timeUntilLured <= 0) {
/* 407 */         this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
/* 408 */         this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
/*     */       } 
/*     */     } else {
/* 411 */       this.timeUntilLured = Mth.nextInt(this.random, 100, 600);
/* 412 */       this.timeUntilLured -= this.lureSpeed;
/*     */     } 
/*     */   }
/*     */   
/*     */   private enum OpenWaterType {
/* 417 */     ABOVE_WATER, INSIDE_WATER, INVALID;
/*     */   }
/*     */   
/*     */   private boolean calculateOpenWater(BlockPos blockPos) {
/* 421 */     OpenWaterType previousLayer = OpenWaterType.INVALID;
/* 422 */     for (int y = -1; y <= 2; y++) {
/* 423 */       OpenWaterType layer = getOpenWaterTypeForArea(blockPos.offset(-2, y, -2), blockPos.offset(2, y, 2));
/* 424 */       switch (layer.ordinal()) {
/*     */         case 2:
/* 426 */           return false;
/*     */         case 0:
/* 429 */           if (previousLayer == OpenWaterType.INVALID)
/* 430 */             return false; 
/*     */           break;
/*     */         case 1:
/* 434 */           if (previousLayer == OpenWaterType.ABOVE_WATER)
/* 435 */             return false; 
/*     */           break;
/*     */       } 
/* 439 */       previousLayer = layer;
/*     */     } 
/* 441 */     return true;
/*     */   }
/*     */   
/*     */   private OpenWaterType getOpenWaterTypeForArea(BlockPos from, BlockPos to) {
/* 445 */     return BlockPos.betweenClosedStream(from, to).map(this::getOpenWaterTypeForBlock).reduce((a, b) -> (a == b) ? a : OpenWaterType.INVALID).orElse(OpenWaterType.INVALID);
/*     */   }
/*     */   
/*     */   private OpenWaterType getOpenWaterTypeForBlock(BlockPos pos) {
/* 449 */     BlockState state = level().getBlockState(pos);
/* 450 */     if (state.isAir() || state.is(Blocks.LILY_PAD))
/* 451 */       return OpenWaterType.ABOVE_WATER; 
/* 453 */     FluidState fluidState = state.getFluidState();
/* 454 */     if (fluidState.is(FluidTags.WATER) && fluidState.isSource() && state.getCollisionShape((BlockGetter)level(), pos).isEmpty())
/* 455 */       return OpenWaterType.INSIDE_WATER; 
/* 457 */     return OpenWaterType.INVALID;
/*     */   }
/*     */   
/*     */   public boolean isOpenWaterFishing() {
/* 461 */     return this.openWater;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {}
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {}
/*     */   
/*     */   public int retrieve(ItemStack rod) {
/* 473 */     Player owner = getPlayerOwner();
/* 474 */     if (level().isClientSide() || owner == null || shouldStopFishing(owner))
/* 475 */       return 0; 
/* 478 */     int dmg = 0;
/* 479 */     if (this.hookedIn != null) {
/* 480 */       pullEntity(this.hookedIn);
/* 481 */       CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)owner, rod, this, Collections.emptyList());
/* 482 */       level().broadcastEntityEvent(this, (byte)31);
/* 483 */       dmg = (this.hookedIn instanceof ItemEntity) ? 3 : 5;
/* 484 */     } else if (this.nibble > 0) {
/* 490 */       LootParams params = (new LootParams.Builder((ServerLevel)level())).withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.TOOL, rod).withParameter(LootContextParams.THIS_ENTITY, this).withLuck(this.luck + owner.getLuck()).create(LootContextParamSets.FISHING);
/* 491 */       LootTable lootTable = level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING);
/* 492 */       ObjectArrayList objectArrayList = lootTable.getRandomItems(params);
/* 493 */       CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)owner, rod, this, (Collection)objectArrayList);
/* 494 */       for (ItemStack itemStack : objectArrayList) {
/* 495 */         ItemEntity entity = new ItemEntity(level(), getX(), getY(), getZ(), itemStack);
/* 496 */         double xa = owner.getX() - getX();
/* 497 */         double ya = owner.getY() - getY();
/* 498 */         double za = owner.getZ() - getZ();
/* 500 */         double speed = 0.1D;
/* 501 */         entity.setDeltaMovement(xa * 0.1D, ya * 0.1D + 
/*     */             
/* 503 */             Math.sqrt(Math.sqrt(xa * xa + ya * ya + za * za)) * 0.08D, za * 0.1D);
/* 506 */         level().addFreshEntity((Entity)entity);
/* 507 */         owner.level().addFreshEntity((Entity)new ExperienceOrb(owner.level(), owner.getX(), owner.getY() + 0.5D, owner.getZ() + 0.5D, this.random.nextInt(6) + 1));
/* 509 */         if (itemStack.is(ItemTags.FISHES))
/* 510 */           owner.awardStat(Stats.FISH_CAUGHT, 1); 
/*     */       } 
/* 513 */       dmg = 1;
/*     */     } 
/* 515 */     if (onGround())
/* 516 */       dmg = 2; 
/* 519 */     discard();
/* 520 */     return dmg;
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 525 */     if (id == 31 && 
/* 526 */       level().isClientSide()) {
/* 526 */       Entity entity = this.hookedIn;
/* 526 */       if (entity instanceof Player) {
/* 526 */         Player player = (Player)entity;
/* 526 */         if (player.isLocalPlayer())
/* 527 */           pullEntity(this.hookedIn); 
/*     */       } 
/*     */     } 
/* 531 */     super.handleEntityEvent(id);
/*     */   }
/*     */   
/*     */   protected void pullEntity(Entity entity) {
/* 535 */     Entity owner = getOwner();
/* 536 */     if (owner == null)
/*     */       return; 
/* 544 */     Vec3 delta = (new Vec3(owner.getX() - getX(), owner.getY() - getY(), owner.getZ() - getZ())).scale(0.1D);
/* 546 */     entity.setDeltaMovement(entity.getDeltaMovement().add(delta));
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 551 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */   
/*     */   public void remove(Entity.RemovalReason reason) {
/* 556 */     updateOwnerInfo((FishingHook)null);
/* 557 */     super.remove(reason);
/*     */   }
/*     */   
/*     */   public void onClientRemoval() {
/* 562 */     updateOwnerInfo((FishingHook)null);
/*     */   }
/*     */   
/*     */   public void setOwner(Entity owner) {
/* 567 */     super.setOwner(owner);
/* 568 */     updateOwnerInfo(this);
/*     */   }
/*     */   
/*     */   private void updateOwnerInfo(FishingHook hook) {
/* 572 */     Player owner = getPlayerOwner();
/* 573 */     if (owner != null)
/* 574 */       owner.fishing = hook; 
/*     */   }
/*     */   
/*     */   public Player getPlayerOwner() {
/* 579 */     Entity owner = getOwner();
/* 580 */     Player player = (Player)owner;
/* 580 */     return (owner instanceof Player) ? player : null;
/*     */   }
/*     */   
/*     */   public Entity getHookedIn() {
/* 584 */     return this.hookedIn;
/*     */   }
/*     */   
/*     */   public boolean canUsePortal(boolean ignorePassenger) {
/* 589 */     return false;
/*     */   }
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 594 */     Entity owner = getOwner();
/* 595 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket(this, serverEntity, (owner == null) ? getId() : owner.getId());
/*     */   }
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 600 */     super.recreateFromPacket(packet);
/* 601 */     if (getPlayerOwner() == null) {
/* 602 */       int ownerId = packet.getData();
/* 603 */       LOGGER.error("Failed to recreate fishing hook on client. {} (id: {}) is not a valid owner.", level().getEntity(ownerId), Integer.valueOf(ownerId));
/* 604 */       discard();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\FishingHook.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */