/*     */ package net.minecraft.world.clock;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public class ServerClockManager extends SavedData implements ClockManager {
/*  25 */   public static final SavedDataType<ServerClockManager> TYPE = new SavedDataType(
/*  26 */       Identifier.withDefaultNamespace("world_clocks"), () -> new ServerClockManager(PackedClockStates.EMPTY), PackedClockStates.CODEC
/*     */       
/*  28 */       .xmap(ServerClockManager::new, ServerClockManager::packState), DataFixTypes.SAVED_DATA_WORLD_CLOCKS);
/*     */   
/*     */   private final PackedClockStates packedClockStates;
/*     */   
/*     */   private MinecraftServer server;
/*     */   
/*  33 */   private final Map<Holder<WorldClock>, ClockInstance> clocks = new HashMap<>();
/*     */   
/*     */   private ServerClockManager(PackedClockStates packedClockStates) {
/*  36 */     this.packedClockStates = packedClockStates;
/*     */   }
/*     */   
/*     */   public void init(MinecraftServer server) {
/*  40 */     this.server = server;
/*  41 */     server.registryAccess().lookupOrThrow(Registries.WORLD_CLOCK).listElements().forEach(definition -> this.clocks.put(definition, new ClockInstance()));
/*  44 */     server.registryAccess().lookupOrThrow(Registries.TIMELINE).listElements().forEach(timeline -> ((Timeline)timeline.value()).registerTimeMarkers(this::registerTimeMarker));
/*  47 */     this.packedClockStates.clocks().forEach((definition, state) -> {
/*     */           ClockInstance instance = getInstance(definition);
/*     */           instance.loadFrom(state);
/*     */         });
/*     */   }
/*     */   
/*     */   private void registerTimeMarker(ResourceKey<ClockTimeMarker> timeMarkerId, ClockTimeMarker timeMarker) {
/*  54 */     (getInstance(timeMarker.clock())).timeMarkers.put(timeMarkerId, timeMarker);
/*     */   }
/*     */   
/*     */   public PackedClockStates packState() {
/*  58 */     return new PackedClockStates(
/*  59 */         Util.mapValues(this.clocks, ClockInstance::packState));
/*     */   }
/*     */   
/*     */   public void tick() {
/*  64 */     boolean advanceTime = ((Boolean)this.server.getGlobalGameRules().get(GameRules.ADVANCE_TIME)).booleanValue();
/*  65 */     if (advanceTime) {
/*  66 */       this.clocks.values().forEach(ClockInstance::tick);
/*  67 */       setDirty();
/*     */     } 
/*     */   }
/*     */   
/*     */   private ClockInstance getInstance(Holder<WorldClock> definition) {
/*  72 */     ClockInstance instance = this.clocks.get(definition);
/*  73 */     if (instance == null)
/*  74 */       throw new IllegalStateException("No clock initialized for definition: " + String.valueOf(definition)); 
/*  76 */     return instance;
/*     */   }
/*     */   
/*     */   public void setTotalTicks(Holder<WorldClock> clock, long totalTicks) {
/*  80 */     modifyClock(clock, instance -> {
/*     */           instance.totalTicks = totalTicks;
/*     */           instance.partialTick = 0.0F;
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean moveToTimeMarker(Holder<WorldClock> clock, ResourceKey<ClockTimeMarker> timeMarkerId) {
/*  87 */     MutableBoolean set = new MutableBoolean();
/*  88 */     modifyClock(clock, instance -> {
/*     */           ClockTimeMarker timeMarker = instance.timeMarkers.get(timeMarkerId);
/*     */           if (timeMarker == null)
/*     */             return; 
/*     */           instance.totalTicks = timeMarker.resolveTimeToMoveTo(instance.totalTicks);
/*     */           instance.partialTick = 0.0F;
/*     */           set.setTrue();
/*     */         });
/*  97 */     return set.booleanValue();
/*     */   }
/*     */   
/*     */   public void addTicks(Holder<WorldClock> clock, int ticks) {
/* 101 */     modifyClock(clock, instance -> instance.totalTicks = Math.max(instance.totalTicks + ticks, 0L));
/*     */   }
/*     */   
/*     */   public void setPaused(Holder<WorldClock> clock, boolean paused) {
/* 105 */     modifyClock(clock, instance -> instance.paused = paused);
/*     */   }
/*     */   
/*     */   public void setRate(Holder<WorldClock> clock, float rate) {
/* 109 */     modifyClock(clock, instance -> instance.rate = rate);
/*     */   }
/*     */   
/*     */   private void modifyClock(Holder<WorldClock> clock, Consumer<? super ClockInstance> action) {
/* 113 */     ClockInstance instance = getInstance(clock);
/* 114 */     action.accept(instance);
/* 116 */     Map<Holder<WorldClock>, ClockNetworkState> updates = Map.of(clock, instance.packNetworkState(this.server));
/* 117 */     this.server.getPlayerList().broadcastAll((Packet)new ClientboundSetTimePacket(getGameTime(), updates));
/* 118 */     setDirty();
/* 121 */     for (ServerLevel level : this.server.getAllLevels())
/* 122 */       level.environmentAttributes().invalidateTickCache(); 
/*     */   }
/*     */   
/*     */   public long getTotalTicks(Holder<WorldClock> definition) {
/* 128 */     return (getInstance(definition)).totalTicks;
/*     */   }
/*     */   
/*     */   public ClientboundSetTimePacket createFullSyncPacket() {
/* 132 */     return new ClientboundSetTimePacket(
/* 133 */         getGameTime(), 
/* 134 */         Util.mapValues(this.clocks, clock -> clock.packNetworkState(this.server)));
/*     */   }
/*     */   
/*     */   private long getGameTime() {
/* 139 */     return this.server.overworld().getGameTime();
/*     */   }
/*     */   
/*     */   public boolean isAtTimeMarker(Holder<WorldClock> clock, ResourceKey<ClockTimeMarker> timeMarkerId) {
/* 143 */     ClockInstance clockInstance = getInstance(clock);
/* 144 */     ClockTimeMarker timeMarker = clockInstance.timeMarkers.get(timeMarkerId);
/* 145 */     return (timeMarker != null && timeMarker.occursAt(clockInstance.totalTicks));
/*     */   }
/*     */   
/*     */   public Stream<ResourceKey<ClockTimeMarker>> commandTimeMarkersForClock(Holder<WorldClock> clock) {
/* 149 */     return (getInstance(clock)).timeMarkers.entrySet().stream()
/* 150 */       .filter(entry -> ((ClockTimeMarker)entry.getValue()).showInCommands())
/* 151 */       .map(Map.Entry::getKey);
/*     */   }
/*     */   
/*     */   private static class ClockInstance {
/* 155 */     private final Map<ResourceKey<ClockTimeMarker>, ClockTimeMarker> timeMarkers = (Map<ResourceKey<ClockTimeMarker>, ClockTimeMarker>)new Reference2ObjectOpenHashMap();
/*     */     
/*     */     private long totalTicks;
/*     */     
/*     */     private float partialTick;
/*     */     
/* 159 */     private float rate = 1.0F;
/*     */     
/*     */     private boolean paused;
/*     */     
/*     */     public void loadFrom(ClockState state) {
/* 163 */       this.totalTicks = state.totalTicks();
/* 164 */       this.partialTick = state.partialTick();
/* 165 */       this.rate = state.rate();
/* 166 */       this.paused = state.paused();
/*     */     }
/*     */     
/*     */     public void tick() {
/* 170 */       if (!this.paused) {
/* 171 */         this.partialTick += this.rate;
/* 172 */         int fullTicks = Mth.floor(this.partialTick);
/* 173 */         this.partialTick -= fullTicks;
/* 174 */         this.totalTicks += fullTicks;
/*     */       } 
/*     */     }
/*     */     
/*     */     public ClockState packState() {
/* 179 */       return new ClockState(this.totalTicks, this.partialTick, this.rate, this.paused);
/*     */     }
/*     */     
/*     */     public ClockNetworkState packNetworkState(MinecraftServer server) {
/* 183 */       boolean advanceTime = ((Boolean)server.getGlobalGameRules().get(GameRules.ADVANCE_TIME)).booleanValue();
/* 184 */       boolean paused = (this.paused || !advanceTime);
/* 185 */       return new ClockNetworkState(this.totalTicks, this.partialTick, paused ? 0.0F : this.rate);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\clock\ServerClockManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */