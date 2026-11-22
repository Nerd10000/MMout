/*     */ package net.minecraft.client.telemetry;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Builder {
/*  86 */   private final Map<TelemetryProperty<?>, Object> entries = (Map<TelemetryProperty<?>, Object>)new Reference2ObjectOpenHashMap();
/*     */   
/*     */   public <T> Builder put(TelemetryProperty<T> property, T value) {
/*  92 */     this.entries.put(property, value);
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public <T> Builder putIfNotNull(TelemetryProperty<T> property, T value) {
/*  97 */     if (value != null)
/*  98 */       this.entries.put(property, value); 
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Builder putAll(TelemetryPropertyMap properties) {
/* 104 */     this.entries.putAll(properties.entries);
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public TelemetryPropertyMap build() {
/* 109 */     return new TelemetryPropertyMap(this.entries);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\TelemetryPropertyMap$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */