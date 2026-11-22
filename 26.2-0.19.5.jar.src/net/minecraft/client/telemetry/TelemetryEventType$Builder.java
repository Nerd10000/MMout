/*     */ package net.minecraft.client.telemetry;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Builder {
/*     */   private final String id;
/*     */   
/*     */   private final String exportKey;
/*     */   
/* 207 */   private final List<TelemetryProperty<?>> properties = new ArrayList<>();
/*     */   
/*     */   private boolean isOptIn;
/*     */   
/*     */   private Builder(String id, String exportKey) {
/* 211 */     this.id = id;
/* 212 */     this.exportKey = exportKey;
/*     */   }
/*     */   
/*     */   public Builder defineAll(List<TelemetryProperty<?>> properties) {
/* 216 */     this.properties.addAll(properties);
/* 217 */     return this;
/*     */   }
/*     */   
/*     */   public <T> Builder define(TelemetryProperty<T> property) {
/* 221 */     this.properties.add(property);
/* 222 */     return this;
/*     */   }
/*     */   
/*     */   public Builder optIn() {
/* 226 */     this.isOptIn = true;
/* 227 */     return this;
/*     */   }
/*     */   
/*     */   public TelemetryEventType register() {
/* 231 */     TelemetryEventType type = new TelemetryEventType(this.id, this.exportKey, List.copyOf(this.properties), this.isOptIn);
/* 232 */     if (TelemetryEventType.REGISTRY.putIfAbsent(this.id, type) != null)
/* 233 */       throw new IllegalStateException("Duplicate TelemetryEventType with key: '" + this.id + "'"); 
/* 235 */     return type;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\TelemetryEventType$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */