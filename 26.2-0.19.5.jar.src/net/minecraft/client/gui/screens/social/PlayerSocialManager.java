/*     */ package net.minecraft.client.gui.screens.social;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.minecraft.UserApiService;
/*     */ import com.mojang.authlib.yggdrasil.FriendsService;
/*     */ import com.mojang.authlib.yggdrasil.response.FriendDto;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class PlayerSocialManager {
/*  23 */   private static final Component FRIEND_ACTION_FAILED_MESSAGE = (Component)Component.translatable("gui.friends.error.failed.message");
/*     */   
/*  24 */   private static final Component FRIEND_ACTION_RATE_LIMITED_MESSAGE = (Component)Component.translatable("gui.friends.error.rateLimited.message");
/*     */   
/*  25 */   private static final Component FRIEND_ACTION_FORBIDDEN_MESSAGE = (Component)Component.translatable("gui.friends.error.forbidden.message");
/*     */   
/*  26 */   private static final Component FRIEND_ACTION_UNKNOWN_PROFILE = (Component)Component.translatable("gui.friends.error.user_may_lack_active_profile");
/*     */   
/*  27 */   private static final Component FRIEND_ACTION_UNAUTHORIZED = (Component)Component.translatable("gui.friends.error.unauthorized");
/*     */   
/*  28 */   private static final Component FRIEND_ACTION_UNAVAILABLE_MESSAGE = (Component)Component.translatable("gui.friends.error.unavailable.message");
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*  31 */   private final Set<UUID> hiddenPlayers = Sets.newHashSet();
/*     */   
/*     */   private final UserApiService service;
/*     */   
/*     */   private final FriendsService friendsService;
/*     */   
/*     */   private final PresenceHandler presenceHandler;
/*     */   
/*  35 */   private final Map<String, UUID> discoveredNamesToUUID = Maps.newHashMap();
/*     */   
/*     */   private boolean onlineMode;
/*     */   
/*  37 */   private CompletableFuture<?> pendingBlockListRefresh = CompletableFuture.completedFuture(null);
/*     */   
/*     */   private final RemoteFriendListUpdateHandler remoteFriendListUpdateHandler;
/*     */   
/*     */   private boolean friendListEnabled;
/*     */   
/*     */   private boolean allowFriendRequests;
/*     */   
/*     */   public PlayerSocialManager(Minecraft minecraft, UserApiService service, FriendsService friendsService, RemoteFriendListUpdateHandler remoteFriendListUpdateHandler) {
/*  43 */     this.minecraft = minecraft;
/*  44 */     this.service = service;
/*  45 */     this.friendsService = friendsService;
/*  46 */     this.remoteFriendListUpdateHandler = remoteFriendListUpdateHandler;
/*  47 */     this.friendListEnabled = minecraft.friendsEnabled();
/*  48 */     this.allowFriendRequests = minecraft.allowFriendRequests();
/*  49 */     this.presenceHandler = new PresenceHandler(minecraft, friendsService);
/*     */   }
/*     */   
/*     */   public void addFriendListUpdateListener(Runnable listener) {
/*  53 */     this.remoteFriendListUpdateHandler.addUpdateListener(listener);
/*     */   }
/*     */   
/*     */   public void removeFriendListUpdateListener(Runnable listener) {
/*  57 */     this.remoteFriendListUpdateHandler.removeUpdateListener(listener);
/*     */   }
/*     */   
/*     */   public List<PlayerData> getFriends() {
/*  61 */     return remap(this.remoteFriendListUpdateHandler.getLatestFriendData().friends());
/*     */   }
/*     */   
/*     */   public List<PlayerData> getIncomingRequests() {
/*  65 */     return remap(this.remoteFriendListUpdateHandler.getLatestFriendData().incomingRequests());
/*     */   }
/*     */   
/*     */   public List<PlayerData> getOutgoingRequests() {
/*  69 */     return remap(this.remoteFriendListUpdateHandler.getLatestFriendData().outgoingRequests());
/*     */   }
/*     */   
/*     */   public RemoteFriendListUpdateHandler.State getFriendListState() {
/*  73 */     return this.remoteFriendListUpdateHandler.getState();
/*     */   }
/*     */   
/*     */   public void hidePlayer(UUID id) {
/*  77 */     this.hiddenPlayers.add(id);
/*     */   }
/*     */   
/*     */   public void showPlayer(UUID id) {
/*  81 */     this.hiddenPlayers.remove(id);
/*     */   }
/*     */   
/*     */   public boolean shouldHideMessageFrom(UUID id) {
/*  85 */     return (isHidden(id) || isBlocked(id));
/*     */   }
/*     */   
/*     */   public boolean isHidden(UUID id) {
/*  89 */     return this.hiddenPlayers.contains(id);
/*     */   }
/*     */   
/*     */   public void startOnlineMode() {
/*  93 */     this.onlineMode = true;
/*  94 */     Objects.requireNonNull(this.service);
/*  94 */     this.pendingBlockListRefresh = this.pendingBlockListRefresh.thenRunAsync(this.service::refreshBlockList, (Executor)Util.nonCriticalIoPool());
/*     */   }
/*     */   
/*     */   public void stopOnlineMode() {
/*  98 */     this.onlineMode = false;
/*     */   }
/*     */   
/*     */   public boolean isBlocked(UUID id) {
/* 102 */     if (!this.onlineMode)
/* 103 */       return false; 
/* 105 */     this.pendingBlockListRefresh.join();
/* 106 */     return this.service.isBlockedPlayer(id);
/*     */   }
/*     */   
/*     */   public Set<UUID> getHiddenPlayers() {
/* 110 */     return this.hiddenPlayers;
/*     */   }
/*     */   
/*     */   public UUID getDiscoveredUUID(String name) {
/* 114 */     return this.discoveredNamesToUUID.getOrDefault(name, Util.NIL_UUID);
/*     */   }
/*     */   
/*     */   public void addPlayer(PlayerInfo info) {
/* 118 */     GameProfile gameProfile = info.getProfile();
/* 119 */     this.discoveredNamesToUUID.put(gameProfile.name(), gameProfile.id());
/* 121 */     Screen screen = this.minecraft.gui.screen();
/* 121 */     if (screen instanceof SocialInteractionsScreen) {
/* 121 */       SocialInteractionsScreen socialInteractionsScreen = (SocialInteractionsScreen)screen;
/* 122 */       socialInteractionsScreen.onAddPlayer(info);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<FriendsService.ResultCode> sendFriendRequest(String name) {
/* 127 */     return runAction(() -> this.friendsService.sendFriendRequest(name));
/*     */   }
/*     */   
/*     */   public void removePlayer(UUID id) {
/* 131 */     Screen screen = this.minecraft.gui.screen();
/* 131 */     if (screen instanceof SocialInteractionsScreen) {
/* 131 */       SocialInteractionsScreen socialInteractionsScreen = (SocialInteractionsScreen)screen;
/* 132 */       socialInteractionsScreen.onRemovePlayer(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<FriendsService.ResultCode> removeFriend(UUID id) {
/* 137 */     return runAction(() -> this.friendsService.removeFriend(id));
/*     */   }
/*     */   
/*     */   public CompletableFuture<FriendsService.ResultCode> acceptIncomingFriendRequest(UUID id) {
/* 141 */     return runAction(() -> this.friendsService.acceptIncomingFriendRequest(id));
/*     */   }
/*     */   
/*     */   public CompletableFuture<FriendsService.ResultCode> declineIncomingFriendRequest(UUID id) {
/* 145 */     return runAction(() -> this.friendsService.declineIncomingFriendRequest(id));
/*     */   }
/*     */   
/*     */   public CompletableFuture<FriendsService.ResultCode> revokeOutgoingFriendRequest(UUID id) {
/* 149 */     return runAction(() -> this.friendsService.revokeOutgoingFriendRequest(id));
/*     */   }
/*     */   
/*     */   public CompletableFuture<FriendsService.ResultCode> updateFriendSettings(boolean friendsListEnabled, boolean allowInvites) {
/* 153 */     return runAction(() -> this.friendsService.updateFriendSettings(friendsListEnabled, allowInvites));
/*     */   }
/*     */   
/*     */   private CompletableFuture<FriendsService.ResultCode> runAction(Supplier<FriendsService.ResultCode> action) {
/* 157 */     return CompletableFuture.supplyAsync(() -> {
/*     */           FriendsService.ResultCode result = action.get();
/*     */           handleResult(result);
/*     */           return result;
/* 161 */         }(Executor)Util.ioPool()).thenComposeAsync(result -> (result == FriendsService.ResultCode.SUCCESS) ? this.remoteFriendListUpdateHandler.forceUpdate().thenApply(()) : CompletableFuture.completedFuture(result), 
/*     */         
/* 166 */         (Executor)Util.ioPool());
/*     */   }
/*     */   
/*     */   private void handleResult(FriendsService.ResultCode result) {
/* 170 */     if (result != FriendsService.ResultCode.SUCCESS)
/* 171 */       showFailureToast(result); 
/*     */   }
/*     */   
/*     */   private void showFailureToast(FriendsService.ResultCode resultCode) {
/* 176 */     switch (resultCode) {
/*     */       default:
/* 176 */         throw new MatchException(null, null);
/*     */       case TOO_MANY_REQUESTS:
/*     */       
/*     */       case UNKNOWN_PROFILE:
/*     */       
/*     */       case UNAUTHORIZED:
/*     */       
/*     */       case FORBIDDEN:
/*     */       
/*     */       case SERVICE_NOT_AVAILABLE:
/*     */       
/*     */       case ERROR:
/*     */       
/*     */       case SUCCESS:
/*     */       case UPGRADE_NEEDED:
/*     */       case CONNECTION_ISSUE:
/*     */       case TEMPORARY_UNAVAILABLE:
/*     */       case GENERIC_ERROR:
/*     */         break;
/*     */     } 
/* 176 */     Component title = 
/*     */       
/* 183 */       null;
/* 185 */     if (title == null)
/*     */       return; 
/* 188 */     this.minecraft.execute(() -> SystemToast.addOrUpdate(this.minecraft.gui.toastManager(), SystemToast.SystemToastId.FRIEND_SYSTEM_NOTIFICATION, title, null));
/*     */   }
/*     */   
/*     */   public boolean isFriendListEnabled() {
/* 193 */     return this.friendListEnabled;
/*     */   }
/*     */   
/*     */   public void setFriendListEnabled(boolean friendListEnabled) {
/* 197 */     this.friendListEnabled = friendListEnabled;
/* 198 */     if (friendListEnabled) {
/* 199 */       this.remoteFriendListUpdateHandler.start();
/*     */     } else {
/* 201 */       this.remoteFriendListUpdateHandler.stop();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isAllowFriendRequests() {
/* 206 */     return this.allowFriendRequests;
/*     */   }
/*     */   
/*     */   public void setAllowFriendRequests(boolean allowFriendRequests) {
/* 210 */     this.allowFriendRequests = allowFriendRequests;
/*     */   }
/*     */   
/*     */   public PresenceHandler getPresenceHandler() {
/* 215 */     return this.presenceHandler;
/*     */   }
/*     */   
/*     */   public boolean isFriend(UUID uuid) {
/* 220 */     for (PlayerData playerData : getFriends()) {
/* 221 */       if (playerData.id.equals(uuid))
/* 222 */         return true; 
/*     */     } 
/* 225 */     return false;
/*     */   }
/*     */   
/*     */   public static final class PlayerData extends Record {
/*     */     private final UUID id;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     public PlayerData(UUID id, String name) {
/* 228 */       this.id = id;
/* 228 */       this.name = name;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #228	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #228	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #228	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public UUID id() {
/* 228 */       return this.id;
/*     */     }
/*     */     
/*     */     public String name() {
/* 228 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   private static List<PlayerData> remap(List<FriendDto> friends) {
/* 231 */     return friends.stream().map(friend -> new PlayerData(friend.profileId(), friend.name())).toList();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\social\PlayerSocialManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */