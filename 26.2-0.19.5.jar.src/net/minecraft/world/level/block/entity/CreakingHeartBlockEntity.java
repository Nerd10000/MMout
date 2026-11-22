/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.TrailParticleOption;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SpawnUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*     */ import net.minecraft.world.level.block.MultifaceBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.apache.commons.lang3.mutable.Mutable;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class CreakingHeartBlockEntity extends BlockEntity {
/*     */   private static final int PLAYER_DETECTION_RANGE = 32;
/*     */   
/*     */   public static final int CREAKING_ROAMING_RADIUS = 32;
/*     */   
/*     */   private static final int DISTANCE_CREAKING_TOO_FAR = 34;
/*     */   
/*     */   private static final int SPAWN_RANGE_XZ = 16;
/*     */   
/*     */   private static final int SPAWN_RANGE_Y = 8;
/*     */   
/*     */   private static final int ATTEMPTS_PER_SPAWN = 5;
/*     */   
/*     */   private static final int UPDATE_TICKS = 20;
/*     */   
/*     */   private static final int UPDATE_TICKS_VARIANCE = 5;
/*     */   
/*     */   private static final int HURT_CALL_TOTAL_TICKS = 100;
/*     */   
/*     */   private static final int NUMBER_OF_HURT_CALLS = 10;
/*     */   
/*     */   private static final int HURT_CALL_INTERVAL = 10;
/*     */   
/*     */   private static final int HURT_CALL_PARTICLE_TICKS = 50;
/*     */   
/*     */   private static final int MAX_DEPTH = 2;
/*     */   
/*     */   private static final int MAX_COUNT = 64;
/*     */   
/*     */   private static final int TICKS_GRACE_PERIOD = 30;
/*     */   
/*  64 */   private static final Optional<Creaking> NO_CREAKING = Optional.empty();
/*     */   
/*     */   private Either<Creaking, UUID> creakingInfo;
/*     */   
/*     */   private long ticksExisted;
/*     */   
/*     */   private int ticker;
/*     */   
/*     */   private int emitter;
/*     */   
/*     */   private Vec3 emitterTarget;
/*     */   
/*     */   private int outputSignal;
/*     */   
/*     */   public CreakingHeartBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  74 */     super(BlockEntityTypes.CREAKING_HEART, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public static void serverTick(Level level, BlockPos pos, BlockState state, CreakingHeartBlockEntity entity) {
/*     */     ServerLevel serverLevel;
/*  78 */     entity.ticksExisted++;
/*  79 */     if (level instanceof ServerLevel) {
/*  79 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/*     */       return;
/*     */     } 
/*  83 */     int computedOutputSignal = entity.computeAnalogOutputSignal();
/*  84 */     if (entity.outputSignal != computedOutputSignal) {
/*  85 */       entity.outputSignal = computedOutputSignal;
/*  86 */       level.updateNeighbourForOutputSignal(pos, Blocks.CREAKING_HEART);
/*     */     } 
/*  89 */     if (entity.emitter > 0) {
/*  90 */       if (entity.emitter > 50) {
/*  91 */         entity.emitParticles(serverLevel, 1, true);
/*  92 */         entity.emitParticles(serverLevel, 1, false);
/*     */       } 
/*  94 */       if (entity.emitter % 10 == 0 && entity.emitterTarget != null) {
/*  95 */         entity.getCreakingProtector().ifPresent(creaking -> entity.emitterTarget = creaking.getBoundingBox().getCenter());
/*  97 */         Vec3 heartPosition = Vec3.atCenterOf((Vec3i)pos);
/*  99 */         float progress = 0.2F + 0.8F * (100 - entity.emitter) / 100.0F;
/* 100 */         Vec3 soundLocation = heartPosition.subtract(entity.emitterTarget).scale(progress).add(entity.emitterTarget);
/* 101 */         BlockPos soundPos = BlockPos.containing((Position)soundLocation);
/* 102 */         float volume = entity.emitter / 2.0F / 100.0F + 0.5F;
/* 103 */         serverLevel.playSound(null, soundPos, SoundEvents.CREAKING_HEART_HURT, SoundSource.BLOCKS, volume, 1.0F);
/*     */       } 
/* 106 */       entity.emitter--;
/*     */     } 
/* 109 */     if (entity.ticker-- >= 0)
/*     */       return; 
/* 113 */     entity.ticker = (entity.level == null) ? 20 : (entity.level.getRandom().nextInt(5) + 20);
/* 115 */     BlockState updatedState = updateCreakingState(level, state, pos, entity);
/* 116 */     if (updatedState != state) {
/* 117 */       level.setBlock(pos, updatedState, 3);
/* 118 */       if (updatedState.getValue((Property)CreakingHeartBlock.STATE) == CreakingHeartState.UPROOTED)
/*     */         return; 
/*     */     } 
/* 123 */     if (entity.creakingInfo != null) {
/* 124 */       Optional<Creaking> optionalCreaking = entity.getCreakingProtector();
/* 125 */       if (optionalCreaking.isPresent()) {
/* 126 */         Creaking creaking = optionalCreaking.get();
/* 127 */         if ((!((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, pos)).booleanValue() && !creaking.isPersistenceRequired()) || entity.distanceToCreaking() > 34.0D || creaking.playerIsStuckInYou())
/* 128 */           entity.removeProtector((DamageSource)null); 
/*     */       } 
/*     */       return;
/*     */     } 
/* 134 */     if (updatedState.getValue((Property)CreakingHeartBlock.STATE) != CreakingHeartState.AWAKE)
/*     */       return; 
/* 138 */     if (!serverLevel.isSpawningMonsters() || serverLevel.getLevelData().getDifficulty() == Difficulty.PEACEFUL)
/*     */       return; 
/* 142 */     Player player = level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 32.0D, false);
/* 143 */     if (player != null) {
/* 144 */       Creaking creaking = spawnProtector(serverLevel, entity);
/* 145 */       if (creaking != null) {
/* 146 */         entity.setCreakingInfo(creaking);
/* 147 */         creaking.makeSound(SoundEvents.CREAKING_SPAWN);
/* 148 */         level.playSound(null, entity.getBlockPos(), SoundEvents.CREAKING_HEART_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BlockState updateCreakingState(Level level, BlockState state, BlockPos pos, CreakingHeartBlockEntity entity) {
/* 154 */     if (!CreakingHeartBlock.hasRequiredLogs(state, (LevelReader)level, pos) && entity.creakingInfo == null)
/* 155 */       return (BlockState)state.setValue((Property)CreakingHeartBlock.STATE, (Comparable)CreakingHeartState.UPROOTED); 
/* 157 */     CreakingHeartState heartState = ((Boolean)level.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, pos)).booleanValue() ? CreakingHeartState.AWAKE : CreakingHeartState.DORMANT;
/* 158 */     return (BlockState)state.setValue((Property)CreakingHeartBlock.STATE, (Comparable)heartState);
/*     */   }
/*     */   
/*     */   private double distanceToCreaking() {
/* 162 */     return ((Double)getCreakingProtector().<Double>map(creaking -> Double.valueOf(Math.sqrt(creaking.distanceToSqr(Vec3.atBottomCenterOf((Vec3i)getBlockPos()))))).orElse(Double.valueOf(0.0D))).doubleValue();
/*     */   }
/*     */   
/*     */   private void clearCreakingInfo() {
/* 166 */     this.creakingInfo = null;
/* 167 */     setChanged();
/*     */   }
/*     */   
/*     */   public void setCreakingInfo(Creaking creaking) {
/* 171 */     this.creakingInfo = Either.left(creaking);
/* 172 */     setChanged();
/*     */   }
/*     */   
/*     */   public void setCreakingInfo(UUID uuid) {
/* 176 */     this.creakingInfo = Either.right(uuid);
/* 177 */     this.ticksExisted = 0L;
/* 178 */     setChanged();
/*     */   }
/*     */   
/*     */   private Optional<Creaking> getCreakingProtector() {
/* 182 */     if (this.creakingInfo == null)
/* 183 */       return NO_CREAKING; 
/* 185 */     if (this.creakingInfo.left().isPresent()) {
/* 186 */       Creaking creaking = this.creakingInfo.left().get();
/* 187 */       if (!creaking.isRemoved())
/* 188 */         return Optional.of(creaking); 
/* 193 */       setCreakingInfo(creaking.getUUID());
/*     */     } 
/* 195 */     Level level = this.level;
/* 195 */     if (level instanceof ServerLevel) {
/* 195 */       ServerLevel serverLevel = (ServerLevel)level;
/* 195 */       if (this.creakingInfo.right().isPresent()) {
/* 198 */         UUID uuid = this.creakingInfo.right().get();
/* 199 */         Entity entity = serverLevel.getEntity(uuid);
/* 200 */         if (entity instanceof Creaking) {
/* 200 */           Creaking resolvedCreaking = (Creaking)entity;
/* 201 */           setCreakingInfo(resolvedCreaking);
/* 202 */           return Optional.of(resolvedCreaking);
/*     */         } 
/* 204 */         if (this.ticksExisted >= 30L)
/* 205 */           clearCreakingInfo(); 
/* 207 */         return NO_CREAKING;
/*     */       } 
/*     */     } 
/* 209 */     return NO_CREAKING;
/*     */   }
/*     */   
/*     */   private static Creaking spawnProtector(ServerLevel level, CreakingHeartBlockEntity entity) {
/* 213 */     BlockPos pos = entity.getBlockPos();
/* 214 */     Optional<Creaking> spawnedMob = SpawnUtil.trySpawnMob(EntityTypes.CREAKING, EntitySpawnReason.SPAWNER, level, pos, 5, 16, 8, SpawnUtil.Strategy.ON_TOP_OF_COLLIDER_NO_LEAVES, true);
/* 215 */     if (spawnedMob.isEmpty())
/* 216 */       return null; 
/* 218 */     Creaking spawnedCreaking = spawnedMob.get();
/* 219 */     level.gameEvent((Entity)spawnedCreaking, (Holder)GameEvent.ENTITY_PLACE, spawnedCreaking.position());
/* 220 */     level.broadcastEntityEvent((Entity)spawnedCreaking, (byte)60);
/* 221 */     spawnedCreaking.setTransient(pos);
/* 222 */     return spawnedCreaking;
/*     */   }
/*     */   
/*     */   public ClientboundBlockEntityDataPacket getUpdatePacket() {
/* 227 */     return ClientboundBlockEntityDataPacket.create(this);
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/* 232 */     return saveCustomOnly(registries);
/*     */   }
/*     */   
/*     */   public void creakingHurt() {
/*     */     ServerLevel serverLevel;
/* 236 */     Optional<Creaking> creaking = getCreakingProtector();
/* 237 */     if (creaking.isEmpty())
/*     */       return; 
/* 240 */     Level level = this.level;
/* 240 */     if (level instanceof ServerLevel) {
/* 240 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 243 */     if (this.emitter > 0)
/*     */       return; 
/* 246 */     emitParticles(serverLevel, 20, false);
/* 247 */     if (getBlockState().getValue((Property)CreakingHeartBlock.STATE) == CreakingHeartState.AWAKE) {
/* 248 */       int numberOfClumps = this.level.getRandom().nextIntBetweenInclusive(2, 3);
/* 249 */       for (int i = 0; i < numberOfClumps; i++) {
/* 250 */         spreadResin(serverLevel).ifPresent(blockPos -> {
/*     */               this.level.playSound(null, blockPos, SoundEvents.RESIN_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
/*     */               this.level.gameEvent((Holder)GameEvent.BLOCK_PLACE, blockPos, GameEvent.Context.of(getBlockState()));
/*     */             });
/*     */       } 
/*     */     } 
/* 256 */     this.emitter = 100;
/* 257 */     this.emitterTarget = ((Creaking)creaking.get()).getBoundingBox().getCenter();
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> spreadResin(ServerLevel level) {
/* 261 */     RandomSource random = level.getRandom();
/* 265 */     MutableObject mutableObject = new MutableObject(null);
/* 266 */     BlockPos.breadthFirstTraversal(this.worldPosition, 2, 64, (pos, acceptor) -> {
/*     */           for (Direction dir : Util.shuffledCopy((Object[])Direction.values(), random)) {
/*     */             BlockPos neighbourPos = pos.relative(dir);
/*     */             if (level.getBlockState(neighbourPos).is(BlockTags.PALE_OAK_LOGS))
/*     */               acceptor.accept(neighbourPos); 
/*     */           } 
/*     */         }pos -> {
/*     */           if (!level.getBlockState(pos).is(BlockTags.PALE_OAK_LOGS))
/*     */             return BlockPos.TraversalNodeStatus.ACCEPT; 
/*     */           for (Direction dir : Util.shuffledCopy((Object[])Direction.values(), random)) {
/*     */             BlockPos neightbourPos = pos.relative(dir);
/*     */             BlockState neighbourState = level.getBlockState(neightbourPos);
/*     */             Direction opposite = dir.getOpposite();
/*     */             if (neighbourState.isAir()) {
/*     */               neighbourState = Blocks.RESIN_CLUMP.defaultBlockState();
/*     */             } else if (neighbourState.is(Blocks.WATER) && neighbourState.getFluidState().isSource()) {
/*     */               neighbourState = (BlockState)Blocks.RESIN_CLUMP.defaultBlockState().setValue((Property)MultifaceBlock.WATERLOGGED, Boolean.valueOf(true));
/*     */             } 
/*     */             if (neighbourState.is(Blocks.RESIN_CLUMP) && !MultifaceBlock.hasFace(neighbourState, opposite)) {
/*     */               level.setBlock(neightbourPos, (BlockState)neighbourState.setValue((Property)MultifaceBlock.getFaceProperty(opposite), Boolean.valueOf(true)), 3);
/*     */               placedResin.setValue(neightbourPos);
/*     */               return BlockPos.TraversalNodeStatus.STOP;
/*     */             } 
/*     */           } 
/*     */           return BlockPos.TraversalNodeStatus.ACCEPT;
/*     */         });
/* 295 */     return Optional.ofNullable((BlockPos)mutableObject.get());
/*     */   }
/*     */   
/*     */   private void emitParticles(ServerLevel serverLevel, int count, boolean towardsCreaking) {
/* 299 */     Optional<Creaking> creaking = getCreakingProtector();
/* 300 */     if (creaking.isEmpty())
/*     */       return; 
/* 303 */     int color = towardsCreaking ? 16545810 : 6250335;
/* 305 */     RandomSource random = serverLevel.getRandom();
/*     */     double i;
/* 307 */     for (i = 0.0D; i < count; i++) {
/* 308 */       AABB box = ((Creaking)creaking.get()).getBoundingBox();
/* 309 */       Vec3 source = box.getMinPosition().add(random.nextDouble() * box.getXsize(), random.nextDouble() * box.getYsize(), random.nextDouble() * box.getZsize());
/* 310 */       Vec3 destination = Vec3.atLowerCornerOf((Vec3i)getBlockPos()).add(random.nextDouble(), random.nextDouble(), random.nextDouble());
/* 311 */       if (towardsCreaking) {
/* 312 */         Vec3 foo = source;
/* 313 */         source = destination;
/* 314 */         destination = foo;
/*     */       } 
/* 317 */       TrailParticleOption particleOption = new TrailParticleOption(destination, color, random.nextInt(40) + 10);
/* 318 */       serverLevel.sendParticles((ParticleOptions)particleOption, true, true, source.x, source.y, source.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 324 */     removeProtector((DamageSource)null);
/*     */   }
/*     */   
/*     */   public void removeProtector(DamageSource damageSource) {
/* 328 */     Optional<Creaking> creakingProtector = getCreakingProtector();
/* 329 */     if (creakingProtector.isPresent()) {
/* 330 */       Creaking creaking = creakingProtector.get();
/* 331 */       if (damageSource == null) {
/* 332 */         creaking.tearDown();
/*     */       } else {
/* 334 */         creaking.creakingDeathEffects(damageSource);
/* 335 */         creaking.setTearingDown();
/* 336 */         creaking.setHealth(0.0F);
/*     */       } 
/* 338 */       clearCreakingInfo();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isProtector(Creaking creaking) {
/* 343 */     return ((Boolean)getCreakingProtector().<Boolean>map(c -> Boolean.valueOf((c == creaking))).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public int getAnalogOutputSignal() {
/* 347 */     return this.outputSignal;
/*     */   }
/*     */   
/*     */   public int computeAnalogOutputSignal() {
/* 351 */     if (this.creakingInfo == null || getCreakingProtector().isEmpty())
/* 352 */       return 0; 
/* 354 */     double distance = distanceToCreaking();
/* 355 */     double scaledDistance = Math.clamp(distance, 0.0D, 32.0D) / 32.0D;
/* 356 */     return 15 - (int)Math.floor(scaledDistance * 15.0D);
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 361 */     super.loadAdditional(input);
/* 362 */     input.read("creaking", UUIDUtil.CODEC).ifPresentOrElse(this::setCreakingInfo, this::clearCreakingInfo);
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 370 */     super.saveAdditional(output);
/* 371 */     if (this.creakingInfo != null)
/* 372 */       output.store("creaking", UUIDUtil.CODEC, this.creakingInfo.map(Entity::getUUID, uuid -> uuid)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\CreakingHeartBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */