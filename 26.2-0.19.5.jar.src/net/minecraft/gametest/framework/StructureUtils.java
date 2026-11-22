/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.commands.arguments.blocks.BlockInput;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*     */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class StructureUtils {
/*     */   public static final int DEFAULT_Y_SEARCH_RADIUS = 10;
/*     */   
/*     */   public static Path testStructuresTargetDir;
/*     */   
/*     */   public static Path testStructuresSourceDir;
/*     */   
/*     */   public static Rotation getRotationForRotationSteps(int rotationSteps) {
/*  41 */     switch (rotationSteps) {
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */     } 
/*  46 */     throw new IllegalArgumentException("rotationSteps must be a value from 0-3. Got value " + rotationSteps);
/*     */   }
/*     */   
/*     */   public static int getRotationStepsForRotation(Rotation rotation) {
/*  51 */     switch (rotation) {
/*     */       case NONE:
/*     */       
/*     */       case CLOCKWISE_90:
/*     */       
/*     */       case CLOCKWISE_180:
/*     */       
/*     */       case COUNTERCLOCKWISE_90:
/*     */       
/*     */     } 
/*  56 */     throw new IllegalArgumentException("Unknown rotation value, don't know how many steps it represents: " + String.valueOf(rotation));
/*     */   }
/*     */   
/*     */   public static TestInstanceBlockEntity createNewEmptyTest(Identifier id, BlockPos structurePos, Vec3i size, Rotation rotation, ServerLevel level) {
/*  61 */     BoundingBox structureBoundingBox = getStructureBoundingBox(TestInstanceBlockEntity.getStructurePos(structurePos), size, rotation);
/*  62 */     clearSpaceForStructure(structureBoundingBox, level);
/*  64 */     level.setBlockAndUpdate(structurePos, Blocks.TEST_INSTANCE_BLOCK.defaultBlockState());
/*  66 */     TestInstanceBlockEntity test = (TestInstanceBlockEntity)level.getBlockEntity(structurePos);
/*  67 */     ResourceKey<GameTestInstance> key = ResourceKey.create(Registries.TEST_INSTANCE, id);
/*  68 */     test.set(new TestInstanceBlockEntity.Data(
/*  69 */           Optional.of(key), size, rotation, false, TestInstanceBlockEntity.Status.CLEARED, Optional.empty()));
/*  72 */     return test;
/*     */   }
/*     */   
/*     */   public static void clearSpaceForStructure(BoundingBox structureBoundingBox, ServerLevel level) {
/*  76 */     int groundHeight = structureBoundingBox.minY() - 1;
/*  78 */     BlockPos.betweenClosedStream(structureBoundingBox).forEach(pos -> clearBlock(groundHeight, pos, level));
/*  79 */     level.getBlockTicks().clearArea(structureBoundingBox);
/*  80 */     level.clearBlockEvents(structureBoundingBox);
/*  81 */     AABB bounds = AABB.of(structureBoundingBox);
/*  82 */     List<Entity> livingEntities = level.getEntitiesOfClass(Entity.class, bounds, mob -> !(mob instanceof net.minecraft.world.entity.player.Player));
/*  83 */     livingEntities.forEach(Entity::discard);
/*     */   }
/*     */   
/*     */   public static BlockPos getTransformedFarCorner(BlockPos structurePosition, Vec3i size, Rotation rotation) {
/*  87 */     BlockPos farCornerBeforeTransform = structurePosition.offset(size).offset(-1, -1, -1);
/*  88 */     return StructureTemplate.transform(farCornerBeforeTransform, Mirror.NONE, rotation, structurePosition);
/*     */   }
/*     */   
/*     */   public static BoundingBox getStructureBoundingBox(BlockPos northWestCorner, Vec3i size, Rotation rotation) {
/*  92 */     BlockPos farCorner = getTransformedFarCorner(northWestCorner, size, rotation);
/*  93 */     BoundingBox boundingBox = BoundingBox.fromCorners((Vec3i)northWestCorner, (Vec3i)farCorner);
/*  95 */     int currentNorthWestCornerX = Math.min(boundingBox.minX(), boundingBox.maxX());
/*  96 */     int currentNorthWestCornerZ = Math.min(boundingBox.minZ(), boundingBox.maxZ());
/*  99 */     return boundingBox.move(northWestCorner.getX() - currentNorthWestCornerX, 0, northWestCorner.getZ() - currentNorthWestCornerZ);
/*     */   }
/*     */   
/*     */   public static Optional<BlockPos> findTestContainingPos(BlockPos pos, int searchRadius, ServerLevel level) {
/* 103 */     return findTestBlocks(pos, searchRadius, level)
/* 104 */       .filter(testBlockPosToCheck -> doesStructureContain(testBlockPosToCheck, pos, level))
/* 105 */       .findFirst();
/*     */   }
/*     */   
/*     */   public static Optional<BlockPos> findNearestTest(BlockPos relativeToPos, int searchRadius, ServerLevel level) {
/* 109 */     Comparator<BlockPos> distanceToPlayer = Comparator.comparingInt(pos -> pos.distManhattan((Vec3i)relativeToPos));
/* 111 */     return findTestBlocks(relativeToPos, searchRadius, level).min(distanceToPlayer);
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> findTestBlocks(BlockPos centerPos, int searchRadius, ServerLevel level) {
/* 115 */     return level.getPoiManager().findAll(p -> p.is(PoiTypes.TEST_INSTANCE), p -> true, centerPos, searchRadius, PoiManager.Occupancy.ANY)
/*     */       
/* 121 */       .map(BlockPos::immutable);
/*     */   }
/*     */   
/*     */   public static Stream<BlockPos> lookedAtTestPos(BlockPos pos, Entity camera, ServerLevel level) {
/* 125 */     int radius = 250;
/* 126 */     Vec3 start = camera.getEyePosition();
/* 127 */     Vec3 end = start.add(camera.getLookAngle().scale(250.0D));
/* 134 */     Objects.requireNonNull(pos);
/* 134 */     return findTestBlocks(pos, 250, level).map(blockPos -> level.getBlockEntity(blockPos, BlockEntityTypes.TEST_INSTANCE_BLOCK)).flatMap(Optional::stream).filter(blockEntity -> blockEntity.getStructureBounds().clip(start, end).isPresent()).map(BlockEntity::getBlockPos).sorted(Comparator.comparing(pos::distSqr))
/* 135 */       .limit(1L);
/*     */   }
/*     */   
/*     */   private static void clearBlock(int airIfAboveThisY, BlockPos pos, ServerLevel level) {
/*     */     BlockState blockState;
/* 140 */     if (pos.getY() < airIfAboveThisY) {
/* 141 */       blockState = Blocks.STONE.defaultBlockState();
/*     */     } else {
/* 143 */       blockState = Blocks.AIR.defaultBlockState();
/*     */     } 
/* 145 */     BlockInput blockInput = new BlockInput(blockState, Collections.emptySet(), null);
/* 146 */     blockInput.place(level, pos, 818);
/* 147 */     level.updateNeighborsAt(pos, blockState.getBlock());
/*     */   }
/*     */   
/*     */   private static boolean doesStructureContain(BlockPos testInstanceBlockPos, BlockPos pos, ServerLevel level) {
/* 151 */     BlockEntity blockEntity = level.getBlockEntity(testInstanceBlockPos);
/* 151 */     if (blockEntity instanceof TestInstanceBlockEntity) {
/* 151 */       TestInstanceBlockEntity testInstanceBlockEntity = (TestInstanceBlockEntity)blockEntity;
/* 152 */       return testInstanceBlockEntity.getStructureBoundingBox().isInside((Vec3i)pos);
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\StructureUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */