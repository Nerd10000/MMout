/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.google.common.collect.Streams;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Locale;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportType;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerWatchdog implements Runnable {
/*  24 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final long MAX_SHUTDOWN_TIME = 10000L;
/*     */   
/*     */   private static final int SHUTDOWN_STATUS = 1;
/*     */   
/*  29 */   private static final Comparator<ThreadInfo> THREAD_INFO_COMPARATOR = Comparator.comparing(ThreadInfo::isDaemon)
/*  30 */     .thenComparing(ThreadInfo::getThreadState)
/*  31 */     .thenComparing(ThreadInfo::getThreadName);
/*     */   
/*     */   private final DedicatedServer server;
/*     */   
/*     */   private final long maxTickTimeNanos;
/*     */   
/*     */   public ServerWatchdog(DedicatedServer server) {
/*  37 */     this.server = server;
/*  38 */     this.maxTickTimeNanos = server.getMaxTickLength() * TimeUtil.NANOSECONDS_PER_MILLISECOND;
/*     */   }
/*     */   
/*     */   public void run() {
/*  43 */     while (this.server.isRunning()) {
/*  44 */       long nextTickTimeNanos = this.server.getNextTickTime();
/*  45 */       long currentTimeNanos = Util.getNanos();
/*  46 */       long deltaNanos = currentTimeNanos - nextTickTimeNanos;
/*  48 */       if (deltaNanos > this.maxTickTimeNanos) {
/*  49 */         LOGGER.error(LogUtils.FATAL_MARKER, "A single server tick took {} seconds (should be max {})", String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf((float)deltaNanos / (float)TimeUtil.NANOSECONDS_PER_SECOND) }), String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(this.server.tickRateManager().millisecondsPerTick() / (float)TimeUtil.MILLISECONDS_PER_SECOND) }));
/*  50 */         LOGGER.error(LogUtils.FATAL_MARKER, "Considering it to be crashed, server will forcibly shutdown.");
/*  52 */         CrashReport report = createWatchdogCrashReport("Watching Server", this.server.getRunningThread().threadId());
/*  53 */         this.server.fillSystemReport(report.getSystemReport());
/*  55 */         CrashReportCategory serverStats = report.addCategory("Performance stats");
/*  56 */         serverStats.setDetail("Random tick rate", () -> this.server.getGameRules().getAsString(GameRules.RANDOM_TICK_SPEED));
/*  57 */         serverStats.setDetail("Level stats", () -> (String)Streams.stream(this.server.getAllLevels()).map(()).collect(Collectors.joining(",\n")));
/*  59 */         Bootstrap.realStdoutPrintln("Crash report:\n" + report.getFriendlyReport(ReportType.CRASH));
/*  61 */         Path file = this.server.getServerDirectory().resolve("crash-reports").resolve("crash-" + Util.getFilenameFormattedDateTime() + "-server.txt");
/*  62 */         if (report.saveToFile(file, ReportType.CRASH)) {
/*  63 */           LOGGER.error("This crash report has been saved to: {}", file.toAbsolutePath());
/*     */         } else {
/*  65 */           LOGGER.error("We were unable to save this crash report to disk.");
/*     */         } 
/*  68 */         exit();
/*     */       } 
/*     */       try {
/*  72 */         Thread.sleep((nextTickTimeNanos + this.maxTickTimeNanos - currentTimeNanos) / TimeUtil.NANOSECONDS_PER_MILLISECOND);
/*  73 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */   }
/*     */   
/*     */   public static CrashReport createWatchdogCrashReport(String message, long mainThreadId) {
/*  79 */     ThreadInfo[] threadInfos = Util.dumpThreadInfo();
/*  80 */     Arrays.sort(threadInfos, THREAD_INFO_COMPARATOR);
/*  82 */     StringBuilder builder = new StringBuilder();
/*  83 */     Error exception = new Error("Watchdog (" + message + ")");
/*  85 */     for (ThreadInfo threadInfo : threadInfos) {
/*  86 */       if (threadInfo.getThreadId() == mainThreadId)
/*  87 */         exception.setStackTrace(threadInfo.getStackTrace()); 
/*  90 */       builder.append("\n");
/*  91 */       builder.append(threadInfo);
/*     */     } 
/*  94 */     CrashReport report = new CrashReport(message, exception);
/*  95 */     CrashReportCategory threadDump = report.addCategory("Thread Dump");
/*  96 */     threadDump.setDetail("Threads", builder);
/*  97 */     return report;
/*     */   }
/*     */   
/*     */   private void exit() {
/*     */     try {
/* 102 */       Timer timer = new Timer();
/* 103 */       timer.schedule(new TimerTask(this) {
/*     */             public void run() {
/* 106 */               Runtime.getRuntime().halt(1);
/*     */             }
/*     */           },  10000L);
/* 110 */       System.exit(1);
/* 111 */     } catch (Throwable ignored) {
/* 112 */       Runtime.getRuntime().halt(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dedicated\ServerWatchdog.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */