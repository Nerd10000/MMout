/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*     */ import net.minecraft.server.level.ParticleStatus;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public enum GraphicsPreset implements StringRepresentable {
/*  13 */   FAST("fast", "options.graphics.fast"),
/*  14 */   FANCY("fancy", "options.graphics.fancy"),
/*  15 */   FABULOUS("fabulous", "options.graphics.fabulous"),
/*  16 */   CUSTOM("custom", "options.graphics.custom");
/*     */   
/*     */   private final String serializedName;
/*     */   
/*     */   private final String key;
/*     */   
/*     */   public static final Codec<GraphicsPreset> CODEC;
/*     */   
/*     */   static {
/*  22 */     CODEC = (Codec<GraphicsPreset>)StringRepresentable.fromEnum(GraphicsPreset::values);
/*     */   }
/*     */   
/*     */   GraphicsPreset(String serializedName, String key) {
/*  25 */     this.serializedName = serializedName;
/*  26 */     this.key = key;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/*  31 */     return this.serializedName;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  35 */     return this.key;
/*     */   }
/*     */   
/*     */   public void apply(Minecraft minecraft) {
/*     */     int viewDistance;
/*  39 */     OptionsSubScreen screen = (minecraft.gui != null && minecraft.gui.screen() instanceof OptionsSubScreen) ? (OptionsSubScreen)minecraft.gui.screen() : null;
/*  40 */     GpuDevice device = RenderSystem.getDevice();
/*  41 */     switch (ordinal()) {
/*     */       case 0:
/*  43 */         viewDistance = 8;
/*  44 */         set(screen, minecraft.options.biomeBlendRadius(), Integer.valueOf(1));
/*  45 */         set(screen, minecraft.options.renderDistance(), Integer.valueOf(8));
/*  46 */         set(screen, minecraft.options.prioritizeChunkUpdates(), PrioritizeChunkUpdates.NONE);
/*  47 */         set(screen, minecraft.options.simulationDistance(), Integer.valueOf(6));
/*  48 */         set(screen, minecraft.options.ambientOcclusion(), Boolean.valueOf(false));
/*  49 */         set(screen, minecraft.options.cloudStatus(), CloudStatus.FAST);
/*  50 */         set(screen, minecraft.options.particles(), ParticleStatus.DECREASED);
/*  51 */         set(screen, minecraft.options.mipmapLevels(), Integer.valueOf(2));
/*  52 */         set(screen, minecraft.options.entityShadows(), Boolean.valueOf(false));
/*  53 */         set(screen, minecraft.options.entityDistanceScaling(), Double.valueOf(0.75D));
/*  54 */         set(screen, minecraft.options.menuBackgroundBlurriness(), Integer.valueOf(2));
/*  55 */         set(screen, minecraft.options.cloudRange(), Integer.valueOf(32));
/*  56 */         set(screen, minecraft.options.cutoutLeaves(), Boolean.valueOf(false));
/*  57 */         set(screen, minecraft.options.improvedTransparency(), Boolean.valueOf(false));
/*  58 */         set(screen, minecraft.options.weatherRadius(), Integer.valueOf(5));
/*  59 */         set(screen, minecraft.options.maxAnisotropyBit(), Integer.valueOf(1));
/*  60 */         set(screen, minecraft.options.textureFiltering(), TextureFilteringMethod.NONE);
/*     */         break;
/*     */       case 1:
/*  63 */         viewDistance = 16;
/*  64 */         set(screen, minecraft.options.biomeBlendRadius(), Integer.valueOf(2));
/*  65 */         set(screen, minecraft.options.renderDistance(), Integer.valueOf(16));
/*  66 */         set(screen, minecraft.options.prioritizeChunkUpdates(), PrioritizeChunkUpdates.PLAYER_AFFECTED);
/*  67 */         set(screen, minecraft.options.simulationDistance(), Integer.valueOf(12));
/*  68 */         set(screen, minecraft.options.ambientOcclusion(), Boolean.valueOf(true));
/*  69 */         set(screen, minecraft.options.cloudStatus(), CloudStatus.FANCY);
/*  70 */         set(screen, minecraft.options.particles(), ParticleStatus.ALL);
/*  71 */         set(screen, minecraft.options.mipmapLevels(), Integer.valueOf(4));
/*  72 */         set(screen, minecraft.options.entityShadows(), Boolean.valueOf(true));
/*  73 */         set(screen, minecraft.options.entityDistanceScaling(), Double.valueOf(1.0D));
/*  74 */         set(screen, minecraft.options.menuBackgroundBlurriness(), Integer.valueOf(5));
/*  75 */         set(screen, minecraft.options.cloudRange(), Integer.valueOf(64));
/*  76 */         set(screen, minecraft.options.cutoutLeaves(), Boolean.valueOf(true));
/*  77 */         set(screen, minecraft.options.improvedTransparency(), Boolean.valueOf(false));
/*  78 */         set(screen, minecraft.options.weatherRadius(), Integer.valueOf(10));
/*  79 */         set(screen, minecraft.options.maxAnisotropyBit(), Integer.valueOf(1));
/*  80 */         set(screen, minecraft.options.textureFiltering(), TextureFilteringMethod.RGSS);
/*     */         break;
/*     */       case 2:
/*  83 */         viewDistance = 32;
/*  84 */         set(screen, minecraft.options.biomeBlendRadius(), Integer.valueOf(2));
/*  85 */         set(screen, minecraft.options.renderDistance(), Integer.valueOf(32));
/*  86 */         set(screen, minecraft.options.prioritizeChunkUpdates(), PrioritizeChunkUpdates.PLAYER_AFFECTED);
/*  87 */         set(screen, minecraft.options.simulationDistance(), Integer.valueOf(12));
/*  88 */         set(screen, minecraft.options.ambientOcclusion(), Boolean.valueOf(true));
/*  89 */         set(screen, minecraft.options.cloudStatus(), CloudStatus.FANCY);
/*  90 */         set(screen, minecraft.options.particles(), ParticleStatus.ALL);
/*  91 */         set(screen, minecraft.options.mipmapLevels(), Integer.valueOf(4));
/*  92 */         set(screen, minecraft.options.entityShadows(), Boolean.valueOf(true));
/*  93 */         set(screen, minecraft.options.entityDistanceScaling(), Double.valueOf(1.25D));
/*  94 */         set(screen, minecraft.options.menuBackgroundBlurriness(), Integer.valueOf(5));
/*  95 */         set(screen, minecraft.options.cloudRange(), Integer.valueOf(128));
/*  96 */         set(screen, minecraft.options.cutoutLeaves(), Boolean.valueOf(true));
/*  97 */         set(screen, minecraft.options.improvedTransparency(), Boolean.valueOf((Util.getPlatform() != Util.OS.OSX)));
/*  98 */         set(screen, minecraft.options.weatherRadius(), Integer.valueOf(10));
/*  99 */         set(screen, minecraft.options.maxAnisotropyBit(), Integer.valueOf(2));
/* 101 */         if (device.getDeviceInfo().hintsAndWorkarounds().anisotropyHasKnownIssues()) {
/* 102 */           set(screen, minecraft.options.textureFiltering(), TextureFilteringMethod.RGSS);
/*     */           break;
/*     */         } 
/* 104 */         set(screen, minecraft.options.textureFiltering(), TextureFilteringMethod.ANISOTROPIC);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> void set(OptionsSubScreen screen, OptionInstance<T> option, T value) {
/* 111 */     if (option.get() != value) {
/* 112 */       option.set(value);
/* 113 */       if (screen != null)
/* 114 */         screen.resetOption(option); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\GraphicsPreset.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */