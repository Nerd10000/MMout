/*    */ package net.minecraft.data;
/*    */ 
/*    */ import com.google.common.base.Stopwatch;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Path;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import net.minecraft.WorldVersion;
/*    */ 
/*    */ public class Cached extends DataGenerator {
/*    */   private final Path rootOutputFolder;
/*    */   
/*    */   private final WorldVersion version;
/*    */   
/*    */   private final boolean alwaysGenerate;
/*    */   
/*    */   public Cached(Path output, WorldVersion version, boolean alwaysGenerate) {
/* 52 */     super(output);
/* 53 */     this.rootOutputFolder = output;
/* 54 */     this.alwaysGenerate = alwaysGenerate;
/* 55 */     this.version = version;
/*    */   }
/*    */   
/*    */   public void run() throws IOException {
/* 60 */     HashCache cache = new HashCache(this.rootOutputFolder, this.allProviderIds, this.version);
/* 62 */     Stopwatch totalTime = Stopwatch.createStarted();
/* 63 */     Stopwatch stopwatch = Stopwatch.createUnstarted();
/* 64 */     this.providersToRun.forEach((providerId, provider) -> {
/*    */           if (!this.alwaysGenerate && !cache.shouldRunInThisVersion(providerId)) {
/*    */             DataGenerator.LOGGER.debug("Generator {} already run for version {}", providerId, this.version.name());
/*    */             return;
/*    */           } 
/*    */           DataGenerator.LOGGER.info("Starting provider: {}", providerId);
/*    */           stopwatch.start();
/*    */           Objects.requireNonNull(provider);
/*    */           cache.applyUpdate(cache.generateUpdate(providerId, provider::run).join());
/*    */           stopwatch.stop();
/*    */           DataGenerator.LOGGER.info("{} finished after {} ms", providerId, Long.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
/*    */           stopwatch.reset();
/*    */         });
/* 76 */     DataGenerator.LOGGER.info("All providers took: {} ms", Long.valueOf(totalTime.elapsed(TimeUnit.MILLISECONDS)));
/* 78 */     cache.purgeStaleAndWrite();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\DataGenerator$Cached.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */