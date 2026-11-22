/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.LadderBlock;
/*      */ import net.minecraft.world.level.block.WallTorchBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ public class Library extends StrongholdPieces.StrongholdPiece {
/*      */   protected static final int WIDTH = 14;
/*      */   
/*      */   protected static final int HEIGHT = 6;
/*      */   
/*      */   protected static final int TALL_HEIGHT = 11;
/*      */   
/*      */   protected static final int DEPTH = 15;
/*      */   
/*      */   private final boolean isTall;
/*      */   
/*      */   public Library(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1076 */     super(StructurePieceType.STRONGHOLD_LIBRARY, genDepth, boundingBox);
/* 1078 */     setOrientation(direction);
/* 1079 */     this.entryDoor = randomSmallDoor(random);
/* 1080 */     this.isTall = (boundingBox.getYSpan() > 6);
/*      */   }
/*      */   
/*      */   public Library(CompoundTag tag) {
/* 1084 */     super(StructurePieceType.STRONGHOLD_LIBRARY, tag);
/* 1085 */     this.isTall = tag.getBooleanOr("Tall", false);
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1090 */     super.addAdditionalSaveData(context, tag);
/* 1091 */     tag.putBoolean("Tall", this.isTall);
/*      */   }
/*      */   
/*      */   public static Library createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1096 */     BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 14, 11, 15, direction);
/* 1098 */     if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1100 */       box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 14, 6, 15, direction);
/* 1102 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1103 */         return null; 
/*      */     } 
/* 1107 */     return new Library(genDepth, random, box, direction);
/*      */   }
/*      */   
/*      */   public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1112 */     int currentHeight = 11;
/* 1113 */     if (!this.isTall)
/* 1114 */       currentHeight = 6; 
/* 1118 */     generateBox(level, chunkBB, 0, 0, 0, 13, currentHeight - 1, 14, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1120 */     generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 1, 0);
/* 1123 */     generateMaybeBox(level, chunkBB, random, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.COBWEB.defaultBlockState(), Blocks.COBWEB.defaultBlockState(), false, false);
/* 1125 */     int bookLeft = 1;
/* 1126 */     int bookRight = 12;
/*      */     int d;
/* 1129 */     for (d = 1; d <= 13; d++) {
/* 1130 */       if ((d - 1) % 4 == 0) {
/* 1131 */         generateBox(level, chunkBB, 1, 1, d, 1, 4, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1132 */         generateBox(level, chunkBB, 12, 1, d, 12, 4, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1134 */         placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST), 2, 3, d, chunkBB);
/* 1135 */         placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST), 11, 3, d, chunkBB);
/* 1137 */         if (this.isTall) {
/* 1138 */           generateBox(level, chunkBB, 1, 6, d, 1, 9, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1139 */           generateBox(level, chunkBB, 12, 6, d, 12, 9, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */         } 
/*      */       } else {
/* 1142 */         generateBox(level, chunkBB, 1, 1, d, 1, 4, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1143 */         generateBox(level, chunkBB, 12, 1, d, 12, 4, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1145 */         if (this.isTall) {
/* 1146 */           generateBox(level, chunkBB, 1, 6, d, 1, 9, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1147 */           generateBox(level, chunkBB, 12, 6, d, 12, 9, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1153 */     for (d = 3; d < 12; d += 2) {
/* 1154 */       generateBox(level, chunkBB, 3, 1, d, 4, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1155 */       generateBox(level, chunkBB, 6, 1, d, 7, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1156 */       generateBox(level, chunkBB, 9, 1, d, 10, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */     } 
/* 1159 */     if (this.isTall) {
/* 1161 */       generateBox(level, chunkBB, 1, 5, 1, 3, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1162 */       generateBox(level, chunkBB, 10, 5, 1, 12, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1163 */       generateBox(level, chunkBB, 4, 5, 1, 9, 5, 2, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1164 */       generateBox(level, chunkBB, 4, 5, 12, 9, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1166 */       placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 11, chunkBB);
/* 1167 */       placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 8, 5, 11, chunkBB);
/* 1168 */       placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 10, chunkBB);
/* 1170 */       BlockState weFence = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1171 */       BlockState nsFence = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1174 */       generateBox(level, chunkBB, 3, 6, 3, 3, 6, 11, nsFence, nsFence, false);
/* 1175 */       generateBox(level, chunkBB, 10, 6, 3, 10, 6, 9, nsFence, nsFence, false);
/* 1176 */       generateBox(level, chunkBB, 4, 6, 2, 9, 6, 2, weFence, weFence, false);
/* 1177 */       generateBox(level, chunkBB, 4, 6, 12, 7, 6, 12, weFence, weFence, false);
/* 1179 */       placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 2, chunkBB);
/* 1180 */       placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 12, chunkBB);
/* 1181 */       placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 10, 6, 2, chunkBB);
/* 1183 */       for (int i = 0; i <= 2; i++) {
/* 1184 */         placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 8 + i, 6, 12 - i, chunkBB);
/* 1185 */         if (i != 2)
/* 1186 */           placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 8 + i, 6, 11 - i, chunkBB); 
/*      */       } 
/* 1191 */       BlockState ladder = (BlockState)Blocks.LADDER.defaultBlockState().setValue((Property)LadderBlock.FACING, (Comparable)Direction.SOUTH);
/* 1192 */       placeBlock(level, ladder, 10, 1, 13, chunkBB);
/* 1193 */       placeBlock(level, ladder, 10, 2, 13, chunkBB);
/* 1194 */       placeBlock(level, ladder, 10, 3, 13, chunkBB);
/* 1195 */       placeBlock(level, ladder, 10, 4, 13, chunkBB);
/* 1196 */       placeBlock(level, ladder, 10, 5, 13, chunkBB);
/* 1197 */       placeBlock(level, ladder, 10, 6, 13, chunkBB);
/* 1198 */       placeBlock(level, ladder, 10, 7, 13, chunkBB);
/* 1201 */       int x = 7;
/* 1202 */       int z = 7;
/* 1203 */       BlockState eFence = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1204 */       placeBlock(level, eFence, 6, 9, 7, chunkBB);
/* 1205 */       BlockState wFence = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/* 1206 */       placeBlock(level, wFence, 7, 9, 7, chunkBB);
/* 1208 */       placeBlock(level, eFence, 6, 8, 7, chunkBB);
/* 1209 */       placeBlock(level, wFence, 7, 8, 7, chunkBB);
/* 1211 */       BlockState nsweFence = (BlockState)((BlockState)nsFence.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1213 */       placeBlock(level, nsweFence, 6, 7, 7, chunkBB);
/* 1214 */       placeBlock(level, nsweFence, 7, 7, 7, chunkBB);
/* 1216 */       placeBlock(level, eFence, 5, 7, 7, chunkBB);
/* 1218 */       placeBlock(level, wFence, 8, 7, 7, chunkBB);
/* 1220 */       placeBlock(level, (BlockState)eFence.setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 6, 7, 6, chunkBB);
/* 1221 */       placeBlock(level, (BlockState)eFence.setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 7, 8, chunkBB);
/* 1223 */       placeBlock(level, (BlockState)wFence.setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 7, 7, 6, chunkBB);
/* 1224 */       placeBlock(level, (BlockState)wFence.setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 7, 7, 8, chunkBB);
/* 1226 */       BlockState torch = Blocks.TORCH.defaultBlockState();
/* 1227 */       placeBlock(level, torch, 5, 8, 7, chunkBB);
/* 1228 */       placeBlock(level, torch, 8, 8, 7, chunkBB);
/* 1229 */       placeBlock(level, torch, 6, 8, 6, chunkBB);
/* 1230 */       placeBlock(level, torch, 6, 8, 8, chunkBB);
/* 1231 */       placeBlock(level, torch, 7, 8, 6, chunkBB);
/* 1232 */       placeBlock(level, torch, 7, 8, 8, chunkBB);
/*      */     } 
/* 1236 */     createChest(level, chunkBB, random, 3, 3, 5, BuiltInLootTables.STRONGHOLD_LIBRARY);
/* 1237 */     if (this.isTall) {
/* 1238 */       placeBlock(level, CAVE_AIR, 12, 9, 1, chunkBB);
/* 1239 */       createChest(level, chunkBB, random, 12, 8, 1, BuiltInLootTables.STRONGHOLD_LIBRARY);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$Library.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */