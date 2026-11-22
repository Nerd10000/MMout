/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.StairBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.Fluids;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ public class NetherFortressPieces {
/*      */   private static final int MAX_DEPTH = 30;
/*      */   
/*      */   private static final int LOWEST_Y_POSITION = 10;
/*      */   
/*      */   public static final int MAGIC_START_Y = 64;
/*      */   
/*      */   private static class PieceWeight {
/*      */     public final Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass;
/*      */     
/*      */     public final int weight;
/*      */     
/*      */     public int placeCount;
/*      */     
/*      */     public final int maxPlaceCount;
/*      */     
/*      */     public final boolean allowInRow;
/*      */     
/*      */     public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass, int weight, int maxPlaceCount, boolean allowInRow) {
/*   46 */       this.pieceClass = pieceClass;
/*   47 */       this.weight = weight;
/*   48 */       this.maxPlaceCount = maxPlaceCount;
/*   49 */       this.allowInRow = allowInRow;
/*      */     }
/*      */     
/*      */     public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass, int weight, int maxPlaceCount) {
/*   53 */       this(pieceClass, weight, maxPlaceCount, false);
/*      */     }
/*      */     
/*      */     public boolean doPlace(int depth) {
/*   57 */       return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*      */     }
/*      */     
/*      */     public boolean isValid() {
/*   61 */       return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*      */     }
/*      */   }
/*      */   
/*   65 */   private static final PieceWeight[] BRIDGE_PIECE_WEIGHTS = new PieceWeight[] { new PieceWeight((Class)BridgeStraight.class, 30, 0, true), new PieceWeight((Class)BridgeCrossing.class, 10, 4), new PieceWeight((Class)RoomCrossing.class, 10, 4), new PieceWeight((Class)StairsRoom.class, 10, 3), new PieceWeight((Class)MonsterThrone.class, 5, 2), new PieceWeight((Class)CastleEntrance.class, 5, 1) };
/*      */   
/*   73 */   private static final PieceWeight[] CASTLE_PIECE_WEIGHTS = new PieceWeight[] { new PieceWeight((Class)CastleSmallCorridorPiece.class, 25, 0, true), new PieceWeight((Class)CastleSmallCorridorCrossingPiece.class, 15, 5), new PieceWeight((Class)CastleSmallCorridorRightTurnPiece.class, 5, 10), new PieceWeight((Class)CastleSmallCorridorLeftTurnPiece.class, 5, 10), new PieceWeight((Class)CastleCorridorStairsPiece.class, 10, 3, true), new PieceWeight((Class)CastleCorridorTBalconyPiece.class, 7, 2), new PieceWeight((Class)CastleStalkRoom.class, 5, 2) };
/*      */   
/*      */   private static NetherBridgePiece findAndCreateBridgePieceFactory(PieceWeight piece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/*   84 */     Class<? extends NetherBridgePiece> pieceClass = piece.pieceClass;
/*   85 */     NetherBridgePiece structurePiece = null;
/*   87 */     if (pieceClass == BridgeStraight.class) {
/*   88 */       structurePiece = BridgeStraight.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*   89 */     } else if (pieceClass == BridgeCrossing.class) {
/*   90 */       structurePiece = BridgeCrossing.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*   91 */     } else if (pieceClass == RoomCrossing.class) {
/*   92 */       structurePiece = RoomCrossing.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*   93 */     } else if (pieceClass == StairsRoom.class) {
/*   94 */       structurePiece = StairsRoom.createPiece(structurePieceAccessor, footX, footY, footZ, depth, direction);
/*   95 */     } else if (pieceClass == MonsterThrone.class) {
/*   96 */       structurePiece = MonsterThrone.createPiece(structurePieceAccessor, footX, footY, footZ, depth, direction);
/*   97 */     } else if (pieceClass == CastleEntrance.class) {
/*   98 */       structurePiece = CastleEntrance.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*   99 */     } else if (pieceClass == CastleSmallCorridorPiece.class) {
/*  100 */       structurePiece = CastleSmallCorridorPiece.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*  101 */     } else if (pieceClass == CastleSmallCorridorRightTurnPiece.class) {
/*  102 */       structurePiece = CastleSmallCorridorRightTurnPiece.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  103 */     } else if (pieceClass == CastleSmallCorridorLeftTurnPiece.class) {
/*  104 */       structurePiece = CastleSmallCorridorLeftTurnPiece.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  105 */     } else if (pieceClass == CastleCorridorStairsPiece.class) {
/*  106 */       structurePiece = CastleCorridorStairsPiece.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*  107 */     } else if (pieceClass == CastleCorridorTBalconyPiece.class) {
/*  108 */       structurePiece = CastleCorridorTBalconyPiece.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*  109 */     } else if (pieceClass == CastleSmallCorridorCrossingPiece.class) {
/*  110 */       structurePiece = CastleSmallCorridorCrossingPiece.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*  111 */     } else if (pieceClass == CastleStalkRoom.class) {
/*  112 */       structurePiece = CastleStalkRoom.createPiece(structurePieceAccessor, footX, footY, footZ, direction, depth);
/*      */     } 
/*  114 */     return structurePiece;
/*      */   }
/*      */   
/*      */   private static abstract class NetherBridgePiece extends StructurePiece {
/*      */     protected NetherBridgePiece(StructurePieceType type, int genDepth, BoundingBox boundingBox) {
/*  119 */       super(type, genDepth, boundingBox);
/*      */     }
/*      */     
/*      */     public NetherBridgePiece(StructurePieceType type, CompoundTag tag) {
/*  123 */       super(type, tag);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {}
/*      */     
/*      */     private int updatePieceWeight(List<NetherFortressPieces.PieceWeight> currentPieces) {
/*  131 */       boolean hasAnyPieces = false;
/*  132 */       int totalWeight = 0;
/*  133 */       for (NetherFortressPieces.PieceWeight piece : currentPieces) {
/*  134 */         if (piece.maxPlaceCount > 0 && piece.placeCount < piece.maxPlaceCount)
/*  135 */           hasAnyPieces = true; 
/*  137 */         totalWeight += piece.weight;
/*      */       } 
/*  139 */       return hasAnyPieces ? totalWeight : -1;
/*      */     }
/*      */     
/*      */     private NetherBridgePiece generatePiece(NetherFortressPieces.StartPiece startPiece, List<NetherFortressPieces.PieceWeight> currentPieces, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth) {
/*  143 */       int totalWeight = updatePieceWeight(currentPieces);
/*  144 */       boolean doStuff = (totalWeight > 0 && depth <= 30);
/*  146 */       int numAttempts = 0;
/*  147 */       while (numAttempts < 5 && doStuff) {
/*  148 */         numAttempts++;
/*  150 */         int weightSelection = random.nextInt(totalWeight);
/*  151 */         for (NetherFortressPieces.PieceWeight piece : currentPieces) {
/*  152 */           weightSelection -= piece.weight;
/*  153 */           if (weightSelection < 0) {
/*  154 */             if (!piece.doPlace(depth) || (piece == startPiece.previousPiece && !piece.allowInRow))
/*      */               break; 
/*  158 */             NetherBridgePiece structurePiece = NetherFortressPieces.findAndCreateBridgePieceFactory(piece, structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*  159 */             if (structurePiece != null) {
/*  160 */               piece.placeCount++;
/*  161 */               startPiece.previousPiece = piece;
/*  163 */               if (!piece.isValid())
/*  164 */                 currentPieces.remove(piece); 
/*  166 */               return structurePiece;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  171 */       return NetherFortressPieces.BridgeEndFiller.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth);
/*      */     }
/*      */     
/*      */     private StructurePiece generateAndAddPiece(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int depth, boolean isCastle) {
/*  175 */       if (Math.abs(footX - startPiece.getBoundingBox().minX()) > 112 || Math.abs(footZ - startPiece.getBoundingBox().minZ()) > 112)
/*  176 */         return NetherFortressPieces.BridgeEndFiller.createPiece(structurePieceAccessor, random, footX, footY, footZ, direction, depth); 
/*  178 */       List<NetherFortressPieces.PieceWeight> availablePieces = startPiece.availableBridgePieces;
/*  179 */       if (isCastle)
/*  180 */         availablePieces = startPiece.availableCastlePieces; 
/*  182 */       StructurePiece newPiece = generatePiece(startPiece, availablePieces, structurePieceAccessor, random, footX, footY, footZ, direction, depth + 1);
/*  183 */       if (newPiece != null) {
/*  184 */         structurePieceAccessor.addPiece(newPiece);
/*  185 */         startPiece.pendingChildren.add(newPiece);
/*      */       } 
/*  187 */       return newPiece;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateChildForward(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int xOff, int yOff, boolean isCastle) {
/*  191 */       Direction orientation = getOrientation();
/*  192 */       if (orientation != null)
/*  193 */         switch (orientation) {
/*      */           case NORTH:
/*  195 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + xOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, orientation, getGenDepth(), isCastle);
/*      */           case SOUTH:
/*  198 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + xOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, orientation, getGenDepth(), isCastle);
/*      */           case WEST:
/*  201 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + xOff, orientation, getGenDepth(), isCastle);
/*      */           case EAST:
/*  204 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + xOff, orientation, getGenDepth(), isCastle);
/*      */         }  
/*  208 */       return null;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateChildLeft(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff, boolean isCastle) {
/*  212 */       Direction orientation = getOrientation();
/*  213 */       if (orientation != null)
/*  214 */         switch (orientation) {
/*      */           case NORTH:
/*  216 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.WEST, getGenDepth(), isCastle);
/*      */           case SOUTH:
/*  219 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.WEST, getGenDepth(), isCastle);
/*      */           case WEST:
/*  222 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), isCastle);
/*      */           case EAST:
/*  225 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), isCastle);
/*      */         }  
/*  229 */       return null;
/*      */     }
/*      */     
/*      */     protected StructurePiece generateChildRight(NetherFortressPieces.StartPiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random, int yOff, int zOff, boolean isCastle) {
/*  233 */       Direction orientation = getOrientation();
/*  234 */       if (orientation != null)
/*  235 */         switch (orientation) {
/*      */           case NORTH:
/*  237 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.EAST, getGenDepth(), isCastle);
/*      */           case SOUTH:
/*  240 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + yOff, this.boundingBox.minZ() + zOff, Direction.EAST, getGenDepth(), isCastle);
/*      */           case WEST:
/*  243 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), isCastle);
/*      */           case EAST:
/*  246 */             return generateAndAddPiece(startPiece, structurePieceAccessor, random, this.boundingBox.minX() + zOff, this.boundingBox.minY() + yOff, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), isCastle);
/*      */         }  
/*  250 */       return null;
/*      */     }
/*      */     
/*      */     protected static boolean isOkBox(BoundingBox box) {
/*  254 */       return (box.minY() > 10);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StartPiece extends BridgeCrossing {
/*      */     private NetherFortressPieces.PieceWeight previousPiece;
/*      */     
/*  261 */     private final List<NetherFortressPieces.PieceWeight> availableBridgePieces = new ArrayList<>();
/*      */     
/*  262 */     private final List<NetherFortressPieces.PieceWeight> availableCastlePieces = new ArrayList<>();
/*      */     
/*  265 */     public final List<StructurePiece> pendingChildren = Lists.newArrayList();
/*      */     
/*      */     public StartPiece(RandomSource random, int west, int north) {
/*  268 */       super(west, north, getRandomHorizontalDirection(random));
/*  270 */       for (NetherFortressPieces.PieceWeight piece : NetherFortressPieces.BRIDGE_PIECE_WEIGHTS) {
/*  271 */         piece.placeCount = 0;
/*  272 */         this.availableBridgePieces.add(piece);
/*      */       } 
/*  275 */       for (NetherFortressPieces.PieceWeight piece : NetherFortressPieces.CASTLE_PIECE_WEIGHTS) {
/*  276 */         piece.placeCount = 0;
/*  277 */         this.availableCastlePieces.add(piece);
/*      */       } 
/*      */     }
/*      */     
/*      */     public StartPiece(CompoundTag tag) {
/*  282 */       super(StructurePieceType.NETHER_FORTRESS_START, tag);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BridgeStraight extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 10;
/*      */     
/*      */     private static final int DEPTH = 19;
/*      */     
/*      */     public BridgeStraight(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  292 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, genDepth, boundingBox);
/*  294 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public BridgeStraight(CompoundTag tag) {
/*  298 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  303 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 3, false);
/*      */     }
/*      */     
/*      */     public static BridgeStraight createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  307 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -3, 0, 5, 10, 19, direction);
/*  309 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  310 */         return null; 
/*  313 */       return new BridgeStraight(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  319 */       generateBox(level, chunkBB, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  321 */       generateBox(level, chunkBB, 1, 5, 0, 3, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  324 */       generateBox(level, chunkBB, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  325 */       generateBox(level, chunkBB, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  328 */       generateBox(level, chunkBB, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  329 */       generateBox(level, chunkBB, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  330 */       generateBox(level, chunkBB, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  331 */       generateBox(level, chunkBB, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  333 */       for (int x = 0; x <= 4; x++) {
/*  334 */         for (int z = 0; z <= 2; z++) {
/*  335 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/*  336 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 18 - z, chunkBB);
/*      */         } 
/*      */       } 
/*  340 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  341 */       BlockState nseFence = (BlockState)nsFence.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  342 */       BlockState nswFence = (BlockState)nsFence.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/*  343 */       generateBox(level, chunkBB, 0, 1, 1, 0, 4, 1, nseFence, nseFence, false);
/*  344 */       generateBox(level, chunkBB, 0, 3, 4, 0, 4, 4, nseFence, nseFence, false);
/*  345 */       generateBox(level, chunkBB, 0, 3, 14, 0, 4, 14, nseFence, nseFence, false);
/*  346 */       generateBox(level, chunkBB, 0, 1, 17, 0, 4, 17, nseFence, nseFence, false);
/*  347 */       generateBox(level, chunkBB, 4, 1, 1, 4, 4, 1, nswFence, nswFence, false);
/*  348 */       generateBox(level, chunkBB, 4, 3, 4, 4, 4, 4, nswFence, nswFence, false);
/*  349 */       generateBox(level, chunkBB, 4, 3, 14, 4, 4, 14, nswFence, nswFence, false);
/*  350 */       generateBox(level, chunkBB, 4, 1, 17, 4, 4, 17, nswFence, nswFence, false);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BridgeEndFiller extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 10;
/*      */     
/*      */     private static final int DEPTH = 8;
/*      */     
/*      */     private final int selfSeed;
/*      */     
/*      */     public BridgeEndFiller(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  362 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, genDepth, boundingBox);
/*  364 */       setOrientation(direction);
/*  365 */       this.selfSeed = random.nextInt();
/*      */     }
/*      */     
/*      */     public BridgeEndFiller(CompoundTag tag) {
/*  369 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_END_FILLER, tag);
/*  370 */       this.selfSeed = tag.getIntOr("Seed", 0);
/*      */     }
/*      */     
/*      */     public static BridgeEndFiller createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  374 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -3, 0, 5, 10, 8, direction);
/*  376 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  377 */         return null; 
/*  380 */       return new BridgeEndFiller(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  385 */       super.addAdditionalSaveData(context, tag);
/*  387 */       tag.putInt("Seed", this.selfSeed);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  392 */       RandomSource selfRandom = RandomSource.createThreadLocalInstance(this.selfSeed);
/*  395 */       for (int i = 0; i <= 4; i++) {
/*  396 */         for (int y = 3; y <= 4; y++) {
/*  397 */           int j = selfRandom.nextInt(8);
/*  398 */           generateBox(level, chunkBB, i, y, 0, i, y, j, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*  404 */       int z = selfRandom.nextInt(8);
/*  405 */       generateBox(level, chunkBB, 0, 5, 0, 0, 5, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  408 */       z = selfRandom.nextInt(8);
/*  409 */       generateBox(level, chunkBB, 4, 5, 0, 4, 5, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       int x;
/*  413 */       for (x = 0; x <= 4; x++) {
/*  414 */         int j = selfRandom.nextInt(5);
/*  415 */         generateBox(level, chunkBB, x, 2, 0, x, 2, j, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       } 
/*  417 */       for (x = 0; x <= 4; x++) {
/*  418 */         for (int y = 0; y <= 1; y++) {
/*  419 */           int j = selfRandom.nextInt(3);
/*  420 */           generateBox(level, chunkBB, x, y, 0, x, y, j, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class BridgeCrossing extends NetherBridgePiece {
/*      */     private static final int WIDTH = 19;
/*      */     
/*      */     private static final int HEIGHT = 10;
/*      */     
/*      */     private static final int DEPTH = 19;
/*      */     
/*      */     public BridgeCrossing(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  432 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, genDepth, boundingBox);
/*  434 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     protected BridgeCrossing(int west, int north, Direction direction) {
/*  438 */       super(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, 0, StructurePiece.makeBoundingBox(west, 64, north, direction, 19, 10, 19));
/*  440 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     protected BridgeCrossing(StructurePieceType type, CompoundTag tag) {
/*  444 */       super(type, tag);
/*      */     }
/*      */     
/*      */     public BridgeCrossing(CompoundTag tag) {
/*  448 */       this(StructurePieceType.NETHER_FORTRESS_BRIDGE_CROSSING, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  453 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 8, 3, false);
/*  454 */       generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 3, 8, false);
/*  455 */       generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 3, 8, false);
/*      */     }
/*      */     
/*      */     public static BridgeCrossing createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  459 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -8, -3, 0, 19, 10, 19, direction);
/*  461 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  462 */         return null; 
/*  465 */       return new BridgeCrossing(genDepth, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  471 */       generateBox(level, chunkBB, 7, 3, 0, 11, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  472 */       generateBox(level, chunkBB, 0, 3, 7, 18, 4, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  474 */       generateBox(level, chunkBB, 8, 5, 0, 10, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  475 */       generateBox(level, chunkBB, 0, 5, 8, 18, 7, 10, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  477 */       generateBox(level, chunkBB, 7, 5, 0, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  478 */       generateBox(level, chunkBB, 7, 5, 11, 7, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  479 */       generateBox(level, chunkBB, 11, 5, 0, 11, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  480 */       generateBox(level, chunkBB, 11, 5, 11, 11, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  481 */       generateBox(level, chunkBB, 0, 5, 7, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  482 */       generateBox(level, chunkBB, 11, 5, 7, 18, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  483 */       generateBox(level, chunkBB, 0, 5, 11, 7, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  484 */       generateBox(level, chunkBB, 11, 5, 11, 18, 5, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  487 */       generateBox(level, chunkBB, 7, 2, 0, 11, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  488 */       generateBox(level, chunkBB, 7, 2, 13, 11, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  489 */       generateBox(level, chunkBB, 7, 0, 0, 11, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  490 */       generateBox(level, chunkBB, 7, 0, 15, 11, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       int x;
/*  491 */       for (x = 7; x <= 11; x++) {
/*  492 */         for (int z = 0; z <= 2; z++) {
/*  493 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/*  494 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 18 - z, chunkBB);
/*      */         } 
/*      */       } 
/*  498 */       generateBox(level, chunkBB, 0, 2, 7, 5, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  499 */       generateBox(level, chunkBB, 13, 2, 7, 18, 2, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  500 */       generateBox(level, chunkBB, 0, 0, 7, 3, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  501 */       generateBox(level, chunkBB, 15, 0, 7, 18, 1, 11, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  502 */       for (x = 0; x <= 2; x++) {
/*  503 */         for (int z = 7; z <= 11; z++) {
/*  504 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB);
/*  505 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), 18 - x, -1, z, chunkBB);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class RoomCrossing extends NetherBridgePiece {
/*      */     private static final int WIDTH = 7;
/*      */     
/*      */     private static final int HEIGHT = 9;
/*      */     
/*      */     private static final int DEPTH = 7;
/*      */     
/*      */     public RoomCrossing(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  517 */       super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, genDepth, boundingBox);
/*  519 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public RoomCrossing(CompoundTag tag) {
/*  523 */       super(StructurePieceType.NETHER_FORTRESS_ROOM_CROSSING, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  528 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 2, 0, false);
/*  529 */       generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 2, false);
/*  530 */       generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 2, false);
/*      */     }
/*      */     
/*      */     public static RoomCrossing createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  534 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -2, 0, 0, 7, 9, 7, direction);
/*  536 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  537 */         return null; 
/*  540 */       return new RoomCrossing(genDepth, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  546 */       generateBox(level, chunkBB, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  548 */       generateBox(level, chunkBB, 0, 2, 0, 6, 7, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  551 */       generateBox(level, chunkBB, 0, 2, 0, 1, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  552 */       generateBox(level, chunkBB, 0, 2, 6, 1, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  553 */       generateBox(level, chunkBB, 5, 2, 0, 6, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  554 */       generateBox(level, chunkBB, 5, 2, 6, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  555 */       generateBox(level, chunkBB, 0, 2, 0, 0, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  556 */       generateBox(level, chunkBB, 0, 2, 5, 0, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  557 */       generateBox(level, chunkBB, 6, 2, 0, 6, 6, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  558 */       generateBox(level, chunkBB, 6, 2, 5, 6, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  561 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  562 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  564 */       generateBox(level, chunkBB, 2, 6, 0, 4, 6, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  565 */       generateBox(level, chunkBB, 2, 5, 0, 4, 5, 0, weFence, weFence, false);
/*  566 */       generateBox(level, chunkBB, 2, 6, 6, 4, 6, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  567 */       generateBox(level, chunkBB, 2, 5, 6, 4, 5, 6, weFence, weFence, false);
/*  568 */       generateBox(level, chunkBB, 0, 6, 2, 0, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  569 */       generateBox(level, chunkBB, 0, 5, 2, 0, 5, 4, nsFence, nsFence, false);
/*  570 */       generateBox(level, chunkBB, 6, 6, 2, 6, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  571 */       generateBox(level, chunkBB, 6, 5, 2, 6, 5, 4, nsFence, nsFence, false);
/*  574 */       for (int x = 0; x <= 6; x++) {
/*  575 */         for (int z = 0; z <= 6; z++)
/*  576 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class StairsRoom extends NetherBridgePiece {
/*      */     private static final int WIDTH = 7;
/*      */     
/*      */     private static final int HEIGHT = 11;
/*      */     
/*      */     private static final int DEPTH = 7;
/*      */     
/*      */     public StairsRoom(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  588 */       super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, genDepth, boundingBox);
/*  590 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public StairsRoom(CompoundTag tag) {
/*  594 */       super(StructurePieceType.NETHER_FORTRESS_STAIRS_ROOM, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  599 */       generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 6, 2, false);
/*      */     }
/*      */     
/*      */     public static StairsRoom createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, int genDepth, Direction direction) {
/*  603 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -2, 0, 0, 7, 11, 7, direction);
/*  605 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  606 */         return null; 
/*  609 */       return new StairsRoom(genDepth, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  615 */       generateBox(level, chunkBB, 0, 0, 0, 6, 1, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  617 */       generateBox(level, chunkBB, 0, 2, 0, 6, 10, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  620 */       generateBox(level, chunkBB, 0, 2, 0, 1, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  621 */       generateBox(level, chunkBB, 5, 2, 0, 6, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  622 */       generateBox(level, chunkBB, 0, 2, 1, 0, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  623 */       generateBox(level, chunkBB, 6, 2, 1, 6, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  624 */       generateBox(level, chunkBB, 1, 2, 6, 5, 8, 6, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  627 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  628 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  630 */       generateBox(level, chunkBB, 0, 3, 2, 0, 5, 4, nsFence, nsFence, false);
/*  631 */       generateBox(level, chunkBB, 6, 3, 2, 6, 5, 2, nsFence, nsFence, false);
/*  632 */       generateBox(level, chunkBB, 6, 3, 4, 6, 5, 4, nsFence, nsFence, false);
/*  635 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 5, 2, 5, chunkBB);
/*  636 */       generateBox(level, chunkBB, 4, 2, 5, 4, 3, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  637 */       generateBox(level, chunkBB, 3, 2, 5, 3, 4, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  638 */       generateBox(level, chunkBB, 2, 2, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  639 */       generateBox(level, chunkBB, 1, 2, 5, 1, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  642 */       generateBox(level, chunkBB, 1, 7, 1, 5, 7, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  643 */       generateBox(level, chunkBB, 6, 8, 2, 6, 8, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  646 */       generateBox(level, chunkBB, 2, 6, 0, 4, 8, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  647 */       generateBox(level, chunkBB, 2, 5, 0, 4, 5, 0, weFence, weFence, false);
/*  649 */       for (int x = 0; x <= 6; x++) {
/*  650 */         for (int z = 0; z <= 6; z++)
/*  651 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MonsterThrone extends NetherBridgePiece {
/*      */     private static final int WIDTH = 7;
/*      */     
/*      */     private static final int HEIGHT = 8;
/*      */     
/*      */     private static final int DEPTH = 9;
/*      */     
/*      */     private boolean hasPlacedSpawner;
/*      */     
/*      */     public MonsterThrone(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  665 */       super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, genDepth, boundingBox);
/*  667 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public MonsterThrone(CompoundTag tag) {
/*  671 */       super(StructurePieceType.NETHER_FORTRESS_MONSTER_THRONE, tag);
/*  672 */       this.hasPlacedSpawner = tag.getBooleanOr("Mob", false);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*  677 */       super.addAdditionalSaveData(context, tag);
/*  679 */       tag.putBoolean("Mob", this.hasPlacedSpawner);
/*      */     }
/*      */     
/*      */     public static MonsterThrone createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, int genDepth, Direction direction) {
/*  683 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -2, 0, 0, 7, 8, 9, direction);
/*  685 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  686 */         return null; 
/*  689 */       return new MonsterThrone(genDepth, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  695 */       generateBox(level, chunkBB, 0, 2, 0, 6, 7, 7, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  698 */       generateBox(level, chunkBB, 1, 0, 0, 5, 1, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  699 */       generateBox(level, chunkBB, 1, 2, 1, 5, 2, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  700 */       generateBox(level, chunkBB, 1, 3, 2, 5, 3, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  701 */       generateBox(level, chunkBB, 1, 4, 3, 5, 4, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  704 */       generateBox(level, chunkBB, 1, 2, 0, 1, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  705 */       generateBox(level, chunkBB, 5, 2, 0, 5, 4, 2, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  706 */       generateBox(level, chunkBB, 1, 5, 2, 1, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  707 */       generateBox(level, chunkBB, 5, 5, 2, 5, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  708 */       generateBox(level, chunkBB, 0, 5, 3, 0, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  709 */       generateBox(level, chunkBB, 6, 5, 3, 6, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  710 */       generateBox(level, chunkBB, 1, 5, 8, 5, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  712 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  713 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  715 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 1, 6, 3, chunkBB);
/*  716 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 5, 6, 3, chunkBB);
/*  718 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 0, 6, 3, chunkBB);
/*  719 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 6, 6, 3, chunkBB);
/*  721 */       generateBox(level, chunkBB, 0, 6, 4, 0, 6, 7, nsFence, nsFence, false);
/*  722 */       generateBox(level, chunkBB, 6, 6, 4, 6, 6, 7, nsFence, nsFence, false);
/*  724 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 0, 6, 8, chunkBB);
/*  725 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 6, 8, chunkBB);
/*  727 */       generateBox(level, chunkBB, 1, 6, 8, 5, 6, 8, weFence, weFence, false);
/*  729 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 1, 7, 8, chunkBB);
/*  730 */       generateBox(level, chunkBB, 2, 7, 8, 4, 7, 8, weFence, weFence, false);
/*  731 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 5, 7, 8, chunkBB);
/*  733 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 2, 8, 8, chunkBB);
/*  734 */       placeBlock(level, weFence, 3, 8, 8, chunkBB);
/*  735 */       placeBlock(level, (BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 4, 8, 8, chunkBB);
/*  737 */       if (!this.hasPlacedSpawner) {
/*  738 */         BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(3, 5, 5);
/*  739 */         if (chunkBB.isInside((Vec3i)mutableBlockPos)) {
/*  740 */           this.hasPlacedSpawner = true;
/*  741 */           level.setBlock((BlockPos)mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*  743 */           BlockEntity blockEntity = level.getBlockEntity((BlockPos)mutableBlockPos);
/*  744 */           if (blockEntity instanceof SpawnerBlockEntity) {
/*  744 */             SpawnerBlockEntity spawner = (SpawnerBlockEntity)blockEntity;
/*  745 */             spawner.setEntityId(EntityTypes.BLAZE, random);
/*      */           } 
/*      */         } 
/*      */       } 
/*  751 */       for (int x = 0; x <= 6; x++) {
/*  752 */         for (int z = 0; z <= 6; z++)
/*  753 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleEntrance extends NetherBridgePiece {
/*      */     private static final int WIDTH = 13;
/*      */     
/*      */     private static final int HEIGHT = 14;
/*      */     
/*      */     private static final int DEPTH = 13;
/*      */     
/*      */     public CastleEntrance(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/*  765 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, genDepth, boundingBox);
/*  767 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public CastleEntrance(CompoundTag tag) {
/*  771 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_ENTRANCE, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  776 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 3, true);
/*      */     }
/*      */     
/*      */     public static CastleEntrance createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  780 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -5, -3, 0, 13, 14, 13, direction);
/*  782 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  783 */         return null; 
/*  786 */       return new CastleEntrance(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  792 */       generateBox(level, chunkBB, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  794 */       generateBox(level, chunkBB, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  797 */       generateBox(level, chunkBB, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  798 */       generateBox(level, chunkBB, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  799 */       generateBox(level, chunkBB, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  800 */       generateBox(level, chunkBB, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  801 */       generateBox(level, chunkBB, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  802 */       generateBox(level, chunkBB, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  803 */       generateBox(level, chunkBB, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  804 */       generateBox(level, chunkBB, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  807 */       generateBox(level, chunkBB, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  810 */       generateBox(level, chunkBB, 5, 8, 0, 7, 8, 0, Blocks.NETHER_BRICK_FENCE.defaultBlockState(), Blocks.NETHER_BRICK_FENCE.defaultBlockState(), false);
/*  812 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  813 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  816 */       for (int i = 1; i <= 11; i += 2) {
/*  817 */         generateBox(level, chunkBB, i, 10, 0, i, 11, 0, weFence, weFence, false);
/*  818 */         generateBox(level, chunkBB, i, 10, 12, i, 11, 12, weFence, weFence, false);
/*  819 */         generateBox(level, chunkBB, 0, 10, i, 0, 11, i, nsFence, nsFence, false);
/*  820 */         generateBox(level, chunkBB, 12, 10, i, 12, 11, i, nsFence, nsFence, false);
/*  821 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 0, chunkBB);
/*  822 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 12, chunkBB);
/*  823 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, i, chunkBB);
/*  824 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, i, chunkBB);
/*  825 */         if (i != 11) {
/*  826 */           placeBlock(level, weFence, i + 1, 13, 0, chunkBB);
/*  827 */           placeBlock(level, weFence, i + 1, 13, 12, chunkBB);
/*  828 */           placeBlock(level, nsFence, 0, 13, i + 1, chunkBB);
/*  829 */           placeBlock(level, nsFence, 12, 13, i + 1, chunkBB);
/*      */         } 
/*      */       } 
/*  832 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, chunkBB);
/*  833 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, chunkBB);
/*  834 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, chunkBB);
/*  835 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, chunkBB);
/*  838 */       for (int z = 3; z <= 9; z += 2) {
/*  839 */         generateBox(level, chunkBB, 1, 7, z, 1, 8, z, (BlockState)nsFence.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), (BlockState)nsFence.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), false);
/*  840 */         generateBox(level, chunkBB, 11, 7, z, 11, 8, z, (BlockState)nsFence.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), (BlockState)nsFence.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), false);
/*      */       } 
/*  844 */       generateBox(level, chunkBB, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  845 */       generateBox(level, chunkBB, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  847 */       generateBox(level, chunkBB, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  848 */       generateBox(level, chunkBB, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  849 */       generateBox(level, chunkBB, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  850 */       generateBox(level, chunkBB, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       int x;
/*  852 */       for (x = 4; x <= 8; x++) {
/*  853 */         for (int j = 0; j <= 2; j++) {
/*  854 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, j, chunkBB);
/*  855 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, 12 - j, chunkBB);
/*      */         } 
/*      */       } 
/*  858 */       for (x = 0; x <= 2; x++) {
/*  859 */         for (int j = 4; j <= 8; j++) {
/*  860 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, j, chunkBB);
/*  861 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - x, -1, j, chunkBB);
/*      */         } 
/*      */       } 
/*  866 */       generateBox(level, chunkBB, 5, 5, 5, 7, 5, 7, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  867 */       generateBox(level, chunkBB, 6, 1, 6, 6, 4, 6, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  868 */       placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 6, 0, 6, chunkBB);
/*  869 */       placeBlock(level, Blocks.LAVA.defaultBlockState(), 6, 5, 6, chunkBB);
/*  871 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(6, 5, 6);
/*  872 */       if (chunkBB.isInside((Vec3i)mutableBlockPos))
/*  873 */         level.scheduleTick((BlockPos)mutableBlockPos, (Fluid)Fluids.LAVA, 0); 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleStalkRoom extends NetherBridgePiece {
/*      */     private static final int WIDTH = 13;
/*      */     
/*      */     private static final int HEIGHT = 14;
/*      */     
/*      */     private static final int DEPTH = 13;
/*      */     
/*      */     public CastleStalkRoom(int genDepth, BoundingBox boundingBox, Direction direction) {
/*  884 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, genDepth, boundingBox);
/*  886 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public CastleStalkRoom(CompoundTag tag) {
/*  890 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/*  895 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 3, true);
/*  896 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 5, 11, true);
/*      */     }
/*      */     
/*      */     public static CastleStalkRoom createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/*  900 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -5, -3, 0, 13, 14, 13, direction);
/*  902 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/*  903 */         return null; 
/*  906 */       return new CastleStalkRoom(genDepth, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/*  912 */       generateBox(level, chunkBB, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  914 */       generateBox(level, chunkBB, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  917 */       generateBox(level, chunkBB, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  918 */       generateBox(level, chunkBB, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  919 */       generateBox(level, chunkBB, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  920 */       generateBox(level, chunkBB, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  921 */       generateBox(level, chunkBB, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  922 */       generateBox(level, chunkBB, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  923 */       generateBox(level, chunkBB, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  924 */       generateBox(level, chunkBB, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  927 */       generateBox(level, chunkBB, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  929 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  930 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  931 */       BlockState nswFence = (BlockState)nsFence.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/*  932 */       BlockState nseFence = (BlockState)nsFence.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  935 */       for (int i = 1; i <= 11; i += 2) {
/*  936 */         generateBox(level, chunkBB, i, 10, 0, i, 11, 0, weFence, weFence, false);
/*  937 */         generateBox(level, chunkBB, i, 10, 12, i, 11, 12, weFence, weFence, false);
/*  938 */         generateBox(level, chunkBB, 0, 10, i, 0, 11, i, nsFence, nsFence, false);
/*  939 */         generateBox(level, chunkBB, 12, 10, i, 12, 11, i, nsFence, nsFence, false);
/*  940 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 0, chunkBB);
/*  941 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), i, 13, 12, chunkBB);
/*  942 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, i, chunkBB);
/*  943 */         placeBlock(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, i, chunkBB);
/*  944 */         if (i != 11) {
/*  945 */           placeBlock(level, weFence, i + 1, 13, 0, chunkBB);
/*  946 */           placeBlock(level, weFence, i + 1, 13, 12, chunkBB);
/*  947 */           placeBlock(level, nsFence, 0, 13, i + 1, chunkBB);
/*  948 */           placeBlock(level, nsFence, 12, 13, i + 1, chunkBB);
/*      */         } 
/*      */       } 
/*  951 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, chunkBB);
/*  952 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, chunkBB);
/*  953 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, chunkBB);
/*  954 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, chunkBB);
/*  957 */       for (int z = 3; z <= 9; z += 2) {
/*  958 */         generateBox(level, chunkBB, 1, 7, z, 1, 8, z, nswFence, nswFence, false);
/*  959 */         generateBox(level, chunkBB, 11, 7, z, 11, 8, z, nseFence, nseFence, false);
/*      */       } 
/*  963 */       BlockState stairs = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/*  964 */       for (int j = 0; j <= 6; j++) {
/*  965 */         int m = j + 4;
/*  966 */         for (int n = 5; n <= 7; n++)
/*  967 */           placeBlock(level, stairs, n, 5 + j, m, chunkBB); 
/*  969 */         if (m >= 5 && m <= 8) {
/*  970 */           generateBox(level, chunkBB, 5, 5, m, 7, j + 4, m, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  971 */         } else if (m >= 9 && m <= 10) {
/*  972 */           generateBox(level, chunkBB, 5, 8, m, 7, j + 4, m, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */         } 
/*  974 */         if (j >= 1)
/*  975 */           generateBox(level, chunkBB, 5, 6 + j, m, 7, 9 + j, m, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false); 
/*      */       } 
/*  978 */       for (int x = 5; x <= 7; x++)
/*  979 */         placeBlock(level, stairs, x, 12, 11, chunkBB); 
/*  981 */       generateBox(level, chunkBB, 5, 6, 7, 5, 7, 7, nseFence, nseFence, false);
/*  982 */       generateBox(level, chunkBB, 7, 6, 7, 7, 7, 7, nswFence, nswFence, false);
/*  983 */       generateBox(level, chunkBB, 5, 13, 12, 7, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  986 */       generateBox(level, chunkBB, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  987 */       generateBox(level, chunkBB, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  988 */       generateBox(level, chunkBB, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  989 */       generateBox(level, chunkBB, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  990 */       generateBox(level, chunkBB, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  991 */       generateBox(level, chunkBB, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  992 */       BlockState eastStairs = (BlockState)stairs.setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/*  993 */       BlockState westStairs = (BlockState)stairs.setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/*  994 */       placeBlock(level, westStairs, 4, 5, 2, chunkBB);
/*  995 */       placeBlock(level, westStairs, 4, 5, 3, chunkBB);
/*  996 */       placeBlock(level, westStairs, 4, 5, 9, chunkBB);
/*  997 */       placeBlock(level, westStairs, 4, 5, 10, chunkBB);
/*  998 */       placeBlock(level, eastStairs, 8, 5, 2, chunkBB);
/*  999 */       placeBlock(level, eastStairs, 8, 5, 3, chunkBB);
/* 1000 */       placeBlock(level, eastStairs, 8, 5, 9, chunkBB);
/* 1001 */       placeBlock(level, eastStairs, 8, 5, 10, chunkBB);
/* 1004 */       generateBox(level, chunkBB, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/* 1005 */       generateBox(level, chunkBB, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/* 1006 */       generateBox(level, chunkBB, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/* 1007 */       generateBox(level, chunkBB, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/* 1010 */       generateBox(level, chunkBB, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1011 */       generateBox(level, chunkBB, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1013 */       generateBox(level, chunkBB, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1014 */       generateBox(level, chunkBB, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1015 */       generateBox(level, chunkBB, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1016 */       generateBox(level, chunkBB, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       int k;
/* 1018 */       for (k = 4; k <= 8; k++) {
/* 1019 */         for (int m = 0; m <= 2; m++) {
/* 1020 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), k, -1, m, chunkBB);
/* 1021 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), k, -1, 12 - m, chunkBB);
/*      */         } 
/*      */       } 
/* 1024 */       for (k = 0; k <= 2; k++) {
/* 1025 */         for (int m = 4; m <= 8; m++) {
/* 1026 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), k, -1, m, chunkBB);
/* 1027 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - k, -1, m, chunkBB);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorPiece extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 7;
/*      */     
/*      */     private static final int DEPTH = 5;
/*      */     
/*      */     public CastleSmallCorridorPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1039 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, genDepth, boundingBox);
/* 1041 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorPiece(CompoundTag tag) {
/* 1045 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1050 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 0, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1054 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/* 1056 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1057 */         return null; 
/* 1060 */       return new CastleSmallCorridorPiece(genDepth, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1066 */       generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1068 */       generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1070 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1073 */       generateBox(level, chunkBB, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1074 */       generateBox(level, chunkBB, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1075 */       generateBox(level, chunkBB, 0, 3, 1, 0, 4, 1, nsFence, nsFence, false);
/* 1076 */       generateBox(level, chunkBB, 0, 3, 3, 0, 4, 3, nsFence, nsFence, false);
/* 1077 */       generateBox(level, chunkBB, 4, 3, 1, 4, 4, 1, nsFence, nsFence, false);
/* 1078 */       generateBox(level, chunkBB, 4, 3, 3, 4, 4, 3, nsFence, nsFence, false);
/* 1081 */       generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1084 */       for (int x = 0; x <= 4; x++) {
/* 1085 */         for (int z = 0; z <= 4; z++)
/* 1086 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorCrossingPiece extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 7;
/*      */     
/*      */     private static final int DEPTH = 5;
/*      */     
/*      */     public CastleSmallCorridorCrossingPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1098 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, genDepth, boundingBox);
/* 1100 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorCrossingPiece(CompoundTag tag) {
/* 1104 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_CROSSING, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1109 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 0, true);
/* 1110 */       generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true);
/* 1111 */       generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorCrossingPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1115 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/* 1117 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1118 */         return null; 
/* 1121 */       return new CastleSmallCorridorCrossingPiece(genDepth, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1127 */       generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1129 */       generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1132 */       generateBox(level, chunkBB, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1133 */       generateBox(level, chunkBB, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1134 */       generateBox(level, chunkBB, 0, 2, 4, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1135 */       generateBox(level, chunkBB, 4, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1138 */       generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1141 */       for (int x = 0; x <= 4; x++) {
/* 1142 */         for (int z = 0; z <= 4; z++)
/* 1143 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorRightTurnPiece extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 7;
/*      */     
/*      */     private static final int DEPTH = 5;
/*      */     
/*      */     private boolean isNeedingChest;
/*      */     
/*      */     public CastleSmallCorridorRightTurnPiece(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1157 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, genDepth, boundingBox);
/* 1159 */       setOrientation(direction);
/* 1161 */       this.isNeedingChest = (random.nextInt(3) == 0);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorRightTurnPiece(CompoundTag tag) {
/* 1165 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_RIGHT_TURN, tag);
/* 1166 */       this.isNeedingChest = tag.getBooleanOr("Chest", false);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1171 */       super.addAdditionalSaveData(context, tag);
/* 1173 */       tag.putBoolean("Chest", this.isNeedingChest);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1178 */       generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorRightTurnPiece createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1182 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/* 1184 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1185 */         return null; 
/* 1188 */       return new CastleSmallCorridorRightTurnPiece(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1194 */       generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1196 */       generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1198 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1199 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1202 */       generateBox(level, chunkBB, 0, 2, 0, 0, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1203 */       generateBox(level, chunkBB, 0, 3, 1, 0, 4, 1, nsFence, nsFence, false);
/* 1204 */       generateBox(level, chunkBB, 0, 3, 3, 0, 4, 3, nsFence, nsFence, false);
/* 1206 */       generateBox(level, chunkBB, 4, 2, 0, 4, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1208 */       generateBox(level, chunkBB, 1, 2, 4, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1209 */       generateBox(level, chunkBB, 1, 3, 4, 1, 4, 4, weFence, weFence, false);
/* 1210 */       generateBox(level, chunkBB, 3, 3, 4, 3, 4, 4, weFence, weFence, false);
/* 1212 */       if (this.isNeedingChest && 
/* 1213 */         chunkBB.isInside((Vec3i)getWorldPos(1, 2, 3))) {
/* 1214 */         this.isNeedingChest = false;
/* 1215 */         createChest(level, chunkBB, random, 1, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
/*      */       } 
/* 1220 */       generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1223 */       for (int x = 0; x <= 4; x++) {
/* 1224 */         for (int z = 0; z <= 4; z++)
/* 1225 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleSmallCorridorLeftTurnPiece extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 7;
/*      */     
/*      */     private static final int DEPTH = 5;
/*      */     
/*      */     private boolean isNeedingChest;
/*      */     
/*      */     public CastleSmallCorridorLeftTurnPiece(int genDepth, RandomSource random, BoundingBox boundingBox, Direction direction) {
/* 1239 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, genDepth, boundingBox);
/* 1241 */       setOrientation(direction);
/* 1243 */       this.isNeedingChest = (random.nextInt(3) == 0);
/*      */     }
/*      */     
/*      */     public CastleSmallCorridorLeftTurnPiece(CompoundTag tag) {
/* 1247 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_SMALL_CORRIDOR_LEFT_TURN, tag);
/* 1248 */       this.isNeedingChest = tag.getBooleanOr("Chest", false);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/* 1253 */       super.addAdditionalSaveData(context, tag);
/* 1255 */       tag.putBoolean("Chest", this.isNeedingChest);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1260 */       generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, 1, true);
/*      */     }
/*      */     
/*      */     public static CastleSmallCorridorLeftTurnPiece createPiece(StructurePieceAccessor structurePieceAccessor, RandomSource random, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1264 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, 0, 0, 5, 7, 5, direction);
/* 1266 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1267 */         return null; 
/* 1270 */       return new CastleSmallCorridorLeftTurnPiece(genDepth, random, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1276 */       generateBox(level, chunkBB, 0, 0, 0, 4, 1, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1278 */       generateBox(level, chunkBB, 0, 2, 0, 4, 5, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1280 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1281 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1284 */       generateBox(level, chunkBB, 4, 2, 0, 4, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1285 */       generateBox(level, chunkBB, 4, 3, 1, 4, 4, 1, nsFence, nsFence, false);
/* 1286 */       generateBox(level, chunkBB, 4, 3, 3, 4, 4, 3, nsFence, nsFence, false);
/* 1288 */       generateBox(level, chunkBB, 0, 2, 0, 0, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1290 */       generateBox(level, chunkBB, 0, 2, 4, 3, 5, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1291 */       generateBox(level, chunkBB, 1, 3, 4, 1, 4, 4, weFence, weFence, false);
/* 1292 */       generateBox(level, chunkBB, 3, 3, 4, 3, 4, 4, weFence, weFence, false);
/* 1294 */       if (this.isNeedingChest && 
/* 1295 */         chunkBB.isInside((Vec3i)getWorldPos(3, 2, 3))) {
/* 1296 */         this.isNeedingChest = false;
/* 1297 */         createChest(level, chunkBB, random, 3, 2, 3, BuiltInLootTables.NETHER_BRIDGE);
/*      */       } 
/* 1302 */       generateBox(level, chunkBB, 0, 6, 0, 4, 6, 4, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1305 */       for (int x = 0; x <= 4; x++) {
/* 1306 */         for (int z = 0; z <= 4; z++)
/* 1307 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleCorridorStairsPiece extends NetherBridgePiece {
/*      */     private static final int WIDTH = 5;
/*      */     
/*      */     private static final int HEIGHT = 14;
/*      */     
/*      */     private static final int DEPTH = 10;
/*      */     
/*      */     public CastleCorridorStairsPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1319 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, genDepth, boundingBox);
/* 1321 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public CastleCorridorStairsPiece(CompoundTag tag) {
/* 1325 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_STAIRS, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1330 */       generateChildForward((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 1, 0, true);
/*      */     }
/*      */     
/*      */     public static CastleCorridorStairsPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1334 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -1, -7, 0, 5, 14, 10, direction);
/* 1336 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1337 */         return null; 
/* 1340 */       return new CastleCorridorStairsPiece(genDepth, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1346 */       BlockState stairs = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/* 1347 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1349 */       for (int step = 0; step <= 9; step++) {
/* 1350 */         int floor = Math.max(1, 7 - step);
/* 1351 */         int roof = Math.min(Math.max(floor + 5, 14 - step), 13);
/* 1352 */         int z = step;
/* 1355 */         generateBox(level, chunkBB, 0, 0, z, 4, floor, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1357 */         generateBox(level, chunkBB, 1, floor + 1, z, 3, roof - 1, z, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1358 */         if (step <= 6) {
/* 1359 */           placeBlock(level, stairs, 1, floor + 1, z, chunkBB);
/* 1360 */           placeBlock(level, stairs, 2, floor + 1, z, chunkBB);
/* 1361 */           placeBlock(level, stairs, 3, floor + 1, z, chunkBB);
/*      */         } 
/* 1364 */         generateBox(level, chunkBB, 0, roof, z, 4, roof, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1366 */         generateBox(level, chunkBB, 0, floor + 1, z, 0, roof - 1, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1367 */         generateBox(level, chunkBB, 4, floor + 1, z, 4, roof - 1, z, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1368 */         if ((step & 0x1) == 0) {
/* 1369 */           generateBox(level, chunkBB, 0, floor + 2, z, 0, floor + 3, z, nsFence, nsFence, false);
/* 1370 */           generateBox(level, chunkBB, 4, floor + 2, z, 4, floor + 3, z, nsFence, nsFence, false);
/*      */         } 
/* 1374 */         for (int x = 0; x <= 4; x++)
/* 1375 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static class CastleCorridorTBalconyPiece extends NetherBridgePiece {
/*      */     private static final int WIDTH = 9;
/*      */     
/*      */     private static final int HEIGHT = 7;
/*      */     
/*      */     private static final int DEPTH = 9;
/*      */     
/*      */     public CastleCorridorTBalconyPiece(int genDepth, BoundingBox boundingBox, Direction direction) {
/* 1387 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, genDepth, boundingBox);
/* 1389 */       setOrientation(direction);
/*      */     }
/*      */     
/*      */     public CastleCorridorTBalconyPiece(CompoundTag tag) {
/* 1393 */       super(StructurePieceType.NETHER_FORTRESS_CASTLE_CORRIDOR_T_BALCONY, tag);
/*      */     }
/*      */     
/*      */     public void addChildren(StructurePiece startPiece, StructurePieceAccessor structurePieceAccessor, RandomSource random) {
/* 1398 */       int zOff = 1;
/* 1400 */       Direction orientation = getOrientation();
/* 1401 */       if (orientation == Direction.WEST || orientation == Direction.NORTH)
/* 1402 */         zOff = 5; 
/* 1405 */       generateChildLeft((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, zOff, (random.nextInt(8) > 0));
/* 1406 */       generateChildRight((NetherFortressPieces.StartPiece)startPiece, structurePieceAccessor, random, 0, zOff, (random.nextInt(8) > 0));
/*      */     }
/*      */     
/*      */     public static CastleCorridorTBalconyPiece createPiece(StructurePieceAccessor structurePieceAccessor, int footX, int footY, int footZ, Direction direction, int genDepth) {
/* 1410 */       BoundingBox box = BoundingBox.orientBox(footX, footY, footZ, -3, 0, 0, 9, 7, 9, direction);
/* 1412 */       if (!isOkBox(box) || structurePieceAccessor.findCollisionPiece(box) != null)
/* 1413 */         return null; 
/* 1416 */       return new CastleCorridorTBalconyPiece(genDepth, box, direction);
/*      */     }
/*      */     
/*      */     public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox chunkBB, ChunkPos chunkPos, BlockPos referencePos) {
/* 1421 */       BlockState nsFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 1422 */       BlockState weFence = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1425 */       generateBox(level, chunkBB, 0, 0, 0, 8, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1427 */       generateBox(level, chunkBB, 0, 2, 0, 8, 5, 8, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1429 */       generateBox(level, chunkBB, 0, 6, 0, 8, 6, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1432 */       generateBox(level, chunkBB, 0, 2, 0, 2, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1433 */       generateBox(level, chunkBB, 6, 2, 0, 8, 5, 0, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1434 */       generateBox(level, chunkBB, 1, 3, 0, 1, 4, 0, weFence, weFence, false);
/* 1435 */       generateBox(level, chunkBB, 7, 3, 0, 7, 4, 0, weFence, weFence, false);
/* 1438 */       generateBox(level, chunkBB, 0, 2, 4, 8, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1439 */       generateBox(level, chunkBB, 1, 1, 4, 2, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1440 */       generateBox(level, chunkBB, 6, 1, 4, 7, 2, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 1443 */       generateBox(level, chunkBB, 1, 3, 8, 7, 3, 8, weFence, weFence, false);
/* 1444 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 0, 3, 8, chunkBB);
/* 1445 */       placeBlock(level, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 8, 3, 8, chunkBB);
/* 1446 */       generateBox(level, chunkBB, 0, 3, 6, 0, 3, 7, nsFence, nsFence, false);
/* 1447 */       generateBox(level, chunkBB, 8, 3, 6, 8, 3, 7, nsFence, nsFence, false);
/* 1450 */       generateBox(level, chunkBB, 0, 3, 4, 0, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1451 */       generateBox(level, chunkBB, 8, 3, 4, 8, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1452 */       generateBox(level, chunkBB, 1, 3, 5, 2, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1453 */       generateBox(level, chunkBB, 6, 3, 5, 7, 5, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1454 */       generateBox(level, chunkBB, 1, 4, 5, 1, 5, 5, weFence, weFence, false);
/* 1455 */       generateBox(level, chunkBB, 7, 4, 5, 7, 5, 5, weFence, weFence, false);
/* 1458 */       for (int z = 0; z <= 5; z++) {
/* 1459 */         for (int x = 0; x <= 8; x++)
/* 1460 */           fillColumnDown(level, Blocks.NETHER_BRICKS.defaultBlockState(), x, -1, z, chunkBB); 
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */