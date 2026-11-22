/*     */ package net.minecraft.data;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ public class Uncached extends DataGenerator {
/*     */   public Uncached(Path output) {
/*  85 */     super(output);
/*     */   }
/*     */   
/*     */   public void run() throws IOException {
/*  90 */     Stopwatch totalTime = Stopwatch.createStarted();
/*  91 */     Stopwatch stopwatch = Stopwatch.createUnstarted();
/*  92 */     this.providersToRun.forEach((providerId, provider) -> {
/*     */           DataGenerator.LOGGER.info("Starting uncached provider: {}", providerId);
/*     */           stopwatch.start();
/*     */           provider.run(CachedOutput.NO_CACHE).join();
/*     */           stopwatch.stop();
/*     */           DataGenerator.LOGGER.info("{} finished after {} ms", providerId, Long.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
/*     */           stopwatch.reset();
/*     */         });
/* 101 */     DataGenerator.LOGGER.info("All providers took: {} ms", Long.valueOf(totalTime.elapsed(TimeUnit.MILLISECONDS)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\DataGenerator$Uncached.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */