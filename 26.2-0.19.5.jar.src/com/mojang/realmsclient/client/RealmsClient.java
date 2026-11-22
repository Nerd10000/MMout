/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.dto.BackupList;
/*     */ import com.mojang.realmsclient.dto.GuardedSerializer;
/*     */ import com.mojang.realmsclient.dto.Ops;
/*     */ import com.mojang.realmsclient.dto.OutboundPlayer;
/*     */ import com.mojang.realmsclient.dto.PendingInvite;
/*     */ import com.mojang.realmsclient.dto.PendingInvitesList;
/*     */ import com.mojang.realmsclient.dto.PingResult;
/*     */ import com.mojang.realmsclient.dto.PlayerInfo;
/*     */ import com.mojang.realmsclient.dto.PreferredRegionsDto;
/*     */ import com.mojang.realmsclient.dto.RealmsConfigurationDto;
/*     */ import com.mojang.realmsclient.dto.RealmsDescriptionDto;
/*     */ import com.mojang.realmsclient.dto.RealmsJoinInformation;
/*     */ import com.mojang.realmsclient.dto.RealmsNews;
/*     */ import com.mojang.realmsclient.dto.RealmsNotification;
/*     */ import com.mojang.realmsclient.dto.RealmsRegion;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsServerList;
/*     */ import com.mojang.realmsclient.dto.RealmsServerPlayerLists;
/*     */ import com.mojang.realmsclient.dto.RealmsSetting;
/*     */ import com.mojang.realmsclient.dto.RealmsSlotUpdateDto;
/*     */ import com.mojang.realmsclient.dto.RealmsWorldOptions;
/*     */ import com.mojang.realmsclient.dto.RealmsWorldResetDto;
/*     */ import com.mojang.realmsclient.dto.ReflectionBasedSerialization;
/*     */ import com.mojang.realmsclient.dto.RegionDataDto;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreference;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreferenceDto;
/*     */ import com.mojang.realmsclient.dto.Subscription;
/*     */ import com.mojang.realmsclient.dto.UploadInfo;
/*     */ import com.mojang.realmsclient.dto.WorldDownload;
/*     */ import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
/*     */ import com.mojang.realmsclient.exception.RealmsHttpException;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.exception.RetryCallException;
/*     */ import com.mojang.realmsclient.util.UploadTokenCache;
/*     */ import com.mojang.util.UndashedUuid;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.WorldVersion;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsClient {
/*     */   public enum Environment {
/*  61 */     PRODUCTION("pc.realms.minecraft.net", "java.frontendlegacy.realms.minecraft-services.net", "https"),
/*  62 */     STAGE("pc-stage.realms.minecraft.net", "java.frontendlegacy.stage-c2a40e62.realms.minecraft-services.net", "https"),
/*  63 */     LOCAL("localhost:8080", "localhost:8080", "http");
/*     */     
/*     */     public final String baseUrl;
/*     */     
/*     */     public final String alternativeUrl;
/*     */     
/*     */     public final String protocol;
/*     */     
/*     */     Environment(String baseUrl, String alternativeUrl, String protocol) {
/*  70 */       this.baseUrl = baseUrl;
/*  71 */       this.alternativeUrl = alternativeUrl;
/*  72 */       this.protocol = protocol;
/*     */     }
/*     */     
/*     */     public static Optional<Environment> byName(String name) {
/*  76 */       switch (name.toLowerCase(Locale.ROOT)) {
/*     */         case "production":
/*     */         
/*     */         case "local":
/*     */         
/*     */         case "stage":
/*     */         case "staging":
/*     */         
/*     */       } 
/*  76 */       return 
/*     */         
/*  80 */         Optional.empty();
/*     */     }
/*     */   }
/*     */   
/*  85 */   public static final Environment ENVIRONMENT = Optional.<String>ofNullable(System.getenv("realms.environment"))
/*  86 */     .or(() -> Optional.ofNullable(System.getProperty("realms.environment")))
/*  87 */     .flatMap(Environment::byName)
/*  88 */     .orElse(Environment.PRODUCTION);
/*     */   
/*  90 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  92 */   private static volatile RealmsClient realmsClientInstance = null;
/*     */   
/*     */   private final CompletableFuture<Set<String>> featureFlags;
/*     */   
/*     */   private final String sessionId;
/*     */   
/*     */   private final String username;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private static final String WORLDS_RESOURCE_PATH = "worlds";
/*     */   
/*     */   private static final String INVITES_RESOURCE_PATH = "invites";
/*     */   
/*     */   private static final String MCO_RESOURCE_PATH = "mco";
/*     */   
/*     */   private static final String SUBSCRIPTION_RESOURCE = "subscriptions";
/*     */   
/*     */   private static final String ACTIVITIES_RESOURCE = "activities";
/*     */   
/*     */   private static final String OPS_RESOURCE = "ops";
/*     */   
/*     */   private static final String REGIONS_RESOURCE = "regions/ping/stat";
/*     */   
/*     */   private static final String PREFERRED_REGION_RESOURCE = "regions/preferredRegions";
/*     */   
/*     */   private static final String TRIALS_RESOURCE = "trial";
/*     */   
/*     */   private static final String NOTIFICATIONS_RESOURCE = "notifications";
/*     */   
/*     */   private static final String FEATURE_FLAGS_RESOURCE = "feature/v1";
/*     */   
/*     */   private static final String PATH_LIST_ALL_REALMS = "/listUserWorldsOfType/any";
/*     */   
/*     */   private static final String PATH_CREATE_SNAPSHOT_REALM = "/$PARENT_WORLD_ID/createPrereleaseRealm";
/*     */   
/*     */   private static final String PATH_SNAPSHOT_ELIGIBLE_REALMS = "/listPrereleaseEligibleWorlds";
/*     */   
/*     */   private static final String PATH_INITIALIZE = "/$WORLD_ID/initialize";
/*     */   
/*     */   private static final String PATH_GET_LIVESTATS = "/liveplayerlist";
/*     */   
/*     */   private static final String PATH_GET_SUBSCRIPTION = "/$WORLD_ID";
/*     */   
/*     */   private static final String PATH_OP = "/$WORLD_ID/$PROFILE_UUID";
/*     */   
/*     */   private static final String PATH_PUT_INTO_MINIGAMES_MODE = "/minigames/$MINIGAME_ID/$WORLD_ID";
/*     */   
/*     */   private static final String PATH_AVAILABLE = "/available";
/*     */   
/*     */   private static final String PATH_TEMPLATES = "/templates/$WORLD_TYPE";
/*     */   
/*     */   private static final String PATH_WORLD_JOIN = "/v1/$ID/join/pc";
/*     */   
/*     */   private static final String PATH_WORLD_GET = "/$ID";
/*     */   
/*     */   private static final String PATH_WORLD_INVITES = "/$WORLD_ID";
/*     */   
/*     */   private static final String PATH_WORLD_UNINVITE = "/$WORLD_ID/invite/$UUID";
/*     */   
/*     */   private static final String PATH_PENDING_INVITES = "/pending";
/*     */   
/*     */   private static final String PATH_ACCEPT_INVITE = "/accept/$INVITATION_ID";
/*     */   
/*     */   private static final String PATH_REJECT_INVITE = "/reject/$INVITATION_ID";
/*     */   
/*     */   private static final String PATH_UNINVITE_MYSELF = "/$WORLD_ID";
/*     */   
/*     */   private static final String PATH_WORLD_CONFIGURE = "/$WORLD_ID/configuration";
/*     */   
/*     */   private static final String PATH_SLOT = "/$WORLD_ID/slot/$SLOT_ID";
/*     */   
/*     */   private static final String PATH_WORLD_OPEN = "/$WORLD_ID/open";
/*     */   
/*     */   private static final String PATH_WORLD_CLOSE = "/$WORLD_ID/close";
/*     */   
/*     */   private static final String PATH_WORLD_RESET = "/$WORLD_ID/reset";
/*     */   
/*     */   private static final String PATH_DELETE_WORLD = "/$WORLD_ID";
/*     */   
/*     */   private static final String PATH_WORLD_BACKUPS = "/$WORLD_ID/backups";
/*     */   
/*     */   private static final String PATH_WORLD_DOWNLOAD = "/$WORLD_ID/slot/$SLOT_ID/download";
/*     */   
/*     */   private static final String PATH_WORLD_UPLOAD = "/$WORLD_ID/backups/upload";
/*     */   
/*     */   private static final String PATH_CLIENT_COMPATIBLE = "/client/compatible";
/*     */   
/*     */   private static final String PATH_TOS_AGREED = "/tos/agreed";
/*     */   
/*     */   private static final String PATH_NEWS = "/v1/news";
/*     */   
/*     */   private static final String PATH_MARK_NOTIFICATIONS_SEEN = "/seen";
/*     */   
/*     */   private static final String PATH_DISMISS_NOTIFICATIONS = "/dismiss";
/*     */   
/* 143 */   private static final GuardedSerializer GSON = new GuardedSerializer();
/*     */   
/*     */   public static RealmsClient getOrCreate() {
/* 146 */     Minecraft minecraft = Minecraft.getInstance();
/* 147 */     return getOrCreate(minecraft);
/*     */   }
/*     */   
/*     */   public static RealmsClient getOrCreate(Minecraft minecraft) {
/* 151 */     String username = minecraft.getUser().getName();
/* 152 */     String sessionId = minecraft.getUser().getSessionId();
/* 153 */     RealmsClient realmsClient = realmsClientInstance;
/* 154 */     if (realmsClient != null)
/* 155 */       return realmsClient; 
/* 157 */     synchronized (RealmsClient.class) {
/* 158 */       RealmsClient rc = realmsClientInstance;
/* 159 */       if (rc != null)
/* 160 */         return rc; 
/* 162 */       rc = new RealmsClient(sessionId, username, minecraft);
/* 163 */       realmsClientInstance = rc;
/* 164 */       return rc;
/*     */     } 
/*     */   }
/*     */   
/*     */   private RealmsClient(String sessionId, String username, Minecraft minecraft) {
/* 169 */     this.sessionId = sessionId;
/* 170 */     this.username = username;
/* 171 */     this.minecraft = minecraft;
/* 173 */     RealmsClientConfig.setProxy(minecraft.getProxy());
/* 174 */     this.featureFlags = CompletableFuture.supplyAsync(this::fetchFeatureFlags, (Executor)Util.nonCriticalIoPool());
/*     */   }
/*     */   
/*     */   public Set<String> getFeatureFlags() {
/* 178 */     return this.featureFlags.join();
/*     */   }
/*     */   
/*     */   private Set<String> fetchFeatureFlags() {
/* 182 */     if (Minecraft.getInstance().isOfflineDeveloperMode())
/* 183 */       return Set.of(); 
/* 185 */     String asciiUrl = url("feature/v1", null, false);
/*     */     try {
/* 187 */       String returnJson = execute(Request.get(asciiUrl, 5000, 10000));
/* 188 */       JsonArray object = LenientJsonParser.parse(returnJson).getAsJsonArray();
/* 191 */       Set<String> featureFlags = (Set<String>)object.asList().stream().map(JsonElement::getAsString).collect(Collectors.toSet());
/* 192 */       LOGGER.debug("Fetched Realms feature flags: {}", featureFlags);
/* 193 */       return featureFlags;
/* 194 */     } catch (RealmsServiceException e) {
/* 195 */       LOGGER.error("Failed to fetch Realms feature flags", (Throwable)e);
/* 196 */     } catch (Exception e) {
/* 197 */       LOGGER.error("Could not parse Realms feature flags", e);
/*     */     } 
/* 199 */     return Set.of();
/*     */   }
/*     */   
/*     */   public RealmsServerList listRealms() throws RealmsServiceException {
/* 203 */     String asciiUrl = url("worlds");
/* 204 */     if (RealmsMainScreen.isSnapshot())
/* 205 */       asciiUrl = asciiUrl + "/listUserWorldsOfType/any"; 
/* 207 */     String json = execute(Request.get(asciiUrl));
/* 208 */     return RealmsServerList.parse(GSON, json);
/*     */   }
/*     */   
/*     */   public List<RealmsServer> listSnapshotEligibleRealms() throws RealmsServiceException {
/* 212 */     String asciiUrl = url("worlds/listPrereleaseEligibleWorlds");
/* 213 */     String json = execute(Request.get(asciiUrl));
/* 214 */     return RealmsServerList.parse(GSON, json).servers();
/*     */   }
/*     */   
/*     */   public RealmsServer createSnapshotRealm(Long parentId) throws RealmsServiceException {
/* 218 */     String parentIdString = String.valueOf(parentId);
/* 219 */     String url = url("worlds" + "/$PARENT_WORLD_ID/createPrereleaseRealm".replace("$PARENT_WORLD_ID", parentIdString));
/* 220 */     return RealmsServer.parse(GSON, execute(Request.post(url, parentIdString)));
/*     */   }
/*     */   
/*     */   public List<RealmsNotification> getNotifications() throws RealmsServiceException {
/* 224 */     String endpoint = url("notifications");
/* 225 */     String responseJson = execute(Request.get(endpoint));
/* 226 */     return RealmsNotification.parseList(responseJson);
/*     */   }
/*     */   
/*     */   private static JsonArray uuidListToJsonArray(List<UUID> uuids) {
/* 230 */     JsonArray array = new JsonArray();
/* 231 */     for (UUID uuid : uuids) {
/* 232 */       if (uuid != null)
/* 233 */         array.add(uuid.toString()); 
/*     */     } 
/* 236 */     return array;
/*     */   }
/*     */   
/*     */   public void notificationsSeen(List<UUID> notificationUuids) throws RealmsServiceException {
/* 240 */     String endpoint = url("notifications/seen");
/* 241 */     execute(Request.post(endpoint, GSON.toJson((JsonElement)uuidListToJsonArray(notificationUuids))));
/*     */   }
/*     */   
/*     */   public void notificationsDismiss(List<UUID> notificationUuids) throws RealmsServiceException {
/* 245 */     String endpoint = url("notifications/dismiss");
/* 246 */     execute(Request.post(endpoint, GSON.toJson((JsonElement)uuidListToJsonArray(notificationUuids))));
/*     */   }
/*     */   
/*     */   public RealmsServer getOwnRealm(long realmId) throws RealmsServiceException {
/* 250 */     String asciiUrl = url("worlds" + "/$ID".replace("$ID", String.valueOf(realmId)));
/* 251 */     String json = execute(Request.get(asciiUrl));
/* 252 */     return RealmsServer.parse(GSON, json);
/*     */   }
/*     */   
/*     */   public PreferredRegionsDto getPreferredRegionSelections() throws RealmsServiceException {
/* 256 */     String asciiUrl = url("regions/preferredRegions");
/* 257 */     String json = execute(Request.get(asciiUrl));
/*     */     try {
/* 259 */       PreferredRegionsDto preferredRegionsDto = (PreferredRegionsDto)GSON.fromJson(json, PreferredRegionsDto.class);
/* 260 */       if (preferredRegionsDto == null)
/* 261 */         return PreferredRegionsDto.empty(); 
/* 266 */       Set<RealmsRegion> regionsInResponse = (Set<RealmsRegion>)preferredRegionsDto.regionData().stream().map(RegionDataDto::region).collect(Collectors.toSet());
/* 268 */       for (RealmsRegion region : RealmsRegion.values()) {
/* 269 */         if (region != RealmsRegion.INVALID_REGION && !regionsInResponse.contains(region))
/* 270 */           LOGGER.debug("No realms region matching {} in server response", region); 
/*     */       } 
/* 274 */       return preferredRegionsDto;
/* 275 */     } catch (Exception e) {
/* 276 */       LOGGER.error("Could not parse PreferredRegionSelections", e);
/* 278 */       return PreferredRegionsDto.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public RealmsServerPlayerLists getLiveStats() throws RealmsServiceException {
/* 282 */     String asciiUrl = url("activities/liveplayerlist");
/* 283 */     String json = execute(Request.get(asciiUrl));
/* 284 */     return RealmsServerPlayerLists.parse(json);
/*     */   }
/*     */   
/*     */   public RealmsJoinInformation join(long realmId) throws RealmsServiceException {
/* 288 */     String asciiUrl = url("worlds" + "/v1/$ID/join/pc".replace("$ID", "" + realmId));
/* 289 */     String json = execute(Request.get(asciiUrl, 5000, 30000));
/* 290 */     return RealmsJoinInformation.parse(GSON, json);
/*     */   }
/*     */   
/*     */   public void initializeRealm(long realmId, String name, String motd) throws RealmsServiceException {
/* 294 */     RealmsDescriptionDto realmsDescription = new RealmsDescriptionDto(name, motd);
/* 295 */     String asciiUrl = url("worlds" + "/$WORLD_ID/initialize".replace("$WORLD_ID", String.valueOf(realmId)));
/* 296 */     String json = GSON.toJson((ReflectionBasedSerialization)realmsDescription);
/* 297 */     execute(Request.post(asciiUrl, json, 5000, 10000));
/*     */   }
/*     */   
/*     */   public boolean hasParentalConsent() throws RealmsServiceException {
/* 301 */     String asciiUrl = url("mco/available");
/* 302 */     String json = execute(Request.get(asciiUrl));
/* 303 */     return Boolean.parseBoolean(json);
/*     */   }
/*     */   
/*     */   public CompatibleVersionResponse clientCompatible() throws RealmsServiceException {
/*     */     CompatibleVersionResponse result;
/* 307 */     String asciiUrl = url("mco/client/compatible");
/* 308 */     String response = execute(Request.get(asciiUrl));
/*     */     try {
/* 312 */       result = CompatibleVersionResponse.valueOf(response);
/* 313 */     } catch (IllegalArgumentException ignored) {
/* 314 */       throw new RealmsServiceException(RealmsError.CustomError.unknownCompatibilityResponse(response));
/*     */     } 
/* 317 */     return result;
/*     */   }
/*     */   
/*     */   public void uninvite(long realmId, UUID profileId) throws RealmsServiceException {
/* 321 */     String asciiUrl = url("invites" + "/$WORLD_ID/invite/$UUID".replace("$WORLD_ID", String.valueOf(realmId)).replace("$UUID", UndashedUuid.toString(profileId)));
/* 322 */     execute(Request.delete(asciiUrl));
/*     */   }
/*     */   
/*     */   public void uninviteMyselfFrom(long realmId) throws RealmsServiceException {
/* 326 */     String asciiUrl = url("invites" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(realmId)));
/* 327 */     execute(Request.delete(asciiUrl));
/*     */   }
/*     */   
/*     */   public List<PlayerInfo> invite(long realmId, String profileName) throws RealmsServiceException {
/* 331 */     OutboundPlayer playerInfo = new OutboundPlayer();
/* 332 */     playerInfo.name = profileName;
/* 334 */     String asciiUrl = url("invites" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(realmId)));
/* 335 */     String json = execute(Request.post(asciiUrl, GSON.toJson((ReflectionBasedSerialization)playerInfo)));
/* 336 */     return (RealmsServer.parse(GSON, json)).players;
/*     */   }
/*     */   
/*     */   public BackupList backupsFor(long realmId) throws RealmsServiceException {
/* 340 */     String asciiUrl = url("worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(realmId)));
/* 341 */     String json = execute(Request.get(asciiUrl));
/* 342 */     return BackupList.parse(json);
/*     */   }
/*     */   
/*     */   public void updateConfiguration(long realmId, String name, String description, RegionSelectionPreferenceDto regionSelectionPreference, int slotId, RealmsWorldOptions options, List<RealmsSetting> settings) throws RealmsServiceException {
/* 346 */     RegionSelectionPreferenceDto preferenceDto = (regionSelectionPreference != null) ? regionSelectionPreference : new RegionSelectionPreferenceDto(RegionSelectionPreference.DEFAULT_SELECTION, null);
/* 347 */     RealmsDescriptionDto realmsDescription = new RealmsDescriptionDto(name, description);
/* 348 */     RealmsSlotUpdateDto slotUpdateDto = new RealmsSlotUpdateDto(slotId, options, RealmsSetting.isHardcore(settings));
/* 349 */     RealmsConfigurationDto realmsConfiguration = new RealmsConfigurationDto(slotUpdateDto, settings, preferenceDto, realmsDescription);
/* 350 */     String asciiUrl = url("worlds" + "/$WORLD_ID/configuration".replace("$WORLD_ID", String.valueOf(realmId)));
/* 351 */     execute(Request.post(asciiUrl, GSON.toJson((ReflectionBasedSerialization)realmsConfiguration)));
/*     */   }
/*     */   
/*     */   public void updateSlot(long realmId, int slotId, RealmsWorldOptions options, List<RealmsSetting> settings) throws RealmsServiceException {
/* 355 */     String asciiUrl = url("worlds" + "/$WORLD_ID/slot/$SLOT_ID".replace("$WORLD_ID", String.valueOf(realmId)).replace("$SLOT_ID", String.valueOf(slotId)));
/* 356 */     String json = GSON.toJson((ReflectionBasedSerialization)new RealmsSlotUpdateDto(slotId, options, RealmsSetting.isHardcore(settings)));
/* 357 */     execute(Request.post(asciiUrl, json));
/*     */   }
/*     */   
/*     */   public boolean switchSlot(long realmId, int slot) throws RealmsServiceException {
/* 362 */     String asciiUrl = url("worlds" + "/$WORLD_ID/slot/$SLOT_ID".replace("$WORLD_ID", String.valueOf(realmId)).replace("$SLOT_ID", String.valueOf(slot)));
/* 363 */     String json = execute(Request.put(asciiUrl, ""));
/* 364 */     return Boolean.valueOf(json).booleanValue();
/*     */   }
/*     */   
/*     */   public void restoreWorld(long realmId, String backupId) throws RealmsServiceException {
/* 368 */     String asciiUrl = url("worlds" + "/$WORLD_ID/backups".replace("$WORLD_ID", String.valueOf(realmId)), "backupId=" + backupId);
/* 369 */     execute(Request.put(asciiUrl, "", 40000, 600000));
/*     */   }
/*     */   
/*     */   public WorldTemplatePaginatedList fetchWorldTemplates(int page, int pageSize, RealmsServer.WorldType type) throws RealmsServiceException {
/* 373 */     String asciiUrl = url("worlds" + "/templates/$WORLD_TYPE".replace("$WORLD_TYPE", type.toString()), String.format(Locale.ROOT, "page=%d&pageSize=%d", new Object[] { Integer.valueOf(page), Integer.valueOf(pageSize) }));
/* 374 */     String json = execute(Request.get(asciiUrl));
/* 375 */     return WorldTemplatePaginatedList.parse(json);
/*     */   }
/*     */   
/*     */   public Boolean putIntoMinigameMode(long realmId, String minigameId) throws RealmsServiceException {
/* 379 */     String path = "/minigames/$MINIGAME_ID/$WORLD_ID".replace("$MINIGAME_ID", minigameId).replace("$WORLD_ID", String.valueOf(realmId));
/* 380 */     String asciiUrl = url("worlds" + path);
/* 381 */     return Boolean.valueOf(execute(Request.put(asciiUrl, "")));
/*     */   }
/*     */   
/*     */   public Ops op(long realmId, UUID profileId) throws RealmsServiceException {
/* 385 */     String path = "/$WORLD_ID/$PROFILE_UUID".replace("$WORLD_ID", String.valueOf(realmId)).replace("$PROFILE_UUID", UndashedUuid.toString(profileId));
/* 386 */     String asciiUrl = url("ops" + path);
/* 387 */     return Ops.parse(execute(Request.post(asciiUrl, "")));
/*     */   }
/*     */   
/*     */   public Ops deop(long realmId, UUID profileId) throws RealmsServiceException {
/* 391 */     String path = "/$WORLD_ID/$PROFILE_UUID".replace("$WORLD_ID", String.valueOf(realmId)).replace("$PROFILE_UUID", UndashedUuid.toString(profileId));
/* 392 */     String asciiUrl = url("ops" + path);
/* 393 */     return Ops.parse(execute(Request.delete(asciiUrl)));
/*     */   }
/*     */   
/*     */   public Boolean open(long realmId) throws RealmsServiceException {
/* 397 */     String asciiUrl = url("worlds" + "/$WORLD_ID/open".replace("$WORLD_ID", String.valueOf(realmId)));
/* 398 */     String json = execute(Request.put(asciiUrl, ""));
/* 399 */     return Boolean.valueOf(json);
/*     */   }
/*     */   
/*     */   public Boolean close(long realmId) throws RealmsServiceException {
/* 403 */     String asciiUrl = url("worlds" + "/$WORLD_ID/close".replace("$WORLD_ID", String.valueOf(realmId)));
/* 404 */     String json = execute(Request.put(asciiUrl, ""));
/* 405 */     return Boolean.valueOf(json);
/*     */   }
/*     */   
/*     */   public Boolean resetWorldWithTemplate(long realmId, String worldTemplateId) throws RealmsServiceException {
/* 409 */     RealmsWorldResetDto worldReset = new RealmsWorldResetDto(null, Long.valueOf(worldTemplateId).longValue(), -1, false, Set.of());
/* 410 */     String asciiUrl = url("worlds" + "/$WORLD_ID/reset".replace("$WORLD_ID", String.valueOf(realmId)));
/* 411 */     String json = execute(Request.post(asciiUrl, GSON.toJson((ReflectionBasedSerialization)worldReset), 30000, 80000));
/* 412 */     return Boolean.valueOf(json);
/*     */   }
/*     */   
/*     */   public Subscription subscriptionFor(long realmId) throws RealmsServiceException {
/* 416 */     String asciiUrl = url("subscriptions" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(realmId)));
/* 417 */     String json = execute(Request.get(asciiUrl));
/* 418 */     return Subscription.parse(json);
/*     */   }
/*     */   
/*     */   public int pendingInvitesCount() throws RealmsServiceException {
/* 423 */     return pendingInvites().pendingInvites().size();
/*     */   }
/*     */   
/*     */   public PendingInvitesList pendingInvites() throws RealmsServiceException {
/* 427 */     String asciiUrl = url("invites/pending");
/* 428 */     String json = execute(Request.get(asciiUrl));
/* 429 */     PendingInvitesList list = PendingInvitesList.parse(json);
/* 430 */     list.pendingInvites().removeIf(this::isBlocked);
/* 431 */     return list;
/*     */   }
/*     */   
/*     */   private boolean isBlocked(PendingInvite invite) {
/* 435 */     return this.minecraft.getPlayerSocialManager().isBlocked(invite.realmOwnerUuid());
/*     */   }
/*     */   
/*     */   public void acceptInvitation(String invitationId) throws RealmsServiceException {
/* 439 */     String asciiUrl = url("invites" + "/accept/$INVITATION_ID".replace("$INVITATION_ID", invitationId));
/* 440 */     execute(Request.put(asciiUrl, ""));
/*     */   }
/*     */   
/*     */   public WorldDownload requestDownloadInfo(long realmId, int slotId) throws RealmsServiceException {
/* 444 */     String asciiUrl = url("worlds" + "/$WORLD_ID/slot/$SLOT_ID/download".replace("$WORLD_ID", String.valueOf(realmId)).replace("$SLOT_ID", String.valueOf(slotId)));
/* 445 */     String json = execute(Request.get(asciiUrl));
/* 446 */     return WorldDownload.parse(json);
/*     */   }
/*     */   
/*     */   public UploadInfo requestUploadInfo(long realmId) throws RealmsServiceException {
/* 450 */     String asciiUrl = url("worlds" + "/$WORLD_ID/backups/upload".replace("$WORLD_ID", String.valueOf(realmId)));
/* 451 */     String uploadToken = UploadTokenCache.get(realmId);
/* 452 */     UploadInfo uploadInfo = UploadInfo.parse(execute(Request.put(asciiUrl, UploadInfo.createRequest(uploadToken))));
/* 453 */     if (uploadInfo != null)
/* 454 */       UploadTokenCache.put(realmId, uploadInfo.token()); 
/* 456 */     return uploadInfo;
/*     */   }
/*     */   
/*     */   public void rejectInvitation(String invitationId) throws RealmsServiceException {
/* 460 */     String asciiUrl = url("invites" + "/reject/$INVITATION_ID".replace("$INVITATION_ID", invitationId));
/* 461 */     execute(Request.put(asciiUrl, ""));
/*     */   }
/*     */   
/*     */   public void agreeToTos() throws RealmsServiceException {
/* 465 */     String asciiUrl = url("mco/tos/agreed");
/* 466 */     execute(Request.post(asciiUrl, ""));
/*     */   }
/*     */   
/*     */   public RealmsNews getNews() throws RealmsServiceException {
/* 470 */     String asciiUrl = url("mco/v1/news");
/* 471 */     String returnJson = execute(Request.get(asciiUrl, 5000, 10000));
/* 472 */     return RealmsNews.parse(returnJson);
/*     */   }
/*     */   
/*     */   public void sendPingResults(PingResult pingResult) throws RealmsServiceException {
/* 476 */     String asciiUrl = url("regions/ping/stat");
/* 477 */     execute(Request.post(asciiUrl, GSON.toJson((ReflectionBasedSerialization)pingResult)));
/*     */   }
/*     */   
/*     */   public Boolean trialAvailable() throws RealmsServiceException {
/* 481 */     String asciiUrl = url("trial");
/* 482 */     String json = execute(Request.get(asciiUrl));
/* 483 */     return Boolean.valueOf(json);
/*     */   }
/*     */   
/*     */   public void deleteRealm(long realmId) throws RealmsServiceException {
/* 487 */     String asciiUrl = url("worlds" + "/$WORLD_ID".replace("$WORLD_ID", String.valueOf(realmId)));
/* 488 */     execute(Request.delete(asciiUrl));
/*     */   }
/*     */   
/*     */   private String url(String path) throws RealmsServiceException {
/* 492 */     return url(path, null);
/*     */   }
/*     */   
/*     */   private String url(String path, String queryString) {
/* 496 */     return url(path, queryString, getFeatureFlags().contains("realms_in_aks"));
/*     */   }
/*     */   
/*     */   private static String url(String path, String queryString, boolean useAlternativeURL) {
/*     */     try {
/* 501 */       return (new URI(ENVIRONMENT.protocol, useAlternativeURL ? ENVIRONMENT.alternativeUrl : ENVIRONMENT.baseUrl, "/" + path, queryString, null)).toASCIIString();
/* 502 */     } catch (URISyntaxException e) {
/* 503 */       throw new IllegalArgumentException(path, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String execute(Request<?> request) throws RealmsServiceException {
/* 508 */     request.cookie("sid", this.sessionId);
/* 509 */     request.cookie("user", this.username);
/* 510 */     WorldVersion version = SharedConstants.getCurrentVersion();
/* 511 */     request.cookie("version", version.name());
/* 512 */     request.addVersionHeaders(version.protocolVersion(), RealmsMainScreen.isSnapshot());
/*     */     try {
/* 515 */       int responseCode = request.responseCode();
/* 517 */       if (responseCode == 503 || responseCode == 277) {
/* 518 */         int pauseTime = request.getRetryAfterHeader();
/* 519 */         throw new RetryCallException(pauseTime, responseCode);
/*     */       } 
/* 522 */       String responseText = request.text();
/* 524 */       if (responseCode < 200 || responseCode >= 300) {
/* 525 */         if (responseCode == 401) {
/* 526 */           String authenticationHeader = request.getHeader("WWW-Authenticate");
/* 527 */           LOGGER.info("Could not authorize you against Realms server: {}", authenticationHeader);
/* 528 */           throw new RealmsServiceException(new RealmsError.AuthenticationError(authenticationHeader));
/*     */         } 
/* 531 */         String contentType = request.connection.getContentType();
/* 532 */         if (contentType != null && contentType.startsWith("text/html"))
/* 533 */           throw new RealmsServiceException(RealmsError.CustomError.htmlPayload(responseCode, responseText)); 
/* 535 */         RealmsError error = RealmsError.parse(responseCode, responseText);
/* 536 */         throw new RealmsServiceException(error);
/*     */       } 
/* 539 */       return responseText;
/* 540 */     } catch (RealmsHttpException e) {
/* 541 */       throw new RealmsServiceException(RealmsError.CustomError.connectivityError(e));
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum CompatibleVersionResponse {
/* 546 */     COMPATIBLE, OUTDATED, OTHER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\RealmsClient.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */