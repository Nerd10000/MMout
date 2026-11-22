/*     */ package net.minecraft.data;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import net.minecraft.WorldVersion;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class DataGenerator {
/*  18 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   protected final PackOutput vanillaPackOutput;
/*     */   
/*  22 */   protected final Set<String> allProviderIds = new HashSet<>();
/*     */   
/*  23 */   protected final Map<String, DataProvider> providersToRun = new LinkedHashMap<>();
/*     */   
/*     */   static {
/*  26 */     Bootstrap.bootStrap();
/*     */   }
/*     */   
/*     */   public DataGenerator(Path output) {
/*  30 */     this.vanillaPackOutput = new PackOutput(output);
/*     */   }
/*     */   
/*     */   public PackGenerator getVanillaPack(boolean toRun) {
/*  36 */     return new PackGenerator(toRun, "vanilla", this.vanillaPackOutput);
/*     */   }
/*     */   
/*     */   public PackGenerator getBuiltinDatapack(boolean toRun, String packId) {
/*  42 */     Path packOutputDir = this.vanillaPackOutput.getOutputFolder(PackOutput.Target.DATA_PACK).resolve("minecraft").resolve("datapacks").resolve(packId);
/*  43 */     return new PackGenerator(toRun, packId, new PackOutput(packOutputDir));
/*     */   }
/*     */   
/*     */   public abstract void run() throws IOException;
/*     */   
/*     */   public static class Cached extends DataGenerator {
/*     */     private final Path rootOutputFolder;
/*     */     
/*     */     private final WorldVersion version;
/*     */     
/*     */     private final boolean alwaysGenerate;
/*     */     
/*     */     public Cached(Path output, WorldVersion version, boolean alwaysGenerate) {
/*  52 */       super(output);
/*  53 */       this.rootOutputFolder = output;
/*  54 */       this.alwaysGenerate = alwaysGenerate;
/*  55 */       this.version = version;
/*     */     }
/*     */     
/*     */     public void run() throws IOException {
/*  60 */       HashCache cache = new HashCache(this.rootOutputFolder, this.allProviderIds, this.version);
/*  62 */       Stopwatch totalTime = Stopwatch.createStarted();
/*  63 */       Stopwatch stopwatch = Stopwatch.createUnstarted();
/*  64 */       this.providersToRun.forEach((providerId, provider) -> {
/*     */             if (!this.alwaysGenerate && !cache.shouldRunInThisVersion(providerId)) {
/*     */               DataGenerator.LOGGER.debug("Generator {} already run for version {}", providerId, this.version.name());
/*     */               return;
/*     */             } 
/*     */             DataGenerator.LOGGER.info("Starting provider: {}", providerId);
/*     */             stopwatch.start();
/*     */             Objects.requireNonNull(provider);
/*     */             cache.applyUpdate(cache.generateUpdate(providerId, provider::run).join());
/*     */             stopwatch.stop();
/*     */             DataGenerator.LOGGER.info("{} finished after {} ms", providerId, Long.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
/*     */             stopwatch.reset();
/*     */           });
/*  76 */       DataGenerator.LOGGER.info("All providers took: {} ms", Long.valueOf(totalTime.elapsed(TimeUnit.MILLISECONDS)));
/*  78 */       cache.purgeStaleAndWrite();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Uncached extends DataGenerator {
/*     */     public Uncached(Path output) {
/*  85 */       super(output);
/*     */     }
/*     */     
/*     */     public void run() throws IOException {
/*  90 */       Stopwatch totalTime = Stopwatch.createStarted();
/*  91 */       Stopwatch stopwatch = Stopwatch.createUnstarted();
/*  92 */       this.providersToRun.forEach((providerId, provider) -> {
/*     */             DataGenerator.LOGGER.info("Starting uncached provider: {}", providerId);
/*     */             stopwatch.start();
/*     */             provider.run(CachedOutput.NO_CACHE).join();
/*     */             stopwatch.stop();
/*     */             DataGenerator.LOGGER.info("{} finished after {} ms", providerId, Long.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
/*     */             stopwatch.reset();
/*     */           });
/* 101 */       DataGenerator.LOGGER.info("All providers took: {} ms", Long.valueOf(totalTime.elapsed(TimeUnit.MILLISECONDS)));
/*     */     }
/*     */   }
/*     */   
/*     */   public class PackGenerator {
/*     */     private final boolean toRun;
/*     */     
/*     */     private final String providerPrefix;
/*     */     
/*     */     private final PackOutput output;
/*     */     
/*     */     private PackGenerator(boolean toRun, String providerPrefix, PackOutput output) {
/* 111 */       this.toRun = toRun;
/* 112 */       this.providerPrefix = providerPrefix;
/* 113 */       this.output = output;
/*     */     }
/*     */     
/*     */     public <T extends DataProvider> T addProvider(DataProvider.Factory<T> factory) {
/* 117 */       T provider = factory.create(this.output);
/* 118 */       String providerId = this.providerPrefix + "/" + this.providerPrefix;
/* 119 */       if (!DataGenerator.this.allProviderIds.add(providerId))
/* 120 */         throw new IllegalStateException("Duplicate provider: " + providerId); 
/* 122 */       if (this.toRun)
/* 123 */         DataGenerator.this.providersToRun.put(providerId, (DataProvider)provider); 
/* 125 */       return provider;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\DataGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */