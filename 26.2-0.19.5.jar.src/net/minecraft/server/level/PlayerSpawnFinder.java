/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class PlayerSpawnFinder {
/*  30 */   private static final EntityDimensions PLAYER_DIMENSIONS = EntityTypes.PLAYER.getDimensions();
/*     */   
/*     */   private static final int ABSOLUTE_MAX_ATTEMPTS = 1024;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final BlockPos spawnSuggestion;
/*     */   
/*     */   private final int radius;
/*     */   
/*     */   private final int candidateCount;
/*     */   
/*     */   private final int coprime;
/*     */   
/*     */   private final int offset;
/*     */   
/*     */   private int nextCandidateIndex;
/*     */   
/*  43 */   private final CompletableFuture<Vec3> finishedFuture = new CompletableFuture<>();
/*     */   
/*     */   private PlayerSpawnFinder(ServerLevel level, BlockPos spawnSuggestion, int radius) {
/*  46 */     this.level = level;
/*  47 */     this.spawnSuggestion = spawnSuggestion;
/*  48 */     this.radius = radius;
/*  49 */     long squareSide = radius * 2L + 1L;
/*  50 */     this.candidateCount = (int)Math.min(1024L, squareSide * squareSide);
/*  51 */     this.coprime = getCoprime(this.candidateCount);
/*  52 */     this.offset = RandomSource.createThreadLocalInstance().nextInt(this.candidateCount);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<Vec3> findSpawn(ServerLevel level, BlockPos spawnSuggestion) {
/*  56 */     if (level.getServer().getWorldData().getGameType() == GameType.ADVENTURE)
/*  57 */       return CompletableFuture.completedFuture(fixupSpawnHeight((CollisionGetter)level, spawnSuggestion)); 
/*  60 */     int radius = Math.max(0, ((Integer)level.getGameRules().get(GameRules.RESPAWN_RADIUS)).intValue());
/*  61 */     int distToBorder = Mth.floor(level.getWorldBorder().getDistanceToBorder(spawnSuggestion.getX(), spawnSuggestion.getZ()));
/*  62 */     if (distToBorder < radius)
/*  63 */       radius = distToBorder; 
/*  65 */     if (distToBorder <= 1)
/*  66 */       radius = 1; 
/*  69 */     PlayerSpawnFinder finder = new PlayerSpawnFinder(level, spawnSuggestion, radius);
/*  70 */     finder.scheduleNext();
/*  72 */     return finder.finishedFuture;
/*     */   }
/*     */   
/*     */   private void scheduleNext() {
/*  76 */     int candidateIndex = this.nextCandidateIndex++;
/*  77 */     if (candidateIndex < this.candidateCount) {
/*  78 */       int value = (this.offset + this.coprime * candidateIndex) % this.candidateCount;
/*  79 */       int deltaX = value % (this.radius * 2 + 1);
/*  80 */       int deltaZ = value / (this.radius * 2 + 1);
/*  81 */       int targetX = this.spawnSuggestion.getX() + deltaX - this.radius;
/*  82 */       int targetZ = this.spawnSuggestion.getZ() + deltaZ - this.radius;
/*  83 */       scheduleCandidate(targetX, targetZ, candidateIndex, () -> {
/*     */             BlockPos spawnPos = getLevelRespawnPos(this.level, targetX, targetZ);
/*  85 */             return (spawnPos != null && noCollisionNoLiquid((CollisionGetter)this.level, spawnPos)) ? Optional.of(Vec3.atBottomCenterOf((Vec3i)spawnPos)) : Optional.empty();
/*     */           });
/*     */     } else {
/*  91 */       scheduleCandidate(this.spawnSuggestion.getX(), this.spawnSuggestion.getZ(), candidateIndex, () -> Optional.of(fixupSpawnHeight((CollisionGetter)this.level, this.spawnSuggestion)));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Vec3 fixupSpawnHeight(CollisionGetter level, BlockPos spawnPos) {
/*  98 */     BlockPos.MutableBlockPos mutablePos = spawnPos.mutable();
/* 100 */     while (!noCollisionNoLiquid(level, (BlockPos)mutablePos) && mutablePos.getY() < level.getMaxY())
/* 101 */       mutablePos.move(Direction.UP); 
/* 104 */     mutablePos.move(Direction.DOWN);
/* 105 */     while (noCollisionNoLiquid(level, (BlockPos)mutablePos) && mutablePos.getY() > level.getMinY())
/* 106 */       mutablePos.move(Direction.DOWN); 
/* 108 */     mutablePos.move(Direction.UP);
/* 110 */     return Vec3.atBottomCenterOf((Vec3i)mutablePos);
/*     */   }
/*     */   
/*     */   private static boolean noCollisionNoLiquid(CollisionGetter level, BlockPos pos) {
/* 114 */     return level.noCollision(null, PLAYER_DIMENSIONS.makeBoundingBox(Vec3.atBottomCenterOf((Vec3i)pos)), true);
/*     */   }
/*     */   
/*     */   private static int getCoprime(int possibleOrigins) {
/* 119 */     return (possibleOrigins <= 16) ? (possibleOrigins - 1) : 17;
/*     */   }
/*     */   
/*     */   private void scheduleCandidate(int candidateX, int candidateZ, int candidateIndex, Supplier<Optional<Vec3>> candidateChecker) {
/* 123 */     if (this.finishedFuture.isDone())
/*     */       return; 
/* 127 */     int chunkX = SectionPos.blockToSectionCoord(candidateX);
/* 128 */     int chunkZ = SectionPos.blockToSectionCoord(candidateZ);
/* 129 */     this.level.getChunkSource().addTicketAndLoadWithRadius(TicketType.SPAWN_SEARCH, new ChunkPos(chunkX, chunkZ), 0).whenCompleteAsync((ignored, throwable) -> {
/*     */           if (throwable == null)
/*     */             try {
/*     */               Optional<Vec3> spawnPos = candidateChecker.get();
/*     */               if (spawnPos.isPresent()) {
/*     */                 this.finishedFuture.complete(spawnPos.get());
/*     */               } else {
/*     */                 scheduleNext();
/*     */               } 
/* 138 */             } catch (Throwable t) {
/*     */               throwable = t;
/*     */             }  
/*     */           if (throwable != null) {
/*     */             CrashReport report = CrashReport.forThrowable(throwable, "Searching for spawn");
/*     */             CrashReportCategory details = report.addCategory("Spawn Lookup");
/*     */             Objects.requireNonNull(this.spawnSuggestion);
/*     */             details.setDetail("Origin", this.spawnSuggestion::toString);
/*     */             details.setDetail("Radius", ());
/*     */             details.setDetail("Candidate", ());
/*     */             details.setDetail("Progress", ());
/*     */             this.finishedFuture.completeExceptionally((Throwable)new ReportedException(report));
/*     */           } 
/* 151 */         }(Executor)this.level.getServer());
/*     */   }
/*     */   
/*     */   protected static BlockPos getLevelRespawnPos(ServerLevel level, int x, int z) {
/* 155 */     boolean caveWorld = level.dimensionType().hasCeiling();
/* 159 */     LevelChunk chunk = level.getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));
/* 160 */     int topY = caveWorld ? level.getChunkSource().getGenerator().getSpawnHeight((LevelHeightAccessor)level) : chunk.getHeight(Heightmap.Types.MOTION_BLOCKING, x & 0xF, z & 0xF);
/* 163 */     if (topY < level.getMinY())
/* 164 */       return null; 
/* 168 */     int surface = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x & 0xF, z & 0xF);
/* 169 */     if (surface <= topY && surface > chunk.getHeight(Heightmap.Types.OCEAN_FLOOR, x & 0xF, z & 0xF))
/* 170 */       return null; 
/* 173 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 175 */     for (int y = topY + 1; y >= level.getMinY(); y--) {
/* 176 */       pos.set(x, y, z);
/* 177 */       BlockState blockState = level.getBlockState((BlockPos)pos);
/* 180 */       if (!blockState.getFluidState().isEmpty())
/*     */         break; 
/* 185 */       if (Block.isFaceFull(blockState.getCollisionShape((BlockGetter)level, (BlockPos)pos), Direction.UP))
/* 186 */         return pos.above().immutable(); 
/*     */     } 
/* 189 */     return null;
/*     */   }
/*     */   
/*     */   public static BlockPos getSpawnPosInChunk(ServerLevel level, ChunkPos chunkPos) {
/* 193 */     if (SharedConstants.debugVoidTerrain(chunkPos))
/* 194 */       return null; 
/* 197 */     for (int x = chunkPos.getMinBlockX(); x <= chunkPos.getMaxBlockX(); x++) {
/* 198 */       for (int z = chunkPos.getMinBlockZ(); z <= chunkPos.getMaxBlockZ(); z++) {
/* 199 */         BlockPos validSpawnPosition = getLevelRespawnPos(level, x, z);
/* 200 */         if (validSpawnPosition != null)
/* 201 */           return validSpawnPosition; 
/*     */       } 
/*     */     } 
/* 206 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\PlayerSpawnFinder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */