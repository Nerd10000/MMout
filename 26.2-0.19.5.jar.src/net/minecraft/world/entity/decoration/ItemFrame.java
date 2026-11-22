/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.MapItem;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DiodeBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ItemFrame extends HangingEntity {
/*  47 */   private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.ITEM_STACK);
/*     */   
/*  48 */   private static final EntityDataAccessor<Integer> DATA_ROTATION = SynchedEntityData.defineId(ItemFrame.class, EntityDataSerializers.INT);
/*     */   
/*     */   public static final int NUM_ROTATIONS = 8;
/*     */   
/*     */   private static final float DEPTH = 0.0625F;
/*     */   
/*     */   private static final float WIDTH = 0.75F;
/*     */   
/*     */   private static final float HEIGHT = 0.75F;
/*     */   
/*     */   private static final byte DEFAULT_ROTATION = 0;
/*     */   
/*     */   private static final float DEFAULT_DROP_CHANCE = 1.0F;
/*     */   
/*     */   private static final boolean DEFAULT_INVISIBLE = false;
/*     */   
/*     */   private static final boolean DEFAULT_FIXED = false;
/*     */   
/*  59 */   private float dropChance = 1.0F;
/*     */   
/*     */   private boolean fixed = false;
/*     */   
/*     */   public ItemFrame(EntityType<? extends ItemFrame> type, Level level) {
/*  63 */     super((EntityType)type, level);
/*  64 */     setInvisible(false);
/*     */   }
/*     */   
/*     */   public ItemFrame(Level level, BlockPos pos, Direction direction) {
/*  68 */     this(EntityTypes.ITEM_FRAME, level, pos, direction);
/*     */   }
/*     */   
/*     */   public ItemFrame(EntityType<? extends ItemFrame> type, Level level, BlockPos pos, Direction direction) {
/*  72 */     super((EntityType)type, level, pos);
/*  73 */     setDirection(direction);
/*  74 */     setInvisible(false);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  79 */     super.defineSynchedData(entityData);
/*  80 */     entityData.define(DATA_ITEM, ItemStack.EMPTY);
/*  81 */     entityData.define(DATA_ROTATION, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   protected void setDirection(Direction direction) {
/*  86 */     Objects.requireNonNull(direction);
/*  88 */     setDirectionRaw(direction);
/*  89 */     if (direction.getAxis().isHorizontal()) {
/*  90 */       setXRot(0.0F);
/*  91 */       setYRot((direction.get2DDataValue() * 90));
/*     */     } else {
/*  93 */       setXRot((-90 * direction.getAxisDirection().getStep()));
/*  94 */       setYRot(0.0F);
/*     */     } 
/*  96 */     this.xRotO = getXRot();
/*  97 */     this.yRotO = getYRot();
/*  99 */     recalculateBoundingBox();
/*     */   }
/*     */   
/*     */   protected final void recalculateBoundingBox() {
/* 104 */     super.recalculateBoundingBox();
/* 105 */     syncPacketPositionCodec(getX(), getY(), getZ());
/*     */   }
/*     */   
/*     */   protected AABB calculateBoundingBox(BlockPos blockPos, Direction direction) {
/* 110 */     return createBoundingBox(blockPos, direction, hasFramedMap());
/*     */   }
/*     */   
/*     */   protected AABB getPopBox() {
/* 115 */     return createBoundingBox(this.pos, getDirection(), false);
/*     */   }
/*     */   
/*     */   private AABB createBoundingBox(BlockPos blockPos, Direction direction, boolean hasFramedMap) {
/* 119 */     float shiftToBlockWall = 0.46875F;
/* 120 */     Vec3 position = Vec3.atCenterOf((Vec3i)blockPos).relative(direction, -0.46875D);
/* 122 */     float width = hasFramedMap ? 1.0F : 0.75F;
/* 123 */     float height = hasFramedMap ? 1.0F : 0.75F;
/* 124 */     Direction.Axis axis = direction.getAxis();
/* 125 */     double xSize = (axis == Direction.Axis.X) ? 0.0625D : width;
/* 126 */     double ySize = (axis == Direction.Axis.Y) ? 0.0625D : height;
/* 127 */     double zSize = (axis == Direction.Axis.Z) ? 0.0625D : width;
/* 128 */     return AABB.ofSize(position, xSize, ySize, zSize);
/*     */   }
/*     */   
/*     */   public boolean survives() {
/* 133 */     if (this.fixed)
/* 134 */       return true; 
/* 137 */     if (hasLevelCollision(getPopBox()))
/* 138 */       return false; 
/* 141 */     BlockState state = level().getBlockState(this.pos.relative(getDirection().getOpposite()));
/* 142 */     if (!state.isSolid() && (!getDirection().getAxis().isHorizontal() || !DiodeBlock.isDiode(state)))
/* 143 */       return false; 
/* 146 */     return canCoexist(true);
/*     */   }
/*     */   
/*     */   public void move(MoverType moverType, Vec3 delta) {
/* 151 */     if (!this.fixed)
/* 152 */       super.move(moverType, delta); 
/*     */   }
/*     */   
/*     */   public void push(double xa, double ya, double za) {
/* 158 */     if (!this.fixed)
/* 159 */       super.push(xa, ya, za); 
/*     */   }
/*     */   
/*     */   public void kill(ServerLevel level) {
/* 165 */     removeFramedMap(getItem());
/* 166 */     super.kill(level);
/*     */   }
/*     */   
/*     */   private boolean shouldDamageDropItem(DamageSource source) {
/* 170 */     return (!source.is(DamageTypeTags.IS_EXPLOSION) && !getItem().isEmpty());
/*     */   }
/*     */   
/*     */   private static boolean canHurtWhenFixed(DamageSource source) {
/* 174 */     return (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || source.isCreativePlayer());
/*     */   }
/*     */   
/*     */   public boolean hurtClient(DamageSource source) {
/* 179 */     if (this.fixed && !canHurtWhenFixed(source))
/* 180 */       return false; 
/* 183 */     return !isInvulnerableToBase(source);
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 188 */     if (this.fixed)
/* 189 */       return (canHurtWhenFixed(source) && super.hurtServer(level, source, damage)); 
/* 192 */     if (isInvulnerableToBase(source))
/* 193 */       return false; 
/* 196 */     if (shouldDamageDropItem(source)) {
/* 197 */       dropItem(level, source.getEntity(), false);
/* 198 */       gameEvent((Holder)GameEvent.BLOCK_CHANGE, source.getEntity());
/* 199 */       playSound(getRemoveItemSound(), 1.0F, 1.0F);
/* 200 */       return true;
/*     */     } 
/* 202 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   public SoundEvent getRemoveItemSound() {
/* 206 */     return SoundEvents.ITEM_FRAME_REMOVE_ITEM;
/*     */   }
/*     */   
/*     */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 211 */     double size = 16.0D;
/* 212 */     size *= 64.0D * getViewScale();
/* 213 */     return (distance < size * size);
/*     */   }
/*     */   
/*     */   public void dropItem(ServerLevel level, Entity causedBy) {
/* 218 */     playSound(getBreakSound(), 1.0F, 1.0F);
/* 219 */     dropItem(level, causedBy, true);
/* 220 */     gameEvent((Holder)GameEvent.BLOCK_CHANGE, causedBy);
/*     */   }
/*     */   
/*     */   public SoundEvent getBreakSound() {
/* 224 */     return SoundEvents.ITEM_FRAME_BREAK;
/*     */   }
/*     */   
/*     */   public void playPlacementSound() {
/* 229 */     playSound(getPlaceSound(), 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public SoundEvent getPlaceSound() {
/* 233 */     return SoundEvents.ITEM_FRAME_PLACE;
/*     */   }
/*     */   
/*     */   private void dropItem(ServerLevel level, Entity causedBy, boolean withFrame) {
/* 237 */     if (this.fixed)
/*     */       return; 
/* 241 */     ItemStack itemStack = getItem();
/* 242 */     setItem(ItemStack.EMPTY);
/* 244 */     if (!((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 245 */       if (causedBy == null)
/* 246 */         removeFramedMap(itemStack); 
/*     */       return;
/*     */     } 
/* 251 */     if (causedBy instanceof Player) {
/* 251 */       Player player = (Player)causedBy;
/* 252 */       if (player.hasInfiniteMaterials()) {
/* 253 */         removeFramedMap(itemStack);
/*     */         return;
/*     */       } 
/*     */     } 
/* 258 */     if (withFrame)
/* 259 */       spawnAtLocation(level, getFrameItemStack()); 
/* 261 */     if (!itemStack.isEmpty()) {
/* 262 */       itemStack = itemStack.copy();
/* 263 */       removeFramedMap(itemStack);
/* 264 */       if (this.random.nextFloat() < this.dropChance)
/* 265 */         spawnAtLocation(level, itemStack); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeFramedMap(ItemStack itemStack) {
/* 271 */     MapId mapId = getFramedMapId(itemStack);
/* 272 */     if (mapId != null) {
/* 273 */       MapItemSavedData mapItemSavedData = MapItem.getSavedData(mapId, level());
/* 274 */       if (mapItemSavedData != null)
/* 275 */         mapItemSavedData.removedFromFrame(this.pos, getId()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 281 */     return (ItemStack)getEntityData().get(DATA_ITEM);
/*     */   }
/*     */   
/*     */   public MapId getFramedMapId(ItemStack itemStack) {
/* 285 */     return (MapId)itemStack.get(DataComponents.MAP_ID);
/*     */   }
/*     */   
/*     */   public boolean hasFramedMap() {
/* 289 */     return getItem().has(DataComponents.MAP_ID);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack itemStack) {
/* 293 */     setItem(itemStack, true);
/*     */   }
/*     */   
/*     */   public void setItem(ItemStack itemStack, boolean updateNeighbours) {
/* 297 */     if (!itemStack.isEmpty())
/* 298 */       itemStack = itemStack.copyWithCount(1); 
/* 301 */     onItemChanged(itemStack);
/* 302 */     getEntityData().set(DATA_ITEM, itemStack);
/* 303 */     if (!itemStack.isEmpty())
/* 304 */       playSound(getAddItemSound(), 1.0F, 1.0F); 
/* 307 */     if (updateNeighbours && this.pos != null)
/* 308 */       level().updateNeighbourForOutputSignal(this.pos, Blocks.AIR); 
/*     */   }
/*     */   
/*     */   public SoundEvent getAddItemSound() {
/* 313 */     return SoundEvents.ITEM_FRAME_ADD_ITEM;
/*     */   }
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 318 */     if (slot == 0)
/* 319 */       return SlotAccess.of(this::getItem, this::setItem); 
/* 321 */     return super.getSlot(slot);
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 326 */     super.onSyncedDataUpdated(accessor);
/* 327 */     if (accessor.equals(DATA_ITEM))
/* 328 */       onItemChanged(getItem()); 
/*     */   }
/*     */   
/*     */   private void onItemChanged(ItemStack item) {
/* 333 */     recalculateBoundingBox();
/*     */   }
/*     */   
/*     */   public int getRotation() {
/* 337 */     return ((Integer)getEntityData().get(DATA_ROTATION)).intValue();
/*     */   }
/*     */   
/*     */   public void setRotation(int rotation) {
/* 341 */     setRotation(rotation, true);
/*     */   }
/*     */   
/*     */   private void setRotation(int rotation, boolean updateNeighbours) {
/* 345 */     getEntityData().set(DATA_ROTATION, Integer.valueOf(rotation % 8));
/* 347 */     if (updateNeighbours && this.pos != null)
/* 348 */       level().updateNeighbourForOutputSignal(this.pos, Blocks.AIR); 
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 354 */     super.addAdditionalSaveData(output);
/* 356 */     ItemStack currentItem = getItem();
/* 357 */     if (!currentItem.isEmpty())
/* 358 */       output.store("Item", ItemStack.CODEC, currentItem); 
/* 361 */     output.putByte("ItemRotation", (byte)getRotation());
/* 362 */     output.putFloat("ItemDropChance", this.dropChance);
/* 363 */     output.store("Facing", Direction.LEGACY_ID_CODEC, getDirection());
/* 364 */     output.putBoolean("Invisible", isInvisible());
/* 365 */     output.putBoolean("Fixed", this.fixed);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 370 */     super.readAdditionalSaveData(input);
/* 371 */     ItemStack itemStack = input.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
/* 374 */     ItemStack currentItem = getItem();
/* 375 */     if (!currentItem.isEmpty() && 
/* 376 */       !ItemStack.matches(itemStack, currentItem))
/* 377 */       removeFramedMap(currentItem); 
/* 381 */     setItem(itemStack, false);
/* 383 */     setRotation(input.getByteOr("ItemRotation", (byte)0), false);
/* 384 */     this.dropChance = input.getFloatOr("ItemDropChance", 1.0F);
/* 385 */     setDirection(input.read("Facing", Direction.LEGACY_ID_CODEC).orElse(Direction.DOWN));
/* 386 */     setInvisible(input.getBooleanOr("Invisible", false));
/* 387 */     this.fixed = input.getBooleanOr("Fixed", false);
/*     */   }
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/* 392 */     ItemStack itemStack = player.getItemInHand(hand);
/* 393 */     boolean frameHasItem = !getItem().isEmpty();
/* 394 */     boolean hasHeldItem = !itemStack.isEmpty();
/* 397 */     if (this.fixed)
/* 398 */       return (InteractionResult)InteractionResult.PASS; 
/* 401 */     if (player.level().isClientSide())
/* 402 */       return (frameHasItem || hasHeldItem) ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS; 
/* 405 */     if (!frameHasItem) {
/* 406 */       if (hasHeldItem && !isRemoved()) {
/* 407 */         MapItemSavedData data = MapItem.getSavedData(itemStack, level());
/* 408 */         if (data != null && data.isTrackedCountOverLimit(256))
/* 409 */           return (InteractionResult)InteractionResult.FAIL; 
/* 411 */         setItem(itemStack);
/* 412 */         gameEvent((Holder)GameEvent.BLOCK_CHANGE, (Entity)player);
/* 413 */         itemStack.consume(1, (LivingEntity)player);
/* 414 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/* 416 */       return (InteractionResult)InteractionResult.PASS;
/*     */     } 
/* 419 */     playSound(getRotateItemSound(), 1.0F, 1.0F);
/* 420 */     setRotation(getRotation() + 1);
/* 421 */     gameEvent((Holder)GameEvent.BLOCK_CHANGE, (Entity)player);
/* 422 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   public SoundEvent getRotateItemSound() {
/* 427 */     return SoundEvents.ITEM_FRAME_ROTATE_ITEM;
/*     */   }
/*     */   
/*     */   public int getAnalogOutput() {
/* 431 */     if (getItem().isEmpty())
/* 432 */       return 0; 
/* 435 */     return getRotation() % 8 + 1;
/*     */   }
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 440 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket(this, getDirection().get3DDataValue(), getPos());
/*     */   }
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 445 */     super.recreateFromPacket(packet);
/* 446 */     setDirection(Direction.from3DDataValue(packet.getData()));
/*     */   }
/*     */   
/*     */   public ItemStack getPickResult() {
/* 451 */     ItemStack framedStack = getItem();
/* 452 */     if (framedStack.isEmpty())
/* 453 */       return getFrameItemStack(); 
/* 455 */     return framedStack.copy();
/*     */   }
/*     */   
/*     */   protected ItemStack getFrameItemStack() {
/* 460 */     return new ItemStack((ItemLike)Items.ITEM_FRAME);
/*     */   }
/*     */   
/*     */   public float getVisualRotationYInDegrees() {
/* 465 */     Direction frameDirection = getDirection();
/* 466 */     int rotationCorrection = frameDirection.getAxis().isVertical() ? (90 * frameDirection.getAxisDirection().getStep()) : 0;
/* 467 */     return Mth.wrapDegrees(180 + frameDirection.get2DDataValue() * 90 + getRotation() * 45 + rotationCorrection);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\decoration\ItemFrame.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */