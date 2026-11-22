/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.EmptyLevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ClientChunkCache extends ChunkSource {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final LevelChunk emptyChunk;
/*     */   
/*     */   private final LevelLightEngine lightEngine;
/*     */   
/*     */   private volatile Storage storage;
/*     */   
/*     */   private final ClientLevel level;
/*     */   
/*     */   public ClientChunkCache(ClientLevel level, int serverChunkRadius) {
/*  41 */     this.level = level;
/*  42 */     this.emptyChunk = (LevelChunk)new EmptyLevelChunk(level, new ChunkPos(0, 0), (Holder)level.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS));
/*  43 */     this.lightEngine = new LevelLightEngine((LightChunkGetter)this, true, level.dimensionType().hasSkyLight());
/*  44 */     this.storage = new Storage(calculateStorageRange(serverChunkRadius));
/*     */   }
/*     */   
/*     */   public LevelLightEngine getLightEngine() {
/*  49 */     return this.lightEngine;
/*     */   }
/*     */   
/*     */   private static boolean isValidChunk(LevelChunk chunk, int x, int z) {
/*  53 */     if (chunk == null)
/*  54 */       return false; 
/*  56 */     ChunkPos pos = chunk.getPos();
/*  57 */     return (pos.x() == x && pos.z() == z);
/*     */   }
/*     */   
/*     */   public void drop(ChunkPos pos) {
/*  61 */     if (!this.storage.inRange(pos.x(), pos.z()))
/*     */       return; 
/*  64 */     int index = this.storage.getIndex(pos.x(), pos.z());
/*  65 */     LevelChunk currentChunk = this.storage.getChunk(index);
/*  66 */     if (isValidChunk(currentChunk, pos.x(), pos.z()))
/*  67 */       this.storage.drop(index, currentChunk); 
/*     */   }
/*     */   
/*     */   public LevelChunk getChunk(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/*  73 */     if (this.storage.inRange(x, z)) {
/*  74 */       LevelChunk chunk = this.storage.getChunk(this.storage.getIndex(x, z));
/*  75 */       if (isValidChunk(chunk, x, z))
/*  76 */         return chunk; 
/*     */     } 
/*  80 */     if (loadOrGenerate)
/*  81 */       return this.emptyChunk; 
/*  83 */     return null;
/*     */   }
/*     */   
/*     */   public BlockGetter getLevel() {
/*  88 */     return (BlockGetter)this.level;
/*     */   }
/*     */   
/*     */   public void replaceBiomes(int chunkX, int chunkZ, FriendlyByteBuf readBuffer) {
/*  92 */     if (!this.storage.inRange(chunkX, chunkZ)) {
/*  93 */       LOGGER.warn("Ignoring chunk since it's not in the view range: {}, {}", Integer.valueOf(chunkX), Integer.valueOf(chunkZ));
/*     */       return;
/*     */     } 
/*  96 */     int index = this.storage.getIndex(chunkX, chunkZ);
/*  98 */     LevelChunk chunk = this.storage.chunks.get(index);
/*  99 */     if (!isValidChunk(chunk, chunkX, chunkZ)) {
/* 100 */       LOGGER.warn("Ignoring chunk since it's not present: {}, {}", Integer.valueOf(chunkX), Integer.valueOf(chunkZ));
/*     */     } else {
/* 102 */       chunk.replaceBiomes(readBuffer);
/*     */     } 
/*     */   }
/*     */   
/*     */   public LevelChunk replaceWithPacketData(int chunkX, int chunkZ, FriendlyByteBuf readBuffer, Map<Heightmap.Types, long[]> heightmaps, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> blockEntities) {
/* 107 */     if (!this.storage.inRange(chunkX, chunkZ)) {
/* 108 */       LOGGER.warn("Ignoring chunk since it's not in the view range: {}, {}", Integer.valueOf(chunkX), Integer.valueOf(chunkZ));
/* 109 */       return null;
/*     */     } 
/* 111 */     int index = this.storage.getIndex(chunkX, chunkZ);
/* 113 */     LevelChunk chunk = this.storage.chunks.get(index);
/* 114 */     ChunkPos pos = new ChunkPos(chunkX, chunkZ);
/* 115 */     if (!isValidChunk(chunk, chunkX, chunkZ)) {
/* 117 */       chunk = new LevelChunk(this.level, pos);
/* 118 */       chunk.replaceWithPacketData(readBuffer, heightmaps, blockEntities);
/* 119 */       this.storage.replace(index, chunk);
/*     */     } else {
/* 121 */       chunk.replaceWithPacketData(readBuffer, heightmaps, blockEntities);
/* 122 */       this.storage.refreshEmptySections(chunk);
/*     */     } 
/* 124 */     this.level.onChunkLoaded(pos);
/* 125 */     return chunk;
/*     */   }
/*     */   
/*     */   public void tick(BooleanSupplier haveTime, boolean tickChunks) {}
/*     */   
/*     */   public void updateViewCenter(int x, int z) {
/* 133 */     this.storage.viewCenterX = x;
/* 134 */     this.storage.viewCenterZ = z;
/*     */   }
/*     */   
/*     */   public void updateViewRadius(int viewRange) {
/* 138 */     int chunkRadius = this.storage.chunkRadius;
/* 139 */     int newChunkRadius = calculateStorageRange(viewRange);
/* 141 */     if (chunkRadius != newChunkRadius) {
/* 142 */       Storage newStorage = new Storage(newChunkRadius);
/* 143 */       newStorage.viewCenterX = this.storage.viewCenterX;
/* 144 */       newStorage.viewCenterZ = this.storage.viewCenterZ;
/* 145 */       for (int i = 0; i < this.storage.chunks.length(); i++) {
/* 146 */         LevelChunk chunk = this.storage.chunks.get(i);
/* 147 */         if (chunk != null) {
/* 148 */           ChunkPos pos = chunk.getPos();
/* 149 */           if (newStorage.inRange(pos.x(), pos.z()))
/* 150 */             newStorage.replace(newStorage.getIndex(pos.x(), pos.z()), chunk); 
/*     */         } 
/*     */       } 
/* 154 */       this.storage = newStorage;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int calculateStorageRange(int viewRange) {
/* 160 */     return Math.max(2, viewRange) + 3;
/*     */   }
/*     */   
/*     */   public String gatherStats() {
/* 165 */     return "" + this.storage.chunks.length() + ", " + this.storage.chunks.length();
/*     */   }
/*     */   
/*     */   public int getLoadedChunksCount() {
/* 170 */     return this.storage.chunkCount;
/*     */   }
/*     */   
/*     */   public void onLightUpdate(LightLayer layer, SectionPos pos) {
/* 175 */     (Minecraft.getInstance()).levelExtractor.setSectionDirty(pos.x(), pos.y(), pos.z());
/*     */   }
/*     */   
/*     */   public LongOpenHashSet addedEmptySections() {
/* 179 */     return this.storage.addedEmptySections[this.storage.updatingSetsIndex];
/*     */   }
/*     */   
/*     */   public LongOpenHashSet removedEmptySections() {
/* 183 */     return this.storage.removedEmptySections[this.storage.updatingSetsIndex];
/*     */   }
/*     */   
/*     */   public LongOpenHashSet addedLoadedChunks() {
/* 187 */     return this.storage.addedLoadedChunks[this.storage.updatingSetsIndex];
/*     */   }
/*     */   
/*     */   public LongOpenHashSet removedLoadedChunks() {
/* 191 */     return this.storage.removedLoadedChunks[this.storage.updatingSetsIndex];
/*     */   }
/*     */   
/*     */   public void flipUpdateTrackingSets() {
/* 195 */     this.storage.updatingSetsIndex = (this.storage.updatingSetsIndex + 1) % 2;
/* 196 */     this.storage.addedEmptySections[this.storage.updatingSetsIndex].clear();
/* 197 */     this.storage.removedEmptySections[this.storage.updatingSetsIndex].clear();
/* 198 */     this.storage.addedLoadedChunks[this.storage.updatingSetsIndex].clear();
/* 199 */     this.storage.removedLoadedChunks[this.storage.updatingSetsIndex].clear();
/*     */   }
/*     */   
/*     */   public void onSectionEmptinessChanged(int sectionX, int sectionY, int sectionZ, boolean empty) {
/* 204 */     this.storage.onSectionEmptinessChanged(sectionX, sectionY, sectionZ, empty);
/*     */   }
/*     */   
/*     */   private final class Storage {
/*     */     private static final int UPDATE_TRACKING_BUFFERS = 2;
/*     */     
/*     */     private final AtomicReferenceArray<LevelChunk> chunks;
/*     */     
/* 210 */     private final LongOpenHashSet[] addedEmptySections = new LongOpenHashSet[2];
/*     */     
/* 211 */     private final LongOpenHashSet[] removedEmptySections = new LongOpenHashSet[2];
/*     */     
/* 212 */     private final LongOpenHashSet[] addedLoadedChunks = new LongOpenHashSet[2];
/*     */     
/* 213 */     private final LongOpenHashSet[] removedLoadedChunks = new LongOpenHashSet[2];
/*     */     
/*     */     private int updatingSetsIndex;
/*     */     
/*     */     private final int chunkRadius;
/*     */     
/*     */     private final int viewRange;
/*     */     
/*     */     private volatile int viewCenterX;
/*     */     
/*     */     private volatile int viewCenterZ;
/*     */     
/*     */     private int chunkCount;
/*     */     
/*     */     private Storage(int chunkRadius) {
/* 222 */       this.chunkRadius = chunkRadius;
/* 223 */       this.viewRange = chunkRadius * 2 + 1;
/* 224 */       this.chunks = new AtomicReferenceArray<>(this.viewRange * this.viewRange);
/* 225 */       for (int i = 0; i < 2; i++) {
/* 226 */         this.addedEmptySections[i] = new LongOpenHashSet();
/* 227 */         this.removedEmptySections[i] = new LongOpenHashSet();
/* 228 */         this.addedLoadedChunks[i] = new LongOpenHashSet();
/* 229 */         this.removedLoadedChunks[i] = new LongOpenHashSet();
/*     */       } 
/*     */     }
/*     */     
/*     */     private int getIndex(int chunkX, int chunkZ) {
/* 234 */       return Math.floorMod(chunkZ, this.viewRange) * this.viewRange + Math.floorMod(chunkX, this.viewRange);
/*     */     }
/*     */     
/*     */     private void replace(int index, LevelChunk newChunk) {
/* 238 */       LevelChunk removedChunk = this.chunks.getAndSet(index, newChunk);
/* 239 */       if (removedChunk != null) {
/* 240 */         this.chunkCount--;
/* 241 */         onChunkRemoved(removedChunk);
/* 242 */         ClientChunkCache.this.level.unload(removedChunk);
/*     */       } 
/* 245 */       if (newChunk != null) {
/* 246 */         this.chunkCount++;
/* 247 */         onChunkAdded(newChunk);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void drop(int index, LevelChunk oldChunk) {
/* 252 */       if (this.chunks.compareAndSet(index, oldChunk, null)) {
/* 253 */         this.chunkCount--;
/* 254 */         onChunkRemoved(oldChunk);
/*     */       } 
/* 256 */       ClientChunkCache.this.level.unload(oldChunk);
/*     */     }
/*     */     
/*     */     public void onSectionEmptinessChanged(int sectionX, int sectionY, int sectionZ, boolean empty) {
/* 260 */       if (!inRange(sectionX, sectionZ))
/*     */         return; 
/* 263 */       long sectionNode = SectionPos.asLong(sectionX, sectionY, sectionZ);
/* 264 */       if (empty) {
/* 265 */         this.addedEmptySections[this.updatingSetsIndex].add(sectionNode);
/*     */       } else {
/* 267 */         this.removedEmptySections[this.updatingSetsIndex].add(sectionNode);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void onChunkRemoved(LevelChunk chunk) {
/* 272 */       ChunkPos chunkPos = chunk.getPos();
/* 273 */       this.removedLoadedChunks[this.updatingSetsIndex].add(chunkPos.pack());
/* 274 */       LevelChunkSection[] sections = chunk.getSections();
/* 275 */       for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++)
/* 276 */         this.removedEmptySections[this.updatingSetsIndex].add(SectionPos.asLong(chunkPos.x(), chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z())); 
/*     */     }
/*     */     
/*     */     private void onChunkAdded(LevelChunk chunk) {
/* 281 */       ChunkPos chunkPos = chunk.getPos();
/* 282 */       this.addedLoadedChunks[this.updatingSetsIndex].add(chunkPos.pack());
/* 283 */       LevelChunkSection[] sections = chunk.getSections();
/* 284 */       for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/* 285 */         LevelChunkSection section = sections[sectionIndex];
/* 286 */         if (section.hasOnlyAir())
/* 287 */           this.addedEmptySections[this.updatingSetsIndex].add(SectionPos.asLong(chunkPos.x(), chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z())); 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void refreshEmptySections(LevelChunk chunk) {
/* 293 */       ChunkPos chunkPos = chunk.getPos();
/* 294 */       LevelChunkSection[] sections = chunk.getSections();
/* 295 */       for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/* 296 */         LevelChunkSection section = sections[sectionIndex];
/* 297 */         long sectionNode = SectionPos.asLong(chunkPos.x(), chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z());
/* 298 */         if (section.hasOnlyAir()) {
/* 299 */           this.addedEmptySections[this.updatingSetsIndex].add(sectionNode);
/*     */         } else {
/* 301 */           this.removedEmptySections[this.updatingSetsIndex].add(sectionNode);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean inRange(int chunkX, int chunkZ) {
/* 307 */       return (Math.abs(chunkX - this.viewCenterX) <= this.chunkRadius && Math.abs(chunkZ - this.viewCenterZ) <= this.chunkRadius);
/*     */     }
/*     */     
/*     */     public LevelChunk getChunk(int index) {
/* 311 */       return this.chunks.get(index);
/*     */     }
/*     */     
/*     */     private void dumpChunks(String file) {
/*     */       try {
/* 316 */         FileOutputStream stream = new FileOutputStream(file);
/*     */         try {
/* 317 */           int chunkRadius = ClientChunkCache.this.storage.chunkRadius;
/* 318 */           for (int z = this.viewCenterZ - chunkRadius; z <= this.viewCenterZ + chunkRadius; z++) {
/* 319 */             for (int x = this.viewCenterX - chunkRadius; x <= this.viewCenterX + chunkRadius; x++) {
/* 320 */               LevelChunk chunk = ClientChunkCache.this.storage.chunks.get(ClientChunkCache.this.storage.getIndex(x, z));
/* 321 */               if (chunk != null) {
/* 322 */                 ChunkPos pos = chunk.getPos();
/* 323 */                 stream.write(("" + pos.x() + "\t" + pos.x() + "\t" + pos.z() + "\n").getBytes(StandardCharsets.UTF_8));
/*     */               } 
/*     */             } 
/*     */           } 
/* 327 */           stream.close();
/*     */         } catch (Throwable throwable) {
/*     */           try {
/*     */             stream.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           } 
/*     */           throw throwable;
/*     */         } 
/* 327 */       } catch (IOException e) {
/* 328 */         ClientChunkCache.LOGGER.error("Failed to dump chunks to file {}", file, e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ClientChunkCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */