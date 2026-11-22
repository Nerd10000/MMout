/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.authlib.GameProfileRepository;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.TimeZone;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class CachedUserNameToIdResolver implements UserNameToIdResolver {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int GAMEPROFILES_MRU_LIMIT = 1000;
/*     */   
/*     */   private static final int GAMEPROFILES_EXPIRATION_MONTHS = 1;
/*     */   
/*     */   private boolean resolveOfflineUsers = true;
/*     */   
/*  47 */   private final Map<String, GameProfileInfo> profilesByName = new ConcurrentHashMap<>();
/*     */   
/*  48 */   private final Map<UUID, GameProfileInfo> profilesByUUID = new ConcurrentHashMap<>();
/*     */   
/*     */   private final GameProfileRepository profileRepository;
/*     */   
/*  50 */   private final Gson gson = (new GsonBuilder()).create();
/*     */   
/*     */   private final File file;
/*     */   
/*  52 */   private final AtomicLong operationCount = new AtomicLong();
/*     */   
/*     */   public CachedUserNameToIdResolver(GameProfileRepository profileRepository, File file) {
/*  55 */     this.profileRepository = profileRepository;
/*  56 */     this.file = file;
/*  58 */     Lists.reverse(load()).forEach(this::safeAdd);
/*     */   }
/*     */   
/*     */   private void safeAdd(GameProfileInfo profileInfo) {
/*  62 */     NameAndId nameAndId = profileInfo.nameAndId();
/*  63 */     profileInfo.setLastAccess(getNextOperation());
/*  64 */     this.profilesByName.put(nameAndId.name().toLowerCase(Locale.ROOT), profileInfo);
/*  65 */     this.profilesByUUID.put(nameAndId.id(), profileInfo);
/*     */   }
/*     */   
/*     */   private Optional<NameAndId> lookupGameProfile(GameProfileRepository profileRepository, String name) {
/*  69 */     if (!StringUtil.isValidPlayerName(name))
/*  70 */       return createUnknownProfile(name); 
/*  73 */     Optional<NameAndId> profile = profileRepository.findProfileByName(name).map(NameAndId::new);
/*  74 */     if (profile.isEmpty())
/*  75 */       return createUnknownProfile(name); 
/*  77 */     return profile;
/*     */   }
/*     */   
/*     */   private Optional<NameAndId> createUnknownProfile(String name) {
/*  81 */     if (this.resolveOfflineUsers)
/*  82 */       return Optional.of(NameAndId.createOffline(name)); 
/*  84 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public void resolveOfflineUsers(boolean value) {
/*  89 */     this.resolveOfflineUsers = value;
/*     */   }
/*     */   
/*     */   public void add(NameAndId nameAndId) {
/*  94 */     addInternal(nameAndId);
/*     */   }
/*     */   
/*     */   private GameProfileInfo addInternal(NameAndId profile) {
/*  98 */     Calendar c = Calendar.getInstance(TimeZone.getDefault(), Locale.ROOT);
/*  99 */     c.setTime(new Date());
/* 100 */     c.add(2, 1);
/* 101 */     Date expirationDate = c.getTime();
/* 103 */     GameProfileInfo profileInfo = new GameProfileInfo(profile, expirationDate);
/* 104 */     safeAdd(profileInfo);
/* 105 */     save();
/* 106 */     return profileInfo;
/*     */   }
/*     */   
/*     */   private long getNextOperation() {
/* 110 */     return this.operationCount.incrementAndGet();
/*     */   }
/*     */   
/*     */   public Optional<NameAndId> get(String name) {
/*     */     Optional<NameAndId> result;
/* 115 */     String userName = name.toLowerCase(Locale.ROOT);
/* 116 */     GameProfileInfo profileInfo = this.profilesByName.get(userName);
/* 118 */     boolean needsSave = false;
/* 120 */     if (profileInfo != null && (new Date()).getTime() >= profileInfo.expirationDate.getTime()) {
/* 122 */       this.profilesByUUID.remove(profileInfo.nameAndId().id());
/* 123 */       this.profilesByName.remove(profileInfo.nameAndId().name().toLowerCase(Locale.ROOT));
/* 124 */       needsSave = true;
/* 125 */       profileInfo = null;
/*     */     } 
/* 130 */     if (profileInfo != null) {
/* 131 */       profileInfo.setLastAccess(getNextOperation());
/* 132 */       result = Optional.of(profileInfo.nameAndId());
/*     */     } else {
/* 134 */       Optional<NameAndId> profile = lookupGameProfile(this.profileRepository, userName);
/* 135 */       if (profile.isPresent()) {
/* 136 */         result = Optional.of(addInternal(profile.get()).nameAndId());
/* 138 */         needsSave = false;
/*     */       } else {
/* 140 */         result = Optional.empty();
/*     */       } 
/*     */     } 
/* 144 */     if (needsSave)
/* 145 */       save(); 
/* 147 */     return result;
/*     */   }
/*     */   
/*     */   public Optional<NameAndId> get(UUID id) {
/* 152 */     GameProfileInfo profileInfo = this.profilesByUUID.get(id);
/* 153 */     if (profileInfo == null)
/* 154 */       return Optional.empty(); 
/* 156 */     profileInfo.setLastAccess(getNextOperation());
/* 157 */     return Optional.of(profileInfo.nameAndId());
/*     */   }
/*     */   
/*     */   private static DateFormat createDateFormat() {
/* 161 */     return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ROOT);
/*     */   }
/*     */   
/*     */   private List<GameProfileInfo> load() {
/* 165 */     List<GameProfileInfo> result = Lists.newArrayList();
/*     */     try {
/* 166 */       Reader reader = Files.newReader(this.file, StandardCharsets.UTF_8);
/*     */       try {
/* 167 */         JsonArray entryList = (JsonArray)this.gson.fromJson(reader, JsonArray.class);
/* 168 */         if (entryList == null) {
/* 169 */           List<GameProfileInfo> list = result;
/* 173 */           if (reader != null)
/* 173 */             reader.close(); 
/*     */           return list;
/*     */         } 
/*     */         DateFormat dateFormat = createDateFormat();
/*     */         entryList.forEach(element -> {
/*     */               Objects.requireNonNull(result);
/*     */               readGameProfile(element, dateFormat).ifPresent(result::add);
/*     */             });
/* 173 */         if (reader != null)
/* 173 */           reader.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (reader != null)
/*     */           try {
/*     */             reader.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 173 */     } catch (FileNotFoundException fileNotFoundException) {
/*     */     
/* 175 */     } catch (IOException|com.google.gson.JsonParseException e) {
/* 176 */       LOGGER.warn("Failed to load profile cache {}", this.file, e);
/*     */     } 
/* 178 */     return result;
/*     */   }
/*     */   
/*     */   public void save() {
/* 183 */     JsonArray entryList = new JsonArray();
/* 184 */     DateFormat dateFormat = createDateFormat();
/* 185 */     getTopMRUProfiles(1000).forEach(entry -> entryList.add(writeGameProfile(entry, dateFormat)));
/* 187 */     String toSave = this.gson.toJson((JsonElement)entryList);
/*     */     try {
/* 188 */       Writer writer = Files.newWriter(this.file, StandardCharsets.UTF_8);
/*     */       try {
/* 189 */         writer.write(toSave);
/* 190 */         if (writer != null)
/* 190 */           writer.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (writer != null)
/*     */           try {
/*     */             writer.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 190 */     } catch (IOException iOException) {}
/*     */   }
/*     */   
/*     */   private Stream<GameProfileInfo> getTopMRUProfiles(int limit) {
/* 196 */     return ImmutableList.copyOf(this.profilesByUUID.values()).stream().sorted(Comparator.<GameProfileInfo, Comparable>comparing(GameProfileInfo::lastAccess).reversed()).limit(limit);
/*     */   }
/*     */   
/*     */   private static JsonElement writeGameProfile(GameProfileInfo src, DateFormat dateFormat) {
/* 200 */     JsonObject object = new JsonObject();
/* 201 */     src.nameAndId().appendTo(object);
/* 202 */     object.addProperty("expiresOn", dateFormat.format(src.expirationDate()));
/* 203 */     return (JsonElement)object;
/*     */   }
/*     */   
/*     */   private static Optional<GameProfileInfo> readGameProfile(JsonElement json, DateFormat dateFormat) {
/* 207 */     if (json.isJsonObject()) {
/* 208 */       JsonObject object = json.getAsJsonObject();
/* 209 */       NameAndId nameAndId = NameAndId.fromJson(object);
/* 210 */       if (nameAndId != null) {
/* 211 */         JsonElement expirationElement = object.get("expiresOn");
/* 212 */         if (expirationElement != null) {
/* 213 */           String dateAsString = expirationElement.getAsString();
/*     */           try {
/* 215 */             Date expirationDate = dateFormat.parse(dateAsString);
/* 216 */             return Optional.of(new GameProfileInfo(nameAndId, expirationDate));
/* 217 */           } catch (ParseException e) {
/* 218 */             LOGGER.warn("Failed to parse date {}", dateAsString, e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 223 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static class GameProfileInfo {
/*     */     private final NameAndId nameAndId;
/*     */     
/*     */     private final Date expirationDate;
/*     */     
/*     */     private volatile long lastAccess;
/*     */     
/*     */     private GameProfileInfo(NameAndId nameAndId, Date expirationDate) {
/* 233 */       this.nameAndId = nameAndId;
/* 234 */       this.expirationDate = expirationDate;
/*     */     }
/*     */     
/*     */     public NameAndId nameAndId() {
/* 238 */       return this.nameAndId;
/*     */     }
/*     */     
/*     */     public Date expirationDate() {
/* 242 */       return this.expirationDate;
/*     */     }
/*     */     
/*     */     public void setLastAccess(long currentOperation) {
/* 246 */       this.lastAccess = currentOperation;
/*     */     }
/*     */     
/*     */     public long lastAccess() {
/* 250 */       return this.lastAccess;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\CachedUserNameToIdResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */