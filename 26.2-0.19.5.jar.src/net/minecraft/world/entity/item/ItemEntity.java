/*     */ package net.minecraft.world.entity.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.TraceableEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ItemEntity extends Entity implements TraceableEntity {
/*  44 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemEntity.class, EntityDataSerializers.ITEM_STACK);
/*     */   
/*     */   private static final float FLOAT_HEIGHT = 0.1F;
/*     */   
/*     */   public static final float EYE_HEIGHT = 0.2125F;
/*     */   
/*     */   private static final int LIFETIME = 6000;
/*     */   
/*     */   private static final int INFINITE_PICKUP_DELAY = 32767;
/*     */   
/*     */   private static final int INFINITE_LIFETIME = -32768;
/*     */   
/*     */   public static final int DEFAULT_HEALTH = 5;
/*     */   
/*     */   private static final short DEFAULT_AGE = 0;
/*     */   
/*     */   private static final short DEFAULT_PICKUP_DELAY = 0;
/*     */   
/*  59 */   private int age = 0;
/*     */   
/*  60 */   private int pickupDelay = 0;
/*     */   
/*  61 */   private int health = 5;
/*     */   
/*     */   private EntityReference<Entity> thrower;
/*     */   
/*     */   private UUID target;
/*     */   
/*     */   public final float bobOffs;
/*     */   
/*     */   public ItemEntity(EntityType<? extends ItemEntity> type, Level level) {
/*  67 */     super(type, level);
/*  68 */     this.bobOffs = this.random.nextFloat() * 3.1415927F * 2.0F;
/*  69 */     setYRot(this.random.nextFloat() * 360.0F);
/*     */   }
/*     */   
/*     */   public ItemEntity(Level level, double x, double y, double z, ItemStack itemStack) {
/*  73 */     this(EntityTypes.ITEM, level);
/*  74 */     setPos(x, y, z);
/*  75 */     setItem(itemStack);
/*  76 */     setDeltaMovement(this.random.nextDouble() * 0.2D - 0.1D, 0.2D, this.random.nextDouble() * 0.2D - 0.1D);
/*     */   }
/*     */   
/*     */   public ItemEntity(Level level, double x, double y, double z, ItemStack itemStack, double deltaX, double deltaY, double deltaZ) {
/*  80 */     this(EntityTypes.ITEM, level);
/*  81 */     setPos(x, y, z);
/*  82 */     setItem(itemStack);
/*  83 */     setDeltaMovement(deltaX, deltaY, deltaZ);
/*     */   }
/*     */   
/*     */   public boolean dampensVibrations() {
/*  88 */     return getItem().is(ItemTags.DAMPENS_VIBRATIONS);
/*     */   }
/*     */   
/*     */   public Entity getOwner() {
/*  93 */     return EntityReference.getEntity(this.thrower, level());
/*     */   }
/*     */   
/*     */   public void restoreFrom(Entity oldEntity) {
/*  98 */     super.restoreFrom(oldEntity);
/*  99 */     if (oldEntity instanceof ItemEntity) {
/*  99 */       ItemEntity item = (ItemEntity)oldEntity;
/* 100 */       this.thrower = item.thrower;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 106 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 111 */     entityData.define(DATA_ITEM, ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   protected double getDefaultGravity() {
/* 116 */     return 0.04D;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 121 */     if (getItem().isEmpty()) {
/* 122 */       discard();
/*     */       return;
/*     */     } 
/* 125 */     super.tick();
/* 126 */     if (this.pickupDelay > 0 && this.pickupDelay != 32767)
/* 127 */       this.pickupDelay--; 
/* 129 */     this.xo = getX();
/* 130 */     this.yo = getY();
/* 131 */     this.zo = getZ();
/* 133 */     Vec3 oldMovement = getDeltaMovement();
/* 136 */     if (isInWater() && getFluidHeight(FluidTags.WATER) > 0.10000000149011612D) {
/* 137 */       setUnderwaterMovement();
/* 138 */     } else if (isInLava() && getFluidHeight(FluidTags.LAVA) > 0.10000000149011612D) {
/* 139 */       setUnderLavaMovement();
/*     */     } else {
/* 141 */       applyGravity();
/*     */     } 
/* 144 */     if (level().isClientSide()) {
/* 145 */       this.noPhysics = false;
/*     */     } else {
/* 147 */       this.noPhysics = !level().noCollision(this, getBoundingBox().deflate(1.0E-7D));
/* 148 */       if (this.noPhysics)
/* 149 */         moveTowardsClosestSpace(getX(), ((getBoundingBox()).minY + (getBoundingBox()).maxY) / 2.0D, getZ()); 
/*     */     } 
/* 152 */     if (!onGround() || getDeltaMovement().horizontalDistanceSqr() > 9.999999747378752E-6D || (this.tickCount + getId()) % 4 == 0) {
/* 153 */       move(MoverType.SELF, getDeltaMovement());
/* 155 */       applyEffectsFromBlocks();
/* 157 */       float airDrag = getAirDrag();
/* 158 */       float groundFriction = airDrag;
/* 159 */       if (onGround())
/* 160 */         groundFriction *= level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction(); 
/* 163 */       setDeltaMovement(getDeltaMovement().multiply(groundFriction, airDrag, groundFriction));
/* 166 */       if (onGround()) {
/* 167 */         Vec3 movement = getDeltaMovement();
/* 168 */         if (movement.y < 0.0D)
/* 169 */           setDeltaMovement(movement.multiply(1.0D, -0.5D, 1.0D)); 
/*     */       } 
/*     */     } else {
/* 173 */       applyEffectsFromBlocksForLastMovements();
/*     */     } 
/* 176 */     boolean moved = (Mth.floor(this.xo) != Mth.floor(getX()) || Mth.floor(this.yo) != Mth.floor(getY()) || Mth.floor(this.zo) != Mth.floor(getZ()));
/* 177 */     int rate = moved ? 2 : 40;
/* 179 */     if (this.tickCount % rate == 0 && 
/* 180 */       !level().isClientSide() && isMergable())
/* 181 */       mergeWithNeighbours(); 
/* 185 */     if (this.age != -32768)
/* 186 */       this.age++; 
/* 190 */     this.needsSync |= updateFluidInteraction();
/* 192 */     if (!level().isClientSide()) {
/* 196 */       double value = getDeltaMovement().subtract(oldMovement).lengthSqr();
/* 197 */       if (value > 0.01D)
/* 198 */         this.needsSync = true; 
/*     */     } 
/* 202 */     if (!level().isClientSide() && this.age >= 6000)
/* 203 */       discard(); 
/*     */   }
/*     */   
/*     */   public BlockPos getBlockPosBelowThatAffectsMyMovement() {
/* 210 */     return getOnPos(0.999999F);
/*     */   }
/*     */   
/*     */   private void setUnderwaterMovement() {
/* 215 */     setFluidMovement(0.9900000095367432D);
/*     */   }
/*     */   
/*     */   private void setUnderLavaMovement() {
/* 220 */     setFluidMovement(0.949999988079071D);
/*     */   }
/*     */   
/*     */   private void setFluidMovement(double multiplier) {
/* 224 */     Vec3 movement = getDeltaMovement();
/* 225 */     setDeltaMovement(movement.x * multiplier, movement.y + (
/*     */         
/* 227 */         (movement.y < 0.05999999865889549D) ? 5.0E-4F : 0.0F), movement.z * multiplier);
/*     */   }
/*     */   
/*     */   private void mergeWithNeighbours() {
/* 233 */     if (!isMergable())
/*     */       return; 
/* 236 */     List<ItemEntity> items = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(0.5D, 0.0D, 0.5D), other -> (other != this && other.isMergable()));
/* 237 */     for (ItemEntity entity : items) {
/* 238 */       if (entity.isMergable()) {
/* 239 */         tryToMerge(entity);
/* 240 */         if (isRemoved())
/*     */           break; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMergable() {
/* 248 */     ItemStack item = getItem();
/* 249 */     return (isAlive() && this.pickupDelay != 32767 && this.age != -32768 && this.age < 6000 && item.getCount() < item.getMaxStackSize());
/*     */   }
/*     */   
/*     */   private void tryToMerge(ItemEntity other) {
/* 253 */     ItemStack thisItemStack = getItem();
/* 254 */     ItemStack otherItemStack = other.getItem();
/* 256 */     if (!Objects.equals(this.target, other.target) || !areMergable(thisItemStack, otherItemStack))
/*     */       return; 
/* 260 */     if (otherItemStack.getCount() < thisItemStack.getCount()) {
/* 261 */       merge(this, thisItemStack, other, otherItemStack);
/*     */     } else {
/* 263 */       merge(other, otherItemStack, this, thisItemStack);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean areMergable(ItemStack thisItemStack, ItemStack otherItemStack) {
/* 268 */     if (otherItemStack.getCount() + thisItemStack.getCount() > otherItemStack.getMaxStackSize())
/* 269 */       return false; 
/* 271 */     return ItemStack.isSameItemSameComponents(thisItemStack, otherItemStack);
/*     */   }
/*     */   
/*     */   public static ItemStack merge(ItemStack toStack, ItemStack fromStack, int maxCount) {
/* 275 */     int delta = Math.min(Math.min(toStack.getMaxStackSize(), maxCount) - toStack.getCount(), fromStack.getCount());
/* 276 */     ItemStack newToStack = toStack.copyWithCount(toStack.getCount() + delta);
/* 277 */     fromStack.shrink(delta);
/* 278 */     return newToStack;
/*     */   }
/*     */   
/*     */   private static void merge(ItemEntity toItem, ItemStack toStack, ItemStack fromStack) {
/* 282 */     ItemStack newToStack = merge(toStack, fromStack, 64);
/* 283 */     toItem.setItem(newToStack);
/*     */   }
/*     */   
/*     */   private static void merge(ItemEntity toItem, ItemStack toStack, ItemEntity fromItem, ItemStack fromStack) {
/* 287 */     merge(toItem, toStack, fromStack);
/* 288 */     toItem.pickupDelay = Math.max(toItem.pickupDelay, fromItem.pickupDelay);
/* 289 */     toItem.age = Math.min(toItem.age, fromItem.age);
/* 291 */     if (fromStack.isEmpty())
/* 292 */       fromItem.discard(); 
/*     */   }
/*     */   
/*     */   public boolean fireImmune() {
/* 298 */     return (!getItem().canBeHurtBy(damageSources().inFire()) || super.fireImmune());
/*     */   }
/*     */   
/*     */   protected boolean shouldPlayLavaHurtSound() {
/* 303 */     if (this.health <= 0)
/* 304 */       return true; 
/* 309 */     return (this.tickCount % 10 == 0);
/*     */   }
/*     */   
/*     */   public final boolean hurtClient(DamageSource source) {
/* 314 */     if (isInvulnerableToBase(source))
/* 315 */       return false; 
/* 317 */     return getItem().canBeHurtBy(source);
/*     */   }
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 322 */     if (isInvulnerableToBase(source))
/* 323 */       return false; 
/* 325 */     if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && source.getEntity() instanceof net.minecraft.world.entity.Mob)
/* 326 */       return false; 
/* 328 */     if (!getItem().canBeHurtBy(source))
/* 329 */       return false; 
/* 331 */     markHurt();
/* 332 */     this.health = (int)(this.health - damage);
/* 333 */     gameEvent((Holder)GameEvent.ENTITY_DAMAGE, source.getEntity());
/* 334 */     if (this.health <= 0) {
/* 335 */       getItem().onDestroyed(this);
/* 336 */       discard();
/*     */     } 
/* 338 */     return true;
/*     */   }
/*     */   
/*     */   public boolean ignoreExplosion(Explosion explosion) {
/* 343 */     if (explosion.shouldAffectBlocklikeEntities())
/* 344 */       return super.ignoreExplosion(explosion); 
/* 346 */     return true;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 351 */     output.putShort("Health", (short)this.health);
/* 352 */     output.putShort("Age", (short)this.age);
/* 353 */     output.putShort("PickupDelay", (short)this.pickupDelay);
/* 354 */     EntityReference.store(this.thrower, output, "Thrower");
/* 355 */     output.storeNullable("Owner", UUIDUtil.CODEC, this.target);
/* 356 */     if (!getItem().isEmpty())
/* 357 */       output.store("Item", ItemStack.CODEC, getItem()); 
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 363 */     this.health = input.getShortOr("Health", (short)5);
/* 364 */     this.age = input.getShortOr("Age", (short)0);
/* 365 */     this.pickupDelay = input.getShortOr("PickupDelay", (short)0);
/* 366 */     this.target = input.read("Owner", UUIDUtil.CODEC).orElse(null);
/* 367 */     this.thrower = EntityReference.read(input, "Thrower");
/* 368 */     setItem(input.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY));
/* 369 */     if (getItem().isEmpty())
/* 370 */       discard(); 
/*     */   }
/*     */   
/*     */   public void playerTouch(Player player) {
/* 376 */     if (level().isClientSide())
/*     */       return; 
/* 380 */     ItemStack itemStack = getItem();
/* 381 */     Item item = itemStack.getItem();
/* 382 */     int orgCount = itemStack.getCount();
/* 383 */     if (this.pickupDelay == 0 && (this.target == null || this.target.equals(player.getUUID())) && player.getInventory().add(itemStack)) {
/* 384 */       player.take(this, orgCount);
/* 385 */       if (itemStack.isEmpty()) {
/* 386 */         discard();
/* 389 */         itemStack.setCount(orgCount);
/*     */       } 
/* 391 */       player.awardStat(Stats.ITEM_PICKED_UP.get(item), orgCount);
/* 392 */       player.onItemPickup(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Component getName() {
/* 398 */     Component name = getCustomName();
/* 399 */     if (name != null)
/* 400 */       return name; 
/* 403 */     return getItem().getItemName();
/*     */   }
/*     */   
/*     */   public boolean isAttackable() {
/* 408 */     return false;
/*     */   }
/*     */   
/*     */   public Entity teleport(TeleportTransition transition) {
/* 413 */     Entity entity = super.teleport(transition);
/* 415 */     if (!level().isClientSide() && entity instanceof ItemEntity) {
/* 415 */       ItemEntity item = (ItemEntity)entity;
/* 416 */       item.mergeWithNeighbours();
/*     */     } 
/* 418 */     return entity;
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 422 */     return (ItemStack)getEntityData().get(DATA_ITEM);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack itemStack) {
/* 426 */     getEntityData().set(DATA_ITEM, itemStack);
/*     */   }
/*     */   
/*     */   public void setTarget(UUID target) {
/* 431 */     this.target = target;
/*     */   }
/*     */   
/*     */   public void setThrower(Entity thrower) {
/* 435 */     this.thrower = EntityReference.of((UniquelyIdentifyable)thrower);
/*     */   }
/*     */   
/*     */   public int getAge() {
/* 439 */     return this.age;
/*     */   }
/*     */   
/*     */   public void setDefaultPickUpDelay() {
/* 444 */     this.pickupDelay = 10;
/*     */   }
/*     */   
/*     */   public void setNoPickUpDelay() {
/* 448 */     this.pickupDelay = 0;
/*     */   }
/*     */   
/*     */   public void setNeverPickUp() {
/* 452 */     this.pickupDelay = 32767;
/*     */   }
/*     */   
/*     */   public void setPickUpDelay(int ticks) {
/* 456 */     this.pickupDelay = ticks;
/*     */   }
/*     */   
/*     */   public boolean hasPickUpDelay() {
/* 460 */     return (this.pickupDelay > 0);
/*     */   }
/*     */   
/*     */   public void setUnlimitedLifetime() {
/* 464 */     this.age = -32768;
/*     */   }
/*     */   
/*     */   public void setExtendedLifetime() {
/* 468 */     this.age = -6000;
/*     */   }
/*     */   
/*     */   public void makeFakeItem() {
/* 472 */     setNeverPickUp();
/* 473 */     this.age = 5999;
/*     */   }
/*     */   
/*     */   public static float getSpin(float ageInTicks, float bobOffset) {
/* 477 */     return ageInTicks / 20.0F + bobOffset;
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 482 */     return SoundSource.AMBIENT;
/*     */   }
/*     */   
/*     */   public float getVisualRotationYInDegrees() {
/* 487 */     return 180.0F - getSpin(getAge() + 0.5F, this.bobOffs) / 6.2831855F * 360.0F;
/*     */   }
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 492 */     if (slot == 0)
/* 493 */       return SlotAccess.of(this::getItem, this::setItem); 
/* 495 */     return super.getSlot(slot);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\item\ItemEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */