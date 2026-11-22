/*     */ package net.minecraft.world.timeline;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.KeyframeTrack;
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ import net.minecraft.world.clock.ClockTimeMarker;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ 
/*     */ public class Builder {
/*     */   private final Holder<WorldClock> clock;
/*     */   
/* 170 */   private Optional<Integer> periodTicks = Optional.empty();
/*     */   
/* 171 */   private final ImmutableMap.Builder<EnvironmentAttribute<?>, AttributeTrack<?, ?>> tracks = ImmutableMap.builder();
/*     */   
/* 172 */   private final ImmutableMap.Builder<ResourceKey<ClockTimeMarker>, Timeline.TimeMarkerInfo> timeMarkers = ImmutableMap.builder();
/*     */   
/*     */   private Builder(Holder<WorldClock> clock) {
/* 175 */     this.clock = clock;
/*     */   }
/*     */   
/*     */   public Builder setPeriodTicks(int periodTicks) {
/* 179 */     this.periodTicks = Optional.of(Integer.valueOf(periodTicks));
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   public <Value, Argument> Builder addModifierTrack(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Argument> modifier, Consumer<KeyframeTrack.Builder<Argument>> builder) {
/* 184 */     attribute.type().checkAllowedModifier(modifier);
/* 185 */     KeyframeTrack.Builder<Argument> argumentTrack = new KeyframeTrack.Builder();
/* 186 */     builder.accept(argumentTrack);
/* 187 */     this.tracks.put(attribute, new AttributeTrack<>(modifier, argumentTrack.build()));
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public <Value> Builder addTrack(EnvironmentAttribute<Value> attribute, Consumer<KeyframeTrack.Builder<Value>> builder) {
/* 192 */     return addModifierTrack(attribute, AttributeModifier.override(), builder);
/*     */   }
/*     */   
/*     */   public Builder addTimeMarker(ResourceKey<ClockTimeMarker> id, int ticks) {
/* 196 */     return addTimeMarker(id, ticks, false);
/*     */   }
/*     */   
/*     */   public Builder addTimeMarker(ResourceKey<ClockTimeMarker> id, int ticks, boolean showInCommands) {
/* 200 */     this.timeMarkers.put(id, new Timeline.TimeMarkerInfo(ticks, showInCommands));
/* 201 */     return this;
/*     */   }
/*     */   
/*     */   public Timeline build() {
/* 205 */     return new Timeline(this.clock, this.periodTicks, (Map<EnvironmentAttribute<?>, AttributeTrack<?, ?>>)this.tracks.build(), (Map<ResourceKey<ClockTimeMarker>, Timeline.TimeMarkerInfo>)this.timeMarkers.build());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\timeline\Timeline$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */