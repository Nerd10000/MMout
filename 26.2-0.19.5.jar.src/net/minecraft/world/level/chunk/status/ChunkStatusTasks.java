/*     */ package net.minecraft.world.level.chunk.status;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.server.level.GenerationChunkHolder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ThreadedLevelLightEngine;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ImposterProtoChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChunkStatusTasks {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static boolean isLighted(ChunkAccess chunk) {
/*  32 */     return (chunk.getPersistedStatus().isOrAfter(ChunkStatus.LIGHT) && chunk.isLightCorrect());
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> passThrough(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  36 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> generateStructureStarts(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  40 */     ServerLevel level = context.level();
/*  41 */     if (level.getServer().getWorldGenSettings().options().generateStructures())
/*  42 */       context.generator().createStructures(level.registryAccess(), level.getChunkSource().getGeneratorState(), level.structureManager(), chunk, context.structureManager(), level.dimension()); 
/*  44 */     level.onStructureStartsAvailable(chunk);
/*  45 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> loadStructureStarts(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk) {
/*  49 */     context.level().onStructureStartsAvailable(chunk);
/*  50 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> generateStructureReferences(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  54 */     ServerLevel level = context.level();
/*  55 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  56 */     context.generator().createReferences((WorldGenLevel)region, level.structureManager().forWorldGenRegion(region), chunk);
/*  57 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> generateBiomes(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  61 */     ServerLevel level = context.level();
/*  62 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  63 */     return context.generator().createBiomes(level.getChunkSource().randomState(), Blender.of(region), level.structureManager().forWorldGenRegion(region), chunk);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> generateNoise(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  67 */     ServerLevel level = context.level();
/*  68 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  69 */     return context.generator().fillFromNoise(Blender.of(region), level.getChunkSource().randomState(), level.structureManager().forWorldGenRegion(region), chunk).thenApply(generatedChunk -> {
/*     */           if (generatedChunk instanceof ProtoChunk) {
/*     */             ProtoChunk protoChunk = (ProtoChunk)generatedChunk;
/*     */             BelowZeroRetrogen belowZeroRetrogen = protoChunk.getBelowZeroRetrogen();
/*     */             if (belowZeroRetrogen != null) {
/*     */               BelowZeroRetrogen.replaceOldBedrock(protoChunk);
/*     */               if (belowZeroRetrogen.hasBedrockHoles())
/*     */                 belowZeroRetrogen.applyBedrockMask(protoChunk); 
/*     */             } 
/*     */           } 
/*     */           return generatedChunk;
/*     */         });
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> generateSurface(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  86 */     ServerLevel level = context.level();
/*  87 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  88 */     context.generator().buildSurface(region, level.structureManager().forWorldGenRegion(region), level.getChunkSource().randomState(), chunk);
/*  89 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> generateCarvers(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  93 */     ServerLevel level = context.level();
/*  94 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  95 */     if (chunk instanceof ProtoChunk) {
/*  95 */       ProtoChunk protoChunk = (ProtoChunk)chunk;
/*  96 */       Blender.addAroundOldChunksCarvingMaskFilter((WorldGenLevel)region, protoChunk);
/*     */     } 
/*  98 */     context.generator().applyCarvers(region, level.getSeed(), level.getChunkSource().randomState(), level.getBiomeManager(), level.structureManager().forWorldGenRegion(region), chunk);
/*  99 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> generateFeatures(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 103 */     ServerLevel level = context.level();
/* 104 */     Heightmap.primeHeightmaps(chunk, EnumSet.of(Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE));
/* 105 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/* 106 */     if (!SharedConstants.DEBUG_DISABLE_FEATURES)
/* 107 */       context.generator().applyBiomeDecoration((WorldGenLevel)region, chunk, level.structureManager().forWorldGenRegion(region)); 
/* 110 */     Blender.generateBorderTicks(region, chunk);
/* 111 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> initializeLight(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 115 */     ThreadedLevelLightEngine lightEngine = context.lightEngine();
/* 116 */     chunk.initializeLightSources();
/* 117 */     ((ProtoChunk)chunk).setLightEngine((LevelLightEngine)lightEngine);
/* 118 */     boolean lighted = isLighted(chunk);
/* 120 */     return lightEngine.initializeLight(chunk, lighted);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> light(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 124 */     boolean lighted = isLighted(chunk);
/* 125 */     return context.lightEngine().lightChunk(chunk, lighted);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> generateSpawn(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 130 */     if (!chunk.isUpgrading())
/* 131 */       context.generator().spawnOriginalMobs(new WorldGenRegion(context.level(), chunks, step, chunk)); 
/* 133 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   public static CompletableFuture<ChunkAccess> full(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 137 */     ChunkPos pos = chunk.getPos();
/* 138 */     GenerationChunkHolder holder = (GenerationChunkHolder)chunks.get(pos.x(), pos.z());
/* 139 */     return CompletableFuture.supplyAsync(() -> {
/*     */           LevelChunk levelChunk;
/*     */           ProtoChunk protoChunk = (ProtoChunk)chunk;
/*     */           ServerLevel level = context.level();
/*     */           if (protoChunk instanceof ImposterProtoChunk) {
/*     */             ImposterProtoChunk imposter = (ImposterProtoChunk)protoChunk;
/*     */             levelChunk = imposter.getWrapped();
/*     */           } else {
/*     */             levelChunk = new LevelChunk(level, protoChunk, ());
/*     */             holder.replaceProtoChunk(new ImposterProtoChunk(levelChunk, false));
/*     */           } 
/*     */           Objects.requireNonNull(holder);
/*     */           levelChunk.setFullStatus(holder::getFullStatus);
/*     */           levelChunk.runPostLoad();
/*     */           levelChunk.setLoaded(true);
/*     */           levelChunk.registerAllBlockEntitiesAfterLevelLoad();
/*     */           levelChunk.registerTickContainerInLevel(level);
/*     */           levelChunk.setUnsavedListener(context.unsavedListener());
/*     */           return (ChunkAccess)levelChunk;
/* 139 */         }context
/*     */         
/* 161 */         .mainThreadExecutor());
/*     */   }
/*     */   
/*     */   private static void postLoadProtoChunk(ServerLevel level, ValueInput.ValueInputList entities) {
/* 165 */     if (!entities.isEmpty())
/* 166 */       level.addWorldGenChunkEntities(EntityType.loadEntitiesRecursive(entities, (Level)level, EntitySpawnReason.LOAD)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\status\ChunkStatusTasks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */