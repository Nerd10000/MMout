/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.serialization.Codec;
/*      */ import java.util.List;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.ExtraCodecs;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ButtonBlock;
/*      */ import net.minecraft.world.level.block.DoorBlock;
/*      */ import net.minecraft.world.level.block.EndPortalFrameBlock;
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.IronBarsBlock;
/*      */ import net.minecraft.world.level.block.LadderBlock;
/*      */ import net.minecraft.world.level.block.SlabBlock;
/*      */ import net.minecraft.world.level.block.StairBlock;
/*      */ import net.minecraft.world.level.block.WallTorchBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.block.state.properties.SlabType;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ public class StrongholdPieces {
/*      */   private static final int SMALL_DOOR_WIDTH = 3;
/*      */   
/*      */   private static final int SMALL_DOOR_HEIGHT = 3;
/*      */   
/*      */   private static final int MAX_DEPTH = 50;
/*      */   
/*      */   private static final int LOWEST_Y_POSITION = 10;
/*      */   
/*      */   private static final boolean CHECK_AIR = true;
/*      */   
/*      */   public static final int MAGIC_START_Y = 64;
/*      */   
/*      */   private static class PieceWeight {
/*      */     public final Class<? extends StrongholdPieces.StrongholdPiece> pieceClass;
/*      */     
/*      */     public final int weight;
/*      */     
/*      */     public int placeCount;
/*      */     
/*      */     public final int maxPlaceCount;
/*      */     
/*      */     public PieceWeight(Class<? extends StrongholdPieces.StrongholdPiece> pieceClass, int weight, int maxPlaceCount) {
/*   62 */       this.pieceClass = pieceClass;
/*   63 */       this.weight = weight;
/*   64 */       this.maxPlaceCount = maxPlaceCount;
/*      */     }
/*      */     
/*      */     public boolean doPlace(int depth) {
/*   68 */       return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*      */     }
/*      */     
/*      */     public boolean isValid() {
/*   72 */       return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*      */     }
/*      */   }
/*      */   
/*   76 */   private static final PieceWeight[] STRONGHOLD_PIECE_WEIGHTS = new PieceWeight[] { 
/*   76 */       new PieceWeight((Class)Straight.class, 40, 0), new PieceWeight((Class)PrisonHall.class, 5, 5), new PieceWeight((Class)LeftTurn.class, 20, 0), new PieceWeight((Class)RightTurn.class, 20, 0), new PieceWeight((Class)RoomCrossing.class, 10, 6), new PieceWeight((Class)StraightStairsDown.class, 5, 5), new PieceWeight((Class)StairsDown.class, 5, 5), new PieceWeight((Class)FiveCrossing.class, 5, 4), new PieceWeight((Class)ChestCorridor.class, 5, 4), new PieceWeight(Library.class, 10, 2) {
/*      */         public boolean doPlace(int depth) {
/*   89 */           return (super.doPlace(depth) && depth > 4);
/*      */         }
/*      */       }, 
/*      */       new PieceWeight(PortalRoom.class, 20, 1) {
/*      */         public boolean doPlace(int depth) {
/*   95 */           return (super.doPlace(depth) && depth > 5);
/*      */         }
/*      */       } };
/*      */   
/*      */   private static List<PieceWeight> currentPieces;
/*      */   
/*      */   private static Class<? extends StrongholdPiece> imposedPiece;
/*      */   
/*      */   private static int totalWeight;
/*      */   
/*      */   public static void resetPieces() {
/*  104 */     currentPieces = Lists.newArrayList();
/*  105 */     for (PieceWeight piece : STRONGHOLD_PIECE_WEIGHTS) {
/*  106 */       piece.placeCount = 0;
/*  107 */       currentPieces.add(piece);
/*      */     } 
/*  109 */     imposedPiece = null;
/*      */   }
/*      */   
/*      */   private static boolean updatePieceWeight() {
/*  113 */     boolean hasAnyPieces = false;
/*  114 */     totalWeight = 0;
/*  115 */     for (PieceWeight piece : currentPieces) {
/*  116 */       if (piece.maxPlaceCount > 0 && piece.placeCount < piece.maxPlaceCount)
/*  117 */         hasAnyPieces = true; 
/*  119 */       totalWeight += piece.weight;
/*      */     } 
/*  121 */     return hasAnyPieces;
/*      */   }
/*      */   
/*      */   private static StrongholdPiece findAndCreatePieceFactory(Class<? extends StrongholdPiece> pieceClass, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/*  125 */     StrongholdPiece strongholdPiece = null;
/*  127 */     if (pieceClass == Straight.class) {
/*  128 */       strongholdPiece = Straight.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  129 */     } else if (pieceClass == PrisonHall.class) {
/*  130 */       strongholdPiece = PrisonHall.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  131 */     } else if (pieceClass == LeftTurn.class) {
/*  132 */       strongholdPiece = LeftTurn.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  133 */     } else if (pieceClass == RightTurn.class) {
/*  134 */       strongholdPiece = RightTurn.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  135 */     } else if (pieceClass == RoomCrossing.class) {
/*  136 */       strongholdPiece = RoomCrossing.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  137 */     } else if (pieceClass == StraightStairsDown.class) {
/*  138 */       strongholdPiece = StraightStairsDown.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  139 */     } else if (pieceClass == StairsDown.class) {
/*  140 */       strongholdPiece = StairsDown.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  141 */     } else if (pieceClass == FiveCrossing.class) {
/*  142 */       strongholdPiece = FiveCrossing.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  143 */     } else if (pieceClass == ChestCorridor.class) {
/*  144 */       strongholdPiece = ChestCorridor.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  145 */     } else if (pieceClass == Library.class) {
/*  146 */       strongholdPiece = Library.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  147 */     } else if (pieceClass == PortalRoom.class) {
/*  148 */       strongholdPiece = PortalRoom.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*      */     } 
/*  151 */     return strongholdPiece;
/*      */   }
/*      */   
/*      */   private static StrongholdPiece generatePieceFromSmallDoor(StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/*  155 */     if (!updatePieceWeight())
/*  156 */       return null; 
/*  159 */     if (imposedPiece != null) {
/*  160 */       StrongholdPiece strongholdPiece = findAndCreatePieceFactory(imposedPiece, structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  161 */       imposedPiece = null;
/*  163 */       if (strongholdPiece != null)
/*  164 */         return strongholdPiece; 
/*      */     } 
/*  168 */     int numAttempts = 0;
/*  169 */     while (numAttempts < 5) {
/*  170 */       numAttempts++;
/*  172 */       int weightSelection = random.nextInt(totalWeight);
/*  173 */       for (PieceWeight piece : currentPieces) {
/*  174 */         weightSelection -= piece.weight;
/*  175 */         if (weightSelection < 0) {
/*  176 */           if (!piece.doPlace(depth) || piece == startPiece.previousPiece)
/*      */             break; 
/*  180 */           StrongholdPiece strongholdPiece = findAndCreatePieceFactory(piece.pieceClass, structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  181 */           if (strongholdPiece != null) {
/*  182 */             piece.placeCount++;
/*  183 */             startPiece.previousPiece = piece;
/*  185 */             if (!piece.isValid())
/*  186 */               currentPieces.remove(piece); 
/*  188 */             return strongholdPiece;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  193 */     BoundingBox box = FillerCorridor.findPieceBox(structurePieceAccessor, random, footX, footY, footZ, direction);
/*  194 */     if (box != null && box.minY() > 1)
/*  195 */       return new FillerCorridor(depth, box, direction); 
/*  198 */     return null;
/*      */   }
/*      */   
/*      */   private static StructurePiece generateAndAddPiece(StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/*  202 */     if (depth > 50)
/*  203 */       return null; 
/*  205 */     if (Math.abs(footX - startPiece.getBoundingBox().minX()) > 112 || Math.abs(footZ - startPiece.getBoundingBox().minZ()) > 112)
/*  206 */       return null; 
/*  209 */     StructurePiece newPiece = generatePieceFromSmallDoor(startPiece, structurePieceAccessor, random, footX, footY, footZ, direction, depth + 1);
/*  210 */     if (newPiece != null) {
/*  211 */       structurePieceAccessor.addPiece(newPiece);
/*  212 */       startPiece.pendingChildren.add(newPiece);
/*      */     } 
/*  214 */     return newPiece;
/*      */   }
/*      */   
/*      */   private static abstract class StrongholdPiece extends StructurePiece {
/*  218 */     protected SmallDoorType entryDoor = SmallDoorType.OPENING;
/*      */     
/*      */     protected StrongholdPiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
/*  221 */       super(type, genDepth, boundingBox);
/*      */     }
/*      */     
/*      */     public StrongholdPiece(StructurePieceType type, CompoundTag tag) {
/*  225 */       super(type, tag);
/*  226 */       this.entryDoor = tag.read("EntryDoor", SmallDoorType.LEGACY_CODEC).orElseThrow();
/*      */     }
/*      */     
/*      */     protected enum SmallDoorType {
/*  230 */       OPENING, WOOD_DOOR, GRATES, IRON_DOOR;
/*      */       
/*      */       @Deprecated
/*  234 */       public static final Codec<SmallDoorType> LEGACY_CODEC = ExtraCodecs.legacyEnum(SmallDoorType::valueOf);
/*      */       
/*      */       static {
/*      */       
/*      */       }
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  239 */       tag.store("EntryDoor", SmallDoorType.LEGACY_CODEC, this.entryDoor);
/*      */     }
/*      */     
/*      */     protected void generateSmallDoor(WorldGenLevel level, RandomSource random, BoundingBox chunkBB, SmallDoorType doorType, int footX, int footY, int footZ) {
/*  243 */       switch (doorType.ordinal()) {
/*      */         case 0:
/*  244 */           generateBox(level, chunkBB, footX, footY, footZ, footX + 3 - 1, footY + 3 - 1, footZ, CAVE_AIR, CAVE_AIR, false);
/*      */           break;
/*      */         case 1:
/*  246 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY, footZ, chunkBB);
/*  247 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 1, footZ, chunkBB);
/*  248 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 2, footZ, chunkBB);
/*  249 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 1, footY + 2, footZ, chunkBB);
/*  250 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 2, footZ, chunkBB);
/*  251 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 1, footZ, chunkBB);
/*  252 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY, footZ, chunkBB);
/*  253 */           placeBlock(level, Blocks.OAK_DOOR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/*  254 */           placeBlock(level, (BlockState)Blocks.OAK_DOOR.defaultBlockState().setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.UPPER), footX + 1, footY + 1, footZ, chunkBB);
/*      */           break;
/*      */         case 2:
/*  257 */           placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/*  258 */           placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), footX + 1, footY + 1, footZ, chunkBB);
/*  259 */           placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY, footZ, chunkBB);
/*  260 */           placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY + 1, footZ, chunkBB);
/*  261 */           placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), footX, footY + 2, footZ, chunkBB);
/*  262 */           placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), footX + 1, footY + 2, footZ, chunkBB);
/*  263 */           placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true)), footX + 2, footY + 2, footZ, chunkBB);
/*  264 */           placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), footX + 2, footY + 1, footZ, chunkBB);
/*  265 */           placeBlock(level, (BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), footX + 2, footY, footZ, chunkBB);
/*      */           break;
/*      */         case 3:
/*  268 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY, footZ, chunkBB);
/*  269 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 1, footZ, chunkBB);
/*  270 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX, footY + 2, footZ, chunkBB);
/*  271 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 1, footY + 2, footZ, chunkBB);
/*  272 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 2, footZ, chunkBB);
/*  273 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY + 1, footZ, chunkBB);
/*  274 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), footX + 2, footY, footZ, chunkBB);
/*  275 */           placeBlock(level, Blocks.IRON_DOOR.defaultBlockState(), footX + 1, footY, footZ, chunkBB);
/*  276 */           placeBlock(level, (BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.UPPER), footX + 1, footY + 1, footZ, chunkBB);
/*  277 */           placeBlock(level, (BlockState)Blocks.STONE_BUTTON.defaultBlockState().setValue((Property)ButtonBlock.FACING, (Comparable)Direction.NORTH), footX + 2, footY + 1, footZ + 1, chunkBB);
/*  278 */           placeBlock(level, (BlockState)Blocks.STONE_BUTTON.defaultBlockState().setValue((Property)ButtonBlock.FACING, (Comparable)Direction.SOUTH), footX + 2, footY + 1, footZ - 1, chunkBB);
/*      */           break;
/*      */       } 
/*      */     }
/*      */     
/*      */     protected SmallDoorType randomSmallDoor(RandomSource random) {
/*  284 */       int selection = random.nextInt(5);
/*  285 */       switch (selection) {
/*      */         default:
/*  289 */           return SmallDoorType.OPENING;
/*      */         case 2:
/*  291 */           return SmallDoorType.WOOD_DOOR;
/*      */         case 3:
/*  293 */           return SmallDoorType.GRATES;
/*      */         case 4:
/*      */           break;
/*      */       } 
/*  295 */       return SmallDoorType.IRON_DOOR;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateSmallDoorChildForward(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int xOff, int yOff) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*      */       //   4: astore #6
/*      */       //   6: aload #6
/*      */       //   8: ifnull -> 220
/*      */       //   11: getstatic net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$3.$SwitchMap$net$minecraft$core$Direction : [I
/*      */       //   14: aload #6
/*      */       //   16: invokevirtual ordinal : ()I
/*      */       //   19: iaload
/*      */       //   20: tableswitch default -> 220, 1 -> 52, 2 -> 94, 3 -> 136, 4 -> 178
/*      */       //   52: aload_1
/*      */       //   53: aload_2
/*      */       //   54: aload_3
/*      */       //   55: aload_0
/*      */       //   56: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   59: invokevirtual minX : ()I
/*      */       //   62: iload #4
/*      */       //   64: iadd
/*      */       //   65: aload_0
/*      */       //   66: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   69: invokevirtual minY : ()I
/*      */       //   72: iload #5
/*      */       //   74: iadd
/*      */       //   75: aload_0
/*      */       //   76: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   79: invokevirtual minZ : ()I
/*      */       //   82: iconst_1
/*      */       //   83: isub
/*      */       //   84: aload #6
/*      */       //   86: aload_0
/*      */       //   87: invokevirtual getGenDepth : ()I
/*      */       //   90: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   93: areturn
/*      */       //   94: aload_1
/*      */       //   95: aload_2
/*      */       //   96: aload_3
/*      */       //   97: aload_0
/*      */       //   98: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   101: invokevirtual minX : ()I
/*      */       //   104: iload #4
/*      */       //   106: iadd
/*      */       //   107: aload_0
/*      */       //   108: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   111: invokevirtual minY : ()I
/*      */       //   114: iload #5
/*      */       //   116: iadd
/*      */       //   117: aload_0
/*      */       //   118: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   121: invokevirtual maxZ : ()I
/*      */       //   124: iconst_1
/*      */       //   125: iadd
/*      */       //   126: aload #6
/*      */       //   128: aload_0
/*      */       //   129: invokevirtual getGenDepth : ()I
/*      */       //   132: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   135: areturn
/*      */       //   136: aload_1
/*      */       //   137: aload_2
/*      */       //   138: aload_3
/*      */       //   139: aload_0
/*      */       //   140: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   143: invokevirtual minX : ()I
/*      */       //   146: iconst_1
/*      */       //   147: isub
/*      */       //   148: aload_0
/*      */       //   149: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   152: invokevirtual minY : ()I
/*      */       //   155: iload #5
/*      */       //   157: iadd
/*      */       //   158: aload_0
/*      */       //   159: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   162: invokevirtual minZ : ()I
/*      */       //   165: iload #4
/*      */       //   167: iadd
/*      */       //   168: aload #6
/*      */       //   170: aload_0
/*      */       //   171: invokevirtual getGenDepth : ()I
/*      */       //   174: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   177: areturn
/*      */       //   178: aload_1
/*      */       //   179: aload_2
/*      */       //   180: aload_3
/*      */       //   181: aload_0
/*      */       //   182: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   185: invokevirtual maxX : ()I
/*      */       //   188: iconst_1
/*      */       //   189: iadd
/*      */       //   190: aload_0
/*      */       //   191: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   194: invokevirtual minY : ()I
/*      */       //   197: iload #5
/*      */       //   199: iadd
/*      */       //   200: aload_0
/*      */       //   201: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   204: invokevirtual minZ : ()I
/*      */       //   207: iload #4
/*      */       //   209: iadd
/*      */       //   210: aload #6
/*      */       //   212: aload_0
/*      */       //   213: invokevirtual getGenDepth : ()I
/*      */       //   216: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   219: areturn
/*      */       //   220: aconst_null
/*      */       //   221: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #300	-> 0
/*      */       //   #301	-> 6
/*      */       //   #302	-> 11
/*      */       //   #304	-> 52
/*      */       //   #307	-> 94
/*      */       //   #310	-> 136
/*      */       //   #313	-> 178
/*      */       //   #317	-> 220
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	222	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StrongholdPiece;
/*      */       //   0	222	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;
/*      */       //   0	222	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*      */       //   0	222	3	random	Lnet/minecraft/util/RandomSource;
/*      */       //   0	222	4	xOff	I
/*      */       //   0	222	5	yOff	I
/*      */       //   6	216	6	orientation	Lnet/minecraft/core/Direction;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateSmallDoorChildLeft(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*      */       //   4: astore #6
/*      */       //   6: aload #6
/*      */       //   8: ifnull -> 224
/*      */       //   11: getstatic net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$3.$SwitchMap$net$minecraft$core$Direction : [I
/*      */       //   14: aload #6
/*      */       //   16: invokevirtual ordinal : ()I
/*      */       //   19: iaload
/*      */       //   20: tableswitch default -> 224, 1 -> 52, 2 -> 95, 3 -> 138, 4 -> 181
/*      */       //   52: aload_1
/*      */       //   53: aload_2
/*      */       //   54: aload_3
/*      */       //   55: aload_0
/*      */       //   56: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   59: invokevirtual minX : ()I
/*      */       //   62: iconst_1
/*      */       //   63: isub
/*      */       //   64: aload_0
/*      */       //   65: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   68: invokevirtual minY : ()I
/*      */       //   71: iload #4
/*      */       //   73: iadd
/*      */       //   74: aload_0
/*      */       //   75: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   78: invokevirtual minZ : ()I
/*      */       //   81: iload #5
/*      */       //   83: iadd
/*      */       //   84: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*      */       //   87: aload_0
/*      */       //   88: invokevirtual getGenDepth : ()I
/*      */       //   91: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   94: areturn
/*      */       //   95: aload_1
/*      */       //   96: aload_2
/*      */       //   97: aload_3
/*      */       //   98: aload_0
/*      */       //   99: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   102: invokevirtual minX : ()I
/*      */       //   105: iconst_1
/*      */       //   106: isub
/*      */       //   107: aload_0
/*      */       //   108: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   111: invokevirtual minY : ()I
/*      */       //   114: iload #4
/*      */       //   116: iadd
/*      */       //   117: aload_0
/*      */       //   118: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   121: invokevirtual minZ : ()I
/*      */       //   124: iload #5
/*      */       //   126: iadd
/*      */       //   127: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*      */       //   130: aload_0
/*      */       //   131: invokevirtual getGenDepth : ()I
/*      */       //   134: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   137: areturn
/*      */       //   138: aload_1
/*      */       //   139: aload_2
/*      */       //   140: aload_3
/*      */       //   141: aload_0
/*      */       //   142: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   145: invokevirtual minX : ()I
/*      */       //   148: iload #5
/*      */       //   150: iadd
/*      */       //   151: aload_0
/*      */       //   152: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   155: invokevirtual minY : ()I
/*      */       //   158: iload #4
/*      */       //   160: iadd
/*      */       //   161: aload_0
/*      */       //   162: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   165: invokevirtual minZ : ()I
/*      */       //   168: iconst_1
/*      */       //   169: isub
/*      */       //   170: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*      */       //   173: aload_0
/*      */       //   174: invokevirtual getGenDepth : ()I
/*      */       //   177: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   180: areturn
/*      */       //   181: aload_1
/*      */       //   182: aload_2
/*      */       //   183: aload_3
/*      */       //   184: aload_0
/*      */       //   185: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   188: invokevirtual minX : ()I
/*      */       //   191: iload #5
/*      */       //   193: iadd
/*      */       //   194: aload_0
/*      */       //   195: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   198: invokevirtual minY : ()I
/*      */       //   201: iload #4
/*      */       //   203: iadd
/*      */       //   204: aload_0
/*      */       //   205: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   208: invokevirtual minZ : ()I
/*      */       //   211: iconst_1
/*      */       //   212: isub
/*      */       //   213: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*      */       //   216: aload_0
/*      */       //   217: invokevirtual getGenDepth : ()I
/*      */       //   220: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   223: areturn
/*      */       //   224: aconst_null
/*      */       //   225: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #321	-> 0
/*      */       //   #322	-> 6
/*      */       //   #323	-> 11
/*      */       //   #325	-> 52
/*      */       //   #328	-> 95
/*      */       //   #331	-> 138
/*      */       //   #334	-> 181
/*      */       //   #338	-> 224
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	226	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StrongholdPiece;
/*      */       //   0	226	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;
/*      */       //   0	226	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*      */       //   0	226	3	random	Lnet/minecraft/util/RandomSource;
/*      */       //   0	226	4	yOff	I
/*      */       //   0	226	5	zOff	I
/*      */       //   6	220	6	orientation	Lnet/minecraft/core/Direction;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateSmallDoorChildRight(StrongholdPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*      */       //   4: astore #6
/*      */       //   6: aload #6
/*      */       //   8: ifnull -> 224
/*      */       //   11: getstatic net/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$3.$SwitchMap$net$minecraft$core$Direction : [I
/*      */       //   14: aload #6
/*      */       //   16: invokevirtual ordinal : ()I
/*      */       //   19: iaload
/*      */       //   20: tableswitch default -> 224, 1 -> 52, 2 -> 95, 3 -> 138, 4 -> 181
/*      */       //   52: aload_1
/*      */       //   53: aload_2
/*      */       //   54: aload_3
/*      */       //   55: aload_0
/*      */       //   56: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   59: invokevirtual maxX : ()I
/*      */       //   62: iconst_1
/*      */       //   63: iadd
/*      */       //   64: aload_0
/*      */       //   65: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   68: invokevirtual minY : ()I
/*      */       //   71: iload #4
/*      */       //   73: iadd
/*      */       //   74: aload_0
/*      */       //   75: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   78: invokevirtual minZ : ()I
/*      */       //   81: iload #5
/*      */       //   83: iadd
/*      */       //   84: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*      */       //   87: aload_0
/*      */       //   88: invokevirtual getGenDepth : ()I
/*      */       //   91: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   94: areturn
/*      */       //   95: aload_1
/*      */       //   96: aload_2
/*      */       //   97: aload_3
/*      */       //   98: aload_0
/*      */       //   99: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   102: invokevirtual maxX : ()I
/*      */       //   105: iconst_1
/*      */       //   106: iadd
/*      */       //   107: aload_0
/*      */       //   108: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   111: invokevirtual minY : ()I
/*      */       //   114: iload #4
/*      */       //   116: iadd
/*      */       //   117: aload_0
/*      */       //   118: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   121: invokevirtual minZ : ()I
/*      */       //   124: iload #5
/*      */       //   126: iadd
/*      */       //   127: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*      */       //   130: aload_0
/*      */       //   131: invokevirtual getGenDepth : ()I
/*      */       //   134: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   137: areturn
/*      */       //   138: aload_1
/*      */       //   139: aload_2
/*      */       //   140: aload_3
/*      */       //   141: aload_0
/*      */       //   142: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   145: invokevirtual minX : ()I
/*      */       //   148: iload #5
/*      */       //   150: iadd
/*      */       //   151: aload_0
/*      */       //   152: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   155: invokevirtual minY : ()I
/*      */       //   158: iload #4
/*      */       //   160: iadd
/*      */       //   161: aload_0
/*      */       //   162: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   165: invokevirtual maxZ : ()I
/*      */       //   168: iconst_1
/*      */       //   169: iadd
/*      */       //   170: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*      */       //   173: aload_0
/*      */       //   174: invokevirtual getGenDepth : ()I
/*      */       //   177: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   180: areturn
/*      */       //   181: aload_1
/*      */       //   182: aload_2
/*      */       //   183: aload_3
/*      */       //   184: aload_0
/*      */       //   185: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   188: invokevirtual minX : ()I
/*      */       //   191: iload #5
/*      */       //   193: iadd
/*      */       //   194: aload_0
/*      */       //   195: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   198: invokevirtual minY : ()I
/*      */       //   201: iload #4
/*      */       //   203: iadd
/*      */       //   204: aload_0
/*      */       //   205: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*      */       //   208: invokevirtual maxZ : ()I
/*      */       //   211: iconst_1
/*      */       //   212: iadd
/*      */       //   213: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*      */       //   216: aload_0
/*      */       //   217: invokevirtual getGenDepth : ()I
/*      */       //   220: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*      */       //   223: areturn
/*      */       //   224: aconst_null
/*      */       //   225: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #342	-> 0
/*      */       //   #343	-> 6
/*      */       //   #344	-> 11
/*      */       //   #346	-> 52
/*      */       //   #349	-> 95
/*      */       //   #352	-> 138
/*      */       //   #355	-> 181
/*      */       //   #359	-> 224
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	226	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StrongholdPiece;
/*      */       //   0	226	1	startPiece	Lnet/minecraft/world/level/levelgen/structure/structures/StrongholdPieces$StartPiece;
/*      */       //   0	226	2	structurePieceAccessor	Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;
/*      */       //   0	226	3	random	Lnet/minecraft/util/RandomSource;
/*      */       //   0	226	4	yOff	I
/*      */       //   0	226	5	zOff	I
/*      */       //   6	220	6	orientation	Lnet/minecraft/core/Direction;
/*      */     }
/*      */     
/*      */     protected static boolean isOkBox(BoundingBox box) {
/*  363 */       return (box.minY() > 10);
/*      */     }
/*      */   }
/*      */   
/*      */   protected enum SmallDoorType {
/*      */     OPENING, WOOD_DOOR, GRATES, IRON_DOOR;
/*      */     
/*      */     @Deprecated
/*      */     public static final Codec<SmallDoorType> LEGACY_CODEC = ExtraCodecs.legacyEnum(SmallDoorType::valueOf);
/*      */     
/*      */     static {
/*      */     
/*      */     }
/*      */   }
/*      */   
/*      */   public static class FillerCorridor extends StrongholdPiece {
/*      */     private final int steps;
/*      */     
/*      */     public FillerCorridor(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  372 */       super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, genDepth, boundingBox);
/*  374 */       setOrientation(direction);
/*  375 */       this.steps = (direction == Direction.NORTH || direction == Direction.SOUTH) ? boundingBox.getZSpan() : boundingBox.getXSpan();
/*      */     }
/*      */     
/*      */     public FillerCorridor(CompoundTag tag) {
/*  379 */       super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, tag);
/*  380 */       this.steps = tag.getIntOr("Steps", 0);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  385 */       super.addAdditionalSaveData(context, tag);
/*  386 */       tag.putInt("Steps", this.steps);
/*      */     }
/*      */     
/*      */     public static BoundingBox findPieceBox(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction) {
/*  390 */       int maxLength = 3;
/*  392 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 4, direction);
/*  394 */       StructurePiece collisionPiece = structurePieceAccessor.findCollisionPiece(box);
/*  395 */       if (collisionPiece == null)
/*  397 */         return null; 
/*  400 */       if (collisionPiece.getBoundingBox().minY() == box.minY())
/*  402 */         for (int depth = 2; depth >= 1; depth--) {
/*  403 */           box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, depth, direction);
/*  404 */           if (!collisionPiece.getBoundingBox().intersects(box))
/*  407 */             return BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, depth + 1, direction); 
/*      */         }  
/*  412 */       return null;
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  418 */       for (int i = 0; i < this.steps; i++) {
/*  420 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, 0, i, chunkBB);
/*  421 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 0, i, chunkBB);
/*  422 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 0, i, chunkBB);
/*  423 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 0, i, chunkBB);
/*  424 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, 0, i, chunkBB);
/*  426 */         for (int y = 1; y <= 3; y++) {
/*  427 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, y, i, chunkBB);
/*  428 */           placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 1, y, i, chunkBB);
/*  429 */           placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 2, y, i, chunkBB);
/*  430 */           placeBlock(level, Blocks.CAVE_AIR.defaultBlockState(), 3, y, i, chunkBB);
/*  431 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, y, i, chunkBB);
/*      */         } 
/*  434 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 0, 4, i, chunkBB);
/*  435 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, i, chunkBB);
/*  436 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, i, chunkBB);
/*  437 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 4, i, chunkBB);
/*  438 */         placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 4, 4, i, chunkBB);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StairsDown extends StrongholdPiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 11;
/*      */     
/*      */     private static final int DEPTH = 5;
/*      */     
/*      */     private final boolean isSource;
/*      */     
/*      */     public StairsDown(StructurePieceType type, int genDepth, int west, int north, Direction direction) {
/*  451 */       super(type, genDepth, makeBoundingBox(west, 64, north, direction, 5, 11, 5));
/*  453 */       this.isSource = true;
/*  454 */       setOrientation(direction);
/*  455 */       this.entryDoor = StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING;
/*      */     }
/*      */     
/*      */     public StairsDown(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  459 */       super(StructurePieceType.STRONGHOLD_STAIRS_DOWN, genDepth, boundingBox);
/*  461 */       this.isSource = false;
/*  462 */       setOrientation(direction);
/*  463 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */     
/*      */     public StairsDown(StructurePieceType type, CompoundTag tag) {
/*  467 */       super(type, tag);
/*  468 */       this.isSource = tag.getBooleanOr("Source", false);
/*      */     }
/*      */     
/*      */     public StairsDown(CompoundTag tag) {
/*  472 */       this(StructurePieceType.STRONGHOLD_STAIRS_DOWN, tag);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  477 */       super.addAdditionalSaveData(context, tag);
/*  478 */       tag.putBoolean("Source", this.isSource);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  483 */       if (this.isSource)
/*  485 */         StrongholdPieces.imposedPiece = (Class)StrongholdPieces.FiveCrossing.class; 
/*  487 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */     }
/*      */     
/*      */     public static StairsDown createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  491 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 11, 5, direction);
/*  493 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  494 */         return null; 
/*  497 */       return new StairsDown(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  503 */       generateBox(level, chunkBB, 0, 0, 0, 4, 10, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*  505 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 7, 0);
/*  507 */       generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 4);
/*  510 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 6, 1, chunkBB);
/*  511 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 1, chunkBB);
/*  512 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 6, 1, chunkBB);
/*  513 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 2, chunkBB);
/*  514 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, 3, chunkBB);
/*  515 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 5, 3, chunkBB);
/*  516 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, 3, chunkBB);
/*  517 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 3, chunkBB);
/*  518 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 4, 3, chunkBB);
/*  519 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 2, chunkBB);
/*  520 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 2, 1, chunkBB);
/*  521 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 3, 1, chunkBB);
/*  522 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 2, 1, chunkBB);
/*  523 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 1, chunkBB);
/*  524 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 2, 1, chunkBB);
/*  525 */       placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 2, chunkBB);
/*  526 */       placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 1, 3, chunkBB);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StartPiece extends StairsDown {
/*      */     public StrongholdPieces.PieceWeight previousPiece;
/*      */     
/*      */     public StrongholdPieces.PortalRoom portalRoomPiece;
/*      */     
/*  535 */     public final List<StructurePiece> pendingChildren = Lists.newArrayList();
/*      */     
/*      */     public StartPiece(RandomSource random, int west, int north) {
/*  538 */       super(StructurePieceType.STRONGHOLD_START, 0, west, north, getRandomHorizontalDirection(random));
/*      */     }
/*      */     
/*      */     public StartPiece(CompoundTag tag) {
/*  542 */       super(StructurePieceType.STRONGHOLD_START, tag);
/*      */     }
/*      */     
/*      */     public BlockPos getLocatorPosition() {
/*  547 */       if (this.portalRoomPiece != null)
/*  548 */         return this.portalRoomPiece.getLocatorPosition(); 
/*  550 */       return super.getLocatorPosition();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Straight extends StrongholdPiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 5;
/*      */     
/*      */     private static final int DEPTH = 7;
/*      */     
/*      */     private final boolean leftChild;
/*      */     
/*      */     private final boolean rightChild;
/*      */     
/*      */     public Straight(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  563 */       super(StructurePieceType.STRONGHOLD_STRAIGHT, genDepth, boundingBox);
/*  565 */       setOrientation(direction);
/*  566 */       this.entryDoor = randomSmallDoor(random);
/*  568 */       this.leftChild = (random.nextInt(2) == 0);
/*  569 */       this.rightChild = (random.nextInt(2) == 0);
/*      */     }
/*      */     
/*      */     public Straight(CompoundTag tag) {
/*  573 */       super(StructurePieceType.STRONGHOLD_STRAIGHT, tag);
/*  574 */       this.leftChild = tag.getBooleanOr("Left", false);
/*  575 */       this.rightChild = tag.getBooleanOr("Right", false);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  580 */       super.addAdditionalSaveData(context, tag);
/*  581 */       tag.putBoolean("Left", this.leftChild);
/*  582 */       tag.putBoolean("Right", this.rightChild);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  587 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*  588 */       if (this.leftChild)
/*  589 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 2); 
/*  591 */       if (this.rightChild)
/*  592 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 2); 
/*      */     }
/*      */     
/*      */     public static Straight createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  597 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 7, direction);
/*  599 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  600 */         return null; 
/*  603 */       return new Straight(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  609 */       generateBox(level, chunkBB, 0, 0, 0, 4, 4, 6, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*  611 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/*  613 */       generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 6);
/*  615 */       BlockState eastTorch = (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST);
/*  616 */       BlockState westTorch = (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST);
/*  618 */       maybeGenerateBlock(level, chunkBB, random, 0.1F, 1, 2, 1, eastTorch);
/*  619 */       maybeGenerateBlock(level, chunkBB, random, 0.1F, 3, 2, 1, westTorch);
/*  620 */       maybeGenerateBlock(level, chunkBB, random, 0.1F, 1, 2, 5, eastTorch);
/*  621 */       maybeGenerateBlock(level, chunkBB, random, 0.1F, 3, 2, 5, westTorch);
/*  623 */       if (this.leftChild)
/*  624 */         generateBox(level, chunkBB, 0, 1, 2, 0, 3, 4, CAVE_AIR, CAVE_AIR, false); 
/*  626 */       if (this.rightChild)
/*  627 */         generateBox(level, chunkBB, 4, 1, 2, 4, 3, 4, CAVE_AIR, CAVE_AIR, false); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ChestCorridor extends StrongholdPiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 5;
/*      */     
/*      */     private static final int DEPTH = 7;
/*      */     
/*      */     private boolean hasPlacedChest;
/*      */     
/*      */     public ChestCorridor(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  640 */       super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, genDepth, boundingBox);
/*  642 */       setOrientation(direction);
/*  643 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */     
/*      */     public ChestCorridor(CompoundTag tag) {
/*  647 */       super(StructurePieceType.STRONGHOLD_CHEST_CORRIDOR, tag);
/*  648 */       this.hasPlacedChest = tag.getBooleanOr("Chest", false);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  653 */       super.addAdditionalSaveData(context, tag);
/*  654 */       tag.putBoolean("Chest", this.hasPlacedChest);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  659 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */     }
/*      */     
/*      */     public static ChestCorridor createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  663 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 7, direction);
/*  665 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  666 */         return null; 
/*  669 */       return new ChestCorridor(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  675 */       generateBox(level, chunkBB, 0, 0, 0, 4, 4, 6, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*  677 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/*  679 */       generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 6);
/*  682 */       generateBox(level, chunkBB, 3, 1, 2, 3, 1, 4, Blocks.STONE_BRICKS.defaultBlockState(), Blocks.STONE_BRICKS.defaultBlockState(), false);
/*  683 */       placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 1, chunkBB);
/*  684 */       placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 1, 5, chunkBB);
/*  685 */       placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 2, chunkBB);
/*  686 */       placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 3, 2, 4, chunkBB);
/*  687 */       for (int z = 2; z <= 4; z++)
/*  688 */         placeBlock(level, Blocks.STONE_BRICK_SLAB.defaultBlockState(), 2, 1, z, chunkBB); 
/*  691 */       if (!this.hasPlacedChest && 
/*  692 */         chunkBB.isInside((Vec3i)getWorldPos(3, 2, 3))) {
/*  693 */         this.hasPlacedChest = true;
/*  694 */         createChest(level, chunkBB, random, 3, 2, 3, BuiltInLootTables.STRONGHOLD_CORRIDOR);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StraightStairsDown extends StrongholdPiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 11;
/*      */     
/*      */     private static final int DEPTH = 8;
/*      */     
/*      */     public StraightStairsDown(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  706 */       super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, genDepth, boundingBox);
/*  708 */       setOrientation(direction);
/*  709 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */     
/*      */     public StraightStairsDown(CompoundTag tag) {
/*  713 */       super(StructurePieceType.STRONGHOLD_STRAIGHT_STAIRS_DOWN, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  718 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */     }
/*      */     
/*      */     public static StraightStairsDown createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  722 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 11, 8, direction);
/*  724 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  725 */         return null; 
/*  728 */       return new StraightStairsDown(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  734 */       generateBox(level, chunkBB, 0, 0, 0, 4, 10, 7, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*  736 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 7, 0);
/*  738 */       generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 7);
/*  741 */       BlockState stairs = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/*  742 */       for (int i = 0; i < 6; i++) {
/*  743 */         placeBlock(level, stairs, 1, 6 - i, 1 + i, chunkBB);
/*  744 */         placeBlock(level, stairs, 2, 6 - i, 1 + i, chunkBB);
/*  745 */         placeBlock(level, stairs, 3, 6 - i, 1 + i, chunkBB);
/*  746 */         if (i < 5) {
/*  747 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5 - i, 1 + i, chunkBB);
/*  748 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 2, 5 - i, 1 + i, chunkBB);
/*  749 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 5 - i, 1 + i, chunkBB);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract class Turn extends StrongholdPiece {
/*      */     protected static final int WIDTH = 5;
/*      */     
/*      */     protected static final int HEIGHT = 5;
/*      */     
/*      */     protected static final int DEPTH = 5;
/*      */     
/*      */     protected Turn(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
/*  761 */       super(type, genDepth, boundingBox);
/*      */     }
/*      */     
/*      */     public Turn(StructurePieceType type, CompoundTag tag) {
/*  765 */       super(type, tag);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class LeftTurn extends Turn {
/*      */     public LeftTurn(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  771 */       super(StructurePieceType.STRONGHOLD_LEFT_TURN, genDepth, boundingBox);
/*  773 */       setOrientation(direction);
/*  774 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */     
/*      */     public LeftTurn(CompoundTag tag) {
/*  778 */       super(StructurePieceType.STRONGHOLD_LEFT_TURN, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  783 */       Direction orientation = getOrientation();
/*  784 */       if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/*  785 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */       } else {
/*  787 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */       } 
/*      */     }
/*      */     
/*      */     public static LeftTurn createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  792 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 5, direction);
/*  794 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  795 */         return null; 
/*  798 */       return new LeftTurn(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  804 */       generateBox(level, chunkBB, 0, 0, 0, 4, 4, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*  806 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/*  808 */       Direction orientation = getOrientation();
/*  809 */       if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/*  810 */         generateBox(level, chunkBB, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } else {
/*  812 */         generateBox(level, chunkBB, 4, 1, 1, 4, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RightTurn extends Turn {
/*      */     public RightTurn(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  819 */       super(StructurePieceType.STRONGHOLD_RIGHT_TURN, genDepth, boundingBox);
/*  821 */       setOrientation(direction);
/*  822 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */     
/*      */     public RightTurn(CompoundTag tag) {
/*  826 */       super(StructurePieceType.STRONGHOLD_RIGHT_TURN, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  831 */       Direction orientation = getOrientation();
/*  832 */       if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/*  833 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */       } else {
/*  835 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */       } 
/*      */     }
/*      */     
/*      */     public static RightTurn createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  840 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 5, 5, 5, direction);
/*  842 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  843 */         return null; 
/*  846 */       return new RightTurn(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  852 */       generateBox(level, chunkBB, 0, 0, 0, 4, 4, 4, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*  854 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/*  856 */       Direction orientation = getOrientation();
/*  857 */       if (orientation == Direction.NORTH || orientation == Direction.EAST) {
/*  858 */         generateBox(level, chunkBB, 4, 1, 1, 4, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } else {
/*  860 */         generateBox(level, chunkBB, 0, 1, 1, 0, 3, 3, CAVE_AIR, CAVE_AIR, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RoomCrossing extends StrongholdPiece {
/*      */     protected static final int WIDTH = 11;
/*      */     
/*      */     protected static final int HEIGHT = 7;
/*      */     
/*      */     protected static final int DEPTH = 11;
/*      */     
/*      */     protected final int type;
/*      */     
/*      */     public RoomCrossing(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  873 */       super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, genDepth, boundingBox);
/*  875 */       setOrientation(direction);
/*  876 */       this.entryDoor = randomSmallDoor(random);
/*  877 */       this.type = random.nextInt(5);
/*      */     }
/*      */     
/*      */     public RoomCrossing(CompoundTag tag) {
/*  881 */       super(StructurePieceType.STRONGHOLD_ROOM_CROSSING, tag);
/*  882 */       this.type = tag.getIntOr("Type", 0);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  887 */       super.addAdditionalSaveData(context, tag);
/*  888 */       tag.putInt("Type", this.type);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  893 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 4, 1);
/*  894 */       generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 4);
/*  895 */       generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 4);
/*      */     }
/*      */     
/*      */     public static RoomCrossing createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  899 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 11, 7, 11, direction);
/*  901 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  902 */         return null; 
/*  905 */       return new RoomCrossing(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*      */       int i;
/*  911 */       generateBox(level, chunkBB, 0, 0, 0, 10, 6, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*  913 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 1, 0);
/*  915 */       generateBox(level, chunkBB, 4, 1, 10, 6, 3, 10, CAVE_AIR, CAVE_AIR, false);
/*  916 */       generateBox(level, chunkBB, 0, 1, 4, 0, 3, 6, CAVE_AIR, CAVE_AIR, false);
/*  917 */       generateBox(level, chunkBB, 10, 1, 4, 10, 3, 6, CAVE_AIR, CAVE_AIR, false);
/*  919 */       switch (this.type) {
/*      */         default:
/*      */           return;
/*      */         case 0:
/*  924 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, chunkBB);
/*  925 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
/*  926 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, chunkBB);
/*  927 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST), 4, 3, 5, chunkBB);
/*  928 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST), 6, 3, 5, chunkBB);
/*  929 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH), 5, 3, 4, chunkBB);
/*  930 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.NORTH), 5, 3, 6, chunkBB);
/*  931 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 4, chunkBB);
/*  932 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 5, chunkBB);
/*  933 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 4, 1, 6, chunkBB);
/*  934 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 4, chunkBB);
/*  935 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 5, chunkBB);
/*  936 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 6, 1, 6, chunkBB);
/*  937 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 4, chunkBB);
/*  938 */           placeBlock(level, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 5, 1, 6, chunkBB);
/*      */         case 1:
/*  941 */           for (i = 0; i < 5; i++) {
/*  942 */             placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3, 1, 3 + i, chunkBB);
/*  943 */             placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 7, 1, 3 + i, chunkBB);
/*  944 */             placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3 + i, 1, 3, chunkBB);
/*  945 */             placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 3 + i, 1, 7, chunkBB);
/*      */           } 
/*  947 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 1, 5, chunkBB);
/*  948 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
/*  949 */           placeBlock(level, Blocks.STONE_BRICKS.defaultBlockState(), 5, 3, 5, chunkBB);
/*  950 */           placeBlock(level, Blocks.WATER.defaultBlockState(), 5, 4, 5, chunkBB);
/*      */         case 2:
/*      */           break;
/*      */       } 
/*  953 */       for (int j = 1; j <= 9; j++) {
/*  954 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 1, 3, j, chunkBB);
/*  955 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 9, 3, j, chunkBB);
/*      */       } 
/*  957 */       for (int x = 1; x <= 9; x++) {
/*  958 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), x, 3, 1, chunkBB);
/*  959 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), x, 3, 9, chunkBB);
/*      */       } 
/*  961 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 4, chunkBB);
/*  962 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 1, 6, chunkBB);
/*  963 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 4, chunkBB);
/*  964 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 5, 3, 6, chunkBB);
/*  965 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, 1, 5, chunkBB);
/*  966 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, 1, 5, chunkBB);
/*  967 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, 3, 5, chunkBB);
/*  968 */       placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, 3, 5, chunkBB);
/*  969 */       for (int y = 1; y <= 3; y++) {
/*  970 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, y, 4, chunkBB);
/*  971 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, y, 4, chunkBB);
/*  972 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 4, y, 6, chunkBB);
/*  973 */         placeBlock(level, Blocks.COBBLESTONE.defaultBlockState(), 6, y, 6, chunkBB);
/*      */       } 
/*  975 */       placeBlock(level, Blocks.WALL_TORCH.defaultBlockState(), 5, 3, 5, chunkBB);
/*  976 */       for (int z = 2; z <= 8; z++) {
/*  977 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 2, 3, z, chunkBB);
/*  978 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 3, 3, z, chunkBB);
/*  979 */         if (z <= 3 || z >= 7) {
/*  980 */           placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 4, 3, z, chunkBB);
/*  981 */           placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 5, 3, z, chunkBB);
/*  982 */           placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 6, 3, z, chunkBB);
/*      */         } 
/*  984 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 7, 3, z, chunkBB);
/*  985 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 8, 3, z, chunkBB);
/*      */       } 
/*  987 */       BlockState ladder = (BlockState)Blocks.LADDER.defaultBlockState().setValue((Property)LadderBlock.FACING, (Comparable)Direction.WEST);
/*  988 */       placeBlock(level, ladder, 9, 1, 3, chunkBB);
/*  989 */       placeBlock(level, ladder, 9, 2, 3, chunkBB);
/*  990 */       placeBlock(level, ladder, 9, 3, 3, chunkBB);
/*  992 */       createChest(level, chunkBB, random, 3, 4, 8, BuiltInLootTables.STRONGHOLD_CROSSING);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class PrisonHall extends StrongholdPiece {
/*      */     protected static final int WIDTH = 9;
/*      */     
/*      */     protected static final int HEIGHT = 5;
/*      */     
/*      */     protected static final int DEPTH = 11;
/*      */     
/*      */     public PrisonHall(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1004 */       super(StructurePieceType.STRONGHOLD_PRISON_HALL, genDepth, boundingBox);
/* 1006 */       setOrientation(direction);
/* 1007 */       this.entryDoor = randomSmallDoor(random);
/*      */     }
/*      */     
/*      */     public PrisonHall(CompoundTag tag) {
/* 1011 */       super(StructurePieceType.STRONGHOLD_PRISON_HALL, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1016 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 1);
/*      */     }
/*      */     
/*      */     public static PrisonHall createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1020 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -1, 0, 9, 5, 11, direction);
/* 1022 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1023 */         return null; 
/* 1026 */       return new PrisonHall(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1032 */       generateBox(level, chunkBB, 0, 0, 0, 8, 4, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1034 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 1, 1, 0);
/* 1036 */       generateBox(level, chunkBB, 1, 1, 10, 3, 3, 10, CAVE_AIR, CAVE_AIR, false);
/* 1039 */       generateBox(level, chunkBB, 4, 1, 1, 4, 3, 1, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1040 */       generateBox(level, chunkBB, 4, 1, 3, 4, 3, 3, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1041 */       generateBox(level, chunkBB, 4, 1, 7, 4, 3, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1042 */       generateBox(level, chunkBB, 4, 1, 9, 4, 3, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1045 */       for (int y = 1; y <= 3; y++) {
/* 1046 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, y, 4, chunkBB);
/* 1047 */         placeBlock(level, (BlockState)((BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 4, y, 5, chunkBB);
/* 1048 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, y, 6, chunkBB);
/* 1050 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 5, y, 5, chunkBB);
/* 1051 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 6, y, 5, chunkBB);
/* 1052 */         placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true)), 7, y, 5, chunkBB);
/*      */       } 
/* 1056 */       placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, 3, 2, chunkBB);
/* 1057 */       placeBlock(level, (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true)), 4, 3, 8, chunkBB);
/* 1058 */       BlockState doorBottom = (BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue((Property)DoorBlock.FACING, (Comparable)Direction.WEST);
/* 1059 */       BlockState doorTop = (BlockState)((BlockState)Blocks.IRON_DOOR.defaultBlockState().setValue((Property)DoorBlock.FACING, (Comparable)Direction.WEST)).setValue((Property)DoorBlock.HALF, (Comparable)DoubleBlockHalf.UPPER);
/* 1060 */       placeBlock(level, doorBottom, 4, 1, 2, chunkBB);
/* 1061 */       placeBlock(level, doorTop, 4, 2, 2, chunkBB);
/* 1062 */       placeBlock(level, doorBottom, 4, 1, 8, chunkBB);
/* 1063 */       placeBlock(level, doorTop, 4, 2, 8, chunkBB);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Library extends StrongholdPiece {
/*      */     protected static final int WIDTH = 14;
/*      */     
/*      */     protected static final int HEIGHT = 6;
/*      */     
/*      */     protected static final int TALL_HEIGHT = 11;
/*      */     
/*      */     protected static final int DEPTH = 15;
/*      */     
/*      */     private final boolean isTall;
/*      */     
/*      */     public Library(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1076 */       super(StructurePieceType.STRONGHOLD_LIBRARY, genDepth, boundingBox);
/* 1078 */       setOrientation(direction);
/* 1079 */       this.entryDoor = randomSmallDoor(random);
/* 1080 */       this.isTall = (boundingBox.getYSpan() > 6);
/*      */     }
/*      */     
/*      */     public Library(CompoundTag tag) {
/* 1084 */       super(StructurePieceType.STRONGHOLD_LIBRARY, tag);
/* 1085 */       this.isTall = tag.getBooleanOr("Tall", false);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1090 */       super.addAdditionalSaveData(context, tag);
/* 1091 */       tag.putBoolean("Tall", this.isTall);
/*      */     }
/*      */     
/*      */     public static Library createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1096 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 14, 11, 15, direction);
/* 1098 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null) {
/* 1100 */         box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 14, 6, 15, direction);
/* 1102 */         if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1103 */           return null; 
/*      */       } 
/* 1107 */       return new Library(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1112 */       int currentHeight = 11;
/* 1113 */       if (!this.isTall)
/* 1114 */         currentHeight = 6; 
/* 1118 */       generateBox(level, chunkBB, 0, 0, 0, 13, currentHeight - 1, 14, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1120 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 1, 0);
/* 1123 */       generateMaybeBox(level, chunkBB, random, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.COBWEB.defaultBlockState(), Blocks.COBWEB.defaultBlockState(), false, false);
/* 1125 */       int bookLeft = 1;
/* 1126 */       int bookRight = 12;
/*      */       int d;
/* 1129 */       for (d = 1; d <= 13; d++) {
/* 1130 */         if ((d - 1) % 4 == 0) {
/* 1131 */           generateBox(level, chunkBB, 1, 1, d, 1, 4, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1132 */           generateBox(level, chunkBB, 12, 1, d, 12, 4, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1134 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST), 2, 3, d, chunkBB);
/* 1135 */           placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST), 11, 3, d, chunkBB);
/* 1137 */           if (this.isTall) {
/* 1138 */             generateBox(level, chunkBB, 1, 6, d, 1, 9, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1139 */             generateBox(level, chunkBB, 12, 6, d, 12, 9, d, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */           } 
/*      */         } else {
/* 1142 */           generateBox(level, chunkBB, 1, 1, d, 1, 4, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1143 */           generateBox(level, chunkBB, 12, 1, d, 12, 4, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1145 */           if (this.isTall) {
/* 1146 */             generateBox(level, chunkBB, 1, 6, d, 1, 9, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1147 */             generateBox(level, chunkBB, 12, 6, d, 12, 9, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */           } 
/*      */         } 
/*      */       } 
/* 1153 */       for (d = 3; d < 12; d += 2) {
/* 1154 */         generateBox(level, chunkBB, 3, 1, d, 4, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1155 */         generateBox(level, chunkBB, 6, 1, d, 7, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1156 */         generateBox(level, chunkBB, 9, 1, d, 10, 3, d, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */       } 
/* 1159 */       if (this.isTall) {
/* 1161 */         generateBox(level, chunkBB, 1, 5, 1, 3, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1162 */         generateBox(level, chunkBB, 10, 5, 1, 12, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1163 */         generateBox(level, chunkBB, 4, 5, 1, 9, 5, 2, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1164 */         generateBox(level, chunkBB, 4, 5, 12, 9, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1166 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 11, chunkBB);
/* 1167 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 8, 5, 11, chunkBB);
/* 1168 */         placeBlock(level, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 10, chunkBB);
/* 1170 */         BlockState weFence = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1171 */         BlockState nsFence = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1174 */         generateBox(level, chunkBB, 3, 6, 3, 3, 6, 11, nsFence, nsFence, false);
/* 1175 */         generateBox(level, chunkBB, 10, 6, 3, 10, 6, 9, nsFence, nsFence, false);
/* 1176 */         generateBox(level, chunkBB, 4, 6, 2, 9, 6, 2, weFence, weFence, false);
/* 1177 */         generateBox(level, chunkBB, 4, 6, 12, 7, 6, 12, weFence, weFence, false);
/* 1179 */         placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 2, chunkBB);
/* 1180 */         placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 12, chunkBB);
/* 1181 */         placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 10, 6, 2, chunkBB);
/* 1183 */         for (int i = 0; i <= 2; i++) {
/* 1184 */           placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 8 + i, 6, 12 - i, chunkBB);
/* 1185 */           if (i != 2)
/* 1186 */             placeBlock(level, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 8 + i, 6, 11 - i, chunkBB); 
/*      */         } 
/* 1191 */         BlockState ladder = (BlockState)Blocks.LADDER.defaultBlockState().setValue((Property)LadderBlock.FACING, (Comparable)Direction.SOUTH);
/* 1192 */         placeBlock(level, ladder, 10, 1, 13, chunkBB);
/* 1193 */         placeBlock(level, ladder, 10, 2, 13, chunkBB);
/* 1194 */         placeBlock(level, ladder, 10, 3, 13, chunkBB);
/* 1195 */         placeBlock(level, ladder, 10, 4, 13, chunkBB);
/* 1196 */         placeBlock(level, ladder, 10, 5, 13, chunkBB);
/* 1197 */         placeBlock(level, ladder, 10, 6, 13, chunkBB);
/* 1198 */         placeBlock(level, ladder, 10, 7, 13, chunkBB);
/* 1201 */         int x = 7;
/* 1202 */         int z = 7;
/* 1203 */         BlockState eFence = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1204 */         placeBlock(level, eFence, 6, 9, 7, chunkBB);
/* 1205 */         BlockState wFence = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/* 1206 */         placeBlock(level, wFence, 7, 9, 7, chunkBB);
/* 1208 */         placeBlock(level, eFence, 6, 8, 7, chunkBB);
/* 1209 */         placeBlock(level, wFence, 7, 8, 7, chunkBB);
/* 1211 */         BlockState nsweFence = (BlockState)((BlockState)nsFence.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1213 */         placeBlock(level, nsweFence, 6, 7, 7, chunkBB);
/* 1214 */         placeBlock(level, nsweFence, 7, 7, 7, chunkBB);
/* 1216 */         placeBlock(level, eFence, 5, 7, 7, chunkBB);
/* 1218 */         placeBlock(level, wFence, 8, 7, 7, chunkBB);
/* 1220 */         placeBlock(level, (BlockState)eFence.setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 6, 7, 6, chunkBB);
/* 1221 */         placeBlock(level, (BlockState)eFence.setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 7, 8, chunkBB);
/* 1223 */         placeBlock(level, (BlockState)wFence.setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 7, 7, 6, chunkBB);
/* 1224 */         placeBlock(level, (BlockState)wFence.setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 7, 7, 8, chunkBB);
/* 1226 */         BlockState torch = Blocks.TORCH.defaultBlockState();
/* 1227 */         placeBlock(level, torch, 5, 8, 7, chunkBB);
/* 1228 */         placeBlock(level, torch, 8, 8, 7, chunkBB);
/* 1229 */         placeBlock(level, torch, 6, 8, 6, chunkBB);
/* 1230 */         placeBlock(level, torch, 6, 8, 8, chunkBB);
/* 1231 */         placeBlock(level, torch, 7, 8, 6, chunkBB);
/* 1232 */         placeBlock(level, torch, 7, 8, 8, chunkBB);
/*      */       } 
/* 1236 */       createChest(level, chunkBB, random, 3, 3, 5, BuiltInLootTables.STRONGHOLD_LIBRARY);
/* 1237 */       if (this.isTall) {
/* 1238 */         placeBlock(level, CAVE_AIR, 12, 9, 1, chunkBB);
/* 1239 */         createChest(level, chunkBB, random, 12, 8, 1, BuiltInLootTables.STRONGHOLD_LIBRARY);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class FiveCrossing extends StrongholdPiece {
/*      */     protected static final int WIDTH = 10;
/*      */     
/*      */     protected static final int HEIGHT = 9;
/*      */     
/*      */     protected static final int DEPTH = 11;
/*      */     
/*      */     private final boolean leftLow;
/*      */     
/*      */     private final boolean leftHigh;
/*      */     
/*      */     private final boolean rightLow;
/*      */     
/*      */     private final boolean rightHigh;
/*      */     
/*      */     public FiveCrossing(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1255 */       super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, genDepth, boundingBox);
/* 1257 */       setOrientation(direction);
/* 1258 */       this.entryDoor = randomSmallDoor(random);
/* 1260 */       this.leftLow = random.nextBoolean();
/* 1261 */       this.leftHigh = random.nextBoolean();
/* 1262 */       this.rightLow = random.nextBoolean();
/* 1263 */       this.rightHigh = (random.nextInt(3) > 0);
/*      */     }
/*      */     
/*      */     public FiveCrossing(CompoundTag tag) {
/* 1267 */       super(StructurePieceType.STRONGHOLD_FIVE_CROSSING, tag);
/* 1268 */       this.leftLow = tag.getBooleanOr("leftLow", false);
/* 1269 */       this.leftHigh = tag.getBooleanOr("leftHigh", false);
/* 1270 */       this.rightLow = tag.getBooleanOr("rightLow", false);
/* 1271 */       this.rightHigh = tag.getBooleanOr("rightHigh", false);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1276 */       super.addAdditionalSaveData(context, tag);
/* 1277 */       tag.putBoolean("leftLow", this.leftLow);
/* 1278 */       tag.putBoolean("leftHigh", this.leftHigh);
/* 1279 */       tag.putBoolean("rightLow", this.rightLow);
/* 1280 */       tag.putBoolean("rightHigh", this.rightHigh);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1285 */       int zOffA = 3;
/* 1286 */       int zOffB = 5;
/* 1288 */       Direction orientation = getOrientation();
/* 1289 */       if (orientation == Direction.WEST || orientation == Direction.NORTH) {
/* 1290 */         zOffA = 8 - zOffA;
/* 1291 */         zOffB = 8 - zOffB;
/*      */       } 
/* 1294 */       generateSmallDoorChildForward((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 1);
/* 1295 */       if (this.leftLow)
/* 1296 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffA, 1); 
/* 1298 */       if (this.leftHigh)
/* 1299 */         generateSmallDoorChildLeft((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffB, 7); 
/* 1301 */       if (this.rightLow)
/* 1302 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffA, 1); 
/* 1304 */       if (this.rightHigh)
/* 1305 */         generateSmallDoorChildRight((StrongholdPieces.StartPiece)startPiece, structurePieceAccessor, random, zOffB, 7); 
/*      */     }
/*      */     
/*      */     public static FiveCrossing createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1310 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -3, 0, 10, 9, 11, direction);
/* 1312 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1313 */         return null; 
/* 1316 */       return new FiveCrossing(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1322 */       generateBox(level, chunkBB, 0, 0, 0, 9, 8, 10, true, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1324 */       generateSmallDoor(level, random, chunkBB, this.entryDoor, 4, 3, 0);
/* 1327 */       if (this.leftLow)
/* 1328 */         generateBox(level, chunkBB, 0, 3, 1, 0, 5, 3, CAVE_AIR, CAVE_AIR, false); 
/* 1330 */       if (this.rightLow)
/* 1331 */         generateBox(level, chunkBB, 9, 3, 1, 9, 5, 3, CAVE_AIR, CAVE_AIR, false); 
/* 1333 */       if (this.leftHigh)
/* 1334 */         generateBox(level, chunkBB, 0, 5, 7, 0, 7, 9, CAVE_AIR, CAVE_AIR, false); 
/* 1336 */       if (this.rightHigh)
/* 1337 */         generateBox(level, chunkBB, 9, 5, 7, 9, 7, 9, CAVE_AIR, CAVE_AIR, false); 
/* 1339 */       generateBox(level, chunkBB, 5, 1, 10, 7, 3, 10, CAVE_AIR, CAVE_AIR, false);
/* 1342 */       generateBox(level, chunkBB, 1, 2, 1, 8, 2, 6, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1344 */       generateBox(level, chunkBB, 4, 1, 5, 4, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1345 */       generateBox(level, chunkBB, 8, 1, 5, 8, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1347 */       generateBox(level, chunkBB, 1, 4, 7, 3, 4, 9, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1350 */       generateBox(level, chunkBB, 1, 3, 5, 3, 3, 6, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1351 */       generateBox(level, chunkBB, 1, 3, 4, 3, 3, 4, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1352 */       generateBox(level, chunkBB, 1, 4, 6, 3, 4, 6, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1355 */       generateBox(level, chunkBB, 5, 1, 7, 7, 1, 8, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1356 */       generateBox(level, chunkBB, 5, 1, 9, 7, 1, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1357 */       generateBox(level, chunkBB, 5, 2, 7, 7, 2, 7, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1360 */       generateBox(level, chunkBB, 4, 5, 7, 4, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1361 */       generateBox(level, chunkBB, 8, 5, 7, 8, 5, 9, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), false);
/* 1362 */       generateBox(level, chunkBB, 5, 5, 7, 7, 5, 9, (BlockState)Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE), (BlockState)Blocks.SMOOTH_STONE_SLAB.defaultBlockState().setValue((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE), false);
/* 1363 */       placeBlock(level, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH), 6, 5, 6, chunkBB);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class PortalRoom extends StrongholdPiece {
/*      */     protected static final int WIDTH = 11;
/*      */     
/*      */     protected static final int HEIGHT = 8;
/*      */     
/*      */     protected static final int DEPTH = 16;
/*      */     
/*      */     private boolean hasPlacedSpawner;
/*      */     
/*      */     public PortalRoom(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1375 */       super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, genDepth, boundingBox);
/* 1377 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public PortalRoom(CompoundTag tag) {
/* 1381 */       super(StructurePieceType.STRONGHOLD_PORTAL_ROOM, tag);
/* 1382 */       this.hasPlacedSpawner = tag.getBooleanOr("Mob", false);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1387 */       super.addAdditionalSaveData(context, tag);
/* 1388 */       tag.putBoolean("Mob", this.hasPlacedSpawner);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1393 */       if (startPiece != null)
/* 1394 */         ((StrongholdPieces.StartPiece)startPiece).portalRoomPiece = this; 
/*      */     }
/*      */     
/*      */     public static PortalRoom createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1399 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -4, -1, 0, 11, 8, 16, direction);
/* 1401 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1402 */         return null; 
/* 1405 */       return new PortalRoom(genDepth, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1411 */       generateBox(level, chunkBB, 0, 0, 0, 10, 7, 15, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1413 */       generateSmallDoor(level, random, chunkBB, StrongholdPieces.StrongholdPiece.SmallDoorType.GRATES, 4, 1, 0);
/* 1416 */       int y = 6;
/* 1417 */       generateBox(level, chunkBB, 1, 6, 1, 1, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1418 */       generateBox(level, chunkBB, 9, 6, 1, 9, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1419 */       generateBox(level, chunkBB, 2, 6, 1, 8, 6, 2, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1420 */       generateBox(level, chunkBB, 2, 6, 14, 8, 6, 14, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1423 */       generateBox(level, chunkBB, 1, 1, 1, 2, 1, 4, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1424 */       generateBox(level, chunkBB, 8, 1, 1, 9, 1, 4, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1425 */       generateBox(level, chunkBB, 1, 1, 1, 1, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/* 1426 */       generateBox(level, chunkBB, 9, 1, 1, 9, 1, 3, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/* 1429 */       generateBox(level, chunkBB, 3, 1, 8, 7, 1, 12, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1430 */       generateBox(level, chunkBB, 4, 1, 9, 6, 1, 11, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
/* 1433 */       BlockState nsBars = (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.NORTH, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.SOUTH, Boolean.valueOf(true));
/* 1434 */       BlockState weBars = (BlockState)((BlockState)Blocks.IRON_BARS.defaultBlockState().setValue((Property)IronBarsBlock.WEST, Boolean.valueOf(true))).setValue((Property)IronBarsBlock.EAST, Boolean.valueOf(true));
/* 1435 */       for (int z = 3; z < 14; z += 2) {
/* 1436 */         generateBox(level, chunkBB, 0, 3, z, 0, 4, z, nsBars, nsBars, false);
/* 1437 */         generateBox(level, chunkBB, 10, 3, z, 10, 4, z, nsBars, nsBars, false);
/*      */       } 
/* 1439 */       for (int x = 2; x < 9; x += 2)
/* 1440 */         generateBox(level, chunkBB, x, 3, 15, x, 4, 15, weBars, weBars, false); 
/* 1444 */       BlockState blockState = (BlockState)Blocks.STONE_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/* 1445 */       generateBox(level, chunkBB, 4, 1, 5, 6, 1, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1446 */       generateBox(level, chunkBB, 4, 2, 6, 6, 2, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1447 */       generateBox(level, chunkBB, 4, 3, 7, 6, 3, 7, false, random, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/* 1448 */       for (int j = 4; j <= 6; j++) {
/* 1449 */         placeBlock(level, blockState, j, 1, 4, chunkBB);
/* 1450 */         placeBlock(level, blockState, j, 2, 5, chunkBB);
/* 1451 */         placeBlock(level, blockState, j, 3, 6, chunkBB);
/*      */       } 
/* 1454 */       BlockState northFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.NORTH);
/* 1455 */       BlockState southFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.SOUTH);
/* 1456 */       BlockState eastFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.EAST);
/* 1457 */       BlockState westFrame = (BlockState)Blocks.END_PORTAL_FRAME.defaultBlockState().setValue((Property)EndPortalFrameBlock.FACING, (Comparable)Direction.WEST);
/* 1459 */       boolean allEyes = true;
/* 1460 */       boolean[] eyes = new boolean[12];
/* 1461 */       for (int i = 0; i < eyes.length; i++) {
/* 1462 */         eyes[i] = (random.nextFloat() > 0.9F);
/* 1463 */         allEyes &= eyes[i];
/*      */       } 
/* 1466 */       placeBlock(level, (BlockState)northFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[0])), 4, 3, 8, chunkBB);
/* 1467 */       placeBlock(level, (BlockState)northFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[1])), 5, 3, 8, chunkBB);
/* 1468 */       placeBlock(level, (BlockState)northFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[2])), 6, 3, 8, chunkBB);
/* 1469 */       placeBlock(level, (BlockState)southFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[3])), 4, 3, 12, chunkBB);
/* 1470 */       placeBlock(level, (BlockState)southFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[4])), 5, 3, 12, chunkBB);
/* 1471 */       placeBlock(level, (BlockState)southFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[5])), 6, 3, 12, chunkBB);
/* 1472 */       placeBlock(level, (BlockState)eastFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[6])), 3, 3, 9, chunkBB);
/* 1473 */       placeBlock(level, (BlockState)eastFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[7])), 3, 3, 10, chunkBB);
/* 1474 */       placeBlock(level, (BlockState)eastFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[8])), 3, 3, 11, chunkBB);
/* 1475 */       placeBlock(level, (BlockState)westFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[9])), 7, 3, 9, chunkBB);
/* 1476 */       placeBlock(level, (BlockState)westFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[10])), 7, 3, 10, chunkBB);
/* 1477 */       placeBlock(level, (BlockState)westFrame.setValue((Property)EndPortalFrameBlock.HAS_EYE, Boolean.valueOf(eyes[11])), 7, 3, 11, chunkBB);
/* 1479 */       if (allEyes) {
/* 1480 */         BlockState portal = Blocks.END_PORTAL.defaultBlockState();
/* 1482 */         placeBlock(level, portal, 4, 3, 9, chunkBB);
/* 1483 */         placeBlock(level, portal, 5, 3, 9, chunkBB);
/* 1484 */         placeBlock(level, portal, 6, 3, 9, chunkBB);
/* 1485 */         placeBlock(level, portal, 4, 3, 10, chunkBB);
/* 1486 */         placeBlock(level, portal, 5, 3, 10, chunkBB);
/* 1487 */         placeBlock(level, portal, 6, 3, 10, chunkBB);
/* 1488 */         placeBlock(level, portal, 4, 3, 11, chunkBB);
/* 1489 */         placeBlock(level, portal, 5, 3, 11, chunkBB);
/* 1490 */         placeBlock(level, portal, 6, 3, 11, chunkBB);
/*      */       } 
/* 1493 */       if (!this.hasPlacedSpawner) {
/* 1494 */         BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(5, 3, 6);
/* 1495 */         if (chunkBB.isInside((Vec3i)mutableBlockPos)) {
/* 1496 */           this.hasPlacedSpawner = true;
/* 1497 */           level.setBlock((BlockPos)mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/* 1499 */           BlockEntity blockEntity = level.getBlockEntity((BlockPos)mutableBlockPos);
/* 1500 */           if (blockEntity instanceof SpawnerBlockEntity) {
/* 1500 */             SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/* 1501 */             spawner.setEntityId(EntityTypes.SILVERFISH, random);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SmoothStoneSelector extends StructurePiece.BlockSelector {
/*      */     public void next(RandomSource random, int worldX, int worldY, int worldZ, boolean isEdge) {
/* 1511 */       if (isEdge) {
/* 1512 */         float selection = random.nextFloat();
/* 1513 */         if (selection < 0.2F) {
/* 1514 */           this.next = Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
/* 1515 */         } else if (selection < 0.5F) {
/* 1516 */           this.next = Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
/* 1517 */         } else if (selection < 0.55F) {
/* 1518 */           this.next = Blocks.INFESTED_STONE_BRICKS.defaultBlockState();
/*      */         } else {
/* 1520 */           this.next = Blocks.STONE_BRICKS.defaultBlockState();
/*      */         } 
/*      */       } else {
/* 1523 */         this.next = Blocks.CAVE_AIR.defaultBlockState();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/* 1528 */   private static final SmoothStoneSelector SMOOTH_STONE_SELECTOR = new SmoothStoneSelector();
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */