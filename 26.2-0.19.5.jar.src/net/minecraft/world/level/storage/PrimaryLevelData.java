/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PrimaryLevelData implements ServerLevelData, WorldData {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String LEVEL_NAME = "LevelName";
/*     */   
/*     */   protected static final String OLD_PLAYER = "Player";
/*     */   
/*     */   protected static final String SINGLEPLAYER_UUID = "singleplayer_uuid";
/*     */   
/*     */   protected static final String OLD_WORLD_GEN_SETTINGS = "WorldGenSettings";
/*     */   
/*     */   private LevelSettings settings;
/*     */   
/*     */   private final SpecialWorldProperty specialWorldProperty;
/*     */   
/*     */   private final Lifecycle worldGenSettingsLifecycle;
/*     */   
/*     */   private LevelData.RespawnData respawnData;
/*     */   
/*     */   private long gameTime;
/*     */   
/*     */   private final UUID singlePlayerUUID;
/*     */   
/*     */   private final int version;
/*     */   
/*     */   private boolean initialized;
/*     */   
/*     */   private final Set<String> knownServerBrands;
/*     */   
/*     */   private boolean wasModded;
/*     */   
/*     */   private final Set<String> removedFeatureFlags;
/*     */   
/*     */   @Deprecated
/*     */   public enum SpecialWorldProperty {
/*  44 */     NONE, FLAT, DEBUG;
/*     */   }
/*     */   
/*     */   private PrimaryLevelData(UUID singlePlayerUUID, boolean wasModded, LevelData.RespawnData respawnData, long gameTime, int version, boolean initialized, Set<String> knownServerBrands, Set<String> removedFeatureFlags, LevelSettings settings, SpecialWorldProperty specialWorldProperty, Lifecycle worldGenSettingsLifecycle) {
/*  81 */     this.wasModded = wasModded;
/*  82 */     this.respawnData = respawnData;
/*  83 */     this.gameTime = gameTime;
/*  84 */     this.version = version;
/*  85 */     this.initialized = initialized;
/*  86 */     this.knownServerBrands = knownServerBrands;
/*  87 */     this.removedFeatureFlags = removedFeatureFlags;
/*  88 */     this.singlePlayerUUID = singlePlayerUUID;
/*  89 */     this.settings = settings;
/*  90 */     this.specialWorldProperty = specialWorldProperty;
/*  91 */     this.worldGenSettingsLifecycle = worldGenSettingsLifecycle;
/*     */   }
/*     */   
/*     */   public PrimaryLevelData(LevelSettings levelSettings, SpecialWorldProperty specialWorldProperty, Lifecycle lifecycle) {
/*  95 */     this(null, false, LevelData.RespawnData.DEFAULT, 0L, 19133, false, 
/*     */         
/* 102 */         Sets.newLinkedHashSet(), new HashSet<>(), levelSettings
/*     */         
/* 104 */         .copy(), specialWorldProperty, lifecycle);
/*     */   }
/*     */   
/*     */   public static <T> PrimaryLevelData parse(Dynamic<T> input, LevelSettings settings, SpecialWorldProperty specialWorldProperty, Lifecycle worldGenSettingsLifecycle) {
/* 111 */     long gameTime = input.get("Time").asLong(0L);
/* 112 */     LevelVersion levelVersion = LevelVersion.parse(input);
/* 115 */     Objects.requireNonNull(UUIDUtil.CODEC);
/* 115 */     return new PrimaryLevelData(input.get("singleplayer_uuid").flatMap(UUIDUtil.CODEC::parse).result().orElse(null), input
/* 116 */         .get("WasModded").asBoolean(false), input
/* 117 */         .get("spawn").read((Decoder)LevelData.RespawnData.CODEC).result().orElse(LevelData.RespawnData.DEFAULT), gameTime, levelVersion
/*     */         
/* 119 */         .levelDataVersion(), input
/* 120 */         .get("initialized").asBoolean(true), (Set<String>)input
/* 121 */         .get("ServerBrands").asStream().flatMap(b -> b.asString().result().stream()).collect(Collectors.toCollection(Sets::newLinkedHashSet)), (Set<String>)input
/* 122 */         .get("removed_features").asStream().flatMap(b -> b.asString().result().stream()).collect(Collectors.toSet()), settings, specialWorldProperty, worldGenSettingsLifecycle);
/*     */   }
/*     */   
/*     */   public CompoundTag createTag(UUID singlePlayerUUID) {
/* 131 */     if (singlePlayerUUID == null)
/* 132 */       singlePlayerUUID = this.singlePlayerUUID; 
/* 134 */     CompoundTag tag = new CompoundTag();
/* 135 */     setTagData(tag, singlePlayerUUID);
/* 136 */     return tag;
/*     */   }
/*     */   
/*     */   private void setTagData(CompoundTag tag, UUID singlePlayerUUID) {
/* 140 */     tag.put("ServerBrands", (Tag)stringCollectionToTag(this.knownServerBrands));
/* 141 */     tag.putBoolean("WasModded", this.wasModded);
/* 143 */     if (!this.removedFeatureFlags.isEmpty())
/* 144 */       tag.put("removed_features", (Tag)stringCollectionToTag(this.removedFeatureFlags)); 
/* 147 */     writeVersionTag(tag);
/* 149 */     NbtUtils.addCurrentDataVersion(tag);
/* 151 */     tag.putInt("GameType", this.settings.gameType().getId());
/* 152 */     tag.store("spawn", LevelData.RespawnData.CODEC, this.respawnData);
/* 153 */     tag.putLong("Time", this.gameTime);
/* 154 */     writeLastPlayed(tag);
/* 155 */     tag.putString("LevelName", this.settings.levelName());
/* 156 */     tag.putInt("version", 19133);
/* 157 */     tag.putBoolean("allowCommands", this.settings.allowCommands());
/* 158 */     tag.putBoolean("initialized", this.initialized);
/* 159 */     tag.store("difficulty_settings", LevelSettings.DifficultySettings.CODEC, this.settings.difficultySettings());
/* 161 */     if (singlePlayerUUID != null)
/* 162 */       tag.storeNullable("singleplayer_uuid", UUIDUtil.CODEC, singlePlayerUUID); 
/* 165 */     tag.store(WorldDataConfiguration.MAP_CODEC, this.settings.dataConfiguration());
/*     */   }
/*     */   
/*     */   public static void writeLastPlayed(CompoundTag tag) {
/* 169 */     tag.putLong("LastPlayed", Util.getEpochMillis());
/*     */   }
/*     */   
/*     */   public static Dynamic<?> writeLastPlayed(Dynamic<?> tag) {
/* 173 */     return tag.set("LastPlayed", tag.createLong(Util.getEpochMillis()));
/*     */   }
/*     */   
/*     */   public static void writeVersionTag(CompoundTag tag) {
/* 177 */     CompoundTag worldVersion = new CompoundTag();
/* 178 */     worldVersion.putString("Name", SharedConstants.getCurrentVersion().name());
/* 179 */     worldVersion.putInt("Id", SharedConstants.getCurrentVersion().dataVersion().version());
/* 180 */     worldVersion.putBoolean("Snapshot", !SharedConstants.getCurrentVersion().stable());
/* 181 */     worldVersion.putString("Series", SharedConstants.getCurrentVersion().dataVersion().series());
/* 182 */     tag.put("Version", (Tag)worldVersion);
/*     */   }
/*     */   
/*     */   public static Dynamic<?> writeVersionTag(Dynamic<?> tag) {
/* 190 */     Dynamic<?> worldVersion = tag.emptyMap().set("Name", tag.createString(SharedConstants.getCurrentVersion().name())).set("Id", tag.createInt(SharedConstants.getCurrentVersion().dataVersion().version())).set("Snapshot", tag.createBoolean(!SharedConstants.getCurrentVersion().stable())).set("Series", tag.createString(SharedConstants.getCurrentVersion().dataVersion().series()));
/* 191 */     return tag.set("Version", worldVersion);
/*     */   }
/*     */   
/*     */   private static ListTag stringCollectionToTag(Set<String> values) {
/* 195 */     ListTag result = new ListTag();
/* 196 */     Objects.requireNonNull(result);
/* 196 */     values.stream().map(StringTag::valueOf).forEach(result::add);
/* 197 */     return result;
/*     */   }
/*     */   
/*     */   public LevelData.RespawnData getRespawnData() {
/* 202 */     return this.respawnData;
/*     */   }
/*     */   
/*     */   public long getGameTime() {
/* 207 */     return this.gameTime;
/*     */   }
/*     */   
/*     */   public UUID getSinglePlayerUUID() {
/* 212 */     return this.singlePlayerUUID;
/*     */   }
/*     */   
/*     */   public void setGameTime(long time) {
/* 217 */     this.gameTime = time;
/*     */   }
/*     */   
/*     */   public void setSpawn(LevelData.RespawnData respawnData) {
/* 222 */     this.respawnData = respawnData;
/*     */   }
/*     */   
/*     */   public String getLevelName() {
/* 227 */     return this.settings.levelName();
/*     */   }
/*     */   
/*     */   public int getVersion() {
/* 232 */     return this.version;
/*     */   }
/*     */   
/*     */   public GameType getGameType() {
/* 237 */     return this.settings.gameType();
/*     */   }
/*     */   
/*     */   public void setGameType(GameType gameType) {
/* 242 */     this.settings = this.settings.withGameType(gameType);
/*     */   }
/*     */   
/*     */   public boolean isHardcore() {
/* 247 */     return this.settings.difficultySettings().hardcore();
/*     */   }
/*     */   
/*     */   public boolean isAllowCommands() {
/* 252 */     return this.settings.allowCommands();
/*     */   }
/*     */   
/*     */   public void setAllowCommands(boolean allowCommands) {
/* 257 */     this.settings = this.settings.withAllowCommands(allowCommands);
/*     */   }
/*     */   
/*     */   public boolean isInitialized() {
/* 262 */     return this.initialized;
/*     */   }
/*     */   
/*     */   public void setInitialized(boolean initialized) {
/* 267 */     this.initialized = initialized;
/*     */   }
/*     */   
/*     */   public Difficulty getDifficulty() {
/* 272 */     return this.settings.difficultySettings().difficulty();
/*     */   }
/*     */   
/*     */   public void setDifficulty(Difficulty difficulty) {
/* 277 */     this.settings = this.settings.withDifficulty(difficulty);
/*     */   }
/*     */   
/*     */   public boolean isDifficultyLocked() {
/* 282 */     return this.settings.difficultySettings().locked();
/*     */   }
/*     */   
/*     */   public void setDifficultyLocked(boolean difficultyLocked) {
/* 287 */     this.settings = this.settings.withDifficultyLock(difficultyLocked);
/*     */   }
/*     */   
/*     */   public void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 292 */     super.fillCrashReportCategory(category, levelHeightAccessor);
/* 293 */     fillCrashReportCategory(category);
/*     */   }
/*     */   
/*     */   public boolean isFlatWorld() {
/* 298 */     return (this.specialWorldProperty == SpecialWorldProperty.FLAT);
/*     */   }
/*     */   
/*     */   public boolean isDebugWorld() {
/* 303 */     return (this.specialWorldProperty == SpecialWorldProperty.DEBUG);
/*     */   }
/*     */   
/*     */   public Lifecycle worldGenSettingsLifecycle() {
/* 308 */     return this.worldGenSettingsLifecycle;
/*     */   }
/*     */   
/*     */   public WorldDataConfiguration getDataConfiguration() {
/* 313 */     return this.settings.dataConfiguration();
/*     */   }
/*     */   
/*     */   public void setDataConfiguration(WorldDataConfiguration dataConfiguration) {
/* 318 */     this.settings = this.settings.withDataConfiguration(dataConfiguration);
/*     */   }
/*     */   
/*     */   public void setModdedInfo(String serverBrand, boolean isModded) {
/* 323 */     this.knownServerBrands.add(serverBrand);
/* 324 */     this.wasModded |= isModded;
/*     */   }
/*     */   
/*     */   public boolean wasModded() {
/* 329 */     return this.wasModded;
/*     */   }
/*     */   
/*     */   public Set<String> getKnownServerBrands() {
/* 334 */     return (Set<String>)ImmutableSet.copyOf(this.knownServerBrands);
/*     */   }
/*     */   
/*     */   public Set<String> getRemovedFeatureFlags() {
/* 339 */     return Set.copyOf(this.removedFeatureFlags);
/*     */   }
/*     */   
/*     */   public ServerLevelData overworldData() {
/* 344 */     return this;
/*     */   }
/*     */   
/*     */   public LevelSettings getLevelSettings() {
/* 349 */     return this.settings.copy();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\PrimaryLevelData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */