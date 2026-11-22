/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ public final class SkyLightEngine extends LightEngine<SkyLightSectionStorage.SkyDataLayerStorageMap, SkyLightSectionStorage> {
/*  19 */   private static final long REMOVE_TOP_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.decreaseAllDirections(15);
/*     */   
/*  20 */   private static final long REMOVE_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.decreaseSkipOneDirection(15, Direction.UP);
/*     */   
/*  21 */   private static final long ADD_SKY_SOURCE_ENTRY = LightEngine.QueueEntry.increaseSkipOneDirection(15, false, Direction.UP);
/*     */   
/*  23 */   private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
/*     */   
/*     */   private final ChunkSkyLightSources emptyChunkSources;
/*     */   
/*     */   public SkyLightEngine(LightChunkGetter chunkSource) {
/*  28 */     this(chunkSource, new SkyLightSectionStorage(chunkSource));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   SkyLightEngine(LightChunkGetter chunkSource, SkyLightSectionStorage storage) {
/*  34 */     super(chunkSource, storage);
/*  35 */     this.emptyChunkSources = new ChunkSkyLightSources((LevelHeightAccessor)chunkSource.getLevel());
/*     */   }
/*     */   
/*     */   private static boolean isSourceLevel(int value) {
/*  39 */     return (value == 15);
/*     */   }
/*     */   
/*     */   private int getLowestSourceY(int x, int z, int defaultValue) {
/*  43 */     ChunkSkyLightSources sources = getChunkSources(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));
/*  44 */     if (sources == null)
/*  45 */       return defaultValue; 
/*  47 */     return sources.getLowestSourceY(SectionPos.sectionRelative(x), SectionPos.sectionRelative(z));
/*     */   }
/*     */   
/*     */   private ChunkSkyLightSources getChunkSources(int chunkX, int chunkZ) {
/*  51 */     LightChunk chunk = this.chunkSource.getChunkForLighting(chunkX, chunkZ);
/*  52 */     return (chunk != null) ? chunk.getSkyLightSources() : null;
/*     */   }
/*     */   
/*     */   protected void checkNode(long blockNode) {
/*  57 */     int x = BlockPos.getX(blockNode);
/*  58 */     int y = BlockPos.getY(blockNode);
/*  59 */     int z = BlockPos.getZ(blockNode);
/*  60 */     long sectionNode = SectionPos.blockToSection(blockNode);
/*  62 */     int lowestSourceY = this.storage.lightOnInSection(sectionNode) ? getLowestSourceY(x, z, 2147483647) : Integer.MAX_VALUE;
/*  63 */     if (lowestSourceY != Integer.MAX_VALUE)
/*  64 */       updateSourcesInColumn(x, z, lowestSourceY); 
/*  67 */     if (!this.storage.storingLightForSection(sectionNode))
/*     */       return; 
/*  71 */     boolean isSource = (y >= lowestSourceY);
/*  72 */     if (isSource) {
/*  73 */       enqueueDecrease(blockNode, REMOVE_SKY_SOURCE_ENTRY);
/*  74 */       enqueueIncrease(blockNode, ADD_SKY_SOURCE_ENTRY);
/*     */     } else {
/*  76 */       int oldLevel = this.storage.getStoredLevel(blockNode);
/*  77 */       if (oldLevel > 0) {
/*  78 */         this.storage.setStoredLevel(blockNode, 0);
/*  79 */         enqueueDecrease(blockNode, LightEngine.QueueEntry.decreaseAllDirections(oldLevel));
/*     */       } else {
/*  81 */         enqueueDecrease(blockNode, PULL_LIGHT_IN_ENTRY);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateSourcesInColumn(int x, int z, int lowestSourceY) {
/*  87 */     int worldBottomY = SectionPos.sectionToBlockCoord(this.storage.getBottomSectionY());
/*  88 */     removeSourcesBelow(x, z, lowestSourceY, worldBottomY);
/*  89 */     addSourcesAbove(x, z, lowestSourceY, worldBottomY);
/*     */   }
/*     */   
/*     */   private void removeSourcesBelow(int x, int z, int lowestSourceY, int worldBottomY) {
/*  93 */     if (lowestSourceY <= worldBottomY)
/*     */       return; 
/*  97 */     int sectionX = SectionPos.blockToSectionCoord(x);
/*  98 */     int sectionZ = SectionPos.blockToSectionCoord(z);
/* 100 */     int startY = lowestSourceY - 1;
/* 102 */     int sectionY = SectionPos.blockToSectionCoord(startY);
/* 103 */     while (this.storage.hasLightDataAtOrBelow(sectionY)) {
/* 104 */       if (this.storage.storingLightForSection(SectionPos.asLong(sectionX, sectionY, sectionZ))) {
/* 105 */         int sectionBottomY = SectionPos.sectionToBlockCoord(sectionY);
/* 106 */         int sectionTopY = sectionBottomY + 15;
/* 107 */         for (int y = Math.min(sectionTopY, startY); y >= sectionBottomY; y--) {
/* 108 */           long blockNode = BlockPos.asLong(x, y, z);
/* 109 */           if (!isSourceLevel(this.storage.getStoredLevel(blockNode)))
/*     */             return; 
/* 112 */           this.storage.setStoredLevel(blockNode, 0);
/* 114 */           enqueueDecrease(blockNode, (y == lowestSourceY - 1) ? REMOVE_TOP_SKY_SOURCE_ENTRY : REMOVE_SKY_SOURCE_ENTRY);
/*     */         } 
/*     */       } 
/* 117 */       sectionY--;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addSourcesAbove(int x, int z, int lowestSourceY, int worldBottomY) {
/* 122 */     int sectionX = SectionPos.blockToSectionCoord(x);
/* 123 */     int sectionZ = SectionPos.blockToSectionCoord(z);
/* 125 */     int neighborLowestSourceY = Math.max(
/* 126 */         Math.max(getLowestSourceY(x - 1, z, -2147483648), getLowestSourceY(x + 1, z, -2147483648)), 
/* 127 */         Math.max(getLowestSourceY(x, z - 1, -2147483648), getLowestSourceY(x, z + 1, -2147483648)));
/* 130 */     int startY = Math.max(lowestSourceY, worldBottomY);
/* 131 */     long sectionNode = SectionPos.asLong(sectionX, SectionPos.blockToSectionCoord(startY), sectionZ);
/* 132 */     while (!this.storage.isAboveData(sectionNode)) {
/* 133 */       if (this.storage.storingLightForSection(sectionNode)) {
/* 134 */         int sectionBottomY = SectionPos.sectionToBlockCoord(SectionPos.y(sectionNode));
/* 135 */         int sectionTopY = sectionBottomY + 15;
/* 136 */         for (int y = Math.max(sectionBottomY, startY); y <= sectionTopY; y++) {
/* 137 */           long blockNode = BlockPos.asLong(x, y, z);
/* 138 */           if (isSourceLevel(this.storage.getStoredLevel(blockNode)))
/*     */             return; 
/* 141 */           this.storage.setStoredLevel(blockNode, 15);
/* 142 */           if (y < neighborLowestSourceY || y == lowestSourceY)
/* 144 */             enqueueIncrease(blockNode, ADD_SKY_SOURCE_ENTRY); 
/*     */         } 
/*     */       } 
/* 148 */       sectionNode = SectionPos.offset(sectionNode, Direction.UP);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void propagateIncrease(long fromNode, long increaseData, int fromLevel) {
/* 154 */     BlockState fromState = null;
/* 155 */     int emptySectionsBelow = countEmptySectionsBelowIfAtBorder(fromNode);
/* 156 */     for (Direction propagationDirection : PROPAGATION_DIRECTIONS) {
/* 157 */       if (LightEngine.QueueEntry.shouldPropagateInDirection(increaseData, propagationDirection)) {
/* 160 */         long toNode = BlockPos.offset(fromNode, propagationDirection);
/* 161 */         if (this.storage.storingLightForSection(SectionPos.blockToSection(toNode))) {
/* 165 */           int toLevel = this.storage.getStoredLevel(toNode);
/* 166 */           int maxPossibleNewToLevel = fromLevel - 1;
/* 167 */           if (maxPossibleNewToLevel > toLevel) {
/* 171 */             this.mutablePos.set(toNode);
/* 172 */             BlockState toState = getState((BlockPos)this.mutablePos);
/* 173 */             int newToLevel = fromLevel - getOpacity(toState);
/* 174 */             if (newToLevel > toLevel) {
/* 178 */               if (fromState == null)
/* 179 */                 fromState = LightEngine.QueueEntry.isFromEmptyShape(increaseData) ? Blocks.AIR.defaultBlockState() : getState((BlockPos)this.mutablePos.set(fromNode)); 
/* 181 */               if (!shapeOccludes(fromState, toState, propagationDirection)) {
/* 182 */                 this.storage.setStoredLevel(toNode, newToLevel);
/* 183 */                 if (newToLevel > 1)
/* 184 */                   enqueueIncrease(toNode, LightEngine.QueueEntry.increaseSkipOneDirection(newToLevel, isEmptyShape(toState), propagationDirection.getOpposite())); 
/* 186 */                 propagateFromEmptySections(toNode, propagationDirection, newToLevel, true, emptySectionsBelow);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void propagateDecrease(long fromNode, long decreaseData) {
/* 193 */     int emptySectionsBelow = countEmptySectionsBelowIfAtBorder(fromNode);
/* 194 */     int oldFromLevel = LightEngine.QueueEntry.getFromLevel(decreaseData);
/* 195 */     for (Direction propagationDirection : PROPAGATION_DIRECTIONS) {
/* 196 */       if (LightEngine.QueueEntry.shouldPropagateInDirection(decreaseData, propagationDirection)) {
/* 199 */         long toNode = BlockPos.offset(fromNode, propagationDirection);
/* 200 */         if (this.storage.storingLightForSection(SectionPos.blockToSection(toNode))) {
/* 204 */           int toLevel = this.storage.getStoredLevel(toNode);
/* 205 */           if (toLevel != 0)
/* 209 */             if (toLevel <= oldFromLevel - 1) {
/* 210 */               this.storage.setStoredLevel(toNode, 0);
/* 211 */               enqueueDecrease(toNode, LightEngine.QueueEntry.decreaseSkipOneDirection(toLevel, propagationDirection.getOpposite()));
/* 212 */               propagateFromEmptySections(toNode, propagationDirection, toLevel, false, emptySectionsBelow);
/*     */             } else {
/* 214 */               enqueueIncrease(toNode, LightEngine.QueueEntry.increaseOnlyOneDirection(toLevel, false, propagationDirection.getOpposite()));
/*     */             }  
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int countEmptySectionsBelowIfAtBorder(long blockNode) {
/* 224 */     int y = BlockPos.getY(blockNode);
/* 225 */     int localY = SectionPos.sectionRelative(y);
/* 226 */     if (localY != 0)
/* 227 */       return 0; 
/* 229 */     int x = BlockPos.getX(blockNode);
/* 230 */     int z = BlockPos.getZ(blockNode);
/* 231 */     int localX = SectionPos.sectionRelative(x);
/* 232 */     int localZ = SectionPos.sectionRelative(z);
/* 233 */     if (localX == 0 || localX == 15 || localZ == 0 || localZ == 15) {
/* 234 */       int sectionX = SectionPos.blockToSectionCoord(x);
/* 235 */       int sectionY = SectionPos.blockToSectionCoord(y);
/* 236 */       int sectionZ = SectionPos.blockToSectionCoord(z);
/* 237 */       int emptySectionsBelow = 0;
/* 238 */       while (!this.storage.storingLightForSection(SectionPos.asLong(sectionX, sectionY - emptySectionsBelow - 1, sectionZ)) && this.storage.hasLightDataAtOrBelow(sectionY - emptySectionsBelow - 1))
/* 239 */         emptySectionsBelow++; 
/* 241 */       return emptySectionsBelow;
/*     */     } 
/* 243 */     return 0;
/*     */   }
/*     */   
/*     */   private void propagateFromEmptySections(long toNode, Direction propagationDirection, int toLevel, boolean increase, int emptySectionsBelow) {
/* 247 */     if (emptySectionsBelow == 0)
/*     */       return; 
/* 251 */     int x = BlockPos.getX(toNode);
/* 252 */     int z = BlockPos.getZ(toNode);
/* 253 */     if (!crossedSectionEdge(propagationDirection, SectionPos.sectionRelative(x), SectionPos.sectionRelative(z)))
/*     */       return; 
/* 257 */     int y = BlockPos.getY(toNode);
/* 258 */     int sectionX = SectionPos.blockToSectionCoord(x);
/* 259 */     int sectionZ = SectionPos.blockToSectionCoord(z);
/* 260 */     int sectionY = SectionPos.blockToSectionCoord(y) - 1;
/* 262 */     int bottomSectionY = sectionY - emptySectionsBelow + 1;
/* 263 */     while (sectionY >= bottomSectionY) {
/* 264 */       if (!this.storage.storingLightForSection(SectionPos.asLong(sectionX, sectionY, sectionZ))) {
/* 265 */         sectionY--;
/*     */         continue;
/*     */       } 
/* 268 */       int sectionMinY = SectionPos.sectionToBlockCoord(sectionY);
/* 269 */       for (int localY = 15; localY >= 0; localY--) {
/* 270 */         long blockNode = BlockPos.asLong(x, sectionMinY + localY, z);
/* 271 */         if (increase) {
/* 272 */           this.storage.setStoredLevel(blockNode, toLevel);
/* 273 */           if (toLevel > 1)
/* 275 */             enqueueIncrease(blockNode, LightEngine.QueueEntry.increaseSkipOneDirection(toLevel, true, propagationDirection.getOpposite())); 
/*     */         } else {
/* 278 */           this.storage.setStoredLevel(blockNode, 0);
/* 279 */           enqueueDecrease(blockNode, LightEngine.QueueEntry.decreaseSkipOneDirection(toLevel, propagationDirection.getOpposite()));
/*     */         } 
/*     */       } 
/* 282 */       sectionY--;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean crossedSectionEdge(Direction propagationDirection, int x, int z) {
/* 287 */     switch (propagationDirection) {
/*     */       case NORTH:
/* 287 */         return 
/* 288 */           (z == 15);
/*     */       case SOUTH:
/* 289 */         return (z == 0);
/*     */       case WEST:
/* 290 */         return (x == 15);
/*     */       case EAST:
/* 291 */         return (x == 0);
/*     */     } 
/*     */     return false;
/*     */   }
/*     */   
/*     */   public void setLightEnabled(ChunkPos pos, boolean enable) {
/* 298 */     super.setLightEnabled(pos, enable);
/* 302 */     if (enable) {
/* 303 */       ChunkSkyLightSources sources = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(pos.x(), pos.z()), this.emptyChunkSources);
/* 304 */       int highestNonSourceY = sources.getHighestLowestSourceY() - 1;
/* 305 */       int lowestFullySourceSectionY = SectionPos.blockToSectionCoord(highestNonSourceY) + 1;
/* 307 */       long zeroNode = SectionPos.getZeroNode(pos.x(), pos.z());
/* 308 */       int topSectionY = this.storage.getTopSectionY(zeroNode);
/* 309 */       int bottomSectionY = Math.max(this.storage.getBottomSectionY(), lowestFullySourceSectionY);
/* 310 */       for (int sectionY = topSectionY - 1; sectionY >= bottomSectionY; sectionY--) {
/* 311 */         DataLayer dataLayer = this.storage.getDataLayerToWrite(SectionPos.asLong(pos.x(), sectionY, pos.z()));
/* 312 */         if (dataLayer != null && dataLayer.isEmpty())
/* 313 */           dataLayer.fill(15); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void propagateLightSources(ChunkPos pos) {
/* 321 */     long zeroNode = SectionPos.getZeroNode(pos.x(), pos.z());
/* 322 */     this.storage.setLightEnabled(zeroNode, true);
/* 324 */     ChunkSkyLightSources sources = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(pos.x(), pos.z()), this.emptyChunkSources);
/* 325 */     ChunkSkyLightSources northSources = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(pos.x(), pos.z() - 1), this.emptyChunkSources);
/* 326 */     ChunkSkyLightSources southSources = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(pos.x(), pos.z() + 1), this.emptyChunkSources);
/* 327 */     ChunkSkyLightSources westSources = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(pos.x() - 1, pos.z()), this.emptyChunkSources);
/* 328 */     ChunkSkyLightSources eastSources = Objects.<ChunkSkyLightSources>requireNonNullElse(getChunkSources(pos.x() + 1, pos.z()), this.emptyChunkSources);
/* 330 */     int topSectionY = this.storage.getTopSectionY(zeroNode);
/* 331 */     int bottomSectionY = this.storage.getBottomSectionY();
/* 333 */     int sectionMinX = SectionPos.sectionToBlockCoord(pos.x());
/* 334 */     int sectionMinZ = SectionPos.sectionToBlockCoord(pos.z());
/* 336 */     for (int sectionY = topSectionY - 1; sectionY >= bottomSectionY; sectionY--) {
/* 337 */       long sectionNode = SectionPos.asLong(pos.x(), sectionY, pos.z());
/* 338 */       DataLayer dataLayer = this.storage.getDataLayerToWrite(sectionNode);
/* 339 */       if (dataLayer != null) {
/* 343 */         int sectionMinY = SectionPos.sectionToBlockCoord(sectionY);
/* 344 */         int sectionMaxY = sectionMinY + 15;
/* 346 */         boolean sourcesBelow = false;
/* 348 */         for (int z = 0; z < 16; z++) {
/* 349 */           for (int x = 0; x < 16; x++) {
/* 350 */             int lowestSourceY = sources.getLowestSourceY(x, z);
/* 351 */             if (lowestSourceY <= sectionMaxY) {
/* 355 */               int northLowestSourceY = (z == 0) ? northSources.getLowestSourceY(x, 15) : sources.getLowestSourceY(x, z - 1);
/* 356 */               int southLowestSourceY = (z == 15) ? southSources.getLowestSourceY(x, 0) : sources.getLowestSourceY(x, z + 1);
/* 357 */               int westLowestSourceY = (x == 0) ? westSources.getLowestSourceY(15, z) : sources.getLowestSourceY(x - 1, z);
/* 358 */               int eastLowestSourceY = (x == 15) ? eastSources.getLowestSourceY(0, z) : sources.getLowestSourceY(x + 1, z);
/* 359 */               int neighborLowestSourceY = Math.max(
/* 360 */                   Math.max(northLowestSourceY, southLowestSourceY), 
/* 361 */                   Math.max(westLowestSourceY, eastLowestSourceY));
/* 364 */               for (int y = sectionMaxY; y >= Math.max(sectionMinY, lowestSourceY); y--) {
/* 365 */                 dataLayer.set(x, SectionPos.sectionRelative(y), z, 15);
/* 366 */                 if (y == lowestSourceY || y < neighborLowestSourceY) {
/* 367 */                   long blockNode = BlockPos.asLong(sectionMinX + x, y, sectionMinZ + z);
/* 368 */                   enqueueIncrease(blockNode, LightEngine.QueueEntry.increaseSkySourceInDirections((y == lowestSourceY), (y < northLowestSourceY), (y < southLowestSourceY), (y < westLowestSourceY), (y < eastLowestSourceY)));
/*     */                 } 
/*     */               } 
/* 378 */               if (lowestSourceY < sectionMinY)
/* 379 */                 sourcesBelow = true; 
/*     */             } 
/*     */           } 
/*     */         } 
/* 384 */         if (!sourcesBelow)
/*     */           break; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\lighting\SkyLightEngine.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */