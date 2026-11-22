/*     */ package net.minecraft.client.gui.screens.social;
/*     */ 
/*     */ import com.mojang.authlib.yggdrasil.FriendsService;
/*     */ import com.mojang.authlib.yggdrasil.response.FriendData;
/*     */ import com.mojang.authlib.yggdrasil.response.FriendDto;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.time.Duration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.toasts.FriendToast;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class RemoteFriendListUpdateHandler {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  30 */   private static final long FOREGROUND_INTERVAL_NANOS = TimeUnit.MINUTES.toNanos(1L);
/*     */   
/*     */   private static final long BACKGROUND_INTERVAL_MULTIPLIER = 5L;
/*     */   
/*     */   private static final long POLL_INTERVAL_SECONDS = 1L;
/*     */   
/*     */   private final FriendsService friendsService;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final ScheduledExecutorService scheduler;
/*     */   
/*  37 */   private final AtomicBoolean updateInProgress = new AtomicBoolean(false);
/*     */   
/*  38 */   private final AtomicBoolean enabled = new AtomicBoolean(false);
/*     */   
/*  39 */   private final Set<Runnable> updateListeners = new CopyOnWriteArraySet<>();
/*     */   
/*  41 */   private volatile long lastUpdateNanos = 0L;
/*     */   
/*  42 */   private volatile FriendData latestFriendData = FriendData.empty();
/*     */   
/*  43 */   private volatile State state = State.LOADING;
/*     */   
/*  44 */   private volatile Set<FriendDto> knownFriends = new HashSet<>();
/*     */   
/*  45 */   private volatile Set<FriendDto> knownIncoming = new HashSet<>();
/*     */   
/*  46 */   private volatile Set<FriendDto> knownOutgoing = new HashSet<>();
/*     */   
/*     */   private ScheduledFuture<?> scheduledTick;
/*     */   
/*     */   public RemoteFriendListUpdateHandler(FriendsService friendsService, Minecraft minecraft) {
/*  50 */     this.friendsService = friendsService;
/*  51 */     this.minecraft = minecraft;
/*  52 */     this.scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
/*     */           Thread thread = new Thread(runnable, "Friends List");
/*     */           thread.setDaemon(true);
/*     */           return thread;
/*     */         });
/*     */   }
/*     */   
/*     */   private void runBackgroundTick() {
/*  60 */     if (this.updateInProgress.get() || !this.enabled.get())
/*     */       return; 
/*  64 */     long now = System.nanoTime();
/*  65 */     if (this.lastUpdateNanos == 0L || now - this.lastUpdateNanos >= getUpdateIntervalNanos())
/*  66 */       runUpdateFriendDataInternal(); 
/*     */   }
/*     */   
/*     */   public FriendData getLatestFriendData() {
/*  71 */     return this.latestFriendData;
/*     */   }
/*     */   
/*     */   public State getState() {
/*  75 */     return this.state;
/*     */   }
/*     */   
/*     */   public void addUpdateListener(Runnable listener) {
/*  79 */     this.updateListeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeUpdateListener(Runnable listener) {
/*  83 */     this.updateListeners.remove(listener);
/*     */   }
/*     */   
/*     */   private long getUpdateIntervalNanos() {
/*  89 */     long foregroundNanos = ((Long)this.friendsService.getFriendsPollInterval().map(Duration::toNanos).orElse(Long.valueOf(FOREGROUND_INTERVAL_NANOS))).longValue();
/*  91 */     Screen screen = this.minecraft.gui.screen();
/*  92 */     if (screen instanceof net.minecraft.client.gui.screens.friends.FriendsOverlayScreen)
/*  93 */       return foregroundNanos; 
/*  95 */     return foregroundNanos * 5L;
/*     */   }
/*     */   
/*     */   void runUpdateFriendDataInternal() {
/* 100 */     if (!this.updateInProgress.compareAndSet(false, true)) {
/* 101 */       LOGGER.debug("Attempted to run Friends List update but update is already in progress");
/*     */       return;
/*     */     } 
/* 105 */     LOGGER.debug("Performing Friends List update");
/* 106 */     AtomicReference<FriendData> friendData = new AtomicReference<>(FriendData.empty());
/* 107 */     boolean shouldNotifyListeners = false;
/*     */     try {
/* 109 */       Objects.requireNonNull(friendData);
/* 109 */       FriendsService.ResultCode resultCode = this.friendsService.getFriendData(friendData::set);
/* 110 */       State newState = mapResultCodeToState(resultCode);
/* 111 */       State previousState = this.state;
/* 112 */       boolean stateTransition = (previousState != newState);
/* 113 */       this.state = newState;
/* 114 */       if (resultCode != FriendsService.ResultCode.SUCCESS) {
/* 115 */         LOGGER.warn("Friends List update failed with result code: {}", resultCode);
/* 116 */         shouldNotifyListeners = true;
/*     */         return;
/*     */       } 
/* 120 */       FriendData data = friendData.get();
/* 121 */       this.latestFriendData = data;
/* 122 */       boolean dataChanged = detectChangesAndShowToast(data, previousState);
/* 123 */       shouldNotifyListeners = (dataChanged || stateTransition);
/* 124 */     } catch (Throwable e) {
/* 125 */       LOGGER.warn("Failed to update friend data", e);
/*     */     } finally {
/* 127 */       this.updateInProgress.set(false);
/* 128 */       this.lastUpdateNanos = System.nanoTime();
/* 129 */       if (shouldNotifyListeners)
/* 130 */         notifyListeners(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static State mapResultCodeToState(FriendsService.ResultCode resultCode) {
/* 136 */     switch (resultCode) {
/*     */       default:
/* 136 */         throw new MatchException(null, null);
/*     */       case TEMPORARY_UNAVAILABLE:
/*     */       case FORBIDDEN:
/*     */       case SERVICE_NOT_AVAILABLE:
/*     */       case TOO_MANY_REQUESTS:
/*     */       
/*     */       case CONNECTION_ISSUE:
/*     */       
/*     */       case UPGRADE_NEEDED:
/*     */       
/*     */       case UNKNOWN_PROFILE:
/*     */       
/*     */       case UNAUTHORIZED:
/*     */       
/*     */       case GENERIC_ERROR:
/*     */       case ERROR:
/*     */       
/*     */       case SUCCESS:
/*     */         break;
/*     */     } 
/* 136 */     return 
/*     */       
/* 143 */       State.SUCCESS;
/*     */   }
/*     */   
/*     */   private void notifyListeners() {
/* 148 */     if (this.updateListeners.isEmpty())
/*     */       return; 
/* 152 */     LOGGER.debug("Notifying {} Friends List update listeners", Integer.valueOf(this.updateListeners.size()));
/* 153 */     this.minecraft.execute(() -> {
/*     */           for (Runnable listener : this.updateListeners) {
/*     */             try {
/*     */               listener.run();
/* 157 */             } catch (Throwable e) {
/*     */               LOGGER.warn("Friends List callback failed", e);
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private boolean detectChangesAndShowToast(FriendData friendData, State previousState) {
/* 165 */     Set<FriendDto> currentFriends = new HashSet<>(friendData.friends());
/* 166 */     Set<FriendDto> currentIncoming = new HashSet<>(friendData.incomingRequests());
/* 167 */     Set<FriendDto> currentOutgoing = new HashSet<>(friendData.outgoingRequests());
/* 169 */     if (previousState != State.SUCCESS) {
/* 170 */       this.knownFriends = currentFriends;
/* 171 */       this.knownIncoming = currentIncoming;
/* 172 */       this.knownOutgoing = currentOutgoing;
/* 173 */       return true;
/*     */     } 
/* 176 */     if (!isInGameAndToastsDisabled()) {
/* 177 */       for (FriendDto friendDto : currentFriends) {
/* 178 */         if (!this.knownFriends.contains(friendDto)) {
/* 179 */           if (this.knownOutgoing.contains(friendDto) || this.knownIncoming.contains(friendDto)) {
/* 180 */             emitToastWithSkin(friendDto.profileId(), friendDto.name(), FriendToast::showFriendRequestAccepted);
/*     */             continue;
/*     */           } 
/* 182 */           emitToastWithSkin(friendDto.profileId(), friendDto.name(), FriendToast::showFriendAdded);
/*     */         } 
/*     */       } 
/* 186 */       for (FriendDto friendDto : currentIncoming) {
/* 187 */         if (!this.knownIncoming.contains(friendDto) && !currentFriends.contains(friendDto))
/* 188 */           emitToastWithSkin(friendDto.profileId(), friendDto.name(), FriendToast::showFriendRequestReceived); 
/*     */       } 
/* 191 */       for (FriendDto friendDto : currentOutgoing) {
/* 192 */         if (!this.knownOutgoing.contains(friendDto) && !currentFriends.contains(friendDto))
/* 193 */           this.minecraft.execute(() -> FriendToast.showFriendRequestSent(this.minecraft, friendDto.name())); 
/*     */       } 
/*     */     } 
/* 198 */     boolean hasChanges = (!this.knownFriends.equals(currentFriends) || !this.knownIncoming.equals(currentIncoming) || !this.knownOutgoing.equals(currentOutgoing));
/* 199 */     this.knownFriends = currentFriends;
/* 200 */     this.knownIncoming = currentIncoming;
/* 201 */     this.knownOutgoing = currentOutgoing;
/* 202 */     return hasChanges;
/*     */   }
/*     */   
/*     */   private boolean isInGameAndToastsDisabled() {
/* 206 */     return (this.minecraft.level != null && !((Boolean)this.minecraft.options.inGameNotification().get()).booleanValue());
/*     */   }
/*     */   
/*     */   private void emitToastWithSkin(UUID playerId, String playerName, FriendToast.SkinToastEmitter emitter) {
/* 210 */     this.minecraft.execute(() -> emitter.emit(this.minecraft, playerName, playerId));
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> forceUpdate() {
/* 214 */     if (!this.enabled.get() || this.scheduler.isShutdown())
/* 215 */       return CompletableFuture.completedFuture(null); 
/* 217 */     CompletableFuture<Void> future = new CompletableFuture<>();
/*     */     try {
/* 219 */       this.scheduler.execute(() -> {
/*     */             try {
/*     */               runUpdateFriendDataInternal();
/*     */             } finally {
/*     */               future.complete(null);
/*     */             } 
/*     */           });
/* 226 */     } catch (Throwable e) {
/* 227 */       LOGGER.warn("Failed to schedule forced Friends List update", e);
/* 228 */       future.complete(null);
/*     */     } 
/* 230 */     return future;
/*     */   }
/*     */   
/*     */   public synchronized void start() {
/* 234 */     if (this.scheduler.isShutdown()) {
/* 235 */       LOGGER.warn("Attempted to start Friends List updater but scheduler is already shut down");
/*     */       return;
/*     */     } 
/* 238 */     if (!this.enabled.compareAndSet(false, true))
/*     */       return; 
/* 241 */     if (this.scheduledTick == null || this.scheduledTick.isCancelled() || this.scheduledTick.isDone())
/* 242 */       this.scheduledTick = this.scheduler.scheduleWithFixedDelay(this::runBackgroundTick, 0L, 1L, TimeUnit.SECONDS); 
/*     */   }
/*     */   
/*     */   public synchronized void stop() {
/* 247 */     this.enabled.set(false);
/* 248 */     if (this.scheduledTick != null) {
/* 249 */       this.scheduledTick.cancel(false);
/* 250 */       this.scheduledTick = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void close() {
/* 255 */     stop();
/* 256 */     this.scheduler.shutdownNow();
/*     */   }
/*     */   
/*     */   public enum State {
/* 260 */     LOADING, UPGRADE_NEEDED, CONNECTION_ISSUE, USER_MAY_LACK_ACTIVE_PROFILE, UNAUTHORIZED, TEMPORARY_UNAVAILABLE, GENERIC_ERROR, SUCCESS;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\social\RemoteFriendListUpdateHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */