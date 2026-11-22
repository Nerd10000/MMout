/*     */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.IdMapper;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.data.worldgen.Pools;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.DoubleTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntitySpawnRequest;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.decoration.painting.Painting;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.EmptyBlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.JigsawBlock;
/*     */ import net.minecraft.world.level.block.LiquidBlockContainer;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
/*     */ import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class StructureTemplate {
/*  70 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String PALETTE_TAG = "palette";
/*     */   
/*     */   public static final String PALETTE_LIST_TAG = "palettes";
/*     */   
/*     */   public static final String ENTITIES_TAG = "entities";
/*     */   
/*     */   public static final String BLOCKS_TAG = "blocks";
/*     */   
/*     */   public static final String BLOCK_TAG_POS = "pos";
/*     */   
/*     */   public static final String BLOCK_TAG_STATE = "state";
/*     */   
/*     */   public static final String BLOCK_TAG_NBT = "nbt";
/*     */   
/*     */   public static final String ENTITY_TAG_POS = "pos";
/*     */   
/*     */   public static final String ENTITY_TAG_BLOCKPOS = "blockPos";
/*     */   
/*     */   public static final String ENTITY_TAG_NBT = "nbt";
/*     */   
/*     */   public static final String SIZE_TAG = "size";
/*     */   
/*  84 */   private final List<Palette> palettes = Lists.newArrayList();
/*     */   
/*  85 */   private final List<StructureEntityInfo> entityInfoList = Lists.newArrayList();
/*     */   
/*  86 */   private Vec3i size = Vec3i.ZERO;
/*     */   
/*  87 */   private String author = "?";
/*     */   
/*     */   public Vec3i getSize() {
/*  90 */     return this.size;
/*     */   }
/*     */   
/*     */   public void setAuthor(String author) {
/*  94 */     this.author = author;
/*     */   }
/*     */   
/*     */   public String getAuthor() {
/*  98 */     return this.author;
/*     */   }
/*     */   
/*     */   public void fillFromWorld(Level level, BlockPos position, Vec3i size, boolean inludeEntities, List<Block> ignoreBlocks) {
/* 102 */     if (size.getX() < 1 || size.getY() < 1 || size.getZ() < 1)
/*     */       return; 
/* 105 */     BlockPos corner2 = position.offset(size).offset(-1, -1, -1);
/* 106 */     List<StructureBlockInfo> fullBlockList = Lists.newArrayList();
/* 107 */     List<StructureBlockInfo> blockEntitiesList = Lists.newArrayList();
/* 108 */     List<StructureBlockInfo> otherBlocksList = Lists.newArrayList();
/* 110 */     BlockPos minCorner = new BlockPos(Math.min(position.getX(), corner2.getX()), Math.min(position.getY(), corner2.getY()), Math.min(position.getZ(), corner2.getZ()));
/* 111 */     BlockPos maxCorner = new BlockPos(Math.max(position.getX(), corner2.getX()), Math.max(position.getY(), corner2.getY()), Math.max(position.getZ(), corner2.getZ()));
/* 112 */     this.size = size;
/* 114 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER);
/*     */     try {
/* 115 */       for (BlockPos pos : BlockPos.betweenClosed(minCorner, maxCorner)) {
/*     */         StructureBlockInfo info;
/* 116 */         BlockPos relativePos = pos.subtract((Vec3i)minCorner);
/* 117 */         BlockState blockState = level.getBlockState(pos);
/* 118 */         Objects.requireNonNull(blockState);
/* 118 */         if (ignoreBlocks.stream().anyMatch(blockState::is))
/*     */           continue; 
/* 121 */         BlockEntity blockEntity = level.getBlockEntity(pos);
/* 125 */         if (blockEntity != null) {
/* 126 */           TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)level.registryAccess());
/* 127 */           blockEntity.saveWithId((ValueOutput)output);
/* 128 */           info = new StructureBlockInfo(relativePos, blockState, output.buildResult());
/*     */         } else {
/* 130 */           info = new StructureBlockInfo(relativePos, blockState, null);
/*     */         } 
/* 133 */         addToLists(info, fullBlockList, blockEntitiesList, otherBlocksList);
/*     */       } 
/* 135 */       List<StructureBlockInfo> blockInfoList = buildInfoList(fullBlockList, blockEntitiesList, otherBlocksList);
/* 137 */       this.palettes.clear();
/* 138 */       this.palettes.add(new Palette(blockInfoList));
/* 140 */       if (inludeEntities) {
/* 141 */         fillEntityList(level, minCorner, maxCorner, (ProblemReporter)reporter);
/*     */       } else {
/* 143 */         this.entityInfoList.clear();
/*     */       } 
/* 145 */       reporter.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void addToLists(StructureBlockInfo info, List<StructureBlockInfo> fullBlockList, List<StructureBlockInfo> blockEntitiesList, List<StructureBlockInfo> otherBlocksList) {
/* 149 */     if (info.nbt != null) {
/* 150 */       blockEntitiesList.add(info);
/* 151 */     } else if (!info.state.getBlock().hasDynamicShape() && info.state.isCollisionShapeFullBlock((BlockGetter)EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) {
/* 152 */       fullBlockList.add(info);
/*     */     } else {
/* 154 */       otherBlocksList.add(info);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<StructureBlockInfo> buildInfoList(List<StructureBlockInfo> fullBlockList, List<StructureBlockInfo> blockEntitiesList, List<StructureBlockInfo> otherBlocksList) {
/* 160 */     Comparator<StructureBlockInfo> comparator = Comparator.<StructureBlockInfo>comparingInt(o -> o.pos.getY()).thenComparingInt(o -> o.pos.getX()).thenComparingInt(o -> o.pos.getZ());
/* 161 */     fullBlockList.sort(comparator);
/* 162 */     otherBlocksList.sort(comparator);
/* 163 */     blockEntitiesList.sort(comparator);
/* 165 */     List<StructureBlockInfo> blockInfoList = Lists.newArrayList();
/* 166 */     blockInfoList.addAll(fullBlockList);
/* 167 */     blockInfoList.addAll(otherBlocksList);
/* 168 */     blockInfoList.addAll(blockEntitiesList);
/* 169 */     return blockInfoList;
/*     */   }
/*     */   
/*     */   private void fillEntityList(Level level, BlockPos minCorner, BlockPos maxCorner, ProblemReporter reporter) {
/* 173 */     List<Entity> entities = level.getEntitiesOfClass(Entity.class, AABB.encapsulatingFullBlocks(minCorner, maxCorner), input -> !(input instanceof net.minecraft.world.entity.player.Player));
/* 174 */     this.entityInfoList.clear();
/* 176 */     for (Entity entity : entities) {
/*     */       BlockPos blockPos;
/* 177 */       Vec3 pos = new Vec3(entity.getX() - minCorner.getX(), entity.getY() - minCorner.getY(), entity.getZ() - minCorner.getZ());
/* 178 */       TagValueOutput output = TagValueOutput.createWithContext(reporter.forChild(entity.problemPath()), (HolderLookup.Provider)entity.registryAccess());
/* 179 */       entity.save((ValueOutput)output);
/* 181 */       if (entity instanceof Painting) {
/* 181 */         Painting painting = (Painting)entity;
/* 182 */         blockPos = painting.getPos().subtract((Vec3i)minCorner);
/*     */       } else {
/* 184 */         blockPos = BlockPos.containing((Position)pos);
/*     */       } 
/* 187 */       this.entityInfoList.add(new StructureEntityInfo(pos, blockPos, output.buildResult().copy()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<StructureBlockInfo> filterBlocks(BlockPos position, StructurePlaceSettings settings, Block block) {
/* 192 */     return (List<StructureBlockInfo>)filterBlocks(position, settings, block, true);
/*     */   }
/*     */   
/*     */   public List<JigsawBlockInfo> getJigsaws(BlockPos position, Rotation rotation) {
/* 197 */     if (this.palettes.isEmpty())
/* 198 */       return new ArrayList<>(); 
/* 201 */     StructurePlaceSettings settings = (new StructurePlaceSettings()).setRotation(rotation);
/* 202 */     List<JigsawBlockInfo> jigsaws = settings.getRandomPalette(this.palettes, position).jigsaws();
/* 205 */     List<JigsawBlockInfo> result = new ArrayList<>(jigsaws.size());
/* 206 */     for (JigsawBlockInfo jigsaw : jigsaws) {
/* 207 */       StructureBlockInfo blockInfo = jigsaw.info;
/* 208 */       result.add(jigsaw.withInfo(new StructureBlockInfo(calculateRelativePosition(settings, blockInfo.pos()).offset((Vec3i)position), blockInfo.state.rotate(settings.getRotation()), blockInfo.nbt)));
/*     */     } 
/* 210 */     return result;
/*     */   }
/*     */   
/*     */   public ObjectArrayList<StructureBlockInfo> filterBlocks(BlockPos position, StructurePlaceSettings settings, Block block, boolean absolute) {
/* 214 */     ObjectArrayList<StructureBlockInfo> result = new ObjectArrayList();
/* 215 */     BoundingBox boundingBox = settings.getBoundingBox();
/* 217 */     if (this.palettes.isEmpty())
/* 218 */       return result; 
/* 220 */     for (StructureBlockInfo blockInfo : settings.getRandomPalette(this.palettes, position).blocks(block)) {
/* 221 */       BlockPos blockPos = absolute ? calculateRelativePosition(settings, blockInfo.pos).offset((Vec3i)position) : blockInfo.pos;
/* 222 */       if (boundingBox != null && !boundingBox.isInside((Vec3i)blockPos))
/*     */         continue; 
/* 225 */       result.add(new StructureBlockInfo(blockPos, blockInfo.state.rotate(settings.getRotation()), blockInfo.nbt));
/*     */     } 
/* 227 */     return result;
/*     */   }
/*     */   
/*     */   public BlockPos calculateConnectedPosition(StructurePlaceSettings settings1, BlockPos connection1, StructurePlaceSettings settings2, BlockPos connection2) {
/* 231 */     BlockPos markerPos1 = calculateRelativePosition(settings1, connection1);
/* 232 */     BlockPos markerPos2 = calculateRelativePosition(settings2, connection2);
/* 233 */     return markerPos1.subtract((Vec3i)markerPos2);
/*     */   }
/*     */   
/*     */   public static BlockPos calculateRelativePosition(StructurePlaceSettings settings, BlockPos pos) {
/* 237 */     return transform(pos, settings.getMirror(), settings.getRotation(), settings.getRotationPivot());
/*     */   }
/*     */   
/*     */   public boolean placeInWorld(ServerLevelAccessor level, BlockPos position, BlockPos referencePos, StructurePlaceSettings settings, RandomSource random, int updateMode) {
/* 241 */     if (this.palettes.isEmpty())
/* 242 */       return false; 
/* 244 */     List<StructureBlockInfo> blockInfoList = settings.getRandomPalette(this.palettes, position).blocks();
/* 245 */     if ((blockInfoList.isEmpty() && (settings.isIgnoreEntities() || this.entityInfoList.isEmpty())) || this.size.getX() < 1 || this.size.getY() < 1 || this.size.getZ() < 1)
/* 246 */       return false; 
/* 249 */     BoundingBox boundingBox = settings.getBoundingBox();
/* 250 */     List<BlockPos> toFill = Lists.newArrayListWithCapacity(settings.shouldApplyWaterlogging() ? blockInfoList.size() : 0);
/* 251 */     List<BlockPos> lockedFluids = Lists.newArrayListWithCapacity(settings.shouldApplyWaterlogging() ? blockInfoList.size() : 0);
/* 252 */     List<Pair<BlockPos, CompoundTag>> placed = Lists.newArrayListWithCapacity(blockInfoList.size());
/* 254 */     int minX = Integer.MAX_VALUE;
/* 255 */     int minY = Integer.MAX_VALUE;
/* 256 */     int minZ = Integer.MAX_VALUE;
/* 258 */     int maxX = Integer.MIN_VALUE;
/* 259 */     int maxY = Integer.MIN_VALUE;
/* 260 */     int maxZ = Integer.MIN_VALUE;
/* 262 */     List<StructureBlockInfo> processedBlockInfoList = processBlockInfos(level, position, referencePos, settings, blockInfoList);
/* 264 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER);
/*     */     try {
/* 265 */       for (StructureBlockInfo blockInfo : processedBlockInfoList) {
/* 266 */         BlockPos blockPos = blockInfo.pos;
/* 268 */         if (boundingBox != null && !boundingBox.isInside((Vec3i)blockPos))
/*     */           continue; 
/* 272 */         FluidState previousFluidState = settings.shouldApplyWaterlogging() ? level.getFluidState(blockPos) : null;
/* 273 */         BlockState state = blockInfo.state.mirror(settings.getMirror()).rotate(settings.getRotation());
/* 275 */         if (blockInfo.nbt != null)
/* 280 */           level.setBlock(blockPos, Blocks.BARRIER.defaultBlockState(), 820); 
/* 282 */         if (level.setBlock(blockPos, state, updateMode)) {
/* 283 */           minX = Math.min(minX, blockPos.getX());
/* 284 */           minY = Math.min(minY, blockPos.getY());
/* 285 */           minZ = Math.min(minZ, blockPos.getZ());
/* 287 */           maxX = Math.max(maxX, blockPos.getX());
/* 288 */           maxY = Math.max(maxY, blockPos.getY());
/* 289 */           maxZ = Math.max(maxZ, blockPos.getZ());
/* 290 */           placed.add(Pair.of(blockPos, blockInfo.nbt));
/* 292 */           if (blockInfo.nbt != null) {
/* 293 */             BlockEntity blockEntity = level.getBlockEntity(blockPos);
/* 294 */             if (blockEntity != null) {
/* 295 */               if (!SharedConstants.DEBUG_STRUCTURE_EDIT_MODE && blockEntity instanceof net.minecraft.world.RandomizableContainer)
/* 296 */                 blockInfo.nbt.putLong("LootTableSeed", random.nextLong()); 
/* 298 */               blockEntity.loadWithComponents(TagValueInput.create(reporter.forChild(blockEntity.problemPath()), (HolderLookup.Provider)level.registryAccess(), blockInfo.nbt));
/*     */             } 
/*     */           } 
/* 301 */           if (previousFluidState != null) {
/* 302 */             if (state.getFluidState().isSource()) {
/* 304 */               lockedFluids.add(blockPos);
/*     */               continue;
/*     */             } 
/* 305 */             if (state.getBlock() instanceof LiquidBlockContainer) {
/* 307 */               ((LiquidBlockContainer)state.getBlock()).placeLiquid((LevelAccessor)level, blockPos, state, previousFluidState);
/* 308 */               if (!previousFluidState.isSource())
/* 310 */                 toFill.add(blockPos); 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 317 */       boolean filled = true;
/* 318 */       Direction[] directions = { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
/* 320 */       while (filled && !toFill.isEmpty()) {
/* 321 */         filled = false;
/* 322 */         for (Iterator<BlockPos> iterator = toFill.iterator(); iterator.hasNext(); ) {
/* 323 */           BlockPos pos = iterator.next();
/* 325 */           FluidState toPlace = level.getFluidState(pos);
/* 326 */           for (int i = 0; i < directions.length && !toPlace.isSource(); i++) {
/* 327 */             BlockPos neighborPos = pos.relative(directions[i]);
/* 328 */             FluidState neighbor = level.getFluidState(neighborPos);
/* 329 */             if (neighbor.isSource() && !lockedFluids.contains(neighborPos))
/* 330 */               toPlace = neighbor; 
/*     */           } 
/* 334 */           if (toPlace.isSource()) {
/* 335 */             BlockState state = level.getBlockState(pos);
/* 336 */             Block block = state.getBlock();
/* 337 */             if (block instanceof LiquidBlockContainer) {
/* 337 */               LiquidBlockContainer liquidBlockContainer = (LiquidBlockContainer)block;
/* 338 */               liquidBlockContainer.placeLiquid((LevelAccessor)level, pos, state, toPlace);
/* 339 */               filled = true;
/* 340 */               iterator.remove();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 346 */       if (minX <= maxX) {
/* 347 */         if (!settings.getKnownShape()) {
/* 348 */           BitSetDiscreteVoxelShape bitSetDiscreteVoxelShape = new BitSetDiscreteVoxelShape(maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1);
/* 350 */           int startX = minX;
/* 351 */           int startY = minY;
/* 352 */           int startZ = minZ;
/* 354 */           for (Pair<BlockPos, CompoundTag> blockInfo : placed) {
/* 355 */             BlockPos blockPos = (BlockPos)blockInfo.getFirst();
/* 356 */             bitSetDiscreteVoxelShape.fill(blockPos.getX() - startX, blockPos.getY() - startY, blockPos.getZ() - startZ);
/*     */           } 
/* 359 */           updateShapeAtEdge((LevelAccessor)level, updateMode, (DiscreteVoxelShape)bitSetDiscreteVoxelShape, startX, startY, startZ);
/*     */         } 
/* 362 */         for (Pair<BlockPos, CompoundTag> blockInfo : placed) {
/* 363 */           BlockPos blockPos = (BlockPos)blockInfo.getFirst();
/* 364 */           if (!settings.getKnownShape()) {
/* 365 */             BlockState state = level.getBlockState(blockPos);
/* 366 */             BlockState newState = Block.updateFromNeighbourShapes(state, (LevelAccessor)level, blockPos);
/* 367 */             if (state != newState)
/* 368 */               level.setBlock(blockPos, newState, updateMode & 0xFFFFFFFE | 0x10); 
/* 370 */             level.updateNeighborsAt(blockPos, newState.getBlock());
/*     */           } 
/* 373 */           if (blockInfo.getSecond() != null) {
/* 374 */             BlockEntity blockEntity = level.getBlockEntity(blockPos);
/* 375 */             if (blockEntity != null)
/* 376 */               blockEntity.setChanged(); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 382 */       if (!settings.isIgnoreEntities())
/* 383 */         placeEntities(level, position, settings.getMirror(), settings.getRotation(), settings.getRotationPivot(), boundingBox, settings.shouldFinalizeEntities(), (ProblemReporter)reporter); 
/* 385 */       reporter.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/* 387 */     return true;
/*     */   }
/*     */   
/*     */   public static void updateShapeAtEdge(LevelAccessor level, int updateMode, DiscreteVoxelShape shape, BlockPos pos) {
/* 391 */     updateShapeAtEdge(level, updateMode, shape, pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public static void updateShapeAtEdge(LevelAccessor level, int updateMode, DiscreteVoxelShape shape, int startX, int startY, int startZ) {
/* 395 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 396 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/* 397 */     shape.forAllFaces((direction, x, y, z) -> {
/*     */           pos.set(startX + x, startY + y, startZ + z);
/*     */           neighborPos.setWithOffset((Vec3i)pos, direction);
/*     */           BlockState state = level.getBlockState((BlockPos)pos);
/*     */           BlockState neighborState = level.getBlockState((BlockPos)neighborPos);
/*     */           BlockState newState = state.updateShape((LevelReader)level, (ScheduledTickAccess)level, (BlockPos)pos, direction, (BlockPos)neighborPos, neighborState, level.getRandom());
/*     */           if (state != newState)
/*     */             level.setBlock((BlockPos)pos, newState, updateMode & 0xFFFFFFFE); 
/*     */           BlockState newNeighborState = neighborState.updateShape((LevelReader)level, (ScheduledTickAccess)level, (BlockPos)neighborPos, direction.getOpposite(), (BlockPos)pos, newState, level.getRandom());
/*     */           if (neighborState != newNeighborState)
/*     */             level.setBlock((BlockPos)neighborPos, newNeighborState, updateMode & 0xFFFFFFFE); 
/*     */         });
/*     */   }
/*     */   
/*     */   public static List<StructureBlockInfo> processBlockInfos(ServerLevelAccessor level, BlockPos position, BlockPos referencePos, StructurePlaceSettings settings, List<StructureBlockInfo> blockInfoList) {
/* 415 */     List<StructureBlockInfo> originalBlockInfoList = new ArrayList<>();
/* 416 */     List<StructureBlockInfo> processedBlockInfoList = new ArrayList<>();
/* 418 */     boolean processOnlyInCurrentChunk = true;
/* 419 */     for (StructureProcessor processor : settings.getProcessors()) {
/* 420 */       if (processor.evaluatesEntirePieceState()) {
/* 421 */         processOnlyInCurrentChunk = false;
/*     */         break;
/*     */       } 
/*     */     } 
/* 426 */     BoundingBox chunkBb = settings.getBoundingBox();
/* 428 */     for (StructureBlockInfo blockInfo : blockInfoList) {
/* 429 */       BlockPos blockPos = calculateRelativePosition(settings, blockInfo.pos).offset((Vec3i)position);
/* 431 */       if (processOnlyInCurrentChunk && chunkBb != null && !chunkBb.isInside((Vec3i)blockPos))
/*     */         continue; 
/* 435 */       StructureBlockInfo processedBlockInfo = new StructureBlockInfo(blockPos, blockInfo.state, (blockInfo.nbt != null) ? blockInfo.nbt.copy() : null);
/* 437 */       Iterator<StructureProcessor> iterator = settings.getProcessors().iterator();
/* 438 */       while (processedBlockInfo != null && iterator.hasNext())
/* 439 */         processedBlockInfo = ((StructureProcessor)iterator.next()).processBlock((LevelReader)level, position, referencePos, blockInfo.pos, processedBlockInfo, settings); 
/* 442 */       if (processedBlockInfo != null) {
/* 443 */         processedBlockInfoList.add(processedBlockInfo);
/* 444 */         originalBlockInfoList.add(blockInfo);
/*     */       } 
/*     */     } 
/* 448 */     for (StructureProcessor processor : settings.getProcessors())
/* 449 */       processedBlockInfoList = processor.finalizeProcessing(level, position, referencePos, originalBlockInfoList, processedBlockInfoList, settings); 
/* 452 */     return processedBlockInfoList;
/*     */   }
/*     */   
/*     */   private void placeEntities(ServerLevelAccessor level, BlockPos position, Mirror mirror, Rotation rotation, BlockPos pivot, BoundingBox boundingBox, boolean finalizeEntities, ProblemReporter problemReporter) {
/* 456 */     for (StructureEntityInfo entityInfo : this.entityInfoList) {
/* 457 */       BlockPos blockPos = transform(entityInfo.blockPos, mirror, rotation, pivot).offset((Vec3i)position);
/* 458 */       if (boundingBox != null && !boundingBox.isInside((Vec3i)blockPos))
/*     */         continue; 
/* 462 */       CompoundTag tag = entityInfo.nbt.copy();
/* 463 */       Vec3 relativePos = transform(entityInfo.pos, mirror, rotation, pivot);
/* 464 */       Vec3 pos = relativePos.add(position.getX(), position.getY(), position.getZ());
/* 466 */       ListTag posTag = new ListTag();
/* 467 */       posTag.add(DoubleTag.valueOf(pos.x));
/* 468 */       posTag.add(DoubleTag.valueOf(pos.y));
/* 469 */       posTag.add(DoubleTag.valueOf(pos.z));
/* 470 */       tag.put("Pos", (Tag)posTag);
/* 472 */       tag.remove("UUID");
/* 474 */       createEntityIgnoreException(problemReporter, level, tag).ifPresent(entity -> {
/*     */             float yRot = entity.rotate(rotation);
/*     */             yRot += entity.mirror(mirror) - entity.getYRot();
/*     */             entity.snapTo(pos.x, pos.y, pos.z, yRot, entity.getXRot());
/*     */             entity.setYBodyRot(yRot);
/*     */             entity.setYHeadRot(yRot);
/*     */             if (finalizeEntities && entity instanceof Mob) {
/*     */               Mob mob = (Mob)entity;
/*     */               mob.finalizeSpawn(level, level.getCurrentDifficultyAt(BlockPos.containing((Position)pos)), EntitySpawnReason.STRUCTURE, null);
/*     */             } 
/*     */             level.addFreshEntityWithPassengers(entity);
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<Entity> createEntityIgnoreException(ProblemReporter reporter, ServerLevelAccessor level, CompoundTag tag) {
/*     */     try {
/* 491 */       return EntityType.create(TagValueInput.create(reporter, (HolderLookup.Provider)level.registryAccess(), tag), (Level)level.getLevel(), new EntitySpawnRequest(EntitySpawnReason.STRUCTURE, false));
/* 492 */     } catch (Exception ignored) {
/* 493 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Vec3i getSize(Rotation rotation) {
/* 498 */     switch (rotation) {
/*     */       case LEFT_RIGHT:
/*     */       case FRONT_BACK:
/*     */       
/*     */     } 
/* 498 */     return 
/*     */       
/* 500 */       this.size;
/*     */   }
/*     */   
/*     */   public static BlockPos transform(BlockPos pos, Mirror mirror, Rotation rotation, BlockPos pivot) {
/* 505 */     int x = pos.getX();
/* 506 */     int y = pos.getY();
/* 507 */     int z = pos.getZ();
/* 509 */     boolean wasMirrored = true;
/* 510 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 511 */         z = -z;
/*     */         break;
/*     */       case FRONT_BACK:
/* 512 */         x = -x;
/*     */         break;
/*     */       default:
/* 513 */         wasMirrored = false;
/*     */         break;
/*     */     } 
/* 516 */     int pivotX = pivot.getX();
/* 517 */     int pivotZ = pivot.getZ();
/* 518 */     switch (rotation) {
/*     */       case null:
/*     */       
/*     */       case LEFT_RIGHT:
/*     */       
/*     */       case FRONT_BACK:
/*     */       
/*     */     } 
/* 518 */     return 
/*     */       
/* 522 */       wasMirrored ? new BlockPos(x, y, z) : pos;
/*     */   }
/*     */   
/*     */   public static Vec3 transform(Vec3 pos, Mirror mirror, Rotation rotation, BlockPos pivot) {
/* 527 */     double x = pos.x;
/* 528 */     double y = pos.y;
/* 529 */     double z = pos.z;
/* 531 */     boolean wasMirrored = true;
/* 532 */     switch (mirror) {
/*     */       case LEFT_RIGHT:
/* 533 */         z = 1.0D - z;
/*     */         break;
/*     */       case FRONT_BACK:
/* 534 */         x = 1.0D - x;
/*     */         break;
/*     */       default:
/* 535 */         wasMirrored = false;
/*     */         break;
/*     */     } 
/* 538 */     int pivotX = pivot.getX();
/* 539 */     int pivotZ = pivot.getZ();
/* 540 */     switch (rotation) {
/*     */       case null:
/*     */       
/*     */       case LEFT_RIGHT:
/*     */       
/*     */       case FRONT_BACK:
/*     */       
/*     */     } 
/* 540 */     return 
/*     */       
/* 544 */       wasMirrored ? new Vec3(x, y, z) : pos;
/*     */   }
/*     */   
/*     */   public BlockPos getZeroPositionWithTransform(BlockPos zeroPos, Mirror mirror, Rotation rotation) {
/* 549 */     return getZeroPositionWithTransform(zeroPos, mirror, rotation, getSize().getX(), getSize().getZ());
/*     */   }
/*     */   
/*     */   public static BlockPos getZeroPositionWithTransform(BlockPos zeroPos, Mirror mirror, Rotation rotation, int sizeX, int sizeZ) {
/* 553 */     sizeX--;
/* 554 */     sizeZ--;
/* 556 */     int mirrorDeltaX = (mirror == Mirror.FRONT_BACK) ? sizeX : 0;
/* 557 */     int mirrorDeltaZ = (mirror == Mirror.LEFT_RIGHT) ? sizeZ : 0;
/* 559 */     BlockPos targetPos = zeroPos;
/* 561 */     switch (rotation) {
/*     */       default:
/* 561 */         throw new MatchException(null, null);
/*     */       case null:
/*     */       
/*     */       case FRONT_BACK:
/*     */       
/*     */       case null:
/*     */       
/*     */       case LEFT_RIGHT:
/*     */         break;
/*     */     } 
/* 561 */     targetPos = 
/*     */       
/* 565 */       zeroPos.offset(mirrorDeltaZ, 0, sizeX - mirrorDeltaX);
/* 567 */     return targetPos;
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox(StructurePlaceSettings settings, BlockPos position) {
/* 571 */     return getBoundingBox(position, settings.getRotation(), settings.getRotationPivot(), settings.getMirror());
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox(BlockPos position, Rotation rotation, BlockPos pivot, Mirror mirror) {
/* 575 */     return getBoundingBox(position, rotation, pivot, mirror, this.size);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static BoundingBox getBoundingBox(BlockPos position, Rotation rotation, BlockPos pivot, Mirror mirror, Vec3i size) {
/* 580 */     Vec3i delta = size.offset(-1, -1, -1);
/* 581 */     BlockPos corner1 = transform(BlockPos.ZERO, mirror, rotation, pivot);
/* 582 */     BlockPos corner2 = transform(BlockPos.ZERO.offset(delta), mirror, rotation, pivot);
/* 583 */     return BoundingBox.fromCorners((Vec3i)corner1, (Vec3i)corner2).move((Vec3i)position);
/*     */   }
/*     */   
/*     */   private static class SimplePalette implements Iterable<BlockState> {
/* 587 */     public static final BlockState DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
/*     */     
/* 589 */     private final IdMapper<BlockState> ids = new IdMapper(16);
/*     */     
/*     */     private int lastId;
/*     */     
/*     */     public int idFor(BlockState state) {
/* 593 */       int id = this.ids.getId(state);
/* 594 */       if (id == -1) {
/* 595 */         id = this.lastId++;
/* 596 */         this.ids.addMapping(state, id);
/*     */       } 
/* 599 */       return id;
/*     */     }
/*     */     
/*     */     public BlockState stateFor(int index) {
/* 603 */       BlockState blockState = (BlockState)this.ids.byId(index);
/* 604 */       return (blockState == null) ? DEFAULT_BLOCK_STATE : blockState;
/*     */     }
/*     */     
/*     */     public Iterator<BlockState> iterator() {
/* 609 */       return this.ids.iterator();
/*     */     }
/*     */     
/*     */     public void addMapping(BlockState state, int id) {
/* 613 */       this.ids.addMapping(state, id);
/*     */     }
/*     */   }
/*     */   
/*     */   public CompoundTag save(CompoundTag tag) {
/* 618 */     if (this.palettes.isEmpty()) {
/* 619 */       tag.put("blocks", (Tag)new ListTag());
/* 620 */       tag.put("palette", (Tag)new ListTag());
/*     */     } else {
/* 622 */       List<SimplePalette> palettes = Lists.newArrayList();
/* 623 */       SimplePalette mainPalette = new SimplePalette();
/* 624 */       palettes.add(mainPalette);
/* 626 */       for (int p = 1; p < this.palettes.size(); p++)
/* 627 */         palettes.add(new SimplePalette()); 
/* 630 */       ListTag blockList = new ListTag();
/* 631 */       List<StructureBlockInfo> mainPaletteBlocks = ((Palette)this.palettes.get(0)).blocks();
/* 632 */       for (int i = 0; i < mainPaletteBlocks.size(); i++) {
/* 633 */         StructureBlockInfo blockInfo = mainPaletteBlocks.get(i);
/* 634 */         CompoundTag blockTag = new CompoundTag();
/* 635 */         blockTag.put("pos", (Tag)newIntegerList(new int[] { blockInfo.pos.getX(), blockInfo.pos.getY(), blockInfo.pos.getZ() }));
/* 636 */         int id = mainPalette.idFor(blockInfo.state);
/* 637 */         blockTag.putInt("state", id);
/* 638 */         if (blockInfo.nbt != null)
/* 639 */           blockTag.put("nbt", (Tag)blockInfo.nbt); 
/* 641 */         blockList.add(blockTag);
/* 643 */         for (int j = 1; j < this.palettes.size(); j++) {
/* 644 */           SimplePalette palette = palettes.get(j);
/* 645 */           palette.addMapping(((StructureBlockInfo)((Palette)this.palettes.get(j)).blocks().get(i)).state, id);
/*     */         } 
/*     */       } 
/* 648 */       tag.put("blocks", (Tag)blockList);
/* 650 */       if (palettes.size() == 1) {
/* 651 */         ListTag paletteList = new ListTag();
/* 652 */         for (BlockState state : mainPalette)
/* 653 */           paletteList.add(NbtUtils.writeBlockState(state)); 
/* 655 */         tag.put("palette", (Tag)paletteList);
/*     */       } else {
/* 657 */         ListTag paletteListList = new ListTag();
/* 658 */         for (SimplePalette palette : palettes) {
/* 659 */           ListTag paletteList = new ListTag();
/* 660 */           for (BlockState state : palette)
/* 661 */             paletteList.add(NbtUtils.writeBlockState(state)); 
/* 663 */           paletteListList.add(paletteList);
/*     */         } 
/* 665 */         tag.put("palettes", (Tag)paletteListList);
/*     */       } 
/*     */     } 
/* 669 */     ListTag entityList = new ListTag();
/* 670 */     for (StructureEntityInfo entityInfo : this.entityInfoList) {
/* 671 */       CompoundTag entityTag = new CompoundTag();
/* 672 */       entityTag.put("pos", (Tag)newDoubleList(new double[] { entityInfo.pos.x, entityInfo.pos.y, entityInfo.pos.z }));
/* 673 */       entityTag.put("blockPos", (Tag)newIntegerList(new int[] { entityInfo.blockPos.getX(), entityInfo.blockPos.getY(), entityInfo.blockPos.getZ() }));
/* 674 */       if (entityInfo.nbt != null)
/* 675 */         entityTag.put("nbt", (Tag)entityInfo.nbt); 
/* 677 */       entityList.add(entityTag);
/*     */     } 
/* 680 */     tag.put("entities", (Tag)entityList);
/* 681 */     tag.put("size", (Tag)newIntegerList(new int[] { this.size.getX(), this.size.getY(), this.size.getZ() }));
/* 682 */     return NbtUtils.addCurrentDataVersion(tag);
/*     */   }
/*     */   
/*     */   public void load(HolderGetter<Block> blockLookup, CompoundTag tag) {
/* 686 */     this.palettes.clear();
/* 687 */     this.entityInfoList.clear();
/* 689 */     ListTag sizeTag = tag.getListOrEmpty("size");
/* 690 */     this.size = new Vec3i(sizeTag.getIntOr(0, 0), sizeTag.getIntOr(1, 0), sizeTag.getIntOr(2, 0));
/* 692 */     ListTag blockList = tag.getListOrEmpty("blocks");
/* 694 */     Optional<ListTag> paletteListList = tag.getList("palettes");
/* 695 */     if (paletteListList.isPresent()) {
/* 696 */       for (int p = 0; p < ((ListTag)paletteListList.get()).size(); p++)
/* 697 */         loadPalette(blockLookup, ((ListTag)paletteListList.get()).getListOrEmpty(p), blockList); 
/*     */     } else {
/* 700 */       loadPalette(blockLookup, tag.getListOrEmpty("palette"), blockList);
/*     */     } 
/* 703 */     tag.getListOrEmpty("entities").compoundStream().forEach(entityTag -> {
/*     */           ListTag posTag = entityTag.getListOrEmpty("pos");
/*     */           Vec3 pos = new Vec3(posTag.getDoubleOr(0, 0.0D), posTag.getDoubleOr(1, 0.0D), posTag.getDoubleOr(2, 0.0D));
/*     */           ListTag blockPosTag = entityTag.getListOrEmpty("blockPos");
/*     */           BlockPos blockPos = new BlockPos(blockPosTag.getIntOr(0, 0), blockPosTag.getIntOr(1, 0), blockPosTag.getIntOr(2, 0));
/*     */           entityTag.getCompound("nbt").ifPresent(());
/*     */         });
/*     */   }
/*     */   
/*     */   private void loadPalette(HolderGetter<Block> blockLookup, ListTag paletteList, ListTag blockList) {
/* 715 */     SimplePalette palette = new SimplePalette();
/* 717 */     for (int i = 0; i < paletteList.size(); i++)
/* 718 */       palette.addMapping(NbtUtils.readBlockState(blockLookup, paletteList.getCompoundOrEmpty(i)), i); 
/* 721 */     List<StructureBlockInfo> fullBlockList = Lists.newArrayList();
/* 722 */     List<StructureBlockInfo> blockEntitiesList = Lists.newArrayList();
/* 723 */     List<StructureBlockInfo> otherBlocksList = Lists.newArrayList();
/* 725 */     blockList.compoundStream().forEach(blockTag -> {
/*     */           ListTag posTag = blockTag.getListOrEmpty("pos");
/*     */           BlockPos pos = new BlockPos(posTag.getIntOr(0, 0), posTag.getIntOr(1, 0), posTag.getIntOr(2, 0));
/*     */           BlockState state = palette.stateFor(blockTag.getIntOr("state", 0));
/*     */           CompoundTag nbt = blockTag.getCompound("nbt").orElse(null);
/*     */           StructureBlockInfo info = new StructureBlockInfo(pos, state, nbt);
/*     */           addToLists(info, fullBlockList, blockEntitiesList, otherBlocksList);
/*     */         });
/* 735 */     List<StructureBlockInfo> blockInfoList = buildInfoList(fullBlockList, blockEntitiesList, otherBlocksList);
/* 737 */     this.palettes.add(new Palette(blockInfoList));
/*     */   }
/*     */   
/*     */   private ListTag newIntegerList(int... values) {
/* 741 */     ListTag res = new ListTag();
/* 742 */     for (int value : values)
/* 743 */       res.add(IntTag.valueOf(value)); 
/* 745 */     return res;
/*     */   }
/*     */   
/*     */   private ListTag newDoubleList(double... values) {
/* 749 */     ListTag res = new ListTag();
/* 750 */     for (double value : values)
/* 751 */       res.add(DoubleTag.valueOf(value)); 
/* 753 */     return res;
/*     */   }
/*     */   
/*     */   public static final class StructureBlockInfo extends Record {
/*     */     private final BlockPos pos;
/*     */     
/*     */     private final BlockState state;
/*     */     
/*     */     private final CompoundTag nbt;
/*     */     
/*     */     public StructureBlockInfo(BlockPos pos, BlockState state, CompoundTag nbt) {
/* 756 */       this.pos = pos;
/* 756 */       this.state = state;
/* 756 */       this.nbt = nbt;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #756	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #756	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$StructureBlockInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public BlockPos pos() {
/* 756 */       return this.pos;
/*     */     }
/*     */     
/*     */     public BlockState state() {
/* 756 */       return this.state;
/*     */     }
/*     */     
/*     */     public CompoundTag nbt() {
/* 756 */       return this.nbt;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 759 */       return String.format(Locale.ROOT, "<StructureBlockInfo | %s | %s | %s>", new Object[] { this.pos, this.state, this.nbt });
/*     */     }
/*     */   }
/*     */   
/*     */   public static JigsawBlockEntity.JointType getJointType(CompoundTag nbt, BlockState state) {
/* 764 */     return nbt.read("joint", (Codec)JigsawBlockEntity.JointType.CODEC).orElseGet(() -> getDefaultJointType(state));
/*     */   }
/*     */   
/*     */   public static JigsawBlockEntity.JointType getDefaultJointType(BlockState state) {
/* 768 */     return JigsawBlock.getFrontFacing(state).getAxis().isHorizontal() ? JigsawBlockEntity.JointType.ALIGNED : JigsawBlockEntity.JointType.ROLLABLE;
/*     */   }
/*     */   
/*     */   public static final class JigsawBlockInfo extends Record {
/*     */     private final StructureTemplate.StructureBlockInfo info;
/*     */     
/*     */     private final JigsawBlockEntity.JointType jointType;
/*     */     
/*     */     private final Identifier name;
/*     */     
/*     */     private final ResourceKey<StructureTemplatePool> pool;
/*     */     
/*     */     private final Identifier target;
/*     */     
/*     */     private final int placementPriority;
/*     */     
/*     */     private final int selectionPriority;
/*     */     
/*     */     public JigsawBlockInfo(StructureTemplate.StructureBlockInfo info, JigsawBlockEntity.JointType jointType, Identifier name, ResourceKey<StructureTemplatePool> pool, Identifier target, int placementPriority, int selectionPriority) {
/* 771 */       this.info = info;
/* 771 */       this.jointType = jointType;
/* 771 */       this.name = name;
/* 771 */       this.pool = pool;
/* 771 */       this.target = target;
/* 771 */       this.placementPriority = placementPriority;
/* 771 */       this.selectionPriority = selectionPriority;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #771	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #771	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplate$JigsawBlockInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public StructureTemplate.StructureBlockInfo info() {
/* 771 */       return this.info;
/*     */     }
/*     */     
/*     */     public JigsawBlockEntity.JointType jointType() {
/* 771 */       return this.jointType;
/*     */     }
/*     */     
/*     */     public Identifier name() {
/* 771 */       return this.name;
/*     */     }
/*     */     
/*     */     public ResourceKey<StructureTemplatePool> pool() {
/* 771 */       return this.pool;
/*     */     }
/*     */     
/*     */     public Identifier target() {
/* 771 */       return this.target;
/*     */     }
/*     */     
/*     */     public int placementPriority() {
/* 771 */       return this.placementPriority;
/*     */     }
/*     */     
/*     */     public int selectionPriority() {
/* 771 */       return this.selectionPriority;
/*     */     }
/*     */     
/*     */     public static JigsawBlockInfo of(StructureTemplate.StructureBlockInfo info) {
/* 781 */       CompoundTag nbt = Objects.<CompoundTag>requireNonNull(info.nbt(), () -> String.valueOf(info) + " nbt was null");
/* 783 */       return new JigsawBlockInfo(info, 
/*     */           
/* 785 */           StructureTemplate.getJointType(nbt, info.state()), nbt
/* 786 */           .read("name", Identifier.CODEC).orElse(JigsawBlockEntity.EMPTY_ID), nbt
/* 787 */           .read("pool", JigsawBlockEntity.POOL_CODEC).orElse(Pools.EMPTY), nbt
/* 788 */           .read("target", Identifier.CODEC).orElse(JigsawBlockEntity.EMPTY_ID), nbt
/* 789 */           .getIntOr("placement_priority", 0), nbt
/* 790 */           .getIntOr("selection_priority", 0));
/*     */     }
/*     */     
/*     */     public String toString() {
/* 796 */       return String.format(Locale.ROOT, "<JigsawBlockInfo | %s | %s | name: %s | pool: %s | target: %s | placement: %d | selection: %d | %s>", new Object[] { this.info.pos, this.info.state, this.name, this.pool.identifier(), this.target, Integer.valueOf(this.placementPriority), Integer.valueOf(this.selectionPriority), this.info.nbt });
/*     */     }
/*     */     
/*     */     public JigsawBlockInfo withInfo(StructureTemplate.StructureBlockInfo info) {
/* 800 */       return new JigsawBlockInfo(info, this.jointType, this.name, this.pool, this.target, this.placementPriority, this.selectionPriority);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class StructureEntityInfo {
/*     */     public final Vec3 pos;
/*     */     
/*     */     public final BlockPos blockPos;
/*     */     
/*     */     public final CompoundTag nbt;
/*     */     
/*     */     public StructureEntityInfo(Vec3 pos, BlockPos blockPos, CompoundTag nbt) {
/* 810 */       this.pos = pos;
/* 811 */       this.blockPos = blockPos;
/* 812 */       this.nbt = nbt;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Palette {
/*     */     private final List<StructureTemplate.StructureBlockInfo> blocks;
/*     */     
/* 819 */     private final Map<Block, List<StructureTemplate.StructureBlockInfo>> cache = Maps.newHashMap();
/*     */     
/*     */     private List<StructureTemplate.JigsawBlockInfo> cachedJigsaws;
/*     */     
/*     */     private Palette(List<StructureTemplate.StructureBlockInfo> blocks) {
/* 823 */       this.blocks = blocks;
/*     */     }
/*     */     
/*     */     public List<StructureTemplate.JigsawBlockInfo> jigsaws() {
/* 827 */       if (this.cachedJigsaws == null)
/* 828 */         this
/*     */           
/* 830 */           .cachedJigsaws = blocks(Blocks.JIGSAW).stream().map(StructureTemplate.JigsawBlockInfo::of).toList(); 
/* 832 */       return this.cachedJigsaws;
/*     */     }
/*     */     
/*     */     public List<StructureTemplate.StructureBlockInfo> blocks() {
/* 836 */       return this.blocks;
/*     */     }
/*     */     
/*     */     public List<StructureTemplate.StructureBlockInfo> blocks(Block filter) {
/* 840 */       return this.cache.computeIfAbsent(filter, block -> (List)this.blocks.stream().filter(()).collect(Collectors.toList()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\StructureTemplate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */