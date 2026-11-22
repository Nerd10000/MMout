/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.IntSupplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.texture.MipmapStrategy;
/*     */ import net.minecraft.client.renderer.texture.ReloadableTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureContents;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ReloadInstance;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class LoadingOverlay extends Overlay {
/*  28 */   public static final Identifier MOJANG_STUDIOS_LOGO_LOCATION = Identifier.withDefaultNamespace("textures/gui/title/mojangstudios.png");
/*     */   
/*  29 */   private static final int LOGO_BACKGROUND_COLOR = ARGB.color(255, 239, 50, 61);
/*     */   
/*  30 */   private static final int LOGO_BACKGROUND_COLOR_DARK = ARGB.color(255, 0, 0, 0);
/*     */   
/*     */   private static final IntSupplier BRAND_BACKGROUND = () -> ((Boolean)(Minecraft.getInstance()).options.darkMojangStudiosBackground().get()).booleanValue() ? LOGO_BACKGROUND_COLOR_DARK : LOGO_BACKGROUND_COLOR;
/*     */   
/*     */   private static final int LOGO_SCALE = 240;
/*     */   
/*     */   private static final float LOGO_QUARTER_FLOAT = 60.0F;
/*     */   
/*     */   private static final int LOGO_QUARTER = 60;
/*     */   
/*     */   private static final int LOGO_HALF = 120;
/*     */   
/*     */   private static final float LOGO_OVERLAP = 0.0625F;
/*     */   
/*     */   private static final float SMOOTHING = 0.95F;
/*     */   
/*     */   public static final long FADE_OUT_TIME = 1000L;
/*     */   
/*     */   public static final long FADE_IN_TIME = 500L;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final ReloadInstance reload;
/*     */   
/*     */   private final Consumer<Optional<Throwable>> onFinish;
/*     */   
/*     */   private final boolean fadeIn;
/*     */   
/*     */   private float currentProgress;
/*     */   
/*  49 */   private long fadeOutStart = -1L;
/*     */   
/*  50 */   private long fadeInStart = -1L;
/*     */   
/*     */   public LoadingOverlay(Minecraft minecraft, ReloadInstance reload, Consumer<Optional<Throwable>> onFinish, boolean fadeIn) {
/*  53 */     this.minecraft = minecraft;
/*  54 */     this.reload = reload;
/*  55 */     this.onFinish = onFinish;
/*  56 */     this.fadeIn = fadeIn;
/*     */   }
/*     */   
/*     */   public static void registerTextures(TextureManager textureManager) {
/*  60 */     textureManager.registerAndLoad(MOJANG_STUDIOS_LOGO_LOCATION, new LogoTexture());
/*     */   }
/*     */   
/*     */   private static int replaceAlpha(int color, int alpha) {
/*  64 */     return color & 0xFFFFFF | alpha << 24;
/*     */   }
/*     */   
/*     */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*     */     float logoAlpha;
/*  69 */     int width = graphics.guiWidth();
/*  70 */     int height = graphics.guiHeight();
/*  71 */     long now = Util.getMillis();
/*  73 */     if (this.fadeIn && this.fadeInStart == -1L)
/*  74 */       this.fadeInStart = now; 
/*  77 */     float fadeOutAnim = (this.fadeOutStart > -1L) ? ((float)(now - this.fadeOutStart) / 1000.0F) : -1.0F;
/*  78 */     float fadeInAnim = (this.fadeInStart > -1L) ? ((float)(now - this.fadeInStart) / 500.0F) : -1.0F;
/*  81 */     if (fadeOutAnim >= 1.0F) {
/*  82 */       if (this.minecraft.gui.screen() != null) {
/*  83 */         this.minecraft.gui.screen().extractRenderStateWithTooltipAndSubtitles(graphics, 0, 0, a);
/*     */       } else {
/*  85 */         this.minecraft.gui.hud.extractDeferredSubtitles();
/*     */       } 
/*  87 */       int alpha = Mth.ceil((1.0F - Mth.clamp(fadeOutAnim - 1.0F, 0.0F, 1.0F)) * 255.0F);
/*  88 */       graphics.nextStratum();
/*  89 */       graphics.fill(0, 0, width, height, replaceAlpha(BRAND_BACKGROUND.getAsInt(), alpha));
/*  90 */       logoAlpha = 1.0F - Mth.clamp(fadeOutAnim - 1.0F, 0.0F, 1.0F);
/*  91 */     } else if (this.fadeIn) {
/*  92 */       if (this.minecraft.gui.screen() != null && fadeInAnim < 1.0F) {
/*  93 */         this.minecraft.gui.screen().extractRenderStateWithTooltipAndSubtitles(graphics, mouseX, mouseY, a);
/*     */       } else {
/*  95 */         this.minecraft.gui.hud.extractDeferredSubtitles();
/*     */       } 
/*  97 */       int alpha = Mth.ceil(Mth.clamp(fadeInAnim, 0.15D, 1.0D) * 255.0D);
/*  98 */       graphics.nextStratum();
/*  99 */       graphics.fill(0, 0, width, height, replaceAlpha(BRAND_BACKGROUND.getAsInt(), alpha));
/* 100 */       logoAlpha = Mth.clamp(fadeInAnim, 0.0F, 1.0F);
/*     */     } else {
/* 103 */       ARGB.setVector4fFromARGB32((this.minecraft.gameRenderer.gameRenderState()).guiRenderState.clearColorOverride, BRAND_BACKGROUND.getAsInt());
/* 104 */       logoAlpha = 1.0F;
/*     */     } 
/* 107 */     int contentX = (int)(graphics.guiWidth() * 0.5D);
/* 108 */     int logoY = (int)(graphics.guiHeight() * 0.5D);
/* 110 */     double logoHeight = Math.min(graphics.guiWidth() * 0.75D, graphics.guiHeight()) * 0.25D;
/* 111 */     int logoHeightHalf = (int)(logoHeight * 0.5D);
/* 112 */     double contentWidth = logoHeight * 4.0D;
/* 113 */     int logoWidthHalf = (int)(contentWidth * 0.5D);
/* 115 */     int color = ARGB.white(logoAlpha);
/* 116 */     graphics.blit(RenderPipelines.MOJANG_LOGO, MOJANG_STUDIOS_LOGO_LOCATION, contentX - logoWidthHalf, logoY - logoHeightHalf, -0.0625F, 0.0F, logoWidthHalf, (int)logoHeight, 120, 60, 120, 120, color);
/* 117 */     graphics.blit(RenderPipelines.MOJANG_LOGO, MOJANG_STUDIOS_LOGO_LOCATION, contentX, logoY - logoHeightHalf, 0.0625F, 60.0F, logoWidthHalf, (int)logoHeight, 120, 60, 120, 120, color);
/* 119 */     int barY = (int)(graphics.guiHeight() * 0.8325D);
/* 121 */     float actualProgress = this.reload.getActualProgress();
/* 122 */     this.currentProgress = Mth.clamp(this.currentProgress * 0.95F + actualProgress * 0.050000012F, 0.0F, 1.0F);
/* 124 */     if (fadeOutAnim < 1.0F)
/* 125 */       extractProgressBar(graphics, width / 2 - logoWidthHalf, barY - 5, width / 2 + logoWidthHalf, barY + 5, 1.0F - Mth.clamp(fadeOutAnim, 0.0F, 1.0F)); 
/* 128 */     if (fadeOutAnim >= 2.0F)
/* 129 */       this.minecraft.gui.setOverlay(null); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 136 */     if (this.fadeOutStart == -1L && this.reload.isDone() && isReadyToFadeOut()) {
/*     */       try {
/* 138 */         this.reload.checkExceptions();
/* 139 */         this.onFinish.accept(Optional.empty());
/* 140 */       } catch (Throwable t) {
/* 141 */         this.onFinish.accept(Optional.of(t));
/*     */       } 
/* 143 */       this.fadeOutStart = Util.getMillis();
/* 144 */       if (this.minecraft.gui.screen() != null) {
/* 146 */         Window window = this.minecraft.getWindow();
/* 147 */         this.minecraft.gui.screen().init(window.getGuiScaledWidth(), window.getGuiScaledHeight());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isReadyToFadeOut() {
/* 153 */     return (!this.fadeIn || (this.fadeInStart > -1L && Util.getMillis() - this.fadeInStart >= 1000L));
/*     */   }
/*     */   
/*     */   private void extractProgressBar(GuiGraphicsExtractor graphics, int x0, int y0, int x1, int y1, float fade) {
/* 157 */     int width = Mth.ceil((x1 - x0 - 2) * this.currentProgress);
/* 158 */     int alpha = Math.round(fade * 255.0F);
/* 159 */     int white = ARGB.color(alpha, 255, 255, 255);
/* 162 */     graphics.fill(x0 + 2, y0 + 2, x0 + width, y1 - 2, white);
/* 164 */     graphics.fill(x0 + 1, y0, x1 - 1, y0 + 1, white);
/* 165 */     graphics.fill(x0 + 1, y1, x1 - 1, y1 - 1, white);
/* 166 */     graphics.fill(x0, y0, x0 + 1, y1, white);
/* 167 */     graphics.fill(x1, y0, x1 - 1, y1, white);
/*     */   }
/*     */   
/*     */   private static class LogoTexture extends ReloadableTexture {
/*     */     public LogoTexture() {
/* 172 */       super(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION);
/*     */     }
/*     */     
/*     */     public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
/* 177 */       ResourceProvider vanillaProvider = Minecraft.getInstance().getVanillaPackResources().asProvider();
/* 178 */       InputStream resource = vanillaProvider.open(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION);
/*     */       try {
/* 179 */         TextureContents textureContents = new TextureContents(NativeImage.read(resource), new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
/* 180 */         if (resource != null)
/* 180 */           resource.close(); 
/*     */         return textureContents;
/*     */       } catch (Throwable throwable) {
/*     */         if (resource != null)
/*     */           try {
/*     */             resource.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\LoadingOverlay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */