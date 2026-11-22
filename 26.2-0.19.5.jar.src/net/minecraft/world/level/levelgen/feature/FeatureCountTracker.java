/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.google.common.cache.CacheBuilder;
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import com.google.common.cache.LoadingCache;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FeatureCountTracker {
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private static final class FeatureData extends Record {
/*    */     private final ConfiguredFeature<?, ?> feature;
/*    */     
/*    */     private final Optional<PlacedFeature> topFeature;
/*    */     
/*    */     private FeatureData(ConfiguredFeature<?, ?> feature, Optional<PlacedFeature> topFeature) {
/* 25 */       this.feature = feature;
/* 25 */       this.topFeature = topFeature;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$FeatureData;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public ConfiguredFeature<?, ?> feature() {
/* 25 */       return this.feature;
/*    */     }
/*    */     
/*    */     public Optional<PlacedFeature> topFeature() {
/* 25 */       return this.topFeature;
/*    */     }
/*    */   }
/*    */   
/*    */   private static final class LevelData extends Record {
/*    */     private final Object2IntMap<FeatureCountTracker.FeatureData> featureData;
/*    */     
/*    */     private final MutableInt chunksWithFeatures;
/*    */     
/*    */     private LevelData(Object2IntMap<FeatureCountTracker.FeatureData> featureData, MutableInt chunksWithFeatures) {
/* 27 */       this.featureData = featureData;
/* 27 */       this.chunksWithFeatures = chunksWithFeatures;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #27	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/FeatureCountTracker$LevelData;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Object2IntMap<FeatureCountTracker.FeatureData> featureData() {
/* 27 */       return this.featureData;
/*    */     }
/*    */     
/*    */     public MutableInt chunksWithFeatures() {
/* 27 */       return this.chunksWithFeatures;
/*    */     }
/*    */   }
/*    */   
/* 29 */   private static final LoadingCache<ServerLevel, LevelData> data = CacheBuilder.newBuilder().weakKeys().expireAfterAccess(5L, TimeUnit.MINUTES).build(new CacheLoader<ServerLevel, LevelData>() {
/*    */         public FeatureCountTracker.LevelData load(ServerLevel level) {
/* 32 */           return new FeatureCountTracker.LevelData(Object2IntMaps.synchronize((Object2IntMap)new Object2IntOpenHashMap()), new MutableInt(0));
/*    */         }
/*    */       });
/*    */   
/*    */   public static void chunkDecorated(ServerLevel level) {
/*    */     try {
/* 38 */       ((LevelData)data.get(level)).chunksWithFeatures().increment();
/* 39 */     } catch (Exception e) {
/* 40 */       LOGGER.error("Failed to increment chunk count", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void featurePlaced(ServerLevel level, ConfiguredFeature<?, ?> feature, Optional<PlacedFeature> topFeature) {
/*    */     try {
/* 46 */       ((LevelData)data.get(level)).featureData().computeInt(new FeatureData(feature, topFeature), (f, old) -> Integer.valueOf((old == null) ? 1 : (old.intValue() + 1)));
/* 47 */     } catch (Exception e) {
/* 48 */       LOGGER.error("Failed to increment feature count", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void clearCounts() {
/* 53 */     data.invalidateAll();
/* 54 */     LOGGER.debug("Cleared feature counts");
/*    */   }
/*    */   
/*    */   public static void logCounts() {
/* 58 */     LOGGER.debug("Logging feature counts:");
/* 59 */     data.asMap().forEach((level, featureCounts) -> {
/*    */           String name = level.dimension().identifier().toString();
/*    */           boolean running = level.getServer().isRunning();
/*    */           Registry<PlacedFeature> featureRegistry = level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
/*    */           String prefix = (running ? "running" : "dead") + " " + (running ? "running" : "dead");
/*    */           int chunks = featureCounts.chunksWithFeatures().intValue();
/*    */           LOGGER.debug("{} total_chunks: {}", prefix, Integer.valueOf(chunks));
/*    */           featureCounts.featureData().forEach(());
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\FeatureCountTracker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */