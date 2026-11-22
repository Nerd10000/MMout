/*     */ package net.minecraft.world.level.biome;
/*     */ 
/*     */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeMap;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ 
/*     */ public class BiomeBuilder {
/*     */   private boolean hasPrecipitation = true;
/*     */   
/*     */   private Float temperature;
/*     */   
/* 325 */   private Biome.TemperatureModifier temperatureModifier = Biome.TemperatureModifier.NONE;
/*     */   
/*     */   private Float downfall;
/*     */   
/* 327 */   private final EnvironmentAttributeMap.Builder attributes = EnvironmentAttributeMap.builder();
/*     */   
/*     */   private BiomeSpecialEffects specialEffects;
/*     */   
/*     */   private MobSpawnSettings mobSpawnSettings;
/*     */   
/*     */   private BiomeGenerationSettings generationSettings;
/*     */   
/*     */   public BiomeBuilder hasPrecipitation(boolean hasPrecipitation) {
/* 333 */     this.hasPrecipitation = hasPrecipitation;
/* 334 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder temperature(float temperature) {
/* 338 */     this.temperature = Float.valueOf(temperature);
/* 339 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder downfall(float downfall) {
/* 343 */     this.downfall = Float.valueOf(downfall);
/* 344 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder putAttributes(EnvironmentAttributeMap attributes) {
/* 348 */     this.attributes.putAll(attributes);
/* 349 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder putAttributes(EnvironmentAttributeMap.Builder attributes) {
/* 353 */     return putAttributes(attributes.build());
/*     */   }
/*     */   
/*     */   public <Value> BiomeBuilder setAttribute(EnvironmentAttribute<Value> attribute, Value value) {
/* 357 */     this.attributes.set(attribute, value);
/* 358 */     return this;
/*     */   }
/*     */   
/*     */   public <Value, Parameter> BiomeBuilder modifyAttribute(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Parameter> modifier, Parameter value) {
/* 362 */     this.attributes.modify(attribute, modifier, value);
/* 363 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder specialEffects(BiomeSpecialEffects specialEffects) {
/* 367 */     this.specialEffects = specialEffects;
/* 368 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder mobSpawnSettings(MobSpawnSettings mobSpawnSettings) {
/* 372 */     this.mobSpawnSettings = mobSpawnSettings;
/* 373 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder generationSettings(BiomeGenerationSettings generationSettings) {
/* 377 */     this.generationSettings = generationSettings;
/* 378 */     return this;
/*     */   }
/*     */   
/*     */   public BiomeBuilder temperatureAdjustment(Biome.TemperatureModifier temperatureModifier) {
/* 382 */     this.temperatureModifier = temperatureModifier;
/* 383 */     return this;
/*     */   }
/*     */   
/*     */   public Biome build() {
/* 387 */     if (this.temperature == null || this.downfall == null || this.specialEffects == null || this.mobSpawnSettings == null || this.generationSettings == null)
/* 393 */       throw new IllegalStateException("You are missing parameters to build a proper biome\n" + String.valueOf(this)); 
/* 396 */     return new Biome(new Biome.ClimateSettings(this.hasPrecipitation, this.temperature
/* 397 */           .floatValue(), this.temperatureModifier, this.downfall.floatValue()), this.attributes
/* 398 */         .build(), this.specialEffects, this.generationSettings, this.mobSpawnSettings);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 407 */     return "BiomeBuilder{\nhasPrecipitation=" + this.hasPrecipitation + ",\ntemperature=" + this.temperature + ",\ntemperatureModifier=" + String.valueOf(this.temperatureModifier) + ",\ndownfall=" + this.downfall + ",\nspecialEffects=" + String.valueOf(this.specialEffects) + ",\nmobSpawnSettings=" + String.valueOf(this.mobSpawnSettings) + ",\ngenerationSettings=" + String.valueOf(this.generationSettings) + ",\n}";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Biome$BiomeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */