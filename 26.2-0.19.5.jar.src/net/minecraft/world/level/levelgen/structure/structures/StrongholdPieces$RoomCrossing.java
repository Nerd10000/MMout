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
/*     */ import net.minecraft.world.level.block.LadderBlock;
/*     */ import net.minecraft.world.level.block.WallTorchBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class RoomCrossing extends StrongholdPieces.StrongholdPiece {
/*     */   protected static final int WIDTH = 11;
/*     */   
/*     */   protected static final int HEIGHT = 7;
/*     */   
/*     */   protected static final int DEPTH = 11;
/*     */   
/*     */   protected final int type;
/*     */   
/*     */   public RoomCrossing(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 873 */     super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, genDepth, boundingBox);
/* 875 */     setOrientation(direction);
/* 876 */     this.entryDoor = randomSmallDoor(random);
/* 877 */     this.type = random.nextInt(5);
/*     */   }
/*     */   
/*     */   public RoomCrossing(CompoundTag tag) {
/* 881 */     super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, tag);
/* 882 */     this.type = tag.getIntOr("Type", 0);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 887 */     super.addAdditionalSaveData(context, tag);
/* 888 */     tag.putInt("Type", this.type);
/*     */   }
/*     */   
/*     */   public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 893 */     generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 4, 1);
/* 894 */     generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 4);
/* 895 */     generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 4);
/*     */   }
/*     */   
/*     */   public static RoomCrossing createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 899 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 11, 7, 11, direction);
/* 901 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 902 */       return null; 
/* 905 */     return new RoomCrossing(genDepth, random, box, direction);
/*     */   }
/*     */   
/*     */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*     */     int i;
/* 911 */     generateBox(level, chunkBB, 0, 0, 0, 10, 6, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 913 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 1, 0);
/* 915 */     generateBox(level, chunkBB, 4, 1, 10, 6, 3, 10, CAVE_AIR, CAVE_AIR, false);
/* 916 */     generateBox(level, chunkBB, 0, 1, 4, 0, 3, 6, CAVE_AIR, CAVE_AIR, false);
/* 917 */     generateBox(level, chunkBB, 10, 1, 4, 10, 3, 6, CAVE_AIR, CAVE_AIR, false);
/* 919 */     switch (this.type) {
/*     */       default:
/*     */         return;
/*     */       case 0:
/* 924 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, chunkBB);
/* 925 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
/* 926 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, chunkBB);
/* 927 */         placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST), 4, 3, 5, chunkBB);
/* 928 */         placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST), 6, 3, 5, chunkBB);
/* 929 */         placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH), 5, 3, 4, chunkBB);
/* 930 */         placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.NORTH), 5, 3, 6, chunkBB);
/* 931 */         placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 4, chunkBB);
/* 932 */         placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 5, chunkBB);
/* 933 */         placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 6, chunkBB);
/* 934 */         placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 4, chunkBB);
/* 935 */         placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 5, chunkBB);
/* 936 */         placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 6, chunkBB);
/* 937 */         placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 4, chunkBB);
/* 938 */         placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 6, chunkBB);
/*     */       case 1:
/* 941 */         for (i = 0; i < 5; i++) {
/* 942 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 1, 3 + i, chunkBB);
/* 943 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 7, 1, 3 + i, chunkBB);
/* 944 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3 + i, 1, 3, chunkBB);
/* 945 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3 + i, 1, 7, chunkBB);
/*     */         } 
/* 947 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, chunkBB);
/* 948 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
/* 949 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, chunkBB);
/* 950 */         placeBlock(level, Blocks.WATER.defaultBlockState(), 5, 4, 5, chunkBB);
/*     */       case 2:
/*     */         break;
/*     */     } 
/* 953 */     for (int j = 1; j <= 9; j++) {
/* 954 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 1, 3, j, chunkBB);
/* 955 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 9, 3, j, chunkBB);
/*     */     } 
/* 957 */     for (int x = 1; x <= 9; x++) {
/* 958 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), x, 3, 1, chunkBB);
/* 959 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), x, 3, 9, chunkBB);
/*     */     } 
/* 961 */     placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 4, chunkBB);
/* 962 */     placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 6, chunkBB);
/* 963 */     placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 4, chunkBB);
/* 964 */     placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 6, chunkBB);
/* 965 */     placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, 1, 5, chunkBB);
/* 966 */     placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, 1, 5, chunkBB);
/* 967 */     placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, 3, 5, chunkBB);
/* 968 */     placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, 3, 5, chunkBB);
/* 969 */     for (int y = 1; y <= 3; y++) {
/* 970 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, y, 4, chunkBB);
/* 971 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, y, 4, chunkBB);
/* 972 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, y, 6, chunkBB);
/* 973 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, y, 6, chunkBB);
/*     */     } 
/* 975 */     placeBlock(level, Blocks.WALL_TORCH.defaultBlockState(), 5, 3, 5, chunkBB);
/* 976 */     for (int z = 2; z <= 8; z++) {
/* 977 */       placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 2, 3, z, chunkBB);
/* 978 */       placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 3, 3, z, chunkBB);
/* 979 */       if (z <= 3 || z >= 7) {
/* 980 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 4, 3, z, chunkBB);
/* 981 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 5, 3, z, chunkBB);
/* 982 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 6, 3, z, chunkBB);
/*     */       } 
/* 984 */       placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 7, 3, z, chunkBB);
/* 985 */       placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 8, 3, z, chunkBB);
/*     */     } 
/* 987 */     BlockState ladder = (BlockState)Blocks.LADDER.defaultBlockState().setValue((Property)LadderBlock.FACING, (Comparable)Direction.WEST);
/* 988 */     placeBlock(level, ladder, 9, 1, 3, chunkBB);
/* 989 */     placeBlock(level, ladder, 9, 2, 3, chunkBB);
/* 990 */     placeBlock(level, ladder, 9, 3, 3, chunkBB);
/* 992 */     createChest(level, chunkBB, random, 3, 4, 8, BuiltInLootTables.STRONGHOLD_CROSSING);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$RoomCrossing.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */