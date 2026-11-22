/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameTestMainUtil {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String DEFAULT_UNIVERSE_DIR = "gametestserver";
/*     */   
/*     */   private static final String LEVEL_NAME = "gametestworld";
/*     */   
/*  31 */   private static final OptionParser parser = new OptionParser();
/*     */   
/*  32 */   private static final OptionSpec<String> universe = (OptionSpec<String>)parser.accepts("universe", "The path to where the test server world will be created. Any existing folder will be replaced.").withRequiredArg().defaultsTo("gametestserver", (Object[])new String[0]);
/*     */   
/*  33 */   private static final OptionSpec<File> report = (OptionSpec<File>)parser.accepts("report", "Exports results in a junit-like XML report at the given path.").withRequiredArg().ofType(File.class);
/*     */   
/*  34 */   private static final OptionSpec<String> tests = (OptionSpec<String>)parser.accepts("tests", "Which test(s) to run (namespaced ID selector using wildcards). Empty means run all.").withRequiredArg();
/*     */   
/*  35 */   private static final OptionSpec<Boolean> verify = (OptionSpec<Boolean>)parser.accepts("verify", "Runs the tests specified with `test` or `testNamespace` 100 times for each 90 degree rotation step").withRequiredArg().ofType(Boolean.class).defaultsTo(Boolean.valueOf(false), (Object[])new Boolean[0]);
/*     */   
/*  36 */   private static final OptionSpec<Integer> repeatCount = (OptionSpec<Integer>)parser.accepts("repeatCount", "Runs each of the specified tests this many times").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(1), (Object[])new Integer[0]);
/*     */   
/*  37 */   private static final OptionSpec<String> packs = (OptionSpec<String>)parser.accepts("packs", "A folder of datapacks to include in the world").withRequiredArg();
/*     */   
/*  38 */   private static final OptionSpec<Void> help = (OptionSpec<Void>)parser.accepts("help").forHelp();
/*     */   
/*     */   @SuppressForbidden(reason = "Using System.err due to no bootstrap")
/*     */   public static void runGameTestServer(String[] args, Consumer<String> onUniverseCreated) throws Exception {
/*  42 */     parser.allowsUnrecognizedOptions();
/*  44 */     OptionSet options = parser.parse(args);
/*  45 */     if (options.has(help)) {
/*  46 */       parser.printHelpOn(System.err);
/*     */       return;
/*     */     } 
/*  50 */     if (((Boolean)options.valueOf(verify)).booleanValue() && !options.has(tests)) {
/*  51 */       LOGGER.error("Please specify a test selection to run the verify option. For example: --verify --tests example:test_something_*");
/*  52 */       System.exit(-1);
/*     */     } 
/*  55 */     if (((Boolean)options.valueOf(verify)).booleanValue() && options.has(repeatCount))
/*  56 */       LOGGER.info("Flag --verify is true, the --repeatCount value will be ignored"); 
/*  61 */     LOGGER.info("Running GameTestMain with cwd '{}', universe path '{}'", System.getProperty("user.dir"), options.valueOf(universe));
/*  63 */     if (options.has(report))
/*  64 */       GlobalTestReporter.replaceWith(new JUnitLikeTestReporter((File)report.value(options))); 
/*  67 */     Bootstrap.bootStrap();
/*  68 */     Util.startTimerHackThread();
/*  70 */     String universePath = (String)options.valueOf(universe);
/*  71 */     createOrResetDir(universePath);
/*  72 */     onUniverseCreated.accept(universePath);
/*  73 */     if (options.has(packs)) {
/*  74 */       String packFolder = (String)options.valueOf(packs);
/*  75 */       copyPacks(universePath, packFolder);
/*     */     } 
/*  78 */     LevelStorageSource.LevelStorageAccess levelStorageSource = LevelStorageSource.createDefault(Paths.get(universePath, new String[0])).createAccess("gametestworld");
/*  79 */     PackRepository packRepository = ServerPacksSource.createPackRepository(levelStorageSource);
/*  80 */     MinecraftServer.spin(thread -> GameTestServer.create(thread, levelStorageSource, packRepository, optionalFromOption(options, tests), ((Boolean)options.valueOf(verify)).booleanValue(), ((Integer)options.valueOf(repeatCount)).intValue()));
/*     */   }
/*     */   
/*     */   private static Optional<String> optionalFromOption(OptionSet options, OptionSpec<String> option) {
/*  84 */     return options.has(option) ? Optional.<String>of((String)options.valueOf(option)) : Optional.<String>empty();
/*     */   }
/*     */   
/*     */   private static void createOrResetDir(String universePath) throws IOException {
/*  88 */     Path universeDir = Paths.get(universePath, new String[0]);
/*  89 */     if (Files.exists(universeDir, new java.nio.file.LinkOption[0]))
/*  90 */       FileUtils.deleteDirectory(universeDir.toFile()); 
/*  92 */     Files.createDirectories(universeDir, (FileAttribute<?>[])new FileAttribute[0]);
/*     */   }
/*     */   
/*     */   private static void copyPacks(String serverPath, String packSourcePath) throws IOException {
/*  96 */     Path worldPackFolder = Paths.get(serverPath, new String[0]).resolve("gametestworld").resolve("datapacks");
/*  97 */     if (!Files.exists(worldPackFolder, new java.nio.file.LinkOption[0]))
/*  98 */       Files.createDirectories(worldPackFolder, (FileAttribute<?>[])new FileAttribute[0]); 
/* 100 */     Path sourceFolder = Paths.get(packSourcePath, new String[0]);
/* 101 */     if (Files.exists(sourceFolder, new java.nio.file.LinkOption[0])) {
/* 102 */       Stream<Path> list = Files.list(sourceFolder);
/*     */       try {
/* 103 */         for (Path path : list.toList()) {
/* 104 */           Path destination = worldPackFolder.resolve(path.getFileName());
/* 105 */           if (Files.isDirectory(path, new java.nio.file.LinkOption[0])) {
/* 106 */             if (Files.isRegularFile(path.resolve("pack.mcmeta"), new java.nio.file.LinkOption[0])) {
/* 107 */               FileUtils.copyDirectory(path.toFile(), destination.toFile());
/* 108 */               LOGGER.info("Included folder pack {}", path.getFileName());
/*     */             } 
/*     */             continue;
/*     */           } 
/* 110 */           if (path.toString().endsWith(".zip")) {
/* 111 */             Files.copy(path, destination, new java.nio.file.CopyOption[0]);
/* 112 */             LOGGER.info("Included zip pack {}", path.getFileName());
/*     */           } 
/*     */         } 
/* 115 */         if (list != null)
/* 115 */           list.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (list != null)
/*     */           try {
/*     */             list.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestMainUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */