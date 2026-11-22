/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.main.GameConfig;
/*    */ import net.minecraft.server.dedicated.ServerWatchdog;
/*    */ import net.minecraft.util.NativeModuleLister;
/*    */ 
/*    */ public class ClientShutdownWatchdog {
/* 16 */   private static final Duration CRASH_REPORT_PRELOAD_LOAD = Duration.ofSeconds(15L);
/*    */   
/* 17 */   private static final AtomicInteger THREAD_COUNTER = new AtomicInteger();
/*    */   
/*    */   private static final int SHUTDOWN_STARTED_ID = -2147483648;
/*    */   
/*    */   public static void startShutdownWatchdog(String callsite, boolean forceShutdown, Minecraft minecraft, GameConfig gameConfig, long mainThreadId) {
/* 22 */     int id = THREAD_COUNTER.incrementAndGet();
/* 23 */     if (id < 0)
/*    */       return; 
/* 27 */     Thread thread = new Thread(() -> {
/*    */           try {
/*    */             Thread.sleep(CRASH_REPORT_PRELOAD_LOAD);
/* 30 */           } catch (InterruptedException e) {
/*    */             return;
/*    */           } 
/*    */           if (!THREAD_COUNTER.compareAndSet(id, -2147483648))
/*    */             return; 
/*    */           CrashReport report = ServerWatchdog.createWatchdogCrashReport("Client shutdown from " + callsite, mainThreadId);
/*    */           CrashReportCategory details = report.addCategory("Client watchdog shutdown details");
/*    */           NativeModuleLister.addCrashSection(details);
/*    */           if (minecraft != null) {
/*    */             minecraft.fillReport(report);
/*    */           } else {
/*    */             Minecraft.fillReport(null, null, gameConfig.game.launchVersion, null, report);
/*    */           } 
/*    */           Minecraft.saveReport(gameConfig.location.gameDirectory, report);
/*    */           if (forceShutdown)
/*    */             System.exit(-8); 
/*    */         }"Client shutdown watchdog #" + id);
/* 59 */     thread.setDaemon(true);
/* 60 */     thread.start();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\platform\ClientShutdownWatchdog.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */