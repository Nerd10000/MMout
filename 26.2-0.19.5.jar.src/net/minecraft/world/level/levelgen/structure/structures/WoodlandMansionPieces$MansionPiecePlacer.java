/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ 
/*     */ class MansionPiecePlacer {
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   
/*     */   private final RandomSource random;
/*     */   
/*     */   private int startX;
/*     */   
/*     */   private int startY;
/*     */   
/*     */   public MansionPiecePlacer(StructureTemplateManager structureTemplateManager, RandomSource random) {
/* 134 */     this.structureTemplateManager = structureTemplateManager;
/* 135 */     this.random = random;
/*     */   }
/*     */   
/*     */   public void createMansion(BlockPos origin, Rotation rotation, List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.MansionGrid mansion) {
/* 139 */     WoodlandMansionPieces.PlacementData data = new WoodlandMansionPieces.PlacementData();
/* 140 */     data.position = origin;
/* 141 */     data.rotation = rotation;
/* 142 */     data.wallType = "wall_flat";
/* 144 */     WoodlandMansionPieces.PlacementData secondData = new WoodlandMansionPieces.PlacementData();
/* 147 */     entrance(pieces, data);
/* 148 */     secondData.position = data.position.above(8);
/* 149 */     secondData.rotation = data.rotation;
/* 150 */     secondData.wallType = "wall_window";
/* 152 */     if (!pieces.isEmpty());
/* 156 */     WoodlandMansionPieces.SimpleGrid baseGrid = mansion.baseGrid;
/* 157 */     WoodlandMansionPieces.SimpleGrid thirdGrid = mansion.thirdFloorGrid;
/* 159 */     this.startX = mansion.entranceX + 1;
/* 160 */     this.startY = mansion.entranceY + 1;
/* 161 */     int endX = mansion.entranceX + 1;
/* 162 */     int endY = mansion.entranceY;
/* 164 */     traverseOuterWalls(pieces, data, baseGrid, Direction.SOUTH, this.startX, this.startY, endX, endY);
/* 165 */     traverseOuterWalls(pieces, secondData, baseGrid, Direction.SOUTH, this.startX, this.startY, endX, endY);
/* 168 */     WoodlandMansionPieces.PlacementData thirdData = new WoodlandMansionPieces.PlacementData();
/* 169 */     thirdData.position = data.position.above(19);
/* 170 */     thirdData.rotation = data.rotation;
/* 171 */     thirdData.wallType = "wall_window";
/* 173 */     boolean done = false;
/* 174 */     for (int y = 0; y < thirdGrid.height && !done; y++) {
/* 175 */       for (int x = thirdGrid.width - 1; x >= 0 && !done; x--) {
/* 176 */         if (WoodlandMansionPieces.MansionGrid.isHouse(thirdGrid, x, y)) {
/* 177 */           thirdData.position = thirdData.position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/* 178 */           thirdData.position = thirdData.position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/* 179 */           traverseWallPiece(pieces, thirdData);
/* 180 */           traverseOuterWalls(pieces, thirdData, thirdGrid, Direction.SOUTH, x, y, x, y);
/* 181 */           done = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 187 */     createRoof(pieces, origin.above(16), rotation, baseGrid, thirdGrid);
/* 188 */     createRoof(pieces, origin.above(27), rotation, thirdGrid, null);
/* 190 */     if (!pieces.isEmpty());
/* 194 */     WoodlandMansionPieces.FloorRoomCollection[] roomCollections = new WoodlandMansionPieces.FloorRoomCollection[3];
/* 195 */     roomCollections[0] = new WoodlandMansionPieces.FirstFloorRoomCollection();
/* 196 */     roomCollections[1] = new WoodlandMansionPieces.SecondFloorRoomCollection();
/* 197 */     roomCollections[2] = new WoodlandMansionPieces.ThirdFloorRoomCollection();
/* 199 */     for (int floorNum = 0; floorNum < 3; floorNum++) {
/* 200 */       BlockPos floorOrigin = origin.above(8 * floorNum + ((floorNum == 2) ? 3 : 0));
/* 201 */       WoodlandMansionPieces.SimpleGrid rooms = mansion.floorRooms[floorNum];
/* 202 */       WoodlandMansionPieces.SimpleGrid grid = (floorNum == 2) ? thirdGrid : baseGrid;
/* 205 */       String southPiece = (floorNum == 0) ? "carpet_south_1" : "carpet_south_2";
/* 206 */       String westPiece = (floorNum == 0) ? "carpet_west_1" : "carpet_west_2";
/* 207 */       for (int i = 0; i < grid.height; i++) {
/* 208 */         for (int x = 0; x < grid.width; x++) {
/* 209 */           if (grid.get(x, i) == 1) {
/* 210 */             BlockPos pos = floorOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (i - this.startY) * 8);
/* 211 */             pos = pos.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/* 212 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "corridor_floor", pos, rotation));
/* 214 */             if (grid.get(x, i - 1) == 1 || (rooms.get(x, i - 1) & 0x800000) == 8388608)
/* 215 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_north", pos.relative(rotation.rotate(Direction.EAST), 1).above(), rotation)); 
/* 217 */             if (grid.get(x + 1, i) == 1 || (rooms.get(x + 1, i) & 0x800000) == 8388608)
/* 218 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "carpet_east", pos.relative(rotation.rotate(Direction.SOUTH), 1).relative(rotation.rotate(Direction.EAST), 5).above(), rotation)); 
/* 220 */             if (grid.get(x, i + 1) == 1 || (rooms.get(x, i + 1) & 0x800000) == 8388608)
/* 221 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, southPiece, pos.relative(rotation.rotate(Direction.SOUTH), 5).relative(rotation.rotate(Direction.WEST), 1), rotation)); 
/* 223 */             if (grid.get(x - 1, i) == 1 || (rooms.get(x - 1, i) & 0x800000) == 8388608)
/* 224 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, westPiece, pos.relative(rotation.rotate(Direction.WEST), 1).relative(rotation.rotate(Direction.NORTH), 1), rotation)); 
/*     */           } 
/*     */         } 
/*     */       } 
/* 230 */       String wallPiece = (floorNum == 0) ? "indoors_wall_1" : "indoors_wall_2";
/* 231 */       String doorPiece = (floorNum == 0) ? "indoors_door_1" : "indoors_door_2";
/* 232 */       List<Direction> doorDirs = new ArrayList<>();
/* 233 */       for (int j = 0; j < grid.height; j++) {
/* 234 */         for (int x = 0; x < grid.width; x++) {
/* 235 */           boolean thirdFloorStartRoom = (floorNum == 2 && grid.get(x, j) == 3);
/* 236 */           if (grid.get(x, j) == 2 || thirdFloorStartRoom) {
/* 237 */             int roomData = rooms.get(x, j);
/* 238 */             int roomType = roomData & 0xF0000;
/* 239 */             int roomId = roomData & 0xFFFF;
/* 242 */             thirdFloorStartRoom = (thirdFloorStartRoom && (roomData & 0x800000) == 8388608);
/* 244 */             doorDirs.clear();
/* 245 */             if ((roomData & 0x200000) == 2097152)
/* 246 */               for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 247 */                 if (grid.get(x + direction.getStepX(), j + direction.getStepZ()) == 1)
/* 248 */                   doorDirs.add(direction); 
/*     */               }  
/* 252 */             Direction doorDir = null;
/* 253 */             if (!doorDirs.isEmpty()) {
/* 254 */               doorDir = doorDirs.get(this.random.nextInt(doorDirs.size()));
/* 255 */             } else if ((roomData & 0x100000) == 1048576) {
/* 257 */               doorDir = Direction.UP;
/*     */             } 
/* 260 */             BlockPos roomPos = floorOrigin.relative(rotation.rotate(Direction.SOUTH), 8 + (j - this.startY) * 8);
/* 261 */             roomPos = roomPos.relative(rotation.rotate(Direction.EAST), -1 + (x - this.startX) * 8);
/* 263 */             if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, j) && !mansion.isRoomId(grid, x - 1, j, floorNum, roomId))
/* 264 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.WEST) ? doorPiece : wallPiece, roomPos, rotation)); 
/* 266 */             if (grid.get(x + 1, j) == 1 && !thirdFloorStartRoom) {
/* 267 */               BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 8);
/* 268 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.EAST) ? doorPiece : wallPiece, pos, rotation));
/*     */             } 
/* 270 */             if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, j + 1) && !mansion.isRoomId(grid, x, j + 1, floorNum, roomId)) {
/* 271 */               BlockPos pos = roomPos.relative(rotation.rotate(Direction.SOUTH), 7);
/* 272 */               pos = pos.relative(rotation.rotate(Direction.EAST), 7);
/* 273 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.SOUTH) ? doorPiece : wallPiece, pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/* 275 */             if (grid.get(x, j - 1) == 1 && !thirdFloorStartRoom) {
/* 276 */               BlockPos pos = roomPos.relative(rotation.rotate(Direction.NORTH), 1);
/* 277 */               pos = pos.relative(rotation.rotate(Direction.EAST), 7);
/* 278 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, (doorDir == Direction.NORTH) ? doorPiece : wallPiece, pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/* 281 */             if (roomType == 65536) {
/* 282 */               addRoom1x1(pieces, roomPos, rotation, doorDir, roomCollections[floorNum]);
/* 283 */             } else if (roomType == 131072 && doorDir != null) {
/* 285 */               Direction roomDir = mansion.get1x2RoomDirection(grid, x, j, floorNum, roomId);
/* 286 */               boolean isStairsRoom = ((roomData & 0x400000) == 4194304);
/* 287 */               addRoom1x2(pieces, roomPos, rotation, roomDir, doorDir, roomCollections[floorNum], isStairsRoom);
/* 288 */             } else if (roomType == 262144 && doorDir != null && doorDir != Direction.UP) {
/* 290 */               Direction roomDir = doorDir.getClockWise();
/* 291 */               if (!mansion.isRoomId(grid, x + roomDir.getStepX(), j + roomDir.getStepZ(), floorNum, roomId))
/* 292 */                 roomDir = roomDir.getOpposite(); 
/* 294 */               addRoom2x2(pieces, roomPos, rotation, roomDir, doorDir, roomCollections[floorNum]);
/* 295 */             } else if (roomType == 262144 && doorDir == Direction.UP) {
/* 296 */               addRoom2x2Secret(pieces, roomPos, rotation, roomCollections[floorNum]);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void traverseOuterWalls(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data, WoodlandMansionPieces.SimpleGrid grid, Direction gridDirection, int startX, int startY, int endX, int endY) {
/* 305 */     int gridX = startX;
/* 306 */     int gridY = startY;
/* 307 */     Direction startDirection = gridDirection;
/*     */     do {
/* 310 */       if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX(), gridY + gridDirection.getStepZ())) {
/* 312 */         traverseTurn(pieces, data);
/* 313 */         gridDirection = gridDirection.getClockWise();
/* 314 */         if (gridX != endX || gridY != endY || startDirection != gridDirection)
/* 315 */           traverseWallPiece(pieces, data); 
/* 317 */       } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX(), gridY + gridDirection.getStepZ()) && WoodlandMansionPieces.MansionGrid.isHouse(grid, gridX + gridDirection.getStepX() + gridDirection.getCounterClockWise().getStepX(), gridY + gridDirection.getStepZ() + gridDirection.getCounterClockWise().getStepZ())) {
/* 319 */         traverseInnerTurn(pieces, data);
/* 320 */         gridX += gridDirection.getStepX();
/* 321 */         gridY += gridDirection.getStepZ();
/* 322 */         gridDirection = gridDirection.getCounterClockWise();
/*     */       } else {
/* 324 */         gridX += gridDirection.getStepX();
/* 325 */         gridY += gridDirection.getStepZ();
/* 326 */         if (gridX != endX || gridY != endY || startDirection != gridDirection)
/* 327 */           traverseWallPiece(pieces, data); 
/*     */       } 
/* 330 */     } while (gridX != endX || gridY != endY || startDirection != gridDirection);
/*     */   }
/*     */   
/*     */   private void createRoof(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roofOrigin, Rotation rotation, WoodlandMansionPieces.SimpleGrid grid, WoodlandMansionPieces.SimpleGrid aboveGrid) {
/*     */     int y;
/* 335 */     for (y = 0; y < grid.height; y++) {
/* 336 */       for (int x = 0; x < grid.width; x++) {
/* 337 */         BlockPos position = roofOrigin;
/* 338 */         position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/* 339 */         position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/* 342 */         boolean isAbove = (aboveGrid != null && WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, y));
/* 344 */         if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y) && !isAbove) {
/* 345 */           pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof", position.above(3), rotation));
/* 347 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/* 348 */             BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/* 349 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation));
/*     */           } 
/* 351 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/* 352 */             BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 0);
/* 353 */             p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/* 354 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */           } 
/* 356 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 357 */             BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/* 358 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */           } 
/* 360 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 361 */             BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/* 362 */             p2 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/* 363 */             pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_front", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 369 */     if (aboveGrid != null)
/* 370 */       for (y = 0; y < grid.height; y++) {
/* 371 */         for (int x = 0; x < grid.width; x++) {
/* 372 */           BlockPos position = roofOrigin;
/* 373 */           position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/* 374 */           position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/* 377 */           boolean isAbove = WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, y);
/* 379 */           if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y) && isAbove) {
/* 381 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/* 382 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 7);
/* 383 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation));
/*     */             } 
/* 385 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/* 386 */               BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/* 387 */               p2 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/* 388 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */             } 
/* 390 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 391 */               BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 0);
/* 392 */               p2 = p2.relative(rotation.rotate(Direction.NORTH), 1);
/* 393 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */             } 
/* 395 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 396 */               BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/* 397 */               p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/* 398 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/* 401 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/* 402 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 403 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 7);
/* 404 */                 p2 = p2.relative(rotation.rotate(Direction.NORTH), 2);
/* 405 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation));
/*     */               } 
/* 407 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 408 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 8);
/* 409 */                 p2 = p2.relative(rotation.rotate(Direction.SOUTH), 7);
/* 410 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */               } 
/*     */             } 
/* 413 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/* 414 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 415 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 2);
/* 416 */                 p2 = p2.relative(rotation.rotate(Direction.NORTH), 1);
/* 417 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */               } 
/* 419 */               if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 420 */                 BlockPos p2 = position.relative(rotation.rotate(Direction.WEST), 1);
/* 421 */                 p2 = p2.relative(rotation.rotate(Direction.SOUTH), 8);
/* 422 */                 pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "small_wall_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }  
/* 430 */     for (y = 0; y < grid.height; y++) {
/* 431 */       for (int x = 0; x < grid.width; x++) {
/* 432 */         BlockPos position = roofOrigin;
/* 433 */         position = position.relative(rotation.rotate(Direction.SOUTH), 8 + (y - this.startY) * 8);
/* 434 */         position = position.relative(rotation.rotate(Direction.EAST), (x - this.startX) * 8);
/* 437 */         boolean isAbove = (aboveGrid != null && WoodlandMansionPieces.MansionGrid.isHouse(aboveGrid, x, y));
/* 439 */         if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y) && !isAbove) {
/* 440 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y)) {
/* 441 */             BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 6);
/* 442 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 443 */               BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/* 444 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p3, rotation));
/* 445 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y + 1)) {
/* 446 */               BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 5);
/* 447 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation));
/*     */             } 
/* 449 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 450 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p2, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/* 451 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x + 1, y - 1)) {
/* 452 */               BlockPos p3 = position.relative(rotation.rotate(Direction.EAST), 9);
/* 453 */               p3 = p3.relative(rotation.rotate(Direction.NORTH), 2);
/* 454 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_90)));
/*     */             } 
/*     */           } 
/* 457 */           if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y)) {
/* 458 */             BlockPos p2 = position.relative(rotation.rotate(Direction.EAST), 0);
/* 459 */             p2 = p2.relative(rotation.rotate(Direction.SOUTH), 0);
/* 460 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y + 1)) {
/* 461 */               BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 6);
/* 462 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_90)));
/* 463 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y + 1)) {
/* 464 */               BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 8);
/* 465 */               p3 = p3.relative(rotation.rotate(Direction.WEST), 3);
/* 466 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/*     */             } 
/* 468 */             if (!WoodlandMansionPieces.MansionGrid.isHouse(grid, x, y - 1)) {
/* 469 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_corner", p2, rotation.getRotated(Rotation.CLOCKWISE_180)));
/* 470 */             } else if (WoodlandMansionPieces.MansionGrid.isHouse(grid, x - 1, y - 1)) {
/* 471 */               BlockPos p3 = p2.relative(rotation.rotate(Direction.SOUTH), 1);
/* 472 */               pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "roof_inner_corner", p3, rotation.getRotated(Rotation.CLOCKWISE_180)));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void entrance(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/* 481 */     Direction west = data.rotation.rotate(Direction.WEST);
/* 482 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "entrance", data.position.relative(west, 9), data.rotation));
/* 483 */     data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 16);
/*     */   }
/*     */   
/*     */   private void traverseWallPiece(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/* 487 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, data.wallType, data.position.relative(data.rotation.rotate(Direction.EAST), 7), data.rotation));
/* 488 */     data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 8);
/*     */   }
/*     */   
/*     */   private void traverseTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/* 492 */     data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), -1);
/* 493 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, "wall_corner", data.position, data.rotation));
/* 494 */     data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), -7);
/* 495 */     data.position = data.position.relative(data.rotation.rotate(Direction.WEST), -6);
/* 496 */     data.rotation = data.rotation.getRotated(Rotation.CLOCKWISE_90);
/*     */   }
/*     */   
/*     */   private void traverseInnerTurn(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, WoodlandMansionPieces.PlacementData data) {
/* 500 */     data.position = data.position.relative(data.rotation.rotate(Direction.SOUTH), 6);
/* 501 */     data.position = data.position.relative(data.rotation.rotate(Direction.EAST), 8);
/* 502 */     data.rotation = data.rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/*     */   }
/*     */   
/*     */   private void addRoom1x1(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms) {
/* 506 */     Rotation pieceRot = Rotation.NONE;
/* 507 */     String roomType = rooms.get1x1(this.random);
/* 508 */     if (doorDir != Direction.EAST)
/* 509 */       if (doorDir == Direction.NORTH) {
/* 510 */         pieceRot = pieceRot.getRotated(Rotation.COUNTERCLOCKWISE_90);
/* 511 */       } else if (doorDir == Direction.WEST) {
/* 512 */         pieceRot = pieceRot.getRotated(Rotation.CLOCKWISE_180);
/* 513 */       } else if (doorDir == Direction.SOUTH) {
/* 514 */         pieceRot = pieceRot.getRotated(Rotation.CLOCKWISE_90);
/*     */       } else {
/* 517 */         roomType = rooms.get1x1Secret(this.random);
/*     */       }  
/* 520 */     BlockPos orientation = StructureTemplate.getZeroPositionWithTransform(new BlockPos(1, 0, 0), Mirror.NONE, pieceRot, 7, 7);
/* 521 */     pieceRot = pieceRot.getRotated(rotation);
/* 522 */     orientation = orientation.rotate(rotation);
/* 523 */     BlockPos pos = roomPos.offset(orientation.getX(), 0, orientation.getZ());
/* 524 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, roomType, pos, pieceRot));
/*     */   }
/*     */   
/*     */   private void addRoom1x2(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction roomDir, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms, boolean isStairsRoom) {
/* 531 */     if (doorDir == Direction.EAST && roomDir == Direction.SOUTH) {
/* 534 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 535 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation));
/* 536 */     } else if (doorDir == Direction.EAST && roomDir == Direction.NORTH) {
/* 539 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 540 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/* 541 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation, Mirror.LEFT_RIGHT));
/* 542 */     } else if (doorDir == Direction.WEST && roomDir == Direction.NORTH) {
/* 545 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/* 546 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/* 547 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_180)));
/* 548 */     } else if (doorDir == Direction.WEST && roomDir == Direction.SOUTH) {
/* 551 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/* 552 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation, Mirror.FRONT_BACK));
/* 553 */     } else if (doorDir == Direction.SOUTH && roomDir == Direction.EAST) {
/* 556 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 557 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90), Mirror.LEFT_RIGHT));
/* 558 */     } else if (doorDir == Direction.SOUTH && roomDir == Direction.WEST) {
/* 561 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/* 562 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/* 563 */     } else if (doorDir == Direction.NORTH && roomDir == Direction.WEST) {
/* 566 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/* 567 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/* 568 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90), Mirror.FRONT_BACK));
/* 569 */     } else if (doorDir == Direction.NORTH && roomDir == Direction.EAST) {
/* 572 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 573 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/* 574 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2SideEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/* 575 */     } else if (doorDir == Direction.SOUTH && roomDir == Direction.NORTH) {
/* 579 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 580 */       pos = pos.relative(rotation.rotate(Direction.NORTH), 8);
/* 581 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation));
/* 582 */     } else if (doorDir == Direction.NORTH && roomDir == Direction.SOUTH) {
/* 586 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 7);
/* 587 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 14);
/* 588 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_180)));
/* 589 */     } else if (doorDir == Direction.WEST && roomDir == Direction.EAST) {
/* 591 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 15);
/* 592 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/* 593 */     } else if (doorDir == Direction.EAST && roomDir == Direction.WEST) {
/* 595 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.WEST), 7);
/* 596 */       pos = pos.relative(rotation.rotate(Direction.SOUTH), 6);
/* 597 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2FrontEntrance(this.random, isStairsRoom), pos, rotation.getRotated(Rotation.COUNTERCLOCKWISE_90)));
/* 598 */     } else if (doorDir == Direction.UP && roomDir == Direction.EAST) {
/* 600 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 15);
/* 601 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2Secret(this.random), pos, rotation.getRotated(Rotation.CLOCKWISE_90)));
/* 602 */     } else if (doorDir == Direction.UP && roomDir == Direction.SOUTH) {
/* 606 */       BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 607 */       pos = pos.relative(rotation.rotate(Direction.NORTH), 0);
/* 608 */       pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get1x2Secret(this.random), pos, rotation));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addRoom2x2(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, Direction roomDir, Direction doorDir, WoodlandMansionPieces.FloorRoomCollection rooms) {
/* 613 */     int east = 0;
/* 614 */     int south = 0;
/* 615 */     Rotation rot = rotation;
/* 616 */     Mirror mirror = Mirror.NONE;
/* 620 */     if (doorDir == Direction.EAST && roomDir == Direction.SOUTH) {
/* 623 */       east = -7;
/* 624 */     } else if (doorDir == Direction.EAST && roomDir == Direction.NORTH) {
/* 627 */       east = -7;
/* 628 */       south = 6;
/* 629 */       mirror = Mirror.LEFT_RIGHT;
/* 630 */     } else if (doorDir == Direction.NORTH && roomDir == Direction.EAST) {
/* 634 */       east = 1;
/* 635 */       south = 14;
/* 636 */       rot = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/* 637 */     } else if (doorDir == Direction.NORTH && roomDir == Direction.WEST) {
/* 641 */       east = 7;
/* 642 */       south = 14;
/* 643 */       rot = rotation.getRotated(Rotation.COUNTERCLOCKWISE_90);
/* 644 */       mirror = Mirror.LEFT_RIGHT;
/* 645 */     } else if (doorDir == Direction.SOUTH && roomDir == Direction.WEST) {
/* 649 */       east = 7;
/* 650 */       south = -8;
/* 651 */       rot = rotation.getRotated(Rotation.CLOCKWISE_90);
/* 652 */     } else if (doorDir == Direction.SOUTH && roomDir == Direction.EAST) {
/* 656 */       east = 1;
/* 657 */       south = -8;
/* 658 */       rot = rotation.getRotated(Rotation.CLOCKWISE_90);
/* 659 */       mirror = Mirror.LEFT_RIGHT;
/* 660 */     } else if (doorDir == Direction.WEST && roomDir == Direction.NORTH) {
/* 663 */       east = 15;
/* 664 */       south = 6;
/* 665 */       rot = rotation.getRotated(Rotation.CLOCKWISE_180);
/* 666 */     } else if (doorDir == Direction.WEST && roomDir == Direction.SOUTH) {
/* 669 */       east = 15;
/* 670 */       mirror = Mirror.FRONT_BACK;
/*     */     } 
/* 673 */     BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), east);
/* 674 */     pos = pos.relative(rotation.rotate(Direction.SOUTH), south);
/* 675 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get2x2(this.random), pos, rot, mirror));
/*     */   }
/*     */   
/*     */   private void addRoom2x2Secret(List<WoodlandMansionPieces.WoodlandMansionPiece> pieces, BlockPos roomPos, Rotation rotation, WoodlandMansionPieces.FloorRoomCollection rooms) {
/* 679 */     BlockPos pos = roomPos.relative(rotation.rotate(Direction.EAST), 1);
/* 680 */     pieces.add(new WoodlandMansionPieces.WoodlandMansionPiece(this.structureTemplateManager, rooms.get2x2Secret(this.random), pos, rotation, Mirror.NONE));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\structures\WoodlandMansionPieces$MansionPiecePlacer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */