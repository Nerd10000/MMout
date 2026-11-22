/*     */ package net.minecraft.world.level.block.piston;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonMovingBlockEntity extends BlockEntity {
/*     */   private static final int TICKS_TO_EXTEND = 2;
/*     */   
/*     */   private static final double PUSH_OFFSET = 0.01D;
/*     */   
/*     */   public static final double TICK_MOVEMENT = 0.51D;
/*     */   
/*  37 */   private static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
/*     */   
/*     */   private static final float DEFAULT_PROGRESS = 0.0F;
/*     */   
/*     */   private static final boolean DEFAULT_EXTENDING = false;
/*     */   
/*     */   private static final boolean DEFAULT_SOURCE = false;
/*     */   
/*  42 */   private BlockState movedState = DEFAULT_BLOCK_STATE;
/*     */   
/*     */   private Direction direction;
/*     */   
/*     */   private boolean extending = false;
/*     */   
/*     */   private boolean isSourcePiston = false;
/*     */   
/*  48 */   private static final ThreadLocal<Direction> NOCLIP = ThreadLocal.withInitial(() -> null);
/*     */   
/*  50 */   private float progress = 0.0F;
/*     */   
/*  51 */   private float progressO = 0.0F;
/*     */   
/*     */   private long lastTicked;
/*     */   
/*     */   private int deathTicks;
/*     */   
/*     */   public PistonMovingBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  57 */     super(BlockEntityTypes.PISTON, worldPosition, blockState);
/*     */   }
/*     */   
/*     */   public PistonMovingBlockEntity(BlockPos worldPosition, BlockState blockState, BlockState movedState, Direction direction, boolean extending, boolean isSourcePiston) {
/*  61 */     this(worldPosition, blockState);
/*  62 */     this.movedState = movedState;
/*  63 */     this.direction = direction;
/*  64 */     this.extending = extending;
/*  65 */     this.isSourcePiston = isSourcePiston;
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
/*  70 */     return saveCustomOnly(registries);
/*     */   }
/*     */   
/*     */   public boolean isExtending() {
/*  74 */     return this.extending;
/*     */   }
/*     */   
/*     */   public Direction getDirection() {
/*  78 */     return this.direction;
/*     */   }
/*     */   
/*     */   public boolean isSourcePiston() {
/*  82 */     return this.isSourcePiston;
/*     */   }
/*     */   
/*     */   public float getProgress(float a) {
/*  86 */     if (a > 1.0F)
/*  87 */       a = 1.0F; 
/*  89 */     return Mth.lerp(a, this.progressO, this.progress);
/*     */   }
/*     */   
/*     */   public float getXOff(float a) {
/*  93 */     return this.direction.getStepX() * getExtendedProgress(getProgress(a));
/*     */   }
/*     */   
/*     */   public float getYOff(float a) {
/*  97 */     return this.direction.getStepY() * getExtendedProgress(getProgress(a));
/*     */   }
/*     */   
/*     */   public float getZOff(float a) {
/* 101 */     return this.direction.getStepZ() * getExtendedProgress(getProgress(a));
/*     */   }
/*     */   
/*     */   private float getExtendedProgress(float progress) {
/* 105 */     return this.extending ? (progress - 1.0F) : (1.0F - progress);
/*     */   }
/*     */   
/*     */   private BlockState getCollisionRelatedBlockState() {
/* 109 */     if (!isExtending() && isSourcePiston() && this.movedState.getBlock() instanceof PistonBaseBlock)
/* 110 */       return (BlockState)((BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState()
/* 111 */         .setValue((Property)PistonHeadBlock.SHORT, Boolean.valueOf((this.progress > 0.25F))))
/* 112 */         .setValue((Property)PistonHeadBlock.TYPE, this.movedState.is(Blocks.STICKY_PISTON) ? (Comparable)PistonType.STICKY : (Comparable)PistonType.DEFAULT))
/* 113 */         .setValue((Property)PistonHeadBlock.FACING, this.movedState.getValue((Property)PistonBaseBlock.FACING)); 
/* 115 */     return this.movedState;
/*     */   }
/*     */   
/*     */   private static void moveCollidedEntities(Level level, BlockPos pos, float newProgress, PistonMovingBlockEntity self) {
/* 119 */     Direction movement = self.getMovementDirection();
/* 121 */     double deltaProgress = (newProgress - self.progress);
/* 123 */     VoxelShape shape = self.getCollisionRelatedBlockState().getCollisionShape((BlockGetter)level, pos);
/* 124 */     if (shape.isEmpty())
/*     */       return; 
/* 128 */     AABB aabb = moveByPositionAndProgress(pos, shape.bounds(), self);
/* 129 */     List<Entity> entities = level.getEntities(null, PistonMath.getMovementArea(aabb, movement, deltaProgress).minmax(aabb));
/* 130 */     if (entities.isEmpty())
/*     */       return; 
/* 134 */     List<AABB> shapeAabbs = shape.toAabbs();
/* 135 */     boolean causeBounce = self.movedState.is(Blocks.SLIME_BLOCK);
/* 136 */     for (Entity entity : entities) {
/* 137 */       if (entity.getPistonPushReaction() == PushReaction.IGNORE)
/*     */         continue; 
/* 141 */       if (causeBounce) {
/* 142 */         if (entity instanceof net.minecraft.server.level.ServerPlayer)
/*     */           continue; 
/* 146 */         Vec3 deltaMovement = entity.getDeltaMovement();
/* 147 */         double dx = deltaMovement.x;
/* 148 */         double dy = deltaMovement.y;
/* 149 */         double dz = deltaMovement.z;
/* 150 */         switch (movement.getAxis()) {
/*     */           case EAST:
/* 151 */             dx = movement.getStepX();
/*     */             break;
/*     */           case WEST:
/* 152 */             dy = movement.getStepY();
/*     */             break;
/*     */           case UP:
/* 153 */             dz = movement.getStepZ();
/*     */             break;
/*     */         } 
/* 156 */         entity.setDeltaMovement(dx, dy, dz);
/*     */       } 
/* 159 */       double delta = 0.0D;
/* 160 */       for (AABB shapeAabb : shapeAabbs) {
/* 163 */         AABB movingAABB = PistonMath.getMovementArea(moveByPositionAndProgress(pos, shapeAabb, self), movement, deltaProgress);
/* 165 */         AABB entityAabb = entity.getBoundingBox();
/* 166 */         if (!movingAABB.intersects(entityAabb))
/*     */           continue; 
/* 170 */         delta = Math.max(delta, getMovement(movingAABB, movement, entityAabb));
/* 173 */         if (delta >= deltaProgress)
/*     */           break; 
/*     */       } 
/* 178 */       if (delta <= 0.0D)
/*     */         continue; 
/* 182 */       delta = Math.min(delta, deltaProgress) + 0.01D;
/* 183 */       moveEntityByPiston(movement, entity, delta, movement);
/* 185 */       if (!self.extending && self.isSourcePiston)
/* 186 */         fixEntityWithinPistonBase(pos, entity, movement, deltaProgress); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void moveEntityByPiston(Direction pistonDirection, Entity entity, double delta, Direction movement) {
/* 193 */     NOCLIP.set(pistonDirection);
/* 194 */     Vec3 previousPos = entity.position();
/* 195 */     entity.move(MoverType.PISTON, new Vec3(delta * movement.getStepX(), delta * movement.getStepY(), delta * movement.getStepZ()));
/* 197 */     entity.applyEffectsFromBlocks(previousPos, entity.position());
/* 199 */     entity.removeLatestMovementRecording();
/* 200 */     NOCLIP.set(null);
/*     */   }
/*     */   
/*     */   private static void moveStuckEntities(Level level, BlockPos pos, float newProgress, PistonMovingBlockEntity self) {
/* 204 */     if (!self.isStickyForEntities())
/*     */       return; 
/* 208 */     Direction movement = self.getMovementDirection();
/* 209 */     if (!movement.getAxis().isHorizontal())
/*     */       return; 
/* 213 */     double stickyTop = self.movedState.getCollisionShape((BlockGetter)level, pos).max(Direction.Axis.Y);
/* 214 */     AABB aabb = moveByPositionAndProgress(pos, new AABB(0.0D, stickyTop, 0.0D, 1.0D, 1.5000010000000001D, 1.0D), self);
/* 216 */     double deltaProgress = (newProgress - self.progress);
/* 218 */     List<Entity> entities = level.getEntities((Entity)null, aabb, entity -> matchesStickyCritera(aabb, entity, pos));
/* 219 */     for (Entity entity : entities)
/* 220 */       moveEntityByPiston(movement, entity, deltaProgress, movement); 
/*     */   }
/*     */   
/*     */   private static boolean matchesStickyCritera(AABB aabb, Entity entity, BlockPos pos) {
/* 225 */     return (entity.getPistonPushReaction() == PushReaction.NORMAL && entity
/* 226 */       .onGround() && (entity
/* 227 */       .isSupportedBy(pos) || (entity
/* 228 */       .getX() >= aabb.minX && entity
/* 229 */       .getX() <= aabb.maxX && entity
/* 230 */       .getZ() >= aabb.minZ && entity
/* 231 */       .getZ() <= aabb.maxZ)));
/*     */   }
/*     */   
/*     */   private boolean isStickyForEntities() {
/* 236 */     return this.movedState.is(Blocks.HONEY_BLOCK);
/*     */   }
/*     */   
/*     */   public Direction getMovementDirection() {
/* 240 */     return this.extending ? this.direction : this.direction.getOpposite();
/*     */   }
/*     */   
/*     */   private static double getMovement(AABB aabbToBeOutsideOf, Direction movement, AABB aabb) {
/* 244 */     switch (movement) {
/*     */       case EAST:
/* 246 */         return aabbToBeOutsideOf.maxX - aabb.minX;
/*     */       case WEST:
/* 248 */         return aabb.maxX - aabbToBeOutsideOf.minX;
/*     */       default:
/* 251 */         return aabbToBeOutsideOf.maxY - aabb.minY;
/*     */       case DOWN:
/* 253 */         return aabb.maxY - aabbToBeOutsideOf.minY;
/*     */       case SOUTH:
/* 255 */         return aabbToBeOutsideOf.maxZ - aabb.minZ;
/*     */       case NORTH:
/*     */         break;
/*     */     } 
/* 257 */     return aabb.maxZ - aabbToBeOutsideOf.minZ;
/*     */   }
/*     */   
/*     */   private static AABB moveByPositionAndProgress(BlockPos pos, AABB aabb, PistonMovingBlockEntity entity) {
/* 262 */     double currentPosition = entity.getExtendedProgress(entity.progress);
/* 263 */     return aabb.move(pos
/* 264 */         .getX() + currentPosition * entity.direction.getStepX(), pos
/* 265 */         .getY() + currentPosition * entity.direction.getStepY(), pos
/* 266 */         .getZ() + currentPosition * entity.direction.getStepZ());
/*     */   }
/*     */   
/*     */   private static void fixEntityWithinPistonBase(BlockPos pos, Entity entity, Direction direction, double deltaProgress) {
/* 271 */     AABB entityAabb = entity.getBoundingBox();
/* 272 */     AABB box = Shapes.block().bounds().move(pos);
/* 273 */     if (entityAabb.intersects(box)) {
/* 274 */       Direction opposite = direction.getOpposite();
/* 277 */       double delta = getMovement(box, opposite, entityAabb) + 0.01D;
/* 278 */       double deltaIntersected = getMovement(box, opposite, entityAabb.intersect(box)) + 0.01D;
/* 280 */       if (Math.abs(delta - deltaIntersected) < 0.01D) {
/* 281 */         delta = Math.min(delta, deltaProgress) + 0.01D;
/* 282 */         moveEntityByPiston(direction, entity, delta, opposite);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockState getMovedState() {
/* 288 */     return this.movedState;
/*     */   }
/*     */   
/*     */   public void finalTick() {
/* 292 */     if (this.level != null && (this.progressO < 1.0F || this.level.isClientSide())) {
/* 293 */       this.progress = 1.0F;
/* 294 */       this.progressO = this.progress;
/* 295 */       this.level.removeBlockEntity(this.worldPosition);
/* 296 */       setRemoved();
/* 297 */       if (this.level.getBlockState(this.worldPosition).is(Blocks.MOVING_PISTON)) {
/*     */         BlockState newState;
/* 299 */         if (this.isSourcePiston) {
/* 300 */           newState = Blocks.AIR.defaultBlockState();
/*     */         } else {
/* 302 */           newState = Block.updateFromNeighbourShapes(this.movedState, (LevelAccessor)this.level, this.worldPosition);
/*     */         } 
/* 304 */         this.level.setBlock(this.worldPosition, newState, 3);
/* 305 */         this.level.neighborChanged(this.worldPosition, newState.getBlock(), ExperimentalRedstoneUtils.initialOrientation(this.level, getPushDirection(), null));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 312 */     finalTick();
/*     */   }
/*     */   
/*     */   public Direction getPushDirection() {
/* 316 */     return this.extending ? this.direction : this.direction.getOpposite();
/*     */   }
/*     */   
/*     */   public static void tick(Level level, BlockPos pos, BlockState state, PistonMovingBlockEntity entity) {
/* 320 */     entity.lastTicked = level.getGameTime();
/* 321 */     entity.progressO = entity.progress;
/* 323 */     if (entity.progressO >= 1.0F) {
/* 324 */       if (level.isClientSide() && entity.deathTicks < 5) {
/* 325 */         entity.deathTicks++;
/*     */         return;
/*     */       } 
/* 328 */       level.removeBlockEntity(pos);
/* 329 */       entity.setRemoved();
/* 330 */       if (level.getBlockState(pos).is(Blocks.MOVING_PISTON)) {
/* 331 */         BlockState newState = Block.updateFromNeighbourShapes(entity.movedState, (LevelAccessor)level, pos);
/* 332 */         if (newState.isAir()) {
/* 333 */           level.setBlock(pos, entity.movedState, 340);
/* 334 */           Block.updateOrDestroy(entity.movedState, newState, (LevelAccessor)level, pos, 3);
/*     */         } else {
/* 336 */           if (newState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && ((Boolean)newState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue())
/* 337 */             newState = (BlockState)newState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)); 
/* 339 */           level.setBlock(pos, newState, 67);
/* 340 */           level.neighborChanged(pos, newState.getBlock(), ExperimentalRedstoneUtils.initialOrientation(level, entity.getPushDirection(), null));
/*     */         } 
/*     */       } 
/*     */       return;
/*     */     } 
/* 346 */     float newProgress = entity.progress + 0.5F;
/* 347 */     moveCollidedEntities(level, pos, newProgress, entity);
/* 348 */     moveStuckEntities(level, pos, newProgress, entity);
/* 349 */     entity.progress = newProgress;
/* 350 */     if (entity.progress >= 1.0F)
/* 351 */       entity.progress = 1.0F; 
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 357 */     super.loadAdditional(input);
/* 359 */     this.movedState = input.read("blockState", BlockState.CODEC).orElse(DEFAULT_BLOCK_STATE);
/* 360 */     this.direction = input.read("facing", Direction.LEGACY_ID_CODEC).orElse(Direction.DOWN);
/* 361 */     this.progress = input.getFloatOr("progress", 0.0F);
/* 362 */     this.progressO = this.progress;
/* 363 */     this.extending = input.getBooleanOr("extending", false);
/* 364 */     this.isSourcePiston = input.getBooleanOr("source", false);
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 369 */     super.saveAdditional(output);
/* 371 */     output.store("blockState", BlockState.CODEC, this.movedState);
/* 372 */     output.store("facing", Direction.LEGACY_ID_CODEC, this.direction);
/* 373 */     output.putFloat("progress", this.progressO);
/* 374 */     output.putBoolean("extending", this.extending);
/* 375 */     output.putBoolean("source", this.isSourcePiston);
/*     */   }
/*     */   
/*     */   public VoxelShape getCollisionShape(BlockGetter level, BlockPos pos) {
/*     */     VoxelShape pistonHeadShape;
/*     */     BlockState blockState;
/* 382 */     if (!this.extending && this.isSourcePiston && this.movedState.getBlock() instanceof PistonBaseBlock) {
/* 383 */       pistonHeadShape = ((BlockState)this.movedState.setValue((Property)PistonBaseBlock.EXTENDED, Boolean.valueOf(true))).getCollisionShape(level, pos);
/*     */     } else {
/* 385 */       pistonHeadShape = Shapes.empty();
/*     */     } 
/* 388 */     Direction noClipDirection = NOCLIP.get();
/* 389 */     if (this.progress < 1.0D && noClipDirection == getMovementDirection())
/* 390 */       return pistonHeadShape; 
/* 394 */     if (isSourcePiston()) {
/* 395 */       blockState = (BlockState)((BlockState)Blocks.PISTON_HEAD.defaultBlockState().setValue((Property)PistonHeadBlock.FACING, (Comparable)this.direction)).setValue((Property)PistonHeadBlock.SHORT, Boolean.valueOf((this.extending != ((1.0F - this.progress < 0.25F)))));
/*     */     } else {
/* 397 */       blockState = this.movedState;
/*     */     } 
/* 399 */     float extendedProgress = getExtendedProgress(this.progress);
/* 400 */     double dx = (this.direction.getStepX() * extendedProgress);
/* 401 */     double dy = (this.direction.getStepY() * extendedProgress);
/* 402 */     double dz = (this.direction.getStepZ() * extendedProgress);
/* 403 */     return Shapes.or(pistonHeadShape, blockState.getCollisionShape(level, pos).move(dx, dy, dz));
/*     */   }
/*     */   
/*     */   public long getLastTicked() {
/* 407 */     return this.lastTicked;
/*     */   }
/*     */   
/*     */   public void setLevel(Level level) {
/* 412 */     super.setLevel(level);
/* 414 */     if (level.holderLookup(Registries.BLOCK).get(this.movedState.getBlock().builtInRegistryHolder().key()).isEmpty())
/* 415 */       this.movedState = Blocks.AIR.defaultBlockState(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\piston\PistonMovingBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */