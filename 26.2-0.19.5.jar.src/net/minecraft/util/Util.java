/*      */ package net.minecraft.util;
/*      */ 
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Ticker;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.Iterators;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.escape.Escaper;
/*      */ import com.google.common.escape.Escapers;
/*      */ import com.google.common.util.concurrent.ListeningExecutorService;
/*      */ import com.google.common.util.concurrent.MoreExecutors;
/*      */ import com.mojang.datafixers.DSL;
/*      */ import com.mojang.datafixers.DataFixUtils;
/*      */ import com.mojang.datafixers.Typed;
/*      */ import com.mojang.datafixers.types.Type;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.jtracy.TracyClient;
/*      */ import com.mojang.jtracy.Zone;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.Dynamic;
/*      */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectLists;
/*      */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ReferenceImmutableList;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.lang.management.ManagementFactory;
/*      */ import java.lang.management.ThreadInfo;
/*      */ import java.lang.management.ThreadMXBean;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.nio.file.CopyOption;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.spi.FileSystemProvider;
/*      */ import java.time.Duration;
/*      */ import java.time.Instant;
/*      */ import java.time.ZonedDateTime;
/*      */ import java.time.format.DateTimeFormatter;
/*      */ import java.time.format.FormatStyle;
/*      */ import java.util.Arrays;
/*      */ import java.util.EnumMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HexFormat;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.BlockingQueue;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.ForkJoinPool;
/*      */ import java.util.concurrent.ForkJoinWorkerThread;
/*      */ import java.util.concurrent.LinkedBlockingQueue;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.function.ToIntFunction;
/*      */ import java.util.function.UnaryOperator;
/*      */ import java.util.stream.Collector;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.IntStream;
/*      */ import java.util.stream.LongStream;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.CharPredicate;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.SuppressForbidden;
/*      */ import net.minecraft.TracingExecutor;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.util.datafix.DataFixers;
/*      */ import net.minecraft.util.thread.BlockableEventLoop;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class Util {
/*  101 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int DEFAULT_MAX_THREADS = 255;
/*      */   
/*      */   private static final int DEFAULT_SAFE_FILE_OPERATION_RETRIES = 10;
/*      */   
/*      */   private static final String MAX_THREADS_SYSTEM_PROPERTY = "max.bg.threads";
/*      */   
/*  105 */   private static final TracingExecutor BACKGROUND_EXECUTOR = makeExecutor("Main");
/*      */   
/*  106 */   private static final TracingExecutor IO_POOL = makeIoExecutor("IO-Worker-", false);
/*      */   
/*  107 */   private static final TracingExecutor DOWNLOAD_POOL = makeIoExecutor("Download-", true);
/*      */   
/*  108 */   private static final DateTimeFormatter FILENAME_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT);
/*      */   
/*      */   public static final int LINEAR_LOOKUP_THRESHOLD = 8;
/*      */   
/*  110 */   private static final Set<String> ALLOWED_UNTRUSTED_LINK_PROTOCOLS = Set.of("http", "https");
/*      */   
/*      */   public static final long NANOS_PER_MILLI = 1000000L;
/*      */   
/*  113 */   private static TimeSource.NanoTimeSource timeSource = System::nanoTime;
/*      */   
/*      */   private static final TimeSource.NanoTimeSource INDIRECT_TIME_SOURCE = () -> timeSource.getAsLong();
/*      */   
/*  115 */   public static final Ticker TICKER = new Ticker() {
/*      */       public long read() {
/*  118 */         return Util.timeSource.getAsLong();
/*      */       }
/*      */     };
/*      */   
/*  121 */   public static final UUID NIL_UUID = new UUID(0L, 0L);
/*      */   
/*      */   public static final FileSystemProvider ZIP_FILE_SYSTEM_PROVIDER;
/*      */   
/*      */   public static final Escaper CONTROL_CHARACTER_ESCAPER;
/*      */   
/*      */   static {
/*  126 */     ZIP_FILE_SYSTEM_PROVIDER = (FileSystemProvider)FileSystemProvider.installedProviders().stream().filter(p -> p.getScheme().equalsIgnoreCase("jar")).findFirst().orElseThrow(() -> new IllegalStateException("No jar file system provider found"));
/*  146 */     CONTROL_CHARACTER_ESCAPER = ((Escapers.Builder)make(Escapers.builder(), escaper -> {
/*      */           HexFormat hexFormat = HexFormat.of().withUpperCase();
/*      */           for (char c = Character.MIN_VALUE; c <= 'ÿ'; c = (char)(c + 1)) {
/*      */             if (Character.isISOControl(c)) {
/*      */               switch (c) {
/*      */                 case '\007':
/*      */                 
/*      */                 case '\b':
/*      */                 
/*      */                 case '\t':
/*      */                 
/*      */                 case '\n':
/*      */                 
/*      */                 case '\013':
/*      */                 
/*      */                 case '\f':
/*      */                 
/*      */                 case '\r':
/*      */                 
/*      */                 default:
/*      */                   break;
/*      */               } 
/*      */               String replacement = "\\x" + hexFormat.toHexDigits(c, 2);
/*      */               escaper.addEscape(c, replacement);
/*      */             } 
/*      */           } 
/*      */           escaper.addEscape('\\', "\\\\");
/*  146 */         })).build();
/*      */   }
/*      */   
/*      */   private static Consumer<String> thePauser = msg -> {
/*      */     
/*      */     };
/*      */   
/*      */   public static <K, V> Collector<Map.Entry<? extends K, ? extends V>, ?, Map<K, V>> toMap() {
/*  151 */     return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
/*      */   }
/*      */   
/*      */   public static <T> Collector<T, ?, List<T>> toMutableList() {
/*  155 */     return Collectors.toCollection(Lists::newArrayList);
/*      */   }
/*      */   
/*      */   public static <T extends Comparable<T>> String getPropertyName(Property<T> key, Object value) {
/*  160 */     return key.getName((Comparable)value);
/*      */   }
/*      */   
/*      */   public static String makeDescriptionId(String prefix, Identifier location) {
/*  164 */     if (location == null)
/*  165 */       return prefix + ".unregistered_sadface"; 
/*  167 */     return prefix + "." + prefix + "." + location.getNamespace();
/*      */   }
/*      */   
/*      */   public static TimeSource.NanoTimeSource timeSource() {
/*  172 */     return INDIRECT_TIME_SOURCE;
/*      */   }
/*      */   
/*      */   public static void setTimeSource(TimeSource.NanoTimeSource timeSource) {
/*  176 */     Util.timeSource = timeSource;
/*      */   }
/*      */   
/*      */   public static void shutdownTimeSource() {
/*  186 */     long currentTime = timeSource.getAsLong();
/*  187 */     timeSource = TimeSource.constant(currentTime);
/*      */   }
/*      */   
/*      */   public static long getMillis() {
/*  191 */     return getNanos() / 1000000L;
/*      */   }
/*      */   
/*      */   public static long getNanos() {
/*  195 */     return timeSource.getAsLong();
/*      */   }
/*      */   
/*      */   public static long getEpochMillis() {
/*  199 */     return Instant.now().toEpochMilli();
/*      */   }
/*      */   
/*      */   public static String getFilenameFormattedDateTime() {
/*  203 */     return FILENAME_DATE_TIME_FORMATTER.format(ZonedDateTime.now());
/*      */   }
/*      */   
/*      */   private static TracingExecutor makeExecutor(final String name) {
/*      */     ExecutorService executor;
/*  207 */     int threads = maxAllowedExecutorThreads();
/*  209 */     if (threads <= 0) {
/*  211 */       ListeningExecutorService listeningExecutorService = MoreExecutors.newDirectExecutorService();
/*      */     } else {
/*  213 */       AtomicInteger workerCount = new AtomicInteger(1);
/*  214 */       executor = new ForkJoinPool(threads, pool -> {
/*      */             final String threadName = "Worker-" + name + "-" + workerCount.getAndIncrement();
/*      */             ForkJoinWorkerThread thread = new ForkJoinWorkerThread(pool) {
/*      */                 protected void onStart() {
/*  219 */                   TracyClient.setThreadName(threadName, name.hashCode());
/*  220 */                   super.onStart();
/*      */                 }
/*      */                 
/*      */                 protected void onTermination(Throwable exception) {
/*  225 */                   if (exception != null) {
/*  226 */                     Util.LOGGER.warn("{} died", getName(), exception);
/*      */                   } else {
/*  228 */                     Util.LOGGER.debug("{} shutdown", getName());
/*      */                   } 
/*  230 */                   super.onTermination(exception);
/*      */                 }
/*      */               };
/*      */             thread.setName(threadName);
/*      */             return thread;
/*      */           }Util::onThreadException, true);
/*      */     } 
/*  237 */     return new TracingExecutor(executor);
/*      */   }
/*      */   
/*      */   public static int maxAllowedExecutorThreads() {
/*  241 */     return Mth.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, getMaxThreads());
/*      */   }
/*      */   
/*      */   private static int getMaxThreads() {
/*  245 */     String maxThreadsString = System.getProperty("max.bg.threads");
/*  246 */     if (maxThreadsString != null)
/*      */       try {
/*  248 */         int maxThreads = Integer.parseInt(maxThreadsString);
/*  249 */         if (maxThreads >= 1 && maxThreads <= 255)
/*  250 */           return maxThreads; 
/*  252 */         LOGGER.error("Wrong {} property value '{}'. Should be an integer value between 1 and {}.", new Object[] { "max.bg.threads", maxThreadsString, Integer.valueOf(255) });
/*  253 */       } catch (NumberFormatException e) {
/*  254 */         LOGGER.error("Could not parse {} property value '{}'. Should be an integer value between 1 and {}.", new Object[] { "max.bg.threads", maxThreadsString, Integer.valueOf(255) });
/*      */       }  
/*  257 */     return 255;
/*      */   }
/*      */   
/*      */   public static TracingExecutor backgroundExecutor() {
/*  261 */     return BACKGROUND_EXECUTOR;
/*      */   }
/*      */   
/*      */   public static TracingExecutor ioPool() {
/*  266 */     return IO_POOL;
/*      */   }
/*      */   
/*      */   public static TracingExecutor nonCriticalIoPool() {
/*  271 */     return DOWNLOAD_POOL;
/*      */   }
/*      */   
/*      */   public static void shutdownExecutors() {
/*  275 */     BACKGROUND_EXECUTOR.shutdownAndAwait(3L, TimeUnit.SECONDS);
/*  276 */     IO_POOL.shutdownAndAwait(3L, TimeUnit.SECONDS);
/*      */   }
/*      */   
/*      */   private static TracingExecutor makeIoExecutor(String prefix, boolean daemon) {
/*  280 */     AtomicInteger workerCount = new AtomicInteger(1);
/*  281 */     return new TracingExecutor(Executors.newCachedThreadPool(runnable -> {
/*      */             String name = prefix + prefix;
/*      */             Thread thread = new Thread(runnable, name);
/*      */             TracyClient.setThreadName(name, prefix.hashCode());
/*      */             thread.setDaemon(daemon);
/*      */             thread.setUncaughtExceptionHandler(Util::onThreadException);
/*      */             return thread;
/*      */           }));
/*      */   }
/*      */   
/*      */   public static void throwAsRuntime(Throwable throwable) {
/*  292 */     RuntimeException runtimeException = (RuntimeException)throwable;
/*  292 */     throw (throwable instanceof RuntimeException) ? runtimeException : new RuntimeException(throwable);
/*      */   }
/*      */   
/*      */   private static void onThreadException(Thread thread, Throwable throwable) {
/*      */     CrashReport report;
/*  296 */     pauseInIde(throwable);
/*  297 */     if (throwable instanceof java.util.concurrent.CompletionException)
/*  298 */       throwable = throwable.getCause(); 
/*  300 */     LOGGER.error("Caught exception in thread {}", thread, throwable);
/*  303 */     if (throwable instanceof ReportedException) {
/*  303 */       ReportedException reportedException = (ReportedException)throwable;
/*  304 */       report = reportedException.getReport();
/*      */     } else {
/*  306 */       report = CrashReport.forThrowable(throwable, "Exception on worker thread");
/*      */     } 
/*  308 */     CrashReportCategory threadInfo = report.addCategory("ThreadInfo");
/*  309 */     threadInfo.setDetail("Name", thread.getName());
/*  310 */     BlockableEventLoop.relayDelayCrash(report);
/*      */   }
/*      */   
/*      */   public static Type<?> fetchChoiceType(DSL.TypeReference reference, String name) {
/*  314 */     if (!SharedConstants.CHECK_DATA_FIXER_SCHEMA)
/*  315 */       return null; 
/*  317 */     return doFetchChoiceType(reference, name);
/*      */   }
/*      */   
/*      */   private static Type<?> doFetchChoiceType(DSL.TypeReference reference, String name) {
/*  321 */     Type<?> dataType = null;
/*      */     try {
/*  323 */       dataType = DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().dataVersion().version())).getChoiceType(reference, name);
/*  324 */     } catch (IllegalArgumentException e) {
/*  325 */       LOGGER.error("No data fixer registered for {}", name);
/*  326 */       if (SharedConstants.IS_RUNNING_IN_IDE)
/*  327 */         throw e; 
/*      */     } 
/*  330 */     return dataType;
/*      */   }
/*      */   
/*      */   public static void runNamed(Runnable runnable, String name) {
/*  334 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  335 */       Thread thread = Thread.currentThread();
/*  336 */       String oldName = thread.getName();
/*  338 */       thread.setName(name);
/*      */       try {
/*  339 */         Zone ignored = TracyClient.beginZone(name, SharedConstants.IS_RUNNING_IN_IDE);
/*      */         try {
/*  340 */           runnable.run();
/*  341 */           if (ignored != null)
/*  341 */             ignored.close(); 
/*      */         } catch (Throwable throwable) {
/*      */           if (ignored != null)
/*      */             try {
/*      */               ignored.close();
/*      */             } catch (Throwable throwable1) {
/*      */               throwable.addSuppressed(throwable1);
/*      */             }  
/*      */           throw throwable;
/*      */         } 
/*      */       } finally {
/*  342 */         thread.setName(oldName);
/*      */       } 
/*      */     } else {
/*  345 */       Zone ignored = TracyClient.beginZone(name, SharedConstants.IS_RUNNING_IN_IDE);
/*      */       try {
/*  346 */         runnable.run();
/*  347 */         if (ignored != null)
/*  347 */           ignored.close(); 
/*      */       } catch (Throwable throwable) {
/*      */         if (ignored != null)
/*      */           try {
/*      */             ignored.close();
/*      */           } catch (Throwable throwable1) {
/*      */             throwable.addSuppressed(throwable1);
/*      */           }  
/*      */         throw throwable;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static <T> String getRegisteredName(Registry<T> registry, T entry) {
/*  352 */     Identifier key = registry.getKey(entry);
/*  353 */     if (key == null)
/*  354 */       return "[unregistered]"; 
/*  356 */     return key.toString();
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> allOf() {
/*  360 */     return context -> true;
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> allOf(Predicate<? super T> condition) {
/*  365 */     return (Predicate)condition;
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> allOf(Predicate<? super T> condition1, Predicate<? super T> condition2) {
/*  369 */     return context -> (condition1.test(context) && condition2.test(context));
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> allOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3) {
/*  373 */     return context -> (condition1.test(context) && condition2.test(context) && condition3.test(context));
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> allOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3, Predicate<? super T> condition4) {
/*  377 */     return context -> (condition1.test(context) && condition2.test(context) && condition3.test(context) && condition4.test(context));
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> allOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3, Predicate<? super T> condition4, Predicate<? super T> condition5) {
/*  381 */     return context -> (condition1.test(context) && condition2.test(context) && condition3.test(context) && condition4.test(context) && condition5.test(context));
/*      */   }
/*      */   
/*      */   @SafeVarargs
/*      */   public static <T> Predicate<T> allOf(Predicate<? super T>... conditions) {
/*  386 */     return context -> {
/*      */         for (Predicate<? super T> entry : conditions) {
/*      */           if (!entry.test((T)context))
/*      */             return false; 
/*      */         } 
/*      */         return true;
/*      */       };
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> allOf(List<? extends Predicate<? super T>> conditions) {
/*  397 */     switch (conditions.size()) {
/*      */       case 0:
/*      */       
/*      */       case 1:
/*      */       
/*      */       case 2:
/*      */       
/*      */       case 3:
/*      */       
/*      */       case 4:
/*      */       
/*      */       case 5:
/*      */       
/*      */     } 
/*  406 */     Predicate[] arrayOfPredicate = (Predicate[])conditions.toArray(x$0 -> new Predicate[x$0]);
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> anyOf() {
/*  413 */     return context -> false;
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> anyOf(Predicate<? super T> condition1) {
/*  418 */     return (Predicate)condition1;
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> anyOf(Predicate<? super T> condition1, Predicate<? super T> condition2) {
/*  422 */     return context -> (condition1.test(context) || condition2.test(context));
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> anyOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3) {
/*  426 */     return context -> (condition1.test(context) || condition2.test(context) || condition3.test(context));
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> anyOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3, Predicate<? super T> condition4) {
/*  430 */     return context -> (condition1.test(context) || condition2.test(context) || condition3.test(context) || condition4.test(context));
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> anyOf(Predicate<? super T> condition1, Predicate<? super T> condition2, Predicate<? super T> condition3, Predicate<? super T> condition4, Predicate<? super T> condition5) {
/*  434 */     return context -> (condition1.test(context) || condition2.test(context) || condition3.test(context) || condition4.test(context) || condition5.test(context));
/*      */   }
/*      */   
/*      */   @SafeVarargs
/*      */   public static <T> Predicate<T> anyOf(Predicate<? super T>... conditions) {
/*  439 */     return context -> {
/*      */         for (Predicate<? super T> entry : conditions) {
/*      */           if (entry.test((T)context))
/*      */             return true; 
/*      */         } 
/*      */         return false;
/*      */       };
/*      */   }
/*      */   
/*      */   public static <T> Predicate<T> anyOf(List<? extends Predicate<? super T>> conditions) {
/*  450 */     switch (conditions.size()) {
/*      */       case 0:
/*      */       
/*      */       case 1:
/*      */       
/*      */       case 2:
/*      */       
/*      */       case 3:
/*      */       
/*      */       case 4:
/*      */       
/*      */       case 5:
/*      */       
/*      */     } 
/*  459 */     Predicate[] arrayOfPredicate = (Predicate[])conditions.toArray(x$0 -> new Predicate[x$0]);
/*      */   }
/*      */   
/*      */   public static <T> boolean isSymmetrical(int width, int height, List<T> ingredients) {
/*  466 */     if (width == 1)
/*  467 */       return true; 
/*  469 */     int centerX = width / 2;
/*  470 */     for (int y = 0; y < height; y++) {
/*  471 */       for (int leftX = 0; leftX < centerX; leftX++) {
/*  472 */         int rightX = width - 1 - leftX;
/*  473 */         T left = ingredients.get(leftX + y * width);
/*  474 */         T right = ingredients.get(rightX + y * width);
/*  475 */         if (!left.equals(right))
/*  476 */           return false; 
/*      */       } 
/*      */     } 
/*  480 */     return true;
/*      */   }
/*      */   
/*      */   public static int growByHalf(int currentSize, int minimalNewSize) {
/*  485 */     return (int)Math.max(Math.min(currentSize + (currentSize >> 1), 2147483639L), minimalNewSize);
/*      */   }
/*      */   
/*      */   @SuppressForbidden(reason = "Intentional use of default locale for user-visible date")
/*      */   public static DateTimeFormatter localizedDateFormatter(FormatStyle formatStyle) {
/*  490 */     return DateTimeFormatter.ofLocalizedDateTime(formatStyle);
/*      */   }
/*      */   
/*      */   public static ThreadInfo[] dumpThreadInfo() {
/*  494 */     ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
/*  495 */     return threadMXBean.dumpAllThreads(threadMXBean.isObjectMonitorUsageSupported(), threadMXBean.isSynchronizerUsageSupported());
/*      */   }
/*      */   
/*      */   public enum OS {
/*  499 */     LINUX("linux"),
/*  500 */     SOLARIS("solaris"),
/*  501 */     WINDOWS("windows") {
/*      */       protected String[] getOpenUriArguments(URI uri) {
/*  504 */         return new String[] { "rundll32", "url.dll,FileProtocolHandler", uri.toString() };
/*      */       }
/*      */     },
/*  507 */     OSX("mac") {
/*      */       protected String[] getOpenUriArguments(URI uri) {
/*  510 */         return new String[] { "open", uri.toString() };
/*      */       }
/*      */     },
/*  513 */     UNKNOWN("unknown");
/*      */     
/*      */     private final String telemetryName;
/*      */     
/*      */     OS(String telemetryName) {
/*  518 */       this.telemetryName = telemetryName;
/*      */     }
/*      */     
/*      */     public void openUri(URI uri) {
/*      */       try {
/*  523 */         Process process = Runtime.getRuntime().exec(getOpenUriArguments(uri));
/*  524 */         process.getInputStream().close();
/*  525 */         process.getErrorStream().close();
/*  526 */         process.getOutputStream().close();
/*  527 */       } catch (IOException e) {
/*  528 */         Util.LOGGER.error("Couldn't open location '{}'", uri, e);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void openFile(File file) {
/*  533 */       openUri(file.toURI());
/*      */     }
/*      */     
/*      */     public void openPath(Path path) {
/*  537 */       openUri(path.toUri());
/*      */     }
/*      */     
/*      */     protected String[] getOpenUriArguments(URI uri) {
/*  541 */       String string = uri.toString();
/*  542 */       if ("file".equals(uri.getScheme()))
/*  544 */         string = string.replace("file:", "file://"); 
/*  546 */       return new String[] { "xdg-open", string };
/*      */     }
/*      */     
/*      */     public void openUri(String uri) {
/*      */       try {
/*  551 */         openUri(new URI(uri));
/*  553 */       } catch (URISyntaxException|IllegalArgumentException e) {
/*  554 */         Util.LOGGER.error("Couldn't open uri '{}'", uri, e);
/*      */       } 
/*      */     }
/*      */     
/*      */     public String telemetryName() {
/*  559 */       return this.telemetryName;
/*      */     }
/*      */   }
/*      */   
/*      */   enum null {
/*      */     protected String[] getOpenUriArguments(URI uri) {
/*      */       return new String[] { "rundll32", "url.dll,FileProtocolHandler", uri.toString() };
/*      */     }
/*      */   }
/*      */   
/*      */   enum null {
/*      */     protected String[] getOpenUriArguments(URI uri) {
/*      */       return new String[] { "open", uri.toString() };
/*      */     }
/*      */   }
/*      */   
/*      */   public static OS getPlatform() {
/*  564 */     String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
/*  565 */     if (osName.contains("win"))
/*  566 */       return OS.WINDOWS; 
/*  568 */     if (osName.contains("mac"))
/*  569 */       return OS.OSX; 
/*  571 */     if (osName.contains("solaris"))
/*  572 */       return OS.SOLARIS; 
/*  574 */     if (osName.contains("sunos"))
/*  575 */       return OS.SOLARIS; 
/*  577 */     if (osName.contains("linux"))
/*  578 */       return OS.LINUX; 
/*  580 */     if (osName.contains("unix"))
/*  581 */       return OS.LINUX; 
/*  583 */     return OS.UNKNOWN;
/*      */   }
/*      */   
/*      */   public static boolean isAarch64() {
/*  587 */     String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);
/*  588 */     return arch.equals("aarch64");
/*      */   }
/*      */   
/*      */   public static URI parseAndValidateUntrustedUri(String uri) throws URISyntaxException {
/*  592 */     URI parsedUri = new URI(uri);
/*  594 */     String scheme = parsedUri.getScheme();
/*  595 */     if (scheme == null)
/*  596 */       throw new URISyntaxException(uri, "Missing protocol in URI: " + uri); 
/*  598 */     String protocol = scheme.toLowerCase(Locale.ROOT);
/*  599 */     if (!ALLOWED_UNTRUSTED_LINK_PROTOCOLS.contains(protocol))
/*  600 */       throw new URISyntaxException(uri, "Unsupported protocol in URI: " + uri); 
/*  603 */     return parsedUri;
/*      */   }
/*      */   
/*      */   public static <T> T findNextInIterable(Iterable<T> collection, T current) {
/*  607 */     Iterator<T> iterator = collection.iterator();
/*  608 */     T first = iterator.next();
/*  610 */     if (current != null) {
/*  611 */       T property = first;
/*      */       while (true) {
/*  613 */         if (property == current) {
/*  614 */           if (iterator.hasNext())
/*  615 */             return iterator.next(); 
/*      */           break;
/*      */         } 
/*  620 */         if (iterator.hasNext())
/*  621 */           property = iterator.next(); 
/*      */       } 
/*      */     } 
/*  625 */     return first;
/*      */   }
/*      */   
/*      */   public static <T> T findPreviousInIterable(Iterable<T> collection, T current) {
/*  629 */     Iterator<T> iterator = collection.iterator();
/*  630 */     T last = null;
/*  631 */     while (iterator.hasNext()) {
/*  632 */       T next = iterator.next();
/*  633 */       if (next == current) {
/*  634 */         if (last == null)
/*  635 */           last = iterator.hasNext() ? (T)Iterators.getLast(iterator) : current; 
/*      */         break;
/*      */       } 
/*  639 */       last = next;
/*      */     } 
/*  641 */     return last;
/*      */   }
/*      */   
/*      */   public static <T> T make(Supplier<T> factory) {
/*  645 */     return factory.get();
/*      */   }
/*      */   
/*      */   public static <T> T make(T t, Consumer<? super T> consumer) {
/*  649 */     consumer.accept(t);
/*  650 */     return t;
/*      */   }
/*      */   
/*      */   public static <K extends Enum<K>, V> Map<K, V> makeEnumMap(Class<K> keyType, Function<K, V> function) {
/*  654 */     EnumMap<K, V> map = new EnumMap<>(keyType);
/*  655 */     for (Enum enum_ : (Enum[])keyType.getEnumConstants())
/*  656 */       map.put((K)enum_, function.apply((K)enum_)); 
/*  658 */     return map;
/*      */   }
/*      */   
/*      */   public static <K, V1, V2> Map<K, V2> mapValues(Map<K, V1> map, Function<? super V1, V2> valueMapper) {
/*  662 */     return (Map<K, V2>)map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> valueMapper.apply(e.getValue())));
/*      */   }
/*      */   
/*      */   public static <K, V1, V2> Map<K, V2> mapValuesLazy(Map<K, V1> map, Function<V1, V2> valueMapper) {
/*  666 */     return Maps.transformValues(map, valueMapper);
/*      */   }
/*      */   
/*      */   public static <T extends Enum<T>> Set<T> allOfEnumExcept(T value) {
/*  670 */     return EnumSet.complementOf(EnumSet.of(value));
/*      */   }
/*      */   
/*      */   public static <V> CompletableFuture<List<V>> sequence(List<? extends CompletableFuture<V>> futures) {
/*  674 */     if (futures.isEmpty())
/*  675 */       return CompletableFuture.completedFuture(List.of()); 
/*  678 */     if (futures.size() == 1)
/*  680 */       return ((CompletableFuture)futures.getFirst()).thenApply(ObjectLists::singleton); 
/*  683 */     CompletableFuture<Void> all = CompletableFuture.allOf((CompletableFuture<?>[])futures.<CompletableFuture>toArray(new CompletableFuture[0]));
/*  684 */     return all.thenApply(ignored -> futures.stream().map(CompletableFuture::join).toList());
/*      */   }
/*      */   
/*      */   public static <V> CompletableFuture<List<V>> sequenceFailFast(List<? extends CompletableFuture<? extends V>> futures) {
/*  689 */     CompletableFuture<List<V>> failureFuture = new CompletableFuture<>();
/*  691 */     Objects.requireNonNull(failureFuture);
/*  691 */     return fallibleSequence(futures, failureFuture::completeExceptionally)
/*  692 */       .applyToEither(failureFuture, (Function)Function.identity());
/*      */   }
/*      */   
/*      */   public static <V> CompletableFuture<List<V>> sequenceFailFastAndCancel(List<? extends CompletableFuture<? extends V>> futures) {
/*  696 */     CompletableFuture<List<V>> failureFuture = new CompletableFuture<>();
/*  698 */     return fallibleSequence(futures, exception -> {
/*      */           if (failureFuture.completeExceptionally(exception))
/*      */             for (CompletableFuture<? extends V> future : (Iterable<CompletableFuture<? extends V>>)futures)
/*      */               future.cancel(true);  
/*  704 */         }).applyToEither(failureFuture, (Function)Function.identity());
/*      */   }
/*      */   
/*      */   private static <V> CompletableFuture<List<V>> fallibleSequence(List<? extends CompletableFuture<? extends V>> futures, Consumer<Throwable> failureHandler) {
/*  708 */     ObjectArrayList<V> results = new ObjectArrayList();
/*  709 */     results.size(futures.size());
/*  710 */     CompletableFuture[] arrayOfCompletableFuture = new CompletableFuture[futures.size()];
/*  712 */     for (int i = 0; i < futures.size(); i++) {
/*  713 */       int index = i;
/*  714 */       arrayOfCompletableFuture[i] = ((CompletableFuture)futures.get(i)).whenComplete((result, exception) -> {
/*      */             if (exception != null) {
/*      */               failureHandler.accept(exception);
/*      */             } else {
/*      */               results.set(index, result);
/*      */             } 
/*      */           });
/*      */     } 
/*  723 */     return CompletableFuture.allOf((CompletableFuture<?>[])arrayOfCompletableFuture).thenApply(nothing -> results);
/*      */   }
/*      */   
/*      */   public static <T> Optional<T> ifElse(Optional<T> input, Consumer<T> onTrue, Runnable onFalse) {
/*  727 */     if (input.isPresent()) {
/*  728 */       onTrue.accept(input.get());
/*      */     } else {
/*  730 */       onFalse.run();
/*      */     } 
/*  732 */     return input;
/*      */   }
/*      */   
/*      */   public static <T> Supplier<T> name(final Supplier<T> task, Supplier<String> nameGetter) {
/*  736 */     if (SharedConstants.DEBUG_NAMED_RUNNABLES) {
/*  737 */       final String name = nameGetter.get();
/*  738 */       return new Supplier<T>() {
/*      */           public T get() {
/*  741 */             return task.get();
/*      */           }
/*      */           
/*      */           public String toString() {
/*  746 */             return name;
/*      */           }
/*      */         };
/*      */     } 
/*  750 */     return task;
/*      */   }
/*      */   
/*      */   public static Runnable name(final Runnable task, Supplier<String> nameGetter) {
/*  755 */     if (SharedConstants.DEBUG_NAMED_RUNNABLES) {
/*  756 */       final String name = nameGetter.get();
/*  757 */       return new Runnable() {
/*      */           public void run() {
/*  760 */             task.run();
/*      */           }
/*      */           
/*      */           public String toString() {
/*  765 */             return name;
/*      */           }
/*      */         };
/*      */     } 
/*  769 */     return task;
/*      */   }
/*      */   
/*      */   public static void logAndPauseIfInIde(String message) {
/*  774 */     LOGGER.error(message);
/*  775 */     if (SharedConstants.IS_RUNNING_IN_IDE)
/*  776 */       doPause(message); 
/*      */   }
/*      */   
/*      */   public static void logAndPauseIfInIde(String message, Throwable throwable) {
/*  781 */     LOGGER.error(message, throwable);
/*  782 */     if (SharedConstants.IS_RUNNING_IN_IDE)
/*  783 */       doPause(message); 
/*      */   }
/*      */   
/*      */   public static <T extends Throwable> T pauseInIde(T t) {
/*  788 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  789 */       LOGGER.error("Trying to throw a fatal exception, pausing in IDE", (Throwable)t);
/*  790 */       doPause(t.getMessage());
/*      */     } 
/*  792 */     return t;
/*      */   }
/*      */   
/*      */   public static void setPause(Consumer<String> pauseFunction) {
/*  796 */     thePauser = pauseFunction;
/*      */   }
/*      */   
/*      */   private static void doPause(String message) {
/*  800 */     Instant preLog = Instant.now();
/*  802 */     LOGGER.warn("Did you remember to set a breakpoint here?");
/*  803 */     boolean dontBotherWithPause = (Duration.between(preLog, Instant.now()).toMillis() > 500L);
/*  804 */     if (!dontBotherWithPause)
/*  805 */       thePauser.accept(message); 
/*      */   }
/*      */   
/*      */   public static String describeError(Throwable err) {
/*  810 */     if (err.getCause() != null)
/*  811 */       return describeError(err.getCause()); 
/*  812 */     if (err.getMessage() != null)
/*  813 */       return err.getMessage(); 
/*  815 */     return err.toString();
/*      */   }
/*      */   
/*      */   public static <T> T getRandom(T[] array, RandomSource random) {
/*  820 */     return array[random.nextInt(array.length)];
/*      */   }
/*      */   
/*      */   public static int getRandom(int[] array, RandomSource random) {
/*  825 */     return array[random.nextInt(array.length)];
/*      */   }
/*      */   
/*      */   public static <T> T getRandom(List<T> list, RandomSource random) {
/*  829 */     return list.get(random.nextInt(list.size()));
/*      */   }
/*      */   
/*      */   public static <T> Optional<T> getRandomSafe(List<T> list, RandomSource random) {
/*  833 */     if (list.isEmpty())
/*  834 */       return Optional.empty(); 
/*  836 */     return Optional.of(getRandom(list, random));
/*      */   }
/*      */   
/*      */   private static BooleanSupplier createRenamer(final Path from, final Path to, CopyOption... options) {
/*  840 */     return new BooleanSupplier() {
/*      */         public boolean getAsBoolean() {
/*      */           try {
/*  844 */             Files.move(from, to, options);
/*  845 */             return true;
/*  846 */           } catch (IOException e) {
/*  847 */             Util.LOGGER.error("Failed to rename", e);
/*  848 */             return false;
/*      */           } 
/*      */         }
/*      */         
/*      */         public String toString() {
/*  854 */           return "rename " + String.valueOf(from) + " to " + String.valueOf(to);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static BooleanSupplier createDeleter(final Path target) {
/*  860 */     return new BooleanSupplier() {
/*      */         public boolean getAsBoolean() {
/*      */           try {
/*  864 */             Files.deleteIfExists(target);
/*  865 */             return true;
/*  866 */           } catch (IOException e) {
/*  867 */             Util.LOGGER.warn("Failed to delete", e);
/*  868 */             return false;
/*      */           } 
/*      */         }
/*      */         
/*      */         public String toString() {
/*  874 */           return "delete old " + String.valueOf(target);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static BooleanSupplier createFileDeletedCheck(final Path target) {
/*  880 */     return new BooleanSupplier() {
/*      */         public boolean getAsBoolean() {
/*  883 */           return !Files.exists(target, new java.nio.file.LinkOption[0]);
/*      */         }
/*      */         
/*      */         public String toString() {
/*  888 */           return "verify that " + String.valueOf(target) + " is deleted";
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static BooleanSupplier createFileCreatedCheck(final Path target) {
/*  894 */     return new BooleanSupplier() {
/*      */         public boolean getAsBoolean() {
/*  897 */           return Files.isRegularFile(target, new java.nio.file.LinkOption[0]);
/*      */         }
/*      */         
/*      */         public String toString() {
/*  902 */           return "verify that " + String.valueOf(target) + " is present";
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static boolean executeInSequence(BooleanSupplier... operations) {
/*  908 */     for (BooleanSupplier operation : operations) {
/*  909 */       if (!operation.getAsBoolean()) {
/*  910 */         LOGGER.warn("Failed to execute {}", operation);
/*  911 */         return false;
/*      */       } 
/*      */     } 
/*  914 */     return true;
/*      */   }
/*      */   
/*      */   private static boolean runWithRetries(int numberOfRetries, String description, BooleanSupplier... operations) {
/*  918 */     for (int retry = 0; retry < numberOfRetries; retry++) {
/*  919 */       if (executeInSequence(operations))
/*  920 */         return true; 
/*  922 */       LOGGER.error("Failed to {}, retrying {}/{}", new Object[] { description, Integer.valueOf(retry), Integer.valueOf(numberOfRetries) });
/*      */     } 
/*  924 */     LOGGER.error("Failed to {}, aborting, progress might be lost", description);
/*  925 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean safeMoveFile(Path fromPath, Path toPath, CopyOption... options) {
/*  929 */     return runWithRetries(10, "move from  " + String.valueOf(fromPath) + " to " + String.valueOf(toPath), new BooleanSupplier[] { createRenamer(fromPath, toPath, options), 
/*  931 */           createFileCreatedCheck(toPath) });
/*      */   }
/*      */   
/*      */   public static void safeReplaceFile(Path targetPath, Path newPath, Path backupPath) {
/*  936 */     safeReplaceOrMoveFile(targetPath, newPath, backupPath, false);
/*      */   }
/*      */   
/*      */   public static boolean safeReplaceOrMoveFile(Path targetPath, Path newPath, Path backupPath, boolean noRollback) {
/*  940 */     if (Files.exists(targetPath, new java.nio.file.LinkOption[0]) && 
/*  941 */       !runWithRetries(10, "create backup " + String.valueOf(backupPath), new BooleanSupplier[] { createDeleter(backupPath), 
/*  943 */           createRenamer(targetPath, backupPath, new CopyOption[0]), 
/*  944 */           createFileCreatedCheck(backupPath) }))
/*  946 */       return false; 
/*  951 */     if (!runWithRetries(10, "remove old " + String.valueOf(targetPath), new BooleanSupplier[] { createDeleter(targetPath), 
/*  953 */           createFileDeletedCheck(targetPath) }))
/*  955 */       return false; 
/*  958 */     if (!runWithRetries(10, "replace " + String.valueOf(targetPath) + " with " + String.valueOf(newPath), new BooleanSupplier[] { createRenamer(newPath, targetPath, new CopyOption[0]), 
/*  960 */           createFileCreatedCheck(targetPath) }) && !noRollback) {
/*  962 */       runWithRetries(10, "restore " + String.valueOf(targetPath) + " from " + String.valueOf(backupPath), new BooleanSupplier[] { createRenamer(backupPath, targetPath, new CopyOption[0]), 
/*  964 */             createFileCreatedCheck(targetPath) });
/*  966 */       return false;
/*      */     } 
/*  968 */     return true;
/*      */   }
/*      */   
/*      */   public static int offsetByCodepoints(String input, int pos, int offset) {
/*  973 */     int length = input.length();
/*  974 */     if (offset >= 0) {
/*  975 */       for (int i = 0; pos < length && i < offset; i++) {
/*  976 */         if (Character.isHighSurrogate(input.charAt(pos++)) && pos < length && 
/*  977 */           Character.isLowSurrogate(input.charAt(pos)))
/*  979 */           pos++; 
/*      */       } 
/*      */     } else {
/*  983 */       for (int i = offset; pos > 0 && i < 0; i++) {
/*  984 */         if (Character.isLowSurrogate(input.charAt(--pos)) && pos > 0 && 
/*  985 */           Character.isHighSurrogate(input.charAt(pos - 1)))
/*  987 */           pos--; 
/*      */       } 
/*      */     } 
/*  991 */     return pos;
/*      */   }
/*      */   
/*      */   public static Consumer<String> prefix(String prefix, Consumer<String> consumer) {
/*  995 */     return s -> consumer.accept(prefix + prefix);
/*      */   }
/*      */   
/*      */   public static DataResult<int[]> fixedSize(IntStream stream, int size) {
/* 1000 */     int[] ints = stream.limit((size + 1)).toArray();
/* 1001 */     if (ints.length != size) {
/* 1002 */       Supplier<String> message = () -> "Input is not a list of " + size + " ints";
/* 1003 */       if (ints.length >= size)
/* 1004 */         return DataResult.error(message, Arrays.copyOf(ints, size)); 
/* 1006 */       return DataResult.error(message);
/*      */     } 
/* 1009 */     return DataResult.success(ints);
/*      */   }
/*      */   
/*      */   public static DataResult<long[]> fixedSize(LongStream stream, int size) {
/* 1014 */     long[] longs = stream.limit((size + 1)).toArray();
/* 1015 */     if (longs.length != size) {
/* 1016 */       Supplier<String> message = () -> "Input is not a list of " + size + " longs";
/* 1017 */       if (longs.length >= size)
/* 1018 */         return DataResult.error(message, Arrays.copyOf(longs, size)); 
/* 1020 */       return DataResult.error(message);
/*      */     } 
/* 1023 */     return DataResult.success(longs);
/*      */   }
/*      */   
/*      */   public static <T> DataResult<List<T>> fixedSize(List<T> list, int size) {
/* 1028 */     if (list.size() != size) {
/* 1029 */       Supplier<String> message = () -> "Input is not a list of " + size + " elements";
/* 1030 */       if (list.size() >= size)
/* 1031 */         return DataResult.error(message, list.subList(0, size)); 
/* 1033 */       return DataResult.error(message);
/*      */     } 
/* 1036 */     return DataResult.success(list);
/*      */   }
/*      */   
/*      */   public static void startTimerHackThread() {
/* 1042 */     Thread timerThread = new Thread("Timer hack thread") {
/*      */         public void run() {
/*      */           try {
/*      */             while (true)
/* 1047 */               Thread.sleep(2147483647L); 
/* 1048 */           } catch (InterruptedException e) {
/* 1049 */             Util.LOGGER.warn("Timer hack thread interrupted, that really should not happen");
/*      */             return;
/*      */           } 
/*      */         }
/*      */       };
/* 1055 */     timerThread.setDaemon(true);
/* 1056 */     timerThread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER));
/* 1057 */     timerThread.start();
/*      */   }
/*      */   
/*      */   public static void copyBetweenDirs(Path sourceDir, Path targetDir, Path sourcePath) throws IOException {
/* 1061 */     Path relative = sourceDir.relativize(sourcePath);
/* 1062 */     Path target = targetDir.resolve(relative);
/* 1063 */     Files.copy(sourcePath, target, new CopyOption[0]);
/*      */   }
/*      */   
/*      */   public static String sanitizeName(String value, CharPredicate isAllowedChar) {
/* 1067 */     return value.toLowerCase(Locale.ROOT).chars().<CharSequence>mapToObj(c -> isAllowedChar.test((char)c) ? Character.toString((char)c) : "_").collect(Collectors.joining());
/*      */   }
/*      */   
/*      */   public static <K, V> SingleKeyCache<K, V> singleKeyCache(Function<K, V> computeValueFunction) {
/* 1071 */     return new SingleKeyCache<>(computeValueFunction);
/*      */   }
/*      */   
/*      */   public static <T, R> Function<T, R> memoize(final Function<T, R> function) {
/* 1075 */     return new Function<T, R>() {
/* 1076 */         private final Map<T, R> cache = new ConcurrentHashMap<>();
/*      */         
/*      */         public R apply(T arg) {
/* 1080 */           return this.cache.computeIfAbsent(arg, function);
/*      */         }
/*      */         
/*      */         public String toString() {
/* 1085 */           return "memoize/1[function=" + String.valueOf(function) + ", size=" + this.cache.size() + "]";
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   public static <T, U, R> BiFunction<T, U, R> memoize(final BiFunction<T, U, R> function) {
/* 1091 */     return new BiFunction<T, U, R>() {
/* 1092 */         private final Map<Pair<T, U>, R> cache = new ConcurrentHashMap<>();
/*      */         
/*      */         public R apply(T a, U b) {
/* 1096 */           return this.cache.computeIfAbsent(Pair.of(a, b), args -> function.apply(args.getFirst(), args.getSecond()));
/*      */         }
/*      */         
/*      */         public String toString() {
/* 1101 */           return "memoize/2[function=" + String.valueOf(function) + ", size=" + this.cache.size() + "]";
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   public static <T> List<T> toShuffledList(Stream<T> stream, RandomSource random) {
/* 1107 */     ObjectArrayList<T> result = stream.collect(ObjectArrayList.toList());
/* 1108 */     shuffle((List<T>)result, random);
/* 1109 */     return (List<T>)result;
/*      */   }
/*      */   
/*      */   public static IntArrayList toShuffledList(IntStream stream, RandomSource random) {
/* 1113 */     IntArrayList result = IntArrayList.wrap(stream.toArray());
/* 1114 */     int size = result.size();
/* 1115 */     for (int i = size; i > 1; i--) {
/* 1116 */       int swapTo = random.nextInt(i);
/* 1117 */       result.set(i - 1, result.set(swapTo, result.getInt(i - 1)));
/*      */     } 
/* 1119 */     return result;
/*      */   }
/*      */   
/*      */   public static <T> List<T> shuffledCopy(T[] array, RandomSource random) {
/* 1123 */     ObjectArrayList<T> copy = new ObjectArrayList((Object[])array);
/* 1124 */     shuffle((List<T>)copy, random);
/* 1125 */     return (List<T>)copy;
/*      */   }
/*      */   
/*      */   public static <T> List<T> shuffledCopy(ObjectArrayList<T> list, RandomSource random) {
/* 1130 */     ObjectArrayList<T> copy = new ObjectArrayList((ObjectList)list);
/* 1131 */     shuffle((List<T>)copy, random);
/* 1132 */     return (List<T>)copy;
/*      */   }
/*      */   
/*      */   public static <T> void shuffle(List<T> list, RandomSource random) {
/* 1136 */     int size = list.size();
/* 1137 */     for (int i = size; i > 1; i--) {
/* 1138 */       int swapTo = random.nextInt(i);
/* 1139 */       list.set(i - 1, list.set(swapTo, list.get(i - 1)));
/*      */     } 
/*      */   }
/*      */   
/*      */   public static <T> CompletableFuture<T> blockUntilDone(Function<Executor, CompletableFuture<T>> task) {
/* 1144 */     return blockUntilDone(task, CompletableFuture::isDone);
/*      */   }
/*      */   
/*      */   public static <T> T blockUntilDone(Function<Executor, T> task, Predicate<T> completionCheck) {
/* 1148 */     BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
/* 1150 */     Objects.requireNonNull(tasks);
/* 1150 */     T result = task.apply(tasks::add);
/* 1151 */     while (!completionCheck.test(result)) {
/*      */       try {
/* 1154 */         Runnable runnable = tasks.poll(100L, TimeUnit.MILLISECONDS);
/* 1155 */         if (runnable != null)
/* 1156 */           runnable.run(); 
/* 1158 */       } catch (InterruptedException e) {
/* 1159 */         LOGGER.warn("Interrupted wait");
/*      */         break;
/*      */       } 
/*      */     } 
/* 1164 */     int remainingSize = tasks.size();
/* 1165 */     if (remainingSize > 0)
/* 1168 */       LOGGER.warn("Tasks left in queue: {}", Integer.valueOf(remainingSize)); 
/* 1171 */     return result;
/*      */   }
/*      */   
/*      */   public static <T> ToIntFunction<T> createIndexLookup(List<T> values) {
/* 1178 */     int size = values.size();
/* 1179 */     if (size < 8) {
/* 1180 */       Objects.requireNonNull(values);
/* 1180 */       return values::indexOf;
/*      */     } 
/* 1183 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap(size);
/* 1184 */     object2IntOpenHashMap.defaultReturnValue(-1);
/* 1185 */     for (int i = 0; i < size; i++)
/* 1186 */       object2IntOpenHashMap.put(values.get(i), i); 
/* 1188 */     return (ToIntFunction<T>)object2IntOpenHashMap;
/*      */   }
/*      */   
/*      */   public static <T> ToIntFunction<T> createIndexIdentityLookup(List<T> values) {
/* 1195 */     int size = values.size();
/* 1197 */     if (size < 8) {
/* 1198 */       ReferenceImmutableList referenceImmutableList = new ReferenceImmutableList(values);
/* 1199 */       Objects.requireNonNull(referenceImmutableList);
/* 1199 */       return referenceImmutableList::indexOf;
/*      */     } 
/* 1202 */     Reference2IntOpenHashMap reference2IntOpenHashMap = new Reference2IntOpenHashMap(size);
/* 1203 */     reference2IntOpenHashMap.defaultReturnValue(-1);
/* 1204 */     for (int i = 0; i < size; i++)
/* 1205 */       reference2IntOpenHashMap.put(values.get(i), i); 
/* 1207 */     return (ToIntFunction<T>)reference2IntOpenHashMap;
/*      */   }
/*      */   
/*      */   public static <A, B> Typed<B> writeAndReadTypedOrThrow(Typed<A> typed, Type<B> newType, UnaryOperator<Dynamic<?>> function) {
/* 1211 */     Dynamic<?> dynamic = (Dynamic)typed.write().getOrThrow();
/* 1213 */     return readTypedOrThrow(newType, function.apply(dynamic), true);
/*      */   }
/*      */   
/*      */   public static <T> Typed<T> readTypedOrThrow(Type<T> type, Dynamic<?> dynamic) {
/* 1217 */     return readTypedOrThrow(type, dynamic, false);
/*      */   }
/*      */   
/*      */   public static <T> Typed<T> readTypedOrThrow(Type<T> type, Dynamic<?> dynamic, boolean acceptPartial) {
/* 1221 */     DataResult<Typed<T>> result = type.readTyped(dynamic).map(Pair::getFirst);
/*      */     try {
/* 1223 */       if (acceptPartial)
/* 1224 */         return (Typed<T>)result.getPartialOrThrow(IllegalStateException::new); 
/* 1226 */       return (Typed<T>)result.getOrThrow(IllegalStateException::new);
/* 1227 */     } catch (IllegalStateException e) {
/* 1228 */       CrashReport report = CrashReport.forThrowable(e, "Reading type");
/* 1229 */       CrashReportCategory category = report.addCategory("Info");
/* 1230 */       category.setDetail("Data", dynamic);
/* 1231 */       category.setDetail("Type", type);
/* 1232 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static <T> List<T> copyAndAdd(List<T> list, T element) {
/* 1237 */     return (List<T>)ImmutableList.builderWithExpectedSize(list.size() + 1)
/* 1238 */       .addAll(list)
/* 1239 */       .add(element)
/* 1240 */       .build();
/*      */   }
/*      */   
/*      */   public static <T> List<T> copyAndAdd(List<T> list, T... elements) {
/* 1244 */     return (List<T>)ImmutableList.builderWithExpectedSize(list.size() + elements.length)
/* 1245 */       .addAll(list)
/* 1246 */       .add((Object[])elements)
/* 1247 */       .build();
/*      */   }
/*      */   
/*      */   public static <T> List<T> copyAndAdd(T element, List<T> list) {
/* 1251 */     return (List<T>)ImmutableList.builderWithExpectedSize(list.size() + 1)
/* 1252 */       .add(element)
/* 1253 */       .addAll(list)
/* 1254 */       .build();
/*      */   }
/*      */   
/*      */   public static <T> List<T> join(List<T> first, List<T> second) {
/* 1258 */     ImmutableList.Builder<T> builder = ImmutableList.builderWithExpectedSize(first.size() + second.size());
/* 1259 */     builder.addAll(first);
/* 1260 */     builder.addAll(second);
/* 1261 */     return (List<T>)builder.build();
/*      */   }
/*      */   
/*      */   public static <T> List<T> join(List<T>... lists) {
/* 1265 */     int size = 0;
/* 1266 */     for (List<T> list : lists)
/* 1267 */       size += list.size(); 
/* 1269 */     ImmutableList.Builder<T> builder = ImmutableList.builderWithExpectedSize(size);
/* 1270 */     for (List<T> list : lists)
/* 1271 */       builder.addAll(list); 
/* 1273 */     return (List<T>)builder.build();
/*      */   }
/*      */   
/*      */   public static <K, V> Map<K, V> copyAndPut(Map<K, V> map, K key, V value) {
/* 1277 */     return (Map<K, V>)ImmutableMap.builderWithExpectedSize(map.size() + 1)
/* 1278 */       .putAll(map)
/* 1279 */       .put(key, value)
/* 1280 */       .buildKeepingLast();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\Util.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */