/*     */ package com.mojang.realmsclient.util.task;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsJoinInformation;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.exception.RetryCallException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsBrokenWorldScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoConnectTaskScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsTermsScreen;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.screens.GenericMessageScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.resources.server.DownloadedPackSource;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GetServerDetailsTask extends LongRunningTask {
/*  33 */   private static final Component APPLYING_PACK_TEXT = (Component)Component.translatable("multiplayer.applyingPack");
/*     */   
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  37 */   private static final Component TITLE = (Component)Component.translatable("mco.connect.connecting");
/*     */   
/*     */   private final RealmsServer server;
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   public GetServerDetailsTask(Screen lastScreen, RealmsServer server) {
/*  43 */     this.lastScreen = lastScreen;
/*  44 */     this.server = server;
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     RealmsJoinInformation address;
/*     */     try {
/*  51 */       address = fetchServerAddress();
/*  52 */     } catch (CancellationException e) {
/*  53 */       LOGGER.info("User aborted connecting to realms");
/*     */       return;
/*  55 */     } catch (RealmsServiceException e) {
/*     */       boolean isOwner;
/*  56 */       switch (e.realmsError.errorCode()) {
/*     */         case 6002:
/*  58 */           setScreen((Screen)new RealmsTermsScreen(this.lastScreen, this.server));
/*     */           return;
/*     */         case 6006:
/*  62 */           isOwner = Minecraft.getInstance().isLocalPlayer(this.server.ownerUUID);
/*  63 */           setScreen(isOwner ? 
/*  64 */               (Screen)new RealmsBrokenWorldScreen(this.lastScreen, this.server.id, this.server.isMinigameActive()) : 
/*  65 */               (Screen)new RealmsGenericErrorScreen((Component)Component.translatable("mco.brokenworld.nonowner.title"), (Component)Component.translatable("mco.brokenworld.nonowner.error"), this.lastScreen));
/*     */           return;
/*     */       } 
/*  69 */       error(e);
/*  70 */       LOGGER.error("Couldn't connect to world", (Throwable)e);
/*     */       return;
/*  74 */     } catch (TimeoutException e) {
/*  75 */       error((Component)Component.translatable("mco.errorMessage.connectionFailure"));
/*     */       return;
/*  77 */     } catch (Exception e) {
/*  78 */       LOGGER.error("Couldn't connect to world", e);
/*  79 */       error(e);
/*     */       return;
/*     */     } 
/*  83 */     if (address.address() == null) {
/*  84 */       error((Component)Component.translatable("mco.errorMessage.connectionFailure"));
/*     */       return;
/*     */     } 
/*  88 */     boolean requiresResourcePack = (address.resourcePackUrl() != null && address.resourcePackHash() != null);
/*  92 */     Screen nextScreen = requiresResourcePack ? (Screen)resourcePackDownloadConfirmationScreen(address, generatePackId(this.server), this::connectScreen) : (Screen)connectScreen(address);
/*  94 */     setScreen(nextScreen);
/*     */   }
/*     */   
/*     */   private static UUID generatePackId(RealmsServer serverData) {
/*  98 */     if (serverData.minigameName != null)
/*  99 */       return UUID.nameUUIDFromBytes(("minigame:" + serverData.minigameName).getBytes(StandardCharsets.UTF_8)); 
/* 101 */     return UUID.nameUUIDFromBytes(("realms:" + (String)Objects.<String>requireNonNullElse(serverData.name, "") + ":" + serverData.activeSlot).getBytes(StandardCharsets.UTF_8));
/*     */   }
/*     */   
/*     */   public Component getTitle() {
/* 106 */     return TITLE;
/*     */   }
/*     */   
/*     */   private RealmsJoinInformation fetchServerAddress() throws RealmsServiceException, TimeoutException, CancellationException {
/* 110 */     RealmsClient client = RealmsClient.getOrCreate();
/* 111 */     for (int i = 0; i < 40; i++) {
/* 112 */       if (aborted())
/* 113 */         throw new CancellationException(); 
/*     */       try {
/* 117 */         return client.join(this.server.id);
/* 118 */       } catch (RetryCallException e) {
/* 119 */         pause(e.delaySeconds);
/*     */       } 
/*     */     } 
/* 122 */     throw new TimeoutException();
/*     */   }
/*     */   
/*     */   public RealmsLongRunningMcoTaskScreen connectScreen(RealmsJoinInformation address) {
/* 126 */     return (RealmsLongRunningMcoTaskScreen)new RealmsLongRunningMcoConnectTaskScreen(this.lastScreen, address, new ConnectTask(this.lastScreen, this.server, address));
/*     */   }
/*     */   
/*     */   private PopupScreen resourcePackDownloadConfirmationScreen(RealmsJoinInformation address, UUID packId, Function<RealmsJoinInformation, Screen> onCompletionScreen) {
/* 130 */     MutableComponent mutableComponent = Component.translatable("mco.configure.world.resourcepack.question");
/* 131 */     return RealmsPopups.infoPopupScreen(this.lastScreen, (Component)mutableComponent, popupScreen -> {
/*     */           setScreen((Screen)new GenericMessageScreen(APPLYING_PACK_TEXT));
/*     */           scheduleResourcePackDownload(address, packId).thenRun(()).exceptionally(());
/*     */         });
/*     */   }
/*     */   
/*     */   private CompletableFuture<?> scheduleResourcePackDownload(RealmsJoinInformation address, UUID packId) {
/*     */     try {
/* 150 */       if (address.resourcePackUrl() == null)
/* 151 */         return CompletableFuture.failedFuture(new IllegalStateException("resourcePackUrl was null")); 
/* 153 */       if (address.resourcePackHash() == null)
/* 154 */         return CompletableFuture.failedFuture(new IllegalStateException("resourcePackHash was null")); 
/* 156 */       DownloadedPackSource packSource = Minecraft.getInstance().getDownloadedPackSource();
/* 157 */       CompletableFuture<Void> result = packSource.waitForPackFeedback(packId);
/* 158 */       packSource.allowServerPacks();
/* 159 */       packSource.pushPack(packId, new URL(address.resourcePackUrl()), address.resourcePackHash());
/* 160 */       return result;
/* 161 */     } catch (Exception e) {
/* 162 */       return CompletableFuture.failedFuture(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclien\\util\task\GetServerDetailsTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */