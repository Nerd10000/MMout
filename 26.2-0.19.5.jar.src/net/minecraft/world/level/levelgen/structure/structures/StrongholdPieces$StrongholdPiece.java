/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ButtonBlock;
/*     */ import net.minecraft.world.level.block.DoorBlock;
/*     */ import net.minecraft.world.level.block.IronBarsBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ abstract class StrongholdPiece extends StructurePiece {
/* 218 */   protected SmallDoorType entryDoor = SmallDoorType.OPENING;
/*     */   
/*     */   protected StrongholdPiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
/* 221 */     super(type, genDepth, boundingBox);
/*     */   }
/*     */   
/*     */   public StrongholdPiece(StructurePieceType type, CompoundTag tag) {
/* 225 */     super(type, tag);
/* 226 */     this.entryDoor = tag.read("EntryDoor", SmallDoorType.LEGACY_CODEC).orElseThrow();
/*     */   }
/*     */   
/*     */   protected enum SmallDoorType {
/* 230 */     OPENING, WOOD_DOOR, GRATES, IRON_DOOR;
/*     */     
/*     */     @Deprecated
/* 234 */     public static final Codec<SmallDoorType> LEGACY_CODEC = ExtraCodecs.legacyEnum(SmallDoorType::valueOf);
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 239 */     tag.store("EntryDoor", SmallDoorType.LEGACY_CODEC, this.entryDoor);
/*     */   }
/*     */   
/*     */   protected void generateSmallDoor(WorldGenLevel level, RandomSource random, BoundingBox chunkBB, SmallDoorType doorType, int footX, int footY, int footZ) {
/* 243 */     switch (doorType.ordinal()) {
/*     */       case 0:
/* 244 */         generateBox(level, chunkBB, footX, footY, footZ, footX + 3 - 1, footY + 3 - 1, footZ, CAVE_AIR, CAVE_AIR, false);
/*     */         break;
/*     */       case 1:
/* 246 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY, footZ, chunkBB);
/* 247 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 1, footZ, chunkBB);
/* 248 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 2, footZ, chunkBB);
/* 249 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 1, footY + 2, footZ, chunkBB);
/* 250 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 2, footZ, chunkBB);
/* 251 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 1, footZ, chunkBB);
/* 252 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY, footZ, chunkBB);
/* 253 */         placeBlock(level, Blocks.OAK_DOOR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/* 254 */         placeBlock(level, (BlockState)Blocks.OAK_DOOR.defaultBlockState().setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.UPPER), footX + 1, footY + 1, footZ, chunkBB);
/*     */         break;
/*     */       case 2:
/* 257 */         placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/* 258 */         placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), footX + 1, footY + 1, footZ, chunkBB);
/* 259 */         placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY, footZ, chunkBB);
/* 260 */         placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY + 1, footZ, chunkBB);
/* 261 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY + 2, footZ, chunkBB);
/* 262 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), footX + 1, footY + 2, footZ, chunkBB);
/* 263 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), footX + 2, footY + 2, footZ, chunkBB);
/* 264 */         placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), footX + 2, footY + 1, footZ, chunkBB);
/* 265 */         placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), footX + 2, footY, footZ, chunkBB);
/*     */         break;
/*     */       case 3:
/* 268 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY, footZ, chunkBB);
/* 269 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 1, footZ, chunkBB);
/* 270 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 2, footZ, chunkBB);
/* 271 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 1, footY + 2, footZ, chunkBB);
/* 272 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 2, footZ, chunkBB);
/* 273 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 1, footZ, chunkBB);
/* 274 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY, footZ, chunkBB);
/* 275 */         placeBlock(level, Blocks.IRON_DOOR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/* 276 */         placeBlock(level, (BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.UPPER), footX + 1, footY + 1, footZ, chunkBB);
/* 277 */         placeBlock(level, (BlockState)Blocks.STONE_BUTTON.defaultBlockState().setValue((Property)ButtonBlock.FACING, (Comparable)Direction.NORTH), footX + 2, footY + 1, footZ + 1, chunkBB);
/* 278 */         placeBlock(level, (BlockState)Blocks.STONE_BUTTON.defaultBlockState().setValue((Property)ButtonBlock.FACING, (Comparable)Direction.SOUTH), footX + 2, footY + 1, footZ - 1, chunkBB);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SmallDoorType randomSmallDoor(RandomSource random) {
/* 284 */     int selection = random.nextInt(5);
/* 285 */     switch (selection) {
/*     */       default:
/* 289 */         return SmallDoorType.OPENING;
/*     */       case 2:
/* 291 */         return SmallDoorType.WOOD_DOOR;
/*     */       case 3:
/* 293 */         return SmallDoorType.GRATES;
/*     */       case 4:
/*     */         break;
/*     */     } 
/* 295 */     return SmallDoorType.IRON_DOOR;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateSmallDoorChildForward(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int xOff, int yOff) {
/* 300 */     Direction orientation = getOrientation();
/* 301 */     if (orientation != null)
/* 302 */       switch (StrongholdPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */         case 1:
/* 304 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + xOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, orientation, getGenDepth());
/*     */         case 2:
/* 307 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + xOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, orientation, getGenDepth());
/*     */         case 3:
/* 310 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + xOff, orientation, getGenDepth());
/*     */         case 4:
/* 313 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + xOff, orientation, getGenDepth());
/*     */       }  
/* 317 */     return null;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateSmallDoorChildLeft(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff) {
/* 321 */     Direction orientation = getOrientation();
/* 322 */     if (orientation != null)
/* 323 */       switch (StrongholdPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */         case 1:
/* 325 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.WEST, getGenDepth());
/*     */         case 2:
/* 328 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.WEST, getGenDepth());
/*     */         case 3:
/* 331 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth());
/*     */         case 4:
/* 334 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth());
/*     */       }  
/* 338 */     return null;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateSmallDoorChildRight(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff) {
/* 342 */     Direction orientation = getOrientation();
/* 343 */     if (orientation != null)
/* 344 */       switch (StrongholdPieces.null.$SwitchMap$net$minecraft$core$Direction[orientation.ordinal()]) {
/*     */         case 1:
/* 346 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.EAST, getGenDepth());
/*     */         case 2:
/* 349 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.EAST, getGenDepth());
/*     */         case 3:
/* 352 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth());
/*     */         case 4:
/* 355 */           return StrongholdPieces.generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth());
/*     */       }  
/* 359 */     return null;
/*     */   }
/*     */   
/*     */   protected static boolean isOkBox(BoundingBox box) {
/* 363 */     return (box.minY() > 10);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$StrongholdPiece.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */