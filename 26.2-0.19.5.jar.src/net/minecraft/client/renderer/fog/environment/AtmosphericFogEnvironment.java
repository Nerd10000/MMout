/*     */ package net.minecraft.client.renderer.fog.environment;
/*     */ 
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.DeltaTracker;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.fog.FogData;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.material.FogType;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class AtmosphericFogEnvironment extends FogEnvironment {
/*     */   private static final int MIN_RAIN_FOG_SKY_LIGHT = 8;
/*     */   
/*     */   private static final float RAIN_FOG_START_OFFSET = -160.0F;
/*     */   
/*     */   private static final float RAIN_FOG_END_OFFSET = -256.0F;
/*     */   
/*     */   private float rainFogMultiplier;
/*     */   
/*     */   public int getBaseColor(ClientLevel level, Camera camera, int renderDistance, float partialTicks) {
/*  31 */     int fogColor = ((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.FOG_COLOR, partialTicks)).intValue();
/*  33 */     if (renderDistance >= 4) {
/*  34 */       float sunAngle = ((Float)camera.attributeProbe().getValue(EnvironmentAttributes.SUN_ANGLE, partialTicks)).floatValue() * 0.017453292F;
/*  35 */       float sunX = (Mth.sin(sunAngle) > 0.0F) ? -1.0F : 1.0F;
/*  36 */       Vector3fc forwardVector = camera.isPanoramicMode() ? camera.panoramicForwards() : camera.forwardVector();
/*  37 */       float lookingAtTheSunFactor = forwardVector.dot(sunX, 0.0F, 0.0F);
/*  38 */       if (lookingAtTheSunFactor > 0.0F) {
/*  39 */         int color = ((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SUNRISE_SUNSET_COLOR, partialTicks)).intValue();
/*  40 */         float alpha = ARGB.alphaFloat(color);
/*  41 */         if (alpha > 0.0F)
/*  42 */           fogColor = ARGB.srgbLerp(lookingAtTheSunFactor * alpha, fogColor, ARGB.opaque(color)); 
/*     */       } 
/*     */     } 
/*  47 */     int skyColor = ((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SKY_COLOR, partialTicks)).intValue();
/*  48 */     skyColor = applyWeatherDarken(skyColor, level.getRainLevel(partialTicks), level.getThunderLevel(partialTicks));
/*  50 */     float skyFogEnd = Math.min(((Float)camera
/*  51 */         .attributeProbe().getValue(EnvironmentAttributes.SKY_FOG_END_DISTANCE, partialTicks)).floatValue() / 16.0F, renderDistance);
/*  54 */     float skyColorMixFactor = Mth.clampedLerp(skyFogEnd / 32.0F, 0.25F, 1.0F);
/*  55 */     skyColorMixFactor = 1.0F - (float)Math.pow(skyColorMixFactor, 0.25D);
/*  57 */     fogColor = ARGB.srgbLerp(skyColorMixFactor, fogColor, skyColor);
/*  59 */     return fogColor;
/*     */   }
/*     */   
/*     */   private static int applyWeatherDarken(int color, float rainLevel, float thunderLevel) {
/*  63 */     if (rainLevel > 0.0F) {
/*  64 */       float rainColorModifier = 1.0F - rainLevel * 0.5F;
/*  65 */       float rainBlueColorModifier = 1.0F - rainLevel * 0.4F;
/*  66 */       color = ARGB.scaleRGB(color, rainColorModifier, rainColorModifier, rainBlueColorModifier);
/*     */     } 
/*  68 */     if (thunderLevel > 0.0F)
/*  69 */       color = ARGB.scaleRGB(color, 1.0F - thunderLevel * 0.5F); 
/*  71 */     return color;
/*     */   }
/*     */   
/*     */   public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
/*  76 */     updateRainFogState(camera, level, deltaTracker);
/*  78 */     float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
/*  79 */     fog.environmentalStart = ((Float)camera.attributeProbe().getValue(EnvironmentAttributes.FOG_START_DISTANCE, partialTicks)).floatValue();
/*  80 */     fog.environmentalEnd = ((Float)camera.attributeProbe().getValue(EnvironmentAttributes.FOG_END_DISTANCE, partialTicks)).floatValue();
/*  82 */     fog.environmentalStart += -160.0F * this.rainFogMultiplier;
/*  85 */     float minRainFogEnd = Math.min(96.0F, fog.environmentalEnd);
/*  86 */     fog.environmentalEnd = Math.max(minRainFogEnd, fog.environmentalEnd + -256.0F * this.rainFogMultiplier);
/*  88 */     fog.skyEnd = Math.min(renderDistance, ((Float)camera
/*     */         
/*  90 */         .attributeProbe().getValue(EnvironmentAttributes.SKY_FOG_END_DISTANCE, partialTicks)).floatValue());
/*  92 */     fog.cloudEnd = Math.min(((
/*  93 */         (Integer)(Minecraft.getInstance()).options.cloudRange().get()).intValue() * 16), ((Float)camera
/*  94 */         .attributeProbe().getValue(EnvironmentAttributes.CLOUD_FOG_END_DISTANCE, partialTicks)).floatValue());
/*  98 */     if ((Minecraft.getInstance()).gui.hud.getBossOverlay().shouldCreateWorldFog()) {
/*  99 */       fog.environmentalStart = Math.min(fog.environmentalStart, 10.0F);
/* 100 */       fog.environmentalEnd = Math.min(fog.environmentalEnd, 96.0F);
/* 101 */       fog.skyEnd = fog.environmentalEnd;
/* 102 */       fog.cloudEnd = fog.environmentalEnd;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateRainFogState(Camera camera, ClientLevel level, DeltaTracker deltaTracker) {
/* 107 */     BlockPos blockPos = camera.blockPosition();
/* 108 */     Biome biome = (Biome)level.getBiome(blockPos).value();
/* 109 */     float deltaTicks = deltaTracker.getGameTimeDeltaTicks();
/* 110 */     float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
/* 111 */     boolean rainsInBiome = biome.hasPrecipitation();
/* 112 */     float skyLightLevelMultiplier = Mth.clamp((level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(blockPos) - 8.0F) / 7.0F, 0.0F, 1.0F);
/* 113 */     float targetRainFogMultiplier = level.getRainLevel(partialTicks) * skyLightLevelMultiplier * (rainsInBiome ? 1.0F : 0.5F);
/* 114 */     this.rainFogMultiplier += (targetRainFogMultiplier - this.rainFogMultiplier) * deltaTicks * 0.2F;
/*     */   }
/*     */   
/*     */   public boolean isApplicable(FogType fogType, Entity entity) {
/* 119 */     return (fogType == FogType.ATMOSPHERIC);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\fog\environment\AtmosphericFogEnvironment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */