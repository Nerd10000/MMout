/*     */ package net.minecraft.client.gui.components.debug;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DebugScreenEntryList {
/*  31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int DEFAULT_DEBUG_PROFILE_VERSION = 4649;
/*     */   
/*  34 */   private final Map<Identifier, DebugScreenEntryStatus> allStatuses = new HashMap<>();
/*     */   
/*  36 */   private final List<Identifier> currentlyEnabled = new ArrayList<>();
/*     */   
/*     */   private boolean isOverlayVisible = false;
/*     */   
/*     */   private DebugScreenProfile profile;
/*     */   
/*     */   private final File debugProfileFile;
/*     */   
/*     */   private long currentlyEnabledVersion;
/*     */   
/*     */   private final Codec<SerializedOptions> codec;
/*     */   
/*     */   public DebugScreenEntryList(File workingDirectory, DataFixer dataFixer) {
/*  44 */     this.debugProfileFile = new File(workingDirectory, "debug-profile.json");
/*  45 */     this.codec = DataFixTypes.DEBUG_PROFILE.wrapCodec(SerializedOptions.CODEC, dataFixer, 4649);
/*  46 */     load();
/*     */   }
/*     */   
/*     */   public void load() {
/*     */     try {
/*  51 */       if (!this.debugProfileFile.isFile()) {
/*  52 */         resetToProfile(DebugScreenProfile.DEFAULT);
/*  53 */         rebuildCurrentList();
/*     */         return;
/*     */       } 
/*  56 */       Dynamic<JsonElement> data = new Dynamic((DynamicOps)JsonOps.INSTANCE, StrictJsonParser.parse(FileUtils.readFileToString(this.debugProfileFile, StandardCharsets.UTF_8)));
/*  57 */       SerializedOptions serializedOptions = (SerializedOptions)this.codec.parse(data).getOrThrow(error -> new IOException("Could not parse debug profile JSON: " + error));
/*  58 */       if (serializedOptions.profile().isPresent()) {
/*  59 */         resetToProfile(serializedOptions.profile().get());
/*     */       } else {
/*  61 */         resetStatuses(serializedOptions.custom().orElse(Map.of()));
/*  62 */         this.profile = null;
/*     */       } 
/*  64 */     } catch (IOException|com.google.gson.JsonSyntaxException e) {
/*  65 */       LOGGER.error("Couldn't read debug profile file {}, resetting to default", this.debugProfileFile, e);
/*  66 */       resetToProfile(DebugScreenProfile.DEFAULT);
/*  67 */       save();
/*     */     } 
/*  69 */     rebuildCurrentList();
/*     */   }
/*     */   
/*     */   private void resetStatuses(Map<Identifier, DebugScreenEntryStatus> newEntries) {
/*  73 */     this.allStatuses.clear();
/*  74 */     this.allStatuses.putAll(newEntries);
/*     */   }
/*     */   
/*     */   private void resetToProfile(DebugScreenProfile profile) {
/*  78 */     this.profile = profile;
/*  79 */     resetStatuses(DebugScreenEntries.PROFILES.get(profile));
/*     */   }
/*     */   
/*     */   public void loadProfile(DebugScreenProfile profile) {
/*  83 */     resetToProfile(profile);
/*  84 */     rebuildCurrentList();
/*     */   }
/*     */   
/*     */   public DebugScreenEntryStatus getStatus(Identifier location) {
/*  88 */     return this.allStatuses.getOrDefault(location, DebugScreenEntryStatus.NEVER);
/*     */   }
/*     */   
/*     */   public boolean isCurrentlyEnabled(Identifier location) {
/*  92 */     return this.currentlyEnabled.contains(location);
/*     */   }
/*     */   
/*     */   public void setStatus(Identifier location, DebugScreenEntryStatus status) {
/*  96 */     this.profile = null;
/*  97 */     this.allStatuses.put(location, status);
/*  98 */     rebuildCurrentList();
/*  99 */     save();
/*     */   }
/*     */   
/*     */   public boolean toggleStatus(Identifier location) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield allStatuses : Ljava/util/Map;
/*     */     //   4: aload_1
/*     */     //   5: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   10: checkcast net/minecraft/client/gui/components/debug/DebugScreenEntryStatus
/*     */     //   13: astore_2
/*     */     //   14: aload_2
/*     */     //   15: astore_3
/*     */     //   16: iconst_0
/*     */     //   17: istore #4
/*     */     //   19: aload_3
/*     */     //   20: iload #4
/*     */     //   22: <illegal opcode> enumSwitch : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;I)I
/*     */     //   27: tableswitch default -> 121, -1 -> 121, 0 -> 56, 1 -> 66, 2 -> 93
/*     */     //   56: aload_0
/*     */     //   57: aload_1
/*     */     //   58: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.NEVER : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   61: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   64: iconst_0
/*     */     //   65: ireturn
/*     */     //   66: aload_0
/*     */     //   67: getfield isOverlayVisible : Z
/*     */     //   70: ifeq -> 83
/*     */     //   73: aload_0
/*     */     //   74: aload_1
/*     */     //   75: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.NEVER : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   78: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   81: iconst_0
/*     */     //   82: ireturn
/*     */     //   83: aload_0
/*     */     //   84: aload_1
/*     */     //   85: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.ALWAYS_ON : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   88: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   91: iconst_1
/*     */     //   92: ireturn
/*     */     //   93: aload_0
/*     */     //   94: getfield isOverlayVisible : Z
/*     */     //   97: ifeq -> 111
/*     */     //   100: aload_0
/*     */     //   101: aload_1
/*     */     //   102: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.IN_OVERLAY : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   105: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   108: goto -> 119
/*     */     //   111: aload_0
/*     */     //   112: aload_1
/*     */     //   113: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.ALWAYS_ON : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   116: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   119: iconst_1
/*     */     //   120: ireturn
/*     */     //   121: aload_0
/*     */     //   122: aload_1
/*     */     //   123: getstatic net/minecraft/client/gui/components/debug/DebugScreenEntryStatus.ALWAYS_ON : Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */     //   126: invokevirtual setStatus : (Lnet/minecraft/resources/Identifier;Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;)V
/*     */     //   129: iconst_1
/*     */     //   130: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #103	-> 0
/*     */     //   #104	-> 14
/*     */     //   #106	-> 56
/*     */     //   #107	-> 64
/*     */     //   #110	-> 66
/*     */     //   #111	-> 73
/*     */     //   #112	-> 81
/*     */     //   #114	-> 83
/*     */     //   #115	-> 91
/*     */     //   #119	-> 93
/*     */     //   #120	-> 100
/*     */     //   #122	-> 111
/*     */     //   #124	-> 119
/*     */     //   #127	-> 121
/*     */     //   #128	-> 129
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	131	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList;
/*     */     //   0	131	1	location	Lnet/minecraft/resources/Identifier;
/*     */     //   14	117	2	status	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryStatus;
/*     */   }
/*     */   
/*     */   public Collection<Identifier> getCurrentlyEnabled() {
/* 134 */     return List.copyOf(this.currentlyEnabled);
/*     */   }
/*     */   
/*     */   public void toggleDebugOverlay() {
/* 138 */     setOverlayVisible(!this.isOverlayVisible);
/*     */   }
/*     */   
/*     */   public void setOverlayVisible(boolean visible) {
/* 142 */     if (this.isOverlayVisible != visible) {
/* 143 */       this.isOverlayVisible = visible;
/* 144 */       rebuildCurrentList();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isOverlayVisible() {
/* 149 */     return this.isOverlayVisible;
/*     */   }
/*     */   
/*     */   public void rebuildCurrentList() {
/* 153 */     this.currentlyEnabled.clear();
/* 155 */     boolean isReducedDebugInfo = Minecraft.getInstance().showOnlyReducedInfo();
/* 156 */     this.allStatuses.forEach((key, value) -> {
/*     */           if (value == DebugScreenEntryStatus.ALWAYS_ON || (this.isOverlayVisible && value == DebugScreenEntryStatus.IN_OVERLAY)) {
/*     */             DebugScreenEntry debug = DebugScreenEntries.getEntry(key);
/*     */             if (debug != null && debug.isAllowed(isReducedDebugInfo))
/*     */               this.currentlyEnabled.add(key); 
/*     */           } 
/*     */         });
/* 165 */     this.currentlyEnabled.sort(Comparator.naturalOrder());
/* 166 */     this.currentlyEnabledVersion++;
/*     */   }
/*     */   
/*     */   public long getCurrentlyEnabledVersion() {
/* 170 */     return this.currentlyEnabledVersion;
/*     */   }
/*     */   
/*     */   public boolean isUsingProfile(DebugScreenProfile profile) {
/* 174 */     return (this.profile == profile);
/*     */   }
/*     */   
/*     */   public void save() {
/* 178 */     SerializedOptions serializedOptions = new SerializedOptions(Optional.ofNullable(this.profile), (this.profile == null) ? Optional.<Map<Identifier, DebugScreenEntryStatus>>of(this.allStatuses) : Optional.<Map<Identifier, DebugScreenEntryStatus>>empty());
/*     */     try {
/* 180 */       FileUtils.writeStringToFile(this.debugProfileFile, ((JsonElement)this.codec.encodeStart((DynamicOps)JsonOps.INSTANCE, serializedOptions).getOrThrow()).toString(), StandardCharsets.UTF_8);
/* 181 */     } catch (IOException e) {
/* 182 */       LOGGER.error("Failed to save debug profile file {}", this.debugProfileFile, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class SerializedOptions extends Record {
/*     */     private final Optional<DebugScreenProfile> profile;
/*     */     
/*     */     private final Optional<Map<Identifier, DebugScreenEntryStatus>> custom;
/*     */     
/*     */     private SerializedOptions(Optional<DebugScreenProfile> profile, Optional<Map<Identifier, DebugScreenEntryStatus>> custom) {
/* 186 */       this.profile = profile;
/* 186 */       this.custom = custom;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #186	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #186	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #186	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/debug/DebugScreenEntryList$SerializedOptions;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Optional<DebugScreenProfile> profile() {
/* 186 */       return this.profile;
/*     */     }
/*     */     
/*     */     public Optional<Map<Identifier, DebugScreenEntryStatus>> custom() {
/* 186 */       return this.custom;
/*     */     }
/*     */     
/* 187 */     private static final Codec<Map<Identifier, DebugScreenEntryStatus>> CUSTOM_ENTRIES_CODEC = (Codec<Map<Identifier, DebugScreenEntryStatus>>)Codec.unboundedMap(Identifier.CODEC, DebugScreenEntryStatus.CODEC);
/*     */     
/*     */     public static final Codec<SerializedOptions> CODEC;
/*     */     
/*     */     static {
/* 189 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)DebugScreenProfile.CODEC.optionalFieldOf("profile").forGetter(SerializedOptions::profile), (App)CUSTOM_ENTRIES_CODEC.optionalFieldOf("custom").forGetter(SerializedOptions::custom)).apply((Applicative)i, SerializedOptions::new));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugScreenEntryList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */