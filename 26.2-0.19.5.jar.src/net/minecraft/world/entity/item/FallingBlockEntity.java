/*     */ package net.minecraft.world.entity.item;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.context.DirectionalPlaceContext;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.AnvilBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Fallable;
/*     */ import net.minecraft.world.level.block.FallingBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FallingBlockEntity extends Entity {
/*  60 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  62 */   private static final BlockState DEFAULT_BLOCK_STATE = Blocks.SAND.defaultBlockState();
/*     */   
/*     */   private static final int DEFAULT_TIME = 0;
/*     */   
/*     */   private static final float DEFAULT_FALL_DAMAGE_PER_DISTANCE = 0.0F;
/*     */   
/*     */   private static final int DEFAULT_MAX_FALL_DAMAGE = 40;
/*     */   
/*     */   private static final boolean DEFAULT_DROP_ITEM = true;
/*     */   
/*     */   private static final boolean DEFAULT_CANCEL_DROP = false;
/*     */   
/*  69 */   private BlockState blockState = DEFAULT_BLOCK_STATE;
/*     */   
/*  70 */   public int time = 0;
/*     */   
/*     */   public boolean dropItem = true;
/*     */   
/*     */   private boolean cancelDrop = false;
/*     */   
/*     */   private boolean hurtEntities;
/*     */   
/*  74 */   private int fallDamageMax = 40;
/*     */   
/*  75 */   private float fallDamagePerDistance = 0.0F;
/*     */   
/*     */   public CompoundTag blockData;
/*     */   
/*     */   public boolean forceTickAfterTeleportToDuplicate;
/*     */   
/*  79 */   protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(FallingBlockEntity.class, EntityDataSerializers.BLOCK_POS);
/*     */   
/*     */   public FallingBlockEntity(EntityType<? extends FallingBlockEntity> type, Level level) {
/*  82 */     super(type, level);
/*     */   }
/*     */   
/*     */   private FallingBlockEntity(Level level, double x, double y, double z, BlockState blockState) {
/*  86 */     this(EntityTypes.FALLING_BLOCK, level);
/*  87 */     this.blockState = blockState;
/*  88 */     this.blocksBuilding = true;
/*  90 */     setPos(x, y, z);
/*  92 */     setDeltaMovement(Vec3.ZERO);
/*  94 */     this.xo = x;
/*  95 */     this.yo = y;
/*  96 */     this.zo = z;
/*  98 */     setStartPos(blockPosition());
/*     */   }
/*     */   
/*     */   public static FallingBlockEntity fall(Level level, BlockPos pos, BlockState state) {
/* 102 */     FallingBlockEntity entity = new FallingBlockEntity(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, state.hasProperty((Property)BlockStateProperties.WATERLOGGED) ? (BlockState)state.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)) : state);
/* 104 */     level.setBlock(pos, state.getFluidState().createLegacyBlock(), 3);
/* 105 */     level.addFreshEntity(entity);
/* 106 */     return entity;
/*     */   }
/*     */   
/*     */   public boolean isAttackable() {
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   public final boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 116 */     if (!isInvulnerableToBase(source))
/* 117 */       markHurt(); 
/* 120 */     return false;
/*     */   }
/*     */   
/*     */   public void setStartPos(BlockPos pos) {
/* 124 */     this.entityData.set(DATA_START_POS, pos);
/*     */   }
/*     */   
/*     */   public BlockPos getStartPos() {
/* 128 */     return (BlockPos)this.entityData.get(DATA_START_POS);
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 133 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 138 */     entityData.define(DATA_START_POS, BlockPos.ZERO);
/*     */   }
/*     */   
/*     */   public boolean isPickable() {
/* 143 */     return !isRemoved();
/*     */   }
/*     */   
/*     */   protected double getDefaultGravity() {
/* 148 */     return 0.04D;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 153 */     if (this.blockState.isAir()) {
/* 154 */       discard();
/*     */       return;
/*     */     } 
/* 158 */     Block block = this.blockState.getBlock();
/* 159 */     this.time++;
/* 161 */     applyGravity();
/* 162 */     move(MoverType.SELF, getDeltaMovement());
/* 163 */     applyEffectsFromBlocks();
/* 164 */     handlePortal();
/* 166 */     Level level = level();
/* 166 */     if (level instanceof ServerLevel) {
/* 166 */       ServerLevel serverLevel = (ServerLevel)level;
/* 166 */       if (isAlive() || this.forceTickAfterTeleportToDuplicate) {
/* 167 */         BlockPos pos = blockPosition();
/* 169 */         boolean isConcrete = this.blockState.getBlock() instanceof net.minecraft.world.level.block.ConcretePowderBlock;
/* 170 */         boolean isStuckInWater = (isConcrete && level().getFluidState(pos).is(FluidTags.WATER));
/* 171 */         double moveVec = getDeltaMovement().lengthSqr();
/* 173 */         if (isConcrete && moveVec > 1.0D) {
/* 176 */           BlockHitResult clip = level().clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
/* 177 */           if (clip.getType() != HitResult.Type.MISS && level().getFluidState(clip.getBlockPos()).is(FluidTags.WATER)) {
/* 179 */             pos = clip.getBlockPos();
/* 180 */             isStuckInWater = true;
/*     */           } 
/*     */         } 
/* 184 */         if (onGround() || isStuckInWater) {
/* 185 */           BlockState currentState = level().getBlockState(pos);
/* 188 */           setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
/* 190 */           if (!currentState.is(Blocks.MOVING_PISTON))
/* 191 */             if (!this.cancelDrop) {
/* 192 */               boolean mayReplace = currentState.canBeReplaced((BlockPlaceContext)new DirectionalPlaceContext(level(), pos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
/* 194 */               boolean wouldContinueFalling = (FallingBlock.isFree(level().getBlockState(pos.below())) && (!isConcrete || !isStuckInWater));
/* 195 */               boolean wouldSurvive = (this.blockState.canSurvive((LevelReader)level(), pos) && !wouldContinueFalling);
/* 196 */               if (mayReplace && wouldSurvive) {
/* 197 */                 if (this.blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && level().getFluidState(pos).is(Fluids.WATER))
/* 198 */                   this.blockState = (BlockState)this.blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)); 
/* 200 */                 if (level().setBlock(pos, this.blockState, 3)) {
/* 202 */                   (serverLevel.getChunkSource()).chunkMap.sendToTrackingPlayers(this, (Packet)new ClientboundBlockUpdatePacket(pos, level().getBlockState(pos)));
/* 203 */                   discard();
/* 204 */                   if (block instanceof Fallable) {
/* 204 */                     Fallable fallable = (Fallable)block;
/* 205 */                     fallable.onLand(level(), pos, this.blockState, currentState, this);
/*     */                   } 
/* 207 */                   if (this.blockData != null && this.blockState.hasBlockEntity()) {
/* 208 */                     BlockEntity blockEntity = level().getBlockEntity(pos);
/* 210 */                     if (blockEntity != null) {
/*     */                       try {
/* 212 */                         ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LOGGER);
/*     */                         try {
/* 213 */                           RegistryAccess registryAccess = level().registryAccess();
/* 215 */                           TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)registryAccess);
/* 216 */                           blockEntity.saveWithoutMetadata((ValueOutput)output);
/* 217 */                           CompoundTag merged = output.buildResult();
/* 219 */                           this.blockData.forEach((name, tag) -> merged.put(name, tag.copy()));
/* 221 */                           blockEntity.loadWithComponents(TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)registryAccess, merged));
/* 222 */                           reporter.close();
/*     */                         } catch (Throwable throwable) {
/*     */                           try {
/*     */                             reporter.close();
/*     */                           } catch (Throwable throwable1) {
/*     */                             throwable.addSuppressed(throwable1);
/*     */                           } 
/*     */                           throw throwable;
/*     */                         } 
/* 223 */                       } catch (Exception e) {
/* 224 */                         LOGGER.error("Failed to load block entity from falling block", e);
/*     */                       } 
/* 226 */                       blockEntity.setChanged();
/*     */                     } 
/*     */                   } 
/* 229 */                 } else if (this.dropItem && ((Boolean)serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 230 */                   discard();
/* 231 */                   callOnBrokenAfterFall(block, pos);
/* 232 */                   spawnAtLocation(serverLevel, (ItemLike)block);
/*     */                 } 
/*     */               } else {
/* 235 */                 discard();
/* 236 */                 if (this.dropItem && ((Boolean)serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 237 */                   callOnBrokenAfterFall(block, pos);
/* 238 */                   spawnAtLocation(serverLevel, (ItemLike)block);
/*     */                 } 
/*     */               } 
/*     */             } else {
/* 242 */               discard();
/* 243 */               callOnBrokenAfterFall(block, pos);
/*     */             }  
/* 246 */         } else if ((this.time > 100 && (pos.getY() <= level().getMinY() || pos.getY() > level().getMaxY())) || this.time > 600) {
/* 247 */           if (this.dropItem && ((Boolean)serverLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue())
/* 248 */             spawnAtLocation(serverLevel, (ItemLike)block); 
/* 250 */           discard();
/*     */         } 
/*     */       } 
/*     */     } 
/* 253 */     setDeltaMovement(getDeltaMovement().scale(getAirDrag()));
/*     */   }
/*     */   
/*     */   public void callOnBrokenAfterFall(Block block, BlockPos pos) {
/* 257 */     if (block instanceof Fallable) {
/* 257 */       Fallable fallable = (Fallable)block;
/* 258 */       fallable.onBrokenAfterFall(level(), pos, this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 264 */     if (!this.hurtEntities)
/* 265 */       return false; 
/* 268 */     int fallDistanceInt = Mth.ceil(fallDistance - 1.0D);
/* 269 */     if (fallDistanceInt < 0)
/* 270 */       return false; 
/* 273 */     Predicate<Entity> entitySelector = EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE);
/* 274 */     Block block = this.blockState.getBlock();
/* 274 */     Fallable fallable = (Fallable)block;
/* 276 */     DamageSource actualDamageSource = (block instanceof Fallable) ? fallable.getFallDamageSource(this) : damageSources().fallingBlock(this);
/* 278 */     float damage = Math.min(Mth.floor(fallDistanceInt * this.fallDamagePerDistance), this.fallDamageMax);
/* 279 */     level().getEntities(this, getBoundingBox(), entitySelector).forEach(entity -> entity.hurt(actualDamageSource, damage));
/* 283 */     boolean isAnvil = this.blockState.is(BlockTags.ANVIL);
/* 284 */     if (isAnvil && damage > 0.0F && this.random.nextFloat() < 0.05F + fallDistanceInt * 0.05F) {
/* 285 */       BlockState newBlockState = AnvilBlock.damage(this.blockState);
/* 286 */       if (newBlockState == null) {
/* 287 */         this.cancelDrop = true;
/*     */       } else {
/* 289 */         this.blockState = newBlockState;
/*     */       } 
/*     */     } 
/* 292 */     return false;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 297 */     output.store("BlockState", BlockState.CODEC, this.blockState);
/* 298 */     output.putInt("Time", this.time);
/* 299 */     output.putBoolean("DropItem", this.dropItem);
/* 300 */     output.putBoolean("HurtEntities", this.hurtEntities);
/* 301 */     output.putFloat("FallHurtAmount", this.fallDamagePerDistance);
/* 302 */     output.putInt("FallHurtMax", this.fallDamageMax);
/* 303 */     if (this.blockData != null)
/* 304 */       output.store("TileEntityData", CompoundTag.CODEC, this.blockData); 
/* 306 */     output.putBoolean("CancelDrop", this.cancelDrop);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 311 */     this.blockState = input.read("BlockState", BlockState.CODEC).orElse(DEFAULT_BLOCK_STATE);
/* 313 */     this.time = input.getIntOr("Time", 0);
/* 315 */     boolean defaultHurtEntities = this.blockState.is(BlockTags.ANVIL);
/* 316 */     this.hurtEntities = input.getBooleanOr("HurtEntities", defaultHurtEntities);
/* 317 */     this.fallDamagePerDistance = input.getFloatOr("FallHurtAmount", 0.0F);
/* 318 */     this.fallDamageMax = input.getIntOr("FallHurtMax", 40);
/* 320 */     this.dropItem = input.getBooleanOr("DropItem", true);
/* 322 */     this.blockData = input.read("TileEntityData", CompoundTag.CODEC).orElse(null);
/* 324 */     this.cancelDrop = input.getBooleanOr("CancelDrop", false);
/*     */   }
/*     */   
/*     */   public void setHurtsEntities(float damagePerDistance, int damageMax) {
/* 328 */     this.hurtEntities = true;
/* 329 */     this.fallDamagePerDistance = damagePerDistance;
/* 330 */     this.fallDamageMax = damageMax;
/*     */   }
/*     */   
/*     */   public void disableDrop() {
/* 334 */     this.cancelDrop = true;
/*     */   }
/*     */   
/*     */   public boolean displayFireAnimation() {
/* 339 */     return false;
/*     */   }
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory category) {
/* 344 */     super.fillCrashReportCategory(category);
/* 345 */     category.setDetail("Immitating BlockState", this.blockState.toString());
/*     */   }
/*     */   
/*     */   public BlockState getBlockState() {
/* 349 */     return this.blockState;
/*     */   }
/*     */   
/*     */   protected Component getTypeName() {
/* 354 */     return (Component)Component.translatable("entity.minecraft.falling_block_type", new Object[] { this.blockState.getBlock().getName() });
/*     */   }
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 359 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket(this, serverEntity, Block.getId(getBlockState()));
/*     */   }
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 364 */     super.recreateFromPacket(packet);
/* 365 */     this.blockState = Block.stateById(packet.getData());
/* 366 */     this.blocksBuilding = true;
/* 368 */     double x = packet.getX();
/* 369 */     double y = packet.getY();
/* 370 */     double z = packet.getZ();
/* 372 */     setPos(x, y, z);
/* 373 */     setStartPos(blockPosition());
/*     */   }
/*     */   
/*     */   public Entity teleport(TeleportTransition transition) {
/* 382 */     ResourceKey<Level> newDimension = transition.newLevel().dimension();
/* 383 */     ResourceKey<Level> oldDimension = level().dimension();
/* 384 */     boolean fromOrToEnd = ((oldDimension == Level.END || newDimension == Level.END) && oldDimension != newDimension);
/* 386 */     Entity newEntity = super.teleport(transition);
/* 387 */     this.forceTickAfterTeleportToDuplicate = (newEntity != null && fromOrToEnd);
/* 388 */     return newEntity;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\item\FallingBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */