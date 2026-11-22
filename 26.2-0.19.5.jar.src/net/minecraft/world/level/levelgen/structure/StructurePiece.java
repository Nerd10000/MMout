/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public abstract class StructurePiece {
/*  64 */   protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */   
/*     */   protected BoundingBox boundingBox;
/*     */   
/*     */   private Direction orientation;
/*     */   
/*     */   private Mirror mirror;
/*     */   
/*     */   private Rotation rotation;
/*     */   
/*     */   protected int genDepth;
/*     */   
/*     */   private final StructurePieceType type;
/*     */   
/*     */   protected StructurePiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
/*  73 */     this.type = type;
/*  74 */     this.genDepth = genDepth;
/*  75 */     this.boundingBox = boundingBox;
/*     */   }
/*     */   
/*     */   public StructurePiece(StructurePieceType type, CompoundTag tag) {
/*  79 */     this(type, tag
/*     */         
/*  81 */         .getIntOr("GD", 0), tag
/*  82 */         .read("BB", BoundingBox.CODEC).orElseThrow());
/*  84 */     int orientation = tag.getIntOr("O", 0);
/*  85 */     setOrientation((orientation == -1) ? null : Direction.from2DDataValue(orientation));
/*     */   }
/*     */   
/*     */   protected static BoundingBox makeBoundingBox(int x, int y, int z, Direction direction, int width, int height, int depth) {
/*  89 */     if (direction.getAxis() == Direction.Axis.Z)
/*  90 */       return new BoundingBox(x, y, z, x + width - 1, y + height - 1, z + depth - 1); 
/*  92 */     return new BoundingBox(x, y, z, x + depth - 1, y + height - 1, z + width - 1);
/*     */   }
/*     */   
/*     */   protected static Direction getRandomHorizontalDirection(RandomSource random) {
/*  97 */     return Direction.Plane.HORIZONTAL.getRandomDirection(random);
/*     */   }
/*     */   
/*     */   public final CompoundTag createTag(StructurePieceSerializationContext context) {
/* 101 */     CompoundTag tag = new CompoundTag();
/* 103 */     tag.putString("id", BuiltInRegistries.STRUCTURE_PIECE.getKey(getType()).toString());
/* 104 */     tag.store("BB", BoundingBox.CODEC, this.boundingBox);
/* 105 */     Direction orientation = getOrientation();
/* 106 */     tag.putInt("O", (orientation == null) ? -1 : orientation.get2DDataValue());
/* 107 */     tag.putInt("GD", this.genDepth);
/* 109 */     addAdditionalSaveData(context, tag);
/* 111 */     return tag;
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {}
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/* 122 */     return this.boundingBox;
/*     */   }
/*     */   
/*     */   public int getGenDepth() {
/* 126 */     return this.genDepth;
/*     */   }
/*     */   
/*     */   public void setGenDepth(int genDepth) {
/* 130 */     this.genDepth = genDepth;
/*     */   }
/*     */   
/*     */   public boolean isCloseToChunk(ChunkPos pos, int distance) {
/* 134 */     int cx = pos.getMinBlockX();
/* 135 */     int cz = pos.getMinBlockZ();
/* 137 */     return this.boundingBox.intersects(cx - distance, cz - distance, cx + 15 + distance, cz + 15 + distance);
/*     */   }
/*     */   
/*     */   public BlockPos getLocatorPosition() {
/* 141 */     return new BlockPos((Vec3i)this.boundingBox.getCenter());
/*     */   }
/*     */   
/*     */   protected BlockPos.MutableBlockPos getWorldPos(int x, int y, int z) {
/* 145 */     return new BlockPos.MutableBlockPos(getWorldX(x, z), getWorldY(y), getWorldZ(x, z));
/*     */   }
/*     */   
/*     */   protected int getWorldX(int x, int z) {
/* 149 */     Direction orientation = getOrientation();
/* 150 */     if (orientation == null)
/* 151 */       return x; 
/* 154 */     switch (orientation) {
/*     */       case NORTH:
/*     */       case SOUTH:
/*     */       
/*     */       case WEST:
/*     */       
/*     */       case EAST:
/*     */       
/*     */     } 
/* 154 */     return 
/*     */       
/* 158 */       x;
/*     */   }
/*     */   
/*     */   protected int getWorldY(int y) {
/* 163 */     if (getOrientation() == null)
/* 164 */       return y; 
/* 166 */     return y + this.boundingBox.minY();
/*     */   }
/*     */   
/*     */   protected int getWorldZ(int x, int z) {
/* 170 */     Direction orientation = getOrientation();
/* 171 */     if (orientation == null)
/* 172 */       return z; 
/* 175 */     switch (orientation) {
/*     */       case NORTH:
/*     */       
/*     */       case SOUTH:
/*     */       
/*     */       case WEST:
/*     */       case EAST:
/*     */       
/*     */     } 
/* 175 */     return 
/*     */       
/* 179 */       z;
/*     */   }
/*     */   
/* 183 */   private static final Set<Block> SHAPE_CHECK_BLOCKS = (Set<Block>)ImmutableSet.builder()
/*     */     
/* 185 */     .add(Blocks.NETHER_BRICK_FENCE)
/* 186 */     .add(Blocks.TORCH)
/* 187 */     .add(Blocks.WALL_TORCH)
/* 188 */     .add(Blocks.OAK_FENCE)
/* 189 */     .add(Blocks.SPRUCE_FENCE)
/* 190 */     .add(Blocks.DARK_OAK_FENCE)
/* 191 */     .add(Blocks.PALE_OAK_FENCE)
/* 192 */     .add(Blocks.ACACIA_FENCE)
/* 193 */     .add(Blocks.BIRCH_FENCE)
/* 194 */     .add(Blocks.JUNGLE_FENCE)
/* 195 */     .add(Blocks.LADDER)
/* 196 */     .add(Blocks.IRON_BARS)
/* 197 */     .build();
/*     */   
/*     */   protected void placeBlock(WorldGenLevel level, BlockState blockState, int x, int y, int z, BoundingBox chunkBB) {
/* 200 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 202 */     if (!chunkBB.isInside((Vec3i)mutableBlockPos))
/*     */       return; 
/* 206 */     if (!canBeReplaced((LevelReader)level, x, y, z, chunkBB))
/*     */       return; 
/* 210 */     if (this.mirror != Mirror.NONE)
/* 211 */       blockState = blockState.mirror(this.mirror); 
/* 213 */     if (this.rotation != Rotation.NONE)
/* 214 */       blockState = blockState.rotate(this.rotation); 
/* 217 */     level.setBlock((BlockPos)mutableBlockPos, blockState, 2);
/* 218 */     FluidState fluidState = level.getFluidState((BlockPos)mutableBlockPos);
/* 219 */     if (!fluidState.isEmpty())
/* 220 */       level.scheduleTick((BlockPos)mutableBlockPos, fluidState.getType(), 0); 
/* 222 */     if (SHAPE_CHECK_BLOCKS.contains(blockState.getBlock()))
/* 223 */       level.getChunk((BlockPos)mutableBlockPos).markPosForPostProcessing((BlockPos)mutableBlockPos); 
/*     */   }
/*     */   
/*     */   protected boolean canBeReplaced(LevelReader level, int x, int y, int z, BoundingBox chunkBB) {
/* 228 */     return true;
/*     */   }
/*     */   
/*     */   protected BlockState getBlock(BlockGetter level, int x, int y, int z, BoundingBox chunkBB) {
/* 242 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 243 */     if (!chunkBB.isInside((Vec3i)mutableBlockPos))
/* 244 */       return Blocks.AIR.defaultBlockState(); 
/* 247 */     return level.getBlockState((BlockPos)mutableBlockPos);
/*     */   }
/*     */   
/*     */   protected boolean isInterior(LevelReader level, int x, int y, int z, BoundingBox chunkBB) {
/* 252 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y + 1, z);
/* 254 */     if (!chunkBB.isInside((Vec3i)mutableBlockPos))
/* 255 */       return false; 
/* 258 */     return (mutableBlockPos.getY() < level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, mutableBlockPos.getX(), mutableBlockPos.getZ()));
/*     */   }
/*     */   
/*     */   protected void generateAirBox(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1) {
/* 262 */     for (int y = y0; y <= y1; y++) {
/* 263 */       for (int x = x0; x <= x1; x++) {
/* 264 */         for (int z = z0; z <= z1; z++)
/* 265 */           placeBlock(level, Blocks.AIR.defaultBlockState(), x, y, z, chunkBB); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateBox(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1, BlockState edgeBlock, BlockState fillBlock, boolean skipAir) {
/* 272 */     for (int y = y0; y <= y1; y++) {
/* 273 */       for (int x = x0; x <= x1; x++) {
/* 274 */         for (int z = z0; z <= z1; z++) {
/* 275 */           if (!skipAir || !getBlock((BlockGetter)level, x, y, z, chunkBB).isAir())
/* 278 */             if (y == y0 || y == y1 || x == x0 || x == x1 || z == z0 || z == z1) {
/* 279 */               placeBlock(level, edgeBlock, x, y, z, chunkBB);
/*     */             } else {
/* 281 */               placeBlock(level, fillBlock, x, y, z, chunkBB);
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateBox(WorldGenLevel level, BoundingBox chunkBB, BoundingBox boxBB, BlockState edgeBlock, BlockState fillBlock, boolean skipAir) {
/* 289 */     generateBox(level, chunkBB, boxBB.minX(), boxBB.minY(), boxBB.minZ(), boxBB.maxX(), boxBB.maxY(), boxBB.maxZ(), edgeBlock, fillBlock, skipAir);
/*     */   }
/*     */   
/*     */   protected void generateBox(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1, boolean skipAir, RandomSource random, BlockSelector selector) {
/* 293 */     for (int y = y0; y <= y1; y++) {
/* 294 */       for (int x = x0; x <= x1; x++) {
/* 295 */         for (int z = z0; z <= z1; z++) {
/* 296 */           if (!skipAir || !getBlock((BlockGetter)level, x, y, z, chunkBB).isAir()) {
/* 299 */             selector.next(random, x, y, z, (y == y0 || y == y1 || x == x0 || x == x1 || z == z0 || z == z1));
/* 300 */             placeBlock(level, selector.getNext(), x, y, z, chunkBB);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateBox(WorldGenLevel level, BoundingBox chunkBB, BoundingBox boxBB, boolean skipAir, RandomSource random, BlockSelector selector) {
/* 307 */     generateBox(level, chunkBB, boxBB.minX(), boxBB.minY(), boxBB.minZ(), boxBB.maxX(), boxBB.maxY(), boxBB.maxZ(), skipAir, random, selector);
/*     */   }
/*     */   
/*     */   protected void generateMaybeBox(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, float probability, int x0, int y0, int z0, int x1, int y1, int z1, BlockState edgeBlock, BlockState fillBlock, boolean skipAir, boolean hasToBeInside) {
/* 311 */     for (int y = y0; y <= y1; y++) {
/* 312 */       for (int x = x0; x <= x1; x++) {
/* 313 */         for (int z = z0; z <= z1; z++) {
/* 314 */           if (random.nextFloat() <= probability)
/* 317 */             if (!skipAir || !getBlock((BlockGetter)level, x, y, z, chunkBB).isAir())
/* 320 */               if (!hasToBeInside || isInterior((LevelReader)level, x, y, z, chunkBB))
/* 323 */                 if (y == y0 || y == y1 || x == x0 || x == x1 || z == z0 || z == z1) {
/* 324 */                   placeBlock(level, edgeBlock, x, y, z, chunkBB);
/*     */                 } else {
/* 326 */                   placeBlock(level, fillBlock, x, y, z, chunkBB);
/*     */                 }    
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void maybeGenerateBlock(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, float probability, int x, int y, int z, BlockState blockState) {
/* 334 */     if (random.nextFloat() < probability)
/* 335 */       placeBlock(level, blockState, x, y, z, chunkBB); 
/*     */   }
/*     */   
/*     */   protected void generateUpperHalfSphere(WorldGenLevel level, BoundingBox chunkBB, int x0, int y0, int z0, int x1, int y1, int z1, BlockState fillBlock, boolean skipAir) {
/* 340 */     float diagX = (x1 - x0 + 1);
/* 341 */     float diagY = (y1 - y0 + 1);
/* 342 */     float diagZ = (z1 - z0 + 1);
/* 344 */     float cx = x0 + diagX / 2.0F;
/* 345 */     float cz = z0 + diagZ / 2.0F;
/* 347 */     for (int y = y0; y <= y1; y++) {
/* 348 */       float normalizedYDistance = (y - y0) / diagY;
/* 350 */       for (int x = x0; x <= x1; x++) {
/* 351 */         float normalizedXDistance = (x - cx) / diagX * 0.5F;
/* 353 */         for (int z = z0; z <= z1; z++) {
/* 354 */           float normalizedZDistance = (z - cz) / diagZ * 0.5F;
/* 356 */           if (!skipAir || !getBlock((BlockGetter)level, x, y, z, chunkBB).isAir()) {
/* 360 */             float dist = normalizedXDistance * normalizedXDistance + normalizedYDistance * normalizedYDistance + normalizedZDistance * normalizedZDistance;
/* 362 */             if (dist <= 1.05F)
/* 363 */               placeBlock(level, fillBlock, x, y, z, chunkBB); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void fillColumnDown(WorldGenLevel level, BlockState blockState, int x, int startY, int z, BoundingBox chunkBB) {
/* 371 */     BlockPos.MutableBlockPos pos = getWorldPos(x, startY, z);
/* 372 */     if (!chunkBB.isInside((Vec3i)pos))
/*     */       return; 
/* 377 */     while (isReplaceableByStructures(level.getBlockState((BlockPos)pos)) && pos.getY() > level.getMinY() + 1) {
/* 378 */       level.setBlock((BlockPos)pos, blockState, 2);
/* 379 */       pos.move(Direction.DOWN);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isReplaceableByStructures(BlockState state) {
/* 384 */     return (state.isAir() || state.liquid() || state.is(Blocks.GLOW_LICHEN) || state.is(Blocks.SEAGRASS) || state.is(Blocks.TALL_SEAGRASS));
/*     */   }
/*     */   
/*     */   protected boolean createChest(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z, ResourceKey<LootTable> lootTable) {
/* 388 */     return createChest((ServerLevelAccessor)level, chunkBB, random, (BlockPos)getWorldPos(x, y, z), lootTable, null);
/*     */   }
/*     */   
/*     */   public static BlockState reorient(BlockGetter level, BlockPos blockPos, BlockState blockState) {
/* 393 */     Direction solidNeighbor = null;
/* 394 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 395 */       BlockPos blockPos1 = blockPos.relative(direction);
/* 396 */       BlockState state = level.getBlockState(blockPos1);
/* 397 */       if (state.is(Blocks.CHEST))
/* 398 */         return blockState; 
/* 400 */       if (state.isSolidRender()) {
/* 401 */         if (solidNeighbor == null) {
/* 402 */           solidNeighbor = direction;
/*     */           continue;
/*     */         } 
/* 404 */         solidNeighbor = null;
/*     */         break;
/*     */       } 
/*     */     } 
/* 409 */     if (solidNeighbor != null)
/* 410 */       return (BlockState)blockState.setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)solidNeighbor.getOpposite()); 
/* 414 */     Direction lockDir = (Direction)blockState.getValue((Property)HorizontalDirectionalBlock.FACING);
/* 415 */     BlockPos relativePos = blockPos.relative(lockDir);
/* 416 */     if (level.getBlockState(relativePos).isSolidRender()) {
/* 417 */       lockDir = lockDir.getOpposite();
/* 418 */       relativePos = blockPos.relative(lockDir);
/*     */     } 
/* 420 */     if (level.getBlockState(relativePos).isSolidRender()) {
/* 421 */       lockDir = lockDir.getClockWise();
/* 422 */       relativePos = blockPos.relative(lockDir);
/*     */     } 
/* 424 */     if (level.getBlockState(relativePos).isSolidRender()) {
/* 425 */       lockDir = lockDir.getOpposite();
/* 426 */       relativePos = blockPos.relative(lockDir);
/*     */     } 
/* 429 */     return (BlockState)blockState.setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)lockDir);
/*     */   }
/*     */   
/*     */   protected boolean createChest(ServerLevelAccessor level, BoundingBox chunkBB, RandomSource random, BlockPos pos, ResourceKey<LootTable> lootTable, BlockState blockState) {
/* 434 */     if (!chunkBB.isInside((Vec3i)pos) || level.getBlockState(pos).is(Blocks.CHEST))
/* 435 */       return false; 
/* 438 */     if (blockState == null)
/* 439 */       blockState = reorient((BlockGetter)level, pos, Blocks.CHEST.defaultBlockState()); 
/* 441 */     level.setBlock(pos, blockState, 2);
/* 443 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 444 */     if (blockEntity instanceof ChestBlockEntity) {
/* 444 */       ChestBlockEntity chestBlockEntity = (ChestBlockEntity)blockEntity;
/* 445 */       chestBlockEntity.setLootTable(lootTable, random.nextLong());
/*     */     } 
/* 447 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean createDispenser(WorldGenLevel level, BoundingBox chunkBB, RandomSource random, int x, int y, int z, Direction facing, ResourceKey<LootTable> lootTable) {
/* 451 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(x, y, z);
/* 453 */     if (chunkBB.isInside((Vec3i)mutableBlockPos) && 
/* 454 */       !level.getBlockState((BlockPos)mutableBlockPos).is(Blocks.DISPENSER)) {
/* 455 */       placeBlock(level, (BlockState)Blocks.DISPENSER.defaultBlockState().setValue((Property)DispenserBlock.FACING, (Comparable)facing), x, y, z, chunkBB);
/* 457 */       BlockEntity blockEntity = level.getBlockEntity((BlockPos)mutableBlockPos);
/* 458 */       if (blockEntity instanceof DispenserBlockEntity) {
/* 458 */         DispenserBlockEntity dispenserBlockEntity = (DispenserBlockEntity)blockEntity;
/* 459 */         dispenserBlockEntity.setLootTable(lootTable, random.nextLong());
/*     */       } 
/* 461 */       return true;
/*     */     } 
/* 464 */     return false;
/*     */   }
/*     */   
/*     */   public void move(int dx, int dy, int dz) {
/* 468 */     this.boundingBox.move(dx, dy, dz);
/*     */   }
/*     */   
/*     */   public static BoundingBox createBoundingBox(Stream<StructurePiece> pieces) {
/* 472 */     Objects.requireNonNull(pieces.map(StructurePiece::getBoundingBox));
/* 472 */     return BoundingBox.encapsulatingBoxes(pieces.map(StructurePiece::getBoundingBox)::iterator).<Throwable>orElseThrow(() -> new IllegalStateException("Unable to calculate boundingbox without pieces"));
/*     */   }
/*     */   
/*     */   public static StructurePiece findCollisionPiece(List<StructurePiece> pieces, BoundingBox box) {
/* 476 */     for (StructurePiece piece : pieces) {
/* 477 */       if (piece.getBoundingBox().intersects(box))
/* 478 */         return piece; 
/*     */     } 
/* 481 */     return null;
/*     */   }
/*     */   
/*     */   public Direction getOrientation() {
/* 485 */     return this.orientation;
/*     */   }
/*     */   
/*     */   public void setOrientation(Direction orientation) {
/* 489 */     this.orientation = orientation;
/* 490 */     if (orientation == null) {
/* 491 */       this.rotation = Rotation.NONE;
/* 492 */       this.mirror = Mirror.NONE;
/*     */     } else {
/* 494 */       switch (orientation) {
/*     */         case SOUTH:
/* 496 */           this.mirror = Mirror.LEFT_RIGHT;
/* 497 */           this.rotation = Rotation.NONE;
/*     */           return;
/*     */         case WEST:
/* 500 */           this.mirror = Mirror.LEFT_RIGHT;
/* 501 */           this.rotation = Rotation.CLOCKWISE_90;
/*     */           return;
/*     */         case EAST:
/* 504 */           this.mirror = Mirror.NONE;
/* 505 */           this.rotation = Rotation.CLOCKWISE_90;
/*     */           return;
/*     */       } 
/* 508 */       this.mirror = Mirror.NONE;
/* 509 */       this.rotation = Rotation.NONE;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Rotation getRotation() {
/* 516 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public Mirror getMirror() {
/* 520 */     return this.mirror;
/*     */   }
/*     */   
/*     */   public StructurePieceType getType() {
/* 524 */     return this.type;
/*     */   }
/*     */   
/*     */   protected abstract void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag);
/*     */   
/*     */   public abstract void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos);
/*     */   
/*     */   public static abstract class BlockSelector {
/* 528 */     protected BlockState next = Blocks.AIR.defaultBlockState();
/*     */     
/*     */     public abstract void next(RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean);
/*     */     
/*     */     public BlockState getNext() {
/* 533 */       return this.next;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\StructurePiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */