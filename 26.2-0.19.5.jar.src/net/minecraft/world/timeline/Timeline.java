/*     */ package net.minecraft.world.timeline;
/*     */ 
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFixedCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.KeyframeTrack;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ import net.minecraft.world.clock.ClockManager;
/*     */ import net.minecraft.world.clock.ClockTimeMarker;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ 
/*     */ public class Timeline {
/*  33 */   public static final Codec<Holder<Timeline>> CODEC = (Codec<Holder<Timeline>>)RegistryFixedCodec.create(Registries.TIMELINE);
/*     */   
/*  35 */   private static final Codec<Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>>> TRACKS_CODEC = Codec.dispatchedMap(EnvironmentAttributes.CODEC, Util.memoize(AttributeTrack::createCodec));
/*     */   
/*     */   public static final Codec<Timeline> DIRECT_CODEC;
/*     */   
/*     */   static {
/*  42 */     DIRECT_CODEC = RecordCodecBuilder.create(i -> i.group((App)WorldClock.CODEC.fieldOf("clock").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("period_ticks").forGetter(()), (App)TRACKS_CODEC.optionalFieldOf("tracks", Map.of()).forGetter(()), (App)Codec.unboundedMap(ClockTimeMarker.KEY_CODEC, TimeMarkerInfo.CODEC).optionalFieldOf("time_markers", Map.of()).forGetter(())).apply((Applicative)i, Timeline::new)).validate(Timeline::validateInternal);
/*     */   }
/*     */   
/*  44 */   public static final Codec<Timeline> NETWORK_CODEC = DIRECT_CODEC.xmap(Timeline::filterSyncableTracks, Timeline::filterSyncableTracks);
/*     */   
/*     */   private final Holder<WorldClock> clock;
/*     */   
/*     */   private final Optional<Integer> periodTicks;
/*     */   
/*     */   private final Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks;
/*     */   
/*     */   private final Map<ResourceKey<ClockTimeMarker>, TimeMarkerInfo> timeMarkers;
/*     */   
/*     */   private static Timeline filterSyncableTracks(Timeline timeline) {
/*  47 */     Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>> syncableTracks = Map.copyOf(Maps.filterKeys(timeline.tracks, EnvironmentAttribute::isSyncable));
/*  48 */     return new Timeline(timeline.clock, timeline.periodTicks, syncableTracks, timeline.timeMarkers);
/*     */   }
/*     */   
/*     */   private Timeline(Holder<WorldClock> clock, Optional<Integer> periodTicks, Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks, Map<ResourceKey<ClockTimeMarker>, TimeMarkerInfo> timeMarkers) {
/*  62 */     this.clock = clock;
/*  63 */     this.periodTicks = periodTicks;
/*  64 */     this.tracks = tracks;
/*  65 */     this.timeMarkers = timeMarkers;
/*     */   }
/*     */   
/*     */   public static void validateRegistry(Registry<Timeline> timelines, Map<ResourceKey<?>, Exception> loadingErrors) {
/*  69 */     HashMultimap hashMultimap = HashMultimap.create();
/*  70 */     timelines.listElements().forEach(timeline -> {
/*     */           Holder<WorldClock> clock = ((Timeline)timeline.value()).clock();
/*     */           for (ResourceKey<ClockTimeMarker> timeMarker : ((Timeline)timeline.value()).timeMarkers.keySet()) {
/*     */             if (!timeMarkersByClock.put(clock, timeMarker))
/*     */               loadingErrors.put(timeline.key(), new IllegalStateException(String.valueOf(timeMarker) + " was defined multiple times in " + String.valueOf(timeMarker))); 
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static DataResult<Timeline> validateInternal(Timeline timeline) {
/*  81 */     if (timeline.periodTicks.isEmpty())
/*  82 */       return DataResult.success(timeline); 
/*  84 */     int periodTicks = ((Integer)timeline.periodTicks.get()).intValue();
/*  85 */     for (Map.Entry<ResourceKey<ClockTimeMarker>, TimeMarkerInfo> entry : timeline.timeMarkers.entrySet()) {
/*  86 */       int ticks = ((TimeMarkerInfo)entry.getValue()).ticks();
/*  87 */       if (ticks < 0 || ticks >= periodTicks)
/*  88 */         return DataResult.error(() -> "Time Marker " + String.valueOf(entry.getKey()) + " must be in range [0; " + periodTicks + ")"); 
/*     */     } 
/*  91 */     DataResult<Timeline> result = DataResult.success(timeline);
/*  92 */     for (AttributeTrack<?, ?> track : timeline.tracks.values())
/*  93 */       result = result.apply2stable((t, $) -> t, 
/*     */           
/*  95 */           AttributeTrack.validatePeriod(track, periodTicks)); 
/*  98 */     return result;
/*     */   }
/*     */   
/*     */   public static Builder builder(Holder<WorldClock> clock) {
/* 102 */     return new Builder(clock);
/*     */   }
/*     */   
/*     */   public int getPeriodCount(ClockManager clockManager) {
/* 106 */     if (this.periodTicks.isEmpty())
/* 107 */       return 0; 
/* 109 */     long totalTicks = getTotalTicks(clockManager);
/* 110 */     return (int)(totalTicks / ((Integer)this.periodTicks.get()).intValue());
/*     */   }
/*     */   
/*     */   public long getCurrentTicks(ClockManager clockManager) {
/* 114 */     long totalTicks = getTotalTicks(clockManager);
/* 115 */     if (this.periodTicks.isEmpty())
/* 116 */       return totalTicks; 
/* 118 */     return totalTicks % ((Integer)this.periodTicks.get()).intValue();
/*     */   }
/*     */   
/*     */   public long getTotalTicks(ClockManager clockManager) {
/* 122 */     return clockManager.getTotalTicks(this.clock);
/*     */   }
/*     */   
/*     */   public Holder<WorldClock> clock() {
/* 126 */     return this.clock;
/*     */   }
/*     */   
/*     */   public Optional<Integer> periodTicks() {
/* 130 */     return this.periodTicks;
/*     */   }
/*     */   
/*     */   public void registerTimeMarkers(BiConsumer<ResourceKey<ClockTimeMarker>, ClockTimeMarker> output) {
/* 134 */     for (Map.Entry<ResourceKey<ClockTimeMarker>, TimeMarkerInfo> entry : this.timeMarkers.entrySet()) {
/* 135 */       TimeMarkerInfo info = entry.getValue();
/* 136 */       output.accept(entry.getKey(), new ClockTimeMarker(this.clock, info.ticks, this.periodTicks, info.showInCommands));
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<EnvironmentAttribute<?>> attributes() {
/* 141 */     return this.tracks.keySet();
/*     */   }
/*     */   
/*     */   public <Value> AttributeTrackSampler<Value, ?> createTrackSampler(EnvironmentAttribute<Value> attribute, ClockManager clockManager) {
/* 146 */     AttributeTrack<Value, ?> track = (AttributeTrack<Value, ?>)this.tracks.get(attribute);
/* 147 */     if (track == null)
/* 148 */       throw new IllegalStateException("Timeline has no track for " + String.valueOf(attribute)); 
/* 150 */     return track.bakeSampler(attribute, this.clock, this.periodTicks, clockManager);
/*     */   }
/*     */   
/*     */   private static final class TimeMarkerInfo extends Record {
/*     */     private final int ticks;
/*     */     
/*     */     private final boolean showInCommands;
/*     */     
/*     */     private static final Codec<TimeMarkerInfo> FULL_CODEC;
/*     */     
/*     */     public static final Codec<TimeMarkerInfo> CODEC;
/*     */     
/*     */     private TimeMarkerInfo(int ticks, boolean showInCommands) {
/* 153 */       this.ticks = ticks;
/* 153 */       this.showInCommands = showInCommands;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #153	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #153	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #153	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int ticks() {
/* 153 */       return this.ticks;
/*     */     }
/*     */     
/*     */     public boolean showInCommands() {
/* 153 */       return this.showInCommands;
/*     */     }
/*     */     
/*     */     static {
/* 157 */       FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ticks").forGetter(TimeMarkerInfo::ticks), (App)Codec.BOOL.optionalFieldOf("show_in_commands", Boolean.valueOf(false)).forGetter(TimeMarkerInfo::showInCommands)).apply((Applicative)i, TimeMarkerInfo::new));
/* 162 */       CODEC = Codec.either(ExtraCodecs.NON_NEGATIVE_INT, FULL_CODEC).xmap(either -> (TimeMarkerInfo)either.map((), ()), timeMarker -> timeMarker.showInCommands ? Either.right(timeMarker) : Either.left(Integer.valueOf(timeMarker.ticks)));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final Holder<WorldClock> clock;
/*     */     
/* 170 */     private Optional<Integer> periodTicks = Optional.empty();
/*     */     
/* 171 */     private final ImmutableMap.Builder<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks = ImmutableMap.builder();
/*     */     
/* 172 */     private final ImmutableMap.Builder<ResourceKey<ClockTimeMarker>, Timeline.TimeMarkerInfo> timeMarkers = ImmutableMap.builder();
/*     */     
/*     */     private Builder(Holder<WorldClock> clock) {
/* 175 */       this.clock = clock;
/*     */     }
/*     */     
/*     */     public Builder setPeriodTicks(int periodTicks) {
/* 179 */       this.periodTicks = Optional.of(Integer.valueOf(periodTicks));
/* 180 */       return this;
/*     */     }
/*     */     
/*     */     public <Value, Argument> Builder addModifierTrack(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Argument> modifier, Consumer<KeyframeTrack.Builder<Argument>> builder) {
/* 184 */       attribute.type().checkAllowedModifier(modifier);
/* 185 */       KeyframeTrack.Builder<Argument> argumentTrack = new KeyframeTrack.Builder();
/* 186 */       builder.accept(argumentTrack);
/* 187 */       this.tracks.put(attribute, new AttributeTrack<>(modifier, argumentTrack.build()));
/* 188 */       return this;
/*     */     }
/*     */     
/*     */     public <Value> Builder addTrack(EnvironmentAttribute<Value> attribute, Consumer<KeyframeTrack.Builder<Value>> builder) {
/* 192 */       return addModifierTrack(attribute, AttributeModifier.override(), builder);
/*     */     }
/*     */     
/*     */     public Builder addTimeMarker(ResourceKey<ClockTimeMarker> id, int ticks) {
/* 196 */       return addTimeMarker(id, ticks, false);
/*     */     }
/*     */     
/*     */     public Builder addTimeMarker(ResourceKey<ClockTimeMarker> id, int ticks, boolean showInCommands) {
/* 200 */       this.timeMarkers.put(id, new Timeline.TimeMarkerInfo(ticks, showInCommands));
/* 201 */       return this;
/*     */     }
/*     */     
/*     */     public Timeline build() {
/* 205 */       return new Timeline(this.clock, this.periodTicks, (Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>>)this.tracks.build(), (Map<ResourceKey<ClockTimeMarker>, Timeline.TimeMarkerInfo>)this.timeMarkers.build());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\timeline\Timeline.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */