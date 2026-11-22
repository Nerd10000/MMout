/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.world.clock.ClockManager;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ 
/*     */ public class Builder {
/* 138 */   private final Map<EnvironmentAttribute<?>, List<EnvironmentAttributeLayer<?>>> layersByAttribute = new HashMap<>();
/*     */   
/*     */   public Builder addDefaultLayers(Level level) {
/* 144 */     EnvironmentAttributeSystem.addDefaultLayers(this, level);
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addConstantLayer(EnvironmentAttributeMap attributeMap) {
/* 149 */     for (EnvironmentAttribute<?> attribute : attributeMap.keySet())
/* 150 */       addConstantEntry(attribute, attributeMap); 
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   private <Value> Builder addConstantEntry(EnvironmentAttribute<Value> attribute, EnvironmentAttributeMap attributeMap) {
/* 156 */     EnvironmentAttributeMap.Entry<Value, ?> entry = attributeMap.get(attribute);
/* 157 */     if (entry == null)
/* 158 */       throw new IllegalArgumentException("Missing attribute " + String.valueOf(attribute)); 
/* 160 */     Objects.requireNonNull(entry);
/* 160 */     return addConstantLayer(attribute, entry::applyModifier);
/*     */   }
/*     */   
/*     */   public <Value> Builder addConstantLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.Constant<Value> layer) {
/* 164 */     return addLayer(attribute, layer);
/*     */   }
/*     */   
/*     */   public <Value> Builder addTimeBasedLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.TimeBased<Value> layer) {
/* 168 */     return addLayer(attribute, layer);
/*     */   }
/*     */   
/*     */   public <Value> Builder addPositionalLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer.Positional<Value> layer) {
/* 172 */     return addLayer(attribute, layer);
/*     */   }
/*     */   
/*     */   private <Value> Builder addLayer(EnvironmentAttribute<Value> attribute, EnvironmentAttributeLayer<Value> layer) {
/* 176 */     ((List<EnvironmentAttributeLayer<Value>>)this.layersByAttribute.computeIfAbsent(attribute, t -> new ArrayList())).add(layer);
/* 177 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addTimelineLayer(Holder<Timeline> timeline, ClockManager clockManager) {
/* 181 */     for (EnvironmentAttribute<?> attribute : (Iterable<EnvironmentAttribute<?>>)((Timeline)timeline.value()).attributes())
/* 182 */       addTimelineLayerForAttribute(timeline, attribute, clockManager); 
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   private <Value> void addTimelineLayerForAttribute(Holder<Timeline> timeline, EnvironmentAttribute<Value> attribute, ClockManager clockManager) {
/* 188 */     addTimeBasedLayer(attribute, (EnvironmentAttributeLayer.TimeBased<Value>)((Timeline)timeline.value()).createTrackSampler(attribute, clockManager));
/*     */   }
/*     */   
/*     */   public EnvironmentAttributeSystem build() {
/* 192 */     return new EnvironmentAttributeSystem(this.layersByAttribute);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\EnvironmentAttributeSystem$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */