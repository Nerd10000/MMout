/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ public class BridgeEndFiller extends NetherFortressPieces.NetherBridgePiece {
/*     */   private static final int WIDTH = 5;
/*     */   
/*     */   private static final int HEIGHT = 10;
/*     */   
/*     */   private static final int DEPTH = 8;
/*     */   
/*     */   private final int selfSeed;
/*     */   
/*     */   public BridgeEndFiller(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 362 */     super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, genDepth, boundingBox);
/* 364 */     setOrientation(direction);
/* 365 */     this.selfSeed = random.nextInt();
/*     */   }
/*     */   
/*     */   public BridgeEndFiller(CompoundTag tag) {
/* 369 */     super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, tag);
/* 370 */     this.selfSeed = tag.getIntOr("Seed", 0);
/*     */   }
/*     */   
/*     */   public static BridgeEndFiller createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 374 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -3, 0, 5, 10, 8, direction);
/* 376 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 377 */       return null; 
/* 380 */     return new BridgeEndFiller(genDepth, random, box, direction);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 385 */     super.addAdditionalSaveData(context, tag);
/* 387 */     tag.putInt("Seed", this.selfSeed);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 392 */     RandomSource selfRandom = RandomSource.createThreadLocalInstance(this.selfSeed);
/* 395 */     for (int i = 0; i <= 4; i++) {
/* 396 */       for (int y = 3; y <= 4; y++) {
/* 397 */         int j = selfRandom.nextInt(8);
/* 398 */         generateBox(level, chunkBB, i, y, 0, i, y, j, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*     */       } 
/*     */     } 
/* 404 */     int z = selfRandom.nextInt(8);
/* 405 */     generateBox(level, chunkBB, 0, 5, 0, 0, 5, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 408 */     z = selfRandom.nextInt(8);
/* 409 */     generateBox(level, chunkBB, 4, 5, 0, 4, 5, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*     */     int x;
/* 413 */     for (x = 0; x <= 4; x++) {
/* 414 */       int j = selfRandom.nextInt(5);
/* 415 */       generateBox(level, chunkBB, x, 2, 0, x, 2, j, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*     */     } 
/* 417 */     for (x = 0; x <= 4; x++) {
/* 418 */       for (int y = 0; y <= 1; y++) {
/* 419 */         int j = selfRandom.nextInt(3);
/* 420 */         generateBox(level, chunkBB, x, y, 0, x, y, j, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$BridgeEndFiller.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */