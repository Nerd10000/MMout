/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.ChestBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ public class WoodlandMansionPieces {
/*      */   public static class WoodlandMansionPiece extends TemplateStructurePiece {
/*      */     public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation) {
/*   37 */       this(structureTemplateManager, templateName, position, rotation, Mirror.NONE);
/*      */     }
/*      */     
/*      */     public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, String templateName, BlockPos position, Rotation rotation, Mirror mirror) {
/*   41 */       super(StructurePieceType.WOODLAND_MANSION_PIECE, 0, structureTemplateManager, makeLocation(templateName), templateName, makeSettings(mirror, rotation), position);
/*      */     }
/*      */     
/*      */     public WoodlandMansionPiece(StructureTemplateManager structureTemplateManager, CompoundTag tag) {
/*   45 */       super(StructurePieceType.WOODLAND_MANSION_PIECE, tag, structureTemplateManager, location -> makeSettings(tag.read("Mi", Mirror.LEGACY_CODEC).orElseThrow(), tag.read("Rot", Rotation.LEGACY_CODEC).orElseThrow()));
/*      */     }
/*      */     
/*      */     protected Identifier makeTemplateLocation() {
/*   50 */       return makeLocation(this.templateName);
/*      */     }
/*      */     
/*      */     private static Identifier makeLocation(String templateName) {
/*   54 */       return Identifier.withDefaultNamespace("woodland_mansion/" + templateName);
/*      */     }
/*      */     
/*      */     private static StructurePlaceSettings makeSettings(Mirror mirror, Rotation rotation) {
/*   58 */       return (new StructurePlaceSettings()).setIgnoreEntities(true).setRotation(rotation).setMirror(mirror).addProcessor((StructureProcessor)BlockIgnoreProcessor.STRUCTURE_BLOCK);
/*      */     }
/*      */     
/*      */     protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
/*   63 */       super.addAdditionalSaveData(context, tag);
/*   65 */       tag.store("Rot", Rotation.LEGACY_CODEC, this.placeSettings.getRotation());
/*   66 */       tag.store("Mi", Mirror.LEGACY_CODEC, this.placeSettings.getMirror());
/*      */     }
/*      */     
/*      */     protected void handleDataMarker(String markerId, BlockPos position, ServerLevelAccessor level, RandomSource random, BoundingBox chunkBB) {
/*   71 */       if (markerId.startsWith("Chest")) {
/*   72 */         Rotation rot = this.placeSettings.getRotation();
/*   73 */         BlockState chestState = Blocks.CHEST.defaultBlockState();
/*   74 */         if ("ChestWest".equals(markerId)) {
/*   75 */           chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.WEST));
/*   76 */         } else if ("ChestEast".equals(markerId)) {
/*   77 */           chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.EAST));
/*   78 */         } else if ("ChestSouth".equals(markerId)) {
/*   79 */           chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.SOUTH));
/*   80 */         } else if ("ChestNorth".equals(markerId)) {
/*   81 */           chestState = (BlockState)chestState.setValue((Property)ChestBlock.FACING, (Comparable)rot.rotate(Direction.NORTH));
/*      */         } 
/*   83 */         createChest(level, chunkBB, random, position, BuiltInLootTables.WOODLAND_MANSION, chestState);
/*      */       } else {
/*      */         int numberOfAllays, i;
/*   85 */         List<Mob> mobs = new ArrayList<>();
/*   86 */         switch (markerId) {
/*      */           case "Mage":
/*   87 */             mobs.add((Mob)EntityTypes.EVOKER.create((Level)level.getLevel(), EntitySpawnReason.STRUCTURE));
/*      */             break;
/*      */           case "Warrior":
/*   88 */             mobs.add((Mob)EntityTypes.VINDICATOR.create((Level)level.getLevel(), EntitySpawnReason.STRUCTURE));
/*      */             break;
/*      */           case "Group of Allays":
/*   90 */             numberOfAllays = level.getRandom().nextInt(3) + 1;
/*   91 */             for (i = 0; i < numberOfAllays; i++)
/*   92 */               mobs.add((Mob)EntityTypes.ALLAY.create((Level)level.getLevel(), EntitySpawnReason.STRUCTURE)); 
/*      */             break;
/*      */           default:
/*      */             return;
/*      */         } 
/*  100 */         for (Mob mob : mobs) {
/*  101 */           if (mob == null)
/*      */             continue; 
/*  104 */           mob.setPersistenceRequired();
/*  105 */           mob.snapTo(position, 0.0F, 0.0F);
/*  106 */           mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.STRUCTURE, null);
/*  107 */           level.addFreshEntityWithPassengers((Entity)mob);
/*  108 */           level.setBlock(position, Blocks.AIR.defaultBlockState(), 2);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateMansion(StructureTemplateManager structureTemplateManager, BlockPos origin, Rotation rotation, List<WoodlandMansionPiece> pieces, RandomSource random) {
/*  115 */     MansionGrid grid = new MansionGrid(random);
/*  116 */     MansionPiecePlacer placer = new MansionPiecePlacer(structureTemplateManager, random);
/*  117 */     placer.createMansion(origin, rotation, pieces, grid);
/*      */   }
/*      */   
/*      */   private static class PlacementData {
/*      */     public Rotation rotation;
/*      */     
/*      */     public BlockPos position;
/*      */     
/*      */     public String wallType;
/*      */   }
/*      */   
/*      */   private static class MansionPiecePlacer {
/*      */     private final StructureTemplateManager structureTemplateManager;
/*      */     
/*      */     private final RandomSource random;
/*      */     
/*      */     private int startX;
/*      */     
/*      */     private int startY;
/*      */     
/*      */     public MansionPiecePlacer(StructureTemplateManager structureTemplateManager, RandomSource random) {
/*  134 */       this.structureTemplateManager = structureTemplateManager;
/*  135 */       this.random = random;
/*      */     }
/*      */     
/*      */     public void createMansion(BlockPos origin, Rotation rotation, List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.MansionGrid mansion) {
/*  139 */       WoodlandMansionPieces.PlacementData data = new WoodlandMansionPieces.PlacementData();
/*  140 */       data.position = origin;
/*  141 */       data.rotation = rotation;
/*  142 */       data.wallType = "wall_flat";
/*  144 */       WoodlandMansionPieces.PlacementData secondData = new WoodlandMansionPieces.PlacementData();
/*  147 */       entrance(pieces, data);
/*  148 */       secondData.position = data.position.above(8);
/*  149 */       secondData.rotation = data.rotation;
/*  150 */       secondData.wallType = "wall_window";
/*  152 */       if (!pieces.isEmpty());
/*  156 */       WoodlandMansionPieces.SimpleGrid baseGrid = mansion.baseGrid;
/*  157 */       WoodlandMansionPieces.SimpleGrid thirdGrid = mansion.thirdFloorGrid;
/*  159 */       this.startX = mansion.entranceX + 1;
/*  160 */       this.startY = mansion.entranceY + 1;
/*  161 */       int endX = mansion.entranceX + 1;
/*  162 */       int endY = mansion.entranceY;
/*  164 */       traverseOuterWalls(pieces, data, baseGrid, Direction.SOUTH, this.startX, this.startY, endX, endY);
/*  165 */       traverseOuterWalls(pieces, secondData, baseGrid, Direction.SOUTH, this.startX, this.startY, endX, endY);
/*  168 */       WoodlandMansionPieces.PlacementData thirdData = new WoodlandMansionPieces.PlacementData();
/*  169 */       thirdData.position = data.position.above(19);
/*  170 */       thirdData.rotation = data.rotation;
/*  171 */       thirdData.wallType = "wall_window";
/*  173 */       boolean done = false;
/*  174 */       for (int y = 0; y < thirdGrid.height && !done; y++) {
/*  175 */         for (int x = thirdGrid.width - 1; x >= 0 && !done; x--) {
/*  176 */           if (WoodlandMansionPieces.MansionGrid.isHouse(thirdGrid, x, y)) {
/*  177 */             thirdData.position = thirdData.position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/*  178 */             thirdData.position = thirdData.position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*  179 */             traverseWallPiece(pieces, thirdData);
/*  180 */             traverseOuterWalls(pieces, thirdData, thirdGrid, Direction.SOUTH, x, y, x, y);
/*  181 */             done = true;
/*      */           } 
/*      */         } 
/*      */       } 
/*  187 */       createRoof(pieces, origin.above(16), rotation, baseGrid, thirdGrid);
/*  188 */       createRoof(pieces, origin.above(27), rotation, thirdGrid, null);
/*  190 */       if (!pieces.isEmpty());
/*  194 */       WoodlandMansionPieces.FloorRoomCollection[] roomCollections = new WoodlandMansionPieces.FloorRoomCollection[3];
/*  195 */       roomCollections[0] = new WoodlandMansionPieces.FirstFloorRoomCollection();
/*  196 */       roomCollections[1] = new WoodlandMansionPieces.SecondFloorRoomCollection();
/*  197 */       roomCollections[2] = new WoodlandMansionPieces.ThirdFloorRoomCollection();
/*  199 */       for (int floorNum = 0; floorNum < 3; floorNum++) {
/*  200 */         BlockPos floorOrigin = origin.above(8 * floorNum + ((floorNum == 2) ? 3 : 0));
/*  201 */         WoodlandMansionPieces.SimpleGrid rooms = mansion.floorRooms[floorNum];
/*  202 */         WoodlandMansionPieces.SimpleGrid grid = (floorNum == 2) ? thirdGrid : baseGrid;
/*  205 */         String southPiece = (floorNum == 0) ? "carpet_south_1" : "carpet_south_2";
/*  206 */         String westPiece = (floorNum == 0) ? "carpet_west_1" : "carpet_west_2";
/*  207 */         for (int i = 0; i < grid.height; i++) {
/*  208 */           for (int x = 0; x < grid.width; x++) {
/*  209 */             if (grid.get(x, i) == 1) {
/*  210 */               BlockPos pos = floorOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (i - this.startY) * 8);
/*  211 */               pos = pos.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*  212 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "corridor_floor", pos, rotation));
/*  214 */               if (grid.get(x, i - 1) == 1 || (rooms.get(x, i - 1) & 0x800000) == 8388608)
/*  215 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_north", pos.relative(rotation.rotate(Direction.EAST), 1).above(), rotation)); 
/*  217 */               if (grid.get(x + 1, i) == 1 || (rooms.get(x + 1, i) & 0x800000) == 8388608)
/*  218 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_east", pos.relative(rotation.rotate(Direction.SOUTH), 1).relative(rotation.rotate(Direction.EAST), 5).above(), rotation)); 
/*  220 */               if (grid.get(x, i + 1) == 1 || (rooms.get(x, i + 1) & 0x800000) == 8388608)
/*  221 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, southPiece, pos.relative(rotation.rotate(Direction.SOUTH), 5).relative(rotation.rotate(Direction.WEST), 1), rotation)); 
/*  223 */               if (grid.get(x - 1, i) == 1 || (rooms.get(x - 1, i) & 0x800000) == 8388608)
/*  224 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, westPiece, pos.relative(rotation.rotate(Direction.WEST), 1).relative(rotation.rotate(Direction.NORTH), 1), rotation)); 
/*      */             } 
/*      */           } 
/*      */         } 
/*  230 */         String wallPiece = (floorNum == 0) ? "indoors_wall_1" : "indoors_wall_2";
/*  231 */         String doorPiece = (floorNum == 0) ? "indoors_door_1" : "indoors_door_2";
/*  232 */         List<Direction> doorDirs = new ArrayList<>();
/*  233 */         for (int j = 0; j < grid.height; j++) {
/*  234 */           for (int x = 0; x < grid.width; x++) {
/*  235 */             boolean thirdFloorStartRoom = (floorNum == 2 && grid.get(x, j) == 3);
/*  236 */             if (grid.get(x, j) == 2 || thirdFloorStartRoom) {
/*  237 */               int roomData = rooms.get(x, j);
/*  238 */               int roomType = roomData & 0xF0000;
/*  239 */               int roomId = roomData & 0xFFFF;
/*  242 */               thirdFloorStartRoom = (thirdFloorStartRoom && (roomData & 0x800000) == 8388608);
/*  244 */               doorDirs.clear();
/*  245 */               if ((roomData & 0x200000) == 2097152)
/*  246 */                 for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  247 */                   if (grid.get(x + direction.getStepX(), j + direction.getStepZ()) == 1)
/*  248 */                     doorDirs.add(direction); 
/*      */                 }  
/*  252 */               Direction doorDir = null;
/*  253 */               if (!doorDirs.isEmpty()) {
/*  254 */                 doorDir = doorDirs.get(this.random.nextInt(doorDirs.size()));
/*  255 */               } else if ((roomData & 0x100000) == 1048576) {
/*  257 */                 doorDir = Direction.UP;
/*      */               } 
/*  260 */               BlockPos roomPos = floorOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (j - this.startY) * 8);
/*  261 */               roomPos = roomPos.relative(rotation.rotate(Direction.EAST), -1 + (x - this.startX) * 8);
/*  263 */               if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, j) && !mansion.isRoomId(grid, x - 1, j, floorNum, roomId))
/*  264 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.WEST) ? doorPiece : wallPiece, roomPos, rotation)); 
/*  266 */               if (grid.get(x + 1, j) == 1 && !thirdFloorStartRoom) {
/*  267 */                 BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 8);
/*  268 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.EAST) ? doorPiece : wallPiece, pos, rotation));
/*      */               } 
/*  270 */               if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, j + 1) && !mansion.isRoomId(grid, x, j + 1, floorNum, roomId)) {
/*  271 */                 BlockPos pos = roomPos.relative(rotation.rotate(Direction.SOUTH), 7);
/*  272 */                 pos = pos.relative(rotation.rotate(Direction.EAST), 7);
/*  273 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.SOUTH) ? doorPiece : wallPiece, pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*  275 */               if (grid.get(x, j - 1) == 1 && !thirdFloorStartRoom) {
/*  276 */                 BlockPos pos = roomPos.relative(rotation.rotate(Direction.NORTH), 1);
/*  277 */                 pos = pos.relative(rotation.rotate(Direction.EAST), 7);
/*  278 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.NORTH) ? doorPiece : wallPiece, pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*  281 */               if (roomType == 65536) {
/*  282 */                 addRoom1x1(pieces, roomPos, rotation, doorDir, roomCollections[floorNum]);
/*  283 */               } else if (roomType == 131072 && doorDir != null) {
/*  285 */                 Direction roomDir = mansion.get1x2RoomDirection(grid, x, j, floorNum, roomId);
/*  286 */                 boolean isStairsRoom = ((roomData & 0x400000) == 4194304);
/*  287 */                 addRoom1x2(pieces, roomPos, rotation, roomDir, doorDir, roomCollections[floorNum], isStairsRoom);
/*  288 */               } else if (roomType == 262144 && doorDir != null && doorDir != Direction.UP) {
/*  290 */                 Direction roomDir = doorDir.getClockWise();
/*  291 */                 if (!mansion.isRoomId(grid, x + roomDir.getStepX(), j + roomDir.getStepZ(), floorNum, roomId))
/*  292 */                   roomDir = roomDir.getOpposite(); 
/*  294 */                 addRoom2x2(pieces, roomPos, rotation, roomDir, doorDir, roomCollections[floorNum]);
/*  295 */               } else if (roomType == 262144 && doorDir == Direction.UP) {
/*  296 */                 addRoom2x2Secret(pieces, roomPos, rotation, roomCollections[floorNum]);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void traverseOuterWalls(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data, WoodlandMansionPieces.SimpleGrid grid, Direction gridDirection, int startX, int startY, int endX, int endY) {
/*  305 */       int gridX = startX;
/*  306 */       int gridY = startY;
/*  307 */       Direction startDirection = gridDirection;
/*      */       do {
/*  310 */         if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX(), gridY + gridDirection.getStepZ())) {
/*  312 */           traverseTurn(pieces, data);
/*  313 */           gridDirection = gridDirection.getClockWise();
/*  314 */           if (gridX != endX || gridY != endY || startDirection != gridDirection)
/*  315 */             traverseWallPiece(pieces, data); 
/*  317 */         } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX(), gridY + gridDirection.getStepZ()) && WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX() + gridDirection.getCounterClockWise().getStepX(), gridY + gridDirection.getStepZ() + gridDirection.getCounterClockWise().getStepZ())) {
/*  319 */           traverseInnerTurn(pieces, data);
/*  320 */           gridX += gridDirection.getStepX();
/*  321 */           gridY += gridDirection.getStepZ();
/*  322 */           gridDirection = gridDirection.getCounterClockWise();
/*      */         } else {
/*  324 */           gridX += gridDirection.getStepX();
/*  325 */           gridY += gridDirection.getStepZ();
/*  326 */           if (gridX != endX || gridY != endY || startDirection != gridDirection)
/*  327 */             traverseWallPiece(pieces, data); 
/*      */         } 
/*  330 */       } while (gridX != endX || gridY != endY || startDirection != gridDirection);
/*      */     }
/*      */     
/*      */     private void createRoof(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roofOrigin, Rotation rotation, WoodlandMansionPieces.SimpleGrid grid, WoodlandMansionPieces.SimpleGrid aboveGrid) {
/*      */       int y;
/*  335 */       for (y = 0; y < grid.height; y++) {
/*  336 */         for (int x = 0; x < grid.width; x++) {
/*  337 */           BlockPos position = roofOrigin;
/*  338 */           position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/*  339 */           position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*  342 */           boolean isAbove = (aboveGrid != null && WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, y));
/*  344 */           if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y) && !isAbove) {
/*  345 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof", position.above(3), rotation));
/*  347 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/*  348 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/*  349 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation));
/*      */             } 
/*  351 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/*  352 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 0);
/*  353 */               p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/*  354 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */             } 
/*  356 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/*  357 */               BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/*  358 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */             } 
/*  360 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/*  361 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/*  362 */               p2 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/*  363 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  369 */       if (aboveGrid != null)
/*  370 */         for (y = 0; y < grid.height; y++) {
/*  371 */           for (int x = 0; x < grid.width; x++) {
/*  372 */             BlockPos position = roofOrigin;
/*  373 */             position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/*  374 */             position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*  377 */             boolean isAbove = WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, y);
/*  379 */             if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y) && isAbove) {
/*  381 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/*  382 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 7);
/*  383 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation));
/*      */               } 
/*  385 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/*  386 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/*  387 */                 p2 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/*  388 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */               } 
/*  390 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/*  391 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 0);
/*  392 */                 p2 = p2.relative(rotation.rotate(Direction.NORTH), 1);
/*  393 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */               } 
/*  395 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/*  396 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/*  397 */                 p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/*  398 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*  401 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/*  402 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/*  403 */                   BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 7);
/*  404 */                   p2 = p2.relative(rotation.rotate(Direction.NORTH), 2);
/*  405 */                   pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation));
/*      */                 } 
/*  407 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/*  408 */                   BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 8);
/*  409 */                   p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/*  410 */                   pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */                 } 
/*      */               } 
/*  413 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/*  414 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/*  415 */                   BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 2);
/*  416 */                   p2 = p2.relative(rotation.rotate(Direction.NORTH), 1);
/*  417 */                   pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */                 } 
/*  419 */                 if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/*  420 */                   BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/*  421 */                   p2 = p2.relative(rotation.rotate(Direction.SOUTH), 8);
/*  422 */                   pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }  
/*  430 */       for (y = 0; y < grid.height; y++) {
/*  431 */         for (int x = 0; x < grid.width; x++) {
/*  432 */           BlockPos position = roofOrigin;
/*  433 */           position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/*  434 */           position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/*  437 */           boolean isAbove = (aboveGrid != null && WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, y));
/*  439 */           if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y) && !isAbove) {
/*  440 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/*  441 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/*  442 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/*  443 */                 BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/*  444 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p3, rotation));
/*  445 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y + 1)) {
/*  446 */                 BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 5);
/*  447 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation));
/*      */               } 
/*  449 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/*  450 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  451 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y - 1)) {
/*  452 */                 BlockPos p3 = position.relative(rotation.rotate(Direction.EAST), 9);
/*  453 */                 p3 = p3.relative(rotation.rotate(Direction.NORTH), 2);
/*  454 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*      */               } 
/*      */             } 
/*  457 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/*  458 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 0);
/*  459 */               p2 = p2.relative(rotation.rotate(Direction.SOUTH), 0);
/*  460 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/*  461 */                 BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/*  462 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  463 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y + 1)) {
/*  464 */                 BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 8);
/*  465 */                 p3 = p3.relative(rotation.rotate(Direction.WEST), 3);
/*  466 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*      */               } 
/*  468 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/*  469 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*  470 */               } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y - 1)) {
/*  471 */                 BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 1);
/*  472 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void entrance(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/*  481 */       Direction west = data.rotation.rotate(Direction.WEST);
/*  482 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "entrance", data.position.relative(west, 9), data.rotation));
/*  483 */       data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 16);
/*      */     }
/*      */     
/*      */     private void traverseWallPiece(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/*  487 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, data.wallType, data.position.relative(data.rotation.rotate(Direction.EAST), 7), data.rotation));
/*  488 */       data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 8);
/*      */     }
/*      */     
/*      */     private void traverseTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/*  492 */       data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), -1);
/*  493 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "wall_corner", data.position, data.rotation));
/*  494 */       data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), -7);
/*  495 */       data.position = data.position.relative(data.rotation.rotate(Direction.WEST), -6);
/*  496 */       data.rotation = data.rotation.getRotated(Rotation.CLOCKWISE_90);
/*      */     }
/*      */     
/*      */     private void traverseInnerTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/*  500 */       data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 6);
/*  501 */       data.position = data.position.relative(data.rotation.rotate(Direction.EAST), 8);
/*  502 */       data.rotation = data.rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*      */     }
/*      */     
/*      */     private void addRoom1x1(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms) {
/*  506 */       Rotation pieceRot = Rotation.NONE;
/*  507 */       String roomType = rooms.get1x1(this.random);
/*  508 */       if (doorDir != Direction.EAST)
/*  509 */         if (doorDir == Direction.NORTH) {
/*  510 */           pieceRot = pieceRot.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  511 */         } else if (doorDir == Direction.WEST) {
/*  512 */           pieceRot = pieceRot.getRotated(Rotation.CLOCKWISE_180);
/*  513 */         } else if (doorDir == Direction.SOUTH) {
/*  514 */           pieceRot = pieceRot.getRotated(Rotation.CLOCKWISE_90);
/*      */         } else {
/*  517 */           roomType = rooms.get1x1Secret(this.random);
/*      */         }  
/*  520 */       BlockPos orientation = StructureTemplate.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, pieceRot, 7, 7);
/*  521 */       pieceRot = pieceRot.getRotated(rotation);
/*  522 */       orientation = orientation.rotate(rotation);
/*  523 */       BlockPos pos = roomPos.offset(orientation.getX(), 0, orientation.getZ());
/*  524 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, roomType, pos, pieceRot));
/*      */     }
/*      */     
/*      */     private void addRoom1x2(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction roomDir, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms, boolean isStairsRoom) {
/*  531 */       if (doorDir == Direction.EAST && roomDir == Direction.SOUTH) {
/*  534 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  535 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation));
/*  536 */       } else if (doorDir == Direction.EAST && roomDir == Direction.NORTH) {
/*  539 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  540 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/*  541 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation, Mirror.LEFT_RIGHT));
/*  542 */       } else if (doorDir == Direction.WEST && roomDir == Direction.NORTH) {
/*  545 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/*  546 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/*  547 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*  548 */       } else if (doorDir == Direction.WEST && roomDir == Direction.SOUTH) {
/*  551 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/*  552 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation, Mirror.FRONT_BACK));
/*  553 */       } else if (doorDir == Direction.SOUTH && roomDir == Direction.EAST) {
/*  556 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  557 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
/*  558 */       } else if (doorDir == Direction.SOUTH && roomDir == Direction.WEST) {
/*  561 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/*  562 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  563 */       } else if (doorDir == Direction.NORTH && roomDir == Direction.WEST) {
/*  566 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/*  567 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/*  568 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
/*  569 */       } else if (doorDir == Direction.NORTH && roomDir == Direction.EAST) {
/*  572 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  573 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/*  574 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  575 */       } else if (doorDir == Direction.SOUTH && roomDir == Direction.NORTH) {
/*  579 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  580 */         pos = pos.relative(rotation.rotate(Direction.NORTH), 8);
/*  581 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation));
/*  582 */       } else if (doorDir == Direction.NORTH && roomDir == Direction.SOUTH) {
/*  586 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/*  587 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 14);
/*  588 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*  589 */       } else if (doorDir == Direction.WEST && roomDir == Direction.EAST) {
/*  591 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 15);
/*  592 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  593 */       } else if (doorDir == Direction.EAST && roomDir == Direction.WEST) {
/*  595 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.WEST), 7);
/*  596 */         pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/*  597 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*  598 */       } else if (doorDir == Direction.UP && roomDir == Direction.EAST) {
/*  600 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 15);
/*  601 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2Secret(this.random), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*  602 */       } else if (doorDir == Direction.UP && roomDir == Direction.SOUTH) {
/*  606 */         BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  607 */         pos = pos.relative(rotation.rotate(Direction.NORTH), 0);
/*  608 */         pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2Secret(this.random), pos, rotation));
/*      */       } 
/*      */     }
/*      */     
/*      */     private void addRoom2x2(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction roomDir, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms) {
/*  613 */       int east = 0;
/*  614 */       int south = 0;
/*  615 */       Rotation rot = rotation;
/*  616 */       Mirror mirror = Mirror.NONE;
/*  620 */       if (doorDir == Direction.EAST && roomDir == Direction.SOUTH) {
/*  623 */         east = -7;
/*  624 */       } else if (doorDir == Direction.EAST && roomDir == Direction.NORTH) {
/*  627 */         east = -7;
/*  628 */         south = 6;
/*  629 */         mirror = Mirror.LEFT_RIGHT;
/*  630 */       } else if (doorDir == Direction.NORTH && roomDir == Direction.EAST) {
/*  634 */         east = 1;
/*  635 */         south = 14;
/*  636 */         rot = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  637 */       } else if (doorDir == Direction.NORTH && roomDir == Direction.WEST) {
/*  641 */         east = 7;
/*  642 */         south = 14;
/*  643 */         rot = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*  644 */         mirror = Mirror.LEFT_RIGHT;
/*  645 */       } else if (doorDir == Direction.SOUTH && roomDir == Direction.WEST) {
/*  649 */         east = 7;
/*  650 */         south = -8;
/*  651 */         rot = rotation.getRotated(Rotation.CLOCKWISE_90);
/*  652 */       } else if (doorDir == Direction.SOUTH && roomDir == Direction.EAST) {
/*  656 */         east = 1;
/*  657 */         south = -8;
/*  658 */         rot = rotation.getRotated(Rotation.CLOCKWISE_90);
/*  659 */         mirror = Mirror.LEFT_RIGHT;
/*  660 */       } else if (doorDir == Direction.WEST && roomDir == Direction.NORTH) {
/*  663 */         east = 15;
/*  664 */         south = 6;
/*  665 */         rot = rotation.getRotated(Rotation.CLOCKWISE_180);
/*  666 */       } else if (doorDir == Direction.WEST && roomDir == Direction.SOUTH) {
/*  669 */         east = 15;
/*  670 */         mirror = Mirror.FRONT_BACK;
/*      */       } 
/*  673 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), east);
/*  674 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), south);
/*  675 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get2x2(this.random), pos, rot, mirror));
/*      */     }
/*      */     
/*      */     private void addRoom2x2Secret(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, WoodlandMansionPieces.FloorRoomCollection rooms) {
/*  679 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/*  680 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get2x2Secret(this.random), pos, rotation, Mirror.NONE));
/*      */     }
/*      */   }
/*      */   
/*      */   private static class MansionGrid {
/*      */     private static final int DEFAULT_SIZE = 11;
/*      */     
/*      */     private static final int CLEAR = 0;
/*      */     
/*      */     private static final int CORRIDOR = 1;
/*      */     
/*      */     private static final int ROOM = 2;
/*      */     
/*      */     private static final int START_ROOM = 3;
/*      */     
/*      */     private static final int TEST_ROOM = 4;
/*      */     
/*      */     private static final int BLOCKED = 5;
/*      */     
/*      */     private static final int ROOM_1x1 = 65536;
/*      */     
/*      */     private static final int ROOM_1x2 = 131072;
/*      */     
/*      */     private static final int ROOM_2x2 = 262144;
/*      */     
/*      */     private static final int ROOM_ORIGIN_FLAG = 1048576;
/*      */     
/*      */     private static final int ROOM_DOOR_FLAG = 2097152;
/*      */     
/*      */     private static final int ROOM_STAIRS_FLAG = 4194304;
/*      */     
/*      */     private static final int ROOM_CORRIDOR_FLAG = 8388608;
/*      */     
/*      */     private static final int ROOM_TYPE_MASK = 983040;
/*      */     
/*      */     private static final int ROOM_ID_MASK = 65535;
/*      */     
/*      */     private final RandomSource random;
/*      */     
/*      */     private final WoodlandMansionPieces.SimpleGrid baseGrid;
/*      */     
/*      */     private final WoodlandMansionPieces.SimpleGrid thirdFloorGrid;
/*      */     
/*      */     private final WoodlandMansionPieces.SimpleGrid[] floorRooms;
/*      */     
/*      */     private final int entranceX;
/*      */     
/*      */     private final int entranceY;
/*      */     
/*      */     public MansionGrid(RandomSource random) {
/*  714 */       this.random = random;
/*  716 */       int houseSize = 11;
/*  717 */       this.entranceX = 7;
/*  718 */       this.entranceY = 4;
/*  720 */       this.baseGrid = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  721 */       this.baseGrid.set(this.entranceX, this.entranceY, this.entranceX + 1, this.entranceY + 1, 3);
/*  722 */       this.baseGrid.set(this.entranceX - 1, this.entranceY, this.entranceX - 1, this.entranceY + 1, 2);
/*  723 */       this.baseGrid.set(this.entranceX + 2, this.entranceY - 2, this.entranceX + 3, this.entranceY + 3, 5);
/*  724 */       this.baseGrid.set(this.entranceX + 1, this.entranceY - 2, this.entranceX + 1, this.entranceY - 1, 1);
/*  725 */       this.baseGrid.set(this.entranceX + 1, this.entranceY + 2, this.entranceX + 1, this.entranceY + 3, 1);
/*  726 */       this.baseGrid.set(this.entranceX - 1, this.entranceY - 1, 1);
/*  727 */       this.baseGrid.set(this.entranceX - 1, this.entranceY + 2, 1);
/*  729 */       this.baseGrid.set(0, 0, 11, 1, 5);
/*  730 */       this.baseGrid.set(0, 9, 11, 11, 5);
/*  732 */       recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY - 2, Direction.WEST, 6);
/*  733 */       recursiveCorridor(this.baseGrid, this.entranceX, this.entranceY + 3, Direction.WEST, 6);
/*  734 */       recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY - 1, Direction.WEST, 3);
/*  735 */       recursiveCorridor(this.baseGrid, this.entranceX - 2, this.entranceY + 2, Direction.WEST, 3);
/*  736 */       while (cleanEdges(this.baseGrid));
/*  739 */       this.floorRooms = new WoodlandMansionPieces.SimpleGrid[3];
/*  740 */       this.floorRooms[0] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  741 */       this.floorRooms[1] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  742 */       this.floorRooms[2] = new WoodlandMansionPieces.SimpleGrid(11, 11, 5);
/*  743 */       identifyRooms(this.baseGrid, this.floorRooms[0]);
/*  744 */       identifyRooms(this.baseGrid, this.floorRooms[1]);
/*  747 */       this.floorRooms[0].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/*  748 */       this.floorRooms[1].set(this.entranceX + 1, this.entranceY, this.entranceX + 1, this.entranceY + 1, 8388608);
/*  750 */       this.thirdFloorGrid = new WoodlandMansionPieces.SimpleGrid(this.baseGrid.width, this.baseGrid.height, 5);
/*  751 */       setupThirdFloor();
/*  752 */       identifyRooms(this.thirdFloorGrid, this.floorRooms[2]);
/*      */     }
/*      */     
/*      */     public static boolean isHouse(WoodlandMansionPieces.SimpleGrid grid, int x, int y) {
/*  756 */       int value = grid.get(x, y);
/*  757 */       return (value == 1 || value == 2 || value == 3 || value == 4);
/*      */     }
/*      */     
/*      */     public boolean isRoomId(WoodlandMansionPieces.SimpleGrid grid, int x, int y, int floor, int roomId) {
/*  761 */       return ((this.floorRooms[floor].get(x, y) & 0xFFFF) == roomId);
/*      */     }
/*      */     
/*      */     public Direction get1x2RoomDirection(WoodlandMansionPieces.SimpleGrid grid, int x, int y, int floorNum, int roomId) {
/*  765 */       for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  766 */         if (isRoomId(grid, x + direction.getStepX(), y + direction.getStepZ(), floorNum, roomId))
/*  767 */           return direction; 
/*      */       } 
/*  770 */       return null;
/*      */     }
/*      */     
/*      */     private void recursiveCorridor(WoodlandMansionPieces.SimpleGrid grid, int x, int y, Direction heading, int depth) {
/*  774 */       if (depth <= 0)
/*      */         return; 
/*  778 */       grid.set(x, y, 1);
/*  779 */       grid.setif(x + heading.getStepX(), y + heading.getStepZ(), 0, 1);
/*  781 */       for (int attempts = 0; attempts < 8; attempts++) {
/*  782 */         Direction nextDir = Direction.from2DDataValue(this.random.nextInt(4));
/*  783 */         if (nextDir != heading.getOpposite())
/*  786 */           if (nextDir != Direction.EAST || !this.random.nextBoolean()) {
/*  790 */             int nx = x + heading.getStepX();
/*  791 */             int ny = y + heading.getStepZ();
/*  792 */             if (grid.get(nx + nextDir.getStepX(), ny + nextDir.getStepZ()) == 0 && grid.get(nx + nextDir.getStepX() * 2, ny + nextDir.getStepZ() * 2) == 0) {
/*  793 */               recursiveCorridor(grid, x + heading.getStepX() + nextDir.getStepX(), y + heading.getStepZ() + nextDir.getStepZ(), nextDir, depth - 1);
/*      */               break;
/*      */             } 
/*      */           }  
/*      */       } 
/*  797 */       Direction cw = heading.getClockWise();
/*  798 */       Direction ccw = heading.getCounterClockWise();
/*  799 */       grid.setif(x + cw.getStepX(), y + cw.getStepZ(), 0, 2);
/*  800 */       grid.setif(x + ccw.getStepX(), y + ccw.getStepZ(), 0, 2);
/*  802 */       grid.setif(x + heading.getStepX() + cw.getStepX(), y + heading.getStepZ() + cw.getStepZ(), 0, 2);
/*  803 */       grid.setif(x + heading.getStepX() + ccw.getStepX(), y + heading.getStepZ() + ccw.getStepZ(), 0, 2);
/*  804 */       grid.setif(x + heading.getStepX() * 2, y + heading.getStepZ() * 2, 0, 2);
/*  805 */       grid.setif(x + cw.getStepX() * 2, y + cw.getStepZ() * 2, 0, 2);
/*  806 */       grid.setif(x + ccw.getStepX() * 2, y + ccw.getStepZ() * 2, 0, 2);
/*      */     }
/*      */     
/*      */     private boolean cleanEdges(WoodlandMansionPieces.SimpleGrid grid) {
/*  810 */       boolean touched = false;
/*  811 */       for (int y = 0; y < grid.height; y++) {
/*  812 */         for (int x = 0; x < grid.width; x++) {
/*  813 */           if (grid.get(x, y) == 0) {
/*  814 */             int directNeighbors = 0;
/*  815 */             directNeighbors += isHouse(grid, x + 1, y) ? 1 : 0;
/*  816 */             directNeighbors += isHouse(grid, x - 1, y) ? 1 : 0;
/*  817 */             directNeighbors += isHouse(grid, x, y + 1) ? 1 : 0;
/*  818 */             directNeighbors += isHouse(grid, x, y - 1) ? 1 : 0;
/*  820 */             if (directNeighbors >= 3) {
/*  822 */               grid.set(x, y, 2);
/*  823 */               touched = true;
/*  824 */             } else if (directNeighbors == 2) {
/*  826 */               int diagonalNeighbors = 0;
/*  827 */               diagonalNeighbors += isHouse(grid, x + 1, y + 1) ? 1 : 0;
/*  828 */               diagonalNeighbors += isHouse(grid, x - 1, y + 1) ? 1 : 0;
/*  829 */               diagonalNeighbors += isHouse(grid, x + 1, y - 1) ? 1 : 0;
/*  830 */               diagonalNeighbors += isHouse(grid, x - 1, y - 1) ? 1 : 0;
/*  831 */               if (diagonalNeighbors <= 1) {
/*  832 */                 grid.set(x, y, 2);
/*  833 */                 touched = true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*  839 */       return touched;
/*      */     }
/*      */     
/*      */     private static final class GridPos extends Record {
/*      */       private final int x;
/*      */       
/*      */       private final int y;
/*      */       
/*      */       private GridPos(int x, int y) {
/*  842 */         this.x = x;
/*  842 */         this.y = y;
/*      */       }
/*      */       
/*      */       public final String toString() {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;)Ljava/lang/String;
/*      */         //   6: areturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #842	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;
/*      */       }
/*      */       
/*      */       public final int hashCode() {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;)I
/*      */         //   6: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #842	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;
/*      */       }
/*      */       
/*      */       public final boolean equals(Object o) {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: aload_1
/*      */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;Ljava/lang/Object;)Z
/*      */         //   7: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #842	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;
/*      */         //   0	8	1	o	Ljava/lang/Object;
/*      */       }
/*      */       
/*      */       public int x() {
/*  842 */         return this.x;
/*      */       }
/*      */       
/*      */       public int y() {
/*  842 */         return this.y;
/*      */       }
/*      */     }
/*      */     
/*      */     private void setupThirdFloor() {
/*  847 */       List<GridPos> potentialRooms = new ArrayList<>();
/*  848 */       WoodlandMansionPieces.SimpleGrid floor = this.floorRooms[1];
/*  849 */       for (int y = 0; y < this.thirdFloorGrid.height; y++) {
/*  850 */         for (int x = 0; x < this.thirdFloorGrid.width; x++) {
/*  851 */           int j = floor.get(x, y);
/*  852 */           int roomType = j & 0xF0000;
/*  853 */           if (roomType == 131072 && (j & 0x200000) == 2097152)
/*  854 */             potentialRooms.add(new GridPos(x, y)); 
/*      */         } 
/*      */       } 
/*  859 */       if (potentialRooms.isEmpty()) {
/*  861 */         this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*      */         return;
/*      */       } 
/*  865 */       GridPos roomPos = potentialRooms.get(this.random.nextInt(potentialRooms.size()));
/*  866 */       int roomData = floor.get(roomPos.x(), roomPos.y());
/*  867 */       floor.set(roomPos.x(), roomPos.y(), roomData | 0x400000);
/*  868 */       Direction roomDir = get1x2RoomDirection(this.baseGrid, roomPos.x(), roomPos.y(), 1, roomData & 0xFFFF);
/*  869 */       int roomEndX = roomPos.x() + roomDir.getStepX();
/*  870 */       int roomEndY = roomPos.y() + roomDir.getStepZ();
/*  872 */       for (int i = 0; i < this.thirdFloorGrid.height; i++) {
/*  873 */         for (int x = 0; x < this.thirdFloorGrid.width; x++) {
/*  874 */           if (!isHouse(this.baseGrid, x, i)) {
/*  875 */             this.thirdFloorGrid.set(x, i, 5);
/*  876 */           } else if (x == roomPos.x() && i == roomPos.y()) {
/*  877 */             this.thirdFloorGrid.set(x, i, 3);
/*  878 */           } else if (x == roomEndX && i == roomEndY) {
/*  879 */             this.thirdFloorGrid.set(x, i, 3);
/*  880 */             this.floorRooms[2].set(x, i, 8388608);
/*      */           } 
/*      */         } 
/*      */       } 
/*  885 */       List<Direction> potentialCorridors = new ArrayList<>();
/*  886 */       for (Direction direction : Direction.Plane.HORIZONTAL) {
/*  887 */         if (this.thirdFloorGrid.get(roomEndX + direction.getStepX(), roomEndY + direction.getStepZ()) == 0)
/*  888 */           potentialCorridors.add(direction); 
/*      */       } 
/*  892 */       if (potentialCorridors.isEmpty()) {
/*  894 */         this.thirdFloorGrid.set(0, 0, this.thirdFloorGrid.width, this.thirdFloorGrid.height, 5);
/*  895 */         floor.set(roomPos.x(), roomPos.y(), roomData);
/*      */         return;
/*      */       } 
/*  898 */       Direction corridorDir = potentialCorridors.get(this.random.nextInt(potentialCorridors.size()));
/*  899 */       recursiveCorridor(this.thirdFloorGrid, roomEndX + corridorDir.getStepX(), roomEndY + corridorDir.getStepZ(), corridorDir, 4);
/*  900 */       while (cleanEdges(this.thirdFloorGrid));
/*      */     }
/*      */     
/*      */     private void identifyRooms(WoodlandMansionPieces.SimpleGrid fromGrid, WoodlandMansionPieces.SimpleGrid roomGrid) {
/*  905 */       List<GridPos> roomPos = new ArrayList<>();
/*  906 */       for (int y = 0; y < fromGrid.height; y++) {
/*  907 */         for (int x = 0; x < fromGrid.width; x++) {
/*  908 */           if (fromGrid.get(x, y) == 2)
/*  909 */             roomPos.add(new GridPos(x, y)); 
/*      */         } 
/*      */       } 
/*  913 */       Util.shuffle(roomPos, this.random);
/*  915 */       int roomId = 10;
/*  916 */       for (GridPos pos : roomPos) {
/*  917 */         int x = pos.x();
/*  918 */         int i = pos.y();
/*  920 */         if (roomGrid.get(x, i) == 0) {
/*  921 */           int x0 = x;
/*  922 */           int x1 = x;
/*  923 */           int y0 = i;
/*  924 */           int y1 = i;
/*  925 */           int type = 65536;
/*  926 */           if (roomGrid.get(x + 1, i) == 0 && roomGrid.get(x, i + 1) == 0 && roomGrid.get(x + 1, i + 1) == 0 && fromGrid
/*  927 */             .get(x + 1, i) == 2 && fromGrid.get(x, i + 1) == 2 && fromGrid.get(x + 1, i + 1) == 2) {
/*  929 */             x1++;
/*  930 */             y1++;
/*  931 */             type = 262144;
/*  932 */           } else if (roomGrid.get(x - 1, i) == 0 && roomGrid.get(x, i + 1) == 0 && roomGrid.get(x - 1, i + 1) == 0 && fromGrid
/*  933 */             .get(x - 1, i) == 2 && fromGrid.get(x, i + 1) == 2 && fromGrid.get(x - 1, i + 1) == 2) {
/*  935 */             x0--;
/*  936 */             y1++;
/*  937 */             type = 262144;
/*  938 */           } else if (roomGrid.get(x - 1, i) == 0 && roomGrid.get(x, i - 1) == 0 && roomGrid.get(x - 1, i - 1) == 0 && fromGrid
/*  939 */             .get(x - 1, i) == 2 && fromGrid.get(x, i - 1) == 2 && fromGrid.get(x - 1, i - 1) == 2) {
/*  941 */             x0--;
/*  942 */             y0--;
/*  943 */             type = 262144;
/*  944 */           } else if (roomGrid.get(x + 1, i) == 0 && fromGrid.get(x + 1, i) == 2) {
/*  945 */             x1++;
/*  946 */             type = 131072;
/*  947 */           } else if (roomGrid.get(x, i + 1) == 0 && fromGrid.get(x, i + 1) == 2) {
/*  948 */             y1++;
/*  949 */             type = 131072;
/*  950 */           } else if (roomGrid.get(x - 1, i) == 0 && fromGrid.get(x - 1, i) == 2) {
/*  951 */             x0--;
/*  952 */             type = 131072;
/*  953 */           } else if (roomGrid.get(x, i - 1) == 0 && fromGrid.get(x, i - 1) == 2) {
/*  954 */             y0--;
/*  955 */             type = 131072;
/*      */           } 
/*  959 */           int doorX = this.random.nextBoolean() ? x0 : x1;
/*  960 */           int doorY = this.random.nextBoolean() ? y0 : y1;
/*  961 */           int doorFlag = 2097152;
/*  962 */           if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/*  963 */             doorX = (doorX == x0) ? x1 : x0;
/*  964 */             doorY = (doorY == y0) ? y1 : y0;
/*  965 */             if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/*  966 */               doorY = (doorY == y0) ? y1 : y0;
/*  967 */               if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/*  968 */                 doorX = (doorX == x0) ? x1 : x0;
/*  969 */                 doorY = (doorY == y0) ? y1 : y0;
/*  970 */                 if (!fromGrid.edgesTo(doorX, doorY, 1)) {
/*  972 */                   doorFlag = 0;
/*  973 */                   doorX = x0;
/*  974 */                   doorY = y0;
/*      */                 } 
/*      */               } 
/*      */             } 
/*      */           } 
/*  979 */           for (int ry = y0; ry <= y1; ry++) {
/*  980 */             for (int rx = x0; rx <= x1; rx++) {
/*  981 */               if (rx == doorX && ry == doorY) {
/*  982 */                 roomGrid.set(rx, ry, 0x100000 | doorFlag | type | roomId);
/*      */               } else {
/*  984 */                 roomGrid.set(rx, ry, type | roomId);
/*      */               } 
/*      */             } 
/*      */           } 
/*  989 */           roomId++;
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class GridPos extends Record {
/*      */     private final int x;
/*      */     
/*      */     private final int y;
/*      */     
/*      */     private GridPos(int x, int y) {
/*      */       this.x = x;
/*      */       this.y = y;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #842	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #842	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #842	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/structure/structures/WoodlandMansionPieces$MansionGrid$GridPos;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public int x() {
/*      */       return this.x;
/*      */     }
/*      */     
/*      */     public int y() {
/*      */       return this.y;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SimpleGrid {
/*      */     private final int[][] grid;
/*      */     
/*      */     private final int width;
/*      */     
/*      */     private final int height;
/*      */     
/*      */     private final int valueIfOutside;
/*      */     
/*      */     public SimpleGrid(int width, int height, int valueIfOutside) {
/* 1002 */       this.width = width;
/* 1003 */       this.height = height;
/* 1004 */       this.valueIfOutside = valueIfOutside;
/* 1005 */       this.grid = new int[width][height];
/*      */     }
/*      */     
/*      */     public void set(int x, int y, int value) {
/* 1009 */       if (x >= 0 && x < this.width && y >= 0 && y < this.height)
/* 1010 */         this.grid[x][y] = value; 
/*      */     }
/*      */     
/*      */     public void set(int x0, int y0, int x1, int y1, int value) {
/* 1015 */       for (int y = y0; y <= y1; y++) {
/* 1016 */         for (int x = x0; x <= x1; x++)
/* 1017 */           set(x, y, value); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int get(int x, int y) {
/* 1023 */       if (x >= 0 && x < this.width && y >= 0 && y < this.height)
/* 1024 */         return this.grid[x][y]; 
/* 1026 */       return this.valueIfOutside;
/*      */     }
/*      */     
/*      */     public void setif(int x, int y, int ifValue, int value) {
/* 1030 */       if (get(x, y) == ifValue)
/* 1031 */         set(x, y, value); 
/*      */     }
/*      */     
/*      */     public boolean edgesTo(int x, int y, int ifValue) {
/* 1036 */       return (get(x - 1, y) == ifValue || get(x + 1, y) == ifValue || get(x, y + 1) == ifValue || get(x, y - 1) == ifValue);
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract class FloorRoomCollection {
/*      */     public abstract String get1x1(RandomSource param1RandomSource);
/*      */     
/*      */     public abstract String get1x1Secret(RandomSource param1RandomSource);
/*      */     
/*      */     public abstract String get1x2SideEntrance(RandomSource param1RandomSource, boolean param1Boolean);
/*      */     
/*      */     public abstract String get1x2FrontEntrance(RandomSource param1RandomSource, boolean param1Boolean);
/*      */     
/*      */     public abstract String get1x2Secret(RandomSource param1RandomSource);
/*      */     
/*      */     public abstract String get2x2(RandomSource param1RandomSource);
/*      */     
/*      */     public abstract String get2x2Secret(RandomSource param1RandomSource);
/*      */   }
/*      */   
/*      */   private static class FirstFloorRoomCollection extends FloorRoomCollection {
/*      */     public String get1x1(RandomSource random) {
/* 1075 */       return "1x1_a" + random.nextInt(5) + 1;
/*      */     }
/*      */     
/*      */     public String get1x1Secret(RandomSource random) {
/* 1080 */       return "1x1_as" + random.nextInt(4) + 1;
/*      */     }
/*      */     
/*      */     public String get1x2SideEntrance(RandomSource random, boolean isStairsRoom) {
/* 1085 */       return "1x2_a" + random.nextInt(9) + 1;
/*      */     }
/*      */     
/*      */     public String get1x2FrontEntrance(RandomSource random, boolean isStairsRoom) {
/* 1090 */       return "1x2_b" + random.nextInt(5) + 1;
/*      */     }
/*      */     
/*      */     public String get1x2Secret(RandomSource random) {
/* 1095 */       return "1x2_s" + random.nextInt(2) + 1;
/*      */     }
/*      */     
/*      */     public String get2x2(RandomSource random) {
/* 1100 */       return "2x2_a" + random.nextInt(4) + 1;
/*      */     }
/*      */     
/*      */     public String get2x2Secret(RandomSource random) {
/* 1105 */       return "2x2_s1";
/*      */     }
/*      */   }
/*      */   
/*      */   private static class SecondFloorRoomCollection extends FloorRoomCollection {
/*      */     public String get1x1(RandomSource random) {
/* 1112 */       return "1x1_b" + random.nextInt(5) + 1;
/*      */     }
/*      */     
/*      */     public String get1x1Secret(RandomSource random) {
/* 1117 */       return "1x1_as" + random.nextInt(4) + 1;
/*      */     }
/*      */     
/*      */     public String get1x2SideEntrance(RandomSource random, boolean isStairsRoom) {
/* 1122 */       if (isStairsRoom)
/* 1123 */         return "1x2_c_stairs"; 
/* 1125 */       return "1x2_c" + random.nextInt(4) + 1;
/*      */     }
/*      */     
/*      */     public String get1x2FrontEntrance(RandomSource random, boolean isStairsRoom) {
/* 1130 */       if (isStairsRoom)
/* 1131 */         return "1x2_d_stairs"; 
/* 1133 */       return "1x2_d" + random.nextInt(5) + 1;
/*      */     }
/*      */     
/*      */     public String get1x2Secret(RandomSource random) {
/* 1138 */       return "1x2_se" + random.nextInt(1) + 1;
/*      */     }
/*      */     
/*      */     public String get2x2(RandomSource random) {
/* 1143 */       return "2x2_b" + random.nextInt(5) + 1;
/*      */     }
/*      */     
/*      */     public String get2x2Secret(RandomSource random) {
/* 1148 */       return "2x2_s1";
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ThirdFloorRoomCollection extends SecondFloorRoomCollection {}
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */