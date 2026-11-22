/*     */ package com.mojang.realmsclient.util;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import java.time.Instant;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.component.ResolvableProfile;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsUtil {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  24 */   private static final Component RIGHT_NOW = (Component)Component.translatable("mco.util.time.now");
/*     */   
/*     */   private static final int MINUTES = 60;
/*     */   
/*     */   private static final int HOURS = 3600;
/*     */   
/*     */   private static final int DAYS = 86400;
/*     */   
/*     */   public static Component convertToAgePresentation(long timeDiff) {
/*  31 */     if (timeDiff < 0L)
/*  32 */       return RIGHT_NOW; 
/*  35 */     long timeDiffInSeconds = timeDiff / 1000L;
/*  37 */     if (timeDiffInSeconds < 60L)
/*  38 */       return (Component)Component.translatable("mco.time.secondsAgo", new Object[] { Long.valueOf(timeDiffInSeconds) }); 
/*  41 */     if (timeDiffInSeconds < 3600L) {
/*  42 */       long minutes = timeDiffInSeconds / 60L;
/*  43 */       return (Component)Component.translatable("mco.time.minutesAgo", new Object[] { Long.valueOf(minutes) });
/*     */     } 
/*  46 */     if (timeDiffInSeconds < 86400L) {
/*  47 */       long hours = timeDiffInSeconds / 3600L;
/*  48 */       return (Component)Component.translatable("mco.time.hoursAgo", new Object[] { Long.valueOf(hours) });
/*     */     } 
/*  51 */     long days = timeDiffInSeconds / 86400L;
/*  52 */     return (Component)Component.translatable("mco.time.daysAgo", new Object[] { Long.valueOf(days) });
/*     */   }
/*     */   
/*     */   public static Component convertToAgePresentationFromInstant(Instant date) {
/*  56 */     return convertToAgePresentation(System.currentTimeMillis() - date.toEpochMilli());
/*     */   }
/*     */   
/*     */   public static void extractPlayerFace(GuiGraphicsExtractor graphics, int x, int y, int size, UUID playerId) {
/*  60 */     PlayerFaceExtractor.extractRenderState(graphics, ResolvableProfile.createUnresolved(playerId), x, y, size);
/*     */   }
/*     */   
/*     */   public static <T> CompletableFuture<T> supplyAsync(RealmsIoFunction<T> function, Consumer<RealmsServiceException> onFailure) {
/*  64 */     return CompletableFuture.supplyAsync(() -> {
/*     */           RealmsClient client = RealmsClient.getOrCreate();
/*     */           try {
/*     */             return function.apply(client);
/*  68 */           } catch (Throwable t) {
/*     */             if (t instanceof RealmsServiceException) {
/*     */               RealmsServiceException e = (RealmsServiceException)t;
/*     */               if (onFailure != null)
/*     */                 onFailure.accept(e); 
/*     */             } else {
/*     */               LOGGER.error("Unhandled exception", t);
/*     */             } 
/*     */             throw new RuntimeException(t);
/*     */           } 
/*  78 */         }(Executor)Util.nonCriticalIoPool());
/*     */   }
/*     */   
/*     */   public static CompletableFuture<Void> runAsync(RealmsIoConsumer function, Consumer<RealmsServiceException> onFailure) {
/*  82 */     return supplyAsync(function, onFailure);
/*     */   }
/*     */   
/*     */   public static Consumer<RealmsServiceException> openScreenOnFailure(Function<RealmsServiceException, Screen> errorScreen) {
/*  86 */     Minecraft minecraft = Minecraft.getInstance();
/*  87 */     return e -> minecraft.execute(());
/*     */   }
/*     */   
/*     */   public static Consumer<RealmsServiceException> openScreenAndLogOnFailure(Function<RealmsServiceException, Screen> errorScreen, String errorMessage) {
/*  91 */     return openScreenOnFailure(errorScreen)
/*  92 */       .andThen(e -> LOGGER.error(errorMessage, (Throwable)e));
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface RealmsIoFunction<T> {
/*     */     T apply(RealmsClient param1RealmsClient) throws RealmsServiceException;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface RealmsIoConsumer extends RealmsIoFunction<Void> {
/*     */     default Void apply(RealmsClient client) throws RealmsServiceException {
/* 106 */       accept(client);
/* 107 */       return null;
/*     */     }
/*     */     
/*     */     void accept(RealmsClient param1RealmsClient) throws RealmsServiceException;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\RealmsUtil.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */