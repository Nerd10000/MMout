/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.AdvancementNode;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.advancements.AdvancementTree;
/*     */ import net.minecraft.advancements.CriterionProgress;
/*     */ import net.minecraft.advancements.CriterionTriggerInstance;
/*     */ import net.minecraft.advancements.DisplayInfo;
/*     */ import net.minecraft.advancements.triggers.Criterion;
/*     */ import net.minecraft.advancements.triggers.CriterionTrigger;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.advancements.AdvancementVisibilityEvaluator;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PlayerAdvancements {
/*  49 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  50 */   private static final Gson GSON = (new GsonBuilder())
/*  51 */     .setPrettyPrinting()
/*  52 */     .create();
/*     */   
/*     */   private final PlayerList playerList;
/*     */   
/*     */   private final Path playerSavePath;
/*     */   
/*     */   private AdvancementTree tree;
/*     */   
/*  56 */   private final Map<AdvancementHolder, AdvancementProgress> progress = new LinkedHashMap<>();
/*     */   
/*  57 */   private final Set<AdvancementHolder> visible = new HashSet<>();
/*     */   
/*  58 */   private final Set<AdvancementHolder> progressChanged = new HashSet<>();
/*     */   
/*  59 */   private final Set<AdvancementNode> rootsToUpdate = new HashSet<>();
/*     */   
/*     */   private ServerPlayer player;
/*     */   
/*     */   private AdvancementHolder lastSelectedTab;
/*     */   
/*     */   private boolean isFirstPacket = true;
/*     */   
/*     */   private final Codec<Data> codec;
/*     */   
/*  64 */   private final Map<CriterionTrigger<?>, Map<TriggerInstanceKey, ? extends CriterionTriggerInstance>> activeTriggers = new IdentityHashMap<>();
/*     */   
/*     */   public PlayerAdvancements(DataFixer dataFixer, PlayerList playerList, ServerAdvancementManager manager, Path playerSavePath, ServerPlayer player) {
/*  67 */     this.playerList = playerList;
/*  68 */     this.playerSavePath = playerSavePath;
/*  69 */     this.player = player;
/*  70 */     this.tree = manager.tree();
/*  72 */     int defaultVersion = 1343;
/*  73 */     this.codec = DataFixTypes.ADVANCEMENTS.wrapCodec(Data.CODEC, dataFixer, 1343);
/*  74 */     load(manager);
/*     */   }
/*     */   
/*     */   public void setPlayer(ServerPlayer player) {
/*  78 */     this.player = player;
/*     */   }
/*     */   
/*     */   public void clearTriggers() {
/*  82 */     this.activeTriggers.clear();
/*     */   }
/*     */   
/*     */   public void reload(ServerAdvancementManager manager) {
/*  86 */     clearTriggers();
/*  87 */     this.progress.clear();
/*  88 */     this.visible.clear();
/*  89 */     this.rootsToUpdate.clear();
/*  90 */     this.progressChanged.clear();
/*  91 */     this.isFirstPacket = true;
/*  92 */     this.lastSelectedTab = null;
/*  93 */     this.tree = manager.tree();
/*  94 */     load(manager);
/*     */   }
/*     */   
/*     */   private void registerListeners(ServerAdvancementManager manager) {
/*  98 */     for (AdvancementHolder advancement : manager.getAllAdvancements())
/*  99 */       registerListeners(advancement); 
/*     */   }
/*     */   
/*     */   private void checkForAutomaticTriggers(ServerAdvancementManager manager) {
/* 104 */     for (AdvancementHolder holder : manager.getAllAdvancements()) {
/* 105 */       Advancement advancement = holder.value();
/* 106 */       if (advancement.criteria().isEmpty()) {
/* 107 */         award(holder, "");
/* 108 */         advancement.rewards().grant(this.player);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void load(ServerAdvancementManager manager) {
/* 114 */     if (Files.isRegularFile(this.playerSavePath, new java.nio.file.LinkOption[0]))
/*     */       try {
/* 115 */         Reader reader = Files.newBufferedReader(this.playerSavePath, StandardCharsets.UTF_8);
/*     */         try {
/* 116 */           JsonElement json = StrictJsonParser.parse(reader);
/* 117 */           Data data = (Data)this.codec.parse((DynamicOps)JsonOps.INSTANCE, json).getOrThrow(JsonParseException::new);
/* 118 */           applyFrom(manager, data);
/* 119 */           if (reader != null)
/* 119 */             reader.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (reader != null)
/*     */             try {
/*     */               reader.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 119 */       } catch (IOException|com.google.gson.JsonIOException e) {
/* 120 */         LOGGER.error("Couldn't access player advancements in {}", this.playerSavePath, e);
/* 121 */       } catch (JsonParseException e) {
/* 122 */         LOGGER.error("Couldn't parse player advancements in {}", this.playerSavePath, e);
/*     */       }  
/* 126 */     checkForAutomaticTriggers(manager);
/* 127 */     registerListeners(manager);
/*     */   }
/*     */   
/*     */   public void save() {
/* 131 */     JsonElement json = (JsonElement)this.codec.encodeStart((DynamicOps)JsonOps.INSTANCE, asData()).getOrThrow();
/*     */     try {
/* 133 */       FileUtil.createDirectoriesSafe(this.playerSavePath.getParent());
/* 134 */       Writer outputWriter = Files.newBufferedWriter(this.playerSavePath, StandardCharsets.UTF_8, new java.nio.file.OpenOption[0]);
/*     */       try {
/* 135 */         GSON.toJson(json, GSON.newJsonWriter(outputWriter));
/* 136 */         if (outputWriter != null)
/* 136 */           outputWriter.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (outputWriter != null)
/*     */           try {
/*     */             outputWriter.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 137 */     } catch (IOException|com.google.gson.JsonIOException e) {
/* 138 */       LOGGER.error("Couldn't save player advancements to {}", this.playerSavePath, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void applyFrom(ServerAdvancementManager manager, Data data) {
/* 143 */     data.forEach((id, progress) -> {
/*     */           AdvancementHolder advancement = manager.get(id);
/*     */           if (advancement == null) {
/*     */             LOGGER.warn("Ignored advancement '{}' in progress file {} - it doesn't exist anymore?", id, this.playerSavePath);
/*     */             return;
/*     */           } 
/*     */           startProgress(advancement, progress);
/*     */           this.progressChanged.add(advancement);
/*     */           markForVisibilityUpdate(advancement);
/*     */         });
/*     */   }
/*     */   
/*     */   private Data asData() {
/* 156 */     Map<Identifier, AdvancementProgress> map = new LinkedHashMap<>();
/* 157 */     this.progress.forEach((advancement, progress) -> {
/*     */           if (progress.hasProgress())
/*     */             map.put(advancement.id(), progress); 
/*     */         });
/* 162 */     return new Data(map);
/*     */   }
/*     */   
/*     */   public boolean award(AdvancementHolder holder, String criterion) {
/* 166 */     boolean result = false;
/* 168 */     AdvancementProgress progress = getOrStartProgress(holder);
/* 169 */     boolean wasDone = progress.isDone();
/* 171 */     if (progress.grantProgress(criterion)) {
/* 172 */       unregisterListeners(holder);
/* 173 */       this.progressChanged.add(holder);
/* 174 */       result = true;
/* 176 */       if (!wasDone && progress.isDone()) {
/* 177 */         holder.value().rewards().grant(this.player);
/* 178 */         holder.value().display().ifPresent(display -> {
/*     */               if (display.shouldAnnounceChat() && ((Boolean)this.player.level().getGameRules().get(GameRules.SHOW_ADVANCEMENT_MESSAGES)).booleanValue())
/*     */                 this.playerList.broadcastSystemMessage((Component)display.getType().createAnnouncement(holder, this.player), false); 
/*     */             });
/*     */       } 
/*     */     } 
/* 186 */     if (!wasDone && progress.isDone())
/* 187 */       markForVisibilityUpdate(holder); 
/* 190 */     return result;
/*     */   }
/*     */   
/*     */   public boolean revoke(AdvancementHolder advancement, String criterion) {
/* 194 */     boolean result = false;
/* 196 */     AdvancementProgress progress = getOrStartProgress(advancement);
/* 197 */     boolean wasDone = progress.isDone();
/* 198 */     if (progress.revokeProgress(criterion)) {
/* 199 */       registerListeners(advancement);
/* 200 */       this.progressChanged.add(advancement);
/* 201 */       result = true;
/*     */     } 
/* 204 */     if (wasDone && !progress.isDone())
/* 205 */       markForVisibilityUpdate(advancement); 
/* 208 */     return result;
/*     */   }
/*     */   
/*     */   private void markForVisibilityUpdate(AdvancementHolder advancement) {
/* 212 */     AdvancementNode node = this.tree.get(advancement);
/* 213 */     if (node != null)
/* 214 */       this.rootsToUpdate.add(node.root()); 
/*     */   }
/*     */   
/*     */   private void registerListeners(AdvancementHolder holder) {
/* 219 */     AdvancementProgress advancementProgress = getOrStartProgress(holder);
/* 220 */     if (advancementProgress.isDone())
/*     */       return; 
/* 223 */     for (Map.Entry<String, Criterion<?>> entry : (Iterable<Map.Entry<String, Criterion<?>>>)holder.value().criteria().entrySet()) {
/* 224 */       CriterionProgress criterionProgress = advancementProgress.getCriterion(entry.getKey());
/* 225 */       if (criterionProgress == null || criterionProgress.isDone())
/*     */         continue; 
/* 228 */       addListener((Criterion<CriterionTriggerInstance>)entry.getValue(), new TriggerInstanceKey(holder, entry.getKey()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void unregisterListeners(AdvancementHolder holder) {
/* 233 */     AdvancementProgress advancementProgress = getOrStartProgress(holder);
/* 234 */     for (Map.Entry<String, Criterion<?>> entry : (Iterable<Map.Entry<String, Criterion<?>>>)holder.value().criteria().entrySet()) {
/* 235 */       CriterionProgress criterionProgress = advancementProgress.getCriterion(entry.getKey());
/* 236 */       if (criterionProgress == null || (!criterionProgress.isDone() && !advancementProgress.isDone()))
/*     */         continue; 
/* 239 */       removeListener(((Criterion)entry.getValue()).trigger(), new TriggerInstanceKey(holder, entry.getKey()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void flushDirty(ServerPlayer player, boolean showAdvancements) {
/* 244 */     if (this.isFirstPacket || !this.rootsToUpdate.isEmpty() || !this.progressChanged.isEmpty()) {
/* 245 */       Map<Identifier, AdvancementProgress> progress = new HashMap<>();
/* 246 */       Set<AdvancementHolder> added = new HashSet<>();
/* 247 */       Set<Identifier> removed = new HashSet<>();
/* 249 */       for (AdvancementNode root : this.rootsToUpdate)
/* 250 */         updateTreeVisibility(root, added, removed); 
/* 252 */       this.rootsToUpdate.clear();
/* 254 */       for (AdvancementHolder holder : this.progressChanged) {
/* 255 */         if (this.visible.contains(holder))
/* 256 */           progress.put(holder.id(), this.progress.get(holder)); 
/*     */       } 
/* 259 */       this.progressChanged.clear();
/* 261 */       if (!progress.isEmpty() || !added.isEmpty() || !removed.isEmpty())
/* 262 */         player.connection.send((Packet)new ClientboundUpdateAdvancementsPacket(this.isFirstPacket, added, removed, progress, showAdvancements)); 
/*     */     } 
/* 265 */     this.isFirstPacket = false;
/*     */   }
/*     */   
/*     */   public void setSelectedTab(AdvancementHolder holder) {
/* 269 */     AdvancementHolder old = this.lastSelectedTab;
/* 270 */     if (holder != null && holder.value().isRoot() && holder.value().display().isPresent()) {
/* 271 */       this.lastSelectedTab = holder;
/*     */     } else {
/* 273 */       this.lastSelectedTab = null;
/*     */     } 
/* 275 */     if (old != this.lastSelectedTab)
/* 276 */       this.player.connection.send((Packet)new ClientboundSelectAdvancementsTabPacket((this.lastSelectedTab == null) ? null : this.lastSelectedTab.id())); 
/*     */   }
/*     */   
/*     */   public AdvancementProgress getOrStartProgress(AdvancementHolder advancement) {
/* 281 */     AdvancementProgress progress = this.progress.get(advancement);
/* 282 */     if (progress == null) {
/* 283 */       progress = new AdvancementProgress();
/* 284 */       startProgress(advancement, progress);
/*     */     } 
/* 286 */     return progress;
/*     */   }
/*     */   
/*     */   private void startProgress(AdvancementHolder holder, AdvancementProgress progress) {
/* 290 */     progress.update(holder.value().requirements());
/* 291 */     this.progress.put(holder, progress);
/*     */   }
/*     */   
/*     */   private void updateTreeVisibility(AdvancementNode root, Set<AdvancementHolder> added, Set<Identifier> removed) {
/* 295 */     AdvancementVisibilityEvaluator.evaluateVisibility(root, node -> getOrStartProgress(node.holder()).isDone(), (node, shouldBeVisible) -> {
/*     */           AdvancementHolder advancement = node.holder();
/*     */           if (shouldBeVisible) {
/*     */             if (this.visible.add(advancement)) {
/*     */               added.add(advancement);
/*     */               if (this.progress.containsKey(advancement))
/*     */                 this.progressChanged.add(advancement); 
/*     */             } 
/*     */           } else if (this.visible.remove(advancement)) {
/*     */             removed.add(advancement.id());
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private <T extends CriterionTriggerInstance> Map<TriggerInstanceKey, T> getOrCreateTriggerMapForType(CriterionTrigger<T> type) {
/* 318 */     return (Map<TriggerInstanceKey, T>)this.activeTriggers.computeIfAbsent(type, paramCriterionTrigger -> new HashMap<>());
/*     */   }
/*     */   
/*     */   private <T extends CriterionTriggerInstance> void addListener(Criterion<T> typeAndInstance, TriggerInstanceKey criterion) {
/* 322 */     getOrCreateTriggerMapForType(typeAndInstance.trigger())
/* 323 */       .put(criterion, typeAndInstance.triggerInstance());
/*     */   }
/*     */   
/*     */   public <T extends CriterionTriggerInstance> Map<TriggerInstanceKey, T> getTriggerMapForType(CriterionTrigger<T> type) {
/* 328 */     return (Map<TriggerInstanceKey, T>)this.activeTriggers.get(type);
/*     */   }
/*     */   
/*     */   private <T extends CriterionTriggerInstance> void removeListener(CriterionTrigger<T> type, TriggerInstanceKey criterion) {
/* 332 */     Map<TriggerInstanceKey, T> map = getTriggerMapForType(type);
/* 333 */     if (map != null) {
/* 334 */       map.remove(criterion);
/* 335 */       if (map.isEmpty())
/* 336 */         this.activeTriggers.remove(type); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final class TriggerInstanceKey extends Record {
/*     */     private final AdvancementHolder advancement;
/*     */     
/*     */     private final String criterion;
/*     */     
/*     */     public TriggerInstanceKey(AdvancementHolder advancement, String criterion) {
/* 341 */       this.advancement = advancement;
/* 341 */       this.criterion = criterion;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #341	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #341	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #341	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public AdvancementHolder advancement() {
/* 341 */       return this.advancement;
/*     */     }
/*     */     
/*     */     public String criterion() {
/* 341 */       return this.criterion;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Data extends Record {
/*     */     private final Map<Identifier, AdvancementProgress> map;
/*     */     
/*     */     private Data(Map<Identifier, AdvancementProgress> map) {
/* 344 */       this.map = map;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/PlayerAdvancements$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #344	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/PlayerAdvancements$Data;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/PlayerAdvancements$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #344	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/PlayerAdvancements$Data;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/PlayerAdvancements$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #344	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/PlayerAdvancements$Data;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Map<Identifier, AdvancementProgress> map() {
/* 344 */       return this.map;
/*     */     }
/*     */     
/* 345 */     public static final Codec<Data> CODEC = Codec.unboundedMap(Identifier.CODEC, AdvancementProgress.CODEC).xmap(Data::new, Data::map);
/*     */     
/*     */     public void forEach(BiConsumer<Identifier, AdvancementProgress> consumer) {
/* 348 */       this.map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> consumer.accept((Identifier)entry.getKey(), (AdvancementProgress)entry.getValue()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\PlayerAdvancements.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */