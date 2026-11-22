/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.state.LightmapRenderState;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class LightmapRenderStateExtractor {
/*  20 */   public static final Vector3fc WHITE = (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F);
/*     */   
/*     */   private boolean needsUpdate;
/*     */   
/*     */   private final GameRenderer renderer;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*  25 */   private final RandomSource randomSource = RandomSource.create();
/*     */   
/*     */   private float blockLightFlicker;
/*     */   
/*     */   public LightmapRenderStateExtractor(GameRenderer renderer, Minecraft minecraft) {
/*  29 */     this.renderer = renderer;
/*  30 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void tick() {
/*  34 */     this.blockLightFlicker += (this.randomSource.nextFloat() - this.randomSource.nextFloat()) * this.randomSource.nextFloat() * this.randomSource.nextFloat() * 0.1F;
/*  35 */     this.blockLightFlicker *= 0.9F;
/*  36 */     this.needsUpdate = true;
/*     */   }
/*     */   
/*     */   private float calculateDarknessScale(LivingEntity camera, float darknessGamma, float partialTickTime) {
/*  40 */     float darkness = 0.45F * darknessGamma;
/*  41 */     return Math.max(0.0F, Mth.cos(((camera.tickCount - partialTickTime) * 3.1415927F * 0.025F)) * darkness);
/*     */   }
/*     */   
/*     */   public void extract(LightmapRenderState renderState, float partialTicks) {
/*  45 */     renderState.needsUpdate = this.needsUpdate;
/*  46 */     if (!this.needsUpdate)
/*     */       return; 
/*  50 */     ClientLevel level = this.minecraft.level;
/*  51 */     LocalPlayer player = this.minecraft.player;
/*  52 */     if (level == null || player == null)
/*     */       return; 
/*  56 */     ProfilerFiller profiler = Profiler.get();
/*  57 */     profiler.push("lightmap");
/*  58 */     Camera camera = this.renderer.mainCamera();
/*  60 */     renderState.blockFactor = this.blockLightFlicker + 1.4F;
/*  61 */     renderState.blockLightTint = (Vector3fc)ARGB.vector3fFromRGB24(((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.BLOCK_LIGHT_TINT, partialTicks)).intValue());
/*  63 */     renderState.skyFactor = ((Float)camera.attributeProbe().getValue(EnvironmentAttributes.SKY_LIGHT_FACTOR, partialTicks)).floatValue();
/*  64 */     renderState.skyLightColor = (Vector3fc)ARGB.vector3fFromRGB24(((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.SKY_LIGHT_COLOR, partialTicks)).intValue());
/*  66 */     EndFlashState endFlashState = level.endFlashState();
/*  67 */     if (endFlashState != null && 
/*  68 */       !((Boolean)this.minecraft.options.hideLightningFlash().get()).booleanValue()) {
/*  69 */       float intensity = endFlashState.getIntensity(partialTicks);
/*  70 */       if (this.minecraft.gui.hud.getBossOverlay().shouldCreateWorldFog()) {
/*  71 */         renderState.skyFactor += intensity / 3.0F;
/*     */       } else {
/*  73 */         renderState.skyFactor += intensity;
/*     */       } 
/*     */     } 
/*  78 */     renderState.ambientColor = (Vector3fc)ARGB.vector3fFromRGB24(((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.AMBIENT_LIGHT_COLOR, partialTicks)).intValue());
/*  80 */     float brightnessOption = ((Double)this.minecraft.options.gamma().get()).floatValue();
/*  81 */     float darknessEffectScaleOption = ((Double)this.minecraft.options.darknessEffectScale().get()).floatValue();
/*  83 */     float darknessEffectBrightnessModifier = player.getEffectBlendFactor(MobEffects.DARKNESS, partialTicks) * darknessEffectScaleOption;
/*  85 */     renderState.brightness = Math.max(0.0F, brightnessOption - darknessEffectBrightnessModifier);
/*  86 */     renderState.darknessEffectScale = calculateDarknessScale((LivingEntity)player, darknessEffectBrightnessModifier, partialTicks) * darknessEffectScaleOption;
/*  88 */     float waterVision = player.getWaterVision();
/*  89 */     if (player.hasEffect(MobEffects.NIGHT_VISION)) {
/*  90 */       renderState.nightVisionEffectIntensity = GameRenderer.nightVisionScale((LivingEntity)player, partialTicks);
/*  91 */     } else if (waterVision > 0.0F && player.hasEffect(MobEffects.CONDUIT_POWER)) {
/*  92 */       renderState.nightVisionEffectIntensity = waterVision;
/*     */     } else {
/*  94 */       renderState.nightVisionEffectIntensity = 0.0F;
/*     */     } 
/*  96 */     renderState.nightVisionColor = (Vector3fc)ARGB.vector3fFromRGB24(((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.NIGHT_VISION_COLOR, partialTicks)).intValue());
/*  97 */     renderState.bossOverlayWorldDarkening = this.renderer.bossOverlayWorldDarkening(partialTicks);
/*  99 */     profiler.pop();
/* 100 */     this.needsUpdate = false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\LightmapRenderStateExtractor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */